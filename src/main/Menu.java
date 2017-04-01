package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Menu {
	private static Scanner scanner = new Scanner(System.in);
	
	
	public Menu() {

	}
	
	/**
	 * Current format of permissions is as follows:
	 * 0 - Register
	 * 1 - Login
	 * 2 - View current bookings
	 * 3 - View available times
	 * 4 - Add new booking
	 * 5 - View summary of bookings
	 * 6 - Add working time/date
	 * 7 - Show all worker availability for next 7 days
	 * 8 - Add an employee
	 * 9 - Log out
	 */
	
    /**
     * Displays all options based on the active user in userManager.
     * If there is no active user then default to only displaying 
     * 'Register' and 'Login'.
     */
	public int displayOptions(boolean[] permissions) {
		int userSelection;
		int optionNum = 0;
		String [] option = {"Log In", "Register", "View Current Bookings", 
		                     "View Available Times", "Add New Booking", "View Summary of Bookings", 
		                     "Add working time/date", "Show all worker availability", "Add an Employee", "Log out"};
		
		
		while (true) {
			System.out.println("--------------------------\nAppointment Booking System\n--------------------------");
			System.out.println("Please enter the identification number of your choice from the options below: ");
			
			for(int i = 0; i < permissions.length; i++) //go through permissions and print each of the available options
			{
				if(permissions[i])
				{
					optionNum++;
					System.out.println(optionNum + ".\t" + option[i]);
				}
			}
			optionNum = 0;
			if(permissions[9] == false) //if you can't log out as you are not logged in, there is a break
			{
				System.out.println("10.\tExit"); // added by Spencer - exit case
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
					if(!permissions[9] && selectedOption != 10)
					{
						selectedOption = 11;
					}
					
				}
				userSelection = selectedOption;
			} catch (NumberFormatException e) {
				System.out.println("Sorry you have provided an invalid option! Please try again:");
				continue;
			}
			if (userSelection > -1 && userSelection < 11) // changed to 11 by Spencer - I'll include invalid option checking in my contrller main aswell
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
		
		System.out.println();
		
		String username;
		String password;
		String name;
		String address;
		String phoneNo;
		Boolean validatedDetails;
		
		
		/**In order to avoid wring characters from being inserted, we have implemented a limit of allowable characters
		 * with the help from source: http://stackoverflow.com/questions/29761008/java-character-input-validation
		 * 
		 */

		while (true) {
			
			System.out.print("Enter your Username: ");
			username = scanner.nextLine();
			
			System.out.print("Enter your Password: ");
			password = scanner.nextLine();
			
			System.out.print("Enter your Name: ");
			name = scanner.nextLine();
			
			System.out.print("Enter your Address: ");
			address = scanner.nextLine();
			
			System.out.print("Enter your Phone Number: ");
			phoneNo = scanner.nextLine();
			
			
			validatedDetails = validatelogin(username,password);
			
			/*
			 * The idea behind this check is to avoid characters that can cause internal
			 * errors like the character ":" from being passed along to the database code.
			 */
			
			if (validatedDetails)
				break;
			else
				System.out.println("/n Sorry your username and password needs to be character from A-z and numbers 1-9 only");
		}
			
		return new String[] {username, password, name, address, phoneNo};
	}
	
	/**
	 * Validate that the login details do not contain invalid characters
	 * @param username
	 * @param password
	 * @return success
	 */
	
	public Boolean validatelogin(String username,String password) {
		
		/**In order to avoid wring characters from being inserted, we have implemented a limit of allowable characters
		 * with the help from source: http://stackoverflow.com/questions/29761008/java-character-input-validation
		 * 
		 */
		
		if (username.matches("[A-Za-z0-9]+") && password.matches("[A-Za-z0-9]+")){
			return true;
		} else {
			return false;
		}
		

	}
	
	/**
	 * Displays login screen where user enters their username and password
	 * @return [0] username, [1] password
	 */
	public String[] login() {
		String username;
		String password;
		Boolean validatedDetails;

		while (true) {
			
			System.out.print("Enter your username: ");
			username = scanner.nextLine();
			
			System.out.print("Enter your password: ");
			password = scanner.nextLine();
			
			validatedDetails = validatelogin(username,password);
			
			/*If the input contains invalid characters we can just assume 
			 * password is incorrect since we would have checked this during registration.
			 * The idea behind this check is to avoid characters that can cause internal
			 * errors like the character ":" from being passed along to the database code.
			 */
			
			if (validatedDetails)
				break;
			else
				System.out.println("Your username and/or password is incorrect. Please try again.");
		}

		
		
		return new String[] {username, password};
	}
	
	/**
	 * Confirmation screen for logging out
	 */
	public boolean logout() {
		int userSelection; //Will be used to store the option selected by the user
		
		/*Print out the prompts in the console*/
		System.out.println("Are you sure you want to logout?");
		System.out.println("1 - Yes");
		System.out.println("2- Cancel");
		
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
	 * @param bookingTime
	 * @return success
	 */
	
	public boolean checkBookingWithinWeek(String bookingTime) {
		
		/* Current Date and Time */
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		try {
		
		/* Given Date and Time */
		SimpleDateFormat formatedDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date newDate = formatedDate.parse(bookingTime);
		
		/*Compare between the dates*/
		  Calendar c=Calendar.getInstance();
		  c.setTime(date);
		  c.add(Calendar.DATE,7); //Add 7 days to the current date
		  if(c.getTime().compareTo(newDate)<0){
			  return false; //It's more than seven days.
			  
		  }
		  else
		  {
			  return true; //It's less than 7 days.
		  }
		} catch (ParseException e) {
	        e.printStackTrace();
	    }
		
		// Something clearly went wrong if we get here
		return false;
		
		
		
	}
	
	/**
	 * Converts date to a readable format
	 * @param date
	 * @return farmatedDate
	 */
	
	public String formatDate(String date) {
	
		try {
		
			SimpleDateFormat formatedDate = new SimpleDateFormat("yyyyMMddHHmmss");
			Date newDate = formatedDate.parse(date);
			formatedDate.applyPattern("dd/MM/yyyy HH:mm:ss");
			return formatedDate.format(newDate);
			} catch (ParseException e) {
				e.printStackTrace();
				}
	
		return "";
		}
	
	/**
	 * Prints all bookings to screen
	 * @param [booking][0 - customerUsername, 1 - startPeriod, 2 - endPeriod]
	 */
	public void viewBookings(String[][] bookings) {
		
		/*Variables to be used to determine each column length*/
		int longestNameLength = 0; //Store the longest customer name (in length of the string)
		int longeststartTimeLength = 0; //Store the longest booking start period (in length of the string)
		int longestendTimeLength = 0; //Store the longest booking end period (in length of the string)
		Boolean canPrintSomething = false; //Check if there is something within 7 days
		
		/*
		 * In order to make sure the table is printed properly,
		 * we need need to make sure that the cells are balanced in length
		 * So we need to first fine the longest Strings for each column.
		 */
		for (int i=0;i < bookings.length; i++) {
			for (int j = 0; j < bookings[i].length; j++) {
				
				//Check if the length of the customer name is currently the longest
				if (j == 0 && bookings[i][j].length() > longestNameLength)
					longestNameLength = bookings[i][j].length();
				
				//Check if the length of the customer start time is currently the longest
				if (j == 1 && bookings[i][j].length() > longeststartTimeLength)
					longeststartTimeLength = bookings[i][j].length();
				
				//Check if atleast this date is within seven days
				if (j ==1) {
					if (checkBookingWithinWeek(bookings[i][j])) //Check if we need to bother print the table
						canPrintSomething = true; //Yes we can print the table
				}
				
				//Check if the length of the customer end time is currently the longest
				if (j == 2 && bookings[i][j].length() > longestendTimeLength)
					longestendTimeLength = bookings[i][j].length();
			}
		}
		
		
					
		//Menu Title			
		System.out.println("\n--------------------\nBookings\n--------------------\n");

		if (canPrintSomething) {
		/*Table Header*/
		
		String tableTitles = "     Customer Name"; //Customer table header
		for (int k = 0; k < (longestNameLength - "Customer Name".length()); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
		
		tableTitles += "   | Start Period"; //Start Period table header
		for (int k = 0; k < (longeststartTimeLength - "Start Period".length()); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
		

		tableTitles += "   | End Period"; //End Period table header
		for (int k = 0; k < (longestendTimeLength - "End Period".length()); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
		
		System.out.println(tableTitles); //Print out the table header titles
		
		for (int k = 0; k < (tableTitles.length()); k++)
			System.out.print("-"); //Add dashes "-" under the table header
		
		System.out.println(); //Create a new line for the rest of the table contents
		
		
				for (int i=0;i < bookings.length; i++) { //Go through all the bookings rows
					for (int j = 0; j < bookings[i].length; j++) { //Go through all the bookings columns
						
						if (j == 1 || j == 2)
							System.out.print("     "+formatDate(bookings[i][j])); //Print out dates in correct format
						else
							System.out.print("     "+bookings[i][j]); //Print out customer name
						
						/*Add enough spaces to keep table column length balanced*/
						
						if (j == 0) //Add spaces after Customer name
							for (int k = 0; k < (longestNameLength - bookings[i][j].length()); k++)
								System.out.print(" ");
						
						if (j == 1) //Add spaces after Booking Start Time
							for (int k = 0; k < (longeststartTimeLength - bookings[i][j].length()); k++)
								System.out.print(" ");
						
						if (j == 2) //Add spaces after Booking End Time
							for (int k = 0; k < (longestendTimeLength - bookings[i][j].length()); k++)
								System.out.print(" ");
							
						System.out.print("     "); //Create a gap between columns
					}
					System.out.println(); //create a new row
				}
				
				for (int k = 0; k < (tableTitles.length()); k++)
					System.out.print("-"); //Add dashes "-" under the table
		} else {
			System.out.println("There are no bookings within the next seven days.");
		}
				
				System.out.println("\n\n Press any key to go back to Menu...");
				
				scanner.nextLine(); //Wait for any user input from the scanner
	}
	
	/**
	 * Prints availability timetable to screen
	 * @param [period][0 - start, 1 - end]
	 */
	public void viewBookingAvailability(String[][] availability) {
		/*Variables to be used to determine each column length*/
		int longeststartTimeLength = 0; //Store the longest booking start period (in length of the string)
		int longestendTimeLength = 0; //Store the longest booking end period (in length of the string)
		
		/*
		 * In order to make sure the table is printed properly,
		 * we need need to make sure that the cells are balanced in length
		 * So we need to first fine the longest Strings for each column.
		 */
		for (int i=0;i < availability.length; i++) {
			for (int j = 0; j < availability[i].length; j++) {
				

				
				//Check if the length of the customer start time is currently the longest
				if (j == 0 && availability[i][j].length() > longeststartTimeLength)
					longeststartTimeLength = availability[i][j].length();
				
				//Check if the length of the customer end time is currently the longest
				if (j == 1 && availability[i][j].length() > longestendTimeLength)
					longestendTimeLength = availability[i][j].length();
			}
		}
		
		
					
		//Menu Title			
		System.out.println("\n--------------------\nAvailable Bookings\n--------------------\n");

		
		/*Table header*/
		
		String tableTitles = "     Start Period"; //Start Period table header
		for (int k = 0; k < (longeststartTimeLength - "Start Period".length()); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
		

		tableTitles += "   | End Period"; //End Period table header
		for (int k = 0; k < (longestendTimeLength - "End Period".length()); k++)
			tableTitles += " "; //Add Spaces to balance the table column length
		
		System.out.println(tableTitles); //Print out the table header titles
		
		for (int k = 0; k < (tableTitles.length()); k++)
			System.out.print("-"); //Add dashes "-" under the table header
		
		System.out.println(); //Create a new line for the rest of the table contents
		
		
				for (int i=0;i < availability.length; i++) { //Go through all the availability rows
					for (int j = 0; j < availability[i].length; j++) { //Go through all the availability columns
						
						
						System.out.print("     "+availability[i][j]); //Print out the current booking detail
						
						/*Add enough spaces to keep table column length balanced*/

						if (j == 0) //Add spaces after Booking Start Time
							for (int k = 0; k < (longeststartTimeLength - availability[i][j].length()); k++)
								System.out.print(" ");
						
						if (j == 1) //Add spaces after Booking End Time
							for (int k = 0; k < (longestendTimeLength - availability[i][j].length()); k++)
								System.out.print(" ");
							
						System.out.print("     "); //Create a gap between columns
					}
					System.out.println(); //create a new row
				}
				
				for (int k = 0; k < (tableTitles.length()); k++)
					System.out.print("-"); //Add dashes "-" under the table
				
				System.out.println("\n\n Press any key to go back to Menu...");
				
				scanner.nextLine(); //Wait for any user input from the scanner
	}
	
	/**
	 * Views screen where user can enter new booking information
	 * @return [0] customerUsername, [1] startDate, [2] endDate
	 */
	public String[] addNewBooking() {
		
		String[] bookingDetails = new String[3]; //We will use this to store the input of the booking details
		
		System.out.println("\nPlease enter the customer booking details below");
		
		System.out.println("\nCustomer username: ");
		bookingDetails[0] = scanner.nextLine(); //Get user input for the customer's username
		
		System.out.println("Booking start time and date: ");
		bookingDetails[1] = scanner.nextLine(); //Get user input for the booking start period
		
		System.out.println("Booking end time and date: ");
		bookingDetails[2] = scanner.nextLine(); //Get user input for the booking end period
		
		
		return bookingDetails;
	}
	
	/**
	 * Add working times until the owner decides to stop
	 * @return [period][0 - start, 1 - end]
	 */
	public String[][] addWorkingTimes() {
		
		String[][] workingTimes = new String[1][2]; //We will use this to store the input of working times
		
		System.out.println("\nPlease enter The period of the employee's working times");
		
		System.out.println("\nStart time for the working period: ");
		workingTimes[0][0] = scanner.nextLine(); //Get user (owner) input for the start of the working period
		
		System.out.println("End time for the working period: ");
		workingTimes[0][1] = scanner.nextLine(); //Get user (owner) input for the start of the working period
		
		return workingTimes;
	}
	
	/**
	 * prints employee timetables
	 * @param employeeTimetables employee1Username:timetable*employee2Username:timetable*employee3Username*timetable
	 * @param [employee][0 - details, 1 - timetable][]
	 */
	public void showEmployeeAvailability(String[][][] employeeTimetables) {
		
	}
	
	/**
	 * Views screen for adding new employees with working times
	 * @return [employee][0 - details, 1 - timetable][]
	 */
	public String[] addEmployee() 
	{
		System.out.println("--------------------------\nAdd Employee\n--------------------------");
		
		String name;
		String phoneNo;
		String address;
		String id;

		System.out.print("Name: ");
		name = scanner.nextLine();
		
		System.out.print("Phone number: ");
		phoneNo = scanner.nextLine();
		
		System.out.print("Address: ");
		address = scanner.nextLine();
		
		System.out.print("Employee ID: ");
		id = scanner.nextLine();
			
		return new String[] {name, phoneNo, address, id};
	}
	
	/**
	 * Format: "Success: [Subject],[Details]"
	 * Example: "Success: Login, username1 logged in" 
	 * @param subject
	 * @param details
	 */
	public void success(String subject, String details) {
		System.out.println(subject + ": " + details);
	}
	/**
	 * Format: "Failure: [Subject],[Details]"
	 * Example: "Failure: Login, password for username1 incorrect" 
	 * @param subject
	 * @param details
	 */
	public void failure(String subject, String details) {
		System.out.println(subject + ": " + details);
	}
}
