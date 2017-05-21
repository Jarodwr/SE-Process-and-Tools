package SARJ.BookingSystem.gui.owner;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SARJ.BookingSystem.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class OwnerAddService {
	
	private Controller c;
    
    @FXML
    private Pane addPane;
 
/*
    @FXML
    private Button deleteService; */

    @FXML
    private Label warning;
/*
    @FXML
    private ComboBox<String> comboServiceList; */

    @FXML
    private TextField Name;

    @FXML
    private TextField Price;

    @FXML
    private TextField TimeTaken;

    @FXML
    private Button addService;
/*
    @FXML
    private Button editService; */

	private boolean cannotProceedName = true;

	private boolean cannotProceedPrice = true;

	private boolean cannotProceedTimeTaken = true;

    /*
    @FXML
    void editService(ActionEvent event) {
    	if (mode == "add") return;
    	String newName = deleteName.getText();
    	String newPrice = deletePrice.getText();
    	String newTimeTaken = deleteTimeTaken.getText();
    	// edit service function
    }
    */
    @FXML
    void addService(ActionEvent event) {
    	//if (mode == "deleteEdit") return;
    	String errorMsg = "";
    	if (cannotProceedName) {
    		validateNameRegex(Name.getText()); // validate again, bug fix
    		if (cannotProceedName) { // check again
    			errorMsg = errorMsg + "Invalid characters in name field. ";
    		}
    	}
    	if (cannotProceedPrice) {
    		errorMsg = errorMsg + "Invalid characters in price field. ";
    	}
    	if (cannotProceedTimeTaken) {
    		errorMsg = errorMsg  + "Invalid amount of periods (30 minute blocks) in time taken field. ";
    	}
    	if (errorMsg != "") {
    		errorMsg = errorMsg + "Errors found in input fields, please fix to continue.";
    		editWarning(errorMsg, "red");
    		return;
    	}
    	String newName = Name.getText();
    	String newPrice = Price.getText();
    	String newTimeTaken = TimeTaken.getText();
    	if (c.addService(newName, convertPrice(newPrice), newTimeTaken)) {
    		editWarning(newName + " successfully added as a service.", "black");
    	}
    	else {
    		editWarning("Service already exists!", "red");
    	}
    }
    /*
    @FXML
    void toggleMode(ActionEvent event) {
    	if (mode == "add") {
    		addPane.setVisible(false);
    		mode = "deleteEdit";
    		deleteEditPane.setVisible(true);
    	}
    	else { 
    	deleteEditPane.setVisible(false);
		mode = "add";
		addPane.setVisible(true);
	}
    	
    } */

    private void validateNameRegex(String text) {
    	String regx = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}"; // source: http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters
    	Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
        	editWarning("Name field has invalid characters, or is empty.", "red");
        	cannotProceedName = true;
        }
        else {
        	cannotProceedName = false;
        	clearWarningField();
        }
		
	}

	private int convertPrice(String newPrice) {
		Number number = null;
		try {
    	    number = NumberFormat.getCurrencyInstance(Locale.US).parse(Price.getText());
    	} catch(ParseException pe) {
    		return -1;
    	}
		
		if (number == null) {
			return -1;
		}
		
		return  (int) (number.doubleValue() * 100);
		/*
    	if (newPrice.charAt(0) == '$') {
    		newPrice.substring(1);
    	}
    	String[] exploded = newPrice.split(".");
    	if (exploded.length != 2) {
    		int sum = 0;
        	sum = sum + Integer.parseInt(exploded[0]) * 100;
        	return 0;
    	}
    	int sum = 0;
    	sum = sum + Integer.parseInt(exploded[0]) * 100;
    	sum = sum + Integer.parseInt(exploded[1]);
    	return sum; */
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
	/*
	@FXML
    void deleteService(ActionEvent event) {
		if (mode == "add") return;
		String selectedService = comboServiceList.getSelectionModel().getSelectedItem().toString();
		System.out.println(selectedService);
		if (selectedService == "Service List") return;
    }*/

    @FXML
    void validateMinutes(KeyEvent event) {
    	try {
    		int i = Integer.parseInt(TimeTaken.getText());
    		if (i % 30 != 0) {
    			throw new Exception();
    		}
    		cannotProceedTimeTaken = false;
    		clearWarningField();
    	}
    	catch(Exception e) {
    		cannotProceedTimeTaken = true;
        	editWarning("Time Taken field has invalid characters, or is not a multiple of 30.", "red");
    	}
    	
    }

    @FXML
    void validateName(KeyEvent event) {
    	validateNameRegex(Name.getText());
    }

    private void clearWarningField() {
		warning.setText("");
		
	}

	@FXML
    void validatePrice(KeyEvent event) {
    	Number number = null;
    	try {
    	    number = NumberFormat.getCurrencyInstance(Locale.US).parse(Price.getText());
    	} catch(ParseException pe) {
    	    // ignore
    	}

    	if (number != null) {
        	cannotProceedPrice = false;
        	clearWarningField();
    	}
    	else {
    		
    	}

    }

	public void init(Controller controller) {
		this.c = controller;
	}
}


