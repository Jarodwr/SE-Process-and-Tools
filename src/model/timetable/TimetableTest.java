package model.timetable;

import org.junit.Test;

public class TimetableTest {

	@Test
	public void mergeTimetable1() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600|21600,43200");
		System.out.println(t.toString());
		assert(t.toString().equals("90000,93600|97200,100600|21600,43200"));
	}

	@Test
	public void mergeTimetable2() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600|21600,43200");
		t.mergeTimetable("93500,97300");
		assert(t.toString().equals("21600,43200|90000,100600"));
	}
	
	@Test
	public void toStringArray() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600|21600,43200");
		String[][] sa = t.toStringArray();

		assert(sa[0][0].equals("90000") && sa[0][1].equals("93600"));
		assert(sa[1][0].equals("97200") && sa[1][1].equals("100600"));
		assert(sa[2][0].equals("21600") && sa[2][1].equals("43200"));
	}
}
