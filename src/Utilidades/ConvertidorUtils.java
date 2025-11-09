

package Utilidades;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Michael Ramos;
**/

public class ConvertidorUtils {

    
    public static BigDecimal aBigDecimal(String textoMonto) {
        if (textoMonto == null || textoMonto.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String textoLimpio = textoMonto.replace(",", "");
            return new BigDecimal(textoLimpio);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO; 
        }
    }
    
    public static int aEntero(String texto, int valorPorDefecto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }
    
    public static String formatearMoneda(BigDecimal monto) {  
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.US);
        return formato.format(monto);
    }
}
