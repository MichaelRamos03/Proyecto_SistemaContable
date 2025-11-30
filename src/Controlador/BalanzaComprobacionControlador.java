package Controlador;

import Dao.BalanzaDao;
import Modelo.BalanzaDto;
import Vista.BalanzaComprobacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import rojeru_san.componentes.RSDateChooser;

public class BalanzaComprobacionControlador implements ActionListener{
    
    private final BalanzaComprobacion vista;
    private final BalanzaDao dao;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    


    public BalanzaComprobacionControlador(BalanzaComprobacion vista) {
        this.vista = vista;
        this.dao = new BalanzaDao();
        this.vista.btnBuscar.addActionListener(this);

        this.vista.btnLimpiar.addActionListener(this);
        
        init();
    }

    private void init() {
        vista.setLocationRelativeTo(null);
        vista.setTitle("Balanza de Comprobación");

        generarBalanza(null, null); 
    }

    private java.sql.Date obtenerFechas(RSDateChooser rsDate) {
        if (rsDate.getDatoFecha() == null) {
            return null;
        }
        return new java.sql.Date(rsDate.getDatoFecha().getTime());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnLimpiar) {
            limpiarFiltrosYRecargarGeneral();
        } else if (e.getSource() == vista.btnBuscar) {
            Date fechaDesde = obtenerFechas(vista.rsDesde);
            Date fechaHasta = obtenerFechas(vista.rsHasta);
            

            if ((fechaDesde != null && fechaHasta == null) || (fechaDesde == null && fechaHasta != null)) {
                 JOptionPane.showMessageDialog(vista, "Debe seleccionar un rango de fechas completo o limpiar los campos.", "Filtro Incompleto", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            
            generarBalanza(fechaDesde, fechaHasta);
        }
    }
    

    private void limpiarFiltrosYRecargarGeneral() {

        vista.rsDesde.setDatoFecha(null); 
        vista.rsHasta.setDatoFecha(null); 
        

        generarBalanza(null, null);
        
        JOptionPane.showMessageDialog(vista, "Filtros limpiados. Mostrando Balanza de Comprobación completa.", "Filtros Resetados", JOptionPane.INFORMATION_MESSAGE);
    }


    public void generarBalanza(Date fechaDesde, Date fechaHasta) {
        
        Date fD = (fechaDesde != null) ? fechaDesde : new Date(0); 
        Date fH = (fechaHasta != null) ? fechaHasta : new Date(new java.util.Date().getTime());

        List<BalanzaDto> listaResultados;
        
        try {
            listaResultados = dao.obtenerTotalesBalanza(fD, fH); 
        } catch (SQLException ex) {
            Logger.getLogger(BalanzaComprobacionControlador.class.getName()).log(Level.SEVERE, "Error al cargar Balanza desde DB", ex);
            JOptionPane.showMessageDialog(vista, "Error al cargar Balanza desde DB: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"Código", "Cuenta", "Debe (Sumas)", "Haber (Sumas)", "Saldo Deudor", "Saldo Acreedor"};
        modelo.setColumnIdentifiers(titulos);
        
        double totalSumasDebe = 0;
        double totalSumasHaber = 0;
        double totalSaldoDeudor = 0;
        double totalSaldoAcreedor = 0;

        for (BalanzaDto cuenta : listaResultados) {
            
            double debe = cuenta.getTotalDebe();
            double haber = cuenta.getTotalHaber();
            double saldoFinal = debe - haber; 
            
            double saldoDeudor = 0;
            double saldoAcreedor = 0;
            
            if (saldoFinal > 0) { saldoDeudor = saldoFinal; } 
            else if (saldoFinal < 0) { saldoAcreedor = Math.abs(saldoFinal); }
            
            totalSumasDebe += debe;
            totalSumasHaber += haber;
            totalSaldoDeudor += saldoDeudor;
            totalSaldoAcreedor += saldoAcreedor;

            modelo.addRow(new Object[]{
                cuenta.getCodigoCuenta(),
                cuenta.getNombreCuenta(),
                formatter.format(debe),
                formatter.format(haber),
                formatter.format(saldoDeudor),
                formatter.format(saldoAcreedor)
            });
        }
        
        vista.TablaBalanza.setModel(modelo);
        
        // Mostrar Totales
        vista.txtDebe.setText(formatter.format(totalSumasDebe));
        vista.txtHaber.setText(formatter.format(totalSumasHaber));
        vista.txtDeudor.setText(formatter.format(totalSaldoDeudor));
        vista.txtAcreedor.setText(formatter.format(totalSaldoAcreedor));
        
        if (totalSumasDebe != totalSumasHaber || totalSaldoDeudor != totalSaldoAcreedor) {
             //añadir una advertencia visual a la interfaz de usuario.
        }
    }
}