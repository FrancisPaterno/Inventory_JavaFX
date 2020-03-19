package application.controller.datamodel.manager;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import application.datamodel.ItemUnit;

public class ManageUnit {
	public static volatile ManageUnit INSTANCE;

	public static ManageUnit getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageSupplier.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageUnit();
				}
			}
		}
		return INSTANCE;
	}
	
	public List<ItemUnit> listUnits(String search){
		String value = "%" + search + "%";
		List<ItemUnit> units = null;
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<ItemUnit> criteria = cb.createQuery(ItemUnit.class);
		Root<ItemUnit> pUnit = criteria.from(ItemUnit.class);
		
		Predicate whereUnit = cb.like(pUnit.get("Unit"), value);
		criteria.where(whereUnit);
		units = session.createQuery(criteria).getResultList();
		return units;
	}
}
