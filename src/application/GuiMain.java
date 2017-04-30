package application;
	
import controller.Controller;
import gui.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;


/*
 * This class is contains the main method for the program. runs first of startup
 */
public class GuiMain extends Application {
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * Runs when the gui starts up. Opens the login page on load
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//open the login page 
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/login/Login.fxml"));
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        //inject a new instance of the main logic controller
	        controller.init(new Controller());
	        controller.initStage(primaryStage);
	        primaryStage.setScene(scene);
	        //show to user
	        primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * main method of the program, launches the GUI
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
