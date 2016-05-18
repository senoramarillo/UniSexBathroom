import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Application demonstrating a fair solution to the "Unisex Bathroom" problem.
 * Creates the GUI and upon clicking "Start", initialises the {@link Bathroom} and populates
 * the queue with females and males.
 * <p/>
 * A random arrival pattern is used.
 *
 * @author Cormac Redmond -- credmond85 /at/ gmail
 */
public class RunBathroom extends JFrame implements Runnable, ActionListener {
    private JLabel maleAverageWaiting, femaleAverageWaiting, totalMales,
            totalFemales, totalUsers;
    private JButton startItem = new JButton("Start");
    private JButton quit = new JButton("Quit");
    private Bathroom bathroom;

    // Boolean representing the gender currently occupying the bathroom; true indicates male, false indicates female.
    private boolean isMale = true;

    public static void main(String[] args) {
        new Thread(new RunBathroom()).start();
    }

    /**
     * Constructor which sets up all the necessary components of the GUI.
     */
    public RunBathroom() {

        super("Unisex Toilet Problem");
        setSize(700, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel bottomPanel = new JPanel();

        // Add start button
        startItem.setText("Start");
        startItem.addActionListener(this);
        bottomPanel.add(startItem);
        startItem.setEnabled(false);

        // Add quit button
        quit.addActionListener(this);
        bottomPanel.add(quit);
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        maleAverageWaiting = new JLabel("0");
        maleAverageWaiting.setHorizontalAlignment(SwingConstants.CENTER);
        femaleAverageWaiting = new JLabel("0");
        femaleAverageWaiting.setHorizontalAlignment(SwingConstants.CENTER);
        totalMales = new JLabel("0");
        totalMales.setHorizontalAlignment(SwingConstants.CENTER);
        totalFemales = new JLabel("0");
        totalFemales.setHorizontalAlignment(SwingConstants.CENTER);
        totalUsers = new JLabel("0");
        totalUsers.setHorizontalAlignment(SwingConstants.CENTER);

        // Add labels
        topPanel.add(new JLabel("Average male queuing time: "));
        topPanel.add(maleAverageWaiting);
        topPanel.add(new JLabel("Average female queuing time: "));
        topPanel.add(femaleAverageWaiting);
        topPanel.add(new JLabel("Total males: "));
        topPanel.add(totalMales);
        topPanel.add(new JLabel("Total females: "));
        topPanel.add(totalFemales);
        topPanel.add(new JLabel("Total number of persons: "));
        topPanel.add(totalUsers);
        mainPanel.add(topPanel);
        Container contentPane = getContentPane();
        contentPane.add(mainPanel, "Center");
        contentPane.add(bottomPanel, "South");
        startItem.setEnabled(true);
    }

    /**
     * When the thread starts, display the GUI.
     */
    public void run() {
        setVisible(true);
    }

    /**
     * Exits on close of window
     *
     * @param e Windowevent (close)
     */
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    /**
     * Listens for mouse-presses on menu items.
     *
     * @param evt The ActionEvent performed
     */
    public void actionPerformed(ActionEvent evt) {

        Object source = evt.getSource();

        // If Quit is clicked, exit program.
        if (source == quit) {
            System.exit(0);
        }
        // If Start is clicked, create a bathroom and add male and female
        // persons to it.
        else if (source == startItem) {
            startItem.setEnabled(false);
            bathroom = new Bathroom(this);
            for (int i = 0; i < 10; i++) {
                new Thread(new Person(bathroom, !isMale)).start();
                new Thread(new Person(bathroom, isMale)).start();
            }
        }
    }

    /**
     * Updates the labels on the GUI
     *
     * @param isMale      Sex referring to the label to update
     * @param averageTime The average time that gender has had to wait
     * @param totalOfSex  The total number of that gender which has used to bathroom
     */
    public void update(boolean isMale, long averageTime, int totalOfSex) {
        if (isMale) {
            maleAverageWaiting.setText(Long.toString(averageTime));
            totalMales.setText(Integer.toString(totalOfSex));
        } else {
            femaleAverageWaiting.setText(Long.toString(averageTime));
            totalFemales.setText(Integer.toString(totalOfSex));
        }
        totalFemales.getText();
        totalUsers.setText(Integer.toString(Integer.parseInt((totalFemales.getText()))
                + Integer.parseInt((totalMales.getText()))));
    }
}
