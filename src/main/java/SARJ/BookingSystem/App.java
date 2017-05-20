package SARJ.BookingSystem;
	
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import SARJ.BookingSystem.controller.Controller;
import SARJ.BookingSystem.gui.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;


/*
 * This class is contains the main method for the program. runs first of startup
 */
public class App extends Application {
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * Runs when the gui starts up. Opens the login page on load
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//open the login page
			System.out.println(getClass().getResource("").getPath() + "fxml/login/Login.fxml");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/login/Login.fxml"));
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        //inject a new instance of the main logic controller
	        Controller c = new Controller();
	        controller.init(c);
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
		Logger LOGGER = Logger.getLogger("main");
		Handler handler;
		try {
			File file = new File("/logs/");
			if (!file.exists()) {
				file.mkdir();
			}
			handler = new FileHandler("/logs/" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime()) + ".txt");
			LOGGER.setLevel(Level.FINEST);
			handler.setLevel(Level.FINEST);
			
		} catch(IOException e) {
			handler = new ConsoleHandler();
			System.out.println(e);
			LOGGER.setLevel(Level.WARNING);
			handler.setLevel(Level.WARNING);
			LOGGER.warning("Cannot create logging file, using console logger");
		}
		handler.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(handler);
		LOGGER.setUseParentHandlers(false);

		launch(args);
	}
}
