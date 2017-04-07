package model.period;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.database.SQLiteConnection;
import model.service.Service;
import model.users.Customer;

public class Booking extends Period {
	
	private String bookingId;
	private String customerUsername;
	private ArrayList<Service> services;
	
	public Booking(String start, String end, boolean formatted, String customerUsername, ArrayList<Service> services, boolean createDatabaseEntry) 
	{
		super(start, end, formatted);
		this.customerUsername = customerUsername;
		this.services = services;
		try {
			ResultSet rs = SQLiteConnection.getAllBookings("SARJ's Milk Business"); /* TODO remove hardcode */
			SQLiteConnection.createBooking(SQLiteConnection.getNextAvailableId(rs, "bookingId"), "SARJ's Milk Business", customerUsername, start, end, Service.arrayOfServicesToString(services, false));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public Booking(String start, String end, boolean formatted, String customerUsername, ArrayList<Service> services) 
	{
		super(start, end, formatted);
		this.customerUsername = customerUsername;
		this.services = services;
	}
	
    /**
    * @return format: customerUsername:period
    */
	public String getUsername() {
		return this.customerUsername;
	}
	
	public String getBookingId() {
		return this.bookingId;
	}
	
	public String[] toStringArray() 
	{
		String first = Long.toString(this.start.getTime());
		String second = Long.toString(this.end.getTime());
		String name = this.customerUsername;
		return new String[] {first, second, name};
	}
	
}
