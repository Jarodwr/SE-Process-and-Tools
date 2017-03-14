/**
 * 
 */
package main;

import static org.junit.Assert.*;

import org.junit.Test;


public class MenuTest {

	@Test
	public void loginTest() {
		
		Menu view = new Menu();
		
		String loginOutput = view.login();
		
		if (loginOutput.equals("user:test")) {
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void plop() {
		fail("failed");
	}

}
