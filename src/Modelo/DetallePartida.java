

package Modelo;

/**
 *
 * @author Michael Ramos;
**/

public class DetallePartida {
    
    private String codigoCuenta;
    private String nombreCuenta;
    private double debe;
    private double haber;

    public DetallePartida(String codigoCuenta, String nombreCuenta, double debe, double haber) {
        this.codigoCuenta = codigoCuenta;
        this.nombreCuenta = nombreCuenta;
        this.debe = debe;
        this.haber = haber;
    }

    public String getCodigoCuenta() { return codigoCuenta; }
    public String getNombreCuenta() { return nombreCuenta; }
    public double getDebe() { return debe; }
    public double getHaber() { return haber; }
}
