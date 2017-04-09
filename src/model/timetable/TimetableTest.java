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
		
		for (int i = 0; i < sa.length; i++) {
			System.out.println(i + " | " + sa[i][0] + " : " + sa[i][1]);
		}
	}
}
