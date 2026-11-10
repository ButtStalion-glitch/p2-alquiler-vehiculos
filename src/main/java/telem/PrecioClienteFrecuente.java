package telem;

public class PrecioClienteFrecuente implements CalculadorPrecio {
    public int calcularPrecio(int precioBase, int dias) {
        int diasConDescuento20 = Math.min(dias, 3);      // como mucho los 3 primeros
        int diasConDescuento40 = Math.max(dias - 3, 0);  // del cuarto en adelante (0 si no llega)
        double total = diasConDescuento20 * precioBase * 0.80
                     + diasConDescuento40 * precioBase * 0.60;
        return (int) Math.round(total);
    }
}
