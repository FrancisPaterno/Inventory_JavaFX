package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.datamodel.ItemBrand;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageItemBrand {

	private static volatile ManageItemBrand INSTANCE;

	public static ManageItemBrand getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageItemBrand.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageItemBrand();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addItemBrand(String brandname, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		try {
			tx= session.beginTransaction();
			ItemBrand itemBrand = new ItemBrand();
			itemBrand.setBrandname(brandname);
			itemBrand.setDescription(description.trim().isEmpty()?null:description.trim());
			session.save(itemBrand);
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

	public Boolean updateItemBrand(String oldBrand, String newBrand, String description) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ItemBrand itemBrand = session.get(ItemBrand.class, oldBrand);
			itemBrand.setBrandname(newBrand);
			itemBrand.setDescription(description);
			session.update(itemBrand);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	public Boolean deleteItemBrand(String brand) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			tx= session.beginTransaction();
			ItemBrand itemBrand = session.get(ItemBrand.class, brand);
			session.delete(itemBrand);
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


	public List<ItemBrand> listItemBrands(String searchCategory, String search){
		String value = "%" + search + "%"; 
		List<ItemBrand> itemCats = null; 
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder(); 
		CriteriaQuery<ItemBrand> criteria =cb.createQuery(ItemBrand.class); 
		Root<ItemBrand> pItemBrand = criteria.from(ItemBrand.class);

		try { 
			Predicate category = cb.like(pItemBrand.get("Brandname"), value); 
			Predicate description = cb.like(pItemBrand.get("Description"), value); 
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
			criteria.where(OR); 
			itemCats = session.createQuery(criteria).getResultList();
		} catch(HibernateException e) 
		{ 
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
