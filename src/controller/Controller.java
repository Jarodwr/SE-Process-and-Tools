package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import database.SQLiteConnection;
import database.UserExistsException;

import java.util.StringTokenizer;

import bookings.Booking;
import main.Menu;
import period.Period;
import timetable.Timetable;
import users.Customer;
import users.Owner;
import users.User;

public class Controller {
	
	private Menu view = new Menu();
	
	private User activeUser;
	
	private Timetable availability = new Timetable();
	private boolean debugMode = false;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false};
	
	@SuppressWarnings("deprecation")
	public Controller() {
		
		//initialize view
		Menu view = new Menu();

		boolean breakLoop = false; // exit program case
		debugMode = true; // remove this line while demoing
		boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		activeUser = null;
		while(!breakLoop) {
			if (activeUser != null) {
				for (int i = 0; i < activeUser.getPermissions().length; i++) {
					if (activeUser.getPermissions()[i])
						dbg(" permissions: true");
					else
						dbg(" permissions: false");
				}
			}
			int option = view.displayOptions(currentPerms);
			switch(option) { /* TODO */
			case 0: activeUser = login(view.login());
					if (activeUser != null) {
						currentPerms = activeUser.getPermissions();
						if (activeUser.isOwner()) {
							dbg("Owner is logged in!");
						}
					}
					
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
			dbg("loginDetails[0] = " + loginDetails[0] + ", loginDetails[1] = " + loginDetails[1]);
		//Search for the user in the arrayList and make sure the password is correct
		if (searchUser(loginDetails[0]) == null) {
			view.failure("Login", "Incorrect Password");
			return null;
		}
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
		view.viewBookingAvailability(availability.toStringArray());
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {
		Booking[] bookings = getFutureBookings();
		if (bookings.length == 0) {
			view.failure("View Booking Summaries", "No future bookings");
		} else {
			String[][] bookingsStringArray = new String[bookings.length][];
			
			for (int i = 0; i < bookings.length; i++) {
				bookingsStringArray[i] = bookings[i].toStringArray();
			}
			
			view.viewBookings(bookingsStringArray);
		}
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
			dbg(e.getMessage());
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
	
	private void dbg(String s) // fast way of adding System prints, dbg = debug
	{
		if (this.debugMode) {
			System.out.println("DEBUG: " + s); // fast af boi
		}
	}
}
