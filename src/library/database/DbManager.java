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
		XSSFSheet sheet = workbook.getSheet("Students");
		
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
		XSSFSheet sheet = workbook.getSheet("Volunteers");
		
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
		XSSFSheet sheet = workbook.getSheet("Students");
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
	 * Removes student given row number
	 * @param row number
	 */
	public static void removeStudent(int rowNum)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet("Students");
		if(rowNum != sheet.getLastRowNum())	{//only shift rows up if they exist
			sheet.removeRow(sheet.getRow(rowNum));//get rid of entry
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);//shift other entries up
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
	 * Edits cell in student sheet at row r and column c
	 * @param r the row
	 * @param c the column
	 * @param val the value
	 */
	public static void editStudentAttribute(int r, int c, Object val)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet("Students");
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
		XSSFSheet sheet = workbook.getSheet("Volunteers");
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

	/**
	 * Removes volunteer given row number
	 * @param row number
	 */
	public static void removeVolunteer(int rowNum)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet("Volunteers");
		if(rowNum != sheet.getLastRowNum())	{//only shift rows up if they exist
			sheet.removeRow(sheet.getRow(rowNum));//get rid of entry
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);//shift other entries up
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
	 * Edits cell in volunteer sheet at row r and column c
	 * @param r the row
	 * @param c the column
	 * @param val the value
	 */
	public static void editVolunteerAttribute(int r, int c, Object val)	{
		openInputStream();
		XSSFSheet sheet = workbook.getSheet("Volunteers");
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
}
