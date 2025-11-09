package Vista;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author Michael Ramos;
 *
 *
 */
public class Login extends javax.swing.JFrame {

    public static final String PLACEHOLDER_USUARIO = "Ingrese su usuario";
    public static final String PLACEHOLDER_PASSWORD = "* * * * * * * *";

    int xMause, yMause;
    private boolean passwordVisible = false;

    public Login() {
        initComponents();

        setLocationRelativeTo(this);

        Bg.setOpaque(true);
        tfUsuario.setOpaque(true);
        tfContraseña.setOpaque(true);
        Bg.setBackground(new Color(255, 255, 255));

        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Bg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 1));

        Utilidades.PlaceholderTextFieldListener userListener = new Utilidades.PlaceholderTextFieldListener(tfUsuario, PLACEHOLDER_USUARIO);
        Utilidades.PlaceholderDocumentListener passListener = new Utilidades.PlaceholderDocumentListener(tfContraseña, PLACEHOLDER_PASSWORD, '*');

        tfContraseña.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String passwordActual = String.valueOf(tfContraseña.getPassword());
                if (passwordActual.equals("* * * * * * * *")) {
                    tfContraseña.setText("");
                    tfContraseña.setForeground(Color.BLACK);
                }
                jSeparator2.setForeground(new Color(80, 80, 80));

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(tfContraseña.getPassword()).isEmpty()) {
                    tfContraseña.setText("* * * * * * * *");
                    tfContraseña.setForeground(Color.GRAY);
                }
                jSeparator2.setForeground(new Color(80, 80, 80));
            }
        });

        lbOjo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String password = String.valueOf(tfContraseña.getPassword());
                if (password.isEmpty() || password.equals("* * * * * * * *")) {
                    jSeparator2.setForeground(Color.RED);
                    tfContraseña.requestFocus();
                } else {
                    passwordVisible = !passwordVisible;
                    if (passwordVisible) {
                        tfContraseña.setEchoChar((char) 0);
                    } else {
                        tfContraseña.setEchoChar('•');
                    }
                }
            }
        });

        KeyAdapter borradorDeErrores = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                    lbMensajeError.setText("");
                }
            }
        };
        tfUsuario.addKeyListener(borradorDeErrores);
        tfContraseña.addKeyListener(borradorDeErrores);

        tfContraseña.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSesion.doClick();
                }
            }
        });

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

    }

    public void iniciarAnimacionShake() {
        final int SHAKE_DISTANCE = 10;
        final int SHAKE_DURATION = 50;
        final int SHAKE_COUNT = 6;

        final java.awt.Point originalLocation = this.getLocation();

        final int[] shakeOffsets = {SHAKE_DISTANCE, -SHAKE_DISTANCE, SHAKE_DISTANCE, -SHAKE_DISTANCE, SHAKE_DISTANCE / 2, -SHAKE_DISTANCE / 2};

        javax.swing.Timer shakeTimer = new javax.swing.Timer(SHAKE_DURATION, new ActionListener() {
            private int shakeIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (shakeIndex < SHAKE_COUNT) {
                    setLocation(originalLocation.x + shakeOffsets[shakeIndex], originalLocation.y);
                    shakeIndex++;
                } else {
                    setLocation(originalLocation);
                    ((javax.swing.Timer) e.getSource()).stop();
                }
            }
        });
        shakeTimer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Bg = new javax.swing.JPanel();
        lbSpinner = new javax.swing.JLabel();
        lbLogoPrincipal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbUsuario = new javax.swing.JLabel();
        tfUsuario = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        lbContraseña = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tfContraseña = new javax.swing.JPasswordField();
        Header = new javax.swing.JPanel();
        btnExit = new javax.swing.JPanel();
        lbX = new javax.swing.JLabel();
        lbLogoContraseña = new javax.swing.JLabel();
        lbLogoUsuario = new javax.swing.JLabel();
        lbOjo = new javax.swing.JLabel();
        lbMensajeError = new javax.swing.JLabel();
        btnSesion = new javax.swing.JButton();
        lbOlvidasteContraseña = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        Bg.setBackground(new java.awt.Color(255, 255, 255));
        Bg.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbSpinner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/spinner.gif"))); // NOI18N
        Bg.add(lbSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 240, 140, 120));

        lbLogoPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png"))); // NOI18N
        Bg.add(lbLogoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 70, 70));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Master Your Monthly Spending with Smart Budgeting Tools!.jpg"))); // NOI18N
        Bg.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 590, 680));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("INICIAR SESIÓN");
        Bg.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 300, 40));

        lbUsuario.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbUsuario.setForeground(new java.awt.Color(0, 0, 0));
        lbUsuario.setText("USUARIO:");
        Bg.add(lbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, -1, -1));

        tfUsuario.setBackground(new java.awt.Color(255, 255, 255));
        tfUsuario.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        tfUsuario.setForeground(new java.awt.Color(204, 204, 204));
        tfUsuario.setBorder(null);
        tfUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        Bg.add(tfUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 390, 40));
        Bg.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 390, 10));

        lbContraseña.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbContraseña.setForeground(new java.awt.Color(0, 0, 0));
        lbContraseña.setText("CONTRASEÑA:");
        Bg.add(lbContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 390, -1, -1));
        Bg.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 370, 10));

        tfContraseña.setBackground(new java.awt.Color(255, 255, 255));
        tfContraseña.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        tfContraseña.setForeground(new java.awt.Color(204, 204, 204));
        tfContraseña.setBorder(null);
        Bg.add(tfContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 440, 370, 40));

        Header.setBackground(new java.awt.Color(255, 255, 255));
        Header.setForeground(new java.awt.Color(255, 255, 255));
        Header.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                HeaderMouseDragged(evt);
            }
        });
        Header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                HeaderMousePressed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(255, 255, 255));

        lbX.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lbX.setForeground(new java.awt.Color(0, 0, 0));
        lbX.setText("     X");
        lbX.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbXMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbXMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbXMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnExitLayout = new javax.swing.GroupLayout(btnExit);
        btnExit.setLayout(btnExitLayout);
        btnExitLayout.setHorizontalGroup(
            btnExitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbX, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );
        btnExitLayout.setVerticalGroup(
            btnExitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbX, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1070, Short.MAX_VALUE))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        Bg.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 60));

        lbLogoContraseña.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-con-llave.png"))); // NOI18N
        Bg.add(lbLogoContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 40, 40));

        lbLogoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N
        Bg.add(lbLogoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 40, 50));

        lbOjo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ojo.png"))); // NOI18N
        lbOjo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Bg.add(lbOjo, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 450, 40, 40));

        lbMensajeError.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lbMensajeError.setForeground(new java.awt.Color(255, 0, 0));
        Bg.add(lbMensajeError, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 630, 380, 50));

        btnSesion.setBackground(new java.awt.Color(153, 102, 255));
        btnSesion.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        btnSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnSesion.setText("INICIAR SESIÓN");
        btnSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSesionMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSesionMouseReleased(evt);
            }
        });
        Bg.add(btnSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, 170, 70));

        lbOlvidasteContraseña.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        lbOlvidasteContraseña.setForeground(new java.awt.Color(51, 51, 255));
        lbOlvidasteContraseña.setText("¿Olvidaste tu contraseña?");
        lbOlvidasteContraseña.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Bg.add(lbOlvidasteContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 210, 30));

        getContentPane().add(Bg, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HeaderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMousePressed

        xMause = evt.getX();
        yMause = evt.getY();
    }//GEN-LAST:event_HeaderMousePressed

    private void HeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMouseDragged
        //
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMause, y - yMause);
    }//GEN-LAST:event_HeaderMouseDragged

    private void lbXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbXMouseClicked
        //Evento de LB Cierre el programa
        System.exit(0);
    }//GEN-LAST:event_lbXMouseClicked

    private void lbXMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbXMouseEntered
        //Cambia el color de el cierre (Evento es cuando el mause entra al elemento X)
        btnExit.setBackground(Color.red);
        lbX.setForeground(Color.white);
    }//GEN-LAST:event_lbXMouseEntered

    private void lbXMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbXMouseExited
        //cambia el color del cierre (Evento cuando el mause sale del elemento X)
        btnExit.setBackground(Color.white);
        lbX.setForeground(Color.red);
    }//GEN-LAST:event_lbXMouseExited

    private void btnSesionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSesionMousePressed
        btnSesion.setBackground(new Color(100, 0, 200));
    }//GEN-LAST:event_btnSesionMousePressed

    private void btnSesionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSesionMouseReleased
        btnSesion.setBackground(new Color(153, 0, 255));
    }//GEN-LAST:event_btnSesionMouseReleased

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
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Login().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bg;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel btnExit;
    public javax.swing.JButton btnSesion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbContraseña;
    private javax.swing.JLabel lbLogoContraseña;
    private javax.swing.JLabel lbLogoPrincipal;
    private javax.swing.JLabel lbLogoUsuario;
    public javax.swing.JLabel lbMensajeError;
    private javax.swing.JLabel lbOjo;
    public javax.swing.JLabel lbOlvidasteContraseña;
    public javax.swing.JLabel lbSpinner;
    private javax.swing.JLabel lbUsuario;
    private javax.swing.JLabel lbX;
    public javax.swing.JPasswordField tfContraseña;
    public javax.swing.JTextField tfUsuario;
    // End of variables declaration//GEN-END:variables
}
