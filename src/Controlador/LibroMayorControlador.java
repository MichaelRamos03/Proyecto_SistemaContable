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
import java.sql.Date;
import rojeru_san.componentes.RSDateChooser; // Import necesario para el tipo de componente de fecha

public class LibroMayorControlador implements ActionListener {

    private final LibroMayor vista;
    private final MayorDao dao;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public LibroMayorControlador(LibroMayor vista) {
        this.vista = vista;
        this.dao = new MayorDao();

        // Enlace de los botones
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);

        init();
    }

    private void init() {
        vista.setLocationRelativeTo(null);
        vista.setTitle("Libro Mayor");
        llenarComboCuentas();
        // Sincronización del JTextField y RSComboBox
        activarSincronizacion();
        // Carga Inicial: Mayor General completo (null, null)
        mostrarMayorGeneral(null, null);
    }

    // Método auxiliar para obtener fechas (convierte util.Date a sql.Date)
    // Devuelve null si no hay fecha seleccionada.
    private java.sql.Date obtenerFechas(RSDateChooser rsDate) {
        if (rsDate.getDatoFecha() == null) {
            return null;
        }
        return new java.sql.Date(rsDate.getDatoFecha().getTime());
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

    // =========================================================================
    // LÓGICA DE EVENTOS (BUSCAR Y LIMPIAR)
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnLimpiar) {
            limpiarFiltrosYRecargarGeneral();
        } else if (e.getSource() == vista.btnBuscar) {
            String codigoCuenta = vista.txtCodigo.getText().trim();

            Date fechaDesde = obtenerFechas(vista.rsDesde);
            Date fechaHasta = obtenerFechas(vista.rsHasta);

            if (!codigoCuenta.isEmpty()) {
                // MODO 1: Búsqueda por cuenta (con o sin fechas)
                mostrarMayor(fechaDesde, fechaHasta);
            } else {
                // MODO 2: Mayor General (con o sin fechas)
                mostrarMayorGeneral(fechaDesde, fechaHasta);
            }
        }
    }

    /**
     * Limpia todos los campos de filtro y recarga el Mayor General completo
     * (todo el historial).
     */
    private void limpiarFiltrosYRecargarGeneral() {
        // 1. Limpiar campos de filtro
        vista.txtCodigo.setText("");
        vista.rsDesde.setDatoFecha(null); // Esto es CLAVE: pone la fecha en null
        vista.rsHasta.setDatoFecha(null); // Esto es CLAVE: pone la fecha en null
        vista.cbCuentasM.setSelectedIndex(0);

        // 2. Recargar el Mayor General completo (pasa null, null)
        mostrarMayorGeneral(null, null);

        JOptionPane.showMessageDialog(vista, "Filtros limpiados. Mostrando Mayor General completo.", "Filtros Resetados", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    // MODALIDAD 1: BÚSQUEDA ESPECÍFICA (Por cuenta con o sin filtro de fechas)
    // =========================================================================
    private void mostrarMayor(Date fechaDesde, Date fechaHasta) {
        String codigoCuenta = vista.txtCodigo.getText().trim();

        // Establecer fechas comodín si son nulas: 1970-01-01 hasta la fecha actual
        Date fD = (fechaDesde != null) ? fechaDesde : new Date(0);
        Date fH = (fechaHasta != null) ? fechaHasta : new Date(new java.util.Date().getTime());

        CuentaMayor cuentaInfo = null;
        List<MayorDto> lsTransaccion = null;

        try {
            cuentaInfo = dao.obtenerInfoCuentaPorCodigo(codigoCuenta);
            // El DAO usa fD y fH, que pueden ser fechas específicas o comodín.
            lsTransaccion = dao.obtenerCuentaMayor(codigoCuenta, fD, fH);

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
            JOptionPane.showMessageDialog(vista, "Esta cuenta no tiene transacciones registradas en el rango seleccionado.", "Sin Registros", JOptionPane.INFORMATION_MESSAGE);
        }

        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"FECHA", "DESCRIPCION", "REF", "DEBE", "HABER", "SALDO"};
        modelo.setColumnIdentifiers(titulos);

        double saldoAcum = 0;
        String naturalezaCuenta = cuentaInfo.getTipoCuenta().toUpperCase();
        String naturalezaFinalEnTabla = "";

        for (MayorDto x : lsTransaccion) {
            // Cálculo del saldo acumulado
            if (naturalezaCuenta.equals("DEUDORA")) {
                saldoAcum = saldoAcum + x.getCargo() - x.getAbono();
            } else if (naturalezaCuenta.equals("ACREEDORA")) {
                saldoAcum = saldoAcum - x.getCargo() + x.getAbono();
            }

            // Determinación de la naturaleza del saldo actual
            if (saldoAcum == 0) {
                naturalezaFinalEnTabla = "";
            } else if ((naturalezaCuenta.equals("DEUDORA") && saldoAcum > 0) || (naturalezaCuenta.equals("ACREEDORA") && saldoAcum < 0)) {
                naturalezaFinalEnTabla = "DEUDORA";
            } else {
                naturalezaFinalEnTabla = "ACREEDORA";
            }

            Object datos[] = {
                x.getFecha(), x.getDescripcion(), x.getPartida(),
                x.getCargo() > 0 ? "$" + formatter.format(x.getCargo()) : "",
                x.getAbono() > 0 ? "$" + formatter.format(x.getAbono()) : "",
                "$" + formatter.format(Math.abs(saldoAcum)) + " " + naturalezaFinalEnTabla
            };
            modelo.addRow(datos);
        }

        vista.TablaM.setModel(modelo);

        String naturalezaFinalReporte;
        double saldoAbsoluto = Math.abs(saldoAcum);
        // Lógica de saldo final
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

    // =========================================================================
    // MODALIDAD 2: MAYOR GENERAL (Todas las cuentas con o sin filtro de fechas)
    // =========================================================================
    private void mostrarMayorGeneral(Date fechaDesde, Date fechaHasta) {

        // Fechas Comodín (todo el historial) si los parámetros son null
        Date fD = (fechaDesde != null) ? fechaDesde : new Date(0);
        Date fH = (fechaHasta != null) ? fechaHasta : new Date(new java.util.Date().getTime());

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
                    // LLAMADA: se usa el código de cuenta y el rango de fechas (filtrado o comodín)
                    List<MayorDto> lsTransaccion = dao.obtenerCuentaMayor(codigoCuenta, fD, fH);

                    if (!lsTransaccion.isEmpty()) {

                        // Encabezado de la Cuenta
                        modelo.addRow(new Object[]{"", "", "", "", "", "", ""});
                        modelo.addRow(new Object[]{
                            "CUENTA", codigoCuenta, cuentaInfo.getNombre(), "INICIO", "", "", ""
                        });

                        double saldoAcum = 0;
                        String naturalezaCuenta = cuentaInfo.getTipoCuenta().toUpperCase();

                        for (MayorDto x : lsTransaccion) {
                            if (naturalezaCuenta.equals("DEUDORA")) {
                                saldoAcum = saldoAcum + x.getCargo() - x.getAbono();
                            } else if (naturalezaCuenta.equals("ACREEDORA")) {
                                saldoAcum = saldoAcum - x.getCargo() + x.getAbono();
                            }

                            String naturalezaEnTabla = (saldoAcum == 0) ? "" : ((naturalezaCuenta.equals("DEUDORA") && saldoAcum >= 0) || (naturalezaCuenta.equals("ACREEDORA") && saldoAcum < 0) ? "DEUDORA" : "ACREEDORA");

                            Object datos[] = {
                                x.getFecha(), "", x.getDescripcion(), x.getPartida(),
                                x.getCargo() > 0 ? "$" + formatter.format(x.getCargo()) : "",
                                x.getAbono() > 0 ? "$" + formatter.format(x.getAbono()) : "",
                                "$" + formatter.format(Math.abs(saldoAcum)) + " " + (saldoAcum == 0 ? "" : naturalezaEnTabla)
                            };
                            modelo.addRow(datos);
                        }

                        // Subtotal de la Cuenta
                        String naturalezaFinalReporte = (saldoAcum == 0) ? "CERO" : (naturalezaCuenta.equals("DEUDORA") ? (saldoAcum > 0 ? "DEUDOR" : "ACREEDOR") : (saldoAcum > 0 ? "ACREEDOR" : "DEUDOR"));

                        modelo.addRow(new Object[]{
                            "", "", "SALDO FINAL", "", "", "",
                            "$" + formatter.format(Math.abs(saldoAcum)) + " SALDO " + naturalezaFinalReporte
                        });
                        modelo.addRow(new Object[]{"", "", "", "", "", "", ""});
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
