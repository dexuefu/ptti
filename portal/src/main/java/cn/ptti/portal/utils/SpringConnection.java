package cn.ptti.portal.utils;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Spring 获取Connection 
 *
 */
public class SpringConnection {
	public static Connection getConnection()throws SQLException{  
		return DataSourceUtils.getConnection((DataSource)SpringConfigTool.getContext().getBean("dataSource"));
	}
	/**
	public static Connection releaseConnection(Connection conn,DataSource dataSource)throws SQLException{  
		
		return DataSourceUtils.releaseConnection(conn,dataSource);;
	}
	**/
}
