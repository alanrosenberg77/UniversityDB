package cs330.arosenberg;

/**
 * Course instances are simple data sacks containing info of a course
 * @author alanr
 *
 */
public class Course {
	
	//instance variables
	private int id;
	private String title;
	private String dept;
	private int credit;
	
	//default constructor
	public Course() {
		id = 42;
		title = "Intro to your mom";
		dept = "Astrophysics";
		credit = 999999;
	}
	
	//parameterized constructor
	public Course(int id, String title, String dept, int credit) {
		this.id = id;
		this.title = title;
		this.dept = dept;
		this.credit = credit;
	}
	
	public String toString() {
		return String.format("[%d] %s", id, title);
	}

	/*
	 * getters and setters
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	
}
