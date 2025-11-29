/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Dao.MayorDao;
import Modelo.CuentaItem;
import Modelo.CuentaMayor;
import Modelo.MayorDto;
import Vista.LibroMayor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class LibroMayorControlador implements ActionListener {

    private LibroMayor vista;
    private MayorDao dao;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public LibroMayorControlador(LibroMayor vista) {
        this.vista = vista;
        this.dao = new MayorDao();
        this.vista.btnBuscar.addActionListener(this);

        init();
    }

    private void init() {
        vista.setLocationRelativeTo(null);
        vista.setTitle("Libro Mayor");
        llenarComboCuentas();
        activarSincronizacion();
        mostrarMayorGeneral();
    }

    private void llenarComboCuentas() {
        vista.cbCuentasM.removeAllItems();
        vista.cbCuentasM.addItem(new CuentaItem("", "--- Seleccione una Cuenta ---"));

        List<CuentaItem> lista = dao.obtenerCuentasParaCombo();
        for (CuentaItem item : lista) {
            vista.cbCuentasM.addItem(item);
        }
    }

    private void activarSincronizacion() {
        vista.cbCuentasM.addActionListener(e -> {

            if (!vista.txtCodigo.isFocusOwner() && vista.cbCuentasM.getSelectedItem() != null) {
                CuentaItem item = (CuentaItem) vista.cbCuentasM.getSelectedItem();
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

    private void buscarEnTiempoReal() {
        if (!vista.txtCodigo.isFocusOwner()) {
            return;
        }

        String codigoBuscado = vista.txtCodigo.getText().trim();
        boolean encontrado = false;

        if (codigoBuscado.isEmpty()) {
            vista.cbCuentasM.setSelectedIndex(0);
            return;
        }

        for (int i = 1; i < vista.cbCuentasM.getItemCount(); i++) {
            CuentaItem item = (CuentaItem) vista.cbCuentasM.getItemAt(i);

            if (item.getCodigo().equals(codigoBuscado)) {
                vista.cbCuentasM.setSelectedIndex(i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            vista.cbCuentasM.setSelectedIndex(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            String codigoCuenta = vista.txtCodigo.getText().trim();

            if (codigoCuenta.isEmpty()) {
                mostrarMayorGeneral();
            } else {
                mostrarMayor();
            }
        }
    }

    private void mostrarMayor() {
        String codigoCuenta = vista.txtCodigo.getText().trim();

        if (codigoCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar o ingresar un código de cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
            vista.TablaM.setModel(new DefaultTableModel());
            vista.txtSaldoFinal.setText("$0.00");
            return;
        }

        CuentaMayor cuentaInfo = null;
        List<MayorDto> lsTransaccion = null;

        try {
            cuentaInfo = dao.obtenerInfoCuentaPorCodigo(codigoCuenta);
            lsTransaccion = dao.obtenerCuentaMayor(codigoCuenta);

        } catch (Exception ex) {
            Logger.getLogger(LibroMayorControlador.class.getName()).log(Level.SEVERE, "Error DB al cargar Mayor", ex);
            JOptionPane.showMessageDialog(vista, "Error al consultar transacciones: " + ex.getMessage(), "Error en la Base de Datos", JOptionPane.ERROR_MESSAGE);
            vista.TablaM.setModel(new DefaultTableModel());
            vista.txtSaldoFinal.setText("$0.00");
            return;
        }

        if (cuentaInfo == null) {
            JOptionPane.showMessageDialog(vista, "Código de cuenta no encontrado.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            vista.TablaM.setModel(new DefaultTableModel());
            vista.txtSaldoFinal.setText("$0.00");
            return;
        }

        if (lsTransaccion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Esta cuenta no tiene transacciones registradas.", "Sin Registros", JOptionPane.INFORMATION_MESSAGE);
            vista.TablaM.setModel(new DefaultTableModel());
            vista.txtSaldoFinal.setText("$0.00");
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"FECHA", "DESCRIPCION", "REF", "DEBE", "HABER", "SALDO"};
        modelo.setColumnIdentifiers(titulos);

        double saldoAcum = 0;
        String naturalezaCuenta = cuentaInfo.getTipoCuenta().toUpperCase(); 
        String naturalezaFinalEnTabla = "";

        for (MayorDto x : lsTransaccion) {

            if (naturalezaCuenta.equals("DEUDORA")) {
                saldoAcum = saldoAcum + x.getCargo() - x.getAbono();

            } else if (naturalezaCuenta.equals("ACREEDORA")) {
                saldoAcum = saldoAcum - x.getCargo() + x.getAbono();
            }

            if (saldoAcum == 0) {
                naturalezaFinalEnTabla = "";
            } else if ((naturalezaCuenta.equals("DEUDORA") && saldoAcum > 0) || (naturalezaCuenta.equals("ACREEDORA") && saldoAcum < 0)) {
                naturalezaFinalEnTabla = "DEUDORA";
            } else {
                naturalezaFinalEnTabla = "ACREEDORA";
            }

            Object datos[] = {
                x.getFecha(),
                x.getDescripcion(),
                x.getPartida(),
                x.getCargo() > 0 ? "$" + formatter.format(x.getCargo()) : "",
                x.getAbono() > 0 ? "$" + formatter.format(x.getAbono()) : "",

                "$" + formatter.format(Math.abs(saldoAcum)) + " " + naturalezaFinalEnTabla
            };
            modelo.addRow(datos);
        }

        vista.TablaM.setModel(modelo);

        String naturalezaFinalReporte;
        double saldoAbsoluto = Math.abs(saldoAcum);

        if (saldoAcum == 0) {
            naturalezaFinalReporte = "CERO";
        } else if (naturalezaCuenta.equals("DEUDORA")) {
            naturalezaFinalReporte = (saldoAcum > 0) ? "DEUDOR" : "ACREEDOR";
        } else { 
            naturalezaFinalReporte = (saldoAcum > 0) ? "ACREEDOR" : "DEUDOR";
        }

        String resultadoFormateado = "$" + formatter.format(saldoAbsoluto);
        resultadoFormateado += " SALDO " + naturalezaFinalReporte;

        vista.txtSaldoFinal.setText(resultadoFormateado);
    }

    private void mostrarMayorGeneral() {
        List<String> codigosDeCuenta = dao.obtenerTodosLosCodigosDeCuenta();

        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"FECHA", "CUENTA", "DESCRIPCION", "REF", "DEBE", "HABER", "SALDO"};
        modelo.setColumnIdentifiers(titulos);

        if (codigosDeCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron cuentas con movimientos.", "Sin Registros", JOptionPane.INFORMATION_MESSAGE);
            vista.TablaM.setModel(modelo);
            vista.txtSaldoFinal.setText("N/A");
            return;
        }

        try {
            for (String codigoCuenta : codigosDeCuenta) {

                CuentaMayor cuentaInfo = dao.obtenerInfoCuentaPorCodigo(codigoCuenta);

                if (cuentaInfo != null) {

                    List<MayorDto> lsTransaccion = dao.obtenerCuentaMayor(codigoCuenta);

                    if (!lsTransaccion.isEmpty()) {

                        modelo.addRow(new Object[]{"", "", "", "", "", "", ""});
                        modelo.addRow(new Object[]{
                            "CUENTA",
                            codigoCuenta + " - " + cuentaInfo.getNombre(),
                            "INICIO",
                            "", "", "", ""
                        });

                        double saldoAcum = 0;
                        String naturalezaCuenta = cuentaInfo.getTipoCuenta().toUpperCase();

                        for (MayorDto x : lsTransaccion) {

                            // Lógica contable
                            if (naturalezaCuenta.equals("DEUDORA")) {
                                saldoAcum = saldoAcum + x.getCargo() - x.getAbono();
                            } else if (naturalezaCuenta.equals("ACREEDORA")) {
                                saldoAcum = saldoAcum - x.getCargo() + x.getAbono();
                            }

                            String naturalezaEnTabla = (saldoAcum == 0) ? "" : (saldoAcum > 0 ? "DEUDORA" : "ACREEDORA");

                            Object datos[] = {
                                x.getFecha(),
                                "",
                                x.getDescripcion(),
                                x.getPartida(),
                                x.getCargo() > 0 ? "$" + formatter.format(x.getCargo()) : "",
                                x.getAbono() > 0 ? "$" + formatter.format(x.getAbono()) : "",
                                "$" + formatter.format(Math.abs(saldoAcum)) + " " + naturalezaEnTabla
                            };
                            modelo.addRow(datos);
                        }

                        String naturalezaFinalReporte = (saldoAcum == 0) ? "CERO" : (saldoAcum > 0 ? "DEUDOR" : "ACREEDOR");

                        modelo.addRow(new Object[]{
                            "", "", "",
                            "SALDO FINAL",
                            "",
                            "",
                            "$" + formatter.format(Math.abs(saldoAcum)) + " " + naturalezaFinalReporte
                        });
                        modelo.addRow(new Object[]{"", "", "", "", "", "", ""}); // Fila en blanco
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(LibroMayorControlador.class.getName()).log(Level.SEVERE, "Error al generar Mayor General", ex);
            JOptionPane.showMessageDialog(vista, "Error fatal al procesar el Mayor General: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
        }

        vista.TablaM.setModel(modelo);
        vista.txtSaldoFinal.setText("MAYOR GENERAL");
    }

}
