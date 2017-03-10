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
	
	public Controller() {
		//initialize databases and add them to ArrayList
		
		//initialize view
		Menu view = new Menu();
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
			view.success("Login", "Welcome back, " + username + "!");
		}
	}
	
	private void register(){
		//Initializes the view and grabs input
				String userDetails = view.register();
				
				//Tokenizes input so it's usable
				StringTokenizer st = new StringTokenizer(userDetails, ":");
				
				String username = st.nextToken();
				String password = st.nextToken();
				String name = st.nextToken();
				String address = st.nextToken();
				String mobileno = st.nextToken();
				
				if (SQLiteConnection.createCustomer(username, password, name, address, mobileno)) { /* TODO add cases for staff and owners */
					
				}
				else
				{
					view.failure("Register", "Your username is already in the database");
				}
				
				//Search for the user in the arrayList and make sure the password is correct
				
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
