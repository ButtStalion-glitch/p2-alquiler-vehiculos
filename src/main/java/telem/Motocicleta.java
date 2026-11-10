package telem;

import org.json.JSONObject;
public class Motocicleta extends Vehiculo {

    int cilindrada;

    public Motocicleta(String matricula, Categoria categoria, int annio,
        int cilindrada
    ) {
        super(matricula, categoria, annio);
        this.cilindrada = cilindrada;
    }
        /**
     * Ejercicio 3: constructor para deserializar una motocicleta desde su JSON.
     */
    public Motocicleta(String jsonString) {
        super(jsonString);                                // atributos comunes (Vehiculo)
        JSONObject json = new JSONObject(jsonString);
        this.cilindrada = json.getInt("cilindrada");      // atributo propio
    }
    

    public int getCilindrada() {
        return cilindrada;
    }

        public String getTipo(){
        return "Motocicleta";
    }
    public int getPrecioBase(){
        int precioBase=0;
        switch(getCategoria()){
            case NORMAL:
                precioBase=30;
                break;
            case ESPECIAL:
                precioBase=50;
                break;
            case SUPERIOR:
                precioBase=70;
                break;        
        }
        return precioBase;
    }

}
