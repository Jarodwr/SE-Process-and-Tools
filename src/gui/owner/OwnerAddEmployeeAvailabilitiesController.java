package gui.owner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;
import model.period.Period;

public class OwnerAddEmployeeAvailabilitiesController {
	
	private Controller c;
	
	private String currentDay = "Monday";

	private ArrayList<String> mondayTimes = new ArrayList<String>();
	private ArrayList<String> tuesdayTimes = new ArrayList<String>();
	private ArrayList<String> wednesdayTimes = new ArrayList<String>();
	private ArrayList<String> thursdayTimes = new ArrayList<String>();
	private ArrayList<String> fridayTimes = new ArrayList<String>();
	private ArrayList<String> saturdayTimes = new ArrayList<String>();
	private ArrayList<String> sundayTimes = new ArrayList<String>();
	
	private ArrayList<ArrayList<String>> fullListOfDays = new ArrayList<ArrayList<String>>();
	
											
	private final String[] listOfDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @FXML
    private ComboBox<String> pickEmployee;

    @FXML
    private Label warning;

    @FXML
    private Button SubmitButton;

    @FXML
    private ComboBox<String> pickDay;

    @FXML
    private Pane TimePickerPane;
  
    

    private TimePicker time;

	private String employeeId;
    
    void init(Controller c) {
    	this.c = c;
   
    	
    	fullListOfDays.add(mondayTimes);
    	fullListOfDays.add(tuesdayTimes);
    	fullListOfDays.add(wednesdayTimes);
    	fullListOfDays.add(thursdayTimes);
    	fullListOfDays.add(fridayTimes);
    	fullListOfDays.add(saturdayTimes);
    	fullListOfDays.add(sundayTimes);
    	
    	String[] employees = c.getEmployeeList();
    	if (employees != null) {
    		pickEmployee.getItems().addAll(employees);
    	}
    	
    	pickDay.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AvailabilityPicker.fxml"));  // load GUI element
			TimePickerPane.getChildren().clear();
			TimePickerPane.getChildren().add(loader.load());
			
			time = loader.getController();
			time.init(TimePickerPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	pickDay.getSelectionModel().select(0); // pick monday
    }

    @FXML
    void submit(ActionEvent event) {
    	ArrayList<String> savedTimes = time.saveTimes(currentDay);
    	if (savedTimes != null ) {
    		for(String s : savedTimes) {
    			fullListOfDays.get(whichDay(currentDay)).add(s);
    		}
    	}
    	ArrayList<String> availabilitiesToSubmit = new ArrayList<String>();
    	for(ArrayList<String> days : fullListOfDays) { //  copy into final arraylist
    		int i = 1;
    		for(String currentTime : days) {
    			if (i%2 == 0) { // skip every second option
    				//continue; // not sure if this works in java but blank works as well so leaving this here
    			}
    			else {
        			availabilitiesToSubmit.add(currentTime + " " + days.get(i));
    				
    			}
    			i++;
    		}
    	}
    	Iterator<String> iter = availabilitiesToSubmit.iterator();
    	while(iter.hasNext()) {
    		String s = iter.next();
    		int i = 0;
    		for(String s2 : availabilitiesToSubmit) {
    			if (s2.equals(s)) {
    				if (i == 0) {
    					i++;
    				}
    				else {
    					iter.remove();
    					break;
    				}
    			}
    		}
    	}
    	
    	c.editAvailability(employeeId, availabilitiesToSubmit);
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add Availability");
		alert.setHeaderText("Availability successfully updated!");
		alert.setContentText("press ok to continue...");
		
		alert.showAndWait();
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	this.employeeId = new StringTokenizer(pickEmployee.getSelectionModel().getSelectedItem()).nextToken(":");
    	this.clearAvailabilities();
    	loadCurrentAvailabilities(this.c.utilities.getEmployeeAvailability(employeeId).toStringArray());
    	update();
    }

    private void clearAvailabilities() { // empty selected availabilities
		for(ArrayList<String> days : fullListOfDays) {
			days.clear();
		}
		
	}

	private void update() { // refresh page with new data
    	time.deselectAll();
    	if (employeeId != null) {
    		
       	 	ArrayList<String> cDay = fullListOfDays.get(whichDay(currentDay));
           	time.setDefaultAvailabilityFromList(cDay, currentDay);
           	
    	}
	}

	@FXML
    void updateCurrentAvailability(ActionEvent event) { // changed day combobox
		fullListOfDays.get(whichDay(currentDay)).clear();
		ArrayList<String> savedTimes = time.saveTimes(currentDay);
		if (savedTimes == null) {
			currentDay = pickDay.getSelectionModel().getSelectedItem();
			this.update();
			return;
		}
		for(String s : savedTimes) {
			fullListOfDays.get(whichDay(currentDay)).add(s);
		}
		currentDay = pickDay.getSelectionModel().getSelectedItem();
    	this.update();
    }
	
	int whichDay(String day) {  // gives a number from 0 - 6 indicating the index of the day in the fullListOfDays array
		for(int i = 0; i < 6; i++) {
			if (listOfDays[i] == day) {
				return i;
			} 
		}
		return 0;
	}
	
	void loadCurrentAvailabilities(String[][] availabilities) { // load the employee's existing availabilities onto our menu
			clearAvailabilities();
		if (availabilities == null || availabilities.length == 0) {
    		return;
		}
    	
		for(String[] s : availabilities) {
			String dayToInsert = Period.convertSecondsToDay(s[0]);
			fullListOfDays.get(whichDay(dayToInsert)).add(s[0]);
			fullListOfDays.get(whichDay(dayToInsert)).add(s[1]);
			
		}
	}

}
