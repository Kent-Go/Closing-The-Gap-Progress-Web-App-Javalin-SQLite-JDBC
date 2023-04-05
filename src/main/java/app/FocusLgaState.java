package app;

import java.util.ArrayList;
import java.util.HashMap;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class FocusLgaState implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/focus-lga-state.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";


        
        // Add some Head information
        html = html + "<head>" + 
               "<title>Focus by LGA or State</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='focus.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/'>Homepage</a>
                <a href='about.html'>ABOUT US</a>
                <a href='latest-2021-data.html'>LATEST 2021 DATA</a>
                <a href='focus-lga-state.html' class = 'current_page'>FOCUS BY LGA/STATE</a>
                <a href='gap-scores.html'>GAP SCORES</a>
                <a href='find-similar-lga.html'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>Focus by LGA or State</h1>
                <p>On this page, you can choose a LGA or State, and compare the data between 2016 and 2021 
                for the total population, households, age demographics and level of education. You may
                filter each category as you wish.</p>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();

        /*Mario: Create an ArrayList of LGA objects for all LGAs to use later,
         * As well as an ArrayList of strings with all LGA names to create the
         * dropdown menu options.
        */
        ArrayList<LGA> lgas = jdbc.getLGAs();
        ArrayList<LGA> states = jdbc.getStates();

        ArrayList<String> lgaNames = jdbc.getLGANames();
        ArrayList<String> stateNames = jdbc.getStateNames();

        //Mario: create the form to choose an LGA from
        
        html = html + """
            <div class = 'select-lga'>
                <form action = 'focus-lga-state.html' method = 'post'>
                    <div class = 'form-group'>
                        <label for = 'choose-lga'>Choose LGA/State:</label>
                        <select id = 'choose-lga' name = 'choose-lga'>                        
                """;

        //Mario: put all State names as dropdown menu options
        html = html + "<optgroup label = 'States'>";
            for(String name: stateNames){
                html = html + "<option>" + name + "</option>";
            }
        html = html + "</optgroup>";

        //Mario: put all LGA names as dropdown menu options
        html = html + "<optgroup label = 'LGAs'>";
            for(String name : lgaNames){
                html = html + "<option>" + name + "</option>";
            }                
        html = html + "</optgroup>";

        //Mario: close the dropdown menu, then the form-group div          
        html = html + "</select>";
        html = html + "</div>";
        //Mario: create the filters for age demographics

        //Mario: creates filters for level of education

        //Mario: creates filters for household weekly income

            //Mario: create button to submit the form, this will require everything to be selected at once currently
        html = html + "<button type = 'submit' class = 'btn btn-primary'>Choose LGA/State</button>";           
        html = html + "</div>";
        //Mario: finally, close the form
        html = html + "</form>";

        ArrayList<LGA> currentLGA = new ArrayList<LGA>();

        ArrayList<String> ageCategories = new ArrayList<String>();
        ArrayList<String> ageHtmlClasses = new ArrayList<String>();
        HashMap<String, Integer> agePeople2016 = new HashMap<String, Integer>();
        HashMap<String, Integer> agePeople2021 = new HashMap<String, Integer>();

        ArrayList<String> schoolCategories = new ArrayList<String>();
        ArrayList<String> schoolHtmlClasses = new ArrayList<String>();
        HashMap<String, Integer> schoolPeople2016 = new HashMap<String, Integer>();
        HashMap<String, Integer> schoolPeople2021 = new HashMap<String, Integer>();

        ArrayList<String> householdCategories = new ArrayList<String>();
        ArrayList<String> householdHtmlClasses = new ArrayList<String>();
        HashMap<String, Integer> householdNumber2016 = new HashMap<String, Integer>();
        HashMap<String, Integer> householdNumber2021 = new HashMap<String, Integer>();

        //Mario: get form results
        String choose_lga = context.formParam("choose-lga");

        boolean noRanking = true;
        boolean rankAge = false;
        boolean rankSchool = false;
        boolean rankHousehold = false;

        String rank_age_range = context.formParam("ranking-age-range");
        String rank_age_gender = context.formParam("ranking-age-gender");
        String rank_age_status = context.formParam("ranking-age-status");
        String rank_age_proportion = context.formParam("ranking-age-proportion");
        if(rank_age_range != null && rank_age_gender != null && rank_age_status != null && rank_age_proportion != null){
            noRanking = false;
            rankAge = true;
        }

        String rank_school_year = context.formParam("ranking-school-year");
        String rank_school_gender = context.formParam("ranking-school-gender");
        String rank_school_status = context.formParam("ranking-school-status");
        String rank_school_proportion = context.formParam("ranking-school-proportion");
        if(rank_school_year != null && rank_school_gender != null && rank_school_status != null && rank_school_proportion != null){
            noRanking = false;
            rankSchool = true;
        }
        
        String rank_household_income = context.formParam("ranking-household-income");
        String rank_household_status = context.formParam("ranking-household-status");
        String rank_household_proportion = context.formParam("ranking-household-proportion");
        if(rank_household_income != null && rank_household_proportion != null && rank_household_status != null){
            noRanking = false;
            rankHousehold = true;
        }
        /*Mario: as the method for choosing the current LGA returns an ArrayList rather than a single object,
         * We need indexes for the two different years in each object, which are always the same.*/

        int index2016 = 0;
        int index2021 = 1;
        

        //Mario: if there is no input in the form yet, let the users know.

        if(choose_lga == null && noRanking) {
            html = html + "<div class = 'information'>";
            html = html + "<h2> No LGA/State selected.</h2>";
            html = html + "</div>";
        }         
        else {
            html = html + "<div class = 'information'>";

            if(choose_lga == null){

                currentLGA = jdbc.getLastLGA();

                if(stateNames.contains(currentLGA.get(index2021).getName())){
                    html = html + "<h2>" + currentLGA.get(index2021).getName() + "</h2>";
                } else {
                    if(currentLGA.get(index2021).checkFullData()){
                        html = html + getLGADetails(currentLGA.get(index2021));
                    } else {
                        html = html + getLGADetails(currentLGA.get(index2016));
                    }    
                }
            } 
            else if(stateNames.contains(choose_lga)){
                currentLGA = jdbc.getSpecificLga(states, choose_lga);

                html = html + "<h2>" + currentLGA.get(index2021).getName() + "</h2>";

                jdbc.setLastState(currentLGA);

            } else {
                currentLGA = jdbc.getSpecificLga(lgas, choose_lga);
        
                if(currentLGA.get(index2021).checkFullData()){
                    html = html + getLGADetails(currentLGA.get(index2021));
                } else {
                    html = html + getLGADetails(currentLGA.get(index2016));
                }

                jdbc.setLastLGA(currentLGA);
            }

            for(LGA lga : currentLGA){
                html = html + displayTotalPopulation(lga);
            }

            if(currentLGA.get(index2016).checkFullData()){
                if(currentLGA.get(index2021).checkFullData()){
                    int difference = currentLGA.get(index2021).getTotalPopulation() - currentLGA.get(index2016).getTotalPopulation();
                    html = html + displayPopulationDifference(difference);
                }
            }

            html = html + "</div>";

            //Mario: Age Demographic code block.

            html = html + """
            <div class = 'options'>
            <h2>Filter age demographic:</h2>
                <div class = 'form-group'>
                    <div class = 'filter'>
                    <label for = 'choose-age-range'>Choose age range:</label>
                    <select id = 'choose-age-range' onchange='showAge()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = '0_4'>0-4 years old</option>
                        <option value = '5_9'>5-9 years old</option>
                        <option value = '10_14'>10-14 years old</option>
                        <option value = '15_19'>15-19 years old</option>
                        <option value = '20_24'>20-24 years old</option>
                        <option value = '25_29'>25-29 years old</option>
                        <option value = '30_34'>30-34 years old</option>
                        <option value = '35_39'>35-39 years old</option>
                        <option value = '40_44'>40-44 years old</option>
                        <option value = '45_49'>45-49 years old</option>
                        <option value = '50_54'>50-54 years old</option>
                        <option value = '55_59'>55-59 years old</option>
                        <option value = '60_64'>60-64 years old</option>
                        <option value = '65_older'>65 and older</option>
                    </select>
                    </div>
                    <div class = 'filter'>
                    <label for = 'choose-age-status'>Choose indigenous status:</label>
                    <select id = 'choose-age-status' onchange='showAge()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = 'indigenous'>Indigenous</option>
                        <option value = 'non_indigenous'>Non-indigenous</option>
                    </select>
                    </div>
                    <div class = 'filter'>
                    <label for = 'choose-age-gender'>Choose gender:</label>
                    <select id = 'choose-age-gender' onchange='showAge()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = 'm'>Male</option>
                        <option value = 'f'>Female</option>
                    </select>
                    </div>
                </div>  
                </div>               
                """; 
                
            html = html + "<div class = 'information'>";
            for(LGA lga : currentLGA){ 

                if(lga.checkFullData()){  

                    for(AgeDemographic age : lga.getAgeDemographic()){
                        String age_gender_text = reverseGender(age.getGender());
                        String age_status_text = reverseStatus(age.getStatus());
                        String age_range_text = reverseRange(age.getRange());
                                        
                        String category = age_status_text + " " + age_gender_text + " aged " + age_range_text;
                        String htmlClass = age.getRange() + age.getStatus() + age.getGender();

                        if(!(ageCategories.contains(category))){
                            ageCategories.add(category);
                        }

                        if(!(ageHtmlClasses.contains(htmlClass))){
                            ageHtmlClasses.add(htmlClass);
                        }

                        if(lga.getYear() == 2016){ 
                            agePeople2016.put(category, age.getNumber());
                        } else {
                            agePeople2021.put(category, age.getNumber());
                        }

                        html = html + "<p class = 'age-info " + htmlClass + "'>" + lga.getYear() + " - " + 
                            age.getNumber() + " " + category + ".</p>";
                    }
                } else {
                    html = html + "<p class = 'age-info missing-age-info'>Data unavailable for " + lga.getYear() + ".</p>";
                }
            }
            
            if(currentLGA.get(index2016).checkFullData()){
                if(currentLGA.get(index2021).checkFullData()){
                    for(int i = 0; i < ageCategories.size(); i++){
                        int difference  = agePeople2021.get(ageCategories.get(i)) - agePeople2016.get(ageCategories.get(i));
                        html = html + displayAgeDemographicDifference(ageCategories.get(i), ageHtmlClasses.get(i), difference);
                    }
                } else {
                    html = html + "<p class = 'age-info missing-age-info'>As data is missing for 2021, no comparison can be made.";
                }
            } else {
                html = html + "<p class = 'age-info missing-age-info'>As data is missing for 2016, no comparison can be made.";
            }
            html = html + "</div>";

            //Mario: Level of education code block.

            html = html + """
            <div class = 'options'>
            <h2>Filter level of education:</h2>
                <div class = 'form-group'>
                    <div class = 'filter'>
                    <label for = 'choose-school-year'>Choose highest year of school completed:</label>
                    <select id = 'choose-school-year' onchange='showSchoolYear()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = 'did_not_go_to_school'>Did not go to school</option>    
                        <option value = 'y8_below'>Year 8 or below</option>
                        <option value = 'y9_equivalent'>Year 9 equivalent</option>
                        <option value = 'y10_equivalent'>Year 10 equivalent</option>
                        <option value = 'y11_equivalent'>Year 11 equivalent</option>
                        <option value = 'y12_equivalent'>Year 12 equivalent</option>
                    </select>
                    </div>
                    <div class = 'filter'>
                    <label for = 'choose-school-status'>Choose indigenous status:</label>
                    <select id = 'choose-school-status' onchange='showSchoolYear()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = 'indigenous'>Indigenous</option>
                        <option value = 'non_indigenous'>Non-indigenous</option>
                    </select>
                    </div>
                    <div class = 'filter'>
                    <label for = 'choose-school-gender'>Choose gender:</label>
                    <select id = 'choose-school-gender' onchange='showSchoolYear()'>
                        <option value='' selected disabled hidden>Choose here</option>
                        <option value = 'm'>Male</option>
                        <option value = 'f'>Female</option>
                    </select>
                    </div>
                </div>    
                </div>             
            """;

            html = html + "<div class = 'information'>";
            for(LGA lga : currentLGA){
                if(lga.checkFullData()){

                    for(HighestSchoolYear schoolYear : lga.getHighestSchoolYear()){
                        String school_year_text = reverseSchoolYear(schoolYear.getSchoolYear());
                        String school_status_text = reverseStatus(schoolYear.getStatus());
                        String school_gender_text = reverseGender(schoolYear.getGender());

                        String category = getSchoolCategory(school_year_text, school_gender_text, school_status_text);
                        String htmlClass = schoolYear.getSchoolYear() + schoolYear.getStatus() + schoolYear.getGender();

                        if(!(schoolCategories.contains(category))){
                            schoolCategories.add(category);
                        }

                        if(!(schoolHtmlClasses.contains(htmlClass))){
                            schoolHtmlClasses.add(htmlClass);
                        }

                        if(lga.getYear() == 2016){ 
                            schoolPeople2016.put(category, schoolYear.getNumber());
                        } else {
                            schoolPeople2021.put(category, schoolYear.getNumber());
                        }

                        html = html + "<p class = 'school-info " + htmlClass + "'>" + lga.getYear() + " - "
                                + schoolYear.getNumber() + " " + category + ".</p>";
                    } 
                } else {
                    html = html + "<p class = 'school-info missing-school-info'>Data unavailable for " + lga.getYear() + ".</p>";
                }
            }

            if(currentLGA.get(index2016).checkFullData()){
                if(currentLGA.get(index2021).checkFullData()){
                    for(int i = 0; i < schoolCategories.size(); i++){
                        int difference  = schoolPeople2021.get(schoolCategories.get(i)) - schoolPeople2016.get(schoolCategories.get(i));
                        html = html + displaySchoolYearDifference(schoolCategories.get(i), schoolHtmlClasses.get(i), difference);
                    }
                } else {
                    html = html + "<p class = 'school-info missing-school-info'>As data is missing for 2021, no comparison can be made.";
                }
            } else {
                html = html + "<p class = 'school-info missing-school-info'>As data is missing for 2016, no comparison can be made.";
            }
            html = html + "</div>";


            //Mario: Household income code block.

            html = html + """
                <div class = 'options'>
                <h2>Filter households by weekly income:</h2>
                    <div class = 'form-group'>
                        <div class = 'filter'>
                        <label for = 'choose-household-income'>Choose weekly household income:</label>
                            <select id = 'choose-household-income' onchange='showHouseholds()'>
                                <option value='' selected disabled hidden>Choose here</option>
                                <option value = '1_149'>$1 - $149 per week</option>    
                                <option value = '150_299'>$150 - $299 per week</option>
                                <option value = '300_399'>$300 - $399 per week</option>
                                <option value = '400_499'>$400 - $499 per week</option>
                                <option value = '500_649'>$500 - $649 per week</option>
                                <option value = '650_799'>$650 - $799 per week</option>
                                <option value = '800_999'>$800 - $999 per week</option>
                                <option value = '1000_1249'>$1000 - $1249 per week</option>
                                <option value = '1250_1499'>$1250 - $1499 per week</option>
                                <option value = '1500_1999'>$1500 - $1999 per week</option>
                                <option value = '2000_2499'>$2000 - $2499 per week</option>
                                <option value = '2500_2999'>$2500 - $2999 per week</option>
                                <option value = '3000_more'>$3000 or more</option>
                            </select>
                        </div>
                        <div class = 'filter'>
                        <label for = 'choose-household-status'>Choose indigenous status:</label>
                            <select id = 'choose-household-status' onchange='showHouseholds()'>
                                <option value='' selected disabled hidden>Choose here</option>
                                <option value = 'indigenous'>Indigenous</option>
                                <option value = 'non_indigenous'>Non-indigenous</option>
                            </select>
                        </div>
                    </div>  
                </div>            
                """;

            html = html + "<div class = 'information'>";
            for(LGA lga : currentLGA){
                if(lga.checkFullData()){

                    lga.setHouseholdIncome(cleanHouseholdIncome(lga.getHouseholdIncome()));

                    for(HouseholdIncome householdIncome : lga.getHouseholdIncome()){
                        String household_income_text = reverseIncome(householdIncome.getIncome());
                        String household_status_text = reverseStatus(householdIncome.getStatus());

                        String category = household_status_text + " households making " + household_income_text;
                        String htmlClass = householdIncome.getIncome() + householdIncome.getStatus();

                        if(!(householdCategories.contains(category))){
                            householdCategories.add(category);
                        }

                        if(!(householdHtmlClasses.contains(htmlClass))){
                            householdHtmlClasses.add(htmlClass);
                        }

                        if(lga.getYear() == 2016){ 
                            householdNumber2016.put(category, householdIncome.getNumber());
                        } else {
                            householdNumber2021.put(category, householdIncome.getNumber());
                        }

                        html = html + "<p class = 'household-info " + htmlClass + "'>" + lga.getYear() + " - "
                                + householdIncome.getNumber() + " " + category + ".</p>";

                    }
                } else {
                    html = html + "<p class = 'household-info missing-household-info'>Data unavailable for " + lga.getYear() + ".</p>";
                }
            }

            if(currentLGA.get(index2016).checkFullData()){
                if(currentLGA.get(index2021).checkFullData()){
                    for(int i = 0; i < householdCategories.size(); i++){
                        int difference  = householdNumber2021.get(householdCategories.get(i)) - householdNumber2016.get(householdCategories.get(i));
                        html = html + displayHouseholdDifference(householdCategories.get(i), householdHtmlClasses.get(i), difference);
                    }
                } else {
                    html = html + "<p class = 'household-info missing-household-info'>As data is missing for 2021, no comparison can be made.";
                }
            } else {
                html = html + "<p class = 'household-info missing-household-info'>As data is missing for 2016, no comparison can be made.";
            }
            html = html + "</div>";
        }

        html = html + """
                <p></p>
                <div class = 'ranking-header'>
                    <h1>LGA/State ranking<h1>
                    <p>On this section of the page, you can view the ranking of all LGAs or states for the category you select.
                    Specifically, each LGA/State is ranked by the proportion of people that fit the specific category filter to the comparison filter.
                    In addition, the ranking for the LGA/State chosen above will be displayed on the top.</p>
                </div>
                """;
        
        html = html + """
            <div class = 'select-lga'>
                <label for = 'choose-ranking-filter'>Select LGA/State ranking category:</label>
                <select id = 'choose-ranking-filter' onchange='showFilters()'>
                    <option value='' selected disabled hidden>Choose here</option>
                    <option value = 'age'>Age Demographic</option>
                    <option value = 'school'>Level of Education</option>
                    <option value = 'households'>Households by weekly income</option>
                </select>
            </div>
            """;

        html = html + "<div id = 'ranking-filter'></div>";
        
        ArrayList<RankedLGA> ranking2016 = new ArrayList<RankedLGA>();
        ArrayList<RankedLGA> ranking2021 = new ArrayList<RankedLGA>();

        if(rankAge){

            String rank_age_range_db = rangeInDatabaseFormat(rank_age_range);
            String rank_age_gender_db = genderInDatabaseFormat(rank_age_gender);
            String rank_age_status_db = statusInDatabaseFormat(rank_age_status);
            String rank_age_proportion_db = ageTotalInDatabaseFormat(rank_age_proportion, rank_age_status_db, rank_age_range_db, rank_age_gender_db);
            String rank_age_proportion_text = ageTotalInTextFormat(rank_age_proportion, rank_age_status, rank_age_range, rank_age_gender);
            
            
            if(currentLGA.get(index2016).getCode() == 0 || currentLGA.get(index2021).getCode() == 0){
                html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_age_range + " " + rank_age_status.toLowerCase() + " " + rank_age_gender.toLowerCase() + 
                " population to the total " + rank_age_proportion_text + " population in states.</h3>";
                ranking2016 = jdbc.getRankedAgeDemographicState(2016, rank_age_proportion_db, rank_age_gender_db, 
                                                            rank_age_range_db, rank_age_status_db);
                ranking2021 = jdbc.getRankedAgeDemographicState(2021, rank_age_proportion_db, rank_age_gender_db, 
                                                            rank_age_range_db, rank_age_status_db);
            } else {
                html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_age_range + " " + rank_age_status.toLowerCase() + " " + rank_age_gender.toLowerCase() + 
                " population to the total " + rank_age_proportion_text + " population in LGAs.</h3>";
                ranking2016 = jdbc.getRankedAgeDemographic(2016, rank_age_proportion_db, rank_age_gender_db, 
                                                            rank_age_range_db, rank_age_status_db);
                ranking2021 = jdbc.getRankedAgeDemographic(2021, rank_age_proportion_db, rank_age_gender_db, 
                                                            rank_age_range_db, rank_age_status_db);
            }

            html = html + "<div class = 'ranked-lgas'>";
            
            html = html + createAgeTable(ranking2016, currentLGA, 2016, rank_age_gender, rank_age_status, rank_age_range, rank_age_proportion_text);
            html = html + createAgeTable(ranking2021, currentLGA, 2021, rank_age_gender, rank_age_status, rank_age_range, rank_age_proportion_text);

            html = html + "</div>";
        } else if(rankSchool){

            String rank_school_year_db = schoolYearInDatabaseFormat(rank_school_year);
            String rank_school_gender_db = genderInDatabaseFormat(rank_school_gender);
            String rank_school_status_db = statusInDatabaseFormat(rank_school_status);
            String rank_school_proportion_db = schoolTotalInDatabaseFormat(rank_school_proportion, rank_school_status_db, rank_school_year_db, rank_school_gender_db);
            String rank_school_proportion_text = schoolTotalInTextFormat(rank_school_proportion, rank_school_status, rank_school_year, rank_school_gender);

            if(currentLGA.get(index2016).getCode() == 0 || currentLGA.get(index2021).getCode() == 0){
                if(rank_school_year.equals("Did not go to school")){
                    html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_school_status.toLowerCase() + " " + rank_school_gender.toLowerCase() + 
                    " population which has not gone to school to the total " + rank_school_proportion_text + " in states.</h3>";
                } else {
                    html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_school_status.toLowerCase() + " " + rank_school_gender.toLowerCase() + 
                    " population which has completed " + rank_school_year + " to the total " + rank_school_proportion_text + " in states.</h3>";
                }
                ranking2016 = jdbc.getRankedSchoolYearState(2016, rank_school_proportion_db, rank_school_gender_db, 
                                                            rank_school_year_db, rank_school_status_db);
                ranking2021 = jdbc.getRankedSchoolYearState(2021, rank_school_proportion_db, rank_school_gender_db, 
                                                            rank_school_year_db, rank_school_status_db);
            } else {
                if(rank_school_proportion.equals("Did not go to school")){
                    html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_school_status.toLowerCase() + " " + rank_school_gender.toLowerCase() + 
                    " population which has not gone to school to the total " + rank_school_proportion_text + " in LGAs.</h3>";
                } else {
                    html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_school_status.toLowerCase() + " " + rank_school_gender.toLowerCase() + 
                    " population which has completed " + rank_school_year + " to the total " + rank_school_proportion_text + " in LGAs.</h3>";
                }
                /*html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_age_range + " " + rank_age_status.toLowerCase() + " " + rank_age_gender.toLowerCase() + 
                " population to total " + rank_age_proportion_text + " population in LGAs.</h3>";*/
                ranking2016 = jdbc.getRankedSchoolYear(2016, rank_school_proportion_db, rank_school_gender_db, 
                                                            rank_school_year_db, rank_school_status_db);
                ranking2021 = jdbc.getRankedSchoolYear(2021, rank_school_proportion_db, rank_school_gender_db, 
                                                            rank_school_year_db, rank_school_status_db);
            }
            html = html + "<div class = 'ranked-lgas'>";

            html = html + createAgeTable(ranking2016, currentLGA, 2016, rank_school_gender, rank_school_status, rank_school_year, rank_school_proportion_text);
            html = html + createAgeTable(ranking2021, currentLGA, 2021, rank_school_gender, rank_school_status, rank_school_year, rank_school_proportion_text);

            html = html + "</div>";

        } else if(rankHousehold){
            String rank_household_income_db2016 = incomeInDatabaseFormat(rank_household_income, 2016);
            String rank_household_income_db2021 = incomeInDatabaseFormat(rank_household_income, 2021);
            String rank_household_status_db = statusInDatabaseFormat(rank_household_status);
            String rank_household_proportion_db2016 = householdTotalInDatabaseFormat(rank_household_proportion, rank_household_status_db, rank_household_income_db2016);
            String rank_household_proportion_db2021 = householdTotalInDatabaseFormat(rank_household_proportion, rank_household_status_db, rank_household_income_db2021);
            String rank_household_proportion_text = householdTotalInTextFormat(rank_household_proportion, rank_household_status, rank_household_income);

            if(currentLGA.get(index2016).getCode() == 0 || currentLGA.get(index2021).getCode() == 0){
                html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_household_status.toLowerCase() +
                " households making " + rank_household_income + " to the total number of " + rank_household_proportion_text + " in states.</h3>";

                ranking2016 = jdbc.getRankedHouseholdsState(2016, rank_household_proportion_db2016, rank_household_income_db2016, rank_household_status_db);
                ranking2021 = jdbc.getRankedHouseholdsState(2021, rank_household_proportion_db2021, rank_household_income_db2021, rank_household_status_db);
            } else {
                html = html + "<h3 id = 'ranking-chosen'>Currently ranking proportion of " + rank_household_status.toLowerCase() +
                " households making " + rank_household_income + " to the total number of " + rank_household_proportion_text + " in LGAs.</h3>";

                ranking2016 = jdbc.getRankedHouseholds(2016, rank_household_proportion_db2016, rank_household_income_db2016, rank_household_status_db);
                ranking2021 = jdbc.getRankedHouseholds(2021, rank_household_proportion_db2021, rank_household_income_db2021, rank_household_status_db);
            }

            html = html + "<div class = 'ranked-lgas'>";

            html = html + createHouseholdTable(ranking2016, currentLGA, 2016, rank_household_status, rank_household_income, rank_household_proportion_text);
            html = html + createHouseholdTable(ranking2021, currentLGA, 2021, rank_household_status, rank_household_income, rank_household_proportion_text);

            html = html + "</div>";
        }


        html = html + 
                "<script>" +
                    "\nfunction showAge() {" +
                        "\n var ageRange = document.getElementById('choose-age-range').value;" + 
                        "\n var ageStatus = document.getElementById('choose-age-status').value;" + 
                        "\n var ageGender = document.getElementById('choose-age-gender').value;" +                       

                        "\n var ageFilter = ageRange + ageStatus + ageGender" +

                        "\n const ageIrrelevant = document.getElementsByClassName('age-info')" +
                        "\n const ageInformation = document.getElementsByClassName(ageFilter)" + 
                        "\n const missingAgeData = document.getElementsByClassName('missing-age-info')" +

                        "\n for(var i = 0; i < ageIrrelevant.length; i++){" +
                        "\n ageIrrelevant[i].style.display = 'none'" +
                        "\n}" +

                        "\n for(var i = 0; i < ageInformation.length; i++){" +
                        "\n ageInformation[i].style.display = 'block'" +
                        "\n}" +

                        "\n if(ageInformation.length != 0){" +
                        "\n for(var i = 0; i < missingAgeData.length; i++){" +
                        "\n missingAgeData[i].style.display = 'block'" +
                        "\n}" +
                        "\n}" +
                    "}" +
                "</script>";

        html = html + 
                "<script>" +
                    "\nfunction showSchoolYear() {" +
                        "\n var schoolYear = document.getElementById('choose-school-year').value;" + 
                        "\n var schoolStatus = document.getElementById('choose-school-status').value;" + 
                        "\n var schoolGender = document.getElementById('choose-school-gender').value;" +                       

                        "\n var schoolFilter = schoolYear + schoolStatus + schoolGender" +

                        "\n const schoolIrrelevant = document.getElementsByClassName('school-info')" +
                        "\n const schoolInformation = document.getElementsByClassName(schoolFilter)" + 
                        "\n const missingSchoolData = document.getElementsByClassName('missing-school-info')" +

                        "\n for(var i = 0; i < schoolIrrelevant.length; i++){" +
                        "\n schoolIrrelevant[i].style.display = 'none'" +
                        "\n}" +

                        "\n for(var i = 0; i < schoolInformation.length; i++){" +
                        "\n schoolInformation[i].style.display = 'block'" +
                        "\n}" +

                        "\n if(schoolInformation.length != 0){" +
                        "\n for(var i = 0; i < missingSchoolData.length; i++){" +
                        "\n missingSchoolData[i].style.display = 'block'" +
                        "\n}" +
                        "\n}" +
                    "}" +
                "</script>";

        html = html + 
                "<script>" +
                    "\nfunction showHouseholds() {" +
                        "\n var householdIncome = document.getElementById('choose-household-income').value;" + 
                        "\n var householdStatus = document.getElementById('choose-household-status').value;" +                      

                        "\n var householdFilter = householdIncome + householdStatus" +

                        "\n const householdIrrelevant = document.getElementsByClassName('household-info')" +
                        "\n const householdInformation = document.getElementsByClassName(householdFilter)" + 
                        "\n const missingHouseholdData = document.getElementsByClassName('missing-household-info')" +

                        "\n for(var i = 0; i < householdIrrelevant.length; i++){" +
                        "\n householdIrrelevant[i].style.display = 'none'" +
                        "\n}" +

                        "\n for(var i = 0; i < householdInformation.length; i++){" +
                        "\n householdInformation[i].style.display = 'block'" +
                        "\n}" +

                        "\n if(householdInformation.length != 0){" +
                        "\n for(var i = 0; i < missingHouseholdData.length; i++){" +
                        "\n missingHouseholdData[i].style.display = 'block'" +
                        "\n}" +
                        "\n}" +
                    "}" +
                "</script>";

        html = html + 
                "<script>" +
                    "\nfunction showFilters() {" +
                        "\n console.log('Lol');" +
                        "\n var filter = document.getElementById('choose-ranking-filter').value;" +
                        "\n if(filter == 'age') {" +
                        "\n document.getElementById('ranking-filter').innerHTML = `";
        html = html + """
                    <div class = 'form-group rank-group'>
                        <form action = 'focus-lga-state.html' method = 'post'>
                            <div class = 'filter'>
                                <label for = 'ranking-age-range'>Choose age range:</label>
                                <select id = 'ranking-age-range' name = 'ranking-age-range' onchange = 'updateAgeOptions()''>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>0-4 years old</option>
                                    <option>5-9 years old</option>
                                    <option>10-14 years old</option>
                                    <option>15-19 years old</option>
                                    <option>20-24 years old</option>
                                    <option>25-29 years old</option>
                                    <option>30-34 years old</option>
                                    <option>35-39 years old</option>
                                    <option>40-44 years old</option>
                                    <option>45-49 years old</option>
                                    <option>50-54 years old</option>
                                    <option>55-59 years old</option>
                                    <option>60-64 years old</option>
                                    <option>65 and older</option>
                                </select>
                            </div>
                            <div class = 'filter'>
                                <label for = 'ranking-age-status'>Choose indigenous status:</label>
                                <select id = 'ranking-age-status' name = 'ranking-age-status' onchange = 'updateAgeOptions()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Indigenous</option>
                                    <option>Non-indigenous</option>
                                </select>
                            </div>
                            <div class = 'filter'>
                                <label for = 'ranking-age-gender'>Choose gender:</label>
                                <select id = 'ranking-age-gender' name = 'ranking-age-gender' onchange = 'updateAgeOptions()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select>
                            </div>
                            <p></p>
                            <div class = 'filter'>
                                <label for = 'ranking-age-proportion'>Compare to:</label>
                                <select id = 'ranking-age-proportion' name = 'ranking-age-proportion'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option value = 'total'>Total population</option>
                                    <option value = 'age' id = 'age-range-option'>Total population aged 0 - 4 years old</option>
                                    <option value = 'status' id = 'age-status-option'>Total indigenous population</option>
                                    <option value = 'gender' id = 'age-gender-option'>Total male population</option>       
                                    <option value = 'age-and-status' id = 'age-range-status-option'>Total indigenous population aged 0 - 4 years old</option>
                                    <option value = 'age-and-gender' id = 'age-range-gender-option'>Total female population aged 0 - 4 years old</option>
                                    <option value = 'gender-and-status' id = 'age-gender-status-option'>Total indigenous female population</option>            
                                </select>
                            </div>
                            <button type = 'submit' class = 'btn btn-primary'>Rank Age Demographics</button>
                        </form>
                    </div>
                    `;
                } else if(filter == 'school') {
                    document.getElementById('ranking-filter').innerHTML = `
                        <div class = 'form-group rank-group'>
                            <form action = 'focus-lga-state.html' method = 'post'>
                            <div class = 'filter'>
                                <label for = 'ranking-school-year'>Choose highest year of school completed:</label>
                                <select id = 'ranking-school-year' name = 'ranking-school-year' onchange = 'updateSchoolOptions()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Did not go to school</option>    
                                    <option>Year 8 or below</option>
                                    <option>Year 9 equivalent</option>
                                    <option>Year 10 equivalent</option>
                                    <option>Year 11 equivalent</option>
                                    <option>Year 12 equivalent</option>
                                </select>
                            </div>
                            <div class = 'filter'>
                                <label for = 'ranking-school-status'>Choose indigenous status:</label>
                                <select id = 'ranking-school-status' name = 'ranking-school-status' onchange = 'updateSchoolOptions()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Indigenous</option>
                                    <option>Non-indigenous</option>
                                </select>
                            </div>
                            <div class = 'filter'>
                                <label for = 'ranking-school-gender'>Choose gender:</label>
                                <select id = 'ranking-school-gender' name = 'ranking-school-gender' onchange = 'updateSchoolOptions()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select>
                            </div>
                            <p></p>
                            <div class = 'filter'>
                                <label for = 'ranking-school-proportion'>Compare to:</label>
                                <select id = 'ranking-school-proportion' name = 'ranking-school-proportion'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option value = 'total'>Total population</option>
                                    <option value = 'year' id = 'school-year-option'>Total population which has not gone to school</option>
                                    <option value = 'status' id = 'school-status-option'>Total indigenous population</option>
                                    <option value = 'gender' id = 'school-gender-option'>Total male population</option>  
                                    <option value = 'year-and-status' id = 'school-year-status-option'>Total indigenous population which has not gone to school</option>
                                    <option value = 'year-and-gender' id = 'school-year-gender-option'>Total male population which has not gone to school</option>
                                    <option value = 'gender-and-status' id = 'school-gender-status-option'>Total indigenous female population</option>                 
                                </select>
                            </div>
                            <button type = 'submit' class = 'btn btn-primary'>Rank Level of Education</button>
                        </form>
                    </div>
                    `;
                } else if(filter == 'households') {
                    document.getElementById('ranking-filter').innerHTML = `
                        <div class = 'form-group rank-group'>
                            <form action = 'focus-lga-state.html' method = 'post'>
                            <div class = 'filter'>
                                <label for = 'ranking-household-income'>Choose weekly household income:</label>
                                <select id = 'ranking-household-income' name = 'ranking-household-income' onchange = 'updateHouseholdOptionsIncome()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>$1 - $149 per week</option>    
                                    <option>$150 - $299 per week</option>
                                    <option>$300 - $399 per week</option>
                                    <option>$400 - $499 per week</option>
                                    <option>$500 - $649 per week</option>
                                    <option>$650 - $799 per week</option>
                                    <option>$800 - $999 per week</option>
                                    <option>$1000 - $1249 per week</option>
                                    <option>$1250 - $1499 per week</option>
                                    <option>$1500 - $1999 per week</option>
                                    <option>$2000 - $2499 per week</option>
                                    <option>$2500 - $2999 per week</option>
                                    <option>$3000 or more</option>
                                </select>
                            </div>
                            <div class = 'filter'>
                                <label for = 'ranking-household-status'>Choose indigenous status:</label>
                                <select id = 'ranking-household-status' name = 'ranking-household-status' onchange = 'updateHouseholdOptionsStatus()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Indigenous</option>
                                    <option>Non-indigenous</option>
                                </select>
                            </div>
                            <p></p>
                            <div class = 'filter'>
                                <label for = 'ranking-household-proportion'>Compare to:</label>
                                <select id = 'ranking-household-proportion' name = 'ranking-household-proportion'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option value = 'total'>Total households</option>
                                    <option value = 'income' id = 'household-income-option'>Total households making $1 - $149 per week</option>
                                    <option value = 'status' id = 'household-status-option'>Total indigenous households</option>               
                                </select>
                            </div>
                            <button type = 'submit' class = 'btn btn-primary'>Rank Households by Weekly Income</button>
                    </div>
                    `;
                }
            }</script>
            """;
        
        html = html + """
                <script>
                    function updateAgeOptions(){
                        var optionRange = document.getElementById('ranking-age-range').value;
                        var optionGender = document.getElementById('ranking-age-gender').value;
                        var optionStatus = document.getElementById('ranking-age-status').value;

                        document.getElementById('age-range-option').innerHTML = \"Total population aged \" + optionRange;
                        document.getElementById('age-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population\";
                        document.getElementById('age-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population\";

                        document.getElementById('age-range-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population aged \" + optionRange;
                        document.getElementById('age-range-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population aged \" + optionRange;
                        document.getElementById('age-gender-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" \" + optionGender.toLowerCase() + \" population\";
                    }

                    function updateSchoolOptions(){
                        var optionYear = document.getElementById('ranking-school-year').value;
                        var optionGender = document.getElementById('ranking-school-gender').value;
                        var optionStatus = document.getElementById('ranking-school-status').value;

                        if(optionYear == 'Did not go to school'){
                            document.getElementById('school-year-option').innerHTML = \"Total population which has not gone to school\";
                            document.getElementById('school-year-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population which has not gone to school\";
                            document.getElementById('school-year-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population which has not gone to school\";
                        } else if(optionYear == ''){
                            document.getElementById('school-year-option').innerHTML = \"Total population\" + optionYear;
                            document.getElementById('school-year-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population\" + optionYear;
                            document.getElementById('school-year-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population\" + optionYear;
                        } else {
                            document.getElementById('school-year-option').innerHTML = \"Total population which has completed \" + optionYear;
                            document.getElementById('school-year-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population which has completed \" + optionYear;
                            document.getElementById('school-year-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population which has completed \" + optionYear;
                        }

                        document.getElementById('school-gender-option').innerHTML = \"Total \" + optionGender.toLowerCase() + \" population\";
                        document.getElementById('school-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" population\";
                        document.getElementById('school-gender-status-option').innerHTML = \"Total \" + optionStatus.toLowerCase() + \" \" + optionGender.toLowerCase() + \" population\";
                    }

                    function updateHouseholdOptionsIncome(){
                        var option = document.getElementById('ranking-household-income').value;
                        document.getElementById('household-income-option').innerHTML = \"Total households making \" + option.toLowerCase();
                    }
                    function updateHouseholdOptionsStatus(){
                        var option = document.getElementById('ranking-household-status').value;
                        document.getElementById('household-status-option').innerHTML = \"Total \" + option.toLowerCase() + \" households\";
                    }
                </script>
                """;
            
        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'> 
                <div class = 'footer-content'>              
                    <h2>We acknowledge the traditional owners of the lands and waters 
                        on which Australians live and work, and pay respects to their 
                        Elders past, present and emerging.</h2>
                    <div class = 'line'></div>
                    <div class = 'links'>
                        <div class = 'link-column'>
                            <a href = '/'>Homepage</a>
                            <a href = 'latest-2021-data.html'>Latest 2021 data</a>
                            <a href = 'find-similar-lga.html'>Find Similar LGAs</a>
                        
                        </div>
                        <div class = 'link-column'>
                            <a href = 'about.html'>About us</a>
                            <a href = 'focus-lga-state.html'>Focus by LGA/State</a>
                            <a href = 'gap-scores.html'>Gap Scores</a>
                        </div>
                    </div>
                </div>
                <p>&copy; 2022 Closing the Gap Progress. All rights reserved.</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
















    //Mario: this method gets all the LGA info displayed on the page, I mainly needed it to test the LGA objects.
    public String getLGADetails(LGA currentLGA){
        String html = "";


        html = html + "<h2>" + currentLGA.getName() + "</h2>";

        html = html + "<div class = 'lga-details'>";

        html = html + "<p>Area: " + String.format("%.2fkm", currentLGA.getArea()) + "<sup>2</sup></p>";
        html = html + "<p>Type: " + getLGAType(currentLGA.getType()) + "</p>";
        html = html + "<p>State: " + currentLGA.getState() + "</p>";

        html = html + "</div>";
        return html;
    }

    public String getLGAType(String type){
        switch(type){
            case "C":
                type = "City";
                break;
            case "A":
                type = "Area";
                break;
            case "RC":
                type = "Rural City";
                break;
            case "B":
                type = "Borough";
                break;
            case "S":
                type = "Shire";
                break;
            case "T":
                type = "Town";
                break;
            case "R":
                type = "Regional Council";
                break;
            case "M":
                type = "Municipality";
                break;
            case "DC":
                type = "District Council";
                break;
            case "AC":
                type = "Aboriginal Council";
                break;
            default:
                type = "unknown";
                break;
        }
        return type;
    }

    public String displayTotalPopulation(LGA lga){
        String html = "";

        if(lga.checkFullData()){
            html = html + "<p>Data for total population in " + lga.getYear() + ": " + lga.getTotalPopulation() + " people.</p>";
        } else {
            html = html + "<p>No data for total population in " + lga.getYear() + ".</p>";
        }

        return html;
    }

    public String displayPopulationDifference(int difference){
        String html = "";

        if(difference < 0){
            html = html + "<p>There are " + Math.abs(difference) + " less people between 2016 and 2021.";
        } else if(difference > 0){
            html = html + "<p>There are " + Math.abs(difference) + " more people between 2016 and 2021.";
        } else if(difference == 0){
            html = html + "<p>There is no difference in the number of people between 2016 and 2021.";
        }

        return html;
    }

    /*Mario: The next methods here use switch-case to convert the filter options to a 
     * format that we can use for our query to the database.
    */

    //Mario: This method converts "Male" or "Female" to just 'm' or 'f'.
    public String genderInDatabaseFormat(String gender){
        switch(gender){
            case "Male":
                gender = "'m'";
                break;
            case "Female":
                gender = "'f'";
                break;
            default:
            //Mario: this default statement should never be executed, so I had some fun with it.
                gender = "wtf";
                break;
        }
        return gender;
    }

    public String reverseGender(String gender){
        switch(gender){
            case "m":
                gender = "men";
                break;
            case "f":
                gender = "women";
                break;
            default:
            //Mario: this default statement should never be executed, so I had some fun with it.
                gender = "wtf";
                break;
        }
        return gender;
    }

    /*Mario: This method converts "Indigenous" or "Non-indigenous" to lowercase,
     * and changes the hyphon in "Non-indigenous" to an underscore.
    */
    public String statusInDatabaseFormat(String status){
        switch(status){
            case "Indigenous":
                status = "'indigenous'";
                break;
            case "Non-indigenous":
                status = "'non_indigenous'";
                break;
            default:
            //Mario: this default statement should never be executed, so I had some fun with it.
                status = "transcended";
                break;
        }
        return status;
    }

    public String reverseStatus(String status){
        switch(status){
            case "indigenous":
                status = "indigenous";
                break;
            case "non_indigenous":
                status = "non-indigenous";
                break;
            default:
            //Mario: this default statement should never be executed, so I had some fun with it.
                status = "transcended";
                break;
        }
        return status;
    }

    /*Mario: This method converts the age range from
     * "x - y years old" to just "x_y".
    */
    public String rangeInDatabaseFormat(String range){
        switch(range){
            case "0-4 years old":
                range = "'0_4'";
                break;   
            case "5-9 years old":
                range = "'5_9'";
                break;
            case "10-14 years old":
                range = "'10_14'";
                break;
            case "15-19 years old":
                range = "'15_19'";       
                break;     
            case "20-24 years old":
                range = "'20_24'";
                break;
            case "25-29 years old":
                range = "'25_29'";
                break;
            case "30-34 years old":
                range = "'30_34'";
                break;                 
            case "35-39 years old":
                range = "'35_39'";
                break;
            case "40-44 years old":
                range = "'40_44'";
                break;
            case "45-49 years old":
                range = "'45_49'";
                break;
            case "50-54 years old":
                range = "'50_54'";
                break;
            case "55-59 years old":
                range = "'55_59'";
                break;
            case "60-64 years old":
                range = "'60_64'";
                break;
            case "65 and older":
                range = "'65_older'";
                break;
            default:
            //Mario: hehe funny inacessible default statement
                range = "immortal";
                break;
        }
        return range;
    }

    public String reverseRange(String range){
        switch(range){
            case "0_4":
                range = "0-4 years old";
                break;   
            case "5_9":
                range = "5-9 years old";
                break;
            case "10_14":
                range = "10-14 years old";
                break;
            case "15_19":
                range = "15-19 years old";       
                break;     
            case "20_24":
                range = "20-24 years old";
                break;
            case "25_29":
                range = "25-29 years old";
                break;
            case "30_34":
                range = "30-34 years old";
                break;                 
            case "35_39":
                range = "35-39 years old";
                break;
            case "40_44":
                range = "40-44 years old";
                break;
            case "45_49":
                range = "45-49 years old";
                break;
            case "50_54":
                range = "50-54 years old";
                break;
            case "55_59":
                range = "55-59 years old";
                break;
            case "60_64":
                range = "60-64 years old";
                break;
            case "65_older":
                range = "65+ years old";
                break;
            default:
            //Mario: hehe funny inacessible default statement
                range = "immortal";
                break;
        }
        return range;
    }

    /*Mario: This method converts the school year completed,
     * turning the word "Year" into 'y', and replacing spaces with underscores.
     */
    public String schoolYearInDatabaseFormat(String schoolYear){
        switch(schoolYear){
            case "Did not go to school":
                schoolYear = "'did_not_go_to_school'";
                break;
            case "Year 8 or below":
                schoolYear = "'y8_below'";
                break;
            case "Year 9 equivalent":
                schoolYear = "'y9_equivalent'";
                break;
            case "Year 10 equivalent":
                schoolYear = "'y10_equivalent'";
                break;
            case "Year 11 equivalent":
                schoolYear = "'y11_equivalent'";
                break;
            case "Year 12 equivalent":
                schoolYear = "'y12_equivalent'";
                break;
            default:
                schoolYear = "what is school?";
                break;
        }
        return schoolYear;
    }

    public String reverseSchoolYear(String schoolYear){
        switch(schoolYear){
            case "did_not_go_to_school":
                schoolYear = "Did not go to school";
                break;
            case "y8_below":
                schoolYear = "Year 8 or below";
                break;
            case "y9_equivalent":
                schoolYear = "Year 9 equivalent";
                break;
            case "y10_equivalent":
                schoolYear = "Year 10 equivalent";
                break;
            case "y11_equivalent":
                schoolYear = "Year 11 equivalent";
                break;
            case "y12_equivalent":
                schoolYear = "Year 12 equivalent";
                break;
            default:
                schoolYear = "what is school?";
                break;
        }
        return schoolYear;
    }

    /*Mario: This method converts the weekly household income.
     * There is a certain case which will produce a different result
     * depending on the year, that being the range '1500 - 1999', as
     * 2016 has it as just '1500 - 1999', while 2021 has it as '1500 - 1749' and '1750 - 1999'
     */
    public String incomeInDatabaseFormat(String income, int year){
        switch(income){
            case "$1 - $149 per week":
                income = "'1_149'";
                break;
            case "$150 - $299 per week":
                income = "'150_299'";
                break;
            case "$300 - $399 per week":
                income = "'300_399'";
                break;
            case "$400 - $499 per week":
                income = "'400_499'";
                break;
            case "$500 - $649 per week":
                income = "'500_649'";
                break;
            case "$650 - $799 per week":
                income = "'650_799'";
                break;
            case "$800 - $999 per week":
                income = "'800_999'";
                break;
            case "$1000 - $1249 per week":
                income = "'1000_1249'";
                break;
            case "$1250 - $1499 per week":
                income = "'1250_1499'";
                break;
            case "$1500 - $1999 per week":
            /*Mario: Because the query's condition will use 'LIKE' rather than '=',
             * for the ranges of '1500 - 1749' and '1750 - 1999' we can use '%17%'
             * as the number 17 doesn't show up in any other income ranges in 2021.
             * However, because the number 17 doesn't show up in 2016 at all, we must
             * make a conditon for it so our query works.
            */
                if(year == 2021) {income = "'%17%'";}
                else {income = "'1500_1999'";}
                break;
            case "$2000 - $2499 per week":
                income = "'2000_2499'";
                break;
            case "$2500 - $2999 per week":
                income = "'2500_2999'";
                break;
            case "$3000 or more":
            /*Mario: In 2016, the range here goes just from 3000 or more.
             * However, in 2021, it is expanded to 3000-3499, and then 3500 or more.
             * Because of this, we will use a string in our query that checks for 4-digit numbers
             * that start with 3.
            */
                income = "'3___\\_%' ESCAPE '\\'";
                break;
            default:
            //Mario: quirky default statement
                income = "beyond the economy";
                break;
        }
        return income;
    }

    public String reverseIncome(String income){
        switch(income){
            case "1_149":
                income = "$1 - $149 per week";
                break;
            case "150_299":
                income = "$150 - $299 per week";
                break;
            case "300_399":
                income = "$300 - $399 per week";
                break;
            case "400_499":
                income = "$400 - $499 per week";
                break;
            case "500_649":
                income = "$500 - $649 per week";
                break;
            case "650_799":
                income = "$650 - $799 per week";
                break;
            case "800_999":
                income = "$800 - $999 per week";
                break;
            case "1000_1249":
                income = "$1000 - $1249 per week";
                break;
            case "1250_1499":
                income = "$1250 - $1499 per week";
                break;
            case "1500_1999":
                income = "$1500 - $1999 per week";
                break;
            case "2000_2499":
                income = "$2000 - $2499 per week";
                break;
            case "2500_2999":
                income = "$2500 - $2999 per week";
                break;
            case "3000_more":
                income = "$3000 or more";
                break;
            default:
            //Mario: quirky default statement
                income = "beyond the economy";
                break;
        }
        return income;
    }

    public String displayAgeDemographicDifference(String category, String htmlClass, int difference){
        String html = "";

        if(difference < 0){
            html = html + "<p class = 'age-info " + htmlClass + "'>There are " + Math.abs(difference) 
                        + " less " + category + " between 2016 and 2021.";
        } else if(difference > 0){
            html = html + "<p class = 'age-info " + htmlClass + "'>There are " + Math.abs(difference) 
                        + " more " + category + " between 2016 and 2021.";
        } else if(difference == 0){
            html = html + "<p class = 'age-info " + htmlClass + "'>There is no difference in " 
                        + category + " between 2016 and 2021.";
        }

        return html;
    }

    public String getSchoolCategory(String school_year, String school_gender, String school_status){
        String category = "";

        if(school_year.equals("Did not go to school")){
            category = school_status + " " + school_gender + " who have not gone to school";
        } else {
            category = school_status + " " + school_gender + " who have completed " + school_year;
        }

        return category;
    }

    public String displaySchoolYearDifference(String category, String htmlClass, int difference){
        String html = "";

        if(difference < 0){
            html = html + "<p class = 'school-info " + htmlClass + "'>There are " + Math.abs(difference) + 
                            " less " + category + " between 2016 and 2021.";
        } else if(difference > 0){
            html = html + "<p class = 'school-info " + htmlClass + "'>There are " + Math.abs(difference) + 
                            " more " + category + " between 2016 and 2021.";
        } else if(difference == 0){
            html = html + "<p class = 'school-info " + htmlClass + "'>There is no difference in " + category + 
                            " between 2016 and 2021.";
        }

        return html;
    }

    public String displayHouseholdDifference(String category, String htmlClass, int difference){
        String html = "";

        if(difference < 0){
            html = html + "<p class = 'household-info " + htmlClass + "'>There are " + Math.abs(difference) 
                            + " less " + category + " between 2016 and 2021.</p>";
        } else if(difference > 0){
            html = html + "<p class = 'household-info " + htmlClass + "'>There are " + Math.abs(difference) 
                            + " more " + category + " between 2016 and 2021.</p>";
        } else if(difference == 0){
            html = html + "<p class = 'household-info " + htmlClass + "'>There is no difference in "
                    + category + " between 2016 and 2021.";
        }

        return html;
    }

    public ArrayList<HouseholdIncome> cleanHouseholdIncome(ArrayList<HouseholdIncome> hh){
        ArrayList<Integer> index1500_1749 = new ArrayList<Integer>();
        ArrayList<Integer> index3000_3499 = new ArrayList<Integer>();
        
        ArrayList<Integer> range1500_1749 = new ArrayList<Integer>();
        ArrayList<Integer> range1750_1999 = new ArrayList<Integer>();
        ArrayList<Integer> range3000_3499 = new ArrayList<Integer>();
        ArrayList<Integer> range3500_more = new ArrayList<Integer>();

        for(int i = 0; i < hh.size(); i++){
            if(hh.get(i).getIncome().equals("1500_1749")){
                range1500_1749.add(hh.get(i).getNumber());
                index1500_1749.add(i);
            } 
            else if(hh.get(i).getIncome().equals("1750_1999")){
                range1750_1999.add(hh.get(i).getNumber());
                hh.remove(i);
                --i;
            } 
            else if(hh.get(i).getIncome().equals("3000_3499")){
                range3000_3499.add(hh.get(i).getNumber());
                index3000_3499.add(i);
            } 
            else if(hh.get(i).getIncome().equals("3500_more")){
                range3500_more.add(hh.get(i).getNumber());
                hh.remove(i);
                --i;
            }
        }

        for(int i = 0; i < range1500_1749.size(); i++){
            int sum1 = range1500_1749.get(i) + range1750_1999.get(i);
            int sum2 = range3000_3499.get(i) + range3500_more.get(i);

            hh.get(index1500_1749.get(i)).setNumber(sum1);
            hh.get(index1500_1749.get(i)).setIncome("1500_1999");

            hh.get(index3000_3499.get(i)).setNumber(sum2);
            hh.get(index3000_3499.get(i)).setIncome("3000_more");
        }

        return hh;
    }

    public String ageTotalInTextFormat(String total, String status, String range, String gender){
        switch(total){
            case "total":
                total = "";
                break;
            case "status":
                total = status.toLowerCase();
                break;
            case "age":
                total = range.toLowerCase();
                break;
            case "gender":
                total = gender.toLowerCase();
                break;
            case "age-and-status":
                total = range.toLowerCase() + " " + status.toLowerCase();
                break;
            case "age-and-gender":
                total = range.toLowerCase() + " " + gender.toLowerCase();
                break;
            case "gender-and-status":
                total = status.toLowerCase() + " " + gender.toLowerCase();
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String ageTotalInDatabaseFormat(String total, String status, String range, String gender){
        switch(total){
            case "total":
                total = "";
                break;
            case "status":
                total = " AND a1.indigenous_status = " + status + " ";
                break;
            case "age":
                total = " AND a1.age_range = " + range + " ";
                break;
            case "gender":
                total = " AND a1.gender = " + gender + " ";
                break;
            case "age-and-status":
                total = " AND a1.age_range = " + range + " ";
                total = total + " AND a1.indigenous_status = " + status + " ";
                break;
            case "age-and-gender":
                total = " AND a1.age_range = " + range + " ";
                total = total + " AND a1.gender = " + gender + " ";
                break;
            case "gender-and-status":
                total = " AND a1.gender = " + gender + " ";
                total = total + " AND a1.indigenous_status = " + status + " ";
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String householdTotalInDatabaseFormat(String total, String status, String range){
        switch(total){
            case "total":
                total = "";
                break;
            case "status":
                total = " AND a1.indigenous_status = " + status + " ";
                break;
            case "income":
                total = " AND a1.weekly_income_range LIKE " + range + " ";
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String householdTotalInTextFormat(String total, String status, String range){
        switch(total){
            case "total":
                total = "";
                break;
            case "status":
                total = status.toLowerCase() + " households ";
                break;
            case "income":
                total = " households making " + range;
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String schoolTotalInDatabaseFormat(String total, String status, String schoolYear, String gender){
        switch(total){
            case "total":
                total = "";
                break;
            case "status":
                total = " AND a1.indigenous_status = " + status + " ";
                break;
            case "year":
                total = " AND a1.highest_year_of_school_completed = " + schoolYear + " ";
                break;
            case "gender":
                total = " AND a1.gender = " + gender + " ";
                break;
            case "year-and-status":
                total = " AND a1.highest_year_of_school_completed = " + schoolYear + " ";
                total = total + " AND a1.indigenous_status = " + status + " ";
                break;
            case "year-and-gender":
                total = " AND a1.highest_year_of_school_completed = " + schoolYear + " ";
                total = total + " AND a1.gender = " + gender + " ";
                break;
            case "gender-and-status":
                total = " AND a1.gender = " + gender + " ";
                total = total + " AND a1.indigenous_status = " + status + " ";
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String schoolTotalInTextFormat(String total, String status, String schoolYear, String gender){
        switch(total){
            case "total":
                total = " population ";
                break;
            case "status":
                total = status.toLowerCase() + " population ";
                break;
            case "year":
                if(schoolYear.equals("Did not go to school")){
                    total = " population which has not gone to school";
                } else {
                    total = " population which has completed " + schoolYear;
                }
                break;
            case "gender":
                total = gender.toLowerCase() + " population ";
                break;
            case "year-and-status":
                if(schoolYear.equals("Did not go to school")){
                    total = status.toLowerCase() + " population which has not gone to school";
                } else {
                    total = status.toLowerCase() + " population which has completed " + schoolYear;
                }
                break;
            case "year-and-gender":
                if(schoolYear.equals("Did not go to school")){
                    total = gender.toLowerCase() + " population which has not gone to school";
                } else {
                    total = gender.toLowerCase() + " population which has completed " + schoolYear;
                }
                break;
            case "gender-and-status":
                total = status.toLowerCase() + " " + gender.toLowerCase() + " population ";
                break;
            default:
                System.out.println("oops");
                break;
        }
        return total;
    }

    public String createAgeTable(ArrayList<RankedLGA> rankedLGAs, ArrayList<LGA> currentLGA, 
                                int year, String gender, String status, String range, String proportion){
        String html = "";

        int index = 69;
        if(year == 2016){index = 0;}
        else if(year == 2021){index = 1;}
        
        html = html + "<table>";
            html = html + "<caption>" + year + " ranking</caption>";

            html = html + "<tr>";
            html = html +   "<th>Rank</th>";
            html = html +   "<th>Name</th>";
            html = html +   "<th>" + range + " " + status.toLowerCase() + " " + gender.toLowerCase() + " population</th>";
            html = html +   "<th>Total " + proportion + " population</th>";
            html = html +   "<th>Percentage</th>";
            html = html + "</tr>";

            for(RankedLGA lga : rankedLGAs){
                if(lga.getName().equals(currentLGA.get(index).getName())){
                    html = html + "<tr id = 'current-lga-ranked'>";
                        html = html + "<td>" + lga.getRank() + "</td>";
                        html = html + "<td>" + lga.getName() + "</td>";
                        html = html + "<td>" + lga.getCategory() + "</td>";
                        html = html + "<td>" + lga.getTotal() + "</td>";
                        html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
                    html = html + "</tr>";
                }
            }

            for(RankedLGA lga : rankedLGAs){
                html = html + "<tr>";
                    html = html + "<td>" + lga.getRank() + "</td>";
                    html = html + "<td>" + lga.getName() + "</td>";
                    html = html + "<td>" + lga.getCategory() + "</td>";
                    html = html + "<td>" + lga.getTotal() + "</td>";
                    html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
                html = html + "</tr>";
            }
            html = html + "</table>";

        return html;
    }

    public String createSchoolTable(ArrayList<RankedLGA> rankedLGAs, ArrayList<LGA> currentLGA, 
                                    int year, String gender, String status, String schoolYear, String proportion){

        int index = 69;
        if(year == 2016){index = 0;} else if(year == 2021){index = 1;}
        String html = "";
        html = html + "<table>";

            html = html + "<caption>" + year + " ranking</caption>";

            html = html + "<tr>";
            html = html +   "<th>Rank</th>";
            html = html +   "<th>Name</th>";
            if(schoolYear.equals("Did not go to school")){
                html = html +   "<th>" + status + " " + gender.toLowerCase() + " population which has not gone to school</th>";
            } else {
                html = html +   "<th>" + status + " " + gender.toLowerCase() + " population which has completed " + schoolYear + "</th>";
            }
            
            html = html +   "<th>Total " + proportion + "</th>";
            html = html +   "<th>Percentage</th>";
            html = html + "</tr>";

            for(RankedLGA lga : rankedLGAs){
                if(lga.getName().equals(currentLGA.get(index).getName())){
                    html = html + "<tr id = 'current-lga-ranked'>";
                        html = html + "<td>" + lga.getRank() + "</td>";
                        html = html + "<td>" + lga.getName() + "</td>";
                        html = html + "<td>" + lga.getCategory() + "</td>";
                        html = html + "<td>" + lga.getTotal() + "</td>";
                        html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
                    html = html + "</tr>";
                }
            }

            for(RankedLGA lga : rankedLGAs){
                html = html + "<tr>";
                    html = html + "<td>" + lga.getRank() + "</td>";
                    html = html + "<td>" + lga.getName() + "</td>";
                    html = html + "<td>" + lga.getCategory() + "</td>";
                    html = html + "<td>" + lga.getTotal() + "</td>";
                    html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
                html = html + "</tr>";
            }

            html = html + "</table>";
            
        return html;
    }

    public String createHouseholdTable(ArrayList<RankedLGA> rankedLGAs, ArrayList<LGA> currentLGA, 
                                        int year, String status, String income, String proportion){
        String html = "";

        int index = 69;
        if(year == 2016){index = 0;} else if(year == 2021){index = 1;}

        html = html + "<table>";
        html = html + "<caption>" + year + " ranking</caption>";

        html = html + "<tr>";
        html = html +   "<th>Rank</th>";
        html = html +   "<th>Name</th>";
        html = html +   "<th>" + status + " households making " + income + "</th>";
        html = html +   "<th>Total " + proportion + "</th>";
        html = html +   "<th>Percentage</th>";
        html = html + "</tr>";

        for(RankedLGA lga : rankedLGAs){
            if(lga.getName().equals(currentLGA.get(index).getName())){
                html = html + "<tr id = 'current-lga-ranked'>";
                    html = html + "<td>" + lga.getRank() + "</td>";
                    html = html + "<td>" + lga.getName() + "</td>";
                    html = html + "<td>" + lga.getCategory() + "</td>";
                    html = html + "<td>" + lga.getTotal() + "</td>";
                    html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
                html = html + "</tr>";
            }
        }

        for(RankedLGA lga : rankedLGAs){
            html = html + "<tr>";
                html = html + "<td>" + lga.getRank() + "</td>";
                html = html + "<td>" + lga.getName() + "</td>";
                html = html + "<td>" + lga.getCategory() + "</td>";
                html = html + "<td>" + lga.getTotal() + "</td>";
                html = html + "<td>" + String.format("%.2f%%",lga.getProportion()) + "</td>";
            html = html + "</tr>";
        }
        html = html + "</table>";

        return html;
    }
}