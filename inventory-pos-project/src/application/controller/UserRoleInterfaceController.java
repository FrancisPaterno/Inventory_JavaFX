package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageUserRole;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserRoleInterfaceController implements Initializable{

	@FXML
	private TextField txtRole;
	@FXML
	private TextField txtDescription;
	@FXML
	private JFXButton btnOk;
	@FXML
	private JFXButton btnCancel;

	private Boolean isEdit;
	private String oldRole;
	private Stage stage;
	public String getOldRole() {
		return oldRole;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage = (Stage) btnOk.getScene().getWindow();

			}
		});
		btnCancel.setOnAction(event->{
			stage.close();
		});

		btnOk.setOnAction(event->{
			if(isValidated()) {
				if(!isEdit) {
					Boolean added = ManageUserRole.getInstance().addUserRole(txtRole.getText().trim(), txtDescription.getText().trim());
					if(added)
						stage.close();
				}
				else {
					Boolean edited = ManageUserRole.getInstance().updateUserRole(oldRole, txtRole.getText().trim(), txtDescription.getText().trim());
					if(edited)
						stage.close();
				}
			}
		});
	}

	public void setOldRole(String oldRole) {
		this.oldRole = oldRole;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	private Boolean isValidated() {
		txtRole.getStyleClass().remove("error");
		if(txtRole.getText().trim().isEmpty()) {
			txtRole.getStyleClass().add("error");
			return false;
		}
		else return true;
	}

	public TextField getTxtRole() {
		return txtRole;
	}

	public void setTxtRole(TextField txtRole) {
		this.txtRole = txtRole;
	}

	public TextField getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(TextField txtDescription) {
		this.txtDescription = txtDescription;
	}
}
