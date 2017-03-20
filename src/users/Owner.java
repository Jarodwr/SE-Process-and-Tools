package users;

public class Owner extends User {

	private String businessName;
	private String name;
	private String address;
	private String phoneNumber;
	protected boolean[] permissions = {false, false, true, true, true, true, true, true, true, true};
	
	public Owner(String username, String password, String businessName, String businessOwnerName, String address, String phoneNumber) {
		super(username, password);
		
		this.businessName = businessName;
		this.name = businessOwnerName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	public String getBusinessName() {
		return this.businessName;
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
		
		return this.username + ":" + this.password + ":" + this.businessName + ":" + this.name + ":" + this.address + ":" + this.phoneNumber;
	}
	
	public boolean[] getPermissions() {
		return permissions;
	}
	
	public boolean isOwner() {
		return true;
	}
}
