package main;

import java.util.Date;

import bookings.Booking;
import controller.Controller;
import database.SQLiteConnection;
import period.Period;

public class Main {

	public static void main(String[] args) {
		SQLiteConnection.createTables(); // Create table if it doesn't exist for all info
		debugCreateOwnerBusiness();
		debugCreateBookingsTable();
		debugCreateEmptyAvailability();
		debugCreateEmployees();
		Controller bookingSystem = new Controller();
		bookingSystem.run();
	}
	
	public static void debugCreateOwnerBusiness() {
		SQLiteConnection.createBusiness("SARJ's Milk Business", "Cherry Lane", "0123456789");
		SQLiteConnection.createOwner("SARJ's Milk Business", "Ownertest", "1234", "Name", "Address", "MobileNo");
	}
	
	public static void debugCreateBookingsTable() {
		SQLiteConnection.createBooking(1, "SARJ's Milk Business", "Gary", "20181011123000", "20181011173000", "test");
		SQLiteConnection.createBooking(2, "SARJ's Milk Business", "Joe", "20161011123000", "20181011173000", "test");
		SQLiteConnection.createBooking(3, "SARJ's Milk Business", "Bob", "20170409143001", "20181011173000", "test");
		SQLiteConnection.createBooking(4, "SARJ's Milk Business", "Bill", "20170409123000", "20181011173000", "test");
	}
	
	public static void debugCreateEmptyAvailability() {
		SQLiteConnection.createAvailability(0, "SARJ's Milk Business", "20181011123000,20181011173000|20181012123000,20181012173000");
		SQLiteConnection.createAvailability(1, "SARJ's Milk Business", "20181011123000,20181011173000|20181012123000,20181012173000");
		SQLiteConnection.createAvailability(2, "SARJ's Milk Business", "20181011123000,20181011173000|20181012123000,20181012173000");
		SQLiteConnection.createAvailability(3, "SARJ's Milk Business", "20181011123000,20181011173000|20181012123000,20181012173000");
	}
	
	public static void debugCreateEmployees() {
		SQLiteConnection.createEmployee(0, "SARJ's Milk Business", "Bob", "32 does not exists st", "0412345678", 0);
		SQLiteConnection.createEmployee(1, "SARJ's Milk Business", "John", "32 does not exists st", "0412345678", 1);
		SQLiteConnection.createEmployee(2, "SARJ's Milk Business", "Alex", "32 does not exists st", "0412345678", 2);
		SQLiteConnection.createEmployee(3, "SARJ's Milk Business", "Jack", "32 does not exists st", "0412345678", 3);
	}

}