package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.period.Period;

public class AvailabilityPicker extends TimePicker {
	
	private ArrayList<Integer> available = new ArrayList<Integer>();
	private final String[] listOfDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private boolean enabled = true;

    private String[] availableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #27e833","-fx-border-color: black;-fx-background-color: #ff0000", "-fx-border-color: black;-fx-background-color: #27e833"};
    
    public void init(Pane parent) {
    	container.setMaxWidth(parent.getPrefWidth());
    	container.setMaxHeight(parent.getPrefHeight());
    	
    	for (int i = 0; i < 24; i++) {
    		Pane am = new Pane();
    		am.setStyle(unavailableStyle[0]);
    		addPaneListener(am, i);
    		timeGrid.add(am, i, 0);
    		
    		Pane pm = new Pane();
    		pm.setStyle(unavailableStyle[0]);
    		addPaneListener(pm, i+24);
    		timeGrid.add(pm, i, 2);
    		
    		if (i%2 == 0) {
    			int time = i%24;
    			if (time == 0)
    				time = 24;
    			time /= 2;
    			//Hacky solution to alignment
    			String str = "";
    			if (time < 10) {
    				str += "  ";
    			}
    			str += "  " + Integer.toString(time);
    			timeGrid.add(new Label(str), i, 1);
    			timeGrid.add(new Label(str), i, 3);
    		} else {
    			timeGrid.add(new Label("am"), i, 1);
    			timeGrid.add(new Label("pm"), i, 3);
    		}
    	}
    }
    
    public ArrayList<String> saveTimes(String day) {
    	int howManySecondsInWeekSoFar = Period.convertDayToSeconds(day);
    	Collections.sort(selected);
    	ArrayList<String> timeperiods = new ArrayList<String>();
    	boolean oddAvailabilities = false;
    	if (selected.size() == 0) return null; // Not available at all 
    	int k = 0;
    	for(int i : selected) {
    		if (k > i) continue;
    		k = i;
    		while(selected.contains(++k)) {
    		}
    		if (k == 0) {
    			timeperiods.add(Integer.toString(howManySecondsInWeekSoFar + i*30*60 ) ) ;
    			timeperiods.add(Integer.toString((howManySecondsInWeekSoFar + i*30*60 + 30*60) - 1) ) ;
    			
    		}
    		else {
        		timeperiods.add(Integer.toString(howManySecondsInWeekSoFar + i*30*60) ) ;
        		timeperiods.add(Integer.toString((howManySecondsInWeekSoFar + k*30*60) - 1) );
    		}
    		
    	}
    	return timeperiods;
    }
    
    public void setDefaultAvailability(String[][] times, String day) {
    	int lowerBoundsDay = Period.convertDayToSeconds(day);
    	int upperBoundsDay = lowerBoundsDay + 86400;
    	selected.clear();
    	if (times == null || times.length == 0) {
    		return;
    	}
    	for(int i = 0; i < times.length; i++) {
    		int time1 = Integer.parseInt(times[i][0])/(30*60); 
    		int time2 = Integer.parseInt(times[i][1])/(30*60);
    		if (time1 < lowerBoundsDay || time1 > upperBoundsDay) {
    			continue;
    		}
    		time1 = time1/(30*60);
    		time2 = time2/(30*60);

    		for(int j = time1; j <= time2; j++) {
    			selected.add(j);
    			getTimePane(j).setStyle(availableStyle[0]);
    		}
    	}

    }
    
    public void setDefaultAvailabilityFromList(ArrayList<String> times, String day) {
    	int lowerBoundsDay = Period.convertDayToSeconds(day);
    	int upperBoundsDay = lowerBoundsDay + 86400;
    	
		String[] style = getAppropriateStyle(0);
    	selected.clear();
    	if (times == null || times.size() == 0) {
    		return;
    	}
    	for(int i = 0; i < times.size(); i = i + 2) {
    		int time1 = (Integer.parseInt(times.get(i)));
    		int time2 = (Integer.parseInt(times.get(i + 1)));

    		time1 = time1%86400/(30*60);

    		time2 = time2%86400/(30*60);

    		for(int j = time1; j <= time2; j++) {
    			selected.add(j);
    			getTimePane(j).setStyle(availableStyle[0]);
    		}
    	}
	}
    
    
    
    protected void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if (enabled) {
					String[] style = getAppropriateStyle(i);
					
					for (int j = i; j < Math.min(i + 1, 48); j++) {
						if (selected.contains(j)) {
							for(int k = 0; k < selected.size(); k++) {
								if (selected.get(k) == j) {
									selected.remove(k);
								}
							}
							getTimePane(j).setStyle(style[1]);
						}
						else {
	
							selected.add(j);
	
							getTimePane(j).setStyle(style[0]);
						}
					}
				}
			}
			
    	});
    	/*
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[1]);
			}
    	}); */
    	/*
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[0]);
			} 
    	}); */
    }
    
    String[] getAppropriateStyle(int i) {
    			return availableStyle;
    }
    
    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }
    
    public void setAvailability(String[][] times, LocalDate date) {
    	long dayInSecs = 86400;
    	long periodInSecs = 1800;
    	
    	for (int i : available)	//Reset all available panes to unavailable panes
    		getTimePane(i).setStyle(unavailableStyle[0]);
    	available.removeAll(available);	//Wipe info from panes

    	int day = (int)(((date.toEpochDay() * dayInSecs) / 86400) % 7);

    	for (long startTime = day * dayInSecs; startTime < (day+1) * dayInSecs; startTime += periodInSecs) {
			Long endTime = startTime + periodInSecs;
			boolean success = false;
			
    		for (String[] p : times) {
    			if (Long.parseLong(p[0]) <= startTime && Long.parseLong(p[1]) >= endTime) {
    				success = true;
    				break;
    			}
    		}
			if (success) {
				available.add((int)((startTime - date.toEpochDay() * dayInSecs)/periodInSecs));
			}
    	}
    	
    	for (int i : available)	{//Paint available panes white
    		getTimePane(i).setStyle(availableStyle[0]);
    	}

    }
}
    
    
