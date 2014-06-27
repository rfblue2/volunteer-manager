/**
 * 
 */
package library.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

/**
 * @author Roland
 *
 */
public class StudentPanel extends JPanel implements ActionListener {
	
	private JTable studentTable;
	private JButton add;
	private JButton remove;
	private JButton search;
	
	/**
	 * 
	 */
	public StudentPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));//adds 10px padding
		
		//instantiate table of students
		//table uses StudentTableModel
		studentTable = new JTable(new StudentTableModel());
		JScrollPane tableScroll = new JScrollPane(studentTable);
		studentTable.setFillsViewportHeight(true);
		
		//instantiate buttons
		JPanel buttonPanel = new JPanel();//holds the buttons
		add = new JButton("Add");
		add.addActionListener(this);
		remove = new JButton("Remove");
		remove.addActionListener(this);
		search = new JButton("Search");
		search.addActionListener(this);
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(search);
		
		//add components to studentPanel
		add(tableScroll);
		add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add)	{
			System.out.println("add student");
		}
		else if(e.getSource() == remove)	{
			System.out.println("remove student");
		}
		else if(e.getSource() == search)	{
			System.out.println("search student");
		}
	}
	
	
}
