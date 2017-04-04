package controller;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import database.SQLiteConnection;
import employee.Employee;

import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import bookings.Booking;
import main.Menu;
import period.Period;
import timetable.Timetable;
import users.Customer;
import users.Owner;
import users.User;

public class Controller {
	private Logger LOGGER = Logger.getLogger(Controller.class.getName(), Controller.class);
	private Menu view = new Menu();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private User activeUser;
	
	//private boolean debugMode = false;
	
	private boolean[] defaultPerms = {true, true, false, false, false, false, false, false ,false, false};
	
	public Controller() {
		
	}
	
	public void run()
	{
		//initialize view
				Menu view = new Menu();

				boolean breakLoop = false; // exit program case
				//debugMode = true; // remove this line while demoing
				boolean[] currentPerms = defaultPerms; // allows for permission changes while program is running
				activeUser = null;
				while(!breakLoop) {
					if (activeUser != null) {
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
		User user = authenticate(loginDetails[0], loginDetails[1]);

		if (user == null) {
			LOGGER.log(Level.FINE, "LOGIN: Failed");
			view.failure("Login", "Incorrect Username/Password");
		} else {
			LOGGER.log(Level.FINE, "LOGIN: Success");
			view.success("Login", "Welcome back, " + user.getUsername());
		}
		
		return user;
	}
	
	protected User register(String[] userDetails){
		LOGGER.log(Level.FINE, "REGISTER: Registration details: " + Arrays.toString(userDetails));
		if (SQLiteConnection.createCustomer(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4])) {
			LOGGER.log(Level.FINE, "REGISTER: Success, user added to system");
			return searchUser(userDetails[0]);
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
		Timetable ConcatenatedTimetable = getAvailableTimes();
		String[][] s = ConcatenatedTimetable.toStringArray();
		view.viewBookingAvailability(s);
	}
	
	/* doesn't work yet don't use */
	private Timetable getAvailableTimes() {
		
		Timetable t = new Timetable();
		try {
			ResultSet rsEmployees = SQLiteConnection.getAllEmployees();
			ResultSet rsTimetables;
			if (!rsEmployees.next()) {
				LOGGER.log(Level.WARNING, "No employees registered in the system");
				return null;
			}
			while(rsEmployees.next()) {
				rsTimetables = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(rsEmployees.getString("employeeId")));
				if (!rsTimetables.next()) {
					continue;
				}
				t.mergeTimetable(rsEmployees.getString("availability"));
				
			}
			return t;
		}
		catch(Exception e) {
			return null;
		}
		//view.viewBookingAvailability(availability.toStringArray());
	}
	
	private void addNewBooking(String[] booking) {
		
	}
	
	private void viewSummaryOfBookings() {
		try {
			Booking[] bookings = bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart(sdf.format(Calendar.getInstance().getTime())));

			if (bookings.length == 0) {
				LOGGER.log(Level.FINE, "VIEW SUMMARY OF BOOKINGS: failure, not bookings in database in the future");
				view.failure("View Booking Summaries", "No future bookings");
			} else {
				String[][] bookingsStringArray = new String[bookings.length][];
				
				for (int i = 0; i < bookings.length; i++) {
					bookingsStringArray[i] = bookings[i].toStringArray();
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
			String employeeID = view.showEmployeeList(getEmployeeList(SQLiteConnection.getAllEmployees()));
			while (!employeeID.equals("")) {
				Timetable t = new Timetable();
				ResultSet rs = SQLiteConnection.getEmployeeAvailability(Integer.parseInt(employeeID));
				t.mergeTimetable(rs.getString(1));
				view.showTimetable(t.toStringArray());
				employeeID = view.showEmployeeList(getEmployeeList(SQLiteConnection.getAllEmployees()));
			}

		} catch(Exception e) {
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
		
		if(!validate(name, "[A-Za-z]+"))
  		{
			view.failure("Add Employee", "Name is not Valid");
			return false;
		}
		if(!validate(phonenumber, "[0-9]+"))
		{
			view.failure("Add Employee", "Phone number is not Valid");
			return false;
		}
		if(!validate(address, "[A-Za-z0-9' ]+"))
		{
			view.failure("Add Employee", "Address is not Valid");
			return false;
		}
		
		
		
		if (SQLiteConnection.createEmployee(Integer.parseInt(id), "", name, address, phonenumber, 0)) 
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
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	protected User searchUser(String username) {
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
	
	private boolean validate(String string, String regex)
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
	
	protected Booking[] bookingResultsetToArray(ResultSet rs) {
		
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
	
	protected User authenticate(String username, String password) {
		User found = searchUser(username);
		System.out.println(found.checkPassword(password));
		if (found != null && found.checkPassword(password)) {
			return found;
		}
		return null;
	}
	
	protected String[][] getEmployeeList(ResultSet rs) {
		
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
			System.out.println(b[1][0]);
			System.out.println(b[1][1]);
			return b;
		} else {
			return null;
		}
	}
}
