package gui.owner;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.users.Owner;

public class OwnerViewBookingsController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="futureBookings"
    private GridPane futureBookings; // Value injected by FXMLLoader

    @FXML // fx:id="deleteItem"
    private CheckBox deleteItem; // Value injected by FXMLLoader

    @FXML // fx:id="deleteBookings"
    private Button deleteBookings; // Value injected by FXMLLoader

    @FXML // fx:id="allBookings"
    private GridPane allBookings; // Value injected by FXMLLoader
    
    @FXML // fx:id="futureBookingsAlert"
    private Label futureBookingsAlert; // Value injected by FXMLLoader
    
    @FXML // fx:id="allBookingsAlert"
    private Label allBookingsAlert; // Value injected by FXMLLoader

    private Controller c;
    private Owner o;
    
    String [][] summaryofBookings = null;
    String [][] newBookings = null;
    ArrayList<CheckBox> newBookingsDelete = null;
    
    @FXML
    void deleteSelectedBookings(ActionEvent event) 
    {
    	if(newBookingsDelete != null)
    	{
    		for(int i = 0; i < newBookingsDelete.size(); i++)
	    	{
	    		if(newBookingsDelete.get(i).isSelected())
	    		{
	    			int id = Integer.parseInt(newBookings[i][0]);
	    			String businessName = o.getBusinessName();
	    			
	    			c.removeBooking(id, businessName);
	    		}
	    	}
    	}
    	
    	initTables();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert futureBookings != null : "fx:id=\"futureBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert deleteItem != null : "fx:id=\"deleteItem\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert deleteBookings != null : "fx:id=\"deleteBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert allBookings != null : "fx:id=\"allBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        
    }
    
    void init(Controller c, Owner o)
    {
    	this.c = c;
    	this.o = o;
    	initTables();
    }
    
    void initTables()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	String[][] bookings = c.getSummaryOfBookings();
    	if (bookings != null) {
        	summaryofBookings = bookings;
    	}
    	String[][] currentBookings = c.getCurrentBookings();
    	if (currentBookings != null) {
    		newBookings = currentBookings;
    	}
    	newBookingsDelete = new ArrayList<CheckBox>();
    	if(!(newBookings == null))
    	{
    		futureBookings.setVgap(10);
    		for(int i = 0; i < newBookings.length; i++)
	    	{
    			Label id = new Label(newBookings[i][0]);
	    		Label customername = new Label(newBookings[i][3]);
	    		Label startTime = new Label(sdf.format(new Date(Integer.parseInt(newBookings[i][1])*1000)));
	    		Label endTime = new Label(sdf.format(new Date(Integer.parseInt(newBookings[i][2])*1000)));
	    		CheckBox delete = new CheckBox();
	    		newBookingsDelete.add(delete);
	    		
	    		futureBookings.add(id, 0 , i+1);
	    		futureBookings.add(customername, 1 , i+1);
	    		futureBookings.add(startTime, 2 , i+1);
	    		futureBookings.add(endTime, 3 , i+1);
	    		futureBookings.add(delete, 4 , i+1);
	    		
	    	}
    		futureBookingsAlert.setStyle("-fx-text-fill: #f5f5f5");
    		deleteBookings.setDisable(false);
    	}
    	else
    	{
    		futureBookingsAlert.setStyle("-fx-text-fill: BLACK");
    		deleteBookings.setDisable(true);
    	}
    	
    	allBookings.setVgap(10);
    	if(allBookings != null)
    	{
    		for(int i = 0; i < summaryofBookings.length; i++)
        	{
        		Label id = new Label(summaryofBookings[i][0]);
        		Label customername = new Label(summaryofBookings[i][3]);
        		Label startTime = new Label(sdf.format(new Date(Long.parseLong(summaryofBookings[i][1])*1000)));
        		Label endTime = new Label(sdf.format(new Date(Long.parseLong(summaryofBookings[i][1])*1000)));
        		Label employeeId = new Label(summaryofBookings[i][5]);
        		Label services = new Label(summaryofBookings[i][4]);
        		
        		allBookings.add(id, 0 , i+1);
        		allBookings.add(customername, 1 , i+1);
        		allBookings.add(startTime, 2 , i+1);
        		allBookings.add(endTime, 3 , i+1);
        		allBookings.add(employeeId, 4 , i+1);
        		allBookings.add(services, 5 , i+1);
        	}
    	}
    	else
    	{
    		allBookingsAlert.setStyle("-fx-text-fill: BLACK");
    	}
    	
    	
    }
}

