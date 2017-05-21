package SARJ.BookingSystem.model.users;

/**
* Defines the basic operable user.
*/
public abstract class User {

	protected String username;
	protected String password;
	
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
	protected boolean[] permissions = {
			true, 	//Register
			true, 	//Login
			false, 	//View current bookings
			false, 	//View available times
			false, 	//Add new booking
			false, 	//View summary of bookings
			false, 	//Add working time/date
			false,	//view working time/date
			false, 	//Show all worker availability for next 7 days
			false, 	//Add Employee
			false,  //Add Booking Ownerwise
			false,	//Edit Availabilities
			true	//Log out
			};
	
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
		return password.equals(this.password);
	}
	
	public boolean[] getPermissions() {
		return permissions;
	}
	
	public boolean isOwner() {
		return false;
	}
	
	public boolean isAdmin() {
		return false;
	}
	
   /**
   * All user classes need to implement a format to be stored in the text file/database 
   */
	public abstract String toString();
}
