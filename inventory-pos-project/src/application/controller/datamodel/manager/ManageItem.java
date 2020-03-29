package application.controller.datamodel.manager;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.Item;
import application.datamodel.ItemBrand;
import application.datamodel.ItemCategory;
import application.datamodel.ItemImage;
import application.datamodel.ItemUnit;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItem {

	private static volatile ManageItem INSTANCE;
	private final static Logger logger = LogManager.getLogger(ManageItem.class);
	public static ManageItem getInstance() {
		if(INSTANCE == null) {
			logger.info("Instantiating " + ManageItem.class);
			synchronized (ManageItemBrand.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageItem();
				}
			}
			logger.debug(INSTANCE);
			logger.info(ManageItem.class + " has been instantiated.");
		}
		return INSTANCE;
	}

	public Boolean addItem(String Id, String name, String description, ItemUnit unit, ItemCategory category, ItemBrand brand, File file) {
		logger.info("Adding item : " + name + ".");
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Item item = new Item();
			item.setId(Id);
			item.setName(name);
			item.setDescription(description);
			item.setUnit(unit);
			item.setCategory(category);
			item.setBrand(brand);

			if(file != null) {
				logger.info("Saving image if selected.");
				FileInputStream input = new FileInputStream(file);
				Blob photoblob = Hibernate.getLobCreator(session).createBlob(input, file.length());
				ItemImage itemImage = new ItemImage();
				itemImage.setImage(photoblob);
				item.setItemImage(itemImage);
				itemImage.setItem(item);
				logger.info("Item Image created.");
				logger.debug("ItemImage : " + itemImage);
			}
			logger.debug("Item created : " + item, item);
			session.save(item);
			tx.commit();
			logger.info("Item saved.");
			return true;
		} catch (Exception e) {
			logger.error("Error saving item.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean updateItem(String olId, String Id, String name, String description, ItemUnit unit, ItemCategory category, ItemBrand brand, Blob blob) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Item item = session.get(Item.class, olId);
			if(item == null) {
				logger.warn("Item : " + olId + " does not exist.");
			}
			else {
				logger.info("Updating item : " + item.getName() + ".");
			}
			item.setId(Id);
			item.setName(name);
			item.setDescription(description);
			item.setUnit(unit);
			item.setCategory(category);
			item.setBrand(brand);
			if(blob != null) {
				if(item.getItemImage() != null) {
					item.getItemImage().setImage(blob);
				}
				else {
					logger.info("Saving image if selected.");
					ItemImage itemImage = new ItemImage();
					itemImage.setImage(blob);
					item.setItemImage(itemImage);
					itemImage.setItem(item);
				}
			}
			else {
				if(item.getItemImage() != null) {
					session.delete(item.getItemImage());
				}
				item.setItemImage(null);
			}
			session.update(item);
			tx.commit();
			logger.info("Item successfully updated.");
			return true;
		} catch (Exception e) {
			logger.error("Error updating item.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean deleteItem(String Id) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Item item = session.get(Item.class, Id);
			if(item == null) {
				logger.warn("Item with id : " + Id + " does not exist");
			}
			else {
				logger.info("Deleting item : " + item.getName() + ".");
			}
			session.delete(item);
			tx.commit();
			logger.info("Item : " + item.getName() + " has been deleted.");
			return true;
		} catch (Exception e) {
			logger.error("Error deleting item.", e);
			if(tx != null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public List<Item> listItem(String category, String search){
		logger.info("Searching for item : " + search + " with " + category + " category.");
		String value = "%" + search + "%";
		List<Item> items = null;
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
		Root<Item> pItem = criteria.from(Item.class);
		Predicate OR = null;
		Predicate name = cb.like(pItem.get("Name"), value);
		Predicate description = cb.like(pItem.get("Description"), value);
		Predicate unit = cb.like(pItem.get("Unit").get("Unit"), value);
		Predicate pcategory = cb.like(pItem.get("Category").get("ItemCategory"), value);
		Predicate brand = cb.like(pItem.get("Brand").get("Brandname"), value);

		logger.debug("Predicate name : " + name);
		logger.debug("Predicate description : " + description);
		logger.debug("Predicate unit : " + unit);
		logger.debug("Predicate itemcategory : " + pcategory);
		logger.debug("Predicate brand : " + brand);

		switch (category) {
		case Item.ALL:
			OR = cb.or(name, description, unit, pcategory, brand);
			break;
		case Item.ITEM_ID:
			if(!search.trim().isEmpty()) {
				OR = cb.equal(pItem.get("Id"), search);
			}
			break;
		case Item.ITEM_NAME:
			OR = cb.like(pItem.get("Name"), value);
			break;
		case Item.ITEM_DESCRIPTION:
			OR = description;
			break;
		case Item.ITEM_UNIT:
			OR = unit;
			break;
		case Item.ITEM_CATEGORY:
			OR = pcategory;
			break;
		case Item.ITEM_BRAND:
			OR = brand;
			break;
		default:
			OR = cb.or(name, description, unit, pcategory, brand);
			break;
		}
		logger.debug("Predicate OR : " + OR);
		criteria.where(OR);
		items = session.createQuery(criteria).getResultList();
		logger.debug("Items : " + items);
		return items;
	}
}
