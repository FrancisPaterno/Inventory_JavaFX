package application.Constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private final static Logger logger = LogManager.getLogger(PermissionConstants.class);

	public static Boolean isPermitted(UserRole userRole, String action) {
		Boolean ress = false;
		logger.info("Getting permission for User Role: " + userRole.getRole() + ", action : " + action + ".");
		logger.debug("UserRole : " + userRole + ", action : " + action);
		for(RolePermission rolePermission : userRole.getRolePermissions()) {
			if(rolePermission.getUserrole().getRole().equals(userRole.getRole())) {
				logger.debug("UserRole : " + userRole);
				if(rolePermission.getPermission().getPermissionName().equals(action)) {
					ress = rolePermission.getIsAllowed();
					logger.debug(ress);
				}
			}
		}
		
		if(ress == false) {
			logger.info("UserRole : " + userRole.getRole() + " is " + ress + " for " + action + ".");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Access Violation");
			alert.setContentText("You do not have enough priveledge to perform this action");
			alert.show();
		}
		return ress;
	}
}
