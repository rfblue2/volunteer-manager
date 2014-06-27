/**
 * 
 */
package library.gui;

import javax.swing.table.AbstractTableModel;

/**
 * @author Roland
 *
 */
public class StudentTableModel extends AbstractTableModel {
	
	//TODO instantiate a database handler to query database to init table
	
	//temporary, filler column names; later retrieve from database
	private String[] colNames = {"Name", "Grade", "Gender", "Address", 
			"Phone #", "Parent Email", "Subject", "Session/Weeks", 
			"Availability", "Request Volunteer", "Notes"};
	//one sample data record - "John Smith"
	private Object[][] data = {{"John Smith", "8", "M", "1 Main St.", "12345", 
		"blah@m.com", "Math", "Spring", "MTWThF", "", ""}};
	
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		if(c < 2)
			return false;
		else
			return true;
	}

	@Override
	public void setValueAt(Object val, int r, int c) {
		data[r][c] = val;
		fireTableCellUpdated(r, c);
	}
	
}
