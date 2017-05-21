package SARJ.BookingSystem.gui;

import java.net.URL;
import java.util.ResourceBundle;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.gui.customer.CustomerAddBooking;
import SARJ.BookingSystem.gui.customer.CustomerViewBookingsController;
import SARJ.BookingSystem.model.users.Customer;
import SARJ.BookingSystem.model.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class CustomerViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBookingButton;
    
    @FXML
    private Button viewBookingButton;

    @FXML
    private Button logoutButton;

    @FXML
    private BorderPane mainScreen;

    @FXML
    private Label welcometxt;
    
    @FXML
    private Pane header;
    
    @FXML
    private Label headerMessage;
    
    private String color = "";
    
    private Customer customer;
    
    private Controller c;
    
    private Stage main;
    
    /**
	 * Add booking button pressed
	 * @param event	the event information of the button press
	 */

    @FXML
    void addBooking(ActionEvent event) {
    	selectButton(addBookingButton);
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("customer/AddBooking.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add((Node) loader.load());
			CustomerAddBooking controller = loader.getController();
		    controller.init(c, customer);
		    controller.changeColour(color);
			
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    /**
	 * View booking button pressed
	 * @param event	the event information of the button press
	 */

    @FXML
    void viewBookings(ActionEvent event) {
    	selectButton(viewBookingButton);
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("customer/ViewBooking.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add((Node) loader.load());
			CustomerViewBookingsController controller = loader.getController();
		    controller.init(c, customer);
		    controller.changeColour(color);
			
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    /**
	 * Log the user out on logout button press
	 * @param event	the event information of the button press
	 */
    
    @FXML
    void logout(ActionEvent event) 
    {
    	try 
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        controller.init(c);
	        controller.initStage(main);
	        main.setScene(scene);
	        main.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Initialize fxml object rules for the GUI
	 */

    @FXML
    void initialize() {
        assert addBookingButton != null : "fx:id=\"addBookingButton\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert mainScreen != null : "fx:id=\"mainScreen\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert welcometxt != null : "fx:id=\"welcometxt\" was not injected: check your FXML file 'CustomerView.fxml'.";

    }
    
    /**
	 * Initialize and setup the variables for the view
	 * @param main	the GUI fxaml main stage
	 * @param c	cthe controller object
	 * @param u the current user object
	 */
    
    public void init(Stage main, Controller c, User u) 
    {
    	this.main = main;
    	customer = (Customer) u;
    	this.c = c;
    	
    	color = c.utilities.getBusinessColor();
    	
    	headerMessage.setText(c.utilities.getBusinessHeader()); 
    	header.setStyle("-fx-background-color : " + color);
    	
	}
    
    /**
	 * Change colour of the selected option for visual feedback on selection
	 * @param button	button/option selected
	 */
    
    @FXML
    private void selectButton(Button selected) {
    	addBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	viewBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	
    	selected.setStyle("-fx-background-color: " + color);
    	
    }
}
