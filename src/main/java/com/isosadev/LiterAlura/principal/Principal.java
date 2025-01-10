package com.isosadev.LiterAlura.principal;

import com.isosadev.LiterAlura.model.*;
import com.isosadev.LiterAlura.repository.AutorRepository;
import com.isosadev.LiterAlura.repository.GeneroRepository;
import com.isosadev.LiterAlura.repository.IdiomaRepository;
import com.isosadev.LiterAlura.repository.LibroRepository;
import com.isosadev.LiterAlura.service.ConsumoAPI;
import com.isosadev.LiterAlura.service.ConvierteDatos;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

public class Principal {

    private final Scanner teclado = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario desde la consola
    private final ConsumoAPI consumoApi = new ConsumoAPI(); // Crea un objeto para consumir la API
    private final String URL_BASE = "https://gutendex.com/books/?search="; // Define la URL base de la API
    private final ConvierteDatos conversor = new ConvierteDatos(); // Crea un objeto para convertir los datos JSON de la API a objetos Java
    private final LibroRepository repositorio; // Repositorio para la entidad Libro
    private final AutorRepository autorRepository; // Repositorio para la entidad Autor
    private final GeneroRepository generoRepository; // Repositorio para la entidad Género
    private final IdiomaRepository idiomaRepository; // Repositorio para la entidad Idioma

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository, GeneroRepository generoRepository, IdiomaRepository idiomaRepository) {
        this.repositorio = libroRepository; // Inicializa el repositorio de libros
        this.autorRepository = autorRepository; // Inicializa el repositorio de autores
        this.generoRepository = generoRepository; // Inicializa el repositorio de géneros
        this.idiomaRepository = idiomaRepository; // Inicializa el repositorio de idiomas
    }

    public void menuUsuario() {
        boolean continuar = true; // Variable para controlar el bucle del menú

        while (continuar) { // Bucle principal del menú
            System.out.println("\nElija el número correspondiente a la opción deseada:");
            System.out.println("1- Buscar libro por titulo");
            System.out.println("2- Listar libros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos en un determinado año");
            System.out.println("5- Listar libros por idioma");
            System.out.println("0- Salir\n");

            String input = teclado.nextLine(); // Lee la entrada del usuario

            try {
                int opcion = Integer.parseInt(input); // Intenta convertir la entrada a un entero

                switch (opcion) { // Estructura switch para manejar las opciones del menú
                    case 1:
                        buscarLibroPorTitulo(); // Llama al método para buscar un libro por título
                        break;
                    case 2:
                        listarLibrosRegistrados(); // Llama al método para listar los libros registrados
                        break;
                    case 3:
                        listarAutoresRegistrados(); // Llama al método para listar los autores registrados
                        break;
                    case 4:
                        listarAutoresVivosPorAnio(); // Llama al método para listar los autores vivos en un año determinado
                        break;
                    case 5:
                        listarLibrosPorIdioma(); // Llama al método para listar los libros por idioma
                        break;
                    case 0:
                        continuar = false; // Cambia la variable para salir del bucle
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente."); // Mensaje para opciones no válidas
                }
            } catch (NumberFormatException e) { // Captura la excepción si la entrada no es un número
                System.out.println("Debe elegir una opción válida (número).");
            }
        }
    }



    @Transactional // Anotación que indica que este método se ejecuta dentro de una transacción.
    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro:");
        String palabrasClave = teclado.nextLine(); // Lee la entrada del usuario (palabras clave de búsqueda).

        // Busca libros en la base de datos cuyo título contenga las palabras clave.
        List<Libro> librosEncontradosBD = repositorio.buscarPorTituloConteniendo(palabrasClave);

        // Verifica si se encontraron libros en la base de datos.
        if (!librosEncontradosBD.isEmpty()) {
            System.out.println("\nLibros encontrados en la base de datos:");
            // Carga todas las relaciones necesarias para evitar LazyInitializationException
            List<Libro> librosConRelaciones = repositorio.findAll();
            // Filtra los libros encontrados en la base de datos dentro de la lista con relaciones cargadas
            librosConRelaciones.stream()
                    .filter(libro -> librosEncontradosBD.contains(libro))
                    .forEach(this::mostrarDetallesLibroDesdeLibro); // Muestra los detalles de cada libro encontrado.
        } else {
            // Si no se encontraron libros en la base de datos, se realiza una búsqueda en una API externa.
            String url = URL_BASE + palabrasClave.replace(" ", "%20"); // Construye la URL para la API, reemplazando espacios con %20.
            var json = consumoApi.obtenerDatos(url); // Obtiene los datos JSON de la API.
            var resultados = conversor.obtenerDatos(json, ResultadosAPI.class); // Convierte el JSON a un objeto ResultadosAPI.

            System.out.println("Total de coincidencias: " + resultados.coincidencias()); // Imprime el número total de coincidencias encontradas por la API.

            DatosLibros libroCoincidente = null; // Inicializa una variable para almacenar el libro coincidente.
            // Itera sobre los resultados de la API para encontrar una coincidencia exacta o parcial en el título.
            for (DatosLibros libro : resultados.resultados()) {
                if (libro.titulo().toLowerCase().contains(palabrasClave.toLowerCase())) {
                    libroCoincidente = libro; // Asigna el libro coincidente.
                    break; // Sale del bucle una vez que se encuentra una coincidencia.
                }
            }

            // Verifica si se encontró un libro coincidente en la API.
            if (libroCoincidente != null) {
                mostrarDetallesLibro(libroCoincidente); // Muestra los detalles del libro encontrado en la API.

                // Procesa la información del autor, si existe.
                if (libroCoincidente.autores() != null && !libroCoincidente.autores().isEmpty()) {
                    Autores autorAPI = libroCoincidente.autores().get(0); // Obtiene el primer autor de la lista (asumiendo que solo se usa el primero).

                    // Busca el autor en la base de datos.
                    Optional<Autor> autorOptional = autorRepository.findByNombre(autorAPI.nombre());
                    Autor autor;
                    // Si el autor existe, lo recupera.
                    if (autorOptional.isPresent()) {
                        autor = autorOptional.get();
                    } else {
                        // Si el autor no existe, lo crea y lo guarda en la base de datos.
                        autor = new Autor(autorAPI.nombre(), autorAPI.añoDeNacimiento(), autorAPI.añoDeMuerte());
                        autor = autorRepository.save(autor);
                    }

                    // Crea un nuevo libro a partir de los datos de la API y lo asocia con el autor.
                    Libro libro = new Libro(libroCoincidente, generoRepository, idiomaRepository);
                    libro.setAutor(autor);

                    try {
                        // Intenta guardar el libro en la base de datos.
                        repositorio.save(libro);
                        System.out.println("Libro guardado correctamente.");
                    } catch (DataIntegrityViolationException e) {
                        // Captura la excepción en caso de que ya exista un libro con los mismos datos (violación de la integridad de la base de datos).
                        System.out.println("Error al guardar el libro (posible duplicado, aunque ya se verificó): " + e.getMessage());
                    }
                }
            } else {
                // Si no se encontraron resultados ni en la base de datos ni en la API.
                System.out.println("No se encontraron resultados para: " + palabrasClave);
            }
        }
    }


    /**
     * Muestra los detalles de un libro obtenidos directamente desde la API.
     * Este método formatea la información del libro para mostrarla en la consola.
     * Maneja casos donde la información del autor, géneros o idiomas no está disponible.
     *
     * @param libro Un objeto DatosLibros que contiene la información del libro obtenida de la API.
     */
    @Transactional
    private void mostrarDetallesLibro(DatosLibros libro) {
        System.out.println("\nTítulo: " + libro.titulo()); // Imprime el título del libro precedido de un salto de línea.

        // Verifica si la lista de autores existe (no es nula) y si no está vacía.
        if (libro.autores() != null && !libro.autores().isEmpty()) {
            Autores primerAutor = libro.autores().get(0); // Obtiene el primer autor de la lista. Se asume que hay al menos un autor si la lista no está vacía.
            System.out.println("Autor: " + primerAutor.nombre()); // Imprime el nombre del autor.
            System.out.println("Año de nacimiento: " + primerAutor.añoDeNacimiento()); // Imprime el año de nacimiento del autor.
            System.out.println("Año de fallecimiento: " + primerAutor.añoDeMuerte()); // Imprime el año de fallecimiento del autor.
        } else {
            System.out.println("Autor: No disponible"); // Si la lista de autores es nula o vacía, imprime "Autor: No disponible".
        }

        // Manejo de la lista de géneros:
        // Se utiliza un operador ternario para evitar NullPointerExceptions.
        // (condicion) ? (valor_si_verdadero) : (valor_si_falso)
        // Si libro.generos() no es nulo, se une la lista de géneros en una cadena separada por comas y espacios.
        // Si libro.generos() es nulo, se imprime "No disponible".
        System.out.println("Géneros: " + (libro.generos() != null ? String.join(", ", libro.generos()) : "No disponible"));

        // Manejo de la lista de idiomas (misma lógica que para géneros):
        System.out.println("Idiomas: " + (libro.idiomas() != null ? String.join(", ", libro.idiomas()) : "No disponible"));

        System.out.println("Descargas: " + libro.descargas()); // Imprime el número de descargas del libro.
        System.out.println(); // Imprime una línea en blanco para separar la información de diferentes libros.
    }


    /**
     * Lista todos los libros registrados en la base de datos.
     * Este método primero verifica si hay libros registrados. Si no hay ninguno,
     * muestra un mensaje indicándolo. En caso contrario, muestra un encabezado
     * y luego los detalles de cada libro utilizando el método mostrarDetallesLibroDesdeLibro().
     */
    @Transactional // Anotación que indica que este método se ejecuta dentro de una transacción de base de datos.
    private void listarLibrosRegistrados() {
        long totalLibros = repositorio.count(); // Obtiene la cantidad total de libros registrados en la base de datos utilizando el método count() del repositorio.

        System.out.println("Total de libros registrados: " + totalLibros); // Imprime en la consola la cantidad total de libros registrados.

        // Condición para verificar si no hay libros registrados.
        if (totalLibros == 0) {
            System.out.println("No hay libros registrados en la base de datos."); // Imprime un mensaje informativo si no hay libros.
            return; // Sale del método inmediatamente usando la palabra reservada 'return' para evitar que se ejecute el resto del código. Esto previene posibles excepciones si se intenta acceder a una lista vacía.
        }

        System.out.println("Libros registrados:"); // Imprime un encabezado indicando que se mostrarán los libros registrados. Esta línea solo se ejecuta si hay al menos un libro en la base de datos.

        List<Libro> librosEnBaseDeDatos = repositorio.findAll(); // Obtiene una lista con todos los libros almacenados en la base de datos utilizando el método findAll() del repositorio.

        // Bucle 'for' que itera sobre la lista de libros obtenida de la base de datos.
        for (Libro libro : librosEnBaseDeDatos) {
            mostrarDetallesLibroDesdeLibro(libro); // Llama al método mostrarDetallesLibroDesdeLibro() para mostrar los detalles de cada libro.
        }

        System.out.println("-----------------------------");
    }

    /**
     * Muestra los detalles de un libro que ya está almacenado en la base de datos.
     * Este método formatea la información del libro para mostrarla en la consola,
     * incluyendo el título, autor, géneros, idiomas y número de descargas.
     * Maneja el caso en que el libro no tenga un autor asociado.
     *
     * @param libro El objeto Libro del cual se mostrarán los detalles.
     */
    @Transactional
    private void mostrarDetallesLibroDesdeLibro(Libro libro) {
        System.out.println("\nTítulo: " + libro.getTitulo()); // Imprime el título del libro, precedido por un salto de línea para mejor formato.

        // Verifica si el libro tiene un autor asociado (es decir, si el autor no es nulo).
        if (libro.getAutor() != null) {
            System.out.println("Autor: " + libro.getAutor().getNombre()); // Imprime el nombre del autor.
            System.out.println("Año de nacimiento: " + libro.getAutor().getAñoDeNacimiento()); // Imprime el año de nacimiento del autor.
            System.out.println("Año de fallecimiento: " + libro.getAutor().getAñoDeMuerte()); // Imprime el año de fallecimiento del autor.
        } else {
            System.out.println("Autor: No disponible"); // Si el libro no tiene un autor asociado, imprime "Autor: No disponible".
        }

        // Manejo de la lista de géneros:
        // Se utiliza un Stream para procesar la lista de géneros.
        // .map(Genero::getNombre) transforma cada objeto Genero en su nombre (un String).
        // .collect(Collectors.joining(", ")) une todos los nombres de los géneros en una sola cadena, separados por comas y espacios.
        String generosStr = libro.getGeneros().stream()
                .map(Genero::getNombre) // Obtiene el nombre de cada género. Referencia al método getNombre de la clase Genero.
                .collect(java.util.stream.Collectors.joining(", ")); // Une los nombres con ", ". Usa un Collector para juntar los elementos del Stream en una cadena.

        // Imprime los géneros. Si la cadena generada está vacía (es decir, no hay géneros), imprime "No disponible".
        System.out.println("Géneros: " + (generosStr.isEmpty() ? "No disponible" : generosStr)); // Operador ternario para manejar el caso de lista vacía.

        // Manejo de la lista de idiomas (misma lógica que para géneros):
        String idiomasStr = libro.getIdiomas().stream()
                .map(Idioma::getNombre) // Obtiene el nombre de cada idioma. Referencia al método getNombre de la clase Idioma.
                .collect(java.util.stream.Collectors.joining(", ")); // Une los nombres con ", ".

        System.out.println("Idiomas: " + (idiomasStr.isEmpty() ? "No disponible" : idiomasStr)); // Operador ternario para manejar el caso de lista vacía.

        System.out.println("Descargas: " + libro.getDescargas()); // Imprime el número de descargas del libro.
        System.out.println("-------------------------------------"); // Imprime un salto de línea para separar la información de diferentes libros.
    }



    /**
     * Lista todos los autores registrados en la base de datos.
     * Este método recupera todos los autores almacenados en la base de datos
     * y muestra su información (nombre, año de nacimiento y año de fallecimiento)
     * en la consola. Primero verifica si existen autores registrados y, en caso
     * contrario, muestra un mensaje informativo.
     */
    @Transactional // Anotación que indica que este método se ejecuta dentro de una transacción de base de datos.
    private void listarAutoresRegistrados() {
        long totalAutores = autorRepository.count(); // Obtiene la cantidad total de autores registrados en la base de datos usando el método count() del repositorio.

        System.out.println("\nTotal de autores registrados: " + totalAutores); // Imprime en la consola la cantidad total de autores registrados.

        // Verifica si no hay autores registrados en la base de datos.
        if (totalAutores == 0) {
            System.out.println("No se han registrado autores.\n"); // Imprime un mensaje informativo si no hay autores registrados. El \n añade un salto de línea para mejor formato.
            return; // Sale del método inmediatamente usando la palabra reservada 'return'. Esto previene la ejecución innecesaria del resto del código y posibles excepciones si se intenta acceder a una lista vacía.
        }

        System.out.println("\nAutores registrados:\n"); // Imprime un encabezado indicando que se mostrarán los autores registrados. El \n añade un salto de línea para mejor formato. Esta línea solo se ejecuta si hay al menos un autor en la base de datos.

        List<Autor> autores = autorRepository.findAll(); // Obtiene una lista con todos los autores almacenados en la base de datos utilizando el método findAll() del repositorio.

        // Bucle 'for' que itera sobre la lista de autores obtenida de la base de datos.
        for (Autor autor : autores) {
            System.out.println("Nombre: " + autor.getNombre()); // Imprime el nombre del autor.
            System.out.println("Año de nacimiento: " + autor.getAñoDeNacimiento()); // Imprime el año de nacimiento del autor.
            System.out.println("Año de fallecimiento: " + autor.getAñoDeMuerte()); // Imprime el año de fallecimiento del autor.
            System.out.println("---------------------------------------"); // Imprime un salto de línea después de cada autor para mejorar la legibilidad.
        }
    }

    /**
     * Lista los autores que estaban vivos en un año específico ingresado por el usuario.
     * Un autor se considera vivo en un año si su año de muerte es nulo (aún vive) o si su año de muerte es posterior al año ingresado.
     */
    private void listarAutoresVivosPorAnio() {
        boolean entradaValida = false; // Variable booleana para controlar la validez de la entrada del usuario. Se inicializa en falso.
        int anio = 0; // Variable entera para almacenar el año ingresado por el usuario.

        // Bucle while que se ejecuta mientras la entrada no sea válida.
        while (!entradaValida) {
            System.out.println("Ingrese el año para listar autores vivos:"); // Pide al usuario que ingrese un año.
            String inputAnio = teclado.nextLine(); // Lee la entrada del usuario como una cadena.

            try {
                anio = Integer.parseInt(inputAnio); // Intenta convertir la cadena ingresada a un entero.
                if (anio > 0) { // Valida que el año sea un número positivo.
                    entradaValida = true; // Si la conversión es exitosa y el año es positivo, la entrada es válida y se sale del bucle.
                } else {
                    System.out.println("Ingrese un año válido (número positivo)."); // Si el año no es positivo, se muestra un mensaje de error.
                }

            } catch (NumberFormatException e) { // Captura la excepción NumberFormatException si la entrada no es un número entero válido.
                System.out.println("Entrada no válida. Debe ingresar un número entero."); // Muestra un mensaje de error indicando que se debe ingresar un número entero.
            }
        }

        // Utiliza el método del repositorio `findByAñoDeMuerteIsNullOrAñoDeMuerteGreaterThan` para obtener una lista de autores vivos en el año especificado.
        // Este método busca autores cuyo año de muerte es nulo (aún viven) O cuyo año de muerte es mayor que el año ingresado.
        List<Autor> autoresVivos = autorRepository.findByAñoDeMuerteIsNullOrAñoDeMuerteGreaterThan(anio);

        // Verifica si la lista de autores vivos está vacía.
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + "."); // Muestra un mensaje si no se encontraron autores vivos en el año especificado.
        } else {
            System.out.println("Autores vivos en el año " + anio + ":"); // Muestra un encabezado indicando los autores vivos en el año especificado.
            // Bucle for-each para iterar sobre la lista de autores vivos.
            for (Autor autor : autoresVivos) {
                System.out.println("- " + autor.getNombre()); // Muestra solo el nombre del autor para mayor concisión.
            }
            System.out.println("----------------------------"); // Imprime un salto de línea al final para mejorar el formato.
        }
    }

    private final Set<String> idiomasValidos = new HashSet<>(Arrays.asList("en", "es", "fr", "de", "it", "pt")); // Lista de idiomas válidos

    /**
     * Permite al usuario listar libros por idioma.
     * El usuario ingresa un código de idioma (ej. "en" para inglés, "es" para español).
     * Si el usuario presiona Enter sin ingresar nada, se muestran estadísticas generales por idioma.
     * Valida la entrada del usuario contra una lista de idiomas válidos.
     */
    private void listarLibrosPorIdioma() {
        boolean entradaValida = false; // Variable booleana para controlar si la entrada del usuario es válida. Se inicializa en falso.
        String idioma = ""; // Variable para almacenar el código de idioma ingresado por el usuario.

        // Bucle while que continúa hasta que el usuario ingresa un código de idioma válido o presiona Enter.
        while (!entradaValida) {
            System.out.println("Ingrese el codigo de idioma deseado:\n" + // Muestra las opciones de idioma al usuario.
                    "en para Inglés\n" +
                    "es para Español\n" +
                    "fr para francés\n" +
                    "de para Alemán\n" +
                    "it para Italiano\n" +
                    "pt para portugués\n"+
                    "o presione Enter para ver estadísticas generales:" // Opción para ver estadísticas generales.
            );

            String idiomaInput = teclado.nextLine(); // Lee la entrada del usuario desde la consola.

            // Verifica si el usuario presionó Enter (es decir, si la entrada está vacía).
            if (idiomaInput.isEmpty()) {
                mostrarEstadisticasPorIdioma(); // Llama al método para mostrar las estadísticas generales por idioma.
                return; // Sale del método inmediatamente después de mostrar las estadísticas.
            } else {
                idioma = idiomaInput.toLowerCase(); // Convierte la entrada a minúsculas para que la comparación no distinga entre mayúsculas y minúsculas.
                // Verifica si el código de idioma ingresado está presente en el conjunto de idiomas válidos (idiomasValidos).
                if (idiomasValidos.contains(idioma)) {
                    entradaValida = true; // Si el idioma es válido, se establece entradaValida a true para salir del bucle while.
                }
            }
        }

        mostrarLibrosPorIdiomaEspecifico(idioma); // Llama al método para mostrar los libros del idioma especificado. Esta línea solo se ejecuta si el usuario ingresó un código de idioma válido y no presionó Enter.
    }

    /**
     * Muestra las estadísticas generales de la cantidad de libros por idioma.
     * Itera sobre una lista predefinida de códigos de idioma y consulta la base de datos
     * para obtener la cantidad de libros en cada idioma. Luego, imprime los resultados
     * en la consola.
     */
    private void mostrarEstadisticasPorIdioma() {
        String[] idiomas = {"en", "es", "fr", "de", "it", "pt"}; // Array de cadenas que contiene los códigos de idioma para los que se mostrarán las estadísticas.
        System.out.println("-----------------------------------" +
                "\nEstadísticas Generales de Libros por Idioma:"); // Imprime un encabezado para las estadísticas, precedido por un salto de línea.

        // Bucle for-each que itera sobre el array de idiomas.
        for (String idioma : idiomas) {
            // Consulta la base de datos utilizando el método findByidiomas_nombre(idioma) del repositorio.
            // Este método busca libros cuyo idioma coincida con el código de idioma actual en el bucle.
            // .size() obtiene la cantidad de libros encontrados para ese idioma.
            long cantidad = repositorio.findByidiomas_nombre(idioma).size();

            System.out.println("Libros en " + idioma + ": " + cantidad+"\n"); // Imprime la cantidad de libros encontrados para el idioma actual.
        }
        System.out.println("--------------------------"); // Imprime un salto de línea al final para mejorar el formato de la salida.
    }

    /**
     * Muestra los libros que pertenecen a un idioma específico.
     * Este método recibe un código de idioma como parámetro y busca en la base de datos
     * los libros que tienen ese idioma asociado. Luego, muestra los títulos de los libros encontrados.
     *
     * @param idioma El código del idioma para el cual se listarán los libros (ej. "en", "es", "fr").
     */
    private void mostrarLibrosPorIdiomaEspecifico(String idioma) {
        // Consulta la base de datos utilizando el método findByidiomas_nombre(idioma) del repositorio.
        // Este método busca libros cuyo idioma coincida con el código de idioma proporcionado.
        List<Libro> librosPorIdioma = repositorio.findByidiomas_nombre(idioma);

        int cantidadEncontrada = librosPorIdioma.size(); // Obtiene la cantidad de libros encontrados en el idioma especificado.

        // Verifica si no se encontraron libros para el idioma especificado.
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma); // Imprime un mensaje indicando que no se encontraron libros en ese idioma.
        } else {
            System.out.println("Se encontraron " + cantidadEncontrada + " libros en " + idioma + ":"); // Imprime un mensaje indicando la cantidad de libros encontrados en el idioma especificado.
            // Bucle for-each para iterar sobre la lista de libros encontrados.
            for (Libro libro : librosPorIdioma) {
                System.out.println("- " + libro.getTitulo()); // Imprime el título de cada libro encontrado, precedido por un guión para mejor formato.
            }
            System.out.println(); // Imprime un salto de línea al final para mejorar el formato de la salida.
        }
    }


}