

package Utilidades;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Michael Ramos;
 * 
**/
public class RenderTableLibroDiario extends DefaultTableCellRenderer {


    private final Color COLOR_MORADO_UI = new Color(115, 60, 180);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setText(value != null ? value.toString() : "");
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.setHorizontalAlignment(JLabel.LEFT);


        Object valorClave = table.getValueAt(row, 3);
        String textoClave = (valorClave != null) ? valorClave.toString() : "";

        if (textoClave.startsWith("PARTIDA N°")) {
            this.setBackground(COLOR_MORADO_UI); 
            this.setForeground(Color.WHITE);     

            if (column == 0) {
                this.setFont(new Font("Tahoma", Font.BOLD, 12));
            } else if (column == 3) { 
                this.setHorizontalAlignment(JLabel.CENTER);
                this.setFont(new Font("Tahoma", Font.BOLD, 14));
            } else {
                this.setText("");
            }
            return this; 
        }

        if (textoClave.trim().startsWith("(") && textoClave.trim().endsWith(")")) {
            this.setBackground(Color.gray); 
            this.setForeground(Color.DARK_GRAY);
            this.setFont(new Font("Tahoma", Font.BOLD, 12));

            if (column == 3) {
                this.setHorizontalAlignment(JLabel.CENTER);
            } else {
                this.setText(""); 
            }
            return this;
        }

        if (column == 5 || column == 6) {
            this.setHorizontalAlignment(JLabel.RIGHT);
        }
        
        if (isSelected) {
            this.setBackground(new Color(230, 230, 250)); 
            this.setForeground(Color.BLACK);
        }

        return this;
    }
}
