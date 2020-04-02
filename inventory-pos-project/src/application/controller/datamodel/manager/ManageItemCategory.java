package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.ItemCategory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItemCategory {
	private static volatile ManageItemCategory INSTANCE;
	private final static Logger logger = LogManager.getLogger(ManageItemCategory.class);

	public static ManageItemCategory getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageItemCategory.class) {
				if(INSTANCE == null) {
					logger.info("Initializing " + ManageItemCategory.class);
					INSTANCE = new ManageItemCategory();
					logger.info(ManageItemCategory.class + " has been instantiated.");
				}
			}
		}
		logger.debug(INSTANCE);
		return INSTANCE;
	}

	public Boolean addItemCategory(String category, String description) {
		logger.info("Adding item category " + category + ".");
		Session session = SessionManager.getSession();
		Transaction tx = null; 
		try {
			tx= session.beginTransaction();
			ItemCategory itmCat = new ItemCategory();
			itmCat.setItemCategory(category);
			itmCat.setDescription(description.trim().isEmpty()?null:description.trim());
			session.save(itmCat);
			tx.commit();
			logger.info("Item category " + itmCat + " has been saved.");
			logger.debug("Item Category : " + itmCat);
			return true;
		} catch (HibernateException e) {
			logger.error("Error saving item category.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean updateItemCategory(String oldCategory, String newcategory, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ItemCategory itmCat = session.get(ItemCategory.class, oldCategory);
			if(itmCat == null) {
				logger.warn(oldCategory + " does not exist.");
			}
			else {
				logger.info("Updating " + itmCat.getItemCategory() + ".");
			}
			itmCat.setItemCategory(newcategory);
			itmCat.setDescription(description);
			session.update(itmCat);
			tx.commit();
			logger.info(itmCat.getItemCategory() + " has been updated.");
			return true;
		}
		catch(HibernateException e) {
			logger.error("Error updating item category.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean deleteItemCategory(String category) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx= session.beginTransaction();
			ItemCategory itmCat = session.get(ItemCategory.class, category);
			if(itmCat == null) {
				logger.warn(category + " does not exist.");
			}
			else {
				logger.info("Updating " + itmCat.getItemCategory() + ".");
			}
			session.delete(itmCat);
			tx.commit();
			logger.info(itmCat.getItemCategory() + " has been deleted.");
			return true;
		}
		catch(HibernateException e)
		{
			if(tx!= null) tx.rollback();
			logger.error("Error deleting item category.", e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public List<ItemCategory> listItemCategory(String searchCategory, String search){
		String value = "%" + search + "%";
		List<ItemCategory> itemCats = null;
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<ItemCategory> criteria = cb.createQuery(ItemCategory.class);
		Root<ItemCategory> pItemCat = criteria.from(ItemCategory.class);

		try {
			Predicate category = cb.like(pItemCat.get("ItemCategory"), value);
			Predicate description = cb.like(pItemCat.get("Description"), value);
			Predicate OR;
			switch (searchCategory) {
			case ItemCategory.ALL:
				OR = cb.or(category, description);
				break;
			case ItemCategory.CATEGORY:
				OR = cb.or(category);
				break;
			case ItemCategory.DESCRIPTION:
				OR = cb.or(description);
				break;
			default:
				OR = cb.or(category, description);
				break;
			}

			criteria.where(OR);
			itemCats = session.createQuery(criteria).getResultList();
			logger.debug("Item category list : " + itemCats);
		}
		catch(HibernateException e) {
			logger.error("Error retrieving item category.", e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
		}
		return itemCats;
	}
}
