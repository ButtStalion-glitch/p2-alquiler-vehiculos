package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Pruebas de FactoriaPoliticasPrecios (ejercicio 4).
 * Se comprueba que la factoría crea la clase correcta, que acepta nombres
 * en texto, que rechaza entradas inválidas y que, usada desde Alquiler,
 * produce los precios esperados.
 */
public class FactoriaPoliticasPreciosTest {

    // 1. La factoría devuelve la clase concreta que corresponde a cada valor del enum.
    @Test
    public void devuelveLaClaseCorrectaParaCadaPolitica() {
        assertTrue(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.Normal)
                instanceof PrecioNormal);
        assertTrue(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.TemporadaAlta)
                instanceof PrecioTemporadaAlta);
        assertTrue(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.ClienteFrecuente)
                instanceof PrecioClienteFrecuente);
        assertTrue(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.Oferton)
                instanceof PrecioOferton);
    }

    // 2. Lo mismo, pidiendo la política por su nombre en texto.
    @Test
    public void devuelveLaClaseCorrectaPorNombre() {
        assertTrue(FactoriaPoliticasPrecios.getCalculador("Normal") instanceof PrecioNormal);
        assertTrue(FactoriaPoliticasPrecios.getCalculador("TemporadaAlta") instanceof PrecioTemporadaAlta);
        assertTrue(FactoriaPoliticasPrecios.getCalculador("ClienteFrecuente") instanceof PrecioClienteFrecuente);
        assertTrue(FactoriaPoliticasPrecios.getCalculador("Oferton") instanceof PrecioOferton);
    }

    // 3. Un nombre que no existe lanza IllegalArgumentException.
    @Test(expected = IllegalArgumentException.class)
    public void nombreDesconocidoLanzaExcepcion() {
        FactoriaPoliticasPrecios.getCalculador("Inventada");
    }

    // 4. El nombre distingue mayúsculas y minúsculas (valueOf es exacto).
    @Test(expected = IllegalArgumentException.class)
    public void nombreEnMinusculasLanzaExcepcion() {
        FactoriaPoliticasPrecios.getCalculador("normal");
    }

    // 5a. Enum nulo.
    @Test(expected = IllegalArgumentException.class)
    public void politicaNulaLanzaExcepcion() {
        FactoriaPoliticasPrecios.getCalculador((PoliticasDePrecios) null);
    }

    // 5b. Texto nulo.
    @Test(expected = IllegalArgumentException.class)
    public void nombreNuloLanzaExcepcion() {
        FactoriaPoliticasPrecios.getCalculador((String) null);
    }

    // 6. Cada estrategia creada por la factoría calcula el precio esperado
    //    (precio base 50, 10 días).
    @Test
    public void estrategiasCalculanElPrecioEsperado() {
        assertEquals(500, FactoriaPoliticasPrecios.getCalculador("Normal").calcularPrecio(50, 10));
        assertEquals(1000, FactoriaPoliticasPrecios.getCalculador("TemporadaAlta").calcularPrecio(50, 10));
        assertEquals(330, FactoriaPoliticasPrecios.getCalculador("ClienteFrecuente").calcularPrecio(50, 10));
        assertEquals(315, FactoriaPoliticasPrecios.getCalculador("Oferton").calcularPrecio(50, 10));
    }

    // 7. Integración con Alquiler (el contexto): se le asigna la estrategia
    //    de la factoría y el alquiler calcula con ella.
    //    Turismo NORMAL = 50 euros/dia; del 1 al 10 de noviembre son 10 dias.
    @Test
    public void alquilerUsaLaPoliticaCreadaPorLaFactoria() {
        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
        Alquiler alquiler = new Alquiler(turismo, cliente,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 10));

        alquiler.setCalculador(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.Oferton));
        assertEquals(315, alquiler.calcularPrecioAlquiler());

        alquiler.setCalculador(FactoriaPoliticasPrecios.getCalculador("TemporadaAlta"));
        assertEquals(1000, alquiler.calcularPrecioAlquiler());
    }
}