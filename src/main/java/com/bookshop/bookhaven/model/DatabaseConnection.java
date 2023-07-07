// Author		: Zay Yar Tun, Thu Htet San
// Admin No		: 2235035, 2235022
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 6.7.2023
// Description	: database configuration

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public static Connection getConnection() {
		// database configuration
		String connURL = "jdbc:postgresql://satao.db.elephantsql.com/mhekoapk";
		String db_username = "mhekoapk";
		String db_password = "o9w2O25Afleif9CCVCEBDQZX4tT79MH7";
		
		Connection conn = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(connURL, db_username, db_password);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in Database Connection .....");
		}
		return conn;
	}
}