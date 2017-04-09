package model.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import model.database.SQLiteConnection;
import model.employee.Employee;
import model.period.Booking;
import model.period.Period;
import model.service.Service;
import model.timetable.Timetable;
import model.users.Customer;
import model.users.Owner;
import model.users.User;

/**
 * A services class, used for interfacing between the controller and the database.
 */
public class Utility {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Logger LOGGER = Logger.getLogger("main");
	
	
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

	public boolean validate(String string, String regex)
	{
		if(string.matches(regex))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @param username Requested user
	 * @param password Attempted password
	 * @return if the user exists with the entered password, return the user. Else return null.
	 */
	public User authenticate(String username, String password) {
		User found = searchUser(username);
		if (found != null && found.checkPassword(password)) {
			return found;
		}
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
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
		if (!employees.isEmpty()) {
			String[][] b = new String[employees.size()][];
			employees.toArray(b);
			return b;
		} else {
			return null;
		}
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
				t.mergeTimetable(rsTimetables.getString("availability"));
			} while(rsEmployees.next());
			
			return t;
		}
		catch(SQLException e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}
	
	public Timetable getAvailableTimesCustomer() {
		Employee[] employees = getAllEmployees();
		Timetable available = new Timetable();
		for (Employee e : employees) {
			try {
				Timetable t = new Timetable();
				ResultSet shifts = SQLiteConnection.getShifts(Integer.parseInt(e.getEmployeeId()), Long.toString(System.currentTimeMillis()/1000));
				
				do {
					t.addPeriod(new Period(shifts.getString("unixstarttime"), shifts.getString("unixendtime"), false));
				} while (shifts.next());
				
				ResultSet bookings = SQLiteConnection.getBookingsByEmployeeId(e.getEmployeeId());
				
				do {
					t.removePeriod(new Period(bookings.getString("starttime"), bookings.getString("endtime"), false));
				} while (bookings.next());
				
				available.mergeTimetable(t);
			} catch(SQLException exception) {
					LOGGER.warning(exception.getMessage());
			}
		}
		if (available.getAllPeriods().length == 0) {
			return null;
		} else {
			return available;
		}
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
	 * Gets a booking query and returns it as an array of bookings
	 * @param rs ResultSet of query
	 * @return Booking[]
	 */
	public Booking[] bookingResultsetToArray(ResultSet rs) {

		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			do {
				bookings.add(new Booking(rs.getString("starttimeunix"), rs.getString("endtimeunix"), false, rs.getString("username"), rs.getString("employeeId"), Service.stringOfServicesToArrayList(rs.getString("bookingData"))));
				
			} while (rs.next());
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		if (!bookings.isEmpty()) {
			Booking[] b = new Booking[bookings.size()];
			bookings.toArray(b);
			return b;
		} else {
			return new Booking[0];
		}
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
	public boolean addEmployeeToDatabase(String id, String businessName, String name, String address, String phonenumber, int timetableID) {
		return SQLiteConnection.createEmployee(Integer.parseInt(id), "", name, address, phonenumber, 0);
	}
	
	/**
	 * @return Array of all employees in the system.
	 */
	public Employee[] getAllEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			ResultSet rs = SQLiteConnection.getAllEmployees();
			
			do {
				employees.add(new Employee(rs.getString(1), rs.getString(2), null));
			} while (rs.next());
			
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
		}
		
		if (!employees.isEmpty()) {
			Employee[] b = new Employee[employees.size()];
			employees.toArray(b);
			return b;
		} else {
			return new Employee[0];
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
			return t;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}
}
