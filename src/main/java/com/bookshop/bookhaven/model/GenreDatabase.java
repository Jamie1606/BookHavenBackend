package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class GenreDatabase {
	// get all genre data from database
	public ArrayList<Genre> getGenres() throws SQLException {
		Connection conn = null;
		ArrayList<Genre> genres = new ArrayList<Genre>();
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			// get genre data ordered by genre ascending
			String sqlStatement = "SELECT * FROM Genre ORDER BY Genre";
			PreparedStatement st = conn.prepareStatement(sqlStatement);

			ResultSet rs = st.executeQuery();

			// genre data is added to genrelist
			// escaping html special characters
			while (rs.next()) {
				Genre genre = new Genre();
				genre.setGenreID(rs.getInt("GenreID"));
				genre.setGenre(StringEscapeUtils.escapeHtml4(rs.getString("Genre")));
				genres.add(genre);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getGenres in GenreDatabase .....");
		} finally {
			conn.close();
		}
		return genres;
	}
}