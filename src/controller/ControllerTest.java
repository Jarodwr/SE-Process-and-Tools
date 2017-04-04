package controller;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import bookings.Booking;
import database.SQLiteConnection;

@SuppressWarnings("unused")
public class ControllerTest {

	static Controller c = new Controller();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	//customer login test data
	static String[] testData1 = new String[2];
	static String[] testData2 = new String[2];
	static String[] testData3 = new String[2];
	static String[] testData4 = new String[2];
	//owner login test data
	static String[] testData5 = new String[2];
	static String[] testData6 = new String[2];
	static String[] testData7 = new String[2];
	static String[] testData8 = new String[2];
	//registration test data
	static String[] testData9 = new String[5];
	static String[] testData10 = new String[5];

	
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
		
		testData5[0] = "OwnerTest";
		testData5[1] = "password";
		
		testData6[0] = "OwnerTest1";
		testData6[1] = "1234";
		
		testData7[0] = "OwnerTest1";
		testData7[1] = "password";
		
		testData8[0] = "Ownertest";
		testData8[1] = "password";
		
		testData9[0] = "Ownertest";
		testData9[1] = "password";
		testData9[2] = "Russell";
		testData9[3] = "Melbourne";
		testData9[4] = "0387656789";
		
		testData10[0] = "admin";
		testData10[1] = "password";
		testData10[2] = "Russell";
		testData10[3] = "Melbourne";
		testData10[4] = "0387656789";
	}
	
	//customer logining in test cases
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
		assert(!c.login(testData4).isOwner());
	}
	
	//owner logging in test cases
	@Test
	public void ownererlogin01() 
	{
		assert(c.login(testData5) == null);
	}
	
	@Test
	public void ownererlogin02() 
	{
		assert(c.login(testData6) == null);
	}
	
	@Test
	public void ownererlogin03() 
	{
		assert(c.login(testData7) == null);
	}
	
	@Test
	public void ownererlogin04() 
	{
		assert(c.login(testData8).isOwner());
	}
	
	//register a customer tests
	@Test
	public void customerregister01() 
	{
		assert(c.register(testData9).equals(null));
	}
	
	@Test
	public void customerregister02() 
	{
		assert(!c.register(testData10).equals(null));
	}
	
	@Test
	public void testGetBookingsAfter01() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
			System.out.println(testBooking[0].getBookingId());
			
			assert(testBooking[0].getBookingId().equals("4"));
			assert(testBooking[1].getBookingId().equals("3"));
			assert(testBooking[2].getBookingId().equals("1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBookingsAfter02() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("00000000000000"));
			assert(testBooking[0].getBookingId().equals("2"));
			assert(testBooking[1].getBookingId().equals("4"));
			assert(testBooking[2].getBookingId().equals("3"));
			assert(testBooking[3].getBookingId().equals("1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBookingsAfter03() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("90000000000000"));
			assert(testBooking.length == 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBookingsAfter04() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
			assert(testBooking[0].getBookingId() == "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBookingsAfter05() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
			assert(testBooking[0].getBookingId() == "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBookingsAfter06() {
		Booking[] testBooking;
		try {
			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
			assert(testBooking[0].getBookingId() == "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
