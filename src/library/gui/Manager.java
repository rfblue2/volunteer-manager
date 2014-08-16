/**
 * 
 */
package library.gui;

import java.awt.Container;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import library.gui.panels.HomePanel;
import library.gui.panels.PairingPanel;
import library.gui.panels.StudentPanel;
import library.gui.panels.VolunteerPanel;

/**
 * @author Roland
 *
 */
public class Manager {
	
	public static final double version = 0.1;
	
	//Panels
	private static JPanel masterPanel;
	private static JTabbedPane tabPanel;
	private static HomePanel pHome;
	private static StudentPanel pStudents;
	private static VolunteerPanel pVolunteers;
	private static PairingPanel pPairing;
	
	private static Font arial16;
	
	/**
	 * Creates MasterPanel, which houses tabs
	 * @return
	 */
	private Container createContentPane() {
		//initialize panels
		masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		tabPanel = new JTabbedPane();
		pHome = new HomePanel();
		pStudents = new StudentPanel();
		pVolunteers = new VolunteerPanel();
		pPairing = new PairingPanel();
		
		arial16 = new Font("Arial", Font.PLAIN, 16);
		
		//add them to tabs
		tabPanel.addTab("Home", pHome);
		tabPanel.addTab("Students", pStudents);
		tabPanel.addTab("Volunteers", pVolunteers);
		tabPanel.addTab("Pairing", pPairing);
		
		tabPanel.setFont(arial16);
		
		masterPanel.add(tabPanel);
		//add date
		JLabel time = new JLabel(new SimpleDateFormat("MMMM dd, yyyy").format(Calendar.getInstance().getTime()));
		masterPanel.add(time);//the date and time
		
		return masterPanel;
	}

	/**
	 * Creates frame that houses GUI
	 */
	public static void createAndShowGUI()	{
		//use System Look and Feel (i.e. on Windows it looks like Windows
		try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e)	{
			e.printStackTrace();
		}
		
		//Create Instance of this program
		Manager manager = new Manager();
		
		//Initialize Frame to house GUI
		JFrame frame = new JFrame("Volunteer Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(manager.createContentPane());
		frame.pack();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);//maximized window
		frame.setVisible(true);
	}

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()	{
				createAndShowGUI();
			}
		});
	}

}
