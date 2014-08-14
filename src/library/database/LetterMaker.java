/**
 * 
 */
package library.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * Interfaces between Word Document Templates and Database names to allow user to print auto generated letters
 * @author Roland
 *
 */
public class LetterMaker {

	private static FileInputStream fin;
	private static FileOutputStream fout;
	private static final String volunteerTemplate = "Volunteer Notification.docx";
	private static final String studentTemplate = "Student Notification.docx";
	private static XWPFDocument doc;
	
	private static String[] volunteerKeys = {"SESSION", "BUDDY", "CONTACT", "GRADE", "SUBJECT", "AVAILABILITY"};
	private static HashMap<String, Object> volunteerFields;
	private static String[] studentKeys = {"SESSION", "BUDDY", "CONTACT", "GRADE", "SUBJECT", "AVAILABILITY"};
	private static HashMap<String, Object> studentFields;
	
	/**
	 * Opens file input stream to word file
	 */
	private static void openInputStream(String data)	{
		//init file and workbook
		try {
			fin = new FileInputStream(new File("./"+data));
			doc = new XWPFDocument(fin);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes file input stream to word file
	 */
	private static void closeInputStream()	{
		try {
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens output file stream to word
	 */
	private static void openOutputStream(String data)	{
		try {
			fout = new FileOutputStream(new File(data));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes output file stream to word
	 */
	private static void closeOutputStream()	{
		try	{
			fout.close();
		} catch (IOException e)	{
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates letter for volunteer regarding student info
	 * @param v volunteer
	 * @param path
	 */
	public static void genVolunteerNotification(Volunteer v, String path)	{
		openInputStream(volunteerTemplate);
		volunteerFields = new HashMap<String, Object>();
		//TODO predetermine hashmap based on something... don't manually code unless last resort
		ArrayList<Student> students = DbManager.getStudents();
		Student student;
		for(Student s : students)	{
			if(StringUtility.getFullName(s).equals(v.getAttribute("Student")))	{
				student = s;
				SimpleDateFormat df = new SimpleDateFormat("MMMM dd");
				volunteerFields.put("SESSION", df.format(DbManager.getSession()[0]) + " - " + df.format(DbManager.getSession()[1]));
				volunteerFields.put("BUDDY", (String)v.getAttribute("Student"));
				if(!((String) student.getAttribute("Phone")).equals(""))
					volunteerFields.put("CONTACT", StringUtility.convertPhoneNumber((String) student.getAttribute("Phone")));
				else if(!((String) student.getAttribute("Parent Email")).equals(""))
					volunteerFields.put("CONTACT", student.getAttribute("Parent Email"));
				else
					volunteerFields.put("CONTACT", "(no contact available)");
				volunteerFields.put("GRADE", (String)student.getAttribute("Grade"));
				volunteerFields.put("SUBJECT", StringUtility.convertSubject((String) student.getAttribute("Subject")));
				volunteerFields.put("AVAILABILITY", StringUtility.getTime((String) student.getAttribute("Availability")));
				break;
			}
		}
		//TODO account for null student
		XWPFDocument newDoc = doc.getXWPFDocument();
		Iterator<IBodyElement> bodyIterator = newDoc.getBodyElementsIterator();
		for(int i = 0; bodyIterator.hasNext(); i++)	{
			IBodyElement ib = bodyIterator.next();
			if(ib.getElementType() == BodyElementType.PARAGRAPH)	{
				XWPFParagraph newP = newDoc.getParagraphs().get(i);
				for(String key : volunteerKeys)	{
					if(newP.getText().contains(key))	{
						List<XWPFRun> runs = newP.getRuns();
						for(XWPFRun run : runs)	{
							if(run.getText(0).contains(key))	{
								run.setText(run.getText(0).replace(key, (String)volunteerFields.get(key)), 0);
								break;
							}
						}
						newDoc.setParagraph(newP, i);
					}
				}
			}
		}
		
		closeInputStream();
		
		openOutputStream(path+"/Volunteer Notification - "+StringUtility.getFullName(v) + ".docx");
		try {
			newDoc.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Generates letter for student regarding volunteer info
	 * @param s student
	 * @param path
	 */
	public static void genStudentNotification(Student s, String path)	{
		openInputStream(studentTemplate);
		studentFields = new HashMap<String, Object>();
		//TODO predetermine hashmap based on something... don't manually code unless last resort
		ArrayList<Volunteer> volunteers = DbManager.getVolunteers();
		Volunteer volunteer;
		for(Volunteer v : volunteers)	{
			if(StringUtility.getFullName(v).equals(s.getAttribute("Volunteer")))	{
				volunteer = v;
				SimpleDateFormat df = new SimpleDateFormat("MMMM dd");
				studentFields.put("SESSION", df.format(DbManager.getSession()[0]) + " - " + df.format(DbManager.getSession()[1]));
				studentFields.put("BUDDY", (String)s.getAttribute("Volunteer"));
				if(!((String) volunteer.getAttribute("Phone Number")).equals(""))
					studentFields.put("CONTACT", StringUtility.convertPhoneNumber((String) volunteer.getAttribute("Phone Number")));
				else if(!((String) volunteer.getAttribute("Email")).equals(""))
					studentFields.put("CONTACT", volunteer.getAttribute("Email"));
				else
					studentFields.put("CONTACT", "(no contact available)");
				studentFields.put("GRADE", (String)volunteer.getAttribute("Grade"));
				studentFields.put("SUBJECT", StringUtility.convertSubject((String) volunteer.getAttribute("Requested Subject")));
				studentFields.put("AVAILABILITY", StringUtility.getTime((String) volunteer.getAttribute("Availability")));
				break;
			}
		}
		//TODO account for null volunteer
		XWPFDocument newDoc = doc.getXWPFDocument();
		Iterator<IBodyElement> bodyIterator = newDoc.getBodyElementsIterator();
		for(int i = 0; bodyIterator.hasNext(); i++)	{
			IBodyElement ib = bodyIterator.next();
			if(ib.getElementType() == BodyElementType.PARAGRAPH)	{
				XWPFParagraph newP = newDoc.getParagraphs().get(i);
				for(String key : studentKeys)	{
					if(newP.getText().contains(key))	{
						List<XWPFRun> runs = newP.getRuns();
						for(XWPFRun run : runs)	{
							if(run.getText(0).contains(key))	{
								run.setText(run.getText(0).replace(key, (String)studentFields.get(key)), 0);
								break;
							}
						}
						newDoc.setParagraph(newP, i);
					}
				}
			}
		}
		
		closeInputStream();
		
		openOutputStream(path+"/Student Notification - "+StringUtility.getFullName(s) + ".docx");
		try {
			newDoc.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
