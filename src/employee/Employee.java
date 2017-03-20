package employee;

import timetable.Timetable;

public class Employee {
	private Timetable availability = new Timetable();
	private String username;
	
	public Employee(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String[][] getAvailability() {
		
		return new String[0][];
	}
	
	public boolean addTimeSlotToAvailability() {
		return false;
	}
	
	public boolean removeTimeSlotFromAvailability() {
		return false;
	}
}
