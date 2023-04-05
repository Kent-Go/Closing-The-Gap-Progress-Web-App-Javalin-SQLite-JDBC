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
public class AboutUs implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/about.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>About Us</title>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";
        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='about.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/'>Homepage</a>
                <a href='about.html' class='current_page'>ABOUT US</a>
                <a href='latest-2021-data.html'>LATEST 2021 DATA</a>
                <a href='focus-lga-state.html'>FOCUS BY LGA/STATE</a>
                <a href='gap-scores.html'>GAP SCORES</a>
                <a href='find-similar-lga.html'>FIND SIMILAR LGAs</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
            
                <section class='banner'>
                    
                    <!-- ⓒ Doris Gingingara  |  Emu and Bush Turkey from https://japingkaaboriginalart.com/collections/aboriginal-art-emu/ -->
                    <img class='banner-img' src='aboutus_cover_img.jpg'>

                    <div class='banner-text'>
                        <h3><span>About Us</span></h3>
                        <h1><span>Closing the Gap Progress</span></h1>
                    </div>

                </section>  

            </div>
        """;

        // Main
        html = html + "<main class = 'site-main'>";

        html = html + """
            <section class='site-purpose'>

                <div class='purpose-title'>
                    <h3><span>Our Purpose</span></h3>
                </div>

                <div class='purpose-text'>
                    <p><span>Closing the Gap Progress aims to address the social challenge: Closing the Gap, through assisting the Agreement parties in viewing the progress made by each party in overcoming the 17 socioeconomic inequalities faced by Aboriginal and Torres Strait Islander people, and empowering Aboriginal people&apos;s capability in accessing all related data and information digitally to make informed decision-makings with the governments in order to accelerate the Agreement&apos;s progress.</span></p>
                </div>
        
        
            </section>
        """;   
        
        html = html + """
            <section class='site-data'>

                <div class='card'>
                   
                    <div class = 'card-text'>
                        <div class='data-title'>
                            <h3>Deliver accurate and reliable data available</h3>
                        </div>
                        <div class='data-desc'>
                            <p>We uses data that are available from Australian Bureau of Statistics (ABS) 2016 and 2021 Census of Population and Housing, Aboriginal and Torres Strait Islander Profile Tables under Creative Commons Attribution 4.0 International licence.
                             This is to ensure all dataset and information displayed are of credible sources.
                             Note that the datasets have been modified for use in COSC2803 (Programming Studio 1) in the School of Computing Technologies at RMIT University.
                            </p>
                        </div>
                    </div>

                    <div class = 'card-img'>
                        <img src='abs-meta-image.png'/>
                    </div>

 
                </div>

                <div class='card'>
                   
                    <div class = 'card-text'>
                        <div class='data-title'>
                            <h3>Maximise access to data</h3>
                        </div>
                        <div class='data-desc'>
                            <p>The data displayed are universally accessible by anyone who wishes to use and learn more about the Closing the Gap's 3 socioeconomic inequalities targets, in particular, Outcome 1, 5 and 8. 
                            Hence, Users could utilise the filtering and sorting function provided to gain information and insights on the Age Demographics, Health Conditons, Level of Education and Weekly Household Income in 2021 and 2016.
                            </p>
                        </div>
                    </div>

                    <div class = 'card-img'>
                        <img src='map.jpg'/>
                    </div>

 
                </div>
                
            </section>
        """;
        
        html = html + """
            <section class='site-persona'>

                <div class='persona-title'>
                    <h3><span>Personas</span></h3>
                </div>""";

        html = html + "<div class = 'persona-container'>";
            html = html + "<div class ='persona-text'>";      
            
            JDBCConnection jdbc = new JDBCConnection();

            
            ArrayList<Persona> personas1 = jdbc.getPersona(0);

            for (Persona persona : personas1) {
            
                html = html + "<h3>Meet Mr." + persona.getPersonaName() + "</h3>";

                html = html + "<p>" + persona.getPersonaQuote() + "</p>";

                html = html + "<h3>Background:</h3>" + "<ul>";

                html = html +       "<li>" + persona.getPersonaAge() + " years old " + persona.getPersonaCareer() + "</li>";

                html = html +       "<li>" + persona.getPersonaEthnicity() + " " + persona.getPersonaGender() + "</li>";

                html = html + "</ul>";

                html = html + "<h3>Need and Goal:</h3>" + "<ul>";

                for(String needGoal : persona.getPersonaNeedGoal()){
                html = html +       "<li>" + needGoal + "</li>";
                }

                html = html + "</ul>";

                html = html + "<h3>Skill and Experience:</h3>" + "<ul>";

                for(String skillExp : persona.getPersonaSkillExp()){
                html = html +       "<li>" + skillExp + "</li>";
                }

                html = html + "</ul>";
            
            }
            
            html = html + "</div>";

            html = html + "<div class ='persona-img'>";

                html = html + "<img src='persona2.png' class = 'profile-img'>";

            html = html + "</div>";

        html = html + "</div>";

        

        html = html + "<div class = 'persona-container'>";
            html = html + "<div class ='persona-text'>";      
            
            JDBCConnection jdbc2 = new JDBCConnection();

            
            ArrayList<Persona> personas2 = jdbc2.getPersona(1);

            for (Persona persona : personas2) {
            
                html = html + "<h3>Meet Mr." + persona.getPersonaName() + "</h3>";

                html = html + "<p>" + persona.getPersonaQuote() + "</p>";

                html = html + "<h3>Background:</h3>" + "<ul>";

                html = html +       "<li>" + persona.getPersonaAge() + " years old " + persona.getPersonaCareer() + "</li>";

                html = html +       "<li>" + persona.getPersonaEthnicity() + " " + persona.getPersonaGender() + "</li>";

                html = html + "</ul>";

                html = html + "<h3>Need and Goal:</h3>" + "<ul>";

                for(String needGoal : persona.getPersonaNeedGoal()){
                html = html +       "<li>" + needGoal + "</li>";
                }

                html = html + "</ul>";

                html = html + "<h3>Skill and Experience:</h3>" + "<ul>";

                for(String skillExp : persona.getPersonaSkillExp()){
                html = html +       "<li>" + skillExp + "</li>";
                }

                html = html + "</ul>";
            
            }
            
            html = html + "</div>";

            html = html + "<div class ='persona-img'>";

                html = html + "<img src='persona1.png' class = 'profile-img'>";

            html = html + "</div>";

        html = html + "</div>";

        html = html + "</section>";
        

        



        html = html + """
            <section class='made-by'>

                <div class = 'made-by-text'>
                    <div class='made-by-title'>
                        <h3>Made by Group 45</h3>
                    </div>
                    <div class='made-by-desc'>
                        <h5>Yoan-Mario Hristov (s3970071)</h5>
                        <h5>Go Chee Kin (s3955624)</h5>
                    </div>
                </div>

            </section>


        """;
        


        html = html + "</main>";

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
