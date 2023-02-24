package dao.interfaces;

import java.util.List;

import cs330.arosenberg.Instructor;
import cs330.arosenberg.Student;

public interface StudentDAO {

	public boolean createStudent(Integer id, String name, String major, Integer i_id);

	public boolean detectGradesTable();
	public boolean deleteGradesTable();
	public boolean createGradesTable();
	
	public double getGPA(Integer id);
	public Student getStudent(Integer id);
	public Instructor getAdvisor(Integer id);
	public int getCredits(Integer id);
	public List<String> getTranscript(Integer id);
	public List<Student> listAllStudent();
	public List<Student> listStudentByName(String name);
	public List<Student> listStudentByID(Integer id);
	
	public boolean addToTranscript(Integer id, Integer c_id, String sem, Integer year, String grade);
	
	public boolean updateStudent(Student s);
	
	public boolean deleteStudent(Student s);

}
