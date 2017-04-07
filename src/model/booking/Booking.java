package model.booking;

import model.period.Period;
import model.users.Customer;

public class Booking {
	
	private String bookingId;
	private Period timeSlot;
	private String customerUsername;
	
	public Booking(String bookingId, String customerUsername, Period timeslot) 
	{
		this.bookingId = bookingId;
		this.customerUsername = customerUsername;
		this.timeSlot = timeslot;
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
		String[] ts = timeSlot.toStringArray();
		
		return new String[]{customerUsername, ts[0], ts[1]};
	}
}
