package gui.owner;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    
    protected String[] availableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ffffff","-fx-border-color: black;-fx-background-color: #dddddd", "-fx-border-color: black;-fx-background-color: #bbbbbb"};
    protected String[] unavailableStyle = new String[]{"-fx-border-color: black;-fx-background-color: #ff0000","-fx-border-color: black;-fx-background-color: #dd0000", "-fx-border-color: black;-fx-background-color: #bb0000"};
    
    
    ArrayList<Integer> selected = new ArrayList<Integer>();
    protected ArrayList<Integer> available = new ArrayList<Integer>();
    
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
    
    public void setAvailability(String[][] times, LocalDate date) {
    	long dayInSecs = 86400;
    	long periodInSecs = 1800;
    	
    	for (int i : available)	//Reset all available panes to unavailable panes
    		getTimePane(i).setStyle(unavailableStyle[0]);
    	available.removeAll(available);	//Wipe info from panes

    	if (times == null || times.length == 0 || date == null)
    		return;
    	
    	for (long startTime = date.toEpochDay() * dayInSecs; startTime < (date.toEpochDay()+1) * dayInSecs; startTime += periodInSecs) {
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
    
    public int[] getSelectedPeriods() {
    	int[] t = new int[selected.size()];
    	for (int i = 0; i < selected.size(); i++)
    		t[i] = selected.get(i);
    	return t;
    }
    
    public boolean validPeriod() {
    	for (int i : selected) {
    		for (int j : available) {
    			if (i == j) {
    				return false;
    			}
    		}
    	}
    	return true;
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
    
    protected void addPaneListener(Pane p, int i) {
    	p.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				p.setStyle(getAppropriateStyle(i)[2]);
				
				for (int j : selected) {
					getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
				}

				selected.removeAll(selected);
				for (int j = i; j < i + Math.min(duration, 48); j++) {
					if (j > 47 && j - 48 < 47) {
						int k = i - (j-47);
						selected.add(k);
						getTimePane(k).setStyle(getAppropriateStyle(k)[2]);
					} else {
						selected.add(j);
						getTimePane(j).setStyle(getAppropriateStyle(j)[2]);
					}
				}
			}
    	});
    	
    	p.onMouseEnteredProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				
				for (int j = i; j < i + Math.min(duration, 48); j++) {
					if (j > 47 && j - 48 < 47) {
						int k = i - (j-47);
						if (!selected.contains(k))
							getTimePane(k).setStyle(getAppropriateStyle(k)[1]);
					} else {
						if (!selected.contains(j))
							getTimePane(j).setStyle(getAppropriateStyle(j)[1]);
					}
				}
			}
    	});
    	
    	p.onMouseExitedProperty().set(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				for (int j = i; j < i + Math.min(duration, 48); j++) {
					if (j > 47 && j - 48 < 47) {
						int k = i - (j-47);
						if (!selected.contains(k))
							getTimePane(k).setStyle(getAppropriateStyle(k)[0]);
					} else {
						if (!selected.contains(j))
							getTimePane(j).setStyle(getAppropriateStyle(j)[0]);
					}
				}
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
    	for(int i :selected) {
    		String[] style = getAppropriateStyle(i);
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
