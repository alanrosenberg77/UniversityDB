package dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBCBaseDAO is a supertype that outlines common functionality between all JDBCDAOs
 * @author alanr
 *
 */
public class JDBCBaseDAO {
	
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource(); 
	
	//creating pooled connections
	static {
					dataSource.setJdbcUrl("jdbc:sqlite:uni.db");
					
					dataSource.setInitialPoolSize(3);
					dataSource.setMinPoolSize(2);
					dataSource.setAcquireIncrement(5);
					dataSource.setMaxPoolSize(5);
	}
	
	//establishes connection
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
