package model.period;

import java.util.ArrayList;

import model.service.Service;

public class Booking extends Period {
	
	private String bookingId;
	private String customerUsername;
	private String employeeId;
	private ArrayList<Service> services;
	
	public Booking(String start, String end, boolean formatted, String customerUsername,String bookingId, String employeeId, ArrayList<Service> services) 
	{
		super(start, end, formatted);
		this.bookingId = bookingId;
		this.customerUsername = customerUsername;
		this.employeeId = employeeId;
		this.services = services;
	}
	
	public String getEmployeeId() {
		return this.employeeId;
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
		String bookingID = this.bookingId;
		String first = Long.toString(this.start.getTime());
		String second = Long.toString(this.end.getTime());
		String name = this.customerUsername;
		String servs = Service.arrayOfServicesToString(services, false);
		String employeeId = this.employeeId;
		return new String[] {bookingID, first, second, name, servs, employeeId};
	}
	
}
