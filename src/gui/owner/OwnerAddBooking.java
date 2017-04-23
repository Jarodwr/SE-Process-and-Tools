package gui.owner;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

public class OwnerAddBooking {

    @FXML
    private ComboBox<?> Employee;

    @FXML
    private DatePicker Date;

    @FXML
    private ComboBox<?> Customer;

    @FXML
    private GridPane Times;

    @FXML
    private Button AddBookingBtn;

    private Controller controller;

    @FXML
    void customerSelect(ActionEvent event) {
    	this.updateTable();
    }

    @FXML
    void dateSelect(ActionEvent event) {
    	this.updateTable();
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	this.updateTable();
    }
    
    private void updateTable() {
    	
    }
    
    @FXML
    void addBooking(ActionEvent event) {

    }
    
    public void init(Controller controller) {
    	this.controller = controller;
//    	String[] employeeList = controller.getEmployeeList();
//    	String[] customerList = controller.getCustomerList();
    }

}
