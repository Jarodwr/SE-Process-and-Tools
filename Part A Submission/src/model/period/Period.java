package model.period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Period {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat viewing = new SimpleDateFormat("h:mm a d:M:yy");
	protected Date start;
	protected Date end;

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
		if (time.charAt(0) == ('0') && time.charAt(1) != (':')) {
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
	
	public long duration() {
		return start.getTime() - end.getTime();
	}
	
	public static String convertSecondsToDay(int seconds) {
		int division = seconds/86400;
		return weekdays[Math.min(6,division)];
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
		for(int i  = 0; i < weekdays.length; i++) {
			if (weekdayToCheck.toLowerCase().equals(weekdays[i].toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static String get24HrTimeFromWeekTime(int seconds) {
		String s = "";
		int secondsRemainder = seconds%86400;
		int hours = secondsRemainder/3600;
		int minutes = (secondsRemainder%3600)/60;
		if (minutes < 9) {
			s = Integer.toString(hours) + ":0" + minutes;
		}
		else {
				s = Integer.toString(hours) + ":" + minutes;
		}	
		return s;
	}
	
	public static String get24HrTimeFromWeekTime(String secondsString) {
		int seconds = Integer.parseInt(secondsString);
		String s = "";
		int secondsRemainder = seconds%86400;
		int hours = secondsRemainder/3600;
		int minutes = (secondsRemainder%3600)/60;
		if (minutes < 9) {
			s = Integer.toString(hours) + ":0" + minutes;
		}
		else if(minutes == 0){
			s = Integer.toString(hours) + ":00";
		}	
		else {
			s = Integer.toString(hours) + ":" + minutes;
		}	
		return s;
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
     */
	public String[] toStringArray() 
	{
		String first = Long.toString(start.getTime());
		String second = Long.toString(end.getTime());
		
		return new String[] {first, second};
	}
	
	public String toString() {
		return toStringArray()[0] + "," + toStringArray()[1];
	}

	public String[] toStringArraySeconds() {

		String first = Long.toString(start.getTime()/1000);
		String second = Long.toString(end.getTime()/1000);
		
		return new String[] {first, second};
	}


}
