package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import Vista.ActualizarContraseña; // Tu vista
import Vista.Login;
import Dao.UsuarioDao;

/**
 *
 * @author Michael Ramos;
*
 */
public class ActualizarContraseñaControlador implements ActionListener {

    private final ActualizarContraseña vista;
    private final UsuarioDao modeloDao;
    private final String correoDelUsuario; // Correo del usuario a actualizar

    /**
     * Constructor.
     *
     * @param vista La vista que va a controlar (ActualizarContrasena.java)
     * @param correo El email del usuario (viene de IngresarCodigoControlador)
     * @param dao El DAO para actualizar la BD
     */
    public ActualizarContraseñaControlador(ActualizarContraseña vista, String correo, UsuarioDao dao) {
        this.vista = vista;
        this.correoDelUsuario = correo;
        this.modeloDao = dao;

        // Registrar el listener del botón
        this.vista.btnGuardar.addActionListener(this);

        // (Asegúrate de tener estos labels en tu vista)
        this.vista.lblError.setText("");
        this.vista.lbSpinner.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardar) {
            iniciarActualizacion();
        }
    }

    private void iniciarActualizacion() {
        // 1. Limpiar errores anteriores
        vista.lblError.setText("");

        // 2. Obtener las contraseñas (¡recuerda usar .getPassword()!)
        String pass1 = new String(vista.pfNuevaContrasena.getPassword());
        String pass2 = new String(vista.pfConfirmarContrasena.getPassword());

        // 3. --- VALIDACIONES ---
        if (pass1.isEmpty() || pass2.isEmpty()) {
            vista.lblError.setText("Los campos no pueden estar vacíos.");
            return;
        }

        if (!pass1.equals(pass2)) {
            vista.lblError.setText("Las contraseñas no coinciden.");
            return;
        }

        // 4. --- INICIAR SWINGWORKER ---
        prepararUI(true); // Mostrar spinner

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // --- LÓGICA REAL ---

// 1. (IMPORTANTE) Encripta la contraseña antes de guardarla
// String passEncriptada = Encriptador.hashPassword(pass1);
// 2. Llama al DAO para actualizar
// return modeloDao.actualizarContrasena(correoDelUsuario, passEncriptada);
// (Por ahora, usa la versión sin encriptar si aún no lo tienes)
                return modeloDao.actualizarContraseña(correoDelUsuario, pass1);
            }

            @Override
            protected void done() {
                Boolean resultadoExitoso = false;
                try {
                    resultadoExitoso = get();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar.", "Error de BD", JOptionPane.ERROR_MESSAGE);
                } finally {
                    prepararUI(false); // Ocultar spinner
                }

                if (resultadoExitoso) {
                    JOptionPane.showMessageDialog(vista, "¡Contraseña actualizada con éxito!", "Proceso Completado", JOptionPane.INFORMATION_MESSAGE);

                    // 7. Cierra esta ventana y vuelve al Login
                    vista.dispose();

                    Login loginView = new Login();
                    UsuarioDao loginDao = new UsuarioDao();
                    LoginControlador loginCtrl = new LoginControlador(loginView, loginDao);
                    loginView.setVisible(true);
                }
            }
        };
        worker.execute();
    }

    private void prepararUI(boolean cargando) {
        vista.lbSpinner.setVisible(cargando);
        vista.btnGuardar.setEnabled(!cargando);
        vista.pfNuevaContrasena.setEnabled(!cargando);
        vista.pfConfirmarContrasena.setEnabled(!cargando);
    }
}
