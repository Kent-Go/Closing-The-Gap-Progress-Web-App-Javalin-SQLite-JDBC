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
 * @author Timothy Wiley, 2022. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Closing the Gap</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/' class = 'current_page'>Homepage</a>
                <a href='about.html'>ABOUT US</a>
                <a href='latest-2021-data.html'>LATEST 2021 DATA</a>
                <a href='focus-lga-state.html'>FOCUS BY LGA/STATE</a>
                <a href='gap-scores.html'>GAP SCORES</a>
                <a href='find-similar-lga.html'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
            
            <section class='page-top'>
            <img class = 'bg' src = 'artwork_homepage.jpeg'>
                <div class='page-top-text'>
                    <h1>
                        Closing the Gap
                    </h1>
                    <h1>
                        Progress
                    </h1>
                    <p>
                    \"To empower Aboriginal and Torres Strait Islander people, 
                    and Agreement parties' capability in addressing the social challenge of socioeconomic inequalities through providing accessible 
                    and useful data for informed decision-makings\"
                    </p>
                </div>                
            </section>
            </div>
        """;

        /* 
        Mario: The next section creates the buttons for each page.
        Each button div is inner-div class, so that the class is different from the content class in the other pages,
        meaning I can style it in css without breaking other pages(for now lol)
        Each button div also has its own unique ID, so that I could adjust how they're laid out.

        Inside each div is a hyperlink of btn class, so that the style of the text can be changed from the default link style.
        In addition, the link contains an h2 element, which itself contains a p element. I did this in order to group all 
        the text in h2, but be able to have different font sizes between the button names and descriptions. 

        Each h2 element has a 'btn-title' id(not sure if making it a class would make a significant difference), 
        and each p element has a 'btn-substitle' id.
        */
        
        /*Mario: Puts the link section in its own div, so that the whole section.
          can have the same background color in the css code*/
        html = html + "<div class='outer-div'>";

        //link to Mission statement
        html = html + "<div id='mission' class='inner-div'>";

        html = html + """
            <a class='btn' href = 'about.html'>
                <h2 id = 'btn-title'>About us
                    <p id = 'btn-subtitle'>Find out more about our website's mission, 
                                           and the people who made it.<p>
                </h2>
            </a>
            """;

        html = html + "</div>";

        //link to Latest 2021 data
        html = html + "<div id='data-2021' class='inner-div'>";

        html = html + """
            <a class='btn' href = 'latest-2021-data.html'>
                <h2 id = 'btn-title'>Latest 2021 data
                    <p id = 'btn-subtitle'>View the latest data from the 2021 census
                                           regarding the Closing the Gap Agreement.<p>
                </h2>
            </a>
            """;

        html = html + "</div>";

        //link to Focus by LGA/State
        html = html + "<div id='focus-lga' class='inner-div'>";

        html = html + """
            <a class='btn' href = 'focus-lga-state.html'>
                <h2 id = 'btn-title'>Focus by LGA/State
                    <p id = 'btn-subtitle'>View and compare data between 2016 and 2021
                                           for a specific LGA or State.<p>
                </h2>
            </a>
            """;

        html = html + "</div>";

        //link to Find Similar LGAs
        html = html + "<div id='similar-lga' class='inner-div'>";

        html = html + """
            <a class='btn' href = 'find-similar-lga.html'>
                <h2 id = 'btn-title'>Find Similar LGAs
                    <p id = 'btn-subtitle'>Find out how each LGA similar in their 
                                           characteristics based on the Outcomes.<p>
                </h2>
            </a>
            """;

        html = html + "</div>";

        //link to Gap Scores
        html = html + "<div id='gap-scores' class='inner-div'>";

        html = html + """
            <a class='btn' href = 'gap-scores.html'>
                <h2 id = 'btn-title'>Gap Scores
                    <p id = 'btn-subtitle'>Display Gap Scores between indigenous and
                                           non-indigenous people for 2016 and 2021.<p>
                </h2>
            </a>
            """;

        html = html + "</div>";

        //close outer div
        html = html + "</div>";

        /*Mario: Section for socioeconomic outcomes, utilising accordion content
         * This is done utilising checkboxes in html, there is no javascript used here
         * The checkbox will expand the accordion content boxes
        */

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<Outcome> outcomes = jdbc.getAllOutcomes();

        
        //Mario: Creates an accordion for outcome 1
        html = html + """
            <div id = 'outcomes'>
                <h2 id = 'section-name'>Socioeconomic Outcomes</h2>
                <p>Read a brief description for each of the 17 socioeconomic outcomes.</p>
                """;
                        
        for(int i = 0; i < outcomes.size(); i++){
            html = html + "<div class = 'acc'>";

            int outcomeId = outcomes.get(i).outcomeId;            
            String outcomeName = "Outcome " + outcomes.get(i).outcomeId + ": " + outcomes.get(i).outcomeName;
            String outcomeDesc = outcomes.get(i).outcomeDesc;

            //Mario: id to use in html checkboxes, oc stands for OutCome
            String accId = "'oc" + outcomeId + "'";

            html = html + "<input type = 'checkbox' id = " + accId + ">";

            html = html + "<label class = 'acc-label' for = " + accId + ">" + outcomeName + "</label>";

            html = html + "<div class = 'acc-content'>" + outcomeDesc + "</div>";

            html = html + "</div>";

            html = html + "<p></p>";
                /*<div class = 'acc'>
                    <input type = 'checkbox' id = 'oc1'>
                    
                    <label class = 'acc-label' for = 'oc1'>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives.</label>
                    <div class = 'acc-content'>
                        Description for outcome 2
                    </div>
                </div>*/

        }

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
