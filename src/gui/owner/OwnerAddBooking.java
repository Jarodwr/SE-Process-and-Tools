package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private String[] unavailableStyle = new String[]{"-fx-background-color: #ff0000","-fx-background-color: #dd0000", "-fx-background-color: #bb0000"};
    
    private ArrayList<Integer> selected = new ArrayList<Integer>();
    private ArrayList<Integer> available = new ArrayList<Integer>();
    
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
        	
        	long dayInMillis = 86400000;
        	long periodInMillis = 1800000;
        	
        	String[][] times = controller.getEmployeeBookingAvailability(employeeId, new Date(date.toEpochDay() * dayInMillis));
        	if (times != null && times.length > 0) {
        		
            	for (int i : available)	//Reset all available panes to unavailable panes
            		getTimePane(i).setStyle(unavailableStyle[0]);
            	available.removeAll(available);	//Wipe info from panes

            	
            	for (long startTime = date.toEpochDay() * dayInMillis; startTime < (date.toEpochDay()+1) * dayInMillis; startTime += periodInMillis) {
        			Long endTime = startTime + periodInMillis;
        			
        			boolean success = true;
            		for (String[] p : times) {
            			if (Long.parseLong(p[0]) < startTime && Long.parseLong(p[1]) > startTime
            					|| Long.parseLong(p[0]) < endTime && Long.parseLong(p[0]) > endTime) {
            				success = false;
            				break;
            			}
            		}
    				if (success)
    					available.add((int)((startTime - date.toEpochDay() * dayInMillis)/periodInMillis));
            	}
            	
            	for (int i : available)	//Paint available panes white
            		getTimePane(i).setStyle(availableStyle[0]);;
            	
        	}

    	}
    	
    	if (employeeId != null && customerUsername != null && date != null && services.size() != 0)
    		AddBookingBtn.setDisable(false);
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
    	
    	AddBookingBtn.setDisable(true);
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
    		am.setStyle(unavailableStyle[0]);
    		addPaneListener(am, i);
    		timeGrid.add(am, i, 0);
    		
    		Pane pm = new Pane();
    		pm.setStyle(unavailableStyle[0]);
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
            	
            	duration = 0;
            	
            	for (MenuItem mi : serviceMenu.getItems()) {
            		CheckMenuItem a = (CheckMenuItem) mi;
        			
            		StringTokenizer tk = new StringTokenizer(a.getText(), ":");
            		String name = tk.nextToken();
            		String durationStr = tk.nextToken();
            		
            		duration += Integer.parseInt(durationStr);
            		
            		if (a.isSelected())
            			
            		
        			for (int i = 0; i < services.size(); i++)
        				if (services.get(i).equals(name)) {
            				if (!a.isSelected())
            					services.remove(i);
        					return;
        				}
        			
    				if (a.isSelected())
        				services.add(name);
            	}
            	update();
            }
        });
    }
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				p.setStyle(style[2]);
				
				for (int j : selected) {
					getTimePane(j).setStyle(style[0]);
				}

				selected.removeAll(selected);
				for (int j = i; j < Math.min(i + duration, 48); j++) {
					selected.add(j);
					getTimePane(j).setStyle(style[2]);
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[0]);
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
    
    private String[] getAppropriateStyle(int i) {
    	for (int a : available)
    		if (a == i)
    			return availableStyle;
    	return unavailableStyle;
    }
}
