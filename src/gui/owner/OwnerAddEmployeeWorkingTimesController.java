package gui.owner;

import java.io.IOException;
import java.util.ArrayList;

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

	private ArrayList<String> mondayTimes = new ArrayList<String>();
	private ArrayList<String> tuesdayTimes = new ArrayList<String>();
	private ArrayList<String> wednesdayTimes = new ArrayList<String>();
	private ArrayList<String> thursdayTimes = new ArrayList<String>();
	private ArrayList<String> fridayTimes = new ArrayList<String>();
	private ArrayList<String> saturdayTimes = new ArrayList<String>();
	private ArrayList<String> sundayTimes = new ArrayList<String>();

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
    
    void init(Controller c) {
    	this.c = c;
    	
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
    	System.out.println(time.saveTimes().toString());
    }

    @FXML
    void updateAvailability(MouseDragEvent event) {

    }

    @FXML
    void updateCurrentAvailability(InputMethodEvent event) {

    }

}
