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

import application.datamodel.Supplier;
import application.utility.DataValidator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManageSupplier {

	private static volatile ManageSupplier INSTANCE;
	private final static Logger logger = LogManager.getLogger(ManageSupplier.class);

	public static ManageSupplier getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageSupplier.class) {
				if(INSTANCE == null) {
					logger.info("Instantiating " + ManageSupplier.class + ".");
					INSTANCE = new ManageSupplier();
				}
			}
		}
		return INSTANCE;
	}

	public Boolean addSupplier(String suppliername, String BRN, String address, String telephone, String telefax, String email, String mobile) {
		Session session = SessionManager.getSession();
		Transaction tx = null;

		try {
			logger.info("Adding Supplier : "+ suppliername + ".");
			tx = session.beginTransaction();
			Supplier supplier = new Supplier();
			supplier.setSupplierName(suppliername);
			supplier.setBRN(BRN);
			supplier.setAddress(address);
			supplier.setTelephone(telephone);
			supplier.setTelefax(telefax.trim().isEmpty()?null:telefax);
			supplier.setEmail(email.trim().isEmpty()?null:email);
			supplier.setMobile(mobile.trim().isEmpty()?null:mobile);
			session.save(supplier);
			tx.commit();
			logger.info(suppliername + " has been added.");
			return true;
		} catch (Exception e) {
			logger.error("Error adding supplier.", e);
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


	public Boolean updateSupplier(Integer oldId, String supplierName, String BRN, String address, String telephone, String telefax, String email, String mobile) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			Supplier supplier = session.get(Supplier.class, oldId);
			if(supplier == null) {
				logger.warn(supplierName + " does not exist.");
			}
			else {
				logger.info("Updating " + supplierName + ".");
			}
			supplier.setSupplierName(supplierName);
			supplier.setBRN(BRN);
			supplier.setAddress(address);
			supplier.setTelephone(telephone);
			supplier.setTelefax(telefax == ""?null:telefax);
			supplier.setEmail(email == ""?null:email);
			supplier.setMobile(mobile == ""?null:mobile);
			session.update(supplier);
			tx.commit();
			logger.info(supplierName + " supplier has been saved.");
			logger.debug("Supplier : " + supplier);
			return true;
		} catch (Exception e) {
			logger.error("Error saving supplier.", e);
			if(tx!= null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}

	public Boolean deleteSupplier(Integer supplierId) {
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx= session.beginTransaction();
			Supplier supplier = session.get(Supplier.class, supplierId);
			if(supplier == null) {
				logger.warn("Supplier " + supplierId + " does not exist.");
			}
			else {
				logger.info("Updating " + supplier.getSupplierName() + ".");
			}
			session.delete(supplier);
			tx.commit();
			logger.info(supplier.getSupplierName() + " has been saved.");
			logger.debug("Supplier : " + supplier);
			return true;
		} catch (Exception e) {
			logger.error("Error saving supplier.", e);
			if(tx!= null) tx.rollback();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.initOwner(null);
			alert.setContentText(e.getMessage());
			alert.show();
			return false;
		}
	}
	
	public List<Supplier> listSupplier(String categorySearch, String search){
		logger.info("Retrieving suppiers.");
		List<Supplier> suppliers = null;
		String value = "%" + search + "%";
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteria = cb.createQuery(Supplier.class);
		Root<Supplier> pSupplier = criteria.from(Supplier.class);

		Predicate OR = null;

		Predicate supplierName = cb.like(pSupplier.get("SupplierName"), value);
		Predicate brn = cb.like(pSupplier.get("BRN"), value);
		Predicate address = cb.like(pSupplier.get("Address"), value);
		Predicate telephone = cb.like(pSupplier.get("Telephone"), value);
		Predicate telefax = cb.like(pSupplier.get("Telefax"), value);
		Predicate email = cb.like(pSupplier.get("Email"), value);
		Predicate mobile = cb.like(pSupplier.get("Mobile"), value);

		switch (categorySearch) {
		case Supplier.ALL:
			OR = cb.or(supplierName, brn, address, telephone, telefax, email, mobile);
			break;
		case Supplier.SUPPLIER_ID:
			if(!search.trim().isEmpty()) {
				if(DataValidator.isInteger(search)) {
					Predicate supplierId = cb.equal(pSupplier.get("SupplierId"), search);
					OR = cb.or(supplierId);
				}
			}
			break;
		case Supplier.SUPPLIER_NAME:
			OR = cb.or(supplierName);
			break;
		case Supplier.BUSINESS_REGISTRATION_NUMBER:
			OR = cb.or(brn);
			break;
		case Supplier.ADDRESS:
			OR = cb.or(address);
			break;
		case Supplier.TELEPHONE:
			OR = cb.or(telephone);
			break;
		case Supplier.TELEFAX:
			OR = cb.or(telefax);
			break;
		case Supplier.EMAIL:
			OR = cb.or(email);
			break;
		case Supplier.MOBILE:
			OR = cb.or(mobile);
			break;
		default:
			OR = cb.or(supplierName, brn, address, telephone, telefax, email, mobile);
			break;
		}
		criteria.where(OR);
		suppliers = session.createQuery(criteria).getResultList();
		logger.debug("Suppliers : " + suppliers);
		return suppliers;
	}
}
