package timetable;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
	public boolean addPeriod(Period newPeriod) {
		for (int i = 0; i < periods.size(); i++) {
			Period combined = periods.get(i).combineWith(newPeriod);
			if (combined != null) {
				newPeriod = combined;
				periods.remove(periods.get(i));
				i = 0;
			}
		}
		periods.add(newPeriod);
		return false;
	}
	
	/**
	 * Finds a period and deletes it
	 * @param start This parameter is used to locate the period in question
	 * @return If successfully removed, will return true
	 */
	public boolean removePeriod(Period period) {
		return false;
	}
	
	public Period[] getAllPeriods() {
		Period[] p = new Period[periods.size()];
		periods.toArray(p);
		return p;
	}

	/**
	 * @return period1|period2|period3|period4
	 */
	public String toString() 
	{
		String string = "";
		 		
		 for(int i = 0; i > periods.size(); i++)
		 {
		 	string += periods.get(i).toString();
		 
		 	if(periods.get(i+1) != null) {
		 		string += "|";
		 	}
		 }
		 return string;
	}
	
	public String[][] toStringArray() 
	{
		String[][] timetable = new String[periods.size()][2];
		
		for(int i = 0; i < periods.size(); i++) {
			timetable[i] = periods.get(i).toStringArray();
		}
		return timetable;
	}
	
	public void mergeTimetable(String timetable) {
		StringTokenizer st = new StringTokenizer(timetable, "|");
		while (st.hasMoreTokens()) {
			StringTokenizer p = new StringTokenizer(st.nextToken(), ",");
			addPeriod(new Period(p.nextToken(), p.nextToken()));
		}
	}
	
	public void mergeTimetable(Timetable timetable) {
		Period[] periods = timetable.getAllPeriods();
		for (Period p : periods) {
			addPeriod(p);
		}
	}
}
