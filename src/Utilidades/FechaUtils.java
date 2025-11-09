package Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michael Ramos;
 *
 */
public class FechaUtils {

    // Define un formato estándar para todo tu sistema
    private static final SimpleDateFormat FORMATO_FECHA
            = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "";
        }
        return FORMATO_FECHA.format(fecha);
    }

    public static Date parsearFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }
        try {
            FORMATO_FECHA.setLenient(false);
            return FORMATO_FECHA.parse(fechaStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getFechaActualStr() {
        return formatearFecha(new Date());
    }

    public static long getDiferenciaDias(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        long diffEnMilis = fechaFin.getTime() - fechaInicio.getTime();
        return TimeUnit.DAYS.convert(diffEnMilis, TimeUnit.MILLISECONDS);
    }
}
