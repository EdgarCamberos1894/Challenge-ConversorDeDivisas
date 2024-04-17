package org.camberos.view;

import com.google.gson.Gson;
import org.camberos.api.CurrencyConverterAPI;
import org.camberos.model.CurrencyDTO;
import org.camberos.utilities.DivisaRenderer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class InterfazUsuario extends JFrame {

    private static final int DEFAULT_DOLAR_INDEX = 52;
    private static final int DEFAULT_PESO_MEXICANO_INDEX = 110;
    private static final String JSON_PATH = "src/main/resources/divisas.json";
    private final Gson gson = new Gson();

    public InterfazUsuario() {
        initComponents();
        cargarDatosEnComboBoxDivisas();
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        //Agregando placeholder
        PromptSupport.setPrompt("Ingresa la cantidad a convertir",jTextField1);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, jTextField1);

        //Hacer los ComboBox sercheables
        AutoCompleteDecorator.decorate(jComboBox1);
        AutoCompleteDecorator.decorate(jComboBox2);
        //Inicializacion de Conversor De Divisas
        jComboBox1.setSelectedIndex(DEFAULT_DOLAR_INDEX);
        jComboBox2.setSelectedIndex(DEFAULT_PESO_MEXICANO_INDEX);
       
    }

    private void cargarDatosEnComboBoxDivisas() {
        try {
            DatosJSON datos = cargarDatosJSON(JSON_PATH);

            if (datos != null && datos.divisas != null) {
                Divisa[] divisas = datos.divisas;

                // Creamos un array de nombres de monedas
                String[] nombresMonedas = Arrays.stream(divisas)
                        .map(divisa -> divisa.moneda)
                        .toArray(String[]::new);

                // Creamos un ComboBoxModel que contiene solo los nombres de las monedas
                DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>(nombresMonedas);
                jComboBox1.setModel(model1);

                // Creamos otro ComboBoxModel para el segundo JComboBox
                DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>(nombresMonedas);
                jComboBox2.setModel(model2);

                jComboBox1.setRenderer(new DivisaRenderer(divisas));
                jComboBox2.setRenderer(new DivisaRenderer(divisas));
            } else {
                mostrarError("Error al cargar datos desde el archivo JSON");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar datos desde el archivo JSON");
        }
    }

    private DatosJSON cargarDatosJSON(String ruta) throws IOException {
        try (FileReader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, DatosJSON.class);
        }
    }


    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static class DatosJSON {
        Divisa[] divisas;
    }

    public static class Divisa {
        String codigo;
        public String moneda;
        public String bandera_url;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jTextField1 = new JTextField();
        jComboBox1 = new JComboBox<>();
        jComboBox2 = new JComboBox<>();
        jSeparator1 = new JSeparator();
        jLabel2 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Conversión de Divisas");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jTextField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0' && c <= '9') || c == '.')) {
                    e.consume();
                }
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 1, 14));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 14));

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24));
        jLabel2.setText(" ");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        jButton1.setBackground(new java.awt.Color(153, 204, 255));
        jButton1.setText("Convertir");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 204, 255));
        jButton2.setIcon(new ImageIcon("src/main/resources/image/intercambiar-icon.png"));
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1)
                                        .addComponent(jLabel2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField1)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jComboBox1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jComboBox2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap())))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    private void jTextField1ActionPerformed(ActionEvent evt) {
        convertirDivisas();
    }

    private void convertirDivisas() {

        int selectedIndex1 = jComboBox1.getSelectedIndex();
        int selectedIndex2 = jComboBox2.getSelectedIndex();

        try {
            DatosJSON datos = cargarDatosJSON(JSON_PATH);
            realizarConversion(selectedIndex1, selectedIndex2, datos);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar datos desde el archivo JSON");
        }
    }

    private void realizarConversion(int indiceOrigen, int indiceDestino, DatosJSON datos) {
        String amount = jTextField1.getText();
        if(amount.isEmpty() || amount.matches("\\d*\\.?\\d*")) {


            String fromCurrency = datos.divisas[indiceOrigen].codigo;
            String toCurrency = datos.divisas[indiceDestino].codigo;

            CurrencyConverterAPI currency = new CurrencyConverterAPI();
            CurrencyDTO resultado = currency.convertCurrency(fromCurrency, toCurrency, amount);

            jLabel2.setText(String.format("%.2f", resultado.conversionResult()) + " (" + resultado.targetCode() + ")");
        }
        else {
            mostrarError("Verifica que tu campo no este vacio y hayas ingresado un valor numerico positivo");
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        convertirDivisas();
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        int monendaOrigenTemporal = jComboBox1.getSelectedIndex();
        jComboBox1.setSelectedIndex(jComboBox2.getSelectedIndex());
        jComboBox2.setSelectedIndex(monendaOrigenTemporal);
        convertirDivisas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InterfazUsuario interfazUsuario = new InterfazUsuario();
                interfazUsuario.setVisible(true);
                interfazUsuario.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify
    private JButton jButton1;
    private JButton jButton2;
    private JComboBox<String> jComboBox1;
    private JComboBox<String> jComboBox2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JSeparator jSeparator1;
    private JTextField jTextField1;
    // End of variables declaration
}