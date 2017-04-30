package gui.owner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;

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
    	
    	pickEmployee.getItems().addAll(c.getEmployeeList());
    	pickDay.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AvailabilityPicker.fxml"));
			TimePickerPane.getChildren().clear();
			TimePickerPane.getChildren().add(loader.load());
			
			time = loader.getController();
			time.init(TimePickerPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	pickDay.getSelectionModel().select(0);
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
    	System.out.println(fullListOfDays.toString());
    	for(ArrayList<String> days : fullListOfDays) {
    		int i = 1;
    		for(String currentTime : days) {
    			if (i%2 == 0) { // skip every second option
    				//continue; // not sure if this works in java but blank works as well so leaving this here
    			}
    			else {

        			availabilitiesToSubmit.add(currentTime + " " + days.get(i));
        			i++;
    				
    			}
    		}
    	}
    	System.out.println(employeeId + " - " + availabilitiesToSubmit.toString());
    	c.editAvailability(employeeId, availabilitiesToSubmit);
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	this.employeeId = new StringTokenizer(pickEmployee.getSelectionModel().getSelectedItem()).nextToken(":");
    	update();
    }

    private void update() {
    	time.deselectAll();
    	if (employeeId != null) {
       	 	String[][] availabilities = c.utilities.getEmployeeAvailability(employeeId).toStringArray();
       	 	if (fullListOfDays.get(whichDay(currentDay)).size() > 0) {
       	 		System.out.println("test1");
           	 	time.setDefaultAvailabilityFromList(fullListOfDays.get(whichDay(currentDay)), currentDay);
       	 	} else
       	 	{
       	 		System.out.println("test2");
           	 	time.setDefaultAvailability(availabilities, currentDay);
       	 	}
    	}
    	
	}

	@FXML
    void updateCurrentAvailability(ActionEvent event) {
		fullListOfDays.get(whichDay(currentDay)).clear();
		ArrayList<String> savedTimes = time.saveTimes(currentDay);
		if (savedTimes == null) {
			this.update();
			return;
		}
		for(String s : savedTimes) {
			fullListOfDays.get(whichDay(currentDay)).add(s);
		}
		currentDay = pickDay.getSelectionModel().getSelectedItem();
    	this.update();
    }
	
	int whichDay(String day) {
		for(int i = 0; i < 6; i++) {
			if (listOfDays[i] == day) {
				return i;
			}
		}
		return 0;
	}

}