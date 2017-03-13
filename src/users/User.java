package users;

/**
* Defines the basic User
*/
public abstract class User {

	private String username;
	private String password;
	
	/**
	 * Current format of permissions is as follows:
	 * 0 - Register
	 * 1 - Login
	 * 2 - View current bookings
	 * 3 - View available times
	 * 4 - Add new booking
	 * 5 - View summary of bookings
	 * 6 - Add working time/date
	 * 7 - Show all worker availability for next 7 days
	 * 8 - Add an employee
	 * 9 - Log out
	 */
	protected final static boolean[] permissions = {true, true, false, false, false, false, false, false, false, false};
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
   /**
   * This method is used to verify that the password entered is the 
   * same as the password of the User being accessed
   * @param password The Password entered by the currently unauthorized user
   * @return boolean Returns false if the password is incorrect
   */
	public boolean checkPassword(String password) {
		return false;
	}
	
	public boolean[] getPermissions() {
		return permissions;
	}
	
   /**
   * All user classes need to implement a format to be stored in the text file/database 
   */
	public abstract String toString();
}
