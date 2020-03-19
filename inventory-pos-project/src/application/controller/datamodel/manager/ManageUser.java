package application.controller.datamodel.manager;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.User;
import application.datamodel.UserRole;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageUser {

	public static volatile ManageUser INSTANCE;
	private User currentUser;
	
	public static ManageUser getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageUser.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageUser();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addUser(String username, String email,String password, Date created, UserRole role) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		String sha1Pass = DigestUtils.sha1Hex(password);
		try {
			tx = session.beginTransaction();
			User user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(sha1Pass);
			user.setCreated(created);
			user.setUserrole(role);
			session.save(user);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	public Boolean updateUser(Integer oldId, String username, String email, String password, Date lastupdate, UserRole role) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		String sha1Pass = DigestUtils.sha1Hex(password);
		try {
			tx = session.beginTransaction();
			User user = session.get(User.class, oldId);
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(sha1Pass);
			user.setLastUpdate(lastupdate);
			user.setUserrole(role);
			session.update(user);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	public Boolean deleteUser(Integer id) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			User user = session.get(User.class, id);
			session.delete(user);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	public List<User> listUsers(String category, String search){
		String value = "%" + search + "%";
		List<User> users = null;
		Session session  = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> pUser = criteria.from(User.class);

		Predicate OR = null;
		Predicate id = cb.equal(pUser.get("Id"), 0);
		if(!search.isEmpty()) {
			try {
				Integer xid = Integer.parseInt(search);
				id = cb.equal(pUser.get("Id"), xid);		
			} catch (NumberFormatException e) {
				id = cb.equal(pUser.get("Id"), 0);
				e.printStackTrace();
			}
		}
		
		Predicate username = cb.like(pUser.get("Username"), value);
		Predicate email = cb.like(pUser.get("Email"), value);
		Predicate role = cb.like(pUser.get("userrole").get("Role"), value);
		switch (category) {
		case User.ALL:
			OR = cb.or(username, email, role);
			break;
		case User.ID:
			OR = id;
			break;
		case User.USERNAME:
			OR = username;
			break;
		case User.EMAIL:
			OR = email;
			break;
		case User.ROLE:
			OR = role;
			break;
		default:
			OR = cb.or(username, email, role);
			break;
		}
		criteria.where(OR);
		users = session.createQuery(criteria).getResultList();
		return users;
	}
	
	public User getUserbyUsername(String username) {
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> pUser = criteria.from(User.class);
		Predicate pusername = cb.equal(pUser.get("Username"), username);
		criteria.where(pusername);
		User user = session.createQuery(criteria).uniqueResult();
		return user;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
