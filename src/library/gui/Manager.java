/**
 * 
 */
package library.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
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
	
	//Panels
	private static JTabbedPane masterPanel;
	private static HomePanel pHome;
	private static StudentPanel pStudents;
	private static VolunteerPanel pVolunteers;
	private static PairingPanel pPairing;
	
	/**
	 * Creates MasterPanel, which houses tabs
	 * @return
	 */
	private Container createContentPane() {
		//initialize panels
		masterPanel = new JTabbedPane();
		pHome = new HomePanel();
		pStudents = new StudentPanel();
		pVolunteers = new VolunteerPanel();
		pPairing = new PairingPanel();
		
		//add them to tabs
		masterPanel.addTab("Home", pHome);
		masterPanel.addTab("Students", pStudents);
		masterPanel.addTab("Volunteers", pVolunteers);
		masterPanel.addTab("Pairing", pPairing);
		
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
