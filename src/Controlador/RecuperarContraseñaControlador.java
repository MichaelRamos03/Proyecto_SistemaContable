package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import Vista.RecuperarContraseña;
import Vista.Login;
import Dao.UsuarioDao;
import Utilidades.NotificadorUtils;
import Utilidades.ServicioEmail;
import Utilidades.ValidadorUtils;
import Vista.IngresarCodigo;

/**
 *
 * @author Michael Ramos;
 *
 **/

public class RecuperarContraseñaControlador implements ActionListener {

    private final RecuperarContraseña vista;
    private final UsuarioDao modeloDao;
    public static final String PLACEHOLDER_CORREO = "ejemplo@correo.com";

    public RecuperarContraseñaControlador(RecuperarContraseña vista, UsuarioDao modeloDao) {
        this.vista = vista;
        this.modeloDao = modeloDao;
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnCancelar) {
            vista.dispose();
            Login loginView = new Login();
            UsuarioDao loginDao = new UsuarioDao();
            LoginControlador loginCtrl = new LoginControlador(loginView, loginDao);
            loginView.setVisible(true);
        }
        if (e.getSource() == vista.btnSiguiente) {
            iniciarVerificacionEmail();
        }
    }

    private void iniciarVerificacionEmail() {

        String correo = vista.txtCorreo.getText().trim();

        if (ValidadorUtils.esCampoVacio(vista.txtCorreo, PLACEHOLDER_CORREO)) {
            NotificadorUtils.mostrarError("Por favor, ingrese un correo.");
            return;
        }

        if (!ValidadorUtils.esEmailValido(correo)) {
            NotificadorUtils.mostrarError("Por favor, ingrese un formato de correo válido (ej. usuario@dominio.com).");
            return;
        }

        prepararUI(true);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

            @Override
            protected Boolean doInBackground() throws Exception {

                ServicioEmail emailServicio = new ServicioEmail();
                String codigo = ServicioEmail.generarCodigoVerificacion();

                boolean codigoGuardado = modeloDao.guardarCodigoTemporal(correo, codigo);

                if (codigoGuardado) {

                    String asunto = "Código de Recuperación de Contraseña";
                    String cuerpo = "Hola,\n\nTu código de verificación es: " + codigo + "\n\nSi no solicitaste esto, ignora este mensaje.";

                    boolean emailEnviado = emailServicio.enviarCorreo(correo, asunto, cuerpo);

                    return emailEnviado;
                } else {
                    return false;
                }
            }

            @Override
            protected void done() {
                Boolean emailEncontrado = false;
                try {
                    emailEncontrado = get();
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificadorUtils.mostrarError("Ocurrió un error inesperado.\n" + e.getMessage());

                } finally {
                    prepararUI(false);
                }

                if (emailEncontrado) {

                    vista.dispose();

                    IngresarCodigo vistaCodigo = new IngresarCodigo();
                    UsuarioDao dao = new UsuarioDao();
                    new IngresarCodigoControlador(vistaCodigo, correo, dao);

                    vistaCodigo.setLocationRelativeTo(null);
                    vistaCodigo.setVisible(true);

                } else {
                    NotificadorUtils.mostrarError("El correo ingresado no está registrado.");
                }
            }
        };
        worker.execute();
    }

    private void prepararUI(boolean cargando) {
        vista.lbSpinner.setVisible(cargando);
        vista.btnSiguiente.setEnabled(!cargando);
        vista.btnCancelar.setEnabled(!cargando);
        vista.txtCorreo.setEnabled(!cargando);
    }
}
