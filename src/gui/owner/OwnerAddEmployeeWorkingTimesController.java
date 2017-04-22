package gui.owner;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.employee.Employee;

public class OwnerAddEmployeeWorkingTimesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> employeeList;

    @FXML
    private ComboBox<?> monStart;

    @FXML
    private ComboBox<?> monEnd;

    @FXML
    private ComboBox<?> tueStart;

    @FXML
    private ComboBox<?> tueEnd;

    @FXML
    private ComboBox<?> wedStart;

    @FXML
    private ComboBox<?> wedEnd;

    @FXML
    private ComboBox<?> thurStart;

    @FXML
    private ComboBox<?> thurEnd;

    @FXML
    private ComboBox<?> friStart;

    @FXML
    private ComboBox<?> friEnd;

    @FXML
    private ComboBox<?> satStart;

    @FXML
    private ComboBox<?> satEnd;

    @FXML
    private ComboBox<?> sunStart;

    @FXML
    private ComboBox<?> sunEnd;

    @FXML
    private Button updateAvailabilityButton;

    private Controller c;
    private Employee [] employees;
    
    @FXML
    void updateCurrentAvailability(ActionEvent event){
    	
    }
    
    @FXML
    void updateAvailability(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert employeeList != null : "fx:id=\"employeeList\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert monStart != null : "fx:id=\"monStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert monEnd != null : "fx:id=\"monEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert tueStart != null : "fx:id=\"tueStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert tueEnd != null : "fx:id=\"tueEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert wedStart != null : "fx:id=\"wedStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert wedEnd != null : "fx:id=\"wedEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert thurStart != null : "fx:id=\"thurStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert thurEnd != null : "fx:id=\"thurEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert friStart != null : "fx:id=\"friStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert friEnd != null : "fx:id=\"friEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert satStart != null : "fx:id=\"satStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert satEnd != null : "fx:id=\"satEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert sunStart != null : "fx:id=\"sunStart\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert sunEnd != null : "fx:id=\"sunEnd\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";
        assert updateAvailabilityButton != null : "fx:id=\"updateAvailabilityButton\" was not injected: check your FXML file 'OwnerAddEmployeeWorkingTimes.fxml'.";

    }
    
    void initData(Controller c, Employee [] e)
    {
    	this.c = c;
    	employees = e;
    }
}
