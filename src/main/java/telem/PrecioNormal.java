package telem;

public class PrecioNormal implements CalculadorPrecio{
     public int calcularPrecio(int precioBase, int dias) {
        return precioBase * dias;
    
    }
}
