package application.controller.datamodel.manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import application.datamodel.Item;
import application.datamodel.ItemBrand;
import application.datamodel.ItemCategory;
import application.datamodel.ItemImage;
import application.datamodel.ItemUnit;
import application.datamodel.Permissions;
import application.datamodel.RolePermission;
import application.datamodel.Supplier;
import application.datamodel.User;
import application.datamodel.UserRole;

public  class SessionManager {

	private static volatile SessionFactory factory;
	private static Session session = null;
	public SessionManager() {
		
	}
	
	public static SessionFactory getInstance() {
		if(factory == null) {
			synchronized (SessionFactory.class) {
				if(factory == null) {
					factory = new Configuration()
							.configure()
							.addPackage("application.controller.datamodel")
							.addAnnotatedClass(ItemCategory.class)
							.addAnnotatedClass(ItemBrand.class)
							.addAnnotatedClass(Supplier.class)
							.addAnnotatedClass(Item.class)
							.addAnnotatedClass(ItemUnit.class)
							.addAnnotatedClass(ItemImage.class)
							.addAnnotatedClass(UserRole.class)
							.addAnnotatedClass(User.class)
							.addAnnotatedClass(RolePermission.class)
							.addAnnotatedClass(Permissions.class)
							.buildSessionFactory();
				}
			}
		}
		return factory;
	}
	
	public static Session getSession() {
		factory = SessionManager.getInstance();
		if(session == null) {
			synchronized (Session.class) {
				if(session == null) {
					session = factory.openSession();
				}
			}
		}
		return session;
	}
}