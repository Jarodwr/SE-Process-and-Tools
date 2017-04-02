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
		SQLiteConnection.createBooking(5, "SARJ's Milk Business", "Bob", "20170409143001", "20181011173000", "test");
		SQLiteConnection.createBooking(6, "SARJ's Milk Business", "Bill", "20170409123000", "20181011173000", "test");
	}
	
	public static void debugCreateEmptyAvailability() {
		SQLiteConnection.createAvailability(0, "SARJ's Milk Business", " ");
	}

}