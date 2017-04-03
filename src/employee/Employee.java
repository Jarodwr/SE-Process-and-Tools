package employee;

import timetable.Timetable;

public class Employee {
	private Timetable availability = new Timetable();
	private String username;
	
	public Employee(String username, Timetable availability) {
		this.username = username;
		this.availability = availability;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String[][] getAvailabilityString() {
		return availability.toStringArray();
	}
	
	public boolean addTimeSlotToAvailability() {
		return false;
	}
	
	public boolean removeTimeSlotFromAvailability() {
		return false;
	}
}
