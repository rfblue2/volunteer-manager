/**
 * 
 */
package library.gui;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import library.database.DbManager;
import library.database.Volunteer;

/**
 * @author Roland
 *
 */
public class VolunteerTableModel extends AbstractTableModel implements TableModelListener {
	
	//temporary, filler column names; later retrieve from database
	private ArrayList<String> colNames;

	private ArrayList<Volunteer> data;
	
	public VolunteerTableModel()	{
		data = DbManager.getVolunteers();
		colNames = Volunteer.fields;
		addTableModelListener(this);
	}
	
	@Override
	public int getColumnCount() {
		return colNames.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		return data.get(r).getAttribute(c);
	}

	@Override
	public Class<?> getColumnClass(int c) {
		//return getValueAt(0, c).getClass();
		return Object.class;//I guess unless you pass obj, you can't mix...
	}

	@Override
	public String getColumnName(int col) {
		return colNames.get(col);
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
		data.get(r).setAttribute(c, val);
		fireTableCellUpdated(r, c);
	}
	
	/**
	 * Adds volunteer to table and database
	 * @param s
	 */
	public void addVolunteer(Volunteer v)	{
		DbManager.addVolunteer(v);
		data.add(v);
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}
	
	/**
	 * Removes volunteer(row) from table and database
	 * @param rowNum
	 */
	public void removeVolunteer(int rowNum)	{
		DbManager.removeVolunteer(rowNum + 1);//add 1 because table is actually 1 row behind excel sheet
		data.remove(rowNum);
		fireTableRowsDeleted(rowNum, rowNum);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int r = e.getFirstRow();
		int c = e.getColumn();
		if(e.getType() == TableModelEvent.UPDATE)	{
			TableModel model = (TableModel)e.getSource();
			Object data = model.getValueAt(r, c);
			DbManager.editVolunteerAttribute(r, c, data);
		}
	}
	
}
