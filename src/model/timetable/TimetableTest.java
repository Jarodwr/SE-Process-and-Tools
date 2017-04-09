package model.timetable;

import org.junit.Test;

import model.period.Period;

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
	public void mergeTimetable3() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600");
		t.mergeTimetable("93500,97300|21600,43200");
		assert(t.toString().equals("90000,100600|21600,43200"));
	}
	
	@Test
	public void mergeTimetable4() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600");
		t.mergeTimetable("21600,43200");
		assert(t.toString().equals("90000,93600|97200,100600|21600,43200"));
	}
	
	@Test
	public void mergeTimetable5() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600");
		t.mergeTimetable("21600,43200");
		assert(t.toString().equals("90000,93600|21600,43200"));
	}
	
	@Test
	public void mergeTimetable6() {
		Timetable t = new Timetable();
		t.mergeTimetable("40000,43600");
		t.mergeTimetable("91600,123200");
		assert(t.toString().equals("40000,43600|91600,123200"));
	}
	
	@Test
	public void addPeriod1() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		assert(t.toString().equals("90000,93600"));
	}
	
	@Test
	public void addPeriod2() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("40000,43600"));
	}
	
	@Test
	public void addPeriod3() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("40000","43600",false));
		t.addPeriod(new Period("90000","93600",false));
		assert(t.toString().equals("40000,43600|90000,93600"));
	}
	
	@Test
	public void addPeriod4() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600|40000,43600"));
	}
	
	
	@Test
	public void removePeriod1() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600|40000,43600"));
		t.removePeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600"));
	}
	
	@Test
	public void removePeriod2() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600|40000,43600"));
		t.removePeriod(new Period("90000","93600",false));
		assert(t.toString().equals("40000,43600"));
	}
	
	@Test
	public void removePeriod3() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600|40000,43600"));
		t.removePeriod(new Period("90000","93600",false));
		t.removePeriod(new Period("40000","43600",false));
		assert(t.toString().equals(""));
	}
	
	
	@Test
	public void removePeriod4() {
		Timetable t = new Timetable();
		t.addPeriod(new Period("90000","93600",false));
		t.addPeriod(new Period("40000","43600",false));
		assert(t.toString().equals("90000,93600|40000,43600"));
		t.removePeriod(new Period("90000","93600",false));
		assert(t.toString().equals("40000,43600"));
		assert(t.removePeriod(new Period("40000","43600",false)));
	}
	
	
	@Test
	public void toStringArray01() {
		Timetable t = new Timetable();
		t.mergeTimetable("90000,93600|97200,100600|21600,43200");
		String[][] sa = t.toStringArray();

		assert(sa[0][0].equals("90000") && sa[0][1].equals("93600"));
		assert(sa[1][0].equals("97200") && sa[1][1].equals("100600"));
		assert(sa[2][0].equals("21600") && sa[2][1].equals("43200"));
	}
}
