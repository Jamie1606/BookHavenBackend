// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date		  	: 6.8.2023
// Description 	: utility bean relating to search functions

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class SearchDatabase {

	public ArrayList<Book> getBookByAuthorName(String authorName) throws SQLException {
		ArrayList<Book> bookList = new ArrayList<>();
		Book uBean = null;
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement="SELECT b.* FROM Book AS b JOIN BookAuthor AS ba ON b.ISBNNo=ba.ISBNNo JOIN Author AS a ON ba.AuthorID=a.AuthorID WHERE Lower(a.Name) LIKE ?";
			PreparedStatement pstmt=conn.prepareStatement(sqlStatement);
			String name="%"+authorName.toLowerCase()+"%";
			pstmt.setString(1,name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				uBean = new Book();
				uBean.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				uBean.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				uBean.setPage(rs.getInt("Page"));
				uBean.setPrice(rs.getDouble("Price"));
				uBean.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				uBean.setPublicationDate(rs.getDate("PublicationDate"));
				uBean.setQty(rs.getInt("Qty"));
				uBean.setRating(rs.getDouble("Rating"));
				uBean.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				uBean.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				uBean.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(uBean);
				System.out.println(".....done writing " + uBean.getISBNNo() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......searchDB : " + e);
		} finally {
			conn.close();
		}
		return bookList;
	}
	
	public ArrayList<Book> getBookByBookTitle(String bookTitle) throws SQLException {
		ArrayList<Book> bookList = new ArrayList<>();
		Book uBean = null;
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement="SELECT * FROM Book WHERE Lower(Title) LIKE ?";
			PreparedStatement pstmt=conn.prepareStatement(sqlStatement);
			String title="%"+bookTitle.toLowerCase()+"%";
			pstmt.setString(1,title);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				uBean = new Book();
				uBean.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				uBean.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				uBean.setPage(rs.getInt("Page"));
				uBean.setPrice(rs.getDouble("Price"));
				uBean.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				uBean.setPublicationDate(rs.getDate("PublicationDate"));
				uBean.setQty(rs.getInt("Qty"));
				uBean.setRating(rs.getDouble("Rating"));
				uBean.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				uBean.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				uBean.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				uBean.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(uBean);
				System.out.println(".....done writing " + uBean.getISBNNo() + " to List!.....");
			}
		} catch (Exception e) {
			System.out.println(".......searchDB : " + e);
		} finally {
			conn.close();
		}
		return bookList;
	}
	
}
