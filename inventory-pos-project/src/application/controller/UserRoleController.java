package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageUser;
import application.controller.datamodel.manager.ManageUserRole;
import application.datamodel.UserRole;
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

public class UserRoleController implements Initializable{

	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private JFXButton btnSearch;
	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<UserRole> tblViewer;
	@FXML
	private Label lblRecordCount;

	private TableColumn<UserRole, String> colRole;
	private TableColumn<UserRole, String> colDesc;
	private TableColumn<UserRole, String> colPermissions;
	private TableColumn<UserRole, String> colEdit;
	private TableColumn<UserRole, String> colDelete;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colRole = new TableColumn<UserRole, String>("Role");
		colRole.setCellValueFactory(new PropertyValueFactory<UserRole, String>("Role"));
		colDesc = new TableColumn<UserRole, String>("Description");
		colDesc.setCellValueFactory(new PropertyValueFactory<UserRole, String>("Description"));
		colPermissions = new TableColumn<UserRole, String>("Permissions");
		colPermissions.setCellValueFactory(new PropertyValueFactory<UserRole, String>("DUMMY"));
		Callback<TableColumn<UserRole, String>, TableCell<UserRole, String>> cellPermissionFactory = 
				new Callback<TableColumn<UserRole,String>, TableCell<UserRole,String>>() {

			@Override
			public TableCell<UserRole, String> call(TableColumn<UserRole, String> param) {
				final TableCell<UserRole, String> cell = new TableCell<UserRole, String>(){
					FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WRENCH);
					final JFXButton btn = new JFXButton("Permmission", icon);

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
								if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.PERMISSION_VIEW)) {
									UserRole usrRole = tblViewer.getItems().get(getIndex());
									try {
										showPermissionsDialog(usrRole);
									} catch (IOException e) {
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

		this.colPermissions.setCellFactory(cellPermissionFactory);

		this.colEdit = new TableColumn<UserRole, String>("Edit");
		this.colEdit.setCellValueFactory(new PropertyValueFactory<UserRole, String>("DUMMY"));
		this.cmbCategorySearch.getItems().addAll(UserRole.ALL, UserRole.ROLE, UserRole.DESCRIPTION);
		this.cmbCategorySearch.getSelectionModel().select(UserRole.ALL);
		Callback<TableColumn<UserRole, String>, TableCell<UserRole, String>> cellEditFactory = 
				new Callback<TableColumn<UserRole,String>, TableCell<UserRole,String>>() {

			@Override
			public TableCell<UserRole, String> call(TableColumn<UserRole, String> param) {
				final TableCell<UserRole, String> cell = new TableCell<UserRole, String>(){
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
									UserRole usrRole = tblViewer.getItems().get(getIndex());
									showEditModal(usrRole);	
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

		this.colDelete = new TableColumn<UserRole, String>("Delete");
		this.colDelete.setCellValueFactory(new PropertyValueFactory<UserRole, String>("DUMMY"));

		Callback<TableColumn<UserRole, String>, TableCell<UserRole, String>> cellDeleteFactory = 
				new Callback<TableColumn<UserRole,String>, TableCell<UserRole,String>>() {

			@Override
			public TableCell<UserRole, String> call(TableColumn<UserRole, String> param) {
				final TableCell<UserRole, String> cell = new TableCell<UserRole, String>(){

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
									UserRole usrRole = tblViewer.getItems().get(getIndex());
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirm Delete");
									alert.setContentText("Delete selected item(s)?");
									alert.initOwner(btn.getScene().getWindow());
									Optional<ButtonType> result = alert.showAndWait();
									if(result.isPresent() && result.get() == ButtonType.OK) {
										if(ManageUserRole.getInstance().deleteUserRole(usrRole.getRole())) {
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
		this.tblViewer.getColumns().addAll(colRole, colDesc, colPermissions, colEdit, colDelete);
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		this.btnAddNew.setOnAction(event->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD))
				showModal();
		});

		this.btnSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		this.txtSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});
	}

	private void showModal() {
		FXMLLoader fxmlUserRoleInterface = new FXMLLoader(getClass().getResource("/application/views/UserRoleInterfaceView.fxml"));
		UserRoleInterfaceController usrRoleInt = new UserRoleInterfaceController();
		usrRoleInt.setIsEdit(false);
		fxmlUserRoleInterface.setController(usrRoleInt);
		try {
			BorderPane root = fxmlUserRoleInterface.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(btnAddNew.getScene().getWindow());
			stage.showAndWait();
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showEditModal(UserRole usrRole) {
		FXMLLoader fxmlUserRoleInterface = new FXMLLoader(getClass().getResource("/application/views/UserRoleInterfaceView.fxml"));
		UserRoleInterfaceController usrRoleInt = new UserRoleInterfaceController();
		usrRoleInt.setIsEdit(true);
		usrRoleInt.setOldRole(usrRole.getRole());

		fxmlUserRoleInterface.setController(usrRoleInt);
		try {
			BorderPane root = fxmlUserRoleInterface.load();
			usrRoleInt.getTxtRole().setText(usrRole.getRole());
			usrRoleInt.getTxtDescription().setText(usrRole.getDescription());
			Scene scene = new Scene(root);
			Stage stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(btnAddNew.getScene().getWindow());
			stage.showAndWait();
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showPermissionsDialog(UserRole usrRole) throws IOException {
		FXMLLoader fxmlPermissionView = new FXMLLoader(getClass().getResource("/application/views/PermissionsView.fxml"));
		PermissionController permissionCont = new PermissionController();
		permissionCont.setUserRole(usrRole);
		fxmlPermissionView.setController(permissionCont);
		BorderPane root = fxmlPermissionView.load();
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(tblViewer.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.showAndWait();
		setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
	}

	private void setDataModel(String category, String search) {
		ObservableList<UserRole> userRoles = FXCollections.observableArrayList(ManageUserRole.getInstance().listUserRole(category, search));
		tblViewer.getItems().removeAll(userRoles);
		this.tblViewer.setItems(FXCollections.observableArrayList(userRoles));
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}
}
