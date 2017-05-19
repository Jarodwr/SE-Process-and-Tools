package model.utility;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import model.database.SQLMaster;
import model.database.SQLiteConnection;
import model.period.Booking;
import model.period.Period;
import model.service.Service;
import model.timetable.Timetable;
import model.users.User;

public class UtilityTest {
	
	static Utility u = new Utility();

	@BeforeClass
	public static void createDB() throws SQLException {
		new File("test.sqlite").delete();	//Deletes previous test database
		new File("TESTMasterDB.sqlite").delete();
		SQLiteConnection db = new SQLiteConnection("test");
		SQLMaster masterDB = new SQLMaster("test");
		u.setConnection("test");
		
//		db.createBusiness(businessname, address, phonenumber)
		masterDB.createBusiness("Massage Business", "123 nicholson st", "040303030303");
		
//		db.createOwner(businessname, username, password, name, address, mobileno)
		masterDB.createOwner("Massage Business", "JoeDoe97", "ayylmao", "Joe", "123 swanston st", "0491827462");
		
//		db.createCustomer(username, password, name, address, mobileno);
		db.createCustomer("jarodwr", "1234", "Massage Business", "Jarod", "32 der st", "0412341234");
		db.createCustomer("yargen", "asdf", "Massage Business", "Bruce", "67 data st", "0412376534");
		db.createCustomer("kate", "zxcv", "Massage Business", "Andy", "A street that doesn't exist", "043841234");
		db.createCustomer("bill", "uuuuuuuuuu", "Massage Business", "Dave", "Junk location data", "0412340294");
		db.createCustomer("death", "life", "Massage Business", "Brandon", "Southern cross station", "0411821234");
		db.createCustomer("grips", "yehnahyehyehnah", "Massage Business", "Will", "entry dataaaaaaaaa", "0412900234");
		
//		db.createAvailability(timetableId, businessname, availabilities);
		db.createAvailability(1, "1800,14399|27000,86399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		db.createAvailability(2, "1800,14399|27000,86399|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		db.createAvailability(3, "1800,14399|27000,86399");
		db.createAvailability(4, "1800,14399|27000,86399");
		db.createAvailability(5, "1800,14399|27000,86399");
		db.createAvailability(6, "1800,14399|27000,86399|189000,203399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		
//		db.createEmployee(businessname, name, address, mobileno, timetableId)
		db.createEmployee("spencer", "any", "0394815108", 1);
		db.createEmployee("russel", "somewhere", "9382738491", 2);
		db.createEmployee("jarod", "nowhere", "1738593827", 3);
		db.createEmployee("anesu", "anywhere", "2918273647", 4);
		db.createEmployee("anthrax", "blah", "9283746374", 5);
		db.createEmployee("marvin", "yes", "0192837465", 6);
		
//		db.addShift(employeeId, businessname, start, end)
		db.addShift(0, "1497544200", "1497546000");	//06/15/2017 @ 4:30pm-5:00pm (UTC)
		db.addShift(0, "1497506400", "1497513600");
		db.addShift(1, "1497513600", "1497520800");
		db.addShift(2, "1497506400", "1497565800");
		db.addShift(3, "1497508200", "1497520800");
		db.addShift(3, "1497522600", "1497565800");
		db.addShift(4, "1497508200", "1497522600");
		db.addShift(5, "1497555000", "1497565800");

//		db.addService(serviceName, servicePrice, serviceMinutes, businessName)
		db.addService("Light Massage", 1000, 30);
		db.addService("Heavy Massage", 3000, 90);
		db.addService("Hair cut", 1500, 60);
		db.addService("Spa", 5000, 120);
		
//		db.createBooking(businessname, customername, employeeId, unixstamp1, unixstamp2, data)
		db.createBooking("jarodwr", "0", "1497549600", "1497555000", "Light Massage");
		db.createBooking("yargen", "0", "1497510000", "1497513600", "Hair cut");
		db.createBooking("derkaderka", "1", "1497555000", "1497558600", "Hair cut");
		db.createBooking("yargen", "2", "1497558600", "1497565800", "Spa");
		db.createBooking("death", "2", "1497513600", "1497520800", "Spa");
		db.createBooking("death", "3", "1497520800", "1497522600", "Heavy Massage");
		db.createBooking("grips", "3", "1497508200", "1497510000", "Heavy Massage");
		db.createBooking("derkaderka", "4", "1497506400", "1497508200", "Heavy Massage");
		
		u.searchUserLogin("JoeDoe97", "Massage Business");
	}
	
	@Test
	public void searchUser1() {
		User user = u.searchUser("yargen");
		assert(user != null);
		assert(user.getUsername().equals("yargen"));
	}
	
	@Test
	public void searchUser2() {
		User user = u.searchUser("bill");
		assert(user != null);
		assert(user.getUsername().equals("bill"));
	}
	
	@Test
	public void searchUser3() {
		User user = u.searchUser("kate");
		assert(user != null);
		assert(user.getUsername().equals("kate"));
	}
	
	@Test
	public void searchUser4() {
		User user = u.searchUser("jarodwr");
		assert(user != null);
		assert(user.getUsername().equals("jarodwr"));
	}
	
	@Test
	public void searchUser5() {
		assert(u.searchUser("jarodw") == null);
	}
	
	@Test
	public void searchUser6() {
		assert(u.searchUser("") == null);
	}
	
	
	@Test
	public void authenticate1() {
		assert(u.authenticate("jarodwr", "1234") != null);
	}
	
	@Test
	public void authenticate2() {
		assert(u.authenticate("yargen", "asdf") != null);
	}
	
	@Test
	public void authenticate3() {
		assert(u.authenticate("kate", "zxcv") != null);
	}
	
	@Test
	public void authenticate4() {
		assert(u.authenticate("No user with this username", "1234") == null);
	}
	
	@Test
	public void authenticate5() {
		assert(u.authenticate("jarodwr", "") == null);
	}
	
	@Test
	public void authenticate6() {
		assert(u.authenticate("", "") == null);
	}
	
	//TODO: All these tests are testing the wrong functionality
	@Test
	public void getAvailableTimes1() {
		Timetable t = u.getEmployeeAvailability("0");
		assert(t.toString().equals("1800,14399|27000,86399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599"));
	}
	
	@Test
	public void getAvailableTimes2() {
		Timetable t = u.getEmployeeAvailability("1");
		assert(t.toString().equals("1800,14399|27000,86399|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599"));
	}
	
	@Test
	public void getAvailableTimes3() {
		Timetable t = u.getEmployeeAvailability("2");
		assert(t.toString().equals("1800,14399|27000,86399"));
	}
	
	@Test
	public void getAvailableTimes4() {
		Timetable t = u.getEmployeeAvailability("3");
		assert(t.toString().equals("1800,14399|27000,86399"));
	}
	
	@Test
	public void getAvailableTimes5() {
		Timetable t = u.getEmployeeAvailability("4");
		assert(t.toString().equals("1800,14399|27000,86399"));
	}
	
	@Test
	public void getAvailableTimes6() {
		Timetable t = u.getEmployeeAvailability("5");
		assert(t.toString().equals("1800,14399|27000,86399|189000,203399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599"));
	}
	
	@Test
	public void getBookingsAfter1() {
		Booking[] b = u.getBookingsAfter(new Date(0));
		assert(b != null);
		assert(b.length == 8);
	}
	
	@Test
	public void getBookingsAfter2() {
		Booking[] b = u.getBookingsAfter(new Date(1497506400));
		assert(b != null);
		assert(b.length == 8);
	}
	
	@Test
	public void getBookingsAfter3() {
		Booking[] b = u.getBookingsAfter(new Date(1497558600));
		assert(b != null);
		assert(b.length == 1);
	}
	
	@Test
	public void getBookingsAfter4() {
		Booking[] b = u.getBookingsAfter(new Date(1497558601));
		assert(b == null);
	}
	
	@Test
	public void getBookingsAfter5() {
		Booking[] b = u.getBookingsAfter(new Date(1497558599));
		assert(b != null);
		assert(b.length == 1);
	}
	
	@Test
	public void getBookingsAfter6() {
		Booking[] b = u.getBookingsAfter(new Date(1497506401));
		assert(b != null);
		assert(b.length == 7);
	}
	
	
	@Test
	public void getEmployeeBookingAvailability1() {
		Timetable t = u.getEmployeeBookingAvailability("0", new Date(0));
		assert(t != null);
		assert(t.toString().equals("1497544200,1497546000|1497506400,1497513600"));
	}
	
	@Test
	public void getEmployeeBookingAvailability2() {
		Timetable t = u.getEmployeeBookingAvailability("3", new Date(0));
		assert(t != null);
		assert(t.toString().equals("1497510000,1497520800|1497522600,1497524400|1497528000,1497565800"));
	}
	
	@Test
	public void getEmployeeBookingAvailability3() {
		Timetable t = u.getEmployeeBookingAvailability("3", new Date(2147410000));
		assert(t == null || t.getAllPeriods().length == 0);
	}
	
	@Test
	public void getEmployeeBookingAvailability4() {
		Timetable t = u.getEmployeeBookingAvailability("3", new Date(1497522600));
		assert(t != null);
		assert(t.getAllPeriods().length == 1);
	}
	
	@Test
	public void getEmployeeBookingAvailability5() {
		Timetable t = u.getEmployeeBookingAvailability("3", new Date(1497522600));
		assert(t != null);
		for (Period p : t.getAllPeriods()) {
			assert(p.getStart().getTime() != 1497508200);
		}
	}
	
	@Test
	public void getEmployeeBookingAvailability6() {
		Timetable t = u.getEmployeeBookingAvailability("3", new Date(1497522601));
		assert(t == null || t.getAllPeriods().length == 0);
	}
	
	
	@Test
	public void addNewBooking1() {
		assert(u.addNewBooking("jarodwr", "3", "1497524400", "1497526200", "Light Massage"));
	}
	
	@Test
	public void addNewBooking2() {
		assert(!u.addNewBooking("jarodwr", "3", "1497524400", "1497526200", "Light Massage"));
	}
	
	@Test
	public void addNewBooking3() {
		assert(!u.addNewBooking("kate", "3", "1497524500", "1497526300", "Light Massage"));
	}
	
	@Test
	public void addNewBooking4() {
		assert(u.addNewBooking("kate", "3", "1497526200", "1497528000", "Light Massage"));
	}
	
	@Test
	public void addNewBooking5() {
		assert(!u.addNewBooking("userdoesn'texist", "2", "1497506400", "1497508200", "Light Massage"));
	}
	
	@Test
	public void addNewBooking6() {
		assert(!u.addNewBooking("jarodwr", "-1", "1497506400", "1497508200", "Light Massage"));
	}
	
	@Test
	public void addNewBooking7() {
		assert(!u.addNewBooking("jarodwr", "2", "1497506400", "1497508200", "serviceDoesn'tExist"));
	}
	
	
	@Test
	public void removeBooking1() {
		assert(!u.removeBooking(-1));
	}
	
	@Test
	public void removeBooking2() {
		assert(!u.removeBooking(0));
	}
	
	@Test
	public void removeBooking3() {
		assert(u.removeBooking(1));
	}
	
	@Test
	public void removeBooking4() {
		assert(!u.removeBooking(60));
	}
	
	@Test
	public void removeBooking5() {
		assert(!u.removeBooking(0));
	}
	
	
	@Test
	public void getShift1() {
		Timetable t = u.getShift("0");
		assert(t != null);
		assert(t.toString().equals("1497544200,1497546000|1497506400,1497513600"));
	}
	
	@Test
	public void getShift2() {
		Timetable t = u.getShift("1");
		assert(t != null);
		assert(t.toString().equals("1497513600,1497520800"));
	}
	
	@Test
	public void getShift3() {
		Timetable t = u.getShift("2");
		assert(t != null);
		assert(t.toString().equals("1497506400,1497565800"));
	}
	
	
	@Test
	public void addShift1() {
		assert(!u.addShift("1", "86400", "100", "50"));
	}
	
	@Test
	public void addShift2() {
		assert(!u.addShift("1", "86400", "100", "100"));
	}
	
	@Test
	public void addShift3() {
		assert(!u.addShift("1", "86400", "-100", "100"));
	}
	
	@Test
	public void addShift4() {
		assert(u.addShift("1", "86400", "1000", "10000"));
	}
	
	
	@Test
	public void addCustomer1() {
		assert(!u.addCustomerToDatabase("jarodwr", "asdfasdf", "Massage Business", "jarod", "32 asa st", "0400440044"));
	}
	
	@Test
	public void addCustomer2() {
		assert(!u.addCustomerToDatabase("!@#$%^&*()??//", "asdf", "Massage Business", "asdf", "asdf", "0412341234"));
	}
	
	@Test
	public void addCustomer3() {
		assert(!u.addCustomerToDatabase("bruceW", "asdf", "Massage Business", "bruce", "asdfasdf", "fffffffffffff"));
	}
	
	@Test
	public void addCustomer4() {
		assert(u.addCustomerToDatabase("kill", "1234", "Massage Business", "Jill", "who cares", "0412345687"));
	}
	
	@Test
	public void addCustomer5() {
		assert(!u.addCustomerToDatabase("CameronO", "", "Massage Business", "Cameron", "yeah yeah", "0412341234"));
	}
	
	@Test
	public void addCustomer6() {
		assert(!u.addCustomerToDatabase("", "1234", "Massage Business", "1234", "asdf", "0412341234"));
	}
	
	
	@Test
	public void addEmployee1() {
		assert(!u.addNewEmployee("1000", "randomname", "544 sdffds", "0412341234", 0));
	}
	
	@Test
	public void addEmployee2() {
		assert(u.addNewEmployee("2000", "mike", "21 no one cares where you live", "0412341234", 2000));
	}
	
	@Test
	public void addEmployee3() {
		assert(!u.addNewEmployee("1", "asdsads", "asdfsadf", "0412341234", 9000));
	}
	
	@Test
	public void addEmployee4() {
		assert(!u.addNewEmployee("30", "brandon", "57 newry", "0412341234", 30));
	}
	
	@Test
	public void addEmployee5() {
		assert(u.addNewEmployee("2001", "mike", "21 no one cares where you live", "", 2001));
	}
	
	@Test
	public void addEmployee6() {
		assert(u.addNewEmployee("2002", "", "21 no one cares where you live", "0412341234", 2002));
	}
	
	
	@Test
	public void getEmployeeAvailability1() {
		assert(u.getEmployeeAvailability("0").toString().equals("1800,14399|27000,86399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599"));
	}
	
	@Test
	public void getEmployeeAvailability2() {
		assert(u.getEmployeeAvailability("1").toString().equals("1800,14399|27000,86399|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599"));
	}
	
	@Test
	public void getEmployeeAvailability3() {
		assert(u.getEmployeeAvailability("1").toString().equals("1800,14399|27000,86399"));
	}
	
	
	@Test
	public void getAllCustomers1() {
		assert(u.getAllCustomers().length == 6);
	}
	
	@Test
	public void getAllCustomers2() {
		assert(u.getAllCustomers()[0].getUsername().equals("jarodwr"));
		assert(u.getAllCustomers()[1].getUsername().equals("yargen"));
		assert(u.getAllCustomers()[2].getUsername().equals("kate"));
		assert(u.getAllCustomers()[3].getUsername().equals("bill"));
		assert(u.getAllCustomers()[4].getUsername().equals("death"));
		assert(u.getAllCustomers()[5].getUsername().equals("grips"));
	}
	
	@Test
	public void getAllCustomers3() {
		assert(u.getAllCustomers() != null);

	}
	
	
	@Test
	public void getAllServices1() {
		Service[] services = u.getAllServices();
		assert(services != null);
		assert(services.length == 4);
	}
	
	@Test
	public void getAllServices2() {
		Service[] services = u.getAllServices();
		assert(services != null);
		assert(services[0].serviceName.equals("Light Massage"));
		assert(services[1].serviceName.equals("Heavy Massage"));
		assert(services[2].serviceName.equals("Hair cut"));
		assert(services[3].serviceName.equals("Spa"));
	}
	

}
