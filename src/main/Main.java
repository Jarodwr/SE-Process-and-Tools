package main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import controller.Controller;
import model.database.SQLiteConnection;
import model.service.Service;

public class Main {
	
	
	public static void main(String[] args) {
		Logger LOGGER = Logger.getLogger("main");
		
		Handler handler;
		try {
			handler = new FileHandler("logs\\" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime()) + ".txt");
			LOGGER.setLevel(Level.FINEST);
			handler.setLevel(Level.FINEST);
			
		}catch(IOException e) {
			handler = new ConsoleHandler();
			LOGGER.setLevel(Level.WARNING);
			handler.setLevel(Level.WARNING);
			LOGGER.warning("Cannot create logging file, using console logger");
		}
		handler.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(handler);
		LOGGER.setUseParentHandlers(false);
		LOGGER.warning("test");
		
		SQLiteConnection.createTables(); // Create table if it doesn't exist for all info
		debugCreateOwnerBusiness();
		debugCreateBookingsTable();
		//debugCreateEmptyAvailability();
		debugCreateEmployees();
		debugCreateService();
		Controller bookingSystem = new Controller(args);
		bookingSystem.run();
	}
	
	public static void debugCreateOwnerBusiness() {
		SQLiteConnection.createBusiness("SARJ's Milk Business", "Cherry Lane", "0123456789");
		SQLiteConnection.createOwner("SARJ's Milk Business", "Ownertest", "1234", "Name", "Address", "MobileNo");
	}
	
	public static void debugCreateService() {
		SQLiteConnection.addService("test", 20000, 15*60, "SARJ's Milk Business");
//		try {
//			Service s = new Service("test", 20000, 15*60, true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public static void debugCreateBookingsTable() {
		SQLiteConnection.createBooking(1, "SARJ's Milk Business", "Gary", "0", "1491580800", "1491584400", "test");
		SQLiteConnection.createBooking(2, "SARJ's Milk Business", "Joe", "1", "1491670800", "1491674400", "test");
		SQLiteConnection.createBooking(3, "SARJ's Milk Business", "Bob", "0", "1491757200", "1491760800", "test");
		SQLiteConnection.createBooking(4, "SARJ's Milk Business", "Bill", "1", "1491764400", "1491768000", "test");
	}
	
	public static void debugCreateEmptyAvailability() {
		if (SQLiteConnection.createAvailability(1, "SARJ's Milk Business", "90000,93600|97200,100600|21600,43200")) {
			
		}
		else {
			SQLiteConnection.deleteAvailabilities(1, "SARJ's Milk Business");
			SQLiteConnection.createAvailability(1, "SARJ's Milk Business", "90000,93600|97200,100600|21600,43200");
		};
	}
	
	public static void debugCreateEmployees() {
		SQLiteConnection.createEmployee(0, "SARJ's Milk Business", "Bob", "32 does not exists st", "0412345678", 0);
		SQLiteConnection.createEmployee(1, "SARJ's Milk Business", "John", "32 does not exists st", "0412345678", 0);
		SQLiteConnection.createEmployee(2, "SARJ's Milk Business", "Alex", "32 does not exists st", "0412345678", 0);
		SQLiteConnection.createEmployee(5, "SARJ's Milk Business", "Greg", "32 does not exists st", "0412345678", 1);
	}

}