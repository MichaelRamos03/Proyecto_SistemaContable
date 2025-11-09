package Utilidades;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Michael Ramos;
 *
 *
 */
public class PlaceholderTextFieldListener implements DocumentListener {

    private JTextField textField;
    private String placeholderText;
    private boolean isPlaceholderActive;

    public PlaceholderTextFieldListener(JTextField textField, String placeholderText) {
        this.textField = textField;
        this.placeholderText = placeholderText;

        this.textField.setText(placeholderText);
        this.textField.setForeground(Color.GRAY);
        this.isPlaceholderActive = true;

        this.textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                handleFocusGained();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                handleFocusLost();
            }
        });

        this.textField.getDocument().addDocumentListener(this);
    }

    private void handleFocusGained() {
        if (isPlaceholderActive) {
            textField.setForeground(Color.BLACK);
            textField.getDocument().removeDocumentListener(this);
            textField.setText("");
            textField.getDocument().addDocumentListener(this);
            isPlaceholderActive = false;
        }
    }

    private void handleFocusLost() {
        if (textField.getText().isEmpty()) {
            textField.setForeground(Color.GRAY);
            textField.getDocument().removeDocumentListener(this);
            textField.setText(placeholderText);
            textField.getDocument().addDocumentListener(this);
            isPlaceholderActive = true;
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (isPlaceholderActive) {
            handleFocusGained();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
