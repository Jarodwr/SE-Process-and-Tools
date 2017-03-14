package users;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {
	
	@Test
	public void CustomerToStringTest() {
		Customer testCustomer = new Customer("jarodwr","mypass","jarod","32 barkly st","0400000000");
		if (!testCustomer.toString().equals("jarodwr:mypass:jarod:32 barkly st:0400000000")) {
			fail();
		}
	}
	
	@Test
	public void EmployeeToStringTest() {
		Employee testEmployee = new Employee("employeeName", "employeePassword", "availability");
		fail("test not complete");
	}
	
	@Test
	public void OwnerToStringTest() {
		Owner testOwner = new Owner("username","password","businessName","name","address","phoneNumber");
		if (!testOwner.toString().equals("username:password:businessName:name:address:phoneNumber"))
		fail("Not yet implemented");
	}

}
