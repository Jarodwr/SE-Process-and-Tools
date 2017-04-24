package gui.owner;

import java.time.LocalDate;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

public class OwnerAddBooking {

    @FXML
    private ComboBox<String> Employee;

    @FXML
    private DatePicker Date;

    @FXML
    private ComboBox<String> Customer;

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
    	String employee = Employee.getSelectionModel().getSelectedItem();
    	String customer = Customer.getSelectionModel().getSelectedItem();
    	LocalDate date = Date.getValue();
    	if (employee != null && customer != null && date != null)
    		System.out.println(Employee.getSelectionModel().getSelectedItem() + "|" + Customer.getSelectionModel().getSelectedItem() + "|" + Date.getValue());
    }
    
    @FXML
    void addBooking(ActionEvent event) {
//    	controller.makeBooking(startTime, startTime + services.duration, customerUsername, employeeId, services);
    	this.updateTable();
    }
    
    public void init(Controller controller) {
    	this.controller = controller;
    	Employee.getItems().addAll(controller.getEmployeeList());
    	Customer.getItems().addAll(controller.getCustomerList());
    }

}
