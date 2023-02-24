package cs330.arosenberg;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.JDBCStudentDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MenuController implements Initializable {

	@FXML
	/*
	 * simple method to switch to student window
	 */
	private void switchToStudent() throws IOException {
		App.setRoot("student");
	}
	
	@FXML
	/*
	 * simple method to switch to instructor window
	 */
	private void switchToInstructor() throws IOException {
		App.setRoot("instructor");
	}
	
	@FXML
	/*
	 * simple method to switch to course window
	 */
	private void switchToCourse() throws IOException {
		App.setRoot("course");
	}

	@Override
	/*
	 * creates the grades table if it does not already exist
	 */
	public void initialize(URL location, ResourceBundle resources) {
		
		JDBCStudentDAO studentDAO = new JDBCStudentDAO();
		
//		studentDAO.deleteGradesTable();
		
		//checking for existence, then creating if necessary
		if(!studentDAO.detectGradesTable()) {
			studentDAO.createGradesTable();
		}
	}
}
