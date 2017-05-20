package model.users;

/**
 * Basic user class with personal details.
 */
public class Customer extends User {

	private String name;
	private String address;
	private String phoneNumber;
	
	public Customer(String username, String password, String name, String address, String phoneNumber) {
		super(username, password);
		
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.permissions = new boolean[] {
				false, 	//Register
				false, 	//Login
				false, 	//View current bookings
				true, 	//View available times
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
	}
	
	public String getName() {
		
		return this.name;
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
}
