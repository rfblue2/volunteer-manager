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
		fields = DbManager.getFields(DbManager.VOLUNTEERS);
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
	 * Gets the attribute of volunteer given a string
	 * @param a
	 * @return
	 */
	public Object getAttribute(String a)	{
		if(DbManager.getFields(DbManager.VOLUNTEERS).indexOf(a) != -1)
			return attributes.get(DbManager.getFields(DbManager.VOLUNTEERS).indexOf(a));
		else return 0;
	}
	
	/**
	 * Sets attribute of volunteer
	 * @param index
	 * @param value
	 */
	public void setAttribute(int id, Object val)	{
		attributes.set(id,  val);
	}

	/**
	 * Sets attribute of volunteer
	 * @param id
	 * @param value
	 */
	public void setAttribute(String a, Object val)	{
		if(DbManager.getFields(DbManager.VOLUNTEERS).indexOf(a) != -1)
			attributes.set(DbManager.getFields(DbManager.VOLUNTEERS).indexOf(a), val);
		else System.out.println("Error: Volunteer attribute does not exist");
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
