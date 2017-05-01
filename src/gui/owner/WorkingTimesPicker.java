package gui.owner;

import java.time.LocalDate;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.period.Period;

public class WorkingTimesPicker extends TimePicker {
	
    @Override
    public void init(Pane p) {
    	super.init(p);
    	this.availableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ffffff","-fx-border-color: black;-fx-background-color: #dddddd", "-fx-border-color: black;-fx-background-color: #79CDCD"};
    	this.unavailableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ff0000","-fx-border-color: black;-fx-background-color: #dd0000", "-fx-border-color: black;-fx-background-color: #79CDCD"};
    }
    
    public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}
    
    public void setSelected(String[][] times, String day) {
    	int lowerBoundsDay = Period.convertDayToSeconds(day);
    	int upperBoundsDay = lowerBoundsDay + 86400;
    	
    	selected.clear();
    	
    	if (times == null || times.length == 0)
    		return;

    	for(int i = 0; i < times.length; i++) {
    		int time1 = Integer.parseInt(times[i][0])/(30*60); 
    		int time2 = Integer.parseInt(times[i][1])/(30*60);
    		
    		if (time1 < lowerBoundsDay || time1 > upperBoundsDay)
    			continue;

    		time1 = time1/(30*60);
    		time2 = time2/(30*60);

    		for(int j = time1; j <= time2; j++) {
    			selected.add(j);
    			getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
    		}
    	}
    }
    
    @Override
    public void setAvailability(String[][] times, LocalDate date) {
    	long dayInSecs = 86400;
    	long periodInSecs = 1800;
    	
    	for (int i : available)	{//Reset all available panes to unavailable panes
    		if (getTimePane(i) != null)
    			getTimePane(i).setStyle(unavailableStyle[0]);
    	}
    	
    	available.removeAll(available);	//Wipe info from panes

    	int day = (int)(((date.toEpochDay() * dayInSecs) / 86400) % 7);
    	System.out.println(day);
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
    	
    	for (int i : available)	{	//Paint available panes white
    		getTimePane(i).setStyle(availableStyle[0]);
    	}
    }
    
    @Override
    protected void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				
				if (selected.contains(i)) {
					selected.remove(Integer.valueOf(i));
					p.setStyle(getAppropriateStyle(i)[0]);
				} else {
					selected.add(i);
					p.setStyle(getAppropriateStyle(i)[2]);
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if (selected.contains(i))
					return;
				p.setStyle(getAppropriateStyle(i)[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if (selected.contains(i))
					return;
				p.setStyle(getAppropriateStyle(i)[0]);
			}
    	});
    }
    
    public void setDefaultAvailability(String[][] times, String day) {
    	System.out.println(day);
    	int lowerBoundsDay = ((Integer.parseInt(day)-4)%7 )* 86400;
    	int upperBoundsDay = lowerBoundsDay + 86400;
    	
    	for (int i : available) {
    		getTimePane(i).setStyle(unavailableStyle[0]);
    	}
    	available.clear();

    	if (times == null || times.length == 0) {
    		return;
    	}
    	
    	for(int i = 0; i < times.length; i++) {
    		int time1 = Integer.parseInt(times[i][0]); 
    		int time2 = Integer.parseInt(times[i][1]);
    		if (time1 < lowerBoundsDay || time1 > upperBoundsDay) {
    			continue;
    		}
    		
    		time1 = (time1 / (30*60))%48;
    		time2 = (time2 / (30*60))%48;
    		
    		System.out.println(time1 + ":" + time2);
    		for(int j = time1; j <= time2; j++) {
    			available.add(j);
    			getTimePane(j).setStyle(availableStyle[0]);
    		}
    	}
    }
}
