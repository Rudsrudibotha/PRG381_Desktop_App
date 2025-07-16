import javax.swing.JFrame;

/**
 *
 * @author rudsr
 */
public class mainFrame extends JFrame {

    public static dbConnection db = new dbConnection();

    public mainFrame() {
        setTitle("Wellness Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Add Swing GUI components
    }

    public static void main(String[] args) {
        try {
            db.connect();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            new mainFrame().setVisible(true);
        });
    }
}
