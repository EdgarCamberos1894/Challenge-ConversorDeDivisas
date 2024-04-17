package org.camberos.model;

import com.google.gson.annotations.SerializedName;

/**
 * Representa el objeto de transferencia de datos (DTO) para la información de conversión de divisas.
 */
public record CurrencyDTO(
        @SerializedName("result") String result,
        @SerializedName("base_code") String baseCode,
        @SerializedName("target_code") String targetCode,
        @SerializedName("conversion_rate") double conversionRate,
        @SerializedName("conversion_result") double conversionResult){

    /**
     * Construye un nuevo objeto CurrencyDTO con los atributos proporcionados.
     *
     * @param result            El resultado de la conversión de divisas.
     * @param baseCode          El código de la moneda base.
     * @param targetCode        El código de la moneda objetivo.
     * @param conversionRate    La tasa de conversión entre las monedas base y objetivo.
     * @param conversionResult  El resultado de la conversión.
     */
    public CurrencyDTO {}
}
