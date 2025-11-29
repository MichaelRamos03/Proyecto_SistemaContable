/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author PC
 */
public class BalanzaComprobacionControlador implements ActionListener{
private final BalanzaComprobacion vista;
    private final BalanzaDao dao;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public BalanzaComprobacionControlador(BalanzaComprobacion vista) {
        this.vista = vista;
        this.dao = new BalanzaDao();
        
        // Asumiendo que la balanza se carga al inicio, sin botones de búsqueda.
        init();
    }

    private void init() {
        vista.setLocationRelativeTo(null);
        vista.setTitle("Balanza de Comprobación");
        generarBalanza(); // Llama a la función de cálculo al iniciar
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Si hay botones en la vista de Balanza, su lógica iría aquí.
    }

    public void generarBalanza() {
        List<BalanzaDto> listaResultados;
        
        try {
            listaResultados = dao.obtenerTotalesBalanza();
        } catch (SQLException ex) {
            Logger.getLogger(BalanzaComprobacionControlador.class.getName()).log(Level.SEVERE, "Error al cargar Balanza desde DB", ex);
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"Código", "Cuenta", "Debe (Sumas)", "Haber (Sumas)", "Saldo Deudor", "Saldo Acreedor"};
        modelo.setColumnIdentifiers(titulos);
        
        // Acumuladores para los 4 totales en la parte inferior
        double totalSumasDebe = 0;
        double totalSumasHaber = 0;
        double totalSaldoDeudor = 0;
        double totalSaldoAcreedor = 0;

        for (BalanzaDto cuenta : listaResultados) {
            
            double debe = cuenta.getTotalDebe();
            double haber = cuenta.getTotalHaber();
            double saldoFinal = debe - haber; // Positivo = Deudor, Negativo = Acreedor
            
            double saldoDeudor = 0;
            double saldoAcreedor = 0;
            
            // --- 1. Calcular Saldo Deudor/Acreedor ---
            if (saldoFinal > 0) {
                // El saldo es DEUDOR (Debe > Haber)
                saldoDeudor = saldoFinal;
            } else if (saldoFinal < 0) {
                // El saldo es ACREEDOR (Haber > Debe, usamos el valor absoluto)
                saldoAcreedor = Math.abs(saldoFinal);
            }
            
            // --- 2. Acumular los 4 Totales Globales ---
            totalSumasDebe += debe;
            totalSumasHaber += haber;
            totalSaldoDeudor += saldoDeudor;
            totalSaldoAcreedor += saldoAcreedor;

            // --- 3. Añadir Fila a la Tabla ---
            modelo.addRow(new Object[]{
                cuenta.getCodigoCuenta(),
                cuenta.getNombreCuenta(),
                formatter.format(debe),
                formatter.format(haber),
                formatter.format(saldoDeudor),
                formatter.format(saldoAcreedor)
            });
        }
        
        // 4. Mostrar Resultados en la Tabla y en las Etiquetas
        vista.TablaBalanza.setModel(modelo);
        
        // Mapeo a las etiquetas de tu vista:
        // Sumas
        vista.txtDebe.setText(formatter.format(totalSumasDebe));
        vista.txtHaber.setText(formatter.format(totalSumasHaber));
        
        // Saldos
        vista.txtDeudor.setText(formatter.format(totalSaldoDeudor));
        vista.txtAcreedor.setText(formatter.format(totalSaldoAcreedor));
        
        // (Opcional) Puedes añadir lógica aquí para resaltar si NO CUADRA
        if (totalSumasDebe != totalSumasHaber || totalSaldoDeudor != totalSaldoAcreedor) {
            System.out.println("ADVERTENCIA: ¡La Balanza NO Cuadra!");
        }
    }
}
