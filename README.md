[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=8815825&assignment_repo_type=AssignmentRepo)
# Closing The Gap Progress Web App RMIT Studio Project (Semester 2 - September 2022)

Closing the Gap Progress Web App aims to address the social challenge: Closing the Gap, through assisting the Agreement parties in viewing the progress made by each party in overcoming the 17 socioeconomic inequalities faced by Aboriginal and Torres Strait Islander people, and empowering Aboriginal people&apos;s capability in accessing all related data and information digitally to make informed decision-makings with the governments in order to accelerate the Agreement&apos;s progress.

This app contains:
* Java PageIndex class for the Home page (index.html)
* Java AboutUs class for the About Us page (about.html)
* Java Latest2021Data class for the Latest 2021 Data page (latest-2021-data.html.html)
* Java FocusLgaState class for the Focus by LGA page (focus-lga-state.html)
* Java GapScores class for the Gap Scores page (gap-scores.html)
* Java FindSimilarLga class for the Find Similar LGAs page (find-similar-lga.html)
* JDBCConnection Java class, that uses the CTG Database. This class contains one method to return all LGAs contained in the Database.
* Database:
    * ```ctg.db``` - contains a starting database for you based on the example CTG ER Model.
* Optional helper program (``CTGProcessCSV.java``) that shows an example of how to load the SQLite database by using Java to read the CSV files and JDBC insert statements to update the CTG SQLite database.
* Optional helper SQL files (```ctg_create_tables.sql```) that creates two tables (```LGA``` and ```PopulationStatistics```) based on the example CTG ER Model.

Other Classes:
```bash
├── java/app                                - Package location for all Java files for the webserver
│         ├── App.java                      - Main Application entrypoint for Javalin
│         └── JDBCConnection.java           - Example JDBC Connection class based on Studio Project Workshop content
├── java/helper                             - Location of the helper file for loading SQLite with JDBC
│         └── CTGProcessCSV.java               - Helper Java program to load SQLite database from the provided CSVs
```

Folders:
```bash
├── /src/main                    - Location of all files as required by build configuration
│         ├── java               - Java Source location
│         │    ├── app           - Package location for all Java files for the webserver
│         │    └── helper        - Location of the helper file for loading SQLite with JDBC
│         └── resources          - Web resources (html templates / style sheets)
│               ├── css          - CSS Style-sheets. Base example style sheet (common.css) provided
│               └── images       - Image files. Base example image (RMIT Logo) provided
│ 
├── /target                      - build directory (DO NOT MODIFY)
├── /database                    - The folder to store sqlite database files (*.db files), SQL script (*.sql), and other files related to the database
├── pom.xml                      - Configure Build (DO NOT MODIFY)
└── README.md                    - This file ;)
```

Current Libraries:
* org.xerial.sqlite-jdbc (SQLite JDBC library)
* javalin (lightweight Java Webserver)

Libraries required as dependencies:
* By javalin
   * slf4j-simple (lightweight logging)
* By xerial/jdbc
   * sqlite-jdbc

# Building & Running the code
There are two places code can be run from
1. The **main** web server program
2. the **optional** helper program to use JDBC to load your SQLite database from the CSVs using Java

## Running the Main web server
You can run the main webserver program similar to the project workshop activities
1. Open this project within VSCode
2. Allow VSCode to read the pom.xml file
 - Allow the popups to run and "say yes" to VSCode configuring the build
 - Allow VSCode to download the required Java libraries
3. To Build & Run
 - Open the ``src/main/java/app/App.java`` source file, and select "Run" from the pop-up above the main function
4. Go to: http://localhost:7001

## Running the Helper Program
The helper program in ``src/main/java/helper/ProcessCSV.java`` can be run separetly from the main webserver. This gives a demonstration of how you can use Java to read the provided CSV files and store the information in an SQLite database. This example transforms the data in the ``database/lga_indigenous_status_by_age_by_sex_census_2016.csv`` file to match the format of the ``PopulationStatistics`` entity as given in the example ER Model for Milestone 1 for the Cloing-the-Gap social challenge. That is, the code converts the columns of the CSV into rows that can be loaded into the SQLite database using ``INSERT`` statements.

You can run the optional helper program by
1. Open this ``src/main/java/helper/ProcessCSV.java`` source file
1. Select "Debug" from the pop-up above the main function (or "Debug Java" from the top-right dropdown)
1. Allow the program to run

You can modify this file as you wish, for other tables and CSVs. When modifying you may need to pay attention to:
* ``DATABASE`` field to change the database location
* ``CSV_FILE`` to change which CSV file is bring read
* ``categoty``, ``status``, and ``sex`` arrays which should match the setup of the CSV file being read
* ``INSERT`` statement construction to:
    * Change the table being used
    * Column data being stored

## Testing on GitHub Codespaces
In Semester 2 2022, student have access to GitHub Codespaces through the RMIT GitHub Organisation. It is highly recommended to test that your code is fully functional in Codespaces.

GitHub Codespaces will be used as the common location to test and verify your studio project. Specifically, GitHub Codespaces will be used to verify your project in the event the code does not correctly function on the local assessor's computer.

# DEV Container for GitHub Codespaces
The ```.devcontainer``` folder contains configuration files for GitHub Codespaces.
This ensures that when the GitHub classroom is cloned, the workspace is correctly configured for Java (V16) and with the required VSCode extensions.
This folder will not affect a *local* VSCode setup on a computer.

# Authors
* Chee Kin Go (Kent), Bachelor of Computer Science, School of Computing Technologies, STEM College, RMIT University.
* Yoan Mario, Bachelor of Computer Science, School of Computing Technologies, STEM College, RMIT University.

