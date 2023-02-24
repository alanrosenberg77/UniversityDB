package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs330.arosenberg.Instructor;
import dao.interfaces.InstructorDAO;

/**
 * JDBCInstructorDAO is an expert at projecting, creating, deleting, and updating
 * data regarding Instructors
 * @author alanr
 *
 */
public class JDBCInstructorDAO extends JDBCBaseDAO implements InstructorDAO {

	@Override
	/**
	 * @return true if a new instructor is created in uni.db, false if there is an issue with creation
	 */
	public boolean createInstructor(int id, String name, String dept, int sal) {
		Connection conn;
		PreparedStatement stmt;
		boolean success;
		
		try {
			//establishing connection and preparing a statement
			conn = super.getConnection();
			stmt = conn.prepareStatement("INSERT INTO INSTRUCTOR (ID, NAME, DEPT_NAME, SALARY)"
					+ "VALUES (?,?,?,?)");
			
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, dept);
			stmt.setInt(4, sal);
			
			//executing
			success = stmt.executeUpdate() >= 1;
			
			stmt.close();
			conn.close();
			
			return success;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	/**
	 * @return Instructor object whose ID matches the given ID
	 */
	public Instructor getInstructor(Integer id) {
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT * from INSTRUCTOR where ID=?");
		    stmt.setInt(1, id);
		    
		    //executing and building new instructor
		    rs = stmt.executeQuery();
		    
		    Instructor i = builder(rs);
		    
		    rs.close();
		    stmt.close();
		    conn.close();
		    
		    return i;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	/**
	 * @return ArrayList of Strings that show the title of all taught courses
	 */
	public List<String> getTaughtCourses(Integer id) {
		ArrayList<String> courses = new ArrayList<>();
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT distinct '['||COURSE_ID||']   '||TITLE \"cTitle\" from TEACHES JOIN COURSE using (COURSE_ID) where ID = ?");
		    stmt.setInt(1, id);
		    
		    //executing and recording course info
		    rs = stmt.executeQuery();
		    
		    while(rs.next()) {
		    	courses.add(rs.getString("cTitle"));
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
	 * @return ArrayList of all Instructor in uni.db
	 */
	public List<Instructor> listAllInstructor() {
		ArrayList<Instructor> instructors = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection and making statement
			conn = super.getConnection();
		    rs = conn.createStatement().executeQuery("SELECT substr('00000'||ID, -5) \"ID\", name, dept_name, salary from INSTRUCTOR order by ID");
		    
		    //adding to list
		    while(rs.next()) {
		    	instructors.add(builder(rs));
		    }
		    
		    rs.close();
		    conn.close();
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return instructors;
	}

	@Override
	/**
	 * @return ArrayList of Instructor with a given name in uni.db
	 */
	public List<Instructor> listInstructorByName(String name) {
		ArrayList<Instructor> instructors = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection and making statement
			conn = super.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * from INSTRUCTOR where NAME='").append(name).append("'");
			
			//executing and building instructor(s)
			rs = conn.createStatement().executeQuery(sb.toString());
			
			while(rs.next()) {
				instructors.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return instructors;
	}

	@Override
	/**
	 * @return ArrayList of all instructors with the given id
	 */
	public List<Instructor> listInstructorByID(Integer id) {
		ArrayList<Instructor> instructors = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
		    rs = conn.createStatement().executeQuery(String.format("SELECT * from INSTRUCTOR where ID = %s", id));
		    
		    //building instructor
		    while(rs.next()) {
		    	instructors.add(builder(rs));
		    }
		    
		    rs.close();
		    conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return instructors;
	}

	@Override
	/**
	 * @return true if Instructor is properly updated, false if there was an issue updating
	 */
	public boolean updateInstructor(Instructor i) {
		Connection conn;
		PreparedStatement stmt;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("UPDATE instructor SET name=?, dept_name=?, salary=? WHERE id=?");
		    
		    stmt.setString(1, i.getName());
		    stmt.setString(2, i.getDept());
		    stmt.setInt(3, i.getSal());
		    stmt.setInt(4, i.getId());
		    
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
	  * @return true if instructor was properly deleted, false if there was an issue deleting
	  */
	public boolean deleteInstructor(Instructor i) {
		Connection conn;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
		    boolean success = conn.createStatement().executeUpdate("DELETE from INSTRUCTOR where ID="+i.getId()) >= 1;
		    
		    conn.close();
		    
		    return success;
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * builder is a private helper method that streamlines the
	 * creation of instructor objects from result sets from queries
	 */
	private Instructor builder(ResultSet rs) throws SQLException {
		return new Instructor(rs.getInt("ID"), rs.getString("NAME"), rs.getString("DEPT_NAME"), rs.getInt("SALARY"));
	}

}
