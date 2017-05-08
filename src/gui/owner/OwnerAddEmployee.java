package gui.owner;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class OwnerAddEmployee {

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    @FXML
    private Label warning;

    @FXML
    private Button submitEmployee;
    
    @FXML
    private GridPane timeGrid;
    
    @FXML
    private Label errorMessage;
    
    private Controller controller;
    
    @FXML
    void submitEmployee(ActionEvent event) {	//TODO: When exception is added use try/catch to display error
    	if (controller.addEmployee(name.getText(), phone.getText(), address.getText(), controller.getOwner())) {
    		errorMessage.setStyle("-fx-text-fill: GREEN");
    		errorMessage.setText("Employee " + name.getText() + " successfully added.");
    	} else {
    		errorMessage.setStyle("-fx-text-fill: red");
    		errorMessage.setText("Failed to add employee!");
    	}
    }
    

    @FXML
    void addressChange(KeyEvent event) {
		if(!address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			address.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
    		errorMessage.setText("Address is invalid");
    	} else {
			address.setStyle("-fx-border-color: green");
			errorMessage.setStyle("-fx-text-fill: #F2F2F2");
			checkFields();
    	}
    }

    @FXML
    void nameChange(KeyEvent event) {
		if(!name.getText().matches("[-A-Za-z ']+")) {
			name.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
    		errorMessage.setText("Name is invalid");
		} else {
			name.setStyle("-fx-border-color: green");
			errorMessage.setStyle("-fx-text-fill: #F2F2F2");
			checkFields();
		}
	}

    @FXML
    void phoneChange(KeyEvent event) {
		if(!phone.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			phone.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
    		errorMessage.setText("Phone number is invalid");
		} else {
			phone.setStyle("-fx-border-color: green");
			errorMessage.setStyle("-fx-text-fill: #F2F2F2");
			checkFields();
		}
    }
    
    public void init(Controller controller) {
    	this.controller = controller;
    	submitEmployee.setDisable(true);
    	checkFields();
    }

    private void checkFields() {
    	if(name.getText().matches("[-A-Za-z ']+") && 
    			phone.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
    			address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
    		submitEmployee.setDisable(false);
    	else
    		submitEmployee.setDisable(true);
    }
}
