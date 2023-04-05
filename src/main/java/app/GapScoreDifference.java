package app;

public class GapScoreDifference {
    private String name;
    private double gs2016;
    private double gs2021;
    private double gsDiff;
    private int rank;

    public GapScoreDifference(String name, double gs2016, double gs2021, double gsDiff, int rank){
        this.name = name;
        this.gs2016 = gs2016;
        this.gs2021 = gs2021;
        this.gsDiff = gsDiff;
        this.rank = rank;
    }

    public String getName(){
        return this.name;
    }
    public double getScore2016(){
        return gs2016;
    }
    public double getScore2021(){
        return gs2021;
    }
    public double getScoreDifference(){
        return gsDiff;
    }
    public int getRank(){
        return rank;
    }
}
