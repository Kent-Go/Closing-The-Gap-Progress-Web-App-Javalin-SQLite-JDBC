package app;
import java.util.ArrayList;

public class SimilarDiseaseDataset {
    private int lgacode;
    private String lganame;
    private String indigenous_status;
    private char gender;
    private String disease;
    private int number_of_people;
    private int difference;
    private int Selected_LGA_number_of_people;

    public SimilarDiseaseDataset(int lgacode, String lganame, String indigenous_status, char gender, String disease, int number_of_people, int difference, int Selected_LGA_number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.indigenous_status = indigenous_status;
        this.gender = gender;
        this.disease = disease;
        this.number_of_people = number_of_people;
        this.difference = difference;
        this.Selected_LGA_number_of_people = Selected_LGA_number_of_people;
    }

    public SimilarDiseaseDataset(int lgacode, String lganame, String indigenous_status, char gender, String disease, int number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.indigenous_status = indigenous_status;
        this.gender = gender;
        this.disease = disease;
        this.number_of_people = number_of_people;
    }

    public int getLgaCode() {
        return lgacode;
    }

    public String getLgaName() {
        return lganame;
    }

    public String getStatus() {
        return indigenous_status;
    }

    public char getGender() {
        return gender;
    }

    public String getDisease() {
        return disease;
    }

    public int getNumberOfPeople() {
        return number_of_people;
    }

    public int getDifference() {
        return difference;
    }

    public int getSelectedLGANumberOfPeople() {
        return Selected_LGA_number_of_people;
    }
}