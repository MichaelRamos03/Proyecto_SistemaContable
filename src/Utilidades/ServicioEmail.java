package Utilidades;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Michael Ramos;
*
 */
public class ServicioEmail {


    // El correo desde el que se enviarán los mensajes
    private static final String CORREO_REMITENTE = "botcontable2025@gmail.com";
    // La CONTRASEÑA DE APLICACIÓN de 16 dígitos
    private static final String CONTRASENA_APP = "uyuugdbbmrobzckg";

    public boolean enviarCorreo(String destinatario, String asunto, String cuerpo) {

        // 1. Configuración de las propiedades del servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
        props.put("mail.smtp.port", "587"); // Puerto para TLS
        props.put("mail.smtp.auth", "true"); // Requiere autenticación
        props.put("mail.smtp.starttls.enable", "true"); // Habilita encriptación TLS

        // 2. Creación de la Sesión con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_REMITENTE, CONTRASENA_APP);
            }
        });

        // 3. Creación y envío del mensaje
        try {
            // Crea el objeto del mensaje
            Message message = new MimeMessage(session);

            // Establece el remitente
            message.setFrom(new InternetAddress(CORREO_REMITENTE));

            // Establece el destinatario
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

            // Establece el asunto
            message.setSubject(asunto);

            // Establece el cuerpo del mensaje (puedes usar HTML si quieres)
            message.setText(cuerpo);

            // Envía el correo
            Transport.send(message);

            System.out.println("¡Correo enviado exitosamente a " + destinatario + "!");
            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Genera un código de verificación aleatorio de 6 dígitos.
     *
     * @return Un String con el código (ej. "123456").
     */
    public static String generarCodigoVerificacion() {
        Random random = new Random();
        // Genera un número entre 100000 y 999999
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

}
