package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import application.Constants.PermissionConstants;
import application.Constants.UserRoleConstants;
import application.controller.datamodel.manager.ManagePermission;
import application.controller.datamodel.manager.ManageUser;
import application.datamodel.Permissions;
import application.datamodel.RolePermission;
import application.datamodel.UserRole;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PermissionController implements Initializable{

	@FXML
	private TableView<Permissions> tblView;
	@FXML
	private JFXButton btnOk;
	@FXML
	private JFXButton btnCancel;

	private UserRole userRole;

	private TableColumn<Permissions, String> colPermission;
	private TableColumn<Permissions, Boolean> colAllowed;
	private Stage stage;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colPermission = new TableColumn<Permissions, String>("Permission");
		colPermission.setCellValueFactory(new PropertyValueFactory<Permissions, String>("PermissionName"));

		Callback<TableColumn<Permissions, Boolean>, TableCell<Permissions, Boolean>> cellPermissionFactory = 
				new Callback<TableColumn<Permissions,Boolean>, TableCell<Permissions,Boolean>>() {

			@Override
			public TableCell<Permissions, Boolean> call(TableColumn<Permissions, Boolean> param) {
				final TableCell<Permissions, Boolean> cell = new TableCell<Permissions, Boolean>(){
					//FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WRENCH);
					final JFXCheckBox chkBox = new JFXCheckBox();

					@Override
					public void updateItem(Boolean item, boolean empty) {
						//icon.setSize("25");
						//chkBox.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {

							Permissions permission = tblView.getItems().get(getIndex());
							chkBox.setId(permission.getPermissionName());

							for(RolePermission rolePermission : permission.getPermissions()) {
								if(rolePermission.getUserrole().getRole().equals(userRole.getRole())) {
									chkBox.setSelected(rolePermission.getIsAllowed());
								}
							}
							setGraphic(chkBox);
							setAlignment(Pos.CENTER);
							setText(null);
						}
					}
				};
				return cell;
			}
		};
		colAllowed = new TableColumn<Permissions, Boolean>("Allow");
		colAllowed.setCellValueFactory(new PropertyValueFactory<Permissions, Boolean>("DUMMY"));
		colAllowed.setCellFactory(cellPermissionFactory);

		tblView.getColumns().addAll(colPermission, colAllowed);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage = (Stage) btnCancel.getScene().getWindow();
				setDataModel();
			}
		});

		if(userRole.getRole().equals(UserRoleConstants.ADMINISTRATOR)) {
			btnOk.setDisable(true);
		}
		
		btnCancel.setOnAction(event->{
			stage.close();
		});

		btnOk.setOnAction(event->{
			if(PermissionConstants.isPermitted(ManageUser.getInstance().getCurrentUser().getUserrole(), PermissionConstants.PERMISSION_ALTER)) {
				if(ManagePermission.getInstance().saveOrUpdatePermissions(tblView, userRole)) {
					stage.close();
				}	
			}
		});
	}

	private void setDataModel() {
		ObservableList<Permissions> permissions = FXCollections.observableArrayList(ManagePermission.getInstance().listPermissions());
		tblView.getItems().removeAll(permissions);
		tblView.setItems(permissions);
		//lblRecordCount.setText("Record(s) : " + String.valueOf(tblViewer.getItems().size()));
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
}
