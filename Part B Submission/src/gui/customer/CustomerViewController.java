package gui.customer;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import gui.login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.users.Customer;
import model.users.User;


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
    
    private Customer customer;
    
    private Controller c;
    
    private Stage main;

    @FXML
    void addBooking(ActionEvent event) {
    	selectButton(addBookingButton);
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerAddBooking.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			CustomerAddBooking controller = loader.getController();
		    controller.init(c, customer);
			
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void viewBookings(ActionEvent event) {
    	selectButton(viewBookingButton);
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerViewBookingSummery.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			CustomerViewBookingsController controller = loader.getController();
		    controller.init(c, customer);
			
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void logout(ActionEvent event) 
    {
    	try 
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/login/Login.fxml")); /* TODO replace with page that says "are you sure you want to log out?" */
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

    @FXML
    void initialize() {
        assert addBookingButton != null : "fx:id=\"addBookingButton\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert mainScreen != null : "fx:id=\"mainScreen\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert welcometxt != null : "fx:id=\"welcometxt\" was not injected: check your FXML file 'CustomerView.fxml'.";

    }
    
    public void init(Stage main, Controller c, User u) 
    {
    	this.main = main;
    	customer = (Customer) u;
    	this.c = c;
	}
    
    @FXML
    private void selectButton(Button selected) {
    	addBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	viewBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	
    	selected.setStyle("-fx-background-color: #ff5930");
    	
    }
}
