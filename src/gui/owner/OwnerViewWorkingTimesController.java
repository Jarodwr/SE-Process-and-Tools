package gui.owner;

import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import java.util.ResourceBundle;

import controller.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import model.employee.Employee;
import model.period.Period;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.TableColumn.CellDataFeatures;

import javafx.util.Callback;

public class OwnerViewWorkingTimesController {
	
	private String[] allEmployees;

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
    	int selectedWeek = week.getSelectionModel().getSelectedIndex(); // Get the index value of the selected week in the comboBox
    	int selectedEmployeeIndex = employeeList.getSelectionModel().getSelectedIndex(); // Get the index value of the selected week in the comboBox
    	String employeeId = "";
    	
    	if (selectedEmployeeIndex != -1 && selectedWeek != -1) {
    		String selectedEmployee = allEmployees[selectedEmployeeIndex];//(String) employeeList.getValue();
        	
    	
    	int idSplit = selectedEmployee.indexOf(":");

    	if (idSplit != -1)
    		employeeId = selectedEmployee.substring(0 , idSplit);
    	
    	String employeeName = selectedEmployee.substring(idSplit+1 ,selectedEmployee.length());

    	if (idSplit != -1) { //Check if everything is fine before continuing
    		insertEmployeeTimes(selectedWeek,employeeId,employeeName); //Set columns dates of the selected week
    	}
    	
    	} else {
    		
    		//Inform the user of what they are missing
    		
    		if (selectedEmployeeIndex == -1) { // Invalid employee selected
        		tableView.setPlaceholder(new Label("Please select an employee.")); //Notify the user of not selecting an employee
        	}
    		
    		if (selectedWeek == -1) { //No week selected
        		tableView.setPlaceholder(new Label("Please select a valid week.")); //Notify the user of not selecting a week
        	}
    		
    		if (selectedWeek == -1 && selectedEmployeeIndex == -1) { // Nothing selected
    			tableView.setPlaceholder(new Label("You need to both select an employee and the week of interest."));
    		}
    		
    		
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
    	
    	allEmployees = Employees;
    	
    	//Default table placeholder
    	tableView.setPlaceholder(new Label("Please select an employee and the week of interest."));
    	
    	//FIXME Remove this before submission. Only for test purposes
    	this.c.addWorkingTime("0", "27/04/2017", "10:00", "11:00");
    	
    	int idPos;
    	
    	if (Employees[0] != null) {
    		for (int i = 0; i < Employees.length; i++) {
    			idPos = Employees[i].indexOf(":"); //Check position of ID and remove it
    			
    			
    			
    			if (idPos != -1) //Make sure we found an id before continuing
    				employeeList.getItems().addAll(Employees[i].substring(idPos+1 ,Employees[i].length())); //Add employee to the drop downlist
    		}
    	}
    	
    	week.getItems().addAll("Past Week");
    	week.getItems().addAll("Current Week");
    	week.getItems().addAll("Next Week");
    	
    	tableView.getColumns().clear(); // Clear the columns of the table
    	
    }
    
    
    public void insertEmployeeTimes(int weekNo, String employeeID,String employeeName) {
    	
    	
        
        tableView.getColumns().clear(); // Clear the columns

        String[][] employeeWorkingTimes = this.c.getWorkingTimes(employeeID);
        
        if (employeeWorkingTimes == null) {
        	tableView.setPlaceholder(new Label(employeeName+" has no registered working times"));
        } else {
        
        
        	String [][] convertedWorkingTimes = getWeekDays(employeeWorkingTimes,this.c.Weekdays,weekNo);
        
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(convertedWorkingTimes));
        data.remove(0);//remove titles from data
        for (int i = 0; i < convertedWorkingTimes[0].length; i++) {
        	TableColumn tc = new TableColumn(convertedWorkingTimes[0][i]);
        	
        	final int colNo = i;
        	tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
        		@Override
        		public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
        			return new SimpleStringProperty((p.getValue()[colNo]));
        			}
        		});
        	tc.setPrefWidth(120);
        	tableView.getColumns().add(tc);
}
        tableView.setItems(data);
        }
        

    }
    
    
    
    
    public String[][] getWeekDays(String[][] contents,String[] Weekdays,int weekNo) {
    	int[] DayPeriodCounts = new int[Weekdays.length]; //Used to figure out the number of rows in the table
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
		
		for (int i=0;i < DayPeriodCounts.length; i++) {
			DayPeriodCounts[i] = 0; //initialize all the days to 0 count of working periods
		}
		String dayTemp;
		Long tempLong;
		Long tempStartOfWeek;
		String ConvertedDay; //Used to store a day name converted from Unix Seconds
		int dayWithMostWorkingHours = 0;
		
		for (int i=0;i < contents.length; i++) { //Find out the maximum number of working hours/period per day
			
					//ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0])/1000  )); //Convert to day from milliseconds to seconds then day
					//Convert to day from milliseconds to seconds then day
					
					dayTemp = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]) - Period.getCurrentWeekBeginning(contents[i][0]))); 
					tempLong = Long.parseLong(contents[i][0]) - Period.getCurrentWeekBeginning(contents[i][0]);
					tempStartOfWeek = Period.getCurrentWeekBeginning(contents[i][0]);
					System.out.println("Subtraction LONG: "+tempLong+" Un-modified Seconds: "+contents[i][0]+" Start of Week: "+tempStartOfWeek+" CONVERTED To Day: "+dayTemp);
					
					ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]))); 
					DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] += 1; //increase the specific day's count of working periods
					
						if (DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] > dayWithMostWorkingHours) //If this day has the highest number of periods,
							dayWithMostWorkingHours = DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)]; //make this maximum number of rows
				
			
		}
		
		
		
		String[][] convertedDates = new String[dayWithMostWorkingHours+1][Weekdays.length]; //Array that will be used to print out the table of the weekly view
		
		//Add weeks
		for (int i = 0; i < Weekdays.length; i++) { 
			if (i != 0) {
    			cl.add(Calendar.DATE, 1);
    		}
    		
    		
    		currentDate = cl.getTime();
			convertedDates[0][i] = Weekdays[i]+" "+weekDate.format(currentDate);
		}
		
		
		
		
		
		for (int i=0;i < contents.length; i++) {
			
			ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0])));
			
			/* Add it to the table under the specific day column with start - end time 24 hr format*/
			for (int j = 1; j < contents[i].length; j++) { 
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
		
		//Menu Title			
				System.out.println("\n--------------------\nEmployee Availability\n--------------------\n");

				
				/*Table header*/
				String tableTitles = "";
				
				for (int i = 0; i < Weekdays.length;i++) {
					if (i == 0)
						tableTitles += "     "+Weekdays[i]; // First table header title
					else
						tableTitles += "   | "+Weekdays[i]; // Second or more element of the table header title
					
					for (int k = 0; k < (12-Weekdays[i].length()); k++)
						tableTitles += " ";
					
				
				}
				System.out.print(tableTitles); //Print all the header titles
				
				//Close header
				System.out.println(); 
				for (int k = 0; k < (tableTitles.length()); k++)
					System.out.print("-"); //Line below the headers
				
				
				System.out.println(); //Create a new line for the rest of the table contents
				
				
						for (int i=1;i < convertedDates.length; i++) { //Go through all the rows
							for (int j = 0; j < convertedDates[i].length; j++) { //Go through all the columns
								
								if (j ==0)
									System.out.print("   ");
								
								System.out.print("  "+convertedDates[i][j]+"   "); //Print out the current detail
								
								/*Add enough spaces to keep table column length balanced*/
								
								if ( convertedDates[i][j] == "")
									for (int k = 0; k < 12; k++)
										System.out.print(" "); //Create a gap between columns
							}
							System.out.println(); //create a new row
						}
						
						for (int k = 0; k < (tableTitles.length()); k++)
							System.out.print("-"); //Add dashes "-" under the table
		
		
		
		return convertedDates;
    
   
    }
}
