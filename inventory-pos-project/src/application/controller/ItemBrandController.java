package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageItemBrand;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.ItemBrand;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class ItemBrandController implements Initializable{

	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private JFXButton btnSearch;
	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<ItemBrand> tblViewer;
	@FXML
	private Label lblRecordCount;

	private TableColumn<ItemBrand, String> colBrand;
	private TableColumn<ItemBrand, String> colDescription;
	private TableColumn<ItemBrand, String> colEdit;
	private TableColumn<ItemBrand, String> colDelete;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.colBrand = new TableColumn<ItemBrand, String>("Brand");
		this.colBrand.setCellValueFactory(new PropertyValueFactory<>("Brandname"));
		this.colDescription = new TableColumn<ItemBrand, String>("Description");
		this.colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
		this.colEdit = new TableColumn<ItemBrand, String>("Edit");
		this.colEdit.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<ItemBrand, String>, TableCell<ItemBrand, String>> cellEditFactory = 
				new Callback<TableColumn<ItemBrand,String>, TableCell<ItemBrand,String>>() {

			@Override
			public TableCell<ItemBrand, String> call(TableColumn<ItemBrand, String> param) {
				final TableCell<ItemBrand, String> cell = new TableCell<ItemBrand, String>(){
					FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
					final JFXButton btn = new JFXButton("Edit", icon);

					@Override
					public void updateItem(String item, boolean empty) {
						icon.setSize("25");
						btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction(event -> {
								if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.EDIT)) {
									ItemBrand itemBrand = getTableView().getItems().get(getIndex());
									try {
										showEditDialog(itemBrand);
									} catch (IOException e) {
										e.printStackTrace();
									}	
								}

							});
							setGraphic(btn);
							setAlignment(Pos.CENTER);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		colEdit.setCellFactory(cellEditFactory);

		this.colDelete = new TableColumn<ItemBrand, String>("Delete");
		this.colDelete.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<ItemBrand, String>, TableCell<ItemBrand, String>> cellDeleteFactory = 
				new Callback<TableColumn<ItemBrand,String>, TableCell<ItemBrand,String>>() {

			@Override
			public TableCell<ItemBrand, String> call(TableColumn<ItemBrand, String> param) {
				final TableCell<ItemBrand, String> cell = new TableCell<ItemBrand, String>(){

					FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.REMOVE);
					final JFXButton btn = new JFXButton("Delete", icon);
					@Override
					public void updateItem(String item, boolean empty) {
						icon.setSize("25");
						btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction(event -> {
								if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.DELETE)) {
									ItemBrand itemBrand = getTableView().getItems().get(getIndex());
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirm Delete");
									alert.setContentText("Delete selected item(s)?");
									alert.initOwner(btn.getScene().getWindow());
									Optional<ButtonType> result = alert.showAndWait();
									if(result.isPresent() && result.get() == ButtonType.OK) {
										if(ManageItemBrand.getInstance().deleteItemBrand(itemBrand.getBrandname())) {
											setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
										}
									}	
								}
							});
							setGraphic(btn);
							setAlignment(Pos.CENTER);
							setText(null);
						}
					}
				};
				return cell;
			}
		};
		colDelete.setCellFactory(cellDeleteFactory);
		this.tblViewer.getColumns().addAll(this.colBrand, this.colDescription, this.colEdit, this.colDelete);
		this.cmbCategorySearch.setItems(FXCollections.observableArrayList(ItemBrand.ALL, ItemBrand.BRAND, ItemBrand.DESCRIPTION));
		this.cmbCategorySearch.getSelectionModel().select(ItemBrand.ALL);

		this.txtSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		this.btnSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnAddNew.setOnAction(e->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD)) {
				try {
					showModal(e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	private void setDataModel(String category, String search) {
		ObservableList<ItemBrand> itemBrand = FXCollections.observableArrayList(ManageItemBrand.getInstance().listItemBrands(category, search));
		tblViewer.getItems().removeAll(itemBrand);
		tblViewer.setItems(itemBrand);
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}

	public void searchData(ActionEvent event) {
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	public void showModal(ActionEvent event) throws IOException {
		FXMLLoader fxmlModal = new FXMLLoader(getClass().getResource("/application/views/ItemBrandInterfaceView.fxml"));
		BorderPane modal = fxmlModal.load();
		ItemBrandInterfaceController itemBrandCont = fxmlModal.getController();
		itemBrandCont.setIsEdit(false);
		Stage dialog = new Stage(StageStyle.TRANSPARENT);
		Scene scene = new Scene(modal);
		dialog.setScene(scene);
		dialog.initOwner(btnAddNew.getScene().getWindow());
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	public void showEditDialog(ItemBrand brand) throws IOException {
		FXMLLoader fxmlModal = new FXMLLoader(getClass().getResource("/application/views/ItemBrandInterfaceView.fxml"));
		BorderPane modal = fxmlModal.load();
		ItemBrandInterfaceController itemBrandCont = fxmlModal.getController();
		itemBrandCont.setIsEdit(true);
		itemBrandCont.setOldBrand(brand.getBrandname());
		itemBrandCont.getTxtBrand().setText(brand.getBrandname());
		itemBrandCont.getTxtDescription().setText(brand.getDescription());
		Stage dialog = new Stage(StageStyle.TRANSPARENT);
		Scene scene = new Scene(modal);
		dialog.setScene(scene);
		dialog.initOwner(btnAddNew.getScene().getWindow());
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}
}
