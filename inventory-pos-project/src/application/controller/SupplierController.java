package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageSupplier;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.Supplier;
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

public class SupplierController implements Initializable {

	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private JFXButton btnSearch;
	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<Supplier> tblViewer;
	@FXML
	private Label lblRecordCount;

	private TableColumn<Supplier, Integer> colsupplierID;
	private TableColumn<Supplier, String> colsupplierName;
	private TableColumn<Supplier, String> colBRN;
	private TableColumn<Supplier, String> colAddress;
	private TableColumn<Supplier, String> colTelephone;
	private TableColumn<Supplier, String> colTelefax;
	private TableColumn<Supplier, String> colEmail;
	private TableColumn<Supplier, String> colMobile;
	private TableColumn<Supplier, String> colEdit;
	private TableColumn<Supplier, String> colDelete;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.colsupplierID = new TableColumn<Supplier, Integer>("ID");
		this.colsupplierID.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("SupplierId"));
		this.colsupplierName = new TableColumn<Supplier, String>("Name");
		this.colsupplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("SupplierName"));
		this.colBRN = new TableColumn<Supplier, String>("BRN");
		this.colBRN.setCellValueFactory(new PropertyValueFactory<Supplier, String>("BRN"));
		this.colAddress = new TableColumn<Supplier, String>("Address");
		this.colAddress.setCellValueFactory(new PropertyValueFactory<Supplier, String>("Address"));
		this.colTelephone = new TableColumn<Supplier, String>("Telephone");
		this.colTelephone.setCellValueFactory(new PropertyValueFactory<Supplier, String>("Telephone"));
		this.colTelefax = new TableColumn<Supplier, String>("Telefax");
		this.colTelefax.setCellValueFactory(new PropertyValueFactory<Supplier, String>("Telefax"));
		this.colEmail = new TableColumn<Supplier, String>("Email");
		this.colEmail.setCellValueFactory(new PropertyValueFactory<Supplier, String>("Email"));
		this.colMobile = new TableColumn<Supplier, String>("Mobile");
		this.colMobile.setCellValueFactory(new PropertyValueFactory<Supplier, String>("Mobile"));
		this.colEdit = new TableColumn<Supplier, String>("Edit");
		this.colEdit.setCellValueFactory(new PropertyValueFactory<Supplier, String>("DUMMY"));

		Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellEditFactory = 
				new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

			@Override
			public TableCell<Supplier, String> call(TableColumn<Supplier, String> param) {
				final TableCell<Supplier, String> cell = new TableCell<Supplier, String>(){
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
									Supplier supplier = tblViewer.getItems().get(getIndex());
									showEditModal(supplier);	
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

		this.colDelete = new TableColumn<Supplier, String>("Delete");
		this.colDelete.setCellValueFactory(new PropertyValueFactory<Supplier, String>("DUMMY"));

		Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellDeleteFactory = 
				new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

			@Override
			public TableCell<Supplier, String> call(TableColumn<Supplier, String> param) {
				final TableCell<Supplier, String> cell = new TableCell<Supplier, String>(){

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
									Supplier supplier = tblViewer.getItems().get(getIndex());
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirm Delete");
									alert.setContentText("Delete selected item(s)?");
									alert.initOwner(btn.getScene().getWindow());
									Optional<ButtonType> result = alert.showAndWait();
									if(result.isPresent() && result.get() == ButtonType.OK) {
										if(ManageSupplier.getInstance().deleteSupplier(supplier.getSupplierId())) {
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

		this.tblViewer.getColumns().addAll(this.colsupplierID, this.colsupplierName, this.colBRN, this.colAddress,
				this.colTelephone, this.colTelefax, this.colEmail, this.colMobile, this.colEdit, this.colDelete);
		this.cmbCategorySearch.getItems().addAll(Supplier.ALL, Supplier.SUPPLIER_ID, Supplier.SUPPLIER_NAME, Supplier.BUSINESS_REGISTRATION_NUMBER,
				Supplier.ADDRESS, Supplier.TELEPHONE, Supplier.TELEFAX, Supplier.EMAIL, Supplier.MOBILE);
		this.cmbCategorySearch.getSelectionModel().select(Supplier.ALL);

		txtSearch.setOnAction(event ->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnAddNew.setOnAction(event->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD)) {
				showModal();
				setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());	
			}
		});

		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	private void setDataModel(String category, String search) {
		ObservableList<Supplier> suppliers = FXCollections.observableArrayList(ManageSupplier.getInstance().listSupplier(category, search)) ;
		tblViewer.getItems().removeAll(suppliers);
		this.tblViewer.setItems(suppliers);
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}

	private void showModal() {
		try {
			FXMLLoader fxmlSupplierInterface = new FXMLLoader(getClass().getResource("/application/views/SupplierInterfaceView.fxml"));
			BorderPane root = fxmlSupplierInterface.load();
			SupplierInterfaceController supplierIntController = fxmlSupplierInterface.getController();
			supplierIntController.setIsEdit(false);
			Scene scene = new Scene(root);
			Stage stage = new Stage(StageStyle.TRANSPARENT);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAddNew.getScene().getWindow());
			stage.setScene(scene);
			stage.showAndWait();
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showEditModal(Supplier supplier) {
		try {
			FXMLLoader fxmlSupplierInterface = new FXMLLoader(getClass().getResource("/application/views/SupplierInterfaceView.fxml"));
			BorderPane root = fxmlSupplierInterface.load();
			SupplierInterfaceController supplierIntController = fxmlSupplierInterface.getController();
			supplierIntController.setIsEdit(true);
			supplierIntController.getTxtSupplier().setText(supplier.getSupplierName());
			supplierIntController.getTxtBRN().setText(supplier.getBRN());
			supplierIntController.getTxtAddress().setText(supplier.getAddress());
			supplierIntController.getTxtTelephone().setText(supplier.getTelephone());
			supplierIntController.getTxtTelefax().setText(supplier.getTelefax());
			supplierIntController.getTxtEmail().setText(supplier.getEmail());
			supplierIntController.getTxtMobile().setText(supplier.getMobile());
			supplierIntController.setOldId(supplier.getSupplierId());
			Scene scene = new Scene(root);
			Stage stage = new Stage(StageStyle.TRANSPARENT);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAddNew.getScene().getWindow());
			stage.setScene(scene);
			stage.showAndWait();
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
