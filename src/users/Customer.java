package users;

public class Customer extends User {

	private String name;
	private String address;
	private String phoneNumber;
	public final boolean[] permissions = {false, false, true, true, false, false, false, false, false, true};
	
	public Customer(String username, String password, String name, String address, String phoneNumber) {
		super(username, password);
		
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
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
