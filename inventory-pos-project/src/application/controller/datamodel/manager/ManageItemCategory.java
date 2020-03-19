package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.ItemCategory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItemCategory {
	private static volatile ManageItemCategory INSTANCE;

	public static ManageItemCategory getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageItemCategory.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageItemCategory();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addItemCategory(String category, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null; 
		try {
			tx= session.beginTransaction();
			ItemCategory itmCat = new ItemCategory();
			itmCat.setItemCategory(category);
			itmCat.setDescription(description.trim().isEmpty()?null:description.trim());
			session.save(itmCat);
			tx.commit();
			return true;
		} catch (HibernateException e) {
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

	public Boolean updateItemCategory(String oldCategory, String newcategory, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ItemCategory itmCat = session.get(ItemCategory.class, oldCategory);
			itmCat.setItemCategory(newcategory);
			itmCat.setDescription(description);
			session.update(itmCat);
			tx.commit();
			return true;
		}
		catch(HibernateException e) {
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

	public Boolean deleteItemCategory(String category) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx= session.beginTransaction();
			ItemCategory itmCat = session.get(ItemCategory.class, category);
			session.delete(itmCat);
			tx.commit();
			return true;
		}
		catch(HibernateException e)
		{
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
		}
		catch(HibernateException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
		}
		return itemCats;
	}
}
