package org.camberos.view;

import com.google.gson.Gson;
import org.camberos.model.Config;
import org.camberos.controller.CurrencyController;
import org.camberos.model.CurrencyDTO;
import org.camberos.model.Divisas;
import org.camberos.model.DatosJSON;
import org.camberos.view.render.JComboBoxRenderer;
import org.camberos.model.Historial;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class InterfazUsuario extends JFrame {
    private static final String JSON_PATH = Config.getInstance().getJsonPath();
    private static final String CSV_PATH = Config.getInstance().getCsvPath();
    private static final int DEFAULT_DOLAR_INDEX = 51;
    private static final int DEFAULT_PESO_MEXICANO_INDEX = 109;

    private final Gson gson = new Gson();
    private DatosJSON datos;

    public InterfazUsuario() {
        initComponents();
        cargarDatosDivisas();
        ConfigurarInterfazComboBox();
        cargarHistorial();
    }

    private void cargarDatosDivisas() {
        datos = CurrencyController.cargarDatosDivisasJSON(JSON_PATH);
    }

    private void ConfigurarInterfazComboBox() {
        Divisas[] divisas = datos.divisas();
        String[] nombresMonedas = Arrays.stream(datos.divisas())
                .map(Divisas::moneda)
                .toArray(String[]::new);

        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>(nombresMonedas);
        jComboBox1.setModel(model1);
        DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>(nombresMonedas);
        jComboBox2.setModel(model2);

        // Renderizadores
        jComboBox1.setRenderer(new JComboBoxRenderer(divisas));
        jComboBox2.setRenderer(new JComboBoxRenderer(divisas));

        //Agregando placeholder
        PromptSupport.setPrompt("Ingresa la cantidad a convertir",jTextField1);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, jTextField1);
        PromptSupport.setForeground(Color.GRAY, jTextField1);

        //Hacer los ComboBox sercheables
        AutoCompleteDecorator.decorate(jComboBox1);
        AutoCompleteDecorator.decorate(jComboBox2);

        // Asignar el valor por defecto al cargar interfaz
        jComboBox1.setSelectedIndex(DEFAULT_DOLAR_INDEX);
        jComboBox2.setSelectedIndex(DEFAULT_PESO_MEXICANO_INDEX);
    }

    public void cargarHistorial(){
        Historial CSVHandler = new Historial();
        List<String> lineas = CSVHandler.leerCSV(CSV_PATH);
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Fecha", "Moneda Origen", "Moneda Destino", "Cantidad", "Valor Unitario", "Resultado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }
        };
        for (String linea : lineas) {
            String[] datos = linea.split(",");
            modelo.addRow(datos);
        }
        jTable1.setModel(modelo);
    }

    private void jTextField1ActionPerformed() {
        convertirDivisas();
    }

    private void jButton1ActionPerformed() {
        convertirDivisas();
    }

    private void jButton2ActionPerformed() {
        intercambiarMonedas();
        convertirDivisas();
    }

    private void intercambiarMonedas() {
        int monedaOrigenTemporal = jComboBox1.getSelectedIndex();
        jComboBox1.setSelectedIndex(jComboBox2.getSelectedIndex());
        jComboBox2.setSelectedIndex(monedaOrigenTemporal);
    }

    private void convertirDivisas() {
        int indiceDivisaOrigen = jComboBox1.getSelectedIndex();
        int indiceDivisaDestino = jComboBox2.getSelectedIndex();
        String cantidad = jTextField1.getText();

        if (validarCantidad(cantidad)) {
            CurrencyDTO resultado = CurrencyController.convertirDivisas(datos, indiceDivisaOrigen, indiceDivisaDestino, cantidad);
            actualizarResultado(resultado);
            guardarResultado(new Date(),resultado, cantidad);
        } else {
            JOptionPane.showMessageDialog(null, "Verifica que tu campo no esté vacío y hayas ingresado un valor numérico positivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCantidad(String cantidad) {
        return !cantidad.isEmpty() && cantidad.matches("\\d*\\.?\\d*");
    }

    private void actualizarResultado(CurrencyDTO resultado) {
        String precioTotal = String.format("%.2f", resultado.conversionResult());
        jLabel2.setText(precioTotal +" "+resultado.targetCode());
    }

    private void guardarResultado(Date fecha,CurrencyDTO resultado, String cantidad) {
        String monedaOrigen = resultado.baseCode();
        String monedaDestino = resultado.targetCode();
        String precioUnitario = String.format("%.2f", resultado.conversionRate());
        String precioTotal = String.format("%.2f", resultado.conversionResult());

        actualizarTabla(fecha,monedaOrigen,monedaDestino,cantidad,precioUnitario,precioTotal);

        Historial.guardarResultado(fecha, monedaOrigen, monedaDestino, cantidad, precioUnitario, precioTotal, CSV_PATH);
    }

    private void actualizarTabla(Date fecha, String monedaOrigen, String monedaDestino, String cantidad, String precioUnitario, String precioTotal){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Config.getInstance().getDateFormat());
        String fechaFormato = DATE_FORMAT.format(fecha);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.insertRow(0, new Object[]{fechaFormato, monedaOrigen, monedaDestino, cantidad, precioUnitario, precioTotal});

    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        JPanel jPanel2 = new JPanel();
        jTextField1 = new JTextField();
        jComboBox1 = new JComboBox<>();
        jComboBox2 = new JComboBox<>();
        JSeparator jSeparator1 = new JSeparator();
        JSeparator jSeparator2 = new JSeparator();
        jLabel2 = new JLabel();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\edgar\\Downloads\\Currency_Conversion_icon-icons.com_56682 (4).png"));
        jLabel1.setText("Conversión de Divisas");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));
        jTextField1.addActionListener(evt -> jTextField1ActionPerformed());
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0' && c <= '9') || c == '.')) {
                    evt.consume();
                }
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", Font.BOLD, 24));
        jLabel2.setText(" ");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        jButton1.setBackground(new java.awt.Color(153, 204, 255));
        jButton1.setText("Convertir");
        jButton1.addActionListener(evt -> jButton1ActionPerformed());

        jButton2.setBackground(new java.awt.Color(153, 204, 255));
        jButton2.setIcon(new ImageIcon("src/main/resources/image/intercambiar-icon.png"));
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        jTable1.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12));

        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jComboBox1, 0, 559, Short.MAX_VALUE)
                                                                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
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
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            InterfazUsuario interfazUsuario = new InterfazUsuario();
            interfazUsuario.setVisible(true);
            interfazUsuario.setLocationRelativeTo(null);
        });
    }

    private JComboBox<String> jComboBox1;
    private JComboBox<String> jComboBox2;
    private JTextField jTextField1;
    private JLabel jLabel2;
    private JTable jTable1;
}
