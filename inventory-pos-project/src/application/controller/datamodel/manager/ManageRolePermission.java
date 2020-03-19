package application.controller.datamodel.manager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import application.datamodel.Permissions;
import application.datamodel.RolePermission;
import application.datamodel.UserRole;

public class ManageRolePermission {

	private static volatile ManageRolePermission INSTANCE;
	
	public static ManageRolePermission getInstance() {
		if(INSTANCE == null) {
			synchronized (ManageRolePermission.class) {
				if(INSTANCE == null) {
					INSTANCE = new ManageRolePermission();
				}
			}
		}
		return INSTANCE;
	}
	
	public RolePermission getRolePermissionByRoleAndPermission(UserRole role, Permissions permission) {
		RolePermission rolePermission = null;
		Session session = SessionManager.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<RolePermission> criteria = cb.createQuery(RolePermission.class);
		Root<RolePermission> pRolePermission = criteria.from(RolePermission.class);
		
		Predicate pRole = cb.equal(pRolePermission.get("userrolepermission"), role);
		Predicate pPermission = cb.equal(pRolePermission.get("permission"), permission);
		Predicate xAnd = cb.and(pRole, pPermission);
		criteria.where(xAnd);
		rolePermission = session.createQuery(criteria).uniqueResult();
		return rolePermission;
	}
}
