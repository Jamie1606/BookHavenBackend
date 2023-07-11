// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: this is to store admin data from database

package com.bookshop.bookhaven.model;

import java.sql.*;

import org.apache.commons.text.StringEscapeUtils;

public class AdminDatabase {

	// get one admin admin data by adminID from database
	public Admin getAdmin(int id) throws SQLException {
		Connection conn = null;
		Admin admin = null;
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM Admin WHERE AdminID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			// escaping html special characters
			if (rs.next()) {
				admin = new Admin();
				admin.setAdminID(rs.getInt("AdminID"));
				admin.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				admin.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				admin.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("Password")));
				Timestamp timestamp = rs.getTimestamp("LastActive");
				if (timestamp != null) {
					admin.setLastActive(new Date(timestamp.getTime()));
				} else {
					admin.setLastActive(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getAdmin in AdminDatabase .....");
		} finally {
			conn.close();
		}
		return admin;
	}

	// log in as admin
	public Admin loginAdmin(Admin admin) throws SQLException {
		Admin loginAdmin = null;
		Connection conn = null;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, admin.getEmail());
			st.setString(2, admin.getPassword());

			ResultSet rs = st.executeQuery();

			// escaping html special characters
			if (rs.next()) {
				loginAdmin = new Admin();
				loginAdmin.setAdminID(rs.getInt("AdminID"));
				loginAdmin.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in loginAdmin in AdminDatabase .....");
		} finally {
			conn.close();
		}
		return loginAdmin;
	}
	
	// update admin log in time
	public int updateLoginTime(int id) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "UPDATE Admin SET LastActive = CURRENT_TIMESTAMP WHERE AdminID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, id);

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in updateLoginTime in AdminDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}
}