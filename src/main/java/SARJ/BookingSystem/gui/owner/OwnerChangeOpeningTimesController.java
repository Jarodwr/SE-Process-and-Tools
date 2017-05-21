package SARJ.BookingSystem.gui.owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class OwnerChangeOpeningTimesController {

    @FXML // fx:id="warning"
    private Label warning; // Value injected by FXMLLoader

    @FXML // fx:id="SubmitButton"
    private Button SubmitButton; // Value injected by FXMLLoader

    @FXML // fx:id="pickDay"
    private ComboBox<?> pickDay; // Value injected by FXMLLoader

    @FXML // fx:id="TimePickerPane"
    private Pane TimePickerPane; // Value injected by FXMLLoader

    @FXML // fx:id="errorMessage"
    private Label errorMessage; // Value injected by FXMLLoader

    @FXML
    void submit(ActionEvent event) {

    }

    @FXML
    void updateCurrentAvailability(ActionEvent event) {

    }

}
