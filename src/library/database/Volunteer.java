/**
 * 
 */
package library.database;

import java.util.ArrayList;

/**
 * @author Roland
 *
 */
public class Volunteer {
	public static ArrayList<String> fields;//set from database
	private ArrayList<Object> attributes;
	
	public Volunteer()	{
		fields = DbManager.getFields("Volunteers");
		attributes = new ArrayList<Object>();
		//prevent null pointer exception by initing values in arraylist
		for(int i = 0; i < fields.size(); i++)
			attributes.add("");
	}
	
	/**
	 * Gets attribute of volunteer
	 * @param index
	 * @return attribute
	 */
	public Object getAttribute(int id)	{
		return attributes.get(id);
	}
	
	/**
	 * Sets attribute of volunteer
	 * @param index
	 * @param value
	 */
	public void setAttribute(int id, Object val)	{
		attributes.set(id,  val);
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
