package sk.magiksoft.swing;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;

/**
 * @author wladimiiir
 */
public class StyledTextPane extends JTextPane {

    private DefaultStyledDocument doc = new DefaultStyledDocument();

    public StyledTextPane() {
        setDocument(doc);

    }

    private class StyledText {
        private String text;


    }
}
