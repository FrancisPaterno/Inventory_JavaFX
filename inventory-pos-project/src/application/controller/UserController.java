package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Constants.PermissionConstants;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.User;
import application.datamodel.UserRole;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class UserController implements Initializable{

	@FXML
	private JFXButton btnAddNew;
	@FXML
	private TableView<User> tblViewer;
	@FXML
	private Label lblRecordCount;
	@FXML
	private ComboBox<String> cmbCategorySearch;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;

	private TableColumn<User, Integer> colId;
	private TableColumn<User, String> colUsername;
	private TableColumn<User, String> colEmail;
	private TableColumn<User, Date> colCreated;
	private TableColumn<User, Date> colLastUpdate;
	private TableColumn<User, UserRole> colRole;
	private TableColumn<User, String> colEdit;
	private TableColumn<User, String> colDelete;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmbCategorySearch.getItems().addAll(User.ALL, User.ID, User.USERNAME, User.EMAIL, User.ROLE);
		cmbCategorySearch.getSelectionModel().select(User.ALL);
		colId = new TableColumn<User, Integer>("ID");
		colId.setCellValueFactory(new PropertyValueFactory<User, Integer>("Id"));
		colUsername = new TableColumn<User, String>("Username");
		colUsername.setCellValueFactory(new PropertyValueFactory<User, String>("Username"));
		colEmail = new TableColumn<User, String>("Email");
		colEmail.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
		colCreated = new TableColumn<User, Date>("Created");
		colCreated.setCellValueFactory(new PropertyValueFactory<User, Date>("Created"));
		colLastUpdate = new TableColumn<User, Date>("Last Update");
		colLastUpdate.setCellValueFactory(new PropertyValueFactory<User, Date>("LastUpdate"));
		colRole = new TableColumn<User, UserRole>("Role");
		colRole.setCellValueFactory(new PropertyValueFactory<User, UserRole>("userrole"));
		colEdit = new TableColumn<User, String>("Edit");
		colEdit.setCellValueFactory(new PropertyValueFactory<User, String>("DUMMY"));
		colDelete = new TableColumn<User, String>("Delete");
		colDelete.setCellValueFactory(new PropertyValueFactory<User, String>("DUMMY"));
		Callback<TableColumn<User, String>, TableCell<User, String>> cellEditFactory = 
				new Callback<TableColumn<User,String>, TableCell<User,String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> param) {
				final TableCell<User, String> cell = new TableCell<User, String>(){
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
									User user = tblViewer.getItems().get(getIndex());
									showEditModal(user);	
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
		Callback<TableColumn<User, String>, TableCell<User, String>> cellDeleteFactory = 
				new Callback<TableColumn<User,String>, TableCell<User,String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> param) {
				final TableCell<User, String> cell = new TableCell<User, String>(){

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
									User user = tblViewer.getItems().get(getIndex());
									if(user.getUsername().equals(ManageUser.getInstance().getCurrentUser().getUsername())) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Current User");
										alert.setContentText("Current user cannot be deleted.");
										alert.initOwner(btn.getScene().getWindow());
										alert.show();
									}
									else {
										Alert alert = new Alert(AlertType.CONFIRMATION);
										alert.setTitle("Confirm Delete");
										alert.setContentText("Delete selected item(s)?");
										alert.initOwner(btn.getScene().getWindow());
										Optional<ButtonType> result = alert.showAndWait();
										if(result.isPresent() && result.get() == ButtonType.OK) {
											Boolean deleted = ManageUser.getInstance().deleteUser(user.getId());
											if(deleted) {
												setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
											}
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
		tblViewer.getColumns().addAll(colId, colUsername, colEmail, colCreated, colLastUpdate, colRole, colEdit, colDelete);

		this.btnAddNew.setOnAction(event->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.ADD)) {
				showModal();	
			}
		});

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
			}
		});

		txtSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});

		btnSearch.setOnAction(event->{
			setDataModel(cmbCategorySearch.getSelectionModel().getSelectedItem(), txtSearch.getText().trim());
		});
	}

	private void showModal() {
		FXMLLoader fxmlUserInterface = new FXMLLoader(getClass().getResource("/application/views/UserInterfaceView.fxml"));
		UserInterfaceController usrInt = new UserInterfaceController();
		usrInt.setIsEdit(false);
		fxmlUserInterface.setController(usrInt);
		try {
			BorderPane root = fxmlUserInterface.load();
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

	private void showEditModal(User user) {
		FXMLLoader fxmlUserInterface = new FXMLLoader(getClass().getResource("/application/views/UserInterfaceView.fxml"));
		UserInterfaceController usrInt = new UserInterfaceController();
		usrInt.setIsEdit(true);
		fxmlUserInterface.setController(usrInt);
		try {
			BorderPane root = fxmlUserInterface.load();
			usrInt.setOldId(user.getId());
			usrInt.getTxtUsername().setText(user.getUsername());
			usrInt.getTxtEmail().setText(user.getEmail());
			usrInt.getCmbRole().getSelectionModel().select(user.getUserrole());
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

	private void setDataModel(String category,String search) {
		ObservableList<User> users = FXCollections.observableArrayList(ManageUser.getInstance().listUsers(category, search));
		tblViewer.getItems().removeAll(users);
		tblViewer.setItems(users);
		lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}
}
