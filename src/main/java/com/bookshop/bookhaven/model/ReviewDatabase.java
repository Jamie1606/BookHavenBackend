package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class ReviewDatabase {
	
	
	public int updateReviewStatus(int reviewid, int adminid, String status) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE Review SET Status = ?, AdminID = ?, ApproveDate = CURRENT_TIMESTAMP WHERE ReviewID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, status);
			st.setInt(2, adminid);
			st.setInt(3, reviewid);
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return rowsAffected;
	}
	
	
	public ArrayList<Review> getAllReview() throws SQLException {
		
		Connection conn = null;
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT r.*, m.Name FROM Review r, Member m WHERE r.MemberID = m.MemberID";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Review review = new Review();
				review.setReviewDate(rs.getDate("ReviewDate"));
				review.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				review.setRating(rs.getShort("Rating"));
				review.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				review.setReviewID(rs.getInt("ReviewID"));
				review.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				review.setMemberName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				reviews.add(review);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
		
		return reviews;
	}
	
	
	public ArrayList<Review> getReviewByISBN(String isbn) throws SQLException {
		
		Connection conn = null;
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT r.*, m.Name, m.Image FROM Review r, Member m WHERE r.ISBNNo = ? AND r.Status = 'approved' AND r.MemberID = m.MemberID ORDER BY r.ReviewDate DESC";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Review review = new Review();
				review.setReviewDate(rs.getDate("ReviewDate"));
				review.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				review.setRating(rs.getShort("Rating"));
				review.setMemberImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				review.setMemberName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				reviews.add(review);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
		
		return reviews;
	}
	
	// create new review
	public int createReview(Review review, int memberid) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "INSERT INTO Review (Description, ReviewDate, Rating, ISBNNo, MemberID, Status) "
					+ "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, review.getDescription());
			st.setShort(2, review.getRating());
			st.setString(3, review.getISBNNo());
			st.setInt(4, memberid);
			st.setString(5, review.getStatus());
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createReview in ReviewDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
}