/**
 * 
 */
package library.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import library.database.DbManager;
import library.database.LetterMaker;
import library.database.Student;
import library.gui.StudentTableModel;

/**
 * @author Roland
 * Displays a table of students and actions
 */
public class StudentPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2980697251806384042L;
	private static JTable studentTable;
	private static JButton add;
	private static JButton remove;
	private static JLabel filterLabel;
	private static JTextField filterText;
	private static JComboBox<String> filterFields;
	private static JButton genLetters;
	private static JButton genLabels;
	private Color lightRed;
	private TableRowSorter<StudentTableModel> sorter;
	private Font arial16;
	private JFileChooser fChooser;
	
	/**
	 * 
	 */
	public StudentPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));//adds 10px padding
		lightRed = new Color(255, 204, 204);
		setBackground(lightRed);
		arial16 = new Font("Arial", Font.PLAIN, 16);
		
		//instantiate table of students
		//table uses StudentTableModel
		StudentTableModel stm = new StudentTableModel();
		sorter = new TableRowSorter<StudentTableModel>(stm);
		studentTable = new JTable(stm);
		studentTable.setRowSorter(sorter);
		studentTable.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
		JScrollPane tableScroll = new JScrollPane(studentTable);
		studentTable.setFillsViewportHeight(true);
		studentTable.getTableHeader().setFont(arial16);
		
		//instantiate buttons
		JPanel buttonPanel = new JPanel();//holds the buttons
		buttonPanel.setBackground(lightRed);
		add = new JButton("Add");
		add.addActionListener(this);
		add.setFont(arial16);
		remove = new JButton("Remove");
		remove.addActionListener(this);
		remove.setFont(arial16);
		filterLabel = new JLabel("Filter: ");
		filterLabel.setFont(arial16);
		filterFields = new JComboBox<String>(DbManager.getFields(DbManager.STUDENTS).toArray(new String[DbManager.getFields(DbManager.STUDENTS).size()]));
		filterFields.addActionListener(this);
		filterFields.setFont(arial16);
		filterText = new JTextField(15);
		filterText.setFont(arial16);
		filterText.getDocument().addDocumentListener(new DocumentListener()	{
			@Override
			public void changedUpdate(DocumentEvent e) {newFilter();}
			@Override
			public void insertUpdate(DocumentEvent e) {newFilter();}
			@Override
			public void removeUpdate(DocumentEvent e) {newFilter();}
			
		});
		genLetters = new JButton("Generate Letters");
		genLetters.addActionListener(this);
		genLetters.setFont(arial16);
		
		genLabels = new JButton("Generate Labels");
		genLabels.addActionListener(this);
		genLabels.setFont(arial16);
		
		fChooser = new JFileChooser();
		
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(filterLabel);
		buttonPanel.add(filterFields);
		buttonPanel.add(filterText);
		buttonPanel.add(genLetters);
		buttonPanel.add(genLabels);
		
		//add components to studentPanel
		add(tableScroll);
		add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add)	{
			System.out.println("add student");
			new AddStudentDialog();
		}
		else if(e.getSource() == remove)	{
			System.out.println("remove student");
			if(JOptionPane.showConfirmDialog(this, "Are you sure you want to remove students?") == JOptionPane.YES_OPTION)	{
				StudentTableModel stm = (StudentTableModel) studentTable.getModel();
				//delete selected rows, but backwards in order to prevent changing indices from affecting removal
				int[] rows = studentTable.getSelectedRows();
				for(int r = rows[rows.length - 1]; r >= rows[0]; r--)	{
					int newR = studentTable.convertRowIndexToModel(r);
					if(!DbManager.getStudents().get(newR).getAttribute("Volunteer").equals(""))	{
						PairingPanel.unpair(DbManager.getStudents().get(newR));
					}
					stm.removeStudent((Integer.parseInt(studentTable.getModel().getValueAt(newR, 0).toString())));//gets the ID
				}
			}
		}
		else if(e.getSource() == genLetters)	{
			File sFolder = null;
			String path = null;
			int val = fChooser.showSaveDialog(fChooser);
			if(val == JFileChooser.APPROVE_OPTION)	{
				path = fChooser.getSelectedFile().getPath();
				sFolder = new File(path);
			}
			
			try	{
				sFolder.mkdir();
			} catch(SecurityException e1)	{
				e1.printStackTrace();
			}
			
			int[] rows = studentTable.getSelectedRows();
			if(rows.length == 0)	{
				JOptionPane.showMessageDialog(this, "Please select at least one student", "Error", JOptionPane.ERROR_MESSAGE);
			} else	{
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				for(int r : rows)	{
					int newR = studentTable.convertRowIndexToModel(r);
					LetterMaker.genStudentNotification(DbManager.getStudents().get(newR), path);
				}
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "Generating Letters Complete", "Finished", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if(e.getSource() == genLabels)	{
			String path = null;
			int val = fChooser.showSaveDialog(fChooser);
			if(val == JFileChooser.APPROVE_OPTION)
				path = fChooser.getSelectedFile().getPath() + ".docx";
			int[] rows = studentTable.getSelectedRows();
			if(rows.length == 0)	{
				JOptionPane.showMessageDialog(this, "Please select at least one student", "Error", JOptionPane.ERROR_MESSAGE);
			} else	{
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				ArrayList<Student> stus = new ArrayList<Student>();
				for(int r : rows)	{
					int newR = studentTable.convertRowIndexToModel(r);
					stus.add(DbManager.getStudents().get(newR));
				}//TODO alphabetize
				LetterMaker.genLabels(stus, path);
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "Generating Labels Complete", "Finished", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * Creates new filter for input text and chosen field
	 */
	private void newFilter()	{
		RowFilter<StudentTableModel, Object> rf = null;
		try	{
			rf = RowFilter.regexFilter(filterText.getText(),  filterFields.getSelectedIndex());
		} catch (PatternSyntaxException e)	{
			return;
		}
		sorter.setRowFilter(rf);
	}
	
	/**
	 * @return table model of students
	 */
	public static StudentTableModel getStudentTableModel()	{
		return (StudentTableModel) studentTable.getModel();
	}
	
	/**
	 * Displays a dialog to add students
	 * @author Roland
	 *
	 */
	private static class AddStudentDialog extends JDialog	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2974940935633723763L;
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
			fields[DbManager.getFields(DbManager.STUDENTS).indexOf("ID")].setEditable(false);//do not allow editing ID
			fields[DbManager.getFields(DbManager.STUDENTS).indexOf("ID")].setText(""+(DbManager.getStudents().size() + 1));
			fields[DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer")].setEditable(false);//do not allow editing pair this way
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
