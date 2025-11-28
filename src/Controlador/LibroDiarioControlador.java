package Controlador;

import Dao.PartidaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Vista.LibroDiario;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Michael Ramos;
 *
 */
public class LibroDiarioControlador implements ActionListener {

   private LibroDiario vista;
    private PartidaDao dao;

    public LibroDiarioControlador(LibroDiario vista) {
        this.vista = vista;
        this.dao = new PartidaDao();
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this); 
        vista.rsDesde.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) generarReportePorFechas();
            }
        });
        
        vista.rsHasta.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) generarReportePorFechas();
            }
        });

        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Libro Diario");
        maquillarTable();
        
        mostrarTodoElHistorial(); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            generarReportePorFechas();
        }
         else if (e.getSource() == vista.btnLimpiar) {
            vista.rsDesde.setDatoFecha(null);
            vista.rsHasta.setDatoFecha(null);
            mostrarTodoElHistorial();
        } 
    }

    private void mostrarTodoElHistorial() {
        List<Object[]> datos = dao.obtenerLibroDiarioCompleto();
        llenarTablaConFormato(datos);
    }

    private void generarReportePorFechas() {
        if (vista.rsDesde.getDatoFecha()== null || vista.rsHasta.getDatoFecha()== null) {
            return;
        }

        java.sql.Date fecha1 = new java.sql.Date(vista.rsDesde.getDatoFecha().getTime());
        java.sql.Date fecha2 = new java.sql.Date(vista.rsHasta.getDatoFecha().getTime());

        List<Object[]> datos = dao.obtenerReporteLibroDiario(fecha1, fecha2);
        llenarTablaConFormato(datos);
    }

    private void llenarTablaConFormato(List<Object[]> datos) {
        DefaultTableModel modelo = (DefaultTableModel) vista.Table.getModel();
        modelo.setRowCount(0);

        if (datos.isEmpty()) {
            vista.lbTotalDebe.setText("0.00");
            vista.lbTotalHaber.setText("0.00");
            return;
        }

        int idActual = -1;
        int idAnterior = -1;
        String conceptoPendiente = "";
        double totalGlobalDebe = 0;
        double totalGlobalHaber = 0;

        for (Object[] fila : datos) {
            idActual = (int) fila[1]; 

            if (idActual != idAnterior) {
                if (idAnterior != -1) {
                    agregarFilaConcepto(modelo, conceptoPendiente);
                }
                String fechaStr = fila[0].toString();
                modelo.addRow(new Object[]{fechaStr, "", "", "PARTIDA N° " + idActual, "", "", ""});
                idAnterior = idActual;
            }

            double debe = (double) fila[5];
            double haber = (double) fila[6];
            
            modelo.addRow(new Object[]{
                "", "", fila[2], fila[3], "", 
                (debe > 0) ? String.format("%.2f", debe) : "", 
                (haber > 0) ? String.format("%.2f", haber) : ""
            });

            conceptoPendiente = (String) fila[4];
            totalGlobalDebe += debe;
            totalGlobalHaber += haber;
        }

        if (idActual != -1) {
            agregarFilaConcepto(modelo, conceptoPendiente);
        }

        vista.lbTotalDebe.setText(String.format("$ %.2f", totalGlobalDebe));
        vista.lbTotalHaber.setText(String.format("$ %.2f", totalGlobalHaber));
    }

    private void agregarFilaConcepto(DefaultTableModel modelo, String concepto) {
        modelo.addRow(new Object[]{"", "", "", "   ( " + concepto + " )", "", "", ""});
        modelo.addRow(new Object[]{"", "", "", "", "", "", ""}); 
    }
    
    private void maquillarTable() {

        vista.Table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Fecha
        vista.Table.getColumnModel().getColumn(1).setPreferredWidth(0);  // #
             vista.Table.getColumnModel().getColumn(1).setMaxWidth(0);    // Ocultamos columna #
        vista.Table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Código
        vista.Table.getColumnModel().getColumn(3).setPreferredWidth(250); // CUENTA (Ancha)
        vista.Table.getColumnModel().getColumn(4).setPreferredWidth(0);   // Concepto (Oculto o pequeño)
        vista.Table.getColumnModel().getColumn(4).setMinWidth(0);         // Ocultamos columna concepto
        vista.Table.getColumnModel().getColumn(4).setMaxWidth(0);         // porque el concepto lo ponemos abajo en la fila
        vista.Table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Debe
        vista.Table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Haber

        vista.Table.setRowHeight(25);

        javax.swing.table.DefaultTableCellRenderer derecha = new javax.swing.table.DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

//        vista.Table.getColumnModel().getColumn(5).setCellRenderer(derecha); // Debe a la derecha
//        vista.Table.getColumnModel().getColumn(6).setCellRenderer(derecha); // Haber a la derecha
        
        vista.Table.setDefaultRenderer(Object.class, new Utilidades.RenderTableLibroDiario());

    }
}
