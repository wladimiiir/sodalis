package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class EnsembleGroup extends AbstractDatabaseEntity {

    public static final int GROUP_TYPE_DANCER = 1;
    public static final int GROUP_TYPE_SINGER = 2;
    public static final int GROUP_TYPE_MUSICIAN = 4;
    public static final String GROUPINFO_DANCER_TYPE = "DancerType";
    public static final String GROUPINFO_SINGER_TYPE = "SingerType";
    public static final String GROUPINFO_MUSICIAN_INSTRUMENT = "MusicianInstrument";
    public static final String GROUPINFO_MANAGER_POSITION = "ManagerPosition";
    private int groupType = 0;
    private Map<String, Object> groupInfos = new HashMap<String, Object>();

    public EnsembleGroup() {
    }

    public EnsembleGroup(int groupType) {
        this.groupType = groupType;
    }

    public void putGroupInfo(String key, Object value) {
        groupInfos.put(key, value);
    }

    public Object getGroupInfo(String key) {
        return groupInfos.get(key);
    }

    public int getGroupType() {
        return groupType;
    }

    public String getGroupTypeToString() {
        StringBuilder groupTypeString = new StringBuilder();
        String additional;

        if (isGroupType(GROUP_TYPE_DANCER)) {
            groupTypeString.append(LocaleManager.getString("EnsembleGroup.dancer"));
            additional = getDancerType();
            if (!additional.isEmpty()) {
                groupTypeString.append(" ").append(additional);
            }
        }
        if (isGroupType(GROUP_TYPE_SINGER)) {
            if (groupTypeString.length() > 0) {
                groupTypeString.append(", ");
            }
            groupTypeString.append(LocaleManager.getString("EnsembleGroup.singer"));
            additional = getSingerType();
            if (!additional.isEmpty()) {
                groupTypeString.append(" ").append(additional);
            }
        }
        if (isGroupType(GROUP_TYPE_MUSICIAN)) {
            if (groupTypeString.length() > 0) {
                groupTypeString.append(", ");
            }
            groupTypeString.append(LocaleManager.getString("EnsembleGroup.musician"));
            additional = getMusicianInstrument();
            if (!additional.isEmpty()) {
                groupTypeString.append(" ").append(additional);
            }
        }

        return groupTypeString.toString();
    }

    private String getDancerType() {
        List types = (List) getGroupInfo(GROUPINFO_DANCER_TYPE);
        StringBuilder typeString = new StringBuilder();

        if (types == null || types.isEmpty()) {
            return typeString.toString();
        }

        for (Object object : types) {
            if (typeString.length() > 0) {
                typeString.append(", ");
            }
            typeString.append(object.toString());
        }

        return "(" + typeString.toString() + ")";
    }

    private String getSingerType() {
        List types = (List) getGroupInfo(GROUPINFO_SINGER_TYPE);
        StringBuilder typeString = new StringBuilder();

        if (types == null || types.isEmpty()) {
            return typeString.toString();
        }

        for (Object object : types) {
            if (typeString.length() > 0) {
                typeString.append(", ");
            }
            typeString.append(object.toString());
        }

        return "(" + typeString.toString() + ")";
    }

    private String getMusicianInstrument() {
        List instrument = (List) getGroupInfo(GROUPINFO_MUSICIAN_INSTRUMENT);
        StringBuilder instrumentString = new StringBuilder();

        if (instrument == null || instrument.isEmpty()) {
            return instrumentString.toString();
        }

        for (Object object : instrument) {
            if (instrumentString.length() > 0) {
                instrumentString.append(", ");
            }
            instrumentString.append(object.toString());
        }

        return "(" + instrumentString.toString() + ")";
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public boolean isGroupType(int groupType) {
        return (groupType & this.groupType) == 0 ? false : true;
    }

    public Map<String, Object> getGroupInfos() {
        return groupInfos;
    }

    public void setGroupInfos(Map<String, Object> groupInfos) {
        this.groupInfos = groupInfos;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EnsembleGroup)) {
            return;
        }
        EnsembleGroup group = (EnsembleGroup) entity;

        this.groupType = group.groupType;
        this.groupInfos = new HashMap<String, Object>(group.groupInfos);
    }

    @Override
    public String toString() {
        return getGroupTypeToString();
    }
}
