package SARJ.BookingSystem.gui;

import java.net.URL;
import java.util.ResourceBundle;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.model.exceptions.ValidationException;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RegisterBusinessController{

    //variables injected from Register.fxml
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordCon;
    
    @FXML
    private Label passStrength;

    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField number;

    @FXML
    private Label registerErrorMessage;

    @FXML
    private Button backButton;
    
    @FXML
    private TextField businessName;
    
    @FXML
    private TextField businessPhoneNumber;
    
    @FXML
    private TextField businessAddress;
    
    
    
    //variables to be injected from where it is created
    private Controller c;
    private String selectedBusiness;
    private Stage main;
    
    //when any field is changed check all validation
    @FXML
    void checkAll(KeyEvent event) {
    	checkAll();
    }
    
    //checks and updates the password strength field based on complexity of password
    void checkStrength()
    {
    	String pass = password.getText();
    	
    	//if password is crap then strength is week
    	if(pass.matches(".{0,}"))
    	{
    		passStrength.setText("Weak");
    		passStrength.setStyle("-fx-text-fill: RED");
    	}
    	//if password is just good enough then strength is good
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{8,}$"))
    	{
    		passStrength.setText("Good");
    		passStrength.setStyle("-fx-text-fill: #ff8000");
    	}
    	//if password is better than enough then strength is strong
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{10,}$"))
    	{
    		passStrength.setText("Strong");
    		passStrength.setStyle("-fx-text-fill: #009900");
    	}
    	//if password is extremely complex then strength is very strong
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{12,}$"))
    	{
    		passStrength.setText("Very Strong");
    		passStrength.setStyle("-fx-text-fill: #00cc00");
    	}
    }

    
    //checks the address against regex
    void checkAddress()
    {
    	//checks the address against regex
    	if(!address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+[,]?\\s[a-zA-Z])+")) {
    		//alert user that it is not valid
			address.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Owner address is not valid!");
    	} else {
    		//if it is valid mark as good
			address.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
    	}
    }
    
    void checkBusinessAddress()
    {
    	//checks the address against regex
    	if(!businessAddress.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+[,]?\\s[a-zA-Z])+")) {
    		//alert user that it is not valid
    		businessAddress.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Business address is not valid!");
    	} else {
    		//if it is valid mark as good
    		businessAddress.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
    	}
    }
    
    /*
     * this method check the password and shows errors on why it doesnt match
     */
    void checkPassword()
    {
    	String pass = password.getText();
    	//checks strength of password
    	checkStrength();
    	//check against regex
    	if(!pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{8,}$")) {
    		if(!pass.matches("^(?=.*[0-9]).{8,}$"))
			{
				//if it doesn't have a number in it, print error
    			registerErrorMessage.setText("Password must contain atleast 1 number!");
			}
			if(!pass.matches("^(?=.*[a-z]).{8,}$"))
			{
				//if it doesn't have a lower case letter, print error
				registerErrorMessage.setText("Password must contain atleast 1 lower case Letter!");
			}
			if(!pass.matches("^(?=.*[A-Z]).{8,}$"))
			{
				//if it doesn't have a upper case letter, print error
				registerErrorMessage.setText("Password must contain atleast 1 upper case Letter!");
			}
			if(!pass.matches("^(?=.*[!`~@#\\$%\\^&\\+=]).{8,}$"))
			{
				//if it doesn't have a special character, print error
				registerErrorMessage.setText("Password must contain atleast 1 special character!");
			}
			if(!pass.matches(".{8,}"))
			{
				//if password is not long enough, print error
				registerErrorMessage.setText("Password must be atleast 8 characters long!");
			}
    		password.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	
		} else {
			//if it passes show good box border colour
			password.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    //checks if the passwords matches each other
    void checkConfPassword()
    {
    	//check if the passwords match
    	if(!password.getText().equals(passwordCon.getText()) || passwordCon.getText().equals("")) {
    		//if it doesn't, print error
			passwordCon.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Passwords do not match!");
		} else {
			//if it passes show good box border colour
			passwordCon.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    //checks the name characters
    void checkName()
    {
    	//check if it only contains certain characters
    	if(!name.getText().matches("[A-Za-z -']+")) {
    		//if it doesn't, print error
			name.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Name must only contain only letters!");
		} else {
			//if it passes show good box border colour
			name.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkBusinessName()
    {
    	//check if it only contains certain characters
    	if(!businessName.getText().matches("[A-Za-z0-9 -']+")) {
    		//if it doesn't, print error
			businessName.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Business name must only contain only letters!");
		} else {
			//if it passes show good box border colour
			businessName.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkPhoneNumber()
    {
    	if(!number.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			number.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Phone number is not valid!");
		} else {
			//if it passes show good box border colour
			number.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkBusinessPhoneNumber()
    {
    	if(!businessPhoneNumber.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			businessPhoneNumber.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Business phone number is not valid!");
		} else {
			//if it passes show good box border colour
			businessPhoneNumber.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    //checks the user name to see if it exists
    void checkUsername()
    {
    	if(c.utilities.searchUserLogin(username.getText(), "") != null)
    	{
    		username.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Username is already taken!");
    	}
    	else
    	{
    		//if it passes show good box border colour
			username.setStyle("-fx-border-color: green");
			//check all fields and if true remove error message
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
    	}
    }
    //runs all checks
    void checkAll()
    {
    	checkBusinessPhoneNumber();
    	checkBusinessAddress();
    	checkBusinessName();
    	checkConfPassword();
    	checkPassword();
    	checkPhoneNumber();
    	checkAddress();
    	checkName();
    	checkUsername();
    }
    
    /*
     * if the back button is pressed, go to the login screen
     */
    @FXML
    void onBackButtonClick(ActionEvent event) 
    {
    	try 
    	{
			//load fxml file
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
    		//load it into a pane
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        controller.init(new Controller());
	        controller.initStage(main);
	        //put it onto the main stage and show
	        main.setScene(scene);
	        main.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    //if an enter button is pressed from any input buttons then try to register
    @FXML
    void checkEnter(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		//check if the register button is disabled from validation checks
    		if(!registerButton.isDisabled())
    		{
    			//if all good, try to create user
    			createBusinessAndOwner();
    		}
        }
    }

    //when the register button is clicked try to create a user
    @FXML
    void onRegisterClick(ActionEvent event) 
    {
    	//try to create a user
    	createBusinessAndOwner();
    }
    
    //try to create a user
    void createBusinessAndOwner()
    {
    	try {
    		//try to register a user, it will throw an exception if it is invalid
    		c.registerBusiness(businessName.getText(),  businessAddress.getText(), businessPhoneNumber.getText());
    		System.out.println("username is " + username.getText());
    		c.registerOwner(username.getText(), password.getText(),  selectedBusiness,  
        			name.getText(), address.getText(), number.getText(), passwordCon.getText());
    		//if it doesnt throw an exception then user was created and alert the user
			registerErrorMessage.setStyle("-fx-text-fill: GREEN");
			registerErrorMessage.setText("Business and Owner created!");
			
		} catch (ValidationException e) {
			//if an error occurs then alert the user about the error
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText(e.getMessage());
		}    	
    }

	//make sure that the fxml objects exist
    @FXML
    void initialize() {
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'Register.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Register.fxml'.";
        assert registerButton != null : "fx:id=\"registerButton\" was not injected: check your FXML file 'Register.fxml'.";
        assert passwordCon != null : "fx:id=\"passwordCon\" was not injected: check your FXML file 'Register.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'Register.fxml'.";
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'Register.fxml'.";
        assert number != null : "fx:id=\"number\" was not injected: check your FXML file 'Register.fxml'.";
        assert registerErrorMessage != null : "fx:id=\"registerErrorMessage\" was not injected: check your FXML file 'Register.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'Register.fxml'.";

    }
    
    //used for dependency injection for the main stage and the controller instance
    public void initData(Stage stage, Controller c)
    {
    	this.c = c;
    	main = stage;
    }
    
    //checks all fields against regex's
    private boolean checkFields() {
    	if(password.getText().equals(passwordCon.getText()) &&
    			name.getText().matches("[A-Za-z -']+") && 
    			number.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
    			address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+") &&
    			businessPhoneNumber.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
    			businessName.getText().matches("[A-Za-z0-9 -']+") &&
    			businessAddress.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+[,]?\\s[a-zA-Z])+")){
    		//if it passes enable the register button to be clicked
    		registerButton.setDisable(false);
    		return true;
    	}
    	else{
    		//if it fails then disable the register button
    		registerButton.setDisable(true);
    		return false;
    	}
    }
}
