import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles connection to the embedded Apache Derby database.
 * Creates the database "WellnessDB" if it does not exist.
 */
public class dbConnection {
    // Apache Derby driver class
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    // JDBC URL for the embedded database
    private static final String JDBC_URL = "jdbc:derby:WellnessDB;create=true";

    // Connection object
    private Connection con;

    // Constructor
    public dbConnection() {}

    // Connects to the database
    public void connect() throws ClassNotFoundException {
        try {
            // Load the driver class
            Class.forName(DRIVER);

            // Establish the connection
            this.con = DriverManager.getConnection(JDBC_URL);

            if (this.con != null) {
                System.out.println("Connected to DB");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

   //Getter to access the connection object
    public Connection getConnection() {
        return this.con;
    }

    // Method to close the connection
    public void closeConnection() {
        try {
            if (this.con != null && !this.con.isClosed()) {
                this.con.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void createTables() {
        String createCounselorsT = "CREATE TABLE Counselors ("
                + "CounselorID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "Name VARCHAR(100) NOT NULL, "
                + "Specialization VARCHAR(100) NOT NULL, "
                + "Availability VARCHAR(50) NOT NULL"
                + ")";

        String createAppointmentsT = "CREATE TABLE Appointments ("
                + "AppointmentID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "StudentName VARCHAR(100) NOT NULL, "
                + "CounselorID INT, "
                + "AppointmentDate DATE NOT NULL, "
                + "AppointmentTime TIME NOT NULL, "
                + "Status VARCHAR(50) DEFAULT 'Scheduled', "
                + "FOREIGN KEY (CounselorID) REFERENCES Counselors(CounselorID)"
                + ")";

        String createFeedbackT = "CREATE TABLE Feedback ("
                + "FeedbackID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "StudentName VARCHAR(100) NOT NULL, "
                + "Rating INT CHECK (Rating >= 1 AND Rating <= 5), "
                + "Comments VARCHAR(255)"
                + ")";

        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createCounselorsT);
            stmt.executeUpdate(createAppointmentsT);
            stmt.executeUpdate(createFeedbackT);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Tables already exist.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
