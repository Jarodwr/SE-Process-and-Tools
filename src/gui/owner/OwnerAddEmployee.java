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
    private Label errorMessage;

    @FXML
    private Button submitEmployee;
    
    @FXML
    private GridPane timeGrid;
    
    private Controller controller;
    
    @FXML
    void submitEmployee(ActionEvent event) {
    	if (controller.addEmployee(name.getText(), phone.getText(), address.getText(), controller.getOwner())) {
    		errorMessage.setStyle("-fx-text-fill: GREEN");
        	errorMessage.setText("Employee successfully added!");
    	} else {
    		errorMessage.setStyle("-fx-text-fill: RED");
        	errorMessage.setText("Failed to add Employee!");
    	}
    }
    

    @FXML
    void checkAll(KeyEvent event) {
		checkAll();
    }

    
    public void init(Controller controller) {
    	this.controller = controller;
    	submitEmployee.setDisable(true);
    }

    private void checkAll()
    {
    	if(!address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			address.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
        	errorMessage.setText("Please enter a valid address!");
    	} else {
			address.setStyle("-fx-border-color: green");
    	}
    	
    	if(!phone.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			phone.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
        	errorMessage.setText("Please enter a valid phone number!");
		} else {
			phone.setStyle("-fx-border-color: green");
		}
    	
    	if(!name.getText().matches("[-A-Za-z ']+")) {
			name.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
        	errorMessage.setText("Name contains invalid characters!");
		} else {
			name.setStyle("-fx-border-color: green");
			
			
		}
    	if(checkFields())
    		errorMessage.setStyle("-fx-text-fill: #F2F2F2");
    	
    }
    
    private boolean checkFields() {
    	if(name.getText().matches("[-A-Za-z ']+") && 
    			phone.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
    			address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
    	{
    		submitEmployee.setDisable(false);
    		return true;
    	}
    	else
    	{
    		submitEmployee.setDisable(true);
    		return false;
    	}
    		
    }
}
