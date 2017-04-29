package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class TimePicker {


    @FXML
	protected AnchorPane container;

    @FXML
	protected GridPane timeGrid;
    
    private String[] availableStyle = new String[]{"-fx-background-color: #ffffff","-fx-background-color: #dddddd", "-fx-background-color: #bbbbbb"};
    protected String[] unavailableStyle = new String[]{"-fx-background-color: #ff0000","-fx-background-color: #dd0000", "-fx-background-color: #bb0000"};
    
    ArrayList<Integer> selected = new ArrayList<Integer>();
    private ArrayList<Integer> available = new ArrayList<Integer>();
    
    int duration = 1;

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
    
    public void setAvailability(String[][] times, LocalDate date) {
    	long dayInMillis = 86400000;
    	long periodInMillis = 1800000;
    	
    	for (int i : available)	//Reset all available panes to unavailable panes
    		getTimePane(i).setStyle(unavailableStyle[0]);
    	available.removeAll(available);	//Wipe info from panes

    	
    	for (long startTime = date.toEpochDay() * dayInMillis/1000; startTime < (date.toEpochDay()+1) * dayInMillis/1000; startTime += periodInMillis) {
			Long endTime = startTime + periodInMillis;
			boolean success = false;
			
    		for (String[] p : times) {
    			if (Long.parseLong(p[0]) <= startTime && Long.parseLong(p[1]) >= endTime) {
    				success = true;
    				break;
    			}
    		}
			if (success) {
				available.add((int)((startTime - date.toEpochDay() * dayInMillis/1000)/periodInMillis));
			}
    	}
    	
    	for (int i : available)	{//Paint available panes white
    		getTimePane(i).setStyle(availableStyle[0]);
    	}

    }
    
    public int[] getSelectedPeriods() {
    	int[] t = new int[selected.size()];
    	for (int i = 0; i < selected.size(); i++)
    		t[i] = selected.get(i);
    	return t;
    }
    
    public void setDuration(int duration) {
    	this.duration = duration;
    }
    
    Pane getTimePane(int i) {
    	int row, col;
    	if (i < 24) {
    		row = 0;
    		col = i;
    	} else if (i < 48) {
    		row = 2;
    		col = i-24;
    	} else {
    		return null;
    	}

    	for (Node node : timeGrid.getChildren())
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row)
                return (Pane)node;

        return null;
    }
    
    private void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				p.setStyle(style[2]);
				
				for (int j : selected) {
					getTimePane(j).setStyle(style[0]);
				}

				selected.removeAll(selected);
				for (int j = i; j < Math.min(i + duration, 48); j++) {
					selected.add(j);
					getTimePane(j).setStyle(style[2]);
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[1]);
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String[] style = getAppropriateStyle(i);
				for (int j = i; j < Math.min(i + duration, 48); j++)
					if (!selected.contains(j))
						getTimePane(j).setStyle(style[0]);
			}
    	});
    }
    
    String[] getAppropriateStyle(int i) {
    	for (int a : available)
    		if (a == i)
    			return availableStyle;
    	return unavailableStyle;
    }

	public ArrayList<String> saveTimes(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deselectAll() {
		String[] style = getAppropriateStyle(0);
    	for(int i :selected) {
    		getTimePane(i).setStyle(style[1]);
    	}
    	selected.clear();
    	
		
	}

	public void setDefaultAvailability(String[][] availabilities, String currentDay) {
		// TODO Auto-generated method stub
		
	}

	public void setDefaultAvailabilityFromList(ArrayList<String> arrayList, String currentDay) {
		// TODO Auto-generated method stub
		
	}
    	
}
