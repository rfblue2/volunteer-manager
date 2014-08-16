/**
 * 
 */
package library.database;

/**
 * @author Roland
 * Represents a pair between student and volunteer
 */
public class Pair {
	public Student student;
	public Volunteer volunteer;
	
	public Pair(Student s, Volunteer v)	{
		student = s;
		volunteer = v;
	}
}
