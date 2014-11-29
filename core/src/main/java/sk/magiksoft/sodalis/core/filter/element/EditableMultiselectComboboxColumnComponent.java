package sk.magiksoft.sodalis.core.filter.element;

/**
 * @author wladimiiir
 */
public class EditableMultiselectComboboxColumnComponent extends MultiselectComboboxColumnComponent {

    private String whereText;

    public EditableMultiselectComboboxColumnComponent() {
        component.setEditable(true);
    }

    @Override
    public String getWhereQuery() {
        StringBuilder whereQuery = new StringBuilder();
        String[] whereTexts = component.getEditorText().split(",");

        for (String whereText : whereTexts) {
            if (whereQuery.length() > 0) {
                whereQuery.append(" OR ");
            }
            this.whereText = "remove_diacritics('%" + whereText.trim().toUpperCase() + "%')";
            whereQuery.append(super.getWhereQuery());
        }

        return whereQuery.toString();
    }

    @Override
    public boolean isIncluded() {
        return !component.getEditorText().trim().isEmpty();
    }

    @Override
    protected String getWhereText() {
        return whereText;
    }

    @Override
    protected String getComparator() {
        return "LIKE";
    }
}
