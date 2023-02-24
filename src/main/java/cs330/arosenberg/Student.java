package cs330.arosenberg;

/**
 * Student instances are simple data sacks containing info about a student
 * @author alanr
 *
 */
public class Student {

	//instance variables
	private int id;
	private String name;
	private String major;
	private int credit;
	private Instructor advisor;
	private double gpa;
	
	//default constructor
	public Student() {
		id = 69;
		name = "Joe Mama";
		major = "Comp. Sci.";
		credit = 0;
		advisor = new Instructor();
		gpa = 4.20;
	}
	
	//partially parameterized constructor
	public Student(int id, String name, String major) {
		this.id = id;
		this.name = name;
		this.major = major;
		credit = 0;
		advisor = null;
		gpa = 0;
	}
	
	//less partially parameterized constructor
	public Student(int id, String name, String major, int credit, double gpa) {
		this.id = id;
		this.name = name;
		this.major = major;
		this.credit = credit;
		advisor = null;
		this.gpa = gpa;
	}
	
	public String toString() {
		return String.format("[%d] %s", id, name);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public Instructor getAdvisor() {
		return advisor;
	}

	public void setAdvisor(Instructor advisor) {
		this.advisor = advisor;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}
}
