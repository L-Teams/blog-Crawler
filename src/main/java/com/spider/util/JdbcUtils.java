package com.spider.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 操作数据库工具类
 * @author 孙洪亮
 *
 */
public class JdbcUtils {
	private static Logger LOG = Logger.getLogger(JdbcUtils.class);
	private static ComboPooledDataSource ds = null;
	static{
		InputStream inputStream = null;
		try {
			inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
			Properties p = new Properties();
			p.load(inputStream);
			ds = new ComboPooledDataSource();
			ds.setDriverClass(p.getProperty("driver.class.name", ""));
			ds.setJdbcUrl(p.getProperty("url", ""));
			ds.setUser(p.getProperty("username", ""));
			ds.setPassword(p.getProperty("password", ""));
			ds.setInitialPoolSize(5);
		} catch (Exception e) {
			LOG.error("数据库初始化异常",e);
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.error("文件流关闭异常",e);
				}
			}
		}
	}
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	/**
	 * 只会返回一条结果集(查询语句自动封装成对象)
	 * 注意：保证对象和数据库的字段名称和类型对应
	 */
	public static <T> T executeQuery(String sql,Class<T> t,Object... objects){
		List<T> queryList = executeQueryList(sql, t, objects);
		if(queryList == null || queryList.size() <= 0){
			return null;
		}else
			return queryList.get(0);
	}
	
	/**
	 * 查询结果集为单列数据时候使用
	 */
	@SuppressWarnings("unchecked")
	public static <T> T executeQuerySingle(String sql, Class<T> t,Object... objects) {
		T result = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
			preparedStatement = connection.prepareStatement(sql);
			for(int i = 0 ; i < objects.length ; i++){
				preparedStatement.setObject(i + 1, objects[i]);
			}
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				result = (T) resultSet.getObject(1);
			}
		} catch (Exception e) {
			LOG.error("数据库执行失败", e);
		}finally{
			free(resultSet,preparedStatement,connection);
		}
		return result;
	}
	
	public static <T> List<T> executeQueryList(String sql,Class<T> t,Object... objects){
		List<T> list = new ArrayList<T>();
		T result = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
			preparedStatement = connection.prepareStatement(sql);
			for(int i = 0 ; i < objects.length ; i++){
				preparedStatement.setObject(i + 1, objects[i]);
			}
			resultSet = preparedStatement.executeQuery();
			BeanInfo beanInfo = Introspector.getBeanInfo(t, Object.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			while(resultSet.next()){
				result = t.newInstance();
				for(PropertyDescriptor f : propertyDescriptors){
					String fieldName = f.getName();
					Method writeMethod = f.getWriteMethod();
					Object object = resultSet.getObject(fieldName);
					if(object != null){
						writeMethod.invoke(result, object);
					}
				}
				list.add(result);
			}
		} catch (Exception e) {
			LOG.error("数据库执行失败", e);
		}finally{
			free(resultSet,preparedStatement,connection);
		}
		return list;
	}
	
	
	/**
	 * 可以进行操作（添加，更新，删除）自动提交
	 */
	public static boolean executeUpdate(String sql,Object... objects){
		boolean result = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
			preparedStatement = connection.prepareStatement(sql);
			for(int i = 0 ; i < objects.length ; i++){
				preparedStatement.setObject(i + 1, objects[i]);
			}
			int executeUpdate = preparedStatement.executeUpdate();
			result = executeUpdate >= 1 ? true : false;
		} catch (Exception e) {
			LOG.error("数据库执行失败", e);
		}finally{
			free(preparedStatement,connection);
		}
		return result;
	}
	
	private static void free(AutoCloseable... autoCloseable){
		for(AutoCloseable c : autoCloseable){
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					LOG.error("关闭异常",e);
				}
			}
		}
	}
}
