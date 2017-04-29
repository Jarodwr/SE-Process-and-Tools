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

public class OwnerAddEmployeeWorkingTimesController {
	
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
    }

    @FXML
    void submit(ActionEvent event) {
    	System.out.println(time.saveTimes("Monday").toString());
    }

    @FXML
    void updateAvailability(ActionEvent event) {
    	employeeId = new StringTokenizer(pickEmployee.getSelectionModel().getSelectedItem(), ":").nextToken();
    	this.update();
    }

    private void update() {
    	time.deselectAll();
    	if (employeeId != null) {
       	 	String[][] availabilities = c.utilities.getEmployeeAvailability(employeeId).toStringArray();
       	 	time.setDefaultAvailability(availabilities, currentDay);
    	}
    	
	}

	@FXML
    void updateCurrentAvailability(ActionEvent event) {
		fullListOfDays.get(whichDay(currentDay)).clear();
		ArrayList<String> savedTimes = time.saveTimes(currentDay);
		if (savedTimes == null) return;
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
