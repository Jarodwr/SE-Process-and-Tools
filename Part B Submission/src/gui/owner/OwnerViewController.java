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
/*
 * This class is the main controller for the owner view
 * it will change the view when the owner selects the respective menu options
 * and log out if that option is selected
 */
public class OwnerViewController {
	
	//injected variables by OwnerView.fxml file
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
    
    //objects to initialize by dependency injection
	private Stage main;

	private Controller c;

	private User u;

    /*
     * opens the add booking page when the button is clicked
     */
    @FXML
    void addBooking(ActionEvent event) {
    	//change the selected button
    	selectButton(addBookingButton);
    	
    	try {
    		//open the page in the mainScreen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddBooking.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerAddBooking addBookingController = loader.getController();
			//inject variables
			addBookingController.init(c);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /*
     * opens the add employee page when the button is clicked
     */
    @FXML
    void addEmployee(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(addEmployeeButton);
    	
    	try
    	{
			//open page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddEmployee.fxml"));
			mainScreen.getChildren().clear();
			//add to main screen
			mainScreen.getChildren().add(loader.load());
			OwnerAddEmployee addEmployeeController = loader.getController();
			//inject variables
			addEmployeeController.init(c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /*
     * opens the add service page when the button is clicked
     */
    @FXML
    void addService(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(AddServiceButton);
    	
    	try
    	{
			//open page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddService.fxml"));
			
			mainScreen.getChildren().clear();
			//add to main screen
			mainScreen.getChildren().add(loader.load());
			OwnerAddService controller = loader.getController();
			//inject variables
			controller.init(this.c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /*
     * opens the add working times page when the button is clicked
     */
    @FXML
    void addWorkingTimes(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(addWorkingTimesButton);
    	
    	try
    	{
			//open the page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerChangeWorkingTimes.fxml"));
			mainScreen.getChildren().clear();
			//add to main screen
			mainScreen.getChildren().add(loader.load());
			OwnerChangeWorkingTimes controller = loader.getController();
			//inject variables
		    controller.init(c);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /*
     * opens the edit employee availability page when the button is clicked
     */
    @FXML
    void editEmployeeAvailability(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(editEmployeeAvailabilityButton);
    	
    	try
    	{
			//open the page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerAddEmployeeAvailabilities.fxml"));
			mainScreen.getChildren().clear();
			//add to the main screen
			mainScreen.getChildren().add(loader.load());
			OwnerAddEmployeeAvailabilitiesController controller = loader.getController();
			//inject variables
		    controller.init(c);
			
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /*
     * opens the login page when the button is clicked
     */
    @FXML
    void logout(ActionEvent event) 
    {
    	try 
    	{
    		//open the login page
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/login/Login.fxml")); /* TODO replace with page that says "are you sure you want to log out?" */
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        controller.init(c);
	        //inject the main stage
	        controller.initStage(main);
	        main.setScene(scene);
	        main.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    /*
     * opens the view bookings page when the button is clicked
     */
    @FXML
    void viewBookings(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(veiwBookingsButton);
    	
    	try
    	{
			//open page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerViewBookingSummary.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerViewBookingsController controller = loader.getController();
			//inject variables
			controller.init(c, (Owner) u);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /*
     * opens the view working times page when the button is clicked
     */
    @FXML
    void viewWorkingTimes(ActionEvent event) 
    {
    	//change the selected button
    	selectButton(viewWorkingTimesButton);
    	try
    	{
			//open the page
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerViewWorkingTimes.fxml"));
			mainScreen.getChildren().clear();
			mainScreen.getChildren().add(loader.load());
			OwnerViewWorkingTimesController controller = loader.getController();
			//get the list of employees
			String[] allEmployees = this.c.getEmployeeList();
			//inject data to controller for the page
			controller.initData(c, allEmployees);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }
    
    /*
     * injects the main, controller and the current user into the page
     */
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
    
    /*
     * this method changes the button colour to indicate which option is selected
     */
    @FXML
    private void selectButton(Button selected)
    {
    	//reset all to default colour 
    	addEmployeeButton.setStyle("-fx-background-color: #e6e6e6");
    	addWorkingTimesButton.setStyle("-fx-background-color: #e6e6e6");
    	veiwBookingsButton.setStyle("-fx-background-color: #e6e6e6");
    	viewWorkingTimesButton.setStyle("-fx-background-color: #e6e6e6");
    	editEmployeeAvailabilityButton.setStyle("-fx-background-color: #e6e6e6");
    	AddServiceButton.setStyle("-fx-background-color: #e6e6e6");
    	addBookingButton.setStyle("-fx-background-color: #e6e6e6");
    	
    	//change the selected button to the selected colour
    	selected.setStyle("-fx-background-color: #ff5930");
    	
    }

	
}
