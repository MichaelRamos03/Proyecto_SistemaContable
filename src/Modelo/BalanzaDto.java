/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author PC
 */
public class BalanzaDto {
    private String codigoCuenta;
    private String nombreCuenta;
    private double totalDebe;
    private double totalHaber;

    public BalanzaDto(String codigoCuenta, String nombreCuenta, double totalDebe, double totalHaber) {
        this.codigoCuenta = codigoCuenta;
        this.nombreCuenta = nombreCuenta;
        this.totalDebe = totalDebe;
        this.totalHaber = totalHaber;
    }

    public BalanzaDto() {
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public double getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(double totalDebe) {
        this.totalDebe = totalDebe;
    }

    public double getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(double totalHaber) {
        this.totalHaber = totalHaber;
    }
    
    
}
