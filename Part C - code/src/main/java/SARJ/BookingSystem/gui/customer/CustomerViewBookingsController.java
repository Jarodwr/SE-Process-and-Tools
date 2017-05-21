package SARJ.BookingSystem.gui.customer;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.gui.Accent;
import SARJ.BookingSystem.model.users.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CustomerViewBookingsController implements Accent{

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
    private Customer o;
    
    String [][] summeryofBookings = null;
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
	    			
	    			c.removeBooking(id);
	    		}
	    	}
    	}
    	
    	initTables();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert futureBookings != null : "fx:id=\"futureBookings\" was not injected: check your FXML file 'OwnerViewBookingSummery.fxml'.";
        assert deleteItem != null : "fx:id=\"deleteItem\" was not injected: check your FXML file 'OwnerViewBookingSummery.fxml'.";
        assert deleteBookings != null : "fx:id=\"deleteBookings\" was not injected: check your FXML file 'OwnerViewBookingSummery.fxml'.";
        assert allBookings != null : "fx:id=\"allBookings\" was not injected: check your FXML file 'OwnerViewBookingSummery.fxml'.";
        
    }
    
    public void init(Controller c, Customer o)
    {
    	this.c = c;
    	this.o = o;
    	initTables();
    }
    
    void initTables()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	summeryofBookings = c.getSummaryOfBookings();
    	newBookings = c.getCurrentBookings();
    	newBookingsDelete = new ArrayList<CheckBox>();
    	if(!(newBookings == null))
    	{
    		futureBookings.setVgap(10);
    		int j = 0;
    		for(int i = 0; i < newBookings.length; i++)
	    	{
    			if(o.getUsername().equals(newBookings[i][3]))
    			{
    				Label id = new Label(newBookings[i][0]);
    	    		Label customername = new Label(newBookings[i][3]);
    	    		Label startTime = new Label(sdf.format(new Date(Integer.parseInt(newBookings[i][1])*1000)));
    	    		Label endTime = new Label(sdf.format(new Date(Integer.parseInt(newBookings[i][2])*1000)));
    	    		CheckBox delete = new CheckBox();
    	    		newBookingsDelete.add(delete);
    	    		
    	    		futureBookings.add(id, 0 , j+1);
    	    		futureBookings.add(customername, 1 , j+1);
    	    		futureBookings.add(startTime, 2 , j+1);
    	    		futureBookings.add(endTime, 3 , j+1);
    	    		futureBookings.add(delete, 4 , j+1);
    	    		j++;
    			}
    			
	    	}
    		futureBookingsAlert.setStyle("-fx-text-fill: #f5f5f5");
    		deleteBookings.setDisable(false);
    		if(j == 0)
    		{
    			futureBookingsAlert.setStyle("-fx-text-fill: BLACK");
        		deleteBookings.setDisable(true);
    		}
    	}
    	else
    	{
    		futureBookingsAlert.setStyle("-fx-text-fill: BLACK");
    		deleteBookings.setDisable(true);
    	}
    	
    	allBookings.setVgap(10);
    	if(allBookings != null && summeryofBookings != null)
    	{
    		int j = 0;
    		for(int i = 0; i < summeryofBookings.length; i++)
        	{
    			if(o.getUsername().equals(summeryofBookings[i][3]))
    			{
    				
    				
    				Label id = new Label(summeryofBookings[i][0]);
            		Label customername = new Label(summeryofBookings[i][3]);
            		Label startTime = new Label(sdf.format(new Date(Long.parseLong(summeryofBookings[i][1])*1000)));
            		Label endTime = new Label(sdf.format(new Date(Long.parseLong(summeryofBookings[i][1])*1000)));
            		Label employeeId = new Label(summeryofBookings[i][5]);
            		Label services = new Label(summeryofBookings[i][4]);
            		
            		allBookings.add(id, 0 , j+1);
            		allBookings.add(customername, 1 , j+1);
            		allBookings.add(startTime, 2 , j+1);
            		allBookings.add(endTime, 3 , j+1);
            		allBookings.add(employeeId, 4 , j+1);
            		allBookings.add(services, 5 , j+1);
            		j++;
    			}
        	}
    		if(j == 0)
    		{
    			allBookingsAlert.setStyle("-fx-text-fill: BLACK");
    		}
    	}
    	else
    	{
    		allBookingsAlert.setStyle("-fx-text-fill: BLACK");
    	}
    	
    	
    }
    
    public void changeColour(String colour) {
		deleteBookings.setStyle("-fx-background-color: " + colour);
		
	}
}

