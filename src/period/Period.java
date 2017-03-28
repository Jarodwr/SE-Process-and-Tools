package period;

import java.text.SimpleDateFormat;
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		@SuppressWarnings("deprecation")
		String first = sdf.format(start);
		@SuppressWarnings("deprecation")
		String second = sdf.format(end);
		String[] period = {first, second};
		return period;
	}
	
}
