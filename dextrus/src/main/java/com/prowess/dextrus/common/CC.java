package com.prowess.dextrus.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.prowess.dextrus.entity.ConnectionEntity;

public class CC {
	public static final String GET_TABLES_BY_PATTERN_QUERY = "SELECT TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE ?";
	private static Connection con;

	public static Connection getConnection(ConnectionEntity props) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(props.getUrl(), props.getUsername(), props.getPassword());
		} catch (SQLException | ClassNotFoundException e) {
			con=null;
			e.printStackTrace();
		}
		return con;

	}

}
