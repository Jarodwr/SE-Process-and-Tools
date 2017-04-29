package model.database;

import java.sql.*;
import java.util.logging.Logger;


/**
 * SQLite wrapper class
 */
public class SQLiteConnection {
	private static Connection conn = null;
	private static Logger LOGGER = Logger.getLogger("main");
	
	
	public static void createTables() {
		createUsersTable();
		createBusinessTable();
		createOwnerTable();
		createEmployeeTable();
		createAvailabilitiesTable();
		createEmployeeWorkingTimesTable();
		createBookingsTable();
		createServicesTable();
	}
	
	/**
	 * Initialize database connection
	 * @return
	 */
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
	
	/**
	 * Userinfo (<br>
	 * 1 - username Varchar(255) Primary Key,<br>
	 * 2 - password Varchar(255),<br>
	 * 3 - name Varchar(255),<br>
	 * 4 - address Varchar(255)<br>
	 * )
	 */
	public static void createUsersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Userinfo (username Varchar(255) Primary Key, password Varchar(255), name Varchar(255), address Varchar(255), mobileno Varchar(255))";
				try {
					Connection c = getDBConnection();
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
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
	public static void createBusinessTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Businessinfo (businessname Varchar(255) Primary Key, address Varchar(255), phonenumber Varchar(255))";
				try {
					Connection c = getDBConnection();
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
	public static void createOwnerTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Ownerinfo (businessname Varchar(255), username Varchar(255), Foreign Key(businessname) references Businessinfo(businessname), Foreign Key(username) references Userinfo(username))";
				try {
					Connection c = getDBConnection();
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
	public static void createEmployeeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Employeeinfo (employeeId integer primary key, businessname Varchar(255),  name Varchar(255), address Varchar(255), mobileno Varchar(255), timetableId integer, Foreign Key(timetableId) references Timetableinfo(timetableId), Foreign Key(businessname) references Businessinfo(businessname))";
				try {
					Connection c = getDBConnection();
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
	public static void createAvailabilitiesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS Timetableinfo (timetableId integer primary key, businessname Varchar(255), availability Varchar(255), Foreign Key(businessname) references Businessinfo(businessname))";
		try {
			Connection c = getDBConnection();
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
	public static void createEmployeeWorkingTimesTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS EmployeeWorkingTimes (businessname Varchar(255), employeeId integer, unixstarttime Varchar(255), unixendtime Varchar(255), "
				+ "Foreign Key(businessname) references Businessinfo(businessname),"
				+ "Foreign Key(employeeId) references Employeeinfo(employeeId))";
		try {
			Connection c = getDBConnection();
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
	public static void createBookingsTable()  {
		String sql = "CREATE TABLE IF NOT EXISTS BookingsTable ( bookingId integer Primary Key, businessname Varchar(255), username Varchar(255), employeeId Varchar(255), starttimeunix Varchar(255), endtimeunix Varchar(255), bookingData Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname), Foreign Key(employeeId) references Employeeinfo(employeeId))";
		try {
			Connection c = getDBConnection();
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
	public static void createServicesTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ServicesTable (servicename Varchar(255) Primary Key, serviceprice integer, serviceminutes integer, businessname Varchar(255),  Foreign Key(businessname) references Businessinfo(businessname))"; // serviceprice is cents, as in $1.00 is 100, serviceminutes is the time in minutes that the service takes eg 120 for two hours or 15 for 15 minutes
		try {
			Connection c = getDBConnection();
			Statement stmt = c.createStatement();
	            stmt.execute(sql);
		}
		catch(Exception e){
			LOGGER.warning(e.getMessage());
		}
	}
		
	public static ResultSet getUserRow(String username) throws SQLException {
		username = username.toLowerCase();
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
	
	/**
	 * @return True if creation is successful, else false.
	 */
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
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if user cannot be found.
	 */
	public static boolean deleteCustomer(String username) {
		username = username.toLowerCase();
		Connection c = getDBConnection();
		try {
			ResultSet rs = getUserRow(username); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			String query = "DELETE FROM Userinfo WHERE username = ?";
			PreparedStatement pst = c.prepareStatement(query);
			pst.setString(1, username);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	public static ResultSet getAllCustomers() throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Userinfo";
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public static boolean createOwner(String businessname, String username, String password, String name, String address, String mobileno) {
		username = username.toLowerCase();
		Connection c = getDBConnection();
		Boolean needToAddUser = true;
		try {
			ResultSet rs = getUserRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				rs.close();
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
				rs2.close();
				return false;
			}
			
			PreparedStatement ps2 = c.prepareStatement("INSERT INTO Ownerinfo VALUES (?, ?);"); // this links a user to a business, making them an owner
			ps2.setString(1, businessname);
			ps2.setString(2, username);
			ps2.executeUpdate();
			ps2.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public static boolean createBusiness(String businessname, String address, String phonenumber) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getBusinessRow(businessname); // search through businessnames to check if this user currently exists

			if (rs != null) {
				rs.close();
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
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public static boolean createBooking(String businessname, String customername, String employeeId, String unixstamp1, String unixstamp2, String data) {
		
		customername = customername.toLowerCase();
		Connection c = getDBConnection();
		try {
			int bookingId = getNextAvailableId(getAllBookings(businessname), "bookingId");
			ResultSet rs = getBookingRow(bookingId); // search through businessnames to check if this user currently exists

			if (rs != null) {
				rs.close();
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO BookingsTable VALUES (?, ?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, bookingId);
			ps.setString(2, businessname);
			ps.setString(3, customername);
			ps.setString(4, employeeId);
			ps.setString(5, unixstamp1);
			ps.setString(6, unixstamp2);
			ps.setString(7, data);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public static boolean deleteBooking(int bookingId, String businessname) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getBookingRow(bookingId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			String query = "DELETE FROM BookingsTable WHERE bookingId = ?";
			PreparedStatement pst = c.prepareStatement(query);
			pst.setInt(1, bookingId);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public static boolean createEmployee(String businessname, String name, String address, String mobileno, int timetableId) {
		Connection c = getDBConnection();
		
		try {
			
			PreparedStatement ps = c.prepareStatement("INSERT INTO Employeeinfo VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, SQLiteConnection.getNextAvailableId(getAllEmployees(), "employeeId"));
			ps.setString(2, businessname);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setString(5, mobileno);
			ps.setInt(6, timetableId);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public static boolean deleteEmployee(int employeeId) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getEmployeeRow(employeeId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			String query = "DELETE FROM Employeeinfo WHERE employeeId = ?";
			PreparedStatement pst = c.prepareStatement(query);
			pst.setInt(1, employeeId);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}

	public static ResultSet getEmployeeRow(int employeeId) throws SQLException {
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
	
	public static ResultSet getBookingsByEmployeeId(String employeeId) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching employeeId
		String query = "SELECT * FROM BookingsTable WHERE employeeId=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, employeeId);
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
	
	/**
	 * Gets all bookings after a certain date
	 * @param l unixtimestamp format date
	 * @return	table
	 * @throws SQLException
	 */
	public static ResultSet getBookingsByPeriodStart(long l) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE CAST(starttimeunix AS INTEGER)>=CAST(? AS INTEGER) ORDER BY CAST(starttimeunix AS INTEGER)";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setLong(1, l);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public static boolean createAvailability(int timetableId, String businessname, String availabilities) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this user currently exists

			if (rs != null) {
				rs.close();
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO Timetableinfo VALUES (?, ?, ?);"); // this creates a new user
			ps.setInt(1, timetableId);
			ps.setString(2, businessname);
			ps.setString(3, availabilities);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public static boolean deleteAvailabilities(int timetableId, String businessname) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			rs.close();
			
			String query = "DELETE FROM Timetableinfo WHERE timetableId = ?";
			PreparedStatement pst = c.prepareStatement(query);
			pst.setInt(1, timetableId);
			pst.executeUpdate();
			pst.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}

	public static ResultSet getAvailabilityRow(int timetableId) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo WHERE timetableId=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setInt(1, timetableId);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * Sets an employee's availability to point to a new availability timetable
	 * @return True if updating the employee's availability is successful, false if unsuccessful.
	 */
	public static boolean updateAvailabilityforEmployee(int employeeId, int timetableId) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			ResultSet rs2 = getEmployeeRow(employeeId);
			if (rs2 == null) {
				return false;
			}

			PreparedStatement ps = c.prepareStatement("UPDATE Employeeinfo SET timetableId=? WHERE employeeId=?"); // this creates a new user
			ps.setInt(1, timetableId);
			ps.setInt(2, employeeId);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	public static ResultSet getEmployeeAvailability(int employeeId) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo WHERE timetableId= (SELECT timetableId FROM Employeeinfo WHERE employeeId=?)";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setInt(1, employeeId);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getAllAvailabilities() throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo";
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getAllEmployees() throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Employeeinfo";
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * gets all shifts for given employee after specified time.
	 */
	public static ResultSet getShifts(long l, String unixtime) throws SQLException { /* TODO */
		
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE employeeId = ? AND CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER)";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setLong(1, l);
		pst.setString(2, unixtime);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getShifts(String start, String end) throws SQLException{
		Connection c = getDBConnection();
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER) AND CAST(unixendtime AS INTEGER) <= CAST(? AS INTEGER)";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, start);
		pst.setString(2, end);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}

	public static boolean addShift(int employeeId, String businessname, String start, String end) {
		Connection c = getDBConnection();
		try {
			PreparedStatement ps = c.prepareStatement("INSERT INTO EmployeeWorkingTimes VALUES (?, ?, ?, ?);"); // this creates a new user
			ps.setString(1, businessname);
			ps.setInt(2, employeeId);
			ps.setString(3, start);
			ps.setString(4, end);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}
	
	public static ResultSet getService(String servicename, String businessname) throws SQLException { /* TODO */
	
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM ServicesTable WHERE servicename = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, servicename);
		ResultSet rs = pst.executeQuery();
		
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static ResultSet getAllServices(String businessName) throws SQLException{
		
		Connection c = getDBConnection();
		
		String query = "SELECT * FROM ServicesTable WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessName);
		ResultSet rs = pst.executeQuery();
		
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public static boolean addService(String serviceName, int servicePrice, int serviceMinutes, String businessName) {
		Connection c = getDBConnection();
		try {
			ResultSet rs = getService(serviceName, businessName);
			if (rs != null) {
				rs.close();
				return false;
			}
			
			PreparedStatement ps = c.prepareStatement("INSERT INTO ServicesTable VALUES (?, ?, ?, ?);"); // this creates a new user
			ps.setString(1, serviceName);
			ps.setInt(2, servicePrice);
			ps.setInt(3, serviceMinutes);
			ps.setString(4, businessName);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
	}

	public static ResultSet getAllBookings(String businessname) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}

	public static int getNextAvailableId(ResultSet rs, String idString) throws SQLException {
		int i = 0;
		do {
			if (rs.getInt(idString) > i) {
				i = rs.getInt(idString) + 1;
			}
		} while(rs.next());
		System.out.println(i);
		rs.close();
		return i;
		
	}
	
	public static ResultSet genericGetQuery(String query) throws SQLException {
		Connection c = getDBConnection();
		// Search for rows with matching usernames
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
		
		
	}
}
