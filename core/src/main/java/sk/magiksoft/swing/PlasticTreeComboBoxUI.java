package sk.magiksoft.swing;


import com.jgoodies.looks.plastic.PlasticComboBoxUI;

import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;

/**
 * @author wladimiiir
 */
public class PlasticTreeComboBoxUI extends PlasticComboBoxUI {

    @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        g.setColor(Color.red);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    protected ComboPopup createPopup() {
        return new TreeComboBoxPopup(comboBox);
    }

    @Override
    protected FocusListener createFocusListener() {
        return new FocusAdapter() {
        };
    }
}
