package controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import model.booking.Booking;
import model.services.Services;
import model.timetable.Timetable;
import model.users.User;
import view.console.Console;

public class Controller {
	private Logger LOGGER = Logger.getLogger(Controller.class.getName(), Controller.class);
	
	private Console view = new Console();
	private Services services = new Services();

	private User activeUser;
	
	//private boolean debugMode = false;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false};
	
	public Controller() {
		
	}
	
	public void run()
	{
		//initialize view
		Console view = new Console();

		boolean breakLoop = false; // exit program case
		//debugMode = true; // remove this line while demoing
		boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
		activeUser = null;
		while(!breakLoop) {
			if (activeUser != null) 
			{
				LOGGER.log(Level.FINE, "Active user permissions : " + Arrays.toString(activeUser.getPermissions()));
			}
			int option = view.displayOptions(currentPerms);
			switch(option) {
			
			case 0: LOGGER.log(Level.FINE, "MENU OPTION CHOSEN: LOGIN");
			activeUser = login(view.login());
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
		User user = services.authenticate(loginDetails[0], loginDetails[1]);

		if (user == null) {
			LOGGER.log(Level.FINE, "LOGIN: Failed");
			view.failure("Login", "Incorrect Username/Password");
		} else {
			LOGGER.log(Level.FINE, "LOGIN: Success");
			view.success("Login", "Welcome back, " + user.getUsername());
		}
		
		return user;
	}
	
	protected User register(String[] userDetails)
	{
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
		if(!services.validate(userDetails[3], "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+"))
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
		
//		if (SQLiteConnection.createCustomer(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) {
		if (services.addCustomerToDatabase(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) {
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			return services.searchUser(userDetails[0]);
		}
		else
		{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username already taken");
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
		Timetable ConcatenatedTimetable = services.getAvailableTimes();
		if (ConcatenatedTimetable.equals(null)) {
			LOGGER.log(Level.WARNING, "No employees registered in the system");
			view.failure("View Available Times", "No Employees registered in the system with available times");
		}
		System.out.println(ConcatenatedTimetable.toString());
		String[][] s = ConcatenatedTimetable.toStringArray();
		view.viewBookingAvailability(s);
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {
		try {
			Booking[] bookings = services.getBookingsAfter(Calendar.getInstance().getTime());

			if (bookings.length == 0) {
				LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
				view.failure("View Booking Summaries", "No future bookings");
			} else {
				String[][] bookingsStringArray = new String[bookings.length][];
				
				for (int i = 0; i < bookings.length; i++) {
					bookingsStringArray[i] = bookings[i].toStringArray();
					System.out.println(bookingsStringArray[i]);
				}
				LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
				view.viewBookings(bookingsStringArray);
			}
		} catch (Exception e) {
			
		}
	}
	
	private void addWorkingTimes(String[][] workingTimes) {
		
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
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	//Need to add logging for this once it is completed
	private boolean addEmployee(String[] newEmployee) 
	{
		String name = newEmployee[0];
		String phonenumber = newEmployee[1];
		String address = newEmployee[2];
		String id = newEmployee[3];
		
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
