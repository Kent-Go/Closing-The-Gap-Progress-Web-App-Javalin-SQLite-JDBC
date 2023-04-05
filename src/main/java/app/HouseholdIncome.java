package app;

public class HouseholdIncome {
    private String income;
    private String status;
    private int number;

    public HouseholdIncome(String income, String status, int number){
        this.income = income;
        this.status = status;
        this.number = number;
    }

    public void setIncome(String income){
        this.income = income;
    }
    public void setNumber(int number){
        this.number = number;
    }
    
    public String getIncome(){
        return this.income;
    }
    public String getStatus(){
        return this.status;
    }
    public int getNumber(){
        return this.number;
    }
}
