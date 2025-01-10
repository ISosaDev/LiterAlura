package com.isosadev.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record que representa a un autor de un libro, obtenido de una API externa.
 * Se utiliza para mapear la respuesta JSON a un objeto Java.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora cualquier propiedad desconocida en el JSON para evitar errores de deserialización.
public record Autores(

        /**
         * Nombre del autor.
         * Se utiliza la anotación @JsonAlias para mapear el campo "name" del JSON a esta propiedad.
         */
        @JsonAlias("name") String nombre,

        /**
         * Año de nacimiento del autor.
         * Se utiliza la anotación @JsonAlias para mapear el campo "birth_year" del JSON a esta propiedad.
         */
        @JsonAlias("birth_year") Integer añoDeNacimiento,

        /**
         * Año de muerte del autor.
         * Se utiliza la anotación @JsonAlias para mapear el campo "death_year" del JSON a esta propiedad.
         */
        @JsonAlias("death_year") Integer añoDeMuerte
) {
}
