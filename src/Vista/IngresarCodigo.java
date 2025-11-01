package Vista;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Michael Ramos;
 */
public class IngresarCodigo extends javax.swing.JFrame {

    public IngresarCodigo() {
        initComponents();
        this.setLocationRelativeTo(this);
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png")).getImage());

        // --- INICIO DE LÓGICA DE 6 CAJAS ---
// 1. Agrupa todos tus JTextField en un array para manejarlos fácil
        JTextField[] campos = {txtCodigo1, txtCodigo2, txtCodigo3, txtCodigo4, txtCodigo5, txtCodigo6};

// 2. Itera sobre cada campo para añadir la lógica
        for (int i = 0; i < campos.length; i++) {
            JTextField campoActual = campos[i];
            int indiceActual = i;

            // A. Limita cada campo a 1 SOLO CARÁCTER y que sea NÚMERO
            ((AbstractDocument) campoActual.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) {
                        return;
                    }
                    // Acepta solo 1 dígito
                    if ((fb.getDocument().getLength() + string.length()) <= 1 && string.matches("\\d")) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) {
                        return;
                    }
                    // Acepta solo 1 dígito al reemplazar
                    if ((fb.getDocument().getLength() + text.length() - length) <= 1 && text.matches("\\d")) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });

            // B. Añade el "Key Listener" para AUTO-TAB, BORRADO y PEGADO
            campoActual.addKeyListener(new KeyAdapter() {

                // --- Lógica de Auto-Tab y Borrado ---
                @Override
                public void keyReleased(KeyEvent e) {
                    String texto = campoActual.getText();

                    // CASO 1: Si se escribió un dígito y la caja está llena
                    if (texto.length() == 1 && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                        // Mueve el foco al SIGUIENTE campo (si no es el último)
                        if (indiceActual < campos.length - 1) {
                            campos[indiceActual + 1].requestFocusInWindow();
                        }
                    }

                    // CASO 2: Si se presionó BORRAR (Backspace) y la caja está vacía
                    if (texto.isEmpty() && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        // Mueve el foco al campo ANTERIOR (si no es el primero)
                        if (indiceActual > 0) {
                            campos[indiceActual - 1].requestFocusInWindow();
                        }
                    }
                }

                // --- Lógica de Pegado (CTRL+V) ---
                @Override
                public void keyPressed(KeyEvent e) {
                    // Detecta si el usuario presionó CTRL+V (Pegar)
                    // Solo lo procesamos en la PRIMERA caja para evitar líos
                    if (indiceActual == 0 && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                        e.consume(); // Consume el evento para que no pegue todo en la caja 1

                        try {
                            // Obtiene el texto del portapapeles
                            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                            String data = (String) c.getData(DataFlavor.stringFlavor);

                            // Limpia y valida el texto (quita espacios, etc.)
                            String codigo = data.trim().replaceAll("[^\\d]", ""); // Solo números

                            // Si es un código de 6 dígitos, lo distribuye
                            if (codigo.length() == 6) {
                                for (int j = 0; j < campos.length; j++) {
                                    campos[j].setText(String.valueOf(codigo.charAt(j)));
                                }
                                // Mueve el foco al final
                                campos[5].requestFocusInWindow();
                            } else if (codigo.length() > 0) {
                                // Si pegó algo que no son 6 dígitos, solo pega el primero
                                campoActual.setText(String.valueOf(codigo.charAt(0)));
                            }

                        } catch (Exception ex) {
                            System.err.println("Error al pegar: " + ex.getMessage());
                        }
                    }
                }
            });
        }
// --- FIN DE LÓGICA DE 6 CAJAS ---

        String rutaSpinner = "/Imagenes/spinner.gif";
        try {
            // Carga la imagen original
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(rutaSpinner));

            // Define el tamaño deseado (ej. 32x32)
            int anchoDeseado = 90;
            int altoDeseado = 90;

            // Obtiene la imagen y la escala
            // Usamos Image.SCALE_DEFAULT para que respete la animación del GIF
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    anchoDeseado, altoDeseado, Image.SCALE_DEFAULT
            );

            // Crea un nuevo ImageIcon con la imagen escalada
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Asigna el icono escalado (y ahora pequeño) al JLabel
            lbSpinner.setIcon(scaledIcon);
            lbSpinner.setVisible(false); // Sigue oculto por defecto

        } catch (Exception ex) {
            System.err.println("Error al cargar o redimensionar el spinner: " + ex.getMessage());
            lbSpinner.setVisible(false);
        }
    }
    
    /**
     * Método público para que el Controlador obtenga el código completo.
     * @return El código de 6 dígitos (o un string incompleto).
     */
    public String getCodigoIngresado() {
        // Asegúrate que los nombres (txtCodigo1, txtCodigo2, etc.)
        // coincidan con los de tu diseñador.
        return txtCodigo1.getText() +
               txtCodigo2.getText() +
               txtCodigo3.getText() +
               txtCodigo4.getText() +
               txtCodigo5.getText() +
               txtCodigo6.getText();
    }

// ... aquí van tus variables autogeneradas (txtCodigo1, etc.)...
// } <-- Este es el último '}' de la clase

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondo = new javax.swing.JPanel();
        FondoCodigo = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        txtCodigo1 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        txtCodigo2 = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        txtCodigo3 = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        txtCodigo4 = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        txtCodigo5 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        txtCodigo6 = new javax.swing.JTextField();
        lbSpinner = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();
        lblTimer = new javax.swing.JLabel();
        btnReenviar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Fondo.setBackground(new java.awt.Color(255, 255, 255));

        FondoCodigo.setBackground(new java.awt.Color(255, 255, 255));
        FondoCodigo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 50, 11));

        txtCodigo1.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo1.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo1.setBorder(null);
        txtCodigo1.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 82, 50, 50));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 50, 11));

        txtCodigo2.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo2.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo2.setBorder(null);
        txtCodigo2.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 50, 50));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 50, 11));

        txtCodigo3.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo3.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo3.setBorder(null);
        txtCodigo3.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 50, 50));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 50, 11));

        txtCodigo4.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo4.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo4.setBorder(null);
        txtCodigo4.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 50, 50));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, 50, 11));

        txtCodigo5.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo5.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo5.setBorder(null);
        txtCodigo5.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 50, 50));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FondoCodigo.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, 50, 11));

        txtCodigo6.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo6.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo6.setBorder(null);
        txtCodigo6.setPreferredSize(new java.awt.Dimension(40, 40));
        FondoCodigo.add(txtCodigo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 50, 50));

        lbSpinner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/spinner.gif"))); // NOI18N
        FondoCodigo.add(lbSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, -10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("INGRESE CODIGO DE VERIFICACIÓN");

        btnSiguiente.setBackground(new java.awt.Color(153, 0, 255));
        btnSiguiente.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setText("SIGUIENTE");
        btnSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lblTimer.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblTimer.setForeground(new java.awt.Color(0, 0, 0));
        lblTimer.setText("Tiempo restante: 2:00");

        btnReenviar.setBackground(new java.awt.Color(153, 0, 255));
        btnReenviar.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        btnReenviar.setForeground(new java.awt.Color(255, 255, 255));
        btnReenviar.setText("Reenviar");
        btnReenviar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReenviar.setEnabled(false);

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(FondoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addComponent(btnReenviar)
                        .addGap(145, 145, 145))))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addComponent(FondoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReenviar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IngresarCodigo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresarCodigo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresarCodigo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresarCodigo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngresarCodigo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JPanel FondoCodigo;
    public javax.swing.JButton btnReenviar;
    public javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    public javax.swing.JLabel lbSpinner;
    public javax.swing.JLabel lblTimer;
    public javax.swing.JTextField txtCodigo1;
    public javax.swing.JTextField txtCodigo2;
    public javax.swing.JTextField txtCodigo3;
    public javax.swing.JTextField txtCodigo4;
    public javax.swing.JTextField txtCodigo5;
    public javax.swing.JTextField txtCodigo6;
    // End of variables declaration//GEN-END:variables
}
