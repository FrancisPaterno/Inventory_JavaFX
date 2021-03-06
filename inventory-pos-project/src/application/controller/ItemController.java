package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageItem;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.Item;
import application.datamodel.ItemBrand;
import application.datamodel.ItemCategory;
import application.datamodel.ItemUnit;
import application.utility.GUIUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class ItemController implements Initializable{

	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<Item> tblViewer;
	@FXML
	private Label lblRecordCount;
	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;

	private TableColumn<Item, String> colId;
	private TableColumn<Item, String> colName;
	private TableColumn<Item, String> colDescription;
	private TableColumn<Item, ItemUnit> colUnit;
	private TableColumn<Item, ItemCategory> colCategory;
	private TableColumn<Item, ItemBrand> colBrand;
	private TableColumn<Item, String> colEdit;
	private TableColumn<Item, String> colDelete;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colId = new TableColumn<Item, String>("Id");
		colId.setCellValueFactory(new PropertyValueFactory<Item, String>("Id"));
		colName = new TableColumn<Item, String>("Name");
		colName.setCellValueFactory(new PropertyValueFactory<Item, String>("Name"));
		colDescription = new TableColumn<Item, String>("Description");
		colDescription.setCellValueFactory(new PropertyValueFactory<Item, String>("Description"));
		colUnit = new TableColumn<Item, ItemUnit>("Unit");
		colUnit.setCellValueFactory(new PropertyValueFactory<Item, ItemUnit>("Unit"));
		colCategory = new TableColumn<Item, ItemCategory>("Category");
		colCategory.setCellValueFactory(new PropertyValueFactory<Item, ItemCategory>("Category"));
		colBrand = new TableColumn<Item, ItemBrand>("Brand");
		colBrand.setCellValueFactory(new PropertyValueFactory<Item, ItemBrand>("Brand"));
		colEdit = new TableColumn<Item, String>("Edit");
		colEdit.setCellValueFactory(new PropertyValueFactory<Item, String>("DUMMY"));
		colDelete = new TableColumn<Item, String>("Delete");
		colDelete.setCellValueFactory(new PropertyValueFactory<Item, String>("DUMMY"));
		Callback<TableColumn<Item, String>, TableCell<Item, String>> cellEditFactory = 
				new Callback<TableColumn<Item,String>, TableCell<Item,String>>() {

			@Override
			public TableCell<Item, String> call(TableColumn<Item, String> param) {
				final TableCell<Item, String> cell = new TableCell<Item, String>(){
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
									Item xitem = tblViewer.getItems().get(getIndex());
									try {
										showEditModal(xitem);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
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

		Callback<TableColumn<Item, String>, TableCell<Item, String>> cellDeleteFactory = 
				new Callback<TableColumn<Item,String>, TableCell<Item,String>>() {

			@Override
			public TableCell<Item, String> call(TableColumn<Item, String> param) {
				final TableCell<Item, String> cell = new TableCell<Item, String>(){

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
									Item xitem = tblViewer.getItems().get(getIndex());
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirm Delete");
									alert.setContentText("Delete selected item(s)?");
									alert.initOwner(btn.getScene().getWindow());
									Optional<ButtonType> result = alert.showAndWait();
									if(result.isPresent() && result.get() == ButtonType.OK) {
										if(ManageItem.getInstance().deleteItem(xitem.getId())) {
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
		tblViewer.getColumns().addAll(colId, colName, colDescription, colUnit, colCategory, colBrand, colEdit, colDelete);
		cmbCategorySearch.setItems(FXCollections.observableArrayList(Item.ALL, Item.ITEM_ID, Item.ITEM_NAME, Item.ITEM_DESCRIPTION, 
				Item.ITEM_UNIT, Item.ITEM_UNIT, Item.ITEM_CATEGORY, Item.ITEM_BRAND));
		cmbCategorySearch.getSelectionModel().select(Item.ALL);
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());

		txtSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnAddNew.setOnAction(event->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD)) {
				try {
					showModal();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void setDataModel(String category, String search) {
		ObservableList<Item> items = FXCollections.observableArrayList(ManageItem.getInstance().listItem(category, search));
		tblViewer.getItems().removeAll(items);
		tblViewer.setItems(items);
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}

	private void showModal() throws IOException {
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAddNew.getScene().getWindow());
		FXMLLoader fxmlItemInterface = new FXMLLoader(getClass().getResource("/application/views/ItemInterfaceView.fxml"));
		ItemInterfaceController itemIntCont = new ItemInterfaceController();
		itemIntCont.setIsEdit(false);
		fxmlItemInterface.setController(itemIntCont);
		BorderPane ItemInterfacepane = fxmlItemInterface.load();
		stage.setScene(new Scene(ItemInterfacepane));
		stage.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	private void showEditModal(Item item) throws IOException, SQLException {
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAddNew.getScene().getWindow());
		FXMLLoader fxmlItemInterface = new FXMLLoader(getClass().getResource("/application/views/ItemInterfaceView.fxml"));
		ItemInterfaceController itemIntCont = new ItemInterfaceController();
		fxmlItemInterface.setController(itemIntCont);
		BorderPane ItemInterfacepane = fxmlItemInterface.load();
		stage.setScene(new Scene(ItemInterfacepane));
		itemIntCont.setIsEdit(true);
		itemIntCont.setOldId(item.getId());
		itemIntCont.getTxtItemNo().setText(item.getId());
		itemIntCont.getTxtName().setText(item.getName()); ;
		itemIntCont.getTxtDescription().setText(item.getDescription());
		itemIntCont.getCmbUnit().getSelectionModel().select(item.getUnit());
		itemIntCont.getCmbCategory().getSelectionModel().select(item.getCategory());
		itemIntCont.getCmbBrand().getSelectionModel().select(item.getBrand());
		if(item.getItemImage() != null) {
			if(item.getItemImage().getItem() != null) 
			{
				Blob blob = item.getItemImage().getImage();
				byte[] bImg = blob.getBytes(1, (int) blob.length());
				Image img = GUIUtils.readImageFromByte(bImg);
				itemIntCont.getImageViewer().setImage(img);
			}
		}
		stage.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}
}
