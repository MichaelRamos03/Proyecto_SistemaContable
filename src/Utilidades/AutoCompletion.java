package Utilidades;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;

/**
 *
 * @author Michael Ramos;
 *
**/

public class AutoCompletion extends PlainDocument {

    JComboBox comboBox;
    JTextComponent editor;
    boolean selecting = false;

    public AutoCompletion(final JComboBox comboBox) {
        this.comboBox = comboBox;
        comboBox.setEditable(true); 
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.setDocument(this);

        editor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (comboBox.isDisplayable()) {
                    comboBox.setPopupVisible(true);
                }
            }
        });
    }

    public static void enable(JComboBox comboBox) {
        new AutoCompletion(comboBox);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (selecting) {
            return;
        }
        super.insertString(offs, str, a);
        Object item = lookupItem(getText(0, getLength()));
        if (item != null) {
            selecting = true;
            super.remove(0, getLength());
            super.insertString(0, item.toString(), a);
            comboBox.setSelectedItem(item);
            selecting = false;
            if (item.toString().equals(str)) {
                return;
            }
            editor.setSelectionStart(offs + str.length());
            editor.setSelectionEnd(getLength());
        }
    }

    private Object lookupItem(String pattern) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object obj = comboBox.getItemAt(i);
            if (obj != null && obj.toString().toUpperCase().startsWith(pattern.toUpperCase())) {
                return obj;
            }
        }
        return null;
    }
}
