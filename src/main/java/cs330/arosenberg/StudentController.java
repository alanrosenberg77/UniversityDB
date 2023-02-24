package cs330.arosenberg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.JDBCStudentDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * StudentControllers will function as the controller for the student window when
 * root is set to student.fxml. Adds functionality to all buttons and some GUI elements.
 * Also initializes the window with default configurations of GUI elements.
 * @author alanr
 *
 */
public class StudentController implements Initializable {

	/*
	 * FXML elements
	 */
	@FXML
	TextField idTF;
	
	@FXML
	TextField nameTF;
	
	@FXML
	TextField majorTF;
	
	@FXML
	TextField creditTF;
	
	@FXML
	TextField advisorTF;
	
	@FXML
	TextField gpaTF;
	
	@FXML
	Button deleteBtn;
	
	@FXML
	Button updateBtn;
	
	@FXML
	Button doneBtn;
	
	@FXML
	Button addCourseBtn;
	
	@FXML
	ListView<Student> studentLV;
	
	@FXML
	ListView<String> transcriptLV;
	
	//local data access object
	JDBCStudentDAO studentDAO;
	
	//constructor to initialize DAO
	public StudentController() {
		studentDAO = new JDBCStudentDAO();
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
     * simple method to switch to course window
     */
    private void switchToCourse() throws IOException {
    	System.err.println("switching to course");

    	App.setRoot("course");
    }
    
    @FXML
    /*
     * algorithm to add a new course to a student's transcript
     * Uses dialogs to get info from user, then makes a new tuple
     * in the takes table of uni.db
     */
    private void addButtonDo() {
    	/*
    	 * configuring all dialogs
    	 */
    	TextInputDialog idDialog = new TextInputDialog();
    	idDialog.setTitle("Add Course");
    	idDialog.setContentText("Enter Course ID:");
    	
    	TextInputDialog semDialog = new TextInputDialog();
    	semDialog.setTitle("Add Course");
    	semDialog.setContentText("Enter Semester:");
    	
    	TextInputDialog yearDialog = new TextInputDialog();
    	yearDialog.setTitle("Add Course");
    	yearDialog.setContentText("Enter Year:");
    	
    	TextInputDialog gradeDialog = new TextInputDialog();
    	gradeDialog.setTitle("Add Course");
    	gradeDialog.setContentText("Enter Grade:");
    	
    	/*
    	 * showing, waiting, and retrieving inputs
    	 */
    	idDialog.showAndWait();
    	int id = Integer.parseInt(idDialog.getEditor().getText());
    	
    	semDialog.showAndWait();
    	String sem = semDialog.getEditor().getText();
    	
    	yearDialog.showAndWait();
    	int year = Integer.parseInt(yearDialog.getEditor().getText());
    	
    	gradeDialog.showAndWait();
    	String grade = gradeDialog.getEditor().getText();
    	
    	//adding course to transcript
    	studentDAO.addToTranscript(Integer.parseInt(idTF.getText()), id, sem, year, grade);
    	
    	//restoring GUI elements to default config
    	transcriptLV.getItems().clear();
    	studentLV.getSelectionModel().clearSelection();
    	idTF.setText("");
    	nameTF.setText("");
    	majorTF.setText("");
    	creditTF.setText("");
    	advisorTF.setText("");
    	gpaTF.setText("");
    	nameTF.setEditable(false);
    	majorTF.setEditable(false);
    	advisorTF.setEditable(false);
    }
    
    @FXML
    /*
     * first half of the algorithm to create a new student object by
     * configuring GUI elements to prepare them to receive new values.
     */
    private void createButtonDo() {
    	System.err.println("create button pressed");

    	//preparing GUI elements for input
    	studentLV.getSelectionModel().clearSelection();
    	transcriptLV.getItems().clear();
    	idTF.setText("");
    	nameTF.setText("");
    	majorTF.setText("");
    	creditTF.setText("");
    	advisorTF.setText("");
    	gpaTF.setText("");
    	idTF.setPromptText("Enter integer");
    	nameTF.setPromptText("Enter characters");
    	majorTF.setPromptText("Enter characters");
    	advisorTF.setPromptText("Enter int ID");
    	
    	//prevents inputs in wrong places
    	idTF.setEditable(true);
    	nameTF.setEditable(true);
    	majorTF.setEditable(true);
    	creditTF.setDisable(true);
    	advisorTF.setEditable(true);
    	gpaTF.setDisable(true);
    	
    	//showing done button
    	updateBtn.setVisible(false);
    	updateBtn.setDisable(true);
    	doneBtn.setVisible(true);
    	doneBtn.setDisable(false);
    	
    	
    }
    
    @FXML
    /*
     * second half of the algorithm to create a new student object. Starts by grabbing
     * values of the GUI elements, then creates a new student with those values in uni.db.
     * Then method then restores GUI elements to their default configurations.
     */
    private void doneButtonDo() {
    	System.err.println("done button pressed");
    	
    	//preventing empty inputs
    	if(idTF.getText().equals("")) {
    		idTF.setPromptText("Enter integer");
    		
    	} else if (nameTF.getText().equals("")) {
    		nameTF.setPromptText("Enter characters");
    		
    	} else if (majorTF.getText().equals("")) {
    		majorTF.setPromptText("Enter characters");
    		
    	} else if(advisorTF.getText().equals("")) {
    		advisorTF.setPromptText("Enter integer");	
    		
    	} else {
    		//creating new student with input values
    		studentDAO.createStudent(Integer.parseInt(idTF.getText()), nameTF.getText(), majorTF.getText(),
    				Integer.parseInt(advisorTF.getText()));
    		
    		//restoring GUI elements to default configs
    		idTF.setEditable(false);
        	creditTF.setDisable(false);
        	advisorTF.setDisable(false);
        	gpaTF.setDisable(false);
        	nameTF.setEditable(false);
        	majorTF.setEditable(false);
        	advisorTF.setEditable(false);
        	
        	idTF.setText("");
        	nameTF.setText("");
        	majorTF.setText("");
        	creditTF.setText("");
        	advisorTF.setText("");
        	gpaTF.setText("");
        	
        	idTF.setPromptText("");
        	nameTF.setPromptText("");
        	majorTF.setPromptText("");
        	advisorTF.setPromptText("");
        	
        	updateBtn.setVisible(true);
        	updateBtn.setDisable(false);
        	doneBtn.setVisible(false);
        	doneBtn.setDisable(true);
        	
        	//refreshing studentLV
        	refresh();
    	}
    }
    
    @FXML
    /*
     * removes the selected Student object from studentLV, deletes it from the database,
     * and resets GUI to prevent modification of null objects
     */
    private void deleteButtonDo() {
    	System.err.println("delete button pressed");

    	//getting select item and removing it from studentLV and uni.db
    	int i = studentLV.getSelectionModel().getSelectedIndex();
    	Student s = studentLV.getItems().remove(i);
    	studentDAO.deleteStudent(s);
    	
    	//restoring GUI elements to default configs
    	transcriptLV.getItems().clear();
    	studentLV.getSelectionModel().clearSelection();
    	idTF.setText("");
    	nameTF.setText("");
    	majorTF.setText("");
    	creditTF.setText("");
    	advisorTF.setText("");
    	gpaTF.setText("");
    	
    	deleteBtn.setDisable(true);
    	updateBtn.setDisable(true);
    }
    
    @FXML
    /*
     * uses current values of the GUI elements to update the currently selected student object
     */
    private void updateButtonDo() {
    	System.err.println("update button pressed");
    	
    	//using values from GUI elements to update selected student
    	Student s = new Student(Integer.parseInt(idTF.getText()), nameTF.getText(), majorTF.getText());
    	studentDAO.updateStudent(s);
    	
    	//refreshing studentLV
    	refresh();
    }

    @FXML
    /*
     * Upon selection of a Student object in studentLV, the studentDAO will fetch data about
     * the student including its transcript, display data in the labels, and display transcript
     * in transcriptLV. It will also enable GUI elements allowing data modification.
     */
    private void studentClicked() {
    	
    	transcriptLV.getItems().clear();
    	
    	//retrieving selected student object from uni.db
    	int id = studentLV.getSelectionModel().getSelectedItem().getId();
    	Student s = studentDAO.getStudent(id);
    	
    	//deriving attributes
    	s.setAdvisor(studentDAO.getAdvisor(id));
    	s.setCredit(studentDAO.getCredits(id));
    	s.setGpa(studentDAO.getGPA(id));
    	
    	//retrieving transcript from uni.db and population transcriptLV
    	ArrayList<String> transcript = (ArrayList<String>) studentDAO.getTranscript(s.getId());
    	transcriptLV.getItems().addAll(transcript);
    	
    	//updating GUI elements with values of selected student object
    	idTF.setText(String.valueOf(s.getId()));
    	nameTF.setText(s.getName());
    	majorTF.setText(s.getMajor());
    	creditTF.setText(String.valueOf(s.getCredit()));
    	advisorTF.setText(s.getAdvisor().toString());
    	gpaTF.setText(String.format("%.2f", s.getGpa()));
    	
    	//configuring GUI elements to allow for data manipulation
    	deleteBtn.setDisable(false);
    	updateBtn.setDisable(false);
    	nameTF.setEditable(true);
    	majorTF.setEditable(true);
    	
    }
    
    @Override
    /*
     * Populates studentLV with all students from uni.db when root set to student.fxml
     */
	public void initialize(URL location, ResourceBundle resources) {
	    
    	//populating studentLV
	    refresh();
	    
	}
    
    /*
     * private helper method to refresh the list of students
     */
    private void refresh(){
    	//retrieving all students from uni.db and displaying in studentLV
    	ArrayList<Student> allStudents = (ArrayList<Student>) studentDAO.listAllStudent();
	    studentLV.getItems().clear();
	    studentLV.getItems().addAll(allStudents);
    }
}
