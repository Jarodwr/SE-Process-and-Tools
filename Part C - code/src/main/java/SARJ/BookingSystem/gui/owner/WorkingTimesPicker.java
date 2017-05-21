package SARJ.BookingSystem.gui.owner;

import java.time.LocalDate;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WorkingTimesPicker extends TimePicker {
	
	int focus = -1;
	
    @Override
    public void init(Pane p) {
    	super.init(p);
    	this.availableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ffffff","-fx-border-color: black;-fx-background-color: #dddddd", 
    			"-fx-border-color: black;-fx-background-color: #79CDCD", "-fx-border-color: black;-fx-background-color: #528989"};
    	this.unavailableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ff0000","-fx-border-color: black;-fx-background-color: #dd0000", 
    			"-fx-border-color: black;-fx-background-color: #79CDCD", "-fx-border-color: black;-fx-background-color: #528989"};
    }
    
    public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}
    
    @Override
    protected void addPaneListener(final Pane p, final int i) {
    	
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0) {
				if (focus > -1 && focus != i) {
					if (focus > i) {
						for (int j = i; j <= focus; j++) {
							if (arg0.getButton() == MouseButton.SECONDARY) {
								selected.remove(Integer.valueOf(j));
								getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
							} else if (arg0.getButton() == MouseButton.PRIMARY){
								getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
								if (!selected.contains(j))
									selected.add(j);
							}
							
						}
					} else {
						for (int j = i; j >= focus; j--) {
							if (arg0.getButton() == MouseButton.SECONDARY) {
								selected.remove(Integer.valueOf(j));
								getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
							} else if (arg0.getButton() == MouseButton.PRIMARY){
								getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
								if (!selected.contains(j))
									selected.add(j);
							}
							
						}
					}
					focus = -1;
				} else if (focus == i) {
					if (arg0.getButton() == MouseButton.SECONDARY) {
						if (selected.contains(i))
							selected.remove(Integer.valueOf(i));
					} else if (arg0.getButton() == MouseButton.PRIMARY) {
						if (selected.contains(i))
							return;
						selected.add(i);
					}
					p.setStyle(getAppropriateStyle(i)[2]);
					focus = -1;
				} else {
					focus = i;	
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0) {

				if (focus > -1) {
					if (focus > i) {
						for (int j = i; j < focus; j++) {
							if (selected.contains(j)) {
								getTimePane(j).setStyle(getAppropriateStyle(j)[3]);
							} else {
								getTimePane(j).setStyle(getAppropriateStyle(j)[1]);
							}
						}
					} else {
						for (int j = i; j > focus; j--) {
							if (selected.contains(j)) {
								getTimePane(j).setStyle(getAppropriateStyle(j)[3]);
							} else {
								getTimePane(j).setStyle(getAppropriateStyle(j)[1]);
							}
						}
					}
				} else {
					if (selected.contains(i)) {
						getTimePane(i).setStyle(getAppropriateStyle(i)[3]);
					} else {
						p.setStyle(getAppropriateStyle(i)[1]);
					}
				}
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0) {

				if (focus > -1) {
					if (focus > i) {
						for (int j = i; j < focus; j++) {
							if (selected.contains(j)) {
								getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
							} else {
								getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
							}
						}
					} else {
						for (int j = i; j > focus; j--) {
							if (selected.contains(j)) {
								getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
							} else {
								getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
							}
						}
					}
				} else {
					if (selected.contains(i)) {
						getTimePane(i).setStyle(getAppropriateStyle(i)[2]);
					} else {
						p.setStyle(getAppropriateStyle(i)[0]);
					}
				}
			}
    	});
    }
    
    public void setDefaultAvailability(String[][] times, LocalDate day) {

    	long lowerBoundsDay = ((day.toEpochDay()-4)%7 )* 86400;
    	long upperBoundsDay = lowerBoundsDay + 86400;
    	
    	for (int i : available)
    		getTimePane(i).setStyle(unavailableStyle[0]);
    	available.clear();

    	if (times == null || times.length == 0)
    		return;
    	
    	for(int i = 0; i < times.length; i++) {
    		int time1 = Integer.parseInt(times[i][0]); 
    		int time2 = Integer.parseInt(times[i][1]);
    		if (time1 < lowerBoundsDay || time1 > upperBoundsDay)
    			continue;
    		
    		time1 = (time1 / (30*60))%48;
    		time2 = (time2 / (30*60))%48;
    		
    		for(int j = time1; j < time2; j++) {
    			available.add(j);
    			getTimePane(j).setStyle(availableStyle[0]);
    		}
    	}
    }
    
    public void setDefaultSelected(String[][] times, LocalDate day) {

    	long lowerBoundsDay = day.toEpochDay() * 86400;
    	long upperBoundsDay = lowerBoundsDay + 86400;
    	
    	for (int i : selected)
    		getTimePane(i).setStyle(getAppropriateStyle(i)[0]);
    	selected.clear();

    	if (times == null || times.length == 0)
    		return;

    	for(int i = 0; i < times.length; i++) {
    		int time1 = Integer.parseInt(times[i][0]); 
    		int time2 = Integer.parseInt(times[i][1]);
    		if (time1 < lowerBoundsDay || time1 > upperBoundsDay)
    			continue;
    		
    		time1 = (time1 / (30*60))%48;
    		time2 = (time2 / (30*60))%48;
    		
    		for(int j = time1; j < time2; j++) {
    			selected.add(j);
    			getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
    		}
    	}
    }
}
