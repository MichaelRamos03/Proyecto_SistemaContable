package Controlador;

// --- IMPORTS NECESARIOS ---
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer; // ¡Importante para el contador!

// --- IMPORTS DE TU PROYECTO ---
import Vista.IngresarCodigo;
import Vista.ActualizarContraseña;
import Dao.UsuarioDao;

/**
 *
 * @author Michael Ramos;
 *
 */
/**
 * Controlador para la vista de Ingresar Código. Gestiona la verificación, el
 * temporizador y el reenvío.
 */
public class IngresarCodigoControlador implements ActionListener {

    // --- VARIABLES DE CLASE ---
    private final IngresarCodigo vista;
    private final UsuarioDao modeloDao;
    private final String correoDelUsuario; // Correo que recibimos del paso anterior

    private Timer timer; // El objeto contador
    private int segundosRestantes; // El tiempo en segundos

    /**
     * Constructor del Controlador.
     *
     * @param vista La vista que va a controlar (IngresarCodigo.java)
     * @param correo El email del usuario (viene de
     * RecuperarContrasenaControlador)
     * @param dao El DAO para verificar en la Base de Datos
     */
    public IngresarCodigoControlador(IngresarCodigo vista, String correo, UsuarioDao dao) {
        this.vista = vista;
        this.correoDelUsuario = correo;
        this.modeloDao = dao;

        // 1. Registrar los listeners (los "espías" de los botones)
        // Asegúrate que tus botones se llamen así en las Propiedades de la vista
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnReenviar.addActionListener(this);

        // 2. Iniciar el contador de tiempo por primera vez
        iniciarContador();
    }

    /**
     * Método principal que se dispara cuando se hace clic en un botón.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // --- Opción 1: El usuario presiona SIGUIENTE ---
        if (e.getSource() == vista.btnSiguiente) {
            iniciarVerificacionDeCodigo();
        }

        // --- Opción 2: El usuario presiona REENVIAR ---
        if (e.getSource() == vista.btnReenviar) {
            iniciarReenvioDeCodigo();
        }
    }

    /**
     * Inicia o reinicia el contador de 120 segundos.
     */
    private void iniciarContador() {
        segundosRestantes = 120; // 2 minutos

        // 1. Prepara la UI para el conteo
        vista.btnReenviar.setEnabled(false); // Deshabilita el botón
        vista.lblTimer.setVisible(true);
        vista.lblTimer.setText("Tiempo restante: 02:00"); // Texto inicial

        // 2. Detiene cualquier timer anterior (si existía)
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // 3. Crea un nuevo Timer de Swing (se dispara cada 1000 ms)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundosRestantes--; // Resta un segundo

                // Formatea el tiempo a "mm:ss"
                long minutos = (segundosRestantes % 3600) / 60;
                long segundos = segundosRestantes % 60;
                vista.lblTimer.setText(String.format("Tiempo restante: %02d:%02d", minutos, segundos));

                // 4. Si el tiempo llega a 0
                if (segundosRestantes <= 0) {
                    timer.stop(); // Detiene el timer
                    vista.lblTimer.setText("¿No recibiste el código?");
                    vista.btnReenviar.setEnabled(true); // Habilita el botón de reenviar
                }
            }
        });

        timer.start(); // ¡Inicia el contador!
    }

    /**
     * Verifica el código que el usuario escribió.
     */
    private void iniciarVerificacionDeCodigo() {
        // Llama al método público que creamos en la vista
        String codigoIngresado = vista.getCodigoIngresado();

        if (codigoIngresado.length() < 6) {
            JOptionPane.showMessageDialog(vista, "El código debe tener 6 dígitos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Preparar UI para la carga
        prepararUI(true, "Verificando..."); // Deshabilita todo y muestra spinner

        // 2. Crear el SwingWorker
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                // --- ¡AQUÍ VA LA LÓGICA DE VERIFICACIÓN! ---
                // --- LÓGICA REAL ---
// Compara el código ingresado con el que guardaste en la BD
                boolean esCorrecto = modeloDao.verificarCodigo(correoDelUsuario, codigoIngresado);
                return esCorrecto;
            }

            @Override
            protected void done() {
                Boolean esCorrecto = false;
                try {
                    esCorrecto = get();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(vista, "Error al verificar el código.", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // 3. Restaurar UI
                    prepararUI(false, "");
                }

                // 4. Decidir qué hacer
                if (esCorrecto) {
                    // ¡Éxito! Detiene el timer y abre la siguiente vista
                    if (timer != null) {
                        timer.stop(); // Detiene el contador
                    }
                    vista.dispose(); // Cierra esta vista

                    // Lanza la siguiente vista: ActualizarContrasena
                    ActualizarContraseña vistaActualizar = new ActualizarContraseña();

                    // (Aquí crearías el controlador para 'ActualizarContrasena')
                    ActualizarContraseñaControlador ctrlActualizar
                            = new ActualizarContraseñaControlador(vistaActualizar, correoDelUsuario, modeloDao);

                    vistaActualizar.setVisible(true);

                } else {
                    // Código incorrecto
                    JOptionPane.showMessageDialog(vista, "El código ingresado es incorrecto.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    /**
     * Reenvía el código (simulado).
     */
    private void iniciarReenvioDeCodigo() {
        // 1. Preparar UI (mostrar spinner, deshabilitar botón)
        prepararUI(true, "Reenviando...");
        vista.btnReenviar.setEnabled(false); // Lo deshabilita mientras carga

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // --- LÓGICA PARA REENVIAR EL EMAIL ---
                // modeloDao.generarYEnviarCodigo(correoDelUsuario);

                // Simulación de 3 segundos
                Thread.sleep(3000);
                return true;
            }

            @Override
            protected void done() {
                // 3. Restaurar UI
                prepararUI(false, "");
                JOptionPane.showMessageDialog(vista, "Se ha reenviado un nuevo código a " + correoDelUsuario);

                // 4. ¡REINICIAR EL CONTADOR!
                iniciarContador(); // Vuelve a llamar al método del timer
            }
        };

        worker.execute();
    }

    /**
     * Método de ayuda para activar/desactivar la UI durante la carga.
     *
     * @param cargando true si la carga inicia, false si termina.
     */
    private void prepararUI(boolean cargando, String mensajeSpinner) {
        vista.lbSpinner.setVisible(cargando);
        // (Opcional: cambiar el texto del spinner)

        vista.btnSiguiente.setEnabled(!cargando);
        vista.btnReenviar.setEnabled(!cargando); // Se deshabilita también

        // Deshabilita las 6 cajas de texto
        vista.txtCodigo1.setEnabled(!cargando);
        vista.txtCodigo2.setEnabled(!cargando);
        vista.txtCodigo3.setEnabled(!cargando);
        vista.txtCodigo4.setEnabled(!cargando);
        vista.txtCodigo5.setEnabled(!cargando);
        vista.txtCodigo6.setEnabled(!cargando);
    }
}
