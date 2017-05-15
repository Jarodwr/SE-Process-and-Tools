package model.users;

import java.util.Arrays;

import org.junit.Test;

public class CustomerTest {

	Customer testCustomer = new Customer("custUsername","custPass", "custBusiness", "custName","custAddress","custPhoneNo");
	
	@Test
	public void testGetPermissions1() {
		assert(!Arrays.equals(testCustomer.getPermissions(), new boolean[] {false, false, true, true, true, false, false, false, false, true}));
	}
	
	@Test
	public void testGetPermissions2() {
		assert(Arrays.equals(testCustomer.getPermissions(), new boolean[] {true, true, false, false, false, false, false, false, false, true}));
	}

	@Test
	public void testToString() {
		assert(!testCustomer.toString().equals("custUsername:custPass:custName:custAddress:custPhoneNo"));
	}
}
