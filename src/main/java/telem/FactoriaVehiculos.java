package telem;

import java.time.LocalDate;
import org.json.JSONObject;

public class FactoriaVehiculos {
    public static Vehiculo getVehiculo(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String clase = json.getString("tipo");
        switch (clase) {
            case "Turismo":
                return new Turismo(jsonString);
            case "Motocicleta":
                return new Motocicleta(jsonString);
            case "VehiculoIndustrial":
                return new VehiculoIndustrial(jsonString);
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + clase);
        }
    }

    /**
     * Ejemplo de uso: se serializan objetos a JSON y se reconstruyen.
     */
    public static void main(String[] args) {
        // 1. Vehículos: se serializan y la factoría los reconstruye sin saber su tipo
        Vehiculo[] originales = {
            new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0),
            new Motocicleta("5678DEF", Categoria.SUPERIOR, 2022, 600),
            new VehiculoIndustrial("9999XYZ", Categoria.ESPECIAL, 2018, 3500, 3)
        };
        for (Vehiculo original : originales) {
            String serializado = original.toString();                      // objeto -> JSON
            Vehiculo copia = FactoriaVehiculos.getVehiculo(serializado);   // JSON -> objeto
            System.out.println("JSON:  " + serializado);
            System.out.println("Clase: " + copia.getClass().getSimpleName()
                    + ", matricula " + copia.getMatricula());
            System.out.println("-----------------------------------------------");
        }

        // 2. Cliente
        Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
        Cliente clienteCopia = new Cliente(cliente.toString());
        System.out.println("Cliente reconstruido: " + clienteCopia.getNombre());

        // 3. Alquiler: lleva dentro un cliente, un vehículo y dos fechas
        Alquiler alquiler = new Alquiler(originales[1], cliente,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 10));
        Alquiler alquilerCopia = new Alquiler(alquiler.toString());
        System.out.println("Alquiler reconstruido: " + alquilerCopia.getVehiculo().getTipo()
                + " de " + alquilerCopia.getCliente().getNombre()
                + ", del " + alquilerCopia.getFechaInicio() + " al " + alquilerCopia.getFechaFin()
                + ", precio " + alquilerCopia.calcularPrecioAlquiler() + " euros");
    } 
}
