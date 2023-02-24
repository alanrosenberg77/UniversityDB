package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs330.arosenberg.Instructor;
import cs330.arosenberg.Student;
import dao.interfaces.StudentDAO;

/**
 * An instance of this class can manage data access and manipulation 
 * for Student entities.
 * 
 * @author alanr
 *
 */
public class JDBCStudentDAO extends JDBCBaseDAO implements StudentDAO {

	@Override
	/**
	 * @return true if a new student is created in uni.db, false if there is an issue with creation
	 */
	public boolean createStudent(Integer id, String name, String major, Integer i_id) {
		Connection conn;
		PreparedStatement stmt;
		boolean success;
		
		try  {
			//establishing connection and preparing statement
		    conn = super.getConnection();
		    stmt = conn.prepareStatement("INSERT INTO STUDENT (ID, NAME, DEPT_NAME)"
		    		+ "VALUES (?,?,?)");
		    
		    stmt.setInt(1, id);
		    stmt.setString(2, name);
		    stmt.setString(3, major);
		    
		    //executing
		    success = stmt.executeUpdate() >= 1;
		    
		    //preparing next statement
		    stmt = conn.prepareStatement("INSERT INTO advisor (s_id, i_id)"
					+ "VALUES(?, ?)");
			
			stmt.setInt(1, id);
			stmt.setInt(2, i_id);
			
			//executing
			stmt.executeUpdate();
		    
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
	 * @return true indicates the Grades table exists, false indicates the Grades table does not exist
	 */
	public boolean detectGradesTable() {
		
		Connection conn;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			conn.createStatement().executeQuery("SELECT * FROM Grades");
			
		    conn.close();
			
			return true;
			
		} catch(SQLException e) {
			System.err.print("Grades table isn't here...\nLets fix that!");
			
			return false;
		}
	}
	
	@Override
	/**
	 * @return true if Grades table is dropped, App terminates if Grades table is not dropped
	 */
	public boolean deleteGradesTable() {
		
		Connection conn;
		boolean success;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			success = !conn.createStatement().execute("DROP TABLE Grades");
			
			conn.close();
			
			return success;
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}
	
	@Override
	/**
	 * @return true if Grades table is created, false if Grades table is not created
	 */
	public boolean createGradesTable() {
		
		Connection conn;
		PreparedStatement stmt = null;
		boolean success;
		
		try  {
			//establishing connection
		    conn = super.getConnection();
		    
		    String[] letters = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"};
			double[] points = {4.3, 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.3, 1.0, 0.7, 0.0};
		    
			//making statement, executing
		    success = conn.createStatement().executeUpdate("CREATE TABLE grades(letter CHAR(3) PRIMARY KEY NOT NULL, grade_points REAL NOT NULL);") == 0;
		    
		    //preparing many statements for each item in arrays
		    for(int i=0 ; i<letters.length ; i++) {
		    	stmt = conn.prepareStatement("INSERT INTO grades (letter, grade_points)"
			    		+ "VALUES (?, ?)");
		    	stmt.setString(1, letters[i]);
		    	stmt.setDouble(2, points[i]);
		    	
		    	//executing
		    	stmt.executeUpdate();
		    }
		    
		    stmt.close();
		    conn.close();
		    
		    return success;

		} catch (SQLException e) {
		    System.err.println("Something went wrong making Grades table...oops\n");
		    e.printStackTrace();
		    return false;
		}
	}
	
	@Override
	/**
	 * @return double value of a student's GPA based on the classes they have taken
	 */
	public double getGPA(Integer id) {
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		float gp = 0;
		int size = 0;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT grade_points FROM takes JOIN grades WHERE grade = letter AND id = ?");
		    stmt.setInt(1, id);
		    
		    //executing
		    rs = stmt.executeQuery();
		    
		    //summing
		    while(rs.next()) {
		    	gp += rs.getDouble("grade_points");
		    	size++;
		    }
		    
		    rs.close();
		    conn.close();
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return gp/size;
	}

	@Override
	/**
	 * @return Student object whose ID matches the given ID
	 */
	public Student getStudent(Integer id) {
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
		    conn = super.getConnection();

			stmt = conn.prepareStatement("SELECT id, name, dept_name from STUDENT where ID=?");
			stmt.setInt(1, id);
			
			//executing and building
			rs = stmt.executeQuery();
			Student s = builder(rs);
			
			rs.close();
			stmt.close();
			conn.close();
			
			return s;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	/**
	 * @return int value of the number of credits had by a student of the given id
	 */
	public int getCredits(Integer id) {
		int credits = 0;
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT SUM(credits) \"TotalCreds\" FROM takes JOIN course USING (course_id) WHERE id = ?");
		    stmt.setInt(1, id);
		    
		    //executing and recording info
		    rs = stmt.executeQuery();
		    
		    while(rs.next()) {
		    	credits = rs.getInt("TotalCreds");
		    }
		    
		    rs.close();
		    conn.close();
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return credits;
	}
	
	@Override
	/**
	 * @return ArrayList of Strings that show the title of all classes taken by a student
	 * with the semester and year of when they were taken
	 */
	public List<String> getTranscript(Integer id) {
		ArrayList<String> transcript = new ArrayList<>();
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT substr(semester,1,1)||substr(year,3,4)||'   '||title \"tEntry\" from TAKES JOIN COURSE using (COURSE_ID) where ID = ? order by year,semester");
		    stmt.setInt(1, id);
		    
		    //executing and adding to transcript
		    rs = stmt.executeQuery();
		    
		    while(rs.next()) {
		    	transcript.add(rs.getString("tEntry"));
		    }
		    
		    rs.close();
		    conn.close();
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return transcript;
	}
	
	@Override
	/**
	 * @return Instructor object that acts as an advisor for a student of the given id
	 */
	public Instructor getAdvisor(Integer id) {
		
		int iid;
		String name;
		String dept;
		int sal;
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT * from advisor JOIN instructor where i_id = id and s_id = ?");
		    stmt.setInt(1, id);
		    
		    //executing and recording data
		    rs = stmt.executeQuery();
		    
		    iid = rs.getInt("i_id");
		    name = rs.getString("name");
		    dept = rs.getString("dept_name");
		    sal = rs.getInt("salary");
		    
		    rs.close();
		    conn.close();
		    
		    return new Instructor(iid, name, dept, sal);
		    
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	/**
	 * @return ArrayList of all Students in uni.db
	 */
	public List<Student> listAllStudent() {
		ArrayList<Student> students = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection, making statement, executing
		    conn = super.getConnection();
			rs = conn.createStatement().executeQuery("SELECT substr('00000'||ID, -5) \"ID\", name, dept_name from STUDENT order by ID");
			
			//building students
			while(rs.next()) {
				students.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}

	@Override
	/**
	 * @return ArrayList of Students with a given name in uni.db
	 */
	public List<Student> listStudentByName(String name) {
		ArrayList<Student> students = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection and making statement
			conn = super.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT id, name, dept_name from STUDENT where NAME='").append(name).append("'");
			
			//executing
			rs = conn.createStatement().executeQuery(sb.toString());
			
			//building
			while(rs.next()) {
				students.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}

	@Override
	/**
	 * @return ArrayList of all students with the given id
	 */
	public List<Student> listStudentByID(Integer id) {
		ArrayList<Student> students = new ArrayList<>();
		
		Connection conn;
		ResultSet rs;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			rs = conn.createStatement().executeQuery(String.format("SELECT id, name, dept_name from STUDENT where ID = %s", id));
			
			//building students
			while(rs.next()) {
				students.add(builder(rs));
			}
			
			rs.close();
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}
	
	@Override
	public boolean addToTranscript(Integer id, Integer c_id, String sem, Integer year, String grade) {
		
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		int s_id = 0;
		boolean success;
		
		try  {
			//establishing connection and preparing statement
		    conn = super.getConnection();
		    stmt = conn.prepareStatement("SELECT sec_id FROM section WHERE course_id=?");
		    
		    stmt.setInt(1, id);
		    
		    //executing and recording data
		    rs = stmt.executeQuery();
		    
		    while(rs.next()) {
		    	s_id = rs.getInt("sec_id");
		    }
		    
		    //preparing new statement
		    stmt = conn.prepareStatement("INSERT INTO TAKES (id, course_id, sec_id, semester, year, grade)"
		    		+ "VALUES (?,?,?,?,?,?)");
		    
		    stmt.setInt(1, id);
		    stmt.setInt(2, c_id);
		    stmt.setInt(3, s_id);
		    stmt.setString(4, sem);
		    stmt.setInt(5, year);
		    stmt.setString(6, grade);
		    
		    //executing
			success = stmt.executeUpdate() == 0;
		    
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
	 * @return true if Student is properly updated, false if there was an issue updating
	 */
	public boolean updateStudent(Student s) {
		
		Connection conn;
		PreparedStatement stmt;
		
		try {
			//establishing connection and preparing statement
			conn = super.getConnection();
			stmt = conn.prepareStatement("UPDATE student SET name=?, dept_name=? WHERE id = ?");
			
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getMajor());
			stmt.setInt(3, s.getId());
			
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
	  * @return true if student was properly deleted, false if there was an issue deleting
	  */
	public boolean deleteStudent(Student s) {
		
		Connection conn;
		
		try {
			//establishing connection, making statement, executing
			conn = super.getConnection();
			boolean success = conn.createStatement().executeUpdate("DELETE from STUDENT where ID="+s.getId()) >= 1;
			
			conn.close();
			
			return success;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * builder is a private helper method that streamlines the
	 * creation of student objects from result sets from queries
	 */
	private Student builder(ResultSet rs) throws SQLException {
		return new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getString("DEPT_NAME"));
	}

}
