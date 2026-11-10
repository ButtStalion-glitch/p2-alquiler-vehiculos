package telem;

/**
 * Patrón Estrategia: interfaz común a todas las políticas de precios.
 * Cada política (estrategia) implementa este método a su manera.
 * Alquiler (el contexto) solo conoce esta interfaz, no las clases concretas.
 */
public interface CalculadorPrecio {

    /**
     * Calcula el precio total de un alquiler.
     * @param precioBase precio base diario del vehículo (getPrecioBase()).
     * @param dias número de días del alquiler (incluidos el de inicio y el de fin).
     * @return precio total en euros, redondeado al entero más cercano.
     */
    int calcularPrecio(int precioBase, int dias);
}