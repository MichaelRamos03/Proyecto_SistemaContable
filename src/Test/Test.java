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

//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                final SplashScreen splash = new SplashScreen();
//                splash.fadeIn();
//
//                int duracionSplashVisible = 3000;
//
//                Timer timer = new Timer(duracionSplashVisible, new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//
//                        ((Timer) e.getSource()).stop();
//
//                        splash.fadeOut(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//
//                                UsuarioDao modelo = new UsuarioDao();
//                                Login vista = new Login();
//                                LoginControlador controlador = new LoginControlador(vista, modelo);
//                                vista.setVisible(true);
//                            }
//                        });
//                    }
//                });
//                timer.setRepeats(false);
//                timer.start();
//            }
//        });


        // Vista crear partida
//      Vista.CrearPartida vista = new Vista.CrearPartida();    
//      Controlador.CrearPartidaControlador ctrl = new Controlador.CrearPartidaControlador(vista);
//      vista.setVisible(true);

        // Vista libro diario
//        Vista.LibroDiario vistaLD = new Vista.LibroDiario();
//        Controlador.LibroDiarioControlador ctrl = new Controlador.LibroDiarioControlador(vistaLD);
//        vistaLD.setVisible(true);

        //vista Libro Mayor
//        Vista.LibroMayor vistaM = new Vista.LibroMayor();
//        Controlador.LibroMayorControlador ctrl = new Controlador.LibroMayorControlador(vistaM);
//        vistaM.setVisible(true);
        
//        Vista.BalanzaComprobacion vistaB = new Vista.BalanzaComprobacion();
//        Controlador.BalanzaComprobacionControlador ctrl = new Controlador.BalanzaComprobacionControlador(vistaB);
//        vistaB.setVisible(true);

    }

}
