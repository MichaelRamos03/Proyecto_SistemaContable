package Utilidades;

import java.util.regex.Pattern;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Michael Ramos;
 *
 *
 */
public class ValidadorUtils {

    // 1. Revisa si un campo de texto está vacío (o tiene el placeholder)
    public static boolean esCampoVacio(JTextField campo, String placeholder) {
        return campo.getText().isEmpty() || campo.getText().equals(placeholder);
    }

    // 3. Revisa si un correo tiene un formato válido (simple)
    public static boolean esEmailValido(String email) {
        if (email == null) {
            return false;
        }
        // Esta es una expresión regular (regex) simple para validar emails
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    // 4. Revisa si dos contraseñas coinciden
    public static boolean contrasenasCoinciden(JPasswordField pass1, JPasswordField pass2) {
        String p1 = new String(pass1.getPassword());
        String p2 = new String(pass2.getPassword());
        return p1.equals(p2);
    }

    public static boolean esSoloNumeros(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        // Revisa si cada carácter en el texto es un dígito
        for (char c : texto.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false; // Se encontró algo que no es un número
            }
        }
        return true; // Todos son números
    }

    // 1. Patrones (Expresiones Regulares) pre-compilados para eficiencia
    // Email (un estándar simple)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    // Contraseña segura: 
    // (?=.*[0-9]) -> al menos un número
    // (?=.*[a-z]) -> al menos una minúscula
    // (?=.*[A-Z]) -> al menos una mayúscula
    // (?=.*[@#$%^&+=!]) -> al menos un carácter especial
    // (?!.*\s) -> sin espacios
    // .{8,} -> al menos 8 caracteres
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?!.*\\s).{8,30}$"
    );

    // Solo letras (permite espacios y acentos)
    private static final Pattern SOLO_LETRAS_PATTERN = Pattern.compile(
            "^[p{L} .'-]+$" // p{L} es "cualquier letra en cualquier idioma"
    );

    // Solo números (permite decimales con punto o coma)
    private static final Pattern SOLO_NUMEROS_DECIMALES_PATTERN = Pattern.compile(
            "^\\d+([.,]\\d{1,2})?$" // 1 o 2 decimales
    );

    // Solo números enteros (sin decimales)
    private static final Pattern SOLO_NUMEROS_ENTEROS_PATTERN = Pattern.compile(
            "^\\d+$"
    );

    /**
     * Valida que la contraseña sea segura. Reglas: Mínimo 8 car, 1 mayúscula, 1
     * minúscula, 1 número, 1 car. especial.
     */
    public static boolean esContrasenaSegura(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Valida si el texto contiene solo letras, espacios y acentos. Útil para
     * nombres de personas.
     */
    public static boolean esSoloLetras(String texto) {
        if (texto == null) {
            return false;
        }
        return SOLO_LETRAS_PATTERN.matcher(texto).matches();
    }
    
    /**
     * Valida si es un número decimal (ej. "123.45" o "123,45"). Útil para
     * montos.
     */
    public static boolean esSoloDecimal(String texto) {
        if (texto == null) {
            return false;
        }
        return SOLO_NUMEROS_DECIMALES_PATTERN.matcher(texto).matches();
    }

    /**
     * Valida que el texto esté dentro de un rango de longitud.
     */
    public static boolean tieneLongitud(String texto, int min, int max) {
        if (texto == null) {
            return false;
        }
        int len = texto.length();
        return len >= min && len <= max;
    }

  
    /**
     * Valida si el texto contiene solo números enteros (sin decimales).
     */
    public static boolean esSoloEntero(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        // Esta expresión regular (Pattern) revisa que cada carácter sea un dígito
        return texto.matches("^\\d+$");
    }

    /**
     * Valida si el texto tiene una longitud exacta.
     */
    public static boolean tieneLongitudExacta(String texto, int longitud) {
        if (texto == null) {
            return false;
        }
        return texto.length() == longitud;
    }
}
