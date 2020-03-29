package application.controller.datamodel.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

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
	private static final Logger logger = LogManager.getLogger(SessionManager.class);
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

	public static Connection getConnection()  {
		Connection c = null;
		logger.info("Obtaining connection for database logger...");
		try {
			c = getInstance().getSessionFactoryOptions().getServiceRegistry()
					.getService(ConnectionProvider.class).getConnection();
			logger.info("Connection for database logger successfully obtained.");
		} catch (SQLException e) {
			logger.error("Error obtaining connection for database logger...", e);
		}
		return c;
	}
}