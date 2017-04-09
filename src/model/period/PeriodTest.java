package model.period;

import static org.junit.Assert.*;

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
	}

}
