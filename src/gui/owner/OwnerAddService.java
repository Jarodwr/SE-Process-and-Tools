package gui.owner;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;
import model.service.Service;

public class OwnerAddService {
	
	private Controller c;
	
    @FXML
    private TextField addName;

    @FXML
    private TextField addPrice;

    @FXML
    private TextField addTimeTaken;

    @FXML
    private Button deleteService;

    @FXML
    private Label warning;

    @FXML
    private ComboBox<String> comboServiceList;

    @FXML
    private TextField deleteName;

    @FXML
    private TextField deletePrice;

    @FXML
    private TextField deleteTimeTaken;

    @FXML
    private Button addService;

    @FXML
    private Button editService;

    @FXML
    void editService(ActionEvent event) {
    	String newName = deleteName.getText();
    	String newPrice = deletePrice.getText();
    	String newTimeTaken = deleteTimeTaken.getText();
    	// edit service function
    }

    @FXML
    void addService(ActionEvent event) {
    	String newName = addName.getText();
    	String newPrice = addPrice.getText();
    	String newTimeTaken = addTimeTaken.getText();
    	if (c.addService(newName, convertPrice(newPrice), newTimeTaken)) {
    		editWarning(newName + " successfully added as a service.", "black");
    	}
    	else {
    		editWarning("Service already exists!", "red");
    	}
    }

    private int convertPrice(String newPrice) {
    	if (newPrice.charAt(0) == '$') {
    		newPrice.substring(1);
    	}
    	String[] exploded = newPrice.split(".");
    	if (exploded.length != 2) {
    		return -1;
    	}
    	int sum = 0;
    	sum = sum + Integer.parseInt(exploded[0]) * 100;
    	sum = sum + Integer.parseInt(exploded[1]);
    	return sum;
	}

	private void editWarning(String string, String color) {
		warning.setText(string);
		if (color == "black") {
			warning.setTextFill(Color.BLACK);
		}
		else {
			warning.setTextFill(Color.RED);
		}
		
	}

	@FXML
    void deleteService(ActionEvent event) {
		String selectedService = comboServiceList.getSelectionModel().getSelectedItem().toString();
		System.out.println(selectedService);
		if (selectedService == "Service List") return;
    }

    @FXML
    void validateMinutes(InputMethodEvent event) {

    }

    @FXML
    void validateName(InputMethodEvent event) {

    }

    @FXML
    void validatePrice(InputMethodEvent event) {

    }

	void init(Controller controller) {
		this.c = controller;
		
		comboServiceList.getItems().clear();
		Service[] servicelist = this.c.utilities.getAllServices();
		for(Service s : servicelist) {
			comboServiceList.getItems().add(s.serviceName);
		}
	}
}


