package model.utility;

import java.io.File;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import model.database.SQLiteConnection;
import model.period.Booking;
import model.period.Period;
import model.timetable.Timetable;

public class UtilityTest {
	
	static Utility u = new Utility();

	@BeforeClass
	public static void createDB() {
		new File("test.sqlite").delete();	//Deletes previous test database
		
		SQLiteConnection db = new SQLiteConnection("jdbc:sqlite:test.sqlite");
		db.createTables();
		u.setConnection(db);
		u.setCurrentBusiness("Massage Business");
		
//		db.createBusiness(businessname, address, phonenumber)
		db.createBusiness("Massage Business", "123 nicholson st", "040303030303");
		
//		db.createOwner(businessname, username, password, name, address, mobileno)
		db.createOwner("Massage Business", "JoeDoe97", "ayylmao", "Joe", "123 swanston st", "0491827462");
		
//		db.createCustomer(username, password, name, address, mobileno);
		db.createCustomer("jarodwr", "1234", "Jarod", "32 der st", "0412341234");
		db.createCustomer("yargen", "asdf", "Bruce", "67 data st", "0412376534");
		db.createCustomer("kate", "zxcv", "Andy", "A street that doesn't exist", "043841234");
		db.createCustomer("bill", "uuuuuuuuuu", "Dave", "Junk location data", "0412340294");
		db.createCustomer("death", "life", "Brandon", "Southern cross station", "0411821234");
		db.createCustomer("grips", "yehnahyehyehnah", "Will", "entry dataaaaaaaaa", "0412900234");
		
//		db.createAvailability(timetableId, businessname, availabilities);
		db.createAvailability(1, "Massage Business", "1800,14399|27000,86399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		db.createAvailability(2, "Massage Business", "1800,14399|27000,86399|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		db.createAvailability(3, "Massage Business", "1800,14399|27000,86399");
		db.createAvailability(4, "Massage Business", "1800,14399|27000,86399");
		db.createAvailability(5, "Massage Business", "1800,14399|27000,86399");
		db.createAvailability(6, "Massage Business", "1800,14399|27000,86399|189000,203399|441000,442799|446400,448199|450000,451799|453600,455399|457200,458999|460800,462599");
		
//		db.createEmployee(businessname, name, address, mobileno, timetableId)
		db.createEmployee("Massage Business", "spencer", "any", "0394815108", 1);
		db.createEmployee("Massage Business", "russel", "somewhere", "9382738491", 2);
		db.createEmployee("Massage Business", "jarod", "nowhere", "1738593827", 3);
		db.createEmployee("Massage Business", "anesu", "anywhere", "2918273647", 4);
		db.createEmployee("Massage Business", "anthrax", "blah", "9283746374", 5);
		db.createEmployee("Massage Business", "marvin", "yes", "0192837465", 6);
		
//		db.addShift(employeeId, businessname, start, end)
		db.addShift(0, "Massage Business", "1497544200", "1497546000");	//06/15/2017 @ 4:30pm-5:00pm (UTC)
		db.addShift(0, "Massage Business", "1497506400", "1497513600");
		db.addShift(1, "Massage Business", "1497513600", "1497520800");
		db.addShift(2, "Massage Business", "1497506400", "1497565800");
		db.addShift(3, "Massage Business", "1497508200", "1497520800");
		db.addShift(3, "Massage Business", "1497522600", "1497565800");
		db.addShift(4, "Massage Business", "1497508200", "1497522600");
		db.addShift(5, "Massage Business", "1497555000", "1497565800");

//		db.addService(serviceName, servicePrice, serviceMinutes, businessName)
		db.addService("Light Massage", 1000, 30, "Massage Business");
		db.addService("Heavy Massage", 3000, 90, "Massage Business");
		db.addService("Hair cut", 1500, 60, "Massage Business");
		db.addService("Spa", 5000, 120, "Massage Business");
		
//		db.createBooking(businessname, customername, employeeId, unixstamp1, unixstamp2, data)
		db.createBooking("Massage Business", "jarodwr", "0", "1497549600", "1497555000", "Light Massage");
		db.createBooking("Massage Business", "yargen", "0", "1497510000", "1497513600", "Hair cut");
		db.createBooking("Massage Business", "derkaderka", "1", "1497555000", "1497558600", "Hair cut");
		db.createBooking("Massage Business", "yargen", "2", "1497558600", "1497565800", "Spa");
		db.createBooking("Massage Business", "death", "2", "1497513600", "1497520800", "Spa");
		db.createBooking("Massage Business", "death", "3", "1497520800", "1497522600", "Heavy Massage");
		db.createBooking("Massage Business", "grips", "3", "1497508200", "1497510000", "Heavy Massage");
		db.createBooking("Massage Business", "derkaderka", "4", "1497506400", "1497508200", "Heavy Massage");
		
	}
	
	@Test
	public void searchUser1() {
		assert(u.searchUser("yargen").getUsername().equals("yargen"));
	}
	
	@Test
	public void searchUser2() {
		assert(u.searchUser("bill").getUsername().equals("bill"));
	}
	
	@Test
	public void searchUser3() {
		assert(u.searchUser("kate").getUsername().equals("kate"));
	}
	
	@Test
	public void searchUser4() {
		assert(u.searchUser("jarodwr").getUsername().equals("jarodwr"));
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

	
	@Test
	public void getEmployeeList1() {
		
		assert(u.getEmployeeList().length == 6);
	}
	
	@Test
	public void getEmployeeList2() {
		assert(u.getEmployeeList()[0][1].equals("spencer"));
		assert(u.getEmployeeList()[1][1].equals("russel"));
		assert(u.getEmployeeList()[2][1].equals("jarod"));
		assert(u.getEmployeeList()[3][1].equals("anesu"));
		assert(u.getEmployeeList()[4][1].equals("anthrax"));
		assert(u.getEmployeeList()[5][1].equals("marvin"));
	}
	
	@Test
	public void getEmployeeList3() {
		assert(u.getEmployeeList() != null);
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
		assert(b.length == 8);
	}
	
	@Test
	public void getBookingsAfter2() {
		Booking[] b = u.getBookingsAfter(new Date(1497506400));
		assert(b.length == 8);
	}
	
	@Test
	public void getBookingsAfter3() {
		Booking[] b = u.getBookingsAfter(new Date(1497558600));
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
		assert(b.length == 1);
	}
	
	@Test
	public void getBookingsAfter6() {
		Booking[] b = u.getBookingsAfter(new Date(1497506401));
		assert(b.length == 7);
	}
	
	
	@Test
	public void getEmployeeBookingAvailability1() {
		assert(u.getEmployeeBookingAvailability("0", new Date(0)).toString().equals("1497544200,1497546000|1497506400,1497513600"));
	}
	
	@Test
	public void getEmployeeBookingAvailability2() {
		assert(u.getEmployeeBookingAvailability("3", new Date(0)).toString().equals("1497510000,1497520800|1497522600,1497524400|1497528000,1497565800"));
	}
	
	@Test
	public void getEmployeeBookingAvailability3() {
		assert(u.getEmployeeBookingAvailability("3", new Date(2147410000)).getAllPeriods().length == 0);
	}
	
	@Test
	public void getEmployeeBookingAvailability4() {
		assert(u.getEmployeeBookingAvailability("3", new Date(1497522600)).getAllPeriods().length == 1);
	}
	
	@Test
	public void getEmployeeBookingAvailability5() {
		for (Period p : u.getEmployeeBookingAvailability("3", new Date(1497522600)).getAllPeriods()) {
			assert(p.getStart().getTime() != 1497508200);
		}
	}
	
	@Test
	public void getEmployeeBookingAvailability6() {
		assert(u.getEmployeeBookingAvailability("3", new Date(1497522601)).getAllPeriods().length == 0);
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
		assert(!u.removeBooking(-1, "Massage Business"));
	}
	
	@Test
	public void removeBooking2() {
		assert(!u.removeBooking(0, ""));
	}
	
	@Test
	public void removeBooking3() {
		assert(u.removeBooking(1, "Massage Business"));
	}
	
	@Test
	public void removeBooking4() {
		assert(!u.removeBooking(60, "Massage Business"));
	}
	
	@Test
	public void removeBooking5() {
		assert(!u.removeBooking(0, "Massage Business"));
	}
	
	
	@Test
	public void getShift1() {
		assert(u.getShift("0").toString().equals("1497544200,1497546000|1497506400,1497513600"));
	}
	
	@Test
	public void getShift2() {
		assert(u.getShift("1").toString().equals("1497513600,1497520800"));
	}
	
	@Test
	public void getShift3() {
		assert(u.getShift("2").toString().equals("1497506400,1497565800"));
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
		assert(!u.addCustomerToDatabase("jarodwr", "asdfasdf", "jarod", "32 asa st", "0400440044"));
	}
	
	@Test
	public void addCustomer2() {
		assert(!u.addCustomerToDatabase("!@#$%^&*()??//", "asdf", "asdf", "asdf", "0412341234"));
	}
	
	@Test
	public void addCustomer3() {
		assert(!u.addCustomerToDatabase("bruceW", "asdf", "bruce", "asdfasdf", "fffffffffffff"));
	}
	
	@Test
	public void addCustomer4() {
		assert(u.addCustomerToDatabase("kill", "1234", "Jill", "who cares", "0412345687"));
	}
	
	@Test
	public void addCustomer5() {
		assert(!u.addCustomerToDatabase("CameronO", "", "Cameron", "yeah yeah", "0412341234"));
	}
	
	@Test
	public void addCustomer6() {
		assert(!u.addCustomerToDatabase("", "1234", "1234", "asdf", "0412341234"));
	}
	
	
	@Test
	public void addEmployee1() {
		assert(!u.addNewEmployee("1000", "Massage Business", "randomname", "544 sdffds", "0412341234", 0));
	}
	
	@Test
	public void addEmployee2() {
		assert(u.addNewEmployee("2000", "Massage Business", "mike", "21 no one cares where you live", "0412341234", 2000));
	}
	
	@Test
	public void addEmployee3() {
		assert(!u.addNewEmployee("1", "Massage Business", "asdsads", "asdfsadf", "0412341234", 9000));
	}
	
	@Test
	public void addEmployee4() {
		assert(!u.addNewEmployee("30", "non existent business", "brandon", "57 newry", "0412341234", 30));
	}
	
	@Test
	public void addEmployee5() {
		assert(u.addNewEmployee("2001", "Massage Business", "mike", "21 no one cares where you live", "", 2001));
	}
	
	@Test
	public void addEmployee6() {
		assert(u.addNewEmployee("2002", "Massage Business", "", "21 no one cares where you live", "0412341234", 2002));
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
		assert(u.getAllServices().length == 4);
	}
	
	@Test
	public void getAllServices2() {
		assert(u.getAllServices()[0].serviceName.equals("Light Massage"));
		assert(u.getAllServices()[1].serviceName.equals("Heavy Massage"));
		assert(u.getAllServices()[2].serviceName.equals("Hair cut"));
		assert(u.getAllServices()[3].serviceName.equals("Spa"));
	}
	
}
