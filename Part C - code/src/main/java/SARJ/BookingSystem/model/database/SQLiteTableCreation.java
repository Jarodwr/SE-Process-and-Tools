package SARJ.BookingSystem.model.database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

public class SQLiteTableCreation {
	private Connection conn = null;
	private Logger LOGGER = Logger.getLogger("main");
	
	public SQLiteTableCreation(Connection c) {
		this.conn = c;
		createTables();
	}
	
	public void createTables() {
		createUsersTable();
		createEmployeeTable();
		createAvailabilitiesTable();
		createEmployeeWorkingTimesTable();
		createBookingsTable();
		createServicesTable();
	}
	
	/**
	 * Userinfo (<br>
	 * 1 - username Varchar(255) Primary Key,<br>
	 * 2 - password Varchar(255),<br>
	 * 3 - name Varchar(255),<br>
	 * 4 - address Varchar(255)<br>
	 * )
	 */
	public void createUsersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Userinfo (username Varchar(255) Primary Key, password Varchar(255), name Varchar(255), address Varchar(255), mobileno Varchar(255))";
				try {
					Connection c = this.conn;
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					LOGGER.warning(e.getMessage());
				}
	}
	
	/**
	 * Employeeinfo (<br>
	 * 1 - employeeId integer primary key,<br>
	 * 2 - name Varchar(255),<br>
	 * 3 - address Varchar(255),<br>
	 * 4 - mobileno Varchar(255),<br>
	 * 5 - timetableId integer references Timetableinfo(timetableId)<br>
	 * )
	 */
	public void createEmployeeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Employeeinfo (employeeId integer primary key, name Varchar(255), address Varchar(255), mobileno Varchar(255), timetableId integer, Foreign Key(timetableId) references Timetableinfo(timetableId))";
				try {
					Connection c = this.conn;
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					LOGGER.warning(e.getMessage());
				}
	}
	
	/**
	 * Timetableinfo (<br>
	 * 1 - timetableId integer primary key,<br>
	 * 2 - availability Varchar(255)<br>
	 * )
	 */
	public void createAvailabilitiesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS Timetableinfo (timetableId integer primary key, availability Varchar(255))";
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	/**
	 * EmployeeWorkingTimes (<br>
	 * 1 - employeeId Varchar(255),<br>
	 * 2 - unixstarttime Varchar(255),<br>
	 * 3 - unixendtime Varchar(255),<br>
	 * )
	 */
	public void createEmployeeWorkingTimesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeWorkingTimes (employeeId integer, unixstarttime Varchar(255), unixendtime Varchar(255), "
				+ "Foreign Key(employeeId) references Employeeinfo(employeeId))";
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	/**
	 * BookingsTable ( 
	 * 1 - bookingId integer Primary Key,<br>
	 * 2 - username Varchar(255),<br>
	 * 3 - employeeId Varchar(255),<br>
	 * 4 - starttimeunix Varchar(255),<br>
	 * 5 - endtimeunix Varchar(255),<br>
	 * 6 - bookingData Varchar(255)<br>
	 * )
	 */
	public void createBookingsTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS BookingsTable (bookingId integer Primary Key, username Varchar(255), employeeId Varchar(255), starttimeunix Varchar(255), endtimeunix Varchar(255), bookingData Varchar(255), Foreign Key(employeeId) references Employeeinfo(employeeId))";
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	/**
	 * ServicesTable (
	 * 1 - servicename Varchar(255) Primary Key,<br>
	 * 2 - serviceprice integer,<br>
	 * 3 - serviceminutes integer,<br>
	 * )
	 */
	public void createServicesTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ServicesTable (servicename Varchar(255) Primary Key, serviceprice integer, serviceminutes integer)"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
}
