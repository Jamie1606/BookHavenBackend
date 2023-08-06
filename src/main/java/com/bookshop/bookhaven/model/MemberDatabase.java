// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date        	: 7.7.2023
// Description 	: utility bean relating to "Member" TABLE

package com.bookshop.bookhaven.model;

import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class MemberDatabase {

	
	// get member info for userdetails by email
	public boolean checkUserByEmail(String email) throws SQLException {
		Connection conn = null;
		boolean condition = false;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM Member WHERE Email = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, email);

			ResultSet rs = st.executeQuery();

			// escaping html special characters
			if (rs.next()) {
				condition = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in loadUserByUsername in MemberDatabase .....");
		} finally {
			conn.close();
		}

		return condition;
	}

	// update member log in time
	public int updateLoginTime(int id) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "UPDATE Member SET LastActive = CURRENT_TIMESTAMP WHERE MemberID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, id);

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			System.out.print(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	// login as member
	public Member loginMember(String email, String password) {
		Connection conn = null;
		Member loginMember = null;

		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Member WHERE Email = ? AND Password = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				loginMember = new Member();
				loginMember.setMemberID(rs.getInt("MemberID"));
				loginMember.setEmail(rs.getString("Email"));
			}
		} catch (Exception e) {
			System.out.print(".......memberDB : " + e);
		}

		return loginMember;
	}

	// check member email duplication
	public boolean checkMemberEmailExists(String email) throws SQLException {
		Connection conn = null;
		boolean isExist = false;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Member WHERE Email=?";
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
			String sqlStatement = "INSERT INTO Member (Name, Email, Password, Address, Phone, Gender, BirthDate, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
	public ArrayList<Member> getAllMember() throws SQLException {
		
		ArrayList<Member> memberList = new ArrayList<>();
		Connection conn = null;
		Member uBean = null;
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Member";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				uBean = new Member();
				uBean.setMemberID(rs.getInt("MemberID"));
				uBean.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				
				if(rs.getString("Gender") == null) {
					uBean.setGender('N');
				}
				else {
					uBean.setGender(rs.getString("Gender").charAt(0));
				}
				
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone(StringEscapeUtils.escapeHtml4(rs.getString("Phone")));
				uBean.setAddress(StringEscapeUtils.escapeHtml4(rs.getString("Address")));
				uBean.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				uBean.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("Password")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				uBean.setLastActive(rs.getDate("LastActive"));
				memberList.add(uBean);
				System.out.println(".....done writing " + uBean.getMemberID() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
			memberList = null;
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
			String sqlStatement = "SELECT * FROM Member WHERE MemberID=?";

			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				uBean = new Member();
				uBean.setMemberID(rs.getInt("MemberID"));
				uBean.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				
				if(rs.getString("Gender") == null) {
					uBean.setGender('N');
				}
				else {
					uBean.setGender(rs.getString("Gender").charAt(0));
				}
				
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone(StringEscapeUtils.escapeHtml4(rs.getString("Phone")));
				uBean.setAddress(StringEscapeUtils.escapeHtml4(rs.getString("Address")));
				uBean.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				uBean.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("Password")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
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


	// update member data to db
	public int updateMember(Member member) throws SQLException {
		
		Connection conn = null;
		int rec = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			String name = member.getName();
			char gender = member.getGender();
			Date birthDate = member.getBirthDate();
			String phone = member.getPhone();
			String address = member.getAddress();
			String image = member.getImage();
			int memberID = member.getMemberID();
			String password = member.getPassword();
			
			if (password == null || password.isEmpty()) {
				
				// Without Password
				String sqlStatement = "UPDATE Member SET Name = ?, Gender = ?, BirthDate = ?, Phone = ?, Address = ?, Image = ? WHERE MemberID = ?;";
				PreparedStatement pstmt = conn.prepareStatement(sqlStatement);

				pstmt.setString(1, name);
				if (gender != 'N') {
					pstmt.setString(2, String.valueOf(gender));
				} else {
					pstmt.setNull(2, Types.CHAR);
				}
				if (birthDate == null) {
					pstmt.setNull(3, Types.DATE);
				} else {
					pstmt.setDate(3, Date.valueOf(birthDate.toString()));
				}
				pstmt.setString(4, phone);
				pstmt.setString(5, address);
				pstmt.setString(6, image);
				pstmt.setInt(7, memberID);
				rec = pstmt.executeUpdate();
				System.out.println("...in MemberDB-done update member data...");
				// end-without password
				
			} else {
				
				// With Password
				String sqlStatement = "UPDATE Member SET Name = ?, Gender = ?, BirthDate = ?, Phone = ?, Address = ?, Image = ?, Password=? WHERE MemberID = ?;";
				PreparedStatement pstmt = conn.prepareStatement(sqlStatement);

				pstmt.setString(1, name);
				if (gender != 'N') {
					pstmt.setString(2, String.valueOf(gender));
				} else {
					pstmt.setNull(2, Types.CHAR);
				}
				if (birthDate == null) {
					pstmt.setNull(3, Types.DATE);
				} else {
					pstmt.setDate(3, Date.valueOf(birthDate.toString()));
				}
				pstmt.setString(4, phone);
				pstmt.setString(5, address);
				pstmt.setString(6, image);
				pstmt.setString(7, password);
				pstmt.setInt(8, memberID);
				rec = pstmt.executeUpdate();
				System.out.println("...in MemberDB-done update member data...");
				// end-with password
			}
		} 
		catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} 
		finally {
			conn.close();
		}
		
		return rec;
	}

	// delete member from db by ID
	public int deleteMember(int ID) throws SQLException {
		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "DELETE FROM Member WHERE MemberID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
			nrow = pstmt.executeUpdate();
			System.out.println("...in MemberDB-done delete member data...");
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
		} finally {
			conn.close();
		}
		return nrow;
	}
	
	// select top customers
	public ArrayList<Member> getTopMemberList(int limit) throws SQLException {
		
		ArrayList<Member> memberList = new ArrayList<>();
		Connection conn = null;
		Member uBean = null;
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT m.*  FROM bookhavendb.Member m JOIN bookhavendb.Order o ON m.MemberID = o.MemberID WHERE m.MemberID IS NOT NULL GROUP BY m.MemberID ORDER BY SUM(o.amount) DESC LIMIT ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				uBean = new Member();
				uBean.setMemberID(rs.getInt("MemberID"));
				uBean.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				
				if(rs.getString("Gender") == null) {
					uBean.setGender('N');
				}
				else {
					uBean.setGender(rs.getString("Gender").charAt(0));
				}
				
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone(StringEscapeUtils.escapeHtml4(rs.getString("Phone")));
				uBean.setAddress(StringEscapeUtils.escapeHtml4(rs.getString("Address")));
				uBean.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				uBean.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("Password")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				uBean.setLastActive(rs.getDate("LastActive"));
				memberList.add(uBean);
				System.out.println(".....done writing " + uBean.getMemberID() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
			memberList = null;
		} finally {
			conn.close();
		}
		
		return memberList;
	}
	
	// select all member data
	public ArrayList<Member> getMemberListByISBN(String isbn) throws SQLException {
		
		ArrayList<Member> memberList = new ArrayList<>();
		Connection conn = null;
		Member uBean = null;
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT DISTINCT m.*\r\n"
					+ "FROM bookhavendb.Member m\r\n"
					+ "INNER JOIN bookhavendb.Order o ON m.MemberID = o.MemberID\r\n"
					+ "INNER JOIN OrderItem oi ON o.OrderID = oi.OrderID\r\n"
					+ "INNER JOIN Book b ON oi.ISBNNo = b.ISBNNo\r\n"
					+ "WHERE b.ISBNNo = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, isbn);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				uBean = new Member();
				uBean.setMemberID(rs.getInt("MemberID"));
				uBean.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				
				if(rs.getString("Gender") == null) {
					uBean.setGender('N');
				}
				else {
					uBean.setGender(rs.getString("Gender").charAt(0));
				}
				
				uBean.setBirthDate(rs.getDate("BirthDate"));
				uBean.setPhone(StringEscapeUtils.escapeHtml4(rs.getString("Phone")));
				uBean.setAddress(StringEscapeUtils.escapeHtml4(rs.getString("Address")));
				uBean.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				uBean.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("Password")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				uBean.setLastActive(rs.getDate("LastActive"));
				memberList.add(uBean);
				System.out.println(".....done writing " + uBean.getMemberID() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......memberDB : " + e);
			memberList = null;
		} finally {
			conn.close();
		}
		
		return memberList;
	}
}
