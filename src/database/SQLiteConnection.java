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
				conn = DriverManager.getConnection("jdbc:sqlite:Users.sqlite");
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
		String sql = "CREATE TABLE IF NOT EXISTS Businessinfo (businessname Varchar(255) Primary Key, name Varchar(255), address Varchar(255), mobileno Varchar(255))";
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
		String sql = "CREATE TABLE IF NOT EXISTS Ownerinfo (Foreign Key(businessname) references Businessinfo(businessname), username Varchar(255) Primary Key, name Varchar(255), address Varchar(255), mobileno Varchar(255))";
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
		String sql = "CREATE TABLE IF NOT EXISTS Employeeinfo (Foreign Key(businessname) references Businessinfo(businessname), name Varchar(255) Primary Key, address Varchar(255), mobileno Varchar(255))";
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
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeAvailabilities (Foreign Key(businessname) references Businessinfo(businessname), name Varchar(255), availability Varchar(255))";
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
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeWorkingTimes (Foreign Key(businessname) references Businessinfo(businessname), name Varchar(255), shift Varchar(255))";
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
		String sql = "CREATE TABLE IF NOT EXISTS BookingsTable ( bookingId integer Primary Key, Foreign Key(businessname) references Businessinfo(businessname), username Varchar(255), bookingData Varchar(255)";
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

		return rs;
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

			if (rs.next())
				return false; // remove userexistsexception
			rs.close();

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

}
