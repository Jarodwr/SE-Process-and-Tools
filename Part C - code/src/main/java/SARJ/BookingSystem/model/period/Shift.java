package SARJ.BookingSystem.model.period;

import java.util.Date;

public class Shift extends Period
{
	private String employeeId;
	
	
	public Shift(Date start, Date end, String employeeId) 
	{
		super(start, end);
		this.employeeId = employeeId;
	}
	
	public Shift(String start, String end, boolean formatted, String employeeId) 
	{
		super(start, end, formatted);
		this.employeeId = employeeId;
	}
	
	public String getEmployeeId()
	{
		return employeeId;
	}

	public String[] toStringArray() 
	{
		String first = Long.toString(start.getTime());
		String second = Long.toString(end.getTime());
		
		return new String[] {first, second, employeeId};
	}
}
