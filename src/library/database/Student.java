/**
 * 
 */
package library.database;

import java.util.ArrayList;

//TODO for flexibility, try a hashmap of attributes
/**
 * @author Roland
 *
 */
public class Student {
	
	public static ArrayList<String> fields;//set from database
	private ArrayList<Object> attributes;

	/**
	 * 
	 */
	public Student() {
		fields = DbManager.getFields("Students");
		attributes = new ArrayList<Object>();
		//prevent null pointer exception by initing values in arraylist
		for(int i = 0; i < fields.size(); i++)
			attributes.add("");
	}
	
	/**
	 * @return the phoneNumber as String
	 */
	/*public String getPhoneNumberAsString()	{
		String number = "(" + String.valueOf((phoneNumber - (phoneNumber % 10000000)) / 10000000) + ") ";
		number = number + String.valueOf(((phoneNumber % 10000000) - (phoneNumber % 10000)) / (10000)) + "-";
		number = number + String.valueOf(phoneNumber % 10000);
		return number;
	}*/
	
	/**
	 * Gets attribute of student
	 * @param id
	 * @return attribute
	 */
	public Object getAttribute(int id)	{
		return attributes.get(id);
	}
	
	/**
	 * Sets attribute of student
	 * @param id
	 * @param value
	 */
	public void setAttribute(int id, Object val)	{
		attributes.set(id, val);
	}
	
	@Override
	public String toString()	{
		String s = "[";
		for(int i = 0; i < fields.size(); i++)
			s = s + fields.get(i) + ": " + attributes.get(i)+", ";
		s = s + "]";
		return s;
	}
}
