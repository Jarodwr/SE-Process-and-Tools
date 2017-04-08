package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.database.SQLiteConnection;
import model.period.Booking;
import model.period.Period;
import model.timetable.Timetable;
import model.users.User;
import model.utility.Utility;
import view.console.Console;

/*
 * The controller class is the core functionality of our code
 * It is responsible for validation checking as well as running the the menu loop
 * and calling the view to get user input and to send updates to the view.
 */
public class Controller {
	private Logger LOGGER = Logger.getLogger("main");
	
	private Console view = new Console();
	private Utility services = new Utility();

	private User activeUser;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false, false};
	
	public Controller() {}
	/*
	 * the run function starts the the menu loop and gets the user input,
	 * shows the user the menu loop and call the respective functions from the 
	 * controller class
	 */
	public void run()
	{
		//initialize view
		Console view = new Console();

		boolean breakLoop = false; // exit program case
		//debugMode = true; // remove this line while demoing
		
		//when the menu is started set to the default permissions so that the user can only log in 
		//and register till they log in
		boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		
		//set the active user as there is no one logged in whene the program starts
		activeUser = null;
		
		//start the menu loop
		while(!breakLoop) 
		{
			//if the user set then log their permissions
			if (activeUser != null) 
			{
				LOGGER.log(Level.FINE, "Active user permissions : " + Arrays.toString(activeUser.getPermissions()));
				//change their permissions if their logged in
				currentPerms = activeUser.getPermissions();
			}
			
			// get the option number from the options specified from their permissions
			int option = view.displayOptions(currentPerms);
			//run the option that is returned from the user
			switch(option) {
			
			// if the user selects the register option then run the register function
			case 1: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: REGISTER");
				register(view.register());
				break;
			// if the user selects the login option then run the login function
			case 0: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: LOGIN");
				activeUser = login(view.login());
				if (activeUser != null) {
					//if they are the owner then log it
					if (activeUser.isOwner()) {
						LOGGER.log(Level.FINE, "User is owner");
					}
				}	
				break;
			// if the user selects the view current bookings option then run the view current bookings function
			case 2: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW CURRENT BOOKINGS");
				viewCurrentBookings();
				break;
			// if the user selects the view available times option then run the view available times function
			case 3: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW AVAILABLE TIMES");
				viewAvailableTimes();
				break;
			// if the user selects the add new booking option then run the add new booking function
			case 4: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD NEW BOOKING");
				addNewBooking(view.addNewBooking());
				break;
			// if the user selects the view summary of bookings option then run the view summary of bookings function
			case 5: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW SUMMARY OF BOOKINGS");
				viewSummaryOfBookings();
				break;
			// if the user selects the add working times option then run the add working times function
			case 6: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD WORKING TIMES");
				addWorkingTimes(view.addWorkingTimes());
				break;
			// if the user selects the show worker availability option then run the show worker availability function
			case 7: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: SHOW WORKER AVAILABILITY");
				showWorkerAvailability();
				break;
			// if the user selects the add employee option then run the add employee function
			case 8: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD EMPLOYEE");
				addEmployee(view.addEmployee());
				break;
			// if the user selects the edit availabilities option then run the edit availabilities function
			case 9: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: EDIT AVAILABILITIES");
				editAvailability();
				break;
			// if the user selects the logout option
			case 10: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: LOGOUT");
				//run the logout view function and break the loop terminating the program
				view.logout();
			case 11: breakLoop = true;
				break;
			//if the user selects an invalid option the print an error message to the view and go through the loop again
			default: LOGGER.log(Level.FINE, "INVALID MENU OPTION CHOSEN");
				view.failure("Sorry you have provided an invalid option! Please try again", "");
				break;
			}
			
		}
	}
	

	/**
	 * The login function receives an array of strings and then authenticates the username and password
	 * returns null if the user authenticates or the user if they exist
	 * @param [0] username, [1] password
	 * @return the user if the authentication passes
	 */
	protected User login(String[] loginDetails) {
		LOGGER.log(Level.FINE, "LOGIN: Login details: " + Arrays.toString(loginDetails));
		//Search for the user in the arrayList and make sure the password is correct
		User user = services.authenticate(loginDetails[0], loginDetails[1]);
		
		//check if the user is authenticated or not
		if (user == null) {
			//if it fails then alert the user
			LOGGER.log(Level.FINE, "LOGIN: Failed");
			view.failure("Login", "Incorrect Username/Password");
		} else {
			//if it succeeds then inform the user
			LOGGER.log(Level.FINE, "LOGIN: Success");
			view.success("Login", "Welcome back, " + user.getUsername());
		}
		//return the user value
		return user;
	}
	
	/**
	 * This method validates the user input and then if it passes creates a new user
	 * providing that there is no other user with the same username
	 * @param userDetails [0] username, [1] password, [2] name, [3] address, [4] mobile number
	 * @return returns the user created or null it it fails validation
	 */
	protected User register(String[] userDetails)
	{
		//validate all the user input to match the regular expression
		LOGGER.log(Level.FINE, "REGISTER: Registration details: " + Arrays.toString(userDetails));
		if(!services.validate(userDetails[0], "[A-Za-z0-9]+"))
  		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username does not match regex");
			view.failure("Register", "Username is not Valid");
			return null;
		}
		if(!services.validate(userDetails[2], "[A-Za-z]+"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, Name does not match regex");
			view.failure("Register", "Name is not Valid");
			return null;
		}
		if(!services.validate(userDetails[3], "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, Address does not match regex");
			view.failure("Register", "Address is not Valid");
			return null;
		}
		if(!services.validate(userDetails[4], "\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, phone number does not match regex");
			view.failure("Register", "Phone Number is not Valid");
			return null;
		}
		// if they all pass then try to create a new user and return the created user
		if (services.addCustomerToDatabase(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) {
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			return services.searchUser(userDetails[0]);
		}
		else //if not then inform the user that there is a user with that name and return null
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username already taken");
			view.failure("Register", "The entered username is already in the database");
			return null;
		}
				
	}
	
	
	private void viewCurrentBookings() {
		
	}
	
	/**
	 * 
	 */
	private void viewAvailableTimes() {
		Timetable ConcatenatedTimetable = services.getAvailableTimes();
		if (ConcatenatedTimetable == null) {
			LOGGER.warning("No employees registered in the system");
			view.failure("View Available Times", "No Employees registered in the system with available times");
			return;
		}
		String[][] s = ConcatenatedTimetable.toStringArray();
		view.viewBookingAvailability(s);
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {

		Booking[] bookings = services.getBookingsAfter(new Date(Calendar.getInstance().getTimeInMillis()));

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
	
	
	private void editAvailability() {
		String employeeId = "";
		Timetable t = new Timetable();
		try {
			employeeId = view.showEmployeeList(services.getEmployeeList());
			if(employeeId == null || employeeId.equals("")) {
				LOGGER.log(Level.FINE, "EDIT AVAILABILITIES: Failure, no such employee exists");
				return;
			}
			
		} catch(Exception e) {
			LOGGER.warning(e.getMessage());
		}
		ArrayList<String> availabilities = view.addAvailableTimes();
		Iterator<String> iter = availabilities.iterator();
		while(iter.hasNext()) {
			String[] start = iter.next().split(" ");
			
			if (!iter.hasNext()) {
				break; // weird issue with console where we're missing an end period, just cancel here
			}
			
			String end = iter.next();
			if (start.length != 2) {
				continue;
			}
			String weekday = start[0];
			if (!Period.checkIsValidWeekday(weekday)) {
				continue;
			}
			String starttime = Integer.toString(Period.convertDayToSeconds(weekday) + Period.convert24HrTimeToDaySeconds(start[1]));
			String endtime = Integer.toString(Period.convertDayToSeconds(weekday) + Period.convert24HrTimeToDaySeconds(end));
			t.addPeriod(new Period(starttime, endtime, false));
		}
		if (employeeId.equals("")) {

			LOGGER.log(Level.FINE, "EDIT AVAILABILITIES: Failure, no such employee exists");
			return;
		}
		try {
			ResultSet rs = SQLiteConnection.getAllAvailabilities();
			int id = SQLiteConnection.getNextAvailableId(rs, "timetableId");
			SQLiteConnection.createAvailability(id, "SARJ's Milk Business", t.toString()); /* TODO Part B: Remove hardcode */
			SQLiteConnection.updateAvailabilityforEmployee(Integer.parseInt(employeeId), id);
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		 
	}
	
	
	private void showWorkerAvailability() {
		try {
			String employeeId = view.showEmployeeList(services.getEmployeeList());
			while (employeeId != null && !employeeId.equals("")) {
				Timetable t = services.getEmployeeAvailability(employeeId);
				if (t.equals(null) || t.getAllPeriods().length == 0) {
					view.failure("View worker availability", "This worker has no available times");
				} else {
					view.showTimetable(t.toStringArray());
				}
				employeeId = view.showEmployeeList(services.getEmployeeList());
			}
			
		} catch(Exception e) {
			LOGGER.warning(e.getMessage());
		}

	}

	//Need to add logging for this once it is completed
	boolean addEmployee(String[] newEmployee) 
	{
		String name = newEmployee[0];
		String phonenumber = newEmployee[1];
		String address = newEmployee[2];
		String id = "";
		try {
			ResultSet rs = SQLiteConnection.getAllEmployees();
			int i = SQLiteConnection.getNextAvailableId(rs, "employeeId");
			rs.close();
			id = Integer.toString(i);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		

		
		if(!services.validate(name, "[A-Za-z]+"))
  		{
			view.failure("Add Employee", "Name is not Valid");
			return false;
		}
		
		if(!services.validate(phonenumber, "[0-9]+"))
		{
			view.failure("Add Employee", "Phone number is not Valid");
			return false;
		}
		
		if(!services.validate(address, "[A-Za-z0-9' ]+"))
		{
			view.failure("Add Employee", "Address is not Valid");
			return false;
		}
		
		if (services.addEmployeeToDatabase(id, "", name, address, phonenumber, 0))
		{ /* TODO add cases for staff and owners */
			view.success("Add Employee", name + " was successfully added to the database");
			return true;
		}
		else
		{
			view.failure("Add Employee", "The entered username is already in the database");
			return false;
		}
	}
}
