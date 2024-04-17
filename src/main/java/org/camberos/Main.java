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

        // Se crea un objeto Scanner para la entrada del usuario
        Scanner scanner = new Scanner(System.in);
        boolean shouldExit = false;

        // Bucle principal del programa
        do {
            // Se muestra el menú principal
            printMainMenu();
            // Se obtiene la opción del usuario
            String option = getUserInput(scanner);
            // Se procesa la opción elegida por el usuario
            shouldExit = processOption(option, scanner, ventanaThread);
        } while (!shouldExit);

        // Se cierra el scanner
        scanner.close();
    }

    // Métodos auxiliares
    /**
     * Imprime el menú principal del programa.
     */
    private static void printMainMenu() {
        System.out.println(MAIN_MENU);
    }

    /**
     * Obtiene la entrada del usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return La entrada del usuario como una cadena de texto.
     */
    private static String getUserInput(Scanner scanner) {
        return scanner.nextLine();
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
            // Casos para las opciones de conversión de divisas
            case "1", "2", "3", "4", "5", "6" -> {
                System.out.println(convertCurrency(option, scanner));
                return askForAnotherOperation(scanner, ventanaThread);
            }
            // Caso para salir del programa
            case "7" -> {
                System.out.println("            *********** ¡Hasta luego! *************");
                closeInterface(ventanaThread);
                return true;
            }
            // Caso para mostrar la interfaz de usuario
            case "8" -> {
                showUserInterface(ventanaThread);
                return false;
            }
            // Caso para opciones inválidas
            default -> {
                System.out.println(invalidOptionMessage);
                return false;
            }
        }
    }

    /**
     * Pide al usuario que elija si desea realizar otra operación.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @param ventanaThread El hilo de la interfaz de usuario.
     * @return true si el usuario desea salir, false si desea realizar otra operación.
     */
    private static boolean askForAnotherOperation(Scanner scanner, Thread ventanaThread) {
        System.out.println(askForAnotherOperationMessage);
        String response = getUserInput(scanner);

        while (!response.equals("1") && !response.equals("2")) {
            System.out.println(invalidOptionMessage);
            response = getUserInput(scanner);
        }

        if (response.equals("2")) {
            closeInterface(ventanaThread);
            return true;
        }

        return false;
    }

    /**
     * Convierte la cantidad de una divisa a otra.
     * @param option La opción elegida por el usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return El resultado de la conversión de divisas como una cadena de texto.
     */
    private static String convertCurrency(String option, Scanner scanner) {
        CurrencyOption currencyOption = CURRENCY_OPTIONS.get(option);
        String fromCurrency = currencyOption.getFromCurrency();
        String toCurrency = currencyOption.getToCurrency();

        double amount = getValidAmount(scanner);

        CurrencyConverter currencyConverter = new CurrencyConverter();
        CurrencyDTO result = currencyConverter.convertCurrency(fromCurrency, toCurrency, Double.toString(amount));

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
    private static double getValidAmount(Scanner scanner) {
        double amount = 0;
        boolean isValid;

        do {
            System.out.println("      *******  Ingrese el valor que deseas convertir ****** ");
            String input = getUserInput(scanner);

            try {
                amount = Double.parseDouble(input);
                isValid = amount >= 0;
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
        closeInterface(ventanaThread);
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
class CurrencyOption {
    private final String fromCurrency;
    private final String toCurrency;

    public CurrencyOption(String fromCurrency, String toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }
}

/**
 * Clase que realiza la conversión de divisas utilizando una API externa.
 */
class CurrencyConverter {
    public CurrencyDTO convertCurrency(String fromCurrency, String toCurrency, String amount) {
        CurrencyConverterAPI currency = new CurrencyConverterAPI();
        return currency.convertCurrency(fromCurrency, toCurrency, amount);
    }
}
