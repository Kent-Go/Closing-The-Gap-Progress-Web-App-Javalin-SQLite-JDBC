package app;

public class SchoolDataset2021 {
    private int lgacode;
    private String lganame;
    private String highest_year_of_school_completed;
    private String indigenous_status;
    private char gender;
    private int number_of_people;

    public SchoolDataset2021(int lgacode, String lganame, String highest_year_of_school_completed, String indigenous_status, char gender, int number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.highest_year_of_school_completed = highest_year_of_school_completed;
        this.indigenous_status = indigenous_status;
        this.gender = gender;
        this.number_of_people = number_of_people;
    }

    public int getLgaCode() {
        return lgacode;
    }

    public String getLgaName() {
        return lganame;
    }

    public String getHighestSchoolYearCompleted() {
        return highest_year_of_school_completed;
    }

    public String getStatus() {
        return indigenous_status;
    }

    public char getGender() {
        return gender;
    }

    public int getNumberOfPeople() {
        return number_of_people;
    }
}
