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
    void onRegisterClick(ActionEvent event) 
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
}
