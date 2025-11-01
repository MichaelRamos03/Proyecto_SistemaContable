package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author Michael Ramos;
 */
public class SplashScreen extends javax.swing.JFrame {
    
    private final int FADE_STEP = 30; // Milisegundos entre cada paso del fade
    private final float OPACITY_INCREMENT = 0.05f; // Cuánto aumenta/disminuye la opacidad en cada paso

    public SplashScreen() {
        initComponents();
        
        jpbBarraDeprogreso.setIndeterminate(true);
        jpbBarraDeprogreso.setStringPainted(false);
        
        this.setLocationRelativeTo(null); // Centra la ventana
        this.setOpacity(0.0f); // ¡Inicia completamente transparente!
        
        try {
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png")).getImage();
            this.setIconImage(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono de la aplicación");
        }
    }
    
    // --- Métodos para el Fade ---

    public void fadeIn() {
        // Muestra la ventana (sigue transparente)
        this.setVisible(true); 
        
        Timer fadeInTimer = new Timer(FADE_STEP, new ActionListener() {
            private float opacity = 0.0f; // Empieza desde la transparencia total

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += OPACITY_INCREMENT;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador cuando es opaco
                }
                setOpacity(opacity);
            }
        });
        fadeInTimer.start(); // Inicia el fade-in
    }

    public void fadeOut(ActionListener onComplete) {
        Timer fadeOutTimer = new Timer(FADE_STEP, new ActionListener() {
            private float opacity = 1.0f; // Empieza desde la opacidad total

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= OPACITY_INCREMENT;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador
                    dispose(); // Cierra la ventana del splash
                    if (onComplete != null) {
                        onComplete.actionPerformed(null); // Ejecuta la acción de "completado"
                    }
                }
                setOpacity(opacity);
            }
        });
        fadeOutTimer.start(); // Inicia el fade-out
    }

    // ... (El resto de tu código initComponents() y variables) ...

//    public static void main(String args[]) {
//        // Ejemplo de cómo se vería el splash si lo ejecutas directamente
//        // Es útil para probar solo el splash
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                SplashScreen s = new SplashScreen();
//                s.fadeIn(); // Inicia el fade-in
//                // Puedes simular un tiempo y luego el fade-out
//            }
//        });
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpbBarraDeprogreso = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(153, 0, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ahorrar-dinero.png"))); // NOI18N

        jpbBarraDeprogreso.setForeground(new java.awt.Color(153, 0, 255));

        jLabel2.setBackground(new java.awt.Color(153, 0, 255));
        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 255));
        jLabel2.setText("CARGANDO ...");

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 255));
        jLabel3.setText("© 2025 Sistema Contable.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jpbBarraDeprogreso, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(178, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(217, 217, 217))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpbBarraDeprogreso, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(jLabel3))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

//    public static void main(String args[]) {
//  
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
//            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//      
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SplashScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jpbBarraDeprogreso;
    // End of variables declaration//GEN-END:variables
}
