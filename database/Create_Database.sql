PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS Lga;

DROP TABLE IF EXISTS Age_Demographic;

DROP TABLE IF EXISTS Health_Condition;

DROP TABLE IF EXISTS Highest_School_Year;

DROP TABLE IF EXISTS Household_Weekly_Income;

PRAGMA foreign_keys = ON;



CREATE TABLE Lga (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    lganame TEXT NOT NULL,

    lgatype VARCHAR(2),

    area FLOAT,

    latitude FLOAT,

    longitude FLOAT,
    
    state TEXT NOT NULL,

    PRIMARY KEY (year, lgacode)

);



--year,lgacode,age_range,indigenous_status,gender,number_of_people

CREATE TABLE Age_Demographic (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    age_range TEXT NOT NULL,

    indigenous_status TEXT NOT NULL,

    gender CHAR(1) NOT NULL,

    number_of_people INTEGER NOT NULL,

    PRIMARY KEY (year, lgacode, age_range, indigenous_status, gender),

    FOREIGN KEY (year, lgacode) REFERENCES Lga
);



--year,lgacode,disease,indigenous_status,gender,number_of_people

CREATE TABLE Health_Condition (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    disease TEXT NOT NULL,

    indigenous_status TEXT NOT NULL,

    gender CHAR(1) NOT NULL,

    number_of_people INTEGER NOT NULL,

    PRIMARY KEY (year, lgacode, disease, indigenous_status, gender),

    FOREIGN KEY (year, lgacode) REFERENCES Lga
);



--year,lgacode,highest_year_of_school_completed,indigenous_status,gender,number_of_people

CREATE TABLE Highest_School_Year (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    highest_year_of_school_completed TEXT NOT NULL,

    indigenous_status TEXT NOT NULL,

    gender CHAR(1) NOT NULL,

    number_of_people INTEGER NOT NULL,

    PRIMARY KEY (year,lgacode,highest_year_of_school_completed,indigenous_status,gender),

    FOREIGN KEY (year, lgacode) REFERENCES Lga
);



--year,lgacode,weekly_income_range,indigenous_status,number_of_household

CREATE TABLE Household_Weekly_Income (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    weekly_income_range TEXT NOT NULL,

    indigenous_status TEXT NOT NULL,

    number_of_household INTEGER NOT NULL,

    PRIMARY KEY (year, lgacode, weekly_income_range, indigenous_status),

    FOREIGN KEY (year, lgacode) REFERENCES Lga

);