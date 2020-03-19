package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageSupplier;
import application.utility.DataValidator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SupplierInterfaceController implements Initializable{

	@FXML
	private JFXButton btnOk;
	@FXML
	private JFXButton btnCancel;
	@FXML
	private TextField txtSupplier;
	@FXML
	private TextField txtBRN;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtTelephone;
	@FXML
	private TextField txtTelefax;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtMobile;

	private Stage stage;

	public Boolean isEdit;
	public Integer oldId;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnOk.setOnAction(event->{
			if(isValidated()) {
				if(!isEdit) {
					Boolean added = ManageSupplier.getInstance().addSupplier(txtSupplier.getText().trim(), txtBRN.getText().trim(),
							txtAddress.getText().trim(), txtTelephone.getText().trim(), txtTelefax.getText().trim(),
							txtEmail.getText().trim(),
							txtMobile.getText().trim());
					if(added) {
						stage.close();
					}
				}
				else {
					Boolean edited = ManageSupplier.getInstance().updateSupplier(oldId, txtSupplier.getText().trim(), 
							txtBRN.getText().trim(), txtAddress.getText().trim(), txtTelephone.getText().trim(), 
							txtTelefax.getText(), txtEmail.getText(), txtMobile.getText());
					if(edited) {
						stage.close();
					}
				}
			}
		});

		

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage = (Stage) btnCancel.getScene().getWindow();
				
				btnCancel.setOnAction(event->{
					stage.close();
				});
			}
		});
	}

	private Boolean isValidated() {
		txtSupplier.getStyleClass().remove("error");
		txtBRN.getStyleClass().remove("error");
		txtAddress.getStyleClass().remove("error");
		txtTelephone.getStyleClass().remove("error");
		txtTelefax.getStyleClass().remove("error");
		txtEmail.getStyleClass().remove("error");
		txtMobile.getStyleClass().remove("error");

		if(txtSupplier.getText().trim().isEmpty()) {
			txtSupplier.getStyleClass().add("error");
			txtSupplier.requestFocus();
			return false;
		}
		else if (txtBRN.getText().trim().isEmpty()) {
			txtBRN.requestFocus();
			txtBRN.getStyleClass().add("error");
			return false;
		}
		else if (txtAddress.getText().trim().isEmpty()) {
			txtAddress.requestFocus();
			txtAddress.getStyleClass().add("error");
			return false;
		}
		else if (txtTelephone.getText().trim().isEmpty()) {
			txtTelephone.requestFocus();
			txtTelephone.getStyleClass().add("error");
			return false;
		}

		else if ((txtEmail.getText() != null)) {
			if(!DataValidator.isValidEmail(txtEmail.getText().trim())) {
				txtEmail.requestFocus(); txtEmail.getStyleClass().add("error"); return false;
			} else return true; }

		else {
			return true;
		}
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public TextField getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(TextField txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public TextField getTxtBRN() {
		return txtBRN;
	}

	public void setTxtBRN(TextField txtBRN) {
		this.txtBRN = txtBRN;
	}

	public TextField getTxtAddress() {
		return txtAddress;
	}

	public void setTxtAddress(TextField txtAddress) {
		this.txtAddress = txtAddress;
	}

	public TextField getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(TextField txtTelephone) {
		this.txtTelephone = txtTelephone;
	}

	public TextField getTxtTelefax() {
		return txtTelefax;
	}

	public void setTxtTelefax(TextField txtTelefax) {
		this.txtTelefax = txtTelefax;
	}

	public TextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(TextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public TextField getTxtMobile() {
		return txtMobile;
	}

	public void setTxtMobile(TextField txtMobile) {
		this.txtMobile = txtMobile;
	}
}
