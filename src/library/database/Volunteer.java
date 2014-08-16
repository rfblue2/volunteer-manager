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
public class Volunteer {
	public static ArrayList<String> fields;//set from database
	private HashMap<String, Object> attributes;
	
	public Volunteer()	{
		fields = DbManager.getFields(DbManager.VOLUNTEERS);
		attributes = new HashMap<String, Object>();
		//prevent null pointer exception by initing values in arraylist
		for(int i = 0; i < fields.size(); i++)
			attributes.put(fields.get(i), "");
	}
	
	/**
	 * Gets attribute of volunteer
	 * @param index
	 * @return attribute
	 */
	public Object getAttribute(int id)	{
		return attributes.get(fields.get(id));
	}
	
	/**
	 * Gets the attribute of volunteer given a string
	 * @param a
	 * @return
	 */
	public Object getAttribute(String a)	{
		if(fields.indexOf(a) >= 0)
			return attributes.get(a);
		else return 0;
	}
	
	/**
	 * Sets attribute of volunteer
	 * @param index
	 * @param value
	 */
	public void setAttribute(int id, Object val)	{
		attributes.put(fields.get(id), val);
	}

	/**
	 * Sets attribute of volunteer
	 * @param id
	 * @param value
	 */
	public void setAttribute(String a, Object val)	{
		if(fields.indexOf(a) >= 0)
			attributes.put(a, val);
		else System.out.println("Error: Volunteer attribute does not exist");
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
