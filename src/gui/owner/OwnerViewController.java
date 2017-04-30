package gui.owner;

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
import model.users.Owner;
import model.users.User;

public class OwnerViewController {
	

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addEmployeeButton;

    @FXML
    private Button addWorkingTimesButton;

    @FXML
    private Button veiwBookingsButton;

    @FXML
    private Button viewWorkingTimesButton;

    @FXML
    private Button editEmployeeAvailabilityButton;

    @FXML
    private Button AddServiceButton;

    @FXML
    private Button addBookingButton;

    @FXML
    private Button logoutButton;
    
    @FXML
    private Label welcometxt;
    
    @FXML
    private BorderPane mainScreen;

	private Stage main;

	private Controller c;

	private User u;

    
    @FXML
    void addBooking(ActionEvent event) {
    	selectButton(addBookingButton);
    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddBooking.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerAddBooking addBookingController = loader.getController();
			addBookingController.init(c);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void addEmployee(ActionEvent event) 
    {
    	selectButton(addEmployeeButton);
    	
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddEmployee.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerAddEmployee addEmployeeController = loader.getController();
			addEmployeeController.init(c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @FXML
    void addService(ActionEvent event) 
    {
    	selectButton(AddServiceButton);
    	
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddService.fxml"));
			
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());

			OwnerAddService controller = loader.getController();
			controller.init(this.c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @FXML
    void addWorkingTimes(ActionEvent event) 
    {
    	selectButton(addWorkingTimesButton);
    	
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerChangeWorkingTimes.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerChangeWorkingTimes controller = loader.getController();
		    controller.init(c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @FXML
    void editEmployeeAvailability(ActionEvent event) 
    {
    	selectButton(editEmployeeAvailabilityButton);
    	
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddEmployeeAvailabilities.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerAddEmployeeAvailabilitiesController controller = loader.getController();
		    controller.init(c);
			
			
    	}
    	catch(Exception e)
    	{
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
    void viewBookings(ActionEvent event) 
    {
    	selectButton(veiwBookingsButton);
    	
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerViewBookingSummery.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerViewBookingsController controller = loader.getController();
			controller.init(c, (Owner) u);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @FXML
    void viewWorkingTimes(ActionEvent event) {
    	selectButton(viewWorkingTimesButton);
    	try
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerViewWorkingTimes.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerViewWorkingTimesController controller = loader.getController();
			
			String[] allEmployees = this.c.getEmployeeList();
			
			controller.initData(c, allEmployees);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }
    
    public void init(Stage main, Controller c, User u) {
    	this.main = main;
    	this.u = u;
    	this.c = c;
	}
    
    /*
     * check that all the button elements definitely exist on the page
     */
    @FXML
    void initialize() {
        assert addEmployeeButton != null : "fx:id=\"addEmployeeButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert addWorkingTimesButton != null : "fx:id=\"addWorkingTimesButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert veiwBookingsButton != null : "fx:id=\"veiwBookingsButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert viewWorkingTimesButton != null : "fx:id=\"viewWorkingTimesButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert editEmployeeAvailabilityButton != null : "fx:id=\"editEmployeeAvailabilityButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert AddServiceButton != null : "fx:id=\"AddServiceButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert addBookingButton != null : "fx:id=\"addBookingButton\" was not injected: check your FXML file 'OwnerView.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'OwnerView.fxml'.";

    }
    
    @FXML
    private void selectButton(Button selected)
    {
    	addEmployeeButton.setStyle("-fx-background-color: #e6e6e6");
    	addWorkingTimesButton.setStyle("-fx-background-color: #e6e6e6");
    	veiwBookingsButton.setStyle("-fx-background-color: #e6e6e6");
    	viewWorkingTimesButton.setStyle("-fx-background-color: #e6e6e6");
    	editEmployeeAvailabilityButton.setStyle("-fx-background-color: #e6e6e6");
    	AddServiceButton.setStyle("-fx-background-color: #e6e6e6");
    	addBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	
    	selected.setStyle("-fx-background-color: #ff5930");
    	
    }

	
}
