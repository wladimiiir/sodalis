package sk.magiksoft.sodalis.folkensemble.member.filter;

import sk.magiksoft.sodalis.core.filter.element.MultiselectComboboxColumnComponent;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleGroup;

/**
 * @author wladimiiir
 */
public class EnsembleGroupColumnComponent extends MultiselectComboboxColumnComponent {

    public EnsembleGroupColumnComponent() {
        addItem(new EnsembleGroup(EnsembleGroup.GROUP_TYPE_DANCER));
        addItem(new EnsembleGroup(EnsembleGroup.GROUP_TYPE_SINGER));
        addItem(new EnsembleGroup(EnsembleGroup.GROUP_TYPE_MUSICIAN));
    }

    @Override
    public String getWhereQuery() {
        StringBuilder whereQuery = new StringBuilder();

        for (int i = 0; i < component.getSelectedObjects().size(); i++) {
            EnsembleGroup ensembleGroup = (EnsembleGroup) component.getSelectedObjects().get(i);
            if (whereQuery.length() > 0) {
                whereQuery.append(" OR ");
            }

            whereQuery.append("mod(")
                    .append(where)
                    .append(" / ")
                    .append(ensembleGroup.getGroupType())
                    .append(", 2) = 1");
        }

        return whereQuery.length() == 0 ? "" : whereQuery.insert(0, "ed in elements(p.personDatas) AND (").append(")").toString();
    }

    @Override
    public boolean isIncluded() {
        return component.getSelectedObjects().size() > 0;
    }
}
