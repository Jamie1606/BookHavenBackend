// Author		: Zay Yar Tun, Thu Htet San
// Admin No		: 2235035, 2235022
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 10.7.2023
// Description	: database configuration

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public static Connection getConnection() {
		// database configuration
		String connURL = "jdbc:postgresql://bookhaven.clcf0fzaiwt0.us-east-2.rds.amazonaws.com:5432/postgres";
		String db_username = "postgres";
		String db_password = "bookhaven";
		
		Connection conn = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(connURL, db_username, db_password);
			conn.setSchema("bookhavendb");
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in Database Connection .....");
		}
		return conn;
	}
}