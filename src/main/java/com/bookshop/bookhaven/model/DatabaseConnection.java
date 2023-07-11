// Author		: Zay Yar Tun, Thu Htet San
// Admin No		: 2235035, 2235022
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: database configuration

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public static Connection getConnection() {
		// database configuration
		String connURL = "jdbc:mysql://bookhaven.csdkuovyzywj.ap-southeast-1.rds.amazonaws.com:3306/bookhavendb";
		String db_username = "admin";
		String db_password = "Jx3GMvmTKzUmrsgHXim4";
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(connURL, db_username, db_password);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in Database Connection .....");
		}
		return conn;
	}
}