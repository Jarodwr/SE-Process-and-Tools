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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.users.User;

public class LoginController 
{
	
	private Controller c; // named c to avoid confusion with JavaFX 2's controller classes, they are different things

	public void init(Controller c) {
		this.c = c;
	}
	@FXML // fx:id="login_username"
    private TextField login_username; // Value injected by FXMLLoader

    @FXML // fx:id="login_password"
    private PasswordField login_password; // Value injected by FXMLLoader

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private Label loginErrorMessage;
    
    private Stage main;
    
    @FXML
    void loginClick(ActionEvent event) 
    {
    	login();
    }

    void login()
    {
    	//to change the error message
    	loginErrorMessage.setStyle("-fx-text-fill: RED");
    	loginErrorMessage.setText("Incorrect Username/Password");
    	
    	try {
    		User u = this.c.login(login_username.getText(), login_password.getText());
	    	if (u == null) { // fail
	    		// display incorrect username or password screen
	    	} else if (u.isOwner()) {
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/owner/OwnerView.fxml"));
		    	BorderPane root = loader.load();
		    	Scene ownerview = new Scene(root, 900, 600);
				main.setScene(ownerview);
				OwnerViewController controller = loader.getController();
		    	controller.init(main, this.c, u);
		    	main.show();
	    	}
	    	else { 
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/customer/CustomerView.fxml"));
		    	BorderPane root = loader.load();
		    	Scene ownerview = new Scene(root, 900, 600);
				main.setScene(ownerview);
				CustomerViewController controller = loader.getController();
		    	controller.init(main, this.c, u);
		    	main.show();
	    	}
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void checkEnter(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		if(!loginButton.isDisabled())
    		{
    			login();
    		}
        }
    }
    
    @FXML
    void loginSignupClick(ActionEvent event) 
    {
    	try 
    	{
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/register/Register.fxml"));
	    	BorderPane root = loader.load();
	    	Scene ownerview = new Scene(root, 900, 600);
			main.setScene(ownerview);
			RegisterController controller = loader.getController();
			controller.initData(main, c);
	    	main.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void initStage(Stage scene)
    {
    	main = scene;
    }
}
