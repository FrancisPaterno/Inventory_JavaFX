package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import application.datamodel.ItemUnit;

public class ManageUnit {
	private static volatile ManageUnit INSTANCE;
	private final static Logger logger = LogManager.getLogger(ManageUnit.class);

	public static ManageUnit getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageSupplier.class) {
				if(INSTANCE == null) {
					logger.info("Instantiating " + ManageUnit.class);
					INSTANCE = new ManageUnit();
				}
			}
		}
		logger.debug(ManageUnit.class + " : " + INSTANCE);
		return INSTANCE;
	}
	
	public List<ItemUnit> listUnits(String search){
		logger.info("Retrieving Item units.");
		String value = "%" + search + "%";
		List<ItemUnit> units = null;
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<ItemUnit> criteria = cb.createQuery(ItemUnit.class);
		Root<ItemUnit> pUnit = criteria.from(ItemUnit.class);
		
		Predicate whereUnit = cb.like(pUnit.get("Unit"), value);
		criteria.where(whereUnit);
		units = session.createQuery(criteria).getResultList();
		logger.debug(ItemUnit.class + " : " + units);
		return units;
	}
}
