package bookings;

import period.Period;
import users.Customer;

public class Booking {
	
	private Period timeSlot;
	private String customerUsername;
	
	protected Booking(Period timeslot, String customerUsername) 
	{
		this.timeSlot = timeslot;
		this.customerUsername = customerUsername;
	}
	
    /**
    * @return format: customerUsername:period
    */
	public String[][] toStringArray() 
	{
		String[][] booking = new String[2][2];
		booking[0][0] = customerUsername;
		booking[1] = timeSlot.toStringArray();
		
		return booking;
	}
}
