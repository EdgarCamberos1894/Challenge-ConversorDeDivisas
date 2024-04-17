package org.camberos.utilities;

import org.camberos.view.InterfazUsuario.Divisa;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Renderizador personalizado para mostrar un icono de la bandera correspondiente al inicio de la lista de divisas.
 */
public class DivisaRenderer extends DefaultListCellRenderer {
    private final Divisa[] divisas;

    /**
     * Crea un nuevo renderizador de divisa con las divisas proporcionadas.
     *
     * @param divisas Arreglo de objetos Divisa que contiene información sobre las divisas.
     */
    public DivisaRenderer(Divisa[] divisas) {
        this.divisas = divisas;
    }

    /**
     * Devuelve el componente de la celda de la lista con el formato adecuado.
     *
     * @param list          La lista que contiene la celda.
     * @param value         El valor de la celda.
     * @param index         El índice de la celda.
     * @param isSelected    Indica si la celda está seleccionada.
     * @param cellHasFocus  Indica si la celda tiene el foco.
     * @return El componente de la celda con el formato adecuado.
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof String) {
            // Si el valor es una cadena, se busca la Divisa correspondiente
            String nombreMoneda = (String) value;
            for (Divisa divisa : divisas) {
                if (divisa.moneda.equals(nombreMoneda)) {
                    setText(nombreMoneda);
                    try {
                        // Intentar cargar la imagen desde la URL de la Divisa
                        URL url = new URL(divisa.bandera_url);
                        setIcon(new ImageIcon(url));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        // Manejar el error de URL incorrecta
                        setIcon(null); // Limpiar el icono si la URL es incorrecta
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Manejar otros errores de carga de imagen
                        setIcon(null); // Limpiar el icono en caso de error
                    }
                    break;
                }
            }
        }

        return this;
    }
}
