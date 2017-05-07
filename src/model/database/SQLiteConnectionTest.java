package model.database;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;

public class SQLiteConnectionTest {

	SQLiteConnection testDb = new SQLiteConnection();
	
	@Test
	public void testConnection() {
		if (testDb.getDBConnection()==null) {
			fail("could not connect");
		}
		else {
			System.out.println("test");
		}
	}
	
	@Test
	public void testCreateCustomer() {
		try {
			testDb.deleteUser("test");
			testDb.createUsersTable();
			testDb.createCustomer("test", "test", "Test Test", "11 Test Place", "0498232444");
			ResultSet rs = testDb.getUserRow("test");
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
				testDb.deleteEmployee(i);
				
			}
			testDb.createBusiness("T Business", "Test Lane", "0412345678");
			testDb.createEmployee("T Business", "Dad", "A", "1", 0);
			testDb.createEmployee("T Business", "Mum", "A", "1", 0);
			testDb.createEmployee("T Business", "Brother", "A", "1", 0);
			testDb.createEmployee("T Business", "Sister", "A", "1", 0);
			testDb.createEmployee("T Business", "Baby", "A", "1", 0);
			String[] s = {"Dad", "Mum", "Brother", "Sister", "Baby"};
			for(int i = 1; i < 6; i++) {
				ResultSet rs = testDb.getEmployeeRow(i);
				assert(rs.getString("name").equals(s[i]));
				
			}

			for(int i = 1; i < 6; i++) {
				testDb.deleteEmployee(i);
				
			}
		
		}
		catch (Exception x) {
			x.printStackTrace();
			fail("Exception");
		}
	}

}
