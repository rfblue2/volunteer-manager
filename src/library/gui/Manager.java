/**
 * 
 */
package library.gui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author Roland
 *
 */
public class Manager {
	
	JTabbedPane masterPanel;
	JPanel pHome;
	JPanel pStudents;
	JPanel pVolunteers;
	JPanel pPairing;
	
	private Container createContentPane() {
		masterPanel = new JTabbedPane();
		pHome = new JPanel();
		pStudents = new JPanel();
		pVolunteers = new JPanel();
		pPairing = new JPanel();
		
		masterPanel.addTab("Home", pHome);
		masterPanel.addTab("Students", pStudents);
		masterPanel.addTab("Volunteers", pVolunteers);
		masterPanel.addTab("Pairing", pPairing);
		return masterPanel;
	}

	public static void createAndShowGUI()	{
		try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e)	{
			e.printStackTrace();
		}
		Manager manager = new Manager();
		
		JFrame frame = new JFrame("Volunteer Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(manager.createContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	/**
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
