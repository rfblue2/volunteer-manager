/**
 * 
 */
package library.gui.panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import library.database.Student;
import library.gui.StudentTableModel;

/**
 * @author Roland
 *
 */
public class StudentPanel extends JPanel implements ActionListener {
	private static JTable studentTable;
	private static JButton add;
	private static JButton remove;
	private static JButton search;
	
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
			AddStudentDialog asd = new AddStudentDialog();
		}
		else if(e.getSource() == remove)	{
			System.out.println("remove student");
			if(JOptionPane.showConfirmDialog(this, "Are you sure you want to remove students?") == JOptionPane.YES_OPTION)	{
				StudentTableModel stm = (StudentTableModel) studentTable.getModel();
				//delete selected rows, but backwards in order to prevent changing indices from affecting removal
				int[] ids = studentTable.getSelectedRows();
				for(int id = ids[ids.length - 1]; id >= ids[0]; id--)	{
					stm.removeStudent(id);
				}
			}
		}
		else if(e.getSource() == search)	{
			System.out.println("search student");
		}
	}
	
	private static class AddStudentDialog extends JDialog	{
		
		private Container content;
		private JTextField[] fields;
		private JLabel[] fieldLabels;
		private JPanel[] panels;
		private HashMap<String, JTextField> inputs;
		private JButton ok;
		private JButton cancel;
		private JPanel buttons;
		
		/**
		 * Constructor inits GUI
		 */
		public AddStudentDialog()	{
			content = getContentPane();
			//init arrays
			int size = Student.fields.size();
			fields = new JTextField[size];
			fieldLabels = new JLabel[size];
			panels = new JPanel[size];
			inputs = new HashMap<String, JTextField>();
			content.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			//init input panels with label and textfield
			for(int i = 0; i < size; i++)	{
				fieldLabels[i] = new JLabel(Student.fields.get(i));
				fieldLabels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
				fields[i] = new JTextField(15);
				fields[i].setAlignmentX(Component.LEFT_ALIGNMENT);
				inputs.put(Student.fields.get(i), fields[i]);//place text field into hashmap
				panels[i] = new JPanel();
				panels[i].add(fieldLabels[i]);
				panels[i].add(fields[i]);
				panels[i].setAlignmentY(Component.BOTTOM_ALIGNMENT);
				content.add(panels[i]);//add panel into the container
			}
			//init buttons
			buttons = new JPanel();
			ok = new JButton("Ok");
			ok.addActionListener(new ActionListener()	{
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO check input?
					Student s = new Student();
					for(int i = 0; i < fields.length; i++)	{
						if(s.getAttribute(i) instanceof Integer)
							s.setAttribute(i, Integer.parseInt(fields[i].getText()));
						else if(s.getAttribute(i) instanceof Long)
							s.setAttribute(i, Long.parseLong(fields[i].getText()));
						else
							s.setAttribute(i, fields[i].getText());
					}
					StudentTableModel stm = (StudentTableModel) studentTable.getModel();
					stm.addStudent(s);
					dispose();
				}
			});
			cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener()	{
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			buttons.add(ok);
			buttons.add(cancel);
			content.add(buttons);
			
			setAlwaysOnTop(true);
			setAutoRequestFocus(true);
			setVisible(true);
			setEnabled(true);
			this.pack();
		}
		
	}
	
}
