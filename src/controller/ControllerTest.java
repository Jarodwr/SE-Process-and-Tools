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
	
	//customer logining in test cases
	@Test
	public void customerlogin01() 
	{
		assert(c.login(testData1[0], testData1[1]) == null);
	}

	@Test
	public void customerlogin02() 
	{
		assert(c.login(testData2[0], testData2[1]) == null);
	}
	
	@Test
	public void customerlogin03() 
	{
		assert(c.login(testData3[0], testData3[1]) == null);
	}
	
	@Test
	public void customerlogin04() 
	{
		assert(!c.login(testData4[0], testData4[1]).isOwner());
	}
	
	//owner logging in test cases
	@Test
	public void ownerlogin01() 
	{
		assert(c.login(testData5[0], testData5[1]) == null);
	}
	
	@Test
	public void ownerlogin02() 
	{
		assert(c.login(testData6[0], testData6[1]) == null);
	}
	
	@Test
	public void ownerlogin03() 
	{
		assert(c.login(testData7[0], testData7[1]) == null);
	}
	
	@Test
	public void ownerlogin04() 
	{
		assert(c.login(testData8[0], testData8[1]).isOwner());
	}
	
	//register a customer tests
	@Test
	public void customerregister01() throws ValidationException 
	{
		assert(c.register(testData9[0], testData9[1], testData9[2], testData9[3], testData9[4], testData9[5]).equals(null));
	}
	
	@Test
	public void customerregister02() throws ValidationException 
	{
		assert(!c.register(testData10[0], testData10[1], testData10[2], testData10[3], testData10[4], testData10[5]).equals(null));
	}
	
	@Test
	public void customerregister03() throws ValidationException 
	{
		assert(c.register(testData11[0], testData11[1], testData11[2], testData11[3], testData11[4], testData11[5]).equals(null));
	}
	
	@Test
	public void customerregister04() throws ValidationException 
	{
		assert(c.register(testData12[0], testData12[1], testData12[2], testData12[3], testData12[4], testData12[5]).equals(null));
	}
	
	@Test
	public void customerregister05() throws ValidationException 
	{
		assert(c.register(testData13[0], testData13[1], testData13[2], testData13[3], testData13[4], testData13[5]).equals(null));
	}
	
	//add new employee tests
	@Test
	public void addemployeetest01() 
	{
		assert(!c.addEmployee(testData14[0], testData14[1], testData14[2], user));
	}
	
	@Test
	public void addemployeetest02() 
	{
		assert(!c.addEmployee(testData15[0], testData15[1], testData15[2], user));
	}
	
	@Test
	public void addemployeetest03() 
	{
		assert(!c.addEmployee(testData16[0], testData16[1], testData16[2], user));
	}
	
	@Test
	public void addemployeetest04() 
	{
		assert(c.addEmployee(testData17[0], testData17[1], testData17[2], user));
	}
	
	@Test
	public void testGetBookingsAfter01() {

	}
	
	@Test
	public void testGetBookingsAfter02() {

	}
	
	@Test
	public void testGetBookingsAfter03() {

	}
	
	@Test
	public void testGetBookingsAfter04() {

	}
	
	@Test
	public void testGetBookingsAfter05() {

	}
	
	@Test
	public void testGetBookingsAfter06() {

	}
	
	@Test
	public void addService01() {

	}
	
	@Test
	public void addService02() {

	}
	
	@Test
	public void addService03() {

	}
	
	@Test
	public void addService04() {

	}
	
	@Test
	public void addService05() {

	}
	
	@Test
	public void addService06() {

	}
	
	
	@Test
	public void getCurrentBookings01() {

	}
	
	@Test
	public void getCurrentBookings02() {

	}
	
	@Test
	public void getCurrentBookings03() {

	}
	
	@Test
	public void getCurrentBookings04() {

	}
	
	@Test
	public void getCurrentBookings05() {

	}
	
	@Test
	public void getCurrentBookings06() {

	}
	
	
	@Test
	public void addNewBooking01() {

	}
	
	@Test
	public void addNewBooking02() {

	}
	
	@Test
	public void addNewBooking03() {

	}
	
	@Test
	public void addNewBooking04() {

	}
	
	@Test
	public void addNewBooking05() {

	}
	
	@Test
	public void addNewBooking06() {

	}
	
	
	
	@Test
	public void getAvailableTimes01() {

	}
	
	@Test
	public void getAvailableTimes02() {

	}
	
	@Test
	public void getAvailableTimes03() {

	}
	
	@Test
	public void getAvailableTimes04() {

	}
	
	@Test
	public void getAvailableTimes05() {

	}
	
	@Test
	public void getAvailableTimes06() {

	}
	
	
	@Test
	public void getSummaryOfBookings01() {

	}
	
	@Test
	public void getSummaryOfBookings02() {

	}
	
	@Test
	public void getSummaryOfBookings03() {

	}
	
	@Test
	public void getSummaryOfBookings04() {

	}
	
	@Test
	public void getSummaryOfBookings05() {

	}
	
	@Test
	public void getSummaryOfBookings06() {

	}
	
	
	@Test
	public void removeBookings01() {

	}
	
	@Test
	public void removeBookings02() {

	}
	
	@Test
	public void removeBookings03() {

	}
	
	@Test
	public void removeBookings04() {

	}
	
	@Test
	public void removeBookings05() {

	}
	
	@Test
	public void removeBookings06() {

	}
	
	
	@Test
	public void addWorkingTime01() {

	}
	
	@Test
	public void addWorkingTime02() {

	}
	
	@Test
	public void addWorkingTime03() {

	}
	
	@Test
	public void addWorkingTime04() {

	}
	
	@Test
	public void addWorkingTime05() {

	}
	
	@Test
	public void addWorkingTime06() {

	}
	
	
	@Test
	public void removeWorkingTime01() {

	}
	
	@Test
	public void removeWorkingTime02() {

	}
	
	@Test
	public void removeWorkingTime03() {

	}
	
	@Test
	public void removeWorkingTime04() {

	}
	
	@Test
	public void removeWorkingTime05() {

	}
	
	@Test
	public void removeWorkingTime06() {

	}
	
	
	@Test
	public void getWorkingTimes01() {

	}
	
	@Test
	public void getWorkingTimes02() {

	}
	
	@Test
	public void getWorkingTimes03() {

	}
	
	@Test
	public void getWorkingTimes04() {

	}
	
	@Test
	public void getWorkingTimes05() {

	}
	
	@Test
	public void getWorkingTimes06() {

	}
	
	
	@Test
	public void getWorkerAvailability01() {

	}
	
	@Test
	public void getWorkerAvailability02() {

	}
	
	@Test
	public void getWorkerAvailability03() {

	}
	
	@Test
	public void getWorkerAvailability04() {

	}
	
	@Test
	public void getWorkerAvailability05() {

	}
	
	@Test
	public void getWorkerAvailability06() {

	}
	
	
	@Test
	public void addEmployee01() {

	}
	
	@Test
	public void addEmployee02() {

	}
	
	@Test
	public void addEmployee03() {

	}
	
	@Test
	public void addEmployee04() {

	}
	
	@Test
	public void addEmployee05() {

	}
	
	@Test
	public void addEmployee06() {

	}
	
	
	@Test
	public void getEmployeeList01() {

	}
	
	@Test
	public void getEmployeeList02() {

	}
	
	@Test
	public void getEmployeeList03() {

	}
	
	@Test
	public void getEmployeeList04() {

	}
	
	@Test
	public void getEmployeeList05() {

	}
	
	@Test
	public void getEmployeeList06() {

	}
	
	
	@Test
	public void getCustomerList01() {

	}
	
	@Test
	public void getCustomerList02() {

	}
	
	@Test
	public void getCustomerList03() {

	}
	
	@Test
	public void getCustomerList04() {

	}
	
	@Test
	public void getCustomerList05() {

	}
	
	@Test
	public void getCustomerList06() {

	}
	
	
	@Test
	public void getServicesList01() {

	}
	
	@Test
	public void getServicesList02() {

	}
	
	@Test
	public void getServicesList03() {

	}
	
	@Test
	public void getServicesList04() {

	}
	
	@Test
	public void getServicesList05() {

	}
	
	@Test
	public void getServicesList06() {

	}
	
	
	@Test
	public void getEmployeeBookingAvailability01() {

	}
	
	@Test
	public void getEmployeeBookingAvailability02() {

	}
	
	@Test
	public void getEmployeeBookingAvailability03() {

	}
	
	@Test
	public void getEmployeeBookingAvailability04() {

	}
	
	@Test
	public void getEmployeeBookingAvailability05() {

	}
	
	@Test
	public void getEmployeeBookingAvailability06() {

	}
}
