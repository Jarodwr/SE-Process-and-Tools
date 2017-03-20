package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.SQLiteConnection;
import database.UserExistsException;

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
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false};
	
	public Controller() {
		//initialize databases and add them to ArrayList
		
		//initialize view
		Menu view = new Menu();

		boolean breakLoop = false; // exit program case
		boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		activeUser = null;
		
		while(!breakLoop) {
			int option = view.displayOptions(currentPerms);
			switch(option) { /* TODO */
			case 0: activeUser = login(view.login());
					currentPerms = activeUser.getPermissions();
				break;
			case 1: activeUser = register(view.register());
				break;
			case 2: viewCurrentBookings();
				break;
			case 3: viewAvailableTimes();
				break;
			case 4: addNewBooking(view.addNewBooking());
				break;
			case 5: viewSummaryOfBookings();
				break;
			case 6: addWorkingTimes(view.addWorkingTimes());
				break;
			case 7: showWorkerAvailability();
				break;
			case 8: addEmployee(view.addEmployee());
				break;
			case 9: logout(view.logout());
			case 10: breakLoop = true;
				break;
			default: view.failure("Sorry you have provided an invalid option! Please try again", "");
				break;
			}
			
		}
	}
	
	protected User login(String[] loginDetails) {
				
		//Search for the user in the arrayList and make sure the password is correct
		if (searchUser(loginDetails[0]).checkPassword(loginDetails[1])) {
			
			//If the password is incorrect, display a failure message
			view.failure("Login", "Incorrect Password");
			return null;
		}
		else {
			
			//If the password is correct, display a success message
			view.success("Login", "Welcome back, " + loginDetails[0] + "!");
			return searchUser(loginDetails[0]);
		}
	}
	
	private User register(String[] userDetails){
		
		if (SQLiteConnection.createCustomer(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) { /* TODO add cases for staff and owners */

			return searchUser(userDetails[0]);
		}
		else
		{
			view.failure("Register", "The entered username is already in the database");
			return null;
		}
		
		//Search for the user in the arrayList and make sure the password is correct
				
	}
	
	private void logout(boolean success) {
		
	}
	
	private void viewCurrentBookings() {
		
	}
	
	private void viewAvailableTimes() {
		
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {
		
	}
	
	private void addWorkingTimes(String[][] workingTimes) {
		
	}
	
	private void showWorkerAvailability() {
		
	}
	
	private void addEmployee(String[] newEmployee) 
	{
		String username = newEmployee[0];
		String password = newEmployee[1];
		String timetable = newEmployee[2];
		
		if(searchUser(username) == null)
		{
			if (SQLiteConnection.createCustomer(username, password, timetable, null, null)) { /* TODO add cases for staff and owners */
	
				searchUser(username);
			}
			else
			{
				view.failure("Add Employee", "The entered username is already in the database");
			}
		}
		else
		{
			view.failure("Add Employee", "The entered name is already in the database");
		}
	}
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	private User searchUser(String username) {
		ResultSet rs;
		try {
			rs = SQLiteConnection.getUserRow(username);
			return new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Loops through 'users' and writes all user information to a text file
	 * @param filePath filepath of the txt file being written
	 * @return Return true if successful
	 * 
	 * 
	 * 
	 * Spencer note: Redundant, remove when 100% sure we dont need it
	 */
	private boolean writeUserInfoToText(String filePath) {
		
		return false;
	}
}
