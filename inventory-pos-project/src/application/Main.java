package application;
	
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static volatile Stage INSTANCE;
	private final static Logger logger = LogManager.getLogger(Main.class);
	@Override
	public void start(Stage primaryStage) {
		logger.info("Application initializing...");
		try {
			INSTANCE = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("/application/views/MainView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
			logger.info("Application has started...");
		} catch(Exception e) {
			logger.error("Error starting application.", e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
