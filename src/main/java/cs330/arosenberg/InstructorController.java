package cs330.arosenberg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.JDBCInstructorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * InstructorControllers will function as the controller for the instructor window when
 * root is set to instructor.fxml. Adds functionality to all buttons and some GUI elements.
 * Also initializes the window with default configurations of GUI elements.
 * @author alanr
 *
 */
public class InstructorController implements Initializable {

	/*
	 * FXML elements
	 */
	@FXML
	ListView<Instructor> instructorLV;
	
	@FXML
	ListView<String> teachesLV;
	
	@FXML
	TextField idTF;
	
	@FXML
	TextField nameTF;
	
	@FXML
	TextField deptTF;
	
	@FXML
	TextField salTF;
	
	@FXML
	Button deleteBtn;
	
	@FXML
	Button updateBtn;
	
	@FXML
	Button doneBtn;
	
	//local data access object
	JDBCInstructorDAO instructorDAO;
	
	//constructor to initialize DAO
	public InstructorController() {
		instructorDAO = new JDBCInstructorDAO();
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
     * simple method to switch to course indow
     */
    private void switchToCourse() throws IOException {
    	System.err.println("switching to course");

    	App.setRoot("course");
    }
    
    @FXML
    /*
     * first half of the algorithm to create a new instructor object by
     * configuring GUI elements to prepare them to receive new values.
     */
    private void createButtonDo() {
    	System.err.println("create button pressed");

    	//preparing GUI elements for input
    	instructorLV.getSelectionModel().clearSelection();
    	teachesLV.getItems().clear();
    	idTF.setText("");
    	nameTF.setText("");
    	deptTF.setText("");
    	salTF.setText("");
    	idTF.setPromptText("Enter integer");
    	nameTF.setPromptText("Enter characters");
    	deptTF.setPromptText("Enter characters");
    	salTF.setPromptText("Enter integer");
    	
    	//prevents inputs in wrong places
    	idTF.setEditable(true);
    	nameTF.setEditable(true);
    	deptTF.setEditable(true);
    	salTF.setEditable(true);
    	
    	//showing done button
    	updateBtn.setVisible(false);
    	updateBtn.setDisable(true);
    	doneBtn.setVisible(true);
    	doneBtn.setDisable(false);
    }
    
    @FXML
    /*
     * second half of the algorithm to create a new instructor object. Starts by grabbing
     * values of the GUI elements, then creates a new instructor with those values in uni.db.
     * Then method then restores GUI elements to their default configurations.
     */
    private void doneButtonDo() {
    	System.err.println("done button pressed");
    	
    	//preventing empty inputs
    	if(idTF.getText().equals("")) {
    		idTF.setPromptText("Enter integer");
    		
    	} else if (nameTF.getText().equals("")) {
    		nameTF.setPromptText("Enter characters");
    		
    	} else if (deptTF.getText().equals("")) {
    		deptTF.setPromptText("Enter characters");
    		
    	} else if(salTF.getText().equals("")) {
    		salTF.setPromptText("Enter integer");
    		
    	} else {
    		//creating new instructor with input values
    		instructorDAO.createInstructor(Integer.parseInt(idTF.getText()), nameTF.getText(), deptTF.getText(),
    				Integer.parseInt(salTF.getText()));
    		
    		//restoring GUI elements to default configurations
    		updateBtn.setVisible(true);
        	updateBtn.setDisable(false);
        	doneBtn.setVisible(false);
        	doneBtn.setDisable(true);
        	
        	idTF.setText("");
        	nameTF.setText("");
        	deptTF.setText("");
        	salTF.setText("");
        	
        	idTF.setPromptText("");
        	nameTF.setPromptText("");
        	deptTF.setPromptText("");
        	salTF.setPromptText("");
        	
        	idTF.setEditable(false);
        	nameTF.setEditable(false);
        	deptTF.setEditable(false);
        	salTF.setEditable(false);
        	
        	//refreshing instructorLV
        	refresh();
    	}
    }
    
    @FXML
    /*
     * removes the selected Instructor object from instructorLV, deletes it from the database,
     * and resets GUI to prevent modification of null objects
     */
    private void deleteButtonDo() {
    	System.err.println("delete button pressed");

    	//getting select item and removing it from instructorLV and uni.db
    	int j = instructorLV.getSelectionModel().getSelectedIndex();
    	Instructor i = instructorLV.getItems().remove(j);
    	instructorDAO.deleteInstructor(i);
    	
    	//restoring GUI elements to default config
    	teachesLV.getItems().clear();
    	instructorLV.getSelectionModel().clearSelection();
    	idTF.setText("");
    	nameTF.setText("");
    	deptTF.setText("");
    	salTF.setText("");
    	
    	deleteBtn.setDisable(true);
    	updateBtn.setDisable(true);
    }
    
    @FXML
    /*
     * uses current values of the GUI elements to update the currently selected student object
     */
    private void updateButtonDo() {
    	System.err.println("update button pressed");
    	
    	//using values from GUI elements to update selected instructor
    	Instructor i = new Instructor(Integer.parseInt(idTF.getText()), nameTF.getText(),
    			deptTF.getText(), Integer.parseInt(salTF.getText()));
    	instructorDAO.updateInstructor(i);
    	
    	//refreshing instructorLV
    	refresh();
    }
    
    @FXML
    /*
     * Upon selection of a Instructor object in instructorLV, the instructorDAO will fetch data about
     * the instructor including taught classes, display data in the labels, and display taught classes
     * in teachesLV. It will also enable GUI elements allowing data modification.
     */
    private void instructorClicked() {
    	
    	teachesLV.getItems().clear();
    	
    	//retrieving selected instructor object from uni.db
    	int id = instructorLV.getSelectionModel().getSelectedItem().getId();
    	Instructor i = instructorDAO.getInstructor(id);
    	
    	//updating GUI elements with values of selected instructor object
    	idTF.setText(String.valueOf(i.getId()));
    	nameTF.setText(i.getName());
    	deptTF.setText(i.getDept());
    	salTF.setText(String.valueOf(i.getSal()));
    	
    	//retrieving taught courses from uni.db and populating teachesLV
    	ArrayList<String> coursesTaught = (ArrayList<String>) instructorDAO.getTaughtCourses(i.getId());
    	teachesLV.getItems().addAll(coursesTaught);
    	
    	//configuring GUI elements to allow for data modification
    	deleteBtn.setDisable(false);
    	updateBtn.setDisable(false);
    	nameTF.setEditable(true);
    	deptTF.setEditable(true);
    	salTF.setEditable(true);
    }

	@Override
	/*
     * Populates instructorLV with all instructors from uni.db when root set to instructor.fxml
     */
	public void initialize(URL location, ResourceBundle resources) {
		
		//populating instructorLV
		refresh();
	}
	
	/*
     * private helper method to refresh the list of instructors
     */
	private void refresh() {
		//retrieving all instructors from uni.db and displaying in instructorLV
		ArrayList<Instructor> allInstructors = (ArrayList<Instructor>) instructorDAO.listAllInstructor();
		instructorLV.getItems().clear();
		instructorLV.getItems().addAll(allInstructors);
	}
}