package main;

import controller.Controller;
import database.SQLiteConnection;

public class Main {

	//testcommit5
	public static void main(String[] args) {
		SQLiteConnection.createUsersTable(); // Create table if it doesn't exist for user info
		
		Controller bookingSystem = new Controller();
	}

}