package SARJ.BookingSystem.model;

public class Employee {
	
	private String employeeId;
	private String username;
	private Timetable availability = new Timetable();

	
	public Employee(String employeeId, String username, Timetable availability) {
		this.employeeId = employeeId;
		this.username = username;
		this.availability = availability;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmployeeId() {
		return this.employeeId;
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
