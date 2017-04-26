package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class OwnerAddBooking {

    @FXML
    private ComboBox<String> employeeMenu;

    @FXML
    private DatePicker dateMenu;

    @FXML
    private ComboBox<String> customerMenu;
   
    @FXML
    private MenuButton serviceMenu;
    
    @FXML
    private GridPane timeGrid;

    @FXML
    private Button AddBookingBtn;

    private Controller controller;
    
    private String customerUsername = null;
    private String employeeId = null;
    
    private ArrayList<String> services = new ArrayList<String>();
    
    private LocalDate date = null;
    
    private String[] availableStyle = new String[]{"-fx-background-color: #ffffff","-fx-background-color: #dddddd", "-fx-background-color: #bbbbbb"};
    private String[] UnavailableStyle = new String[]{"-fx-background-color: #ff0000","-fx-background-color: #dd0000", "-fx-background-color: #bb0000"};
    
    private ArrayList<Integer> selected = new ArrayList<Integer>();
    
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
    	if (employeeId != null && customerUsername != null && date != null)
    		System.out.println(employeeId + "|" + customerUsername + "|" + date.toString() + "|" + services);
    }
    
    @FXML
    void addBooking(ActionEvent event) {
    	int localStart = 0;
    	for (int i : selected)
    		if (i > localStart)
    			localStart = i;

    	long startTime = date.toEpochDay() * 86400000 + localStart * 1800000;
    	
    	controller.addNewBooking(customerUsername, Long.toString(startTime), services.toString(), employeeId);
    	this.update();
    }
    
    public void init(Controller controller) {
    	this.controller = controller;
    	employeeMenu.getItems().addAll(controller.getEmployeeList());
    	customerMenu.getItems().addAll(controller.getCustomerList());
    	
    	
    	//Add all services to list
    	String[] sList = controller.getServicesList();
    	for (String s : sList) {
    		CheckMenuItem cmi = new CheckMenuItem(s);
    		serviceMenu.getItems().add(cmi);
    	}
    	
    	//Time grid init
    	for (int i = 0; i < 24; i++) {
    		Pane am = new Pane();
    		am.setStyle(UnavailableStyle[0]);
    		addPaneListener(am, i);
    		timeGrid.add(am, i, 0);
    		
    		Pane pm = new Pane();
    		pm.setStyle(UnavailableStyle[0]);
    		addPaneListener(pm, i+24);
    		timeGrid.add(pm, i, 2);
    		
    		if (i%2 == 0) {
    			timeGrid.add(new Label(Integer.toString(i/2)), i, 1);
    			timeGrid.add(new Label(Integer.toString((i + 24)/2)), i, 3);
    		}
    	}
    	
    	serviceMenu.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            	for (MenuItem mi : serviceMenu.getItems()) {
            		
            		CheckMenuItem a = (CheckMenuItem) mi;
        			
        			for (int i = 0; i < services.size(); i++)
        				if (services.get(i).equals(new StringTokenizer(a.getText(), ":").nextToken())) {
            				if (!a.isSelected())
            					services.remove(i);
        					return;
        				}
        			
    				if (a.isSelected())
        				services.add(new StringTokenizer(a.getText(), ":").nextToken());
            	}
            	update();
            }
        });
    }
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				p.setStyle(UnavailableStyle[2]);
				
				for (int j : selected)
					getTimePane(j).setStyle(UnavailableStyle[0]);

				selected.removeAll(selected);
				for (int j = i; j <= Math.min(i + duration, 47); j++) {
					selected.add(j);
					getTimePane(j).setStyle(UnavailableStyle[2]);
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				for (int j = i; j < Math.min(i + duration + 1, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(UnavailableStyle[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				for (int j = i; j < Math.min(i + duration + 1, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(UnavailableStyle[0]);
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
