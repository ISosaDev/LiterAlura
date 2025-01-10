package com.isosadev.LiterAlura.repository;

import com.isosadev.LiterAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad Autor.
 * Extiende JpaRepository para heredar métodos de acceso a datos básicos (CRUD).
 */
public interface AutorRepository extends JpaRepository<Autor,Long> {

    /**
     * Busca un autor por su nombre.
     *
     * @param nombre El nombre del autor a buscar.
     * @return Un Optional que contiene el autor si se encuentra, o un Optional vacío si no se encuentra.
     */
    Optional<Autor> findByNombre(String nombre);

    /**
     * Busca autores que estaban vivos en un año determinado.
     * Un autor se considera vivo si su año de muerte es nulo (aún vive) o si su año de muerte es posterior al año proporcionado.
     *
     * @param anio El año para el cual se buscan autores vivos.
     * @return Una lista de autores que cumplen la condición de estar vivos en el año especificado.
     */
    List<Autor> findByAñoDeMuerteIsNullOrAñoDeMuerteGreaterThan(int anio);

}
