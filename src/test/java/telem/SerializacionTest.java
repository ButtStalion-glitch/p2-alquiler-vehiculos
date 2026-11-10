package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Ejercicio 3: tests de serialización y deserialización con el Método Factoría.
 * Objetivo: comprobar el "viaje de ida y vuelta": objeto -> JSON -> objeto,
 * y que el objeto reconstruido tiene los mismos datos que el original.
 */
public class SerializacionTest {

    // ---------- Cliente ----------

    @Test
    public void clienteIdaYVuelta() {
        Cliente original = new Cliente("Tomas Sanchez", "12345678X");
        Cliente copia = new Cliente(original.toString()); // constructor desde JSON
        assertEquals(original.getNombre(), copia.getNombre());
        assertEquals(original.getDni(), copia.getDni());
    }

    // ---------- Vehículos: la factoría elige la subclase ----------

    @Test
    public void factoriaReconstruyeUnTurismo() {
        Turismo original = new Turismo("1234ABC", Categoria.ESPECIAL, 2021, 5, 450.0);
        original.setDisponible(true);
        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof Turismo);             // la factoría eligió bien la clase
        Turismo t = (Turismo) copia;                      // downcasting para leer lo propio
        assertEquals("1234ABC", t.getMatricula());
        assertEquals(Categoria.ESPECIAL, t.getCategoria());
        assertEquals(2021, t.getAnnio());
        assertTrue(t.isDisponible());
        assertEquals(5, t.getPlazas());
        assertEquals(450.0, t.getCapacidadMaletero(), 0.001);
    }

    @Test
    public void factoriaReconstruyeUnaMotocicleta() {
        Motocicleta original = new Motocicleta("5678DEF", Categoria.SUPERIOR, 2022, 600);
        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof Motocicleta);
        assertEquals("5678DEF", copia.getMatricula());
        assertEquals(600, ((Motocicleta) copia).getCilindrada());
        assertEquals(70, copia.getPrecioBase());          // SUPERIOR: el precio se mantiene
    }

    @Test
    public void factoriaReconstruyeUnVehiculoIndustrial() {
        VehiculoIndustrial original = new VehiculoIndustrial("9999XYZ", Categoria.NORMAL, 2018, 3500, 3);
        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof VehiculoIndustrial);
        VehiculoIndustrial vi = (VehiculoIndustrial) copia;
        assertEquals(3500, vi.getCapacidadCarga());
        assertEquals(3, vi.getNumeroEjes());
    }

    @Test
    public void laCopiaGeneraElMismoJson() {
        // Si reconstruimos y volvemos a serializar, sale el mismo texto
        Vehiculo original = new Motocicleta("5678DEF", Categoria.NORMAL, 2019, 125);
        String json = original.toString();
        assertEquals(json, FactoriaVehiculos.getVehiculo(json).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaUnTipoDesconocido() {
        // expected: el test pasa solo si se lanza esta excepción
        FactoriaVehiculos.getVehiculo("{\"tipo\":\"Avion\",\"matricula\":\"X\"}");
    }

    // ---------- Alquiler: JSON con objetos dentro ----------

    @Test
    public void alquilerIdaYVuelta() {
        Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
        Vehiculo moto = new Motocicleta("5678DEF", Categoria.SUPERIOR, 2022, 600);
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = LocalDate.of(2026, 11, 10);
        Alquiler original = new Alquiler(moto, cliente, inicio, fin);

        Alquiler copia = new Alquiler(original.toString());

        assertEquals("Tomas Sanchez", copia.getCliente().getNombre());
        assertTrue(copia.getVehiculo() instanceof Motocicleta);   // vía la factoría
        assertEquals("5678DEF", copia.getVehiculo().getMatricula());
        assertEquals(inicio, copia.getFechaInicio());              // vía LocalDate.parse
        assertEquals(fin, copia.getFechaFin());
        assertEquals(original.calcularPrecioAlquiler(), copia.calcularPrecioAlquiler());
    }
}