package app;
import java.util.ArrayList;

public class SimilarIncomeDataset {
    private int lgacode;
    private String lganame;
    private String indigenous_status;
    private int number_of_people;
    private int difference;
    private int Selected_LGA_number_of_people;

    public SimilarIncomeDataset(int lgacode, String lganame, String indigenous_status, int number_of_people, int difference, int Selected_LGA_number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.indigenous_status = indigenous_status;
        this.number_of_people = number_of_people;
        this.difference = difference;
        this.Selected_LGA_number_of_people = Selected_LGA_number_of_people;
    }

    public SimilarIncomeDataset(int lgacode, String lganame, String indigenous_status, int number_of_people) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.indigenous_status = indigenous_status;
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