import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles connection to the embedded Apache Derby database.
 * Creates the database "WellnessDB" if it does not exist.
 * Also creates necessary tables and inserts test data.
 * 
 * @author rudsr
 */
public class dbConnection {
    // Apache Derby driver class
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    // JDBC URL for the embedded database
    private static final String JDBC_URL = "jdbc:derby:WellnessDB;create=true";

    // Connection object to connect to the database
    private Connection con;

    // Connect to the database
    public void connect() throws ClassNotFoundException{
        try {
            // Load the driver class
            Class.forName(DRIVER);

            // Establish the connection
            this.con = DriverManager.getConnection(JDBC_URL);

            System.out.println("Connected to DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter to access the connection object
    public Connection getConnection() {
        return con;
    }
    
    // Create the tables required
 // Create the tables required
    public void createTables() {
        try (Statement stmt = con.createStatement()) {
            // Create Counselors Table
            stmt.executeUpdate(
                "CREATE TABLE Counselors (" +
                "CounselorID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "Name VARCHAR(100), " +
                "Specialization VARCHAR(100), " +
                "Availability VARCHAR(100))"
            );
            System.out.println("Counselors table created.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) { // Table already exists
                System.out.println("Counselors table already exists.");
            } else {
                e.printStackTrace();
            }
        }

        try (Statement stmt = con.createStatement()) {
            // Create Appointments Table
            stmt.executeUpdate(
                "CREATE TABLE Appointments (" + // Opens SQL Statement
                "AppointmentID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "StudentName VARCHAR(100), " +
                "CounselorID INT, " +
                "AppointmentDate DATE, " +
                "AppointmentTime TIME, " +
                "Status VARCHAR(50), " +
                "FOREIGN KEY (CounselorID) REFERENCES Counselors(CounselorID))"
            );
            System.out.println("Appointments table created.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Appointments table already exists.");
            } else {
                e.printStackTrace();
            }
        }

        try (Statement stmt = con.createStatement()) {
            // Create Feedback Table
            stmt.executeUpdate(
                "CREATE TABLE Feedback (" +
                "FeedbackID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "StudentName VARCHAR(100), " +
                "Rating INT, " +
                "Comments VARCHAR(255))"
            );
            System.out.println("Feedback table created.");
            
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Feedback table already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
