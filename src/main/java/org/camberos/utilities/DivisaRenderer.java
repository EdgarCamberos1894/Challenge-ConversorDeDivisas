package org.camberos.utilities;

import org.camberos.view.InterfazUsuario;
import org.camberos.view.InterfazUsuario.Divisa;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class DivisaRenderer extends DefaultListCellRenderer {
    private final Map<String, Divisa> divisas;

    public DivisaRenderer(Divisa[] divisasArray) {
        this.divisas = new HashMap<>();
        for (Divisa divisa : divisasArray) {
            this.divisas.put(divisa.moneda, divisa);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String nombreMoneda = (String) value;
        Divisa divisa = divisas.get(nombreMoneda);
        if (divisa != null) {
            setText(nombreMoneda);
            try {  // Intentar cargar la imagen desde la URL de la Divisa
                URL url = new URL(divisa.bandera_url);
                setIcon(new ImageIcon(url));
            } catch (Exception e) {  // Manejar erroes de carga ed imagen y URL incorrectas
                e.printStackTrace();
                setIcon(null); // Limpiar el icono en caso de error
            }
        }
        return this;
    }
}
