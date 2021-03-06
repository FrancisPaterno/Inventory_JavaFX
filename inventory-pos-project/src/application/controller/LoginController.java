package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.controller.datamodel.manager.ManageUser;
import application.datamodel.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable{

	@FXML
	private ImageView imgView;
	@FXML
	private AnchorPane parentPane;
	@FXML
	private AnchorPane signinPane;
	@FXML
	private JFXTextField txtUsername;
	@FXML
	private JFXPasswordField txtPassword;
	@FXML
	private JFXButton btnSignin;
	@FXML
	private Label lblError;
	
	private AnchorPane mainStage;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Image img = new Image(getClass().getResourceAsStream("/application/images/techImage.jpg"));
				imgView.setImage(img);
				imgView.fitWidthProperty().bind(parentPane.widthProperty());
				imgView.fitHeightProperty().bind(parentPane.widthProperty());
				mainStage  = (AnchorPane) parentPane.getParent();
				btnSignin.setOnAction(event->{
					if(isValidated()) {
						if(isUserValid()) {
							Label lblUserInfo =  (Label) mainStage.lookup("#lblUserInfo");
							User user = ManageUser.getInstance().getCurrentUser();
							lblUserInfo.setText("User : " + user.getUsername() + ", Role : " + user.getUserrole().getRole());
						}
					}
				});
			}
		});
	}
	
	private Boolean isUserValid() {
		String sha1Pass = DigestUtils.sha1Hex(txtPassword.getText().trim());
		lblError.setText(null);
		User user = ManageUser.getInstance().getUserbyUsername(txtUsername.getText().trim());
		if(user == null) {
			lblError.setText("User is invalid or User does not exist.");
			txtUsername.getStyleClass().add("error");
			txtUsername.requestFocus();
			return false;
		}
		else {
			if(!sha1Pass.equals(user.getPassword())) {
				lblError.setText("Password is incorrect.");
				txtPassword.getStyleClass().add("error");
				txtPassword.requestFocus();
				return false;
			}
			else {
				ManageUser.getInstance().setCurrentUser(user);
				AnchorPane parent = (AnchorPane) parentPane.getParent();
				parent.getChildren().remove(parentPane);
				return true;
			}
		}
	}
	
	private Boolean isValidated() {
		txtUsername.getStyleClass().remove("error");
		txtPassword.getStyleClass().remove("error");
		if(txtUsername.getText().trim().isEmpty()) {
			txtUsername.getStyleClass().add("error");
			txtUsername.requestFocus();
			return false;
		}
		else if (txtPassword.getText().trim().isEmpty()) {
			txtPassword.getStyleClass().add("error");
			txtPassword.requestFocus();
			return false;
		}
		else return true;
	}
}
