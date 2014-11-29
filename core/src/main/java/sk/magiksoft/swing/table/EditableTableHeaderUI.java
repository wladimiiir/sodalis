package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author wladimiiir
 * @since 2011/4/11
 */
public class EditableTableHeaderUI extends BasicTableHeaderUI {

    protected MouseInputListener createMouseInputListener() {
        return new MouseInputHandler((EditableTableHeader) header);
    }

    public class MouseInputHandler extends BasicTableHeaderUI.MouseInputHandler {
        private Component dispatchComponent;

        protected EditableTableHeader header;

        public MouseInputHandler(EditableTableHeader header) {
            this.header = header;
        }

        private void setDispatchComponent(MouseEvent e) {
            Component editorComponent = header.getEditorComponent();
            Point p = e.getPoint();
            Point p2 = SwingUtilities.convertPoint(header, p, editorComponent);
            dispatchComponent = SwingUtilities.getDeepestComponentAt(
                    editorComponent, p2.x, p2.y);
        }

        private boolean repostEvent(MouseEvent e) {
            if (dispatchComponent == null) {
                return false;
            }
            MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e,
                    dispatchComponent);
            dispatchComponent.dispatchEvent(e2);
            return true;
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            super.mousePressed(e);

            if (header.getResizingColumn() == null) {
                Point p = e.getPoint();
                TableColumnModel columnModel = header.getColumnModel();
                int index = columnModel.getColumnIndexAtX(p.x);
                if (index != -1) {
                    if (header.editCellAt(index, e)) {
                        setDispatchComponent(e);
                        repostEvent(e);
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            repostEvent(e);
            dispatchComponent = null;
        }

    }

}
