package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Main;
import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageItemCategory;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.ItemCategory;
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
import javafx.scene.control.Button;
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

public class ItemCategoryController implements Initializable{

	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<ItemCategory> tblViewer;
	private TableColumn<ItemCategory, String> colCategory;
	private TableColumn<ItemCategory, String> colDescription;
	private TableColumn<ItemCategory, String> colEdit;
	private TableColumn<ItemCategory, String> colDelete;
	@FXML
	private Label lblRecordCount;
	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colCategory = new TableColumn<ItemCategory, String>("Category");
		colCategory.setCellValueFactory(new PropertyValueFactory<ItemCategory, String>("ItemCategory"));
		colDescription = new TableColumn<ItemCategory, String>("Description");
		colDescription.setCellValueFactory(new PropertyValueFactory<ItemCategory, String>("Description"));
		colEdit = new TableColumn<ItemCategory, String>("Edit");
		colEdit.setCellValueFactory(new PropertyValueFactory<ItemCategory, String>("DUMMY"));
		colDelete = new TableColumn<ItemCategory, String>("Delete");
		colDelete.setCellValueFactory(new PropertyValueFactory<ItemCategory, String>("DUMMY"));
		tblViewer.getColumns().addAll(colCategory, colDescription, colEdit, colDelete);
		cmbCategorySearch.setItems(FXCollections.observableArrayList(ItemCategory.ALL, ItemCategory.CATEGORY, ItemCategory.DESCRIPTION));
		cmbCategorySearch.getSelectionModel().select(ItemCategory.ALL);
		Callback<TableColumn<ItemCategory, String>, TableCell<ItemCategory, String>> cellEditFactory = 
				new Callback<TableColumn<ItemCategory,String>, TableCell<ItemCategory,String>>() {

			@Override
			public TableCell<ItemCategory, String> call(TableColumn<ItemCategory, String> param) {
				final TableCell<ItemCategory, String> cell = new TableCell<ItemCategory, String>(){
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
								ItemCategory itemCat = getTableView().getItems().get(getIndex());
								try {
									if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.EDIT)) {
										showEditDialog(itemCat.getItemCategory(), itemCat.getDescription());
										setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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
		Callback<TableColumn<ItemCategory, String>, TableCell<ItemCategory, String>> cellDeleteFactory = 
				new Callback<TableColumn<ItemCategory,String>, TableCell<ItemCategory,String>>() {

			@Override
			public TableCell<ItemCategory, String> call(TableColumn<ItemCategory, String> param) {
				final TableCell<ItemCategory, String> cell = new TableCell<ItemCategory, String>(){

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
									ItemCategory itemCat = getTableView().getItems().get(getIndex());
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirm Delete");
									alert.setContentText("Delete selected item(s)?");
									alert.initOwner(btn.getScene().getWindow());
									Optional<ButtonType> result = alert.showAndWait();
									if(result.isPresent() && result.get() == ButtonType.OK) {
										if(ManageItemCategory.getInstance().deleteItemCategory(itemCat.getItemCategory())) {
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

		txtSearch.setOnAction(e ->{
			searchData(e);
		});

		btnSearch.setOnAction(e->{
			searchData(e);
		});

		btnAddNew.setOnAction(e-> {
			try {
				if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD)) {
					showModal(e);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	public void showModal(ActionEvent event) throws IOException {
		BorderPane modalView;
		Stage dialog;
		modalView = FXMLLoader.load(getClass().getResource("/application/views/ItemCategoryInterfaceView.fxml"));
		dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(btnAddNew.getScene().getWindow());
		dialog.setScene(new Scene(modalView));
		ItemCategoryInterfaceController.setIsEdit(false);
		dialog.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	public void showEditDialog(String category, String description) throws IOException {
		BorderPane modalView;
		Stage dialog;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/ItemCategoryInterfaceView.fxml"));
		//fxmlLoader = FXMLLoader.load(getClass().getResource("/application/views/ItemCategoryInterfaceView.fxml"));
		modalView = fxmlLoader.load();
		ItemCategoryInterfaceController itemCont = fxmlLoader.getController();
		itemCont.getTxtCategory().setText(category);
		itemCont.getTxtDescription().setText(description);
		itemCont.setOldCategory(category);
		dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(Main.INSTANCE);
		dialog.setScene(new Scene(modalView));
		ItemCategoryInterfaceController.setIsEdit(true);
		dialog.showAndWait();
	}


	public void searchData(ActionEvent event) {
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	public void setDataModel(String category, String search) {
		ObservableList<ItemCategory> itemCat = FXCollections.observableArrayList(ManageItemCategory.getInstance().listItemCategory(category, search));
		tblViewer.getItems().removeAll(itemCat);
		tblViewer.setItems(itemCat);
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}
}
