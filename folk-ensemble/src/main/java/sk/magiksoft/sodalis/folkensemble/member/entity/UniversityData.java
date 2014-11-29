package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.person.entity.PersonData;

/**
 * @author wladimiiir
 */
public class UniversityData extends AbstractDatabaseEntity implements PersonData {

    private String university;
    private String faculty;
    private String department;
    private String specialization;
    private String studiumType;
    private int year;

    public UniversityData() {
    }

    public UniversityData(String university, String faculty, String department, String specialization, String studiumType, int year) {
        this.university = university;
        this.faculty = faculty;
        this.department = department;
        this.specialization = specialization;
        this.studiumType = studiumType;
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStudiumType() {
        return studiumType;
    }

    public void setStudiumType(String studiumType) {
        this.studiumType = studiumType;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof UniversityData)) {
            return;
        }

        UniversityData universityData = (UniversityData) entity;
        this.department = universityData.department;
        this.faculty = universityData.faculty;
        this.specialization = universityData.specialization;
        this.studiumType = universityData.studiumType;
        this.university = universityData.university;
        this.year = universityData.year;
    }

}
