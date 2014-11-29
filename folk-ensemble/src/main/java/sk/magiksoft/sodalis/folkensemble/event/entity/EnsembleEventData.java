package sk.magiksoft.sodalis.folkensemble.event.entity;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.event.entity.AbstractEventData;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnsembleEventData extends AbstractEventData {
    public static final int TYPE_EXIBITION = 0;
    public static final int TYPE_CONCERT = 1;
    public static final int TYPE_COMPETITION = 2;
    public static final int TYPE_PRACTICE = 3;
    public static final int TYPE_GENERAL_PRACTICE = 4;

    private List<PersonWrapper> participants = new ArrayList<PersonWrapper>();
    private Programme programme;
    private String place = "";

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }


    public List<PersonWrapper> getParticipants() {
        return participants;
    }

    public void setParticipants(List<PersonWrapper> participants) {
        this.participants = participants;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public void clearIDs() {
        super.clearIDs();
        for (PersonWrapper participant : participants) {
            participant.clearIDs();
        }
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EnsembleEventData)) {
            return;
        }
        EnsembleEventData data = (EnsembleEventData) entity;

        this.participants = new ArrayList<PersonWrapper>(data.participants);
        this.programme = data.programme;
        this.place = data.place;
    }
}
