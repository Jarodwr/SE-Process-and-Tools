package timetable;

import java.util.ArrayList;

import period.Period;

public class Timetable {
	
	private ArrayList<Period> periods = new ArrayList<Period>();
	
	public Timetable() {
		
	}
	
	/**
	 * Checks if the period being added clashes with any periods currently
	 * on the timetable and adds it to 'periods'
	 * @param start start time of the period
	 * @param end end time of the period
	 * @return Returns true if there is no clash and the period is 
	 * successfully added
	 */
	public boolean addPeriod(String start, String end) {
		return false;
	}
	
	/**
	 * Finds a period and deletes it
	 * @param start This parameter is used to locate the period in question
	 * @return If successfully removed, will return true
	 */
	public boolean removePeriod(String start) {
		return false;
	}

	/**
	 * @return period1|period2|period3|period4
	 */
	public String toString() {
		return "";
	}
}
