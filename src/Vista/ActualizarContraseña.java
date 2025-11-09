package Vista;

import Utilidades.PlaceholderDocumentListener;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Michael Ramos;
 *
 *
 */
public class ActualizarContraseña extends javax.swing.JFrame {

    private final String NUEVA_PASS_PLACEHOLDER = "Escriba la nueva contraseña";
    private final String CONFIRMAR_PASS_PLACEHOLDER = "Confirme la contraseña";
    private final char ECHO_CHAR = '•';

    public ActualizarContraseña() {
        initComponents();
        this.setLocationRelativeTo(this);

        this.setLocationRelativeTo(null);
        this.setResizable(false);

        try {
            setIconImage(new ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png")).getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el ícono de la app.");
        }

        if (lblError != null) {
            lblError.setText("");
            lblError.setForeground(Color.RED);
        }

        // Configurar placeholder
        pfNuevaContrasena.setText(NUEVA_PASS_PLACEHOLDER);
        pfNuevaContrasena.setEchoChar((char) 0);
        pfNuevaContrasena.setForeground(Color.GRAY);

        pfNuevaContrasena.getDocument().addDocumentListener(new PlaceholderDocumentListener(
                pfNuevaContrasena, NUEVA_PASS_PLACEHOLDER, ECHO_CHAR
        ));

        // Configurar placeholder
        pfConfirmarContrasena.setText(CONFIRMAR_PASS_PLACEHOLDER);
        pfConfirmarContrasena.setEchoChar((char) 0);
        pfConfirmarContrasena.setForeground(Color.GRAY);

        pfConfirmarContrasena.getDocument().addDocumentListener(new PlaceholderDocumentListener(
                pfConfirmarContrasena, CONFIRMAR_PASS_PLACEHOLDER, ECHO_CHAR
        ));

        String rutaSpinner = "/Imagenes/spinner.gif";
        try {

            ImageIcon originalIcon = new ImageIcon(getClass().getResource(rutaSpinner));

            int anchoDeseado = 90;
            int altoDeseado = 90;

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    anchoDeseado, altoDeseado, Image.SCALE_DEFAULT
            );

            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            lbSpinner.setIcon(scaledIcon);
            lbSpinner.setVisible(false);

        } catch (Exception ex) {
            System.err.println("Error al cargar o redimensionar el spinner: " + ex.getMessage());
            lbSpinner.setVisible(false);
        }

        try {
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png")).getImage();
            this.setIconImage(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono de la aplicación");
        }

        this.getRootPane().setDefaultButton(btnGuardar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbSpinner = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pfNuevaContrasena = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        pfConfirmarContrasena = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnGuardar = new javax.swing.JButton();
        lblError = new javax.swing.JLabel();
        lbOjo = new javax.swing.JLabel();
        lbOjo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Fondo.setBackground(new java.awt.Color(255, 255, 255));
        Fondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 0, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 980, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        Fondo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 588, 980, 20));

        lbSpinner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/spinner.gif"))); // NOI18N
        Fondo.add(lbSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, 210, 150));

        jLabel1.setFont(new java.awt.Font("Roboto Black", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Crear nueva contraseña");
        Fondo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 560, 41));

        jLabel2.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Ingresa tu nueva contraseña. Asegurate de que sea segura.");
        Fondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 490, 70));

        jLabel3.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Ingrese la nueva contraseña:");
        Fondo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 250, 30));

        pfNuevaContrasena.setBackground(new java.awt.Color(255, 255, 255));
        pfNuevaContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pfNuevaContrasena.setForeground(new java.awt.Color(0, 0, 0));
        pfNuevaContrasena.setText("jPasswordField1");
        pfNuevaContrasena.setBorder(null);
        Fondo.add(pfNuevaContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 380, 40));

        jLabel4.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Confirmar Contraseña:");
        Fondo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 190, 30));

        pfConfirmarContrasena.setBackground(new java.awt.Color(255, 255, 255));
        pfConfirmarContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pfConfirmarContrasena.setForeground(new java.awt.Color(0, 0, 0));
        pfConfirmarContrasena.setText("jPasswordField1");
        pfConfirmarContrasena.setBorder(null);
        Fondo.add(pfConfirmarContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 380, 40));
        Fondo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, 380, 20));
        Fondo.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 380, 20));

        btnGuardar.setBackground(new java.awt.Color(153, 0, 255));
        btnGuardar.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar Cambios");
        Fondo.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 190, 60));

        lblError.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblError.setForeground(new java.awt.Color(255, 0, 51));
        Fondo.add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, 370, 60));

        lbOjo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ojo.png"))); // NOI18N
        lbOjo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbOjo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbOjoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lbOjoMouseReleased(evt);
            }
        });
        Fondo.add(lbOjo, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 220, 40, 40));

        lbOjo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ojo.png"))); // NOI18N
        lbOjo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbOjo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbOjo1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lbOjo1MouseReleased(evt);
            }
        });
        Fondo.add(lbOjo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 310, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbOjoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbOjoMousePressed
        pfNuevaContrasena.setEchoChar((char) 0);
    }//GEN-LAST:event_lbOjoMousePressed

    private void lbOjoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbOjoMouseReleased
        pfNuevaContrasena.setEchoChar('*');
    }//GEN-LAST:event_lbOjoMouseReleased

    private void lbOjo1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbOjo1MousePressed
        pfConfirmarContrasena.setEchoChar((char) 0);
    }//GEN-LAST:event_lbOjo1MousePressed

    private void lbOjo1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbOjo1MouseReleased
        pfConfirmarContrasena.setEchoChar('*');
    }//GEN-LAST:event_lbOjo1MouseReleased

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ActualizarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ActualizarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ActualizarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ActualizarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ActualizarContraseña().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbOjo;
    private javax.swing.JLabel lbOjo1;
    public javax.swing.JLabel lbSpinner;
    public javax.swing.JLabel lblError;
    public javax.swing.JPasswordField pfConfirmarContrasena;
    public javax.swing.JPasswordField pfNuevaContrasena;
    // End of variables declaration//GEN-END:variables
}
