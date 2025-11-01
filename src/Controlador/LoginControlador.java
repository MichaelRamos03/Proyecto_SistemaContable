package Controlador;

import Modelo.Usuario;
import Dao.UsuarioDao;
import Utilidades.SesionUsuario;
import Vista.Login;
import Vista.RecuperarContraseña;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
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

    public LoginControlador(Login vista, UsuarioDao modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnSesion.addActionListener(this);
        this.vista.lbOlvidasteContraseña.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Llamamos a un nuevo método para manejar ese clic
                abrirVistaRecuperar();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // --- 1. INICIA LA ESPERA (ESTO SÍ ES INMEDIATO) ---
        vista.btnSesion.setEnabled(false);
        vista.lbSpinner.setVisible(true);
        vista.lbMensajeError.setText(""); // Limpia errores anteriores

        // Obtenemos los datos ANTES de iniciar el hilo
        String nombreUsuario = vista.tfUsuario.getText();
        String contrasena = new String(vista.tfContraseña.getPassword());

        // --- 2. CREA EL "SEGUNDO TRABAJADOR" ---
        SwingWorker<Usuario, Void> worker = new SwingWorker<Usuario, Void>() {

            /**
             * Tarea B: El trabajo pesado se hace aquí, en otro hilo. La
             * interfaz NO se congela.
             */
            @Override
            protected Usuario doInBackground() throws Exception {

                // --- ¡AQUÍ ESTÁ LA SIMULACIÓN! ---
                // Le decimos al hilo que "duerma" por 2 segundos.
                try {
                    TimeUnit.SECONDS.sleep(2); // <-- AÑADE ESTA LÍNEA
                } catch (InterruptedException ie) {
                    // Manejo de la interrupción (normalmente se deja así)
                }
                // --- FIN DE LA SIMULACIÓN ---

                // El trabajo real de la base de datos
                return modelo.validarUsuario(nombreUsuario, contrasena);
            }

            /**
             * Tarea C: Se ejecuta automáticamente cuando doInBackground()
             * termina. Vuelve al hilo principal de la interfaz para
             * actualizarla.
             */
            @Override
            protected void done() {
                Usuario usuarioValidado = null;
                boolean errorOcurrio = false;

                try {
                    // 1. Obtenemos el resultado del hilo de trabajo
                    // Este 'get()' puede lanzar una excepción si el hilo falló
                    usuarioValidado = get();

                } catch (Exception ex) {
                    // 2. Si get() falla (ej. error de DB), marcamos que hubo un error
                    errorOcurrio = true;
                    vista.lbMensajeError.setText("Error inesperado al validar.");
                    ex.printStackTrace(); // Para que TÚ veas el error en la consola

                } finally {
                    // 3. ¡ESTO ES CLAVE! SE EJECUTA INMEDIATAMENTE DESPUÉS DEL 'TRY/CATCH'
                    // Reseteamos la interfaz ANTES de mostrar cualquier mensaje modal.
                    vista.btnSesion.setEnabled(true);
                    vista.lbSpinner.setVisible(false);
                    vista.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                // 4. AHORA, con la interfaz ya limpia, mostramos los resultados
                if (usuarioValidado != null) {
                    // ÉXITO
                    SesionUsuario.getInstancia().setUsuarioLogueado(usuarioValidado);
                    String rol = usuarioValidado.getRol().getNombreRol();

                    // Mostramos el "Bienvenido" (la interfaz ya está limpia)
                    JOptionPane.showMessageDialog(vista, "¡Bienvenido, " + usuarioValidado.getNombreUsuario() + " (" + rol + ")!");

                    // Y cerramos la ventana de login
                    vista.dispose();

                } else if (!errorOcurrio) {
                    // FALLO (si no fue una excepción en el 'catch')
                    vista.lbMensajeError.setText("Usuario o contraseña incorrectos.");

                    // ¡AQUÍ ESTÁ LA LLAMADA!
                    vista.iniciarAnimacionShake();
                }
            }
        };

        // --- 3. INICIA EL TRABAJADOR ---
        worker.execute();
    }
    
        private void abrirVistaRecuperar() {
        
        // 1. Cierra la ventana de Login
        vista.dispose();
        
        // 2. Crea la nueva vista de Recuperación
        RecuperarContraseña vistaRecu = new RecuperarContraseña(vista,true);
        
        // 3. Crea el DAO (o usa el que ya tienes si prefieres)
        UsuarioDao dao = new UsuarioDao(); 
        // o: UsuarioDao dao = this.modelo;

        // 4. ¡AQUÍ CONECTAS EL NUEVO CONTROLADOR!
        // Le pasas la vista y el modelo que acabas de crear
        RecuperarContraseñaControlador ctrlRecu = new RecuperarContraseñaControlador(vistaRecu, dao);

        // 5. Muestra la nueva vista.
        // El controlador 'ctrlRecu' ya está escuchando los botones "Siguiente" y "Cancelar"
        vistaRecu.setVisible(true);
    }
    

}
