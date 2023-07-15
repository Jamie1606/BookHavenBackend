package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDatabase {

	// insert book into database
	public int createBook(Book book) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, book.getISBNNo());
			st.setString(2, book.getTitle());
			st.setInt(3, book.getPage());
			st.setDouble(4, book.getPrice());
			st.setString(5, book.getPublisher());
			st.setDate(6, Date.valueOf(book.getPublicationDate().toString()));
			st.setInt(7, book.getQty());
			st.setDouble(8, book.getRating());
			st.setString(9, book.getDescription());
			st.setString(10, book.getImage());
			st.setString(11, book.getImage3D());
			st.setString(12, book.getStatus());

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createBook in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		return rowsAffected;
	}
}