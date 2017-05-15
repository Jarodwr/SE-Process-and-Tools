package model.users;

public class Admin extends Owner {

	public Admin(String password) {
		super("admin", password, "none", "Admin", "none", "none");
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAdmin() {
		return true;
	}

}
