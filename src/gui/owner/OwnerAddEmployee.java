package gui.owner;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    
    private Controller controller;
    
    @FXML
    void submitEmployee(ActionEvent event) {
    	if (controller.addEmployee(name.getText(), phone.getText(), address.getText(), controller.getOwner())) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Add Employee");
    		alert.setHeaderText(null);
    		alert.setContentText("Employee " + name.getText() + " successfully added.");
    		alert.showAndWait();
    	} else {
    		String warnMsg = "";
    		
    		if(!name.getText().matches("[A-Za-z -']+")) {
    			warnMsg += "name is not valid\n";
    		}
    		
    		if(!phone.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
    			warnMsg += "phone number is not valid\n";
    		}
    		
    		if(!address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
    			warnMsg += "address is not valid";
    		}
    		
    		warning.setText(warnMsg);
    	}
    }
    
    public void init(Controller controller) {
    	this.controller = controller;
    }

}
