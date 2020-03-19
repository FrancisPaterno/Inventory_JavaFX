package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageItemCategory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ItemCategoryInterfaceController implements Initializable{

	@FXML
	private BorderPane parentContainer;
	@FXML
	private TextField txtCategory;
	@FXML
	private TextField txtDescription;
	@FXML
	private JFXButton btnOk;
	@FXML
	private JFXButton btnCancel;
	
	private Stage stage;
	
	private String oldCategory;
	
	public static volatile Boolean isEdit;
	
	public static Boolean getIsEdit() {
		return isEdit;
	}

	public static void setIsEdit(Boolean isEdit) {
		ItemCategoryInterfaceController.isEdit = isEdit;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				stage = (Stage) parentContainer.getScene().getWindow();
			}
		});
	}

	private Boolean isValidated() {
		txtCategory.getStyleClass().remove("error");
		if(txtCategory.getText().trim().isEmpty()) {
			txtCategory.getStyleClass().add("error");
			txtCategory.requestFocus();
			return false;
		}
		else return true;
	}
	
	public void addCategory(ActionEvent event) {
		if(isValidated()) {
			if(!isEdit) {
				if(ManageItemCategory.getInstance().addItemCategory(txtCategory.getText().trim(), txtDescription.getText().trim())) {
					stage.close();
				}
			}
			else {
				if(ManageItemCategory.getInstance().updateItemCategory(this.oldCategory, txtCategory.getText().trim(), txtDescription.getText().trim())) {
					stage.close();
				}
			}
			
		}
	}
	
	public void hideDialog(ActionEvent event) {
		stage.close();
	}

	public TextField getTxtCategory() {
		return txtCategory;
	}

	public void setTxtCategory(TextField txtCategory) {
		this.txtCategory = txtCategory;
	}

	public TextField getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(TextField txtDescription) {
		this.txtDescription = txtDescription;
	}

	public String getOldCategory() {
		return oldCategory;
	}

	public void setOldCategory(String oldCategory) {
		this.oldCategory = oldCategory;
	}
}
 