package com.isosadev.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Record que representa los datos de un libro obtenidos de una API externa.
 * Se utiliza para mapear la respuesta JSON a un objeto Java.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora cualquier propiedad desconocida en el JSON para evitar errores de deserialización.
public record DatosLibros(

        /**
         * Título del libro.
         * Se utiliza la anotación @JsonAlias para mapear el campo "title" del JSON a esta propiedad.
         */
        @JsonAlias("title") String titulo,

        /**
         * Lista de autores del libro.
         * Se utiliza la anotación @JsonProperty para mapear el campo "authors" del JSON a esta propiedad.
         */
        @JsonProperty("authors") List<Autores> autores,

        /**
         * Lista de géneros del libro.
         * Se utiliza la anotación @JsonAlias para mapear el campo "subjects" del JSON a esta propiedad.
         */
        @JsonAlias("subjects") List<String> generos,

        /**
         * Lista de idiomas del libro.
         * Se utiliza la anotación @JsonAlias para mapear el campo "languages" del JSON a esta propiedad.
         */
        @JsonAlias("languages") List<String> idiomas,

        /**
         * Cantidad de descargas del libro.
         * Se utiliza la anotación @JsonAlias para mapear el campo "download_count" del JSON a esta propiedad.
         */
        @JsonAlias("download_count") Integer descargas){

}