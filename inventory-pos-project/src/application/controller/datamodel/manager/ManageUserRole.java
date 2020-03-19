package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.UserRole;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageUserRole {

	private static volatile ManageUserRole INSTANCE;
	
	public static ManageUserRole getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageUserRole.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageUserRole();
				}
			}
		}
		return INSTANCE;
	}
	
	public Boolean addUserRole(String role, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			UserRole usrRole = new UserRole();
			usrRole.setRole(role);
			usrRole.setDescription(description == ""?null:description);
			session.save(usrRole);
			tx.commit();
			return true;
		} catch (Exception e) {
			if(tx!= null) tx.rollback();
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}
	
	public Boolean updateUserRole(String oldrole, String newrole, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			UserRole usrRole = session.get(UserRole.class, oldrole);
			usrRole.setRole(newrole);
			usrRole.setDescription(description == ""?null:description);
			session.update(usrRole);
			tx.commit();
			return true;
		} catch (Exception e) {
			if(tx!= null) tx.rollback();
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}
	
	public Boolean deleteUserRole(String role) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			UserRole usrRole = session.get(UserRole.class, role);
			session.delete(usrRole);
			tx.commit();
			return true;
		} catch (Exception e) {
			if(tx!= null) tx.rollback();
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}
	
	public List<UserRole> listUserRole(String category, String search){
		List<UserRole> userroles = null;
		String value = "%" + search + "%";
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserRole> criteria = cb.createQuery(UserRole.class);
		Root<UserRole> pUserRole = criteria.from(UserRole.class);
		Predicate OR = null;
		Predicate role = cb.like(pUserRole.get("Role"), value);
		Predicate desc = cb.like(pUserRole.get("Description"), value);
		
		switch (category) {
		case UserRole.ALL:
			OR = cb.or(role, desc);
			break;
		case UserRole.ROLE:
			OR = role;
			break;
		case UserRole.DESCRIPTION:
			OR = desc;
			break;
		default:
			OR = cb.or(role, desc);
			break;
		}
		criteria.where(OR);
		try {
			userroles = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
		}
		return userroles;
	}
}
