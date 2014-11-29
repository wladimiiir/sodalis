package sk.magiksoft.sodalis.event.ui;

import scala.collection.JavaConversions;
import sk.magiksoft.sodalis.event.EventContextManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventStatusPanel extends JPanel {

    public EventStatusPanel() {
        initComponents();
    }

    private void initComponents() {
        final List<Action> contextActions = JavaConversions.seqAsJavaList(EventContextManager.getContextActions());
        JButton button;

        setLayout(new GridLayout(1, contextActions.size()));

        for (Action action : contextActions) {
            button = new JButton(action);
            button.setFocusPainted(false);
            add(button);
        }
    }
}
