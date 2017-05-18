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

	public SQLiteConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:BookingSystemDB.sqlite");
			SQLiteTableCreation tableCreate = new SQLiteTableCreation(conn); // calling this creates tables
		} catch (Exception x) {
			LOGGER.log(Level.WARNING, x.getMessage());
		}
	}
	
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
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM Userinfo WHERE Username=?";
		PreparedStatement pst = c.prepareStatement(query);
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
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM Ownerinfo WHERE Username=?";
		PreparedStatement pst = c.prepareStatement(query);
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
   	 * Gets the row in the database of the requested buisness from the name of the buisness
   	 * @param businessname	The business name to look for in the database
   	 * @return searchResult
   	 **/
	
	/**
   	 * Delete user from the database using the given username
   	 * @param username	The username to look for in the database
   	 **/
	
	public void deleteUser(String username) throws SQLException {
		Connection c = this.conn;
		String query = "DELETE FROM Userinfo WHERE username = ?";
		PreparedStatement pst = c.prepareStatement(query);
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
		Connection c = this.conn;
		try {
			ResultSet rs = getUserRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				LOGGER.log(Level.FINE, "Failed to add customer into the database because a customer with the same username exists with username: "+ username);
				rs.close();
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
			
			PreparedStatement ps2 = c.prepareStatement("INSERT INTO UserBusinessTable VALUES (?, ?);"); // this creates a new user
			ps2.setString(1, username);
			ps2.setString(2, business);
			ps2.executeUpdate();
			ps2.close();

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
		Connection c = this.conn;
		try {
			ResultSet rs = getUserRow(username); // search through businessnames to check if this user currently exists

			if (rs == null) {
				LOGGER.log(Level.FINE, "Failed to remove customer from the database because a customer does not exist with the username: "+ username);
				return false;
			}
			
			String query = "DELETE FROM Userinfo WHERE username = ?";
			PreparedStatement pst = c.prepareStatement(query);
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
		Connection c = this.conn;
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
	 * Adds a new business into the database
	 * @param businessname	Name of the business
	 * @param address	Business' address
	 * @param phonenumber	Business' phone number
	 * @return success True if creation is successful, false if unsuccessful.
	 */
	
	
	/**
	 * Adds a new booking for a buisness in the database
	 * @param businessname	Name of the buisness
	 * @param customername	Name of customer
	 * @param employeeId	Employee assigned to the booking's ID
	 * @param unixstamp1	The start time of the booking period in unix time stamp format
	 * @param unixstamp2	The end time of the booking period in unix time stamp format
	 * @param data	Services for the booking
	 * @return success True if creation is successful, false if unsuccessful.
	 */
	public boolean createBooking(String businessname, String customername, String employeeId, String unixstamp1, String unixstamp2, String data) {
		
		customername = customername.toLowerCase();
		Connection c = this.conn;
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
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Removes a booking from a specific buisness from the database
	 * @param bookingId	The assigned ID of the booking
	 * @param businessname	The name of the buisness the booking belongs to
	 * @return success True if deletion is successful, false if unsuccessful.
	 */
	
	public boolean deleteBooking(int bookingId, String businessname) {
		Connection c = this.conn;
		try {
			ResultSet rs = getBookingRow(bookingId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			rs.close();
			
			String query = "DELETE FROM BookingsTable WHERE bookingId = ?";
			PreparedStatement pst = c.prepareStatement(query);
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
	public boolean createEmployee(String businessname, String name, String address, String mobileno, int timetableId) {
		Connection c = this.conn;
		
		try {
			PreparedStatement ps = c.prepareStatement("INSERT INTO Employeeinfo VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, this.getNextAvailableId(getAllEmployees(), "employeeId"));
			ps.setString(2, businessname);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setString(5, mobileno);
			ps.setInt(6, timetableId);
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
		Connection c = this.conn;
		try {
			ResultSet rs = getEmployeeRow(employeeId); // search through businessnames to check if this user currently exists

			if (rs == null) {
				return false;
			}
			
			rs.close();
			
			String query = "DELETE FROM Employeeinfo WHERE employeeId = ?";
			PreparedStatement pst = c.prepareStatement(query);
			pst.setInt(1, employeeId);
			pst.executeUpdate();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getEmployeeRow(int employeeId) throws SQLException {
		Connection c = this.conn;
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

	public ResultSet getBookingRow(int bookingId) throws SQLException {
		Connection c = this.conn;
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
	
	public ResultSet getBookingsByEmployeeId(String employeeId) throws SQLException {
		Connection c = this.conn;
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
		
	
	public ResultSet getBookingsByUsername(String username) throws SQLException { // not currently in use
		Connection c = this.conn;
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
	
	/**
	 * @return True if creation is successful, false if unsuccessful.
	 */
	public boolean createAvailability(int timetableId, String businessname, String availabilities) {
		Connection c = this.conn;
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
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	/**
	 * @return True if deletion is successful, false if unsuccessful.
	 */
	public boolean deleteAvailabilities(int timetableId, String businessname) {
		Connection c = this.conn;
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
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getAvailabilityRow(int timetableId) throws SQLException {
		Connection c = this.conn;
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
	public boolean updateAvailabilityforEmployee(int employeeId, int timetableId) {
		Connection c = this.conn;
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
			
			PreparedStatement ps = c.prepareStatement("UPDATE Employeeinfo SET timetableId=? WHERE employeeId=?"); // this creates a new user
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
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query1 = "SELECT timetableId FROM Employeeinfo WHERE employeeId=?";
		PreparedStatement pst1 = c.prepareStatement(query1);
		pst1.setInt(1, employeeId);
		ResultSet rs1 = pst1.executeQuery();

		if (rs1.next()) {
			if (rs1.getInt("timetableId") == 0 ) {
				System.out.println("test");
				return null;
			}
			else {
				String query2 = "SELECT * FROM Timetableinfo WHERE timetableId= (SELECT timetableId FROM Employeeinfo WHERE employeeId=?)";
				PreparedStatement pst2 = c.prepareStatement(query2);
				pst2.setInt(1, employeeId);
				ResultSet rs2 = pst2.executeQuery();

				if (rs2.next()) {
					return rs2;
				}
				else return null;
			}
		}
		else return null;
		
	}
	
	public ResultSet getAllAvailabilities() throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM Timetableinfo";
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public ResultSet getAllEmployees() throws SQLException {
		Connection c = this.conn;
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
	public ResultSet getShifts(long l, String unixtime) throws SQLException { /* TODO */
		
		Connection c = this.conn;
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
	
	public ResultSet getShifts(String start, String end) throws SQLException{
		Connection c = this.conn;
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
	
	public boolean isShiftIn(int employeeId, String businessname, String start, String end) throws SQLException{
		Connection c = this.conn;
		String query = "SELECT * "
				+ "FROM EmployeeWorkingTimes "
				+ "WHERE CAST(unixstarttime AS INTEGER) >= CAST(? AS INTEGER) AND "
				+ "CAST(unixendtime AS INTEGER) <= CAST(? AS INTEGER) AND "
				+ "employeeId = ? AND "
				+ "businessname = ?";
		PreparedStatement pst;
		try {
			pst = c.prepareStatement(query);
			pst.setString(1, start);
			pst.setString(2, end);
			pst.setInt(3, employeeId);
			pst.setString(4, businessname);
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

	public boolean addShift(int employeeId, String businessname, String start, String end){
		Connection c = this.conn;
		try {
			if (isShiftIn(employeeId, businessname, start, end))
				return false;

			PreparedStatement ps = c.prepareStatement("INSERT INTO EmployeeWorkingTimes VALUES (?, ?, ?, ?);"); // this creates a new user
			ps.setString(1, businessname);
			ps.setInt(2, employeeId);
			ps.setString(3, start);
			ps.setString(4, end);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
			return false;
		}
	}
	
	public boolean removeShift(int employeeId, String businessname, String start, String end) throws SQLException{
		Connection c = this.conn;
		String query = "DELETE FROM EmployeeWorkingTimes WHERE employeeId = ? AND"
				+ " businessname = ? AND"
				+ " unixstarttime = ? AND"
				+ " unixendtime = ?";

		PreparedStatement pst = c.prepareStatement(query);
		pst.setInt(1, employeeId);
		pst.setString(2, businessname);
		pst.setString(3, start);
		pst.setString(4, end);
		pst.executeUpdate();
		return true;

	}
	
	public ResultSet getService(String servicename, String businessname) throws SQLException { /* TODO */
	
		Connection c = this.conn;
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
	
	public ResultSet getAllServices(String businessName) throws SQLException{
		
		Connection c = this.conn;
		
		String query = "SELECT * FROM ServicesTable WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessName);
		ResultSet rs = pst.executeQuery();
		
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	
	public boolean addService(String serviceName, int servicePrice, int serviceMinutes, String businessName) {
		Connection c = this.conn;
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
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public ResultSet getAllBookings(String businessname) throws SQLException {
		Connection c = this.conn;
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

	public int getNextAvailableId(ResultSet rs, String idString) throws SQLException { // edit to remove space leakage
		int i = 0;
		if (rs == null) return i;
		do {
			if (rs.getInt(idString) >= i) {
				i = rs.getInt(idString) + 1;
			}
		} while(rs.next());
		rs.close();
		return i;
		
	}
	
	public ResultSet genericGetQuery(String query) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;


	}
	/* mark for testing */
	public boolean createBusinessHours(String businessname, String listOfHours) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO BusinessHoursTable VALUES (?, ?);");
			ps.setString(1, listOfHours);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public boolean updateBusinessHours(String businessname, String listOfHours) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("UPDATE BusinessHoursTable "
					+ "SET stringOfTimes=?"
					+ "WHERE businessname = ?;");
			ps.setString(1, listOfHours);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public ResultSet getBusinessHours(String businessname) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM BusinessHoursTable WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	/* mark for testing */
	public ResultSet getBusinessLogo(String businessname) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM BusinessLogo WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	/* mark for testing */
	public boolean createBusinessLogo(String businessname, String link) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO BusinessLogo VALUES (?, ?);");
			ps.setString(1, businessname);
			ps.setString(2, link);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public boolean updateBusinessLogo(String businessname, String link) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("UPDATE BusinessLogo "
					+ "SET logoLink=?"
					+ "WHERE businessname = ?;");
			ps.setString(1, link);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public ResultSet getBusinessHeader(String businessname) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM BusinessHeader WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	/* mark for testing */
	public boolean createBusinessHeader(String businessname, String header) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO BusinessLogo VALUES (?, ?);");
			ps.setString(1, header);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public boolean updateBusinessHeader(String businessname, String header) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("UPDATE BusinessLogo "
					+ "SET header=?"
					+ "WHERE businessname = ?;");
			ps.setString(1, header);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public ResultSet getBusinessColor(String businessname) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM BusinessColor WHERE businessname = ?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();
	
		if (rs.next()) {
			return rs;
		}
		else return null;
	}
	/* mark for testing */
	public boolean createBusinessColor(String businessname, String colorHex) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO BusinessColor VALUES (?, ?);");
			ps.setString(1, colorHex);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/* mark for testing */
	public boolean updateBusinessColor(String businessname, String colorHex) {
		Connection c = this.conn;
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("UPDATE BusinessLogo "
					+ "SET colorHex=?"
					+ "WHERE businessname = ?;");
			ps.setString(1, colorHex);
			ps.setString(2, businessname);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public ResultSet getUserBusinessRow(String username) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM UserBusinessTable WHERE username=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs;
		}
		else {
			LOGGER.log(Level.INFO, "Failed to find a business for the user: "+username);
			return null;
		}
	}
}
