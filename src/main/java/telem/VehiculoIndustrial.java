package telem;

import org.json.JSONObject;

public class VehiculoIndustrial extends Vehiculo {

    private int capacidadCarga;
    private int numeroEjes;

    public VehiculoIndustrial(String matricula, Categoria categoria, int annio,
        int capacidadCarga, int numeroEjes
    ) {
        super(matricula, categoria, annio);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
    }
    
        /**
     * Ejercicio 3: constructor para deserializar un vehículo industrial desde su JSON.
     */
    public VehiculoIndustrial(String jsonString) {
        super(jsonString);                                     // atributos comunes (Vehiculo)
        JSONObject json = new JSONObject(jsonString);
        this.capacidadCarga = json.getInt("capacidadCarga");   // atributos propios
        this.numeroEjes = json.getInt("numeroEjes");
    }

    public int getCapacidadCarga() {
        return capacidadCarga;
    }

    public int getNumeroEjes() {
        return numeroEjes;
    }

    public String getTipo(){
        return "VehiculoIndustrial";
    }

    public int getPrecioBase() {
        int precioBase = 0;
        switch (getCategoria()) {
            case NORMAL:
                precioBase = 80;
                break;
            case ESPECIAL:
                precioBase = 120;
                break;
            case SUPERIOR:
                precioBase = 150;
                break;
        }
        return precioBase;
    }
    
    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }
}
