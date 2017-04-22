package gui.owner;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import model.employee.Employee;

public class OwnerViewWorkingTimesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> employeeList;

    @FXML
    private Button generateTimetableButton;

    @FXML
    private ComboBox<?> week;
    
    

    @FXML
    void generateTimetable(ActionEvent event) {

    }

    @FXML
    void updateAvailability(MouseDragEvent event) {

    }

    @FXML
    void updateCurrentAvailability(InputMethodEvent event) {

    }
    
    private Controller c;
    private Employee [] employees;

    @FXML
    void initialize() {
        assert employeeList != null : "fx:id=\"employeeList\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        assert generateTimetableButton != null : "fx:id=\"generateTimetableButton\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        assert week != null : "fx:id=\"week\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";

    }
    
    void initData(Controller c, Employee [] e)
    {
    	this.c = c;
    	employees = e;
    }
}
