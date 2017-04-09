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
	public boolean addPeriod(Period period) {
		for (int i = 0; i < periods.size(); i++) {
			
			Period p = periods.get(i);
			int compareStarts = period.getStart().compareTo(p.getStart());
			int compareEnds = period.getEnd().compareTo(p.getEnd());
			
			//Exact match, period directly removed
			if (compareStarts == 0 && compareEnds == 0) {
				return true;
			} 
			//Subset match, removal period is subset of current period, removal period is removed.
			else if (compareStarts > 0 && compareEnds < 0) {
				return true;
			}
			//Superset match, current period is a subset of removal period
			else if (compareStarts < 0 && compareEnds > 0) {
				periods.remove(i);
			}

			//End match, start of current period to end of removal period is removed.
			else if (compareStarts <= 0 && compareEnds < 0 && period.getEnd().compareTo(p.getStart()) > 0) {
				period = new Period(period.getStart(), periods.get(i).getEnd());
				periods.remove(i);
			} 
			//Start match, start of removal period to end of current period is removed.
			else if (compareStarts > 0 && compareEnds >= 0 && period.getStart().compareTo(p.getEnd()) < 0) {
				period = new Period(periods.get(i).getStart(), period.getEnd());
				periods.remove(i);
			} else {
				continue;
			}
			i = -1;
		}
		periods.add(period);
		return true;
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
			else if (compareStarts <= 0 && compareEnds < 0 && period.getEnd().compareTo(p.getStart()) > 0) {
				periods.add(new Period(period.getEnd(), p.getEnd()));
				periods.remove(i);
				remove = true;
			} 
			//Start match, start of removal period to end of current period is removed.
			else if (compareStarts > 0 && compareEnds >= 0 && period.getStart().compareTo(p.getEnd()) < 0) {
				periods.add(new Period(p.getStart(), period.getStart()));
				periods.remove(i);
				remove = true;
			}
		}
		return remove;
	}
	
	public Timetable applicablePeriods(long duration) {
		Timetable t = new Timetable();
		for (Period p : periods) {
			if (p.duration() > duration) {
				t.addPeriod(new Period(Long.toString(p.getStart().getTime()), Long.toString(p.getEnd().getTime() - duration), false));
			}
		}
		return t;
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
		String[][] timetable = new String[periods.size()][];
		
		for(int i = 0; i < periods.size(); i++)
		{
			timetable[i] = periods.get(i).toStringArray();
		}
		return timetable;
	}
	
	public boolean isStartTimeIn(long startTime) {
		for (Period p : periods) {
			if (startTime > p.getStart().getTime() && startTime < p.getEnd().getTime()) {
				return true;
			}
		}
		return false;
	}
	
	public String[][] toStringArraySeconds() 
	{
		String[][] timetable = new String[periods.size()][];
		
		for(int i = 0; i < periods.size(); i++)
		{
			timetable[i] = periods.get(i).toStringArraySeconds();
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
			addPeriod(new Period(p.nextToken(), p.nextToken(), false));
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
