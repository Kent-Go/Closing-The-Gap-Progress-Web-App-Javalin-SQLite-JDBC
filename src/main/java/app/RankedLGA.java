package app;

public class RankedLGA {
    private int rank;
    private String name;
    private int category;
    private int total;
    private double proportion;

    public RankedLGA(int rank, String name, int category, int total, double proportion){
        this.rank = rank;
        this.name = name;
        this.category = category;
        this.total = total;
        this.proportion = proportion;
    }

    public int getRank(){
        return this.rank;
    }
    public String getName(){
        return this.name;
    }
    public int getCategory(){
        return this.category;
    }
    public int getTotal(){
        return this.total;
    }
    public double getProportion(){
        return this.proportion;
    }
}
