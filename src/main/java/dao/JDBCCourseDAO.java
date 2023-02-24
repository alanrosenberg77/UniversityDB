package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs330.arosenberg.Course;
import dao.interfaces.CourseDAO;

/**
 * JDBCCourseDAO is an expert at projecting, creating, deleting, and updating
 * data regarding Courses
 * @author alanr
 *
 */
public class JDBCCourseDAO extends JDBCBaseDAO implements CourseDAO {

	@Override
	/**
	 * @return true if a new course is created in uni.db, false if there is an issue with creation
	 */
	public boolean createCourse(int id, String title, String dept, int credit) {
		Connection conn;
		PreparedStatement stmt;
		boolean success;
		
		try  {
			//establishing connection and preparing a statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("INSERT INTO COURSE (COURSE_ID, TITLE, DEPT_NAME, CREDITS)"
		    		+ "VALUES (?,?,?,?)");
		    
		    stmt.setInt(1, id);
		    stmt.setString(2, title);
		    stmt.setString(3, dept);
		    stmt.setInt(4, credit);
		    
		    //executing
		    success = stmt.executeUpdate() >= 1;
		    
		    stmt.close();
		    conn.close();
		    
		    return success;

		} catch (SQLException e) {
		    System.err.println(e.getMessage());
		    return false;
		}
	}

	@Override
	/**
	 * @return Course object whose ID matches the given ID
	 */
	public Course getCourse(Integer id) {
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
			stmt = conn.prepareStatement("SELECT * from COURSE where COURSE_ID=?");
			stmt.setInt(1, id);
			
			//executing and building new course
			rs = stmt.executeQuery();
			Course c = builder(rs);
			
			rs.close();
			stmt.close();
			conn.close();
			
			return c;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	/**
	 * @return ArrayList of Strings that show the title of all prereqs
	 */
	public List<String> getPrerequisites(Integer id){
		ArrayList<String> prereq = new ArrayList<>();
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT '['||prereq_ID||']   '||title \"cTitle\" FROM prereq p JOIN course c where prereq_id = c.course_id and p.course_id = ?");
		    stmt.setInt(1, id);
		    
		    //executing and recording prereq info
		    rs = stmt.executeQuery();
		    
		    while(rs.next()) {
		    	prereq.add(rs.getString("cTitle"));
		    }
		    
		    rs.close();
		    conn.close();
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return prereq;
	}

	@Override
	/**
	 * @return ArrayList of all Courses in uni.db
	 */
	public List<Course> listAllCourse() {
		ArrayList<Course> courses = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection and making statement
			conn = super.getConnection();
			rs = conn.createStatement().executeQuery("SELECT * from COURSE order by COURSE_ID");
			
			//adding to List
			while(rs.next()) {
				courses.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}

	@Override
	/**
	 * @return ArrayList of Courses with a given title in uni.db
	 */
	public List<Course> listCourseByTitle(String title) {
		ArrayList<Course> courses = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection and making statement
			conn = super.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * from COURSES where TITLE='").append(title).append("'");
			
			//executing and building course(s)
			rs = conn.createStatement().executeQuery(sb.toString());
			
			while(rs.next()) {
				courses.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}

	@Override
	/**
	 * @return ArrayList of all courses with the given course_id
	 */
	public List<Course> listCourseByID(Integer id) {
		ArrayList<Course> courses = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			rs = conn.createStatement().executeQuery(String.format("SELECT * from COURSE where COURSE_ID = %s", id));
			
			//building course
			while(rs.next()) {
				courses.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}

	@Override
	/**
	 * @return true if Course is properly updated, false if there was an issue updating
	 */
	public boolean updateCourse(Course c) {
		Connection conn;
		PreparedStatement stmt;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
			stmt = conn.prepareStatement("UPDATE course SET title=?, dept_name=?, credits=? WHERE course_id=?");
			
			stmt.setString(1, c.getTitle());
			stmt.setString(2, c.getDept());
			stmt.setInt(3, c.getCredit());
			stmt.setInt(4, c.getId());
			
			//executing
			boolean success = stmt.executeUpdate() >= 1;
			
			stmt.close();
			conn.close();
			
			return success;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	/**
	  * @return true if course was properly deleted, false if there was an issue deleting
	  */
	public boolean deleteCourse(Course c) {
		Connection conn;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			boolean success = conn.createStatement().executeUpdate("DELETE from COURSE where COURSE_ID="+c.getId()) >= 1;
			
			conn.close();
			
			return success;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * builder is a private helper method that streamlines the
	 * creation of course objects from result sets from queries
	 */
	private Course builder(ResultSet rs) throws SQLException {
		return new Course(rs.getInt("COURSE_ID"), rs.getString("TITLE"), rs.getString("DEPT_NAME"), rs.getInt("CREDITS"));
	}
}
