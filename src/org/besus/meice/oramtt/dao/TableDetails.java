package org.besus.meice.oramtt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.besus.meice.oramtt.ui.ConstraintDailog;
import org.besus.meice.oramtt.util.ConnectionManager;
import org.besus.meice.oramtt.util.QueryConstants;

public class TableDetails {

	public static String errorMessage = "";

	public static List<String> getTableNames() {
		List<String> tables = new ArrayList<String>();
		Connection conn = ConnectionManager.openConnection();

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(QueryConstants.TABLE_SELECTION_MYSQL);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString(1);
				tables.add(tableName);
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection();
		}
		return tables;
	}

	public static Object[][] getTableData(String tableName, List<String> columns) {
		Object[][] data = null;
		Connection conn = ConnectionManager.openConnection();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("select * from " + tableName,
					ResultSet.TYPE_SCROLL_SENSITIVE);

			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int numberOfColumns = metaData.getColumnCount();

			for (int i = 1; i <= numberOfColumns; i++) {
				columns.add(metaData.getColumnName(i));
			}

			int rowCount = 0;

			while (rs.next()) {
				rowCount++;
			}

			data = new Object[rowCount][numberOfColumns];
			rs.beforeFirst();

			rowCount = 0;
			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					data[rowCount][i - 1] = rs.getObject(i);
				}
				rowCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection();
		}
		return data;
	}

	public static void deleteRow(Map<String, Object> row, String tableName) {
		String primaryKey = "";
		String command = "";
		Connection conn = ConnectionManager.openConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("desc " + tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("Key");
				if (key.equals("PRI")) {
					primaryKey = rs.getString(1);
				}
				if (!primaryKey.equals("")) {
					command = "delete from " + tableName + " where "
							+ primaryKey + "=" + row.get(primaryKey);
					pstmt = conn.prepareStatement(command);
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection();
		}
	}

	public static int insertRow(Map<Integer, Object> data, String tableName,
			int noOfAttribute) {
		Connection conn = ConnectionManager.openConnection();
		String command = "";
		Object valueInserted;
		try {
			PreparedStatement pstmt;
			command = "insert into " + tableName + " values  (";
			for (int col = 0; col < noOfAttribute; col++) {
				if (data.get(col) == null) {
					valueInserted = "NULL";
				} else if (data.get(col) instanceof String
						|| data.get(col) instanceof Date) {
					valueInserted = " '" + data.get(col) + "' ";
				} else
					valueInserted = data.get(col);
				if (col < noOfAttribute - 1)
					command = command + valueInserted + ",";
				else
					command = command + valueInserted + ");";
			}
			// }

//			System.out.println("COMMAND:" + command);
			pstmt = conn.prepareStatement(command);
			pstmt.executeUpdate();

		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage = e.getMessage();
			return -1;
		} finally {
			ConnectionManager.closeConnection();
		}
		return 0;

	}

	public static int UpdateCell(Map<String, Object> row, String attributeName,
			Object updatedValue, Object oldValue, String tableName) {
		String primaryKey = "";
		String command = "";
		Object primaryKeyValue = "";
		Connection conn = ConnectionManager.openConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("desc " + tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("Key");
				if (key.equals("PRI")) {
					primaryKey = rs.getString(1);
				}
			}
			if (!primaryKey.equals("")) {
				primaryKeyValue = row.get(primaryKey);
				// primary key checking
				if (primaryKey.equals(attributeName)) {
					pstmt = conn.prepareStatement("select " + primaryKey
							+ " from " + tableName);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						if (rs.getString(1).equals(row.get(primaryKey))) {
							return 1;
						}
					}
					primaryKeyValue = oldValue;

				}
				command = "update " + tableName + " set " + attributeName
						+ " = ";

				if (updatedValue instanceof String
						|| updatedValue instanceof Date) {
					command = command + "'" + updatedValue + "'";
				} else
					command = command + updatedValue;

				command = command + " where " + primaryKey + "="
						+ primaryKeyValue;
//				System.out.println("command:" + command);
				pstmt = conn.prepareStatement(command);
				pstmt.executeUpdate();
				System.out.println("Executing update..");

			}

		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage = e.getMessage();
			return -1;
		} finally {
			ConnectionManager.closeConnection();
		}
		return 0;
	}

	// Retrive Indices Names
	public static List<String> retriveConstraints(String tableName) {
		Connection conn = ConnectionManager.openConnection();
		List<String> indicesNames = new ArrayList<String>();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("show create table " + tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String index = rs.getString("Create Table");
				String lastPart = index
						.substring(index.lastIndexOf("PRIMARY KEY (") + 14,
								index.length());
				String primaryKey = index.substring(
						index.lastIndexOf("PRIMARY KEY (") + 14,
						index.lastIndexOf("PRIMARY KEY (")
								+ lastPart.indexOf(")") + 13);
				if(primaryKey.contains("`")){
					primaryKey=primaryKey.replace("`", "");
				}
				indicesNames.add(primaryKey + " [PRIMARY]");
				if (index.contains("CONSTRAINT")) {
					String remPart = index.substring(
							index.indexOf("CONSTRAINT `") + 12, index.length());
//					System.out.println("lastPart..." + remPart);
					String foreignKeyName = remPart.substring(0,
							remPart.indexOf("`"));
					String foreignKeyVal = remPart.substring(
							remPart.indexOf("FOREIGN KEY (`") + 14,
							remPart.indexOf("`)"));
//					System.out.println("FOREIGN KEY.." + foreignKeyName + " "
//							+ foreignKeyVal);
					indicesNames.add(foreignKeyVal + " [" + foreignKeyName+"] ");
				}
			}

		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage = e.getMessage();
		} finally {
			ConnectionManager.closeConnection();
		}
		return indicesNames;
	}

	public static List<String> retriveColumnDetails(String tableName) {
		Connection conn = ConnectionManager.openConnection();
		List<String> columnDetails = new ArrayList<String>();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("desc " + tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String colName = rs.getString("Field");
				String colType = rs.getString("Type");
				columnDetails.add(colName + " [" + colType + "]");
			}

		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage = e.getMessage();
		} finally {
			ConnectionManager.closeConnection();
		}
		return columnDetails;
	}

	// Retrive Foriegn Key Details
	public static void retriveForiegnKeyDetails(String tableName) {
		Connection conn = ConnectionManager.openConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("show create table " + tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("Create Table");
				if (key.contains("CONSTRAINT"))
					key = key.substring(key.indexOf("CONSTRAINT"),
							(key.indexOf(") ENGINE") - 1));
				else
					key = "No References";
				ConstraintDailog.setForeignKeyRefDesc(key);

			}

		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage = e.getMessage();
		} finally {
			ConnectionManager.closeConnection();
		}
	}

}
