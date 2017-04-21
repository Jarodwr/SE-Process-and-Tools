package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.GuiMain;
import model.database.SQLiteConnection;
import model.exceptions.ValidationException;
import model.period.Booking;
import model.period.Period;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Owner;
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
	private Utility utilities = new Utility();

	private User activeUser;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false, false ,false, false, false, false};

	private String[] args = {};
	
	public Controller(String[] args) {
		this.args = args;
	}
	/*
	 * the run function starts the the menu loop and gets the user input,
	 * shows the user the menu loop and call the respective functions from the 
	 * controller class
	 */
	public void run()
	{
		//initialize view
		//Console view = new Console();
		
		// launch GFX for testing - remove this line to use console instead

		//boolean breakLoop = false; // exit program case
		//debugMode = true; // remove this line while demoing
		
		//when the menu is started set to the default permissions so that the user can only log in 
		//and register till they log in
		//boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		
		
		//int defaultOption = -1; // allows us to pick a default option to choose in the menu via launch config. Ignores permissions.
		
		//set the active user as there is no one logged in whene the program starts,
		//unless someone parsed a username and password as launch parameters
		/*
		if (args.length == 0) {
			activeUser = null;
		}
		else if( args[0] != null && args[1] != null) {
			activeUser = login(new String[] {args[0], args[1]});
		}
		else {
			activeUser = null;
		}
		try {
			if (args.length >= 3) {
				 defaultOption = Integer.parseInt(args[2]);
			}
		}
		catch (Exception e) {
			LOGGER.warning("3rd argument from launch parameter was detected but isn't a number");
		} */
				
		/* commented out for now, no longer needed 
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
			int option;
			if (defaultOption == -1) {
				option = view.displayOptions(currentPerms);
			}
			else {
				option = defaultOption;
				defaultOption = -1;
			}
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
				addNewBookings(view.addNewBooking("user"));
				break;
			// if the user selects the view summary of bookings option then run the view summary of bookings function
			case 5: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW SUMMARY OF BOOKINGS");
				viewSummaryOfBookings();
				break;
			// if the user selects the add working times option then run the add working times function
			case 6: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD WORKING TIMES");
				addWorkingTimes(view.showEmployeeList(utilities.getEmployeeList()), view.addWorkingTimes());
				break;
			// if the user selects the show view working times option then run the view working times function
			case 7: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: VIEW WORKING TIMES");
				viewWorkingTimes(view.showEmployeeList(utilities.getEmployeeList()));
				break;
			// if the user selects the show worker availability option then run the show worker availability function
			case 8: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: SHOW WORKER AVAILABILITY");
				showWorkerAvailability(view.showEmployeeList(utilities.getEmployeeList()));
				break;
			// if the user selects the add employee option then run the add employee function
			case 9: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD EMPLOYEE");
				addEmployee(view.addEmployee(), (Owner)activeUser);
				break;
			// if the user selects the edit availabilities option then run the edit availabilities function
			case 10: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD NEW BOOKING");
				addNewBookings(view.addNewBooking("owner"));
				break;
			// if the user selects the edit availabilities option then run the edit availabilities function
			case 11: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: EDIT AVAILABILITIES");
				//gets the employee id from the user
				editAvailability(view.showEmployeeList(utilities.getEmployeeList()), (Owner)activeUser, view.addAvailableTimes());
				break;
			case 12: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: ADD SERVICES");
				//gets the employee id from the user
				addServices(view.getNewServices());
				break;
		// if the user selects the logout option
			case 13: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: LOGOUT");
				//run the logout view function and break the loop terminating the program
				Boolean b = view.logout();
				if (b) {
					activeUser = null;
					currentPerms = defaultPerms;
				}
				break;
			case 14: breakLoop = true;
				break;
			//if the user selects an invalid option the print an error message to the view and go through the loop again
			default: LOGGER.log(Level.FINE, "INVALID MENU OPTION CHOSEN");
				view.failure("Sorry you have provided an invalid option! Please try again", "");
				break;
			}
			
		} */
	}
	
	private void addServices(String[][] services) {
		for(String[] s : services) {
				new Service(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), true);
			
		}
	}
	/*
	 * Array Guide: 
	 * 0 = list of utilities
	 * 1 = start time
	 * 2 = User name
	 * 3 = employeeId
	 * 
	 */
	private void addNewBookings(String[] bookingData) {
		ArrayList<Service> servs;
		servs = Service.stringOfServicesToArrayList(bookingData[0]);
		utilities.addNewBooking(new String[]{bookingData[1], Integer.toString(Integer.parseInt(bookingData[1])+Service.getTotalArrayDuration(servs)), bookingData[2], bookingData[3], bookingData[0]});
		
	}
	/**
	 * The login function receives an array of strings and then authenticates the username and password
	 * returns null if the user authenticates or the user if they exist
	 * @param loginDetails [0] username, [1] password
	 * @return the user if the authentication passes
	 */
	public User login(String[] loginDetails) {
		LOGGER.log(Level.FINE, "LOGIN: Login details: " + Arrays.toString(loginDetails));
		//Search for the user in the arrayList and make sure the password is correct
		User user = utilities.authenticate(loginDetails[0], loginDetails[1]);
		
		//check if the user is authenticated or not
		if (user == null) {
			//if it fails then alert the user
			LOGGER.log(Level.FINE, "LOGIN: Failed");
			view.failure("Login", "Incorrect Username/Password");
		} else {
			//if it succeeds then inform the user
			LOGGER.log(Level.FINE, "LOGIN: Success");
			view.success("Login", "Welcome back, " + user.getUsername());

			this.activeUser = user;
			
		}
		//return the user value
		return user;
	}
	
	/**
	 * This method validates the user input and then if it passes creates a new user
	 * providing that there is no other user with the same username
	 * @param userDetails [0] username, [1] password, [2] name, [3] address, [4] mobile number
	 * @return returns the user created or null it it fails validation
	 * @throws ValidationException 
	 */
	public User register(String[] userDetails) throws ValidationException
	{
		//validate all the user input to match the regular expression
		LOGGER.log(Level.FINE, "REGISTER: Registration details: " + Arrays.toString(userDetails));
		if(!utilities.validate(userDetails[0], "[A-Za-z0-9]+"))
  		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username does not match regex");
			view.failure("Register", "Username is not Valid");
			throw new ValidationException("Username only contains letters and numbers!");
		}
		if(!userDetails[1].equals(userDetails[2]))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, Passwords dont match");
			view.failure("Register", "Passwords dont match");
			throw new ValidationException("Passwords do not match!");
		}
		if(!utilities.validate(userDetails[3], "[A-Za-z]+"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, Name does not match regex");
			view.failure("Register", "Name is not Valid");
			throw new ValidationException("Name must only contain letters!");
		}
		if(!utilities.validate(userDetails[4], "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, Address does not match regex");
			view.failure("Register", "Address is not Valid");
			throw new ValidationException("Address is not valid!");
		}
		if(!utilities.validate(userDetails[5], "\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}"))
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, phone number does not match regex");
			view.failure("Register", "Phone Number is not Valid");
			throw new ValidationException("Mobile number is not valid!");
		}
		// if they all pass then try to create a new user and return the created user
		if (utilities.addCustomerToDatabase(userDetails[0].toLowerCase(), userDetails[1], userDetails[2], userDetails[3], userDetails[4])) {
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			view.success("Register", userDetails[0] + " added to system");
			return utilities.searchUser(userDetails[0]);
		}
		else //if not then inform the user that there is a user with that name and return null
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username already taken");
			view.failure("Register", "The entered username is already in the database");
			throw new ValidationException("Username already exists in the system!");
		}
				
	}
	
	/**
	 * This method shows the list of all future bookings in the system
	 */
	private void viewCurrentBookings() 
	{
		//gets the bookings list of all the booking from the time the method is called
		Booking[] bookings = utilities.getBookingsAfter(new Date(Calendar.getInstance().getTimeInMillis()));
		
		//if there are no bookings in the future then alert the user and exit function
		if (bookings.length == 0) {
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
			view.failure("View Booking Summaries", "No future bookings");
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the list of bookings
			for (int i = 0; i < bookings.length; i++) {
				bookingsStringArray[i] = bookings[i].toStringArray();
			}
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			view.viewBookings(bookingsStringArray);
		}
	}
	
	/* TODO requires javadoc */
	public void addNewBookings(String username, String startTime, String listOfServices, String employeeId) {
		ArrayList<Service> servs;
		servs = Service.stringOfServicesToArrayList(listOfServices);
		utilities.addNewBooking(new String[]{startTime, (startTime+Service.getTotalArrayDuration(servs)), username, employeeId, listOfServices});
	}
	
	/**
	 * This method gets all employees timetables and merges them all together to form the business hours
	 * and outputs it to the view
	 */
	private void viewAvailableTimes() {
		//get the available timetable
		Timetable t = utilities.getAvailableBookingTimes();
		if (t != null && t.getAllPeriods().length != 0) {
			String[][] sa = t.toStringArray();
			
			for (int i = 0; i < sa.length; i++) {
				sa[i][0] = Long.toString(Long.parseLong(sa[i][0]));
				sa[i][1] = Long.toString(Long.parseLong(sa[i][1]));
			}
			
			view.printTable(sa,new String[]{"Start Period", "End Period"},"Available Booking Times", false,false,"There are no available times to display.");
		} else {
			view.failure("View Available Times", "No available times");
		}
//		//if there is no timetable then alert the user and end the function
//		if (ConcatenatedTimetable == null) {
//			LOGGER.warning("No employees registered in the system");
//			view.failure("View Available Times", "No Employees registered in the system with available times");
//			return;
//		}
//		//if it isnt null then create a 2d array of the timetable and send it to the view to show to the user
//		String[][] s = ConcatenatedTimetable.toStringArray();
//		view.viewBookingAvailability(s);
	}
	
	
	
	/**
	 * This method shows the list of all bookings in the system
	 */
	private void viewSummaryOfBookings() {

		//gets the bookings list of all the booking from the time the method is called
		Booking[] bookings = utilities.getBookingsAfter(new Date(0));
		
		//if there are no bookings in the future then alert the user and exit function
		if (bookings.length == 0) {
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
			view.failure("View Booking Summaries", "No future bookings");
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the list of bookings
			for (int i = 0; i < bookings.length; i++) {
				bookingsStringArray[i] = bookings[i].toStringArray();
			}
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			view.viewBookings(bookingsStringArray);
		}
	}
	
	/**
	 * 
	 * @param workingTimes [0][0] employee ID  [0][1] Name [0][2] start [0][3] end
	 */
	private void addWorkingTimes(String employeeId, String[] workingTimes) {
		String[][] a = utilities.getEmployeeAvailability(employeeId).toStringArray();
		if (a.length != 0) {
			view.showTimetable(a);
		} else {
		 	view.failure("Add Working Times: ", "Employee has no available working times");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = sdf.parse(workingTimes[0]);
			Long starttime = date.getTime() + (Period.convert24HrTimeToDaySeconds(workingTimes[1]) * 1000);
			Long endtime = date.getTime() + (Period.convert24HrTimeToDaySeconds(workingTimes[2]) * 1000);
			if (starttime > endtime) {
				view.failure("Add Working Times", "Error, shift could not be added");
			}
			else if (SQLiteConnection.addShift(Integer.parseInt(employeeId), "SARJ's Milk Business", Long.toString(starttime/1000), Long.toString(endtime/1000))) {
				view.success("Add Working Times", "Shift successfully added");
			} else {
				view.failure("Add Working Times", "Error, shift could not be added");
			}
			
		} catch (ParseException e) {
			view.failure("Add Working Times", "incompatible date/time format");
			LOGGER.log(Level.WARNING, e.getMessage());
			return;
		}
				
	}
	
	/**
	 * this method gets the working times of an employee and outputs it to the user if it exists
	 */
	private void viewWorkingTimes(String employeeId)
	{
		//get the list of shifts for the employee
		Timetable t = utilities.getShift(employeeId);
		
		//if there are working times
		if (t != null) {
			//convert it to a 2d string array and create a second list skeleton minus the employee ID
			String[][] sa = t.toStringArray();
			String[][] shifts = new String [sa.length][2];
			
			//loop through the array convert all the start and end times to Unix time stamps
			for (int i = 0; i < sa.length; i++) {
				sa[i][0] = Long.toString(Long.parseLong(sa[i][0]));
				sa[i][1] = Long.toString(Long.parseLong(sa[i][1]));
			}
			
			//copy the details to the second array minus the id for each shift
			for(int i = 0; i < shifts.length; i++)
			{
				shifts[i][0] = sa[i][0];
				shifts[i][1] = sa[i][1];
			}
			
			//show the working times to the user
			view.printTable(shifts,new String[]{"Start Period", "End Period"},"Working Times", false,false,"There are no shifts to display.");
		} else {
			//if the working times don't exist then alert the user and return to the menu.
			view.failure("View Working Times", "No shifts available");
		}
		
//		//get the array of all working times
//		
//		String [][] workingTimes = utilities.getWorkingTimes();
//		
//		//check to see if there are working times
//		if(workingTimes.length == 0)
//		{
//			//if there are not then inform the user and end the function
//			view.failure("View Working Times", "There are no working times available");
//			return;
//		}
//		
//		//else then show the working times
//		view.viewWorkingTimes(workingTimes);	
	}
	
	/**
	 * This method allows the user to edit an employees availability
	 * @param employeeId a employee id taken from the users input
	 * @param user the business owner passed to the function
	 */
	private void editAvailability(String employeeId, Owner user, ArrayList<String> availabilities) {

		Timetable t = new Timetable();

		try {
			//if the employee selected doesn't exist alert the user and exit the function
			if(employeeId == null || employeeId.equals("")) {
				LOGGER.log(Level.FINE, "EDIT AVAILABILITIES: Failure, no such employee exists");
				return;
			}
			
		} catch(Exception e) {
			LOGGER.warning(e.getMessage());
		}
		//use an iterator to go through the availabilities
		Iterator<String> iter = availabilities.iterator();
		//go through the the iterator to split the availabilities
		while(iter.hasNext()) {
			//split the string to an array
			String[] start = iter.next().split(" ");
			
			if (!iter.hasNext()) {
				break; // weird issue with console where we're missing an end period, just cancel here
			}
			
			//splits and outputs the end time into an array
			String end = iter.next();
			//if there is more than one start time then go to the next iteration
			if (start.length != 2) {
				continue;
			}
			
			//start creating the new timetable
			String weekday = start[0];
			//check if the time is a valid day of the week
			if (!Period.checkIsValidWeekday(weekday)) {
				continue;
			}
			//create the start and end times for the period of availability
			String starttime = Integer.toString(Period.convertDayToSeconds(weekday) + Period.convert24HrTimeToDaySeconds(start[1]));
			String endtime = Integer.toString(Period.convertDayToSeconds(weekday) + Period.convert24HrTimeToDaySeconds(end));
			//add it to the timetable
			t.addPeriod(new Period(starttime, endtime, false));
		}
		//if the employee doesn't exit then alert the user and exit the function
		if (employeeId.equals("")) {

			LOGGER.log(Level.FINE, "EDIT AVAILABILITIES: Failure, no such employee exists");
			return;
		}
		//add the availabilities to the timetable
		/* TODO turn this try block below into a utility method */
		try {
			ResultSet rs = SQLiteConnection.getAllAvailabilities();
			int id = SQLiteConnection.getNextAvailableId(rs, "timetableId");
			if (SQLiteConnection.createAvailability(id, user.getBusinessName(), t.toString())){
				
			}
			else {
				SQLiteConnection.deleteAvailabilities(id, user.getBusinessName());
				SQLiteConnection.createAvailability(id, user.getBusinessName(), t.toString());
			};
			SQLiteConnection.updateAvailabilityforEmployee(Integer.parseInt(employeeId), id);
			rs.close();
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		 
	}
	
	/**
	 * This method shows the availability for a particular employee
	 */
	private void showWorkerAvailability(String employeeId) {
		try {
			//get the employee ID of the selected employee to view their availability
			//go through a loop till the user chooses to exit to the menu
			if (employeeId != null && !employeeId.equals("")) {
				//get the employees timetable
				Timetable t = utilities.getEmployeeAvailability(employeeId);
				//if the employee doesn't have a timetable then alert the user and exit
				if (t.equals(null) || t.getAllPeriods().length == 0) {
					view.failure("View worker availability", "This worker has no available times");
				} else {
					//else show the timetable to the user
					view.showTimetable(t.toStringArray());
				}
				//halt till the user exits or chooses another employee
			}
			
		} catch(Exception e) {
			//log any exceptions created
			LOGGER.warning(e.getMessage());
		}

	}

	/**
	 * This method adds an employee to the database
	 * @param newEmployee [0] name, [1] phone number, [2] address. 
	 * @param user the active user which is an owner
	 * @return true if it succeeds or false if it fails
	 */
	boolean addEmployee(String[] newEmployee, Owner user) 
	{
		//set variables that are used for checking and creating the new employee
		String name = newEmployee[0];
		String phonenumber = newEmployee[1];
		String address = newEmployee[2];
		String business = user.getBusinessName();
		String id = "";
		//create a unique ID for the new employee
		
		/* TODO Turn try block into utility method */
		try {
			ResultSet rs = SQLiteConnection.getAllEmployees();
			int i = SQLiteConnection.getNextAvailableId(rs, "employeeId");
			rs.close();
			id = Integer.toString(i);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		

		//validate all the user inputs
		/* These need to also be in gui */
		if(!utilities.validate(name, "[A-Za-z]+"))
  		{
			view.failure("Add Employee", "Name is not Valid");
			return false;
		}
		
		if(!utilities.validate(phonenumber, "\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}"))
		{
			view.failure("Add Employee", "Phone number is not Valid");
			return false;
		}
		
		if(!utilities.validate(address, "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
		{
			view.failure("Add Employee", "Address is not Valid");
			return false;
		}
		
		//try to ad the new employee to the database
		if (utilities.addEmployeeToDatabase(id, business, name, address, phonenumber, 0))
		{ /* TODO add cases for staff and owners */
			//if it works then add tell the user and return true
			view.success("Add Employee", name + " was successfully added to the database");
			return true;
		}
		else
		{
			//if it fails then alert the user and return false
			view.failure("Add Employee", "SQL Error");
			return false;
		}
	}
}
