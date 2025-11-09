package Utilidades;

import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Michael Ramos;
 *
 *
 */
public class PlaceholderDocumentListener implements DocumentListener {

    private JPasswordField passwordField;
    private String placeholderText;
    private char echoChar;
    private boolean isPlaceholderActive;


    public PlaceholderDocumentListener(JPasswordField passwordField, String placeholderText, char echoChar) {
        this.passwordField = passwordField;
        this.placeholderText = placeholderText;
        this.echoChar = echoChar;

        this.passwordField.setEchoChar((char) 0);
        this.passwordField.setText(placeholderText);
        this.passwordField.setForeground(Color.GRAY);
        this.isPlaceholderActive = true;

        this.passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                handleFocusGained();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                handleFocusLost();
            }
        });

        this.passwordField.getDocument().addDocumentListener(this);
    }

    private void handleFocusGained() {
        if (isPlaceholderActive) {

            passwordField.setForeground(Color.BLACK);
            passwordField.setEchoChar(echoChar);

            passwordField.getDocument().removeDocumentListener(this);
            passwordField.setText("");
            passwordField.getDocument().addDocumentListener(this);

            isPlaceholderActive = false;
        }
    }

    private void handleFocusLost() {
        if (new String(passwordField.getPassword()).isEmpty()) {

            passwordField.setForeground(Color.GRAY);
            passwordField.setEchoChar((char) 0);

            passwordField.getDocument().removeDocumentListener(this);
            passwordField.setText(placeholderText);
            passwordField.getDocument().addDocumentListener(this);

            isPlaceholderActive = true;
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateFieldState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateFieldState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    private void updateFieldState() {

        if (isPlaceholderActive) {

            handleFocusGained();
        } else if (new String(passwordField.getPassword()).isEmpty()) {

        }
    }
}
