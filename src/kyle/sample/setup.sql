DROP TABLE IF EXISTS Enrollments;
DROP TABLE IF EXISTS Courses;
DROP TABLE IF EXISTS Countries;
DROP TABLE IF EXISTS Students;
DROP TABLE IF EXISTS Teachers;
DROP TABLE IF EXISTS Addresses;

CREATE TABLE Teachers(
    teacherID INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255)
);

CREATE TABLE Countries(
    countryID INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255)
);

CREATE TABLE Addresses(
    addressID INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255),
    countryID INTEGER,
    FOREIGN KEY (countryID) REFERENCES Countries(countryID)
);

CREATE TABLE Students(
    studentID INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    addressID INTEGER,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID)
);

CREATE TABLE Courses(
    courseID INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255),
    teacherID INTEGER,
    FOREIGN KEY (teacherID) REFERENCES Teachers(teacherID)
);

CREATE TABLE Enrollments(
    enrollmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    studentID INTEGER,
    courseID INTEGER,
    grade INTEGER,
    FOREIGN KEY (studentID) REFERENCES Students(studentID),
    FOREIGN KEY (courseID) REFERENCES Courses(courseID)
);

INSERT INTO Countries (name)
VALUES ( "Denmark" ),
       ( "Sweden" );

INSERT INTO Addresses (name, countryID)
VALUES ("4800 Nykøbing F"       , 1 ),
       ("Karlskrona"            , 2 ),
       ("7190 Billund"          , 1 ),
       ("4180 Sorø"             , 1 ),
       ("4863 Eskildstrup"      , 1 ),
       ("5000 Odense"           , 1 ),
       ("Stockholm"             , 2 ),
       ("4340 Tølløse"          , 1 ),
       ("4040 Jyllinge"         , 1 );

INSERT INTO Students (name, addressID)
VALUES  ( "Aisha Lincoln"  , 1 ),
        ( "Anya Nielsen"   , 1 ),
        ( "Alfred Jensen"  , 2 ),
        ( "Berta Bertelsen", 3 ),
        ( "Albert Antonsen", 4 ),
        ( "Eske Eriksen"   , 5 ),
        ( "Olaf Olesen"    , 6 ),
        ( "Salma Simonsen" , 7 ),
        ( "Theis Thomasen" , 8 ),
        ( "Janet Jensen"   , 9 );

INSERT INTO Courses (name, teacherID)
VALUES ( "SD2019" , 1 ),
       ( "SD2020" , 1 ),
       ( "ES2019" , 2 );

INSERT INTO Teachers (name)
VALUES ( "Line" ),
       ( "Ebbe" );

INSERT INTO Enrollments (studentID, courseID, grade)
VALUES ( 1 , 1, 12 ),
       ( 1 , 3, 10 ),
       ( 2 , 2, NULL ),
       ( 2 , 3, 12 ),
       ( 3 , 1, 7 ),
       ( 3 , 3, 10 ),
       ( 4 , 2, NULL ),
       ( 4 , 3, 2 ),
       ( 5 , 1, 10 ),
       ( 5 , 3, 7 ),
       ( 6 , 2, NULL ),
       ( 6 , 3, 10 ),
       ( 7 , 1, 4 ),
       ( 7 , 3, 12 ),
       ( 8 , 2, NULL ),
       ( 8 , 3, 12 ),
       ( 9 , 1, 12 ),
       ( 9 , 3, 12 ),
       ( 10, 2, NULL ),
       ( 10, 3, 7 );
