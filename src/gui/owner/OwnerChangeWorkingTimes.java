package gui.owner;

import java.io.IOException;

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
    
    private TimePicker time;
    
    private Controller controller;

    @FXML
    void dateSelect(ActionEvent event) {

    }

    @FXML
    void employeeSelect(ActionEvent event) {

    }

    @FXML
    void updateWorkingTime(ActionEvent event) {

    }
    
    public void init(Controller controller) {
    	
    	this.controller = controller;
    	employeeMenu.getItems().addAll(controller.getEmployeeList());
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AvailabilityPicker.fxml"));
			timeMenu.getChildren().clear();
			timeMenu.getChildren().add(loader.load());
			
			time = loader.getController();
			time.init(timeMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void update() {
    	
    }

}
