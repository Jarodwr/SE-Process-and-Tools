package model.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * SQLite wrapper class
 */
public class SQLiteConnection {
	private Connection conn = null;
	private Logger LOGGER = Logger.getLogger("main");

	public SQLiteConnection(String db) {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:" + db + ".sqlite");
			SQLiteTableCreation tableCreate = new SQLiteTableCreation(conn); // calling this creates tables
		} catch (Exception x) {
			LOGGER.log(Level.WARNING, x.getMessage());
		}
	}
	
	/**
   	 * Gets the row in the database of the requested user using their username
   	 * @param username	The username to look for in the database
   	 * @return searchResult
   	 **/
		
	public ResultSet getUserRow(String username) throws SQLException {
		username = username.toLowerCase();
		// Search for rows with matching usernames
		String query = "SELECT * FROM Userinfo WHERE Username=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else { 
			LOGGER.log(Level.INFO, "Failed to find a customer user in the database with the username: "+username);
			return null;
		}
	}
	
	/**
   	 * Gets the row in the database of the requested owner using their username
   	 * @param username	The username to look for in the database
   	 * @return searchResult
   	 **/
	
	public ResultSet getOwnerRow(String username) throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Ownerinfo WHERE Username=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else {
			LOGGER.log(Level.INFO, "Failed to find an owner user in the database with the username: "+username);
			return null;
		}
	}
	
	/**
   	 * Delete user from the database using the given username
   	 * @param username	The username to look for in the database
   	 **/
	
	public void deleteUser(String username) throws SQLException {
		String query = "DELETE FROM Userinfo WHERE username = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, username);
		pst.executeUpdate();
	}
	
	/**
	 * Add a new customer into the database
	 * @param username	Customer's username
	 * @param password	Customer's password
	 * @param name	Customer's real name
	 * @param address	Customer's physical home address
	 * @param mobileno	Customer's mobile number
	 * @return success True if creation is successful, else false.
	 */
	public boolean createCustomer(String username, String password, String business, String name, String address, String mobileno) {
		try {
			ResultSet rs = getUserRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				LOGGER.log(Level.FINE, "Failed to add customer into the database because a customer with the same username exists with username: "+ username);
				rs.close();
				return false;
			}

			PreparedStatement ps = conn.prepareStatement("INSERT INTO Userinfo VALUES (?, ?, ?, ?, ?);"); // this creates a new user
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setString(5, mobileno);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Deletes a customer from the database using their username
	 * @param username	Customer's username
	 * @return success True if deletion is successful, false if user cannot be found.
	 */
	
	public boolean deleteCustomer(String username) {
		username = username.toLowerCase();
		try {
			ResultSet rs = getUserRow(username); // search through businessnames to check if this user currently exists

			if (rs == null) {
				LOGGER.log(Level.FINE, "Failed to remove customer from the database because a customer does not exist with the username: "+ username);
				return false;
			}
			
			String query = "DELETE FROM Userinfo WHERE username = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			pst.executeUpdate();
			rs.close();
			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	public ResultSet getAllCustomers() throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Userinfo";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * Adds a new booking for a buisness in the database
	 * @param customername	Name of customer
	 * @param employeeId	Employee assigned to the booking's ID
	 * @param unixstamp1	The start time of the booking period in unix time stamp format
	 * @param unixstamp2	The end time of the booking period in unix time stamp format
	 * @param data	Services for the booking
	 * @return success True if creation is successful, false if unsuccessful.
	 */
	public boolean createBooking(String customername, String employeeId, String unixstamp1, String unixstamp2, String data) {
		customername = customername.toLowerCase();
		try {
			int bookingId = getNextAvailableId(getAllBookings(), "bookingId");
			
			PreparedStatement ps = conn.prepareStatement("INSERT INTO BookingsTable VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, bookingId);
			ps.setString(2, customername);
			ps.setString(3, employeeId);
			ps.setString(4, unixstamp1);
			ps.setString(5, unixstamp2);
			ps.setString(6, data);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Removes a booking from a specific buisness from the database
	 * @param bookingId	The assigned ID of the booking
	 * @return success True if deletion is successful, false if unsuccessful.
	 */
	
	public boolean deleteBooking(int bookingId) {
		try {
			ResultSet rs = getBookingRow(bookingId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			rs.close();
			
			String query = "DELETE FROM BookingsTable WHERE bookingId = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, bookingId);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public boolean createEmployee(String name, String address, String mobileno) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Employeeinfo VALUES (?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, getNextAvailableId(getAllEmployees(), "employeeId"));
			ps.setString(2, name);
			ps.setString(3, address);
			ps.setString(4, mobileno);
			ps.setInt(5, getNextAvailableId(getAllAvailabilities(), "timetableId"));
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public boolean deleteEmployee(int employeeId) {
		try {
			ResultSet rs = getEmployeeRow(employeeId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			rs.close();
			
			String query = "DELETE FROM Employeeinfo WHERE employeeId = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, employeeId);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getEmployeeRow(int employeeId) throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Employeeinfo WHERE employeeId=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, employeeId);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
	}

	public ResultSet getBookingRow(int bookingId) throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE bookingId=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, bookingId);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
		
	}
	
	public ResultSet getBookingsByEmployeeId(String employeeId) throws SQLException {
		// Search for rows with matching employeeId
		String query = "SELECT * FROM BookingsTable WHERE employeeId=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, employeeId);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
		
	
	public ResultSet getBookingsByUsername(String username) throws SQLException { // not currently in use
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable WHERE username=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public boolean createAvailability(int timetableId, String availabilities) {
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this user currently exists

			if (rs != null) {
				rs.close();
				return false;
			}

			PreparedStatement ps = conn.prepareStatement("INSERT INTO Timetableinfo VALUES (?, ?);"); // this creates a new user
			ps.setInt(1, timetableId);
			ps.setString(2, availabilities);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public boolean deleteAvailabilities(int timetableId) {
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			rs.close();
			
			String query = "DELETE FROM Timetableinfo WHERE timetableId = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, timetableId);
			pst.executeUpdate();
			pst.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getAvailabilityRow(int timetableId) throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo WHERE timetableId=?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, timetableId);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public ResultSet getAllAvailabilities(int timetableId) throws SQLException{
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo";
		PreparedStatement pst = conn.prepareStatement(query);
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
	public boolean updateAvailabilityforEmployee(int employeeId, int timetableId) {
		try {
			ResultSet rs = getAvailabilityRow(timetableId); // search through businessnames to check if this timetable currently exists

			if (rs == null) {
				return false;
			}
			rs.close();
			
			ResultSet rs2 = getEmployeeRow(employeeId);
			if (rs2 == null) {
				return false;
			}
			rs2.close();
			
			PreparedStatement ps = conn.prepareStatement("UPDATE Employeeinfo SET timetableId=? WHERE employeeId=?"); // this creates a new user
			ps.setInt(1, timetableId);
			ps.setInt(2, employeeId);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	public ResultSet getEmployeeAvailability(int employeeId) throws SQLException { // change to return special if timetableId = 0
		// Search for rows with matching usernames
		String query1 = "SELECT timetableId FROM Employeeinfo WHERE employeeId=?";
		PreparedStatement pst1 = conn.prepareStatement(query1);
		pst1.setInt(1, employeeId);
		ResultSet rs1 = pst1.executeQuery();

		if (rs1.next()) {
			String timetableId = rs1.getString(1);
			System.out.println(timetableId);
			String query2 = "SELECT * FROM Timetableinfo WHERE timetableId = ?";
			PreparedStatement pst2 = conn.prepareStatement(query2);
			pst2.setInt(1, Integer.parseInt(timetableId));
			ResultSet rs2 = pst2.executeQuery();
			if (rs2.next()) {
				return rs2;
			}
		}
		return null;
		
	}
	
	public ResultSet getAllAvailabilities() throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public ResultSet getAllEmployees() throws SQLException {
		// Search for rows with matching usernames
		String query = "SELECT * FROM Employeeinfo";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	/**
	 * gets all shifts for given employee after specified time.
	 */
	public ResultSet getShifts(long l, String unixtime) throws SQLException { /* TODO */
		// Search for rows with matching usernames
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE employeeId = ? AND CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER)";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setLong(1, l);
		pst.setString(2, unixtime);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public ResultSet getShifts(String start, String end) throws SQLException{
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER) AND CAST(unixendtime AS INTEGER) <= CAST(? AS INTEGER)";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, start);
		pst.setString(2, end);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public boolean isShiftIn(int employeeId, String start, String end) throws SQLException{
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER) AND "
				+ "CAST(unixendtime AS INTEGER) <= CAST(? AS INTEGER) AND "
				+ "employeeId = ?";
		PreparedStatement pst;
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, start);
			pst.setString(2, end);
			pst.setInt(3, employeeId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				rs.close();
				return true;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return false;
	}

	public boolean addShift(int employeeId, String start, String end){
		try {
			if (isShiftIn(employeeId, start, end))
				return false;

			PreparedStatement ps = conn.prepareStatement("INSERT INTO EmployeeWorkingTimes VALUES (?, ?, ?);"); // this creates a new user
			ps.setInt(1, employeeId);
			ps.setString(2, start);
			ps.setString(3, end);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
			return false;
		}
	}
	
	public boolean removeShift(int employeeId, String start, String end) throws SQLException{
		
		String query = "DELETE FROM EmployeeWorkingTimes WHERE employeeId = ? AND"
				+ " unixstarttime = ? AND"
				+ " unixendtime = ?";

		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, employeeId);
		pst.setString(2, start);
		pst.setString(3, end);
		pst.executeUpdate();
		return true;

	}
	
	public ResultSet getService(String servicename) throws SQLException { /* TODO */
		// Search for rows with matching usernames
		String query = "SELECT * FROM ServicesTable WHERE servicename = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, servicename);
		ResultSet rs = pst.executeQuery();
		
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public ResultSet getAllServices() throws SQLException{
		String query = "SELECT * FROM ServicesTable";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public boolean addService(String serviceName, int servicePrice, int serviceMinutes) {
		
		try {
			ResultSet rs = getService(serviceName);
			if (rs != null) {
				rs.close();
				return false;
			}
			
			PreparedStatement ps = conn.prepareStatement("INSERT INTO ServicesTable VALUES (?, ?, ?);"); // this creates a new user
			ps.setString(1, serviceName);
			ps.setInt(2, servicePrice);
			ps.setInt(3, serviceMinutes);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getAllBookings() throws SQLException {
		
		// Search for rows with matching usernames
		String query = "SELECT * FROM BookingsTable";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}

	public int getNextAvailableId(ResultSet rs, String idString) throws SQLException { // edit to remove space leakage
		int i = 0;
		if (rs == null) {
			return i;
		}
		do {
			if (rs.getInt(idString) >= i) {
				i = rs.getInt(idString) + 1;
			}
		} while(rs.next());
		rs.close();
		return i;
		
	}
	
	public ResultSet genericGetQuery(String query) throws SQLException {
		// Search for rows with matching usernames
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;

	}

	public void close() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * Gets all bookings after a certain date
	 * @param l unixtimestamp format date
	 * @return	table
	 * @throws SQLException
	 */
	public ResultSet getBookingsByPeriodStart(long l) throws SQLException {
		Connection c = this.conn;
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
}