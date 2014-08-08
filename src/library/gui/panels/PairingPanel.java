/**
 * 
 */
package library.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import library.database.DbManager;
import library.gui.StudentTableModel;
import library.gui.VolunteerTableModel;

/**
 * @author Roland
 *
 */
public class PairingPanel extends JPanel {

	private static JTable studentTable;//unpaired students
	private static JTable volunteerTable;//unpaired volunteers
	private TableRowSorter<StudentTableModel> studentSorter;
	private TableRowSorter<VolunteerTableModel> volunteerSorter;
	private static JTable pairTable;
	private Color lightPurple;
	private Font arial16;
	
	public PairingPanel()	{
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lightPurple = new Color(229, 204, 255);
		setBackground(lightPurple);
		arial16 = new Font("Arial", Font.PLAIN, 16);
		
		JLabel unpaired = new JLabel("Unpaired Students and Volunteers");
		unpaired.setBackground(lightPurple);
		unpaired.setFont(arial16);
		add(unpaired);
		JPanel tables = new JPanel();
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
		tables.add(new JScrollPane(studentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		
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
		tables.add(new JScrollPane(volunteerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		add(tables);
		
	}
}
