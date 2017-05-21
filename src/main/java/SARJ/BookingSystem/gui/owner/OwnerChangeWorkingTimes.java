package SARJ.BookingSystem.gui.owner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.gui.Accent;
import SARJ.BookingSystem.model.Timetable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class OwnerChangeWorkingTimes implements Accent{

    @FXML
    private ComboBox<String> employeeMenu;

    @FXML
    private DatePicker dateMenu;

    @FXML
    private Button submit;

    @FXML
    private Pane timeMenu;
    
    @FXML
    private Label errorMessage;
    
    private WorkingTimesPicker time;
    
    private String employeeId;
    private LocalDate date;
    
    private Controller controller;

    @FXML
    void dateSelect(ActionEvent event) {
    	this.date = dateMenu.getValue();
    	update();
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	this.employeeId = new StringTokenizer(employeeMenu.getValue()).nextToken(":");
    	update();
    }

    @FXML
    void updateWorkingTime(ActionEvent event) {
    	int[] selected = time.getSelectedPeriods();
    	ArrayList<Integer> unselected = new ArrayList<Integer>();
    	
    	for (int i = 0; i < 48; i++) {
    		unselected.add(i);
    	}

    	boolean warn = false;
    	for (int i : selected) {
    		unselected.remove(Integer.valueOf(i));
    		controller.addWorkingTime(employeeId, Long.toString(date.toEpochDay() * 24 * 60 * 60), Integer.toString(i * 30 * 60), Integer.toString((i+1) * 30 * 60));
    		boolean isIn = false;
    		for (int j : time.available) {
    			if (i == j) {
    				isIn = true;
    			}
    		}
    		if (!isIn) {
    			warn = true;
    		}
    	}
    	for (int i : unselected) {
    		controller.removeWorkingTime(employeeId, Long.toString(date.toEpochDay() * 24 * 60 * 60), Integer.toString(i * 30 * 60), Integer.toString((i+1) * 30 * 60));

    	}
    	
    	if (!warn) {
        	errorMessage.setStyle("-fx-text-fill: GREEN");
        	errorMessage.setText("Working times for this day successfully updated!");
    	} else {
        	errorMessage.setStyle("-fx-text-fill: #CCCC00");
        	errorMessage.setText("Times updated however working times scheduled on unavailable periods");
    	}
    }
    
    public void init(Controller controller) {
    	
    	this.controller = controller;
    	String[] employees = controller.getEmployeeList();
    	if (employees != null) {
        	employeeMenu.getItems().addAll(employees);
    	}
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkingTimesPicker.fxml"));
			timeMenu.getChildren().clear();
			timeMenu.getChildren().add((Node) loader.load());
			
			time = loader.getController();
			time.init(timeMenu);
    		time.setEnabled(false);
    		submit.setDisable(true);
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void update() {
    	if (date != null && employeeId != null) {
    		time.setEnabled(true);
    		
        	Timetable availabilities = controller.getWorkerAvailabilityTimetable(employeeId);
        	Timetable openingHours = controller.getOpeningHours();
        	if (openingHours != null) {
        		availabilities.mergeTimetable(openingHours);
        	}
        	String[][] availableTimes = availabilities.toStringArray();
        	if (availableTimes != null && availableTimes.length > 0) {
        		time.setDefaultAvailability(availableTimes, date);
        		
        	}
        	
        	String[][] shifts = controller.getWorkingTimes(employeeId);
        	if (shifts != null && shifts.length > 0) {
        		time.setDefaultSelected(shifts, date);
        	}
        	
        	submit.setDisable(false);
    	} else {
    		submit.setDisable(true);
    	}
    	errorMessage.setStyle("-fx-text-fill: #F2F2F2");
    }

	public void changeColour(String colour) {
		submit.setStyle("-fx-background-color: " + colour);
	}

}
