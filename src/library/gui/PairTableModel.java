/**
 * 
 */
package library.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import library.database.Pair;
import library.database.StringUtility;
import library.database.Student;
import library.database.Volunteer;

/**
 * @author Roland
 *
 */
public class PairTableModel extends AbstractTableModel {

	private String[] colNames = {"Student", "Volunteer"};
	private ArrayList<Pair> pairs;
	
	public PairTableModel()	{
		pairs = new ArrayList<Pair>();
	}
	
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getRowCount() {
		return pairs.size();
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public Object getValueAt(int r, int c) {
		switch(c)	{
		case 0: return StringUtility.getFullName(pairs.get(r).student);
		case 1: return StringUtility.getFullName(pairs.get(r).volunteer);
		default: return null;
		}
	}
	
	/**
	 * Adds pair to table
	 * @param s student
	 * @param v volunteer
	 */
	public void addPair(Student s, Volunteer v)	{
		pairs.add(new Pair(s, v));
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}
	
	/**
	 * Removes pair from table given row number
	 * @param rowNum
	 */
	public void removePair(int rowNum)	{
		pairs.remove(rowNum);
		fireTableRowsDeleted(rowNum, rowNum);
	}
	
	/**
	 * @param rowNum
	 * @returns the pair at row number
	 */
	public Pair getPair(int rowNum)	{
		return pairs.get(rowNum);
	}

}
