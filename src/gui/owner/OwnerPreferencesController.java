package gui.owner;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class OwnerPreferencesController {

    @FXML // fx:id="SubmitButton"
    private Button SubmitButton; // Value injected by FXMLLoader

    @FXML // fx:id="headerMessage"
    private TextField headerMessage; // Value injected by FXMLLoader

    @FXML // fx:id="accent"
    private ColorPicker accent; // Value injected by FXMLLoader

    @FXML // fx:id="errorMessage"
    private Label errorMessage; // Value injected by FXMLLoader

    private Controller c;
    private String colour = "";
    
    @FXML
    void submit(ActionEvent event) {
    	System.out.println("Colour: " + accent.getValue());
    	Color color = accent.getValue();
    	SubmitButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#"));
    }

    public void init(Controller c, String color)
    {
    	c = this.c;
    	colour = color;
    	SubmitButton.setStyle("-fx-background-color: " + colour);
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert SubmitButton != null : "fx:id=\"SubmitButton\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert headerMessage != null : "fx:id=\"headerMessage\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert accent != null : "fx:id=\"accent\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";
        assert errorMessage != null : "fx:id=\"errorMessage\" was not injected: check your FXML file 'OwnerPreferences.fxml'.";

    }
}
