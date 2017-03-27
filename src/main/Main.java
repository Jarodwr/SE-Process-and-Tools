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
		Controller bookingSystem = new Controller();
	}
	
	public static void debugCreateOwnerBusiness() {
		SQLiteConnection.createBusiness("SARJ's Milk Business", "Cherry Lane", "0123456789");
		SQLiteConnection.createOwner("SARJ's Milk Business", "Ownertest", "1234", "Name", "Address", "MobileNo");
	}
	
	public static void debugCreateBookingsTable() {
		SQLiteConnection.createBooking(1, "SARJ's Milk Business", "Gary", "20181011123000", "20181011173000", "test");
	}

}