/**
 * 
 */
package library.database;

import java.util.HashMap;

/**
 * @author Roland
 * Utility functions to manage strings
 */
public class StringUtility {
	
	private static final HashMap<String, String> times;
	static	{//static initializer
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("A", "MON 3-5PM");
		map.put("B", "MON 5-7PM");
		map.put("C", "MON 7-9PM");
		map.put("D", "TUE 3-5PM");
		map.put("E", "TUE 5-7PM");
		map.put("F", "TUE 7-9PM");
		map.put("G", "WED 3-5PM");
		map.put("H", "WED 5-7PM");
		map.put("I", "WED 7-9PM");
		map.put("J", "THR 3-5PM");
		map.put("K", "THR 5-7PM");
		map.put("L", "THR 7-9PM");
		map.put("M", "SAT 10AM-12PM");
		map.put("N", "SAT 1-3PM");
		map.put("O", "SAT 3-5PM");
		map.put("P", "SUN 1-3PM");
		map.put("Q", "SUN 3-5PM");
		map.put("R", "FLEXIBLE");
		times = new HashMap<String, String>(map);
	}
	
	/**
	 * Converts the letters to actual times
	 * @param letters
	 * @return time as string
	 */
	public static String getTime(String letters)	{
		String s = "";
		for(char c : letters.toCharArray())	{
			s = s + times.get(c+"")+", ";
		}
		s = s.substring(0, s.length() - 2);
		return s;
	}
	
	/**
	 * Formats phone number into (###) ###-####
	 * @param phone
	 * @return formatted phone number
	 */
	public static String convertPhoneNumber(String phone)	{
		if(phone.length() == 10)
			return "("+phone.substring(0, 3)+") "+phone.substring(3, 6)+"-"+phone.substring(6);
		else return phone;
	}
	
	/**
	 * Converts "Both" to "Math & Reading" for clarity
	 * @param subj subject
	 * @return
	 */
	public static String convertSubject(String subj)	{
		if(subj.equals("Both")) return "Math & Reading";
		else return subj;
	}
	
	/**
	 * Returns full name of volunteer
	 * @param v
	 * @return
	 */
	public static String getFullName(Volunteer v)	{
		return (String)v.getAttribute("First Name")+" "+(String)v.getAttribute("Last Name");
	}
	
	/**
	 * Returns full name of student
	 * @param s
	 * @return
	 */
	public static String getFullName(Student s)	{
		return (String)s.getAttribute("First Name")+" "+(String)s.getAttribute("Last Name");
	}
}
