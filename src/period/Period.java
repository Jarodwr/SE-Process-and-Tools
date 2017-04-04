package period;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Period {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Date start;
	private Date end;
	
	public Period(Date start, Date end) {
		
		this.start = start;
		this.end = end;
	}
	
	public Period(String start, String end) {
		try {
			this.start = sdf.parse(start);
			this.end = sdf.parse(end);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * Verifies that the current period isn't overlapping with another period
     * @param otherPeriod The other period currently being tested against
     * @return boolean Returns true if there is any intersection
     */
	public Period combineWith(Period otherPeriod) {
		Date start = this.start;
		Date end = this.end;
		
		if (otherPeriod.getStart().compareTo(this.start) * this.start.compareTo(otherPeriod.getEnd()) > 0) {
			start = otherPeriod.getStart();
		}
		
		if (otherPeriod.getStart().compareTo(this.end) * this.end.compareTo(otherPeriod.getEnd()) > 0) {
			end = otherPeriod.getEnd();
		}
		
		if (start.equals(this.start) && end.equals(this.end)) {
			return null;
		}
		return new Period(start, end);
	}
	
	public Date getStart() {
		return this.start;
	}
	
	public Date getEnd() {
		return this.end;
	}
	
    /**
     * date format: yyyyMMddHHmmss
     * format: "startDate,endDate"
     */
	public String[] toStringArray() 
	{
		String first = sdf.format(start);
		String second = sdf.format(end);
		return new String[]{first, second};
	}
}
