package sk.magiksoft.swing;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

/**
 * @author wladimiiir
 */
public class CheckedTextField extends JTextField {
    private String regex;

    public CheckedTextField(String regex) {
        this.regex = regex;
        init();
    }

    private void init() {
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (!Pattern.matches(regex, getText() + e.getKeyChar())) {
                    e.consume();
                }
            }
        });
    }
}
