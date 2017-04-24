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
    
    private String customerUsername, employeeId, startTime, services;

    @FXML
    void customerSelect(ActionEvent event) {
    	this.update();
    }

    @FXML
    void dateSelect(ActionEvent event) {
    	this.update();
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	this.update();
    }
    
    private void update() {
    	String employee = Employee.getSelectionModel().getSelectedItem();
    	String customer = Customer.getSelectionModel().getSelectedItem();
    	LocalDate date = Date.getValue();
    	if (employee != null && customer != null && date != null)
    		System.out.println(Employee.getSelectionModel().getSelectedItem() + "|" + Customer.getSelectionModel().getSelectedItem() + "|" + Date.getValue());
    }
    
    @FXML
    void addBooking(ActionEvent event) {
//    	controller.addNewBooking();
    	this.update();
    }
    
    public void init(Controller controller) {
    	this.controller = controller;
    	Employee.getItems().addAll(controller.getEmployeeList());
    	Customer.getItems().addAll(controller.getCustomerList());
    }

}
