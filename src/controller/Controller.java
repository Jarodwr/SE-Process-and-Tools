package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.database.SQLiteConnection;
import model.employee.Employee;
import model.exceptions.ValidationException;
import model.period.Booking;
import model.period.Period;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Customer;
import model.users.Owner;
import model.users.User;
import model.utility.Utility;

/*
 * The controller class is the interface between our view and controller
 * Only superficial business logic like field testing
 */
public class Controller {
	private Logger LOGGER = Logger.getLogger("main");
	public Utility utilities = new Utility();
	
	public static final String[] Weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	public boolean addService(String name, int priceInCents, String duration) {
		
		return SQLiteConnection.addService(name, priceInCents, Integer.parseInt(duration), utilities.getCurrentBusiness());
	}

	/**
	 * The login function receives an array of strings and then authenticates the username and password
	 * returns null if the user authenticates or the user if they exist
	 * @param loginDetails [0] username, [1] password
	 * @return the user if the authentication passes
	 */
	public User login(String username, String password) {
		LOGGER.log(Level.FINE, "LOGIN: Login details: Username - " + username + ", Password - " + password);
		//Search for the user in the arrayList and make sure the password is correct
		User user = utilities.authenticate(username, password);
		
		if (user == null)
			LOGGER.log(Level.FINE, "LOGIN: Failed");
		else
			LOGGER.log(Level.FINE, "LOGIN: Success");

		return user;
	}
	
	/**
	 * This method validates the user input and then if it passes creates a new user
	 * providing that there is no other user with the same username
	 * @param userDetails [0] username, [1] password, [2] name, [3] address, [4] mobile number
	 * @return returns the user created or null it it fails validation
	 * @throws ValidationException 
	 */
	public User register(String username, String password, String passwordConfirmation, String name, String address, String phoneno) throws ValidationException {
		
		//validate all the user input to match the regular expression
		
		LOGGER.log(Level.FINE, "REGISTER: Registration details: "
				+ "Username - " + username + 
				", Password - " + password + 
				", Name - " + name + 
				", Address - " + address + 
				", Phone Number - " + phoneno);
		
		if(!username.matches("[A-Za-z0-9]+"))	{
			LOGGER.log(Level.FINE, "REGISTER: Failure, username does not match regex");
			throw new ValidationException("Username only contains letters and numbers!");
		}
		
		if(!password.equals(passwordConfirmation)) {
			LOGGER.log(Level.FINE, "REGISTER: Failure, Passwords dont match");
			throw new ValidationException("Passwords do not match!");
		}
		
		if(!name.matches("[A-Za-z]+")) {
			LOGGER.log(Level.FINE, "REGISTER: Failure, Name does not match regex");
			throw new ValidationException("Name must only contain letters!");
		}
		
		if(!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			LOGGER.log(Level.FINE, "REGISTER: Failure, Address does not match regex");
			throw new ValidationException("Address is not valid!");
		}
		
		if(!phoneno.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			LOGGER.log(Level.FINE, "REGISTER: Failure, phone number does not match regex");
			throw new ValidationException("Mobile number is not valid!");
		}
		
		// if they all pass then try to create a new user and return the created user
		if (utilities.addCustomerToDatabase(username.toLowerCase(), password, name, address, phoneno)) {
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			return utilities.searchUser(username);
		} else {
			//	inform the user that there is a user with that name and return null
			LOGGER.log(Level.FINE, "REGISTER: Failure, username already taken");
			throw new ValidationException("Username already exists in the system!");
		}
				
	}
	
	/**
	 * This method shows the list of all future bookings in the system
	 */
	public String[][] getCurrentBookings()
	{
		//gets the bookings list of all the booking from the time the method is called
		Booking[] bookings = utilities.getBookingsAfter(new Date(Calendar.getInstance().getTimeInMillis()));
		
		//if there are no bookings in the future then alert the user and exit function
		if (bookings == null || bookings.length == 0) {
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
			return null;
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the array of bookings
			for (int i = 0; i < bookings.length; i++)
				bookingsStringArray[i] = bookings[i].toStringArray();
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			return bookingsStringArray;
		}
	}
	
	/* TODO requires javadoc */
	public boolean addNewBooking(String username, String startTime, String listOfServices, String employeeId) {
		ArrayList<Service> servs;
		servs = Service.stringOfServicesToArrayList(listOfServices);
		return utilities.addNewBooking(username, employeeId, startTime, (startTime + Service.getTotalArrayDuration(servs)), listOfServices);
	}
	
	/**
	 * This method gets all employees timetables and merges them all together to form the business hours
	 * and outputs it to the view
	 */
	private String[][] getAvailableTimes() {
		//get the available timetable
		Timetable t = utilities.getAvailableBookingTimes();
		
		if (t != null && t.getAllPeriods().length != 0)
			return t.toStringArray();
		else
			return null;
	}
	
	
	
	/**
	 * This method shows the list of all bookings in the system
	 */
	public String[][] getSummaryOfBookings() {

		//gets the bookings list of all the booking from the time the method is called
		Booking[] bookings = utilities.getBookingsAfter(new Date(0));
		
		//if there are no bookings in the future then alert the user and exit function
		if (bookings.length == 0) {
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
			return null;
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the list of bookings
			for (int i = 0; i < bookings.length; i++)
				bookingsStringArray[i] = bookings[i].toStringArray();
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			return bookingsStringArray;
		}
	}
	
	public boolean removeBooking(int id, String businessname)
	{
		return utilities.removeBooking(id, businessname);
	}
	
	/**
	 * 
	 * @param workingTimes [0][0] employee ID  [0][1] Name [0][2] start [0][3] end
	 */
	//TODO: Need to expand this string array out
	//TODO: Fix millisecond conversion
	public boolean addWorkingTime(String employeeId, String rawDate, String startTime, String endTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = sdf.parse(rawDate);
			Long starttime = date.getTime() + (Period.convert24HrTimeToDaySeconds(startTime) * 1000);
			Long endtime = date.getTime() + (Period.convert24HrTimeToDaySeconds(endTime) * 1000);
			
			if (starttime > endtime)
				return false;
			else
				return (SQLiteConnection.addShift(Integer.parseInt(employeeId), "SARJ's Milk Business", 
						Long.toString(starttime/1000), Long.toString(endtime/1000)));
			
		} catch (ParseException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
				
	}
	
	/**
	 * this method gets the working times of an employee and outputs it to the user if it exists
	 */
	public String[][] getWorkingTimes(String employeeId)
	{
		//get the list of shifts for the employee
		Timetable t = utilities.getShift(employeeId);
		
		//if there are working times
		if (t != null) {
			String[][] shifts = t.toStringArray();
			
			//TODO: Convert to unix time stamps in model
			//loop through the array convert all the start and end times to Unix time stamps
			for (int i = 0; i < shifts.length; i++) {
				shifts[i][0] = Long.toString(Long.parseLong(shifts[i][0]));
				shifts[i][1] = Long.toString(Long.parseLong(shifts[i][1]));
			}
			
			return shifts;
			
		}
		
		return null;
		
	}
	
	/**
	 * This method allows the user to edit an employees availability
	 * @param employeeId a employee id taken from the users input
	 * @param user the business owner passed to the function
	 */
	public void editAvailability(String employeeId, ArrayList<String> availabilities) {

		Timetable t = new Timetable();


		//if the employee selected doesn't exist alert the user and exit the function
		if(employeeId == null || employeeId.equals("")) {
			LOGGER.log(Level.FINE, "EDIT AVAILABILITIES: Failure, no such employee exists");
			return;
		}

		//use an iterator to go through the availabilities
		Iterator<String> iter = availabilities.iterator();
		//go through the the iterator to split the availabilities
		while(iter.hasNext()) {
			String[] values = iter.next().split(" ");
			
			//start creating the new timetable
			//add it to the timetable
			t.addPeriod(new Period(values[0], values[1], false));
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
			int id;
			if (rs != null) {
				id = SQLiteConnection.getNextAvailableId(rs, "timetableId");
				rs.close();
			} 
			else {
				id = 0;
			}
			if (SQLiteConnection.createAvailability(id, utilities.getCurrentBusiness(), t.toString())){
				
			}
			else {
				SQLiteConnection.deleteAvailabilities(id, utilities.getCurrentBusiness());
				SQLiteConnection.createAvailability(id, utilities.getCurrentBusiness(), t.toString());
			}
			SQLiteConnection.updateAvailabilityforEmployee(Integer.parseInt(employeeId), id);
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		 
	}
	
	/**
	 * This method shows the availability for a particular employee
	 */
	public String[][] getWorkerAvailability(String employeeId) {
		try {
			//get the employee ID of the selected employee to view their availability
			//go through a loop till the user chooses to exit to the menu
			if (employeeId != null && !employeeId.equals("")) {
				//get the employees timetable
				Timetable t = utilities.getEmployeeAvailability(employeeId);
				if (t.equals(null) || t.getAllPeriods().length == 0)
					return null;
				else
					return t.toStringArray();
			}
			
		} catch(Exception e) {
			//log any exceptions created
			LOGGER.warning(e.getMessage());
		}
		
		return null;

	}

//	Move the logic and checking into model
	/**
	 * This method adds an employee to the database
	 * @param newEmployee [0] name, [1] phone number, [2] address. 
	 * @param user the active user which is an owner
	 * @return true if it succeeds or false if it fails
	 */
	public boolean addEmployee(String name, String phone, String address, Owner user) 
	{
		//set variables that are used for checking and creating the new employee
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

		if(name.matches("[A-Za-z -']+") &&
				phone.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
				address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+"))
			return utilities.addNewEmployee(id, business, name, address, phone, 0); //try to add the new employee to the database
		return false;
		
	}
	
	public Owner getOwner() {
		return utilities.getBusinessOwner();
	}
	
	public String[] getEmployeeList() {
		Employee[] eList = utilities.getAllEmployees();
		String[] employees = new String[eList.length];
		
		for (int i = 0; i < eList.length; i++)
			employees[i] = eList[i].getEmployeeId() + ":" + eList[i].getUsername();
		
		return employees;
	}
	public String[] getCustomerList() {
		Customer[] eList = utilities.getAllCustomers();
		String[] employees = new String[eList.length];
		
		for (int i = 0; i < eList.length; i++)
			employees[i] = eList[i].getUsername() + ":" + eList[i].getName();
		
		return employees;

	}

	public String[] getServicesList() {
		Service[] sList = utilities.getAllServices();
		String[] services = new String[sList.length];
		
		for (int i = 0; i < sList.length; i++)
			services[i] = sList[i].toString();
		
		return services;
	}
	
	/**
	 * 
	 * @param employeeId	employee in focus
	 * @param date	day being observed
	 * @return	Timetable in string[][] format
	 */
	public String[][] getEmployeeBookingAvailability(String employeeId, Date date) {
		//gets the bookings list of all the booking from the time the method is called
		Timetable shiftsTimetable = utilities.getShift(employeeId);
		Booking[] bookings = utilities.getBookingsAfter(date);
		
		if (shiftsTimetable != null) {
			if (bookings != null && bookings.length > 0) {
				for (Booking b : bookings) {
					if (b.getEmployeeId().equals(employeeId))
						shiftsTimetable.removePeriod(b);
				}
			}
			return shiftsTimetable.toStringArray();
		}	
		return null;
	}
}
