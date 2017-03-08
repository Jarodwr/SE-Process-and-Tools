package bookings;

import period.Period;
import users.Customer;

public class Booking {
	
	private Period timeSlot;
	private String customerUsername;
	
	protected Booking(Period timeslot, String customerUsername) {
		this.timeSlot = timeslot;
		this.customerUsername = customerUsername;
	}
	
    /**
    * @return format: customerUsername: periodDetails
    */
	@Override
	public String toString() {
		return "";
	}
}
