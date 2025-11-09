package Utilidades;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author Michael Ramos;
**/


public class Estilos {
    
    public static void aplicarEstiloBoton(JButton boton) {
        boton.setBackground(new Color(153, 51, 255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Roboto Black", Font.BOLD, 18));
        // ... etc.
    }
}
