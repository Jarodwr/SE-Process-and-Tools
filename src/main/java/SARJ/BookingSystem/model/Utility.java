package SARJ.BookingSystem.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import SARJ.BookingSystem.model.database.SQLMaster;
import SARJ.BookingSystem.model.database.SQLiteConnection;
import SARJ.BookingSystem.model.period.Booking;
import SARJ.BookingSystem.model.period.Period;
import SARJ.BookingSystem.model.period.Shift;
import SARJ.BookingSystem.model.users.Admin;
import SARJ.BookingSystem.model.users.Customer;
import SARJ.BookingSystem.model.users.Owner;
import SARJ.BookingSystem.model.users.User;

/**
 * A services class, used for interfacing between the controller and the database.
 */
public class Utility {

	private Logger LOGGER = Logger.getLogger("main");
	private User currentUser;
	private int currentBusiness;
	private SQLiteConnection db;
	private SQLMaster masterDB;
	private String masterDBName;
	
	public Utility() {
		masterDBName = "BookingSystemMasterDB";
		masterDB = new SQLMaster("BookingSystemMasterDB");
	}
	
	public Utility(String dbname) {
		masterDBName = dbname;
		masterDB = new SQLMaster(masterDBName);
	}
	
	/**
	 * Get a user
	 * @param username username of the user requested
	 * @return returns the user if it exists, null if there is no user.
	 */
	public User searchUser(String username) {
		ResultSet rs;
		try {
			rs = db.getUserRow(username);
			if (username.equals("admin")) {
				Admin admin = new Admin(rs.getString("password"));
				LOGGER.warning("success in finding admin, password = " + rs.getString("password"));
				rs.close();
				return admin;
			}
			else {
				rs = db.getUserRow(username);
				
				Customer customer = new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return customer;
			}

		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
		
	}
	
	public User searchUserLogin(String username, String businessname) {
		ResultSet rs;
		if (username.equals("admin")) {
			Admin admin = new Admin("1234");
			return admin;
		}
		try {
				rs = masterDB.getOwnerRow(username);
				Owner owner = new Owner(username, rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("phonenumber"));
				LOGGER.warning("success in finding admin, password = " + rs.getString("password"));
				rs.close();
				return owner;
			}
			catch (Exception e) {
		
			}
		try {
			ResultSet br = masterDB.getBusinessRow(businessname);
			if (br != null) {
				SQLiteConnection tempConn = new SQLiteConnection(masterDBName + "_" + br.getString("businessid"));
				rs = tempConn.getUserRow(username);
				if (username.equals("admin")) {
					Admin admin = new Admin(rs.getString("password"));
					LOGGER.warning("success in finding admin, password = " + rs.getString("password"));
					rs.close();
					return admin;
				}
				else {
					rs = tempConn.getUserRow(username);
					Customer customer = new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
					rs.close();
					return customer;
				}
			}

		} catch (Exception e) {
			//LOGGER.warning(e.getMessage()); Exceptions are intended behaviour here, no need to log
		}
		return null;
		
	}
	
	private void setBusinessDBConnection(int businessId) throws SQLException {
		if (db != null) {
			this.db.close();
		}
		this.db = new SQLiteConnection(masterDBName + "_" + Integer.toString(businessId));
		this.currentBusiness = businessId;
	}

	/**
	 * Checks that the username and password match
	 * @param username Requested user
	 * @param password Attempted password
	 * @return if the user exists with the entered password, return the user. Else return null.
	 */
	public User authenticate(String username, String password) {
		User found = searchUserLogin(username.toLowerCase(), "");
		if (found != null && found.checkPassword(password) && found.getUsername() != "admin") {
			try {
				ResultSet rs = masterDB.getOwnerRow(username.toLowerCase());
				if (rs == null) {
					return null;
				}
				int businessId = Integer.parseInt(rs.getString("businessid"));
//				String businessName = masterDB.getBusinessRow(businessId).getString("businessname");
				this.setBusinessDBConnection(businessId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return found;
		}
		else if (found != null && found.getUsername() == "admin" && found.checkPassword(password)) {
			return found;
		}
		return null;
	}
	
	/**
	 * Set the availability of an employee
	 * @param employeeId	employee being set
	 * @param availabilities	new availabilities
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
		try {
			ResultSet rs = db.getAllAvailabilities();
			int id;
			if (rs != null) {
				id = db.getNextAvailableId(rs, "timetableId");
				rs.close();
			} 
			else {
				id = 0;
			}
			if (db.createAvailability(id, t.toString())){
				
			}
			else {
				db.deleteAvailabilities(id);
				db.createAvailability(id, t.toString());
			}
			db.updateAvailabilityforEmployee(Integer.parseInt(employeeId), id);
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		 
	}
	
	/**
	 * Gets an array of bookings that start after the date specified
	 * @param date Searching all bookings after this date
	 * @return Booking[]
	 */
	public Booking[] getBookingsAfter(Date date) {
		try {
			ResultSet rs = db.getBookingsByPeriodStart(date.getTime());
			if (rs != null)  {
				return bookingResultsetToArray(rs);
			}
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Adds a new booking to the system, yet to be implemented till part B
	 * @param booking
	 * @return 
	 */
	public boolean addNewBooking(String customerUsername, String employeeId, String start, String end, String services) {

		if (stringOfServicesToArrayList(services).size() == 0) {
			return false;
		}
		
		for (Customer c : getAllCustomers()) {
			if (c.getUsername().equals(customerUsername)) {
				Timetable t = getEmployeeBookingAvailability(employeeId, new Date(Long.parseLong(start)));	//All employee shifts
				if (t != null) {
					for (Period p : t.getAllPeriods()) {
						long allowable = (p.getEnd().getTime() - p.getStart().getTime()) - (Long.parseLong(end) - Long.parseLong(start));	//Max allowable time from the period start
						
						if (Long.parseLong(start) >= p.getStart().getTime() && Long.parseLong(start) <= p.getStart().getTime() + allowable) {
							return db.createBooking(customerUsername, employeeId, start, end, services);
						}
					}
				}
				return false;
			}
		}
		return false;

	}
	
	/**
	 * Removes an existing booking from the system
	 * @param bookingID, the ID of the booking
	 * @param businessname, the business name of the booking
	 * @return true or false for success or failure
	 */
	public boolean removeBooking(int bookingID)
	{
		return db.deleteBooking(bookingID);
	}
	
	/**
	 * Gets a booking query and returns it as an array of bookings
	 * @param rs ResultSet of query
	 * @return Booking[]
	 */
	private Booking[] bookingResultsetToArray(ResultSet rs){

		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			do {
				bookings.add(new Booking(rs.getString("starttimeunix"), rs.getString("endtimeunix"), false, rs.getString("username"), rs.getString("bookingId"), rs.getString("employeeId"), stringOfServicesToArrayList(rs.getString("bookingData"))));
			} while (rs.next());
			if (!bookings.isEmpty()) {
				Booking[] b = new Booking[bookings.size()];
				bookings.toArray(b);
				rs.close();
				return b;
			} else {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new Booking[0];
	}
	
	/**
	 * gets all shifts of an employee
	 * @param employeeId	id of requested employee
	 * @return	Returns a timetable of shifts for the employee if the employee exists, else returns null
	 */
	public Timetable getShift(String employeeId) {
		Timetable t = new Timetable();
		try {
			if (employeeId.equals("")) {
				return null;
			}
			
			ResultSet shifts = db.getShifts(Long.parseLong(employeeId), "0");
			if (shifts == null) {
				return null;
			}
			do {
				t.addPeriod(new Shift(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false, employeeId));
			} while (shifts.next());
			shifts.close();
			
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		return t;
	}
	
	/**
	 * 	Adds a working shift to an employee
	 * @param employeeId	Id of the requested employee
	 * @param rawDate	date of the shift
	 * @param startTime	start time of the shift
	 * @param endTime	end time of the shift
	 * @return	whether the shift was added or not
	 */
	public boolean addShift(String employeeId, String rawDate, String start, String end) {

		Long startTime = Long.parseLong(start);
		Long endTime = Long.parseLong(end);
		
		if (startTime < endTime && startTime > 0) {
			try {
				return (db.addShift(Integer.parseInt(employeeId), Long.toString(Long.parseLong(rawDate) + startTime), Long.toString(Long.parseLong(rawDate) + endTime)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	/**
	 * Removal of a working shift for an employee
	 * @param employeeId	Id of the requested employee
	 * @param rawDate	date of the shift
	 * @param startTime	start time of the shift
	 * @param endTime	end time of the shift
	 * @return	whether or not a shift was removed
	 */
	public boolean removeShift(String employeeId, String rawDate, String startTime, String endTime) {
		
		Long starttime = Long.parseLong(rawDate) + Long.parseLong(startTime);
		Long endtime = Long.parseLong(rawDate) + Long.parseLong(endTime);
		
		if (starttime > endtime)
			return false;
		else
			try {
				return (db.removeShift(Integer.parseInt(employeeId), Long.toString(starttime), Long.toString(endtime)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}
	
	/**
	 * Gets the details of the customer and creates a user from it.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addCustomerToDatabase(String username, String password, String business, String name, String address, String mobileno) {

		if(!name.matches("[A-Za-z -']+") ||
				!mobileno.matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") ||
				!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+") ||
				!username.matches("[A-Za-z0-9]+") ||
				password.length() == 0) {
			return false;
		}
		try {
			setBusinessDBConnection(masterDB.getBusinessDBFromName(business));
		} catch(SQLException e) {
			
		}
		return db.createCustomer(username, password, business, name, address, mobileno);
	}
	
	/**
	 * Creates an employee based off the information given by the owner.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addNewEmployee(String name, String address, String phonenumber) {
		if (name.length() == 0 || address.length() == 0 || phonenumber.length() < 6) {
			return false;
		}
		return db.createEmployee(name, address, phonenumber);
	}
	
	/**
	 * @return Array of all employees in the system.
	 * @throws SQLException 
	 */
	public Employee[] getAllEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		ResultSet rs;
		try {
			rs = db.getAllEmployees();
			if (rs == null) {
				return null;
			}
			do {
				employees.add(new Employee(rs.getString(1), rs.getString(2), null));
			} while (rs.next());
			if (!employees.isEmpty()) {
				Employee[] b = new Employee[employees.size()];
				employees.toArray(b);
				rs.close();
				return b;
			} else {
				rs.close();
				return new Employee[0];
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the availability timetable of the searched employee
	 * @param employeeId Of the requested employee
	 * @return null if there is no availability for the requested employee or the employee doesn't exist.
	 */
	public Timetable getEmployeeAvailability(String employeeId) {
		try {
			ResultSet rs = db.getEmployeeAvailability(Integer.parseInt(employeeId));
			if (rs != null) {
				Timetable t = new Timetable();
				t.mergeTimetable(rs.getString("availability"));
				rs.close();
				return t;
			}
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets an array of all customers within the business
	 * @return	an array of all customers
	 */
	public Customer[] getAllCustomers(){
		ArrayList<Customer> customers = new ArrayList<Customer>();
		try {
			ResultSet rs = db.getAllCustomers();
			if (rs != null) {
				do {
					customers.add(new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno")));
					
				} while (rs.next());

				rs.close();
			}

			if (!customers.isEmpty()) {
				Customer[] b = new Customer[customers.size()];
				customers.toArray(b);
				return b;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Getter for the currently logged in user
	 * @return	Current user
	 */
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	/**
	 * Getter for the current business
	 * @return	Current business
	 */
	public int getCurrentBusiness() {
		return this.currentBusiness;
	}
	
	/**
	 * Setter for the current business
	 * @param business	New business
	 */
	public void setCurrentBusiness(int business) {
		this.currentBusiness = business;
	}

	/**
	 * Gets an array of all services for the current business
	 * @return	An array of all services for the current business
	 */
	public Service[] getAllServices() {
		
		ArrayList<Service> services = new ArrayList<Service>();
		try {
			ResultSet rs = db.getAllServices();
			if (rs == null) {
				throw new Exception();
			}
			do {
				services.add(new Service(rs.getString("servicename"), Integer.parseInt(rs.getString("serviceprice")), Integer.parseInt(rs.getString("serviceminutes"))));
			} while (rs.next());
			
			if (!services.isEmpty()) {
				Service[] b = new Service[services.size()];
				services.toArray(b);
				rs.close();
				return b;
			}
			if (rs != null){
				rs.close();
			}

		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * Get the booking availability for the specified employee
	 * @param employeeId	Id of the requested employee
	 * @param date	minimum date of the requested availability
	 * @return	Timetable of all available slots for an employee
	 */
	public Timetable getEmployeeBookingAvailability(String employeeId, Date date) {
		//gets the bookings list of all the booking from the time the method is called
		Timetable shiftsTimetable = getShift(employeeId);
		Booking[] bookings = getBookingsAfter(new Date(0));
		
		if (shiftsTimetable != null) {
			if (bookings != null && bookings.length > 0) {
				for (Booking b : bookings) {
					if (b.getEmployeeId().equals(employeeId)) {
						shiftsTimetable.removePeriod(b);
					}
				}
			}
			shiftsTimetable.removePeriod(new Period(new Date(0), date));
		}	
		return shiftsTimetable;
	}

	/**
	 * Add a service to the business
	 * @param name	name of the service
	 * @param priceInCents	price in cents of the service ie. 100 would equal $1.00
	 * @param duration	duration in minutes of the service
	 * @return	whether or not adding the service was a success
	 */
	public boolean addService(String name, int priceInCents, String duration) {
		if (!name.matches("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}")) {
			return false;
		}
		return db.addService(name, priceInCents, Integer.parseInt(duration));
	}

	/**
	 * Parsing a string of services to an arraylist of equivalent services in the business
	 * @param services	parsed string
	 * @return	arraylist of services
	 */
	public ArrayList<Service> stringOfServicesToArrayList(String services) {
		String[] servicesSplit = services.split(":");
		ArrayList<Service> servs = new ArrayList<Service>();
		
		try {
			for(int i = 0; i < servicesSplit.length; i++) {
				ResultSet rs = db.getService(servicesSplit[i]);
				if (rs == null) {
				}
				else {
					Service s = new Service(rs.getString("servicename"), Integer.parseInt(rs.getString("serviceprice")), Integer.parseInt(rs.getString("serviceminutes")));
					servs.add(s);
					rs.close();
				}
			}
			return servs;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* mark for testing */
	/**
	 * Edits the opening hours for a business
	 * TODO: user this.currentBusiness instead of a businessname parameter
	 * @param businessname	name of the business
	 * @param times	new times being added
	 */
	public void editBusinessHours(ArrayList<String> times) {

		Timetable t = new Timetable();
		//if the business selected doesn't exist alert the user and exit the function
		
		//use an iterator to go through the availabilities
		Iterator<String> iter = times.iterator();
		//go through the the iterator to split the availabilities
		while(iter.hasNext()) {
			String[] values = iter.next().split(" ");
			
			//start creating the new timetable
			//add it to the timetable
			t.addPeriod(new Period(values[0], values[1], false));
		}
		//if the employee doesn't exit then alert the user and exit the function
		//add the availabilities to the timetable

		masterDB.setOpeningHours(this.currentBusiness, t.toString());
		 
	}
	
	/* marked for testing */
	/**
	 * Getter for business opening hours
	 * TODO: change parameter to this.currentBusiness
	 * @param currentBusiness name of the business being requested
	 * @return	Timetable of all opening hours for the business
	 */
	public Timetable getOpeningHours() {
		Timetable t = new Timetable();
		String oh = masterDB.getOpeningHours(currentBusiness);

		if (oh != null) {
			t.mergeTimetable(oh);
		}
		
		return t;
	}
	
	/* marked for testing */
	/**
	 * Get the customized color for the business
	 * TODO: use this.currentBusiness instead
	 * @param businessname	name of the requested business
	 * @return	Hex format color of the current business
	 */
	public String getBusinessColor() {
		return masterDB.getColor(currentBusiness);
	}
	
	/**
	 * Setter for the current business color
	 * TODO: user this.currentBusiness
	 * @param businessname	business being requested
	 * @param colorHex	Color to be set
	 */
	public void editBusinessColor(String colorHex) {
		masterDB.setColor(currentBusiness, colorHex);
	}
	
	/* marked for testing */
	/**
	 * @return
	 */
	public String getBusinessHeader() {
		System.out.println("test");
		if (masterDB.getHeader(currentBusiness) != null) {
			System.out.println(masterDB.getHeader(currentBusiness));
			return masterDB.getHeader(currentBusiness);
		}
		else {
			System.out.println(masterDB.getBusinessNameFromID(currentBusiness));
			return masterDB.getBusinessNameFromID(currentBusiness);
		}
	}
	
	/**
	 * 
	 * @param businessname
	 * @param header
	 */
	public void editBusinessHeader(String header) {
		masterDB.setHeader(currentBusiness, header);
	}
	
	/* marked for testing */
	/**
	 * 
	 * @param businessname
	 * @return
	 */
	public String getBusinessLogo() {
		return masterDB.getLogo(this.currentBusiness);
	}
	
	/**
	 * 
	 * @param businessname
	 * @param logoLink
	 */
	public void editBusinessLogo(String logoLink) {
		masterDB.setLogo(currentBusiness, logoLink);
	}

	public String[] getBusinessList() {
		try {
			ResultSet rs = masterDB.getAllBusinesses();
			if (rs == null) {
				return null;
			}
			ArrayList<String> businessList = new ArrayList<String>();
			
			do {
				businessList.add(rs.getString("businessname"));
			} while(rs.next());
			
			String[] businessStringArray = new String[businessList.size()];
			int i = 0;
			for(String s : businessList) {
				businessStringArray[i] = s;
				i++;
			}
			rs.close();
			return businessStringArray;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.warning(e.getMessage());
			return null;
		}
	}

	public boolean addOwnerToDatabase(String username, String password, String business, String name, String address,
			String phoneNumber) {
		System.out.println("username is " + username);
		return masterDB.createOwner(business, username, password, name, address, phoneNumber);
	}

	public boolean addBusinessToDatabase(String businessName, String address, String phoneNumber) {
		return masterDB.createBusiness(businessName, address, phoneNumber);
	}

	public User authenticate(String username, String password, String selectedBusiness) {
		User found = searchUserLogin(username.toLowerCase(), selectedBusiness);
		if (found != null && found.checkPassword(password)) {
			try {
				this.setBusinessDBConnection(Integer.parseInt(masterDB.getBusinessRow(selectedBusiness).getString("businessId")));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return found;
		}
		return null;
	}
}
