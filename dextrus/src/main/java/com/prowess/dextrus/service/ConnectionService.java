package com.prowess.dextrus.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.ExecutableType;

import org.springframework.stereotype.Service;
import com.prowess.dextrus.common.CC;
import com.prowess.dextrus.entity.ConnectionEntity;
import com.prowess.dextrus.entity.TableDescription;
import com.prowess.dextrus.entity.TableType;

@Service
public class ConnectionService {

	public Connection getSqlConnection(ConnectionEntity props) {
		Connection con = CC.getConnection(props);
		return con;

	}

	public List<String> getCatalogs(ConnectionEntity props) {
		List<String> catalogs = new ArrayList<>();
		Connection con = CC.getConnection(props);
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT name FROM sys.databases");

			while (rs.next()) {
				catalogs.add(rs.getString(1));

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return catalogs;

	}

	public List<String> getSchema(ConnectionEntity props, String catalog) {
		List<String> schema = null;

		try {
			Connection con = CC.getConnection(props);
			ResultSet rs = con.createStatement().executeQuery("SELECT name FROM " + catalog + ".sys.schemas");
			schema = new ArrayList<>();
			while (rs.next()) {
				schema.add(rs.getString(1));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return schema;

	}

	public List<TableType> getViewsAndTables(ConnectionEntity props, String catalog, String schema) {
		List<TableType> viewsandtables = new ArrayList<>();

		try {
			Connection con = CC.getConnection(props);
			PreparedStatement preparestatemnet = con.prepareStatement("USE " + catalog + "; SELECT TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE  TABLE_SCHEMA= ? ;");
//			preparestatemnet.setString(1, catalog);
			preparestatemnet.setString(1, schema);
			ResultSet rs = preparestatemnet.executeQuery();

			while (rs.next()) {

				TableType tabletype = new TableType();
				tabletype.setTable_name(rs.getString(1));
				tabletype.setTable_type(rs.getString(2));
				viewsandtables.add(tabletype);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
    	return viewsandtables;

	}

	public ArrayList<TableDescription> getDescOfTables(ConnectionEntity props, String catalog, String schema,
			String table) {
		ArrayList<TableDescription> al = new ArrayList<>();
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = CC.getConnection(props);
			PreparedStatement preparestatement = con
					.prepareStatement("USE " + catalog + "; SELECT * FROM " + schema + "." + table + ";");
			ResultSet rs = preparestatement.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			int columnCount = data.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				TableDescription td = new TableDescription();
				td.setColumnName(data.getColumnName(i));
				td.setDataType(data.getColumnTypeName(i));
				td.setIsNullable(data.isNullable(i));
				td.setMaxlength(data.getColumnDisplaySize(i));
				td.setPrecision(data.getPrecision(i));
				al.add(td);
			}

		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		return al;
	}

	public List<List<Object>> getTableData(ConnectionEntity props, String query) {
		List<List<Object>> rows = new ArrayList<>();
		
		try {
			Connection con = CC.getConnection(props);
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			ResultSetMetaData meta = resultSet.getMetaData();
			int columnCount = meta.getColumnCount();
			
			while (resultSet.next()) {

				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {

					String columnName = meta.getColumnName(i);
					String columnType = meta.getColumnTypeName(i);
					switch (columnType) {
					case "varchar": {
						row.add(columnName + " : " + resultSet.getString(columnName));
						break;
					}
					case "float": {
						row.add(columnName + " : " + resultSet.getFloat(columnName));
						break;
					}
					case "boolean": {
						row.add(columnName + " : " + resultSet.getBoolean(columnName));
						break;
					}
					case "int": {
						row.add(columnName + " : " + resultSet.getInt(columnName));
						break;
					}
					case "timestamp": {
						row.add(columnName + " : " + resultSet.getTimestamp(columnName));
						break;
					}
					case "decimal": {
						row.add(columnName + " : " + resultSet.getBigDecimal(columnName));
						break;
					}
					case "date": {
						row.add(columnName + " : " + resultSet.getDate(columnName));
						break;
					}
					default:
						row.add("!-!-! " + columnName + " : " + resultSet.getObject(columnName));
						System.out.println("Datatype Not available for Column: " + columnName);
					}
				}
				rows.add(row);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return rows;
	}

	public List<TableType> getTablesAndViewsByPattern(ConnectionEntity properties, String catalog, String pattern) {
		List<TableType> viewsAndTables = new ArrayList<>();
		try {
			Connection connection = CC.getConnection(properties);
			PreparedStatement statement = connection
					.prepareStatement("use " + catalog + "; " + CC.GET_TABLES_BY_PATTERN_QUERY);
			statement.setString(1, pattern);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				TableType tableType = new TableType();
				tableType.setTable_name(resultSet.getString("TABLE_NAME"));
				tableType.setTable_type(resultSet.getString("TABLE_TYPE"));
				viewsAndTables.add(tableType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewsAndTables;
	}
	
	

}
