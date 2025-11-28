package Controlador;

import Dao.PartidaDao;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Modelo.CuentaItem;
import Modelo.DetallePartida;
import Vista.CrearPartida;

/**
 *
 * @author Michael Ramos;
*
 */
public class CrearPartidaControlador implements ActionListener {

    private CrearPartida vista;
    private PartidaDao dao;
    private DefaultTableModel modeloTabla;


    final String COD_IVA_CREDITO = "111401"; 
    final String COD_IVA_DEBITO = "211001"; 

    public CrearPartidaControlador(CrearPartida vista) {
        this.vista = vista;
        this.dao = new PartidaDao();
        this.modeloTabla = (DefaultTableModel) vista.Tabla.getModel();
        this.modeloTabla.setRowCount(0);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);   
        this.vista.btnGenerarPartida.addActionListener(this);

        init();
    }

    private void init() {
        vista.setLocationRelativeTo(null);
        vista.setTitle("Registro de Asientos Contables");
        llenarComboCuentas();
        activarSincronizacion();
    }

    private void llenarComboCuentas() {
        vista.cbCuentas.removeAllItems();
        vista.cbCuentas.addItem(new CuentaItem("", "--- Seleccione una Cuenta ---"));

        List<CuentaItem> lista = dao.obtenerCuentasParaCombo();
        for (CuentaItem item : lista) {
            vista.cbCuentas.addItem(item);
        }
    }

    private void activarSincronizacion() {
        vista.cbCuentas.addActionListener(e -> {

            if (!vista.txtCodigo.isFocusOwner() && vista.cbCuentas.getSelectedItem() != null) {
                CuentaItem item = (CuentaItem) vista.cbCuentas.getSelectedItem();
                if (item.getCodigo().equals("")) {
                    vista.txtCodigo.setText("");
                } else {
                    vista.txtCodigo.setText(item.getCodigo());
                }
            }
        });

        vista.txtCodigo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscarEnTiempoReal();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscarEnTiempoReal();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscarEnTiempoReal();
            }
        });
    }

    private void buscarYSeleccionarCuenta() {
        String codigoBuscado = vista.txtCodigo.getText().trim();
        boolean encontrado = false;
        for (int i = 0; i < vista.cbCuentas.getItemCount(); i++) {
            CuentaItem item = (CuentaItem) vista.cbCuentas.getItemAt(i);
            if (item.getCodigo().equals(codigoBuscado)) {
                vista.cbCuentas.setSelectedIndex(i);
                vista.txtDebe.requestFocus();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(vista, "Código no encontrado");
        }
    }

  @Override
public void actionPerformed(ActionEvent e) {
    
    if (e.getSource() == vista.btnAgregar) {
        agregarFilaALaTabla();
    } 
    else if (e.getSource() == vista.btnEliminar) {
        eliminarFila();
    }
    else if (e.getSource() == vista.btnGenerarPartida) {
        guardarPartidaEnBD();
    }
}

    private void agregarFilaALaTabla() {
        if (vista.cbCuentas.getSelectedItem() == null) {
            return;
        }
        CuentaItem cuenta = (CuentaItem) vista.cbCuentas.getSelectedItem();

        double debe = 0, haber = 0;
        try {
            if (!vista.txtDebe.getText().isEmpty()) {
                debe = Double.parseDouble(vista.txtDebe.getText());
            }
            if (!vista.txtHaber.getText().isEmpty()) {
                haber = Double.parseDouble(vista.txtHaber.getText());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese montos válidos");
            return;
        }

        if (debe == 0 && haber == 0) {
            return; 
        }
        agregarFilaVisual(cuenta.getCodigo(), cuenta.getNombre(), debe, haber);

        if (vista.jcbIva.isSelected()) {
            double montoIVA = 0;
            if (debe > 0) {
                montoIVA = debe * 0.13;
                agregarFilaVisual(COD_IVA_CREDITO, "IVA CRÉDITO FISCAL", montoIVA, 0);
            }
            else if (haber > 0) {
                montoIVA = haber * 0.13;
                agregarFilaVisual(COD_IVA_DEBITO, "IVA DÉBITO FISCAL", 0, montoIVA);
            }
        }

        calcularTotales();
        vista.txtDebe.setText("");
        vista.txtHaber.setText("");
        vista.txtCodigo.requestFocus();
    }

    private void agregarFilaVisual(String cod, String nom, double d, double h) {
        modeloTabla.addRow(new Object[]{cod, nom, d, h});
    }

    private void calcularTotales() {
        double totalDebe = 0;
        double totalHaber = 0;

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            totalDebe += Double.parseDouble(modeloTabla.getValueAt(i, 2).toString());
            totalHaber += Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
        }
        
        
        vista.lbTotalDebe.setText(String.format("%.2f", totalDebe).replace(",", "."));
        vista.lbTotalHaber.setText(String.format("%.2f", totalHaber).replace(",", "."));
    }

    private void guardarPartidaEnBD() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "La tabla está vacía");
            return;
        }

        double tDebe = Double.parseDouble(vista.lbTotalDebe.getText());
        double tHaber = Double.parseDouble(vista.lbTotalHaber.getText());

        if (Math.abs(tDebe - tHaber) > 0.01) {
            JOptionPane.showMessageDialog(vista, "¡LA PARTIDA NO CUADRA!\nDebe: " + tDebe + "\nHaber: " + tHaber);
            return;
        }

        List<DetallePartida> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            detalles.add(new DetallePartida(
                    modeloTabla.getValueAt(i, 0).toString(), 
                    modeloTabla.getValueAt(i, 1).toString(), 
                    Double.parseDouble(modeloTabla.getValueAt(i, 2).toString()), 
                    Double.parseDouble(modeloTabla.getValueAt(i, 3).toString())
            ));
        }

        java.util.Date fechaJ = vista.rsFecha.getDatoFecha();
        if (fechaJ == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fecha");
            return;
        }
        java.sql.Date fechaSQL = new java.sql.Date(fechaJ.getTime());
        String descripcion = vista.txtDescripcion.getText();


        if (dao.guardarPartida(fechaSQL, descripcion, detalles)) {
            JOptionPane.showMessageDialog(vista, "¡Partida Guardada Exitosamente!");
   
            modeloTabla.setRowCount(0);
            vista.txtDescripcion.setText("");
            calcularTotales();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar en Base de Datos");
        }
    }

    private void buscarEnTiempoReal() {
        if (!vista.txtCodigo.isFocusOwner()) {
            return;
        }

        String codigoBuscado = vista.txtCodigo.getText().trim();
        boolean encontrado = false;

        if (codigoBuscado.isEmpty()) {
            vista.cbCuentas.setSelectedIndex(0);
            return;
        }

        for (int i = 1; i < vista.cbCuentas.getItemCount(); i++) {
            CuentaItem item = (CuentaItem) vista.cbCuentas.getItemAt(i);

            if (item.getCodigo().equals(codigoBuscado)) {
                vista.cbCuentas.setSelectedIndex(i);
                encontrado = true;
                break;
            }

            //  Coincidencia Parcial (Como autocompletado)
            // al escribir "11" ya te seleccione "1101 - CAJA"
            /* if (item.getCodigo().startsWith(codigoBuscado)) {
            vista.cboCuenta.setSelectedIndex(i);
            encontrado = true;
            break; 
        } 
             */
        }

        if (!encontrado) {
            vista.cbCuentas.setSelectedIndex(0);
        }
    }
    
    private void eliminarFila() {
    int filaSeleccionada = vista.Tabla.getSelectedRow();
    
    if (filaSeleccionada >= 0) {
        
        int respuesta = JOptionPane.showConfirmDialog(vista, "¿Seguro que deseas eliminar esta cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            modeloTabla.removeRow(filaSeleccionada);
            
            calcularTotales(); 
            
            JOptionPane.showMessageDialog(vista, "Fila eliminada.");
        }
        
    } else {
        JOptionPane.showMessageDialog(vista, "Por favor, selecciona una fila en la tabla para eliminar.");
    }
}

}
