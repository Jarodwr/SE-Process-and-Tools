package gui.register;

import java.net.URL;
import java.util.ResourceBundle;
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
import model.exceptions.ValidationException;
import controller.Controller;
import gui.login.LoginController;

public class RegisterController{

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
    
    private Controller c;
    
    private Stage main;
    

    @FXML
    void checkAddress(KeyEvent event) {
    	checkAll();
    }

    @FXML
    void checkConfPassword(KeyEvent event) {
    	checkAll();
    }

    @FXML
    void checkName(KeyEvent event) {
    	checkAll();
    }

    @FXML
    void checkPassword(KeyEvent event) {
    	checkAll();
    }
    
    void checkStrength()
    {
    	String pass = password.getText();
    	
    	if(pass.matches(".{0,}"))
    	{
    		passStrength.setText("Weak");
    		passStrength.setStyle("-fx-text-fill: RED");
    	}
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{8,}$"))
    	{
    		passStrength.setText("Good");
    		passStrength.setStyle("-fx-text-fill: #ff8000");
    	}
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{10,}$"))
    	{
    		passStrength.setText("Strong");
    		passStrength.setStyle("-fx-text-fill: #009900");
    	}
    	if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{12,}$"))
    	{
    		passStrength.setText("Very Strong");
    		passStrength.setStyle("-fx-text-fill: #00cc00");
    	}
    }

    @FXML
    void checkPhoneNumber(KeyEvent event) {
    	checkAll();
    }
    
    void checkAddress()
    {
    	if(!address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+[,]?\\s[a-zA-Z])+")) {
			address.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Enter a valid Address!");
    	} else {
			address.setStyle("-fx-border-color: green");
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
    	}
    }
    
    void checkPassword()
    {
    	String pass = password.getText();
    	checkStrength();
    	if(!pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!`~@#\\$%\\^&\\+=])(?=\\S+$).{8,}$")) {
    		if(!pass.matches("^(?=.*[0-9]).{8,}$"))
			{
				registerErrorMessage.setText("Password must contain atleast 1 number!");
			}
			if(!pass.matches("^(?=.*[a-z]).{8,}$"))
			{
				registerErrorMessage.setText("Password must contain atleast 1 lower case Letter!");
			}
			if(!pass.matches("^(?=.*[A-Z]).{8,}$"))
			{
				registerErrorMessage.setText("Password must contain atleast 1 upper case Letter!");
			}
			if(!pass.matches("^(?=.*[!`~@#\\$%\\^&\\+=]).{8,}$"))
			{
				registerErrorMessage.setText("Password must contain atleast 1 special charater!");
			}
			if(!pass.matches(".{8,}"))
			{
				registerErrorMessage.setText("Password must be atleast 8 characters long!");
			}
    		password.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	
		} else {
			password.setStyle("-fx-border-color: green");
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkConfPassword()
    {
    	if(!password.getText().equals(passwordCon.getText())) {
			passwordCon.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Passwords Do not Match!");
		} else {
			passwordCon.setStyle("-fx-border-color: green");
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkName()
    {
    	if(!name.getText().matches("[A-Za-z -']+")) {
			name.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Name must only contain only letteres!");
		} else {
			name.setStyle("-fx-border-color: green");
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkPhoneNumber()
    {
    	if(!number.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}")) {
			number.setStyle("-fx-border-color: red");
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText("Enter a valid Phone Number!");
		} else {
			number.setStyle("-fx-border-color: green");
			if(checkFields())
				registerErrorMessage.setStyle("-fx-text-fill: #dddddd");
		}
    }
    
    void checkAll()
    {
    	checkPhoneNumber();
    	checkAddress();
    	checkName();
    	checkConfPassword();
    	checkPassword();
    }
    
    @FXML
    void onBackButtonClick(ActionEvent event) 
    {
    	try 
    	{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/login/Login.fxml"));
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        controller.init(new Controller());
	        controller.initStage(main);
	        main.setScene(scene);
	        main.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void checkEnter(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		if(!registerButton.isDisabled())
    		{
    			createUser();
    		}
        }
    }

    @FXML
    void onRegisterClick(ActionEvent event) 
    {
    	createUser();
    }
    
    
    void createUser()
    {
    	try {
    		c.register(username.getText(), password.getText(), passwordCon.getText(), 
        			name.getText(), address.getText(), number.getText());
			registerErrorMessage.setStyle("-fx-text-fill: GREEN");
			registerErrorMessage.setText("User created!!");
			
		} catch (ValidationException e) {
			registerErrorMessage.setStyle("-fx-text-fill: RED");
	    	registerErrorMessage.setText(e.getMessage());
		}    	
    }

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
    
    public void initData(Stage stage, Controller c)
    {
    	this.c = c;
    	main = stage;
    }
    
    private boolean checkFields() {
    	if(password.getText().equals(passwordCon.getText()) &&
    			name.getText().matches("[A-Za-z -']+") && 
    			number.getText().matches("\\d{4}[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{3}") &&
    			address.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z])+")){
    		registerButton.setDisable(false);
    		return true;
    	}
    	else{
    		registerButton.setDisable(true);
    		return false;
    	}
    }
}
