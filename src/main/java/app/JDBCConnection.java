package app;

import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Timothy Wiley, 2022. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class JDBCConnection {

    // Name of database file (contained in database folder)
    private static final String DATABASE = "jdbc:sqlite:database/ctg.db";
    private static final String CTG_DATABASE = "jdbc:sqlite:database/ClosingTheGapProgress.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Personas in the database.
     * @return
     *    Returns an ArrayList of Personas objects
     */
    public ArrayList<Persona> getPersona(int ID) {
        // Create the ArrayList of LGA objects to return
        ArrayList<Persona> personas = new ArrayList<Persona>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM Persona WHERE PId =" + ID + ";";
            
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                int PersonaId     = results.getInt("PId");
                String PersonaName  = results.getString("PName");
                String PersonaQuote  = results.getString("PQuote");
                int PersonaAge  = results.getInt("PAge");
                String PersonaCareer  = results.getString("PCareer");
                String PersonaEthnicity  = results.getString("PEthnicity");
                String PersonaGender  = results.getString("PGender");
                Persona persona = new Persona(PersonaId, PersonaName, PersonaQuote, PersonaAge, PersonaCareer, PersonaEthnicity, PersonaGender);
                personas.add(persona);
            }
                
            String queryNeed = "SELECT PNeedGoal FROM Persona JOIN NeedGoal ON Persona.PId = NeedGoal.PId WHERE NeedGoal.PId =" + ID + ";";
            ResultSet resultsNeed = statement.executeQuery(queryNeed);
            

            ArrayList<String> PersonaNeedGoal  = new ArrayList<String>();
            while (resultsNeed.next()){
                String NeedGoal = resultsNeed.getString("PNeedGoal");
                PersonaNeedGoal.add(NeedGoal);
            }
            

            personas.get(0).setNeedGoal(PersonaNeedGoal);

            String querySkill = "SELECT PSkillExp FROM Persona JOIN SkillExp ON Persona.PId = SkillExp.PId WHERE SkillExp.PId =" + ID + ";";
            ResultSet resultsSkill = statement.executeQuery(querySkill);

            ArrayList<String> PersonaSkillExp = new ArrayList<String>();
            while (resultsSkill.next()){
                String SkillExp = resultsSkill.getString("PSkillExp");
                PersonaSkillExp.add(SkillExp);
            }

            personas.get(0).setSkillExp(PersonaSkillExp);

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return personas;
    }

    public ArrayList<LGA> getLGAs() {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM Lga ORDER BY Lga.lganame";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                int code = results.getInt("lgacode");
                String name = results.getString("lganame");
                String type = results.getString("lgatype");
                double area = results.getDouble("area");
                String state = results.getString("state");
                int year = results.getInt("year");

                LGA lga = new LGA(code, name, area, type, year, state);

                ArrayList<AgeDemographic> ageDemographic = getAgeDemographic(lga); 
                lga.setAgeDemographic(ageDemographic);

                ArrayList<HighestSchoolYear> highestSchoolYear = getHighestSchoolYear(lga);
                lga.setHighestSchoolYear(highestSchoolYear);
                
                ArrayList<HouseholdIncome> householdIncome = getHouseholds(lga);
                lga.setHouseholdIncome(householdIncome);

                int population = getTotalPopulation(lga);
                lga.setTotalPopulation(population);

                lgas.add(lga);                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public void setLastLGA(ArrayList<LGA> lastLGA){
        int lgacode2016 = lastLGA.get(0).getCode();
        int lgacode2021 = lastLGA.get(1).getCode();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = """
                drop VIEW if exists focus_last_lga;
                drop VIEW if exists focus_last_age;
                drop VIEW if exists focus_last_school;
                drop VIEW if exists focus_last_households;                
                    """;

            query = query + " CREATE VIEW focus_last_lga AS " +
                            " SELECT Lga.* " +
                            " FROM Lga " +
                            " WHERE Lga.lgacode = " + lgacode2016 + " OR Lga.lgacode = " + lgacode2021 + "; ";;
            
            query = query + " CREATE VIEW focus_last_age AS " +
                            " SELECT a1.* " +
                            " FROM Lga " + 
                            " JOIN Age_Demographic a1 ON a1.lgacode = Lga.lgacode and a1.year = Lga.year " +
                            " WHERE Lga.lgacode = " + lgacode2016 + " OR Lga.lgacode = " + lgacode2021 + "; " ;
            
            query = query + " CREATE VIEW focus_last_school AS " +
                            " SELECT s1.* " +
                            " FROM Lga " +
                            " JOIN Highest_School_Year s1 ON s1.lgacode = Lga.lgacode and s1.year = Lga.year " +
                            " WHERE Lga.lgacode = " + lgacode2016 + " OR Lga.lgacode = " + lgacode2021 + "; " ;

            query = query + " CREATE VIEW focus_last_households AS " +
                            " SELECT hh1.* " +
                            " FROM Lga " +
                            " JOIN Household_Weekly_Income hh1 ON hh1.lgacode = Lga.lgacode and hh1.year = Lga.year " +
                            " WHERE Lga.lgacode = " + lgacode2016 + " OR Lga.lgacode = " + lgacode2021 + "; " ;

            // Get Result
            statement.executeUpdate(query);

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<LGA> getStates(){
        ArrayList<LGA> states = new ArrayList<LGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Lga.state, Lga.year " +
                            "FROM Lga " +
                            "GROUP BY Lga.state, Lga.year";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String name = results.getString("state");
                int year = results.getInt("year");

                LGA state = new LGA(name, year);

                ArrayList<AgeDemographic> ageDemographic = getAgeDemographicState(state); 
                state.setAgeDemographic(ageDemographic);

                ArrayList<HighestSchoolYear> highestSchoolYear = getHighestSchoolYearState(state);
                state.setHighestSchoolYear(highestSchoolYear);

                ArrayList<HouseholdIncome> householdIncome = getHouseholdsState(state);
                state.setHouseholdIncome(householdIncome);

                int totalPopulation = getTotalPopulationState(state);
                state.setTotalPopulation(totalPopulation);
                
                states.add(state);                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return states;
    }

    public void setLastState(ArrayList<LGA> lastState){
        String stateName = lastState.get(0).getName();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = """
                drop VIEW if exists focus_last_lga;
                drop VIEW if exists focus_last_age;
                drop VIEW if exists focus_last_school;
                drop VIEW if exists focus_last_households;                
                    """;

            query = query + "CREATE VIEW focus_last_lga AS " +
                            " SELECT Lga.state, Lga.year " +
                            " FROM Lga " +
                            " WHERE Lga.state = '" + stateName + "' " +
                            " GROUP BY Lga.state, Lga.year; ";
            
            query = query + "CREATE VIEW focus_last_age AS " +
                            " SELECT Lga.state, a1.year, SUM(a1.number_of_people) AS 'number_of_people', a1.age_range, a1.gender, a1.indigenous_status " +
                            " FROM Lga " + 
                            " JOIN Age_Demographic a1 on Lga.lgacode = a1.lgacode and Lga.year = a1.year " +
                            " WHERE Lga.state = '" + stateName + "' " + 
                            " GROUP BY a1.year, a1.age_range, a1.gender, a1.indigenous_status; ";
            
            query = query + "CREATE VIEW focus_last_school AS " +
                            " SELECT Lga.state, s1.year, SUM(s1.number_of_people) AS 'number_of_people', s1.highest_year_of_school_completed, s1.gender, s1.indigenous_status " +
                            " FROM Lga " +
                            " JOIN Highest_School_Year s1 on Lga.lgacode = s1.lgacode and Lga.year = s1.year  " +
                            " WHERE Lga.state = '" + stateName + "' " + 
                            " GROUP BY s1.year, s1.highest_year_of_school_completed, s1.gender, s1.indigenous_status; ";

            query = query + "CREATE VIEW focus_last_households AS " +
                            " SELECT Lga.state, hh1.year, SUM(hh1.number_of_household) AS 'number_of_household', hh1.weekly_income_range, hh1.indigenous_status " +
                            " FROM Lga " +
                            " JOIN Household_Weekly_Income hh1 on Lga.lgacode = hh1.lgacode and Lga.year = hh1.year " +
                            " WHERE Lga.state = '" + stateName + "' " +
                            " GROUP BY hh1.year, hh1.weekly_income_range, hh1.indigenous_status; ";

            // Get Result
            statement.executeUpdate(query);
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<LGA> getLastLGA(){
        ArrayList<LGA> lastLGA = new ArrayList<LGA>();
        boolean isState;

        LGA lga2016 = new LGA(2016);
        LGA lga2021 = new LGA(2021);

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM focus_last_lga";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            ResultSetMetaData checkType = results.getMetaData();
            
            if(checkType.getColumnCount() == 2){
                isState = true;
            } else {
                isState = false;
            }
            // Process all of the results
            while(results.next()){
                LGA lga;

                if(isState){
                    String name = results.getString("state");
                    int year = results.getInt("year");

                    lga = new LGA(name, year);
                } 
                else {
                    int code = results.getInt("lgacode");
                    String name = results.getString("lganame");
                    String type = results.getString("lgatype");
                    double area = results.getDouble("area");
                    String state = results.getString("state");
                    int year = results.getInt("year");
    
                    lga = new LGA(code, name, area, type, year, state);
                }

                int totalPopulation = getLastPopulation(lga);
                lga.setTotalPopulation(totalPopulation);
                
                ArrayList<AgeDemographic> ages = getLastAgeDemographic(lga);
                lga.setAgeDemographic(ages);

                ArrayList<HighestSchoolYear> school = getLastHighestSchoolYear(lga);
                lga.setHighestSchoolYear(school);

                ArrayList<HouseholdIncome> households = getLastHouseholdIncome(lga);
                lga.setHouseholdIncome(households);

                if(lga.getYear() == 2016){
                    lga2016 = lga;
                } else if(lga.getYear() == 2021){
                    lga2021 = lga;
                }
            }

            lastLGA.add(lga2016);
            lastLGA.add(lga2021);
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return lastLGA;
    }

    public int getLastPopulation(LGA lga){
        int totalPopulation = -1;
        
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: gets the total population from the age demographics table.
             * The LGA code and year come from the LGA object.
            */
            String queryAge = "SELECT SUM(number_of_people) AS 'Total_population' " +
                              " FROM focus_last_age " +
                              " WHERE year = " + lga.getYear();
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);

            // Process all of the results

            /*Mario: Checks if the result is null as a string, as SUM will return NULL rather than an empty table.
             * Checking it as int will return a 0, but NULL and 0 are different,
             * because 0 could also mean the population is of 0 people.
            */

            while(results.next()){
                if(results.getString("Total_population") != null) {totalPopulation = results.getInt("Total_population");}
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return totalPopulation;
    }

    public ArrayList<AgeDemographic> getLastAgeDemographic(LGA lga){
        ArrayList<AgeDemographic> data = new ArrayList<AgeDemographic>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM focus_last_age WHERE year = " + lga.getYear();

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String range = results.getString("age_range");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number_of_people");

                AgeDemographic age = new AgeDemographic(range, status, gender, number);

                data.add(age);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public ArrayList<HighestSchoolYear> getLastHighestSchoolYear(LGA lga){

        ArrayList<HighestSchoolYear> data = new ArrayList<HighestSchoolYear>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM focus_last_school WHERE year = " + lga.getYear();

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String schoolYear = results.getString("highest_year_of_school_completed");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number_of_people");
    
                HighestSchoolYear year = new HighestSchoolYear(schoolYear, status, gender, number);
    
                data.add(year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public ArrayList<HouseholdIncome> getLastHouseholdIncome(LGA lga){

        ArrayList<HouseholdIncome> data = new ArrayList<HouseholdIncome>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM focus_last_households WHERE year = " + lga.getYear();

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String income = results.getString("weekly_income_range");
                String status = results.getString("indigenous_status");
                int number = results.getInt("number_of_household");

                HouseholdIncome weeklyIncome = new HouseholdIncome(income, status, number);

                data.add(weeklyIncome);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public ArrayList<Outcome> getAllOutcomes(){

        ArrayList<Outcome> outcomes = new ArrayList<Outcome>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM Outcomes";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                int id = results.getInt("OutcomeID");
                String name = results.getString("OutcomeName");
                String desc = results.getString("OutcomeDescription");

                Outcome outcome = new Outcome(id, name, desc);
                outcomes.add(outcome);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        
        return outcomes;
    }
    
    //Mario: this method gets all the LGA names to be used as dropdown options in the Focus by LGA/State page.
    public ArrayList<String> getLGANames(){
        ArrayList<String> names = new ArrayList<String>();
        
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT(Lga.lganame) FROM Lga ORDER BY Lga.lganame ASC";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String name = results.getString("lganame");
                names.add(name);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return names;
    }

    public ArrayList<String> getStateNames(){
        ArrayList<String> names = new ArrayList<String>();
        
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT(Lga.state) FROM Lga";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while(results.next()){
                String name = results.getString("state");
                names.add(name);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return names;
    }
    /*Mario: This method takes the ArrayList of all LGAs and the name of the LGA as arguments.
     * It returns an ArrayList with LGA object that match the name.
     * The reason why it returns an ArrayList rather than an object is because there is
     * an individual LGA object for 2016 and 2021, meaning the method must return 2 objects.
    */
    public ArrayList<LGA> getSpecificLga(ArrayList<LGA> lgas, String name){
        ArrayList<LGA> specificLga = new ArrayList<LGA>();

        /*Mario: Constructs a mostly blank object, only setting the years.
         * The default for all other values is either empty strings or 0.
        */
        LGA specificLga2016 = new LGA(2016);
        LGA specificLga2021 = new LGA(2021);

        /*Mario: Go through all LGAs, checking if the names match.
         * When there is a match, check if the year is 2016/2021,
         * and set the LGA objects above to the object currently viewed.
         * If data for a year is missing, then the specificLga2016/2021 object will remain with the default values.
         */
        for(LGA lga : lgas){
            if(lga.getName().equals(name)){
                if(lga.getYear() == 2016){
                    specificLga2016 = lga;
                } else if(lga.getYear() == 2021) {
                    specificLga2021 = lga;
                }
            }
        }
        /*Mario: Adds the 2016 and 2021 LGAs in order,
         * so that the 2016 index is always 0
         * and the 2021 index is always 1.
         */
        specificLga.add(specificLga2016);
        specificLga.add(specificLga2021);

        return specificLga;
    }

    //Mario: Focus by LGA/State methods
 
    //Mario: Computes the total population using the Age Demographics table using a LGA object.

    public int getTotalPopulation(LGA lga){

        int totalPopulation = -1;
        
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: gets the total population from the age demographics table.
             * The LGA code and year come from the LGA object.
            */
            String queryAge = "SELECT SUM(Age_Demographic.number_of_people) AS 'Total_population'" + 
                            "FROM Lga " + 
                            /*Mario: It is necessary for the JOIN to have both the code and year, 
                            otherwise it gives results for both years at once.*/
                            "JOIN Age_Demographic on Lga.lgacode = Age_Demographic.lgacode and Lga.year = Age_Demographic.year " +
                            "WHERE Lga.lgacode = " + lga.getCode() + 
                            " And Lga.year = " + lga.getYear();
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);

            // Process all of the results

            /*Mario: Checks if the result is null as a string, as SUM will return NULL rather than an empty table.
             * Checking it as int will return a 0, but NULL and 0 are different,
             * because 0 could also mean the population is of 0 people.
            */
            while(results.next()){
                if(results.getString("Total_population") != null) {totalPopulation = results.getInt("Total_population");}
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return totalPopulation;
    }

    public int getTotalPopulationState(LGA lga){

        int totalPopulation = -1;
        
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: gets the total population from the age demographics table.
             * The LGA code and year come from the LGA object.
            */
            String queryAge = "SELECT SUM(Age_Demographic.number_of_people) AS 'Total_population' " + 
                            "FROM Lga " + 
                            /*Mario: It is necessary for the JOIN to have both the code and year, 
                            otherwise it gives results for both years at once.*/
                            "JOIN Age_Demographic on Lga.lgacode = Age_Demographic.lgacode and Lga.year = Age_Demographic.year " +
                            "WHERE Lga.state = '" + lga.getName() + "' " +
                            " And Lga.year = " + lga.getYear();
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);

            // Process all of the results

            /*Mario: Checks if the result is null as a string, as SUM will return NULL rather than an empty table.
             * Checking it as int will return a 0, but NULL and 0 are different,
             * because 0 could also mean the population is of 0 people.
            */
            while(results.next()){
                if(results.getString("Total_population") != null) {totalPopulation = results.getInt("Total_population");}
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return totalPopulation;
    }

    /*Mario: This method generates and executes a query for the age demographic,
     * taking the LGA object, age range, gender and indigenous status as parameters.
     * These parameters come from the Focus by LGA/State page itself.
     * The method returns the number of people that fit the filters.
     * The default value for the number of people is -1, so that we can deal with situations where data is missing.
    */

    public ArrayList<AgeDemographic> getAgeDemographic(LGA lga){
        ArrayList<AgeDemographic> ageDemographics = new ArrayList<AgeDemographic>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: the strings for age range, indigenous status and gender are already surrounded with single quotes,
             * while the LGA code and year are integers, which do not need quotes.
             * This method uses the LGA object to get the code and year rather than taking the code and year as parameters.
            */
            String queryAge = "SELECT Age_Demographic.* " + 
                            "FROM Lga " + 
                            /*Mario: It is necessary for the JOIN to have both the code and year, 
                            otherwise it gives results for both years at once.*/
                            "JOIN Age_Demographic on Lga.lgacode = Age_Demographic.lgacode and Lga.year = Age_Demographic.year " +
                            "WHERE Lga.lgacode = " + lga.getCode() + 
                            " And Lga.year = " + lga.getYear();
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);

            // Process all of the results
            while(results.next()){
                String range = results.getString("age_range");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number_of_people");

                AgeDemographic age = new AgeDemographic(range, status, gender, number);

                ageDemographics.add(age);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return ageDemographics;
    }

    public ArrayList<AgeDemographic> getAgeDemographicState(LGA lga){
        ArrayList<AgeDemographic> ageDemographics = new ArrayList<AgeDemographic>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: the strings for age range, indigenous status and gender are already surrounded with single quotes,
             * while the LGA code and year are integers, which do not need quotes.
             * This method uses the LGA object to get the code and year rather than taking the code and year as parameters.
            */
            String queryAge = "SELECT SUM(Age_Demographic.number_of_people) AS 'number', Age_Demographic.age_range, Age_Demographic.gender, Age_Demographic.indigenous_status " + 
                              "FROM Lga " +
                              "JOIN Age_Demographic on Lga.lgacode = Age_Demographic.lgacode and Lga.year = Age_Demographic.year " +
                              " WHERE Lga.state = '" + lga.getName() + "' " +
                              " AND Lga.year = " + lga.getYear() +
                              " GROUP BY Age_Demographic.age_range, Age_Demographic.gender, Age_Demographic.indigenous_status";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);

            // Process all of the results
            while(results.next()){
                String range = results.getString("age_range");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number");

                AgeDemographic age = new AgeDemographic(range, status, gender, number);

                ageDemographics.add(age);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return ageDemographics;
    }

    /*Mario: This method generates and executes a query for the school year completed,
     * taking the LGA object, school year, gender and indigenous status as parameters.
     * These parameters come from the Focus by LGA/State page itself.
     * The method returns the number of people that fit the filters.
     * The default value for the number of people is -1, so that we can deal with situations where data is missing.
    */
    
    public ArrayList<HighestSchoolYear> getHighestSchoolYear(LGA lga){
        ArrayList<HighestSchoolYear> data = new ArrayList<HighestSchoolYear>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: the strings for school year, indigenous status and gender are already surrounded with single quotes,
             * while the LGA code and year are integers, which do not need quotes.
             * This method uses the LGA object to get the code and year rather than taking the code and year as parameters.
            */
            String querySchool = "SELECT Highest_School_Year.* " + 
                            "FROM Lga " + 
                            /*Mario: It is necessary for the JOIN to have both the code and year, 
                            otherwise it gives results for both years at once.*/
                            "JOIN Highest_School_Year on Lga.lgacode = Highest_School_Year.lgacode and Lga.year = Highest_School_Year.year " +
                            "WHERE Lga.lgacode = " + lga.getCode() + 
                            " And Lga.year = " + lga.getYear();

            // Get Result
            ResultSet results = statement.executeQuery(querySchool);

            // Process all of the results
            while(results.next()){
                String schoolYear = results.getString("highest_year_of_school_completed");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number_of_people");

                HighestSchoolYear year = new HighestSchoolYear(schoolYear, status, gender, number);

                data.add(year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return data;
    }

    public ArrayList<HighestSchoolYear> getHighestSchoolYearState(LGA lga){
        ArrayList<HighestSchoolYear> data = new ArrayList<HighestSchoolYear>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: the strings for school year, indigenous status and gender are already surrounded with single quotes,
             * while the LGA code and year are integers, which do not need quotes.
             * This method uses the LGA object to get the code and year rather than taking the code and year as parameters.
            */
            String querySchool = "SELECT SUM(h1.number_of_people) AS 'number', h1.highest_year_of_school_completed, h1.gender, h1.indigenous_status " + 
                                "FROM Lga " +
                                "JOIN Highest_School_Year h1 on Lga.lgacode = h1.lgacode and Lga.year = h1.year " +
                                " WHERE Lga.state = '" + lga.getName() + "' " +
                                " AND Lga.year = " + lga.getYear() +
                                " GROUP BY h1.highest_year_of_school_completed, h1.gender, h1.indigenous_status";

            // Get Result
            ResultSet results = statement.executeQuery(querySchool);

            // Process all of the results
            while(results.next()){
                String schoolYear = results.getString("highest_year_of_school_completed");
                String status = results.getString("indigenous_status");
                String gender = results.getString("gender");
                int number = results.getInt("number");

                HighestSchoolYear year = new HighestSchoolYear(schoolYear, status, gender, number);

                data.add(year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return data;
    }

    

    /*Mario: This method generates and executes a query for the weekly household income,
     * taking the LGA object, income range and indigenous status as parameters.
     * These parameters come from the Focus by LGA/State page itself.
     * The method returns the number of people that fit the filters.
     * The default value for the number of people is -1, so that we can deal with situations where data is missing.
    */

    public ArrayList<HouseholdIncome> getHouseholds(LGA lga){
        ArrayList<HouseholdIncome> data = new ArrayList<HouseholdIncome>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: Compared to the previous 2 queries, this query uses SUM and LIKE.
             * This is because 2021 has more specific ranges that need to be summed together 
             * to fit the broader income ranges from 2016, meaning we need to use SUM 
             * to actually get the proper values from 2021, and we need to use LIKE in our
             * condition so that we can capture those specific ranges.
             * Other than that, the strings used are surrounded in single quotes, and the way of
             * getting the LGA code and year is the same.
            */
            String queryIncome = "SELECT hh1.* " + 
                            "FROM Lga " + 
                            "JOIN Household_Weekly_Income hh1 on Lga.lgacode = hh1.lgacode and Lga.year = hh1.year " + 
                            "WHERE Lga.lgacode = " + lga.getCode() +
                            " AND Lga.year = " + lga.getYear();                            

            // Get Result
            ResultSet results = statement.executeQuery(queryIncome);

            /*Mario: Checks if the result is null as a string, as SUM will return NULL rather than an empty table.
             * Checking it as int will return a 0, but NULL and 0 are different,
             * because 0 could also mean the population is of 0 people.
            */
            while(results.next()){
                String income = results.getString("weekly_income_range");
                String status = results.getString("indigenous_status");
                int number = results.getInt("number_of_household");

                HouseholdIncome weeklyIncome = new HouseholdIncome(income, status, number);

                data.add(weeklyIncome);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return data;
    }

    public ArrayList<HouseholdIncome> getHouseholdsState(LGA lga){
        ArrayList<HouseholdIncome> data = new ArrayList<HouseholdIncome>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            /*Mario: Compared to the previous 2 queries, this query uses SUM and LIKE.
             * This is because 2021 has more specific ranges that need to be summed together 
             * to fit the broader income ranges from 2016, meaning we need to use SUM 
             * to actually get the proper values from 2021, and we need to use LIKE in our
             * condition so that we can capture those specific ranges.
             * Other than that, the strings used are surrounded in single quotes, and the way of
             * getting the LGA code and year is the same.
            */
            String queryIncome = "SELECT SUM(hh1.number_of_household) AS 'number', hh1.weekly_income_range, hh1.indigenous_status " + 
                            " FROM Lga " + 
                            " JOIN Household_Weekly_Income hh1 on Lga.lgacode = hh1.lgacode and Lga.year = hh1.year " + 
                            " WHERE Lga.state = '" + lga.getName() + "' " +
                            " AND Lga.year = " + lga.getYear() +
                            " GROUP BY hh1.weekly_income_range, hh1.indigenous_status" +
                            " ORDER BY 'number'";

            // Get Result
            ResultSet results = statement.executeQuery(queryIncome);

            /*Mario: Checks if the result is null as a string, as SUM will return NULL rather than an empty table.
             * Checking it as int will return a 0, but NULL and 0 are different,
             * because 0 could also mean the population is of 0 people.
            */
            while(results.next()){
                String income = results.getString("weekly_income_range");
                String status = results.getString("indigenous_status");
                int number = results.getInt("number");

                HouseholdIncome weeklyIncome = new HouseholdIncome(income, status, number);

                data.add(weeklyIncome);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return data;
    }

    public ArrayList<RankedLGA> getRankedAgeDemographic(int year, String proportion, String gender, String range, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            
            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " FROM (SELECT SUM(a1.number_of_people) as 'total', a2.number_of_people as 'category', Lga.lganame as 'name' " +
                              " FROM Lga " + 
                              " JOIN Age_Demographic a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Age_Demographic a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.gender = " + gender +
                              " AND a2.age_range = " + range +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.lgacode) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<RankedLGA> getRankedAgeDemographicState(int year, String proportion, String gender, String range, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            
            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " FROM (SELECT SUM(a1.number_of_people) as 'total', SUM(DISTINCT(a2.number_of_people)) as 'category', Lga.state as 'name' " +
                              " FROM Lga " + 
                              " JOIN Age_Demographic a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Age_Demographic a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.gender = " + gender +
                              " AND a2.age_range = " + range +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.state) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<RankedLGA> getRankedSchoolYear(int year, String proportion, String gender, String schoolYear, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " FROM (SELECT SUM(a1.number_of_people) as 'total', a2.number_of_people as 'category', Lga.lganame as 'name' " +
                              " FROM Lga " + 
                              " JOIN Highest_School_Year a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Highest_School_Year a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.gender = " + gender +
                              " AND a2.highest_year_of_school_completed = " + schoolYear +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.lgacode) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<RankedLGA> getRankedSchoolYearState(int year, String proportion, String gender, String schoolYear, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query

            
            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " SELECT SUM(a1.number_of_people) as 'total', SUM(DISTINCT(a2.number_of_people)) as 'category', Lga.state as 'name' " +
                              " FROM Lga " + 
                              " JOIN Highest_School_Year a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Highest_School_Year a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.gender = " + gender +
                              " AND a2.highest_year_of_school_completed = " + schoolYear +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.state) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<RankedLGA> getRankedHouseholds(int year, String proportion, String income, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " FROM (SELECT SUM(DISTINCT(a1.number_of_household)) as 'total', SUM(DISTINCT(a2.number_of_household)) as 'category', Lga.lganame as 'name' " +
                              " FROM Lga " + 
                              " JOIN Household_Weekly_Income a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Household_Weekly_Income a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.weekly_income_range LIKE " + income +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.lgacode) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<RankedLGA> getRankedHouseholdsState(int year, String proportion, String income, String status){
        ArrayList<RankedLGA> ranking = new ArrayList<RankedLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String queryAge = "SELECT name, category, total, CAST(category AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'proportion' " +
                              " FROM (SELECT SUM(DISTINCT(a1.number_of_household)) as 'total', SUM(DISTINCT(a2.number_of_household)) as 'category', Lga.state as 'name' " +
                              " FROM Lga " + 
                              " JOIN Household_Weekly_Income a1 on lga.lgacode = a1.lgacode and lga.year = a1.year " +
                              " JOIN Household_Weekly_Income a2 on lga.lgacode = a2.lgacode and lga.year = a2.year " +
                              " WHERE lga.year = " + year +
                              " " + proportion + " " +
                              " AND a2.weekly_income_range LIKE " + income +
                              " AND a2.indigenous_status = " + status +
                              " GROUP BY lga.state) " +
                              " ORDER BY proportion DESC ";
            // Get Result
            ResultSet results = statement.executeQuery(queryAge);
            int rank = 1;
            
            while(results.next()){
                String name = results.getString("name");
                int category = results.getInt("category");
                int total = results.getInt("total");
                double prop = results.getDouble("proportion");

                RankedLGA lga = new RankedLGA(rank, name, category, total, prop);
                ranking.add(lga);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return ranking;
    }

    public ArrayList<AgeDataset2021> sortAgeDataset2021(String sort_drop) {
        // Create the ArrayList of LGA objects to return
        ArrayList<AgeDataset2021> sort_age_datasets_2021 = new ArrayList<AgeDataset2021>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (sort_drop.equals("age_range_ascending")) {
                sort_drop = """
                      ORDER BY CASE
                    WHEN age_range = '0_4' then 1
                    WHEN age_range = '5_9' then 2 
                    WHEN age_range = '10_14' then 3
                    WHEN age_range = '15_19' then 4
                    WHEN age_range = '20_24' then 5
                    WHEN age_range = '25_29' then 6 
                    WHEN age_range = '30_34' then 7
                    WHEN age_range = '35_39' then 8
                    WHEN age_range = '40_44' then 9
                    WHEN age_range = '45_49' then 10
                    WHEN age_range = '50_54' then 11
                    WHEN age_range = '55_59' then 12
                    WHEN age_range = '60_64' then 13
                    WHEN age_range = '65_older' then 14
                    END ASC  
                        """;
            }
            else if (sort_drop.equals("age_range_descending")) {
                sort_drop = """
                      ORDER BY CASE
                    WHEN age_range = '0_4' then 1
                    WHEN age_range = '5_9' then 2 
                    WHEN age_range = '10_14' then 3
                    WHEN age_range = '15_19' then 4
                    WHEN age_range = '20_24' then 5
                    WHEN age_range = '25_29' then 6 
                    WHEN age_range = '30_34' then 7
                    WHEN age_range = '35_39' then 8
                    WHEN age_range = '40_44' then 9
                    WHEN age_range = '45_49' then 10
                    WHEN age_range = '50_54' then 11
                    WHEN age_range = '55_59' then 12
                    WHEN age_range = '60_64' then 13
                    WHEN age_range = '65_older' then 14
                    END DESC  
                        """;
            }

            // The Query
            String query = "SELECT DISTINCT Lga.lgacode, lganame, age_range, indigenous_status, gender, number_of_people " + 
                            " FROM Lga " + 
                            " JOIN Age_Demographic ON Age_Demographic.lgacode = Lga.lgacode AND Lga.year = Age_Demographic.year " + 
                            " WHERE Age_Demographic.year = 2021" +
                            " " + sort_drop ;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String age_range     = results.getString("age_range");
                String indigenous_status     = results.getString("indigenous_status");
                char gender = results.getString("gender").charAt(0);
                int number_of_people  = results.getInt("number_of_people");


                AgeDataset2021 age_data_2021 = new AgeDataset2021(lgacode, lganame, age_range, indigenous_status, gender, number_of_people);
                sort_age_datasets_2021.add(age_data_2021);
            }
        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return sort_age_datasets_2021;
    }

    public ArrayList<HealthDataset2021> sortHealthDataset2021(String sort_drop) {
        // Create the ArrayList of LGA objects to return
        ArrayList<HealthDataset2021> sort_health_datasets_2021 = new ArrayList<HealthDataset2021>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT Lga.lgacode, lganame, disease, indigenous_status, gender, number_of_people " + 
                            " FROM Lga " + 
                            " JOIN Health_Condition ON Health_Condition.lgacode = Lga.lgacode AND Lga.year = Health_Condition.year " + 
                            " WHERE Health_Condition.year = 2021 " +
                            " " + sort_drop;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String disease     = results.getString("disease");
                String indigenous_status     = results.getString("indigenous_status");
                char gender = results.getString("gender").charAt(0);
                int number_of_people  = results.getInt("number_of_people");


                HealthDataset2021 health_data_2021 = new HealthDataset2021(lgacode, lganame, disease, indigenous_status, gender, number_of_people);
                sort_health_datasets_2021.add(health_data_2021);
            }
        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return sort_health_datasets_2021;
    }

    public ArrayList<SchoolDataset2021> sortSchoolDataset2021(String sort_drop) {
        // Create the ArrayList of LGA objects to return
        ArrayList<SchoolDataset2021> sort_health_datasets_2021 = new ArrayList<SchoolDataset2021>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (sort_drop.equals("school_year_ascending")) {
                sort_drop = """
                      ORDER BY CASE  
                    WHEN highest_year_of_school_completed = 'did_not_go_to_school' then 1
                    WHEN highest_year_of_school_completed = 'y8_below' then 2
                    WHEN highest_year_of_school_completed = 'y9_equivalent' then 3
                    WHEN highest_year_of_school_completed = 'y10_equivalent' then 4
                    WHEN highest_year_of_school_completed = 'y11_equivalent' then 5
                    WHEN highest_year_of_school_completed = 'y12_equivalent' then 6 
                    END ASC    
                        """;
            }
            else if (sort_drop.equals("school_year_descending")) {
                sort_drop = """
                     ORDER BY CASE  
                    WHEN highest_year_of_school_completed = 'did_not_go_to_school' then 1
                    WHEN highest_year_of_school_completed = 'y8_below' then 2
                    WHEN highest_year_of_school_completed = 'y9_equivalent' then 3
                    WHEN highest_year_of_school_completed = 'y10_equivalent' then 4
                    WHEN highest_year_of_school_completed = 'y11_equivalent' then 5
                    WHEN highest_year_of_school_completed = 'y12_equivalent' then 6 
                    END DESC  
                        """;
            }
            // The Query
            String query = "SELECT DISTINCT Lga.lgacode, lganame, highest_year_of_school_completed, indigenous_status, gender, number_of_people " + 
                            " FROM Lga " + 
                            " JOIN Highest_School_Year ON Highest_School_Year.lgacode = Lga.lgacode AND Lga.year = Highest_School_Year.year " + 
                            " WHERE Highest_School_Year.year = 2021 " +
                            " " + sort_drop;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String highest_year_of_school_completed     = results.getString("highest_year_of_school_completed");
                String indigenous_status     = results.getString("indigenous_status");
                char gender = results.getString("gender").charAt(0);
                int number_of_people  = results.getInt("number_of_people");


                SchoolDataset2021 school_data_2021 = new SchoolDataset2021(lgacode, lganame, highest_year_of_school_completed, indigenous_status, gender, number_of_people);
                sort_health_datasets_2021.add(school_data_2021);
            }
        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return sort_health_datasets_2021;
    }

    public ArrayList<HouseholdDataset2021> sortHouseholdDataset2021(String sort_drop) {
        // Create the ArrayList of LGA objects to return
        ArrayList<HouseholdDataset2021> sort_household_datasets_2021 = new ArrayList<HouseholdDataset2021>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (sort_drop.equals("income_range_ascending")) {
                sort_drop = """
                      ORDER BY CASE  
                    WHEN weekly_income_range = '1_149' then 1
                    WHEN weekly_income_range = '150_299' then 2
                    WHEN weekly_income_range = '300_399' then 3
                    WHEN weekly_income_range = '400_499' then 4
                    WHEN weekly_income_range = '500_649' then 5
                    WHEN weekly_income_range = '650_799' then 6 
                    WHEN weekly_income_range = '800_999' then 7
                    WHEN weekly_income_range = '1000_1249' then 8
                    WHEN weekly_income_range = '1250_1499' then 9
                    WHEN weekly_income_range = '1500_1749' then 10
                    WHEN weekly_income_range = '1750_1999' then 11
                    WHEN weekly_income_range = '2000_2499' then 12
                    WHEN weekly_income_range = '2500_2999' then 13
                    WHEN weekly_income_range = '3000_3499' then 14
                    WHEN weekly_income_range = '3500_more' then 15
                    END ASC   
                        """;
            }
            else if (sort_drop.equals("income_range_descending")) {
                sort_drop = """
                      ORDER BY CASE  
                    WHEN weekly_income_range = '1_149' then 1
                    WHEN weekly_income_range = '150_299' then 2
                    WHEN weekly_income_range = '300_399' then 3
                    WHEN weekly_income_range = '400_499' then 4
                    WHEN weekly_income_range = '500_649' then 5
                    WHEN weekly_income_range = '650_799' then 6 
                    WHEN weekly_income_range = '800_999' then 7
                    WHEN weekly_income_range = '1000_1249' then 8
                    WHEN weekly_income_range = '1250_1499' then 9
                    WHEN weekly_income_range = '1500_1749' then 10
                    WHEN weekly_income_range = '1750_1999' then 11
                    WHEN weekly_income_range = '2000_2499' then 12
                    WHEN weekly_income_range = '2500_2999' then 13
                    WHEN weekly_income_range = '3000_3499' then 14
                    WHEN weekly_income_range = '3500_more' then 15
                    END DESC  
                        """;
            }

            // The Query
            String query = "SELECT DISTINCT Lga.lgacode, lganame, weekly_income_range, indigenous_status, number_of_household " + 
                            " FROM Lga " + 
                            " JOIN Household_Weekly_Income ON Household_Weekly_Income.lgacode = Lga.lgacode AND Lga.year = Household_Weekly_Income.year " + 
                            " WHERE Household_Weekly_Income.year = 2021 " +
                            " " + sort_drop;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String weekly_income_range     = results.getString("weekly_income_range");
                String indigenous_status     = results.getString("indigenous_status");
                int number_of_household  = results.getInt("number_of_household");


                HouseholdDataset2021 household_data_2021 = new HouseholdDataset2021(lgacode, lganame, weekly_income_range, indigenous_status, number_of_household);
                sort_household_datasets_2021.add(household_data_2021);
            }
        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return sort_household_datasets_2021;
    }

    public HashMap<String, ArrayList<SimilarAgeDataset>> findSimilarAgeDataset(String lganame_drop, String year_drop, String status_drop, String gender_drop, int min_age_drop, int max_age_drop, String similarity_drop, int number_result_textbox) {
        // Create the ArrayList of LGA objects to return
        HashMap<String, ArrayList<SimilarAgeDataset>> hashmap_similar_age_datasets = new HashMap<String, ArrayList<SimilarAgeDataset>>();

        ArrayList<SimilarAgeDataset> selected_age_datasets = new ArrayList<SimilarAgeDataset>();
        ArrayList<SimilarAgeDataset> compared_age_datasets = new ArrayList<SimilarAgeDataset>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String[] age_range = {"0_4", "5_9", "10_14", "15_19", "20_24", "25_29", "30_34", "35_39", "40_44", "45_49", "50_54", "55_59", "60_64", "65_older"};
            
            int add_OR_Operator = max_age_drop - min_age_drop;

            String age_range_query = "";

            for (int i = min_age_drop; i <= max_age_drop; i++) {
                age_range_query += " age_range = '" + age_range[i] + "' ";

                if (add_OR_Operator > 0) {
                    age_range_query += " OR ";
                    add_OR_Operator -= 1;
                }
            }

            if (status_drop.equals("both")) {
                status_drop = " (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ";
            }
            else if (status_drop.equals("indigenous")) {
                status_drop = " (indigenous_status = 'indigenous' ) ";
            }
            else if (status_drop.equals("non_indigenous")) {
                status_drop = " (indigenous_status = 'non_indigenous' ) ";
            }

            String querySelectedLGA = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, SUM(number_of_people) FROM Lga " +
            " JOIN Age_Demographic ON Age_Demographic.lgacode = Lga.lgacode AND Lga.year = Age_Demographic.year " + 
            " WHERE Age_Demographic.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND (" + age_range_query + ") " +
            " AND lganame = '" + lganame_drop + "' ";

            ResultSet resultsSelectedLGA = statement.executeQuery(querySelectedLGA);

            System.out.println(querySelectedLGA);

            int Selected_LGA_number_of_people = 0;

            while (resultsSelectedLGA.next()) {
                int Selected_LGA_lgacode = resultsSelectedLGA.getInt("lgacode");
                String Selected_LGA_lganame = resultsSelectedLGA.getString("lganame");
                String Selected_LGA_indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    Selected_LGA_indigenous_status = "Both";
                }
                else {
                    Selected_LGA_indigenous_status     = resultsSelectedLGA.getString("indigenous_status");
                }    
                char Selected_LGA_gender = resultsSelectedLGA.getString("gender").charAt(0);
                Selected_LGA_number_of_people  = resultsSelectedLGA.getInt("SUM(number_of_people)");

                SimilarAgeDataset similar_age_data = new SimilarAgeDataset(Selected_LGA_lgacode, Selected_LGA_lganame, Selected_LGA_indigenous_status, Selected_LGA_gender, Selected_LGA_number_of_people);
                selected_age_datasets.add(similar_age_data);
            }    

            hashmap_similar_age_datasets.put("SelectedLGA", selected_age_datasets);

            String String_Selected_LGA_number = Integer.toString(Selected_LGA_number_of_people);
            
            // The Query for selected LGA number of people to be used to compare in query below
            String query = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, SUM(number_of_people) AS 'number_of_people', " + 

            " (ABS(SUM(number_of_people) - ( " + String_Selected_LGA_number + " ) ) ) AS 'difference'" +                               
            " FROM Lga JOIN Age_Demographic ON Age_Demographic.lgacode = Lga.lgacode AND Lga.year = Age_Demographic.year " +
            " WHERE Age_Demographic.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND (" + age_range_query + ") " + 
            " AND NOT lganame = '" + lganame_drop + "' " + 
            " GROUP BY lganame ORDER BY difference " + similarity_drop + " LIMIT " + number_result_textbox + " ;";
            // ADD ASC or DESC and LIMIT

            // Get Result
            ResultSet results = statement.executeQuery(query);

            System.out.println(query);

            
            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    indigenous_status = "Both";
                }
                else {
                    indigenous_status     = results.getString("indigenous_status");
                }    
                char gender = results.getString("gender").charAt(0);
                int number_of_people  = results.getInt("number_of_people");
                int difference = results.getInt("difference");

                SimilarAgeDataset compared_age_data = new SimilarAgeDataset(lgacode, lganame, indigenous_status, gender, number_of_people, difference, Selected_LGA_number_of_people);
                compared_age_datasets.add(compared_age_data);
            }
            
            hashmap_similar_age_datasets.put("ComparedLGA", compared_age_datasets);

        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return hashmap_similar_age_datasets;
    }

    public HashMap<String, ArrayList<SimilarDiseaseDataset>> findSimilarDiseaseDataset(String lganame_drop, String year_drop, String status_drop, String gender_drop, String disease_drop, String similarity_drop, int number_result_textbox) {
        // Create the ArrayList of LGA objects to return
        HashMap<String, ArrayList<SimilarDiseaseDataset>> hashmap_similar_disease_datasets = new HashMap<String, ArrayList<SimilarDiseaseDataset>>();

        ArrayList<SimilarDiseaseDataset> selected_disease_datasets = new ArrayList<SimilarDiseaseDataset>();
        ArrayList<SimilarDiseaseDataset> compared_disease_datasets = new ArrayList<SimilarDiseaseDataset>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (status_drop.equals("both")) {
                status_drop = " (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ";
            }
            else if (status_drop.equals("indigenous")) {
                status_drop = " (indigenous_status = 'indigenous' ) ";
            }
            else if (status_drop.equals("non_indigenous")) {
                status_drop = " (indigenous_status = 'non_indigenous' ) ";
            }

            String querySelectedLGA = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, disease, SUM(number_of_people) FROM Lga " +
            " JOIN Health_Condition ON Health_Condition.lgacode = Lga.lgacode AND Lga.year = Health_Condition.year " + 
            " WHERE Health_Condition.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND disease = '" + disease_drop + "' " +
            " AND lganame = '" + lganame_drop + "' ";

            ResultSet resultsSelectedLGA = statement.executeQuery(querySelectedLGA);

            System.out.println(querySelectedLGA);

            int Selected_LGA_number_of_people = 0;

            while (resultsSelectedLGA.next()) {
                int Selected_LGA_lgacode = resultsSelectedLGA.getInt("lgacode");
                String Selected_LGA_lganame = resultsSelectedLGA.getString("lganame");
                String Selected_LGA_indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    Selected_LGA_indigenous_status = "Both";
                }
                else {
                    Selected_LGA_indigenous_status     = resultsSelectedLGA.getString("indigenous_status");
                }    
                char Selected_LGA_gender = resultsSelectedLGA.getString("gender").charAt(0);
                String Selected_LGA_disease = resultsSelectedLGA.getString("disease");
                Selected_LGA_number_of_people  = resultsSelectedLGA.getInt("SUM(number_of_people)");
                SimilarDiseaseDataset similar_disease_data = new SimilarDiseaseDataset(Selected_LGA_lgacode, Selected_LGA_lganame, Selected_LGA_indigenous_status, Selected_LGA_gender, Selected_LGA_disease, Selected_LGA_number_of_people);
                selected_disease_datasets.add(similar_disease_data);
            }    

            hashmap_similar_disease_datasets.put("SelectedLGA", selected_disease_datasets);

            String String_Selected_LGA_number = Integer.toString(Selected_LGA_number_of_people);
            
            // The Query for selected LGA number of people to be used to compare in query below
            String query = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, Health_Condition.disease, SUM(number_of_people) AS 'number_of_people', " + 

            " (ABS(SUM(number_of_people) - ( " + String_Selected_LGA_number + " ) ) ) AS 'difference'" +                               
            " FROM Lga JOIN Health_Condition ON Health_Condition.lgacode = Lga.lgacode AND Lga.year = Health_Condition.year " +
            " WHERE Health_Condition.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND Health_Condition.disease = '" + disease_drop + "' " + 
            " AND NOT lganame = '" + lganame_drop + "' " + 
            " GROUP BY lganame ORDER BY difference " + similarity_drop + " LIMIT " + number_result_textbox + " ;";
            // ADD ASC or DESC and LIMIT

            // Get Result
            ResultSet results = statement.executeQuery(query);

            System.out.println(query);

            
            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    indigenous_status = "Both";
                }
                else {
                    indigenous_status     = results.getString("indigenous_status");
                }    
                char gender = results.getString("gender").charAt(0);
                String disease = results.getString("disease");
                int number_of_people  = results.getInt("number_of_people");
                int difference = results.getInt("difference");

                SimilarDiseaseDataset compared_disease_data = new SimilarDiseaseDataset(lgacode, lganame, indigenous_status, gender, disease, number_of_people, difference, Selected_LGA_number_of_people);
                compared_disease_datasets.add(compared_disease_data);
            }
            
            hashmap_similar_disease_datasets.put("ComparedLGA", compared_disease_datasets);

        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return hashmap_similar_disease_datasets;
    }

    public HashMap<String, ArrayList<SimilarSchoolDataset>> findSimilarSchoolDataset(String lganame_drop, String year_drop, String status_drop, String gender_drop, int min_year_drop, int max_year_drop, String similarity_drop, int number_result_textbox) {
        // Create the ArrayList of LGA objects to return
        HashMap<String, ArrayList<SimilarSchoolDataset>> hashmap_similar_school_datasets = new HashMap<String, ArrayList<SimilarSchoolDataset>>();

        ArrayList<SimilarSchoolDataset> selected_school_datasets = new ArrayList<SimilarSchoolDataset>();
        ArrayList<SimilarSchoolDataset> compared_school_datasets = new ArrayList<SimilarSchoolDataset>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String[] school_year = {"did_not_go_to_school", "y8_below", "y9_equivalent", "y10_equivalent", "y11_equivalent", "y12_equivalent"};
            
            int add_OR_Operator = max_year_drop - min_year_drop;

            String school_year_query = "";

            for (int i = min_year_drop; i <= max_year_drop; i++) {
                school_year_query += " highest_year_of_school_completed = '" + school_year[i] + "' ";

                if (add_OR_Operator > 0) {
                    school_year_query += " OR ";
                    add_OR_Operator -= 1;
                }
            }

            if (status_drop.equals("both")) {
                status_drop = " (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ";
            }
            else if (status_drop.equals("indigenous")) {
                status_drop = " (indigenous_status = 'indigenous' ) ";
            }
            else if (status_drop.equals("non_indigenous")) {
                status_drop = " (indigenous_status = 'non_indigenous' ) ";
            }

            String querySelectedLGA = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, SUM(number_of_people) FROM Lga " +
            " JOIN Highest_School_Year ON Highest_School_Year.lgacode = Lga.lgacode AND Lga.year = Highest_School_Year.year " + 
            " WHERE Highest_School_Year.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND (" + school_year_query + ") " +
            " AND lganame = '" + lganame_drop + "' ";

            ResultSet resultsSelectedLGA = statement.executeQuery(querySelectedLGA);

            System.out.println(querySelectedLGA);

            int Selected_LGA_number_of_people = 0;

            while (resultsSelectedLGA.next()) {
                int Selected_LGA_lgacode = resultsSelectedLGA.getInt("lgacode");
                String Selected_LGA_lganame = resultsSelectedLGA.getString("lganame");
                String Selected_LGA_indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    Selected_LGA_indigenous_status = "Both";
                }
                else {
                    Selected_LGA_indigenous_status     = resultsSelectedLGA.getString("indigenous_status");
                }    
                char Selected_LGA_gender = resultsSelectedLGA.getString("gender").charAt(0);
                Selected_LGA_number_of_people  = resultsSelectedLGA.getInt("SUM(number_of_people)");

                SimilarSchoolDataset similar_school_data = new SimilarSchoolDataset(Selected_LGA_lgacode, Selected_LGA_lganame, Selected_LGA_indigenous_status, Selected_LGA_gender, Selected_LGA_number_of_people);
                selected_school_datasets.add(similar_school_data);
            }    

            hashmap_similar_school_datasets.put("SelectedLGA", selected_school_datasets);

            String String_Selected_LGA_number = Integer.toString(Selected_LGA_number_of_people);
            
            // The Query for selected LGA number of people to be used to compare in query below
            String query = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, gender, SUM(number_of_people) AS 'number_of_people', " + 

            " (ABS(SUM(number_of_people) - ( " + String_Selected_LGA_number + " ) ) ) AS 'difference'" +                               
            " FROM Lga JOIN Highest_School_Year ON Highest_School_Year.lgacode = Lga.lgacode AND Lga.year = Highest_School_Year.year " +
            " WHERE Highest_School_Year.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND gender = '" + gender_drop + "' " +
            " AND (" + school_year_query + ") " + 
            " AND NOT lganame = '" + lganame_drop + "' " + 
            " GROUP BY lganame ORDER BY difference " + similarity_drop + " LIMIT " + number_result_textbox + " ;";
            // ADD ASC or DESC and LIMIT

            // Get Result
            ResultSet results = statement.executeQuery(query);

            System.out.println(query);

            
            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    indigenous_status = "Both";
                }
                else {
                    indigenous_status     = results.getString("indigenous_status");
                }    
                char gender = results.getString("gender").charAt(0);
                int number_of_people  = results.getInt("number_of_people");
                int difference = results.getInt("difference");

                SimilarSchoolDataset compared_school_data = new SimilarSchoolDataset(lgacode, lganame, indigenous_status, gender, number_of_people, difference, Selected_LGA_number_of_people);
                compared_school_datasets.add(compared_school_data);
            }
            
            hashmap_similar_school_datasets.put("ComparedLGA", compared_school_datasets);

        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return hashmap_similar_school_datasets;
    }

    public HashMap<String, ArrayList<SimilarIncomeDataset>> findSimilarIncomeDataset(String lganame_drop, String year_drop, String status_drop, int min_income_drop, int max_income_drop, String similarity_drop, int number_result_textbox) {
        // Create the ArrayList of LGA objects to return
        HashMap<String, ArrayList<SimilarIncomeDataset>> hashmap_similar_income_datasets = new HashMap<String, ArrayList<SimilarIncomeDataset>>();

        ArrayList<SimilarIncomeDataset> selected_income_datasets = new ArrayList<SimilarIncomeDataset>();
        ArrayList<SimilarIncomeDataset> compared_income_datasets = new ArrayList<SimilarIncomeDataset>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String[] income_range = {"1_149", "150_299", "300_399", "400_499", "500_649", "650_799", "800_999", "1000_1249", "1250_1499", "1500_1999", "2000_2499", "2500_2999", "3000_more"};
            
            int add_OR_Operator = max_income_drop - min_income_drop;

            String income_range_query = "";

            if (year_drop.equals("2016")) {
                for (int i = min_income_drop; i <= max_income_drop; i++) {
                    income_range_query += " weekly_income_range = '" + income_range[i] + "' ";
    
                    if (add_OR_Operator > 0) {
                        income_range_query += " OR ";
                        add_OR_Operator -= 1;
                    }
                }
            }
            else {
                for (int i = min_income_drop; i <= max_income_drop; i++) {

                    if (i == 9) {
                        income_range_query += " weekly_income_range = '1500_1749' OR  weekly_income_range = '1750_1999' ";
                    }
                    else if (i == 12) {
                        income_range_query += " weekly_income_range = '3000_3499' OR  weekly_income_range = '3500_more' ";
                    }
                    else {
                        income_range_query += " weekly_income_range = '" + income_range[i] + "' ";
                    }
                    
                    if (add_OR_Operator > 0) {
                        income_range_query += " OR ";
                        add_OR_Operator -= 1;
                    }
                }
            }
            

            if (status_drop.equals("both")) {
                status_drop = " (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ";
            }
            else if (status_drop.equals("indigenous")) {
                status_drop = " (indigenous_status = 'indigenous' ) ";
            }
            else if (status_drop.equals("non_indigenous")) {
                status_drop = " (indigenous_status = 'non_indigenous' ) ";
            }

            String querySelectedLGA = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, SUM(number_of_household) FROM Lga " +
            " JOIN Household_Weekly_Income ON Household_Weekly_Income.lgacode = Lga.lgacode AND Lga.year = Household_Weekly_Income.year " + 
            " WHERE Household_Weekly_Income.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND (" + income_range_query + ") " +
            " AND lganame = '" + lganame_drop + "' ";

            ResultSet resultsSelectedLGA = statement.executeQuery(querySelectedLGA);

            System.out.println(querySelectedLGA);

            int Selected_LGA_number_of_household = 0;

            while (resultsSelectedLGA.next()) {
                int Selected_LGA_lgacode = resultsSelectedLGA.getInt("lgacode");
                String Selected_LGA_lganame = resultsSelectedLGA.getString("lganame");
                String Selected_LGA_indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    Selected_LGA_indigenous_status = "Both";
                }
                else {
                    Selected_LGA_indigenous_status     = resultsSelectedLGA.getString("indigenous_status");
                }    
                Selected_LGA_number_of_household  = resultsSelectedLGA.getInt("SUM(number_of_household)");

                SimilarIncomeDataset similar_income_data = new SimilarIncomeDataset(Selected_LGA_lgacode, Selected_LGA_lganame, Selected_LGA_indigenous_status, Selected_LGA_number_of_household);
                selected_income_datasets.add(similar_income_data);
            }    

            hashmap_similar_income_datasets.put("SelectedLGA", selected_income_datasets);

            String String_Selected_LGA_number_of_household = Integer.toString(Selected_LGA_number_of_household);
            
            // The Query for selected LGA number of people to be used to compare in query below
            String query = "SELECT DISTINCT Lga.lgacode, lganame, indigenous_status, SUM(number_of_household) AS 'number_of_household', " + 

            " (ABS(SUM(number_of_household) - ( " + String_Selected_LGA_number_of_household + " ) ) ) AS 'difference'" +                               
            " FROM Lga JOIN Household_Weekly_Income ON Household_Weekly_Income.lgacode = Lga.lgacode AND Lga.year = Household_Weekly_Income.year " +
            " WHERE Household_Weekly_Income.year = " + year_drop + 
            " AND " + status_drop + " " +
            " AND (" + income_range_query + ") " + 
            " AND NOT lganame = '" + lganame_drop + "' " + 
            " GROUP BY lganame ORDER BY difference " + similarity_drop + " LIMIT " + number_result_textbox + " ;";
            // ADD ASC or DESC and LIMIT

            // Get Result
            ResultSet results = statement.executeQuery(query);

            System.out.println(query);

            
            while (results.next()) {
                // Lookup the columns we need
                int lgacode  = results.getInt("lgacode");
                String lganame     = results.getString("lganame");
                String indigenous_status;
                if (status_drop.equals(" (indigenous_status = 'indigenous' OR indigenous_status = 'non_indigenous') ")) {
                    indigenous_status = "Both";
                }
                else {
                    indigenous_status     = results.getString("indigenous_status");
                }    
                int number_of_household  = results.getInt("number_of_household");
                int difference = results.getInt("difference");

                SimilarIncomeDataset compared_income_data = new SimilarIncomeDataset(lgacode, lganame, indigenous_status, number_of_household, difference, Selected_LGA_number_of_household);
                compared_income_datasets.add(compared_income_data);
            }
            
            hashmap_similar_income_datasets.put("ComparedLGA", compared_income_datasets);

        
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return hashmap_similar_income_datasets;
    }

    public void createGapScoreAgeViews(int year, int area_min, int area_max, 
                                        String range, String gender, 
                                        int pop_min, int pop_max){

        int other_year = 0;
        if(year == 2016){other_year = 2021;}
        else if (year == 2021){other_year = 2016;}
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String queryAge = "DROP view IF EXISTS gap_score_age_2016; " +
                              " DROP view IF EXISTS gap_score_age_2021; " +
                              " DROP view IF EXISTS gap_score_age_difference; " +

                              " CREATE VIEW gap_score_age_" + year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_people)) AS 'total', SUM(DISTINCT(a2.number_of_people)) AS 'indigenous', SUM(DISTINCT(a3.number_of_people)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Age_Demographic a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Age_Demographic a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Age_Demographic a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + year + " " +
                              " AND Lga.area >= " + area_min + " " + 
                              " AND Lga.area <= " + area_max + " " +

                              " AND a1.age_range = " + range + " " + 
                              " AND a1.gender  = " + gender + " " +

                              " AND a2.age_range = " + range + " " + 
                              " AND a2.gender  = " + gender + " " +
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.age_range = " + range + " " + 
                              " AND a3.gender  = " + gender + " " +
                              " AND a3.indigenous_status = 'non_indigenous' " +

                              " GROUP BY lga.lgacode " +

                              " HAVING total >= " + pop_min + " " +
                              " AND total <= " + pop_max + ")); " +

                              
                              " CREATE VIEW gap_score_age_" + other_year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_people)) AS 'total', SUM(DISTINCT(a2.number_of_people)) AS 'indigenous', SUM(DISTINCT(a3.number_of_people)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Age_Demographic a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Age_Demographic a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Age_Demographic a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + other_year + " " +
                              " AND Lga.lganame in (SELECT name FROM gap_score_age_" + year + ") " + 
                              " AND a1.age_range = " + range + " " + 
                              " AND a1.gender  = " + gender + " " +
                              " AND a2.age_range = " + range + " " + 
                              " AND a2.gender  = " + gender + " " +
                              " AND a2.indigenous_status = 'indigenous' " +
                              " AND a3.age_range = " + range + " " + 
                              " AND a3.gender  = " + gender + " " +
                              " AND a3.indigenous_status = 'non_indigenous' " +
                              " GROUP BY lga.lgacode));" + 

                              " CREATE VIEW gap_score_age_difference AS " +
                              " SELECT a1.name, a1.gap_score as 'gap_score_2016', a2.gap_score as 'gap_score_2021', a2.gap_score - a1.gap_score AS 'difference' " +
                              " FROM gap_score_age_2016 a1 " + 
                              " JOIN gap_score_age_2021 a2 on a1.name = a2.name ";
            
            // Get Result
            statement.executeUpdate(queryAge);

            System.out.println("created views");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<GapScoreLGA> getAgeGapScores(String sorter, int year){
        ArrayList<GapScoreLGA> gapScores = new ArrayList<GapScoreLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_age_" + year + " ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");

                int indPop = results.getInt("indigenous");
                int nonPop = results.getInt("non_indigenous");
                int totPop = results.getInt("total");

                double indPer = results.getDouble("indigenous_percent");
                double nonPer = results.getDouble("non_indigenous_percent");

                double gapScore = results.getDouble("gap_score");

                double area = results.getDouble("area");

                GapScoreLGA data = new GapScoreLGA(name, indPop, nonPop, totPop, indPer, nonPer, gapScore, area, rank);

                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }

    public ArrayList<GapScoreDifference> getAgeGapScoreDifference(String sorter){
        ArrayList<GapScoreDifference> gapScores = new ArrayList<GapScoreDifference>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_age_difference ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");
                double gs2016 = results.getDouble("gap_score_2016");
                double gs2021 = results.getDouble("gap_score_2021");
                double gsDiff = Math.abs(results.getDouble("difference"));

                GapScoreDifference data = new GapScoreDifference(name, gs2016, gs2021, gsDiff, rank);
                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }
        
    public void createGapScoreSchoolViews(int year, int area_min, int area_max, 
                                        String school_year, String gender, 
                                        int pop_min, int pop_max){

        int other_year = 0;
        if(year == 2016){other_year = 2021;}
        else if (year == 2021){other_year = 2016;}
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String querySchool = "DROP view IF EXISTS gap_score_school_2016; " +
                              " DROP view IF EXISTS gap_score_school_2021; " +
                              " DROP view IF EXISTS gap_score_school_difference; " +

                              " CREATE VIEW gap_score_school_" + year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_people)) AS 'total', SUM(DISTINCT(a2.number_of_people)) AS 'indigenous', SUM(DISTINCT(a3.number_of_people)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Highest_School_Year a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Highest_School_Year a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Highest_School_Year a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + year + " " +
                              " AND Lga.area >= " + area_min + " " + 
                              " AND Lga.area <= " + area_max + " " +

                              " AND a1.highest_year_of_school_completed = " + school_year + " " + 
                              " AND a1.gender  = " + gender + " " +

                              " AND a2.highest_year_of_school_completed = " + school_year + " " + 
                              " AND a2.gender  = " + gender + " " +
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.highest_year_of_school_completed = " + school_year + " " +  
                              " AND a3.gender  = " + gender + " " +
                              " AND a3.indigenous_status = 'non_indigenous' " +

                              " GROUP BY lga.lgacode " +

                              " HAVING total >= " + pop_min + " " +
                              " AND total <= " + pop_max + ")); " +

                              
                              " CREATE VIEW gap_score_school_" + other_year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_people)) AS 'total', SUM(DISTINCT(a2.number_of_people)) AS 'indigenous', SUM(DISTINCT(a3.number_of_people)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Highest_School_Year a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Highest_School_Year a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Highest_School_Year a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + other_year + " " +
                              " AND Lga.lganame in (SELECT name FROM gap_score_school_" + year + ") " + 
                              " AND a1.highest_year_of_school_completed = " + school_year + " " + 
                              " AND a1.gender  = " + gender + " " +

                              " AND a2.highest_year_of_school_completed = " + school_year + " " + 
                              " AND a2.gender  = " + gender + " " +
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.highest_year_of_school_completed = " + school_year + " " +  
                              " AND a3.gender  = " + gender + " " +
                              " AND a3.indigenous_status = 'non_indigenous' " +
                              " GROUP BY lga.lgacode));" + 



                              " CREATE VIEW gap_score_school_difference AS " +
                              " SELECT a1.name, a1.gap_score as 'gap_score_2016', a2.gap_score as 'gap_score_2021', a2.gap_score - a1.gap_score AS 'difference' " +
                              " FROM gap_score_school_2016 a1 " + 
                              " JOIN gap_score_school_2021 a2 on a1.name = a2.name ";
            
            // Get Result
            statement.executeUpdate(querySchool);

            System.out.println("created views");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<GapScoreLGA> getSchoolGapScores(String sorter, int year){
        ArrayList<GapScoreLGA> gapScores = new ArrayList<GapScoreLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_school_" + year + " ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");

                int indPop = results.getInt("indigenous");
                int nonPop = results.getInt("non_indigenous");
                int totPop = results.getInt("total");

                double indPer = results.getDouble("indigenous_percent");
                double nonPer = results.getDouble("non_indigenous_percent");

                double gapScore = results.getDouble("gap_score");

                double area = results.getDouble("area");

                GapScoreLGA data = new GapScoreLGA(name, indPop, nonPop, totPop, indPer, nonPer, gapScore, area, rank);

                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }

    public ArrayList<GapScoreDifference> getSchoolGapScoreDifference(String sorter){
        ArrayList<GapScoreDifference> gapScores = new ArrayList<GapScoreDifference>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_school_difference ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");
                double gs2016 = results.getDouble("gap_score_2016");
                double gs2021 = results.getDouble("gap_score_2021");
                double gsDiff = Math.abs(results.getDouble("difference"));

                GapScoreDifference data = new GapScoreDifference(name, gs2016, gs2021, gsDiff, rank);
                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }

        
    public void createGapScoreHouseholdViews(int year, int area_min, int area_max, 
                                        String income, String other_income, 
                                        int pop_min, int pop_max){

        int other_year = 0;
        if(year == 2016){other_year = 2021;}
        else if (year == 2021){other_year = 2016;}
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String queryHH = "DROP view IF EXISTS gap_score_households_2016; " +
                              " DROP view IF EXISTS gap_score_households_2021; " +
                              " DROP view IF EXISTS gap_score_households_difference; " +

                              " CREATE VIEW gap_score_households_" + year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_household)) AS 'total', SUM(DISTINCT(a2.number_of_household)) AS 'indigenous', SUM(DISTINCT(a3.number_of_household)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Household_Weekly_Income a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Household_Weekly_Income a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Household_Weekly_Income a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + year + " " +
                              " AND Lga.area >= " + area_min + " " + 
                              " AND Lga.area <= " + area_max + " " +

                              " AND a1.weekly_income_range LIKE " + income + " " + 

                              " AND a2.weekly_income_range LIKE " + income + " " + 
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.weekly_income_range LIKE " + income + " " + 
                              " AND a3.indigenous_status = 'non_indigenous' " +

                              " GROUP BY lga.lgacode " +

                              " HAVING total >= " + pop_min + " " +
                              " AND total <= " + pop_max + ")); " +

                              
                              " CREATE VIEW gap_score_households_" + other_year + " AS" + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_household)) AS 'total', SUM(DISTINCT(a2.number_of_household)) AS 'indigenous', SUM(DISTINCT(a3.number_of_household)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Household_Weekly_Income a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Household_Weekly_Income a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Household_Weekly_Income a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = " + other_year + " " +
                              " AND Lga.lganame in (SELECT name FROM gap_score_households_" + year + ") " + 

                              " AND a1.weekly_income_range LIKE " + other_income + " " + 

                              " AND a2.weekly_income_range LIKE " + other_income + " " + 
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.weekly_income_range LIKE " + other_income + " " + 
                              " AND a3.indigenous_status = 'non_indigenous' " +
                              " GROUP BY lga.lgacode));" + 


                              " CREATE VIEW gap_score_households_difference AS " +
                              " SELECT a1.name, a1.gap_score as 'gap_score_2016', a2.gap_score as 'gap_score_2021', a2.gap_score - a1.gap_score AS 'difference' " +
                              " FROM gap_score_households_2016 a1 " + 
                              " JOIN gap_score_households_2021 a2 on a1.name = a2.name ";
            
            // Get Result
            statement.executeUpdate(queryHH);

            System.out.println("created views");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<GapScoreLGA> getHouseholdGapScores(String sorter, int year){
        ArrayList<GapScoreLGA> gapScores = new ArrayList<GapScoreLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_households_" + year + " ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");

                int indPop = results.getInt("indigenous");
                int nonPop = results.getInt("non_indigenous");
                int totPop = results.getInt("total");

                double indPer = results.getDouble("indigenous_percent");
                double nonPer = results.getDouble("non_indigenous_percent");

                double gapScore = results.getDouble("gap_score");

                double area = results.getDouble("area");

                GapScoreLGA data = new GapScoreLGA(name, indPop, nonPop, totPop, indPer, nonPer, gapScore, area, rank);

                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }

    public ArrayList<GapScoreDifference> getHouseholdGapScoreDifference(String sorter){
        ArrayList<GapScoreDifference> gapScores = new ArrayList<GapScoreDifference>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_households_difference ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");
                double gs2016 = results.getDouble("gap_score_2016");
                double gs2021 = results.getDouble("gap_score_2021");
                double gsDiff = Math.abs(results.getDouble("difference"));

                GapScoreDifference data = new GapScoreDifference(name, gs2016, gs2021, gsDiff, rank);
                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }
        
    public void createGapScoreHealthView(int area_min, int area_max, 
                                        String disease, String gender, 
                                        int pop_min, int pop_max){
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String queryHealth = "DROP view IF EXISTS gap_score_health; " +

                              " CREATE VIEW gap_score_health AS " + 
                              " SELECT name, indigenous, non_indigenous, total, indigenous_percent,non_indigenous_percent, non_indigenous_percent - indigenous_percent AS 'gap_score', area " +
                              " FROM(SELECT total, indigenous, non_indigenous, CAST(indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'indigenous_percent', CAST(non_indigenous AS FLOAT) / CAST(total AS FLOAT) * 100 AS 'non_indigenous_percent', name, area " +
                              " FROM(SELECT SUM(DISTINCT(a1.number_of_people)) AS 'total', SUM(DISTINCT(a2.number_of_people)) AS 'indigenous', SUM(DISTINCT(a3.number_of_people)) AS 'non_indigenous' , Lga.lganame AS 'name', Lga.area AS 'area' " +
                              " FROM Lga " +
                              " JOIN Health_Condition a1 on a1.lgacode = lga.lgacode and a1.year = lga.year " +
                              " JOIN Health_Condition a2 on a2.lgacode = lga.lgacode and a2.year = lga.year " +
                              " JOIN Health_Condition a3 on a3.lgacode = lga.lgacode and a3.year = lga.year " +
                              " WHERE Lga.year = 2021 " +
                              " AND Lga.area >= " + area_min + " " + 
                              " AND Lga.area <= " + area_max + " " +

                              " AND a1.disease = " + disease + " " + 
                              " AND a1.gender = " + gender + " " +

                              " AND a2.disease = " + disease + " " + 
                              " AND a2.gender  = " + gender + " " +
                              " AND a2.indigenous_status = 'indigenous' " +

                              " AND a3.disease = " + disease + " " + 
                              " AND a3.gender  = " + gender + " " +
                              " AND a3.indigenous_status = 'non_indigenous' " +

                              " GROUP BY lga.lgacode " +

                              " HAVING total >= " + pop_min + " " +
                              " AND total <= " + pop_max + ")); ";
            
            // Get Result
            statement.executeUpdate(queryHealth);

            System.out.println("created views");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<GapScoreLGA> getHealthGapScores(String sorter){
        ArrayList<GapScoreLGA> gapScores = new ArrayList<GapScoreLGA>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(CTG_DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM gap_score_health ORDER BY " + sorter + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            int rank = 1;

            while(results.next()){
                String name = results.getString("name");

                int indPop = results.getInt("indigenous");
                int nonPop = results.getInt("non_indigenous");
                int totPop = results.getInt("total");

                double indPer = results.getDouble("indigenous_percent");
                double nonPer = results.getDouble("non_indigenous_percent");

                double gapScore = results.getDouble("gap_score");

                double area = results.getDouble("area");

                GapScoreLGA data = new GapScoreLGA(name, indPop, nonPop, totPop, indPer, nonPer, gapScore, area, rank);

                gapScores.add(data);

                ++rank;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return gapScores;
    }
}
