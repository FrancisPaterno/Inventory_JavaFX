package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig.RootLogger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;

import application.controller.datamodel.manager.SessionManager;
import application.logging.manager.ConnectionFactory;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainViewController implements Initializable {

	@FXML
	private AnchorPane mainStage;
	@FXML
	private Button masterList;
	@FXML
	private Button mwnhome;
	@FXML
	private VBox vboxmasterlist;
	@FXML 
	private Button itemcategory;
	@FXML
	private Button itembrand;
	@FXML
	private AnchorPane contentpane;
	@FXML 
	private HBox statusBarPane;

	private FXMLLoader fxmlLogin;
	private FXMLLoader fxmlItemCategory;
	private FXMLLoader fxmlItemBrand;
	private FXMLLoader fxmlSupplier;
	private FXMLLoader fxmlItem;
	private FXMLLoader fxmlHome;
	private FXMLLoader fxmlUserRole;
	private FXMLLoader fxmlUser;
	private AnchorPane loginPane;
	private AnchorPane itemcategorypane;
	private AnchorPane itembrandpane;
	private AnchorPane itemsupplierpane;
	private AnchorPane itempane;
	private FlowPane mwnHomePage;
	private AnchorPane userRolepane;
	private AnchorPane userpane;
	private TranslateTransition hideMasterListFast;
	private TranslateTransition showMasterList;
	private TranslateTransition hideMasterList;

	public static String ITEM_CATEGORY = "ITEM CATEGORY";
	public static String ITEM_BRAND = "ITEM BRAND";
	public static String ITEM_SUPPLIER = "ITEM SUPPLIER";
	public static String ITEM = "ITEM";

	private final static Logger logger = LogManager.getLogger(MainViewController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			logger.info("Connecting to database logger.");
			Connection c =ConnectionFactory.getDatabaseConnection();
			if(c == null) {
				logger.info("Cannot contact databse logger.");
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm Continue");
				alert.setContentText("Error contacting database logger, only file logger will be used for logging.\nDo you want to resume anyway?");
				alert.initOwner(null);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent() && result.get() == ButtonType.CANCEL) {
					logger.info("User chose not to continue, system will now exit.");
					System.exit(0);
				}else {
					final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
					final Configuration config = ctx.getConfiguration();
					config.getRootLogger().removeAppender("MWNDatabase");
					ctx.updateLoggers();
					logger.info("Database logger turned off.");
				}
			}
			else {
				logger.info("Database logger contacted.");
				logger.info("Connected to database logger.");
			}

			logger.debug("Database Connection :" + c);
		} catch (Exception e1) {
			logger.error("Error connecting to database logger.", e1);
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Continue");
			alert.setContentText("Database logger cannot be used for logging, only file logger will be used.\nDo you want to resume anyway?");
			alert.initOwner(null);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.CANCEL) {
				System.exit(0);
			}
			else {
				Configurator.setRootLevel(Level.OFF);
				logger.info("Turn off database logger.");
			}
		}

		try {
			logger.info("Obtaining databse connection.");
			Session session = SessionManager.getSession();
			logger.info("Connection established successfully.");
			logger.debug(session);
		} catch (Exception e) {
			logger.error("Error obtaining connection from database.", e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText("Error starting up! Cannot connect to database.\nApplication will now exit.");
			alert.showAndWait();
			System.exit(0);
		}

		this.hideMasterListFast = new TranslateTransition(Duration.millis(.1), vboxmasterlist);
		this.showMasterList = new TranslateTransition(Duration.millis(200), vboxmasterlist);
		this.showMasterList.setToX(this.vboxmasterlist.getTranslateX() - this.vboxmasterlist.getWidth());
		this.hideMasterList = new TranslateTransition(Duration.millis(100), vboxmasterlist);
		HomeController homeController = new HomeController();
		try {
			fxmlItemCategory = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlItemCategory.setController(new ItemCategoryController());
			itemcategorypane = fxmlItemCategory.load();
			itemcategorypane.setStyle("-fx-background-color: #6b4262");
			fxmlItemBrand = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlItemBrand.setController(new ItemBrandController());
			itembrandpane = fxmlItemBrand.load();
			itembrandpane.setStyle("-fx-background-color: #1a648f");
			fxmlSupplier = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlSupplier.setController(new SupplierController());
			itemsupplierpane = fxmlSupplier.load();
			itemsupplierpane.setStyle("-fx-background-color: #87a360");
			fxmlItem = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlItem.setController(new ItemController());
			itempane = fxmlItem.load();
			itempane.setStyle("-fx-background-color: #99622b");
			fxmlHome = new FXMLLoader(getClass().getResource("/application/views/HomeView.fxml"));
			fxmlHome.setController(homeController);
			mwnHomePage = fxmlHome.load();
			fxmlUserRole = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlUserRole.setController(new UserRoleController());
			userRolepane = fxmlUserRole.load();
			userRolepane.setStyle("-fx-background-color: #702107");
			fxmlUser = new FXMLLoader(getClass().getResource("/application/views/ListViewer.fxml"));
			fxmlUser.setController(new UserController());
			userpane = fxmlUser.load();
			userpane.setStyle("-fx-background-color:#197594");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				hideMasterListFast.setToX(-(vboxmasterlist.getWidth()));
				hideMasterListFast.play();
				loadHome();
				fxmlLogin = new FXMLLoader(getClass().getResource("/application/views/LoginView.fxml"));
				fxmlLogin.setController(new LoginController());
				try {
					loginPane = fxmlLogin.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AnchorPane.setTopAnchor(loginPane, 1.0);
				AnchorPane.setBottomAnchor(loginPane, 1.0);
				AnchorPane.setLeftAnchor(loginPane, 1.0);
				AnchorPane.setRightAnchor(loginPane, 1.0);
				mainStage.getChildren().add(loginPane);
				homeController.btnRole.setOnAction(event->{
					clearContents();
					loadUserRole();
				});

				homeController.btnUsers.setOnAction(event->{
					clearContents();
					loadUser();
				});
			}
		});
	}

	public void hideMasterList(MouseEvent event) {
		this.hideMasterList.setToX(-(vboxmasterlist.getWidth()));
		hideMasterList.setOnFinished(e->{
			AnchorPane.setLeftAnchor(contentpane, 1.0);
		});
		hideMasterList.play();

	}

	public void showSidePane(ActionEvent event) {
		if(event.getSource() == masterList) {
			AnchorPane.setLeftAnchor(contentpane, vboxmasterlist.getWidth());
			this.showMasterList.play();
		}else {
			//	 if(event.getSource() == mwnhome) {
			this.hideMasterList.setToX(-(vboxmasterlist.getWidth()));
			hideMasterList.play();
			hideMasterList.setOnFinished(e->{
				clearContents();
				loadHome();
			});
		}
	}

	public void showSidePaneHover(MouseEvent event) {
		if(event.getSource() == masterList) {
			AnchorPane.setLeftAnchor(contentpane, vboxmasterlist.getWidth());
			this.showMasterList.play();
		}
		else{//(event.getSource() == mwnhome) {
			this.hideMasterList.setToX(-(vboxmasterlist.getWidth()));
			hideMasterList.play();
		}

	}

	public void loadItemCategory() throws IOException {
		if(!contentpane.getChildren().contains(itemcategorypane)) {
			itemcategorypane.setPrefHeight(contentpane.getHeight() - statusBarPane.getHeight());
			itemcategorypane.setPrefWidth(contentpane.getWidth());
			itemcategorypane.translateXProperty().set(-(contentpane.getWidth()));
			contentpane.getChildren().add(itemcategorypane);

			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(itemcategorypane.translateXProperty(), 0, Interpolator.EASE_IN);
			KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);

			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itemcategorypane);
			ftransition.setFromValue(0);
			ftransition.setToValue(1);
			ParallelTransition ptransition = new ParallelTransition(timeline, ftransition);
			ptransition.setOnFinished(e->{
				AnchorPane.setTopAnchor(itemcategorypane, 1.0); 
				AnchorPane.setBottomAnchor(itemcategorypane, 1.0);
				AnchorPane.setLeftAnchor(itemcategorypane, 1.0); 
				AnchorPane.setRightAnchor(itemcategorypane, 1.0);
				removeContents(ITEM_CATEGORY);
			});
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					ptransition.play();
				}
			});

		}
	}

	public void loadItemBrand() {
		if(!contentpane.getChildren().contains(itembrandpane)) {
			itembrandpane.setPrefHeight(contentpane.getHeight());
			itembrandpane.setPrefWidth(contentpane.getWidth());
			itembrandpane.translateXProperty().set(-(contentpane.getHeight()));
			contentpane.getChildren().add(itembrandpane);

			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(itembrandpane.translateXProperty(), 0, Interpolator.EASE_IN);
			KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);

			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itembrandpane);
			ftransition.setFromValue(0);
			ftransition.setToValue(1);
			ParallelTransition ptransition = new ParallelTransition(timeline, ftransition);
			ptransition.setOnFinished(e->{
				AnchorPane.setTopAnchor(itembrandpane, 1.0); 
				AnchorPane.setBottomAnchor(itembrandpane, 1.0);
				AnchorPane.setLeftAnchor(itembrandpane, 1.0); 
				AnchorPane.setRightAnchor(itembrandpane, 1.0);
				removeContents(ITEM_BRAND);
			});
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					ptransition.play();
				}
			});

		}
	}

	public void loadItemSupplier() {
		if(!contentpane.getChildren().contains(itemsupplierpane)) {
			itemsupplierpane.setPrefHeight(contentpane.getHeight());
			itemsupplierpane.setPrefWidth(contentpane.getWidth());
			itemsupplierpane.translateXProperty().set(-(contentpane.getHeight()));
			contentpane.getChildren().add(itemsupplierpane);

			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(itemsupplierpane.translateXProperty(), 0, Interpolator.EASE_IN);
			KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);

			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itemsupplierpane);
			ftransition.setFromValue(0);
			ftransition.setToValue(1);
			ParallelTransition ptransition = new ParallelTransition(timeline, ftransition);
			ptransition.setOnFinished(e->{
				AnchorPane.setTopAnchor(itemsupplierpane, 1.0); 
				AnchorPane.setBottomAnchor(itemsupplierpane, 1.0);
				AnchorPane.setLeftAnchor(itemsupplierpane, 1.0); 
				AnchorPane.setRightAnchor(itemsupplierpane, 1.0);
				removeContents(ITEM_SUPPLIER);
			});
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					ptransition.play();
				}
			});

		}
	}

	public void loadItem() {
		if(!contentpane.getChildren().contains(itempane)) {
			itempane.setPrefHeight(contentpane.getHeight());
			itempane.setPrefWidth(contentpane.getWidth());
			itempane.translateXProperty().set(-(contentpane.getHeight()));
			contentpane.getChildren().add(itempane);

			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(itempane.translateXProperty(), 0, Interpolator.EASE_IN);
			KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);
			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itempane);
			ftransition.setFromValue(0);
			ftransition.setToValue(1);
			ParallelTransition ptransition = new ParallelTransition(timeline, ftransition);
			ptransition.setOnFinished(e->{
				AnchorPane.setTopAnchor(itempane, 1.0); 
				AnchorPane.setBottomAnchor(itempane, 1.0);
				AnchorPane.setLeftAnchor(itempane, 1.0); 
				AnchorPane.setRightAnchor(itempane, 1.0);
				removeContents(ITEM);
			});
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					ptransition.play();
					loadHome();
				}
			});
		}
	}

	private void loadHome() {
		if(!contentpane.getChildren().contains(mwnHomePage)) {
			mwnHomePage.setPrefHeight(contentpane.getHeight());
			mwnHomePage.setPrefWidth(contentpane.getWidth());
			contentpane.getChildren().add(mwnHomePage);
			if(mwnHomePage.getOpacity() == 0.0) {
				FadeTransition ftransition = new FadeTransition(Duration.millis(300), mwnHomePage);
				ftransition.setFromValue(0);
				ftransition.setToValue(1);
				ftransition.play();
			}
		}
	}

	private void loadUserRole() {
		if(!contentpane.getChildren().contains(userRolepane)) {
			userRolepane.setPrefHeight(contentpane.getHeight());
			userRolepane.setPrefWidth(contentpane.getWidth());
			contentpane.getChildren().add(userRolepane);
		}
	}

	private void loadUser() {
		if(!contentpane.getChildren().contains(userpane)) {
			userpane.setPrefHeight(contentpane.getHeight());
			userpane.setPrefWidth(contentpane.getWidth());
			contentpane.getChildren().add(userpane);
		}
	}

	public void removeItemCategory() {
		if(contentpane.getChildren().contains(itemcategorypane)) {
			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itemcategorypane);
			ftransition.setFromValue(1);
			ftransition.setToValue(0);
			ftransition.setOnFinished(e->{
				contentpane.getChildren().remove(itemcategorypane);
			});
			ftransition.play();
		}
	}

	public void removeItemBrand() {
		if(contentpane.getChildren().contains(itembrandpane)) {
			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itembrandpane);
			ftransition.setFromValue(1);
			ftransition.setToValue(0);
			ftransition.setOnFinished(e->{
				contentpane.getChildren().remove(itembrandpane);
			});
			ftransition.play();
		}
	}

	public void removeItemSupplier() {
		if(contentpane.getChildren().contains(itemsupplierpane)) {
			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itemsupplierpane);
			ftransition.setFromValue(1);
			ftransition.setToValue(0);
			ftransition.setOnFinished(e->{
				contentpane.getChildren().remove(itemsupplierpane);
			});
			ftransition.play();
		}
	}

	public void removeItem() {
		if(contentpane.getChildren().contains(itempane)) {
			FadeTransition ftransition = new FadeTransition(Duration.millis(200), itempane);
			ftransition.setFromValue(1);
			ftransition.setToValue(0);
			ftransition.setOnFinished(e->{
				contentpane.getChildren().remove(itempane);
			});
			ftransition.play();
		}
	}

	public void removeContents(String excludeContent) {
		if(!excludeContent.equals(ITEM_CATEGORY)) 
			removeItemCategory();

		if(!excludeContent.equals(ITEM_BRAND))
			removeItemBrand();

		if(!excludeContent.equals(ITEM_SUPPLIER))
			removeItemSupplier();

		if(!excludeContent.equals(ITEM))
			removeItem();
	}

	private void clearContents() {
		if(contentpane.getChildren().size() != 0) {
			contentpane.getChildren().removeAll(contentpane.getChildren());
		}
	}
}
