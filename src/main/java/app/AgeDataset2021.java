package app;
import java.util.ArrayList;

public class AgeDataset2021 {
    private int lgacode;
    private String lganame;
    private String age_range;
    private String indigenous_status;
    private char gender;
    private int number_of_people;

    public AgeDataset2021(int lgacode, String lganame, String age_range, String indigenous_status, char gender, int number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.age_range = age_range;
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

    public String getAgeRange() {
        return age_range;
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
