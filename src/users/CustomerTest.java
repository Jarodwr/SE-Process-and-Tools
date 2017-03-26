package users;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CustomerTest {

	Customer testCustomer = new Customer("custUsername","custPass","custName","custAddress","custPhoneNo");
	
	@Test
	public void testGetPermissions1() {
		if (!Arrays.equals(testCustomer.getPermissions(), new boolean[] {false, false, true, true, true, false, false, false, false, true})) {
			fail("incorrect permissions");
		}
	}
	
	@Test
	public void testGetPermissions2() {
		if (Arrays.equals(testCustomer.getPermissions(), new boolean[] {true, true, false, false, false, false, false, false, false, true})) {
			fail("Uses default User permissions");
		}
	}

	@Test
	public void testToString() {
		if (!testCustomer.toString().equals("custUsername:custPass:custName:custAddress:custPhoneNo")) {
			fail("does not return 'custUsername:custPass:custName:custAddress:custPhoneNo'");
		}
	}
}
