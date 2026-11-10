package telem;

public class PrecioOferton implements  CalculadorPrecio{
     public int calcularPrecio(int precioBase, int dias) {
        double total = precioBase * 0.70 * dias;   // -30 % al precio base
        if (dias > 7) {
            total = total * 0.90;                  // -10 % adicional al total
        }
        return (int) Math.round(total);
    }
}

