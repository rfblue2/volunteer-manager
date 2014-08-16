/**
 * 
 */
package library.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import library.database.DbManager;
import library.database.Pair;
import library.database.StringUtility;
import library.database.Student;
import library.database.Volunteer;
import library.gui.PairTableModel;
import library.gui.StudentTableModel;
import library.gui.VolunteerTableModel;

/**
 * @author Roland
 *
 */
public class PairingPanel extends JPanel implements ActionListener	{

	private static JTable studentTable;//unpaired students
	private static JTable volunteerTable;//unpaired volunteers
	private static TableRowSorter<StudentTableModel> studentSorter;
	private static TableRowSorter<VolunteerTableModel> volunteerSorter;
	private static JTable pairTable;
	private Color lightPurple;
	private Font arial16;
	
	private JPanel buttons;
	private JButton autoPair;
	private JButton manPair;
	private JButton unpair;
	
	public PairingPanel()	{
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lightPurple = new Color(229, 204, 255);
		setBackground(lightPurple);
		arial16 = new Font("Arial", Font.PLAIN, 16);
		
		JPanel tables = new JPanel();
		tables.setLayout(new BoxLayout(tables, BoxLayout.X_AXIS));
		tables.setBackground(lightPurple);
		studentSorter = new TableRowSorter<StudentTableModel>(StudentPanel.getStudentTableModel());
		studentTable = new JTable(StudentPanel.getStudentTableModel());
		studentTable.setFillsViewportHeight(true);
		studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		studentTable.setRowSorter(studentSorter);
		studentTable.getTableHeader().setFont(arial16);
		RowFilter<Object, Object> rfs = null;
		try	{
			rfs = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer")));
		} catch (PatternSyntaxException e)	{
			return;
		} catch (NullPointerException e)	{
			JOptionPane.showMessageDialog(this, "Is there a \"Volunteer\" field for students?", "Error", JOptionPane.ERROR_MESSAGE);
		}
		studentSorter.setRowFilter(rfs);
		
		volunteerSorter = new TableRowSorter<VolunteerTableModel>(VolunteerPanel.getVolunteerTableModel());
		volunteerTable = new JTable(VolunteerPanel.getVolunteerTableModel());
		volunteerTable.setFillsViewportHeight(true);
		volunteerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		volunteerTable.setRowSorter(volunteerSorter);
		volunteerTable.getTableHeader().setFont(arial16);
		RowFilter<Object, Object> rfv = null;
		try	{
			rfv = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student")));
		} catch (PatternSyntaxException e)	{
			return;
		} catch (NullPointerException e)	{
			JOptionPane.showMessageDialog(this, "Is there a \"Student\" field for volunteers?", "Error", JOptionPane.ERROR_MESSAGE);
		}
		volunteerSorter.setRowFilter(rfv);	
		
		pairTable = new JTable();
		pairTable.setModel(new PairTableModel());
		pairTable.setFillsViewportHeight(true);
		pairTable.getTableHeader().setFont(arial16);
		
		//add pairs to the table
		putPairsToPairTable();

		JPanel studentPanel = new JPanel();
		studentPanel.setLayout(new BorderLayout());
		JLabel unpairedStudents = new JLabel("Unpaired Students");
		unpairedStudents.setFont(arial16);
		studentPanel.add(unpairedStudents, BorderLayout.PAGE_START);
		studentPanel.add(new JScrollPane(studentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.LINE_END);
		studentPanel.setBackground(lightPurple);
		
		JPanel volunteerPanel = new JPanel();
		volunteerPanel.setLayout(new BorderLayout());
		JLabel unpairedVolunteers = new JLabel("Unpaired Volunteers");
		unpairedVolunteers.setFont(arial16);
		volunteerPanel.add(unpairedVolunteers, BorderLayout.PAGE_START);
		volunteerPanel.add(new JScrollPane(volunteerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.LINE_END);
		volunteerPanel.setBackground(lightPurple);
		
		JPanel pairPanel = new JPanel();
		pairPanel.setLayout(new BorderLayout());
		JLabel pairs = new JLabel("Pairs");
		pairs.setFont(arial16);
		pairPanel.add(pairs, BorderLayout.PAGE_START);
		pairPanel.add(new JScrollPane(pairTable), BorderLayout.LINE_END);
		pairPanel.setBackground(lightPurple);
		
		tables.add(studentPanel);
		tables.add(pairPanel);
		tables.add(volunteerPanel);
		
		add(tables);
		
		buttons = new JPanel();
		buttons.setBackground(lightPurple);
		
		autoPair = new JButton("Automatically Pair");
		autoPair.addActionListener(this);
		autoPair.setEnabled(false);
		
		manPair = new JButton("Manually Pair");
		manPair.addActionListener(this);
		
		unpair = new JButton("Delete Pair(s)");
		unpair.addActionListener(this);
		
		buttons.add(autoPair);
		buttons.add(manPair);
		buttons.add(unpair);
		
		add(buttons);
	}

	/**
	 * Populates table of student-volunteer pairs
	 */
	private void putPairsToPairTable()	{
		ArrayList<Volunteer> volunteers = ((VolunteerTableModel)volunteerTable.getModel()).getData();
		for(Student s : ((StudentTableModel)studentTable.getModel()).getData())	{
			String vName = (String) s.getAttribute("Volunteer");
			if(vName != null)	{
				String[] name = vName.split(" ");
				for(Volunteer v : volunteers)	{
					if(v.getAttribute("First Name").equals(name[0])
							&& v.getAttribute("Last Name").equals(name[1]))	{
						((PairTableModel) pairTable.getModel()).addPair(s, v);
					}
				}
			}
		}
	}
/* ************************************************************************8	
	private void autoPair()	{
		//gale-shapley, note suitor optimal (i.e. better for students)
		//note these are temporary ArrayList, so make new ArrayList and then place data into them
		ArrayList<Student> uStu = new ArrayList<Student>(((StudentTableModel)studentTable.getModel()).getData());
		ArrayList<Volunteer> uVol = new ArrayList<Volunteer>(((VolunteerTableModel)volunteerTable.getModel()).getData());
		//holds list of currently unpaired students/volunteers
		ArrayList<Student> tStu = new ArrayList<Student>(((StudentTableModel)studentTable.getModel()).getData());
		ArrayList<Volunteer> tVol = new ArrayList<Volunteer>(((VolunteerTableModel)volunteerTable.getModel()).getData());
		ArrayList<Pair> ps = new ArrayList<Pair>();
		/*
		 * Algorithm 1:
		 * Cycle s through all v, then pick best one
		 * If s cannot find a v, increment a counter
		 * If the counter is equal to the size of unmatched s, exit loop
		 * 
		 * Algorithm 2:
		 * Find first v that satisfies s
		 * For all next s, skip paired v and only look at unpaired
		 * If cannot find a v, then search paired and swap out best match
		 * If still cannot find a v, increment counter
		 * If counter equal to size of unmatched s, exit loop
		 * 
		 *  Algorithm 2 variation
		 *  When searching paired, swap out first match that works
		 *  Problem: what if two continually swap out...?
		 *
		while(uStu.size() > 0 && uVol.size() > 0)	{
			Pair best = new Pair(uStu.get(0), tVol.get(0));
			//now cycle through all possibilities until get the best one
			
		}
	}
	
	/**
	 * Returns is pair1 is better than p2 (1), or vice versa (-1) or equal (0)
	 * @param p1
	 * @param p2
	 * @return
	 *
	private int compare(Pair p1, Pair p2)	{
		//Check requested volunteer/buddy (return either 1 or 0, otherwise continue)
		String[] name1 = ((String) p1.student.getAttribute("Volunteer")).split(" ");
		String[] name2 = ((String) p2.student.getAttribute("Volunteer")).split(" ");
		if(name1[0].equals(p1.volunteer.getAttribute("First Name")) && name1[1].equals(p1.volunteer.getAttribute("Last Name")))	{
			if(name2[0].equals(p2.volunteer.getAttribute("First Name")) && name2[1].equals(p2.volunteer.getAttribute("Last Name")))	{
				return 0;
			}
			return 1;
		} else if(name2[0].equals(p2.volunteer.getAttribute("First Name")) && name2[1].equals(p2.volunteer.getAttribute("Last Name")))	{
			return -1;
		}
		//Check grade (return 1 or -1 if a grade doesn't match)
		if(((String)p1.volunteer.getAttribute("Grade Preference")).contains((String)p1.student.getAttribute("Grade")))	{
			if(((String)p2.volunteer.getAttribute("Grade Preference")).contains((String)p2.student.getAttribute("Grade")))	{
				return 0;
			}
			return 1;
		}
		else if(((String)p2.volunteer.getAttribute("Grade Preference")).contains((String)p2.student.getAttribute("Grade")))	{
			return -1;
		}
		//Check availability
		//if availability is R
		if((p1.volunteer.getAttribute("Availability")).equals("R"))	{
			if((p2.volunteer.getAttribute("Availability")).equals("R"))
				return 0;
			return 1;
		}
		else if((p2.volunteer.getAttribute("Availability")).equals("R"))	{
			return -1;
		}
		//which set of letters overlaps more
		int p1Count = 0;
		for(char c : ((String) p1.student.getAttribute("Availability")).toCharArray())	{
			if(((String)p1.volunteer.getAttribute("Availability")).contains(c+""))	{
				p1Count++;
			}
		}
		int p2Count = 0;
		for(char c : ((String) p2.student.getAttribute("Availability")).toCharArray())	{
			if(((String)p2.volunteer.getAttribute("Availability")).contains(c+""))	{
				p2Count++;
			}
		}
		if(p1Count < p2Count)
			return 1;
		else if(p2Count > p2Count)
			return -1;
		return 0;
	}
*************************************************************************************** */
	/**
	 * Unpair the pair this volunteer is in
	 * @param v
	 */
	public static void unpair(Volunteer v)	{
		for(int i = 0; i < ((PairTableModel)pairTable.getModel()).getRowCount(); i++)	{
			Pair p = ((PairTableModel)pairTable.getModel()).getPair(i);
			if(p.volunteer.getAttribute("ID").equals(v.getAttribute("ID")))	{
				unpair(i);
				break;
			}
		}
	}

	/**
	 * Unpair the pair this student is in
	 * @param s
	 */
	public static void unpair(Student s)	{
		for(int i = 0; i < ((PairTableModel)pairTable.getModel()).getRowCount(); i++)	{
			Pair p = ((PairTableModel)pairTable.getModel()).getPair(i);
			if(p.student.getAttribute("ID").equals(s.getAttribute("ID")))	{
				unpair(i);
				break;
			}
		}
	}
	
	/**
	 * Unpairs student and volunteer given row number in paired table
	 * @param row
	 */
	public static void unpair(int row)	{
		Pair rPair = ((PairTableModel)pairTable.getModel()).getPair(row);
		((PairTableModel)pairTable.getModel()).removePair(row);
		//use instanceof for int and string
		for(int i = 0; i < studentTable.getModel().getRowCount(); i++)	{
			if(studentTable.getModel().getValueAt(i, DbManager.getFields(DbManager.STUDENTS).indexOf("ID")) instanceof String)	{
				if(studentTable.getModel().getValueAt(i, DbManager.getFields(
						DbManager.STUDENTS).indexOf("ID")).equals(rPair.student.getAttribute("ID")))	{
					studentTable.getModel().setValueAt("", i, DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer"));
					break;
				}
			}
			else	{
				if(studentTable.getModel().getValueAt(i, DbManager.getFields(
						DbManager.STUDENTS).indexOf("ID")) == rPair.student.getAttribute("ID"))	{
					studentTable.getModel().setValueAt("", i, DbManager.getFields(
							DbManager.STUDENTS).indexOf("Volunteer"));
					break;
				}
			}
		}
		for(int i = 0; i < volunteerTable.getModel().getRowCount(); i++)	{
			if(volunteerTable.getModel().getValueAt(i, DbManager.getFields(DbManager.VOLUNTEERS).indexOf("ID")) instanceof String)	{
				if(volunteerTable.getModel().getValueAt(i, DbManager.getFields(
						DbManager.VOLUNTEERS).indexOf("ID")).equals(rPair.volunteer.getAttribute("ID")))	{
					volunteerTable.getModel().setValueAt("", i, DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student"));
					break;
				}
			}
			else	{
				if(volunteerTable.getModel().getValueAt(i, DbManager.getFields(
						DbManager.VOLUNTEERS).indexOf("ID")) == rPair.volunteer.getAttribute("ID"))	{
					volunteerTable.getModel().setValueAt("", i, DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student"));
					break;
				}
			}
		}
		//refresh filters
		RowFilter<Object, Object> rfs = null;
		try	{
			rfs = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer")));
		} catch (PatternSyntaxException e1)	{
			e1.printStackTrace();
		}
		studentSorter.setRowFilter(rfs);
		RowFilter<Object, Object> rfv = null;
		try	{
			rfv = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student")));
		} catch (PatternSyntaxException e1)	{
			e1.printStackTrace();
		}
		volunteerSorter.setRowFilter(rfv);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == autoPair)	{
			
		}
		else if(e.getSource() == manPair)	{
			if(studentTable.getSelectedRowCount() != 1 || volunteerTable.getSelectedRowCount() != 1)	{
				JOptionPane.showMessageDialog(this, "Please select 1 volunteer and 1 student", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else	{
				int studentIndex = studentTable.convertRowIndexToModel(studentTable.getSelectedRow());
				int volunteerIndex = volunteerTable.convertRowIndexToModel(volunteerTable.getSelectedRow());
				Student selectedStudent = DbManager.getStudents().get(studentIndex);
				Volunteer selectedVolunteer = DbManager.getVolunteers().get(volunteerIndex);
				String vName = StringUtility.getFullName(selectedVolunteer);
				String sName = StringUtility.getFullName(selectedStudent);
				selectedStudent.setAttribute("Volunteer", vName);
				selectedVolunteer.setAttribute("Student", sName);
				studentTable.getModel().setValueAt(vName, studentIndex, DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer"));
				volunteerTable.getModel().setValueAt(sName, volunteerIndex, DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student"));
				((PairTableModel) pairTable.getModel()).addPair(selectedStudent, selectedVolunteer);
				//refresh filters
				RowFilter<Object, Object> rfs = null;
				try	{
					rfs = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.STUDENTS).indexOf("Volunteer")));
				} catch (PatternSyntaxException e1)	{
					e1.printStackTrace();
				}
				studentSorter.setRowFilter(rfs);
				RowFilter<Object, Object> rfv = null;
				try	{
					rfv = RowFilter.notFilter(RowFilter.regexFilter("..*",  DbManager.getFields(DbManager.VOLUNTEERS).indexOf("Student")));
				} catch (PatternSyntaxException e1)	{
					e1.printStackTrace();
				}
				volunteerSorter.setRowFilter(rfv);
			}
		}
		else if(e.getSource() == unpair)	{

			if(pairTable.getSelectedRowCount() > 0)	{
				int rows[] = pairTable.getSelectedRows();
				for(int i = rows.length; i > 0; i--)	{
					unpair(rows[i - 1]);
				}
			} else	{
				JOptionPane.showMessageDialog(this, "Please Select Pairs", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
