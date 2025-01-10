package com.isosadev.LiterAlura.repository;

import com.isosadev.LiterAlura.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad Genero.
 * Extiende JpaRepository para heredar métodos de acceso a datos básicos (CRUD).
 */
public interface GeneroRepository extends JpaRepository<Genero, Long> {

    /**
     * Busca un género por su nombre.
     *
     * @param nombre El nombre del género a buscar.
     * @return Un Optional que contiene el género si se encuentra, o un Optional vacío si no se encuentra.
     */
    Optional<Genero> findByNombre(String nombre);
}
