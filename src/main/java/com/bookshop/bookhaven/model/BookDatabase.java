// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: book related database functions

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class BookDatabase {
	
	
	public int updateBookRating(String isbn, int rating) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE Book SET TotalRating = TotalRating + ?, RatingCount = RatingCount + 1, "
					+ "Rating = TotalRating / RatingCount "
					+ "WHERE ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, rating);
			st.setString(2, isbn);
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in updateBookRating in BookDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	public int increaseSoldQty(int qty, String isbnno) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE Book SET SoldQty = SoldQty + ? WHERE ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, qty);
			st.setString(2, isbnno);
			
			rowsAffected = st.executeUpdate();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in increaseSoldQty in BookDatabase .....");
		}
		
		return rowsAffected;
	}
	
	
	public ArrayList<Book> getBestSeller(int limit) throws SQLException {
		Connection conn = null;
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Book ORDER BY SoldQty DESC LIMIT ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, limit);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(book);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getTopRated in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return bookList;
	}
	
	public ArrayList<Book> getLeastSeller(int limit) throws SQLException {
		Connection conn = null;
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Book ORDER BY SoldQty ASC LIMIT ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, limit);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(book);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getLeastSeller in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return bookList;
	}
	
	public ArrayList<Book> getLowStock() throws SQLException {
		Connection conn = null;
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM bookhavendb.Book WHERE qty < 10;";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(book);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getLowStock in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return bookList;
	}
	
	public ArrayList<Book> getTopRated(int limit) throws SQLException {
		Connection conn = null;
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM Book ORDER BY Rating DESC LIMIT ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, limit);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				bookList.add(book);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getTopRated in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return bookList;
	}
	
	
	// if you want to subtract, give positive value
	// if you want to add, give negative value
	public int changeBookQty(ArrayList<OrderItem> items) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			conn = DatabaseConnection.getConnection();
			
			for(OrderItem item: items) {
				String sqlStatement = "UPDATE Book SET Qty = Qty - ?, "
						+ "Status = CASE WHEN (Qty - ?) <= 0 THEN 'unavailable' "
						+ "ELSE 'available' END WHERE ISBNNo = ?";
				PreparedStatement st = conn.prepareStatement(sqlStatement);
				
				st.setInt(1, item.getQty());
				st.setString(2, item.getIsbnno());
				
				rowsAffected += st.executeUpdate();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in reduceBookQty in BookDatabase .....");
		}
		
		return rowsAffected;
	}
	
	
	// get book by author id from database
	public ArrayList<Book> getBookByAuthorID(int[] id, String isbn) throws SQLException {
		Connection conn = null;
		ArrayList<Book> books = new ArrayList<Book>();
		
		try {
			conn = DatabaseConnection.getConnection();
			String authorIDstr = "";
			
			for(int i = 0; i < id.length; i++) {
				if(i == id.length - 1) {
					authorIDstr += "?";
				}
				else {
					authorIDstr += "?, ";
				}
			}
			
			String sqlStatement = "SELECT * FROM Book b, BookAuthor ba WHERE b.ISBNNo = ba.ISBNNo AND ba.AuthorID IN (" + authorIDstr + ") AND b.ISBNNo != ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			for(int i = 0; i < id.length; i++) {
				st.setInt(i + 1, id[i]);
			}
			st.setString(id.length + 1, isbn);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				books.add(book);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getBookByAuthorID in BookDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return books;
	}
	

	// get book by isbn from database
	public ArrayList<Book> getBookByGenreID(int[] id, String isbn) throws SQLException {
		Connection conn = null;
		ArrayList<Book> books = new ArrayList<Book>();

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();
			String genreIDstr = "";
			
			for(int i = 0; i < id.length; i++) {
				if(i == id.length - 1) {
					genreIDstr += "?";
				}
				else {
					genreIDstr += "?, ";
				}
			}

			String sqlStatement = "SELECT b.* FROM Book b, BookGenre bg WHERE b.ISBNNo = bg.ISBNNo AND bg.GenreID IN (" + genreIDstr + ") AND b.ISBNNo != ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			for(int i = 0; i < id.length; i++) {
				st.setInt(i + 1, id[i]);
			}
			st.setString(id.length + 1, isbn);

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				books.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getBookByGenreID in BookDatabase .....");
		} finally {
			conn.close();
		}
		
		return books;
	}

	
	// get all book data from database
	public ArrayList<Book> getLatest(int no) throws SQLException {
		Connection conn = null;
		ArrayList<Book> books = new ArrayList<Book>();
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			// get author data ordered by name ascending
			String sqlStatement = "SELECT * FROM Book WHERE Status = 'available' ORDER BY PublicationDate DESC LIMIT ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, no);

			ResultSet rs = st.executeQuery();

			// book data is added to arraylist
			// escaping html special characters
			while (rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setPublisher(rs.getString("Publisher"));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				books.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getBooks in BookDatabase .....");
		} finally {
			conn.close();
		}
		
		return books;
	}

	
	// get all book data from database
	public ArrayList<Book> getBooks() throws SQLException {
		Connection conn = null;
		ArrayList<Book> books = new ArrayList<Book>();
		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			// get author data ordered by name ascending
			String sqlStatement = "SELECT * FROM Book ORDER BY Title";
			PreparedStatement st = conn.prepareStatement(sqlStatement);

			ResultSet rs = st.executeQuery();

			// book data is added to arraylist
			// escaping html special characters
			while (rs.next()) {
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setPublisher(rs.getString("Publisher"));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				books.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getBooks in BookDatabase .....");
		} finally {
			conn.close();
		}
		
		return books;
	}

	
	// get book by isbn from database
	public Book getBookByISBN(String isbn) throws SQLException {
		Connection conn = null;
		Book book = null;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM Book WHERE ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);

			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setQty(rs.getInt("Qty"));
				book.setRating(rs.getDouble("Rating"));
				book.setRatingcount(rs.getInt("RatingCount"));
				book.setSoldqty(rs.getInt("SoldQty"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getBookByISBN in BookDatabase .....");
		} finally {
			conn.close();
		}
		return book;
	}

	
	// insert book into database
	public int createBook(Book book) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, book.getISBNNo());
			st.setString(2, book.getTitle());
			st.setInt(3, book.getPage());
			st.setDouble(4, book.getPrice());
			st.setString(5, book.getPublisher());
			st.setDate(6, Date.valueOf(book.getPublicationDate().toString()));
			st.setInt(7, book.getQty());
			st.setDouble(8, book.getRating());
			st.setInt(9, 0);
			st.setInt(10, 0);
			st.setInt(11, 0);
			st.setString(12, book.getDescription());
			st.setString(13, book.getImage());
			st.setString(14, book.getImage3D());
			st.setString(15, book.getStatus());

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createBook in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// insert book and author into database
	public int createBookAuthor(String isbn, ArrayList<Author> author) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			for (Author a : author) {
				String sqlStatement = "INSERT INTO BookAuthor VALUES (?, ?)";
				PreparedStatement st = conn.prepareStatement(sqlStatement);
				st.setString(1, isbn);
				st.setInt(2, a.getAuthorID());
				rowsAffected += st.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createBookAuthor in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// get author details by ISBNNo from database
	public ArrayList<Author> getAuthorByISBN(String isbn) throws SQLException {
		ArrayList<Author> authorList = new ArrayList<Author>();
		Connection conn = null;

		try {
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT a.* FROM BookAuthor ba, Author a WHERE a.AuthorID = ba.AuthorID AND ba.ISBNNo = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, isbn);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Author author = new Author();
				author.setAuthorID(rs.getInt("AuthorID"));
				author.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				author.setNationality(StringEscapeUtils.escapeHtml4(rs.getString("Nationality")));
				author.setBiography(StringEscapeUtils.escapeHtml4(rs.getString("Biography")));
				author.setLink(StringEscapeUtils.escapeHtml4(rs.getString("Link")));
				author.setBirthDate(rs.getDate("BirthDate"));
				authorList.add(author);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getAuthorByISBN in BookDatabase .....");
		} finally {
			conn.close();
		}

		return authorList;
	}

	
	// get genre details by ISBNNo from database
	public ArrayList<Genre> getGenreByISBN(String isbn) throws SQLException {
		ArrayList<Genre> genreList = new ArrayList<Genre>();
		Connection conn = null;

		try {
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT g.* FROM BookGenre bg, Genre g WHERE g.GenreID = bg.GenreID AND bg.ISBNNo = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
			pstmt.setString(1, isbn);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Genre genre = new Genre();
				genre.setGenreID(rs.getInt("GenreID"));
				genre.setGenre(StringEscapeUtils.escapeHtml4(rs.getString("Genre")));
				genreList.add(genre);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getGenreByISBN in BookDatabase .....");
		} finally {
			conn.close();
		}

		return genreList;
	}

	
	// insert book and genre into database
	public int createBookGenre(String isbn, ArrayList<Genre> genre) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			for (Genre g : genre) {
				String sqlStatement = "INSERT INTO BookGenre VALUES (?, ?)";
				PreparedStatement st = conn.prepareStatement(sqlStatement);
				st.setString(1, isbn);
				st.setInt(2, g.getGenreID());
				rowsAffected += st.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createBookGenre in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// update book into database
	public int updateBook(String isbn, Book book) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "UPDATE Book SET ISBNNo = ?, Title = ?, Page = ?, Price = ?, Publisher = ?, PublicationDate = ?, Qty = ?, Rating = ?, Description = ?, Image = ?, Image3D = ?, Status = ? WHERE ISBNNo = ?";
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
			st.setString(13, isbn);

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in updateBook in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// delete book data from Book table
	public int deleteBook(String isbn) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "DELETE FROM Book WHERE ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);

			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in deleteBook in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// get BookAuthor rows count
	public int getBookAuthorCount(String isbn, int authorid) throws SQLException {
		Connection conn = null;
		int count = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM BookAuthor WHERE ISBNNo = ? OR AuthorID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);
			st.setInt(2, authorid);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				isbn = rs.getString("ISBNNo");
				if (isbn != null && !isbn.isEmpty())
					count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
			System.out.println("..... Error in getBookAuthorCount in BookDatabase .....");
		} finally {
			conn.close();
		}
		return count;
	}

	
	// get BookGenre rows count
	public int getBookGenreCount(String isbn, int genreid) throws SQLException {
		Connection conn = null;
		int count = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "SELECT * FROM BookGenre WHERE ISBNNo = ? OR GenreID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);
			st.setInt(2, genreid);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				isbn = rs.getString("ISBNNo");
				if (isbn != null && !isbn.isEmpty())
					count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
			System.out.println("..... Error in getBookGenreCount in BookDatabase .....");
		} finally {
			conn.close();
		}
		return count;
	}

	
	// delete book and author from database
	public int deleteBookAuthor(String isbn, int authorid) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "DELETE FROM BookAuthor WHERE ISBNNo = ? OR AuthorID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);
			st.setInt(2, authorid);
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in deleteBookAuthor in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}

	
	// delete book and genre from database
	public int deleteBookGenre(String isbn, int genreid) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "DELETE FROM BookGenre WHERE ISBNNo = ? OR GenreID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setString(1, isbn);
			st.setInt(2, genreid);
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in deleteBookGenre in BookDatabase .....");
		} finally {
			conn.close();
		}
		return rowsAffected;
	}
}