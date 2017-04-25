package gui.owner;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import model.employee.Employee;
import model.period.Period;

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
    		insertEmployeeTimes(output); //Set columns dates of the selected week
    		
    		String[][] employeeWorkingTimes = this.c.getWorkingTimes("1");
    		
    		//String[] temp = {"1", "2", "T", "Wednesday", "Thursday", "Friday", "Saturday"};

    		
    		
    		
    		/*
    		for (int i = tableView.getColumns().size(); i < values.length; i++) {
                TableColumn<List<String>, String> col = new TableColumn<>("Column "+(i+1));
                col.setMinWidth(80);
                final int colIndex = i ;
                col.setCellValueFactory(data -> {
                    List<String> rowValues = data.getValue();
                    String cellValue ;
                    if (colIndex < rowValues.size()) {
                        cellValue = rowValues.get(colIndex);
                    } else {
                         cellValue = "" ;
                    }
                    return new ReadOnlyStringWrapper(cellValue);
                });
                table.getColumns().add(col);
            }

            // add row:
            table.getItems().add(Arrays.asList(values));*/
    		
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
    	
    	//ONLY do this once
    	this.c.addWorkingTime("0", "26/04/2017", "10:00", "11:00");
    	
    	if (Employees[0] != null) {
    		for (int i = 0; i < Employees.length; i++)
    			employeeList.getItems().addAll(Employees[i]);
    	}
    	
    	week.getItems().addAll("Past Week");
    	week.getItems().addAll("Current Week");
    	week.getItems().addAll("Next Week");
    	
    	tableView.getColumns().clear(); // Clear the columns of the table
    	
    }
    
    
    public void insertEmployeeTimes(int weekNo) {
    	
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
        SimpleDateFormat weekDate = new SimpleDateFormat("d/M"); // Format for the week dates
        
        tableView.getColumns().clear(); // Clear the columns
    	
        List<String> stringValues = Arrays.asList("One", "Two", "Three", "Four", "Five");
        
        for (int i = 0; i < stringValues.size(); i++) {
            tableView.getItems().add(i);
        }
        
        
        
        String[][] employeeWorkingTimes = this.c.getWorkingTimes("0");
        String currentTime;
        
        String [][] convertedWorkingTimes = getWeekDays(employeeWorkingTimes,this.c.Weekdays);
        
        if (employeeWorkingTimes != null)
        	System.out.println(employeeWorkingTimes[0][0]);
    	
    	for (int i = 0; i <  convertedWorkingTimes.length; i++) { //this.c.Weekdays.length
    		
    		
    		
    		if (i != 0) {
    			cl.add(Calendar.DATE, 1);
    		}
    		
    		
    		currentDate = cl.getTime();
    		
    		
    		
    		for (int j = 0; j < convertedWorkingTimes[i].length; j++) {
    			
    			//employeeWorkingTimes[i].length
    		
    		
    			
    			System.out.println("TEST: "+convertedWorkingTimes[i].length);
    			

    			System.out.println("TEST: "+convertedWorkingTimes[0][j]);
    			
    			TableColumn<Integer, String> TableColumn = new TableColumn<>(this.c.Weekdays[j]+" "+weekDate.format(currentDate));
    			
    		TableColumn.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(convertedWorkingTimes[0][rowIndex]); //stringValues.get(rowIndex)
            });
    		
    		tableView.getColumns().add(TableColumn);
    	}
    		//formatDateToTime(employeeWorkingTimes[0][0])
            
            
    		//}
    		//tempColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14));
            //TableColumn.setMinWidth(80); // Might use this later
    	}
    	
    	
    	
    	
    	
    	
    	
    }
    
    public static String formatDateToTime(String unixdate) {
		Long date = Long.parseLong(unixdate) * 1000;
		SimpleDateFormat viewing = new SimpleDateFormat("h:mm");
		
		return viewing.format(date);
}
    
    
    public String[][] getWeekDays(String[][] contents,String[] Weekdays) {
int[] DayPeriodCounts = new int[Weekdays.length]; //Used to figure out the number of rows in the table
		
		for (int i=0;i < DayPeriodCounts.length; i++) {
			DayPeriodCounts[i] = 0; //initialize all the days to 0 count of working periods
		}
		
		String ConvertedDay; //Used to store a day name converted from Unix Seconds
		int dayWithMostWorkingHours = 0;
		
		for (int i=0;i < contents.length; i++) { //Find out the maximum number of working hours/period per day
			
					ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]) /1000)); //Convert to day from milliseconds to seconds then day
					ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]))); //Convert to day from milliseconds to seconds then day
					DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] += 1; //increase the specific day's count of working periods
					
						if (DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] > dayWithMostWorkingHours) //If this day has the highest number of periods,
							dayWithMostWorkingHours = DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)]; //make this maximum number of rows
				
			
		}
		
		
		
		String[][] convertedDates = new String[dayWithMostWorkingHours][Weekdays.length]; //Array that will be used to print out the table of the weekly view
		
		for (int i=0;i < contents.length; i++) {
			
			ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0])));
			
			/* Add it to the table under the specific day column with start - end time 24 hr format*/
			for (int j = 0; j < contents[i].length; j++) { 
				if (convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] == null) {
					convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] = Period.get24HrTimeFromWeekTime(contents[i][0])+" - "+Period.get24HrTimeFromWeekTime(contents[i][1]);
					break;
				}
				

			}
		}
		
		for (int i=0;i < convertedDates.length; i++) {
			for (int j = 0; j < convertedDates[i].length; j++) {
				if (convertedDates[i][j] == null)
					convertedDates[i][j] = ""; //Make null table cells empty
			
		}
    }
		
		
		
		return convertedDates;
    
   
    }
}
