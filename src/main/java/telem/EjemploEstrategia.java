package telem;

import java.time.LocalDate;

public class EjemploEstrategia {
/**
 * Ejercicio 2: ejemplo de uso del patrón Estrategia.
 * Un mismo alquiler calcula su precio con las cuatro políticas,
 * cambiando la estrategia sin tocar la clase Alquiler.
 */
    public static void main(String[] args) {
        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0); // 50 €/día
        Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = LocalDate.of(2026, 11, 10);                                   // 10 días

        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, fin);
        System.out.println("Turismo NORMAL (50 euros/dia), del " + inicio + " al " + fin
                + " (" + Alquiler.calcularDias(inicio, fin) + " dias)");
        System.out.println("-----------------------------------------------");

        // Array de estrategias: todas son CalculadorPrecio (polimorfismo)
        CalculadorPrecio[] politicas = {
            new PrecioNormal(),
            new PrecioTemporadaAlta(),
            new PrecioOferton(),
            new PrecioClienteFrecuente()
        };
        String[] nombres = {"Normal", "Temporada alta", "Oferton", "Cliente frecuente"};

        for (int i = 0; i < politicas.length; i++) {
            alquiler.setCalculador(politicas[i]);   // se cambia la estrategia
            System.out.println(nombres[i] + ": " + alquiler.calcularPrecioAlquiler() + " euros");
        }
    }
}   

