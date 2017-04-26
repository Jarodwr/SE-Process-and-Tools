package model.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
	
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Logger LOGGER = Logger.getLogger("main");
	private User currentUser = null;
	private String currentBusiness = "SARJ's Milk Business"; //TODO
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	public User searchUser(String username) {
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
			rs = SQLiteConnection.getAllEmployees();
		} catch (SQLException e) {
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
			ResultSet rsEmployees = SQLiteConnection.getAllEmployees();
			do {
				ResultSet rsTimetables = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(rsEmployees.getString("employeeId")));
				if (rsTimetables == null) {
					continue;
				}
				System.out.println("test");
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
	public Employee[] getApplicableEmployees(long duration, long startTime) {
		ArrayList<Employee> applicable = new ArrayList<Employee>();
		try {
			Employee[] employees = getAllEmployees();
			for (Employee e : employees) {
				Timetable t = new Timetable();
				ResultSet shifts = SQLiteConnection.getShifts(Integer.parseInt(e.getEmployeeId()), Long.toString(System.currentTimeMillis()));
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = SQLiteConnection.getBookingsByEmployeeId(e.getEmployeeId());
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
	
	public Timetable getAvailableBookingTimesByDuration(long duration) {
		Timetable appPeriods = new Timetable();
		try {
			Employee[] employees = getAllEmployees();
			for (Employee e : employees) {
				Timetable t = new Timetable();
				ResultSet shifts = SQLiteConnection.getShifts(Integer.parseInt(e.getEmployeeId()), Long.toString(System.currentTimeMillis()));
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = SQLiteConnection.getBookingsByEmployeeId(e.getEmployeeId());
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
	
	public Timetable getAvailableBookingTimes() {
		Employee[] employees;

		employees = getAllEmployees();
		Timetable available = new Timetable();
		for (Employee e : employees) {
			try {
				Timetable t = new Timetable();
				ResultSet shifts = SQLiteConnection.getShifts(Integer.parseInt(e.getEmployeeId()), "0");
				
				if (shifts == null) {
					continue;
				}
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				shifts.close();
				
				ResultSet bookings = SQLiteConnection.getBookingsByEmployeeId(e.getEmployeeId());
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
			return bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart(date.getTime() / 1000));
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Adds a new booking to the system, yet to be implemented till part B
	 * @param booking
	 * @return 
	 */
	public boolean addNewBooking(String customerUsername, String employeeId, String start, String end, String services) {
		//TODO: validation code
		return SQLiteConnection.createBooking("SARJ's Milk Business", customerUsername, employeeId, start, end, services);
//		new Booking(booking[0], booking[1], false, booking[2], booking[3], Service.stringOfServicesToArrayList(booking[4]), true);
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
				bookings.add(new Booking(rs.getString("starttimeunix"), rs.getString("endtimeunix"), false, rs.getString("username"), rs.getString("employeeId"), Service.stringOfServicesToArrayList(rs.getString("bookingData"))));
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
			ResultSet shifts = SQLiteConnection.getShifts(Integer.parseInt(employeeId), Long.toString(System.currentTimeMillis()/1000));
			if (shifts == null) {
				return null;
			}
			do {
				t.addPeriod(new Shift(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false, employeeId));
			} while (shifts.next());
			
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		return t;
	}
	
	/**
	 * Gets the details of the customer and creates a user from it.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addCustomerToDatabase(String username, String password, String name, String address, String mobileno) {
		return SQLiteConnection.createCustomer(username, password, name, address, mobileno);
	}
	
	/**
	 * Creates an employee based off the information given by the owner.
	 * @return If creation is a success, return true. Else return false.
	 */
	public boolean addNewEmployee(String id, String businessName, String name, String address, String phonenumber, int timetableID) {
		return SQLiteConnection.createEmployee("", name, address, phonenumber, 0);
	}
	
	/**
	 * @return Array of all employees in the system.
	 * @throws SQLException 
	 */
	public Employee[] getAllEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		ResultSet rs;
		try {
			rs = SQLiteConnection.getAllEmployees();
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
			ResultSet rs = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(employeeId));
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
			ResultSet rs = SQLiteConnection.getAllCustomers();
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
	
	public String[][] getWorkingTimes() 
	{
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
			ResultSet rs = SQLiteConnection.getAllServices(currentBusiness);
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
}
