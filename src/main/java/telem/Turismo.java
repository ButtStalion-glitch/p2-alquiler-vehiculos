package telem;

  import org.json.JSONObject;

public class Turismo extends Vehiculo {


    int plazas;
    double capacidadMaletero;

    public Turismo(String matricula, Categoria categoria, int annio,
        int plazas, double capacidadMaletero
    ) {
        super(matricula, categoria, annio);
        this.plazas = plazas;
        this.capacidadMaletero = capacidadMaletero;
    }
        /**
     * Ejercicio 3: constructor para deserializar un turismo desde su JSON.
     */
    public Turismo(String jsonString) {
        super(jsonString);                                         // atributos comunes (Vehiculo)
        JSONObject json = new JSONObject(jsonString);
        this.plazas = json.getInt("plazas");                       // atributos propios
        this.capacidadMaletero = json.getDouble("capacidadMaletero");
    }
    public int getPlazas() {
        return plazas;
    }

    public double getCapacidadMaletero() {
        return capacidadMaletero;
    }

    public String getTipo(){
        return "Turismo";
    }

    public int getPrecioBase(){
        int precioBase=0;
        switch (getCategoria()){
            case NORMAL:
                precioBase=50;
                break;
            case ESPECIAL:
                precioBase=80;
                break;
            case SUPERIOR:
                precioBase=100;
                break;
        }
        return precioBase;
    }
}
