package app;

import java.util.ArrayList;
import java.util.HashMap;

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
public class FindSimilarLga implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/find-similar-lga.html";

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
               "<title>Find Similar LGAs</title>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='similar.css' />";
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
                <a href='gap-scores.html'>GAP SCORES</a>
                <a href='find-similar-lga.html' class='current_page'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <section class = 'header-area'>
                    <div class='header-text'>
                        <h1>Find Similar LGAs</h1>
                        <p>Similarity of LGAs are based on the closeness of the number of people from other LGAs that satisfied your selection criteria</p>
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
        
        html = html + "<div id='display_filter'></div>";

        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> lgaNames = jdbc.getLGANames();

        html = html + """
            <script>

            function filterFunciton() {
                var x = document.getElementById('outcome_drop').value;

                if (x == 'dataset_demographic') {
                document.getElementById('display_filter').innerHTML = `
                
                <form action='/find-similar-lga.html' method='post' id='form_dataset_demographic_similar'>
                
                    <div class='form-group'>
                    <div>
                        <label for='lganame_drop'>Select LGA to Compare:</label>
                        <select id='lganame_drop' name='lganame_drop'>
                            <option value='' selected disabled hidden>Choose here</option>""";
                                
                    for(String name : lgaNames){
                            html = html + "<option value='" + name + "'>" + name + "</option>";
                    };

        html = html + """   
                        </select> 
                    </div>

                    <div>
                        <label for='year_drop'>Select Year:</label>
                        <select id='year_drop' name='year_drop'>  
                            <option value='' selected disabled hidden>Choose here</option>          
                            <option value='2016'>2016</option>
                            <option value='2021'>2021</option>
                        </select>

                        <label for='status_drop'>Select Status:</label>
                        <select id='status_drop' name='status_drop'>      
                            <option value='' selected disabled hidden>Choose here</option>        
                            <option value='indigenous'>Indigenous</option>
                            <option value='non_indigenous'>Non-Indigenous</option>
                            <option value='both'>Both</option>
                        </select>
    
                        <label for='gender_drop'>Select Gender:</label>
                        <select id='gender_drop' name='gender_drop'>  
                            <option value='' selected disabled hidden>Choose here</option>          
                            <option value='m'>Male</option>
                            <option value='f'>Female</option>
                        </select>
    
                        <label for='min_age_drop'>Select Minimum Age:</label>
                        <select id='min_age_drop' name='min_age_drop'>
                            <option value='999' selected disabled hidden>Choose here</option>                
                            <option value='0'>0</option>
                            <option value='1'>5</option>
                            <option value='2'>10</option>
                            <option value='3'>15</option>
                            <option value='4'>20</option>
                            <option value='5'>25</option>
                            <option value='6'>30</option>
                            <option value='7'>35</option>
                            <option value='8'>40</option>
                            <option value='9'>45</option>
                            <option value='10'>50</option>
                            <option value='11'>55</option>
                            <option value='12'>60</option>
                            <option value='13'>65</option>
                        </select>

                        <label for='max_age_drop'>Select Maximum Age:</label>
                        <select id='max_age_drop' name='max_age_drop'>
                            <option value='999' selected disabled hidden>Choose here</option>                
                            <option value='0'>4</option>
                            <option value='1'>9</option>
                            <option value='2'>14</option>
                            <option value='3'>19</option>
                            <option value='4'>24</option>
                            <option value='5'>29</option>
                            <option value='6'>34</option>
                            <option value='7'>39</option>
                            <option value='8'>44</option>
                            <option value='9'>49</option>
                            <option value='10'>54</option>
                            <option value='11'>59</option>
                            <option value='12'>64</option>
                            <option value='13'>above 65</option>
                        </select>
                    </div>

                    <div>
                        <label for='similarity_drop'>Order Results By:</label>
                        <select id='similarity_drop' name='similarity_drop'>
                            <option value='' selected disabled hidden>Choose here</option>                
                            <option value='ASC'>Most Similar (Increase)</option>
                            <option value='DESC'>Least Similar (Decrease)</option>
                        </select>

                        <label for='number_result_textbox'>Number of Result to Show:</label>               
                            <input class='form-control' id='number_result_textbox' name='number_result_textbox' placeholder='Starting from 1'>
                    <div>
                    </div>
                    <input type='reset' value='Clear All Selection'>
                    <button type='submit' class='btn btn-primary' form='form_dataset_demographic_similar'>Submit</button>

                </form>
                
                `;
                }

                else if (x == 'dataset_health') {
                    document.getElementById('display_filter').innerHTML = `
                    
                    <form action='/find-similar-lga.html' method='post' id='form_dataset_health_similar'>
                    
                        <div class='form-group'>
                        <div>
                            <label for='lganame_drop'>Select LGA to Compare:</label>
                            <select id='lganame_drop' name='lganame_drop'>
                                <option value='' selected disabled hidden>Choose here</option>""";
                                    
                        for(String name : lgaNames){
                                html = html + "<option value='" + name + "'>" + name + "</option>";
                        };
    
            html = html + """   
                            </select> 
                        </div>
    
                        <div>
                            <label for='year_drop'>Select Year:</label>
                            <select id='year_drop' name='year_drop'>  
                                <option value='' selected disabled hidden>Choose here</option>          
                                <option value='2016'>2016</option>
                                <option value='2021'>2021</option>
                            </select>
    
                            <label for='status_drop'>Select Status:</label>
                            <select id='status_drop' name='status_drop'>      
                                <option value='' selected disabled hidden>Choose here</option>        
                                <option value='indigenous'>Indigenous</option>
                                <option value='non_indigenous'>Non-Indigenous</option>
                                <option value='both'>Both</option>
                            </select>
        
                            <label for='gender_drop'>Select Gender:</label>
                            <select id='gender_drop' name='gender_drop'>  
                                <option value='' selected disabled hidden>Choose here</option>          
                                <option value='m'>Male</option>
                                <option value='f'>Female</option>
                            </select>
        
                            <label for='disease_drop'>Select Disease:</label>
                            <select id='disease_drop' name='disease_drop'>
                                <option value='' selected disabled hidden>Choose here</option>                
                                <option value='arthritis'>Arthritis</option>
                                <option value='asthma'>Asthma</option>
                                <option value='cancer'>Cancer</option>
                                <option value='dementia'>Dementia</option>
                                <option value='diabetes'>Diabetes</option>
                                <option value='heart_disease'>Heart Disease</option>
                                <option value='kidney_disease'>Kidney Disease</option>
                                <option value='lung_condition'>Lung Condition</option>
                                <option value='mental_health'>Mental Health</option>
                                <option value='stroke'>Stroke</option>
                                <option value='other'>Other</option>
                            </select>
    
                        </div>
    
                        <div>
                            <label for='similarity_drop'>Order Results By:</label>
                            <select id='similarity_drop' name='similarity_drop'>
                                <option value='' selected disabled hidden>Choose here</option>                
                                <option value='ASC'>Most Similar (Increase)</option>
                                <option value='DESC'>Least Similar (Decrease)</option>
                            </select>
    
                            <label for='number_result_textbox'>Number of Result to Show:</label>               
                                <input class='form-control' id='number_result_textbox' name='number_result_textbox' placeholder='Starting from 1'>
                        <div>
                        </div>
                        <input type='reset' value='Clear All Selection'>
                        <button type='submit' class='btn btn-primary' form='form_dataset_health_similar'>Submit</button>
    
                    </form>
                    
                    `;
                    }

                    else if (x == 'dataset_school') {
                        document.getElementById('display_filter').innerHTML = `
                        
                        <form action='/find-similar-lga.html' method='post' id='form_dataset_school_similar'>
                        
                            <div class='form-group'>
                            <div>
                                <label for='lganame_drop'>Select LGA to Compare:</label>
                                <select id='lganame_drop' name='lganame_drop'>
                                    <option value='' selected disabled hidden>Choose here</option>""";
                                        
                            for(String name : lgaNames){
                                    html = html + "<option value='" + name + "'>" + name + "</option>";
                            };
        
                html = html + """   
                                </select> 
                            </div>
        
                            <div>
                                <label for='year_drop'>Select Year:</label>
                                <select id='year_drop' name='year_drop'>  
                                    <option value='' selected disabled hidden>Choose here</option>          
                                    <option value='2016'>2016</option>
                                    <option value='2021'>2021</option>
                                </select>
        
                                <label for='status_drop'>Select Status:</label>
                                <select id='status_drop' name='status_drop'>      
                                    <option value='' selected disabled hidden>Choose here</option>        
                                    <option value='indigenous'>Indigenous</option>
                                    <option value='non_indigenous'>Non-Indigenous</option>
                                    <option value='both'>Both</option>
                                </select>
            
                                <label for='gender_drop'>Select Gender:</label>
                                <select id='gender_drop' name='gender_drop'>  
                                    <option value='' selected disabled hidden>Choose here</option>          
                                    <option value='m'>Male</option>
                                    <option value='f'>Female</option>
                                </select>
            
                                <label for='min_year_drop'>Select Minimum Year:</label>
                                <select id='min_year_drop' name='min_year_drop'>
                                    <option value='999' selected disabled hidden>Choose here</option>                
                                    <option value='0'>Did not attend school</option>
                                    <option value='1'>Year 8 and below</option>
                                    <option value='2'>Year 9 equivalent</option>
                                    <option value='3'>Year 10 equivalent</option>
                                    <option value='4'>Year 11 equivalent</option>
                                    <option value='5'>Year 12 equivalent</option>
                                </select>

                                <label for='max_year_drop'>Select Maximum Year:</label>
                                <select id='max_year_drop' name='max_year_drop'>
                                    <option value='999' selected disabled hidden>Choose here</option>                
                                    <option value='0'>Did not attend school</option>
                                    <option value='1'>Year 8 and below</option>
                                    <option value='2'>Year 9 equivalent</option>
                                    <option value='3'>Year 10 equivalent</option>
                                    <option value='4'>Year 11 equivalent</option>
                                    <option value='5'>Year 12 equivalent</option>
                                </select>
                
                            </div>
        
                            <div>
                                <label for='similarity_drop'>Order Results By:</label>
                                <select id='similarity_drop' name='similarity_drop'>
                                    <option value='' selected disabled hidden>Choose here</option>                
                                    <option value='ASC'>Most Similar (Increase)</option>
                                    <option value='DESC'>Least Similar (Decrease)</option>
                                </select>
        
                                <label for='number_result_textbox'>Number of Result to Show:</label>               
                                    <input class='form-control' id='number_result_textbox' name='number_result_textbox' placeholder='Starting from 1'>
                            <div>
                            </div>
                            <input type='reset' value='Clear All Selection'>
                            <button type='submit' class='btn btn-primary' form='form_dataset_school_similar'>Submit</button>
        
                        </form>
                        
                        `;
                    }

                    else if (x == 'dataset_household') {
                        document.getElementById('display_filter').innerHTML = `
                        
                        <form action='/find-similar-lga.html' method='post' id='form_dataset_household_similar'>
                        
                            <div class='form-group'>
                            <div>
                                <label for='lganame_drop'>Select LGA to Compare:</label>
                                <select id='lganame_drop' name='lganame_drop'>
                                    <option value='' selected disabled hidden>Choose here</option>""";
                                        
                            for(String name : lgaNames){
                                    html = html + "<option value='" + name + "'>" + name + "</option>";
                            };
        
                html = html + """   
                                </select> 
                            </div>
        
                            <div>
                                <label for='year_drop'>Select Year:</label>
                                <select id='year_drop' name='year_drop'>  
                                    <option value='' selected disabled hidden>Choose here</option>          
                                    <option value='2016'>2016</option>
                                    <option value='2021'>2021</option>
                                </select>
        
                                <label for='status_drop'>Select Status:</label>
                                <select id='status_drop' name='status_drop'>      
                                    <option value='' selected disabled hidden>Choose here</option>        
                                    <option value='indigenous'>Indigenous</option>
                                    <option value='non_indigenous'>Non-Indigenous</option>
                                    <option value='both'>Both</option>
                                </select>
            
                                <label for='min_income_drop'>Select Minimum Income:</label>
                                <select id='min_income_drop' name='min_income_drop'>
                                    <option value='999' selected disabled hidden>Choose here</option>                
                                    <option value='0'>1</option>
                                    <option value='1'>150</option>
                                    <option value='2'>300</option>
                                    <option value='3'>400</option>
                                    <option value='4'>500</option>
                                    <option value='5'>650</option>
                                    <option value='6'>800</option>
                                    <option value='7'>1000</option>
                                    <option value='8'>1250</option>
                                    <option value='9'>1500</option>
                                    <option value='10'>2000</option>
                                    <option value='11'>2500</option>
                                    <option value='12'>3000</option>
                                </select>
        
                                <label for='max_income_drop'>Select Maximum Income:</label>
                                <select id='max_income_drop' name='max_income_drop'>
                                    <option value='999' selected disabled hidden>Choose here</option>                
                                    <option value='0'>149</option>
                                    <option value='1'>299</option>
                                    <option value='2'>399</option>
                                    <option value='3'>499</option>
                                    <option value='4'>649</option>
                                    <option value='5'>799</option>
                                    <option value='6'>999</option>
                                    <option value='7'>1249</option>
                                    <option value='8'>1499</option>
                                    <option value='9'>1999</option>
                                    <option value='10'>2499</option>
                                    <option value='11'>2999</option>
                                    <option value='12'>above 3000</option>
                                </select>
                            </div>
        
                            <div>
                                <label for='similarity_drop'>Order Results By:</label>
                                <select id='similarity_drop' name='similarity_drop'>
                                    <option value='' selected disabled hidden>Choose here</option>                
                                    <option value='ASC'>Most Similar (Increase)</option>
                                    <option value='DESC'>Least Similar (Decrease)</option>
                                </select>
        
                                <label for='number_result_textbox'>Number of Result to Show:</label>               
                                    <input class='form-control' id='number_result_textbox' name='number_result_textbox' placeholder='Starting from 1'>
                            <div>
                            </div>
                            <input type='reset' value='Clear All Selection'>
                            <button type='submit' class='btn btn-primary' form='form_dataset_household_similar'>Submit</button>
        
                        </form>
                        
                        `;
                    }
            }
            </script>
                """;

        String lganame_drop = context.formParam("lganame_drop");
        String year_drop = context.formParam("year_drop");
        String status_drop = context.formParam("status_drop");
        String gender_drop = context.formParam("gender_drop");
        String similarity_drop = context.formParam("similarity_drop");

        //Age Demographic
        int min_age_drop = 999;

        if (context.formParam("min_age_drop") != null) {
            min_age_drop = Integer.parseInt(context.formParam("min_age_drop"));
        }

        System.out.println(min_age_drop);

        int max_age_drop = 999;

        if (context.formParam("max_age_drop") != null) {
            max_age_drop = Integer.parseInt(context.formParam("max_age_drop"));
        }
        System.out.println(max_age_drop);

        //Health Condition
        String disease_drop = context.formParam("disease_drop");

        //Highest School Year
        int min_year_drop = 999;

        if (context.formParam("min_year_drop") != null) {
            min_year_drop = Integer.parseInt(context.formParam("min_year_drop"));
        }

        System.out.println(min_year_drop);

        int max_year_drop = 999;

        if (context.formParam("max_year_drop") != null) {
            max_year_drop = Integer.parseInt(context.formParam("max_year_drop"));
        }
        System.out.println(max_year_drop);

        //Household Income
        int min_income_drop = 999;

        if (context.formParam("min_income_drop") != null) {
            min_income_drop = Integer.parseInt(context.formParam("min_income_drop"));
        }

        System.out.println(min_income_drop);

        int max_income_drop = 999;

        if (context.formParam("max_income_drop") != null) {
            max_income_drop = Integer.parseInt(context.formParam("max_income_drop"));
        }
        System.out.println(max_income_drop);

        //Number of Result to Show
        int number_result_textbox = 0;

        if (context.formParam("number_result_textbox") != null && context.formParam("number_result_textbox") != "") {
            number_result_textbox = Integer.parseInt(context.formParam("number_result_textbox"));
        }

        System.out.println(number_result_textbox);

        if (lganame_drop == null || year_drop == null || status_drop == null || gender_drop == null || min_age_drop == 999 || max_age_drop == 999 || number_result_textbox == 0 || disease_drop == null
        || min_year_drop == 999 || max_year_drop == 999 || min_income_drop == 999 || max_income_drop == 999) {
            html = html + "<h5>Please Select from the Dropdown</h5>";
        }

        
        if (lganame_drop != null && year_drop != null && status_drop != null && gender_drop != null && min_age_drop >= 0 && max_age_drop >= 0 && min_age_drop <= 13 && max_age_drop <= 13 && min_age_drop <= max_age_drop && similarity_drop != null && number_result_textbox > 0  ) {


            HashMap<String, ArrayList<SimilarAgeDataset>> similar_age_range_dataset = jdbc.findSimilarAgeDataset(lganame_drop, year_drop, status_drop, gender_drop, min_age_drop, max_age_drop, similarity_drop, number_result_textbox);
            
            html = html +"<h1 class='outcome_title'>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives</h1>";

            html = html + "<h3 class='dataset_title'>Dataset: Age Demographic by Indigenous Status by Sex</h3>";

            html = html + "<h4 class='selected_filer'>Chosen LGA: " + lganame_drop + "</h4>";

            String gender_filter_diplay = "";
            if (gender_drop.equals("m")) {
                gender_filter_diplay = "male";
            }
            else if (gender_drop.equals("f")){
                gender_filter_diplay = "female";
            }

            String min_age_display = "";
            String max_age_display = "";

            switch(min_age_drop) {
                case 0:
                    min_age_display = "0";
                    break;
                case 1:
                    min_age_display = "5";
                    break;
                case 2:
                    min_age_display = "10";
                    break;
                case 3:
                    min_age_display = "15";
                    break;
                case 4:
                    min_age_display = "20";
                    break;
                case 5:
                    min_age_display = "25";
                    break;
                case 6:
                    min_age_display = "30";
                    break;
                case 7:
                    min_age_display = "35";
                    break;
                case 8:
                    min_age_display = "40";
                    break;
                case 9:
                    min_age_display = "45";
                    break;
                case 10:
                    min_age_display = "50";
                    break;
                case 11:
                    min_age_display = "55";
                    break;
                case 12:
                    min_age_display = "60";
                    break;
                case 13:
                    min_age_display = "65";
                    break;
            }

            switch(max_age_drop) {
                case 0:
                    max_age_display = "4";
                    break;
                case 1:
                    max_age_display = "9";
                    break;
                case 2:
                    max_age_display = "14";
                    break;
                case 3:
                    max_age_display = "19";
                    break;
                case 4:
                    max_age_display = "24";
                    break;
                case 5:
                    max_age_display = "29";
                    break;
                case 6:
                    max_age_display = "34";
                    break;
                case 7:
                    max_age_display = "39";
                    break;
                case 8:
                    max_age_display = "44";
                    break;
                case 9:
                    max_age_display = "49";
                    break;
                case 10:
                    max_age_display = "54";
                    break;
                case 11:
                    max_age_display = "59";
                    break;
                case 12:
                    max_age_display = "64";
                    break;
                case 13:
                    max_age_display = "above 65";
                    break;
            }

            String order_display = "";

            if (similarity_drop.equals("ASC")) {
                order_display = "Most Similar (Ascending)";
            }
            else if (similarity_drop.equals("DESC")) {
                order_display = "Least Similar (Descending)";
            }

            html = html + "<h4 class='selected_filer'>Chosen Filter: Age Demographic by " + status_drop + " status by " +
            gender_filter_diplay + " gender within " + min_age_display + "  to " + max_age_display + " years old in " + year_drop + "</h4>";

            html = html + "<h4 class='selected_filer'>Chosen Order: " + order_display + "</h4>";

            html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Number of People</th>
                                    <th>Difference</th>
                                    <th>Similarity Percentage</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                for (SimilarAgeDataset data : similar_age_range_dataset.get("SelectedLGA")) {
                        html = html + "<tr class='selected_lga_row'>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>--</td>";
                        html = html + "     <td>--</td>";
                        html = html + "</tr>";
                }
                for (SimilarAgeDataset data : similar_age_range_dataset.get("ComparedLGA")) {
                        html = html + "<tr>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>" + data.getDifference() + "</td>";

                        float similarity;
                        if (data.getDifference() <= data.getSelectedLGANumberOfPeople()) {
                            similarity = (float) (data.getSelectedLGANumberOfPeople() - data.getDifference())/data.getSelectedLGANumberOfPeople();
                            //Similarity = (selected LGA number of people - difference) / (selected LGA number of people)
                            //Only for compared LGAs with difference less than the selected LGA number of people
                        }                       
                        else {
                            similarity = 0;
                            //Similarity = 0 for compared LGAs with difference more than the selected LGA number of people
                        }
                        html = html + "     <td>" + (String) roundOffTo2DecPlaces((similarity)*100) + "%</td>";

                        html = html + "</tr>";
                }      
                

                html = html + """
                            </tbody>
                        </table>
                    </div>
                    </div>
                """;
        }

        if (lganame_drop != null && year_drop != null && status_drop != null && gender_drop != null && disease_drop != null && similarity_drop != null && number_result_textbox > 0  ) {


            HashMap<String, ArrayList<SimilarDiseaseDataset>> similar_disease_dataset = jdbc.findSimilarDiseaseDataset(lganame_drop, year_drop, status_drop, gender_drop, disease_drop, similarity_drop, number_result_textbox);
            
            html = html +"<h1 class='outcome_title'>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives</h1>";

            html = html + "<h3 class='dataset_title'>Dataset: Health Condition by Indigenous Status by Sex</h3>";

            html = html + "<h4 class='selected_filer'>Chosen LGA: " + lganame_drop + "</h4>";

            String gender_filter_diplay = "";
            if (gender_drop.equals("m")) {
                gender_filter_diplay = "male";
            }
            else if (gender_drop.equals("f")){
                gender_filter_diplay = "female";
            }

            String disease_filter_display = "";

            if (disease_drop.contains("_")) {
                disease_filter_display = disease_drop.replace('_', ' ');
            }
            else {
                disease_filter_display = disease_drop;
            }

            disease_filter_display = disease_filter_display.substring(0, 1).toUpperCase() + disease_filter_display.substring(1); 
            
            String order_display = "";

            if (similarity_drop.equals("ASC")) {
                order_display = "Most Similar (Ascending)";
            }
            else if (similarity_drop.equals("DESC")) {
                order_display = "Least Similar (Descending)";
            }

            html = html + "<h4 class='selected_filer'>Chosen Filter: " + disease_filter_display + " by " + status_drop + " status by " +
            gender_filter_diplay + " gender in " + year_drop + "</h4>";

            html = html + "<h4 class='selected_filer'>Chosen Order: " + order_display + "</h4>";

            html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Disease</th>
                                    <th>Number of People</th>
                                    <th>Difference</th>
                                    <th>Similarity Percentage</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                for (SimilarDiseaseDataset data : similar_disease_dataset.get("SelectedLGA")) {
                        html = html + "<tr class='selected_lga_row'>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getDisease() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>--</td>";
                        html = html + "     <td>--</td>";
                        html = html + "</tr>";
                }
                for (SimilarDiseaseDataset data : similar_disease_dataset.get("ComparedLGA")) {
                        html = html + "<tr>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getDisease() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>" + data.getDifference() + "</td>";

                        float similarity;
                        if (data.getDifference() <= data.getSelectedLGANumberOfPeople()) {
                            similarity = (float) (data.getSelectedLGANumberOfPeople() - data.getDifference())/data.getSelectedLGANumberOfPeople();
                            //Similarity = (selected LGA number of people - difference) / (selected LGA number of people)
                            //Only for compared LGAs with difference less than the selected LGA number of people
                        }                       
                        else {
                            similarity = 0;
                            //Similarity = 0 for compared LGAs with difference more than the selected LGA number of people
                        }
                        html = html + "     <td>" + (String) roundOffTo2DecPlaces((similarity)*100) + "%</td>";

                        html = html + "</tr>";
                }      
                

                html = html + """
                            </tbody>
                        </table>
                    </div>
                    </div>
                """;
        }

        if (lganame_drop != null && year_drop != null && status_drop != null && gender_drop != null && min_year_drop >= 0 && max_year_drop >= 0 && min_year_drop <= 5 && max_year_drop <= 5 && min_year_drop <= max_year_drop && similarity_drop != null && number_result_textbox > 0  ) {


            HashMap<String, ArrayList<SimilarSchoolDataset>> similar_school_dataset = jdbc.findSimilarSchoolDataset(lganame_drop, year_drop, status_drop, gender_drop, min_year_drop, max_year_drop, similarity_drop, number_result_textbox);
            
            html = html +"<h1 class='outcome_title'>Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential</h1>";

            html = html + "<h3 class='dataset_title'>Dataset: Highest School Year Completed by Indigenous Status by Sex</h3>";

            html = html + "<h4 class='selected_filer'>Chosen LGA: " + lganame_drop + "</h4>";

            String gender_filter_diplay = "";
            if (gender_drop.equals("m")) {
                gender_filter_diplay = "male";
            }
            else if (gender_drop.equals("f")){
                gender_filter_diplay = "female";
            }

            String min_year_display = "";
            String max_year_display = "";

            switch(min_year_drop) {
                case 0:
                    min_year_display = "Did not attend school";
                    break;
                case 1:
                    min_year_display = "Year 8 and below";
                    break;
                case 2:
                    min_year_display = "Year 9 equivalent";
                    break;
                case 3:
                    min_year_display = "Year 10 equivalent";
                    break;
                case 4:
                    min_year_display = "Year 11 equivalent";
                    break;
                case 5:
                    min_year_display = "Year 12 equivalent";
                    break;
            }

            switch(max_year_drop) {
                case 0:
                    max_year_display = "Did not attend school";
                    break;
                case 1:
                    max_year_display = "Year 8 and below";
                    break;
                case 2:
                    max_year_display = "Year 9 equivalent";
                    break;
                case 3:
                    max_year_display = "Year 10 equivalent";
                    break;
                case 4:
                    max_year_display = "Year 11 equivalent";
                    break;
                case 5:
                    max_year_display = "Year 12 equivalent";
                    break;
            }

            String order_display = "";

            if (similarity_drop.equals("ASC")) {
                order_display = "Most Similar (Ascending)";
            }
            else if (similarity_drop.equals("DESC")) {
                order_display = "Least Similar (Descending)";
            }

            if (!min_year_display.equals(max_year_display)) { 
                html = html + "<h4 class='selected_filer'>Chosen Filter: " + min_year_display + "  to " + max_year_display + " by " + status_drop + " status by " +
                gender_filter_diplay + " gender in " + year_drop + "</h4>";
            }
            else {
                html = html + "<h4 class='selected_filer'>Chosen Filter: " + min_year_display + " by " + status_drop + " status by " +
                gender_filter_diplay + " gender in " + year_drop + "</h4>";
            }

            html = html + "<h4 class='selected_filer'>Chosen Order: " + order_display + "</h4>";

            html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Number of People</th>
                                    <th>Difference</th>
                                    <th>Similarity Percentage</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                for (SimilarSchoolDataset data : similar_school_dataset.get("SelectedLGA")) {
                        html = html + "<tr class='selected_lga_row'>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>--</td>";
                        html = html + "     <td>--</td>";
                        html = html + "</tr>";
                }
                for (SimilarSchoolDataset data : similar_school_dataset.get("ComparedLGA")) {
                        html = html + "<tr>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getGender() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>" + data.getDifference() + "</td>";

                        float similarity;
                        if (data.getDifference() <= data.getSelectedLGANumberOfPeople()) {
                            similarity = (float) (data.getSelectedLGANumberOfPeople() - data.getDifference())/data.getSelectedLGANumberOfPeople();
                            //Similarity = (selected LGA number of people - difference) / (selected LGA number of people)
                            //Only for compared LGAs with difference less than the selected LGA number of people
                        }                       
                        else {
                            similarity = 0;
                            //Similarity = 0 for compared LGAs with difference more than the selected LGA number of people
                        }
                        html = html + "     <td>" + (String) roundOffTo2DecPlaces((similarity)*100) + "%</td>";

                        html = html + "</tr>";
                }      
                

                html = html + """
                            </tbody>
                        </table>
                    </div>
                    </div>
                """;
        }

        if (lganame_drop != null && year_drop != null && status_drop != null && min_income_drop >= 0 && max_income_drop >= 0 && min_income_drop <= 12 && max_income_drop <= 12 && min_income_drop <= max_income_drop && similarity_drop != null && number_result_textbox > 0  ) {


            HashMap<String, ArrayList<SimilarIncomeDataset>> similar_income_dataset = jdbc.findSimilarIncomeDataset(lganame_drop, year_drop, status_drop, min_income_drop, max_income_drop, similarity_drop, number_result_textbox);
            
            html = html +"<h1 class='outcome_title'>Outcome 8: Strong economic participation and development of Aboriginal and Torres Strait Islander people and communities</h1>";

            html = html + "<h3 class='dataset_title'>Dataset: Household Weekly Income by Indigenous Status of Household</h3>";

            html = html + "<h4 class='selected_filer'>Chosen LGA: " + lganame_drop + "</h4>";

            String min_income_display = "";
            String max_income_display = "";

            switch(min_income_drop) {
                case 0:
                    min_income_display = "1";
                    break;
                case 1:
                    min_income_display = "150";
                    break;
                case 2:
                    min_income_display = "300";
                    break;
                case 3:
                    min_income_display = "400";
                    break;
                case 4:
                    min_income_display = "500";
                    break;
                case 5:
                    min_income_display = "650";
                    break;
                case 6:
                    min_income_display = "800";
                    break;
                case 7:
                    min_income_display = "1000";
                    break;
                case 8:
                    min_income_display = "1250";
                    break;
                case 9:
                    min_income_display = "1500";
                    break;
                case 10:
                    min_income_display = "2000";
                    break;
                case 11:
                    min_income_display = "2500";
                    break;
                case 12:
                    min_income_display = "3000";
                    break;
            }

            switch(max_income_drop) {
                case 0:
                    max_income_display = "149";
                    break;
                case 1:
                    max_income_display = "299";
                    break;
                case 2:
                    max_income_display = "399";
                    break;
                case 3:
                    max_income_display = "499";
                    break;
                case 4:
                    max_income_display = "649";
                    break;
                case 5:
                    max_income_display = "799";
                    break;
                case 6:
                    max_income_display = "999";
                    break;
                case 7:
                    max_income_display = "1249";
                    break;
                case 8:
                    max_income_display = "1499";
                    break;
                case 9:
                    max_income_display = "1999";
                    break;
                case 10:
                    max_income_display = "2499";
                    break;
                case 11:
                    max_income_display = "2999";
                    break;
                case 12:
                    max_income_display = "above 3000";
                    break;
            }

            String order_display = "";

            if (similarity_drop.equals("ASC")) {
                order_display = "Most Similar (Ascending)";
            }
            else if (similarity_drop.equals("DESC")) {
                order_display = "Least Similar (Descending)";
            }

            html = html + "<h4 class='selected_filer'>Chosen Filter: Household Weekly Income by " + status_drop + " status by within AUD" + min_income_display + "  to AUD" + max_income_display + " in " + year_drop + "</h4>";

            html = html + "<h4 class='selected_filer'>Chosen Order: " + order_display + "</h4>";

            html = html + """
                    <div class='result_container'>
                    <div class='data_table'>
                        <table>
                            <thead>
                                <tr>
                                    <th>LGA Code</th>
                                    <th>LGA Name</th>
                                    <th>Indigenous Status</th>
                                    <th>Number of People</th>
                                    <th>Difference</th>
                                    <th>Similarity Percentage</th>
                                </tr>
                            </thead>

                            <tbody>
                """;

                for (SimilarIncomeDataset data : similar_income_dataset.get("SelectedLGA")) {
                        html = html + "<tr class='selected_lga_row'>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>--</td>";
                        html = html + "     <td>--</td>";
                        html = html + "</tr>";
                }
                for (SimilarIncomeDataset data : similar_income_dataset.get("ComparedLGA")) {
                        html = html + "<tr>";
                        html = html + "     <td>" + data.getLgaCode() + "</td>";
                        html = html + "     <td>" + data.getLgaName() + "</td>";
                        html = html + "     <td>" + data.getStatus() + "</td>";
                        html = html + "     <td>" + data.getNumberOfPeople() + "</td>";
                        html = html + "     <td>" + data.getDifference() + "</td>";

                        float similarity;
                        if (data.getDifference() <= data.getSelectedLGANumberOfPeople()) {
                            similarity = (float) (data.getSelectedLGANumberOfPeople() - data.getDifference())/data.getSelectedLGANumberOfPeople();
                            //Similarity = (selected LGA number of people - difference) / (selected LGA number of people)
                            //Only for compared LGAs with difference less than the selected LGA number of people
                        }                       
                        else {
                            similarity = 0;
                            //Similarity = 0 for compared LGAs with difference more than the selected LGA number of people
                        }
                        html = html + "     <td>" + (String) roundOffTo2DecPlaces((similarity)*100) + "%</td>";

                        html = html + "</tr>";
                }      
                

                html = html + """
                            </tbody>
                        </table>
                    </div>
                    </div>
                """;
        }
        
        html = html + "</section>" + "</main>";
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

}
