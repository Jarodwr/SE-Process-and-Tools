package controller;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import bookings.Booking;

@SuppressWarnings("unused")
public class ControllerTest {

	static Controller c = new Controller();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
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
	
//	@Test
//	public void testGetBookingsAfter01() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("20170330123000"));
//			assert(testBooking[0].getBookingId().equals("4"));
//			assert(testBooking[1].getBookingId().equals("3"));
//			assert(testBooking[2].getBookingId().equals("1"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter02() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("00000000000000"));
//			assert(testBooking[0].getBookingId().equals("2"));
//			assert(testBooking[1].getBookingId().equals("4"));
//			assert(testBooking[2].getBookingId().equals("3"));
//			assert(testBooking[3].getBookingId().equals("1"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter03() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("90170330123000"));
//			assert(testBooking.length == 0);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter04() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter05() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter06() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.getBookingsAfter(sdf.parse("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
