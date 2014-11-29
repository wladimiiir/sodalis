package sk.magiksoft.swing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

/**
 * @author wladimiiir
 */
public class TreeComboBoxPopup extends BasicComboPopup {

    public TreeComboBoxPopup(JComboBox combo) {
        super(combo);
    }

    @Override
    protected void configurePopup() {
        setLayout(new BorderLayout());
        setBorderPainted(true);
        setBorder(new LineBorder(Color.BLACK));
        setOpaque(false);
        add(scroller, BorderLayout.CENTER);
        setDoubleBuffered(true);
        setFocusable(false);
    }

    protected JTree createTree() {
        return ((TreeComboBox) comboBox).getTree();
    }

    @Override
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane(createTree(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBar(null);
        sp.getViewport().setBackground(Color.WHITE);

        return sp;
    }

    @Override
    protected MouseListener createListMouseListener() {
        return new MouseAdapter() {
        };
    }

    @Override
    protected int getPopupHeightForRowCount(int maxRowCount) {
        return 200;
    }
}
