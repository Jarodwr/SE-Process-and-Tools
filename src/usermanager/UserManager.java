package usermanager;

import java.util.ArrayList;

import users.*;

public class UserManager {

	private User activeUser;
	private ArrayList<User> users = new ArrayList<User>(); // Maybe turn this into an arrayList
	
	/**
	 * @return Accessor for the current user's permissions
	 */
	public boolean[] getCurrentUserPermissions() {
		
		return new boolean[10];
	}
	
	/**
	 * Authenticates the user by checking the password against the user and then logs the user in
	 * @param username
	 * @param password
	 * @return Returns true if the user is logged in
	 */
	public boolean login(String username, String password) {
		
		return false;
	}
	
	/**
	 * @return Returns true if the user is logged out
	 */
	public boolean logout() {
		
		return false;
	}
	/**
	 * Verify that the username isn't already taken and add the customer to 'users'
	 * @param username
	 * @param password
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @return Returns true if customer is successfully added to 'users'
	 */
	public boolean addCustomer(String username, String password, String name, String address, String phoneNumber) {
		
		return false;
	}
	
	/**
	 * @return String that prints username of the employee and their respective timetable
	 * Example:
	 * -----------------
	 * employeeUsername1
	 * -----------------
	 * period1
	 * -----------------
	 * period2
	 * -----------------
	 * period3
	 * -----------------
	 * employeeUsername2
	 * -----------------
	 * period1
	 * -----------------
	 * period2
	 * -----------------
	 */
	public String viewEmployeeAvailability() {
		
		return "";
	}
	
	/**
	 * Creates a basic employee with no timetable entries and adds it to 'users'
	 * @param username
	 * @param password
	 * @return Returns true if employee is added to 'users'
	 */
	public boolean addEmployee(String username, String password) {
		
		return false;
	}
	
	/**
	 * @param username username being searched
	 * @return Returns the user with the username being searched
	 */
	private User getUser(String username) {
		//Needs some sort of user not found exception
		return new Customer("", "", "", "", "");
	}
	
	/**
	 * Loops through 'users' and writes all user information to a text file
	 * @param filePath filepath of the txt file being written
	 * @return Return true if successful
	 */
	private boolean writeUserInfoToText(String filePath) {
		
		return false;
	}
}
