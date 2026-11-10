package telem;

public class PrecioTemporadaAlta implements CalculadorPrecio{
    public int calcularPrecio(int precioBase, int dias) {
        return precioBase * 2 * dias;
    
    }
}
