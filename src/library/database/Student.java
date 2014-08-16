/**
 * 
 */
package library.database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Roland
 *
 */
public class Student {
	
	public static ArrayList<String> fields;//set from database
	private HashMap<String, Object> attributes;

	/**
	 * 
	 */
	public Student() {
		fields = DbManager.getFields(DbManager.STUDENTS);
		attributes = new HashMap<String, Object>();
		//prevent null pointer exception by initing values in arraylist
		for(int i = 0; i < fields.size(); i++)
			attributes.put(fields.get(i), "");
	}
	
	/**
	 * Gets attribute of student
	 * @param id
	 * @return attribute
	 */
	public Object getAttribute(int id)	{
		return attributes.get(fields.get(id));
	}
	
	/**
	 * Gets the attribute of student given a string
	 * @param a
	 * @return
	 */
	public Object getAttribute(String a)	{
		if(fields.indexOf(a) >= 0)
			return attributes.get(a);
		else return 0;
	}
	
	/**
	 * Sets attribute of student
	 * @param id
	 * @param value
	 */
	public void setAttribute(int id, Object val)	{
		attributes.put(fields.get(id), val);
	}

	/**
	 * Sets attribute of student
	 * @param id
	 * @param value
	 */
	public void setAttribute(String a, Object val)	{
		if(fields.indexOf(a) >= 0)
			attributes.put(a, val);
		else System.out.println("Error: Student attribute does not exist");
	}
	
	@Override
	public String toString()	{
		String s = "[";
		for(int i = 0; i < fields.size(); i++)
			s = s + fields.get(i) + ": " + attributes.get(fields.get(i))+", ";
		s = s + "]";
		return s;
	}
}
