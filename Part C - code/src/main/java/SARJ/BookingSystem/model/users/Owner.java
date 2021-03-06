package SARJ.BookingSystem.model.users;

/**
 * Very few differences to basic User, has heightened permissions and some business oriented information
 */
public class Owner extends User {

	private String name;
	private String address;
	private String phoneNumber;
	
	public Owner(String username, String password, String businessOwnerName, String address, String phoneNumber) {
		super(username, password);
		this.name = businessOwnerName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.permissions = new boolean[] {
				false, 	//Register
				false, 	//Login
				false, 	//View current bookings
				false, 	//View available times
				false, 	//Add new booking
				true, 	//View summary of bookings
				true, 	//Add working time/date
				true,	//view working time/date
				true, 	//Show all worker availability for next 7 days
				true, 	//Add Employee
				true,  //Add Booking Ownerwise
				true,	//Edit Availabilities
				true	//Log out
				};
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	@Override
	public String toString() {
		
		return this.username + ":" + this.password + ":" + this.name + ":" + this.address + ":" + this.phoneNumber;
	}
	
	public boolean isOwner() {
		return true;
	}
}
