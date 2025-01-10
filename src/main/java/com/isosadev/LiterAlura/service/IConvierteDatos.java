package com.isosadev.LiterAlura.service;

/**
 * Interfaz que define el contrato para la conversión de datos, específicamente de JSON a objetos Java.
 */
public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

    /**
     * Método genérico para convertir una cadena JSON a un objeto de tipo T.
     *
     * @param json  La cadena JSON que se va a convertir.
     * @param clase La clase a la que se va a convertir el JSON. Se utiliza para determinar el tipo del objeto resultante.
     * @param <T>   Parámetro de tipo genérico que representa el tipo de la clase a la que se convierte el JSON.
     * @return      Un objeto de tipo T resultante de la conversión del JSON.
     */

}
