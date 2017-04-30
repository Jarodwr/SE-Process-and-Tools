/**
 * 
 */
package view.console;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Scanner;

import org.junit.*;


public class ConsoleTest {

	Console view = new Console();
	
	@BeforeClass
	public static void setup()
	{
		
	}
	
//	@Test
//	public void loginValidTest() {
//		
//		/*This check is also done during user registration*/
//		
//		Boolean loginCheck = view.validatelogin("username","password");
//		
//		if (!loginCheck) {
//			fail("Login invalid character check failed.");
//		}
//	}
//	
//	@Test
//	public void loginInvalidTest() {
//		
//		/*This check is also done during user registration*/
//		
//		Boolean loginCheck = view.validatelogin("username","!password");
//		
//		if (!loginCheck) {
//			fail("Login invalid character found.");
//		}
//	}
//	
	@Test
	public void UserLoginTest1()
	{
		String data = "Hello, World!\r\n";
		InputStream stdin = System.in;
		try {
		  System.setIn(new ByteArrayInputStream(data.getBytes()));
		  Scanner scanner = new Scanner(System.in);
		  System.out.println(scanner.nextLine());
		} finally {
		  System.setIn(stdin);
		}
		
	}

}
