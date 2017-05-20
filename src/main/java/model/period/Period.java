package model.period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Period {
	private static Logger LOGGER = Logger.getLogger("main");
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
			LOGGER.log(Level.SEVERE,e.toString());
			e.printStackTrace();
		}
	}
	
	
	/**
   	 * Converts 24 hr time to Day seconds in unix format
   	 * @param time	Time in 24 hr format
   	 * @return seconds
   	 **/
	
	public static int convert24HrTimeToDaySeconds(String time) {
		int seconds = 0;
		if (time.charAt(0) == ('0') && time.charAt(1) != (':')) {
			time = time.substring(1);
		}
		String[] splithoursseconds= time.split(":");
		seconds = seconds + (Integer.parseInt(splithoursseconds[0]) * 3600);
		seconds = seconds + (Integer.parseInt(splithoursseconds[1]) * 60);
		LOGGER.log(Level.FINE, "convert24HrTimeToDaySeconds given argument time:"+time+" Converted seconds:"+seconds);
		return seconds;
	}
	
	/**
   	 * Converts a given day to unix seconds
   	 * @param day	weekday in string format i.e Tuesday
   	 * @return seconds
   	 **/
	
	public static int convertDayToSeconds(String day) {
		int i;
		for(i = 0; i < 7;i++) {
				if (weekdays[i].equals(day)) {
					LOGGER.log(Level.FINE, "convertDayToSeconds given argument day:"+day+" Converted seconds:"+i * secondsInDay);
					return i * secondsInDay;
				}
		}
		LOGGER.log(Level.FINE, "convertDayToSeconds given argument day:"+day+" Converted seconds: Failed");
		return 0; // should be unreachable generally unless the day var is not valid
	}
	
	/**
   	 * Get duration of the period
   	 * @return seconds
   	 **/
	
	public long duration() {
		return end.getTime() - start.getTime();
	}
	
	/**
   	 * Converts unix seconds to a weekday
   	 * @param seconds	Integer seconds in unix format
   	 * @return weekday
   	 **/
	
	public static String convertSecondsToDay(int seconds) {
		int division = seconds/86400;
		LOGGER.log(Level.FINE, "convertSecondsToDay given argument seconds:"+seconds+" Converted day:"+weekdays[Math.min(6,division)]);
		return weekdays[Math.min(6,division)];
	}
	
	/**
   	 * Converts unix seconds to a weekday
   	 * @param seconds	String seconds in unix format
   	 * @return weekday
   	 **/
	
	public static String convertSecondsToDay(String seconds) {
		int secs = Integer.parseInt(seconds);
		int division = secs/86400;
		LOGGER.log(Level.FINE, "convertSecondsToDay given argument seconds:"+seconds+" Converted day:"+weekdays[Math.min(6,division)]);
		return weekdays[Math.min(6,division)];
	}
	
	/**
   	 * Converts unix seconds to a calendar object
   	 * @param seconds	String seconds in unix format
   	 * @return calendar
   	 **/
	
	public static Calendar unixToCalendar(long unixTime){
	    Calendar c = Calendar.getInstance();
	    c.setTimeInMillis(unixTime);
	    return c;
	}
	
	/**
   	 * Converts a calendar object to unix seconds
   	 * @param calendar	A calendar object
   	 * @return seconds
   	 **/
	
	public static long calendarToUnix(Calendar calendar){
	    Long unix = calendar.getTimeInMillis();
	    LOGGER.log(Level.FINE, "calendarToUnix Converted unix:"+unix);
	    return unix;
	}
	
	/**
   	 * Get week beginning from a date based on unix time
   	 * @param unixtimestamp	in String format
   	 * @return seconds
   	 **/
	
	public static long getCurrentWeekBeginning(String unixtimestamp) {
		Calendar c = unixToCalendar(Long.parseLong(unixtimestamp)*1000);
		c.getFirstDayOfWeek();
		LOGGER.log(Level.FINE, "getCurrentWeekBeginning given argument unixtimestamp:"+unixtimestamp+" Converted String weekBeginning:"+calendarToUnix(c)/1000);
		return calendarToUnix(c)/1000;
			
	}
	
	/**
   	 * Get week beginning from a date based on unix time
   	 * @param unixtimestamp	in Long format
   	 * @return seconds
   	 **/
	
	public static long getCurrentWeekBeginning(Long unixtimestamp) {
		Calendar c = unixToCalendar(unixtimestamp*1000);
		c.getFirstDayOfWeek();
		LOGGER.log(Level.FINE, "getCurrentWeekBeginning given argument unixtimestamp:"+unixtimestamp+" Converted Long weekBeginning:"+calendarToUnix(c)/1000);
		return calendarToUnix(c)/1000;
			
	}
	
	/**
   	 * Get if a string is a valid weekday
   	 * @param weekdayToCheck
   	 * @return success
   	 **/
	
	public static boolean checkIsValidWeekday(String weekdayToCheck) {
		for(int i  = 0; i < weekdays.length; i++) {
			if (weekdayToCheck.toLowerCase().equals(weekdays[i].toLowerCase())) {
				LOGGER.log(Level.FINE, "checkIsValidWeekday given argument weekdayToCheck:"+weekdayToCheck+" returned Boolean: true");
				return true;
			}
		}
		LOGGER.log(Level.FINE, "checkIsValidWeekday given argument weekdayToCheck:"+weekdayToCheck+" returned Boolean: false");
		return false;
	}
	
	/**
   	 * Get time in 24 hr format from week unix time
   	 * @param seconds	in int format
   	 * @return time
   	 **/
	
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
		LOGGER.log(Level.FINE, "get24HrTimeFromWeekTime given argument seconds:"+seconds+" returned Int Seconds:"+s);
		return s;
	}
	
	/**
   	 * Get time in 24 hr format from week unix time
   	 * @param seconds	in String format
   	 * @return time
   	 **/
	
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
		
		LOGGER.log(Level.FINE, "get24HrTimeFromWeekTime given argument seconds:"+seconds+" returned String Time:"+s);
		return s;
  }
	
	/**
   	 * Get start of current period
   	 * @return time
   	 **/
	
	public Date getStart() {
		return this.start;
	}
	
	/**
   	 * Get end of current period
   	 * @return time
   	 **/
	
	public Date getEnd() {
		return this.end;
	}
	
	
	
	/**
   	 * Get start and end of current period in an array
   	 * @return time [start][end]
   	 **/
	
	public String[] toStringArray() 
	{
		String first = Long.toString(start.getTime());
		String second = Long.toString(end.getTime());
		
		return new String[] {first, second};
	}
	
	/**
   	 * Get start and end of current period
   	 * @return seconds  separated by a comma
   	 **/
	
	public String toString() {
		return start.getTime() + "," + end.getTime();
	}
	
	/**
   	 * Get start and end of current period in an array
   	 * @return time [start][end] in seconds
   	 **/

	public String[] toStringArraySeconds() {

		String first = Long.toString(start.getTime()/1000);
		String second = Long.toString(end.getTime()/1000);
		
		return new String[] {first, second};
	}


}
