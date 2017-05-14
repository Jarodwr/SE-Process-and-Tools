package model.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.database.SQLiteConnection;
import model.employee.Employee;
import model.period.Booking;
import model.period.Period;
import model.period.Shift;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Customer;
import model.users.Owner;
import model.users.User;

/**
 * A services class, used for interfacing between the controller and the database.
 */
public class Utility {

	private Logger LOGGER = Logger.getLogger("main");
	private User currentUser = null;
	private String currentBusiness = "SARJ's Milk Business"; //TODO
	private SQLiteConnection db = new SQLiteConnection();
	
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	
	public Utility() {
		this.currentBusiness = "SARJ's Milk Business";
	}
	
	public void setConnection(SQLiteConnection newDb) {
		this.db = newDb;
	}
	
	public User searchUser(String username) {
		ResultSet rs;
		String business;
		try {
			String type = db.findUserType();
			business = db.findUserBusiness();
			rs = db.getUserRow(username);
			if (db.getOwnerRow(username) != null) {
				ResultSet rs2;
				rs2 = db.getOwnerRow(username);
				String business = rs2.getString("businessname");
				rs2.close();

				rs = db.getUserRow(username);
				Owner owner = new Owner(username, rs.getString("password"), business, rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return owner;
			}
			else {
				rs = db.getUserRow(username);
				Customer customer = new Customer(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("address"), rs.getString("mobileno"));
				rs.close();
				return customer;
			}

		} catch (Exception e) {
			//LOGGER.warning(e.getMessage()); Exceptions are intended behaviour here, no need to log
			return null;
		}
		
	}
	
	/**
	 * @param username Requested user
	 * @param password Attempted password
	 * @return if the user exists with the entered password, return the user. Else return null.
	 */
	public User authenticate(String username, String password) {
		User found = searchUser(username.toLowerCase());
		if (found != null && found.checkPassword(password))
			return found;
		return null;
	}
	
	/**
	 * Gets a list of employees and returns their details as an array of strings
	 * TODO: Change this to return a list&lt;Employee&gt; instead
	 * @return String[Employee][Details]
	 */
	public String[][] getEmployeeList() {
		ResultSet rs;
		try {
			rs = db.getAllEmployees();
			if (rs == null) throw new Exception("No employees found");
		} catch (Exception e) {
				LOGGER.warning(e.getMessage());
			return null;
		}

		ArrayList<String[]> employees = new ArrayList<String[]>();
		try {
			do {
				employees.add(new String[] {rs.getString(1), rs.getString(3)});
			} while (rs.next());
			
			rs.close();
			
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
		
		if (!employees.isEmpty()) {
			String[][] b = new String[employees.size()][];
			employees.toArray(b);
			return b;
		}
		
		return null;
	}
	
	/**
	 * Compiles all available times for all employees
	 * @return Merged timetable
	 */
	public Timetable getAvailableTimes() {
		
		Timetable t = new Timetable();

		try {
			ResultSet rsEmployees = db.getAllEmployees();
			do {
				ResultSet rsTimetables = db.getEmployeeAvailability(Integer.parseInt(rsEmployees.getString("employeeId")));
				if (rsTimetables == null) {
					continue;
				}
				t.mergeTimetable(rsTimetables.getString("availability"));
				rsTimetables.close();
			} while(rsEmployees.next());
			
			rsEmployees.close();
			return t;
		}
		catch(SQLException e) {

			LOGGER.warning(e.getMessage());
			return null;
		}
	}
	
	//TODO: Deprecate
	public Employee[] getApplicableEmployees(long duration, long startTime) {
		ArrayList<Employee> applicable = new ArrayList<Employee>();
		try {
			Employee[] employees = getAllEmployees();
			for (Employee e : employees) {
				Timetable t = new Timetable();
				ResultSet shifts = db.getShifts(Integer.parseInt(e.getEmployeeId()), Long.toString(System.currentTimeMillis()));
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = db.getBookingsByEmployeeId(e.getEmployeeId());
				if (bookings != null) {
					do {
						t.removePeriod(new Period(bookings.getString("starttimeunix"), bookings.getString("endtimeunix"), false));
					} while (bookings.next());
					bookings.close();
				}
				Timetable ap = t.applicablePeriods(duration);
				if (ap.getAllPeriods().length > 0 && ap.isStartTimeIn(startTime)) {
					applicable.add(e);
				}
			}
		} catch (SQLException exception) {
			LOGGER.warning(exception.getMessage());
		}
		
		Employee[] filtered = new Employee[applicable.size()];
		applicable.toArray(filtered);
		return filtered;
	}
	
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
	
	//TODO: deprecated
	public Timetable getAvailableBookingTimesByDuration(long duration) {
		Timetable appPeriods = new Timetable();
		try {
			Employee[] employees = getAllEmployees();
			for (Employee e : employees) {
				Timetable t = new Timetable();
				ResultSet shifts = db.getShifts(Integer.parseInt(e.getEmployeeId()), Long.toString(System.currentTimeMillis()));
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = db.getBookingsByEmployeeId(e.getEmployeeId());
				if (bookings == null) {
					appPeriods.mergeTimetable(t.applicablePeriods(duration));
					continue;
				}
				do {
					t.removePeriod(new Period(bookings.getString("starttimeunix"), bookings.getString("endtimeunix"), false));
				} while (bookings.next());
				bookings.close();
				appPeriods.mergeTimetable(t.applicablePeriods(duration));
			}
		} catch (SQLException exception) {
			LOGGER.warning(exception.getMessage());
		}
		return appPeriods;
	}
	
	//TODO: Deprecated
	public Timetable getAvailableBookingTimes() {
		Employee[] employees;

		employees = getAllEmployees();
		Timetable available = new Timetable();
		for (Employee e : employees) {
			try {
				Timetable t = new Timetable();
				ResultSet shifts = db.getShifts(Integer.parseInt(e.getEmployeeId()), "0");
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = db.getBookingsByEmployeeId(e.getEmployeeId());
				if (bookings == null) {
					available.mergeTimetable(t);
					continue;
				}
				do {
					t.removePeriod(new Period(bookings.getString("starttimeunix"), bookings.getString("endtimeunix"), false));
				} while (bookings.next());
				bookings.close();
				available.mergeTimetable(t);
			} catch(SQLException exception) {
				exception.printStackTrace();
				LOGGER.warning(exception.getMessage());
			}

		}
		return available;

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
	 * @throws SQLException 
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
	public boolean addCustomerToDatabase(String username, String password, String name, String address, String mobileno) {
		return db.createCustomer(username, password, name, address, mobileno);
	}
	
	/**
	 * Creates an employee based off the information given by the owner.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addNewEmployee(String id, String businessName, String name, String address, String phonenumber, int timetableID) {
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

	public Customer[] getAllCustomers(){
		ArrayList<Customer> customers = new ArrayList<Customer>();
		try {
			ResultSet rs = db.getAllCustomers();
			do {
				customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
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
	
	//TODO: remove
	public String[][] getWorkingTimes() 
	{
		//TODO: refactor into other employeelist functions
		String [][] employeelist = getEmployeeList();
		Timetable t = new Timetable();
		ArrayList<String[]> allShifts = new ArrayList<String[]>();
		
		for(int i = 0; i > employeelist.length; i++)
		{
			t = getShift(employeelist[i][0]);
			String[][] table = t.toStringArray();
			allShifts.addAll(Arrays.asList(table));
		}

		String[][] ftable = Arrays.copyOf(allShifts.toArray(), allShifts.toArray().length, String[][].class);
		
		return ftable;
	}

	public Owner getBusinessOwner() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public String getCurrentBusiness() {
		return this.currentBusiness;
	}
	
	public void setCurrentBusiness(String business) {
		this.currentBusiness = business;
	}

	public Service[] getAllServices() {
		
		ArrayList<Service> services = new ArrayList<Service>();
		try {
			ResultSet rs = db.getAllServices(currentBusiness);
			do {
				services.add(new Service(rs.getString("servicename"), Integer.parseInt(rs.getString("serviceprice")), Integer.parseInt(rs.getString("serviceminutes"))));
			} while (rs.next());
			
			if (!services.isEmpty()) {
				Service[] b = new Service[services.size()];
				services.toArray(b);
				rs.close();
				return b;
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	public boolean addService(String name, int priceInCents, String duration) {
		return db.addService(name, priceInCents, Integer.parseInt(duration), getCurrentBusiness());
	}

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
		/* TODO turn this try block below into a utility method */
		try {
			ResultSet rs = db.getBusinessHours(businessname);
			int id;
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
	public Timetable getOpeningHours(String currentBusiness) {
		Timetable t = null;
		ResultSet rs;
		try {
			rs = db.getBusinessHours(currentBusiness);
			if (rs == null) {
				return t;
			}
			else {
				t.mergeTimetable(rs.getString(1));
				return t;
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
			return t;
		}
		
	}
	/* marked for testing */
	public String getBusinessColor(String businessname) {
		ResultSet rs;
		try {
			rs = db.getBusinessColor(businessname);
			if (rs == null) {
				return ""; // TODO HEX FOR RED
			}
			else
				return rs.getString("colorHex");
		} catch (SQLException e) {
			return ""; // TODO HEX FOR RED
		}
		
	}
	
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
	public String getBusinessHeader(String businessname) {
		ResultSet rs;
		try {
			rs = db.getBusinessHeader(businessname);
			if (rs == null) {
				return "";
			}
			else
				return rs.getString("header");
		} catch (SQLException e) {
			return "";
		}
	}
	
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
}
