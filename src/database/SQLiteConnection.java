package database;

import java.sql.*;

public class SQLiteConnection {
	private static Connection conn = null;
	
	public static void createTables() {
		createUsersTable();
		createBusinessTable();
		createOwnerTable();
		createEmployeeTable();
		createAvailabilitiesTable();
		createEmployeeWorkingTimesTable();
		createBookingsTable();		
	}
	
	public static Connection getDBConnection() { // connects to a database only once, stays open after that
		if (conn == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:BookingSystemDB.sqlite");
			} catch (Exception x) {
				System.out.println(x.getMessage());
			}
		}

		return conn;
	}
	
	public static void createUsersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Userinfo (username Varchar(255) Primary Key, password Varchar(255), name Varchar(255), address Varchar(255), mobileno Varchar(255))";
				try {
					Connection c = getDBConnection();
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
	}
	
	public static void createBusinessTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Businessinfo (businessname Varchar(255) Primary Key, address Varchar(255), phonenumber Varchar(255))";
				try {
					Connection c = getDBConnection();
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
	}
	
	public static void createOwnerTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Ownerinfo (businessname Varchar(255), username Varchar(255), Foreign Key(businessname) references Businessinfo(businessname), Foreign Key(username) references Userinfo(username))";
				try {
					Connection c = getDBConnection();
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
	}
	
	public static void createEmployeeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Employeeinfo (businessname Varchar(255), employeeId integer primary key, name Varchar(255), address Varchar(255), mobileno Varchar(255), timetableId integer, Foreign Key(timetableId) references Timetableinfo(timetableId), Foreign Key(businessname) references Businessinfo(businessname))";
				try {
					Connection c = getDBConnection();
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
	}
	
	public static void createAvailabilitiesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeAvailabilities (timetableId integer primary key, businessname Varchar(255), name Varchar(255), availability Varchar(255), Foreign Key(businessname) references Businessinfo(businessname))";
		try {
			Connection c = getDBConnection();
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void createEmployeeWorkingTimesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeWorkingTimes (businessname Varchar(255), name Varchar(255), shift Varchar(255), Foreign Key(businessname) references Businessinfo(businessname))";
		try {
			Connection c = getDBConnection();
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void createBookingsTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS BookingsTable ( bookingId integer Primary Key, businessname Varchar(255), username Varchar(255), starttimeunix Varchar(255), endtimeunix Varchar(255), bookingData Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))";
		try {
			Connection c = getDBConnection();
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
		
	
	public static ResultSet getUserRow(String username) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Userinfo WHERE Username=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getOwnerRow(String username) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Ownerinfo WHERE Username=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getBusinessRow(String businessname) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Businessinfo WHERE businessname=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	public static void deleteUser(String username) throws SQLException {
		Connection c = getDBConnection();
		String query = "DELETE FROM Userinfo WHERE username = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, username);
		pst.executeUpdate();
	}
	
	public static boolean createCustomer(String username, String password, String name, String address, String mobileno) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getUserRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO Userinfo VALUES (?, ?, ?, ?, ?);"); // this creates a new user
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setString(5, mobileno);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createOwner(String businessname, String username, String password, String name, String address, String mobileno) {
		Connection c = getDBConnection();
		Boolean needToAddUser = true;
		try {
			ResultSet rs = getUserRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				return false;
			}
			if (needToAddUser) {
				PreparedStatement ps = c.prepareStatement("INSERT INTO Userinfo VALUES (?, ?, ?, ?, ?);"); // this creates a new user
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, name);
				ps.setString(4, address);
				ps.setString(5, mobileno);
				ps.executeUpdate();
				ps.close();
			}
			
			ResultSet rs2 = getOwnerRow(username); // search through usernames to check if this user currently exists

			if (rs2 != null) {
				return false;
			}
			
			PreparedStatement ps2 = c.prepareStatement("INSERT INTO Ownerinfo VALUES (?, ?);"); // this links a user to a business, making them an owner
			ps2.setString(1, businessname);
			ps2.setString(2, username);
			ps2.executeUpdate();
			ps2.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createBusiness(String businessname, String address, String phonenumber) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getBusinessRow(businessname); // search through businessnames to check if this user currently exists

			if (rs != null) {
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO Businessinfo VALUES (?, ?, ?);"); // this creates a new user
			ps.setString(1, businessname);
			ps.setString(2, address);
			ps.setString(3, phonenumber);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createBooking(int bookingId, String businessname, String customername, String unixstamp1, String unixstamp2, String data) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getBookingRow(bookingId); // search through businessnames to check if this user currently exists

			if (rs != null) {
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO BookingsTable VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, bookingId);
			ps.setString(2, businessname);
			ps.setString(3, customername);
			ps.setString(4, unixstamp1);
			ps.setString(5, unixstamp2);
			ps.setString(6, data);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createEmployee(int employeeId, String businessname, String name, String address, String mobileno, int availabilityId) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getEmployeeRow(employeeId); // search through businessnames to check if this user currently exists

			if (rs != null) {
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO Employeeinfo VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, employeeId);
			ps.setString(2, businessname);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setString(5, mobileno);
			ps.setInt(6, availabilityId);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static ResultSet getEmployeeRow(int employeeId) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Employeeinfo WHERE employeeId=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setInt(1, employeeId);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}

	public static ResultSet getBookingRow(int bookingId) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE bookingId=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setInt(1, bookingId);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
		
	}
	public static ResultSet getBookingsByUsername(String username) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE username=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getBookingsByPeriodStart(String periodstartunixstamp) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE CAST(starttimeunix AS INTEGER)>=CAST(? AS INTEGER)";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, periodstartunixstamp);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
}
