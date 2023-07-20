//Author 	  : Thu Htet San
//Admin No    : 2235022
//Class       : DIT/FT/2A/02
//Group       : 10
//Date        : 7.7.2023
//Description : utility bean relating to "Member" TABLE

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;


public class MemberDatabase {

	// check member email duplication
	public boolean checkMemberEmailExists(String email) throws SQLException {
		Connection conn = null;
		boolean isExist = false;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM \"public\".\"Member\" WHERE \"Email\"=?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				isExist = true;
			}
		} catch (Exception e) {
			System.out.print(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return isExist;
	}

	// insert new member to database
	public int insertMember(Member member) throws SQLException {

		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			// [INSERT DATA TO TABLE]
			String sqlStatement = "INSERT INTO \"public\".\"Member\" (\"Name\", \"Email\", \"Password\", \"Address\", \"Phone\",\"Gender\",\"BirthDate\",\"Image\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPassword());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getPhone());
			if (member.getGender() == 'N') {
				pstmt.setNull(6, Types.CHAR);
			} else {
				pstmt.setString(6, String.valueOf(member.getGender()));
			}

			if (member.getBirthDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setDate(7, Date.valueOf(member.getBirthDate().toString()));
			}

			if (member.getImage() == null) {
				pstmt.setString(8, "");
			} else {
				pstmt.setString(8, member.getImage());
			}
			nrow = pstmt.executeUpdate();
			System.out.println("...in MemberDB-done insert new member...");
		} catch (Exception e) {
			System.out.print(".......memberDB : " + e);
		} finally {
			conn.close();
		}

		return nrow;
	}

	// select all member data
	public ArrayList<Member> getMember() throws SQLException {
		ArrayList<Member> memberList = new ArrayList<>();
		Connection conn = null;
		Member uBean = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM \"public\".\"Member\"";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				uBean = new Member();
				uBean.setName( StringEscapeUtils.escapeHtml4 (rs.getString("Name")));
				uBean.setGender(rs.getString("Gender").charAt(0));
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone( StringEscapeUtils.escapeHtml4 (rs.getString("Phone")));
				uBean.setAddress( StringEscapeUtils.escapeHtml4 (rs.getString("Address")));
				uBean.setEmail( StringEscapeUtils.escapeHtml4 (rs.getString("Email")));
				uBean.setPassword( StringEscapeUtils.escapeHtml4 (rs.getString("Password")));
				uBean.setImage( StringEscapeUtils.escapeHtml4 (rs.getString("Image")));
				uBean.setLastActive(rs.getDate("LastActive"));
				memberList.add(uBean);
				System.out.println(".....done writing " + uBean.getMemberID() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return memberList;

	}

	// select member data by ID
	public Member getMemberByID(int ID) throws SQLException {
		Member uBean = null;
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM \"public\".\"Member\" WHERE \"MemberID\"=?";

			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				uBean = new Member();
				uBean.setName( StringEscapeUtils.escapeHtml4 (rs.getString("Name")));
				uBean.setGender(rs.getString("Gender").charAt(0));
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone( StringEscapeUtils.escapeHtml4 (rs.getString("Phone")));
				uBean.setAddress( StringEscapeUtils.escapeHtml4 (rs.getString("Address")));
				uBean.setEmail( StringEscapeUtils.escapeHtml4 (rs.getString("Email")));
				uBean.setPassword( StringEscapeUtils.escapeHtml4 (rs.getString("Password")));
				uBean.setImage( StringEscapeUtils.escapeHtml4 (rs.getString("Image")));
				uBean.setLastActive(rs.getDate("LastActive"));
				System.out.println(".....done writing to bean!.....");

			}
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return uBean;
	}

	// update member password to db
	public int updateMemberPassword(int ID, String password) throws SQLException {
		Connection conn = null;
		int rec = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE \"public\".\"Member\" SET \"Password\" = ? WHERE \"MemberID\" = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, password);
			pstmt.setInt(2, ID);
			rec = pstmt.executeUpdate();
			System.out.println("...in MemberDB-done update member password...");
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return rec;
	}

	// update member data to db
	public int updateMember(Member member) throws SQLException {
		Connection conn = null;
		int rec = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE \"public\".\"Member\" SET \"Name\" = ?, \"Gender\" = ?, \"BirthDate\" = ?, \"Phone\" = ?, \"Address\" = ?, \"Image\" = ? WHERE \"MemberID\" = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);

			pstmt.setString(1, member.getName());
			if (member.getGender() != 'N') {
				pstmt.setString(2, String.valueOf(member.getGender()));
			} else {
				pstmt.setNull(2, Types.CHAR);
			}
			if (member.getBirthDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setDate(3, Date.valueOf(member.getBirthDate().toString()));
			}
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getAddress());
			pstmt.setString(6, member.getImage());
			pstmt.setInt(7, member.getMemberID());
			rec = pstmt.executeUpdate();
			System.out.println("...in MemberDB-done update member data...");
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return rec;
	}
	
	//delete member from db by ID
	public int deleteMember(int ID) throws SQLException {
		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "DELETE FROM \"public\".\"Member\" WHERE \"MemberID\" = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
			nrow = pstmt.executeUpdate();
			System.out.println("...in MemberDB-done delete member data...");
		}catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return nrow;
	}
}