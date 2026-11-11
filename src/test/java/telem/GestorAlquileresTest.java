package telem;

import static org.junit.Assert.*;

import java.io.*;
import java.time.LocalDate;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;

public class GestorAlquileresTest {

    // Fichero temporal que usan las pruebas (se crea en la raíz del proyecto).
    private static final String FICHERO = "test_datos.json";

    // Se ejecuta tras CADA prueba: borra el fichero para que no se contaminen entre sí.
    @After
    public void limpiar() {
        new File(FICHERO).delete();
    }

    // Cuenta posiciones no nulas (los arrays del gestor tienen huecos null).
    private int contar(Object[] array) {
        int n = 0;
        for (Object o : array) {
            if (o != null) {
                n++;
            }
        }
        return n;
    }

    // Gestor con un alquiler: motocicleta NORMAL (30 euros/día), cliente "003", 5 días.
    private GestorAlquileres gestorConAlquiler() {
        GestorAlquileres g = new GestorAlquileres();
        g.agregarAlquiler(new Alquiler(g.obtenerVehiculos()[1], g.obtenerClientes()[2],
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 5)));
        return g;
    }

    // Escribe un texto cualquiera en el fichero de prueba (para simular ficheros inválidos).
    private void escribirFichero(String contenido) throws IOException {
        FileWriter w = new FileWriter(FICHERO);
        try {
            w.write(contenido);
        } finally {
            w.close();
        }
    }

    // Guardar crea un fichero no vacío.
    @Test
    public void guardar_creaElFichero() throws IOException {
        new GestorAlquileres().guardarEnFichero(FICHERO);
        File f = new File(FICHERO);
        assertTrue(f.exists());
        assertTrue(f.length() > 0);
    }

    // Tras guardar y cargar en otro gestor, hay las mismas cantidades.
    @Test
    public void guardarYCargar_conservaCantidades() throws IOException {
        GestorAlquileres origen = gestorConAlquiler();
        origen.guardarEnFichero(FICHERO);

        GestorAlquileres destino = new GestorAlquileres();
        destino.cargarDeFichero(FICHERO);

        assertEquals(contar(origen.obtenerClientes()), contar(destino.obtenerClientes()));
        assertEquals(contar(origen.obtenerVehiculos()), contar(destino.obtenerVehiculos()));
        assertEquals(1, contar(destino.obtenerAlquileres()));
    }

    // La factoría reconstruye la subclase correcta de cada vehículo.
    @Test
    public void cargar_conservaLasSubclasesDeVehiculo() throws IOException {
        new GestorAlquileres().guardarEnFichero(FICHERO);
        GestorAlquileres g = new GestorAlquileres();
        g.cargarDeFichero(FICHERO);

        assertTrue(g.obtenerVehiculos()[0] instanceof Turismo);
        assertTrue(g.obtenerVehiculos()[1] instanceof Motocicleta);
        assertTrue(g.obtenerVehiculos()[2] instanceof VehiculoIndustrial);
    }

    // El alquiler cargado conserva vehículo, cliente, fechas y precio.
    @Test
    public void cargar_conservaLosDatosDelAlquiler() throws IOException {
        gestorConAlquiler().guardarEnFichero(FICHERO);
        GestorAlquileres g = new GestorAlquileres();
        g.cargarDeFichero(FICHERO);

        Alquiler a = g.obtenerAlquileres()[0];
        assertEquals("MNA2021", a.getVehiculo().getMatricula());
        assertEquals("003", a.getCliente().getDni());
        assertEquals(LocalDate.of(2026, 11, 1), a.getFechaInicio());
        assertEquals(LocalDate.of(2026, 11, 5), a.getFechaFin());
        assertEquals(5 * 30, a.calcularPrecioAlquiler()); // 5 días x 30 euros
    }

    // Cargar sustituye el estado anterior, no lo suma.
    @Test
    public void cargar_sustituyeElEstadoAnterior() throws IOException {
        new GestorAlquileres().guardarEnFichero(FICHERO); // fichero sin alquileres
        GestorAlquileres g = gestorConAlquiler();
        assertEquals(1, contar(g.obtenerAlquileres()));

        g.cargarDeFichero(FICHERO);
        assertEquals(0, contar(g.obtenerAlquileres()));
    }

    // Tras cargar, un nuevo alquiler se añade a continuación (contadorAlquileres bien actualizado).
    @Test
    public void cargar_actualizaElContadorDeAlquileres() throws IOException {
        gestorConAlquiler().guardarEnFichero(FICHERO);
        GestorAlquileres g = new GestorAlquileres();
        g.cargarDeFichero(FICHERO);

        g.agregarAlquiler(new Alquiler(g.obtenerVehiculos()[0], g.obtenerClientes()[0],
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3)));

        assertEquals(2, contar(g.obtenerAlquileres())); // el cargado + el nuevo, sin sobrescribir
    }

    // Fichero inexistente: expected acepta IOException y subclases (FileNotFoundException).
    @Test(expected = IOException.class)
    public void cargar_ficheroInexistente_lanzaIOException() throws IOException {
        new GestorAlquileres().cargarDeFichero("no_existe.json");
    }

    // Texto que no es JSON: excepción y estado intacto.
    @Test
    public void cargar_jsonInvalido_lanzaExcepcionYNoModificaElEstado() throws IOException {
        escribirFichero("esto no es json");
        GestorAlquileres g = gestorConAlquiler();

        // try/catch en vez de "expected" porque después hay que comprobar el estado.
        try {
            g.cargarDeFichero(FICHERO);
            fail("Debería haber lanzado JSONException");
        } catch (JSONException e) {
            // esperado
        }
        assertEquals(1, contar(g.obtenerAlquileres()));
        assertEquals(6, contar(g.obtenerClientes()));
    }

    // JSON válido pero sin las claves esperadas: excepción y estado intacto.
    @Test
    public void cargar_jsonSinClaves_lanzaExcepcionYNoModificaElEstado() throws IOException {
        escribirFichero("{\"clientes\":[]}");
        GestorAlquileres g = gestorConAlquiler();

        try {
            g.cargarDeFichero(FICHERO);
            fail("Debería haber lanzado JSONException");
        } catch (JSONException e) {
            // esperado: faltan "vehiculos" y "alquileres"
        }
        assertEquals(1, contar(g.obtenerAlquileres()));
    }

    // Tipo de vehículo desconocido: la factoría lanza excepción y el estado queda intacto.
    @Test
    public void cargar_tipoDeVehiculoDesconocido_lanzaExcepcionYNoModificaElEstado() throws IOException {
        escribirFichero("{\"clientes\":[],\"alquileres\":[],\"vehiculos\":[{\"tipo\":\"Barco\"}]}");
        GestorAlquileres g = gestorConAlquiler();

        try {
            g.cargarDeFichero(FICHERO);
            fail("Debería haber lanzado IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // esperado: la factoría no conoce el tipo "Barco"
        }
        assertEquals(1, contar(g.obtenerAlquileres()));
    }
}