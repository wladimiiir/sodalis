package sk.magiksoft.sodalis.folkensemble.programme.action;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.programme.settings.ProgrammeSettings;
import sk.magiksoft.sodalis.folkensemble.programme.ui.ProgrammeInfoPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author wladimiiir
 */
public class AddProgrammeAction extends MessageAction {

    private Programme programme;
    private OkCancelDialog dialog;
    private ProgrammeInfoPanel programmeInfoPanel;

    public AddProgrammeAction() {
        super("", IconFactory.getInstance().getIcon("add"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("addProgramme"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            initDialog();
        }

        programme = EntityFactory.getInstance().createEntity(Programme.class);
        programmeInfoPanel.setupPanel(programme);
        programmeInfoPanel.initData();

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void initDialog() {
        dialog = new OkCancelDialog();

        programmeInfoPanel = new ProgrammeInfoPanel(false);
        programmeInfoPanel.initLayout();
        dialog.setModal(true);
        dialog.setTitle(LocaleManager.getString("addProgramme"));
        dialog.setMainPanel(programmeInfoPanel);
        dialog.setOkAction(new SaveAction());
        UIUtils.makeISDialog(dialog);
        dialog.setSize(500, 350);
    }

    private List<Category> getCategories(Programme programme) {
        List<Long> ids = (List<Long>) ProgrammeSettings.getInstance().getValue(
                ProgrammeSettings.O_SELECTED_CATEGORIES);
        List<Category> categories = CategoryDataManager.getInstance().getCategories(ids);

        return categories;
    }

    private class SaveAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            programmeInfoPanel.setupObject(programme);
            programme.setCategories(getCategories(programme));
            ProgrammeDataManager.getInstance().addDatabaseEntity(programme);
            dialog.setVisible(false);
        }
    }
}
