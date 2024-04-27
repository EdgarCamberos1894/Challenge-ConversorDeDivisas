package org.camberos.utilities;

import org.camberos.model.Divisas;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class DivisaRenderer extends DefaultListCellRenderer {
    private final Map<String, Divisas> divisas;

    public DivisaRenderer(Divisas[] divisasArray) {
        this.divisas = new HashMap<>();
        for (Divisas divisas : divisasArray) {
            this.divisas.put(divisas.moneda(), divisas);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String nombreMoneda = (String) value;
        Divisas divisas = this.divisas.get(nombreMoneda);
        if (divisas != null) {
            setText(nombreMoneda);
            try {  // Intentar cargar la imagen desde la URL de la Divisa
                URI uri = new URI(divisas.bandera_url());
                URL url = uri.toURL();
                setIcon(new ImageIcon(url));
            } catch (Exception e) {  // Manejar erroes de carga ed imagen y URL incorrectas
                e.printStackTrace();
                setIcon(null); // Limpiar el icono en caso de error
            }
        }
        return this;
    }
}
