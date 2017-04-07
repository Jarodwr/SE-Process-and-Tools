package model.period;

import static org.junit.Assert.*;

import org.junit.Test;

public class PeriodTest {

	@Test
	public void testConvertSecondsToDay() {
		assert(Period.convertSecondsToDay(86401).equals("Tuesday"));
		assert(Period.convertSecondsToDay(1).equals("Monday"));
	}

}
