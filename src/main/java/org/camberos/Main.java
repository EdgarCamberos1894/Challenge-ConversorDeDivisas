package org.camberos;

import org.camberos.api.CurrencyConverterAPI;
import org.camberos.model.CurrencyDTO;
import org.camberos.view.InterfazUsuario;

import java.util.Map;
import java.util.Scanner;

/**
 * Esta clase representa el programa principal que ejecuta el conversor de divisas.
 */
public class Main {

    public static void main(String[] args) {
        // Se inicia un hilo para la interfaz de usuario
        Thread ventanaThread = new Thread(Main::initInterface);
        ventanaThread.start();

        Scanner scanner = new Scanner(System.in);
        boolean shouldExit;

        // Bucle principal del programa
        do {
            System.out.println(MAIN_MENU);
            String option = scanner.nextLine();
            shouldExit = processOption(option, scanner, ventanaThread);
        } while (!shouldExit);

        scanner.close();

    }

    /**
     * Procesa la opción elegida por el usuario.
     * @param option La opción elegida por el usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @param ventanaThread El hilo de la interfaz de usuario.
     * @return true si el programa debe salir, false en caso contrario.
     */
    private static boolean processOption(String option, Scanner scanner, Thread ventanaThread) {
        switch (option) {
            case "1", "2", "3", "4", "5", "6" -> {  // Casos para las opciones de conversión de divisas
                System.out.println(convertCurrency(option, scanner));
                return askForAnotherOperation(scanner, ventanaThread);
            }
            case "7" -> {  // Caso para salir del programa
                System.out.println(farewellPhrase);
                closeInterface(ventanaThread);
                return true;
            }
            case "8" -> { //Caso para mostrar la interfaz grafica
                showUserInterface(ventanaThread);
                return true;
            }
            default -> {  // Caso para opciones inválidas
                System.out.println(invalidOptionMessage);
                return false;
            }
        }
    }

    /**
     * Convierte la cantidad de una divisa a otra.
     * @param option La opción elegida por el usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return El resultado de la conversión de divisas como una cadena de texto.
     */
    private static String convertCurrency(String option, Scanner scanner) {
        String amount = getValidAmount(scanner);
        CurrencyOption currencyOption = CURRENCY_OPTIONS.get(option);

        CurrencyConverterAPI currency = new CurrencyConverterAPI();
        CurrencyDTO result = currency.convertCurrency(currencyOption.fromCurrency(), currencyOption.toCurrency(), amount);

        return String.format("""
                           ******************************
                                      Resultado
                                     %.2f (%s)
                           ******************************
           """, result.conversionResult(), result.targetCode());
    }

    /**
     * Obtiene una cantidad válida de divisas ingresada por el usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return La cantidad válida de divisas.
     */
    private static String getValidAmount(Scanner scanner) {
        String amount;
        boolean isValid;

        do {
            System.out.println("      *******  Ingrese el valor que deseas convertir ****** ");
            amount = scanner.nextLine();

            try {
                isValid = Double.parseDouble(amount) >= 0;
            } catch (NumberFormatException e) {
                isValid = false;
            }

            if (!isValid) {
                System.out.println("      ******** Ingrese un número válido y positivo. *******");
            }
        } while (!isValid);

        return amount;
    }

    /**
     * Pide al usuario que elija si desea realizar otra operación.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return true si el usuario desea salir, false si desea realizar otra operación.
     */
    private static boolean askForAnotherOperation(Scanner scanner, Thread ventanaThread) {
        System.out.println(askForAnotherOperationMessage);
        String response = scanner.nextLine();

        while (!response.equals("1") && !response.equals("2")) {
            System.out.println(invalidOptionMessage);
            response = scanner.nextLine();
        }

        if (response.equals("2")) {
            closeInterface(ventanaThread);
            System.out.println(farewellPhrase);
            return true;
        }
        return false;
    }

    /**
     * Inicializa la interfaz de usuario.
     */
    private static void initInterface() {
        interfazUsuario = new InterfazUsuario();
        interfazUsuario.setVisible(false);
    }

    /**
     * Muestra la interfaz de usuario.
     * @param ventanaThread El hilo de la interfaz de usuario.
     */
    private static void showUserInterface(Thread ventanaThread) {
        System.out.println(loadingMessage);
        verifyInterfaceInMemory(ventanaThread);
        interfazUsuario.setVisible(true);
        interfazUsuario.setLocationRelativeTo(null);
    }

    /**
     * Cierra la interfaz de usuario.
     * @param ventanaThread El hilo de la interfaz de usuario.
     */
    private static void closeInterface(Thread ventanaThread) {
        verifyInterfaceInMemory(ventanaThread);
        interfazUsuario.dispose();
    }

    /**
     * Verifica si la interfaz de usuario está en memoria.
     * @param ventanaThread El hilo de la interfaz de usuario.
     */
    private static void verifyInterfaceInMemory(Thread ventanaThread) {
        try {
            ventanaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Variables y mensajes
    private static final String invalidOptionMessage = "     ******** Opción no válida, vuelva a intentarlo ********";
    private static final String farewellPhrase = "            *********** ¡Hasta luego! *************";
    private static final String loadingMessage = """
             ************************************************
                  Cargando Interfaz, por favor espere...
             ************************************************
    """;
    private static final String askForAnotherOperationMessage = """
            ****************************************************
                     ¿Desea realizar otra operación?
                            1: Sí      2: No
            *****************++*********************************
    """;
    private static final String MAIN_MENU = """
        **********************************************************
                Sea Bienvenido/a al Conversor de Divisas
                                                                     
            1) Dólar (USD)           ==>  Peso Mexicano (MXN)
            2) Peso Mexicano (MXN)   ==>  Dólar (USD)
            3) Dólar (USD)           ==>  Real Brasileño (BRL)
            4) Real Brasileño (BRL)  ==>  Dólar (USD)
            5) Dólar (USD)           ==>  Peso Colombiano (COP)
            6) Peso Colombiano (COP) ==>  Dólar (USD)
            7) Salir
                                                                 
            8) Interfaz Gráfica
                                                                  
                             Elija una opción
        **********************************************************
    """;
    private static final Map<String, CurrencyOption> CURRENCY_OPTIONS = Map.of(
            "1", new CurrencyOption("USD", "MXN"),
            "2", new CurrencyOption("MXN", "USD"),
            "3", new CurrencyOption("USD", "BRL"),
            "4", new CurrencyOption("BRL", "USD"),
            "5", new CurrencyOption("USD", "COP"),
            "6", new CurrencyOption("COP", "USD")
    );

    private static InterfazUsuario interfazUsuario;

}

/**
 * Clase que representa una opción de conversión de divisas.
 */
record CurrencyOption(String fromCurrency, String toCurrency) {
}


