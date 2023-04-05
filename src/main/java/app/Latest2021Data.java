package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.lang.Math;


/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class Latest2021Data implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/latest-2021-data.html";

    String roundOffTo2DecPlaces(float val)
    {
        return String.format("%.2f", val);
    }

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Latest 2021 Data</title>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='latest.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/'>Homepage</a>
                <a href='about.html'>ABOUT US</a>
                <a href='latest-2021-data.html' class='current_page'>LATEST 2021 DATA</a>
                <a href='focus-lga-state.html'>FOCUS BY LGA/STATE</a>
                <a href='gap-scores.html'>GAP SCORES</a>
                <a href='find-similar-lga.html'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <section class = 'header-area'>
                    <div class='header-text'>
                        <h1>Latest Data from 2021 Census</h1>
                        <p>Only Applicable for Outcome 1, 5 and 8</p>
                    </div>
                </section>
            </div>
        """;
        html = html + "<main>";
        html = html + "<section class = 'body-area'>";
        html = html + "     <div class = 'outcome_div'>" ;
        html = html + "      <label for='outcome_drop'>Outcome and Dataset:</label>";
        html = html + "      <select id='outcome_drop' onchange='filterFunciton()'>";

        html = html + "         <option value='' selected disabled hidden>Choose here</option>";
        
        html = html + "         <optgroup label= 'Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives'>";
        html = html + "             <option value='dataset_demographic'>Age Demographic by Indigenous Status by Sex</option>";
        html = html + "             <option value='dataset_health'>Health Condition by Indigenous Status by Sex</option>";
        html = html + "         </optgroup>";

        html = html + "         <optgroup label= 'Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential'>";
        html = html + "             <option value='dataset_school'>Highest Year of School Completed by Indigenous Status by Sex</option>";
        html = html + "         </optgroup>";

        html = html + "         <optgroup label= 'Outcome 8: Strong economic participation and development of Aboriginal and Torres Strait Islander people and communities'>";
        html = html + "             <option value='dataset_household'>Household Weekly Income by Indigenous Status of Household</option>";
        html = html + "         </optgroup>";

        html = html + "      </select>";
        html = html + "     </div>" ;

        /* Get the Form Data
         *  from the drop down list
         * Need to be Careful!!
         *  If the form is not filled in, then the form will return null!
         * 
        */

        html = html + "<div id='display_filter'></div>";

        html = html + """
            <script>

            function filterFunciton() {
                var x = document.getElementById('outcome_drop').value;

                if (x == 'dataset_demographic') {
                document.getElementById('display_filter').innerHTML = `
                
                <form action='/latest-2021-data.html' method='post' id='form_dataset_demographic'>
                
                    <div class='form-group'>
                
                        <label for='age_sort_drop'>Sort Results By:</label>
                        <select id='age_sort_drop' name='age_sort_drop'> 
                            <option value='' selected disabled hidden>Choose here</option>   

                            <optgroup label= 'LGA Code'>             
                                <option value=' ORDER BY Lga.lgacode ASC'>Ascending</option>
                                <option value=' ORDER BY Lga.lgacode DESC'>Descending</option>
                            </optgroup>
                            
                            <optgroup label= 'LGA Name'>             
                                <option value=' ORDER BY lganame ASC'>Ascending</option>
                                <option value=' ORDER BY lganame DESC'>Descending</option>
                            </optgroup>

                            <optgroup label= 'Age Range'>             
                                <option value='age_range_ascending'>Ascending</option>
                                <option value='age_range_descending'>Descending</option>
                            </optgroup>

                            <optgroup label= 'Status'>             
                                <option value=' ORDER BY indigenous_status ASC'>Indigenous</option>
                                <option value=' ORDER BY indigenous_status DESC'>Non-Indigenous</option>
                            </optgroup>

                            <optgroup label= 'Gender'>             
                                <option value=' ORDER BY gender ASC'>Female</option>
                                <option value=' ORDER BY gender DESC'>Male</option>
                            </optgroup>

                            <optgroup label= 'Number of People'>             
                                <option value=' ORDER BY number_of_people ASC'>Ascending</option>
                                <option value=' ORDER BY number_of_people DESC'>Descending</option>
                            </optgroup>

                        </select>

                    </div>

                    <button type='submit' class='btn btn-primary' form='form_dataset_demographic'>Submit</button>

                </form>
                
                `;
                }

                else if (x == 'dataset_health') {
                    document.getElementById('display_filter').innerHTML = `
                    
                    <form action='/latest-2021-data.html' method='post' id='form_dataset_health'>
                    
                        <div class='form-group'>
                    
                            <label for='health_sort_drop'>Sort Results By:</label>
                            <select id='health_sort_drop' name='health_sort_drop'> 
                                <option value='' selected disabled hidden>Choose here</option>   
    
                                <optgroup label= 'LGA Code'>             
                                    <option value=' ORDER BY Lga.lgacode ASC'>Ascending</option>
                                    <option value=' ORDER BY Lga.lgacode DESC'>Descending</option>
                                </optgroup>
                                
                                <optgroup label= 'LGA Name'>             
                                    <option value=' ORDER BY lganame ASC'>Ascending</option>
                                    <option value=' ORDER BY lganame DESC'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Disease'>             
                                    <option value=' ORDER BY disease ASC'>Ascending</option>
                                    <option value=' ORDER BY disease DESC'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Status'>             
                                    <option value=' ORDER BY indigenous_status ASC'>Indigenous</option>
                                    <option value=' ORDER BY indigenous_status DESC'>Non-Indigenous</option>
                                </optgroup>
    
                                <optgroup label= 'Gender'>             
                                    <option value=' ORDER BY gender ASC'>Female</option>
                                    <option value=' ORDER BY gender DESC'>Male</option>
                                </optgroup>
    
                                <optgroup label= 'Number of People'>             
                                    <option value=' ORDER BY number_of_people ASC'>Ascending</option>
                                    <option value=' ORDER BY number_of_people DESC'>Descending</option>
                                </optgroup>
    
                            </select>
    
                        </div>
    
                        <button type='submit' class='btn btn-primary' form='form_dataset_health'>Submit</button>
    
                    </form>
                    
                    `;
                }

                else if (x == 'dataset_school') {
                    document.getElementById('display_filter').innerHTML = `
                    
                    <form action='/latest-2021-data.html' method='post' id='form_dataset_school'>
                    
                        <div class='form-group'>
                    
                            <label for='school_sort_drop'>Sort Results By:</label>
                            <select id='school_sort_drop' name='school_sort_drop'> 
                                <option value='' selected disabled hidden>Choose here</option>   
    
                                <optgroup label= 'LGA Code'>             
                                    <option value=' ORDER BY Lga.lgacode ASC'>Ascending</option>
                                    <option value=' ORDER BY Lga.lgacode DESC'>Descending</option>
                                </optgroup>
                                
                                <optgroup label= 'LGA Name'>             
                                    <option value=' ORDER BY lganame ASC'>Ascending</option>
                                    <option value=' ORDER BY lganame DESC'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Highest School Year Completed'>             
                                    <option value='school_year_ascending'>Ascending</option>
                                    <option value='school_year_descending'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Status'>             
                                    <option value=' ORDER BY indigenous_status ASC'>Indigenous</option>
                                    <option value=' ORDER BY indigenous_status DESC'>Non-Indigenous</option>
                                </optgroup>
    
                                <optgroup label= 'Gender'>             
                                    <option value=' ORDER BY gender ASC'>Female</option>
                                    <option value=' ORDER BY gender DESC'>Male</option>
                                </optgroup>
    
                                <optgroup label= 'Number of People'>             
                                    <option value=' ORDER BY number_of_people ASC'>Ascending</option>
                                    <option value=' ORDER BY number_of_people DESC'>Descending</option>
                                </optgroup>
    
                            </select>
    
                        </div>
    
                        <button type='submit' class='btn btn-primary' form='form_dataset_school'>Submit</button>
    
                    </form>
                    
                    `;
                }

                else if (x == 'dataset_household') {
                    document.getElementById('display_filter').innerHTML = `
                    
                    <form action='/latest-2021-data.html' method='post' id='form_dataset_household'>
                    
                        <div class='form-group'>
                    
                            <label for='household_sort_drop'>Sort Results By:</label>
                            <select id='household_sort_drop' name='household_sort_drop'> 
                                <option value='' selected disabled hidden>Choose here</option>   
    
                                <optgroup label= 'LGA Code'>             
                                    <option value=' ORDER BY Lga.lgacode ASC'>Ascending</option>
                                    <option value=' ORDER BY Lga.lgacode DESC'>Descending</option>
                                </optgroup>
                                
                                <optgroup label= 'LGA Name'>             
                                    <option value=' ORDER BY lganame ASC'>Ascending</option>
                                    <option value=' ORDER BY lganame DESC'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Weekly Income Range'>             
                                    <option value='income_range_ascending'>Ascending</option>
                                    <option value='income_range_descending'>Descending</option>
                                </optgroup>
    
                                <optgroup label= 'Status'>             
                                    <option value=' ORDER BY indigenous_status ASC'>Indigenous</option>
                                    <option value=' ORDER BY indigenous_status DESC'>Non-Indigenous</option>
                                </optgroup>
    
                                <optgroup label= 'Number of Household'>             
                                    <option value=' ORDER BY number_of_household ASC'>Ascending</option>
                                    <option value=' ORDER BY number_of_household DESC'>Descending</option>
                                </optgroup>
    
                            </select>
    
                        </div>
    
                        <button type='submit' class='btn btn-primary' form='form_dataset_household'>Submit</button>
    
                    </form>
                    
                    `;
                }
            }
            </script>
                """;
            String outcome_drop = context.formParam("outcome_drop");
            String age_sort_drop = context.formParam("age_sort_drop");
            String health_sort_drop = context.formParam("health_sort_drop");
            String school_sort_drop = context.formParam("school_sort_drop");
            String household_sort_drop = context.formParam("household_sort_drop");

            // Look up some information from JDBC
            // First we need to use your JDBCConnection class
            JDBCConnection jdbc = new JDBCConnection();

            if (age_sort_drop == null && health_sort_drop == null && school_sort_drop == null && household_sort_drop == null) {
                html = html + "<h5>Please Select from the Dropdown to Continue</h5>";
            }
            

            if (age_sort_drop != null){
                ArrayList<AgeDataset2021> age_range_dataset_2021 = jdbc.sortAgeDataset2021(age_sort_drop);

                html = html +"<h1 class='outcome_title'>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives</h1>";

                html = html + "<h3 class='dataset_title'>Dataset: Age Demographic by Indigenous Status by Sex</h3>";


                switch(age_sort_drop) {
                    case " ORDER BY Lga.lgacode ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Ascending)</p>";
                        break;
                    case " ORDER BY Lga.lgacode DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Descending)</p>";
                        break;
                    case " ORDER BY lganame ASC":
                        html = html + "<p class='sort_by_desc'Sort By: LGA Name (Ascending)</p>";
                        break;
                    case " ORDER BY lganame DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Name (Descending)</p>";
                        break;
                    case "age_range_ascending":
                        html = html + "<p class='sort_by_desc'>Sort By: Age Range (Ascending)</p>";
                        break;
                    case "age_range_descending":
                        html = html + "<p class='sort_by_desc'>Sort By: Age Range (Descending)</p>";
                        break;
                    case " ORDER BY indigenous_status DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Non-Indigenous)</p>";
                        break;
                    case " ORDER BY indigenous_status ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Indigenous)</p>";
                        break;
                    case " ORDER BY gender ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Female)</p>";
                        break;
                    case " ORDER BY gender DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Male)</p>";
                        break;
                    case " ORDER BY number_of_people ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Ascending)</p>";
                        break;
                    case " ORDER BY number_of_people DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Descending)</p>";
                        break;
                }


                html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Age Range</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Number of People</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                int sumIndigenous = 0;
                int sumNonIndigenous = 0;
                int sumMale= 0;
                int sumFemale= 0;
                int sum0_4 = 0;
                int sum5_9 = 0;
                int sum10_14 = 0;
                int sum15_19 = 0;
                int sum20_24 = 0;
                int sum25_29 = 0;
                int sum30_34 = 0;
                int sum35_39 = 0;
                int sum40_44 = 0;
                int sum45_49 = 0;
                int sum50_54 = 0;
                int sum55_59 = 0;
                int sum60_64 = 0;
                int sum65_older = 0;

                int sumIndigenousMale0_4 = 0;
                int sumIndigenousMale5_9 = 0;
                int sumIndigenousMale10_14 = 0;
                int sumIndigenousMale15_19 = 0;
                int sumIndigenousMale20_24 = 0;
                int sumIndigenousMale25_29 = 0;
                int sumIndigenousMale30_34 = 0;
                int sumIndigenousMale35_39 = 0;
                int sumIndigenousMale40_44 = 0;
                int sumIndigenousMale45_49 = 0;
                int sumIndigenousMale50_54 = 0;
                int sumIndigenousMale55_59 = 0;
                int sumIndigenousMale60_64 = 0;
                int sumIndigenousMale65_older = 0;

                int sumNonIndigenousMale0_4 = 0;
                int sumNonIndigenousMale5_9 = 0;
                int sumNonIndigenousMale10_14 = 0;
                int sumNonIndigenousMale15_19 = 0;
                int sumNonIndigenousMale20_24 = 0;
                int sumNonIndigenousMale25_29 = 0;
                int sumNonIndigenousMale30_34 = 0;
                int sumNonIndigenousMale35_39 = 0;
                int sumNonIndigenousMale40_44 = 0;
                int sumNonIndigenousMale45_49 = 0;
                int sumNonIndigenousMale50_54 = 0;
                int sumNonIndigenousMale55_59 = 0;
                int sumNonIndigenousMale60_64 = 0;
                int sumNonIndigenousMale65_older = 0;

                int sumIndigenousFemale0_4 = 0;
                int sumIndigenousFemale5_9 = 0;
                int sumIndigenousFemale10_14 = 0;
                int sumIndigenousFemale15_19 = 0;
                int sumIndigenousFemale20_24 = 0;
                int sumIndigenousFemale25_29 = 0;
                int sumIndigenousFemale30_34 = 0;
                int sumIndigenousFemale35_39 = 0;
                int sumIndigenousFemale40_44 = 0;
                int sumIndigenousFemale45_49 = 0;
                int sumIndigenousFemale50_54 = 0;
                int sumIndigenousFemale55_59 = 0;
                int sumIndigenousFemale60_64 = 0;
                int sumIndigenousFemale65_older = 0;

                int sumNonIndigenousFemale0_4 = 0;
                int sumNonIndigenousFemale5_9 = 0;
                int sumNonIndigenousFemale10_14 = 0;
                int sumNonIndigenousFemale15_19 = 0;
                int sumNonIndigenousFemale20_24 = 0;
                int sumNonIndigenousFemale25_29 = 0;
                int sumNonIndigenousFemale30_34 = 0;
                int sumNonIndigenousFemale35_39 = 0;
                int sumNonIndigenousFemale40_44 = 0;
                int sumNonIndigenousFemale45_49 = 0;
                int sumNonIndigenousFemale50_54 = 0;
                int sumNonIndigenousFemale55_59 = 0;
                int sumNonIndigenousFemale60_64 = 0;
                int sumNonIndigenousFemale65_older = 0;

                for (AgeDataset2021 data : age_range_dataset_2021) {
                    html = html + "<tr>";
                    html = html + "     <td>" + data.getLgaCode() + "</td>";
                    html = html + "     <td>" + data.getLgaName() + "</td>";
                    html = html + "     <td>" + data.getAgeRange() + "</td>";
                    html = html + "     <td>" + data.getStatus() + "</td>";
                    html = html + "     <td>" + data.getGender() + "</td>";
                    html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                    html = html + "</tr>";

                    if (data.getStatus().equals("indigenous")) {
                        sumIndigenous += data.getNumberOfPeople();

                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getAgeRange().equals("0_4")) {
                                sum0_4 += data.getNumberOfPeople();
                                sumIndigenousMale0_4 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("5_9")) {
                                sum5_9 += data.getNumberOfPeople();
                                sumIndigenousMale5_9 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("10_14")) {
                                sum10_14 += data.getNumberOfPeople();
                                sumIndigenousMale10_14 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("15_19")) {
                                sum15_19 += data.getNumberOfPeople();
                                sumIndigenousMale15_19 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("20_24")) {
                                sum20_24 += data.getNumberOfPeople();
                                sumIndigenousMale20_24 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("25_29")) {
                                sum25_29 += data.getNumberOfPeople();
                                sumIndigenousMale25_29 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("30_34")) {
                                sum30_34 += data.getNumberOfPeople();
                                sumIndigenousMale30_34 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("35_39")) {
                                sum35_39 += data.getNumberOfPeople();
                                sumIndigenousMale35_39 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("40_44")) {
                                sum40_44 += data.getNumberOfPeople();
                                sumIndigenousMale40_44 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("45_49")) {
                                sum45_49 += data.getNumberOfPeople();
                                sumIndigenousMale45_49 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("50_54")) {
                                sum50_54 += data.getNumberOfPeople();
                                sumIndigenousMale50_54 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("55_59")) {
                                sum55_59 += data.getNumberOfPeople();
                                sumIndigenousMale55_59 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("60_64")) {
                                sum60_64 += data.getNumberOfPeople();
                                sumIndigenousMale60_64 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("65_older")) {
                                sum65_older += data.getNumberOfPeople();
                                sumIndigenousMale65_older += data.getNumberOfPeople();
                            }
                        }
                    
                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getAgeRange().equals("0_4")) {
                                sum0_4 += data.getNumberOfPeople();
                                sumIndigenousFemale0_4 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("5_9")) {
                                sum5_9 += data.getNumberOfPeople();
                                sumIndigenousFemale5_9 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("10_14")) {
                                sum10_14 += data.getNumberOfPeople();
                                sumIndigenousFemale10_14 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("15_19")) {
                                sum15_19 += data.getNumberOfPeople();
                                sumIndigenousFemale15_19 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("20_24")) {
                                sum20_24 += data.getNumberOfPeople();
                                sumIndigenousFemale20_24 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("25_29")) {
                                sum25_29 += data.getNumberOfPeople();
                                sumIndigenousFemale25_29 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("30_34")) {
                                sum30_34 += data.getNumberOfPeople();
                                sumIndigenousFemale30_34 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("35_39")) {
                                sum35_39 += data.getNumberOfPeople();
                                sumIndigenousFemale35_39 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("40_44")) {
                                sum40_44 += data.getNumberOfPeople();
                                sumIndigenousFemale40_44 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("45_49")) {
                                sum45_49 += data.getNumberOfPeople();
                                sumIndigenousFemale45_49 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("50_54")) {
                                sum50_54 += data.getNumberOfPeople();
                                sumIndigenousFemale50_54 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("55_59")) {
                                sum55_59 += data.getNumberOfPeople();
                                sumIndigenousFemale55_59 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("60_64")) {
                                sum60_64 += data.getNumberOfPeople();
                                sumIndigenousFemale60_64 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("65_older")) {
                                sum65_older += data.getNumberOfPeople();
                                sumIndigenousFemale65_older += data.getNumberOfPeople();
                            }
                        }
                    }

                    else if (data.getStatus().equals("non_indigenous")) {
                        sumNonIndigenous += data.getNumberOfPeople();
                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getAgeRange().equals("0_4")) {
                                sum0_4 += data.getNumberOfPeople();
                                sumNonIndigenousMale0_4 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("5_9")) {
                                sum5_9 += data.getNumberOfPeople();
                                sumNonIndigenousMale5_9 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("10_14")) {
                                sum10_14 += data.getNumberOfPeople();
                                sumNonIndigenousMale10_14 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("15_19")) {
                                sum15_19 += data.getNumberOfPeople();
                                sumNonIndigenousMale15_19 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("20_24")) {
                                sum20_24 += data.getNumberOfPeople();
                                sumNonIndigenousMale20_24 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("25_29")) {
                                sum25_29 += data.getNumberOfPeople();
                                sumNonIndigenousMale25_29 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("30_34")) {
                                sum30_34 += data.getNumberOfPeople();
                                sumNonIndigenousMale30_34 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("35_39")) {
                                sum35_39 += data.getNumberOfPeople();
                                sumNonIndigenousMale35_39 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("40_44")) {
                                sum40_44 += data.getNumberOfPeople();
                                sumNonIndigenousMale40_44 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("45_49")) {
                                sum45_49 += data.getNumberOfPeople();
                                sumNonIndigenousMale45_49 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("50_54")) {
                                sum50_54 += data.getNumberOfPeople();
                                sumNonIndigenousMale50_54 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("55_59")) {
                                sum55_59 += data.getNumberOfPeople();
                                sumNonIndigenousMale55_59 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("60_64")) {
                                sum60_64 += data.getNumberOfPeople();
                                sumNonIndigenousMale60_64 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("65_older")) {
                                sum65_older += data.getNumberOfPeople();
                                sumNonIndigenousMale65_older += data.getNumberOfPeople();
                            }
                        }

                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getAgeRange().equals("0_4")) {
                                sum0_4 += data.getNumberOfPeople();
                                sumNonIndigenousFemale0_4 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("5_9")) {
                                sum5_9 += data.getNumberOfPeople();
                                sumNonIndigenousFemale5_9 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("10_14")) {
                                sum10_14 += data.getNumberOfPeople();
                                sumNonIndigenousFemale10_14 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("15_19")) {
                                sum15_19 += data.getNumberOfPeople();
                                sumNonIndigenousFemale15_19 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("20_24")) {
                                sum20_24 += data.getNumberOfPeople();
                                sumNonIndigenousFemale20_24 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("25_29")) {
                                sum25_29 += data.getNumberOfPeople();
                                sumNonIndigenousFemale25_29 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("30_34")) {
                                sum30_34 += data.getNumberOfPeople();
                                sumNonIndigenousFemale30_34 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("35_39")) {
                                sum35_39 += data.getNumberOfPeople();
                                sumNonIndigenousFemale35_39 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("40_44")) {
                                sum40_44 += data.getNumberOfPeople();
                                sumNonIndigenousFemale40_44 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("45_49")) {
                                sum45_49 += data.getNumberOfPeople();
                                sumNonIndigenousFemale45_49 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("50_54")) {
                                sum50_54 += data.getNumberOfPeople();
                                sumNonIndigenousFemale50_54 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("55_59")) {
                                sum55_59 += data.getNumberOfPeople();
                                sumNonIndigenousFemale55_59 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("60_64")) {
                                sum60_64 += data.getNumberOfPeople();
                                sumNonIndigenousFemale60_64 += data.getNumberOfPeople();
                            }
                            else if (data.getAgeRange().equals("65_older")) {
                                sum65_older += data.getNumberOfPeople();
                                sumNonIndigenousFemale65_older += data.getNumberOfPeople();
                            }
                        }
                    }


                }
                html = html + """
                            </tbody>
                        </table>
                    </div>
                """;

                html = html + "<div class='data_insight'>";

                html = html + "     <h3>Data Insights (across surveyed LGA)</h3>";

                //Indigenous Status:
                html = html + "<h4>Indigenous Status:</h4>";
                //Total number of Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Indigenous Population: " + sumIndigenous + "</p>";
                //Total number of Non-Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Non-Indigenous Population: " + sumNonIndigenous + "</p>";
                //Proportion of Inidgenous Status across surveyed LGA
                html = html + "<p>Proportion of Inidgenous Status: </p>";
                html = html + "<ul>";
                //Indigenous : Non-Indigenous
                int proportionIndigenous = Math.round(100*(sumIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                int proportionNonIndigenous = Math.round(100*(sumNonIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                html = html + "<li>Indigenous: " + proportionIndigenous + "% </li>";
                html = html + "<li>Non-Indigenous: " + proportionNonIndigenous + "% </li>";
                //The Gap:
                html = html + "<li>The Gap: " + Math.abs(proportionNonIndigenous - proportionIndigenous) + "% Differences</li>";
                html = html + "</ul>";


                //Gender:
                html = html + "<h4>Gender:</h4>";
                //Total number of Male Population across surveyed LGA
                html = html + "<p>Total number of Male Population: " + sumMale + "</p>";
                //Total number of Female Population across surveyed LGA
                html = html + "<p>Total number of Female Population: " + sumFemale + "</p>";
                //Proportion of Gender across surveyed LGA
                html = html + "<p>Proportion of Gender: </p>";
                html = html + "<ul>";
                //Male:Female
                int proportionMale = Math.round(100*(sumMale/(float)(sumMale + sumFemale)));
                int proportionFemale= Math.round(100*(sumFemale/(float)(sumMale + sumFemale)));
                html = html + "<li>Male: " + proportionMale + "% </li>";
                html = html + "<li>Female: " + proportionFemale + "% </li>";
                html = html + "</ul>";

                //Age Range:
                html = html + "<h4>Age Range:</h4>";
                html = html + "<p>Total number of 0 - 4 years old Population: " + sum0_4 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale0_4 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale0_4/sum0_4)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale0_4 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale0_4/sum0_4)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale0_4 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale0_4/sum0_4)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale0_4 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale0_4/sum0_4)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale0_4+sumNonIndigenousFemale0_4-sumIndigenousMale0_4-sumIndigenousFemale0_4))/sum0_4)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 5 - 9 years old Population: " + sum5_9 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale5_9 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale5_9/sum5_9)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale5_9 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale5_9/sum5_9)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale5_9 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale5_9/sum5_9)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale5_9 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale5_9/sum5_9)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale5_9+sumNonIndigenousFemale5_9-sumIndigenousMale5_9-sumIndigenousFemale5_9))/sum5_9)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 10 - 14 years old Population: " + sum10_14 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale10_14 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale10_14/sum10_14)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale10_14 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale10_14/sum10_14)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale10_14 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale10_14/sum10_14)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale10_14 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale10_14/sum10_14)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale10_14+sumNonIndigenousFemale10_14-sumIndigenousMale10_14-sumIndigenousFemale10_14))/sum10_14)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 15 - 19 years old Population: " + sum15_19 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale15_19 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale15_19/sum15_19)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale15_19 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale15_19/sum15_19)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale15_19 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale15_19/sum15_19)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale15_19 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale15_19/sum15_19)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale15_19+sumNonIndigenousFemale15_19-sumIndigenousMale15_19-sumIndigenousFemale15_19))/sum15_19)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 20 - 24 years old Population: " + sum20_24 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale20_24 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale20_24/sum20_24)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale20_24 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale20_24/sum20_24)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale20_24 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale20_24/sum20_24)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale20_24 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale20_24/sum20_24)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale20_24+sumNonIndigenousFemale20_24-sumIndigenousMale20_24-sumIndigenousFemale20_24))/sum20_24)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 25 - 29 years old Population: " + sum25_29 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale25_29 +  " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale25_29/sum25_29)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale25_29 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale25_29/sum25_29)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale25_29 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale25_29/sum25_29)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale25_29 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale25_29/sum25_29)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale25_29+sumNonIndigenousFemale25_29-sumIndigenousMale25_29-sumIndigenousFemale25_29))/sum25_29)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 30 - 34 years old Population: " + sum30_34 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale30_34 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale30_34/sum30_34)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale30_34 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale30_34/sum30_34)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale30_34 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale30_34/sum30_34)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale30_34 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale30_34/sum30_34)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale30_34+sumNonIndigenousFemale30_34-sumIndigenousMale30_34-sumIndigenousFemale30_34))/sum30_34)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 35 - 39 years old Population: " + sum35_39 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale35_39 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale35_39/sum35_39)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale35_39 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale35_39/sum35_39)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale35_39 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale35_39/sum35_39)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale35_39 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale35_39/sum35_39)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale35_39+sumNonIndigenousFemale35_39-sumIndigenousMale35_39-sumIndigenousFemale35_39))/sum35_39)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 40 - 44 years old Population: " + sum40_44 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale40_44 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale40_44/sum40_44)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale40_44 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale40_44/sum40_44)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale40_44 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale40_44/sum40_44)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale40_44 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale40_44/sum40_44)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale40_44+sumNonIndigenousFemale40_44-sumIndigenousMale40_44-sumIndigenousFemale40_44))/sum40_44)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 45 - 49 years old Population: " + sum45_49 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale45_49 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale45_49/sum45_49)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale45_49 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale45_49/sum45_49)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale45_49 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale45_49/sum45_49)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale45_49 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale45_49/sum45_49)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale45_49+sumNonIndigenousFemale45_49-sumIndigenousMale45_49-sumIndigenousFemale45_49))/sum45_49)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 50 - 54 years old Population: " + sum50_54 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale50_54 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale50_54/sum50_54)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale50_54 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale50_54/sum50_54)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale50_54 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale50_54/sum50_54)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale50_54 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale50_54/sum50_54)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale50_54+sumNonIndigenousFemale50_54-sumIndigenousMale50_54-sumIndigenousFemale50_54))/sum50_54)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 55 - 59 years old Population: " + sum55_59 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale55_59 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale55_59/sum55_59)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale55_59 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale55_59/sum55_59)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale55_59 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale55_59/sum55_59)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale55_59 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale55_59/sum55_59)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale55_59+sumNonIndigenousFemale55_59-sumIndigenousMale55_59-sumIndigenousFemale55_59))/sum55_59)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 60 - 64 years old Population: " + sum60_64 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale60_64 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale60_64/sum60_64)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale60_64 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale60_64/sum60_64)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale60_64 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale60_64/sum60_64)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale60_64 + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale60_64/sum60_64)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale60_64+sumNonIndigenousFemale60_64-sumIndigenousMale60_64-sumIndigenousFemale60_64))/sum60_64)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of 65 and more years old Population: " + sum65_older + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMale65_older + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMale65_older/sum65_older)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMale65_older + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMale65_older/sum65_older)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemale65_older + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemale65_older/sum65_older)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemale65_older + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemale65_older/sum65_older)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMale65_older+sumNonIndigenousFemale65_older-sumIndigenousMale65_older-sumIndigenousFemale65_older))/sum65_older)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                //Proportion of Age Range across surveyed LGA
                html = html + "<p>Proportion of Age Range: </p>";
                html = html + "<ul>";
                //Male:Female
                int sumAgeRange = sum0_4 + sum5_9 + sum10_14 + sum15_19 + sum20_24 + sum25_29 + sum30_34 + sum35_39 + sum40_44
                + sum45_49 + sum50_54 + sum55_59 + sum55_59 + sum60_64 + sum65_older;

                String  propAgeRange0_4 = (String) roundOffTo2DecPlaces(((float)sum0_4/ sumAgeRange)*100);
                String  propAgeRange5_9 = (String) roundOffTo2DecPlaces(((float)sum5_9/ sumAgeRange)*100);
                String  propAgeRange10_14 = (String) roundOffTo2DecPlaces(((float)sum10_14/ sumAgeRange)*100);
                String  propAgeRange15_19 = (String) roundOffTo2DecPlaces(((float)sum15_19/ sumAgeRange)*100);
                String  propAgeRange20_24 = (String) roundOffTo2DecPlaces(((float)sum20_24/ sumAgeRange)*100);
                String  propAgeRange25_29 = (String) roundOffTo2DecPlaces(((float)sum25_29/ sumAgeRange)*100);
                String  propAgeRange30_34 = (String) roundOffTo2DecPlaces(((float)sum30_34/ sumAgeRange)*100);
                String  propAgeRange35_39 = (String) roundOffTo2DecPlaces(((float)sum35_39/ sumAgeRange)*100);
                String  propAgeRange40_44 = (String) roundOffTo2DecPlaces(((float)sum40_44/ sumAgeRange)*100);
                String  propAgeRange45_49 = (String) roundOffTo2DecPlaces(((float)sum45_49/ sumAgeRange)*100);
                String  propAgeRange50_54 = (String) roundOffTo2DecPlaces(((float)sum50_54/ sumAgeRange)*100);
                String  propAgeRange55_59 = (String) roundOffTo2DecPlaces(((float)sum55_59/ sumAgeRange)*100);
                String  propAgeRange60_64 = (String) roundOffTo2DecPlaces(((float)sum60_64/ sumAgeRange)*100);
                String  propAgeRange65_older = (String) roundOffTo2DecPlaces(((float)sum65_older/ sumAgeRange)*100);
                
                html = html + "<li>0 - 4 years old: " + propAgeRange0_4 + "% </li>";
                html = html + "<li>5 - 9 years old: " + propAgeRange5_9 + "% </li>";
                html = html + "<li>10 - 14 years old: " + propAgeRange10_14 + "% </li>";
                html = html + "<li>15 - 19 years old: " + propAgeRange15_19 + "% </li>";
                html = html + "<li>20 - 24 years old: " + propAgeRange20_24 + "% </li>";
                html = html + "<li>25 - 29 years old: " + propAgeRange25_29 + "% </li>";
                html = html + "<li>30 - 34 years old: " + propAgeRange30_34 + "% </li>";
                html = html + "<li>35 - 39 years old: " + propAgeRange35_39 + "% </li>";
                html = html + "<li>40 - 44 years old: " + propAgeRange40_44 + "% </li>";
                html = html + "<li>45 - 49 years old: " + propAgeRange45_49 + "% </li>";
                html = html + "<li>50 - 54 years old: " + propAgeRange50_54 + "% </li>";
                html = html + "<li>55 - 59 years old: " + propAgeRange55_59 + "% </li>";
                html = html + "<li>60 - 64 years old: " + propAgeRange60_64 + "% </li>";
                html = html + "<li>65 and older years old: " + propAgeRange65_older + "% </li>";

                html = html + "</ul>";
                html = html + "</div>";
        
            }

            if (health_sort_drop != null){
                ArrayList<HealthDataset2021> health_dataset_2021 = jdbc.sortHealthDataset2021(health_sort_drop);

                html = html +"<h1 class='outcome_title'>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives</h1>";

                html = html + "<h3 class='dataset_title'>Dataset: Health Condition by Indigenous Status by Sex</h3>";


                switch(health_sort_drop) {
                    case " ORDER BY Lga.lgacode ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Ascending)</p>";
                        break;
                    case " ORDER BY Lga.lgacode DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Descending)</p>";
                        break;
                    case " ORDER BY lganame ASC":
                        html = html + "<p class='sort_by_desc'Sort By: LGA Name (Ascending)</p>";
                        break;
                    case " ORDER BY lganame DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Name (Descending)</p>";
                        break;
                    case " ORDER BY disease ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Disease (Ascending)</p>";
                        break;
                    case " ORDER BY disease DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Disease (Descending)</p>";
                        break;
                    case " ORDER BY indigenous_status ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Indigenous)</p>";
                        break;
                    case " ORDER BY indigenous_status DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Non-Indigenous)</p>";
                        break;
                    case " ORDER BY gender ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Female)</p>";
                        break;
                    case " ORDER BY gender DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Male)</p>";
                        break;
                    case " ORDER BY number_of_people ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Ascending)</p>";
                        break;
                    case " ORDER BY number_of_people DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Descending)</p>";
                        break;
                }


                html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Disease</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Number of People</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                int sumIndigenous = 0;
                int sumNonIndigenous = 0;
                int sumMale= 0;
                int sumFemale= 0;
                int sumArthritis = 0;
                int sumAsthma = 0;
                int sumCancer = 0;
                int sumDementia = 0;
                int sumDiabetes = 0;
                int sumHeart_disease = 0;
                int sumKidney_disease = 0;
                int sumLung_condition = 0;
                int sumMental_health = 0;
                int sumOther = 0;
                int sumStroke = 0;

                int sumIndigenousMaleArthritis = 0;
                int sumIndigenousMaleAsthma = 0;
                int sumIndigenousMaleCancer = 0;
                int sumIndigenousMaleDementia = 0;
                int sumIndigenousMaleDiabetes = 0;
                int sumIndigenousMaleHeart_disease = 0;
                int sumIndigenousMaleKidney_disease = 0;
                int sumIndigenousMaleLung_condition = 0;
                int sumIndigenousMaleMental_health = 0;
                int sumIndigenousMaleOther = 0;
                int sumIndigenousMaleStroke = 0;

                int sumNonIndigenousMaleArthritis = 0;
                int sumNonIndigenousMaleAsthma = 0;
                int sumNonIndigenousMaleCancer = 0;
                int sumNonIndigenousMaleDementia = 0;
                int sumNonIndigenousMaleDiabetes = 0;
                int sumNonIndigenousMaleHeart_disease = 0;
                int sumNonIndigenousMaleKidney_disease = 0;
                int sumNonIndigenousMaleLung_condition = 0;
                int sumNonIndigenousMaleMental_health = 0;
                int sumNonIndigenousMaleOther = 0;
                int sumNonIndigenousMaleStroke = 0;

                int sumIndigenousFemaleArthritis = 0;
                int sumIndigenousFemaleAsthma = 0;
                int sumIndigenousFemaleCancer = 0;
                int sumIndigenousFemaleDementia = 0;
                int sumIndigenousFemaleDiabetes = 0;
                int sumIndigenousFemaleHeart_disease = 0;
                int sumIndigenousFemaleKidney_disease = 0;
                int sumIndigenousFemaleLung_condition = 0;
                int sumIndigenousFemaleMental_health = 0;
                int sumIndigenousFemaleOther = 0;
                int sumIndigenousFemaleStroke = 0;

                int sumNonIndigenousFemaleArthritis = 0;
                int sumNonIndigenousFemaleAsthma = 0;
                int sumNonIndigenousFemaleCancer = 0;
                int sumNonIndigenousFemaleDementia = 0;
                int sumNonIndigenousFemaleDiabetes = 0;
                int sumNonIndigenousFemaleHeart_disease = 0;
                int sumNonIndigenousFemaleKidney_disease = 0;
                int sumNonIndigenousFemaleLung_condition = 0;
                int sumNonIndigenousFemaleMental_health = 0;
                int sumNonIndigenousFemaleOther = 0;
                int sumNonIndigenousFemaleStroke = 0;
                

                for (HealthDataset2021 data : health_dataset_2021) {
                    html = html + "<tr>";
                    html = html + "     <td>" + data.getLgaCode() + "</td>";
                    html = html + "     <td>" + data.getLgaName() + "</td>";
                    html = html + "     <td>" + data.getDisease() + "</td>";
                    html = html + "     <td>" + data.getStatus() + "</td>";
                    html = html + "     <td>" + data.getGender() + "</td>";
                    html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                    html = html + "</tr>";

                    if (data.getStatus().equals("indigenous")) {
                        sumIndigenous += data.getNumberOfPeople();

                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getDisease().equals("arthritis")) {
                                sumArthritis += data.getNumberOfPeople();
                                sumIndigenousMaleArthritis += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("asthma")) {
                                sumAsthma += data.getNumberOfPeople();
                                sumIndigenousMaleAsthma += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("cancer")) {
                                sumCancer += data.getNumberOfPeople();
                                sumIndigenousMaleCancer += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("dementia")) {
                                sumDementia += data.getNumberOfPeople();
                                sumIndigenousMaleDementia += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("diabetes")) {
                                sumDiabetes += data.getNumberOfPeople();
                                sumIndigenousMaleDiabetes += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("heart_disease")) {
                                sumHeart_disease += data.getNumberOfPeople();
                                sumIndigenousMaleHeart_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("kidney_disease")) {
                                sumKidney_disease += data.getNumberOfPeople();
                                sumIndigenousMaleKidney_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("lung_condition")) {
                                sumLung_condition += data.getNumberOfPeople();
                                sumIndigenousMaleLung_condition += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("mental_health")) {
                                sumMental_health += data.getNumberOfPeople();
                                sumIndigenousMaleMental_health += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("other")) {
                                sumOther += data.getNumberOfPeople();
                                sumIndigenousMaleOther += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("stroke")) {
                                sumStroke += data.getNumberOfPeople();
                                sumIndigenousMaleStroke += data.getNumberOfPeople();
                            }
                        }

                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getDisease().equals("arthritis")) {
                                sumArthritis += data.getNumberOfPeople();
                                sumIndigenousFemaleArthritis += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("asthma")) {
                                sumAsthma += data.getNumberOfPeople();
                                sumIndigenousFemaleAsthma += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("cancer")) {
                                sumCancer += data.getNumberOfPeople();
                                sumIndigenousFemaleCancer += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("dementia")) {
                                sumDementia += data.getNumberOfPeople();
                                sumIndigenousFemaleDementia += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("diabetes")) {
                                sumDiabetes += data.getNumberOfPeople();
                                sumIndigenousFemaleDiabetes += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("heart_disease")) {
                                sumHeart_disease += data.getNumberOfPeople();
                                sumIndigenousFemaleHeart_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("kidney_disease")) {
                                sumKidney_disease += data.getNumberOfPeople();
                                sumIndigenousFemaleKidney_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("lung_condition")) {
                                sumLung_condition += data.getNumberOfPeople();
                                sumIndigenousFemaleLung_condition += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("mental_health")) {
                                sumMental_health += data.getNumberOfPeople();
                                sumIndigenousFemaleMental_health += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("other")) {
                                sumOther += data.getNumberOfPeople();
                                sumIndigenousFemaleOther += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("stroke")) {
                                sumStroke += data.getNumberOfPeople();
                                sumIndigenousFemaleStroke += data.getNumberOfPeople();
                            }
                        }
                    
                    }

                    else if (data.getStatus().equals("non_indigenous")) {
                        sumNonIndigenous += data.getNumberOfPeople();

                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getDisease().equals("arthritis")) {
                                sumArthritis += data.getNumberOfPeople();
                                sumNonIndigenousMaleArthritis += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("asthma")) {
                                sumAsthma += data.getNumberOfPeople();
                                sumNonIndigenousMaleAsthma += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("cancer")) {
                                sumCancer += data.getNumberOfPeople();
                                sumNonIndigenousMaleCancer += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("dementia")) {
                                sumDementia += data.getNumberOfPeople();
                                sumNonIndigenousMaleDementia += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("diabetes")) {
                                sumDiabetes += data.getNumberOfPeople();
                                sumNonIndigenousMaleDiabetes += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("heart_disease")) {
                                sumHeart_disease += data.getNumberOfPeople();
                                sumNonIndigenousMaleHeart_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("kidney_disease")) {
                                sumKidney_disease += data.getNumberOfPeople();
                                sumNonIndigenousMaleKidney_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("lung_condition")) {
                                sumLung_condition += data.getNumberOfPeople();
                                sumNonIndigenousMaleLung_condition += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("mental_health")) {
                                sumMental_health += data.getNumberOfPeople();
                                sumNonIndigenousMaleMental_health += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("other")) {
                                sumOther += data.getNumberOfPeople();
                                sumNonIndigenousMaleOther += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("stroke")) {
                                sumStroke += data.getNumberOfPeople();
                                sumNonIndigenousMaleStroke += data.getNumberOfPeople();
                            }
                        }

                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getDisease().equals("arthritis")) {
                                sumArthritis += data.getNumberOfPeople();
                                sumNonIndigenousFemaleArthritis += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("asthma")) {
                                sumAsthma += data.getNumberOfPeople();
                                sumNonIndigenousFemaleAsthma += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("cancer")) {
                                sumCancer += data.getNumberOfPeople();
                                sumNonIndigenousFemaleCancer += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("dementia")) {
                                sumDementia += data.getNumberOfPeople();
                                sumNonIndigenousFemaleDementia += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("diabetes")) {
                                sumDiabetes += data.getNumberOfPeople();
                                sumNonIndigenousFemaleDiabetes += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("heart_disease")) {
                                sumHeart_disease += data.getNumberOfPeople();
                                sumNonIndigenousFemaleHeart_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("kidney_disease")) {
                                sumKidney_disease += data.getNumberOfPeople();
                                sumNonIndigenousFemaleKidney_disease += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("lung_condition")) {
                                sumLung_condition += data.getNumberOfPeople();
                                sumNonIndigenousFemaleLung_condition += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("mental_health")) {
                                sumMental_health += data.getNumberOfPeople();
                                sumNonIndigenousFemaleMental_health += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("other")) {
                                sumOther += data.getNumberOfPeople();
                                sumNonIndigenousFemaleOther += data.getNumberOfPeople();
                            }
                            else if (data.getDisease().equals("stroke")) {
                                sumStroke += data.getNumberOfPeople();
                                sumNonIndigenousFemaleStroke += data.getNumberOfPeople();
                            }
                        }
                    
                    }

                }
                html = html + """
                            </tbody>
                        </table>
                    </div>
                """;

                html = html + "<div class='data_insight'>";

                html = html + "     <h3>Data Insights (across surveyed LGA)</h3>";

                //Indigenous Status:
                html = html + "<h4>Indigenous Status:</h4>";
                //Total number of Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Indigenous Population: " + sumIndigenous + "</p>";
                //Total number of Non-Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Non-Indigenous Population: " + sumNonIndigenous + "</p>";
                //Proportion of Inidgenous Status across surveyed LGA
                html = html + "<p>Proportion of Inidgenous Status: </p>";
                html = html + "<ul>";
                //Indigenous : Non-Indigenous
                int proportionIndigenous = Math.round(100*(sumIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                int proportionNonIndigenous = Math.round(100*(sumNonIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                html = html + "<li>Indigenous: " + proportionIndigenous + "% </li>";
                html = html + "<li>Non-Indigenous: " + proportionNonIndigenous + "% </li>";
                //The Gap:
                html = html + "<li>The Gap: " + Math.abs(proportionNonIndigenous - proportionIndigenous) + "% Differences</li>";
                html = html + "</ul>";


                //Gender:
                html = html + "<h4>Gender:</h4>";
                //Total number of Male Population across surveyed LGA
                html = html + "<p>Total number of Male Population: " + sumMale + "</p>";
                //Total number of Female Population across surveyed LGA
                html = html + "<p>Total number of Female Population: " + sumFemale + "</p>";
                //Proportion of Gender across surveyed LGA
                html = html + "<p>Proportion of Gender: </p>";
                html = html + "<ul>";
                //Male:Female
                int proportionMale = Math.round(100*(sumMale/(float)(sumMale + sumFemale)));
                int proportionFemale= Math.round(100*(sumFemale/(float)(sumMale + sumFemale)));
                html = html + "<li>Male: " + proportionMale + "% </li>";
                html = html + "<li>Female: " + proportionFemale + "% </li>";
                html = html + "</ul>";

                //Disease:
                html = html + "<h4>Disease:</h4>";
                html = html + "<p>Total number of Arthritis Patient: " + sumArthritis + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleArthritis + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleArthritis/sumArthritis)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleArthritis +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleArthritis/sumArthritis)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleArthritis + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleArthritis/sumArthritis)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleArthritis + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleArthritis/sumArthritis)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleArthritis+sumNonIndigenousFemaleArthritis-sumIndigenousMaleArthritis-sumIndigenousFemaleArthritis))/sumArthritis)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Asthma Patient: " + sumAsthma + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleAsthma + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleAsthma/sumAsthma)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleAsthma +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleAsthma/sumAsthma)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleAsthma + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleAsthma/sumAsthma)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleAsthma + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleAsthma/sumAsthma)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleAsthma+sumNonIndigenousFemaleAsthma-sumIndigenousMaleAsthma-sumIndigenousFemaleAsthma))/sumAsthma)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Cancer Patient: " + sumCancer + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleCancer + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleCancer/sumCancer)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleCancer +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleCancer/sumCancer)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleCancer + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleCancer/sumCancer)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleCancer + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleCancer/sumCancer)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleCancer+sumNonIndigenousFemaleCancer-sumIndigenousMaleCancer-sumIndigenousFemaleCancer))/sumCancer)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Dementia Patient: " + sumDementia + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleDementia + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleDementia/sumDementia)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleDementia +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleDementia/sumDementia)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleDementia + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleDementia/sumDementia)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleDementia + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleDementia/sumDementia)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleDementia+sumNonIndigenousFemaleDementia-sumIndigenousMaleDementia-sumIndigenousFemaleDementia))/sumDementia)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Diabetes Patient: " + sumDiabetes + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleDiabetes + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleDiabetes/sumDiabetes)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleDiabetes +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleDiabetes/sumDiabetes)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleDiabetes + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleDiabetes/sumDiabetes)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleDiabetes + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleDiabetes/sumDiabetes)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleDiabetes+sumNonIndigenousFemaleDiabetes-sumIndigenousMaleDiabetes-sumIndigenousFemaleDiabetes))/sumDiabetes)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Heart Disease Patient: " + sumHeart_disease + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleHeart_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleHeart_disease/sumHeart_disease)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleHeart_disease +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleHeart_disease/sumHeart_disease)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleHeart_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleHeart_disease/sumHeart_disease)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleHeart_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleHeart_disease/sumHeart_disease)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleHeart_disease+sumNonIndigenousFemaleHeart_disease-sumIndigenousMaleHeart_disease-sumIndigenousFemaleHeart_disease))/sumHeart_disease)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Kidney Disease Patient: " + sumKidney_disease + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleKidney_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleKidney_disease/sumKidney_disease)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleKidney_disease +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleKidney_disease/sumKidney_disease)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleKidney_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleKidney_disease/sumKidney_disease)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleKidney_disease + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleKidney_disease/sumKidney_disease)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleKidney_disease+sumNonIndigenousFemaleKidney_disease-sumIndigenousMaleHeart_disease-sumIndigenousFemaleKidney_disease))/sumKidney_disease)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Lung Condition Patient: " + sumLung_condition + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleLung_condition + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleLung_condition/sumLung_condition)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleLung_condition +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleLung_condition/sumLung_condition)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleLung_condition + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleLung_condition/sumLung_condition)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleLung_condition + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleLung_condition/sumLung_condition)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleLung_condition+sumNonIndigenousFemaleLung_condition-sumIndigenousMaleLung_condition-sumIndigenousFemaleLung_condition))/sumLung_condition)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Mental Health Patient: " + sumMental_health + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleMental_health + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleMental_health/sumMental_health)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleMental_health +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleMental_health/sumMental_health)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleMental_health + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleMental_health/sumMental_health)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleMental_health + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleMental_health/sumMental_health)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleMental_health+sumNonIndigenousFemaleMental_health-sumIndigenousMaleMental_health-sumIndigenousFemaleMental_health))/sumMental_health)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Stroke Patient: " + sumStroke + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleStroke + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleStroke/sumStroke)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleStroke +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleStroke/sumStroke)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleStroke + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleStroke/sumStroke)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleStroke + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleStroke/sumStroke)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleStroke+sumNonIndigenousFemaleStroke-sumIndigenousMaleStroke-sumIndigenousFemaleStroke))/sumStroke)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Other Disease Patient: " + sumOther + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaleOther + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaleOther/sumOther)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaleOther +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaleOther/sumOther)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaleOther + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaleOther/sumOther)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaleOther + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaleOther/sumOther)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaleOther+sumNonIndigenousFemaleOther-sumIndigenousMaleOther-sumIndigenousFemaleOther))/sumOther)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                //Proportion of Disease across surveyed LGA
                html = html + "<p>Proportion of Disease: </p>";
                html = html + "<ul>";
                
                int sumDisease= sumArthritis + sumAsthma + sumCancer + sumDementia + sumDiabetes + sumHeart_disease + sumKidney_disease + sumLung_condition + sumMental_health
                + sumStroke + sumOther;
                String  propArthritis = (String) roundOffTo2DecPlaces(((float) sumArthritis/sumDisease)*100);
                String  propAsthma = (String) roundOffTo2DecPlaces(((float) sumAsthma/sumDisease)*100);
                String  propCancer = (String) roundOffTo2DecPlaces(((float) sumCancer/sumDisease)*100);
                String  propDementia = (String) roundOffTo2DecPlaces(((float) sumDementia/sumDisease)*100);
                String  propDiabetes = (String) roundOffTo2DecPlaces(((float) sumDiabetes/sumDisease)*100);
                String  propHeart_disease = (String) roundOffTo2DecPlaces(((float) sumHeart_disease/sumDisease)*100);
                String  propKidney_disease = (String) roundOffTo2DecPlaces(((float) sumKidney_disease/sumDisease)*100);
                String  propLung_condition = (String) roundOffTo2DecPlaces(((float) sumLung_condition/sumDisease)*100);
                String  propMental_health = (String) roundOffTo2DecPlaces(((float) sumMental_health/sumDisease)*100);
                String  propStroke = (String) roundOffTo2DecPlaces(((float) sumStroke/sumDisease)*100);
                String  propOther = (String) roundOffTo2DecPlaces(((float) sumOther/sumDisease)*100);
                
                html = html + "<li>Arthritis: " + propArthritis + "% </li>";
                html = html + "<li>Asthma: " + propAsthma + "% </li>";
                html = html + "<li>Cancer: " + propCancer + "% </li>";
                html = html + "<li>Dementia: " + propDementia + "% </li>";
                html = html + "<li>Diabetes: " + propDiabetes + "% </li>";
                html = html + "<li>Heart Disease: " + propHeart_disease + "% </li>";
                html = html + "<li>Kidney Disease: " + propKidney_disease + "% </li>";
                html = html + "<li>Lung Condition: " + propLung_condition + "% </li>";
                html = html + "<li>Mental Health: " + propMental_health + "% </li>";
                html = html + "<li>Stroke: " + propStroke + "% </li>";
                html = html + "<li>Other Disease: " + propOther + "% </li>";

                html = html + "</ul>";
                html = html + "</div>";
        
            }
            
            if (school_sort_drop != null){
                ArrayList<SchoolDataset2021> school_dataset_2021 = jdbc.sortSchoolDataset2021(school_sort_drop);

                html = html +"<h1 class='outcome_title'>Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential</h1>";

                html = html + "<h3 class='dataset_title'>Dataset: Highest Year of School Completed by Indigenous Status by Sex</h3>";


                switch(school_sort_drop) {
                    case " ORDER BY Lga.lgacode ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Ascending)</p>";
                        break;
                    case " ORDER BY Lga.lgacode DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Descending)</p>";
                        break;
                    case " ORDER BY lganame ASC":
                        html = html + "<p class='sort_by_desc'Sort By: LGA Name (Ascending)</p>";
                        break;
                    case " ORDER BY lganame DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Name (Descending)</p>";
                        break;
                    case "school_year_ascending":
                        html = html + "<p class='sort_by_desc'>Sort By: Highest School Year Completed (Ascending)</p>";
                        break;
                    case "school_year_descending":
                        html = html + "<p class='sort_by_desc'>Sort By: Highest School Year Completed (Descending)</p>";
                        break;
                    case " ORDER BY indigenous_status ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Indigenous)</p>";
                        break;
                    case " ORDER BY indigenous_status DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Non-Indigenous)</p>";
                        break;
                    case " ORDER BY gender ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Female)</p>";
                        break;
                    case " ORDER BY gender DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Gender (Male)</p>";
                        break;
                    case " ORDER BY number_of_people ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Ascending)</p>";
                        break;
                    case " ORDER BY number_of_people DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of People (Descending)</p>";
                        break;
                }


                html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Highest School Year Completed</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Number of People</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                int sumIndigenous = 0;
                int sumNonIndigenous = 0;
                int sumMale= 0;
                int sumFemale= 0;
                int sumNeverAttend = 0;
                int sumY8below = 0;
                int sumY9 = 0;
                int sumY10 = 0;
                int sumY11 = 0;
                int sumY12 = 0;

                int sumIndigenousMaledid_not = 0;
                int sumIndigenousMaley8_below = 0;
                int sumIndigenousMaley9_equivalent = 0;
                int sumIndigenousMaley10_equivalent = 0;
                int sumIndigenousMaley11_equivalent = 0;
                int sumIndigenousMaley12_equivalent = 0;

                int sumIndigenousFemaledid_not = 0;
                int sumIndigenousFemaley8_below = 0;
                int sumIndigenousFemaley9_equivalent = 0;
                int sumIndigenousFemaley10_equivalent = 0;
                int sumIndigenousFemaley11_equivalent = 0;
                int sumIndigenousFemaley12_equivalent = 0;

                int sumNonIndigenousMaledid_not = 0;
                int sumNonIndigenousMaley8_below = 0;
                int sumNonIndigenousMaley9_equivalent = 0;
                int sumNonIndigenousMaley10_equivalent = 0;
                int sumNonIndigenousMaley11_equivalent = 0;
                int sumNonIndigenousMaley12_equivalent = 0;

                int sumNonIndigenousFemaledid_not = 0;
                int sumNonIndigenousFemaley8_below = 0;
                int sumNonIndigenousFemaley9_equivalent = 0;
                int sumNonIndigenousFemaley10_equivalent = 0;
                int sumNonIndigenousFemaley11_equivalent = 0;
                int sumNonIndigenousFemaley12_equivalent = 0;
                
                

                for (SchoolDataset2021 data : school_dataset_2021) {
                    html = html + "<tr>";
                    html = html + "     <td>" + data.getLgaCode() + "</td>";
                    html = html + "     <td>" + data.getLgaName() + "</td>";
                    html = html + "     <td>" + data.getHighestSchoolYearCompleted() + "</td>";
                    html = html + "     <td>" + data.getStatus() + "</td>";
                    html = html + "     <td>" + data.getGender() + "</td>";
                    html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                    html = html + "</tr>";

                    if (data.getStatus().equals("indigenous")) {
                        sumIndigenous += data.getNumberOfPeople();

                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getHighestSchoolYearCompleted().equals("did_not_go_to_school")) {
                                sumNeverAttend += data.getNumberOfPeople();
                                sumIndigenousMaledid_not += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y8_below")) {
                                sumY8below += data.getNumberOfPeople();
                                sumIndigenousMaley8_below += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y9_equivalent")) {
                                sumY9 += data.getNumberOfPeople();
                                sumIndigenousMaley9_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y10_equivalent")) {
                                sumY10 += data.getNumberOfPeople();
                                sumIndigenousMaley10_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y11_equivalent")) {
                                sumY11 += data.getNumberOfPeople();
                                sumIndigenousMaley11_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y12_equivalent")) {
                                sumY12 += data.getNumberOfPeople();
                                sumIndigenousMaley12_equivalent += data.getNumberOfPeople();
                            }   
                        }

                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getHighestSchoolYearCompleted().equals("did_not_go_to_school")) {
                                sumNeverAttend += data.getNumberOfPeople();
                                sumIndigenousFemaledid_not += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y8_below")) {
                                sumY8below += data.getNumberOfPeople();
                                sumIndigenousFemaley8_below += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y9_equivalent")) {
                                sumY9 += data.getNumberOfPeople();
                                sumIndigenousFemaley9_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y10_equivalent")) {
                                sumY10 += data.getNumberOfPeople();
                                sumIndigenousFemaley10_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y11_equivalent")) {
                                sumY11 += data.getNumberOfPeople();
                                sumIndigenousFemaley11_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y12_equivalent")) {
                                sumY12 += data.getNumberOfPeople();
                                sumIndigenousFemaley12_equivalent += data.getNumberOfPeople();
                            }
                            
                        }
                    }

                    else if (data.getStatus().equals("non_indigenous")) {
                        sumNonIndigenous += data.getNumberOfPeople();

                        if (Character.compare(data.getGender(), 'm') == 0) {
                            sumMale += data.getNumberOfPeople();
                            if (data.getHighestSchoolYearCompleted().equals("did_not_go_to_school")) {
                                sumNeverAttend += data.getNumberOfPeople();
                                sumNonIndigenousMaledid_not += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y8_below")) {
                                sumY8below += data.getNumberOfPeople();
                                sumNonIndigenousMaley8_below += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y9_equivalent")) {
                                sumY9 += data.getNumberOfPeople();
                                sumNonIndigenousMaley9_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y10_equivalent")) {
                                sumY10 += data.getNumberOfPeople();
                                sumNonIndigenousMaley10_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y11_equivalent")) {
                                sumY11 += data.getNumberOfPeople();
                                sumNonIndigenousMaley11_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y12_equivalent")) {
                                sumY12 += data.getNumberOfPeople();
                                sumNonIndigenousMaley12_equivalent += data.getNumberOfPeople();
                            }   
                        }

                        else if (Character.compare(data.getGender(), 'f') == 0) {
                            sumFemale += data.getNumberOfPeople();
                            if (data.getHighestSchoolYearCompleted().equals("did_not_go_to_school")) {
                                sumNeverAttend += data.getNumberOfPeople();
                                sumNonIndigenousFemaledid_not += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y8_below")) {
                                sumY8below += data.getNumberOfPeople();
                                sumNonIndigenousFemaley8_below += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y9_equivalent")) {
                                sumY9 += data.getNumberOfPeople();
                                sumNonIndigenousFemaley9_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y10_equivalent")) {
                                sumY10 += data.getNumberOfPeople();
                                sumNonIndigenousFemaley10_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y11_equivalent")) {
                                sumY11 += data.getNumberOfPeople();
                                sumNonIndigenousFemaley11_equivalent += data.getNumberOfPeople();
                            }
                            else if (data.getHighestSchoolYearCompleted().equals("y12_equivalent")) {
                                sumY12 += data.getNumberOfPeople();
                                sumNonIndigenousFemaley12_equivalent += data.getNumberOfPeople();
                            }
                            
                        }
                    }
                    
                }

                html = html + """
                            </tbody>
                        </table>
                    </div>
                """;

                html = html + "<div class='data_insight'>";

                html = html + "     <h3>Data Insights (across surveyed LGA)</h3>";

                //Indigenous Status:
                html = html + "<h4>Indigenous Status:</h4>";
                //Total number of Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Indigenous Population: " + sumIndigenous + "</p>";
                //Total number of Non-Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Non-Indigenous Population: " + sumNonIndigenous + "</p>";
                //Proportion of Inidgenous Status across surveyed LGA
                html = html + "<p>Proportion of Inidgenous Status: </p>";
                html = html + "<ul>";
                //Indigenous : Non-Indigenous
                int proportionIndigenous = Math.round(100*(sumIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                int proportionNonIndigenous = Math.round(100*(sumNonIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                html = html + "<li>Indigenous: " + proportionIndigenous + "% </li>";
                html = html + "<li>Non-Indigenous: " + proportionNonIndigenous + "% </li>";
                //The Gap:
                html = html + "<li>The Gap: " + Math.abs(proportionNonIndigenous - proportionIndigenous) + "% Differences</li>";
                html = html + "</ul>";


                //Gender:
                html = html + "<h4>Gender:</h4>";
                //Total number of Male Population across surveyed LGA
                html = html + "<p>Total number of Male Population: " + sumMale + "</p>";
                //Total number of Female Population across surveyed LGA
                html = html + "<p>Total number of Female Population: " + sumFemale + "</p>";
                //Proportion of Gender across surveyed LGA
                html = html + "<p>Proportion of Gender: </p>";
                html = html + "<ul>";
                //Male:Female
                int proportionMale = Math.round(100*(sumMale/(float)(sumMale + sumFemale)));
                int proportionFemale= Math.round(100*(sumFemale/(float)(sumMale + sumFemale)));
                html = html + "<li>Male: " + proportionMale + "% </li>";
                html = html + "<li>Female: " + proportionFemale + "% </li>";
                html = html + "</ul>";

                //Highest School Year Completed:
                html = html + "<h4>Highest School Year Completed:</h4>";
                html = html + "<p>Total number of People did not attend School: " + sumNeverAttend + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaledid_not + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaledid_not/sumNeverAttend)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaledid_not +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaledid_not/sumNeverAttend)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaledid_not + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaledid_not/sumNeverAttend)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaledid_not + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaledid_not/sumNeverAttend)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaledid_not+sumNonIndigenousFemaledid_not-sumIndigenousMaledid_not-sumIndigenousFemaledid_not))/sumNeverAttend)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of People Completed Year 8 or Below: " + sumY8below + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaley8_below + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaley8_below/sumY8below)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaley8_below +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaley8_below/sumY8below)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaley8_below + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaley8_below/sumY8below)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaley8_below + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaley8_below/sumY8below)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaley8_below+sumNonIndigenousFemaley8_below-sumIndigenousMaley8_below-sumIndigenousFemaley8_below))/sumY8below)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of People Completed Year 9 or Equivalent: " + sumY9 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaley9_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaley9_equivalent/sumY9)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaley9_equivalent +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaley9_equivalent/sumY9)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaley9_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaley9_equivalent/sumY9)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaley9_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaley9_equivalent/sumY9)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaley9_equivalent+sumNonIndigenousFemaley9_equivalent-sumIndigenousMaley9_equivalent-sumIndigenousFemaley9_equivalent))/sumY9)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of People Completed Year 10 or Equivalent: " + sumY10 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaley10_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaley10_equivalent/sumY10)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaley10_equivalent +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaley10_equivalent/sumY10)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaley10_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaley10_equivalent/sumY10)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaley10_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaley10_equivalent/sumY10)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaley10_equivalent+sumNonIndigenousFemaley10_equivalent-sumIndigenousMaley10_equivalent-sumIndigenousFemaley10_equivalent))/sumY10)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of People Completed Year 11 or Equivalent: " + sumY11 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaley11_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaley11_equivalent/sumY11)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaley11_equivalent +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaley11_equivalent/sumY11)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaley11_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaley11_equivalent/sumY11)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaley11_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaley11_equivalent/sumY11)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaley11_equivalent+sumNonIndigenousFemaley11_equivalent-sumIndigenousMaley11_equivalent-sumIndigenousFemaley11_equivalent))/sumY11)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of People Completed Year 12 or Equivalent: " + sumY12 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous Male: " + sumIndigenousMaley12_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousMaley12_equivalent/sumY12)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Male: " + sumNonIndigenousMaley12_equivalent +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousMaley12_equivalent/sumY12)*100) +"%)</li>";
                html = html + "         <li>Indigenous Female: " + sumIndigenousFemaley12_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenousFemaley12_equivalent/sumY12)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous Female: " + sumNonIndigenousFemaley12_equivalent + " (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenousFemaley12_equivalent/sumY12)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenousMaley12_equivalent+sumNonIndigenousFemaley12_equivalent-sumIndigenousMaley12_equivalent-sumIndigenousFemaley12_equivalent))/sumY12)) * 100) +"% Difference</li>";
                html = html + "     </ul>";

                //Proportion of Disease across surveyed LGA
                html = html + "<p>Proportion of Highest School Year Completed: </p>";
                html = html + "<ul>";
                
                int sumSchoolYear = sumNeverAttend + sumY8below + sumY9 + sumY10 + sumY11 + sumY12;
                String  propNeverAttend = (String) roundOffTo2DecPlaces(((float) sumNeverAttend/sumSchoolYear)*100);
                String  propY8below = (String) roundOffTo2DecPlaces(((float) sumY8below/sumSchoolYear)*100);
                String  propY9 = (String) roundOffTo2DecPlaces(((float) sumY9/sumSchoolYear)*100);
                String  propY10 = (String) roundOffTo2DecPlaces(((float) sumY10/sumSchoolYear)*100);
                String  propY11 = (String) roundOffTo2DecPlaces(((float) sumY11/sumSchoolYear)*100);
                String  propY12 = (String) roundOffTo2DecPlaces(((float) sumY12/sumSchoolYear)*100);
                
                html = html + "<li>Never Attend School: " + propNeverAttend + "% </li>";
                html = html + "<li>Year 8 or Below: " + propY8below + "% </li>";
                html = html + "<li>Year 9 or Equivalent: " + propY9 + "% </li>";
                html = html + "<li>Year 10 or Equivalent: " + propY10 + "% </li>";
                html = html + "<li>Year 11 or Equivalent: " + propY11 + "% </li>";
                html = html + "<li>Year 12 or Equivalent: " + propY12 + "% </li>";

                html = html + "</ul>";
                html = html + "</div>";
        
            }
            
            if (household_sort_drop != null){
                ArrayList<HouseholdDataset2021> household_dataset_2021 = jdbc.sortHouseholdDataset2021(household_sort_drop);

                html = html +"<h1 class='outcome_title'>Outcome 8: Strong economic participation and development of Aboriginal and Torres Strait Islander people and communities</h1>";

                html = html + "<h3 class='dataset_title'>Dataset: Household Weekly Income by Indigenous Status of Household</h3>";


                switch(household_sort_drop) {
                    case " ORDER BY Lga.lgacode ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Ascending)</p>";
                        break;
                    case " ORDER BY Lga.lgacode DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Code (Descending)</p>";
                        break;
                    case " ORDER BY lganame ASC":
                        html = html + "<p class='sort_by_desc'Sort By: LGA Name (Ascending)</p>";
                        break;
                    case " ORDER BY lganame DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: LGA Name (Descending)</p>";
                        break;
                    case "income_range_ascending":
                        html = html + "<p class='sort_by_desc'>Sort By: Household Weekly Income Range (Ascending)</p>";
                        break;
                    case "income_range_descending":
                        html = html + "<p class='sort_by_desc'>Sort By: Household Weekly Income Range (Descending)</p>";
                        break;
                    case " ORDER BY indigenous_status ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Indigenous)</p>";
                        break;
                    case " ORDER BY indigenous_status DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Status (Non-Indigenous)</p>";
                        break;
                    case " ORDER BY number_of_household ASC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of Household (Ascending)</p>";
                        break;
                    case " ORDER BY number_of_household DESC":
                        html = html + "<p class='sort_by_desc'>Sort By: Number of Household (Descending)</p>";
                        break;
                }


                html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Household Weekly Income Range</th>
                                    <th>Indigenous Status</th>
                                    <th>Number of Household</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                int sumIndigenous = 0;
                int sumNonIndigenous = 0;
                int sum1_149 = 0;
                int sum150_299 = 0;
                int sum300_399 = 0;
                int sum400_499 = 0;
                int sum500_649 = 0;
                int sum650_799 = 0;
                int sum800_999 = 0;
                int sum1000_1249 = 0;
                int sum1250_1499 = 0;
                int sum1500_1749 = 0;
                int sum1750_1999 = 0;
                int sum2000_2499 = 0;
                int sum2500_2999 = 0;
                int sum3000_3499 = 0;
                int sum3500_more = 0;

                int sumIndigenous1_149 = 0;
                int sumIndigenous150_299 = 0;
                int sumIndigenous300_399 = 0;
                int sumIndigenous400_499 = 0;
                int sumIndigenous500_649 = 0;
                int sumIndigenous650_799 = 0;
                int sumIndigenous800_999 = 0;
                int sumIndigenous1000_1249 = 0;
                int sumIndigenous1250_1499 = 0;
                int sumIndigenous1500_1749 = 0;
                int sumIndigenous1750_1999 = 0;
                int sumIndigenous2000_2499 = 0;
                int sumIndigenous2500_2999 = 0;
                int sumIndigenous3000_3499 = 0;
                int sumIndigenous3500_more = 0;

                int sumNonIndigenous1_149 = 0;
                int sumNonIndigenous150_299 = 0;
                int sumNonIndigenous300_399 = 0;
                int sumNonIndigenous400_499 = 0;
                int sumNonIndigenous500_649 = 0;
                int sumNonIndigenous650_799 = 0;
                int sumNonIndigenous800_999 = 0;
                int sumNonIndigenous1000_1249 = 0;
                int sumNonIndigenous1250_1499 = 0;
                int sumNonIndigenous1500_1749 = 0;
                int sumNonIndigenous1750_1999 = 0;
                int sumNonIndigenous2000_2499 = 0;
                int sumNonIndigenous2500_2999 = 0;
                int sumNonIndigenous3000_3499 = 0;
                int sumNonIndigenous3500_more = 0;
                

                for (HouseholdDataset2021 data : household_dataset_2021) {
                    html = html + "<tr>";
                    html = html + "     <td>" + data.getLgaCode() + "</td>";
                    html = html + "     <td>" + data.getLgaName() + "</td>";
                    html = html + "     <td>" + data.getHouseholdWeeklyIncome() + "</td>";
                    html = html + "     <td>" + data.getStatus() + "</td>";
                    html = html + "     <td>" + data.getNumberOfHousehold() + "</td>";
                    html = html + "</tr>";


                    if (data.getStatus().equals("indigenous")) {
                        sumIndigenous += data.getNumberOfHousehold();

                        if (data.getHouseholdWeeklyIncome().equals("1_149")) {
                            sum1_149 += data.getNumberOfHousehold();
                            sumIndigenous1_149 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("150_299")) {
                            sum150_299 += data.getNumberOfHousehold();
                            sumIndigenous150_299 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("300_399")) {
                            sum300_399 += data.getNumberOfHousehold();
                            sumIndigenous300_399 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("400_499")) {
                            sum400_499 += data.getNumberOfHousehold();
                            sumIndigenous400_499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("500_649")) {
                            sum500_649 += data.getNumberOfHousehold();
                            sumIndigenous500_649 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("650_799")) {
                            sum650_799 += data.getNumberOfHousehold();
                            sumIndigenous650_799 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("800_999")) {
                            sum800_999 += data.getNumberOfHousehold();
                            sumIndigenous800_999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1000_1249")) {
                            sum1000_1249 += data.getNumberOfHousehold();
                            sumIndigenous1000_1249 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1250_1499")) {
                            sum1250_1499 += data.getNumberOfHousehold();
                            sumIndigenous1250_1499 += data.getNumberOfHousehold();
                        } 
                        else if (data.getHouseholdWeeklyIncome().equals("1500_1749")) {
                            sum1500_1749 += data.getNumberOfHousehold();
                            sumIndigenous1500_1749 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1750_1999")) {
                            sum1750_1999 += data.getNumberOfHousehold();
                            sumIndigenous1750_1999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("2000_2499")) {
                            sum2000_2499 += data.getNumberOfHousehold();
                            sumIndigenous2000_2499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("2500_2999")) {
                            sum2500_2999 += data.getNumberOfHousehold();
                            sumIndigenous2500_2999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("3000_3499")) {
                            sum3000_3499 += data.getNumberOfHousehold();
                            sumIndigenous3000_3499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("3500_more")) {
                            sum3500_more += data.getNumberOfHousehold();
                            sumIndigenous3500_more += data.getNumberOfHousehold();
                        }  
                    }

                    else if (data.getStatus().equals("non_indigenous")) {
                        sumNonIndigenous += data.getNumberOfHousehold();

                        if (data.getHouseholdWeeklyIncome().equals("1_149")) {
                            sum1_149 += data.getNumberOfHousehold();
                            sumNonIndigenous1_149 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("150_299")) {
                            sum150_299 += data.getNumberOfHousehold();
                            sumNonIndigenous150_299 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("300_399")) {
                            sum300_399 += data.getNumberOfHousehold();
                            sumNonIndigenous300_399 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("400_499")) {
                            sum400_499 += data.getNumberOfHousehold();
                            sumNonIndigenous400_499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("500_649")) {
                            sum500_649 += data.getNumberOfHousehold();
                            sumNonIndigenous500_649 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("650_799")) {
                            sum650_799 += data.getNumberOfHousehold();
                            sumNonIndigenous650_799 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("800_999")) {
                            sum800_999 += data.getNumberOfHousehold();
                            sumNonIndigenous800_999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1000_1249")) {
                            sum1000_1249 += data.getNumberOfHousehold();
                            sumNonIndigenous1000_1249 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1250_1499")) {
                            sum1250_1499 += data.getNumberOfHousehold();
                            sumNonIndigenous1250_1499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1500_1749")) {
                            sum1500_1749 += data.getNumberOfHousehold();
                            sumNonIndigenous1500_1749 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("1750_1999")) {
                            sum1750_1999 += data.getNumberOfHousehold();
                            sumNonIndigenous1750_1999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("2000_2499")) {
                            sum2000_2499 += data.getNumberOfHousehold();
                            sumNonIndigenous2000_2499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("2500_2999")) {
                            sum2500_2999 += data.getNumberOfHousehold();
                            sumNonIndigenous2500_2999 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("3000_3499")) {
                            sum3000_3499 += data.getNumberOfHousehold();
                            sumNonIndigenous3000_3499 += data.getNumberOfHousehold();
                        }
                        else if (data.getHouseholdWeeklyIncome().equals("3500_more")) {
                            sum3500_more += data.getNumberOfHousehold();
                            sumNonIndigenous3500_more += data.getNumberOfHousehold();
                        }  
                    }
                }

                html = html + """
                            </tbody>
                        </table>
                    </div>
                """;

                html = html + "<div class='data_insight'>";

                html = html + "     <h3>Data Insights (across surveyed LGA)</h3>";

                //Indigenous Status:
                html = html + "<h4>Indigenous Status:</h4>";
                //Total number of Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Indigenous Population: " + sumIndigenous + "</p>";
                //Total number of Non-Inidgenous Population across surveyed LGA
                html = html + "<p>Total number of Non-Indigenous Population: " + sumNonIndigenous + "</p>";
                //Proportion of Inidgenous Status across surveyed LGA
                html = html + "<p>Proportion of Inidigenous Status: </p>";
                html = html + "<ul>";
                //Indigenous : Non-Indigenous
                int proportionIndigenous = Math.round(100*(sumIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                int proportionNonIndigenous = Math.round(100*(sumNonIndigenous/(float)(sumIndigenous + sumNonIndigenous)));
                html = html + "<li>Indigenous: " + proportionIndigenous + "% </li>";
                html = html + "<li>Non-Indigenous: " + proportionNonIndigenous + "% </li>";
                //The Gap:
                html = html + "<li>The Gap: " + Math.abs(proportionNonIndigenous - proportionIndigenous) + "% Differences</li>";
                html = html + "</ul>";

                //Household Weekly Income Range:
                html = html + "<h4>Household Weekly Income Range:</h4>";
                html = html + "<p>Total number of Household with AUD 1 - 149 Weekly Income: " + sum1_149 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous1_149 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous1_149/sum1_149)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous1_149 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous1_149/sum1_149)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous1_149-sumIndigenous1_149))/sum1_149)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 150 - 299 Weekly Income: " + sum150_299 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous150_299 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous150_299/sum150_299)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous150_299 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous150_299/sum150_299)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous150_299-sumIndigenous150_299))/sum150_299)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 300 - 399 Weekly Income: " + sum300_399 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous300_399 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous300_399/sum300_399)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous300_399 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous300_399/sum300_399)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous300_399-sumIndigenous300_399))/sum300_399)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 400 - 499 Weekly Income: " + sum400_499 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous400_499 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous400_499/sum400_499)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous400_499 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous400_499/sum400_499)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous400_499-sumIndigenous400_499))/sum400_499)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 500 - 649 Weekly Income: " + sum500_649 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous500_649 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous500_649/sum500_649)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous500_649 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous500_649/sum500_649)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous500_649-sumIndigenous500_649))/sum500_649)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 650 - 799 Weekly Income: " + sum650_799 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous650_799 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous650_799/sum650_799)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous650_799 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous650_799/sum650_799)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous650_799-sumIndigenous650_799))/sum650_799)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 800 - 999 Weekly Income: " + sum800_999 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous800_999 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous800_999/sum800_999)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous800_999 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous800_999/sum800_999)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous800_999-sumIndigenous800_999))/sum800_999)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 1000 - 1249 Weekly Income: " + sum1000_1249 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous1000_1249 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous1000_1249/sum1000_1249)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous1000_1249 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous1000_1249/sum1000_1249)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous1000_1249-sumIndigenous1000_1249))/sum1000_1249)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 1250 - 1499 Weekly Income: " + sum1250_1499 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous1250_1499 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous1250_1499/sum1250_1499)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous1250_1499 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous1250_1499/sum1250_1499)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous1250_1499-sumIndigenous1250_1499))/sum1250_1499)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 1500 - 1749 Weekly Income: " + sum1500_1749 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous1500_1749 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous1500_1749/sum1500_1749)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous1500_1749 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous1500_1749/sum1500_1749)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous1500_1749-sumIndigenous1500_1749))/sum1500_1749)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 1750 - 1999 Weekly Income: " + sum1750_1999 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous1750_1999 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous1750_1999/sum1750_1999)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous1750_1999 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous1750_1999/sum1750_1999)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous1750_1999-sumIndigenous1750_1999))/sum1750_1999)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 2000 - 2499 Weekly Income: " + sum2000_2499 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous2000_2499 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous2000_2499/sum2000_2499)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous2000_2499 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous2000_2499/sum2000_2499)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous2000_2499-sumIndigenous2000_2499))/sum2000_2499)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 2500 - 2999 Weekly Income: " + sum2500_2999 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous2500_2999 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous2500_2999/sum2500_2999)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous2500_2999 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous2500_2999/sum2500_2999)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous2500_2999-sumIndigenous2500_2999))/sum2500_2999)) * 100) +"% Difference</li>";
                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 3000 - 3499 Weekly Income: " + sum3000_3499 + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous3000_3499 + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous3000_3499/sum3000_3499)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous3000_3499 +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous3000_3499/sum3000_3499)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous3000_3499-sumIndigenous3000_3499))/sum3000_3499)) * 100) +"% Difference</li>";

                html = html + "     </ul>";
                html = html + "<p>Total number of Household with AUD 3500 and more Weekly Income: " + sum3500_more + "</p>";
                html = html + "     <ul>";
                html = html + "         <li>Indigenous: " + sumIndigenous3500_more + " (" + (String) roundOffTo2DecPlaces(((float) sumIndigenous3500_more/sum3500_more)*100) +"%)</li>";
                html = html + "         <li>Non-Indigenous: " + sumNonIndigenous3500_more +" (" + (String) roundOffTo2DecPlaces(((float) sumNonIndigenous3500_more/sum3500_more)*100) +"%)</li>";
                html = html + "         <li>The Gap: " + (String) roundOffTo2DecPlaces( ( (((float) Math.abs(sumNonIndigenous3500_more-sumIndigenous3500_more))/sum3500_more)) * 100) +"% Difference</li>";
                html = html + "     </ul>";

                //Proportion of Household Weekly Income Range across surveyed LGA
                html = html + "<p>Proportion of Household Weekly Income Range: </p>";
                html = html + "<ul>";
                
                int sumIncomeRange = sum1_149 + sum150_299 + sum300_399 + sum400_499 + sum500_649 + sum650_799 + sum800_999 + sum1000_1249
                + sum1250_1499 + sum1500_1749 + sum1750_1999 + sum2000_2499 + sum2500_2999 + sum3000_3499 + sum3500_more;
                String  prop1_149 = (String) roundOffTo2DecPlaces(((float) sum1_149/sumIncomeRange)*100);
                String  prop150_299 = (String) roundOffTo2DecPlaces(((float) sum150_299/sumIncomeRange)*100);
                String  prop300_399 = (String) roundOffTo2DecPlaces(((float) sum300_399/sumIncomeRange)*100);
                String  prop400_499 = (String) roundOffTo2DecPlaces(((float) sum400_499/sumIncomeRange)*100);
                String  prop500_649 = (String) roundOffTo2DecPlaces(((float) sum500_649/sumIncomeRange)*100);
                String  prop650_799 = (String) roundOffTo2DecPlaces(((float) sum650_799/sumIncomeRange)*100);
                String  prop800_999 = (String) roundOffTo2DecPlaces(((float) sum800_999/sumIncomeRange)*100);
                String  prop1000_1249 = (String) roundOffTo2DecPlaces(((float) sum1000_1249/sumIncomeRange)*100);
                String  prop1250_1499 = (String) roundOffTo2DecPlaces(((float) sum1250_1499/sumIncomeRange)*100);
                String  prop1500_1749 = (String) roundOffTo2DecPlaces(((float) sum1500_1749/sumIncomeRange)*100);
                String  prop1750_1999 = (String) roundOffTo2DecPlaces(((float) sum1750_1999/sumIncomeRange)*100);
                String  prop2000_2499 = (String) roundOffTo2DecPlaces(((float) sum2000_2499/sumIncomeRange)*100);
                String  prop2500_2999 = (String) roundOffTo2DecPlaces(((float) sum2500_2999/sumIncomeRange)*100);
                String  prop3000_3499 = (String) roundOffTo2DecPlaces(((float) sum3000_3499/sumIncomeRange)*100);
                String  prop3000_more = (String) roundOffTo2DecPlaces(((float) sum3500_more/sumIncomeRange)*100);
                
                html = html + "<li>AUD 1 - 149: " + prop1_149 + "% </li>";
                html = html + "<li>AUD 150 - 299: " + prop150_299 + "% </li>";
                html = html + "<li>AUD 300 - 399: " + prop300_399 + "% </li>";
                html = html + "<li>AUD 400 - 499: " + prop400_499 + "% </li>";
                html = html + "<li>AUD 500 - 649: " + prop500_649 + "% </li>";
                html = html + "<li>AUD 650 - 799: " + prop650_799 + "% </li>";
                html = html + "<li>AUD 800 - 999: " + prop800_999 + "% </li>";
                html = html + "<li>AUD 1000 - 1249: " + prop1000_1249 + "% </li>";
                html = html + "<li>AUD 1250 - 1499: " + prop1250_1499 + "% </li>";
                html = html + "<li>AUD 1500 - 1749: " + prop1500_1749 + "% </li>";
                html = html + "<li>AUD 1750 - 1999: " + prop1750_1999 + "% </li>";
                html = html + "<li>AUD 2000 - 2499: " + prop2000_2499 + "% </li>";
                html = html + "<li>AUD 2500 - 2999: " + prop2500_2999 + "% </li>";
                html = html + "<li>AUD 3000 - 3499: " + prop3000_3499 + "% </li>";
                html = html + "<li>AUD 3500 and more: " + prop3000_more + "% </li>";

                html = html + "</ul>";
                html = html + "</div>";
        
            }

            html = html + "</section>";
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
            html = html + "</main>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
