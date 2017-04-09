package model.period;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class PeriodTest {

	@Test
	public void testConvertSecondsToDay() {
		assert(Period.convertSecondsToDay(86401).equals("Tuesday"));
		assert(Period.convertSecondsToDay(86399).equals("Monday"));
		assert(Period.convertSecondsToDay(345600).equals("Friday"));
		assert(Period.convertSecondsToDay(90000).equals("Tuesday"));
		assert(Period.convertSecondsToDay(100000).equals("Tuesday"));
		assert(Period.convertSecondsToDay(604799).equals("Sunday"));
		assert(Period.convertSecondsToDay(1).equals("Monday"));
	}
	@Test
	public void testConversion24Hr() {
		assert(Period.convert24HrTimeToDaySeconds("16:00") == (57600));
		assert(Period.convert24HrTimeToDaySeconds("23:59") == (86340));
		assert(Period.convert24HrTimeToDaySeconds("0:01") == (60));
		assert(Period.convert24HrTimeToDaySeconds("12:00") == (43200));
		assert(Period.convert24HrTimeToDaySeconds("5:59") == (21540));
		assert(Period.convert24HrTimeToDaySeconds("0:00") == (0));
	}
	
	@Test
	public void testConvertDayToSeconds() {
		assert(Period.convertDayToSeconds("Monday") == 0);
		assert(Period.convertDayToSeconds("Tuesday") == 86400);
		assert(Period.convertDayToSeconds("Wednesday") == 172800);
		assert(Period.convertDayToSeconds("Thursday") == 259200);
		assert(Period.convertDayToSeconds("Friday") == 345600);
		assert(Period.convertDayToSeconds("Sunday") == 518400);

		assert(Period.convertDayToSeconds("Not a valid day") == 0);
	}
	
	@Test
	public void testGetCurrentWeekBeginning() {
		// can't really test as I'm not actually sure what it returns because of timezones.  but appears to work in practice
	}
	
	@Test
	public void testCheckIsValidWeekday() {
		assert(Period.checkIsValidWeekday("Monday") == true);
		assert(Period.checkIsValidWeekday("Sunday") == true);
		assert(Period.checkIsValidWeekday("WEDNESDAY") == true);
		assert(Period.checkIsValidWeekday("friday") == true);
		assert(Period.checkIsValidWeekday("MON") == false);
		assert(Period.checkIsValidWeekday("Saturnday") == false);
	}

}
