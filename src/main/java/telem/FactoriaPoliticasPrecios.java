
//Ejercico 4 Método Factoría para crear políticas de precios
//---------------------------------------------------------------

package telem;

import java.time.LocalDate;

 //Le pide a la clase cada política por su nombre y la factoría decide qué
 //clase concreta crear.
 // Relacionado a otras clases:
 //-	CalculadorPrecio
 //- 	*PrecioNormal, PrecioTemporadaAlta, PrecioClienteFrecuente, 
 //	PrecioOferton (Estrategias que la factoria crea)
 //-    PoliticasDePrecios(lista los nombres válidos de política)
 //-    Alquiler contexto del patrón Estrategia; aquí solo se usa en el main.

public class FactoriaPoliticasPrecios {

    /**
     * Creo estrategia de precio que corresponde a la política indicada.
     *
     * @param politica valor del enum PoliticasDePrecios
     * @return CalculadorPrecio
     * @throws IllegalArgumentException salta si la politica es nula 
     */

    public static CalculadorPrecio getCalculador(PoliticasDePrecios politica) {
        if (politica == null) {
            throw new IllegalArgumentException ("La política de precios no puede ser nula");
        }

        switch (politica) {  
	// Cada case crea clase implementado CalculadorPrecio
            case Normal:
                return new PrecioNormal();  		// precioBase * días
            case TemporadaAlta:
                return new PrecioTemporadaAlta();  	// precio base duplicado
            case ClienteFrecuente:
                return new PrecioClienteFrecuente();  	// 20% los 3 primeros días, 40% después
            case Oferton:
                return new PrecioOferton();  		// 30% de descuento, +10% si más de 7 días
            default:
                throw new IllegalArgumentException ("Política desconocida: " + politica);
        }
    }

    /**
     * Creo estrategia a partir del nombre de la política como texto(metodo anterior)
     *
     * @param nombre nombre de la política: "Normal", "TemporadaAlta","ClienteFrecuente" u "Oferton"
     * @return CalculadorPrecio 
     * @throws IllegalArgumentException == nulo(nombre)
     */

    public static CalculadorPrecio getCalculador(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la política no puede ser nulo");
  	}
         try {
            // Ejemplo con "Oferton" == PoliticasDePrecio.Oferton
            return getCalculador(PoliticasDePrecios.valueOf(nombre));
        } catch (IllegalArgumentException e) {
            // valueOf lanza esta excepción si el texto no coincide con ningún valor del enum.
	    // La capturamos solo para relanzarla con un mensaje más claro.
            throw new IllegalArgumentException("Política de precios desconocida: " + nombre);
        }
    }

 
     // Ejemplo de uso: el mismo alquiler se calcula con cada política, pidiéndoselas a la factoría.
     
    public static void main(String[] args) {

        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Tomas Sanchez", "12345678X");
        Alquiler alquiler = new Alquiler(turismo, cliente,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 10));          

        // 1. Recorremos(for-each) todas las políticas del enum

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            alquiler.setCalculador(FactoriaPoliticasPrecios.getCalculador(politica));
            System.out.println(politica + ": " + alquiler.calcularPrecioAlquiler() + " euros");
        }

        System.out.println("-----------------------------------------------");

        // 2. Pedir una política por su nombre en texto

        alquiler.setCalculador(FactoriaPoliticasPrecios.getCalculador("Oferton"));
        System.out.println("Por nombre (Oferton): " + alquiler.calcularPrecioAlquiler() + " euros");
	
	alquiler.setCalculador(FactoriaPoliticasPrecios.getCalculador("Normal"));
	System.out.println("Por nombre (Normal): " + alquiler.calcularPrecioAlquiler() + " euros");

	System.out.println("-----------------------------------------------");


        // 3. Un nombre que no existe provoca una excepción controlada

        try {
            FactoriaPoliticasPrecios.getCalculador("Inventada");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }
}




