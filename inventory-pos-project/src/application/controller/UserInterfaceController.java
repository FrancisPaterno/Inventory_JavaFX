package application.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageUser;
import application.controller.datamodel.manager.ManageUserRole;
import application.datamodel.UserRole;
import application.utility.DataValidator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserInterfaceController implements Initializable{

	@FXML
	private JFXButton btnOk;
	@FXML
	private JFXButton btnCancel;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<UserRole> cmbRole;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private PasswordField txtConPassword;

	private Stage stage;

	private  Boolean isEdit;

	private Integer oldId;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<UserRole> usrRoles = FXCollections.observableArrayList(ManageUserRole.getInstance().listUserRole(UserRole.ALL, ""));
		cmbRole.setItems(usrRoles);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage = (Stage) btnCancel.getScene().getWindow();
			}
		});
		btnCancel.setOnAction(event->{
			stage.close();
		});

		btnOk.setOnAction(event->{
			if(isValidated()) {
				Calendar calendar = Calendar.getInstance();
				if(!isEdit) {
					Boolean added = ManageUser.getInstance().addUser(txtUsername.getText().trim(), txtEmail.getText().trim(), txtPassword.getText().trim(), calendar.getTime(), cmbRole.getSelectionModel().getSelectedItem());
					if(added) {
						stage.close();
					}
				}
				else {
					Boolean updated = ManageUser.getInstance().updateUser(oldId, txtUsername.getText().trim(), txtEmail.getText().trim(), txtPassword.getText().trim(), calendar.getTime(), cmbRole.getSelectionModel().getSelectedItem());
					if(updated) {
						stage.close();
					}
				}
			}
		});
	}

	private Boolean isValidated() {
		txtUsername.getStyleClass().remove("error");
		txtEmail.getStyleClass().remove("error");
		cmbRole.getStyleClass().remove("error");
		txtPassword.getStyleClass().remove("error");
		txtConPassword.getStyleClass().remove("error");

		if(txtUsername.getText().trim().isEmpty()) {
			txtUsername.getStyleClass().add("error");
			txtUsername.requestFocus();
			return false;
		}
		else if (txtEmail.getText().trim().isEmpty()) {
			txtEmail.getStyleClass().add("error");
			txtEmail.requestFocus();
			return false;
		}
		else if(!txtEmail.getText().trim().isEmpty()&& (!DataValidator.isValidEmail(txtEmail.getText().trim()))) {
			txtEmail.getStyleClass().add("error");
			txtEmail.requestFocus();
			return false;
		}
		else if (cmbRole.getSelectionModel().getSelectedItem() == null) {
			cmbRole.getStyleClass().add("error");
			cmbRole.requestFocus();
			return false;
		}
		else if (txtPassword.getText().trim().isEmpty()) {
			txtPassword.getStyleClass().add("error");
			txtPassword.requestFocus();
			return false;
		}
		else if (txtConPassword.getText().trim().isEmpty()) {
			txtConPassword.getStyleClass().add("error");
			txtConPassword.requestFocus();
			return false;
		}
		if(!txtPassword.getText().trim().equals(txtConPassword.getText().trim())) {
			txtConPassword.getStyleClass().add("error");
			txtConPassword.requestFocus();
			return false;
		}
		else return true;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public TextField getTxtUsername() {
		return txtUsername;
	}

	public void setTxtUsername(TextField txtUsername) {
		this.txtUsername = txtUsername;
	}

	public TextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(TextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public ComboBox<UserRole> getCmbRole() {
		return cmbRole;
	}

	public void setCmbRole(ComboBox<UserRole> cmbRole) {
		this.cmbRole = cmbRole;
	}

	public PasswordField getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(PasswordField txtPassword) {
		this.txtPassword = txtPassword;
	}

	public PasswordField getTxtConPassword() {
		return txtConPassword;
	}

	public void setTxtConPassword(PasswordField txtConPassword) {
		this.txtConPassword = txtConPassword;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}
}