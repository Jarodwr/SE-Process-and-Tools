package gui.owner;

import java.util.ArrayList;
import java.util.Collections;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class AvailabilityPicker extends TimePicker {
	

    private String[] availableStyle = new String[]{"-fx-background-color: #27e833","-fx-background-color: #ff0000", "-fx-background-color: #27e833"};
    
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
    			timeGrid.add(new Label(Integer.toString(i/2)), i, 1);
    			timeGrid.add(new Label(Integer.toString((i + 24)/2)), i, 3);
    		}
    	}
    }
    
    public ArrayList<String> saveTimes() {
    	Collections.sort(selected);
    	ArrayList<String> timeperiods = new ArrayList<String>();
    	boolean oddAvailabilities = false;
    	if (selected.size() == 0) return null; // Not available at all 
    	if (selected.size() % 2 != 0) oddAvailabilities = true;
    	int j = 1;
    	for(int i : selected) {
    		if (j%2 == 0){  // end of period 
    			j++;
    			continue;
    		}
    		if (oddAvailabilities && j == selected.size()) {
    			timeperiods.add(Integer.toString( selected.get(j - 1)*30*60 )  + "-" + Integer.toString( (selected.get(j - 1)*30*60 + 30*60) ) );
    			break;
    		}
    		timeperiods.add(Integer.toString( (selected.get(j)*30*60) ) + "-" + Integer.toString( (selected.get(j)*30*60) ) );
    		
    		j++;
    	}
    	return timeperiods;
    }
    
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				
				for (int j = i; j < Math.min(i + 1, 48); j++) {
					if (selected.contains(j)) {
						for(int k = 0; k < selected.size(); k++) {
							if (selected.get(k) == j) {
								System.out.println(selected.toString());
								selected.remove(k);
							}
						}
						getTimePane(j).setStyle(style[1]);
					}
					else {

						selected.add(j);

						System.out.println(selected.toString());
						getTimePane(j).setStyle(style[0]);
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
}
    
    
