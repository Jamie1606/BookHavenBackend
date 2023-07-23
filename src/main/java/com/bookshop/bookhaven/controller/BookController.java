// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: middleware for author

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BookController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

	@RequestMapping(path = "/getRelated/{isbn}/{limit}", method = RequestMethod.GET)
	public String getRelated(@PathVariable("isbn") String isbn, @PathVariable("limit") String limit) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			ArrayList<Genre> genreList = new ArrayList<Genre>();
			BookDatabase book_db = new BookDatabase();
			genreList = book_db.getGenreByISBN(isbn);
			
			for(int i = 0; i < genreList.size(); i++) {
				ArrayList<Book> temp = book_db.getBookByGenreID(genreList.get(i).getGenreID(), isbn);
				for(int j = 0; j < temp.size(); j++) {
					bookList.add(temp.get(j));
					if(bookList.size() == Integer.parseInt(limit)) {
						break;
					}
				}
			}
			
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}
	
	
	@RequestMapping(path = "/getLatest/{no}", method = RequestMethod.GET)
	public String getLatest(@PathVariable String no) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			bookList = book_db.getLatest(Integer.parseInt(no));
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}
	
	
	@RequestMapping(path = "/getAllBook", method = RequestMethod.GET)
	public String getAllBook() {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			bookList = book_db.getBooks();
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}

	@RequestMapping(path = "/getAllBook/details", method = RequestMethod.GET)
	public String getAllBookDetails() {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			bookList = book_db.getBooks();
			if (bookList != null && bookList.size() > 0) {
				for (int i = 0; i < bookList.size(); i++) {
					bookList.get(i).setAuthors(book_db.getAuthorByISBN(bookList.get(i).getISBNNo()));
					bookList.get(i).setGenres(book_db.getGenreByISBN(bookList.get(i).getISBNNo()));
				}
			}
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/{isbn}")
	public ResponseEntity<?> getBook(@PathVariable String isbn) {
		Book book = new Book();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			book = book_db.getBookByISBN(isbn);
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(book);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Book does not exist!");
		}
		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/details/{isbn}")
	public ResponseEntity<?> getBookDetails(@PathVariable String isbn) {
		Book book = new Book();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			book = book_db.getBookByISBN(isbn);
			if(book != null) {
				book.setAuthors(book_db.getAuthorByISBN(isbn));
				book.setGenres(book_db.getGenreByISBN(isbn));
			}
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(book);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Book does not exist!");
		}
		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", path = "/createBook")
	public int createBook(@RequestBody Book book) {
//		try {
//			ObjectMapper obj = new ObjectMapper();
//			String jsonBook = obj.writeValueAsString(book);
//			LOGGER.info("JSON data: ", jsonBook);
//			System.out.println(jsonBook);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		int row = 0;
		try {
			BookDatabase book_db = new BookDatabase();
			if (book_db.getBookByISBN(book.getISBNNo()) == null) {
				row = book_db.createBook(book);
				if (row == 1) {
					if (book_db.createBookAuthor(book.getISBNNo(), book.getAuthors()) == book.getAuthors().size()) {
						if (book_db.createBookGenre(book.getISBNNo(), book.getGenres()) == book.getGenres().size()) {
							row = 1;
						}
						else {
							row = -2;
						}
					}
					else {
						row = -2;
					}
				}
			} else {
				row = -1; // setting -1 for row (duplicate isbnno)
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return row;
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", path = "/updateBook/{isbn}")
	public int updateBook(@PathVariable String isbn, @RequestBody Book book) {
//		try {
//			ObjectMapper obj = new ObjectMapper();
//			String jsonBook = obj.writeValueAsString(book);
//			LOGGER.info("JSON data: ", jsonBook);
//			System.out.println(jsonBook);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		int row = 0;
		try {
			BookDatabase book_db = new BookDatabase();
			if (book_db.getBookByISBN(isbn) != null) {
				row = book_db.updateBook(isbn, book);
				if (row == 1) {
					
					// get BookAuthor row count
					// check BookAuthor data is deleted
					if(book_db.getBookAuthorCount(isbn, 0) == book_db.deleteBookAuthor(isbn, 0)) {		
						
						
						// get BookGenre row count
						// check BookGenre data is deleted
						if(book_db.getBookGenreCount(isbn, 0) == book_db.deleteBookGenre(isbn, 0)) {	
	
							// check all BookAuthor is inserted
							if(book_db.createBookAuthor(book.getISBNNo(), book.getAuthors()) == book.getAuthors().size()) {		
								
								// check all BookGenre is inserted
								if(book_db.createBookGenre(book.getISBNNo(), book.getGenres()) == book.getGenres().size()) {	
									row = 1;									
								}
								// not all BookGenre are inserted
								else {	
									row = 0;
								}
							}
							// not all BookAuthor are not inserted
							else {	
								row = 0;
							}
						}
						// some BookGenre are not deleted
						else {	
							row = 0;
						}
					}
					// some BookAuthor are not deleted
					else {	
						row = 0;
					}
				}
			} 
			// invalid isbn (not existing in Book table)
			else {
				row = 0; 	 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return row;
	}
	
	
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteBook/{isbn}")
	public int deleteBook(@PathVariable String isbn) {
//		try {
//			ObjectMapper obj = new ObjectMapper();
//			String jsonBook = obj.writeValueAsString(book);
//			LOGGER.info("JSON data: ", jsonBook);
//			System.out.println(jsonBook);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		// image delete left
		int row = 0;
		try {
			BookDatabase book_db = new BookDatabase();
			if(book_db.getBookAuthorCount(isbn, 0) == book_db.deleteBookAuthor(isbn, 0)) {
				if(book_db.getBookGenreCount(isbn, 0) == book_db.deleteBookGenre(isbn, 0)) {
					row = book_db.deleteBook(isbn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return row;
	}
	

	// check isbn is referenced from the following website
	// https://isbn-information.com/check-digit-for-the-13-digit-isbn.html
	@RequestMapping(method = RequestMethod.GET, path = "/checkISBN13/{isbn}")
	public boolean checkISBN13(@PathVariable String isbn) {
		boolean condition = false;
		ArrayList<Integer> isbnInt = new ArrayList<Integer>();
		try {
			char[] testChars = isbn.toCharArray();
			for (char c : testChars) {
				if (Character.isDigit(c)) {
					isbnInt.add(Integer.parseInt(String.valueOf(c)));
				}
			}
			if (isbnInt.size() == 13) {
				int total = 0;
				for (int i = 0; i < 12; i++) {
					if (i % 2 == 0) {
						total += (isbnInt.get(i) * 1);
					} else {
						total += (isbnInt.get(i) * 3);
					}
				}
				total = total % 10;
				if (total != 0) {
					total = 10 - total;
				}
				if (isbnInt.get(isbnInt.size() - 1) == total) {
					condition = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return condition;
	}
}