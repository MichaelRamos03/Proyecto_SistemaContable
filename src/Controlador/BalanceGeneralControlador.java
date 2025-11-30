
package Controlador;

import Dao.PartidaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Vista.BalanceGeneral; // Asegúrate que tu vista se llame así


/**
 *
 * @author Michael Ramos;
**/


public class BalanceGeneralControlador implements ActionListener {

    private BalanceGeneral vista;
    private PartidaDao dao;

    public BalanceGeneralControlador(BalanceGeneral vista) {
        this.vista = vista;
        this.dao = new PartidaDao();
        
        this.vista.btnGenerar.addActionListener(this);
        // Si tienes botón limpiar: this.vista.btnLimpiar.addActionListener(this);
        
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Balance General");
        
        // Formato visual a la tabla (opcional)
        vista.Table.setRowHeight(25);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGenerar) {
            generarBalance();
        }
    }

    private void generarBalance() {
        // 1. VALIDAR FECHA DE CORTE (La foto)
        if (vista.rsFecha.getDatoFecha() == null) {
             JOptionPane.showMessageDialog(vista, "Seleccione la fecha de corte");
             return;
        }
        
        // Fecha Fin (La que eligió el usuario)
        java.sql.Date hasta = new java.sql.Date(vista.rsFecha.getDatoFecha().getTime());
        
        // Fecha Inicio (Calculada: 1 de Enero del mismo año)
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hasta);
        cal.set(java.util.Calendar.MONTH, java.util.Calendar.JANUARY);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        java.sql.Date desde = new java.sql.Date(cal.getTimeInMillis());

        // 2. PEDIR INVENTARIO FINAL (Manual)
        String resp = Utilidades.NotificadorUtils.pregunta("Ingrese el Inventario Final:");
        if (resp == null || resp.trim().isEmpty()) return;
        
        double invFinal = 0;
        try { invFinal = Double.parseDouble(resp); } catch(Exception ex){ return; }

        // 3. OBTENER DATOS DE LA BD (Balanza de Comprobación)
        List<Object[]> balanza = dao.obtenerBalanzaComprobacion(desde, hasta);
        
        // 4. CLASIFICAR Y CALCULAR RESULTADOS
        List<Object[]> activos = new ArrayList<>();
        List<Object[]> pasivos = new ArrayList<>();
        List<Object[]> patrimonio = new ArrayList<>();
        
        double totalIngresos = 0, totalGastos = 0;

        for (Object[] cuenta : balanza) {
            String tipo = (String) cuenta[2]; // ACTIVO, PASIVO, ETC.
            double saldo = (double) cuenta[3];
            
            if (tipo.equals("ACTIVO")) activos.add(cuenta);
            else if (tipo.equals("PASIVO")) pasivos.add(cuenta);
            else if (tipo.equals("PATRIMONIO")) patrimonio.add(cuenta);
            else if (tipo.equals("INGRESO")) totalIngresos += saldo;
            else if (tipo.equals("GASTO")) totalGastos += saldo;
        }

        // --- CÁLCULO DE UTILIDAD E IMPUESTOS ---
        // En método analítico, el Inv Final reduce el costo -> Aumenta utilidad
        // Asumimos que los gastos traen compras brutas
        totalGastos -= invFinal; 

        double utilidadBruta = totalIngresos - totalGastos;
        double reservaLegal = 0;
        double isr = 0;
        double utilidadNeta = utilidadBruta;

        if (utilidadBruta > 0) {
            reservaLegal = utilidadBruta * 0.07;
            double utilidadAntesISR = utilidadBruta - reservaLegal;
            isr = utilidadAntesISR * 0.30;
            utilidadNeta = utilidadAntesISR - isr;
        }

        // --- AGREGAR CUENTAS FALTANTES ---
        // 1. Inventario Final (Activo)
        if (invFinal > 0) activos.add(new Object[]{"111102", "INVENTARIO FINAL", "ACTIVO", invFinal});
        
        // 2. ISR por Pagar (Pasivo)
        if (isr > 0) pasivos.add(new Object[]{"211201", "ISR POR PAGAR", "PASIVO", isr});
        
        // 3. Reserva y Utilidad (Patrimonio)
        if (reservaLegal > 0) patrimonio.add(new Object[]{"310401", "RESERVA LEGAL", "PATRIMONIO", reservaLegal});
        patrimonio.add(new Object[]{"310601", "UTILIDAD DEL EJERCICIO", "PATRIMONIO", utilidadNeta});

        // 5. LLENAR TABLA DOBLE
        llenarTabla(activos, pasivos, patrimonio);
    }

    private void llenarTabla(List<Object[]> activos, List<Object[]> pasivos, List<Object[]> patri) {
        DefaultTableModel model = (DefaultTableModel) vista.Table.getModel();
        model.setRowCount(0);
        
        double sumaActivo = 0;
        double sumaPasivoPatrimonio = 0;

        // Unimos Pasivo y Patrimonio para la derecha
        List<Object[]> derecha = new ArrayList<>(pasivos);
        derecha.addAll(patri);

        int maxFilas = Math.max(activos.size(), derecha.size());

        for (int i = 0; i < maxFilas; i++) {
            // Izquierda (Activo)
            String nAct = "", sAct = "";
            if (i < activos.size()) {
                nAct = (String) activos.get(i)[1];
                double val = (double) activos.get(i)[3];
                sAct = String.format("%.2f", val);
                sumaActivo += val;
            }

            // Derecha (Pasivo + Pat)
            String nPas = "", sPas = "";
            if (i < derecha.size()) {
                nPas = (String) derecha.get(i)[1];
                double val = (double) derecha.get(i)[3];
                sPas = String.format("%.2f", val);
                sumaPasivoPatrimonio += val;
            }

            model.addRow(new Object[]{nAct, sAct, nPas, sPas});
        }

        // MOSTRAR TOTALES EN LAS CAJAS DE ABAJO
        vista.lbTotalActivo.setText(String.format("$ %.2f", sumaActivo));
        vista.lbTotalPasivo.setText(String.format("$ %.2f", sumaPasivoPatrimonio));
    }
}