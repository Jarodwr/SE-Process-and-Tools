package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import database.SQLiteConnection;
import database.UserExistsException;

import java.util.StringTokenizer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import bookings.Booking;
import main.Menu;
import period.Period;
import timetable.Timetable;
import users.Customer;
import users.Owner;
import users.User;

public class Controller {
	private Logger LOGGER = Logger.getLogger(Controller.class.getName(), Controller.class);
	private Menu view = new Menu();
	
	private User activeUser;
	
	//private boolean debugMode = false;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false};
	
	@SuppressWarnings("deprecation")
	public Controller() {
		
		//initialize view
		Menu view = new Menu();

		boolean breakLoop = false; // exit program case
		//debugMode = true; // remove this line while demoing
		boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		activeUser = null;
		while(!breakLoop) {
			if (activeUser != null) {
				LOGGER.log(Level.FINE, "Active user permissions : " + Arrays.toString(activeUser.getPermissions()));
			}
			int option = view.displayOptions(currentPerms);
			switch(option) { /* TODO */
			case 0: activeUser = login(view.login());
					if (activeUser != null) {
						currentPerms = activeUser.getPermissions();
						if (activeUser.isOwner()) {
							LOGGER.log(Level.FINE, "User is owner");
						}
					}
					
				break;
			case 1: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: REGISTER");
				activeUser = register(view.register());
				break;
			case 2: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW CURRENT BOOKINGS");
				viewCurrentBookings();
				break;
			case 3: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW AVAILABLE TIMES");
				viewAvailableTimes();
				break;
			case 4: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD NEW BOOKING");
				addNewBooking(view.addNewBooking());
				break;
			case 5: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW SUMMARY OF BOOKINGS");
				viewSummaryOfBookings();
				break;
			case 6: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD WORKING TIMES");
				addWorkingTimes(view.addWorkingTimes());
				break;
			case 7: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: SHOW WORKER AVAILABILITY");
				showWorkerAvailability();
				break;
			case 8: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD EMPLOYEE");
				addEmployee(view.addEmployee());
				break;
			case 9: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: LOGOUT");
				logout(view.logout());
			case 10: breakLoop = true;
				break;
			default: LOGGER.log(Level.FINE, "INVALID MENU OPTION CHOSEN");
				view.failure("Sorry you have provided an invalid option! Please try again", "");
				break;
			}
			
		}
	}
	
	protected User login(String[] loginDetails) {
		LOGGER.log(Level.FINE, "LOGIN: Login details: " + Arrays.toString(loginDetails));
		//Search for the user in the arrayList and make sure the password is correct
		if (searchUser(loginDetails[0]) == null) {
			view.failure("Login", "Incorrect Password");
			return null;
		}
		if (searchUser(loginDetails[0]).checkPassword(loginDetails[1])) {
			
			LOGGER.log(Level.FINE, "LOGIN: Failed");
			//If the password is incorrect, display a failure message
			view.failure("Login", "Incorrect Username/Password");
			return null;
		}
		else {
			LOGGER.log(Level.FINE, "LOGIN: Success");
			//If the password is correct, display a success message
			view.success("Login", "Welcome back, " + loginDetails[0] + "!");
			return searchUser(loginDetails[0]);
		}
	}
	
	private User register(String[] userDetails){
		LOGGER.log(Level.FINE, "REGISTER: Registration details: " + Arrays.toString(userDetails));
		if (SQLiteConnection.createCustomer(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) { /* TODO add cases for staff and owners */
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			return searchUser(userDetails[0]);
		}
		else
		{
			LOGGER.log(Level.FINE, "LOGIN: Failure, username already taken");
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
		//view.viewBookingAvailability(availability.toStringArray());
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {
		Booking[] bookings = getFutureBookings();
		if (bookings.length == 0) {
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
			view.failure("View Booking Summaries", "No future bookings");
		} else {
			String[][] bookingsStringArray = new String[bookings.length][];
			
			for (int i = 0; i < bookings.length; i++) {
				bookingsStringArray[i] = bookings[i].toStringArray();
			}
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			view.viewBookings(bookingsStringArray);
		}
	}
	
	private void addWorkingTimes(String[][] workingTimes) {
		
	}
	
	private void showWorkerAvailability() {
		
	}

	//Need to add logging for this once it is completed
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
	protected User searchUser(String username) {
		ResultSet rs;
		try {
			rs = SQLiteConnection.getUserRow(username);
			if (SQLiteConnection.getOwnerRow(username) != null) {
				ResultSet rs2;
				rs2 = SQLiteConnection.getOwnerRow(username);
				String business = rs2.getString("businessname");
				rs2.close();

				rs = SQLiteConnection.getUserRow(username);
				Owner owner = new Owner(username, rs.getString("password"), business, rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return owner;
			}
			else {
				rs = SQLiteConnection.getUserRow(username);
				Customer customer = new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return customer;
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "SEARCH USER: Exception: " + e.getMessage());
			return null;
		}
		
	}
	
	protected Booking[] getFutureBookings() {
		ResultSet rs;
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sdf.format(Calendar.getInstance().getTime());
		try {
			rs = SQLiteConnection.getBookingsByPeriodStart(currentTime);
			
			do {
				bookings.add(new Booking(rs.getString(1),rs.getString(3) , new Period(sdf.parse(rs.getString(4)), sdf.parse(rs.getString(5)))));
				
			} while (rs.next());
			
			if (!bookings.isEmpty()) {
				Booking[] b = new Booking[bookings.size()];
				bookings.toArray(b);
				return b;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return new Booking[0];
	}
}
