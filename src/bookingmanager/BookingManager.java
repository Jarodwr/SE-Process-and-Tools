package bookingmanager;

import java.util.ArrayList;

import bookings.Booking;
import period.Period;
import timetable.Timetable;
import users.Customer;

public class BookingManager {
	
	private Timetable timetable = new Timetable();
	private ArrayList<Booking> bookings = new ArrayList<Booking>();
	
	BookingManager() {
		
	}
	
   /**
   * Checks if booking can be made and adds a new booking to bookings if the booking 
   * is valid.
   * @param startTime Start time of the booking
   * @param endTime End time of the booking
   * @param customerUsername Username of the customer making the booking
   * @return Returns true if booking is valid, returns false if booking is not valid
   */
	public boolean makeBooking(String startTime, String endTime, String customerUsername) {
		
		return false;
	}
	
   /**
    * Will create a String based on the Customer's current bookings.
    * 
    * Example string: 
    * -----------------
    * Booking 1 Details
    * -----------------
    * Booking 2 Details
    * -----------------
    * 
    * @param customerUsername The username of the customer requesting their booking details
    * @return Returns a description of all bookings under the current user's name.
    */
	public String viewCustomersBookings(String customerUsername) {
		// Calling a booking's toString() will return all necessary details
		return "";
	}
	
   /**
    * Similar to viewCustomersBookings() except it returns all customer details instead
    * of just the requested customer
    * @return Returns a description of all current bookings
    */
	public String viewAllBookings() {
		
		return "";
	}
   /**
    * Accessor to the timetable's toString() function
    * @return Returns a String of all available time slots.
    */
	public String viewTimeSlotAvailability() {
		
		return "";
	}
}
