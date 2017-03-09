package controller;

import java.util.ArrayList;
import java.util.StringTokenizer;

import bookings.Booking;
import main.Menu;
import timetable.Timetable;
import users.Customer;
import users.User;

public class Controller {
	
	private Menu view = new Menu();
	
	private User activeUser;
	private ArrayList<User> users = new ArrayList<User>(); // Maybe turn this into an arrayList
	
	private Timetable availability = new Timetable();
	private ArrayList<Booking> bookings = new ArrayList<Booking>();
	
	public Controller() {
		//initialize databases and add them to ArrayList
		
		//initialize view
		view.displayOptions(new boolean[10]);
	}
	
	private void login() {
		
		//Initializes the view and grabs input
		String loginDetails = view.login();
		
		//Tokenizes input so it's usable
		StringTokenizer st = new StringTokenizer(loginDetails, ":");
		
		String username = st.nextToken();
		String password = st.nextToken();
		
		//Search for the user in the arrayList and make sure the password is correct
		if (searchUser(username).checkPassword(password)) {
			
			//If the password is incorrect, display a failure message
			view.failure("Login", "Incorrect Password");
		}
		else {
			
			//If the password is correct, display a success message
			view.success("Login", "Welcome back " + username);
		}
	}
	
	private void register() {
		
	}
	
	private void logout() {
		
	}
	
	private void viewCurrentBookings() {
		
	}
	
	private void viewAvailableTimes() {
		
	}
	
	private void addNewBooking() {
		
	}
	
	private void viewSummaryOfBookings() {
		
	}
	
	private void addWorkingTimes() {
		
	}
	
	private void showWorkerAvailability() {
		
	}
	
	private void addEmployee() {
		
	}
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	private User searchUser(String username) {
		//Needs some sort of user not found exception
		return new Customer("", "", "", "", "");
	}
	
	/**
	 * Loops through 'users' and writes all user information to a text file
	 * @param filePath filepath of the txt file being written
	 * @return Return true if successful
	 */
	private boolean writeUserInfoToText(String filePath) {
		
		return false;
	}
}
