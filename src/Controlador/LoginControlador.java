package Controlador;

import Modelo.Usuario;
import Dao.UsuarioDao;
import Utilidades.NotificadorUtils;
import Utilidades.SesionUsuario;
import Utilidades.ValidadorUtils;
import Vista.Login;
import Vista.RecuperarContraseña;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michael Ramos;
 *
 */
public class LoginControlador implements ActionListener {

    private final Login vista;
    private final UsuarioDao modelo;
    private static final String PLACEHOLDER_USUARIO = "Ingrese su usuario";
    private static final String PLACEHOLDER_PASSWORD = "* * * * * * * *";
    

    public LoginControlador(Login vista, UsuarioDao modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnSesion.addActionListener(this);
        this.vista.lbOlvidasteContraseña.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirVistaRecuperar();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        
        String nombreUsuario = vista.tfUsuario.getText();
        String contrasena = new String(vista.tfContraseña.getPassword());
        
        if (ValidadorUtils.esCampoVacio(vista.tfUsuario, PLACEHOLDER_USUARIO )) {
            vista.lbMensajeError.setText("El campo de usuario esta vacío.");
            vista.iniciarAnimacionShake();
            return;
        }
        
        if (ValidadorUtils.esCampoVacio(vista.tfContraseña, PLACEHOLDER_PASSWORD)) {
            vista.lbMensajeError.setText("El campo contraseña esta vacia.");
            vista.iniciarAnimacionShake();
            return;
        }
        
        vista.btnSesion.setEnabled(false);
        vista.lbSpinner.setVisible(true);
        vista.lbMensajeError.setText("");

        SwingWorker<Usuario, Void> worker = new SwingWorker<Usuario, Void>() {

            @Override
            protected Usuario doInBackground() throws Exception {

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                }
                return modelo.validarUsuario(nombreUsuario, contrasena);
            }

            @Override
            protected void done() {
                Usuario usuarioValidado = null;
                boolean errorOcurrio = false;

                try {
                    usuarioValidado = get();

                } catch (Exception ex) {
                    errorOcurrio = true;
                    vista.lbMensajeError.setText("Error inesperado al validar.");
                    ex.printStackTrace();

                } finally {
                    vista.btnSesion.setEnabled(true);
                    vista.lbSpinner.setVisible(false);
                    vista.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                if (usuarioValidado != null) {
                    SesionUsuario.getInstancia().setUsuarioLogueado(usuarioValidado);
                    String rol = usuarioValidado.getRol().getNombreRol();
                    NotificadorUtils.mostrarExito("¡Bienvenido, " + usuarioValidado.getNombreUsuario() + " (" + rol + ")!");
                    vista.dispose();

                } else if (!errorOcurrio) {
                    vista.lbMensajeError.setText("Usuario o contraseña incorrectos.");


                    vista.iniciarAnimacionShake();
                }
            }
        };
        worker.execute();
    }
    
        private void abrirVistaRecuperar() {
        vista.dispose();
        RecuperarContraseña vistaRecu = new RecuperarContraseña(vista,true);
        RecuperarContraseñaControlador ctrlRecu = new RecuperarContraseñaControlador(vistaRecu, this.modelo);
        vistaRecu.setVisible(true);
    }
}
