package Utilidades;

import javax.swing.JOptionPane;

/**
 *
 * @author Michael Ramos;
 *
**/

public class NotificadorUtils {

    public static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmarAccion(String pregunta) {
        int respuesta = JOptionPane.showConfirmDialog(null, pregunta, "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public static String pregunta(String textoPregunta) {
        return JOptionPane.showInputDialog(
                null,
                textoPregunta,
                "Dato Requerido",
                JOptionPane.QUESTION_MESSAGE
        );
    }
}
