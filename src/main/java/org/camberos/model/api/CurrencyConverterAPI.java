package org.camberos.model.api;

import com.google.gson.Gson;
import org.camberos.model.Config;
import org.camberos.model.CurrencyDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que proporciona métodos para interactuar con la API de conversión de divisas.
 */
public class CurrencyConverterAPI {

    private CurrencyDTO result;
    private final String API_KEY = Config.getInstance().getApiKey();


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


}
