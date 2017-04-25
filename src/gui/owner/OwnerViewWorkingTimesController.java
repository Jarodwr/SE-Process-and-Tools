package gui.owner;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import model.employee.Employee;

public class OwnerViewWorkingTimesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox employeeList;

    @FXML
    private Button generateTimetableButton;

    @FXML
    private ComboBox week;
    
    @FXML
    private TableView tableView;
    
    

    @FXML
    void generateTimetable(ActionEvent event) {
    	int output = week.getSelectionModel().getSelectedIndex();
    	
    	if (output != -1) {
    		setWeek(output); //Set columns dates of the selected week
    		
    		String[][] employeeWorkingTimes = this.c.getWorkingTimes("1");
    	}
    }

    @FXML
    void updateAvailability(MouseDragEvent event) {

    }

    @FXML
    void updateCurrentAvailability(InputMethodEvent event) {

    }
    
    private Controller c;
    private Employee [] employees;
    
    public void init(Controller c) {
		this.c = c;
	}

    @FXML
    void initialize() {
        assert employeeList != null : "fx:id=\"employeeList\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        assert generateTimetableButton != null : "fx:id=\"generateTimetableButton\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        assert week != null : "fx:id=\"week\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        

    }
    void initData(Controller c, String[] Employees)
    {
    	this.c = c;
    	
    	if (Employees[0] != null) {
    		for (int i = 0; i < Employees.length; i++)
    			employeeList.getItems().addAll(Employees[i]);
    	}
    	
    	week.getItems().addAll("Past Week");
    	week.getItems().addAll("Current Week");
    	week.getItems().addAll("Next Week");
    	
    	tableView.getColumns().clear(); // Clear the columns of the table
    	
    }
    
    
    public void setWeek(int weekNo) {
    	
    	Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        int n = cl.get(Calendar.DAY_OF_WEEK) - cl.getFirstDayOfWeek();
        
        if (weekNo == 0) //Last Week
        	cl.add(Calendar.DATE, -n - 7);
        
        if (weekNo == 1) //This Week
        	cl.add(Calendar.DATE, -n);
        
        if (weekNo == 2) //Next Week
        	cl.add(Calendar.DATE, -n + 7);
        
        
        Date currentDate; // Will store current date being processed
        TableColumn tempColumn; //Strs current column being processed
        SimpleDateFormat weekDate = new SimpleDateFormat("d/M"); // Format for the week dates
        
        tableView.getColumns().clear(); // Clear the columns
    	
        
    	
    	for (int i = 0; i < this.c.Weekdays.length; i++) {
    		
    		if (i != 0) {
    			cl.add(Calendar.DATE, 1);
    		}
    		
    		currentDate = cl.getTime();
    		
    		tempColumn = new TableColumn(this.c.Weekdays[i]+" "+weekDate.format(currentDate));
    		tableView.getColumns().addAll(tempColumn);
    		tempColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14));
    	}
    }
    

    
    
}
