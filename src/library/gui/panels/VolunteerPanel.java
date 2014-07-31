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

import library.database.Volunteer;
import library.gui.VolunteerTableModel;

/**
 * @author Roland
 *
 */
public class VolunteerPanel extends JPanel implements ActionListener {
	private static JTable volunteerTable;
	private static JButton add;
	private static JButton remove;
	private static JButton search;
	
	public VolunteerPanel()	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));//adds 10px padding
		
		//instantiate table of volunteers
		//table uses VolunteerTableModel
		volunteerTable = new JTable(new VolunteerTableModel());
		JScrollPane tableScroll = new JScrollPane(volunteerTable);
		volunteerTable.setFillsViewportHeight(true);
		
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
		
		//add components to volunteerPanel
		add(tableScroll);
		add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add)	{
			System.out.println("add volunteer");
			AddVolunteerDialog asd = new AddVolunteerDialog();
		}
		else if(e.getSource() == remove)	{
			System.out.println("remove volunteer");
			if(JOptionPane.showConfirmDialog(this, "Are you sure you want to remove Volunteers?") == JOptionPane.YES_OPTION)	{
				VolunteerTableModel vtm = (VolunteerTableModel) volunteerTable.getModel();
				//delete selected rows, but backwards in order to prevent changing indices from affecting removal
				int[] ids = volunteerTable.getSelectedRows();
				for(int id = ids[ids.length - 1]; id >= ids[0]; id--)	{
					vtm.removeVolunteer(id);
				}
			}
		}
		else if(e.getSource() == search)	{
			System.out.println("search volunteer");
		}
	}
	
	private static class AddVolunteerDialog extends JDialog	{
		
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
		public AddVolunteerDialog()	{
			content = getContentPane();
			//init arrays
			int size = Volunteer.fields.size();
			fields = new JTextField[size];
			fieldLabels = new JLabel[size];
			panels = new JPanel[size];
			inputs = new HashMap<String, JTextField>();
			content.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			//init input panels with label and textfield
			for(int i = 0; i < size; i++)	{
				fieldLabels[i] = new JLabel(Volunteer.fields.get(i));
				fieldLabels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
				fields[i] = new JTextField(15);
				fields[i].setAlignmentX(Component.LEFT_ALIGNMENT);
				inputs.put(Volunteer.fields.get(i), fields[i]);//place text field into hashmap
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
					Volunteer v = new Volunteer();
					for(int i = 0; i < fields.length; i++)	{
						if(v.getAttribute(i) instanceof Integer)
							v.setAttribute(i, Integer.parseInt(fields[i].getText()));
						else if(v.getAttribute(i) instanceof Long)
							v.setAttribute(i, Long.parseLong(fields[i].getText()));
						else
							v.setAttribute(i, fields[i].getText());
					}
					VolunteerTableModel vtm = (VolunteerTableModel) volunteerTable.getModel();
					vtm.addVolunteer(v);
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
