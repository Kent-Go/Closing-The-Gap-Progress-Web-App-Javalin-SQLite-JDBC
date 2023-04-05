PRAGMA foreign_keys = OFF;
drop table if exists Persona;
drop table if exists NeedGoal;
drop table if exists SkillExp;
DROP TABLE IF EXISTS Lga;
DROP TABLE IF EXISTS Age_Demographic;
DROP TABLE IF EXISTS Health_Condition;
DROP TABLE IF EXISTS Highest_School_Year;
DROP TABLE IF EXISTS Household_Weekly_Income;
PRAGMA foreign_keys = ON;

CREATE TABLE Persona (
    PId               INTEGER NOT NULL,
    PName             VARCHAR(30) NOT NULL,
    PQuote            VARCHAR(100) NOT NULL,
    PAge              INTEGER NOT NULL,
    PCareer           TEXT NOT NULL,
    PEthnicity        VARCHAR(30) NOT NULL,
    PGender           VARCHAR(6) NOT NULL,
    PRIMARY KEY (PId)
);

CREATE TABLE NeedGoal (
    PId               INTEGER NOT NULL,
    PNeedGoalID       INTEGER NOT NULL,
    PNeedGoal         TEXT NOT NULL,
    PRIMARY KEY (PId, PNeedGoalID),
    FOREIGN KEY (PId) REFERENCES Persona(PId)
);

CREATE TABLE SkillExp (
    PId               INTEGER NOT NULL,
    PSkillExpID       INTEGER NOT NULL,
    PSkillExp         TEXT NOT NULL,
    PRIMARY KEY (PId, PSkillExpID),
    FOREIGN KEY (PId) REFERENCES Persona(PId)
);
-- Persona#1
INSERT INTO Persona VALUES (0, 'Henry Smith', '“We must make sure that this Agreement is going well.”', 61, 'Victoria State Government Official', 'Caucasian', 'Male');

INSERT INTO NeedGoal VALUES (0, 0, 'Keep track of all socioeconomic target progress in Closing the Gap agreement in Victoria region');

INSERT INTO NeedGoal VALUES (0, 1, 'Want to know if any of the progress are falling behind, needs to be able to easily access data regarding the agreement');

INSERT INTO NeedGoal VALUES (0, 2, 'Needs data to be presented in a clear and understandable way');

INSERT INTO SkillExp VALUES (0, 0, 'Not very well experienced with the Internet');

INSERT INTO SkillExp VALUES (0, 1, 'able to interpret data and graphs');

-- Persona#2
INSERT INTO Persona VALUES(1, 'Jarrah Wunjurrah', '"I believe it’s important for Indegenous children and youth to receive quality education"', 55, 'Novelist', 'Aboriginal', 'Male');

INSERT INTO NeedGoal VALUES(1, 0, 'Gather accurate raw and proportional value on Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential');

INSERT INTO NeedGoal VALUES(1, 1, 'The data must illustrate the number of Aboriginal students who are unable to attend school within the Victoria region');

INSERT INTO NeedGoal VALUES(1, 2, 'Wanted to identify difficulties students are facing that prevent them from accessing Australian education');

INSERT INTO NeedGoal VALUES(1, 3, 'Establish relevant implementation plans such as building local schools with the Victorian government agents to address identified issue(s)');

INSERT INTO NeedGoal VALUES(1, 4, 'Unable to access website that illustrate and analyse the Outcome 5’s data in 2016 and 2021');

INSERT INTO SkillExp VALUES(1, 0, 'Has advanced literacy skills in expressing and communicating ideas');

INSERT INTO SkillExp VALUES(1, 1, 'Able to use and browse the Internet on computer devices');

INSERT INTO SkillExp VALUES(1, 2, 'Limited data analysis skill');

INSERT INTO SkillExp VALUES(1, 3, '25+ years of experience in social work for Aboriginal communities');

CREATE TABLE Lga (

    year INT NOT NULL,

    lgacode INT NOT NULL,

    lganame TEXT NOT NULL,

    lgatype VARCHAR(2),

    area FLOAT,

    latitude FLOAT,

    longitude FLOAT,

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