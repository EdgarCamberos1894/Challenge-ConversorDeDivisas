package org.camberos.api;

import com.google.gson.Gson;
import org.camberos.model.CurrencyDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

/**
 * Clase que proporciona métodos para interactuar con la API de conversión de divisas.
 */
public class CurrencyConverterAPI {

    private CurrencyDTO result;
    private final String API_KEY;

    /**
     * Constructor que carga la API key al crear una instancia de la clase.
     */
    public CurrencyConverterAPI() {
        API_KEY = loadApiKey();
    }

    /**
     * Método para convertir una cantidad de una divisa a otra.
     *
     * @param fromCurrency La divisa de origen.
     * @param toCurrency   La divisa de destino.
     * @param amount       La cantidad a convertir.
     * @return Un objeto CurrencyDTO que contiene el resultado de la conversión.
     */
    public CurrencyDTO convertCurrency(String fromCurrency, String toCurrency, String amount) {
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%s", API_KEY, fromCurrency, toCurrency, amount);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            result = gson.fromJson(response.body(), CurrencyDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Método para cargar la API key desde un archivo de configuración.
     *
     * @return La API key cargada desde el archivo de configuración.
     */
    private String loadApiKey() {
        String apiKey = "";
        try {
            InputStream input = new FileInputStream("src/main/resources/config.properties");
            Properties properties = new Properties();
            properties.load(input);
            apiKey = properties.getProperty("api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
}
