package main;

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
		                     "View Available Tiems", "Add New Booking", "View Summary of Bookings", 
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
		return false;
	}
	
	/**
	 * Prints all bookings to screen
	 * @param [booking][0 - customerUsername, 1 - startPeriod, 2 - endPeriod]
	 */
	public void viewBookings(String[][] bookings) {
		
	}
	
	/**
	 * Prints availability timetable to screen
	 * @param [period][0 - start, 1 - end]
	 */
	public void viewBookingAvailability(String[][] availability) {
		
	}
	
	/**
	 * Views screen where user can enter new booking information
	 * @return [0] customerUsername, [1] startDate, [2] endDate
	 */
	public String[] addNewBooking() {
		return new String[3];
	}
	
	/**
	 * Add working times until the owner decides to stop
	 * @return [period][0 - start, 1 - end]
	 */
	public String[][] addWorkingTimes() {
		
		return new String[0][0];
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
		
		String username;
		String password;
		Boolean validatedDetails;
		
		
		/**In order to avoid wring characters from being inserted, we have implemented a limit of allowable characters
		 * with the help from source: http://stackoverflow.com/questions/29761008/java-character-input-validation
		 * 
		 */
		System.out.print("Enter your Username: ");
		username = scanner.nextLine();
		
		System.out.print("Enter your Password: ");
		password = scanner.nextLine();
		
		validatedDetails = validatelogin(username,password);
		
		/*
		 * The idea behind this check is to avoid characters that can cause internal
		 * errors like the character ":" from being passed along to the database code.
		 */
		
		if (!validatedDetails)
		{
			System.out.println("/n Sorry your username and password needs to be character from A-z and numbers 1-9 only");
			return null;
		}
			
		return new String[] {username, password};
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
