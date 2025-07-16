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
    public void connect() throws ClassNotFoundException {
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

    // Create the tables required by the Wellness Management System
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
    // Batch insert counselors Table test data 
    public void insertCounselorsTestDataBatch() {
        String sql = "INSERT INTO Counselors (Name, Specialization, Availability) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            String[][] counselors = {
                {"Dr. Thandi Mokoena", "Anxiety", "Mon-Fri 09:00-12:00"},
                {"Ms. Zanele Nkosi", "Depression", "Mon-Wed 13:00-16:00"},
                {"Mr. Sipho Dlamini", "Stress Management", "Tue-Thu 10:00-14:00"},
                {"Dr. Lindiwe Khumalo", "Grief", "Wed-Fri 08:00-11:00"},
                {"Ms. Lerato Molefe", "Relationship Issues", "Mon 08:00-15:00"},
                {"Mr. Themba Mthethwa", "Time Management", "Thu 10:00-14:00"},
                {"Dr. Nandi Maseko", "Career Guidance", "Fri 09:00-12:00"},
                {"Ms. Ayanda Shabalala", "Exam Stress", "Tue 09:00-13:00"},
                {"Mr. Kabelo Khosa", "Bullying", "Wed-Fri 11:00-15:00"},
                {"Dr. Sibusiso Ntuli", "Social Anxiety", "Mon-Wed 10:00-12:00"},
                {"Ms. Nokuthula Dube", "Self-Esteem", "Tue 11:00-14:00"},
                {"Mr. Vusi Madonsela", "Sleep Issues", "Thu 13:00-15:00"},
                {"Dr. Nomvula Mbatha", "Family Conflict", "Fri 09:00-11:00"},
                {"Ms. Precious Msimango", "Depression", "Mon-Wed 10:00-13:00"},
                {"Mr. Andile Zwane", "Anxiety", "Tue-Thu 08:00-12:00"},
                {"Dr. Thabo Mabuza", "Stress Management", "Mon-Fri 09:00-11:00"},
                {"Ms. Phindile Nxumalo", "Relationship Issues", "Wed-Fri 14:00-17:00"},
                {"Mr. Sanele Khumalo", "Career Guidance", "Tue 10:00-13:00"},
                {"Dr. Lerato Sithole", "Grief", "Mon-Wed 08:00-10:00"},
                {"Ms. Zinhle Cele", "Exam Stress", "Thu 09:00-12:00"},
                {"Mr. Siyabonga Mthembu", "Bullying", "Fri 11:00-14:00"},
                {"Dr. Nkosinathi Gumede", "Social Anxiety", "Tue-Thu 10:00-12:00"},
                {"Ms. Thandeka Mlangeni", "Family Conflict", "Mon 13:00-16:00"},
                {"Mr. Themba Dube", "Depression", "Wed-Fri 09:00-12:00"},
                {"Dr. Ayanda Mkhize", "Self-Esteem", "Tue-Thu 14:00-17:00"},
                {"Ms. Nonhlanhla Khumalo", "Career Guidance", "Mon-Fri 08:00-10:00"},
                {"Mr. Bongani Zulu", "Anxiety", "Wed 10:00-13:00"},
                {"Dr. Siphelele Mthembu", "Stress Management", "Thu 09:00-12:00"},
                {"Ms. Lethabo Mokoena", "Exam Stress", "Fri 13:00-16:00"},
                {"Mr. Sibusiso Ndlovu", "Bullying", "Mon-Wed 09:00-11:00"}
            };
            // sets parameters to be inserted 
            for (String[] c : counselors) {
                pstmt.setString(1, c[0]);
                pstmt.setString(2, c[1]);
                pstmt.setString(3, c[2]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Inserted counselors test data.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Batch insert appointments test data
    public void insertAppointmentsTestDataBatch() {
        String sql = "INSERT INTO Appointments (StudentName, CounselorID, AppointmentDate, AppointmentTime, Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            String[] students = {
                "Kagiso Molefe", "Naledi Khumalo", "Lerato Mokoena", "Sipho Dlamini", "Andiswa Dube",
                "Lwazi Nkosi", "Sibusiso Zwane", "Nokuthula Maseko", "Phindile Ndlovu", "Themba Sithole",
                "Zanele Mthembu", "Ayanda Gumede", "Bongani Mkhize", "Nandi Khosa", "Thabo Zwane",
                "Precious Dlamini", "Vusi Mthethwa", "Nonhlanhla Nkosi", "Sanele Khumalo", "Nomvula Mokoena",
                "Siyabonga Dube", "Andile Sithole", "Zinhle Zwane", "Phila Mthembu", "Nokwazi Gumede",
                "Lethabo Mkhize", "Ayanda Khosa", "Sibusiso Zwane", "Zanele Dlamini", "Thandi Mokoena"
            };

            String[] dates = {
                "2025-07-01", "2025-07-02", "2025-07-03", "2025-07-04", "2025-07-05",
                "2025-07-06", "2025-07-07", "2025-07-08", "2025-07-09", "2025-07-10",
                "2025-07-11", "2025-07-12", "2025-07-13", "2025-07-14", "2025-07-15",
                "2025-07-16", "2025-07-17", "2025-07-18", "2025-07-19", "2025-07-20",
                "2025-07-21", "2025-07-22", "2025-07-23", "2025-07-24", "2025-07-25",
                "2025-07-26", "2025-07-27", "2025-07-28", "2025-07-29", "2025-07-30"
            };

            String[] times = {
                "09:00:00", "10:00:00", "11:00:00", "13:00:00", "14:00:00",
                "15:00:00", "09:30:00", "10:30:00", "11:30:00", "13:30:00",
                "14:30:00", "15:30:00", "09:00:00", "10:00:00", "11:00:00",
                "13:00:00", "14:00:00", "15:00:00", "09:30:00", "10:30:00",
                "11:30:00", "13:30:00", "14:30:00", "15:30:00", "09:00:00",
                "10:00:00", "11:00:00", "13:00:00", "14:00:00", "15:00:00"
            };

            String[] statuses = {
                "Scheduled", "Completed", "Cancelled", "Scheduled", "Scheduled",
                "Completed", "Cancelled", "Scheduled", "Scheduled", "Completed",
                "Cancelled", "Scheduled", "Scheduled", "Completed", "Cancelled",
                "Scheduled", "Scheduled", "Completed", "Cancelled", "Scheduled",
                "Scheduled", "Completed", "Cancelled", "Scheduled", "Scheduled",
                "Completed", "Cancelled", "Scheduled", "Scheduled", "Completed"
            };

            for (int i = 0; i < 30; i++) {
                pstmt.setString(1, students[i]);
                pstmt.setInt(2, (i % 10) + 1);  // CounselorID 1 to 10 repeating
                pstmt.setDate(3, java.sql.Date.valueOf(dates[i]));
                pstmt.setTime(4, java.sql.Time.valueOf(times[i]));
                pstmt.setString(5, statuses[i]);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Inserted appointments test data.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Batch test data inserted for Feedback table 
    public void insertFeedbackTestDataBatch() {
        String sql = "INSERT INTO Feedback (StudentName, Rating, Comments) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            String[][] feedbacks = {
                {"Kagiso Molefe", "5", "Very helpful and understanding."},
                {"Naledi Khumalo", "4", "Good session."},
                {"Lerato Mokoena", "3", "Okay session."},
                {"Sipho Dlamini", "4", "Helpful tips."},
                {"Andiswa Dube", "5", "Excellent guidance."},
                {"Lwazi Nkosi", "3", "Average."},
                {"Sibusiso Zwane", "4", "Good advice."},
                {"Nokuthula Maseko", "5", "Highly recommend."},
                {"Phindile Ndlovu", "2", "Not satisfied."},
                {"Themba Sithole", "4", "Good counselor."},
                {"Zanele Mthembu", "5", "Excellent."},
                {"Ayanda Gumede", "3", "Could improve."},
                {"Bongani Mkhize", "4", "Helpful."},
                {"Nandi Khosa", "5", "Very caring."},
                {"Thabo Zwane", "4", "Good experience."},
                {"Precious Dlamini", "5", "Loved the session."},
                {"Vusi Mthethwa", "3", "Okay service."},
                {"Nonhlanhla Nkosi", "4", "Good advice."},
                {"Sanele Khumalo", "5", "Excellent support."},
                {"Nomvula Mokoena", "4", "Helpful."},
                {"Siyabonga Dube", "3", "Average."},
                {"Andile Sithole", "5", "Very good."},
                {"Zinhle Zwane", "4", "Good."},
                {"Phila Mthembu", "3", "Okay."},
                {"Nokwazi Gumede", "5", "Excellent."},
                {"Lethabo Mkhize", "4", "Good."},
                {"Ayanda Khosa", "3", "Average."},
                {"Sibusiso Zwane", "5", "Excellent."},
                {"Zanele Dlamini", "4", "Good."},
                {"Thandi Mokoena", "3", "Okay."}
            };

            for (String[] f : feedbacks) {
                pstmt.setString(1, f[0]); // sets first parameter Student Name
                pstmt.setInt(2, Integer.parseInt(f[1])); // parse string to intiger second paramter
                pstmt.setString(3, f[2]); // sets comments as the third paramater
                pstmt.addBatch(); // sets batch to be executed later
            }

            pstmt.executeBatch(); // Runs all batch data at once 
            System.out.println("Inserted feedback Table test data.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
