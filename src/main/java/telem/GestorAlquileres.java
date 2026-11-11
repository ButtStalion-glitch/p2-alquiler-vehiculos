package telem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

public class GestorAlquileres {
    
    private int contadorAlquileres = 0;
    private Alquiler[] alquileres = new Alquiler[1000];

    // Para simplificar, vamos a definir un array de clientes y 
    // otro de vehículos dentro del gestor.
    // En una aplicación real estarían en una base de datos.
    private Cliente[] clientes = new Cliente[10];
    private Vehiculo[] vehiculos = new Vehiculo[100];


    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INICIALIZACIÓN DE CLIENTES Y VEHÍCULOS DE EJEMPLO

    // Definimos métodos privados para inicializar clientes y vehículos de ejemplo
    private void inicializarClientes() {
        clientes[0] = new Cliente("Clara Videncia", "001");
        clientes[1] = new Cliente("Aitor Menta", "002");
        clientes[2] = new Cliente("Lola Mento", "003");
        clientes[3] = new Cliente("Armando Guerra", "004");
        clientes[4] = new Cliente("Ana Lítica", "005");
        clientes[5] = new Cliente("Carmelo Cotón", "006");
    }

    private void inicializarVehiculos() {
        vehiculos[0] = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        vehiculos[1] = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        vehiculos[2] = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);
        vehiculos[3] = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0);
        vehiculos[4] = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);
        vehiculos[5] = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        vehiculos[6] = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);
        vehiculos[7] = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);
        vehiculos[8] = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR VEHÍCULOS

     public Vehiculo[] obtenerVehiculos(String tipo) {
        
        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo)) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }   

    /**
     * Ejercicio 6: devuelve array de vehículos del tipo y categoría especificados.
     * Reutiliza el método obtenerVehiculos(String tipo) para filtrar por tipo y luego filtra por categoría.
     * @param tipo
     * @param categoria
     * @return lista de vehículos del tipo y categoría especificados
     */
    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {

        Vehiculo [] delTipo = obtenerVehiculos(tipo);          // reutiliza el método anterior para filtrar por tipo
        Vehiculo[] vehiculosFiltrados = new Vehiculo[delTipo.length];
        int contador = 0;

        for (Vehiculo v : delTipo) {
            if (v != null) {   // comprueba que no sea null
                if (v.getCategoria() == categoria) {  // filtro por categoría
                    vehiculosFiltrados[contador++] = v;
                }
            }
        }

        // Ajustar el tamaño real del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }

    /**
     * Ejercicio 6: METODO AUXILIAR añadido que busca un vehículo por matrícula(unica)
     * Dada una matrícula, devuelve el vehículo del gestor, o null si no existe.
     * @param matricula matrícula del vehículo a buscar
     * @return el vehiculo
     */

    public Vehiculo buscarVehiculo(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v != null && v.getMatricula().equals(matricula)) {
                return v; // Devuelve el vehículo encontrado
            }
        }
        return null; // Devuelve null si no se encuentra el vehículo
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR ALQUILERES

    /**
     * Agrega un nuevo alquiler al array de alquileres.
     * @param alquiler
     */
    public void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    /**
     * Devuelve un array con todos los alquileres registrados en el gestor
     * @return
     */
    public Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    /**
     * Ejercicio 6:
     * Devuelve un array con todos los alquileres de un cliente específico
     * Los clientes se comparan por el Dni porque Clientes no redefinen equals() y hashCode() y por tanto no se pueden comparar con equals()
     * @return array con los alquileres del cliente, vacio si no tiene
     */
    public Alquiler[] obtenerAlquileres(Cliente cliente) {   // histo de alquileres de un cliente específico
        Alquiler[] delCliente = new Alquiler[contadorAlquileres]; // tamaño máximo posible
        int contador = 0;

        //Solo recorro posiciones ocupadas de alquileres, no todo el array
        for(int i = 0; i < contadorAlquileres; i++) {
           if(alquileres[i].getCliente().getDni().equals(cliente.getDni())) {  // Compara DNi del cliente si coincide con el del cliente buscado
                delCliente[contador++] = alquileres[i];  // guarda en el array temporal y aumenta contador
            }
        }

        Alquiler[] resultado = new Alquiler[contador]; // tamaño exacto del array de resultados
        System.arraycopy(delCliente, 0, resultado, 0, contador);
        return resultado;

    }

    /**
     * Ejercicio 6: METODO AUXILIAR añadido que indica si el alquiler en la posición i del array de alquileres esta en curso
     * Esta en curso si el vehiculo no esta disponible y no hay alquiler posterior para el mismo vehiculo
     * (el ultimo alquiler de un vehiculo es el vigente; los anterires ya son historicos)
     */
    public boolean estaEnCurso(int i) {
        String matricula = alquileres[i].getVehiculo().getMatricula(); // identifica matricula vehiculo
        Vehiculo v = buscarVehiculo(matricula); // busca vehiculo en el gestor
        if (v == null || v.isDisponible() ) { // null si no existe o no esta disponible
            return false;    
        }
        for( int j = i + 1; j < contadorAlquileres; j++) { // recorre los alquileres posteriores al i
            if (alquileres[j].getVehiculo().getMatricula().equals(matricula)) {
                return false; // Hay un alquiler posterior para el mismo vehículo, no está en curso
            }
        }
        return true; // No hay alquiler posterior para el mismo vehículo, no esta disponible,está en curso
    }

    /**
     * Ejercicio6: 
     * Informa si un cliente es cliente frecuente en función del número de alquileres previos.
     * @param cliente cliente a evaluar
     * @param umbral número mínimo de alquileres previos que se consideran para determinar si es cliente frecuente
     * @return true si tiene al menos "umbral" alquileres previos, false en caso contrario
     */
    private boolean esClienteFrecuente(Cliente cliente, int umbral) {

       return obtenerAlquileres(cliente).length >= umbral;  // si tiene más o el mismo número de alquileres que el umbral, es cliente frecuente
    }

    //Nuevo (EJERCICIO 5 )
    /////////////////////////////////////////////////////////////////////////////////////
    // PERSISTENCIA EN FICHERO (JSON)

    //Getter de clientes: lo necesitan las pruebas para comprobar el estado
    public Cliente[] obtenerClientes() {
     return clientes;
     }
 
    //Getter de todos los vehículos
    public Vehiculo[] obtenerVehiculos() {
     return vehiculos;
     }
    

    ////----------------------(1) --------------------------//// Guardar
    /**
    * Guarda clientes, vehículos y alquileres en un fichero de texto en formato JSON.
    * Estructura: {"clientes":[...], "vehiculos":[...], "alquileres":[...]}
    */
    public void guardarEnFichero(String ruta) throws IOException {
    // Un JSONArray por colección. toString() de cada objeto ya devuelve su JSON;
    // se convierte a JSONObject para que quede anidado como objeto y no como texto

     JSONArray jsonClientes = new JSONArray();
     for (Cliente c : clientes) {
        if (c != null) {                                        // recorre el array si tiene huecos null para que estos no aparezcan el c.toString()
            jsonClientes.put(new JSONObject(c.toString()));              // Convierte el String en un objeto JSON 
        }
     }

     JSONArray jsonVehiculos = new JSONArray();
     for (Vehiculo v : vehiculos) {                               // elige la subclase según el campo "tipo"
        if (v != null) {                                         // recorre el array si tiene huecos null para que estos no aparezcan el v.toString()
            jsonVehiculos.put(new JSONObject(v.toString()));             // Convierte el String en un objeto JSON
        }
     }

     JSONArray jsonAlquileres = new JSONArray();
     for (int i = 0; i < contadorAlquileres; i++) {                       // solo posiciones ocupadas no hasta alquileres.length
        jsonAlquileres.put(new JSONObject(alquileres[i].toString()));
     }

     // raiz agrupa las tres colecciones
     JSONObject raiz = new JSONObject();
     raiz.put("clientes", jsonClientes);
     raiz.put("vehiculos", jsonVehiculos);
     raiz.put("alquileres", jsonAlquileres);
	
     // Se crea el fichero y se escribe. finally que garantiza que close() se ejecute
     // siempre, haya o no excepción, para no dejar el fichero abierto.
     FileWriter escritor = new FileWriter(ruta);
     try {
        escritor.write(raiz.toString(2));        // genera el JSON como texto indentando 2 espacios 
     } finally {
        escritor.close();             //siempre se ejecuta para no quedar bloqueado
     }
    
     }

     ////----------------------(2) --------------------------//// Cargar
    /**
    * Sustituye los datos del gestor por los del fichero.
    * Si el fichero es inválido se lanza excepción y el estado actual NO se modifica.
    */
    public void cargarDeFichero(String ruta) throws IOException {

     // 1 Leer todo el fichero en un String.
    StringBuilder texto = new StringBuilder();
     BufferedReader lector = new BufferedReader(new FileReader(ruta));   //abre fichero para leer(por lineas) los caracteres
     try {
        String linea;
        while ((linea = lector.readLine()) != null) {    //lee linea y la guarda en linea. readLine() = null cuando llega  al final
            texto.append(linea).append("\n");            //añade linea(con un salto de linea) 
        }
     } finally {
        lector.close();    //cierro fichero
     }

     // 2 Si no es JSON válido, JSONObject lanza JSONException (RuntimeException).
     // Convierte texto leido en ficheros en JSON manejables
     JSONObject raiz = new JSONObject(texto.toString());
     //clientes , vehiculos , alquiler se usaron raiz.put en guardarEnnFichero
     JSONArray jsonClientes = raiz.getJSONArray("clientes");
     JSONArray jsonVehiculos = raiz.getJSONArray("vehiculos");
     JSONArray jsonAlquileres = raiz.getJSONArray("alquileres");

     // 3 Comprobar que caben en los arrays de tamaño fijo del gestor.
     if (jsonClientes.length() > clientes.length) {                          // Si el fichero tiene mas elementos de los que caben = Excepcion
        throw new IllegalArgumentException("Hay demasiados clientes en el fichero");
     }
     if (jsonVehiculos.length() > vehiculos.length) {
        throw new IllegalArgumentException("Hay demasiados vehículos en el fichero");
     }
     if (jsonAlquileres.length() > alquileres.length) {
        throw new IllegalArgumentException("Hay demasiados alquileres en el fichero");
     }
    
     // 4 Reconstruir en arrays TEMPORALES/NUEVOS (para punto 5) 
     Cliente[] nuevosClientes = new Cliente[clientes.length];   // Nuevo array con tamaño igual que el del gestor                       
     for (int i = 0; i < jsonClientes.length(); i++) {                              
        nuevosClientes[i] = new Cliente(jsonClientes.getJSONObject(i).toString());   //Constructor de deseralizacion(Ejercicio 3) lee dni y nombre y construye cliente
     }
     Vehiculo[] nuevosVehiculos = new Vehiculo[vehiculos.length];    // No hago "new Vehiculo" porque no sabe de que subclase es cada uno. De eso se encarga FactoriaVehiculos.getVehiculo 
     for (int i = 0; i < jsonVehiculos.length(); i++) {
        // Patrón Método Factoría : elige la subclase según el campo "tipo"
        nuevosVehiculos[i] = FactoriaVehiculos.getVehiculo(jsonVehiculos.getJSONObject(i).toString());
     }
     Alquiler[] nuevosAlquileres = new Alquiler[alquileres.length];  //Igual que cliente pero con alquiler
     for (int i = 0; i < jsonAlquileres.length(); i++) {
        nuevosAlquileres[i] = new Alquiler(jsonAlquileres.getJSONObject(i).toString());
     }

     // 5 Paso 4 funciona. Se apuntan los atributos de los nuevos arrays
     this.clientes = nuevosClientes;
     this.vehiculos = nuevosVehiculos;
     this.alquileres = nuevosAlquileres;
     this.contadorAlquileres = jsonAlquileres.length();
}

    /////////////////////////////////////////////////////////////////////////////////////
    /// //Nuevo (EJERCICIO 5 )


    /////////////////////////////////////////////////////////////////////////////////////
    // INTERFAZ DE USUARIO (CONSOLA) Y LÓGICA DE NEGOCIO

    // La interfaz de usuario y la lógica de negocio suelen implementarse por separado.
    // Aquí se integran ambas por simplicidad, pero en una aplicación real hay que separarlas.


    public void menuPrincipal(){
        
        boolean salir = false;
        
        while(!salir){

            // Se da oportunidad de salir inmediatamente de la app.
            System.out.println("Gestíon de Alquileres");
            System.out.println("¿Desea salir de la app (S/N)?");

            char s = KeyBoard.readChar();
            if (s == 's' || s == 'S'){
                System.out.println("Bye ...");
                return;
            }

            // Primero se identifica al cliente:
            Cliente cliente = identificarCliente();

            // Ahora el cliente puede elegir una opción o salir de la aplicación.
            if (cliente != null) {
                System.out.println("Sr " + cliente.getNombre() + " Introduzca una opción");
                System.out.println("(1) seleccionar coche para alquilar");
                System.out.println("(2) entregar un coche alquilado");
                System.out.println("(3) listar su histórico de alquileres");
                //AMPLIACION DE LA INTERFAZ USUARIO PARA GUARDAR Y CARGAR DATOS EN FICHERO (EJERCICIO 5)
                System.out.println("(4) guardar datos en fichero");      // nuevo EJERCICIO5
                System.out.println("(5) cargar datos desde fichero");    // nuevo EJERCICIO5

                int opcion = KeyBoard.readInt();
                System.out.println("opción escogida: " + opcion);

                switch (opcion) {
                    case 1:
                        Vehiculo v = seleccionarVehiculo(cliente);
                        if ( v!=null && !v.isDisponible()){
                            // para simplificar la IU se supone que el alquiler comienza "ahora"
                            // y el usuario especifica duración en días.
                            System.out.println("¿cuántos días desea alquilar el vehículo?");
                            int dias = KeyBoard.readInt();
                            LocalDate inicio = LocalDate.now();
                            LocalDate fin = inicio.plusDays(dias);

                            // Creamos y registramos el alquiler
                            Alquiler q = new Alquiler(v, cliente, inicio, fin);
                            this.agregarAlquiler(q);

                            // Calculamos precio.
                            // Aquí habría que determinar la política de precios aplicable según el tipo de cliente y la fecha.
                            // Para simplificar, se aplicará la política de cliente frecuente si el cliente tiene más de 3 alquileres previos, 
                            // y la política normal en caso contrario.

                            //Comprobamos si el cliente es usuario frecuente para aplicarle
                            // la tarifa que le corresponde.
                            boolean usuarioFrecuente = this.esClienteFrecuente(cliente, 3);

                            if (usuarioFrecuente){
                                // Estrategia: se asigna la política de cliente frecuente al alquiler (la crea la factoría)
                                q.setCalculador(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.ClienteFrecuente));
                                v.setDisponible(false);   // el vehículo pasa a no disponible
                                System.out.println("Precio total: " + q.calcularPrecioAlquiler() + " euros");
                               
                            }
                            else{
                                // Estrategia: política normal
                                q.setCalculador(FactoriaPoliticasPrecios.getCalculador(PoliticasDePrecios.Normal));
                                v.setDisponible(false);
                                System.out.println("Precio total: " + q.calcularPrecioAlquiler() + " euros");
                            }
                            
                        }
                        else{
                            System.out.println("Debe seleccionar un vehículo disponible");
                        }
                        break;

                    case 2: 
                    // índices (en el array alquileres) de los alquileres en curso del cliente
                    int[] enCurso = new int[contadorAlquileres];
                    int pendientes = 0;
                    for (int i = 0; i < contadorAlquileres; i++) {
                        if (alquileres[i].getCliente().getDni().equals(cliente.getDni()) && estaEnCurso(i)) {
                    enCurso[pendientes++] = i;
                        }
                    }
                    if (pendientes == 0) {
                    System.out.println("No tiene vehículos pendientes de entrega.");
                    } else {
                        for (int k = 0; k < pendientes; k++) {
                        System.out.println(k + " ---> " + alquileres[enCurso[k]]);
                        }
                        System.out.println("Introduzca el número del vehículo a entregar:");
                        int e = KeyBoard.readInt();
                        if (e >= 0 && e < pendientes) {
                            // se libera el vehículo DEL GESTOR (buscado por matrícula)
                            Vehiculo ve = buscarVehiculo(alquileres[enCurso[e]].getVehiculo().getMatricula());
                        ve.setDisponible(true);
                        System.out.println("Vehículo entregado.");
                        } else {
                        System.out.println("Selección no válida");
                        }
                    }
                    break;
                    

                    case 3:
                        Alquiler alqs [] = obtenerAlquileres(cliente);
                        for (Alquiler q: alqs){
                            System.out.print(q);
                        }
                        break;
                        //AMPLIACION DE LA INTERFAZ USUARIO PARA GUARDAR Y CARGAR DATOS EN FICHERO (EJERCICIO 5)
                    case 4:                                                  // nuevo
                        System.out.println("Nombre del fichero:");
                        String rutaGuardar = KeyBoard.readString();
                        try {
                            guardarEnFichero(rutaGuardar);
                            System.out.println("Datos guardados en " + rutaGuardar);
                        } catch (IOException e) {
                            System.out.println("Error al guardar: " + e.getMessage());
                        }
                        break;
                    case 5:                                                  // nuevo
                        System.out.println("Nombre del fichero:");
                        String rutaCargar = KeyBoard.readString();
                        try {
                            cargarDeFichero(rutaCargar);
                            System.out.println("Datos cargados desde " + rutaCargar);
                        } catch (IOException e) {
                        System.out.println("Error al leer el fichero: " + e.getMessage());
                        } catch (RuntimeException e) {
                       // JSONException e IllegalArgumentException (contenido inválido)
                        System.out.println("Fichero con contenido inválido: " + e.getMessage());
                       }
                    break;
            default:
                    System.out.println("Opción no válida.");
                    break;
                }
            }
            else{
                System.out.println("Debe identificarse correctamente o salir de la app");
            }
        }
    }

    public Cliente identificarCliente(){

        boolean seleccionado = false;
        Cliente cliente = null;

        while (!seleccionado) {
        
            System.out.println("Bienvenido al Gestor de Alquileres");
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i] != null) {
                    System.out.println(i + " ---> " + clientes[i].getNombre() + " - " + clientes[i].getDni());
                }
            }
            System.out.println("Identífiquese como cliente con su número de cliente (-1 para salir):");
            int numeroCliente = KeyBoard.readInt();
            if (numeroCliente == -1) {
                return null;
            }
            else if (numeroCliente < 0 || numeroCliente >= clientes.length || clientes[numeroCliente] == null) {
                System.out.println("identificarCliente:Número de cliente inválido.");
                return null;
            }
            else{
                seleccionado = true;
                cliente = clientes[numeroCliente];
            }
        }
        return cliente;
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente){
        boolean seleccionado = false;
        Vehiculo vehiculo = null;

        while (!seleccionado) {
        
            System.out.println("Estimado Sr./Sra. " + cliente.getNombre());
            System.out.println("Introduzca el número correspondiente al tipo de vehículo que desea alquilar:");
            System.out.println("1. Turismo, 2. Motocicleta, 3. Vehículo Industrial: ");

            int tipoVehiculo = KeyBoard.readInt();
            String tipo = "";
            Vehiculo [] elegibles = new Vehiculo[100];

            switch (tipoVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Turismo.");
                    tipo = "Turismo";
                    break;
                case 2:
                    System.out.println("Ha seleccionado Motocicleta.");
                    tipo = "Motocicleta";
                    break;
                case 3:
                    System.out.println("Ha seleccionado Vehículo Industrial.");
                    tipo = "VehículoIndustrial";
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un tipo de vehículo válido.");
                    continue; // Volver a pedir la selección
            }

            System.out.println("Vehiculos disponibles");

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre() + "\n seleccione categoría.");
            System.out.println("Introduzca el número correspondiente a la categoría que desea alquilar:");
            System.out.println("1. Normal, 2. Superior, 3. Especial, 4.Cualquiera: ");

            int categoriaVehiculo = KeyBoard.readInt();

            switch (categoriaVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Categoría Normal.");
                    elegibles = obtenerVehiculos(tipo, Categoria.NORMAL);
                    break;
                case 2:
                    System.out.println("Ha seleccionado Categoría Superior.");
                    elegibles = obtenerVehiculos(tipo, Categoria.SUPERIOR);
                    break;
                case 3:
                    System.out.println("Ha seleccionado Categoría Especial.");
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break;
                default:
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break; // Volver a pedir la selección
            }

            System.out.println("Coches elegibles");

            for(int i = 0; i < elegibles.length; i++){
                System.out.println(i + " ---> " + elegibles[i]);
            }

            System.out.println("Introduzca número coche (sólo si está disponible)");
            int seleccion = KeyBoard.readInt();
            vehiculo = elegibles[seleccion];
            seleccionado = true;
        }
        return vehiculo;
    }

    public void listarAlquileres (){
        System.out.println("Listado de alquileres");
        for (Alquiler alq: alquileres){
            System.out.println(alq);
        }
        System.out.println();
    }

    public void listarVehiculos(){
        System.out.println("Listado de vehículos");
        for (Vehiculo veh: vehiculos){
            System.out.println(veh);
        }
        System.out.println();
    }


    public static void main(String [] args){
        GestorAlquileres app = new GestorAlquileres();
        app.menuPrincipal();
    }
}
