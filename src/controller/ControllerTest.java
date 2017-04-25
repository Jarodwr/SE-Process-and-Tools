package controller;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.database.SQLiteConnection;
import model.exceptions.ValidationException;
import model.period.Booking;
import model.users.Owner;

@SuppressWarnings("unused")
public class ControllerTest {
	static Controller c = new Controller();
	static Owner user = new Owner("name", "pass", "SARJ's Milk Business", "Admin", "124 Address", "0412345678");
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
	static String[] testData11 = new String[5];
	static String[] testData12 = new String[5];
	static String[] testData13 = new String[5];
	
	//add employee test data
	static String[] testData14 = new String[4];
	static String[] testData15 = new String[4];
	static String[] testData16 = new String[4];
	static String[] testData17 = new String[4];
	
	
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
		testData9[3] = "12 Melbourne";
		testData9[4] = "0387656789";
		
		testData10[0] = "admin";
		testData10[1] = "password";
		testData10[2] = "Russell";
		testData10[3] = "12 Melbourne";
		testData10[4] = "0387656789";
		
		testData11[0] = "admin";
		testData11[1] = "password";
		testData11[2] = "Russell234";
		testData11[3] = "12 Melbourne";
		testData11[4] = "0387656789";
		
		testData12[0] = "admin";
		testData12[1] = "password";
		testData12[2] = "Russell";
		testData12[3] = "Fake888***";
		testData12[4] = "0387656789";
		
		testData13[0] = "admin";
		testData13[1] = "password";
		testData13[2] = "Russell";
		testData13[3] = "12 Melbourne";
		testData13[4] = "abcd";
		
		
		testData14[0] = "Fred1234";
		testData14[1] = "03123456789";
		testData14[2] = "12 Melbourne";
		testData14[3] = "s234567";
		
		testData15[0] = "Fred";
		testData15[1] = "asdf";
		testData15[2] = "12 Melbourne";
		testData15[3] = "s234567";
		
		testData16[0] = "Fred";
		testData16[1] = "03123456789";
		testData16[2] = "Melbourne";
		testData16[3] = "s234567";
		
		testData17[0] = "Fred";
		testData17[1] = "03123456789";
		testData17[2] = "12 Melbourne";
		testData17[3] = "s234567";
	}
	
//	//customer logining in test cases
//	@Test
//	public void customerlogin01() 
//	{
//		assert(c.login(testData1) == null);
//	}
//
//	@Test
//	public void customerlogin02() 
//	{
//		assert(c.login(testData2) == null);
//	}
//	
//	@Test
//	public void customerlogin03() 
//	{
//		assert(c.login(testData3) == null);
//	}
//	
//	@Test
//	public void customerlogin04() 
//	{
//		assert(!c.login(testData4).isOwner());
//	}
//	
//	//owner logging in test cases
//	@Test
//	public void ownererlogin01() 
//	{
//		assert(c.login(testData5) == null);
//	}
//	
//	@Test
//	public void ownererlogin02() 
//	{
//		assert(c.login(testData6) == null);
//	}
//	
//	@Test
//	public void ownererlogin03() 
//	{
//		assert(c.login(testData7) == null);
//	}
//	
//	@Test
//	public void ownererlogin04() 
//	{
//		assert(c.login(testData8).isOwner());
	}
	
	//register a customer tests
//	@Test
//	public void customerregister01() throws ValidationException 
//	{
//		assert(c.register(testData9).equals(null));
//	}
//	
//	@Test
//	public void customerregister02() throws ValidationException 
//	{
//		assert(!c.register(testData10).equals(null));
//	}
//	
//	@Test
//	public void customerregister03() throws ValidationException 
//	{
//		assert(c.register(testData11).equals(null));
//	}
//	
//	@Test
//	public void customerregister04() throws ValidationException 
//	{
//		assert(c.register(testData12).equals(null));
//	}
//	
//	@Test
//	public void customerregister05() throws ValidationException 
//	{
//		assert(c.register(testData13).equals(null));
//	}
	
	//add new employee tests
//	@Test
//	public void addemployeetest01() 
//	{
//		assert(!c.addEmployee(testData14, user));
//	}
//	
//	@Test
//	public void addemployeetest02() 
//	{
//		assert(!c.addEmployee(testData15, user));
//	}
//	
//	@Test
//	public void addemployeetest03() 
//	{
//		assert(!c.addEmployee(testData16, user));
//	}
//	
//	@Test
//	public void addemployeetest04() 
//	{
//		assert(c.addEmployee(testData17, user));
//	}
	
//	@Test
//	public void testGetBookingsAfter01() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
//			System.out.println(testBooking[0].getBookingId());
//			
//			assert(testBooking[0].getBookingId().equals("4"));
//			assert(testBooking[1].getBookingId().equals("3"));
//			assert(testBooking[2].getBookingId().equals("1"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter02() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("00000000000000"));
//			assert(testBooking[0].getBookingId().equals("2"));
//			assert(testBooking[1].getBookingId().equals("4"));
//			assert(testBooking[2].getBookingId().equals("3"));
//			assert(testBooking[3].getBookingId().equals("1"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter03() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("90000000000000"));
//			assert(testBooking.length == 0);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter04() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter05() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetBookingsAfter06() {
//		Booking[] testBooking;
//		try {
//			testBooking = c.bookingResultsetToArray(SQLiteConnection.getBookingsByPeriodStart("20170330123000"));
//			assert(testBooking[0].getBookingId() == "1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//}
