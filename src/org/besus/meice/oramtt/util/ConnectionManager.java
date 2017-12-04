package org.besus.meice.oramtt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static Connection conn = null;

	/*
	 * private static String driver = "oracle.jdbc.driver.OracleDriver"; private
	 * static String url = "jdbc:oracle:thin:@10.25.1.13:1521:pdsit"; private
	 * static String uid = "scott"; private static String password = "tiger";
	 */

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/";
	private static String database = "meice";
	private static String uid = "root";
	private static String password = "root";

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		ConnectionManager.database = database;
	}

	public static Connection openConnection() {
		try {
			if (conn == null) {
				Class.forName(driver);
				conn = DriverManager.getConnection(url + database, uid,
						password);
				return conn;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
