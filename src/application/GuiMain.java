package application;
	
import controller.Controller;
import gui.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;



public class GuiMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/login/Login.fxml"));
			BorderPane root = loader.load();
	        Scene scene = new Scene(root, 900, 600);
	        LoginController controller = loader.getController();
	        controller.init(new Controller());
	        controller.initStage(primaryStage);
	        primaryStage.setScene(scene);
	        primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
