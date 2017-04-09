package model.utility;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import controller.Controller;
import model.users.Customer;
import model.users.Owner;
import model.users.User;

public class UtilityTest {
	
	static Controller c = new Controller();
	static User user = new Owner("name", "pass", "SARJ's Milk Business", "Admin", "124 Address", "0412345678");
	static Utility u = new Utility();

	
	
	@Test
	public void testSerarchUser1() {
		
		User owner = u.searchUser("Ownertest123");
		assert(owner == null);
	}
	
	@Test
	public void testSerarchUser2() {
		
		User owner = u.searchUser("Ownertest");
		assertThat(owner, instanceOf(Owner.class));
	}
	
	/*@Test
	public void testSerarchUser3() {
		
		User owner = u.searchUser("name");
		assertThat(owner, instanceOf(Customer.class));
	}*/

}
