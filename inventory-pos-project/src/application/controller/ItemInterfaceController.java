package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.jfoenix.controls.JFXButton;

import application.controller.datamodel.manager.ManageItem;
import application.controller.datamodel.manager.ManageItemBrand;
import application.controller.datamodel.manager.ManageItemCategory;
import application.controller.datamodel.manager.ManageUnit;
import application.datamodel.ItemBrand;
import application.datamodel.ItemCategory;
import application.datamodel.ItemUnit;
import application.utility.GUIUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ItemInterfaceController implements Initializable{

	@FXML
	private JFXButton btnCancel;
	@FXML
	private TextField txtItemNo;
	@FXML
	private TextField txtName;
	@FXML
	private ComboBox<ItemUnit> cmbUnit;
	@FXML
	private ComboBox<ItemCategory> cmbCategory;
	@FXML
	private ComboBox<ItemBrand> cmbBrand;
	@FXML
	private TextArea txtDescription;
	@FXML
	private ImageView imageViewer;
	@FXML
	private JFXButton btnLoad;
	@FXML
	private JFXButton btnClear;
	@FXML
	private JFXButton btnOk;
	
	private Stage stage;
	//private Boolean isImageLoaded;
	private Boolean isEdit;
	private File file;
	private String oldId;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmbUnit.setItems(FXCollections.observableArrayList(ManageUnit.getInstance().listUnits("")));
		cmbCategory.setItems(FXCollections.observableArrayList(ManageItemCategory.getInstance().listItemCategory(ItemCategory.ALL, "")));
		cmbBrand.setItems(FXCollections.observableArrayList(ManageItemBrand.getInstance().listItemBrands(ItemBrand.ALL, "")));
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				stage = (Stage) btnCancel.getScene().getWindow();
			}
		});
		
		btnCancel.setOnAction(event->{
			stage.close();
		});
		
		btnLoad.setOnAction(event->{
			loadImage();
		});
		
		btnClear.setOnAction(event->{
			clearImage();
		});
		
		btnOk.setOnAction(event->{
			if(isValidated()) {
				if(!isEdit) {
					Boolean added = ManageItem.getInstance().addItem(txtItemNo.getText().trim(), txtName.getText().trim(), txtDescription.getText().trim(), cmbUnit.getSelectionModel().getSelectedItem(), cmbCategory.getSelectionModel().getSelectedItem(), cmbBrand.getSelectionModel().getSelectedItem(), file);
					if(added)
						stage.close();
				}
				else {
					
					try {
						Blob blob = null;
						if(imageViewer.getImage() != null) {
							byte[] byteImage = GUIUtils.readBytesFromImage(imageViewer.getImage());
							blob = new SerialBlob(byteImage);
						}
						
						Boolean edited = ManageItem.getInstance().updateItem(oldId, txtItemNo.getText().trim(), txtName.getText().trim(), 
								txtDescription.getText().trim(), cmbUnit.getSelectionModel().getSelectedItem(), cmbCategory.getSelectionModel().getSelectedItem(), cmbBrand.getSelectionModel().getSelectedItem(), blob);
						if(edited)
							stage.close();
					} catch (SerialException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void loadImage() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter imgFilter = new ExtensionFilter("Images", "*.jpg", "*.png", "*.tif", "*.gif");
		fileChooser.getExtensionFilters().add(imgFilter);
		file = fileChooser.showOpenDialog(btnLoad.getScene().getWindow());
		
		if (file == null)
			return;
		
		imageViewer.setImage(new Image(file.toURI().toString()));
		//isImageLoaded = true;
	}
	
	private void clearImage() {
		imageViewer.setImage(null);
		//isImageLoaded = false;
	}
	
	private Boolean isValidated() {
		txtItemNo.getStyleClass().remove("error");
		txtName.getStyleClass().remove("error");
		cmbUnit.getStyleClass().remove("error");
		cmbCategory.getStyleClass().remove("error");
		cmbBrand.getStyleClass().remove("error");
		if(txtItemNo.getText().trim().isEmpty()) {
			txtItemNo.getStyleClass().add("error");
			txtItemNo.requestFocus();
			return false;
		}
		else if(txtName.getText().trim().isEmpty()) {
			txtName.getStyleClass().add("error");
			txtName.requestFocus();
			return false;
		}
		else if(cmbUnit.getSelectionModel().getSelectedItem() == null) {
			cmbUnit.getStyleClass().add("error");
			cmbUnit.requestFocus();
			return false;
		}
		else if(cmbCategory.getSelectionModel().getSelectedItem() == null) {
			cmbCategory.getStyleClass().add("error");
			cmbCategory.requestFocus();
			return false;
		}
		else if(cmbBrand.getSelectionModel().getSelectedItem() == null) {
			cmbBrand.getStyleClass().add("error");
			cmbBrand.requestFocus();
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

	public TextField getTxtItemNo() {
		return txtItemNo;
	}

	public void setTxtItemNo(TextField txtItemNo) {
		this.txtItemNo = txtItemNo;
	}

	public TextField getTxtName() {
		return txtName;
	}

	public void setTxtName(TextField txtName) {
		this.txtName = txtName;
	}

	public ComboBox<ItemUnit> getCmbUnit() {
		return cmbUnit;
	}

	public void setCmbUnit(ComboBox<ItemUnit> cmbUnit) {
		this.cmbUnit = cmbUnit;
	}

	public ComboBox<ItemCategory> getCmbCategory() {
		return cmbCategory;
	}

	public void setCmbCategory(ComboBox<ItemCategory> cmbCategory) {
		this.cmbCategory = cmbCategory;
	}

	public ComboBox<ItemBrand> getCmbBrand() {
		return cmbBrand;
	}

	public void setCmbBrand(ComboBox<ItemBrand> cmbBrand) {
		this.cmbBrand = cmbBrand;
	}

	public TextArea getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(TextArea txtDescription) {
		this.txtDescription = txtDescription;
	}

	public ImageView getImageViewer() {
		return imageViewer;
	}

	public void setImageViewer(ImageView imageViewer) {
		this.imageViewer = imageViewer;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
}
