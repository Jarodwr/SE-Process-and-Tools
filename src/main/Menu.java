package main;

import bookingmanager.BookingManager;
import usermanager.UserManager;

public class Menu {

	UserManager userManager;
	BookingManager bookingManager;
	
	Menu() {

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
	public void displayOptions() {
		
	}
	
	// All of these private functions just display the text and call
	// the related function from userManager or bookingManager to get
	// the appropriate string
	
	private void register() {
		
	}
	
	private void login() {
		
	}
	
	private void viewCurrentBookings() {
		
	}
	
	private void viewBookingAvailability() {
		
	}
	
	private void addNewBooking() {
		
	}
	
	
	private void viewAllBookings() {
		
	}
	
	private void addWorkingTimes() {
		
	}
	
	
	private void showEmployeeAvailability() {
		
	}
	
	private void addEmployee() {
		
	}
	
	private void logout() {
		
	}
}
