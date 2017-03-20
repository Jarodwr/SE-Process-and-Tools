package period;

import java.util.Date;

public class Period {

	private Date start;
	private Date end;
	
	public Period(Date start, Date end) {
		
		this.start = start;
		this.end = end;
	}
    /**
     * Verifies that the current period isn't overlapping with another period
     * @param otherPeriod The other period currently being tested against
     * @return boolean Returns true if there is any intersection
     */
	public boolean clashingWith(Period otherPeriod) {
		return false;
	}
	
    /**
     * date format: hh:mm dd/mm/yyyy
     * format: "startDate,endDate"
     */
	public String[] toStringArray() 
	{
		@SuppressWarnings("deprecation")
		String first = start.getHours() + ":" + start.getMinutes() + " " + start.getDay() + "/" + start.getMonth() + "/" + start.getYear();
		@SuppressWarnings("deprecation")
		String second = end.getHours() + ":" + end.getMinutes() + " " + end.getDay() + "/" + end.getMonth() + "/" + end.getYear();
		String[] period = {first, second};
		return period;
	}
	
}
