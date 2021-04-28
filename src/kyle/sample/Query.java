package kyle.sample;

/** SQL */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Query {
    private Connection connection;
    private Statement statement;
    private String DBURL;

    // Singleton for sharing model between multiple classes
    private final static Query instance = new Query();
    public static Query getInstance() {
        return instance;
    }

    public void printTable( String tableName ) {
        String query = "SELECT * FROM " + tableName;

        try {
            ResultSet results = this.statement.executeQuery( query );
            ResultSetMetaData rsmd = results.getMetaData();

            for ( int i = 1; i < rsmd.getColumnCount(); i++ ) {
                System.out.print( rsmd.getColumnLabel(i) + ' ' );
            }
            System.out.println();

            while (results.next()) {
                for ( int i = 1; i < rsmd.getColumnCount(); i++ ) {
                    System.out.print( results.getString(i) + ' ' );
                }
                System.out.println();
            }

        } catch (SQLException e) {
            throw new Error( "Unable to print table: " + e );
        }
    }

    public Table_Row[] getStudentClassesInfo( String studentID ) {
        String query =  "SELECT Courses.name, Courses.courseID, Teachers.name, Enrollments.grade, CourseAVG\n" +
                        "FROM Enrollments\n" +
                        "    JOIN Courses on Enrollments.courseID = Courses.courseID\n" +
                        "    JOIN Students on Enrollments.studentID = Students.studentID\n" +
                        "    JOIN Teachers on Teachers.teacherID = Courses.teacherID\n" +
                        "    JOIN (\n" +
                        "        SELECT Courses.courseID as ID, AVG(Enrollments.grade) as CourseAVG\n" +
                        "        FROM Courses\n" +
                        "            JOIN Enrollments on Courses.courseID = Enrollments.courseID\n" +
                        "        GROUP BY Courses.courseID\n" +
                        "    ) on ID = Enrollments.courseID\n" +
                        "WHERE Students.studentID = " + studentID + ";";

        try {
            ResultSet results = this.statement.executeQuery( query );
            ArrayList<Table_Row> dArray = new ArrayList<>();

            while ( results.next() ) {
                dArray.add( new Table_Row(
                        results.getString(1),       // Course Name
                        results.getString(2),       // Course ID
                        results.getString(3),       // Course Teacher Name
                        results.getString(4),       // Student grade for course
                        results.getString(5)        // Average grade for course
                ) );
            }

            return dArray.toArray( new Table_Row[0] );

        } catch (SQLException e) {
            throw new Error("Unable to get class info for " + studentID + ": " + e );
        }
    }

    public String getStudentAverage( String studentID ) {
        String query =  "SELECT avg(Enrollments.grade)\n" +
                        "FROM Enrollments\n" +
                        "    JOIN Students on Enrollments.studentID = Students.studentID\n" +
                        "WHERE Students.studentID = " + studentID + ";";

        try {
            ResultSet results = this.statement.executeQuery( query );

            results.next();

            return results.getString(1);
        } catch ( SQLException e ) {
            throw new Error( "Unable to get average for " + studentID + ": " + e );
        }
    }

    public void updateStudentGrade( String value, String studentID, String courseID) {
        String query =  "UPDATE Enrollments " +
                        "SET grade = " + value + " " +
                        "WHERE studentID = " + studentID + " " +
                        "AND courseID = " + courseID +  ";";

        try {
            this.statement.executeUpdate( query );
        } catch ( SQLException e ) {
            throw new Error( "Unable to set grade for " + studentID + ": " + e );
        }
    }

    public String[] getStudentClassesMissingGrades( String studentID ) {
        String query =  "Select Courses.name, Courses.courseID\n" +
                        "FROM Enrollments\n" +
                        "    JOIN Courses on Courses.courseID = Enrollments.courseID\n" +
                        "WHERE Enrollments.grade IS NULL AND Enrollments.studentID = " + studentID + ";";

        try {
            ResultSet results = this.statement.executeQuery( query );
            ArrayList<String> resultsArray = new ArrayList<>();

            while( results.next() ) {
                resultsArray.add( results.getString(1) + " - ID: " + results.getString(2) );
            }

            return resultsArray.toArray(new String[0]);

        } catch ( SQLException e ) {
            throw new Error( "Unable to get classes without grades for " + studentID + ": " + e );
        }
    }

    // No need to worry about an SQL injection here because the student names are from a list, provided from the db
    public String[] getStudentInfo( String studentID ) {
        String query =  "SELECT Countries.name, Addresses.name, Students.name, Students.studentID\n" +
                        "FROM Students\n" +
                        "    JOIN Addresses on Addresses.addressID = Students.addressID\n" +
                        "    JOIN Countries on Addresses.countryID = Countries.countryID\n" +
                        "WHERE Students.studentID = " + studentID + ";";

        try {
            ResultSet results = this.statement.executeQuery( query );

            results.next();
            String[] rtnData = { results.getString(1),
                                 results.getString(2),
                                 results.getString(3),
                                 results.getString(4) };

            return rtnData;


        } catch ( SQLException e ) {
            throw new Error("Couldn't get student information for " + studentID + ":" + e);
        }
    }

    public String[] studentList() {
        String query = "SELECT Students.name, Students.studentID FROM Students";
        String sizeQuery = "SELECT COUNT(*) FROM Students";

        try {
            ResultSet lengthResult = this.statement.executeQuery( sizeQuery );
            lengthResult.next();
            int length = lengthResult.getInt(1);

            ResultSet queryResults = this.statement.executeQuery( query );

            // Java ugliness
            String[] rtnResults = new String[length];

            for ( int i = 1; i <= length; i++ ) {
                queryResults.next();
                rtnResults[i-1] = queryResults.getString(1) +
                                    " - ID: " +
                                    queryResults.getString(2);
            }

            return rtnResults;

        } catch ( SQLException e ) {
            throw new Error( "Unable to get student list: " + e );
        }
    }

    public String[] classesList() {
        String query = "SELECT Courses.name, Courses.courseID FROM Courses";
        String sizeQuery = "SELECT COUNT(*) FROM Courses";

        try {
            ResultSet lengthResult = this.statement.executeQuery( sizeQuery );
            lengthResult.next();
            int length = lengthResult.getInt(1);

            ResultSet queryResults = this.statement.executeQuery( query );

            // Java ugliness
            String[] rtnResults = new String[length];

            for ( int i = 1; i <= length; i++ ) {
                queryResults.next();
                rtnResults[i-1] = queryResults.getString(1) +
                        " - ID: " +
                        queryResults.getString(2);
            }

            return rtnResults;

        } catch ( SQLException e ) {
            throw new Error( "Unable to get student list: " + e );
        }
    }

    public String getAverageGrade( String courseID ) {
        String query =  "SELECT AVG(Enrollments.grade)\n" +
                        "FROM Enrollments\n" +
                        "WHERE Enrollments.courseID = " + courseID + ";";

        try {
            ResultSet results = this.statement.executeQuery( query );

            results.next();

            return results.getString(1);


        } catch ( SQLException e ) {
            throw new Error("Couldn't get course information for " + courseID + ":" + e);
        }
    }

    public void closeConnection () {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch ( SQLException e ) {
            System.out.println( "Unable to close connection: " + e );
        }
    }


    public Query() {
        connection = null;
        DBURL = "jdbc:sqlite:students.db";

        try {
            connection = DriverManager.getConnection(DBURL);
            statement = connection.createStatement();
        } catch( SQLException e ) {
            throw new Error( "Unable to open connection: " + e );
        }
    }
}
