package view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController 
{
	@FXML // fx:id="login_username"
    private TextField login_username; // Value injected by FXMLLoader

    @FXML // fx:id="login_password"
    private PasswordField login_password; // Value injected by FXMLLoader

    @FXML
    void loginClick(ActionEvent event) 
    {
    	System.out.println(login_username.getText());
    	System.out.println(login_password.getText());
    }

    @FXML
    void loginSignupClick(ActionEvent event) 
    {
    	System.out.println("clicked the signup button");
    	
    }
}
