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
		
		while (true) {
			System.out.println("--------------------------\n Appointment Booking System\n --------------------------");
			System.out.println("Please enter the identification number of your choice from the options below: ");
			System.out.println("0 - Login");
			System.out.println("1 - Register");
			System.out.println("10 - Exit"); // added by Spencer - exit case
			try {
				int selectedOption = Integer.parseInt(scanner.nextLine());
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
	 * @return username:password:name:address:phoneNumber
	 */
	public String register() {
		
    System.out.println();
		
		String username;
		String password;
		String name;
		String address;
		String phoneNo;
		
		
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
			
			
			
			if (Character.toString(username.charAt(0)).matches("^[a-pA-P0-9]*$") && Character.toString(password.charAt(0)).matches("^[a-pA-P0-9]*$")) {
				break;
		    }else{
		    	
		    	System.out.println("/n Sorry your username and password needs to be character from A-z and numbers 1-9 only");
		    }
			
		
		
		
		
		
		}
		
		return username+":"+password+":"+name+":"+address+":"+phoneNo;
	}
	
	/**
	 * Displays login screen where user enters their username and password
	 * @return username:password
	 */
	public String login() {
		String username;
		String password;
		
		/**In order to avoid wring characters from being inserted, we have implemented a limit of allowable characters
		 * with the help from source: http://stackoverflow.com/questions/29761008/java-character-input-validation
		 * 
		 */

		while (true) {
			
			System.out.print("Enter your username: ");
			username = scanner.nextLine();
			
			System.out.print("Enter your password: ");
			password = scanner.nextLine();
			
//			if (Character.toString(username.charAt(0)).matches("^[a-pA-P0-9]*$") && Character.toString(password.charAt(0)).matches("^[a-pA-P0-9]*$")) {
//				//Temporary, will fix this checking system later
//				break;
//		    }else{
//		    	
//		    	/*We are avoiding characters that could manipulate the database.
//		    	 * We will just return incorrect password before going any further
//		    	 */
//		    	System.out.println("/n You have entered incorrect username and/or password. Please try again!");
//		    	
//		    	
//		    	
//		    }
			break;
			
		
		
		
		
		
		}

		
		
		return username+":"+password;
	}
	
	/**
	 * Confirmation screen for logging out
	 */
	public boolean logout() {
		return false;
	}
	
	/**
	 * Prints all bookings to screen
	 * @param bookings booking1|booking2|booking3
	 */
	public void viewBookings(String bookings) {
		
	}
	
	/**
	 * Prints availability timetable to screen
	 * @param availability period1|period2|period3|period4
	 */
	public void viewBookingAvailability(String availability) {
		
	}
	
	/**
	 * Views screen where user can enter new booking information
	 * @return customerUsername:startDate,endDate
	 */
	public String addNewBooking() {
		return "";
	}
	
	/**
	 * Add working times until the owner decides to stop
	 * @return startDate1,endDate1|startDate2,endDate2|startDate3,endDate3
	 */
	public String addWorkingTimes() {
		
		return "";
	}
	
	/**
	 * prints employee timetables
	 * @param employeeTimetables employee1Username:timetable*employee2Username:timetable*employee3Username*timetable
	 */
	public void showEmployeeAvailability(String employeeTimetables) {
		
	}
	
	/**
	 * Views screen for adding new employees with working times
	 * @return employeeUsername:employeePassword:timetable
	 */
	public String addEmployee() {
		
		return "";
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
