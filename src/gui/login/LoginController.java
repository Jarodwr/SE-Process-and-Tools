package gui.login;

import java.io.IOException;

import controller.Controller;
import gui.customer.CustomerViewController;
import gui.owner.OwnerViewController;
import gui.register.RegisterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.users.User;
import model.utility.Utility;
/**
 * 
 * This class is the main functionality of the program, it is the first point of contact when the user interacts with the login page
 * It logs a user in and redirects a user to the register page if they would like to create an account
 *
 */
public class LoginController 
{
	
	private Controller c; // named c to avoid confusion with JavaFX 2's controller classes, they are different things

	//dependency injection to inject an instance of the controller object into the controller class
	public void init(Controller c) {
		this.c = c;
	}
	
	//injected variables from the Login.fxml file by their fx:id
	@FXML // fx:id="login_username"
    private TextField login_username; // Value injected by FXMLLoader

    @FXML // fx:id="login_password"
    private PasswordField login_password; // Value injected by FXMLLoader

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;
    
    @FXML
    private Label newBussinessButton;
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private Label loginErrorMessage;
    
    private Stage main;
    
    //this method runs when the login button is clicked
    @FXML
    void loginClick(ActionEvent event) 
    {
    	//run the login function
    	login();
    }
    
    /*
     * this function tries to login a user, if it fails it will display an error message to the user
     */
    void login()
    {
    	//to change the error message when run, if it logs in then the user won't see this as the page will change
    	loginErrorMessage.setStyle("-fx-text-fill: RED");
    	loginErrorMessage.setText("Incorrect Username/Password");
    	
    	try {
    		//try to log in the user and assign the output to a new user object
    		User u = this.c.login(login_username.getText(), login_password.getText());
    		//if the user doesn't exist then end method and display the error message
	    	if (u == null) { // fail
	    		// display incorrect username or password screen
	    	} else if (u.isAdmin()) {
 	    		//if the user that us returned then go to the owner view
 	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/owner/OwnerView.fxml"));
 		    	BorderPane root = loader.load();
 		    	Scene ownerview = new Scene(root, 900, 600);
 				main.setScene(ownerview);
 				OwnerViewController controller = loader.getController();
 				//inject variables into the controller class for the owner view
 		    	controller.init(main, this.c, u);
 		    	//show the new stage to the user
 		    	main.show();
	    	} else if (u.isOwner()) {
	    		//if the user that us returned then go to the owner view
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/owner/OwnerView.fxml"));
		    	BorderPane root = loader.load();
		    	Scene ownerview = new Scene(root, 900, 600);
				main.setScene(ownerview);
				OwnerViewController controller = loader.getController();
				//inject variables into the controller class for the owner view
		    	controller.init(main, this.c, u);
		    	//show the new stage to the user
		    	main.show();
	    	}
	    	else { 
	    		//if the user logging in is not a owner then show them the customer view
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/customer/CustomerView.fxml"));
		    	BorderPane root = loader.load();
		    	Scene ownerview = new Scene(root, 900, 600);
				main.setScene(ownerview);
				CustomerViewController controller = loader.getController();
				//inject variables into the controller class for the customer view
		    	controller.init(main, this.c, u);
		    	main.show();
	    	}
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
     * This method is for checking if the user hits the enter button from either of the input text boxes
     * and runs the login method if it is pressed
     */
    @FXML
    void checkEnter(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		if(!loginButton.isDisabled())
    		{
    			login();
    		}
        }
    }
    
    /*
     * This method is for checking if the user clicks the register button
     */
    @FXML
    void loginSignupClick(ActionEvent event) 
    {
    	try 
    	{
	    	//if the button is clicked then open the register paged
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/register/Register.fxml"));
	    	BorderPane root = loader.load();
	    	Scene ownerview = new Scene(root, 900, 600);
			main.setScene(ownerview);
			RegisterController controller = loader.getController();
			//inject variables into the controller for the register page
			controller.initData(main, c);
			//show to the user
	    	main.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //inject the main stage into this controller
    public void initStage(Stage scene)
    {
    	main = scene;
    }
    
    @FXML
    void registerNewBussiness(MouseEvent event) {

    }
}
