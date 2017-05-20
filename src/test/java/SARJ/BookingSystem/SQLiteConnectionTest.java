package SARJ.BookingSystem;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.ResultSet;

import org.junit.BeforeClass;
import org.junit.Test;

import SARJ.BookingSystem.model.database.SQLMaster;
import SARJ.BookingSystem.model.database.SQLiteConnection;

public class SQLiteConnectionTest {
	static SQLiteConnection testDB;
	static SQLMaster testMasterDB;

	@BeforeClass
	public static void setup() {
		new File("TESTMasterDB.sqlite").delete();
		new File("test.sqlite").delete();
		testDB = new SQLiteConnection("test");
		testMasterDB = new SQLMaster("test");
		
	}
	
	@Test
	public void testCreateCustomer() {
		try {
			testDB.deleteUser("test");
			testDB.createCustomer("test", "test", "test", "Test Test", "11 Test Place", "0498232444");
			ResultSet rs = testDB.getUserRow("test");
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
				testDB.deleteEmployee(i);
			}
			testMasterDB.createBusiness("T Business", "Test Lane", "0412345678");
			testDB.createEmployee("Dad", "A", "1");
			testDB.createEmployee("Mum", "A", "1");
			testDB.createEmployee("Brother", "A", "1");
			testDB.createEmployee("Sister", "A", "1");
			testDB.createEmployee("Baby", "A", "1");
			String[] s = {"Dad", "Mum", "Brother", "Sister", "Baby"};
			for(int i = 0; i < 5; i++) {
				ResultSet rs = testDB.getEmployeeRow(i);
				assert(rs!=null);
				assert(rs.getString("name").equals(s[i]));
				
			}

			for(int i = 1; i < 6; i++) {
				testDB.deleteEmployee(i);
				
			}
		
		}
		catch (Exception x) {
			x.printStackTrace();
			fail("Exception");
		}
	}

}
