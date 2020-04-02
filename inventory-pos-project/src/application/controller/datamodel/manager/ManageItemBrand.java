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

import application.datamodel.ItemBrand;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItemBrand {

	private static volatile ManageItemBrand INSTANCE;
	private final static Logger logger = LogManager.getLogger(ManageItemBrand.class);

	public static ManageItemBrand getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageItemBrand.class) {
				if(INSTANCE == null) {
					logger.info("Instantiating " + ManageItemBrand.class);
					INSTANCE = new ManageItemBrand();
					logger.info(ManageItemBrand.class + " has been instantiated.");
					logger.debug(INSTANCE);
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addItemBrand(String brandname, String description) {
		logger.info("Adding item brand : " + brandname + ".");
		Session session = SessionManager.getSession();
		Transaction tx = null;
		try {
			tx= session.beginTransaction();
			ItemBrand itemBrand = new ItemBrand();
			itemBrand.setBrandname(brandname);
			itemBrand.setDescription(description.trim().isEmpty()?null:description.trim());
			session.save(itemBrand);
			tx.commit();
			logger.info("Item brand : " + brandname + " has been saved.");
			logger.debug("Item brand : " + itemBrand);
			return true;
		} catch (HibernateException e) {
			logger.error("Error adding item brand.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean updateItemBrand(String oldBrand, String newBrand, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ItemBrand itemBrand = session.get(ItemBrand.class, oldBrand);
			if(itemBrand == null) {
				logger.warn("Item brand " + oldBrand + " does not exist.");
			}
			else {
				logger.info("Updating item brand " + itemBrand);
			}
			itemBrand.setBrandname(newBrand);
			itemBrand.setDescription(description);
			session.update(itemBrand);
			tx.commit();
			logger.info("Item brand" + itemBrand + " has been updated.");
			logger.debug(itemBrand);
			return true;
		} catch (Exception e) {
			logger.error("Error updating item brand ", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean deleteItemBrand(String brand) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx= session.beginTransaction();
			ItemBrand itemBrand = session.get(ItemBrand.class, brand);
			if(itemBrand == null) {
				logger.warn("Item " + brand + " does not exist.");
			}
			else {
				logger.info("Deleting item brand " + itemBrand.getBrandname() + ".");
			}
			session.delete(itemBrand);
			tx.commit();
			logger.info("Item brand " + itemBrand + " has been saved.");
			return true;
		}
		catch(HibernateException e)
		{
			logger.error("Error saving item brand.", e);
			if(tx!= null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}


	public List<ItemBrand> listItemBrands(String searchCategory, String search){
		logger.info("Searching for item brand " + search + " with " + searchCategory + " category.");
		String value = "%" + search + "%"; 
		List<ItemBrand> itemCats = null; 
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder(); 
		CriteriaQuery<ItemBrand> criteria =cb.createQuery(ItemBrand.class); 
		Root<ItemBrand> pItemBrand = criteria.from(ItemBrand.class);

		try { 
			Predicate category = cb.like(pItemBrand.get("Brandname"), value); 
			Predicate description = cb.like(pItemBrand.get("Description"), value); 
			logger.debug("Predicate category : " + category);
			logger.debug("Predicate description : " + description);
			Predicate OR; 
			switch(searchCategory) { 
			case ItemBrand.ALL: 
				OR = cb.or(category,description); 
				break; 
			case ItemBrand.BRAND: 
				OR =cb.or(category); 
				break; 
			case ItemBrand.DESCRIPTION: 
				OR =cb.or(description); 
				break; 
			default: 
				OR = cb.or(category, description); 
				break;
			}
			logger.debug("Predicate OR : " + OR);
			criteria.where(OR); 
			itemCats = session.createQuery(criteria).getResultList();
			logger.debug("Item categories : " + itemCats);
		} catch(HibernateException e) {
			logger.error("Error fetching item brands.", e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
		} 
		return itemCats;
	}
}
