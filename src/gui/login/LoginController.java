package gui.login;

import java.io.IOException;

import gui.owner.OwnerViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController 
{
	@FXML // fx:id="login_username"
    private TextField login_username; // Value injected by FXMLLoader

    @FXML // fx:id="login_password"
    private PasswordField login_password; // Value injected by FXMLLoader

    @FXML
    private BorderPane mainPane;
    
    private String owner = "Test Pass String";
    
    private Stage main;
    
    @FXML
    void loginClick(ActionEvent event) 
    {
    	try {
	    	System.out.println(login_username.getText());
	    	System.out.println(login_password.getText());
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/owner/OwnerView.fxml"));
	    	BorderPane root = loader.load();
	    	Scene ownerview = new Scene(root, 900, 600);
			main.setScene(ownerview);
			OwnerViewController controller = loader.getController();
	    	controller.initOwner(owner);
	    	main.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    }

    @FXML
    void loginSignupClick(ActionEvent event) 
    {
    	System.out.println("clicked the signup button");
    	
    }
    
    public void initStage(Stage scene)
    {
    	main = scene;
    	System.out.println("This was executed");
    }
}
