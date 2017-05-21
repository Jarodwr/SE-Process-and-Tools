package SARJ.BookingSystem.model.users;

public class Admin extends Owner {

	public Admin(String password) {
		super("admin", password, "Admin", "none", "none");
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAdmin() {
		return true;
	}

}
