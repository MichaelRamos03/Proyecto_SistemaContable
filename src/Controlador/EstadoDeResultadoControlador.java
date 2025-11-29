

package Controlador;

import Dao.PartidaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import Vista.EstadoDeResultado;
/**
 *
 * @author Michael Ramos;
 * 
**/
public class EstadoDeResultadoControlador implements ActionListener {

    private EstadoDeResultado vista;
    private PartidaDao dao;

    public EstadoDeResultadoControlador(EstadoDeResultado vista) {
        this.vista = vista;
        this.dao = new PartidaDao();
        this.vista.btnCalcular.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this); 
        
        
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnCalcular) {
            pedirInventarioYCalcular();
        }
    }

    private void pedirInventarioYCalcular() {
        String respuesta = Utilidades.NotificadorUtils.pregunta("Ingrese el valor del Inventario Final:");

 
        if (respuesta == null || respuesta.trim().isEmpty()) {
            return; 
        }

        try {

            double inventarioFinal = Double.parseDouble(respuesta);
            

            calcularEstadoResultados(inventarioFinal);
            
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Por favor ingrese un número válido.");
        }
    }

    private void calcularEstadoResultados(double invFinal) {
        // 1. OBTENER FECHAS
        java.sql.Date desde = new java.sql.Date(vista.rsDesde.getDatoFecha().getTime());
        java.sql.Date hasta = new java.sql.Date(vista.rsHasta.getDatoFecha().getTime());
        
        

        double ventasTotales = dao.obtenerSaldoPorCodigo("5101", desde, hasta, "ACREEDORA");

        double rebajasVentas = dao.obtenerSaldoPorCodigo("4103", desde, hasta, "DEUDORA");

        double compras = dao.obtenerSaldoPorCodigo("4101", desde, hasta, "DEUDORA");
 
        double gastosCompra = dao.obtenerSaldoPorCodigo("4102", desde, hasta, "DEUDORA");

        double rebajasCompras = dao.obtenerSaldoPorCodigo("5102", desde, hasta, "ACREEDORA");
        
        double invInicial = dao.obtenerSaldoPorCodigo("111101", desde, hasta, "DEUDORA"); 


        double gastosAdmin = dao.obtenerSaldoPorCodigo("4201", desde, hasta, "DEUDORA");
        double gastosVenta = dao.obtenerSaldoPorCodigo("4202", desde, hasta, "DEUDORA");
        double gastosFinan = dao.obtenerSaldoPorCodigo("4203", desde, hasta, "DEUDORA");
        double otrosIngresos = dao.obtenerSaldoPorCodigo("52", desde, hasta, "ACREEDORA");

      

        double ventasNetas = ventasTotales - rebajasVentas;

        double comprasTotales = compras + gastosCompra;
        double comprasNetas = comprasTotales - rebajasCompras;
        double mercanciaDisponible = invInicial + comprasNetas;
        double costoVentas = mercanciaDisponible - invFinal;


        double utilidadBruta = ventasNetas - costoVentas;


        double totalGastosOp = gastosAdmin + gastosVenta;
        double utilidadOperacion = utilidadBruta - totalGastosOp;


        double uai = utilidadOperacion - gastosFinan + otrosIngresos;


        double reservaLegal = 0;
        double isr = 0;

        if (uai > 0) {
            reservaLegal = uai * 0.07; 
            double utilidadDespuesReserva = uai - reservaLegal;
            isr = utilidadDespuesReserva * 0.30; 
        }

        double utilidadNeta = uai - reservaLegal - isr;

        DefaultTableModel model = (DefaultTableModel) vista.Table.getModel();
        model.setRowCount(0);

        Object[] vacio = {"", "", ""};
        
        agregarFila(model, "INGRESOS DE OPERACIÓN", "", "");
        agregarFila(model, "   Ventas Totales", redondear(ventasTotales), "");
        agregarFila(model, "   (-) Rebajas y Dev. s/Ventas", redondear(rebajasVentas), "");
        agregarFila(model, "(=) VENTAS NETAS", "", redondear(ventasNetas));
        
        model.addRow(vacio);
        agregarFila(model, "COSTO DE VENTAS", "", "");
        agregarFila(model, "   Inventario Inicial", redondear(invInicial), "");
        agregarFila(model, "   Compras", redondear(compras), "");
        agregarFila(model, "   (+) Gastos de Compra", redondear(gastosCompra), "");
        agregarFila(model, "   (=) Compras Totales", redondear(comprasTotales), "");
        agregarFila(model, "   (-) Rebajas y Dev s/Compras", redondear(rebajasCompras), "");
        agregarFila(model, "   (=) Compras Netas", redondear(comprasNetas), "");
        agregarFila(model, "   Mercancia Disponible", redondear(mercanciaDisponible), "");
        agregarFila(model, "   (-) Inventario Final", redondear(invFinal), "");
        agregarFila(model, "(=) COSTO DE VENTAS", "", redondear(costoVentas));

        model.addRow(vacio);
        agregarFila(model, "UTILIDAD BRUTA EN VENTAS", "", redondear(utilidadBruta));
        
        model.addRow(vacio);
        agregarFila(model, "GASTOS DE OPERACIÓN", "", "");
        agregarFila(model, "   Gastos de Administración", redondear(gastosAdmin), "");
        agregarFila(model, "   Gastos de Venta", redondear(gastosVenta), "");
        agregarFila(model, "(=) Total Gastos Operación", "", redondear(totalGastosOp));

        model.addRow(vacio);
        agregarFila(model, "UTILIDAD DE OPERACIÓN", "", redondear(utilidadOperacion));
        
        agregarFila(model, "   (-) Gastos Financieros", redondear(gastosFinan), "");
        agregarFila(model, "   (+) Otros Ingresos", redondear(otrosIngresos), "");
        
        model.addRow(vacio);
        agregarFila(model, "UTILIDAD ANTES DE IMPUESTOS", "", redondear(uai));
        agregarFila(model, "   (-) Reserva Legal (7%)", "", redondear(reservaLegal));
        agregarFila(model, "   (-) Impuesto S/Renta", "", redondear(isr));
        
        model.addRow(vacio);
        agregarFila(model, "UTILIDAD NETA DEL EJERCICIO", "", redondear(utilidadNeta));
    }

    private void agregarFila(DefaultTableModel model, String col1, Object col2, Object col3) {
        model.addRow(new Object[]{col1, col2, col3});
    }
    
    private String redondear(double valor) {
        return String.format("%.2f", valor);
    }
}