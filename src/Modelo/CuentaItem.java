
package Modelo;

/**
 *
 * @author Michael Ramos;
**/

public class CuentaItem {
    
    private String codigo;
    private String nombre;

    public CuentaItem(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre; 
    }
}