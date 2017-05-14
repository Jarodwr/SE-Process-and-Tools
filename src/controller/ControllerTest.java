package controller;

import static org.junit.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import model.database.SQLiteConnection;
import model.exceptions.ValidationException;
import model.users.Owner;
import model.users.User;

public class ControllerTest{
	static Controller c;
	static Owner user = new Owner("name", "pass", "SARJ's Milk Business", "Admin", "124 Address", "0412345678");
	static SQLiteConnection db;
	
	@BeforeClass
	public static void setup()
	{
		new File("test.sqlite").delete();	//Deletes previous test database
		
		db = new SQLiteConnection("jdbc:sqlite:test.sqlite");
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
		
		c = new Controller("jdbc:sqlite:test.sqlite");
		
	}
	
	
	@Test
	public void getEmployeeList01() {
		String[] employees = c.getEmployeeList();
		assert(employees.length == 6);
	}
	
	
	@Test
	public void getCustomerList01() {
		String[] customers = c.getCustomerList();
		assert(customers.length == 7);
	}
	
	
	@Test
	public void getServicesList01() {
		String[] services = c.getServicesList();
		System.out.println(services.length);
		assert(services.length == 5);
	}
	
	
	@Test
	public void getSummaryOfBookings01() {
		String[][] bookings = c.getSummaryOfBookings();
		assert(bookings.length == 8);
	}
	

	@Test
	public void loginTest01() {
		assert(c.login("jarod", "naaahaahhaah") == null);
	}

	@Test
	public void loginTest02() {
		assert(false);
	}
	
	@Test
	public void loginTest03() {
		assert(c.login("yeahyeah", "nahnah") == null);
	}
	
	@Test
	public void loginTest04() {
		assert(!c.login("jarodwr", "1234").isOwner());
	}
	
	//owner logging in test cases
	@Test
	public void loginTest05() {
		assert(c.login("JoeDoe97", "ayylmao").isOwner());
	}
	
	
	//register a customer tests
	@Test
	public void registerTest01() {
		try {
			User customer = c.register("jarodwr", "asdfasdf", "asdfasdf", "jarod", "32 asa st", "0400440044");
			System.out.println(customer);
			assert(customer == null);
		} catch(ValidationException e) {

		}
	}
	
	@Test
	public void registerTest02() {
		try {
			User customer = c.register("!@#$%^&*()??//", "asdf", "asdf", "asdf", "test", "0412341234");
			assert(customer == null);
		} catch(ValidationException e) {

		}
	}
	
	@Test
	public void registerTest03() {
		try {
			User customer = c.register("bruceW", "asdf", "asdf", "bruce", "asdfasdf", "fffffffffffff");
			assert(customer == null);
		} catch(ValidationException e) {
			
		}
	}
	
	@Test
	public void registerTest04() {
		try {
			User customer = c.register("kill", "1234", "1234", "Jill", "12 who cares", "0412345687");
			assert(customer != null);
		} catch(ValidationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void registerTest05() {
		try {
			User customer = c.register("CameronO", "", "", "Cameron", "yeah yeah", "0412341234");
			assert(customer == null);
		} catch(ValidationException e) {
		}
	}
	
	@Test
	public void registerTest06() {
		try {
			User customer = c.register("", "1234", "1234", "1234", "asdf", "0412341234");
			assert(customer == null);
		} catch(ValidationException e) {
		}
	}
	
	
	//add new employee tests
	@Test
	public void addEmployeeTest01() {
		assert(!c.addEmployee("yeahnah", "0412341234", "12 blah st", null));
	}
	
	@Test
	public void addEmployeeTest02() {
		assert(!c.addEmployee("testEmployee", "04sdfg41234", "21 no one cares where you live", c.getOwner()));
	}
	
	@Test
	public void addEmployeeTest03() {
		assert(!c.addEmployee("", "0412341234", "21 no one cares where you live", c.getOwner()));
	}
	
	@Test
	public void addEmployeeTest04() {
		assert(!c.addEmployee("yoyo@#$%@#$yoyo", "0412341234", "21 no one cares where you live", c.getOwner()));
	}
	
	@Test
	public void addEmployeeTest05() {
		assert(!c.addEmployee("McMahon", "0412341234", "", c.getOwner()));
	}
	
	@Test
	public void addEmployeeTest06() {
		assert(c.addEmployee("bowser", "0412341234", "21 no one cares where you live", c.getOwner()));
	}
	
	
	@Test
	public void addService01() {
		assert(!c.addService("?><><??!!@##$%^", 10000, "30"));
	}
	
	@Test
	public void addService02() {
		assert(!c.addService("Light Massage", 10000, "90"));
	}
	
	@Test
	public void addService03() {
		assert(!c.addService("New service", 2500, "-60"));
	}
	
	@Test
	public void addService04() {
		assert(!c.addService("Absolutely new service", -2600, "60"));
	}
	
	@Test
	public void addService05() {
		assert(c.addService("Definitely a new service", 9000, "90"));
	}
	
	@Test
	public void addService06() {
		assert(!c.addService("Service with incorrect duration", 2000, "80"));
	}
	
	
	@Test
	public void getCurrentBookings01() {
		String[][] bookings = c.getCurrentBookings();
		assert(bookings == null);
	}
	
	@Test
	public void getCurrentBookings02() {
		db.createBooking("Massage Business", "jarodwr", "2", "1918726200", "1918728000", "Light Massage");	//TODO: Currently freezes the test
		String[][] bookings = c.getCurrentBookings();
		assert(bookings != null);
	}
	
	
	@Test
	public void addNewBooking01() {
		assert(c.addNewBooking("jarodwr", "1497524400", "Light Massage", "3"));
	}
	
	@Test
	public void addNewBooking02() {
		assert(!c.addNewBooking("jarodwr", "1497524400", "Light Massage", "3"));
	}
	
	@Test
	public void addNewBooking03() {
		assert(!c.addNewBooking("kate", "1497524500", "Light Massage", "3"));
	}
	
	@Test
	public void addNewBooking04() {
		assert(c.addNewBooking("kate", "1497526200", "Light Massage", "3"));
	}
	
	@Test
	public void addNewBooking05() {
		assert(!c.addNewBooking("non existent user", "1497506400", "Light Massage", "-1"));
	}
	
	@Test
	public void addNewBooking06() {
		assert(!c.addNewBooking("jarodwr", "1497506400", "Light Massage", "2"));
	}
	
	
	@Test
	public void removeBookings01() {
		assert(!c.removeBooking(-1, "Massage Business"));
	}
	
	@Test
	public void removeBookings02() {
		assert(!c.removeBooking(2, ""));
	}
	
	@Test
	public void removeBookings03() {
		assert(c.removeBooking(1, "Massage Business"));
	}
	
	@Test
	public void removeBookings04() {
		assert(c.removeBooking(60, "Massage Business"));
	}
	
	@Test
	public void removeBookings05() {
		assert(c.removeBooking(0, "Massage Business"));
	}
	
	
	@Test
	public void addWorkingTime01() {
		assert(!c.addWorkingTime("1", "86400", "100", "50"));
	}
	
	@Test
	public void addWorkingTime02() {
		assert(!c.addWorkingTime("1", "86400", "100", "100"));
	}
	
	@Test
	public void addWorkingTime03() {
		assert(!c.addWorkingTime("1", "86400", "-100", "100"));
	}
	
	@Test
	public void addWorkingTime04() {
		assert(c.addWorkingTime("1", "86400", "1000", "10000"));
	}
	
	
	@Test
	public void getWorkingTimes01() {
		String[][] workingTimes = c.getWorkingTimes("1");
		assert(workingTimes[0][0].equals("1497513600"));
	}
	
	@Test
	public void getWorkingTimes02() {
		String[][] workingTimes = c.getWorkingTimes("2");
		assert(workingTimes[0][0].equals("1497506400"));
	}
	
	@Test
	public void getWorkingTimes03() {
		String[][] workingTimes = c.getWorkingTimes("3");
		assert(workingTimes[0][0].equals("1497508200"));
		assert(workingTimes[1][0].equals("1497522600"));
	}
	
	@Test
	public void getWorkingTimes04() {
		String[][] workingTimes = c.getWorkingTimes("-1");
		assert(workingTimes == null || workingTimes.length == 0);
	}
	
	@Test
	public void getWorkingTimes05() {
		String[][] workingTimes = c.getWorkingTimes("10");
		assert(workingTimes == null || workingTimes.length == 0);
	}
	
	
	@Test
	public void getWorkerAvailability01() {
		String[][] availability = c.getWorkerAvailability("0");
		assert(availability[0][0].equals("1800"));
		assert(availability[1][0].equals("27000"));
		assert(availability[2][0].equals("441000"));
		assert(availability[3][0].equals("446400"));
		assert(availability[4][0].equals("450000"));
		assert(availability[5][0].equals("453600"));
		assert(availability[6][0].equals("457200"));
		assert(availability[7][0].equals("460800"));
	}
	
	@Test
	public void getWorkerAvailability02() {
		String[][] availability = c.getWorkerAvailability("1");
		assert(availability[0][0].equals("1800"));
		assert(availability[1][0].equals("27000"));
		assert(availability[2][0].equals("446400"));
		assert(availability[3][0].equals("450000"));
		assert(availability[4][0].equals("453600"));
		assert(availability[5][0].equals("457200"));
		assert(availability[6][0].equals("460800"));
	}
	
	@Test
	public void getWorkerAvailability03() {
		String[][] availability = c.getWorkerAvailability("2");
		assert(availability[0][0].equals("1800"));
		assert(availability[1][0].equals("27000"));
	}
	
	@Test
	public void getWorkerAvailability04() {
		String[][] availability = c.getWorkerAvailability("3");
		assert(availability[0][0].equals("1800"));
		assert(availability[1][0].equals("27000"));
	}
	
	@Test
	public void getWorkerAvailability05() {
		String[][] availability = c.getWorkerAvailability("10");
		assert(availability == null || availability.length == 0);
	}
	
	@Test
	public void getWorkerAvailability06() {
		String[][] availability = c.getWorkerAvailability("-1");
		assert(availability == null || availability.length == 0);
	}
	
	
	@Test
	public void getEmployeeBookingAvailability01() {
		String[][] bookingAvailability = c.getEmployeeBookingAvailability("0", new Date(0));
		assert(bookingAvailability[0][0].equals("1497544200"));
		assert(bookingAvailability[1][0].equals("1497506400"));
	}
	
	@Test
	public void getEmployeeBookingAvailability02() {
		String[][] bookingAvailability = c.getEmployeeBookingAvailability("3", new Date(0));
		assert(bookingAvailability[0][0].equals("1497522600"));
		assert(bookingAvailability[1][0].equals("1497510000"));
	}
	
	@Test
	public void getEmployeeBookingAvailability03() {
		assert(c.getEmployeeBookingAvailability("3", new Date(2147410000)).length == 0);
	}
}
