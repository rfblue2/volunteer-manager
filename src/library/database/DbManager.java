/**
 * 
 */
package library.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Roland
 */
public class DbManager {
	
	private static final String dataFile = "data1.xlsx";
	public static final String INFO = "Info";
	public static final String STUDENTS = "Students";
	public static final String VOLUNTEERS = "Volunteers";
	private static FileInputStream fin;
	private static FileOutputStream fout;
	private static XSSFWorkbook workbook;
	
	/**
	 * Opens file input stream to excel file
	 */
	private static void openInputStream()	{
		//init file and workbook
		try {
			fin = new FileInputStream(new File(dataFile));
			workbook = new XSSFWorkbook(fin);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes file input stream to excel file
	 */
	private static void closeInputStream()	{
		try {
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens output file stream to excel
	 */
	private static void openOutputStream()	{
		try {
			fout = new FileOutputStream(new File(dataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes output file stream to excel
	 */
	private static void closeOutputStream()	{
		try	{
			fout.close();
		} catch (IOException e)	{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the current session
	 * @param d1 start
	 * @param d2 end
	 */
	public static void setSession(Date d1, Date d2)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(INFO);
		Iterator<Row> rowIterator = sheet.iterator();
		Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
		cellIterator.next();//skip the cell labeled "session"
		cellIterator.next().setCellValue(d1);
		cellIterator.next().setCellValue(d2);
		
		closeInputStream();

		openOutputStream();
		try {
			workbook.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeOutputStream();
	}
	
	/**
	 * Gets the current session
	 * @return session as Date array
	 */
	public static Date[] getSession()	{
		openInputStream();
		Date[] s = new Date[2];
		XSSFSheet sheet = workbook.getSheet(INFO);
		Iterator<Row> rowIterator = sheet.iterator();
		Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
		cellIterator.next();//skip the cell labeled "session"
		s[0] = cellIterator.next().getDateCellValue();
		s[1] = cellIterator.next().getDateCellValue();
		closeInputStream();
		return s;
	}
	
	/**
	 * Returns fields as arraylist of strings given sheet name
	 * @param sheetName the name of the excel sheet
	 * @return fields
	 */
	public static ArrayList<String> getFields(String sheetName)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		ArrayList<String> fields = new ArrayList<String>();
		Iterator<Row> rowIterator = sheet.iterator();//iterator starts at the very beginning (fields)
		Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
		while(cellIterator.hasNext())	{
			fields.add(cellIterator.next().getStringCellValue());
		}
		closeInputStream();
		return fields;
	}
	
	/**
	 * @return Arraylist of students from excel
	 */
	public static ArrayList<Student> getStudents()	{
		openInputStream();//always open before reading to file!
		ArrayList<Student> students = new ArrayList<Student>();
		XSSFSheet sheet = workbook.getSheet(DbManager.STUDENTS);
		
		//iterate through rows of students
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();//skip first row of labels
		while(rowIterator.hasNext())	{
			Row row = rowIterator.next();
			Student s = new Student();
			for(int i = 0; i < Student.fields.size(); i++)	{
				Cell cell = row.getCell(i);
				if(cell != null)	{
					switch(cell.getCellType())	{
					case Cell.CELL_TYPE_NUMERIC:
						s.setAttribute(i, (long)cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						s.setAttribute(i, cell.getStringCellValue());
						break;
					}
				}
			}
			students.add(s);
		}
		closeInputStream();//always close after reading to file!
		return students;
	}

	/**
	 * @return Arraylist of volunteers from excel
	 */
	public static ArrayList<Volunteer> getVolunteers()	{
		openInputStream();//always open before reading to file!
		ArrayList<Volunteer> volunteers = new ArrayList<Volunteer>();
		XSSFSheet sheet = workbook.getSheet(DbManager.VOLUNTEERS);
		
		//iterate through rows of volunteers
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();//skip first row of labels
		while(rowIterator.hasNext())	{
			Row row = rowIterator.next();
			Volunteer v = new Volunteer();
			for(int i = 0; i < Volunteer.fields.size(); i++)	{
				Cell cell = row.getCell(i);
				if(cell != null)	{
					switch(cell.getCellType())	{
					case Cell.CELL_TYPE_NUMERIC:
						v.setAttribute(i, (long)cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						v.setAttribute(i, cell.getStringCellValue());
						break;
					}
				}
			}
			volunteers.add(v);
		}
		closeInputStream();//always close after reading to file!
		return volunteers;
	}
	
	/**
	 * Adds given student to excel
	 * @param student
	 */
	public static void addStudent(Student s)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(DbManager.STUDENTS);
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		for(int i = 0; i < Student.fields.size(); i++)	{
			Cell cell = row.createCell(i);
			Object obj = s.getAttribute(i);//currently all strings because they were initialized to strings
			if(obj instanceof String)
				cell.setCellValue((String)obj);
			else if(obj instanceof Long)
				cell.setCellValue((long)obj);
			else if(obj instanceof Integer)
				cell.setCellValue((int)obj);
		}
		closeInputStream();
		openOutputStream();
		try {
			workbook.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeOutputStream();
	}

	/**
	 * Removes either student or volunteer given row number
	 * @param row number
	 * @param obj student or volunteer
	 */
	public static void remove(int rowNum, String obj)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(obj);
		if(rowNum != sheet.getLastRowNum())	{//only shift rows up if they exist
			sheet.removeRow(sheet.getRow(rowNum));//get rid of entry
			sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), -1);//shift other entries up
			//note that tablemodel actually updates ids every time table is changed!
		}
		else
			sheet.removeRow(sheet.getRow(rowNum));//get rid of entry
		closeInputStream();
		openOutputStream();
		try {
			workbook.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeOutputStream();
	}
	
	/**
	 * Edits cell in specified sheet at row r and column c
	 * @param r the row
	 * @param c the column
	 * @param val the value
	 * @param obj the sheet name
	 */
	public static void editAttribute(int r, int c, Object val, String obj)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(obj);
		Row row = sheet.getRow(r + 1);
		Cell cell = row.getCell(c);
		if(val instanceof String)
			cell.setCellValue((String)val);
		else if(val instanceof Long)
			cell.setCellValue((long)val);
		else if(val instanceof Integer)
			cell.setCellValue((int)val);
		closeInputStream();
		openOutputStream();
		try {
			workbook.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeOutputStream();
	}
	
	/**
	 * Adds given volunteer to excel
	 * @param volunteer
	 */
	public static void addVolunteer(Volunteer v)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet(DbManager.VOLUNTEERS);
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		for(int i = 0; i < Volunteer.fields.size(); i++)	{
			Cell cell = row.createCell(i);
			Object obj = v.getAttribute(i);//currently all strings because they were initialized to strings
			if(obj instanceof String)
				cell.setCellValue((String)obj);
			else if(obj instanceof Long)
				cell.setCellValue((long)obj);
			else if(obj instanceof Integer)
				cell.setCellValue((int)obj);
		}
		closeInputStream();
		openOutputStream();
		try {
			workbook.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeOutputStream();
	}
}
