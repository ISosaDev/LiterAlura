package com.isosadev.LiterAlura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase que implementa la interfaz IConvierteDatos y se encarga de convertir datos JSON a objetos Java.
 */
public class ConvierteDatos implements IConvierteDatos{

    /**
     * Objeto ObjectMapper de la librería Jackson para realizar la conversión de JSON a objetos Java.
     * Se instancia una sola vez para ser reutilizado en las conversiones.
     */
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Método que convierte una cadena JSON a un objeto de la clase especificada.
     *
     * @param json  Cadena de texto en formato JSON que se va a convertir.
     * @param clase Clase a la cual se va a convertir el JSON. El tipo del objeto resultante será de esta clase.
     * @param <T>   Parámetro de tipo genérico que representa el tipo de la clase a la cual se convierte el JSON.
     * @return      Un objeto de tipo T resultante de la conversión del JSON.
     * @throws RuntimeException Si ocurre un error durante el procesamiento del JSON, como un formato incorrecto.
     */
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            // Intenta convertir la cadena JSON al objeto de la clase especificada usando objectMapper.readValue().
            return objectMapper.readValue(json,clase);
        } catch (JsonProcessingException e) {
            // Captura la excepción JsonProcessingException que se produce si hay un error al procesar el JSON (ej. formato inválido).
            // Lanza una RuntimeException envolviendo la excepción original para propagar el error hacia arriba.
            throw new RuntimeException(e);
        }
    }
}
