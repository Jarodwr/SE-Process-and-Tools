package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.employee.Employee;
import model.exceptions.ValidationException;
import model.period.Booking;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Customer;
import model.users.User;
import model.utility.Utility;

/**
 * The controller class is the interface between our view and model classes
 * Only superficial business logic like field testing
 */
public class Controller {
	private Logger LOGGER = Logger.getLogger("main");
	public Utility utilities;
	private static final String nameCheckerRegEx = "[A-Za-z -']+";
	
	public static final String[] Weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	public Controller() {
		utilities = new Utility();
	}
	
	public Controller(String dbName) {
		utilities = new Utility(dbName);
	}
	
	/**
	 * Add a service for the current business
	 * @param name	title of the service
	 * @param priceInCents	price in cents of the service eg. 1000 = $10.00
	 * @param duration duration in minutes eg. 90 = 1 hour 30 minutes
	 * @return	Whether or not the service was added
	 */
	public boolean addService(String name, int priceInCents, String duration) {
		return utilities.addService(name, priceInCents, duration);
	}

	/**
	 * The login function receives an array of strings and then authenticates the username and password
	 * returns null if the user authenticates or the user if they exist
	 * @param loginDetails [0] username, [1] password
	 * @return the user if the authentication passes
	 */
	public User login(String username, String password) {
		LOGGER.log(Level.INFO, "LOGIN: Login details: Username - " + username + ", Password - " + password);
		//Search for the user in the arrayList and make sure the password is correct
		User user = utilities.authenticate(username, password);

		if (user == null) {
			LOGGER.log(Level.INFO, "LOGIN: Failed");
		}
		else
		{
			LOGGER.log(Level.INFO, "LOGIN: Success");
		}

		return user;
	}
	
	/**
	 * This method validates the user input and then if it passes creates a new user
	 * providing that there is no other user with the same username
	 * @param userDetails [0] username, [1] password, [2] name, [3] address, [4] mobile number
	 * @return returns the user created or null it it fails validation
	 * @throws ValidationException 
	 */
	public User register(String username, String password, String business, String passwordConfirmation, String name, String address, String phoneno) throws ValidationException {
		
		//validate all the user input to match the regular expression
		
		LOGGER.log(Level.INFO, "REGISTER: Registration details: "
				+ "Username - " + username + 
				", Password - " + password + 
				", Name - " + name + 
				", Address - " + address + 
				", Phone Number - " + phoneno);
		
		if(!username.matches("[A-Za-z0-9]+"))	{
			LOGGER.log(Level.INFO, "REGISTER: Failure, username does not match regex");
			throw new ValidationException("Username only contains letters and numbers!");
		}
		
		if(!password.equals(passwordConfirmation)) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Passwords dont match");
			throw new ValidationException("Passwords do not match!");
		}
		
		if(!name.matches(nameCheckerRegEx)) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Name does not match regex");
			throw new ValidationException("Name must only contain letters!");
		}
		
		if(!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Address does not match regex");
			throw new ValidationException("Address is not valid!");
		}
		
		if(!phoneno.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, phone number does not match regex");
			throw new ValidationException("Mobile number is not valid!");
		}
		
		// if they all pass then try to create a new user and return the created user
		if (utilities.addCustomerToDatabase(username.toLowerCase(), password, business, name, address, phoneno)) {
			LOGGER.log(Level.INFO, "REGISTER: Success, user added to system");
			return utilities.searchUser(username);
		} else {
			//	inform the user that there is a user with that name and return null
			LOGGER.log(Level.INFO, "REGISTER: Failure, username already taken");
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
			LOGGER.log(Level.INFO, "VIEW SUMMARY OF BOOKINGS: failure, no bookings in database in the future");
			return null;
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the array of bookings
			for (int i = 0; i < bookings.length; i++)
				bookingsStringArray[i] = bookings[i].toStringArray();
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.INFO, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			return bookingsStringArray;
		}
	}
	
	/**
	 * Adding a new booking
	 * @param username	username of the user which wishes to make the booking
	 * @param startTime	start time of the booking
	 * @param listOfServices	String of services separated by ":"
	 * @param employeeId	id of the employee booked for the booking
	 * @return	whether or not the booking was added
	 */
	public boolean addNewBooking(String username, String startTime, String listOfServices, String employeeId) {
		ArrayList<Service> servs;
		servs = utilities.stringOfServicesToArrayList(listOfServices);
		LOGGER.log(Level.FINE, "User has requested to add the following booking: " + username + "|" + startTime + "|" + listOfServices + "|" + employeeId);
		return utilities.addNewBooking(username, employeeId, startTime, Long.toString(Long.parseLong(startTime) + (Service.getTotalArrayDuration(servs)/30) * 1800), listOfServices);
	}
	
	/**
	 * Getter for a summary of all bookings
	 * @return	String[][] of all bookings, first dimension is the booking, second is the details
	 */
	public String[][] getSummaryOfBookings() {

		//gets the bookings list of all the booking from the time the method is called
		Booking[] bookings = utilities.getBookingsAfter(new Date(0));
		
		//if there are no bookings in the future then alert the user and exit function
		if (bookings == null || bookings.length == 0) {
			LOGGER.log(Level.INFO, "VIEW SUMMARY OF BOOKINGS: failure, no bookings in database in the future");
			return null;
		} else {
			//create a 2d array to copy the booking details from the list of bookings
			String[][] bookingsStringArray = new String[bookings.length][];
			
			//copy all details from the list of bookings
			for (int i = 0; i < bookings.length; i++)
				bookingsStringArray[i] = bookings[i].toStringArray();
			
			//send the list of all future bookings to the view to display
			LOGGER.log(Level.INFO, "VIEW SUMMARY OF BOOKINGS: Success, " + bookingsStringArray.length + " bookings are displayed");
			return bookingsStringArray;
		}
	}
	
	/**
	 * Remove a booking
	 * @param id	id of the booking
	 * TODO: remove the business name parameter, use this.currentBusiness in utility
	 * @param businessname	name of the business we need to remove the booking of
	 * @return	Whether or not the booking was removed
	 */
	public boolean removeBooking(int id)
	{
		return utilities.removeBooking(id);
	}
	
	/**
	 * Adding working times to an employee
	 * @param employeeId	id of the employee
	 * @param rawDate	date of the working time
	 * @param startTime	start time in seconds from the start of the day
	 * @param endTime	end time in seconds from the start of the day
	 * @return whether or not the working times are added
	 */
	public boolean addWorkingTime(String employeeId, String rawDate, String startTime, String endTime) {
		return utilities.addShift(employeeId, rawDate, startTime, endTime);
	
	}

	/**
	 * Removing a working shift for an employee
	 * @param employeeId	id of the employee
	 * @param rawDate	date of the working time
	 * @param startTime	start time in seconds from the start of the day
	 * @param endTime	end time in seconds from the start of the day
	 * @return whether or not the working times are removed
	 */
	public boolean removeWorkingTime(String employeeId, String rawDate, String startTime, String endTime) {
		return utilities.removeShift(employeeId, rawDate, startTime, endTime);
	
	}
	
	/**
	 * Getter for an employee's working times
	 * @param employeeId id of the requested employee
	 * @return Timetable formatted as String[][]
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
		utilities.editAvailability(employeeId, availabilities);
	}
	
	/**
	 * getter for worker availability for a given employee
	 * @param employeeId	requested employee's id
	 * @return	Timetable of the worker's availability
	 */
	public Timetable getWorkerAvailabilityTimetable(String employeeId) {
		try {
			//get the employee ID of the selected employee to view their availability
			//go through a loop till the user chooses to exit to the menu
			if (employeeId != null && !employeeId.equals("")) {
				//get the employees timetable
				return utilities.getEmployeeAvailability(employeeId);
			}
			
		} catch(Exception e) {
			//log any exceptions created
			LOGGER.warning(e.getMessage());
		}
		
		return new Timetable();

	}

//	Move the logic and checking into model
	/**
	 * This method adds an employee to the database
	 * @param newEmployee [0] name, [1] phone number, [2] address. 
	 * @param user the active user which is an owner
	 * @return true if it succeeds or false if it fails
	 */
	public boolean addEmployee(String name, String phone, String address) 
	{
		String id = "";
		//create a unique ID for the new employee
		
		/* TODO Turn try block into utility method */
		Employee[] employees = utilities.getAllEmployees();
		int nextId = -1;
		for (Employee e : employees) {
			if (Integer.parseInt(e.getEmployeeId()) > nextId) {
				nextId = Integer.parseInt(e.getEmployeeId());
			}
		}
		if (nextId == -1) {
			nextId = 0;
		}

		id = Integer.toString(nextId);
		
		//validate all the user inputs
		if(name.matches(nameCheckerRegEx) &&
				phone.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
				address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			if (utilities.addNewEmployee(id, name, address, phone, 0)) { //try to add the new employee to the database
				LOGGER.log(Level.INFO, "Employee successfully added");
				return true;
			}
		}

		LOGGER.log(Level.INFO, "Employee could not be added, incorrect field formats");
		return false;
		
	}
	
	/**
	 * getter for list of employees in the current business
	 * @return	String formatted list of employees
	 */
	public String[] getEmployeeList() {
		Employee[] eList = utilities.getAllEmployees();
		if (eList == null) {
			return null;
		}
		String[] employees = new String[eList.length];
		
		for (int i = 0; i < eList.length; i++)
			employees[i] = eList[i].getEmployeeId() + ":" + eList[i].getUsername();
		
		return employees;
	}
	
	/**
	 * Getter for list of customers in current business
	 * @return	String formatted customer list
	 */
	public String[] getCustomerList() {
		Customer[] cList = utilities.getAllCustomers();
		if (cList != null) {
			String[] customers = new String[cList.length];
			
			for (int i = 0; i < cList.length; i++)
				customers[i] = cList[i].getUsername() + ":" + cList[i].getName();
			
			return customers;
		} else {
			return new String[0];
		}


	}

	/**
	 * Getter for list of services in current business
	 * @return	String formatted services list
	 */
	public String[] getServicesList() {
		Service[] sList = utilities.getAllServices();
		if (sList == null) {
			return null;
		}
		String[] services = new String[sList.length];
		
		for (int i = 0; i < sList.length; i++)
			services[i] = sList[i].toString();
		
		return services;
	}
	
	/**
	 * Getter for employee booking availability
	 * TODO: Potentially a duplicate function
	 * @param employeeId	employee in focus
	 * @param date	day being observed
	 * @return	Timetable in string[][] format
	 */
	public String[][] getEmployeeBookingAvailability(String employeeId, Date date) {
		Timetable bookingAvailability = utilities.getEmployeeBookingAvailability(employeeId, date);
		if (bookingAvailability != null) {
			return bookingAvailability.toStringArray();
		}
		return null;
	}

	/**
	 * Getter for opening hours for the current business
	 * TODO: don't need current business parameter, use this.currentBusiness in utility
	 * @param currentBusiness
	 * @return	Timetable of the opening hours
	 */
	public Timetable getOpeningHours() {
		return utilities.getOpeningHours();
	}

	public String[] getBusinessList() {
		return utilities.getBusinessList();
		
	}

	public void registerBusiness(String businessName, String address, String phoneNumber) throws ValidationException {
		if(!businessName.matches(nameCheckerRegEx)) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Business name does not match regex Error 1");
			throw new ValidationException("Name must only contain letters!");
		}
		
		if(!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Business address does not match regex");
			throw new ValidationException("Address is not valid!");
		}
		
		if(!phoneNumber.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			LOGGER.log(Level.INFO, "REGISTER: Failure, Business phone number does not match regex");
			throw new ValidationException("Mobile number is not valid!");
		}
		
		if (utilities.addBusinessToDatabase(businessName, address, phoneNumber)) {
			LOGGER.log(Level.INFO, "REGISTER: Success, business added to system");
			
		} else {
			//	inform the user that there is a user with that name and return null
			LOGGER.log(Level.INFO, "REGISTER: Failure, business name already taken");
			throw new ValidationException("Business already exists in the system!");
		}
		
	}

	public User registerOwner(String username, String password, String business, String name, String address,
			String phoneNumber, String passwordConfirmation) throws ValidationException {
		//validate all the user input to match the regular expression
		
				LOGGER.log(Level.INFO, "REGISTER: Registration details: "
						+ "Username - " + username + 
						", Password - " + password + 
						", Name - " + name + 
						", Address - " + address + 
						", Phone Number - " + phoneNumber);
				
				if(!username.matches("[A-Za-z0-9]+"))	{
					LOGGER.log(Level.INFO, "REGISTER: Failure, username does not match regex");
					throw new ValidationException("Username must only contain letters and numbers!");
				}
				
				
				if(!name.matches(nameCheckerRegEx)) {
					LOGGER.log(Level.INFO, "REGISTER: Failure, Name does not match regex");
					throw new ValidationException("Name must only contain letters Error 5!");
				}
				
				if(!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")) {
					LOGGER.log(Level.INFO, "REGISTER: Failure, Address does not match regex");
					throw new ValidationException("Address is not valid!");
				}
				
				if(!phoneNumber.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
					LOGGER.log(Level.INFO, "REGISTER: Failure, phone number does not match regex");
					throw new ValidationException("Mobile number is not valid!");
				}
				
				// if they all pass then try to create a new user and return the created user
				if (utilities.addOwnerToDatabase(username.toLowerCase(), password, business, name, address, phoneNumber)) {
					LOGGER.log(Level.INFO, "REGISTER: Success, user added to system");
					return utilities.searchUser(username);
				} else {
					//	inform the user that there is a user with that name and return null
					LOGGER.log(Level.INFO, "REGISTER: Failure, username already taken");
					throw new ValidationException("Username already exists in the system!");
				}
		// TODO Auto-generated method stub
		
	}

	public User login(String username, String password, String selectedBusiness) {
		LOGGER.log(Level.INFO, "LOGIN: Login details: Username - " + username + ", Password - " + password);
		//Search for the user in the arrayList and make sure the password is correct
		User user = utilities.authenticate(username, password, selectedBusiness);

		if (user == null) {
			LOGGER.log(Level.INFO, "LOGIN: Failed");
		} else {
			LOGGER.log(Level.INFO, "LOGIN: Success");
		}

		return user;
	}
}
