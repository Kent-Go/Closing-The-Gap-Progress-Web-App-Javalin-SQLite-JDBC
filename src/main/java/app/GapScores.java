package app;

import java.util.ArrayList;

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
public class GapScores implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/gap-scores.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Gap Scores</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='gapscores.css' />";
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
                <a href='focus-lga-state.html'>FOCUS BY LGA/STATE</a>
                <a href='gap-scores.html' class = 'current_page'>GAP SCORES</a>
                <a href='find-similar-lga.html'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>Gap Scores</h1>
                <p>Calculate the Gap Score between indigenous and non-indigenous people for each LGA. Gap score is defined as the difference in percentage 
                between indigenous and non-indigenous people of the total population in each category. <b>A negative Gap Score indicates that there are more indigenous people than non-indigenous.</b></p>
                <p>You may filter the LGAs by area size and total population, and sort the resulting table alphabetically, or by the Gap Score in ascending/descending order.
                In addition, double-clicking on a row will display a text description for the data in said row.</p>
                <p>You may also filter the data depending on the category.</p>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();

        String age_range = context.formParam("age-range");
        String age_gender = context.formParam("age-gender");
        boolean ageScores = false;
        if(age_range != null && age_gender != null){
            ageScores = true;
        }

        String school_year = context.formParam("school-year");
        String school_gender = context.formParam("school-gender");
        boolean schoolScores = false;
        if(school_year != null && school_gender != null){
            schoolScores = true;
        }

        String household_income = context.formParam("household-income");
        boolean householdScores = false;
        if(household_income != null){
            householdScores = true;
        }

        String health_condition = context.formParam("health-condition");
        String health_gender = context.formParam("health-gender");
        boolean healthScores = false;
        if(health_condition != null && health_gender != null){
            healthScores = true;
        }


        int min_population = 0;
        if(context.formParam("min-population") != null){
            min_population = Integer.parseInt(context.formParam("min-population"));
        }

        int max_population = 999999999;
        if(context.formParam("max-population") != null){
            max_population = Integer.parseInt(context.formParam("max-population"));
        }

        boolean populationError = false;
        if(max_population < min_population){
            populationError = true;
        }

        String filter_year_string = context.formParam("filter-year");

        int filter_year = 2021;

        if(filter_year_string != null){
            filter_year = Integer.parseInt(context.formParam("filter-year"));
        }

        int min_area = 0;
        if(context.formParam("min-area") != null){
            min_area = Integer.parseInt(context.formParam("min-area"));
        }

        int max_area = 999999999;
        if(context.formParam("max-area") != null){
            max_area = Integer.parseInt(context.formParam("max-area"));
        }

        boolean areaError = false;
        if(max_area < min_area){
            areaError = true;
        }

        ArrayList<GapScoreLGA> gapScores2016 = new ArrayList<GapScoreLGA>();
        ArrayList<GapScoreLGA> gapScores2021 = new ArrayList<GapScoreLGA>();
        ArrayList<GapScoreDifference> gapScoresDifference = new ArrayList<GapScoreDifference>();

        String sorter = context.formParam("table-sort");

        html = html + """
                <div id = 'outcome_list'>
                    <label for='outcome_drop'>Select outcome and dataset:</label>
                    <select id='outcome_drop' name = 'outcome_drop' onchange='showFilters()'>
                            <option value='' selected disabled hidden>Choose here</option>
                        <optgroup label= 'Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives'>
                            <option value = 'age'>Age Demographics</option>
                            <option value = 'health'>Long-term Health Conditions</option>
                        </optgroup>
                        <optgroup label= 'Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential'>
                            <option value='school'>Level of Education</option>
                        </optgroup>
                        <optgroup label= 'Outcome 8: Strong economic participation and development of Aboriginal and Torres Strait Islander people and communities'>
                            <option value='household'>Household Weekly Income</option>
                        </optgroup>
                    </select>
                </div>

                <div id = 'options'></div>
                """;

        if(populationError || areaError){
            html = html + "<div class = 'warning-error'>";
            if(populationError){
                html = html + "<h2 class = 'warning'>Input higher minimum population than maximum! Please try again.</h2>";
                html = html + "<p class = 'warning-help'>Minimum population: " + min_population + "</p>";
                html = html + "<p class = 'warning=help'>Maximum population: " + max_population + "</p>";
            }
            if(areaError){
                html = html + "<h2 class = 'warning'>Input higher minimum area than maximum! Please try again.</h2>";
                html = html + "<p class = 'warning-help'>Minimum area: " + min_area + " km<sup>2</sup></p>";
                html = html + "<p class = 'warning-help'>Maximum area: " + max_area + " km<sup>2</sup></p>";
            }
            html = html + "</div>";
        } else {
            if(ageScores){
                String age_range_db = rangeInDatabaseFormat(age_range);
                String age_gender_db = genderInDatabaseFormat(age_gender);
                String sorter_db = sorterInDatabaseFormat(sorter);
                String sorter_difference_db = differenceSorterInDatabaseFormat(sorter);

                jdbc.createGapScoreAgeViews(filter_year, min_area, max_area, age_range_db, age_gender_db, min_population, max_population);

                gapScores2016 = jdbc.getAgeGapScores(sorter_db, 2016);
                gapScores2021 = jdbc.getAgeGapScores(sorter_db, 2021);
                gapScoresDifference = jdbc.getAgeGapScoreDifference(sorter_difference_db);

                html = html + "<h2 class = 'filters-chosen'>Showing Gap Scores for: " + age_gender + " population aged " + age_range +
                              " in LGAs with a total of " + min_population + " to " + max_population + " " + age_range + " " + age_gender.toLowerCase() + " people," +
                              " and an area between " + min_area + " to " + max_area + " km<sup>2</sup> in size.</h2>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2016</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createAgeTable(age_gender, age_range, gapScores2016, 2016);
                html = html + "</div>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2021</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createAgeTable(age_gender, age_range, gapScores2021, 2021);
                html = html + "</div>"; 
                
                html = html + "<h2 class = 'table-name'>Gap Score Difference</h2>";
                html = html + "<p class = 'table-name'>This table shows the gap score difference between 2016 and 2021</p>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createScoreDifferenceTable(gapScoresDifference);
                html = html + "</div>";  
            } 
            else if(schoolScores){
                String school_year_db = schoolYearInDatabaseFormat(school_year);
                String school_year_text;
                if(school_year.equals("Did not go to school")){
                    school_year_text = "not gone to school";
                } else {
                    school_year_text = "completed " + school_year;
                }
                String school_gender_db = genderInDatabaseFormat(school_gender);
                String sorter_db = sorterInDatabaseFormat(sorter);
                String sorter_difference_db = differenceSorterInDatabaseFormat(sorter);

                jdbc.createGapScoreSchoolViews(filter_year, min_area, max_area, school_year_db, school_gender_db, min_population, max_population);

                gapScores2016 = jdbc.getSchoolGapScores(sorter_db, 2016);
                gapScores2021 = jdbc.getSchoolGapScores(sorter_db, 2021);
                gapScoresDifference = jdbc.getSchoolGapScoreDifference(sorter_difference_db);

                html = html + "<h2 class = 'filters-chosen'>Showing Gap Scores for: " + school_gender + " population which has " + school_year_text +
                                " in LGAs with a total of " + min_population + " to " + max_population + " " + school_gender.toLowerCase() + " people who have " + school_year_text +
                                "(in " + filter_year + ") and an area between " + min_area + " to " + max_area + " km<sup>2</sup> in size.</h2>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2016</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createSchoolTable(school_gender, school_year, gapScores2016, 2016);
                html = html + "</div>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2021</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createSchoolTable(school_gender, school_year, gapScores2021, 2021);
                html = html + "</div>"; 
                
                html = html + "<h2 class = 'table-name'>Gap Score Difference</h2>";
                html = html + "<p class = 'table-name'>This table shows the gap score difference between 2016 and 2021</p>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createScoreDifferenceTable(gapScoresDifference);
                html = html + "</div>";  
            }
            else if(householdScores){
                String household_income_db_main;
                String household_income_db_other;

                if(filter_year == 2016){
                    household_income_db_main = incomeInDatabaseFormat(household_income, 2016);
                    household_income_db_other = incomeInDatabaseFormat(household_income, 2021);
                } else {
                    household_income_db_main = incomeInDatabaseFormat(household_income, 2021);
                    household_income_db_other = incomeInDatabaseFormat(household_income, 2016);
                }

                String sorter_db = sorterInDatabaseFormat(sorter);
                String sorter_difference_db = differenceSorterInDatabaseFormat(sorter);

                jdbc.createGapScoreHouseholdViews(filter_year, min_area, max_area, household_income_db_main, household_income_db_other, min_population, max_population);

                gapScores2016 = jdbc.getHouseholdGapScores(sorter_db, 2016);
                gapScores2021 = jdbc.getHouseholdGapScores(sorter_db, 2021);
                gapScoresDifference = jdbc.getHouseholdGapScoreDifference(sorter_difference_db);

                html = html + "<h2 class = 'filters-chosen'>Showing Gap Scores for: Households making " + household_income +
                                " in LGAs with a total of " + min_population + " to " + max_population + " Households making " + household_income +
                                "(in " + filter_year + ") and an area between " + min_area + " to " + max_area + " km<sup>2</sup> in size.</h2>";
                
                html = html + "<h3 class = 'table-name'>Gap Scores for 2016</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createHouseholdTable(household_income, gapScores2016, 2016);
                html = html + "</div>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2021</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createHouseholdTable(household_income, gapScores2021, 2021);
                html = html + "</div>"; 
                
                html = html + "<h2 class = 'table-name'>Gap Score Difference</h2>";
                html = html + "<p class = 'table-name'>This table shows the gap score difference between 2016 and 2021</p>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createScoreDifferenceTable(gapScoresDifference);
                html = html + "</div>";  
            }
            else if(healthScores){
                String health_condition_db = conditionInDatabaseFormat(health_condition);
                String health_gender_db = genderInDatabaseFormat(health_gender);
                String sorter_db = sorterInDatabaseFormat(sorter);

                jdbc.createGapScoreHealthView(min_area, max_area, health_condition_db, health_gender_db, min_population, max_population);

                gapScores2021 = jdbc.getHealthGapScores(sorter_db);

                html = html + "<h2 class = 'filters-chosen'>As data for health conditions is unavailable for 2016, only 2021 data will be displayed.</h2>";

                html = html + "<h2 class = 'filters-chosen'>Showing Gap Scores for: " + health_gender + " population suffering from " + health_condition.toLowerCase() +
                                " in LGAs with a total of " + min_population + " to " + max_population + " " + health_gender.toLowerCase() + " people suffering from " + health_condition.toLowerCase() +
                                "(in 2021) and an area between " + min_area + " to " + max_area + " km<sup>2</sup> in size.</h2>";

                html = html + "<h3 class = 'table-name'>Gap Scores for 2021</h3>";
                html = html + "<div class = 'gap-tables'>";
                html = html + createHealthTable(health_gender, health_condition, gapScores2021);
                html = html + "</div>"; 
            }
        }

        






        html = html + """
                <script>
                function showFilters(){
                    var dataset = document.getElementById('outcome_drop').value;

                    if(dataset == 'age'){
                        document.getElementById('options').innerHTML = `

                        <h2>Filter age demographics</h2>

                        <form action = '/gap-scores.html' method = 'post'>
                        <div class = 'filter'>
                            <label for = 'age-range'>Choose age range:</label>
                            <select id = 'age-range' name = 'age-range' onchange = 'updateAgeLabel()'>
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
                            <label for = 'age-gender'>Choose gender:</label>
                            <select id = 'age-gender' name = 'age-gender' onchange = 'updateAgeLabel()'>
                                <option value='' selected disabled hidden>Choose here</option>
                                <option>Male</option>
                                <option>Female</option>
                            </select>
                        </div>  
                        </div>
                    <h2>Filter LGAs:</h2>

                    <div class = 'filter'>
                        <label for 'min-population' id = 'min-population-label'>Enter MINIMUM population:</label>
                        <input type = 'number' id = 'min-population' name = 'min-population'>
                    </div>
                    <div class = 'filter'>
                        <label for 'max-population' id = 'max-population-label'>Enter MAXIMUM population:</label>
                        <input type = 'number' id = 'max-population' name = 'max-population'>
                    </div>
                    <div class = 'filter'>
                        <p id = 'year-information'>As the total population is different between 2016 and 2021, please select which year you're basing your query on.</p>
                        <label for = 'filter-year'>Choose year for population:</label>
                        <select id = 'filter-year' name = 'filter-year'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 2016 id = '2016-population'>Population in 2016</option>
                            <option value = 2021 id = '2021-population'>Population in 2021</option>
                        </select>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                        <label for = 'min-area'>Enter MINIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'min-area' name = 'min-area'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'max-area'>Enter MAXIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'max-area' name = 'max-area'>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                    <label for = 'table-sort'>Sort table by:</label>
                        <select id = 'table-sort' name = 'table-sort'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 'alphabet'>LGAs in alphabetical order</option>
                            <option value = 'gs_desc'>Gap score - ascending</option>
                            <option value = 'gs_asc'>Gap score - descending</option>
                        </select>
                    </div>

                    <button type = 'submit' class = 'btn btn-primary'>Get gap score</button>

                    </form>
                    `} 
                    else if (dataset == 'school'){
                        document.getElementById('options').innerHTML = `

                        <h2>Filter level of education</h2>

                        <form action = '/gap-scores.html' method = 'post'>
                            <div class = 'filter'>
                                <label for = 'school-year'>Choose highest year of school completed:</label>
                                <select id = 'school-year' name = 'school-year' onchange = 'updateSchoolLabel()'>
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
                                <label for = 'school-gender'>Choose gender:</label>
                                <select id = 'school-gender' name = 'school-gender' onchange = 'updateSchoolLabel()'>
                                    <option value='' selected disabled hidden>Choose here</option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select>
                            </div>
                            </div>
                    <h2>Filter LGAs:</h2>

                    <div class = 'filter'>
                        <label for 'min-population' id = 'min-population-label'>Enter MINIMUM population:</label>
                        <input type = 'number' id = 'min-population' name = 'min-population'>
                    </div>
                    <div class = 'filter'>
                        <label for 'max-population' id = 'max-population-label'>Enter MAXIMUM population:</label>
                        <input type = 'number' id = 'max-population' name = 'max-population'>
                    </div>
                    <div class = 'filter'>
                        <p id = 'year-information'>As the total population is different between 2016 and 2021, please select which year you're basing your query on.</p>
                        <label for = 'filter-year'>Choose year for population:</label>
                        <select id = 'filter-year' name = 'filter-year'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 2016 id = '2016-population'>Population in 2016</option>
                            <option value = 2021 id = '2021-population'>Population in 2021</option>
                        </select>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                        <label for = 'min-area'>Enter MINIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'min-area' name = 'min-area'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'max-area'>Enter MAXIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'max-area' name = 'max-area'>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                    <label for = 'table-sort'>Sort table by:</label>
                        <select id = 'table-sort' name = 'table-sort'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 'alphabet'>LGAs in alphabetical order</option>
                            <option value = 'gs_desc'>Gap score - ascending</option>
                            <option value = 'gs_asc'>Gap score - descending</option>
                        </select>
                    </div>

                    <button type = 'submit' class = 'btn btn-primary'>Get gap score</button>

                    </form>
                    `} 
                    else if (dataset == 'household'){
                        document.getElementById('options').innerHTML = `

                        <h2>Filter households</h2>

                        <form action = '/gap-scores.html' method = 'post'>
                            <div class = 'filter'>
                                <label for = 'household-income'>Choose weekly household income:</label>
                                <select id = 'household-income' name = 'household-income' onchange = 'updateHouseholdLabel()'>
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
                    <h2>Filter LGAs:</h2>

                    <div class = 'filter'>
                        <label for = 'min-population' id = 'min-population-label'>Enter MINIMUM number of households:</label>
                        <input type = 'number' id = 'min-population' name = 'min-population'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'max-population' id = 'max-population-label'>Enter MAXIMUM number of households:</label>
                        <input type = 'number' id = 'max-population' name = 'max-population'>
                    </div>
                    <div class = 'filter'>
                    <p id = 'year-information'>As the number of households is different between 2016 and 2021, please select which year you're basing your query on.</p>
                        <label for = 'filter-year'>Choose year for number of households:</label>
                        <select id = 'filter-year' name = 'filter-year'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 2016 id = '2016-population'>Number of households in 2016</option>
                            <option value = 2021 id = '2021-population'>Number of households in 2021</option>
                        </select>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                        <label for = 'min-area'>Enter MINIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'min-area' name = 'min-area'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'min-area'>Enter MINIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'max-area' name = 'max-area'>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                    <label for = 'table-sort'>Sort table by:</label>
                        <select id = 'table-sort' name = 'table-sort'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 'alphabet'>LGAs in alphabetical order</option>
                            <option value = 'gs_desc'>Gap score - ascending</option>
                            <option value = 'gs_asc'>Gap score - descending</option>
                        </select>
                    </div>

                    <button type = 'submit' class = 'btn btn-primary'>Get gap score</button>

                    </form> 
                    `}
                    else if (dataset == 'health'){
                        document.getElementById('options').innerHTML = `

                        <h2>Filter health conditions</h2>

                        <form action = '/gap-scores.html' method = 'post'>
                        <div class = 'filter'>
                            <label for = 'health-condition'>Choose health condition:</label>
                            <select id = 'health-condition' name = 'health-condition' onchange = 'updateHealthLabel()'>
                                <option value='' selected disabled hidden>Choose here</option>
                                <option>Arthritis</option>    
                                <option>Asthma</option>
                                <option>Cancer</option>
                                <option>Dementia</option>
                                <option>Diabetes</option>
                                <option>Heart disease</option>
                                <option>Kidney disease</option>
                                <option>Lung conditions</option>
                                <option>Mental health conditions</option>
                                <option>Stroke</option>
                                <option>Other conditions</option>
                            </select>
                        </div>
                        <div class = 'filter'>
                            <label for = 'health-gender'>Choose gender:</label>
                            <select id = 'health-gender' name = 'health-gender' onchange = 'updateHealthLabel()'>
                                <option value='' selected disabled hidden>Choose here</option>
                                <option>Male</option>
                                <option>Female</option>
                            </select>
                    </div>
                    <h2>Filter LGAs:</h2>

                    <div class = 'filter'>
                        <label for = 'min-population' id = 'min-population-label'>Enter MINIMUM population:</label>
                        <input type = 'number' id = 'min-population' name = 'min-population'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'max-population' id = 'max-population-label'>Enter MAXIMUM population:</label>
                        <input type = 'number' id = 'max-population' name = 'max-population'>
                    </div>
                    <p class = 'warning'>This dataset only has data for 2021!</p>
                    <div class = 'filter'>
                        <label for = 'min-area'>Enter MINIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'min-area' name = 'min-area'>
                    </div>
                    <div class = 'filter'>
                        <label for = 'max-area'>Enter MAXIMUM area of LGA(in km<sup>2</sup>):</label>
                        <input type = 'number' id = 'max-area' name = 'max-area'>
                    </div>
                    <p></p>
                    <div class = 'filter'>
                    <label for = 'table-sort'>Sort table by:</label>
                        <select id = 'table-sort' name = 'table-sort'>
                            <option value='' selected disabled hidden>Choose here</option>
                            <option value = 'alphabet'>LGAs in alphabetical order</option>
                            <option value = 'gs_desc'>Gap score - ascending</option>
                            <option value = 'gs_asc'>Gap score - descending</option>
                        </select>
                    </div>
                    <button type = 'submit' class = 'btn btn-primary'>Get gap score</button>

                    </form>
                `}
            }
            </script>
        """;

        html = html + """
                <script>
                function updateAgeLabel(){
                    var optionRange = document.getElementById('age-range').value;
                    var optionGender = document.getElementById('age-gender').value;

                    document.getElementById('min-population-label').innerHTML = 'Enter MINIMUM ' + optionGender + ' population aged ' + optionRange + ':';
                    document.getElementById('max-population-label').innerHTML = 'Enter MAXIMUM ' + optionGender + ' population aged ' + optionRange + ':';

                    document.getElementById('2016-population').innerHTML = optionGender + ' population aged ' + optionRange + ' in 2016:';
                    document.getElementById('2021-population').innerHTML = optionGender + ' population aged ' + optionRange + ' in 2021:';
                }

                function updateSchoolLabel(){
                    var optionYear = document.getElementById('school-year').value;
                    var optionGender = document.getElementById('school-gender').value;

                    if(optionYear == 'Did not go to school'){
                        document.getElementById('min-population-label').innerHTML = 'Enter MINIMUM ' + optionGender + ' population which has not gone to school:';
                        document.getElementById('max-population-label').innerHTML = 'Enter MAXIMUM ' + optionGender + ' population which has not gone to school:';

                        document.getElementById('2016-population').innerHTML = optionGender + ' population which has not gone to school in 2016';
                        document.getElementById('2021-population').innerHTML = optionGender + ' population which has not gone to school in 2021';
                    } else {
                        document.getElementById('min-population-label').innerHTML = 'Enter MINIMUM ' + optionGender + ' population which has completed ' + optionYear + ':';
                        document.getElementById('max-population-label').innerHTML = 'Enter MAXIMUM ' + optionGender + ' population which has completed ' + optionYear + ':';

                        document.getElementById('2016-population').innerHTML = optionGender + ' population which has completed ' + optionYear + ' in 2016';
                        document.getElementById('2021-population').innerHTML = optionGender + ' population which has completed ' + optionYear + ' in 2021';
                    }
                }

                function updateHouseholdLabel(){
                    var optionIncome = document.getElementById('household-income').value;

                    document.getElementById('min-population-label').innerHTML = 'Enter MINIMUM number of households making ' + optionIncome + ':';
                    document.getElementById('max-population-label').innerHTML = 'Enter MAXIMUM number of households making ' + optionIncome + ':';

                    document.getElementById('2016-population').innerHTML = 'Number of households making ' + optionIncome + ' in 2016';
                    document.getElementById('2021-population').innerHTML = 'Number of households making ' + optionIncome + ' in 2021';
                }

                function updateHealthLabel(){
                    var optionCondition = document.getElementById('health-condition').value;
                    var optionGender = document.getElementById('health-gender').value;

                    document.getElementById('min-population-label').innerHTML = 'Enter MINIMUM ' + optionGender + ' population suffering from ' + optionCondition.toLowerCase() + ':';
                    document.getElementById('max-population-label').innerHTML = 'Enter MAXIMUM ' + optionGender + ' population suffering from ' + optionCondition.toLowerCase() + ':';
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

    public String conditionInDatabaseFormat(String condition){
        switch(condition){
            case "Asthma":
                condition = "'asthma'";
                break;
            case "Arthritis":
                condition = "'arthritis'";
                break;
            case "Cancer":
                condition = "'cancer'";
                break;
            case "Dementia":
                condition = "'dementia'";
                break;
            case "Diabetes":
                condition = "'diabetes'";
                break;
            case "Heart disease":
                condition = "'heart_disease'";
                break;
            case "Kidney disease":
                condition = "'kidney_disease'";
                break;
            case "Lung conditions":
                condition = "'lung_condition'";
                break;
            case "Mental health conditions":
                condition = "'mental_health'";
                break;
            case "Stroke":
                condition = "'stroke'";
                break;
            case "Other conditions":
                condition = "'other'";
                break;
            default:
                System.out.println("oops");
                break;
        }
        return condition;
    }

    public String sorterInDatabaseFormat(String sorter){

        switch(sorter){
            case "alphabet":
                sorter = "name";
                break;
            case "gs_desc":
                sorter = "gap_score DESC";
                break;
            case "gs_asc":
                sorter = "gap_score ASC";
                break;
            default:
                sorter = "gap_score";
                break;
        }

        return sorter;
    }

    public String differenceSorterInDatabaseFormat(String sorter){

        switch(sorter){
            case "alphabet":
                sorter = "name";
                break;
            case "gs_desc":
                sorter = "difference DESC";
                break;
            case "gs_asc":
                sorter = "difference ASC";
                break;
            default:
                sorter = "difference";
                break;
        }

        return sorter;
    }

    public String createAgeTable(String age_gender, String age_range, ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";


        html = html + "<table id = 'table" + year + "'>";

        html = html + "<tr>";
            html = html + "<th>Rank</th>";
            html = html + "<th>Name</th>";
            html = html + "<th>Size (km<sup>2</sup>)";
            html = html + "<th>Total " + age_gender.toLowerCase() + " population aged " + age_range + "</th>";
            html = html + "<th>Total indigenous " + age_gender.toLowerCase() + " population aged " + age_range + "</th>";
            html = html + "<th>Indigenous percent of total</th>";
            html = html + "<th>Total non-indigenous " + age_gender.toLowerCase() + " population aged " + age_range + "</th>";
            html = html + "<th>Non-indigenous percent of total</th>";
            html = html + "<th>Gap Score</th>";
        html = html + "</tr>";
        
        html = html + populateTable(gapScores, year);

        html = html + "</table>";
        html = html + "<div class = 'row-description row-description" + year + "'>";
        html = html + "<p id = 'text-description" + year + "'></p>";
        html = html + "<p id = 'formula" + year + "'></p>";
        html = html + "</div>";

        html = html + createTableScript(gapScores, year);

        return html;
    }

    public String createSchoolTable(String school_gender, String school_year, ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";

        if(school_year.equals("Did not go to school")){
            school_year = "which has not gone to school";
        } else {
            school_year = "which has completed " + school_year;
        }

        html = html + "<table id = 'table" + year + "'>";

        html = html + "<tr>";
            html = html + "<th>Rank</th>";
            html = html + "<th>Name</th>";
            html = html + "<th>Size (km<sup>2</sup>)";
            html = html + "<th>Total " + school_gender.toLowerCase() + " population " + school_year + "</th>";
            html = html + "<th>Total indigenous " + school_gender.toLowerCase() + " population " + school_year + "</th>";
            html = html + "<th>Indigenous percent of total</th>";
            html = html + "<th>Total non-indigenous " + school_gender.toLowerCase() + " population " + school_year + "</th>";
            html = html + "<th>Non-indigenous percent of total</th>";
            html = html + "<th>Gap Score</th>";
        html = html + "</tr>";
        
        html = html + populateTable(gapScores, year);
        
        html = html + "</table>";
        html = html + "<div class = 'row-description row-description" + year + "'>";
        html = html + "<p id = 'text-description" + year + "'></p>";
        html = html + "<p id = 'formula" + year + "'></p>";
        html = html + "</div>";

        html = html + createTableScript(gapScores, year);

        return html;
    }

    public String createHouseholdTable(String income, ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";

        html = html + "<table id = 'table" + year + "'>";

        html = html + "<tr>";
            html = html + "<th>Rank</th>";
            html = html + "<th>Name</th>";
            html = html + "<th>Size (km<sup>2</sup>)";
            html = html + "<th>Total households making " + income + "</th>";
            html = html + "<th>Total indigenous households making " + income + "</th>";
            html = html + "<th>Indigenous percent of total</th>";
            html = html + "<th>Total non-indigenous households making " + income + "</th>";
            html = html + "<th>Non-indigenous percent of total</th>";
            html = html + "<th>Gap Score</th>";
        html = html + "</tr>";
        
        html = html + populateTable(gapScores, year);
        
        html = html + "</table>";
        html = html + "<div class = 'row-description row-description" + year + "'>";
        html = html + "<p id = 'text-description" + year + "'></p>";
        html = html + "<p id = 'formula" + year + "'></p>";
        html = html + "</div>";

        html = html + createHouseholdTableScript(gapScores, year);

        return html;
    }

    public String createHealthTable(String health_gender, String health_condition, ArrayList<GapScoreLGA> gapScores){
        String html = "";

        int year = 2021;

        html = html + "<table id = 'table" + year + "'>";

        html = html + "<tr>";
            html = html + "<th>Rank</th>";
            html = html + "<th>Name</th>";
            html = html + "<th>Size (km<sup>2</sup>)";
            html = html + "<th>Total " + health_gender.toLowerCase() + " population suffering from " + health_condition.toLowerCase() + "</th>";
            html = html + "<th>Total indigenous " + health_gender.toLowerCase() + " population suffering from " + health_condition.toLowerCase() + "</th>";
            html = html + "<th>Indigenous percent of total</th>";
            html = html + "<th>Total non-indigenous " + health_gender.toLowerCase() + " population suffering from " + health_condition.toLowerCase() + "</th>";
            html = html + "<th>Non-indigenous percent of total</th>";
            html = html + "<th>Gap Score</th>";
        html = html + "</tr>";
        
        html = html + populateTable(gapScores, year);

        html = html + "</table>";
        html = html + "<div class = 'row-description row-description" + year + "'>";
        html = html + "<p id = 'text-description" + year + "'></p>";
        html = html + "<p id = 'formula" + year + "'></p>";
        html = html + "</div>";

        html = html + createTableScript(gapScores, year);

        return html;
    }

    public String populateTable(ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";

        for(GapScoreLGA lga : gapScores){
            String className = fixName(lga.getName());
    
    
            html = html + "<tr class = 'row" + year + "' id = '" + className + year + "' ondblclick = '" + className + year + "()'>";
                html = html + "<td id = '" + className + "-rank" + year + "'>" + lga.getRank() + "</td>";
                html = html + "<td id = '" + className + "-name" + year + "'>" + lga.getName() + "</td>";
                html = html + "<td id = '" + className + "-area" + year + "'>" + String.format("%.2f", lga.getArea()) + " km<sup>2</sup></td>";
                html = html + "<td id = '" + className + "-total" + year + "'>" + lga.getTotalPopulation() + "</td>";
                html = html + "<td id = '" + className + "-ind-pop" + year + "'>" + lga.getIndigenousPopulation() + "</td>";
                html = html + "<td id = '" + className + "-ind-per" + year + "'>" + String.format("%.2f%%", lga.getIndigenousPercent()) + "</td>";
                html = html + "<td id = '" + className + "-non-pop" + year + "'>" + lga.getNonIndigenousPopulation() + "</td>";
                html = html + "<td id = '" + className + "-non-per" + year + "'>" + String.format("%.2f%%", lga.getNonIndigenousPercent()) + "</td>";
                html = html + "<td id = '" + className + "-score" + year + "'>" + String.format("%.2f%%", lga.getGapScore()) + "</td>";
            html = html + "</tr>";
            }
        
        return html;
    }

    public String createScoreDifferenceTable(ArrayList<GapScoreDifference> gapScores){
        String html = "";

        html = html + "<table id = 'table-difference'>";

        html = html + "<tr>";
            html = html + "<th>Rank</th>";
            html = html + "<th>Name</th>";
            html = html + "<th>Gap Score in 2016</th>";
            html = html + "<th>Gap Score in 2021</th>";
            html = html + "<th>Gap Score difference</th>";
        html = html + "</tr>";

        for(GapScoreDifference lga : gapScores){
            String className = fixName(lga.getName());
    
    
            html = html + "<tr class = 'row-difference" + "' id = '" + className + "difference' ondblclick = '" + className + "difference()'>";
                html = html + "<td id = '" + className + "-rank-difference'>" + lga.getRank() + "</td>";
                html = html + "<td id = '" + className + "-name-difference'>" + lga.getName() + "</td>";
                html = html + "<td id = '" + className + "-2016-difference'>" + String.format("%.2f%%", lga.getScore2016()) + "</td>";
                html = html + "<td id = '" + className + "-2021-difference'>" + String.format("%.2f%%", lga.getScore2021()) + "</td>";
                html = html + "<td id = '" + className + "-score-difference'>" + String.format("%.2f%%", lga.getScoreDifference()) + "</td>";
            html = html + "</tr>";
        }

        html = html + "</table>";
        html = html + "<div class = 'row-description row-description-difference'>";
        html = html + "<p id = 'text-description-difference'></p>";
        html = html + "<p id = 'formula-difference'></p>";
        html = html + "</div>";

        html = html + createDifferenceTableScript(gapScores);

        return html;
    }

    public String createTableScript(ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";

        for(GapScoreLGA lga : gapScores){
            String name = fixName(lga.getName());

                html = html + "<script>";
                html = html + "\nfunction " + name + year + "(){";
                html = html + "\nvar x = '" + name + "';";
                html = html + "\nconst wholeTable = document.getElementsByClassName('row" + year + "');";
                html = html + """
                            \nfor(var i = 0; i < wholeTable.length; i++){
                                wholeTable[i].style.background = '#DDDDDD';
                            }
                        """;        

                        

                html = html + "\ndocument.getElementById(x + '" + year + "').style.background = '#00FFFF';";
                html = html + "\nvar name = document.getElementById(x + '-name" + year + "').innerHTML;";
                html = html + "\nvar total = document.getElementById(x + '-total" + year + "').innerHTML;";
                html = html + "\nvar ind_pop = document.getElementById(x + '-ind-pop" + year + "').innerHTML;";
                html = html + "\nvar ind_per = document.getElementById(x + '-ind-per" + year + "').innerHTML;";
                html = html + "\nvar non_pop = document.getElementById(x + '-non-pop" + year + "').innerHTML;";
                html = html + "\nvar non_per = document.getElementById(x + '-non-per" + year + "').innerHTML;";
                html = html + "\nvar gap_score = document.getElementById(x + '-score" + year + "').innerHTML;";

                
                html = html + "\ndocument.getElementById('text-description" + year + "').innerHTML = name + ' - The total population for the category is ' + total + ' people. ' +" +
                                                                   " 'The indigenous population is ' + ind_pop + ' people, making up ' + ind_per + ' of the total population. ' + " +
                                                                   " 'The non-indigenous population is ' + non_pop + ' people, making up ' + non_per + ' of the total population. '";
                html = html + "\ndocument.getElementById('formula" + year + "').innerHTML = 'Gap score - ' + non_per + ' - ' + ind_per + ' = ' + gap_score;}";
                    
                html = html + "</script>";
            }

        return html;
    }

    public String createHouseholdTableScript(ArrayList<GapScoreLGA> gapScores, int year){
        String html = "";

        for(GapScoreLGA lga : gapScores){
            String name = fixName(lga.getName());

                html = html + "<script>";
                html = html + "\nfunction " + name + year + "(){";
                html = html + "\nvar x = '" + name + "';";
                html = html + "\nconst wholeTable = document.getElementsByClassName('row" + year + "');";
                html = html + """
                            \nfor(var i = 0; i < wholeTable.length; i++){
                                wholeTable[i].style.background = '#DDDDDD';
                            }
                        """;        

                        

                html = html + "\ndocument.getElementById(x + '" + year + "').style.background = '#00FFFF';";
                html = html + "\nvar name = document.getElementById(x + '-name" + year + "').innerHTML;";
                html = html + "\nvar total = document.getElementById(x + '-total" + year + "').innerHTML;";
                html = html + "\nvar ind_pop = document.getElementById(x + '-ind-pop" + year + "').innerHTML;";
                html = html + "\nvar ind_per = document.getElementById(x + '-ind-per" + year + "').innerHTML;";
                html = html + "\nvar non_pop = document.getElementById(x + '-non-pop" + year + "').innerHTML;";
                html = html + "\nvar non_per = document.getElementById(x + '-non-per" + year + "').innerHTML;";
                html = html + "\nvar gap_score = document.getElementById(x + '-score" + year + "').innerHTML;";

                
                html = html + "\ndocument.getElementById('text-description" + year + "').innerHTML = name + ' - The total number of households for the category is ' + total + ' households. ' +" +
                                                                   " 'The number of indigenous households is ' + ind_pop + ' households, making up ' + ind_per + ' of the total. ' + " +
                                                                   " 'The number of non-indigenous households is ' + non_pop + ' households, making up ' + non_per + ' of the total. '";
                html = html + "\ndocument.getElementById('formula" + year + "').innerHTML = 'Gap score - ' + non_per + ' - ' + ind_per + ' = ' + gap_score;}";
                    
                html = html + "</script>";
            }

        return html;
    }

    public String createDifferenceTableScript(ArrayList<GapScoreDifference> gapScores){
        String html = "";

        for(GapScoreDifference lga : gapScores){
            String name = fixName(lga.getName());

                html = html + "<script>";
                html = html + "\nfunction " + name + "difference(){";
                html = html + "\nvar x = '" + name + "';";
                html = html + "\nconst wholeTable = document.getElementsByClassName('row-difference');";
                html = html + """
                            \nfor(var i = 0; i < wholeTable.length; i++){
                                wholeTable[i].style.background = '#DDDDDD';
                            }
                        """;        

                        

                html = html + "\ndocument.getElementById(x + 'difference').style.background = '#00FFFF';";
                html = html + "\nvar name = document.getElementById(x + '-name-difference').innerHTML;";
                html = html + "\nvar score2016 = document.getElementById(x + '-2016-difference').innerHTML;";
                html = html + "\nvar score2021 = document.getElementById(x + '-2021-difference').innerHTML;";
                html = html + "\nvar scoreDifference = document.getElementById(x + '-score-difference').innerHTML;";

                
                html = html + "\ndocument.getElementById('text-description-difference').innerHTML = name + ' - The Gap score for 2016 is ' + score2016 + '. ' + " +
                                                                   " 'The Gap score for 2021 is ' + score2021 + '. ';";

                html = html + "\ndocument.getElementById('formula-difference').innerHTML = 'Gap Score difference between 2016 and 2021 - ' + scoreDifference;}";
                    
                html = html + "</script>";
            }


        return html;
    }

    public String fixName(String name){
        name = name.toLowerCase();
        name = name.replaceAll("\\s+", "");
        name = name.replaceAll("-", "");
        name = name.replaceAll("/", "");
        name = name.replaceAll("\\.", "");
        name = name.replaceAll("\\'", "");
        name = name.replaceAll("\\(", "");
        name = name.replaceAll("\\)", "");

        return name;
    }
}
