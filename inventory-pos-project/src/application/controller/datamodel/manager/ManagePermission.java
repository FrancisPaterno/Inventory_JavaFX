package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.Permissions;
import application.datamodel.RolePermission;
import application.datamodel.UserRole;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class ManagePermission {

	private static volatile ManagePermission INSTANCE;

	public static ManagePermission getInstance() {
		if(INSTANCE == null) {
			synchronized (ManagePermission.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManagePermission();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean saveOrUpdatePermissions(TableView<Permissions> tblView, UserRole userRole) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			for(Permissions permission : tblView.getItems()) {
				CheckBox chkBox = (CheckBox) tblView.lookup("#" +permission.getPermissionName());
				Boolean checked = chkBox.isSelected();

				RolePermission rolePermission = ManageRolePermission.getInstance().getRolePermissionByRoleAndPermission(userRole, permission);
				if(rolePermission == null) {
					rolePermission = new RolePermission();
				}

				rolePermission.setUserrole(userRole);
				rolePermission.setPermission(permission);
				rolePermission.setIsAllowed(checked);
				session.saveOrUpdate(rolePermission);
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;		
		}
	}

	public List<Permissions> listPermissions(){
		Session session = SessionManager.getSession();
		List<Permissions> permissions = null;
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Permissions> criteria = cb.createQuery(Permissions.class);
		Root<Permissions> pPermission = criteria.from(Permissions.class);
		criteria.select(pPermission);
		permissions = session.createQuery(criteria).getResultList();
		return permissions;
	}

}
