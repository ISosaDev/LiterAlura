package com.isosadev.LiterAlura.model;

import com.isosadev.LiterAlura.repository.GeneroRepository;
import com.isosadev.LiterAlura.repository.IdiomaRepository;
import jakarta.persistence.*;

import java.util.*;

@Entity // Anotación que marca esta clase como una entidad JPA (se mapea a una tabla en la base de datos)
@Table(name = "libros") // Anotación que especifica el nombre de la tabla en la base de datos
public class Libro {

    @Id // Anotación que marca este campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anotación que indica que la clave primaria se genera automáticamente por la base de datos (autoincremental)
    private Long id; // Identificador único del libro

    @Column(unique = true) // Anotación que asegura que el título del libro sea único en la base de datos
    private String titulo; // Título del libro

    @ManyToOne(fetch = FetchType.LAZY) // Anotación que define una relación Many-to-One con la entidad Autor. FetchType.LAZY indica carga perezosa (se carga el autor solo cuando se necesita)
    @JoinColumn(name = "autor_id", nullable = false) // Anotación que especifica la columna de clave foránea en la tabla libros (autor_id). nullable = false indica que el autor es obligatorio
    private Autor autor; // Autor del libro

    @ManyToMany(fetch = FetchType.LAZY) // Anotación que define una relación Many-to-Many con la entidad Genero. FetchType.LAZY indica carga perezosa
    @JoinTable(name = "libro_genero", // Anotación que especifica la tabla de unión para la relación Many-to-Many
            joinColumns = @JoinColumn(name = "libro_id"), // Columna de la tabla de unión que referencia a la tabla libros
            inverseJoinColumns = @JoinColumn(name = "genero_id")) // Columna de la tabla de unión que referencia a la tabla generos
    private Set<Genero> generos = new HashSet<>(); // Conjunto de géneros del libro

    @ManyToMany(fetch = FetchType.LAZY) // Anotación que define una relación Many-to-Many con la entidad Idioma. FetchType.LAZY indica carga perezosa
    @JoinTable(name = "libro_idioma", // Anotación que especifica la tabla de unión para la relación Many-to-Many
            joinColumns = @JoinColumn(name = "libro_id"), // Columna de la tabla de unión que referencia a la tabla libros
            inverseJoinColumns = @JoinColumn(name = "idioma_id")) // Columna de la tabla de unión que referencia a la tabla idiomas
    private Set<Idioma> idiomas = new HashSet<>(); // Conjunto de idiomas del libro

    private Integer descargas; // Número de descargas del libro

    public Libro() { // Constructor vacío (necesario para JPA)
    }

    // Constructor que recibe un objeto DatosLibros y los repositorios de Genero e Idioma para crear un nuevo libro
    public Libro(DatosLibros datosLibros, GeneroRepository generoRepository, IdiomaRepository idiomaRepository) {
        this.titulo = datosLibros.titulo(); // Inicializa el título del libro con el valor del objeto DatosLibros
        this.descargas = datosLibros.descargas(); // Inicializa el número de descargas del libro

        this.generos = new HashSet<>(); // Inicializa el conjunto de géneros
        if (datosLibros.generos() != null && !datosLibros.generos().isEmpty()) { // Verifica si la lista de géneros en datosLibros no es nula ni vacía
            for (String generoNombre : datosLibros.generos()) { // Itera sobre los nombres de los géneros en datosLibros
                Optional<Genero> generoOptional = generoRepository.findByNombre(generoNombre); // Busca el género en la base de datos por su nombre
                Genero genero = generoOptional.orElseGet(() -> { // Si el género no existe en la base de datos, lo crea
                    Genero nuevoGenero = new Genero(generoNombre); // Crea un nuevo objeto Genero
                    return generoRepository.save(nuevoGenero); // Guarda el nuevo género en la base de datos y lo retorna
                });
                this.generos.add(genero); // Añade el género (existente o nuevo) al conjunto de géneros del libro
            }
        }

        this.idiomas = new HashSet<>(); // Inicializa el conjunto de idiomas
        if (datosLibros.idiomas() != null && !datosLibros.idiomas().isEmpty()) { // Verifica si la lista de idiomas en datosLibros no es nula ni vacía
            for (String idiomaNombre : datosLibros.idiomas()) { // Itera sobre los nombres de los idiomas en datosLibros
                Optional<Idioma> idiomaOptional = idiomaRepository.findByNombre(idiomaNombre); // Busca el idioma en la base de datos por su nombre
                Idioma idioma = idiomaOptional.orElseGet(() -> { // Si el idioma no existe en la base de datos, lo crea
                    Idioma nuevoIdioma = new Idioma(idiomaNombre); // Crea un nuevo objeto Idioma
                    return idiomaRepository.save(nuevoIdioma); // Guarda el nuevo idioma en la base de datos y lo retorna
                });
                this.idiomas.add(idioma); // Añade el idioma (existente o nuevo) al conjunto de idiomas del libro
            }
        }
    }

    // Getters y setters (necesarios para JPA)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }

    public Set<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Set<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public boolean equals(Object o) { // Sobreescritura del método equals para comparar objetos Libro por su título
        if (this == o) return true; // Si los objetos son el mismo, retorna true
        if (o == null || getClass() != o.getClass()) return false; // Si el objeto es nulo o no es de la misma clase, retorna false
        Libro libro = (Libro) o; // Castea el objeto a Libro
        return Objects.equals(titulo, libro.titulo); // Compara los títulos de los libros
    }

    @Override
    public int hashCode() { // Sobreescritura del método hashCode para generar un código hash basado en el título
        return Objects.hash(titulo); // Genera el código hash usando el título
    }
}


