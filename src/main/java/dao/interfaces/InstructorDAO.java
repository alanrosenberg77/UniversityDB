package dao.interfaces;

import java.util.List;

import cs330.arosenberg.Instructor;

public interface InstructorDAO {

	public boolean createInstructor(int id, String name, String dept, int sal);
	
	public Instructor getInstructor(Integer id);
	public List<String> getTaughtCourses(Integer id);
	public List<Instructor> listAllInstructor();
	public List<Instructor> listInstructorByName(String name);
	public List<Instructor> listInstructorByID(Integer id);
	
	public boolean updateInstructor(Instructor i);
	
	public boolean deleteInstructor(Instructor i);
}
