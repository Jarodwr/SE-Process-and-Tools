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

import gui.owner.OwnerViewWorkingTimesController;

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
    
    //variables to get from dependency injection when the controller is loaded
    private Controller c;
    private Owner o;
    
    //variables to locally store the bookings
    String [][] summaryofBookings = null;
    String [][] newBookings = null;
    ArrayList<CheckBox> newBookingsDelete = null;
    
    /*
     * if the delete selected button is pressed then delete the selected bookings from the database and refresh tables
     */
    @FXML
    void deleteSelectedBookings(ActionEvent event) 
    {
    	//check if bookings exist in the first place
    	if(newBookingsDelete != null)
    	{
    		//go through each of the new bookings delete check box and check if they are marked for deletion
    		for(int i = 0; i < newBookingsDelete.size(); i++)
	    	{
    			//if it is marked for deletion
	    		if(newBookingsDelete.get(i).isSelected())
	    		{
	    			//get the details of the booking
	    			int id = Integer.parseInt(newBookings[i][0]);
	    			String businessName = o.getBusinessName();
	    			
	    			//delete the booking
	    			c.removeBooking(id, businessName);
	    		}
	    	}
    	}
    	//refresh the tables
    	initTables();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert futureBookings != null : "fx:id=\"futureBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert deleteItem != null : "fx:id=\"deleteItem\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert deleteBookings != null : "fx:id=\"deleteBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        assert allBookings != null : "fx:id=\"allBookings\" was not injected: check your FXML file 'OwnerViewBookingSummary.fxml'.";
        
    }
    
    //Dependency injection to get the current instance of the controller and the owner who is making changes
    void init(Controller c, Owner o)
    {
    	this.c = c;
    	this.o = o;
    	//initiate the tables upon initialization
    	initTables();
    }
    
    /*
     * this method fills the data into the 2 tables on the view bookings page
     */
    void initTables()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	//get all bookings details from database
    	String[][] bookings = c.getSummaryOfBookings();
    	if (bookings != null) {
    		//assign to private variable
        	summaryofBookings = bookings;
    	}
    	//get the list of current bookings from database
    	String[][] currentBookings = c.getSummaryOfBookings();
    	if (currentBookings != null) {
    		//assign to the private variable
    		long time = (new Date().getTime())/1000;
    		int j = 0;
    		ArrayList<String[]> strings = new ArrayList<String[]>();
    		
    		for(int i = 0; i < currentBookings.length; i++)
    		{
    			
    			if(Long.parseLong(currentBookings[i][1]) > time)
    			{
    				strings.add(currentBookings[i]);
    				j++;
    			}
    		}
    		if(j != 0)
    		{
    			newBookings = new String [strings.size()][6];
    			strings.toArray(newBookings);
    		}

    	}
    	
    	//create a new list of check boxes
    	newBookingsDelete = new ArrayList<CheckBox>();
    	
    	//if there are future bookings
    	if(!(newBookings == null))
    	{
    		futureBookings.setVgap(10);
    		//go through each item and print it into gridpane
    		for(int i = 0; i < newBookings.length; i++)
	    	{
    			Label id = new Label(newBookings[i][0]);
	    		Label customername = new Label(newBookings[i][3]);
	    		Label startTime = new Label(sdf.format(new Date((Long.parseLong(newBookings[i][1])*1000) + 14*60*60*1000)));
	    		Label endTime = new Label(sdf.format(new Date((Long.parseLong(newBookings[i][2])*1000) + 14*60*60*1000)));
	    		Label employeeId = new Label(newBookings[i][5]);
        		Label services = new Label(newBookings[i][4]);
	    		CheckBox delete = new CheckBox();
	    		newBookingsDelete.add(delete);
	    		
	    		futureBookings.add(id, 0 , i+1);
	    		futureBookings.add(customername, 1 , i+1);
	    		futureBookings.add(startTime, 2 , i+1);
	    		futureBookings.add(endTime, 3 , i+1);
	    		futureBookings.add(employeeId, 4 , i+1);
	    		futureBookings.add(services, 5 , i+1);
	    		futureBookings.add(delete, 6 , i+1);
	    		
	    	}
    		//remove error message
    		futureBookingsAlert.setStyle("-fx-text-fill: #f5f5f5");
    		deleteBookings.setDisable(false);
    	}
    	else
    	{
    		//if no bookings exist then disable the delete booking button and show the error message
    		futureBookingsAlert.setStyle("-fx-text-fill: BLACK");
    		deleteBookings.setDisable(true);
    	}
    	
    	allBookings.setVgap(10);
    	//if there are any bookings
    	if(summaryofBookings != null)
    	{
    		//go through each one and put it into the gridpane
    		for(int i = 0; i < summaryofBookings.length; i++)
        	{
        		Label id = new Label(summaryofBookings[i][0]);
        		Label customername = new Label(summaryofBookings[i][3]);
        		Label startTime = new Label(OwnerViewWorkingTimesController.getdateFromUnix(summaryofBookings[i][1]));
        		Label endTime = new Label(OwnerViewWorkingTimesController.getdateFromUnix(summaryofBookings[i][2]));
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
    		//if there are none then show the error message
    		allBookingsAlert.setStyle("-fx-text-fill: BLACK");
    	}
    	
    	
    }
}

