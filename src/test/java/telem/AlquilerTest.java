package telem; // Mismo paquete que Alquiler

import static org.junit.Assert.assertEquals; // Compara esperado con real (con equals)
import static org.junit.Assert.assertSame;   // Comprueba que es el MISMO objeto (con ==)
import static org.junit.Assert.assertTrue;   // Comprueba que una condición es verdadera

import java.time.LocalDate; // Las fechas de Alquiler son LocalDate desde la sección 4

import org.junit.Test; // Anotación @Test

/**
 * Tests de la clase Alquiler.
 * Objetivo: comprobar el constructor, el cálculo de días y precio con LocalDate
 * (incluidos casos límite), el registro estático de alquileres y el JSON.
 */
public class AlquilerTest {

    // Datos de prueba comunes a todos los tests (así no se repiten en cada uno).
    // JUnit crea un objeto AlquilerTest nuevo para cada test, así que empiezan siempre igual.
    private final Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0);
    private final Cliente cliente = new Cliente("Ana López", "12345678A");
    // Fechas fijas con LocalDate.of(año, mes, día): con LocalDate.now() el test cambiaría cada día
    private final LocalDate inicio = LocalDate.of(2026, 11, 8);
    private final LocalDate fin = LocalDate.of(2026, 11, 11);

    @Test
    public void constructorGuardaVehiculoClienteYFechas() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        // assertSame: el alquiler guarda una referencia al mismo vehículo y cliente, no una copia
        assertSame(turismo, a.getVehiculo());
        assertSame(cliente, a.getCliente());
        // assertEquals con fechas: LocalDate compara por valor (año, mes y día)
        assertEquals(inicio, a.getFechaInicio());
        assertEquals(fin, a.getFechaFin());
    }

    // ---------- calcularPrecioAlquiler ----------
    // Objetivo: comprobar el "+1" (se cuentan el día de inicio y el de fin) y los casos límite.

    @Test
    public void alquilerDeUnSoloDiaCuestaUnDia() {
        // Mismo día de inicio y fin: between da 0, +1 = 1 día
        Alquiler a = new Alquiler(turismo, cliente, inicio, inicio);
        assertEquals(Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(inicio, inicio));
    }

    @Test
    public void seCuentanElDiaDeInicioYElDeFin() {
        // Del 8 al 11 de noviembre: días 8, 9, 10 y 11 -> 4 días
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        assertEquals(4 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(inicio, fin));
    }

    @Test
    public void alquilerQueCruzaDeMes() {
        // Del 30 de noviembre al 2 de diciembre: 30, 1 y 2 -> 3 días
        // (noviembre tiene 30 días; LocalDate lo sabe)
        LocalDate i = LocalDate.of(2026, 11, 30);
        LocalDate f = LocalDate.of(2026, 12, 2);
        Alquiler a = new Alquiler(turismo, cliente, i, f);
        assertEquals(3 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(i, f));
    }

    @Test
    public void alquilerQueCruzaElCambioDeHora() {
        // El 25 de octubre de 2026 se cambia la hora en España (ese día tiene 25 horas).
        // Con Date y milisegundos este cálculo podía salir mal; con LocalDate no.
        // Este test justifica el cambio de la sección 4.
        LocalDate i = LocalDate.of(2026, 10, 24);
        LocalDate f = LocalDate.of(2026, 10, 26);
        Alquiler a = new Alquiler(turismo, cliente, i, f);
        assertEquals(3 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(i, f));
    }

    // ---------- Registro estático de alquileres ----------

    @Test
    public void agregarAlquilerLoGuardaEnElArrayEstatico() {
        // El array es static: lo comparten todos los objetos y todos los tests.
        // Por eso contamos cuántos hay antes, en vez de suponer que está vacío.
        int antes = contarAlquileres();
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        Alquiler.agregarAlquiler(a);
        assertEquals(antes + 1, contarAlquileres());              // hay uno más
        assertSame(a, Alquiler.obtenerAlquileres()[antes]);        // y está en la primera posición libre
    }

    // ---------- Serialización JSON ----------

    @Test
    public void toStringIncluyeClienteVehiculoYFechasEnFormatoIso() {
        String json = new Alquiler(turismo, cliente, inicio, fin).toString();
        // LocalDate se serializa como "año-mes-día" (formato ISO): fácil de reconstruir
        // con LocalDate.parse(...) en el ejercicio 3
        assertTrue(json.contains("\"fechaInicio\":\"2026-11-08\""));
        assertTrue(json.contains("\"fechaFin\":\"2026-11-11\""));
        // El cliente y el vehículo se serializan dentro del alquiler (JSON anidado)
        assertTrue(json.contains("\"nombre\":\"Ana López\""));
        assertTrue(json.contains("\"tipo\":\"Turismo\""));
    }

    /**
     * Método auxiliar (no es un test porque no lleva @Test):
     * cuenta las posiciones ocupadas del array estático de alquileres.
     */
    private int contarAlquileres() {
        int n = 0;
        for (Alquiler a : Alquiler.obtenerAlquileres()) { // for-each sobre el array de 1000 posiciones
            if (a != null) { // las posiciones vacías valen null
                n++;
            }
        }
        return n;
    }
}
