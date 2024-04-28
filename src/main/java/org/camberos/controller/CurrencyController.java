package org.camberos.controller;

import com.google.gson.Gson;
import org.camberos.model.api.CurrencyConverterAPI;
import org.camberos.model.CurrencyDTO;
import org.camberos.model.DatosJSON;

import java.io.FileReader;

public class CurrencyController {

    public static DatosJSON cargarDatosDivisasJSON(String ruta)  {
        try (FileReader reader = new FileReader(ruta)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, DatosJSON.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CurrencyDTO convertirDivisas(DatosJSON datos,int indiceDivisaOrigen, int indiceDivisaDestino, String cantidad) {
            String fromCurrency = datos.divisas()[indiceDivisaOrigen].codigo();
            String toCurrency = datos.divisas()[indiceDivisaDestino].codigo();

            CurrencyConverterAPI currency = new CurrencyConverterAPI();
            CurrencyDTO resultado = currency.convertCurrency(fromCurrency, toCurrency, cantidad);

            return resultado;
    }
}
