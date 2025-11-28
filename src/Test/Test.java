package Test;

import Controlador.LoginControlador;
import Dao.UsuarioDao;
import Vista.Login;
import Vista.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Michael Ramos;
 *
 */
public class Test {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final SplashScreen splash = new SplashScreen();
                splash.fadeIn();

                int duracionSplashVisible = 3000;

                Timer timer = new Timer(duracionSplashVisible, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        ((Timer) e.getSource()).stop();

                        splash.fadeOut(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                UsuarioDao modelo = new UsuarioDao();
                                Login vista = new Login();
                                LoginControlador controlador = new LoginControlador(vista, modelo);
                                vista.setVisible(true);
                            }
                        });
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

}
