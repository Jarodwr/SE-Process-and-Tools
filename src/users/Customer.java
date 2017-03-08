package users;

public class Customer extends User {

	private String name;
	private String address;
	private String phoneNumber;
	private static final boolean[] permissions = new boolean[10];
	
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
		return "";
	}
}
