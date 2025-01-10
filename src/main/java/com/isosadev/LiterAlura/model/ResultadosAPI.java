package com.isosadev.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
/**
 * Record que representa la respuesta de la API de búsqueda de libros.
 * Contiene el número de coincidencias y la lista de resultados (libros).
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propiedades desconocidas en el JSON para evitar errores de deserialización.
public record ResultadosAPI(

        /**
         * Número total de coincidencias encontradas en la búsqueda.
         * Mapeado desde el campo "count" del JSON.
         */
        @JsonAlias("count") Integer coincidencias,

        /**
         * Lista de libros que coinciden con la búsqueda.
         * Cada elemento de la lista es un objeto DatosLibros.
         * Mapeado desde el campo "results" del JSON.
         */
        @JsonAlias("results") List<DatosLibros> resultados

) {

}
