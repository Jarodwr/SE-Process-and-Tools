package gui.owner;

import java.time.LocalDate;

import controller.Controller;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class OwnerAddBooking {

    @FXML
    private ComboBox<String> Employee;

    @FXML
    private DatePicker Date;

    @FXML
    private ComboBox<String> Customer;

    @FXML
    private GridPane timeGrid;

    @FXML
    private Button AddBookingBtn;

    private Controller controller;
    
    private String customerUsername, employeeId, startTime, services;
    
    private String[] availableStyle = new String[]{"-fx-background-color: #ffffff","-fx-background-color: #eeeeee", "-fx-background-color: #cccccc"};
    private String[] UnavailableStyle = new String[]{"-fx-background-color: #ff0000","-fx-background-color: #cc0000"};
    
    private int selected = 49;

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
    	for (int i = 0; i < 24; i++) {
    		Pane am = new Pane();
    		am.setStyle(availableStyle[0]);
    		addPaneListener(am, i);
    		timeGrid.add(am, i, 0);
    		
    		Pane pm = new Pane();
    		pm.setStyle(availableStyle[0]);
    		addPaneListener(pm, i+24);
    		timeGrid.add(pm, i, 2);
    	}
    }
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				p.setStyle(availableStyle[2]);
				
				Pane newpane = getTimePane(selected);
				System.out.println(newpane);
				if (newpane != null)
					newpane.setStyle(availableStyle[0]);
				
				selected = i;
				System.out.println(i/2);
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if (i != selected)
					p.setStyle(availableStyle[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if (i != selected)
					p.setStyle(availableStyle[0]);
			}
    	});
    }
    
    private Pane getTimePane(int i) {
    	int row, col;
    	if (i < 24) {
    		row = 0;
    		col = i;
    	} else if (i < 48) {
    		row = 2;
    		col = i-24;
    	} else {
    		return null;
    	}
    	for (Node node : timeGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (Pane)node;
            }
        }
        return null;
    }
}
