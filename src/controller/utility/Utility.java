package controller.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.database.SQLiteConnection;
import model.employee.Employee;
import model.period.Booking;
import model.period.Period;
import model.timetable.Timetable;
import model.users.Customer;
import model.users.Owner;
import model.users.User;

public class Utility {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
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
	
	public User authenticate(String username, String password) {
		User found = searchUser(username);
		if (found != null && found.checkPassword(password)) {
			return found;
		}
		return null;
	}
	
	public String[][] getEmployeeList() {
		ResultSet rs;
		try {
			rs = SQLiteConnection.getAllEmployees();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

		ArrayList<String[]> employees = new ArrayList<String[]>();
		try {
			do {
				employees.add(new String[] {rs.getString(1), rs.getString(3)});
			} while (rs.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!employees.isEmpty()) {
			String[][] b = new String[employees.size()][];
			employees.toArray(b);
			return b;
		} else {
			return null;
		}
	}
	
	public Timetable getAvailableTimes() {
		
		Timetable t = new Timetable();
		try {
			ResultSet rsEmployees = SQLiteConnection.getAllEmployees();
			if (!rsEmployees.next()) {
				return null;
			}
			while(rsEmployees.next()) {
				ResultSet rsTimetables = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(rsEmployees.getString("employeeId")));

				t.mergeTimetable(rsTimetables.getString("availability"));
			}
			return t;
		}
		catch(Exception e) {
			return null;
		}
	}
	public Booking[] getBookingsAfter(Date date) {
		try {
			return bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart(sdf.format(date)));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
	public Booking[] bookingResultsetToArray(ResultSet rs) {

		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			do {
				bookings.add(new Booking(rs.getString(1),rs.getString(3) , new Period(sdf.parse(rs.getString(4)), sdf.parse(rs.getString(5)))));
				
			} while (rs.next());
		} catch (Exception e) {
			
		}
		if (!bookings.isEmpty()) {
			Booking[] b = new Booking[bookings.size()];
			bookings.toArray(b);
			return b;
		} else {
			return new Booking[0];
		}
	}
	
	public boolean addCustomerToDatabase(String username, String password, String name, String address, String mobileno) {
		return SQLiteConnection.createCustomer(username, password, name, address, mobileno);
	}
	
	public boolean addEmployeeToDatabase(String id, String businessName, String name, String address, String phonenumber, int timetableID) {
		return SQLiteConnection.createEmployee(Integer.parseInt(id), "", name, address, phonenumber, 0);
	}
	
	public Employee[] getAllEmployees() {
		return new Employee[0];
	}
	
	public Timetable getEmployeeAvailability(String employeeId) {
		try {
			ResultSet rs = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(employeeId));
			Timetable t = new Timetable();
			t.mergeTimetable(rs.getString("availability"));
			return t;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
