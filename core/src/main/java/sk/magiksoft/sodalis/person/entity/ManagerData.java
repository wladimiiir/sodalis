package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

/**
 * @author wladimiiir
 */
public class ManagerData extends AbstractDatabaseEntity implements PersonData {
    private String position = "";

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof ManagerData)) {
            return;
        }
        ManagerData data = (ManagerData) entity;

        this.position = data.position;
    }


}
