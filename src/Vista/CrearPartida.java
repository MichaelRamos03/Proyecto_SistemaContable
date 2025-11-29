
package Vista;


/**
 *
 * @author Michael Ramos;
 */
public class CrearPartida extends javax.swing.JFrame {

    public CrearPartida() {
        initComponents();
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//        Img.setBorder(new Utilidades.Fondo("/Imagenes/1.jpeg"));
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbTotalDebe = new javax.swing.JLabel();
        lbTotalHaber = new javax.swing.JLabel();
        btnGenerarPartida = new javax.swing.JButton();
        rsFecha = new rojeru_san.componentes.RSDateChooser();
        txtHaber = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        cbCuentas = new RSMaterialComponent.RSComboBox();
        txtCodigo = new javax.swing.JTextField();
        txtDebe = new javax.swing.JTextField();
        lbError = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        jcbIva = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Fondo.setBackground(new java.awt.Color(255, 255, 255));
        Fondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Haber: ");
        Fondo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 70, 40));

        jLabel2.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Fecha:");
        Fondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 60, 20));

        jLabel1.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Descripción:");
        Fondo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 110, 40));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cuenta:");
        Fondo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 210, 70, 40));

        jSeparator1.setBackground(new java.awt.Color(153, 51, 255));
        jSeparator1.setForeground(new java.awt.Color(153, 51, 255));
        Fondo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1030, 10));

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 51, 255));
        jLabel5.setText("Creando Partida Contable");
        Fondo.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 490, 60));

        jSeparator2.setBackground(new java.awt.Color(153, 51, 255));
        jSeparator2.setForeground(new java.awt.Color(153, 51, 255));
        Fondo.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1030, 10));

        Tabla.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Cuenta", "Debe", "Haber"
            }
        ));
        Tabla.setGridColor(new java.awt.Color(153, 102, 255));
        Tabla.setRowHeight(25);
        Tabla.setShowGrid(false);
        jScrollPane1.setViewportView(Tabla);

        Fondo.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 950, 280));

        jLabel6.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Codigo:");
        Fondo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 70, 40));

        jLabel7.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Debe:");
        Fondo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 70, 40));

        btnAgregar.setBackground(new java.awt.Color(153, 51, 255));
        btnAgregar.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Registrar +");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        Fondo.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 370, 130, 40));

        jLabel8.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Debe:");
        Fondo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 740, 70, 40));

        jLabel9.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Haber: ");
        Fondo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 740, 70, 40));

        lbTotalDebe.setBackground(new java.awt.Color(204, 204, 204));
        lbTotalDebe.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lbTotalDebe.setForeground(new java.awt.Color(0, 0, 0));
        lbTotalDebe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Fondo.add(lbTotalDebe, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 740, 70, 40));

        lbTotalHaber.setBackground(new java.awt.Color(204, 204, 204));
        lbTotalHaber.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lbTotalHaber.setForeground(new java.awt.Color(0, 0, 0));
        lbTotalHaber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Fondo.add(lbTotalHaber, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 740, 70, 40));

        btnGenerarPartida.setBackground(new java.awt.Color(153, 51, 255));
        btnGenerarPartida.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        btnGenerarPartida.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarPartida.setText("Guardar Partida");
        btnGenerarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPartidaActionPerformed(evt);
            }
        });
        Fondo.add(btnGenerarPartida, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 740, 170, 40));

        rsFecha.setColorBackground(new java.awt.Color(153, 51, 255));
        rsFecha.setColorButtonHover(new java.awt.Color(153, 51, 255));
        rsFecha.setColorForeground(new java.awt.Color(0, 0, 0));
        rsFecha.setFuente(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        Fondo.add(rsFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 220, 40));

        txtHaber.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        txtHaber.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 51, 255), 2, true));
        Fondo.add(txtHaber, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 280, 130, 40));

        txtDescripcion.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        txtDescripcion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 51, 255), 2, true));
        Fondo.add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 360, 40));

        cbCuentas.setColorArrow(new java.awt.Color(153, 51, 255));
        cbCuentas.setColorFondo(new java.awt.Color(153, 51, 255));
        cbCuentas.setColorSeleccion(new java.awt.Color(153, 51, 255));
        Fondo.add(cbCuentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 240, -1));

        txtCodigo.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        txtCodigo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 51, 255), 2, true));
        Fondo.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 180, 40));

        txtDebe.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        txtDebe.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 51, 255), 2, true));
        Fondo.add(txtDebe, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 140, 40));
        Fondo.add(lbError, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 730, 360, 50));

        btnEliminar.setBackground(new java.awt.Color(153, 51, 255));
        btnEliminar.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        Fondo.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 370, 110, 40));

        jcbIva.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jcbIva.setForeground(new java.awt.Color(0, 0, 0));
        jcbIva.setText("Incluye IVA 13%");
        jcbIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbIvaActionPerformed(evt);
            }
        });
        Fondo.add(jcbIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 170, 40));

        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 820));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGenerarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPartidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenerarPartidaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jcbIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbIvaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbIvaActionPerformed

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
//            java.util.logging.Logger.getLogger(CrearPartida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CrearPartida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CrearPartida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CrearPartida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CrearPartida().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    public javax.swing.JTable Tabla;
    public javax.swing.JButton btnAgregar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGenerarPartida;
    public RSMaterialComponent.RSComboBox cbCuentas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JCheckBox jcbIva;
    public javax.swing.JLabel lbError;
    public javax.swing.JLabel lbTotalDebe;
    public javax.swing.JLabel lbTotalHaber;
    public rojeru_san.componentes.RSDateChooser rsFecha;
    public javax.swing.JTextField txtCodigo;
    public javax.swing.JTextField txtDebe;
    public javax.swing.JTextField txtDescripcion;
    public javax.swing.JTextField txtHaber;
    // End of variables declaration//GEN-END:variables
}
