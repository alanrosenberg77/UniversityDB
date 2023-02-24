package dao.interfaces;

import java.util.List;

import cs330.arosenberg.Course;

public interface CourseDAO {
	
	public boolean createCourse(int id, String title, String dept, int credit);
	
	public Course getCourse(Integer id);
	public List<String> getPrerequisites(Integer id);
	public List<Course> listAllCourse();
	public List<Course> listCourseByTitle(String title);
	public List<Course> listCourseByID(Integer id);
	
	public boolean updateCourse(Course c);
	public boolean deleteCourse(Course c);
}
