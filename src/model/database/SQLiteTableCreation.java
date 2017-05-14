package model.database;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
		createUserMasterDB();
		createBusinessTable();
		createOwnerTable();
		createEmployeeTable();
		createAvailabilitiesTable();
		createEmployeeWorkingTimesTable();
		createBookingsTable();
		createServicesTable();
		createBusinessHoursTable();
		createBusinessLogoTable();
		createBusinessHeaderTable();
		createBusinessColorTable();
	}
	
	private void createUserMasterDB() {
		String sql = "CREATE TABLE IF NOT EXISTS UserinfoMaster (userTableName Varchar(255))";
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
		
	}
	
	private void addToMasterDB(String tableName) throws SQLException {
		Connection c = this.conn;
		
		PreparedStatement ps;
		ps = c.prepareStatement("INSERT INTO UserinfoMaster VALUES (?);");
		ps.setString(1, tableName);
		ps.executeUpdate();
		ps.close();
	}

	/**
	 * Userinfo (<br>
	 * 1 - username Varchar(255) Primary Key,<br>
	 * 2 - password Varchar(255),<br>
	 * 3 - name Varchar(255),<br>
	 * 4 - address Varchar(255)<br>
	 * )
	 */
	
	@SuppressWarnings("deprecation") // although deprecated, function .encode() acts as intended and is less lines of code than using recommended version.
	public void createUsersTable(String businessname) {
		businessname = URLEncoder.encode(businessname);
		String sql = "CREATE TABLE IF NOT EXISTS [" + businessname +  "_Userinfo] (username Varchar(255) Primary Key, password Varchar(255), name Varchar(255), address Varchar(255), mobileno Varchar(255))";
				try {
					Connection c = this.conn;
					Statement stmt = c.createStatement();
			        stmt.execute(sql);
			        addToMasterDB(businessname +  "_Userinfo");
				}
				catch(Exception e){
					LOGGER.warning(e.getMessage());
				}
	}
	
	/**
	 * Businessinfo (<br>
	 * 1 - businessname Varchar(255) Primary Key,<br>
	 * 2 - address Varchar(255),<br>
	 * 3 - phonenumber Varchar(255)<br>
	 * )
	 */
	public void createBusinessTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Businessinfo (businessname Varchar(255) Primary Key, address Varchar(255), phonenumber Varchar(255))";
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
	 * Ownerinfo (<br>
	 * 1 - businessname Varchar(255) references Businessinfo(businessname),<br>
	 * 2 - username Varchar(255) references Userinfo(username)<br>
	 * )
	 */
	public void createOwnerTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Ownerinfo (businessname Varchar(255), username Varchar(255), Foreign Key(businessname) references Businessinfo(businessname), Foreign Key(username) references Userinfo(username))";
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
	 * 2 - businessname Varchar(255) references Businessinfo(businessname),<br> 
	 * 3 - name Varchar(255),<br>
	 * 4 - address Varchar(255),<br>
	 * 5 - mobileno Varchar(255),<br>
	 * 6 - timetableId integer references Timetableinfo(timetableId)<br>
	 * )
	 */
	public void createEmployeeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Employeeinfo (employeeId integer primary key, businessname Varchar(255),  name Varchar(255), address Varchar(255), mobileno Varchar(255), timetableId integer, Foreign Key(timetableId) references Timetableinfo(timetableId), Foreign Key(businessname) references Businessinfo(businessname))";
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
	 * 2 - businessname Varchar(255) references Businessinfo(businessname),<br>
	 * 3 - availability Varchar(255)<br>
	 * )
	 */
	public void createAvailabilitiesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS Timetableinfo (timetableId integer primary key, businessname Varchar(255), availability Varchar(255), Foreign Key(businessname) references Businessinfo(businessname))";
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
	 * 1 - businessname Varchar(255) references Businessinfo(businessname),<br>
	 * 2 - employeeId Varchar(255),<br>
	 * 3 - unixstarttime Varchar(255),<br>
	 * 4 - unixendtime Varchar(255),<br>
	 * )
	 */
	public void createEmployeeWorkingTimesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeWorkingTimes (businessname Varchar(255), employeeId integer, unixstarttime Varchar(255), unixendtime Varchar(255), "
				+ "Foreign Key(businessname) references Businessinfo(businessname),"
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
	 * 2 - businessname Varchar(255) references Businessinfo(businessname),<br>
	 * 3 - username Varchar(255),<br>
	 * 4 - employeeId Varchar(255),<br>
	 * 5 - starttimeunix Varchar(255),<br>
	 * 6 - endtimeunix Varchar(255),<br>
	 * 7 - bookingData Varchar(255)<br>
	 * )
	 */
	public void createBookingsTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS BookingsTable ( bookingId integer Primary Key, businessname Varchar(255), username Varchar(255), employeeId Varchar(255), starttimeunix Varchar(255), endtimeunix Varchar(255), bookingData Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname), Foreign Key(employeeId) references Employeeinfo(employeeId))";
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
	 * 4 - businessname Varchar(255) references Businessinfo(businessname),<br>
	 * )
	 */
	public void createServicesTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ServicesTable (servicename Varchar(255) Primary Key, serviceprice integer, serviceminutes integer, businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
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
	 * createBusinessHoursTable (
	 * 1 - stringOfTimes Varchar(255), <br>
	 * 2 - businessname Varchar(255) Foreign Key references Businessinfo(businessname), <br>
	 * )
	 */
	public void createBusinessHoursTable() {
		String sql = "CREATE TABLE IF NOT EXISTS BusinessHoursTable (stringOfTimes varchar(255), businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	public void createBusinessLogoTable() {
		String sql = "CREATE TABLE IF NOT EXISTS BusinessLogo (logoLink varchar(255), businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	public void createBusinessHeaderTable() {
		String sql = "CREATE TABLE IF NOT EXISTS BusinessHeader (headerName varchar(255), businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
		try {
			Connection c = this.conn;
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
	
	public void createBusinessColorTable() {
		String sql = "CREATE TABLE IF NOT EXISTS BusinessColor (colorHex varchar(255), businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
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
