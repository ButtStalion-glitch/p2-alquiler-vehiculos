package telem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Ejercicio 2: tests del patrón Estrategia.
 * Objetivo: comprobar cada política por separado (incluidos sus casos límite)
 * y que Alquiler delega el cálculo en la estrategia que tenga asignada.
 */
public class EstrategiaPrecioTest {

    // ---------- Cada política por separado (precio base 50) ----------

    
    public void politicaNormal() {
        CalculadorPrecio p = new PrecioNormal();
        assertEquals(500, p.calcularPrecio(50, 10)); // 50 x 10
        assertEquals(50, p.calcularPrecio(50, 1));
    }

    
    public void politicaTemporadaAltaDuplicaElPrecioBase() {
        CalculadorPrecio p = new PrecioTemporadaAlta();
        assertEquals(1000, p.calcularPrecio(50, 10)); // 100 x 10
    }

    
    public void ofertonHastaSieteDiasSoloLleva30PorCiento() {
        CalculadorPrecio p = new PrecioOferton();
        assertEquals(245, p.calcularPrecio(50, 7)); // 35 x 7 (justo 7: sin el 10 % extra)
    }

    
    public void ofertonMasDeSieteDiasLlevaUn10PorCientoExtra() {
        CalculadorPrecio p = new PrecioOferton();
        assertEquals(252, p.calcularPrecio(50, 8));  // 35 x 8 = 280 -> x 0,9 = 252
        assertEquals(315, p.calcularPrecio(50, 10)); // 35 x 10 = 350 -> x 0,9 = 315
    }

    
    public void clienteFrecuenteHastaTresDiasSoloLleva20PorCiento() {
        CalculadorPrecio p = new PrecioClienteFrecuente();
        assertEquals(40, p.calcularPrecio(50, 1));   // 40 x 1
        assertEquals(120, p.calcularPrecio(50, 3));  // 40 x 3
    }

    
    public void clienteFrecuenteDesdeElCuartoDiaLleva40PorCiento() {
        CalculadorPrecio p = new PrecioClienteFrecuente();
        assertEquals(150, p.calcularPrecio(50, 4));  // 40 x 3 + 30 x 1
        assertEquals(330, p.calcularPrecio(50, 10)); // 40 x 3 + 30 x 7
    }

    // ---------- Alquiler como contexto de la estrategia ----------

    private final Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
    private final LocalDate inicio = LocalDate.of(2026, 11, 1);
    private final LocalDate fin = LocalDate.of(2026, 11, 10); // 10 días

    
    public void porDefectoAlquilerUsaLaPoliticaNormal() {
        Alquiler a = new Alquiler(new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0), cliente, inicio, fin);
        assertEquals(500, a.calcularPrecioAlquiler());
    }

    
    public void alquilerUsaLaEstrategiaQueSeLeAsigna() {
        Alquiler a = new Alquiler(new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0), cliente, inicio, fin);
        a.setCalculador(new PrecioTemporadaAlta());
        assertEquals(1000, a.calcularPrecioAlquiler());
        a.setCalculador(new PrecioOferton());          // se puede cambiar en cualquier momento
        assertEquals(315, a.calcularPrecioAlquiler());
        a.setCalculador(new PrecioClienteFrecuente());
        assertEquals(330, a.calcularPrecioAlquiler());
    }

    
    public void laEstrategiaUsaElPrecioBaseDelVehiculo() {
        // Motocicleta SUPERIOR: precio base 70 -> normal 70 x 10 = 700
        Alquiler a = new Alquiler(new Motocicleta("B", Categoria.SUPERIOR, 2020, 600), cliente, inicio, fin);
        assertEquals(700, a.calcularPrecioAlquiler());
        a.setCalculador(new PrecioOferton());          // 49 x 10 = 490 -> x 0,9 = 441
        assertEquals(441, a.calcularPrecioAlquiler());
    }

    
    public void calcularDiasCuentaInicioYFin() {
        assertEquals(10, Alquiler.calcularDias(inicio, fin));
        assertEquals(1, Alquiler.calcularDias(inicio, inicio));
    }
}
