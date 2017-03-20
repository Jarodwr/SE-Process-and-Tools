package main;

import controller.Controller;
import database.SQLiteConnection;

public class Main {

	public static void main(String[] args) {
		SQLiteConnection.createTables(); // Create table if it doesn't exist for all info
		debugCreateOwnerBusiness();
		Controller bookingSystem = new Controller();
	}
	
	public static void debugCreateOwnerBusiness() {
		SQLiteConnection.createBusiness("SARJ's Milk Business", "Cherry Lane", "0123456789");
		SQLiteConnection.createOwner("SARJ's Milk Business", "Ownertest", "1234", "Name", "Address", "MobileNo");
	}

}