package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import Vista.ActualizarContraseña;
import Vista.Login;
import Dao.UsuarioDao;
import Utilidades.NotificadorUtils;
import Utilidades.ValidadorUtils;

/**
 *
 * @author Michael Ramos;
 *
**/

public class ActualizarContraseñaControlador implements ActionListener {

    private final ActualizarContraseña vista;
    private final UsuarioDao modeloDao;
    private final String correoDelUsuario;
    
    private final String NUEVA_PASS_PLACEHOLDER = "Escriba la nueva contraseña";
    private final String CONFIRMAR_PASS_PLACEHOLDER = "Confirme la contraseña";


    public ActualizarContraseñaControlador(ActualizarContraseña vista, String correo, UsuarioDao dao) {
        this.vista = vista;
        this.correoDelUsuario = correo;
        this.modeloDao = dao;

        this.vista.btnGuardar.addActionListener(this);
        this.vista.pfNuevaContrasena.addActionListener(this);
        this.vista.pfConfirmarContrasena.addActionListener(this);
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

        vista.lblError.setText("");


        String pass1 = new String(vista.pfNuevaContrasena.getPassword());
        String pass2 = new String(vista.pfConfirmarContrasena.getPassword());

        if (ValidadorUtils.esCampoVacio(vista.pfNuevaContrasena, NUEVA_PASS_PLACEHOLDER)) {
            NotificadorUtils.mostrarError("El campo 'Nueva Contraseña' está vacío.");
            return; 
        }

        if (ValidadorUtils.esCampoVacio(vista.pfConfirmarContrasena, CONFIRMAR_PASS_PLACEHOLDER)) {
            NotificadorUtils.mostrarError("El campo 'Confirmar Contraseña' está vacío.");
            return;
        }

        if (!ValidadorUtils.contrasenasCoinciden(vista.pfNuevaContrasena, vista.pfConfirmarContrasena)) {
            NotificadorUtils.mostrarError("Las contraseñas no coinciden. Verifíquelas.");
            return;
        }

        if (!ValidadorUtils.esContrasenaSegura(pass1)) {
            NotificadorUtils.mostrarError(
                    "La contraseña no es segura. Debe tener:\n"
                    + "- Al menos 8 caracteres\n"
                    + "- Una mayúscula y una minúscula\n"
                    + "- Un número\n"
                    + "- Un símbolo (@#$%^&+=!)"
            );
            return;
        }


        prepararUI(true);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {

                return modeloDao.actualizarContraseña(correoDelUsuario, pass1);
            }

            @Override
            protected void done() {
                Boolean resultadoExitoso = false;
                try {
                    resultadoExitoso = get();
                } catch (Exception e) {
                    NotificadorUtils.mostrarError("Error al actualizar.");
                } finally {
                    prepararUI(false); 
                }

                if (resultadoExitoso) {
                    NotificadorUtils.mostrarExito("¡Contraseña actualizada con éxito!");

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
