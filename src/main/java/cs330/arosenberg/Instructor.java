package cs330.arosenberg;

/**
 * Instructor instances are simple data sacks containing info of an instructor
 * @author alanr
 *
 */
public class Instructor {

	//instance variables
	private int id;
	private String name;
	private String dept;
	private int sal;
	
	//default constructor
	public Instructor() {
		id = 42069;
		name = "Sawcon";
		dept = "Comp. Sci.";
		sal = 69420;
	}
	
	//parameterized constructor
	public Instructor(int id, String name, String dept, int sal) {
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.sal = sal;
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

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}
	
	
}
