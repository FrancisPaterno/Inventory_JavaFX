package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.UserRole;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageUserRole {

	private static volatile ManageUserRole INSTANCE;
	private static final Logger logger = LogManager.getLogger(ManageUserRole.class);
	
	public static ManageUserRole getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageUserRole.class) {
				if(INSTANCE == null) {
					logger.info("Instantiating " + ManageUserRole.class + ".");
					INSTANCE = new ManageUserRole();
				}
			}
		}
		logger.debug(ManageUserRole.class + " : " + INSTANCE);
		return INSTANCE;
	}
	
	public Boolean addUserRole(String role, String description) {
		logger.info("Adding " + role + ".");
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			UserRole usrRole = new UserRole();
			usrRole.setRole(role);
			usrRole.setDescription(description == ""?null:description);
			session.save(usrRole);
			tx.commit();
			logger.info(role + " has been added.");
			logger.debug("Role : " + usrRole);
			return true;
		} catch (Exception e) {
			logger.error("Error adding " + role + ".", e);
			if(tx!= null) tx.rollback();
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
			if(usrRole == null) {
				logger.warn(oldrole + " does not exist.");
			}
			else {
				logger.info("Updating " + oldrole);
			}
			usrRole.setRole(newrole);
			usrRole.setDescription(description == ""?null:description);
			session.update(usrRole);
			tx.commit();
			logger.info(newrole + " has been saved.");
			logger.debug("Role : " + usrRole);
			return true;
		} catch (Exception e) {
			logger.error("Error saving role.", e);
			if(tx!= null) tx.rollback();
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
			if(usrRole == null) {
				logger.warn(role + " does not exist.");
			}
			else {
				logger.info("Deleting "+ role + ".");
			}
			session.delete(usrRole);
			tx.commit();
			logger.info(role + " has been deleted.");
			logger.debug("Role : " + usrRole);
			return true;
		} catch (Exception e) {
			logger.error("Error deleting " + role + ".",e);
			if(tx!= null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}
	
	public List<UserRole> listUserRole(String category, String search){
		logger.info("Retrieving user roles.");
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
			logger.error("Error retrieving user roles.");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
		}
		logger.debug("Roles : " + userroles);
		return userroles;
	}
}
