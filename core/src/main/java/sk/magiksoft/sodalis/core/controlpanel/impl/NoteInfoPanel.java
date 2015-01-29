package sk.magiksoft.sodalis.core.controlpanel.impl;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.ActionFactory;
import org.jdesktop.swingx.action.ActionManager;
import sk.magiksoft.sodalis.core.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.core.entity.NoteHolder;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/6/2
 */
public class NoteInfoPanel extends AbstractInfoPanel {
    private NoteHolder noteHolder;
    private JXEditorPane editorPane;

    public NoteInfoPanel() {
        super(NoteHolder.class);
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();
        ApplicationActionMap map = Application.getInstance().getContext().getActionMap(this);

//        toolbar.add(map.get("cut"));
//        toolbar.add(map.get("copy"));
//        toolbar.add(map.get("paste"));
//        toolbar.addSeparator();
//        toolbar.addSeparator();
//        toolbar.add(new JToggleButton(ActionFactory.createTargetableAction("font-bold", "Bold", "B", true)));
//        toolbar.add(new JToggleButton(map.get("italic")));
//        toolbar.add(new JToggleButton(map.get("underline")));

        List<AbstractActionExt> actions = new ArrayList<AbstractActionExt>();
        actions.add(ActionFactory.createTargetableAction("insert-break", "LF", "F"));

        actions.add(ActionFactory.createTargetableAction("left-justify", "Left", "L", true,
                "position-group"));
        actions.add(ActionFactory.createTargetableAction("center-justify", "Center", "C", true,
                "position-group"));
        actions.add(ActionFactory.createTargetableAction("right-justify", "Right", "R", true,
                "position-group"));

        actions.add(ActionFactory.createTargetableAction("font-bold", "Bold", "B", true));
        actions.add(ActionFactory.createTargetableAction("font-italic", "Italic", "I", true));
        actions.add(ActionFactory.createTargetableAction("font-underline", "Underline", "U", true));

        actions.add(ActionFactory.createTargetableAction("InsertUnorderedList", "UL", "U", true));
        actions.add(ActionFactory.createTargetableAction("InsertOrderedList", "OL", "O", true));
        actions.add(ActionFactory.createTargetableAction("InsertHR", "HR", "H"));
        ActionManager manager = ActionManager.getInstance();

        List<Object> actionNames = new ArrayList<Object>();
        for (AbstractActionExt ext : actions) {
            manager.addAction(ext);
            actionNames.add(ext.getActionCommand());

        }

        // Populate the toolbar. Must use the ActionContainerFactory to ensure
        // that toggle actions are supported.
        ActionContainerFactory factory = new ActionContainerFactory(manager);

        toolbar = factory.createToolBar(actionNames);

        editorPane = new JXEditorPane();
//        add(toolbar, BorderLayout.NORTH);
        layoutPanel.add(editorPane, BorderLayout.CENTER);
        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("notes");
    }

    @Override
    public void setupObject(Object object) {
        if (!acceptObject(object)) {
            return;
        }

        noteHolder = (NoteHolder) object;
        noteHolder.setNote(editorPane.getText());
    }

    @Override
    public void setupPanel(Object object) {
        if (!acceptObject(object)) {
            return;
        }

        noteHolder = (NoteHolder) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || noteHolder == null) {
            return;
        }

        editorPane.setText(noteHolder.getNote());

        initialized = true;
    }
}
