package model.period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Period {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Date start;
	private Date end;

	private static final String[] weekdays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private static final int secondsInDay = 86400;
	
	public Period(Date start, Date end) {
		
		this.start = start;
		this.end = end;
	}
	
	public Period(String start, String end, boolean formatted) {
		try {
			if (formatted) {
				this.start = sdf.parse(start);
				this.end = sdf.parse(end);
			}
			else {
				this.start = new Date(Long.parseLong((start)));
				this.end = new Date(Long.parseLong((end)));
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int convert24HrTimeToDaySeconds(String time) {
		int seconds = 0;
		if (time.charAt(0) == ('0')) {
			time = time.substring(1);
		}
		String[] splithoursseconds= time.split(":");
		seconds = seconds + (Integer.parseInt(splithoursseconds[0]) * 3600);
		seconds = seconds + (Integer.parseInt(splithoursseconds[1]) * 60);
		return seconds;
	}
	
	public static int convertDayToSeconds(String day) {
		int i;
		for(i = 0; i < 7;i++) {
				if (weekdays[i].equals(day)) {
					return i * secondsInDay;
				}
		}
		return 0; // should be unreachable generally unless the day var is not valid
	}
	
	public static String convertSecondsToDay(int seconds) {
		int division = seconds/86400;
		return weekdays[division];
	}
	
	public static Calendar unixToCalendar(long unixTime){
	    Calendar c = Calendar.getInstance();
	    c.setTimeInMillis(unixTime);
	    return c;
	}
	
	public static long calendarToUnix(Calendar calendar){
	    Long unix = calendar.getTimeInMillis();
	    return unix;
	}
	
	public static long getCurrentWeekBeginning(String unixtimestamp) {
		Calendar c = unixToCalendar(Long.parseLong(unixtimestamp)*1000);
		c.getFirstDayOfWeek();
		return calendarToUnix(c)/1000;
			
	}
	
	public static long getCurrentWeekBeginning(Long unixtimestamp) {
		Calendar c = unixToCalendar(unixtimestamp*1000);
		c.getFirstDayOfWeek();
		return calendarToUnix(c)/1000;
			
	}
	public static boolean checkIsValidWeekday(String weekdayToCheck) {
		for(String s : weekdays) {
			if (weekdayToCheck.toLowerCase().toCharArray().equals(s)) {
				return true;
			}
		}
		return false;
	}
    /**
     * Verifies that the current period isn't overlapping with another period
     * @param otherPeriod The other period currently being tested against
     * @return boolean Returns true if there is any intersection
     */
	public Period combineWith(Period otherPeriod) {
		Date start = this.start;
		Date end = this.end;

		if (otherPeriod.getStart().compareTo(this.start) * otherPeriod.getEnd().compareTo(this.end) == 0) {
			return otherPeriod;
		}
		
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
		return new String[] {first, second};
	}
	
	public String toString() {
		return toStringArray()[0] + "," + toStringArray()[1];
	}
}
