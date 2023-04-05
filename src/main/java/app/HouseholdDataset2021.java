package app;

public class HouseholdDataset2021 {
    private int lgacode;
    private String lganame;
    private String weekly_income_range;
    private String indigenous_status;
    private int number_of_household;

    public HouseholdDataset2021(int lgacode, String lganame, String weekly_income_range, String indigenous_status, int number_of_household) {
        this.lgacode = lgacode;
        this.lganame = lganame;
        this.weekly_income_range = weekly_income_range;
        this.indigenous_status = indigenous_status;
        this.number_of_household = number_of_household;
    }

    public int getLgaCode() {
        return lgacode;
    }

    public String getLgaName() {
        return lganame;
    }

    public String getHouseholdWeeklyIncome() {
        return weekly_income_range;
    }

    public String getStatus() {
        return indigenous_status;
    }

    public int getNumberOfHousehold() {
        return number_of_household;
    }
}
