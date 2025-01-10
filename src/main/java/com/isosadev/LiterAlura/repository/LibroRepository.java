package com.isosadev.LiterAlura.repository;

import com.isosadev.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    /**
     * Busca un libro por su título exacto.
     *
     * @param nombreLibro El título del libro a buscar.
     * @return Un Optional que contiene el libro si se encuentra, o un Optional vacío si no se encuentra.
     */
    Optional<Libro> findByTitulo(String nombreLibro);

    /**
     * Busca libros cuyo título contenga una cadena específica, ignorando mayúsculas y minúsculas.
     *
     * @param titulo La cadena que debe estar contenida en el título del libro.
     * @return Una lista de libros cuyos títulos contienen la cadena especificada.
     */
    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Libro> buscarPorTituloConteniendo(@Param("titulo") String titulo);

    /**
     * Obtiene todos los libros, cargando también las relaciones con autor, géneros e idiomas.
     * Utiliza @EntityGraph para evitar problemas de LazyInitializationException.
     *
     * @return Una lista de todos los libros con sus relaciones cargadas.
     */
    @EntityGraph(attributePaths = {"autor", "generos", "idiomas"})
    List<Libro> findAll();

    /**
     * Busca libros por el nombre del idioma.
     *
     * @param nombreIdioma El nombre del idioma de los libros a buscar.
     * @return Una lista de libros que están en el idioma especificado.
     */
    List<Libro> findByidiomas_nombre(String nombreIdioma);
}