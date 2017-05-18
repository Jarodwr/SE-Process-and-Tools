package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLMaster {
	private Connection conn = null;
	private Logger LOGGER = Logger.getLogger("main");
	
	public SQLMaster() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:BookingSystemMasterDB.sqlite");
			createBusinessTable();
			createOwnerTable();
		} catch (Exception x) {
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
		String sql = "CREATE TABLE IF NOT EXISTS Businessinfo (businessId integer Primary Key, businessname Varchar(255), address Varchar(255), phonenumber Varchar(255))";
				try {
					Connection c = this.conn;
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					LOGGER.warning(e.getMessage());
				}
	}
	
	
	public ResultSet getBusinessRow(String businessname) throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM Businessinfo WHERE businessname=?";
		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, businessname);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else {
			LOGGER.log(Level.INFO, "Failed to find a business in the database with the username: "+ businessname);
			return null;
		}
	}
	
	
	public boolean createBusiness(int id, String businessname, String address, String phonenumber) {
		Connection c = this.conn;
		try {
			ResultSet rs = getBusinessRow(businessname); // search through businessnames to check if this user currently exists

			if (rs != null) {
				rs.close();
				return false;
			}

			PreparedStatement ps = c.prepareStatement("INSERT INTO Businessinfo VALUES (?, ?, ?, ?);"); // this creates a new user
			ps.setInt(1, this.getNextAvailableId(getAllBusinesses(), "businessId"));
			ps.setString(2, businessname);
			ps.setString(3, address);
			ps.setString(4, phonenumber);
			ps.executeUpdate();
			ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}
	
	private ResultSet getAllBusinesses() throws SQLException {
		Connection c = this.conn;
		// Search for rows with matching usernames
		String query = "SELECT * FROM Businessinfo";
		PreparedStatement pst = c.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {
			return rs;
		}
		else {
			LOGGER.log(Level.INFO, "Failed to find any businesses!");
			return null;
		}
	}

	public void createOwnerTable() {
		String sql = "CREATE TABLE IF NOT EXISTS Ownerinfo (businessId integer, username Varchar(255), password varchar(255), name varchar(255), address varchar(255), phonenumber varchar(255), Foreign Key(businessId) references Businessinfo(businessId))";
				try {
					Connection c = this.conn;
					Statement stmt = c.createStatement();
			            stmt.execute(sql);
				}
				catch(Exception e){
					LOGGER.warning(e.getMessage());
				}
	}
	

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
	 * Add a new owner into the database
	 * @param businessname	The name of the buisness to assign to the owner
	 * @param username	Owner's username
	 * @param password	Owner's password
	 * @param name	Owner's real name
	 * @param address	Owner's physical home address
	 * @param mobileno	Owner's mobile number
	 * @return success True if creation is successful, else false.
	 */
	
	public boolean createOwner(String businessname, String username, String password, String name, String address, String mobileno) {
		username = username.toLowerCase();
		Connection c = this.conn;
		try {
			ResultSet rs = getOwnerRow(username); // search through usernames to check if this user currently exists

			if (rs != null) {
				LOGGER.log(Level.FINE, "Failed to add owner into the database because an owner with the same username exists with username: "+ username);
				rs.close();
				return false;
			}
				PreparedStatement ps = c.prepareStatement("INSERT INTO Userinfo VALUES (?, ?, ?, ?, ?, ?);"); // this creates a new user
				ps.setInt(1, getBusinessDBFromName(businessname));
				ps.setString(2, username);
				ps.setString(3, password);
				ps.setString(4, name);
				ps.setString(5, address);
				ps.setString(6, mobileno);
				ps.executeUpdate();
				ps.close();

			return true;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public int getBusinessDBFromName(String businessname) {
		// TODO Auto-generated method stub
		return 0;
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
}
