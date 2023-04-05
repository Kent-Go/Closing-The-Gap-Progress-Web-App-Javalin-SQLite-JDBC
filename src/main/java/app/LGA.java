package app;

import java.util.ArrayList;

/**
 * Class represeting a LGA from the Studio Project database
 * In the template, this only uses the code and name for 2016
 *
 * @author Timothy Wiley, 2022. email: timothy.wiley@rmit.edu.au
 */
public class LGA {
   private int year;
   private int code;
   private String name;
   private double area;
   private String type;
   private String state;
   private int totalPopulation;

   private boolean fullData;

   private ArrayList<AgeDemographic> ageDemographic = new ArrayList<AgeDemographic>();
   private ArrayList<HighestSchoolYear> highestSchoolYear = new ArrayList<HighestSchoolYear>();
   private ArrayList<HouseholdIncome> householdIncome = new ArrayList<HouseholdIncome>();

   /*Mario: for the Focus by LGA/State page,
    * a certain method requires LGA objects with blank fields (except the year)
    * to account for LGAs where data for 2016 or 2021 doesn't exist.
    */
   public LGA(int year) {
      code = -1;
      name = "";
      area = 0;
      type = "";
      state = "";
      this.year = year;
      this.fullData = false;
   }

   //Mario: constructor for actual information.
   public LGA(int code, String name, double area, 
               String type, int year, String state){
      this.code = code;
      this.name = name;
      this.area = area;
      this.type = type;
      this.year = year;
      this.state = state;
      this.fullData = true;
   }

   //Mario: constructor for states, as they mostly share the same structure.
   public LGA(String name, int year){
      this.name = name;
      this.year = year;
      this.fullData = true;
      this.code = 0;
   }

   public void setAgeDemographic(ArrayList<AgeDemographic> ageDemographic){
      this.ageDemographic = ageDemographic;
   }

   public void setHighestSchoolYear(ArrayList<HighestSchoolYear> highestSchoolYear){
      this.highestSchoolYear = highestSchoolYear;
   }

   public void setHouseholdIncome(ArrayList<HouseholdIncome> householdIncome){
      this.householdIncome = householdIncome;
   }

   public void setTotalPopulation(int population){
      this.totalPopulation = population;
   }

   //Mario: getter methods for LGAs.
   public int getCode(){
      return this.code;
   }

   public String getName(){
      return this.name;
   }

   public double getArea(){
      return this.area;
   }

   public String getType(){
      return this.type;
   }

   public String getState(){
      return this.state;
   }

   public int getYear(){
      return this.year;
   }

   public int getTotalPopulation(){
      return this.totalPopulation;
   }
   public boolean checkFullData(){
      return fullData;
   }

   public ArrayList<AgeDemographic> getAgeDemographic(){
      return this.ageDemographic;
   }

   public ArrayList<HighestSchoolYear> getHighestSchoolYear(){
      return this.highestSchoolYear;
   }

   public ArrayList<HouseholdIncome> getHouseholdIncome(){
      return this.householdIncome;
   }
   
   //Mario: default constructor that sets all fields to 0 or blank. Not sure if it's necessary to be honest.
   public LGA() {
      code = 0;
      name = "";
      area = 0;
      type = "";
      state = "";
      year = 0;
   }
}
