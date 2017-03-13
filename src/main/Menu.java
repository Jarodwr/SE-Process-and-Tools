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
			try {
				int selectedOption = Integer.parseInt(scanner.nextLine());
				userSelection = selectedOption;
			} catch (NumberFormatException e) {
				System.out.println("Sorry you have provided an invalid option! Please try again:");
				continue;
			}
			
			if (userSelection > -1 && userSelection < 3)
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
		
		return "";
	}
	
	/**
	 * Displays login screen where user enters their username and password
	 * @return username:password
	 */
	public String login() {
		
		/*No error check yet, putting this here as a reminder to do it later*/
		
		System.out.print("Enter your username: ");
		String username = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();

		System.out.println();
		
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
		
	}
	/**
	 * Format: "Failure: [Subject],[Details]"
	 * Example: "Failure: Login, password for username1 incorrect" 
	 * @param subject
	 * @param details
	 */
	public void failure(String subject, String details) {
		
	}
}
