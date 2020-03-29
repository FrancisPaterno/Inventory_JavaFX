package application.logging.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class ConnectionFactory {
	//private final static Logger logger = LogManager.getLogger(ConnectionFactory.class);
	private static interface Singleton {
		final ConnectionFactory INSTANCE = new ConnectionFactory();
	}

	private final DataSource dataSource;

	private ConnectionFactory() {
		Properties properties = new Properties();
		properties.setProperty("user", "root");
		properties.setProperty("password", "Edward143!");

		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:mysql://francispaterno-VirtualBox/logger?serverTimezone=Asia/Manila",properties);
		PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connectionFactory, null);
		poolableConnFactory.setValidationQuery("SELECT 1");
		poolableConnFactory.setValidationQueryTimeout(3);
		poolableConnFactory.setDefaultReadOnly(false);
		poolableConnFactory.setDefaultAutoCommit(false);
		poolableConnFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>(poolableConnFactory);
		poolableConnFactory.setPool(pool);
		this.dataSource = new PoolingDataSource<PoolableConnection>(pool);
	}

	public static Connection getDatabaseConnection()  {
		//logger.info("Obtaining connection for database logger...");
		Connection c = null;
		try {
			c = Singleton.INSTANCE.dataSource.getConnection();
			//logger.info("Connection for database logger successfully obtained.");
		} catch (SQLException e) {
			//logger.error("Error obtaining connection for database logger.", e);
		}
		return c;
	}

	/*
	 * private final static Logger logger =
	 * LogManager.getLogger(ConnectionFactory.class); private static BasicDataSource
	 * dataSource;
	 * 
	 * private ConnectionFactory() {}
	 * 
	 * public static Connection getConnection() {
	 * logger.info("Obtaining connection for database logger..."); Connection c =
	 * null; if(dataSource == null) { synchronized (BasicDataSource.class) {
	 * if(dataSource == null) { dataSource = new BasicDataSource();
	 * dataSource.setUrl("jdbc:mysql://localhost/mwn_enterprise?serverTimezone=UTC")
	 * ; dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	 * dataSource.setUsername("root"); dataSource.setPassword("Edward143!"); } } }
	 * try { c = dataSource.getConnection();
	 * logger.info("Connection for database logger successfully obtained."); } catch
	 * (SQLException e) {
	 * logger.error("Error obtaining connection for database logger.", e); } return
	 * c; }
	 */
}
