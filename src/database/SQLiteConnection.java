package database;

import java.sql.*;

public class SQLiteConnection {
	private static Connection conn = null;
	
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
