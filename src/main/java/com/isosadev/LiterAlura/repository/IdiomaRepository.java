package com.isosadev.LiterAlura.repository;

import com.isosadev.LiterAlura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad Idioma.
 * Extiende JpaRepository para heredar métodos de acceso a datos básicos (CRUD).
 */
public interface IdiomaRepository extends JpaRepository<Idioma, Long> {

    /**
     * Busca un idioma por su nombre.
     *
     * @param nombre El nombre del idioma a buscar.
     * @return Un Optional que contiene el idioma si se encuentra, o un Optional vacío si no se encuentra.
     */
    Optional<Idioma> findByNombre(String nombre);
}
