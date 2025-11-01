package Controlador;

// Imports de Java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

// Imports de tu proyecto
import Vista.RecuperarContraseña;
import Vista.Login;
import Dao.UsuarioDao; // Asumo que el DAO de Usuario tendrá el método para verificar el email
import Utilidades.ServicioEmail;
import Vista.IngresarCodigo;

/**
 *
 * @author Michael Ramos;
 *
 */
public class RecuperarContraseñaControlador implements ActionListener {

    private final RecuperarContraseña vista;
    private final UsuarioDao modeloDao;
    // private final EmailServicio emailServicio; // (Opcional)

    /**
     * Constructor para inicializar el controlador.
     *
     * @param vista La vista que este controlador va a gestionar.
     * @param modeloDao El DAO para acceder a la base de datos (ej. verificar
     * email).
     */
    public RecuperarContraseñaControlador(RecuperarContraseña vista, UsuarioDao modeloDao) {
        this.vista = vista;
        this.modeloDao = modeloDao;
        // this.emailServicio = new EmailServicio(); // (Opcional)

        // Registrar los listeners para los botones
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // --- Opción 1: El usuario presiona CANCELAR ---
        if (e.getSource() == vista.btnCancelar) {
            // Cierra la vista actual
            vista.dispose();

            // (Opcional) Abre la ventana de Login de nuevo
            Login loginView = new Login();
            UsuarioDao loginDao = new UsuarioDao();
            LoginControlador loginCtrl = new LoginControlador(loginView, loginDao);
            loginView.setVisible(true);
        }

        // --- Opción 2: El usuario presiona SIGUIENTE ---
        if (e.getSource() == vista.btnSiguiente) {
            iniciarVerificacionEmail();
        }
    }

    /**
     * Inicia el proceso de verificación de email usando un SwingWorker para no
     * congelar la interfaz.
     */
    private void iniciarVerificacionEmail() {

        // En: RecuperarContrasenaControlador.java
// Dentro de: private void iniciarVerificacionEmail()
        // 1. OBTENER EL CORREO DE LA VISTA
        String correo = vista.txtCorreo.getText();

        // 2. VALIDACIÓN DE FORMATO (¡MEJORADA!)
        if (correo.isEmpty() || correo.equals("ejemplo@correo.com")) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un correo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Esta es la expresión regular para validar un email
        String regexEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        if (!correo.matches(regexEmail)) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un formato de correo válido (ej. usuario@dominio.com).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. PREPARAR LA UI PARA LA CARGA
        // (Si pasa las validaciones, sigue con el spinner)
        prepararUI(true);

        // 4. CREAR EL SWINGWORKER...
        // ...
        // 4. CREAR EL SWINGWORKER
        // <Boolean> indicará si el correo fue encontrado (true) o no (false)
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

            /**
             * Trabajo pesado en segundo plano (Base de Datos, envío de email).
             */
            @Override
            protected Boolean doInBackground() throws Exception {
                // ... en RecuperarContrasenaControlador, dentro de doInBackground() ...

                // --- LÓGICA REAL (reemplaza la simulación) ---
                // 1. Instancia tu servicio de email
                ServicioEmail emailServicio = new ServicioEmail();

                // 2. Genera un código nuevo
                String codigo = ServicioEmail.generarCodigoVerificacion();

                // 3. Guarda este código en la BD para este usuario
                //    (¡Necesitarás crear este método en tu UsuarioDao!)
                boolean codigoGuardado = modeloDao.guardarCodigoTemporal(correo, codigo);

                if (codigoGuardado) {
                    // 4. Si se guardó, prepara y envía el email
                    String asunto = "Código de Recuperación de Contraseña";
                    String cuerpo = "Hola,\n\nTu código de verificación es: " + codigo + "\n\nSi no solicitaste esto, ignora este mensaje.";

                    boolean emailEnviado = emailServicio.enviarCorreo(correo, asunto, cuerpo);

                    return emailEnviado; // Retorna true si se guardó Y se envió
                } else {
                    return false; // Falla si no se pudo guardar el código en la BD
                }

            }

            /**
             * Se ejecuta cuando doInBackground() termina. Actualiza la UI de
             * forma segura.
             */
            @Override
            protected void done() {
                Boolean emailEncontrado = false;
                try {
                    // Obtiene el resultado (true/false) de doInBackground()
                    emailEncontrado = get();
                } catch (Exception e) {
                    // Si hubo un error en la base de datos o en el envío
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(vista, "Ocurrió un error inesperado.\n" + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);

                } finally {
                    // 5. RESTAURAR LA UI (Ocultar spinner, reactivar botones)
                    prepararUI(false);
                }

                // 6. DECIDIR QUÉ HACER CON EL RESULTADO
                if (emailEncontrado) {
                    // Éxito: Cierra esta vista y abre la de "Ingresar Código"
                    vista.dispose();

                    IngresarCodigo vistaCodigo = new IngresarCodigo();
                    // (Aquí crearías el controlador para 'IngresarCodigo')
                    vistaCodigo.setVisible(true);

                } else {
                    // Falla: El correo no existe en la BD
                    JOptionPane.showMessageDialog(vista, "El correo ingresado no está registrado.", "Correo no Encontrado", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        // 5. EJECUTAR EL WORKER
        worker.execute();
    }

    /**
     * Método de ayuda para activar/desactivar la UI durante la carga.
     *
     * @param cargando true si la carga inicia, false si termina.
     */
    private void prepararUI(boolean cargando) {
        vista.lbSpinner.setVisible(cargando);
        vista.btnSiguiente.setEnabled(!cargando);
        vista.btnCancelar.setEnabled(!cargando);
        vista.txtCorreo.setEnabled(!cargando);
    }
}
