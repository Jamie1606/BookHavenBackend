// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date		  	: 19.7.2023
// Description 	: utility bean relating to "Genre" TABLE

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class GenreDatabase {

	// select all genre from db
	public ArrayList<Genre> getAllGenre() throws SQLException {
		Connection conn = null;
		ArrayList<Genre> genreList = new ArrayList<>();
		Genre uBean = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Genre ORDER BY GenreID";

			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				uBean = new Genre();
				uBean.setGenreID(rs.getInt("GenreID"));
				uBean.setGenre(StringEscapeUtils.escapeHtml4(rs.getString("Genre")));
				genreList.add(uBean);
			}
		} catch (Exception e) {
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return genreList;
	}

	// select genre from db by ID
	public Genre getGenreByID(int ID) throws SQLException {
		Connection conn = null;
		Genre uBean = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Genre WHERE GenreID=?";

			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				uBean = new Genre();
				uBean.setGenreID(rs.getInt("GenreID"));
				uBean.setGenre(rs.getString("Genre"));
				System.out.println(".....done writing to bean!.....");
			}
		} catch (Exception e) {
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return uBean;
	}

	// insert new genre to db
	public int insertGenre(Genre genre) throws SQLException {
		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "INSERT INTO Genre (Genre) VALUES (?)";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, genre.getGenre());
			nrow = pstmt.executeUpdate();
			System.out.println("...in GenreDB-done insert genre data...");
		} catch (Exception e) {
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return nrow;
	}

	// update genre data to db
	public int updateGenre(Genre genre) throws SQLException {
		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE Genre SET Genre=? WHERE GenreID=?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, genre.getGenre());
			pstmt.setInt(2, genre.getGenreID());
			nrow = pstmt.executeUpdate();
			System.out.println("...in GenreDB-done update genre data...");
		} catch (Exception e) {
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return nrow;
	}

	// delete genre from db
	public int deleteGenre(int ID) throws SQLException {
		Connection conn = null;
		int nrow = 0;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "DELETE FROM Genre WHERE GenreID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);

			nrow = pstmt.executeUpdate();
			System.out.println("...in GenreDB-done delete genre...");
		} catch (Exception e) {
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return nrow;
	}

	public ArrayList<Book> getBookByGenreID(int ID) throws SQLException {
		ArrayList<Book> bookList = new ArrayList<>();
		Connection conn = null;
		Book uBean = null;
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Book b JOIN BookGenre g ON b.ISBNNo = g.ISBNNo WHERE g.GenreID=? ;";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setInt(1, ID);
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
			System.out.println(".......genreDB : " + e);
		} finally {
			conn.close();
		}
		return bookList;
	}

}