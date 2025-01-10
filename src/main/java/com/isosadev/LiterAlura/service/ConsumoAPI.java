package com.isosadev.LiterAlura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que se encarga de consumir una API externa mediante solicitudes HTTP.
 */
public class ConsumoAPI {

    /**
     * Realiza una solicitud HTTP a la URL proporcionada y devuelve el cuerpo de la respuesta como una cadena.
     *
     * @param url La URL a la que se realizará la solicitud.
     * @return El cuerpo de la respuesta como una cadena (formato JSON generalmente).
     * @throws RuntimeException Si ocurre una excepción de E/S o interrupción durante la solicitud.
     */
    public String obtenerDatos(String url) {
        // Crea un nuevo cliente HTTP.
        HttpClient client = HttpClient.newHttpClient();

        // Crea una nueva solicitud HTTP a partir de la URL proporcionada.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // Define la URI de la solicitud.
                .build(); // Construye la solicitud.

        HttpResponse<String> response = null; // Inicializa la variable de respuesta.

        try {
            // Envía la solicitud HTTP y obtiene la respuesta.
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            // Captura y relanza una excepción de E/S si ocurre un error durante la comunicación.
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // Captura y relanza una excepción de interrupción si la solicitud es interrumpida.
            throw new RuntimeException(e);
        }

        // Obtiene el cuerpo de la respuesta como una cadena.
        String json = response.body();

        // Devuelve el cuerpo de la respuesta (JSON).
        return json;
    }
}