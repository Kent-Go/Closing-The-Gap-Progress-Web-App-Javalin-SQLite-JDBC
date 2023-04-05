package app;
import java.util.ArrayList;

public class HealthDataset2021 {
    private int lgacode;
    private String lganame;
    private String disease;
    private String indigenous_status;
    private char gender;
    private int number_of_people;

    public HealthDataset2021(int lgacode, String lganame, String disease, String indigenous_status, char gender, int number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.disease = disease;
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

    public String getDisease() {
        return disease;
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
