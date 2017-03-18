/**
 * 
 */
package main;

import static org.junit.Assert.*;

import org.junit.Test;


public class MenuTest {

	@Test
	public void loginValidTest() {
		
		/*This check is also done during user registration*/
		
		Menu view = new Menu();
		Boolean loginCheck = view.validatelogin("username","password");
		
		if (!loginCheck) {
			fail("Login invalid character check failed.");
		}
	}
	
	@Test
	public void loginInvalidTest() {
		
		/*This check is also done during user registration*/
		
		Menu view = new Menu();
		Boolean loginCheck = view.validatelogin("username","!password");
		
		if (!loginCheck) {
			fail("Login invalid character found.");
		}
	}

}
