/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;
/**
 *
 * @author PC
 */
public class MayorDto {
    private Date fecha;
    private int partida;
    private String descripcion;
    private double cargo;
    private double abono;

    public MayorDto(Date fecha, int partida, String descripcion, double cargo, double abono) {
        this.fecha = fecha;
        this.partida = partida;
        this.descripcion = descripcion;
        this.cargo = cargo;
        this.abono = abono;
    }

    public MayorDto() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPartida() {
        return partida;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCargo() {
        return cargo;
    }

    public void setCargo(double cargo) {
        this.cargo = cargo;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }
    
    
}
