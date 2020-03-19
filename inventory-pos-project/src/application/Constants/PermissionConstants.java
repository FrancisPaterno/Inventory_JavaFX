package application.Constants;

import application.datamodel.RolePermission;
import application.datamodel.UserRole;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PermissionConstants {

	public final static String ADD = "ADD";
	public final static String EDIT = "EDIT";
	public final static String DELETE = "DELETE";
	public final static String REPORTS = "REPORTS";
	public final static String PERMISSION_VIEW = "PERMISSION_VIEW";
	public final static String PERMISSION_ALTER = "PERMISSION_ALTER";
	
	public static Boolean isPermitted(UserRole userRole, String action) {
		Boolean ress = false;
		for(RolePermission rolePermission : userRole.getRolePermissions()) {
			if(rolePermission.getUserrole().getRole().equals(userRole.getRole())) {
				if(rolePermission.getPermission().getPermissionName().equals(action)) {
					ress = rolePermission.getIsAllowed();
				}
			}
		}
		
		if(ress == false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Access Violation");
			alert.setContentText("You do not have enough priveledge to perform this action");
			alert.show();
		}
		return ress;
	}
}
