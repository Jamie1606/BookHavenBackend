// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 16.7.2023
// Description	: database functions to get keys and token

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TempAuthDatabase {
	public TempAuth getAuthKey() throws SQLException {
		Connection conn = null;
		TempAuth auth = null;
				
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT * FROM AuthKey";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				auth = new TempAuth();
				auth.setAccesskey(rs.getString("AccessKey"));
				auth.setSecretkey(rs.getString("SecretKey"));
				auth.setSessiontoken(rs.getString("SessionToken"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
		return auth;
	}
}