package sk.magiksoft.sodalis.core.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author wladimiiir
 */
public abstract class Validator {
    private static final Color INVALID_BACKGROUD = new Color(255, 208, 175);

    private Component component;
    private Color background;

    public Validator(final JTextField component) {
        this.component = component;
        this.background = component.getBackground();
        component.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                validate(component.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate(component.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate(component.getText());
            }
        });
    }

    protected Color getInvalidBackground() {
        return INVALID_BACKGROUD;
    }

    public void validate(Object object) {
        component.setBackground(isValid(object) ? background : getInvalidBackground());
    }

    public abstract boolean isValid(Object object);
}
