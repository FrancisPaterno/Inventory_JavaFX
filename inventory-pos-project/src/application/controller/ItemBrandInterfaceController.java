package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageItemBrand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ItemBrandInterfaceController implements Initializable {

	@FXML
	private BorderPane parentContainer;
	@FXML
	private TextField txtBrand;
	@FXML
	private TextField txtDescription;
	@FXML
	private JFXButton btnOk;
	@FXML 
	private JFXButton btnCancel;

	private Stage stage;
	private Boolean isEdit;
	private String oldBrand;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnCancel.setOnAction(event->{
			hideDialog();
		});

		btnOk.setOnAction(event->{
			saveBrand();
		});

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage = (Stage) parentContainer.getScene().getWindow();

			}
		});
	}

	private void hideDialog() {
		stage.close();
	}

	public void saveBrand() {
		if(isValidated()) {
			if(!isEdit) {
				Boolean added
				= ManageItemBrand.getInstance().addItemBrand(txtBrand.getText().trim(), txtDescription.getText().trim());
				if(added) {
					stage.close();
				}
			}
			else {
				Boolean edited 
				= ManageItemBrand.getInstance().updateItemBrand(oldBrand, txtBrand.getText().trim(), txtDescription.getText().trim());
				if(edited) {
					stage.close();
				}
			}
		}
	}

	private Boolean isValidated() {
		txtBrand.getStyleClass().remove("error");
		if(txtBrand.getText().trim().isEmpty()) {
			txtBrand.getStyleClass().add("error");
			txtBrand.requestFocus();
			return false;
		}
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

	public String getOldBrand() {
		return oldBrand;
	}

	public void setOldBrand(String oldBrand) {
		this.oldBrand = oldBrand;
	}

	public TextField getTxtBrand() {
		return txtBrand;
	}

	public void setTxtBrand(TextField txtBrand) {
		this.txtBrand = txtBrand;
	}

	public TextField getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(TextField txtDescription) {
		this.txtDescription = txtDescription;
	} 
}
