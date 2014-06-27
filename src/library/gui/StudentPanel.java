/**
 * 
 */
package library.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Roland
 *
 */
public class StudentPanel extends JPanel {
	
	private JTable studentTable;
	private JScrollPane tableScroll;//adds a scroll bar
	private JButton add;
	private JButton remove;
	private JButton search;
	
	/**
	 * 
	 */
	public StudentPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		//instantiate table of students
		studentTable = new JTable(new StudentTableModel());
		tableScroll = new JScrollPane(studentTable);
		studentTable.setFillsViewportHeight(true);
		
		//instantiate buttons
		add = new JButton("Add");
		remove = new JButton("Remove");
		search = new JButton("Search");
		
		//add components to container
		add(tableScroll);
		add(add);
		add(remove);
		add(search);
	}
	
	
}
