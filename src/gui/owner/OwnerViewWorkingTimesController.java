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
	private Boolean isTableEmpty = true;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox employeeList;

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
    		String selectedEmployee = allEmployees[selectedEmployeeIndex];
        	
    	
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
        assert week != null : "fx:id=\"week\" was not injected: check your FXML file 'OwnerViewWorkingTimes.fxml'.";
        

    }
    void initData(Controller c, String[] Employees)
    {
    	
    	this.c = c;
    	
    	allEmployees = Employees;
    	
    	//Default table placeholder
    	tableView.setPlaceholder(new Label("Please select an employee and the week of interest."));
    	
    	//FIXME Remove this before submission. Only for test purposes
    	//this.c.addWorkingTime("0", "01/05/2017", "10:00", "11:00");
    	
    	int idPos;
    	
    	if (Employees != null) { //Check if Employees exist
    	if (Employees[0] != null) {
    		for (int i = 0; i < Employees.length; i++) {
    			idPos = Employees[i].indexOf(":"); //Check position of ID and remove it
    			
    			
    			
    			if (idPos != -1) //Make sure we found an id before continuing
    				employeeList.getItems().addAll(Employees[i].substring(idPos+1 ,Employees[i].length())); //Add employee to the drop downlist
    		}
    	}
    	}
    	
    	Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        Date currentDate; // Will store current date being processed
        SimpleDateFormat weekDate = new SimpleDateFormat("d/M/YYYY"); // Format for the week dates
        int n = cl.get(Calendar.DAY_OF_WEEK) - cl.getFirstDayOfWeek();
        
        /* From Current Week */
        cl.add(Calendar.DATE, -n);
        week.getItems().addAll("From "+weekDate.format(cl.getTime()));
        
        /* From Next Week */
        cl.add(Calendar.DATE, 7);
        week.getItems().addAll("From "+weekDate.format(cl.getTime()));
        
        /* From Week after Next Week */
        cl.add(Calendar.DATE, 7);
        week.getItems().addAll("From "+weekDate.format(cl.getTime()));
    	
    	tableView.getColumns().clear(); // Clear the columns of the table
    	
    }
    
    
    public void insertEmployeeTimes(int weekNo, String employeeID,String employeeName) {
    	
    	
        
        tableView.getColumns().clear(); // Clear the columns

        String[][] employeeWorkingTimes = this.c.getWorkingTimes(employeeID);
        
        if (employeeWorkingTimes == null) {
        	tableView.setPlaceholder(new Label(employeeName+" has no registered working times"));
        } else {
        
        
        	String [][] convertedWorkingTimes = getWeekDays(employeeWorkingTimes,this.c.Weekdays,weekNo);
        
        	if (convertedWorkingTimes != null) {
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
        	} else {
        		tableView.setPlaceholder(new Label(employeeName+" has no registered working times for the selected week"));
        	}
        
        
        }
        

    }
    
    
    public static Long getStartOfWeek(String unixDate) {
    	int tempPeriod;
    	
		tempPeriod = Integer.parseInt(unixDate);
		Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
		
		Date time=new Date((long)tempPeriod*1000);
		cl.setTime(time);
        int p = cl.get(Calendar.DAY_OF_WEEK) - cl.getFirstDayOfWeek();
        cl.add(Calendar.DATE, -p);
        
        SimpleDateFormat weekDate = new SimpleDateFormat("d/M");
        System.out.println("DATE WE NEED: "+weekDate.format(cl.getTime()));
        
        //Long startDate = (long) cl.getTimeInMillis()/1000;
    	
    	return cl.getTimeInMillis()/1000;
    	
    }
    
    
    public static String getDayFromUnix(String unixDate,String[] weekDays) {
    	int tempPeriod;
    	
		tempPeriod = Integer.parseInt(unixDate);
		Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
		
		Date time=new Date((long)tempPeriod*1000);
		cl.setTime(time);
        int p = cl.get(Calendar.DAY_OF_WEEK);
        
        SimpleDateFormat weekDate = new SimpleDateFormat("d/M");
        System.out.println("D: "+p+"DAY WE NEED: "+weekDays[p-1]);
        System.out.println("DATE WE NEED: "+weekDate.format(cl.getTime()));
        
        //Long startDate = (long) cl.getTimeInMillis()/1000;
    	
    	return weekDays[p-1];
    	
    }
    
    public static String get24HrFromUnix(String unixDate) {
int tempPeriod;
    	
		tempPeriod = Integer.parseInt(unixDate);
		Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
		
		Date time=new Date((long)tempPeriod*1000);
		cl.setTime(time);
		cl.add(Calendar.HOUR, 14);

        
        SimpleDateFormat weekDate = new SimpleDateFormat("HH:mm");
        
        //System.out.println("TIME WE NEED: "+weekDates.format(cl.getTime()));
        
        return weekDate.format(cl.getTime());
    }
    
    
    public Boolean checkWithinSameWeek(String periodUnix,int currentWeek, int currentYear) {
    	
    	int tempPeriod;
    	
		tempPeriod = Integer.parseInt(periodUnix);
		Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
		
		Date time=new Date((long)tempPeriod*1000);
		cl.setTime(time);
    	/*Will use these to check if the date is within the same week*/
        int periodYear = cl.get(cl.YEAR); 
        int periodWeek =cl.get(cl.WEEK_OF_YEAR);
        
        if (periodYear == currentYear && periodWeek == currentWeek) {
        	System.out.println("NO SMPTY");
			//isTableEmpty = false;
        	return true;
        }else{
        	return false;
        }
    }
    
    
    
    public String[][] getWeekDays(String[][] contents,String[] Weekdays,int weekNo) {
    	int[] DayPeriodCounts = new int[Weekdays.length]; //Used to figure out the number of rows in the table
    	Boolean issTableEmpty = true;
    	
    	Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        int n = cl.get(Calendar.DAY_OF_WEEK) - cl.getFirstDayOfWeek();
        
        if (weekNo == 0) //This Week
        	cl.add(Calendar.DATE, -n);
        
        if (weekNo == 1) //Next Week
        	cl.add(Calendar.DATE, -n + 7);
        
        if (weekNo == 2) //Week after next week
        	cl.add(Calendar.DATE, -n + 14);
        
        /*Will use these to check if the date is within the same week*/
        int currentYear = cl.get(cl.YEAR); 
        int currentWeek =cl.get(cl.WEEK_OF_YEAR);
        
        
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

					ConvertedDay = getDayFromUnix( contents[i][0],Weekdays);//Period.convertSecondsToDay((int)(Long.parseLong(startPeriod) - getStartOfWeek(startPeriod)));
					
					//ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]))); 
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
		
		
		
		String tempDay;
		String startPeriod;
		String endPeriod;
		String tempRaw;
		int tempPeriod;
		
		
		
		for (int i=0;i < contents.length; i++) {
			
			/* Add it to the table under the specific day column with start - end time 24 hr format*/
			//Long.parseLong(contents[i][0]) - Period.getCurrentWeekBeginning(contents[i][0]))
			//ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0])));
			tempPeriod = Integer.parseInt( contents[i][0]); //1493942400;//
			startPeriod = Integer.toString(tempPeriod);
			tempRaw = startPeriod;
			
			
			
			ConvertedDay = getDayFromUnix(startPeriod,Weekdays);//Period.convertSecondsToDay((int)(Long.parseLong(startPeriod) - getStartOfWeek(startPeriod)));
			
			System.out.println(contents[i][0]+" : "+startPeriod+" : "+ getStartOfWeek(startPeriod));
			
			System.out.println("Converted Day: "+ConvertedDay);
			
			//tempDay = Long.toString(Long.parseLong(startPeriod) - getStartOfWeek(startPeriod));
			startPeriod = get24HrFromUnix(startPeriod); //Period.get24HrTimeFromWeekTime(tempDay);
			
			//System.out.println("Start Period raw: "+tempRaw+" Start Period tempDay: "+tempDay+" startPeriod: "+startPeriod);
			
			//tempDay = Long.toString(Long.parseLong(contents[i][1]) - Period.getCurrentWeekBeginning(contents[i][1]));
			tempPeriod = Integer.parseInt( contents[i][1]); //1493942400;//
			endPeriod = Integer.toString(tempPeriod);
			
			endPeriod = get24HrFromUnix(endPeriod); //Period.get24HrTimeFromWeekTime(tempDay);
			
			//System.out.println("End Period raw: "+contents[i][1]+" Start Period tempDay: "+startPeriod+" endPeriod: "+endPeriod);
			
			
			
			for (int j = 1; j < contents[i].length; j++) { 
				if (convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] == null && checkWithinSameWeek(contents[i][0],currentWeek,currentYear)) {
					convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] = startPeriod+" - "+endPeriod;
					issTableEmpty = false;
					break;
				}
				

			}
		}
		
		isTableEmpty = true;
		for (int i=0;i < convertedDates.length; i++) {
			for (int j = 0; j < convertedDates[i].length; j++) {
				if (convertedDates[i][j] == null){
					///System.out.println("IS SMPTY");
					convertedDates[i][j] = ""; //Make null table cells empty
			}else {
					//System.out.println("NO SMPTY");
					//isTableEmpty = false;
				}
			
		}
    }
		
						
						if (issTableEmpty == false)
							return convertedDates;
						else
							return null; //Table empty
    
   
    }
}
