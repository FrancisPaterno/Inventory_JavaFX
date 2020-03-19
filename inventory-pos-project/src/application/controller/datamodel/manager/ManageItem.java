package application.controller.datamodel.manager;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.Item;
import application.datamodel.ItemBrand;
import application.datamodel.ItemCategory;
import application.datamodel.ItemImage;
import application.datamodel.ItemUnit;
import application.utility.DataValidator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItem {

	private static volatile ManageItem INSTANCE;

	public static ManageItem getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageItemBrand.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageItem();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addItem(String Id, String name, String description, ItemUnit unit, ItemCategory category, ItemBrand brand, File file) {
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
				FileInputStream input = new FileInputStream(file);
				Blob photoblob = Hibernate.getLobCreator(session).createBlob(input, file.length());
				ItemImage itemImage = new ItemImage();
				itemImage.setImage(photoblob);
				item.setItemImage(itemImage);
				itemImage.setItem(item);
			}
			session.save(item);
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

	public Boolean updateItem(String olId, String Id, String name, String description, ItemUnit unit, ItemCategory category, ItemBrand brand, Blob blob) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Item item = session.get(Item.class, olId);
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

	public Boolean deleteItem(String Id) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Item item = session.get(Item.class, Id);
			session.delete(item);
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

	public List<Item> listItem(String category, String search){
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

		criteria.where(OR);
		items = session.createQuery(criteria).getResultList();
		return items;
	}
}
