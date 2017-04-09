package model.period;

import java.util.Date;

public class Shift extends Period
{
	private String employeeId;
	
	
	public Shift(Date start, Date end, String employeeId) 
	{
		super(start, end);
		this.employeeId = employeeId;
	}
	
	public String getEmployeeId()
	{
		return employeeId;
	}

}
