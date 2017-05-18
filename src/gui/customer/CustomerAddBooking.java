package gui.customer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import model.users.Customer;

public class CustomerAddBooking {

    @FXML
    private ComboBox<String> employeeMenu;

    @FXML
    private DatePicker dateMenu;

    @FXML
    private ComboBox<String> customerMenu;
   
    @FXML
    private MenuButton serviceMenu;

    @FXML
    private Button AddBookingBtn;
    
    @FXML
    private Pane timeMenu;
    private TimePicker time;

    private Controller controller;
    
    private String customerUsername = null;
    private String employeeId = null;
    
    private ArrayList<String> services = new ArrayList<String>();
    
    private LocalDate date = null;
    
    private int duration = 1;

    @FXML
    void customerSelect(ActionEvent event) {
    	customerUsername = new StringTokenizer(customerMenu.getSelectionModel().getSelectedItem(), ":").nextToken();
    	this.update();
    }

    @FXML
    void dateSelect(ActionEvent event) {
    	date = dateMenu.getValue();
    	this.update();
    }

    @FXML
    void employeeSelect(ActionEvent event) {
    	employeeId = new StringTokenizer(employeeMenu.getSelectionModel().getSelectedItem(), ":").nextToken();
    	this.update();
    }
    
    private void update() {
    	//Update gridtable with availabilities
    	
    	if (date != null && employeeId != null) {
    		long dayInSecs = 86400;
        	String[][] times = controller.getEmployeeBookingAvailability(employeeId, new Date(date.toEpochDay() * dayInSecs));
        	if (times != null && times.length > 0)
        		time.setAvailability(times, date);

    	}
    	
    	if (employeeId != null && customerUsername != null && date != null && services.size() != 0)
    		AddBookingBtn.setDisable(false);
    	else
    		AddBookingBtn.setDisable(true);

    }
    
    @FXML
    void addBooking(ActionEvent event) {
    	int localStart = 48;
    	for (int i : time.getSelectedPeriods())
    		if (i < localStart)
    			localStart = i;
    	if (localStart == 48) {
    		localStart = 0;
    	}

    	long startTime = (date.toEpochDay() * 86400 + localStart * 1800);
    	
    	String listOfServices = "";
    	for (int i = 0; i < services.size(); i++) {
    		listOfServices += services.get(i);
    		if (i != services.size()-1) {
    			listOfServices += ":";
    		}
    	}
    	
    	if (controller.addNewBooking(customerUsername, Long.toString(startTime), listOfServices, employeeId)) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Add booking");
    		alert.setHeaderText("Booking successfully added!");
    		alert.setContentText("press ok to continue...");

    		alert.showAndWait();
    	} else {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Add booking");
    		if (time.validPeriod()) {
    			alert.setHeaderText("invalid period");
    		} else {
    			alert.setHeaderText("failed to add booking");
    		}
    		
    		alert.setContentText("press ok to continue...");
    		alert.showAndWait();
    	}
    	this.update();
    }
    
    public void init(Controller controller, Customer customer) {
    	this.controller = controller;
    	this.customerUsername = customer.getUsername();
    	
    	AddBookingBtn.setDisable(true);
    	String[] employees = controller.getEmployeeList();
    	if (employees != null) {
        	employeeMenu.getItems().addAll(employees);
    	}
    	
    	
    	//Add all services to list
    	String[] sList = controller.getServicesList();
    	if (sList != null) {
        	for (String s : sList) {
        		CheckMenuItem cmi = new CheckMenuItem(s);
        		serviceMenu.getItems().add(cmi);
        	}
    	}
    	
    	//Alternate time grid init
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TimePicker.fxml"));
			System.out.println(timeMenu);
			timeMenu.getChildren().clear();
			timeMenu.getChildren().add(loader.load());
			
			time = loader.getController();
			time.init(timeMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	serviceMenu.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            	
            	time.deselectAll();
            	
            	duration = 0;
        		
        		services.removeAll(services);
        		
            	for (MenuItem mi : serviceMenu.getItems()) {
            		CheckMenuItem a = (CheckMenuItem) mi;
        			
            		StringTokenizer tk = new StringTokenizer(a.getText(), ":");
            		String name = tk.nextToken();
            		String durationStr = tk.nextToken();

            		
    				if (a.isSelected()) {
    					services.add(name);
                		duration += Integer.parseInt(durationStr)/30;
    				}

            	}
            	if (duration == 0)
            		duration = 1;

            	time.setDuration(duration);
            	update();
            }
        });
    }
}
