package users;

import java.util.ArrayList;

import period.Period;

public class Employee extends User {
	private ArrayList<Period> availability = new ArrayList<Period>();
	public final boolean[] permissions = {false, false, false, true, false, false, false, false, false, true};
	
	public Employee(String username, String password, String availability) {
		super(username, password);
		
		//Tokenize string and add all timeslots to the availability with addTimeSlotToAvailability()
	}

	/**
	 * 
	 * @return timetable string
	 */
	public String getAvailability() {
		
		return availability.toString();
	}
	
	public boolean addTimeSlotToAvailability() {
		return false;
	}
	
	public boolean removeTimeSlotFromAvailability() {
		return false;
	}
	
	@Override
	public String toString() {
		return this.username + ":" + this.password;
	}
}
