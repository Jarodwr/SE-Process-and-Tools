package gui.owner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

public class OwnerChangeWorkingTimes {

    @FXML
    private ComboBox<String> employeeMenu;

    @FXML
    private DatePicker dateMenu;

    @FXML
    private Button submit;

    @FXML
    private Pane timeMenu;
    
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
    	
    	for (int i = 0; i < 48; i++)
    		unselected.add(i);
    	
    	for (int i : selected) {
    		unselected.remove(Integer.valueOf(i));
    		controller.addWorkingTime(employeeId, Long.toString(date.toEpochDay() * 24 * 60 * 60), Integer.toString(i * 30 * 60), Integer.toString((i+1) * 30 * 60));
    	}
    	
    	for (int i : unselected) {
    		controller.removeWorkingTime(employeeId, Long.toString(date.toEpochDay() * 24 * 60 * 60), Integer.toString(i * 30 * 60), Integer.toString((i+1) * 30 * 60));
    	}
    }
    
    public void init(Controller controller) {
    	
    	this.controller = controller;
    	employeeMenu.getItems().addAll(controller.getEmployeeList());
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkingTimesPicker.fxml"));
			timeMenu.getChildren().clear();
			timeMenu.getChildren().add(loader.load());
			
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
        	String[][] times = controller.getWorkerAvailability(employeeId);
        	if (times != null && times.length > 0) {
        		time.setDefaultAvailability(times, Long.toString(date.toEpochDay()));
        	}
        	submit.setDisable(false);
    	} else {
    		submit.setDisable(true);
    	}
    }

}
