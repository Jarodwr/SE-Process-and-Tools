package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
    private MenuButton Services;
    
    @FXML
    private GridPane timeGrid;

    @FXML
    private Button AddBookingBtn;

    private Controller controller;
    
    private String customerUsername, employeeId, startTime, services;
    
    private String[] availableStyle = new String[]{"-fx-background-color: #ffffff","-fx-background-color: #eeeeee", "-fx-background-color: #cccccc"};
    private String[] UnavailableStyle = new String[]{"-fx-background-color: #ff0000","-fx-background-color: #cc0000"};
    
    private ArrayList<Integer> selected = new ArrayList<Integer>();
    private int duration = 3;	//hardcoded rn

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
    
    @FXML
    void servicesSelect(ActionEvent event) {

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
    		
    		if (i%2 == 0) {
    			timeGrid.add(new Label(Integer.toString(i/2)), i, 1);
    			timeGrid.add(new Label(Integer.toString((i + 24)/2)), i, 3);
    		}
    	}
    }
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				p.setStyle(availableStyle[2]);
				
				for (int j : selected)
					getTimePane(j).setStyle(availableStyle[0]);

				selected.removeAll(selected);
				for (int j = i; j <= Math.min(i + duration, 47); j++) {
					selected.add(j);
					getTimePane(j).setStyle(availableStyle[2]);
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				for (int j = i; j < Math.min(i + duration + 1, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(availableStyle[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				for (int j = i; j < Math.min(i + duration + 1, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(availableStyle[0]);
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

    	for (Node node : timeGrid.getChildren())
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row)
                return (Pane)node;

        return null;
    }
}
