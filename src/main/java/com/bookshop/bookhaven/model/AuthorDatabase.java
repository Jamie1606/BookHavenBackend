// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: all database functions related to author

package com.bookshop.bookhaven.model;

import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.text.StringEscapeUtils;

public class AuthorDatabase {
	
	// get all author data from database
	public ArrayList<Author> getAuthors() throws SQLException {
		Connection conn = null;
		ArrayList<Author> authors = new ArrayList<Author>();
		try {			
			// connecting to database
			conn = DatabaseConnection.getConnection();
			
			// get author data ordered by name ascending
			String sqlStatement = "SELECT * FROM Author ORDER BY Name";
			PreparedStatement st = conn.prepareStatement(sqlStatement);

			ResultSet rs = st.executeQuery();
			
			// author data is added to arraylist
			// escaping html special characters
			while(rs.next()) {
				Author author = new Author();
				author.setAuthorID(rs.getInt("AuthorID"));
				author.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				author.setNationality(StringEscapeUtils.escapeHtml4(rs.getString("Nationality")));
				author.setBirthDate(rs.getDate("BirthDate"));
				author.setBiography(StringEscapeUtils.escapeHtml4(rs.getString("Biography")));
				author.setLink(StringEscapeUtils.escapeHtml4(rs.getString("Link")));;
				authors.add(author);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getAuthor in AuthorDatabase .....");
		}
		finally {
			conn.close();
		}
		return authors;
	}

	// get one author data by authorID from database
	public Author getAuthorByID(int id) throws SQLException {
		Author author = null;
		Connection conn = null;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			// get author data by author id
			String sqlStatement = "SELECT * FROM Author WHERE AuthorID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			// escaping html special characters
			// author data is added to author object
			if(rs.next()) {
				author = new Author();
				author.setAuthorID(rs.getInt("AuthorID"));
				author.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				author.setNationality(StringEscapeUtils.escapeHtml4(rs.getString("Nationality")));
				author.setBirthDate(rs.getDate("BirthDate"));
				author.setBiography(StringEscapeUtils.escapeHtml4(rs.getString("Biography")));
				author.setLink(StringEscapeUtils.escapeHtml4(rs.getString("Link")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getAuthorByID in AuthorDatabase .....");
		}
		finally {
			conn.close();
		}
		return author;
	}

	// insert author into database
	public int createAuthor(Author author) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "INSERT INTO Author (Name, Nationality, BirthDate, Biography, Link) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, author.getName());
			st.setString(2, author.getNationality());
			
			// checking null value for birthdate
			if (author.getBirthDate() == null) {
				st.setNull(3, Types.DATE);
			} else {
				st.setDate(3, Date.valueOf(author.getBirthDate().toString()));
			}
			
			st.setString(4, author.getBiography());
			st.setString(5, author.getLink());

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in registerAuthor in AuthorDatabase .....");
		}
		finally {
			conn.close();
		}
		return rowsAffected;
	}

	// update author by author id in database 
	public int updateAuthor(Author author) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT * FROM Author WHERE AuthorID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, author.getAuthorID());
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				sqlStatement = "UPDATE Author SET Name = ?, Nationality = ?, BirthDate = ?, Biography = ?, Link = ? WHERE AuthorID = ?";
				st = conn.prepareStatement(sqlStatement);
				st.setString(1, author.getName());
				st.setString(2, author.getNationality());
	
				// check null value for birthdate
				if (author.getBirthDate() == null) {
					st.setNull(3, Types.DATE);
				} else {
					st.setDate(3, Date.valueOf(author.getBirthDate().toString()));
				}
				
				st.setString(4, author.getBiography());
				st.setString(5, author.getLink());
				st.setInt(6, author.getAuthorID());
	
				rowsAffected = st.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in updateAuthor in AuthorDatabase .....");
		}
		finally {
			conn.close();
		}
		return rowsAffected;
	}

	// delete author by author id in database
	public int deleteAuthor(int id) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "DELETE FROM Author WHERE AuthorID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, id);

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in deleteAuthor in AuthorDatabase .....");
		}
		finally {
			conn.close();
		}
		return rowsAffected;
	}
}