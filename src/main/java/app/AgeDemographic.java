package app;

public class AgeDemographic {
    private int numberOfPeople;
    private String status;
    private String ageRange;
    private String gender;

    public AgeDemographic(String range, String status, String gender, int number){
        this.numberOfPeople = number;
        this.status = status;
        this.ageRange = range;
        this.gender = gender;
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
    public String getRange(){
        return ageRange;
    }
}
