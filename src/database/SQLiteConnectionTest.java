package database;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;

public class SQLiteConnectionTest {

	
	
	@Test
	public void testConnection() {
		if (SQLiteConnection.getDBConnection()==null) {
			fail("could not connect");
		}
		else {
			System.out.println("test");
		}
	}
	
	@Test
	public void testCreateCustomer() {
		try {
			SQLiteConnection.deleteUser("test");
			SQLiteConnection.createUsersTable();
			SQLiteConnection.createCustomer("test", "test", "Test Test", "11 Test Place", "0498232444");
			ResultSet rs = SQLiteConnection.getUserRow("test");
			if (!rs.getString("name").equals("Test Test")) {
				fail("customer wasn't created correctly. rs.getString(\"name\") returns " + rs.getString("name"));
			}
					
		}
		catch(Exception x) {
			x.printStackTrace();
			fail("Exception");
		}
	}

}
