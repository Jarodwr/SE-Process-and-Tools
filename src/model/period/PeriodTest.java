package model.period;

import org.junit.Test;

public class PeriodTest {

	@Test
	public void testConvertSecondsToDay01() {
		assert(Period.convertSecondsToDay(86401).equals("Tuesday"));
	}
	
	@Test
	public void testConvertSecondsToDay02() {
		assert(Period.convertSecondsToDay(86399).equals("Monday"));
	}
	
	@Test
	public void testConvertSecondsToDay03() {
		assert(Period.convertSecondsToDay(345600).equals("Friday"));
	}
	
	@Test
	public void testConvertSecondsToDay04() {
		assert(Period.convertSecondsToDay(90000).equals("Tuesday"));
	}
	
	@Test
	public void testConvertSecondsToDay05() {
		assert(Period.convertSecondsToDay(100000).equals("Tuesday"));
	}
	
	@Test
	public void testConvertSecondsToDay06() {
		assert(Period.convertSecondsToDay(604799).equals("Sunday"));
	}
	
	@Test
	public void testConvertSecondsToDay07() {
		assert(Period.convertSecondsToDay(1).equals("Monday"));
	}
	
	
	@Test
	public void testConversion24Hr01() {
		assert(Period.convert24HrTimeToDaySeconds("16:00") == (57600));
	}
	
	@Test
	public void testConversion24Hr02() {
		assert(Period.convert24HrTimeToDaySeconds("23:59") == (86340));
	}
	
	@Test
	public void testConversion24Hr03() {
		assert(Period.convert24HrTimeToDaySeconds("0:01") == (60));
	}
	
	@Test
	public void testConversion24Hr04() {
		assert(Period.convert24HrTimeToDaySeconds("12:00") == (43200));
	}
	
	@Test
	public void testConversion24Hr05() {
		assert(Period.convert24HrTimeToDaySeconds("5:59") == (21540));
	}
	
	@Test
	public void testConversion24Hr06() {
		assert(Period.convert24HrTimeToDaySeconds("0:00") == (0));
	}
	
	@Test
	public void testConvertDayToSeconds01() {
		assert(Period.convertDayToSeconds("Monday") == 0);
	}
	
	@Test
	public void testConvertDayToSeconds02() {
		assert(Period.convertDayToSeconds("Not a valid day") == 0);
	}
	
	@Test
	public void testConvertDayToSeconds03() {
		assert(Period.convertDayToSeconds("Tuesday") == 86400);
	}
	
	@Test
	public void testConvertDayToSeconds04() {
		assert(Period.convertDayToSeconds("Wednesday") == 172800);
	}
	
	@Test
	public void testConvertDayToSeconds05() {
		assert(Period.convertDayToSeconds("Thursday") == 259200);
	}
	
	@Test
	public void testConvertDayToSeconds06() {
		assert(Period.convertDayToSeconds("Friday") == 345600);
	}
	
	@Test
	public void testConvertDayToSeconds07() {
		assert(Period.convertDayToSeconds("Sunday") == 518400);
	}
	
	
	@Test
	public void testGetCurrentWeekBeginning() {
		// can't really test as I'm not actually sure what it returns because of timezones.  but appears to work in practice
	}
	
	
	@Test
	public void testCheckIsValidWeekday01() {
		assert(Period.checkIsValidWeekday("Monday") == true);
	}
	
	@Test
	public void testCheckIsValidWeekday02() {
		assert(Period.checkIsValidWeekday("Sunday") == true);
	}
	
	@Test
	public void testCheckIsValidWeekday03() {
		assert(Period.checkIsValidWeekday("WEDNESDAY") == true);
	}
	
	@Test
	public void testCheckIsValidWeekday04() {
		assert(Period.checkIsValidWeekday("friday") == true);
	}
	
	@Test
	public void testCheckIsValidWeekday05() {
		assert(Period.checkIsValidWeekday("MON") == false);
	}
	
	@Test
	public void testCheckIsValidWeekday06() {
		assert(Period.checkIsValidWeekday("Saturnday") == false);
	}
	
	
	@Test
	public void testGet24HrTimeFromWeekTime01() {
		assert(Period.get24HrTimeFromWeekTime(60).equals("0:01"));
	}
	
	@Test
	public void testGet24HrTimeFromWeekTime02() {
		assert(Period.get24HrTimeFromWeekTime(3600).equals("1:00"));
	}
	
	@Test
	public void testGet24HrTimeFromWeekTime03() {
		assert(Period.get24HrTimeFromWeekTime(86399).equals("23:59"));
	}
	
	@Test
	public void testGet24HrTimeFromWeekTime04() {
		assert(Period.get24HrTimeFromWeekTime(86340).equals("23:59"));
	}


	@Test
	public void duration01() {
		Period p2 = new Period(Integer.toString(Period.convert24HrTimeToDaySeconds("6:00")), Integer.toString(Period.convert24HrTimeToDaySeconds("7:00")), false);
		assert(p2.duration() == 3600);
	}
	

	@Test
	public void duration02() {
		Period p3 = new Period(Integer.toString(Period.convert24HrTimeToDaySeconds("5:00")), Integer.toString(Period.convert24HrTimeToDaySeconds("7:00")), false);
		assert(p3.duration() == 7200);
	}
	

	@Test
	public void duration03() {
		Period p3 = new Period(Integer.toString(Period.convert24HrTimeToDaySeconds("3:45")), Integer.toString(Period.convert24HrTimeToDaySeconds("11:00")), false);
		assert(p3.duration() == 26100);
	}
	

	@Test
	public void duration04() {
		Period p2 = new Period(Integer.toString(Period.convert24HrTimeToDaySeconds("5:00")), Integer.toString(Period.convert24HrTimeToDaySeconds("11:00")), false);
		assert(p2.duration() == 21600);
	}

}
