package model.booking;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import model.period.Period;

public class BookingsTestCases 
{

	@Test
	public void BookingToStringTest() {
		@SuppressWarnings("deprecation")
		Booking testBooking = new Booking("B00001", "John",new Period(new Date(2017,3,12,7,0), new Date(2017,3,12,9,0)));
		if (!testBooking.toString().equals("John:7:0 12/3/2017,9:0 12/3/2017")) 
		{
			System.out.println(testBooking.toString());
			fail();
		}
	}
	
	

}