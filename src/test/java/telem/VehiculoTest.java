package telem; // Mismo paquete que las clases que probamos

import static org.junit.Assert.assertEquals; // Compara esperado con real
import static org.junit.Assert.assertFalse;  // Comprueba que algo es falso
import static org.junit.Assert.assertTrue;   // Comprueba que algo es verdadero

import org.junit.Test; // Anotación @Test

/**
 * Tests de Vehiculo y sus subclases: Turismo, Motocicleta y VehiculoIndustrial.
 * Objetivo: comprobar la herencia (atributos comunes y propios),
 * los métodos abstractos getTipo() y getPrecioBase() en cada subclase,
 * el polimorfismo y la serialización a JSON.
 */
public class VehiculoTest {

    // ---------- Atributos comunes (heredados de Vehiculo) ----------

    @Test
    public void atributosComunesSeGuardanEnElConstructor() {
        // Vehiculo es abstracta (no se puede hacer new Vehiculo), así que usamos una subclase.
        // La referencia es de tipo Vehiculo para probar solo lo que define la superclase.
        Vehiculo v = new Turismo("1234ABC", Categoria.ESPECIAL, 2021, 5, 450.0);
        // El constructor de Turismo llama a super(...): estos valores los guarda Vehiculo
        assertEquals("1234ABC", v.getMatricula());
        assertEquals(Categoria.ESPECIAL, v.getCategoria()); // se compara con la constante del enum
        assertEquals(2021, v.getAnnio());
    }

    @Test
    public void unVehiculoNuevoNoEstaDisponibleHastaQueSeIndique() {
        Vehiculo v = new Motocicleta("5678DEF", Categoria.NORMAL, 2019, 125);
        // El constructor no inicializa "disponible": un boolean sin inicializar vale false
        assertFalse(v.isDisponible());
        // El setter cambia el estado...
        v.setDisponible(true);
        assertTrue(v.isDisponible());
        // ...en los dos sentidos
        v.setDisponible(false);
        assertFalse(v.isDisponible());
    }

    // ---------- Atributos propios de cada subclase ----------

    @Test
    public void turismoGuardaPlazasYMaletero() {
        // Referencia de tipo Turismo para poder llamar a sus métodos propios
        Turismo t = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0);
        assertEquals(5, t.getPlazas());
        // Con double se pasa un tercer argumento: la tolerancia (diferencia máxima admitida),
        // porque los decimales pueden tener errores mínimos de redondeo
        assertEquals(400.0, t.getCapacidadMaletero(), 0.001);
    }

    @Test
    public void motocicletaGuardaCilindrada() {
        Motocicleta m = new Motocicleta("5678DEF", Categoria.NORMAL, 2019, 125);
        assertEquals(125, m.getCilindrada()); // atributo que solo tienen las motos
    }

    @Test
    public void vehiculoIndustrialGuardaCargaYEjes() {
        VehiculoIndustrial vi = new VehiculoIndustrial("9999XYZ", Categoria.NORMAL, 2018, 1000, 3);
        assertEquals(1000, vi.getCapacidadCarga()); // atributos propios del industrial
        assertEquals(3, vi.getNumeroEjes());
    }

    // ---------- getTipo ----------

    @Test
    public void getTipoDevuelveElNombreDeLaSubclase() {
        // getTipo() es abstracto en Vehiculo: cada subclase da su propia versión.
        // Su valor acaba en el campo "tipo" del JSON y lo usará la factoría del ejercicio 3,
        // así que tiene que coincidir exactamente con el nombre de la clase.
        assertEquals("Turismo", new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0).getTipo());
        assertEquals("Motocicleta", new Motocicleta("B", Categoria.NORMAL, 2020, 125).getTipo());
        assertEquals("VehiculoIndustrial", new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 3).getTipo());
    }

    // ---------- getPrecioBase: cada tipo con cada categoría ----------
    // Objetivo: recorrer las 3 ramas del switch de cada subclase (NORMAL, ESPECIAL, SUPERIOR).
    // Si faltara un break o un precio estuviera mal, uno de estos asserts fallaría.

    @Test
    public void precioBaseTurismo() {
        assertEquals(50, new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(80, new Turismo("A", Categoria.ESPECIAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(100, new Turismo("A", Categoria.SUPERIOR, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void precioBaseMotocicleta() {
        assertEquals(30, new Motocicleta("B", Categoria.NORMAL, 2020, 125).getPrecioBase());
        assertEquals(50, new Motocicleta("B", Categoria.ESPECIAL, 2020, 125).getPrecioBase());
        assertEquals(70, new Motocicleta("B", Categoria.SUPERIOR, 2020, 125).getPrecioBase());
    }

    @Test
    public void precioBaseVehiculoIndustrial() {
        assertEquals(80, new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 3).getPrecioBase());
        assertEquals(120, new VehiculoIndustrial("C", Categoria.ESPECIAL, 2020, 1000, 3).getPrecioBase());
        assertEquals(150, new VehiculoIndustrial("C", Categoria.SUPERIOR, 2020, 1000, 3).getPrecioBase());
    }

    // ---------- Polimorfismo ----------

    @Test
    public void getPrecioBaseEsPolimorfico() {
        // Un array de tipo Vehiculo con un objeto de cada subclase.
        // Al llamar a getPrecioBase() sobre una referencia Vehiculo, Java ejecuta
        // la versión de la clase REAL del objeto (ligadura dinámica).
        // Esto es lo que aprovechará el patrón Estrategia del ejercicio 2.
        Vehiculo[] flota = {
            new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0),
            new Motocicleta("B", Categoria.NORMAL, 2020, 125),
            new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 3)
        };
        int[] esperados = {50, 30, 80}; // precio NORMAL de cada uno, en el mismo orden
        for (int i = 0; i < flota.length; i++) {
            assertEquals(esperados[i], flota[i].getPrecioBase());
        }
    }

    // ---------- Serialización JSON ----------

    @Test
    public void toStringIncluyeTipoYPrecioBase() {
        // toString() usa new JSONObject(this): crea un campo por cada getX()/isX() público
        String json = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0).toString();
        assertTrue(json.contains("\"tipo\":\"Turismo\""));       // gracias a getTipo()
        assertTrue(json.contains("\"precioBase\":50"));          // gracias a getPrecioBase(); número, sin comillas
        assertTrue(json.contains("\"matricula\":\"1234ABC\""));  // atributo heredado
        assertTrue(json.contains("\"categoria\":\"NORMAL\""));   // el enum se guarda como texto
    }
}
