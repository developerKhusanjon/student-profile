package dev.khusanjon.studentprofile.controller;

import dev.khusanjon.studentprofile.config.StageManager;
import dev.khusanjon.studentprofile.model.Student;
import dev.khusanjon.studentprofile.service.StudentService;
import dev.khusanjon.studentprofile.view.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController implements Initializable{

	@FXML
    private Button btnLogout;
	
	@FXML
    private Label userId;
	
	@FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;
    
    @FXML
    private RadioButton rbMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbFemale;
    
    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    
    @FXML
    private Button reset;
	
	@FXML
    private Button saveUser;
	
	@FXML
	private TableView<Student> userTable;

	@FXML
	private TableColumn<Student, Long> colUserId;

	@FXML
	private TableColumn<Student, String> colFirstName;

	@FXML
	private TableColumn<Student, String> colLastName;

	@FXML
	private TableColumn<Student, LocalDate> colDOB;

	@FXML
	private TableColumn<Student, String> colGender;
	
	@FXML
    private TableColumn<Student, String> colRole;

	@FXML
	private TableColumn<Student, String> colEmail;
	
	@FXML
    private TableColumn<Student, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteUsers;
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private StudentService studentService;
	
	private ObservableList<Student> userList = FXCollections.observableArrayList();
	private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");
	
	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
    	stageManager.switchScene(FxmlView.LOGIN);
    }
    
    @FXML
    void reset(ActionEvent event) {
    	clearFields();
    }
    
    @FXML
    private void saveUser(ActionEvent event){
    	
    	if(validate("First Name", getFirstName(), "[a-zA-Z]+") &&
    	   validate("Last Name", getLastName(), "[a-zA-Z]+") &&
    	   emptyValidation("DOB", dob.getEditor().getText().isEmpty()) && 
    	   emptyValidation("Role", getRole() == null) ){
    		
    		if(userId.getText() == null || userId.getText() == ""){
    			if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
    				emptyValidation("Password", getPassword().isEmpty())){
    				
    				Student student = new Student();
        			student.setFirstName(getFirstName());
        			student.setLastName(getLastName());
        			student.setDob(getDob());
        			student.setGender(getGender());
        			student.setRole(getRole());
        			student.setEmail(getEmail());
        			student.setPassword(getPassword());
        			
        			Student newUser = studentService.save(student);
        			
        			saveAlert(newUser);
    			}
    			
    		}else{
    			Student student = studentService.find(Long.parseLong(userId.getText().trim()));
    			student.setFirstName(getFirstName());
    			student.setLastName(getLastName());
    			student.setDob(getDob());
    			student.setGender(getGender());
    			student.setRole(getRole());
    			Student updatedUser =  studentService.update(student);
    			updateAlert(updatedUser);
    		}
    		
    		clearFields();
    		loadUserDetails();
    	}
    	
    	
    }
    
    @FXML
    private void deleteUsers(ActionEvent event){
    	List<Student> users = userTable.getSelectionModel().getSelectedItems();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) studentService.deleteInBatch(users);
    	
    	loadUserDetails();
    }
    
   	private void clearFields() {
		userId.setText(null);
		firstName.clear();
		lastName.clear();
		dob.getEditor().clear();
		rbMale.setSelected(true);
		rbFemale.setSelected(false);
		cbRole.getSelectionModel().clearSelection();
		email.clear();
		password.clear();
	}
	
	private void saveAlert(Student user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created and \n"+getGenderTitle(user.getGender())+" id is "+ user.getId() +".");
		alert.showAndWait();
	}
	
	private void updateAlert(Student user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been updated.");
		alert.showAndWait();
	}
	
	private String getGenderTitle(String gender){
		return (gender.equals("Male")) ? "his" : "her";
	}

	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public LocalDate getDob() {
		return dob.getValue();
	}

	public String getGender(){
		return rbMale.isSelected() ? "Male" : "Female";
	}
	
	public String getRole() {
		return cbRole.getSelectionModel().getSelectedItem();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}
  

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cbRole.setItems(roles);
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();

		loadUserDetails();
	}
	

	private void setColumnProperties(){
		colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEdit.setCellFactory(cellFactory);
	}
	
	Callback<TableColumn<Student, Boolean>, TableCell<Student, Boolean>> cellFactory =
			new Callback<TableColumn<Student, Boolean>, TableCell<Student, Boolean>>()
	{
		@Override
		public TableCell<Student, Boolean> call( final TableColumn<Student, Boolean> param)
		{
			final TableCell<Student, Boolean> cell = new TableCell<Student, Boolean>()
			{
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();
				
				@Override
				public void updateItem(Boolean check, boolean empty)
				{
					super.updateItem(check, empty);
					if(empty)
					{
						setGraphic(null);
						setText(null);
					}
					else{
						btnEdit.setOnAction(e ->{
							Student user = getTableView().getItems().get(getIndex());
							updateUser(user);
						});
						
						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
				        iv.setImage(imgEdit);
				        iv.setPreserveRatio(true);
				        iv.setSmooth(true);
				        iv.setCache(true);
						btnEdit.setGraphic(iv);
						
						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateUser(Student user) {
					userId.setText(user.getId().toString());
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					dob.setValue(user.getDob());
					if(user.getGender().equals("Male")) rbMale.setSelected(true);
					else rbFemale.setSelected(true);
					cbRole.getSelectionModel().select(user.getRole());
				}
			};
			return cell;
		}
	};


	private void loadUserDetails(){
		userList.clear();
		userList.addAll(studentService.findAll());

		userTable.setItems(userList);
	}
	

	private boolean validate(String field, String value, String pattern){
		if(!value.isEmpty()){
			Pattern p = Pattern.compile(pattern);
	        Matcher m = p.matcher(value);
	        if(m.find() && m.group().equals(value)){
	            return true;
	        }else{
	        	validationAlert(field, false);            
	            return false;            
	        }
		}else{
			validationAlert(field, true);            
            return false;
		}        
    }
	
	private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
        	validationAlert(field, true);            
            return false;            
        }
    }	
	
	private void validationAlert(String field, boolean empty){
		Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
        	if(empty) alert.setContentText("Please Enter "+ field);
        	else alert.setContentText("Please Enter Valid "+ field);
        }
        alert.showAndWait();
	}
}
