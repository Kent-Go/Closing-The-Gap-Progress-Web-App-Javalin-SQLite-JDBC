package app;

public class GapScoreLGA {
    private String name;

    private int indPopulation;
    private int nonPopulation;
    private int totalPopulation;

    private double indPercent;
    private double nonPercent;

    private double gapScore;

    private double area;

    private int rank;

    public GapScoreLGA(String name, int iPop, int nPop, int tPop, 
                        double iPer, double nPer, double gScore, double area, int rank){
        this.name = name;

        this.indPopulation = iPop;
        this.nonPopulation = nPop;
        this.totalPopulation = tPop;

        this.indPercent = iPer;
        this.nonPercent = nPer;

        this.gapScore = gScore;

        this.area = area;

        this.rank = rank;
    }

    public String getName(){
        return name;
    }
    public int getIndigenousPopulation(){
        return indPopulation;
    }
    public int getNonIndigenousPopulation(){
        return nonPopulation;
    }
    public int getTotalPopulation(){
        return totalPopulation;
    }
    public double getIndigenousPercent(){
        return indPercent;
    }
    public double getNonIndigenousPercent(){
        return nonPercent;
    }
    public double getGapScore(){
        return gapScore;
    }
    public double getArea(){
        return area;
    }
    public int getRank(){
        return rank;
    }
}
