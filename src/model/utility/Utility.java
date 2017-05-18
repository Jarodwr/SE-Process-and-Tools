package model.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.database.SQLMaster;
import model.database.SQLiteConnection;
import model.employee.Employee;
import model.period.Booking;
import model.period.Period;
import model.period.Shift;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Admin;
import model.users.Customer;
import model.users.Owner;
import model.users.User;

/**
 * A services class, used for interfacing between the controller and the database.
 */
public class Utility {

	private Logger LOGGER = Logger.getLogger("main");
	private User currentUser = null;
	private String currentBusiness;
	private SQLiteConnection db = new SQLiteConnection();
	private SQLMaster masterDB = new SQLMaster();
	
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	
	public Utility() {
		this.currentBusiness = null;
	}
	
	/**
	 * Used for testing
	 * @param newDb
	 */
	public void setConnection(String newDb) {
		this.db = new SQLiteConnection(newDb);
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
				
				Customer customer = new Customer(rs.getString("username"), rs.getString("password"), currentBusiness, rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return customer;
			}

		} catch (Exception e) {
			//LOGGER.warning(e.getMessage()); Exceptions are intended behaviour here, no need to log
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
				Owner owner = new Owner(username, rs.getString("password"), businessname, rs.getString("name"), rs.getString("address"), rs.getString("phonenumber"));
				LOGGER.warning("success in finding admin, password = " + rs.getString("password"));
				rs.close();
				return owner;
			}
			catch (Exception e) {
		
			}
		if (currentBusiness != businessname) {
			setBusinessDBConnection(masterDB.getBusinessDBFromName(businessname));
			currentBusiness = businessname;
		}
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
				
				Customer customer = new Customer(rs.getString("username"), rs.getString("password"), businessname, rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return customer;
			}

		} catch (Exception e) {
			//LOGGER.warning(e.getMessage()); Exceptions are intended behaviour here, no need to log
			return null;
		}
		
	}
	
	private void setBusinessDBConnection(int businessDBFromName) {
		this.db = new SQLiteConnection("businessDB_" + Integer.toString(businessDBFromName));
		
	}

	/**
	 * Checks that the username and password match
	 * @param username Requested user
	 * @param password Attempted password
	 * @return if the user exists with the entered password, return the user. Else return null.
	 */
	public User authenticate(String username, String password) {
		User found = searchUserLogin(username.toLowerCase(), "");
		if (found != null && found.checkPassword(password))
			return found;
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
		/* TODO turn this try block below into a utility method */
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
			if (db.createAvailability(id, getCurrentBusiness(), t.toString())){
				
			}
			else {
				db.deleteAvailabilities(id, getCurrentBusiness());
				db.createAvailability(id, getCurrentBusiness(), t.toString());
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

		Timetable t = getEmployeeBookingAvailability(employeeId, new Date(Long.parseLong(start)));	//All employee shifts
		if (t == null) {
			return false;
		}
		for (Period p : t.getAllPeriods()) {
			long allowable = (p.getEnd().getTime() - p.getStart().getTime()) - (Long.parseLong(end) - Long.parseLong(start));	//Max allowable time from the period start
			
			if (Long.parseLong(start) >= p.getStart().getTime() && Long.parseLong(start) <= p.getStart().getTime() + allowable) {

				return db.createBooking(getCurrentBusiness(), customerUsername, employeeId, start, end, services);
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
	public boolean removeBooking(int bookingID, String businessname)
	{
		return db.deleteBooking(bookingID, businessname);
	}
	
	/**
	 * Gets a booking query and returns it as an array of bookings
	 * @param rs ResultSet of query
	 * @return Booking[]
	 */
	public Booking[] bookingResultsetToArray(ResultSet rs){

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
	public boolean addShift(String employeeId, String rawDate, String startTime, String endTime) {

		Long starttime = Long.parseLong(rawDate) + Long.parseLong(startTime);
		Long endtime = Long.parseLong(rawDate) + Long.parseLong(endTime);
		
		if (starttime > endtime)
			return false;
		else
			try {
				return (db.addShift(Integer.parseInt(employeeId), "SARJ's Milk Business", 
						Long.toString(starttime), Long.toString(endtime)));
			} catch (Exception e) {
				e.printStackTrace();
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
				return (db.removeShift(Integer.parseInt(employeeId), "SARJ's Milk Business", 
						Long.toString(starttime), Long.toString(endtime)));
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
		if (currentBusiness != business) {
			setBusinessDBConnection(masterDB.getBusinessDBFromName(business));
			currentBusiness = business;
		}
		return db.createCustomer(username, password, business, name, address, mobileno);
	}
	
	/**
	 * Creates an employee based off the information given by the owner.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addNewEmployee(String id, String businessName, String name, String address, String phonenumber, int timetableID) {
		if (currentBusiness != businessName) {
			setBusinessDBConnection(masterDB.getBusinessDBFromName(businessName));
			currentBusiness = businessName;
		}
		return db.createEmployee("", name, address, phonenumber, 0);
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
				employees.add(new Employee(rs.getString(1), rs.getString(3), null));
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
			if (rs == null) {
				return new Timetable();
			}
			Timetable t = new Timetable();
			t.mergeTimetable(rs.getString("availability"));
			rs.close();
			return t;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}

	/**
	 * Gets an array of all customers within the business
	 * @return	an array of all customers
	 */
	public Customer[] getAllCustomers(){
		ArrayList<Customer> customers = new ArrayList<Customer>();
		try {
			ResultSet rs = db.getAllCustomers();
			do {
				ResultSet rs2 = db.getUserBusinessRow(rs.getString("username"));
				String business = "";
				if (rs2 != null) {
					business = rs2.getString("businessname");
					rs2.close();
				} else {
					business = this.currentBusiness;
				}

				customers.add(new Customer(rs.getString(1), rs.getString(2), business, rs.getString(3), rs.getString(4), rs.getString(5)));
				
			} while (rs.next());
			
			if (!customers.isEmpty()) {
				Customer[] b = new Customer[customers.size()];
				customers.toArray(b);
				rs.close();
				return b;
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	Getter for owner user in the current business
	 * @return	Current business owner
	 */
	public Owner getBusinessOwner() {
		// TODO Auto-generated method stub
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
	public String getCurrentBusiness() {
		return this.currentBusiness;
	}
	
	/**
	 * Setter for the current business
	 * @param business	New business
	 */
	public void setCurrentBusiness(String business) {
		this.currentBusiness = business;
	}

	/**
	 * Gets an array of all services for the current business
	 * @return	An array of all services for the current business
	 */
	public Service[] getAllServices() {
		
		ArrayList<Service> services = new ArrayList<Service>();
		try {
			ResultSet rs = db.getAllServices(currentBusiness);
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
			e.printStackTrace();
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
		Booking[] bookings = getBookingsAfter(date);
		
		if (shiftsTimetable != null) {
			if (bookings != null && bookings.length > 0) {
				for (Booking b : bookings) {
					if (b.getEmployeeId().equals(employeeId))
						shiftsTimetable.removePeriod(b);
				}
			}
			return shiftsTimetable;
		}	
		return null;
	}

	/**
	 * Add a service to the business
	 * @param name	name of the service
	 * @param priceInCents	price in cents of the service ie. 100 would equal $1.00
	 * @param duration	duration in minutes of the service
	 * @return	whether or not adding the service was a success
	 */
	public boolean addService(String name, int priceInCents, String duration) {
		return db.addService(name, priceInCents, Integer.parseInt(duration), getCurrentBusiness());
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
				ResultSet rs = db.getService(servicesSplit[i], "SARJ's Milk Business");
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
	public void editBusinessHours(String businessname, ArrayList<String> times) {

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
		try {
			ResultSet rs = db.getBusinessHours(businessname);
			int id;	//TODO:????
			if (rs != null) {
				rs.close();
				db.updateBusinessHours(businessname, t.toString());
			} 
			else {
				db.createBusinessHours(businessname, t.toString());
			}
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		 
	}
	
	/* marked for testing */
	/**
	 * Getter for business opening hours
	 * TODO: change parameter to this.currentBusiness
	 * @param currentBusiness name of the business being requested
	 * @return	Timetable of all opening hours for the business
	 */
	public Timetable getOpeningHours(String currentBusiness) {
		Timetable t = new Timetable();
		ResultSet rs;
		try {
			rs = db.getBusinessHours(currentBusiness);
			if (rs == null) {
				return t;
			}
			else {
				t.mergeTimetable(rs.getString(1));
				rs.close();
				return t;
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
			return t;
		}
		
	}
	
	/* marked for testing */
	/**
	 * Get the customized color for the business
	 * TODO: use this.currentBusiness instead
	 * @param businessname	name of the requested business
	 * @return	Hex format color of the current business
	 */
	public String getBusinessColor(String businessname) {
		ResultSet rs;
		try {
			rs = db.getBusinessColor(businessname);
			if (rs == null) {
				return ""; // TODO HEX FOR RED
			}
			else {
				String colorHex = rs.getString("colorHex");
				rs.close();
				return colorHex;
			}
		} catch (SQLException e) {
			return ""; // TODO HEX FOR RED
		}
		
	}
	
	/**
	 * Setter for the current business color
	 * TODO: user this.currentBusiness
	 * @param businessname	business being requested
	 * @param colorHex	Color to be set
	 */
	public void editBusinessColor(String businessname, String colorHex) {
		try {
			ResultSet rs = db.getBusinessColor(businessname);
			if (rs != null) {
				rs.close();
				db.updateBusinessColor(businessname, colorHex);
			} 
			else {
				db.createBusinessColor(businessname, colorHex);
			}
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
	}
	
	/* marked for testing */
	/**
	 * 
	 * @param businessname
	 * @return
	 */
	public String getBusinessHeader(String businessname) {
		ResultSet rs;
		try {
			rs = db.getBusinessHeader(businessname);
			if (rs == null) {
				return "";
			}
			else {
				String header = rs.getString("header");
				rs.close();
				return header;
			}
		} catch (SQLException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param businessname
	 * @param header
	 */
	public void editBusinessHeader(String businessname, String header) {
		try {
			ResultSet rs = db.getBusinessHeader(businessname);
			if (rs != null) {
				rs.close();
				db.updateBusinessHeader(businessname, header);
			} 
			else {
				db.createBusinessHeader(businessname, header);
			}
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
	}
	
	/* marked for testing */
	/**
	 * 
	 * @param businessname
	 * @return
	 */
	public String getBusinessLogo(String businessname) {
		ResultSet rs;
		try {
			rs = db.getBusinessLogo(businessname);
			if (rs == null) {
				return "";
			}
			else
				return rs.getString("logoLink");
		} catch (SQLException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param businessname
	 * @param logoLink
	 */
	public void editBusinessLogo(String businessname, String logoLink) {
		try {
			ResultSet rs = db.getBusinessLogo(businessname);
			if (rs != null) {
				rs.close();
				db.updateBusinessLogo(businessname, logoLink);
			} 
			else {
				db.createBusinessLogo(businessname, logoLink);
			}
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
		}
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
		return masterDB.createOwner(username, password, business, name, address, phoneNumber);
	}

	public boolean addBusinessToDatabase(String businessName, String address, String phoneNumber) {
		return masterDB.createBusiness(businessName, address, phoneNumber);
	}

	public User authenticate(String username, String password, String selectedBusiness) {
		User found = searchUserLogin(username.toLowerCase(), selectedBusiness);
		if (found != null && found.checkPassword(password))
			return found;
		return null;
	}
}
