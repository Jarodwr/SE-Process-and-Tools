package model.timetable;

import java.util.ArrayList;
import java.util.StringTokenizer;

import model.period.Period;

/**
 * An extended collection of periods which allows for adding and removing periods in a specific way
 */
public class Timetable {
	
	private ArrayList<Period> periods = new ArrayList<Period>();
	
	public Timetable() {
		
	}
	
	/**
	 * Checks if the period being added clashes with any periods currently
	 * on the timetable and adds it to 'periods'
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
	 * Finds a period and deletes it, however it needs to leave all connected periods intact
	 * @return If successfully removed, will return true. If no periods are removed return false.
	 */
	public boolean removePeriod(Period period) {
		boolean remove = false;
		for (int i = 0; i < periods.size(); i++) {
			
			Period p = periods.get(i);
			int compareStarts = period.getStart().compareTo(p.getStart());
			int compareEnds = period.getEnd().compareTo(p.getEnd());
			
			//Exact match, period directly removed
			if (compareStarts == 0 && compareEnds == 0) {
				periods.remove(i);
				remove = true;
			} 
			//Subset match, removal period is subset of current period, removal period is removed.
			else if (compareStarts > 0 && compareEnds < 0) {
				periods.add(new Period(p.getStart(), period.getStart()));
				periods.add(new Period(period.getEnd(), p.getEnd()));
				periods.remove(i);
				remove = true;
				break;
			}
			//Superset match, current period is a subset of removal period
			else if (compareStarts < 0 && compareEnds > 0) {
				periods.remove(i);
				remove = true;
			}

			//End match, start of current period to end of removal period is removed.
			else if (compareStarts <= 0 && compareEnds < 0) {
				periods.add(new Period(period.getEnd(), p.getEnd()));
				periods.remove(i);
				remove = true;
			} 
			//Start match, start of removal period to end of current period is removed.
			else if (compareStarts > 0 && compareEnds >= 0) {
				periods.add(new Period(p.getStart(), period.getStart()));
				periods.remove(i);
				remove = true;
			}
		}
		return remove;
	}
	
	public Period[] getAllPeriods() {
		Period[] p = new Period[periods.size()];
		periods.toArray(p);
		return p;
	}

	/**
	 * @return "period1details|period2details|period3details|period4details"
	 */
	public String toString() 
	{
		String string = "";
		 		
		 for(int i = 0; i < periods.size(); i++)
		 {
		 	string += periods.get(i).toString();
		 
		 	if(i != periods.size()-1)
		 	{
		 		string += "|";
		 	}
		 }
		 return string;
	}
	
	/**
	 * @return String[Period][Details]
	 */
	public String[][] toStringArray() 
	{
		String[][] timetable = new String[periods.size()][2];
		
		for(int i = 0; i < periods.size(); i++)
		{
			timetable[i] = periods.get(i).toStringArray();
		}
		return timetable;
	}
	
	/**
	 * Parses a timetable from a string and merges it with the current timetable
	 * @param timetable
	 */
	public void mergeTimetable(String timetable) {
		StringTokenizer st = new StringTokenizer(timetable, "|");
		while (st.hasMoreTokens()) {
			StringTokenizer p = new StringTokenizer(st.nextToken(), ",");
			addPeriod(new Period(p.nextToken() + "000", p.nextToken() + "000", false));
		}
	}
	
	/**
	 * Iterates through the timetable and adds all periods to the current timetable
	 * @param timetable
	 */
	public void mergeTimetable(Timetable timetable) {
		Period[] periods = timetable.getAllPeriods();
		for (Period p : periods) {
			addPeriod(p);
		}
	}
}
