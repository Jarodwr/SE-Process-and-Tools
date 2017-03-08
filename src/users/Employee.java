package users;

import java.util.ArrayList;

import period.Period;

public class Employee extends User {
	private ArrayList<Period> availability = new ArrayList<Period>();
	
	public Employee(String username, String password, String availability) {
		super(username, password);
		
		//Tokenize string and add all timeslots to the availability with addTimeSlotToAvailability()
	}

	public String availabilityToString() {
		
		return "";
	}
	
	private boolean addTimeSlotToAvailability() {
		return false;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
