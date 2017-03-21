package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class ControllerTest {

	static Controller c = new Controller();
	static String[] testData1 = new String[2];
	static String[] testData2 = new String[2];
	static String[] testData3 = new String[2];
	static String[] testData4 = new String[2];
	@Before
	public void setup()
	{
		testData1[0] = "admin";
		testData1[1] = "admin";
		
		testData2[0] = "admin1";
		testData2[1] = "password";
		
		testData3[0] = "admin1";
		testData3[1] = "admin1";
		
		testData4[0] = "admin";
		testData4[1] = "password";
	}
	
	@Test
	public void customerlogin01() 
	{
		assert(c.login(testData1) == null);
	}

	@Test
	public void customerlogin02() 
	{
		assert(c.login(testData2) == null);
	}
	
	@Test
	public void customerlogin03() 
	{
		assert(c.login(testData3) == null);
	}
	
	@Test
	public void customerlogin04() 
	{
		assert(c.login(testData4) != null);
	}
}