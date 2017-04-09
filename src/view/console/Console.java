package view.console;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;

import model.period.Period;

public class Console {
	private static Scanner scanner = new Scanner(System.in);
	private static java.io.Console console = System.console();
	public static final String[] Weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	
	public Console() {

	}
	
    /**
     * Displays all options based on the active user in userManager.
     * If there is no active user then default to only displaying 
     * 'Register' and 'Login'.
     * @param permissions Current format of permissions is as follows: <br>
	 * 0 - Register<br>
	 * 1 - Login<br>
	 * 2 - View current bookings<br>
	 * 3 - View available times<br>
	 * 4 - Add new booking<br>
	 * 5 - View summary of bookings<br>
	 * 6 - Add working time/date<br>
	 * 7 - View working time/date<br>
	 * 8 - Show all worker availability for next 7 days<br>
	 * 9 - Add an employee<br>
	 * 10 - Edit Availabilities<br>
	 * 11 - Log out<br>
	 * @return option number
     */
	public int displayOptions(boolean[] permissions) {
		int userSelection;
		int optionNum = 0;
		String [] option = {"Log In", "Register", "View Current Bookings", 
		                     "View Available Times", "Add New Booking", "View Summary of Bookings", 
		                     "Add working time/date", "View working time/dates", "Show all worker availability", 
		                     "Add an Employee", "Edit Employee Availabilities", "Log out"};
		
		
		while (true) {
			System.out.println("--------------------------\nAppointment Booking System\n--------------------------");
			System.out.println("Please enter the identification number of your choice from the options below: ");
			optionNum = 0;
			for(int i = 0; i < permissions.length; i++) //go through permissions and print each of the available options
			{
				if(permissions[i])
				{
					optionNum++;
					System.out.println(optionNum + ".\t" + option[i]);
				}
			}

			if(permissions[11] == false) //if you can't log out as you are not logged in, there is a break
			{
				System.out.println("12.\tExit"); // added by Spencer - exit case
			}
			System.out.print("Enter your option: ");
			
			try {
				int selectedOption = Integer.parseInt(scanner.nextLine());
				int optionval = 0;
				boolean match = false;
				for(int i = 0; i < permissions.length; i++) //check which option they have selected
				{
					if(permissions[i])
					{
						optionval++;
						if(optionval == selectedOption)
						{
							match = true;
							selectedOption = i;
							break;
						}
					}
				}
				if(!match) //if there is no match, and you can't log out and they haven't selected logout continue through loop
				{
					if(!permissions[11] && selectedOption != 11)
					{
						selectedOption = 12;
					}
					
				}
				userSelection = selectedOption;
			} catch (NumberFormatException e) {
				System.out.println("Sorry you have provided an invalid option! Please try again:");
				continue;
			}
			if (userSelection > -1 && userSelection < 13) // changed to 11 by Spencer - I'll include invalid option checking in my contrller main aswell
				break;
			else
				System.out.println("Sorry you have provided an invalid option! Please try again:");
		}
		return userSelection;
	}
	
	/**
	 * Displays initial register screen where customer details are added
	 * such as username, password, name, address, phone number.
	 * @return [0] username, [1] password, [2] name, [3] address, [4] phoneNumber
	 */
	public String[] register() 
	{
		
		System.out.println("--------------------------\nRegister\n--------------------------");
		
		//create variables to store user inputed values for
		String username;
		String password;
		String name;
		String address;
		String phoneNo;		
		
		/**In order to avoid wring characters from being inserted, we have implemented a limit of allowable characters
		 * with the help from source: http://stackoverflow.com/questions/29761008/java-character-input-validation
		 * 
		 */
		
		//prompt user for each value and store their input as separate variables
		System.out.print("Enter your Username: ");
		username = scanner.nextLine();
		
		System.out.print("Enter your Password: ");
		password = scanner.nextLine();
		
		System.out.print("Enter your Name: ");
		name = scanner.nextLine();
		
		System.out.print("Enter your Address: ");
		address = scanner.nextLine();
		
		System.out.print("Enter your Moblie Phone Number: ");
		phoneNo = scanner.nextLine();
			
		//return the user inputed strings to the controller class
		return new String[] {username, password, name, address, phoneNo};
	}

	
	/**
	 * Displays login screen where user enters their username and password
	 * @return [0] username, [1] password
	 */
	public String[] login() 
	{
		//create variable to store user input
		String username;
		String password;

		System.out.println("--------------------------\nLogin\n--------------------------");
		
		//prompt, get user input and store user input for each variable
		System.out.print("Enter your username: ");
		username = scanner.nextLine();
		
		System.out.print("Enter your password: ");
		password = scanner.nextLine();
		//char[] passString = console.readPassword();
	    //password = new String(passString );

	
		//return the inputed username and password to the controller class for validation
		return new String[] {username, password};
	}
	
	/**
	 * Confirmation screen for logging out
	 * @return success
	 */
	public boolean logout() {
		int userSelection; //Will be used to store the option selected by the user
		
		System.out.println("--------------------------\nLogout\n--------------------------");
		/*Print out the prompts in the console*/
		System.out.println("Are you sure you want to logout?");
		System.out.println("1.\tYes");
		System.out.println("2.\tCancel");
		
		while (true) { //Do not leave this Menu until a valid option is selected
		try {
			int selectedOption = Integer.parseInt(scanner.nextLine());
			userSelection = selectedOption;
			
		} catch (NumberFormatException e) { //Catch an invalid entry that is not an integer
			System.out.println("Sorry you have provided an invalid option! Please try again:");
			continue;
		}
		
		
		if (userSelection > -1 && userSelection < 3) { //Make sure the entry option is within valid parameters
			if (userSelection == 1)
				return true; //Yes option selected
			else
				return false; //Cancel Option selected
		} else {
			//The number entered is not within the given boundaries
			System.out.println("Sorry you have provided an invalid option! Please try again:");
			}	
		}
	}
	
	
	
	/**
	 * Check if the given booking is within 7 days
	 * @param bookingTime String formatted unixtime
	 * @return success
	 */
	
	public boolean checkBookingWithinWeek(String bookingTime) {
		
		/* Current Date and Time */
		Date date = new Date();
		/* Given Date and Time */
		Calendar c1 = Period.unixToCalendar(Long.parseLong(bookingTime));
		
		/*Compare between the dates*/
		  Calendar c2=Calendar.getInstance();
		  c2.setTime(date);
		  c2.add(Calendar.DATE,7); //Add 7 days to the current date
		  if(c2.getTime().compareTo(c1.getTime())<0){
			  return false; //It's more than seven days.
			  
		  }
		  else
		  {
			  return true; //It's less than 7 days.
		  }
		
	}
	
	/**
	 * Converts date to a readable format
	 * @param unixdate In string format
	 * @return formattedDate
	 */
	
	public static String formatDate(String unixdate) {
			Long date = Long.parseLong(unixdate) * 1000;
			SimpleDateFormat viewing = new SimpleDateFormat("h:mm a d:M:yy");
			
			return viewing.format(date);
	}
	
	
	/**
	 * prints a table
	 * @param contents	Table content
	 * @param headerTitles	Titles of each table column
	 * @param title	Title of table
	 * @param checkSevenDayLimit	Limit to 7 days
	 * @param disableTimeConversion	Convert to weektime
	 * @param noContentsMessage	?
	 */
	public void printTable(String[][] contents,String[] headerTitles,String title, Boolean checkSevenDayLimit,Boolean disableTimeConversion,String noContentsMessage) {
		
		int[] longestElements = new int[headerTitles.length+1]; //Storage to determine column length
		
		for (int i = 0; i < longestElements.length; i++)
			longestElements[i] = 0; // Set the minimum length to compare later
		
		Boolean canPrintSomething = false;
		if (checkSevenDayLimit) { //Do we need to check for the 7 day limit?
			canPrintSomething = false; // Check if there is something within 7 days
		} else {
			canPrintSomething = true;
		}
		/*
		 * In order to make sure the table is printed properly,
		 * we need need to make sure that the cells are balanced in length
		 * So we need to first fine the longest Strings for each column.
		 */
		for (int i=0;i < contents.length; i++) {
			for (int j = 0; j < contents[i].length; j++) {
				
				//Check if the length of the contents within this column is currently the longest
				if ((j == 0 || j == 1) && !disableTimeConversion && formatDate(contents[i][j]).length() > longestElements[j])
					longestElements[j] = formatDate(contents[i][j]).length();
				
				//Check if atleast this date is within seven days
				if (j == 0 && checkSevenDayLimit) {
					if (checkBookingWithinWeek(contents[i][j])) //Check if we need to bother print the table
						canPrintSomething = true; //Yes we can print the table
				}
			}
		}
		
					
		//Menu Title			
		System.out.println("\n--------------------\n"+title+"\n--------------------\n");

		if (canPrintSomething) { // If there is any bookings within the next 7 days
		/*Table Header*/
			String tableTitles = "";
			
			
		for (int i = 0; i < headerTitles.length;i++) {
			if (i == 0)
				tableTitles += "     "+headerTitles[i]; // First table header title
			else
				tableTitles += "   | "+headerTitles[i]; // Second or more element of the table header title
			
			if (headerTitles[i].length() > longestElements[i]) {
				longestElements[i] = headerTitles[i].length();
		for (int k = 0; k < (headerTitles[i].length() - longestElements[i]); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
			} else {
				for (int k = 0; k < (longestElements[i] - headerTitles[i].length()); k++)
					tableTitles += " "; //Add Spaces to balance the table column length
			}
		
		}
		
		
		System.out.println(tableTitles); // Print out the table header titles
		
		Boolean continuePrinting = false;
		String currentElement;
		
		for (int k = 0; k < (tableTitles.length()); k++)
			System.out.print("-"); //Add dashes "-" under the table header
		
		System.out.println(); //Create a new line for the rest of the table contents
		
		
				for (int i=0;i < contents.length; i++) { //Go through all the bookings rows
					
					if (checkSevenDayLimit) {
					if (checkBookingWithinWeek(contents[i][0])) { //Check if this is within 7 days
						continuePrinting = true;
					}
					} else {
						continuePrinting = true;
					}
					
					if (continuePrinting) {
					for (int j = 0; j < contents[i].length; j++) { //Go through all the bookings columns
						
						if ((j == 0 || j == 1) && !disableTimeConversion) {
							currentElement = formatDate(contents[i][j]);
								System.out.print("     "+formatDate(contents[i][j])); //Print out dates in correct format
						} else {
							currentElement = contents[i][j];
							System.out.print("     "+contents[i][j]); //Print out customer name
						}
						
						
						if (currentElement.length() == 0)
							for (int k = 0; k < (longestElements[j]); k++)
								System.out.print("");
						
						
						if (currentElement.length() != longestElements[j]) {
							
							for (int k = 0; k < (longestElements[j] - currentElement.length()); k++)
								System.out.print(" ");
						}

					}
					System.out.println(); //create a new row
					}
				}
				
				for (int k = 0; k < (tableTitles.length()); k++)
					System.out.print("-"); //Add dashes "-" under the table
		} else {
			System.out.println(noContentsMessage); // Print Message when nothing can be shown from the table
		}
				
				
	}
	
	
	
	/**
	 * Prints all bookings to screen
	 * @param bookings [booking][0 - customerUsername, 1 - startPeriod, 2 - endPeriod, 3 - services]
	 */
	public void viewBookings(String[][] bookings) {
		String[] headerTitles = {"Start Period","End Period","Services","Customer Name"}; //Set the headers of the table to print
		
		printTable(bookings,headerTitles,"Bookings", true,false,"There are no bookings within the next seven days."); // Print Table
		
		System.out.println("\n\n Press any key to go back to Menu...");
		
		scanner.nextLine(); //Wait for any user input from the scanner
	}
	
	/**
	 * Prints availability timetable to screen
	 * @param availability [period][0 - start, 1 - end]
	 */
	public void viewBookingAvailability(String[][] availability) {
		
		printWeeklyView(availability,"Available Times");

	}
	
	
	public void viewWorkingTimes(String [][] workingTimes)
	{
		String[] headerTitles = {"Start Period","End Period","Employee ID","Employee Name"}; //Set the headers of the table to print
		
		printTable(workingTimes ,headerTitles ,"Working Times/Dates", false,false,"There are no Working Times."); // Print Table
		
		System.out.println("\n\n Press any key to go back to Menu...");
		
		scanner.nextLine(); //Wait for any user input from the scanner
	}
	
	
	

	/**
	 * Views screen where user can enter new booking information
	 * @param availableTimes Times available to book
	 * @return [0] startDate, [1] endDate
	 */
	public String[] addNewBooking(String[][] availableTimes) {
		
		String[] bookingDetails = new String[2];
		String[][] tableToDisplay = new String[availableTimes.length][availableTimes[0].length+1];
		
		for (int i = 0; i < tableToDisplay.length; i++) {
			for (int j = 0; j < tableToDisplay[i].length; j++) {
				if (j < (tableToDisplay[i].length-1))
					tableToDisplay[i][j] = availableTimes[i][j];
				else
					tableToDisplay[i][j] = Integer.toString(i+1);
			}
		}
		
		
		
		String[] headerTitles = {"Start Period","End Period","#"}; //Set the headers of the table to print
		
		System.out.println("\n\n Enter the # of your booking from the list above: ");

		printTable(tableToDisplay,headerTitles,"Add a new booking", true,false,"There are no bookings available."); // Print Table
		
		int selected = 0;
		
		while (true) {
			try {
			
			System.out.println("\n\n Enter the # of your booking from the list above: ");
			selected = scanner.nextInt();
			if (selected <= availableTimes.length && selected > 0) {
				bookingDetails[0] = availableTimes[selected-1][0]; //Set the booking start period
				bookingDetails[1] = availableTimes[selected-1][1]; //Set the booking end period
				break;
			} else {
				System.out.println("\n\n You did not select a valid option. Please try again.");
			}
			} catch (InputMismatchException exception) {
				System.out.println("\n\n You did not select a valid option. Please try again.");
			}
		
		}
		
		return bookingDetails;
	}
	
	/**
	 * Add working times until the owner decides to stop
	 * @return [period][0 - start, 1 - end]
	 */
	public String[][] addWorkingTimes() {
		
		String[][] workingTimes = new String[1][2]; //We will use this to store the input of working times
		
		System.out.println("\nPlease enter The period of the employee's working times");
		
		System.out.println("\nStart time for the working period in the form <Date> <Time>: ");
		workingTimes[0][0] = scanner.nextLine(); //Get user (owner) input for the start of the working period
		
		System.out.println("End time for the working period in the form <Time>: ");
		workingTimes[0][1] = scanner.nextLine(); //Get user (owner) input for the start of the working period
		
		return workingTimes;
	}
	
	public ArrayList<String> addAvailableTimes() {
		
		ArrayList<String> availableTimes = new ArrayList<String>(); //We will use this to store the input of working times
		do {
		System.out.println("\nPlease enter The period of the employee's working times");
		
		System.out.println("\nStart time for the working period in the form <Weekday> <Time>: ");
		availableTimes.add(scanner.nextLine()); //Get user (owner) input for the start of the working period
		
		System.out.println("End time for the working period in the form <Time>: ");
		availableTimes.add(scanner.nextLine()); //Get user (owner) input for the start of the working period

		System.out.println("Continue adding more working periods or return to menu? (type N to return)");
		}while(!(scanner.nextLine().toLowerCase().equals("n")));
		
		
		
		return availableTimes;
	}
	

	
	/**
	 * @param employees [employee][1 - ID, 2 - name]
	 * @return ID of chosen employee, return null if no employee chosen
	 */
	public String showEmployeeList(String[][] employees) {
		
		System.out.println("Employees:");
		
		for (int i=0;i < employees.length; i++) { //Go through all the employees rows
			for (int j = 0; j < employees[i].length; j++) { //Go through all the employees columns
				System.out.print(" "+employees[i][j]);
			}
			System.out.println(); //create a new row
			}
		
		System.out.print("Select an employee of interest as identified above (Employee's ID): ");
		String selectedID = scanner.nextLine();
		
		return selectedID;
	}
	
	/**
	 * @param unixTime String formatted unixtime
	 * @return  formattedDate
	 */
	
	public String unixTimeTo24Hour (String unixTime) {
		
		Date date = new Date((Integer.parseInt(unixTime))*1000L); // convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); 
		String formattedDate = sdf.format(date);
		
		return formattedDate;
	}
	
	/** 
	 * @param timetable [period][start, end]
	 */
	
	public void showTimetable(String[][] timetable) {
		
		printWeeklyView(timetable,"Employee Availability");
 		
 	}
	
	
	/**
	 * Views screen for adding new employees with working times
	 * @return [employee][0 - details, 1 - timetable][]
	 */
	public String[] addEmployee() 
	{
		System.out.println("--------------------------\nAdd Employee\n--------------------------");
		
		String name; //Employee name
		String phoneNo; //Employee phone number
		String address; //Employee address

		System.out.print("Name: ");
		name = scanner.nextLine();
		
		System.out.print("Phone number: ");
		phoneNo = scanner.nextLine();
		
		System.out.print("Address: ");
		address = scanner.nextLine();
			
		return new String[] {name, phoneNo, address};
	}
	
	/**
	 * prints a weekly view of the table
	 * @param contents	Table content
	 * @param tableTitle	Title of table
	 * @param noContentsMessage	Message to show when there is no contents i.e nothing during the week
	 */
	
	public void printWeeklyView(String[][] contents,String tableTitle) {
		
int[] DayPeriodCounts = new int[Weekdays.length]; //Used to figure out the number of rows in the table
		
		for (int i=0;i < DayPeriodCounts.length; i++) {
			DayPeriodCounts[i] = 0; //initialize all the days to 0 count of working periods
		}
		
		String ConvertedDay; //Used to store a day name converted from Unix Seconds
		int dayWithMostWorkingHours = 0;
		
		for (int i=0;i < contents.length; i++) { //Find out the maximum number of working hours/period per day
			
					ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]) /1000)); //Convert to day from milliseconds to seconds then day
					DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] += 1; //increase the specific day's count of working periods
					
						if (DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)] > dayWithMostWorkingHours) //If this day has the highest number of periods,
							dayWithMostWorkingHours = DayPeriodCounts[Arrays.asList(Weekdays).indexOf(ConvertedDay)]; //make this maximum number of rows
				
			
		}
		
		
		
		String[][] convertedDates = new String[dayWithMostWorkingHours][Weekdays.length]; //Array that will be used to print out the table of the weekly view
		
		for (int i=0;i < contents.length; i++) {
			
			ConvertedDay = Period.convertSecondsToDay((int)(Long.parseLong(contents[i][0]) /1000));
			
			/* Add it to the table under the specific day column with start - end time 24 hr format*/
			for (int j = 0; j < contents[i].length; j++) { 
				if (convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] == null) {
					convertedDates[j][Arrays.asList(Weekdays).indexOf(ConvertedDay)] = unixTimeTo24Hour(contents[i][0])+" - "+unixTimeTo24Hour(contents[i][1]);
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
		
		
				for (int i=0;i < convertedDates.length; i++) { //Go through all the rows
					for (int j = 0; j < convertedDates[i].length; j++) { //Go through all the columns
						
						if (j ==0)
							System.out.print("   ");
						
						System.out.print("  "+convertedDates[i][j]); //Print out the current detail
						
						/*Add enough spaces to keep table column length balanced*/
						
						if ( convertedDates[i][j] == "")
							for (int k = 0; k < 15; k++)
								System.out.print(" "); //Create a gap between columns
					}
					System.out.println(); //create a new row
				}
				
				for (int k = 0; k < (tableTitles.length()); k++)
					System.out.print("-"); //Add dashes "-" under the table
				
				System.out.println("\n\n Press any key to go back to Menu...");
				
				scanner.nextLine(); //Wait for any user input from the scanner
	}
	
	
	/**
	 * Format: "Success: [Subject],[Details]"
	 * Example: "Success: Login, username1 logged in" 
	 * @param subject Current operation
	 * @param details Message
	 */
	public void success(String subject, String details) {
		System.out.println("Success: " + subject + ": " + details);
	}
	
	/**
	 * Format: "Failure: [Subject],[Details]"
	 * Example: "Failure: Login, password for username1 incorrect"
	 * @param subject Current operation
	 * @param details reason
	 */
	public void failure(String subject, String details) {
		System.out.println("Failure: " + subject + ": " + details);
	}

	public String getWorkingTimesMenu(String[][] employeeList) {
		// TODO Auto-generated method stub
		return null;
	}
	/* use to select a week to view, can be used for anything that requires week choice */
	public String selectWeek() {
		// TODO Auto-generated method stub
		return null;
	}
}
