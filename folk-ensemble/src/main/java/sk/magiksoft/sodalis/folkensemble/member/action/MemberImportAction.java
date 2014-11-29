package sk.magiksoft.sodalis.folkensemble.member.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.imex.VCardManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;
import sk.magiksoft.sodalis.person.entity.Person;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class MemberImportAction extends AbstractImportAction {

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("importMembers"));
    }

    @Override
    protected void initFileChooser(JFileChooser fileChooser) {
        fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("vCardFiles"), "vcf"));
        super.initFileChooser(fileChooser);
    }

    @Override
    protected List importFile(File file) {
        if (file.getName().toLowerCase().endsWith(".vcf")) {
            return VCardManager.getInstance().fromVCard(file);
        } else {
            return super.importFile(file);
        }
    }

    @Override
    protected void importObjects(List objects) {
        List<Person> members = new LinkedList<Person>();
        for (Object object : objects) {
            if (object instanceof Person) {
                members.add((Person) object);
            }
        }

        if (!members.isEmpty()) {
            members = (List<Person>) MemberDataManager.addOrUpdateEntities(members);
        }
        SodalisApplication.get().showMessage(LocaleManager.getString("membersImported"), members.size());
    }
}
