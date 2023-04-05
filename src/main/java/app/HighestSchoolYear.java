package app;

public class HighestSchoolYear {
    private String schoolYear;
    private String gender;
    private String status;
    private int numberOfPeople;

    public HighestSchoolYear(String year, String status, String gender, int number){
        this.schoolYear = year;
        this.gender = gender;
        this.status = status;
        this.numberOfPeople = number;
    }

    public int getNumber(){
        return numberOfPeople;
    }
    public String getStatus(){
        return status;
    }
    public String getGender(){
        return gender;
    }
    public String getSchoolYear(){
        return schoolYear;
    }
}
