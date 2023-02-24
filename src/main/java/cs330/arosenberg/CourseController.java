package cs330.arosenberg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.JDBCCourseDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * CourseControllers will function as the controller for the course window when
 * root is set to course.fxml. Adds functionality to all buttons and some GUI elements.
 * Also initializes the window with default configurations of GUI elements.
 * @author alanr
 *
 */
public class CourseController implements Initializable {
	
	/*
	 * FXML elements
	 */
	@FXML
	Button deleteBtn;
	
	@FXML
	Button updateBtn;
	
	@FXML
	Button doneBtn;
	
	@FXML
	TextField idTF;
	
	@FXML
	TextField titleTF;
	
	@FXML
	TextField deptTF;
	
	@FXML
	TextField creditTF;
	
	@FXML
	ListView<Course> courseLV;
	
	@FXML
	ListView<String> prereqLV;
	
	//local data access object
	JDBCCourseDAO courseDAO;
	
	//constructor to initialize DAO
	public CourseController() {
		courseDAO = new JDBCCourseDAO();
	}
	
	@FXML
	/*
	 * simple method to switch to student window
	 */
	private void switchToStudent() throws IOException {
    	System.err.println("switching to student");

		App.setRoot("student");
	}
	
	@FXML
	/*
	 * simple method to switch to instructor window
	 */
	private void switchToInstructor() throws IOException {
    	System.err.println("switching to instructor");

		App.setRoot("instructor");
	}
	
	@FXML
	/*
     * first half of the algorithm to create a new course object by
     * configuring GUI elements to prepare them to receive new values.
     */
    private void createButtonDo() {
		System.err.println("create button pressed");

		//preparing GUI elements for input
    	courseLV.getSelectionModel().clearSelection();
    	prereqLV.getItems().clear();
    	idTF.setText("");
    	titleTF.setText("");
    	deptTF.setText("");
    	creditTF.setText("");
    	idTF.setPromptText("Enter integer");
    	titleTF.setPromptText("Enter characters");
    	deptTF.setPromptText("Enter characters");
    	creditTF.setPromptText("Enter integer");
    	
    	//preventing inputs in wrong places
    	idTF.setEditable(true);
    	titleTF.setEditable(true);
    	deptTF.setEditable(true);
    	creditTF.setEditable(true);
    	
    	//showing done button
    	updateBtn.setVisible(false);
    	updateBtn.setDisable(true);
    	doneBtn.setVisible(true);
    	doneBtn.setDisable(false);
    }
	
	@FXML
	/*
     * second half of the algorithm to create a new course object. Starts by grabbing
     * values of the GUI elements, then creates a new course with those values in uni.db.
     * Then method then restores GUI elements to their default configurations.
     */
    private void doneButtonDo() {
    	System.err.println("done button pressed");
    	
    	//preventing empty inputs
    	if(idTF.getText().equals("")) {
    		idTF.setPromptText("Enter integer");
    		
    	} else if (titleTF.getText().equals("")) {
    		titleTF.setPromptText("Enter characters");
    		
    	} else if (deptTF.getText().equals("")) {
    		deptTF.setPromptText("Enter characters");
    		
    	} else if(creditTF.getText().equals("")) {
    		creditTF.setPromptText("Enter integer");
    		
    	} else {
    		//creating new course with input values
    		courseDAO.createCourse(Integer.parseInt(idTF.getText()), titleTF.getText(), deptTF.getText(),
    				Integer.parseInt(creditTF.getText()));
    		
    		//restoring GUI elements to default configurations
    		updateBtn.setVisible(true);
        	updateBtn.setDisable(false);
        	doneBtn.setVisible(false);
        	doneBtn.setDisable(true);
        	
        	idTF.setText("");
        	titleTF.setText("");
        	deptTF.setText("");
        	creditTF.setText("");
        	
        	idTF.setPromptText("");
        	titleTF.setPromptText("");
        	deptTF.setPromptText("");
        	creditTF.setPromptText("");
        	
        	idTF.setEditable(false);
        	titleTF.setEditable(false);
        	deptTF.setEditable(false);
        	creditTF.setEditable(false);
        	
        	//refreshing courseLV
        	refresh();
    	}
    }
    
    @FXML
    /*
     * removes the selected Course object from courseLV, deletes it from the database,
     * and resets GUI to prevent modification of null objects
     */
    private void deleteButtonDo() {
    	System.err.println("delete button pressed");

    	//getting selected item and removing it from courseLV and uni.db
    	int i = courseLV.getSelectionModel().getSelectedIndex();
    	Course c = courseLV.getItems().remove(i);
    	courseDAO.deleteCourse(c);
    	
    	//restoring GUI elements to default config
    	prereqLV.getItems().clear();
    	courseLV.getSelectionModel().clearSelection();
    	idTF.setText("");
    	titleTF.setText("");
    	deptTF.setText("");
    	creditTF.setText("");
    	
    	deleteBtn.setDisable(true);
    	updateBtn.setDisable(true);
    }
    
    @FXML
    /*
     * uses current values of the GUI elements to update the currently selected student object
     */
    private void updateButtonDo() {
    	System.err.println("update button pressed");
    	
    	//using values from GUI elements to update selected course
    	Course c = new Course(Integer.parseInt(idTF.getText()), titleTF.getText(),
    			deptTF.getText(), Integer.parseInt(creditTF.getText()));
    	courseDAO.updateCourse(c);
    	
    	//refreshing courseLV
    	refresh();
    }
    
    @FXML
    /*
     * Upon selection of a Course object in courseLV, the courseDAO will fetch data about
     * the course including its prerequisites, display data in the labels, and display prereqs
     * in prereqLV. It will also enable GUI elements allowing data modification.
     */
    private void courseClicked() {
    	
    	prereqLV.getItems().clear();
    	
    	//Retrieving selected course object from uni.db
    	int id = courseLV.getSelectionModel().getSelectedItem().getId();
    	Course c = courseDAO.getCourse(id);
    	
    	//retrieving prerequisites from uni.db and populating prereqLV
    	ArrayList<String> prereqs = (ArrayList<String>) courseDAO.getPrerequisites(c.getId());
    	prereqLV.getItems().addAll(prereqs);
    	
    	//updating GUI elements with values of selected course object
    	idTF.setText(String.valueOf(c.getId()));
    	titleTF.setText(c.getTitle());
    	deptTF.setText(c.getDept());
    	creditTF.setText(String.valueOf(c.getCredit()));
    	
    	//configuring GUI elements to allow for data modification
    	deleteBtn.setDisable(false);
    	updateBtn.setDisable(false);
    	titleTF.setEditable(true);
    	deptTF.setEditable(true);
    	creditTF.setEditable(true);
    }

	@Override
	/*
     * Populates courseLV with all courses from uni.db when root set to course.fxml
     */
	public void initialize(URL location, ResourceBundle resources) {
		
		//populating courseLV
		refresh();
	    
	}
	
	/*
     * private helper method to refresh the list of courses
     */
	private void refresh() {
		//retrieving all courses from uni.db and displaying them in courseLV
		ArrayList<Course> allCourses = (ArrayList<Course>) courseDAO.listAllCourse();
		courseLV.getItems().clear();
		courseLV.getItems().addAll(allCourses);
	}
}
