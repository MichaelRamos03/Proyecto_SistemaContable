package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import Vista.IngresarCodigo;
import Vista.ActualizarContraseña;
import Dao.UsuarioDao;
import Utilidades.NotificadorUtils;
import Utilidades.ValidadorUtils;

/**
 *
 * @author Michael Ramos;
 *
 **/

public class IngresarCodigoControlador implements ActionListener {

    private final IngresarCodigo vista;
    private final UsuarioDao modeloDao;
    private final String correoDelUsuario;

    private Timer timer;
    private int segundosRestantes;

    public IngresarCodigoControlador(IngresarCodigo vista, String correo, UsuarioDao dao) {
        this.vista = vista;
        this.correoDelUsuario = correo;
        this.modeloDao = dao;
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnReenviar.addActionListener(this);
        iniciarContador();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("CLICK ACTION: " + e.getSource());

        if (e.getSource() == vista.btnSiguiente) {
            iniciarVerificacionDeCodigo();
        }
        if (e.getSource() == vista.btnReenviar) {
            iniciarReenvioDeCodigo();
        }
    }

    private void iniciarContador() {
        segundosRestantes = 120;

        vista.btnReenviar.setEnabled(false);
        vista.lblTimer.setVisible(true);
        vista.lblTimer.setText("Tiempo restante: 02:00");

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundosRestantes--;

                long minutos = (segundosRestantes % 3600) / 60;
                long segundos = segundosRestantes % 60;
                vista.lblTimer.setText(String.format("Tiempo restante: %02d:%02d", minutos, segundos));

                if (segundosRestantes <= 0) {
                    timer.stop();
                    vista.lblTimer.setText("¿No recibiste el código?");
                    vista.btnReenviar.setEnabled(true);
                }
            }
        });

        timer.start();
    }

    private void iniciarVerificacionDeCodigo() {
        String codigoIngresado = vista.getCodigoIngresado().trim();

        if (!ValidadorUtils.tieneLongitudExacta(codigoIngresado, 6)) {
            NotificadorUtils.mostrarError("El código debe tener 6 dígitos.");
            return;
        }

        if (!ValidadorUtils.esSoloEntero(codigoIngresado)) {
            NotificadorUtils.mostrarError("El código solo puede contener números.");
            return;
        }
        prepararUI(true, "Verificando...");

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return modeloDao.verificarCodigo(correoDelUsuario, vista.getCodigoIngresado().trim());

            }

            @Override
            protected void done() {
                prepararUI(false, "");
                try {
                    if (get()) {
                        if (timer != null) {
                            timer.stop();
                        }
                        vista.dispose();
                        ActualizarContraseña v2 = new ActualizarContraseña();
                        new ActualizarContraseñaControlador(v2, correoDelUsuario, modeloDao);
                        v2.setLocationRelativeTo(null);
                        v2.setVisible(true);
                    } else {
                        NotificadorUtils.mostrarError("Código inválido o expirado.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NotificadorUtils.mostrarError("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void iniciarReenvioDeCodigo() {
        prepararUI(true, "Reenviando...");
        vista.btnReenviar.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                String nuevo = Utilidades.ServicioEmail.generarCodigoVerificacion();
                if (!modeloDao.guardarCodigoTemporal(correoDelUsuario, nuevo)) {
                    return false;
                }

                String asunto = "Nuevo código de verificación";
                String cuerpo = "Tu código es: " + nuevo + "\nVence en 10 minutos.";
                return new Utilidades.ServicioEmail().enviarCorreo(correoDelUsuario, asunto, cuerpo);
            }

            @Override
            protected void done() {
                prepararUI(false, "");
                try {
                    if (get()) {
                        NotificadorUtils.mostrarExito("Se envió un nuevo código a " + correoDelUsuario);
                        iniciarContador();
                    } else {
                        NotificadorUtils.mostrarError("No se pudo reenviar el código.");
                        vista.btnReenviar.setEnabled(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NotificadorUtils.mostrarError("Error al reenviar: " + ex.getMessage());
                    vista.btnReenviar.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void prepararUI(boolean cargando, String mensajeSpinner) {
        vista.lbSpinner.setVisible(cargando);

        vista.btnSiguiente.setEnabled(!cargando);
        vista.btnReenviar.setEnabled(!cargando);

        vista.txtCodigo1.setEnabled(!cargando);
        vista.txtCodigo2.setEnabled(!cargando);
        vista.txtCodigo3.setEnabled(!cargando);
        vista.txtCodigo4.setEnabled(!cargando);
        vista.txtCodigo5.setEnabled(!cargando);
        vista.txtCodigo6.setEnabled(!cargando);
    }
}
