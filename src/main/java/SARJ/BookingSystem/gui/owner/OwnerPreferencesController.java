package SARJ.BookingSystem.gui.owner;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.gui.Accent;
import SARJ.BookingSystem.gui.OwnerViewController;
import SARJ.BookingSystem.model.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OwnerPreferencesController implements Accent{

    @FXML // fx:id="SubmitButton"
    private Button SubmitButton; // Value injected by FXMLLoader

    @FXML // fx:id="headerMessage"
    private TextField headerMessage; // Value injected by FXMLLoader

    @FXML // fx:id="accent"
    private ColorPicker accent; // Value injected by FXMLLoader

    @FXML // fx:id="errorMessage"
    private Label errorMessage; // Value injected by FXMLLoader

    private Controller c;
    private User u;
    private Stage main;
    private String colour = "";
    
    @FXML
    void submit(ActionEvent event) {
    	if(!headerMessage.getText().matches("[a-zA-Z!'-_*]{0,50}")) {
    		headerMessage.setStyle("-fx-border-color: red");
			errorMessage.setStyle("-fx-text-fill: RED");
        	errorMessage.setText("Message can not be longer than 50 characters!");
    	} else {
    		headerMessage.setStyle("-fx-border-color: green");
    		
    		Color color = accent.getValue();
        	colour = color.toString().replace("0x", "#");
        	c.utilities.editBusinessHeader(headerMessage.getText());
        	c.utilities.editBusinessColor(colour);
        	
        	try 
        	{
        		//open the login page
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerView.fxml")); 
    			BorderPane root = loader.load();
    	        Scene scene = new Scene(root, 900, 600);
    	        OwnerViewController controller = loader.getController();
    	        controller.init(main, c, u);
    	        main.setScene(scene);
    	        main.show();
    	        controller.preferences(event);
    	        
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
        	
    	}
    }

    public void init(Controller c, User u, Stage main, String color)
    {
    	c = this.c;
    	u = this.u;
    	main = this.main;
    	colour = color;
    	accent.setValue(Color.web(colour));
    	headerMessage.setText(c.utilities.getBusinessHeader());
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert SubmitButton != null : "fx:id=\"SubmitButton\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert headerMessage != null : "fx:id=\"headerMessage\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert accent != null : "fx:id=\"accent\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert errorMessage != null : "fx:id=\"errorMessage\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";

    }

	public void changeColour(String colour) {
		SubmitButton.setStyle("-fx-background-color: " + colour);
	}
}
