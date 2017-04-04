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
	@Test
	public void testEmployeeFunctions() {
		try {

			for(int i = 1; i < 6; i++) {
				SQLiteConnection.deleteEmployee(i);
				
			}
			SQLiteConnection.createBusiness("T Business", "Test Lane", "0412345678");
			SQLiteConnection.createEmployee(1, "T Business", "Dad", "A", "1", 0);
			SQLiteConnection.createEmployee(2, "T Business", "Mum", "A", "1", 0);
			SQLiteConnection.createEmployee(3, "T Business", "Brother", "A", "1", 0);
			SQLiteConnection.createEmployee(4, "T Business", "Sister", "A", "1", 0);
			SQLiteConnection.createEmployee(5, "T Business", "Baby", "A", "1", 0);
			String[] s = {"Dad", "Mum", "Brother", "Sister", "Baby"};
			for(int i = 1; i < 6; i++) {
				ResultSet rs = SQLiteConnection.getEmployeeRow(i);
				assert(rs.getString("name").equals(s[i]));
				
			}

			for(int i = 1; i < 6; i++) {
				SQLiteConnection.deleteEmployee(i);
				
			}
		
		}
		catch (Exception x) {
			x.printStackTrace();
			fail("Exception");
		}
	}

}
