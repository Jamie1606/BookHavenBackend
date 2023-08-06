// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: middleware for book

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.Genre;
import com.bookshop.bookhaven.model.ImageUploadRequest;
import com.bookshop.bookhaven.model.Order;
import com.bookshop.bookhaven.model.OrderDatabase;
import com.bookshop.bookhaven.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BookController {

	private String uploadImageAPI = "https://le5w8tau6b.execute-api.us-east-1.amazonaws.com/s3image";

	@RequestMapping(path = "/getBestSeller/{limit}", method = RequestMethod.GET)
	@Cacheable("bestseller")
	public String getBestSeller(@PathVariable String limit) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			bookList = book_db.getBestSeller(Integer.parseInt(limit));

			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(path = "/getBestSelling/{limit}", method = RequestMethod.GET)
	public ResponseEntity<?> getBestSelling(@PathVariable String limit, HttpServletRequest request) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			System.out.println(
					(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) + "true or false");
			try {
				BookDatabase book_db = new BookDatabase();
				bookList = book_db.getBestSeller(Integer.parseInt(limit));

				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(bookList);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(path = "/getLeastSelling/{limit}", method = RequestMethod.GET)
	public ResponseEntity<?> getLeastSelling(@PathVariable String limit, HttpServletRequest request) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				BookDatabase book_db = new BookDatabase();
				bookList = book_db.getLeastSeller(Integer.parseInt(limit));

				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(bookList);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(path = "/getLowStock", method = RequestMethod.GET)
	public ResponseEntity<?> getLowStock(HttpServletRequest request) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				BookDatabase book_db = new BookDatabase();
				bookList = book_db.getLowStock();

				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(bookList);
			} catch (Exception e) {
				e.printStackTrace();

				return ResponseEntity.internalServerError().body(null);

			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(json);
	}
	
	@RequestMapping(path = "/getBookByDate/{startDate}/{endDate}", method = RequestMethod.GET)
	public ResponseEntity<?> getBookByDate(@PathVariable String startDate,
	        @PathVariable String endDate, HttpServletRequest request) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		System.out.println(startDate);
		System.out.println(endDate);

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				OrderDatabase order_db = new OrderDatabase();
				BookDatabase book_db = new BookDatabase();
				ArrayList<Order> orderList = order_db.getOrdersbyDate(startDate, endDate);

				for (Order order : orderList) {
					
					System.out.println("order.getOrderid()" + order.getOrderid());
				    int orderID = order.getOrderid();
			        bookList = book_db.getBookByOrderID(orderID);
			        System.out.println("booklist" + bookList.size());

					ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
				    for (int i = 0; i < bookList.size(); i++) {
				    	OrderItem item = new OrderItem();
				    	item.setBook(bookList.get(i));
				    	orderItemList.add(item);
				    }
				    order.setOrderitems(orderItemList);
				}

				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(orderList);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(json);
	}
	
	@RequestMapping(path = "/getTopRated/{limit}", method = RequestMethod.GET)
	@Cacheable("toprated")
	public String getTopRated(@PathVariable String limit) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			bookList = book_db.getTopRated(Integer.parseInt(limit));

			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(path = "/getBookByAuthorID/{id}", method = RequestMethod.GET)
	@Cacheable(value = "bookList", key = "'author-' + #id + '-books'")
	public String getBookByAuthorID(@PathVariable("id") String id) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			int[] authorID = new int[] { Integer.parseInt(id) };
			bookList = book_db.getBookByAuthorID(authorID, "");

			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(path = "/getRelated/{isbn}/{limit}", method = RequestMethod.GET)
	@Cacheable(value = "bookList", key = "#isbn + '-related'")
	public String getRelated(@PathVariable("isbn") String isbn, @PathVariable("limit") String limit) {

		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;

		try {
			ArrayList<Genre> genreList = new ArrayList<Genre>();
			BookDatabase book_db = new BookDatabase();
			genreList = book_db.getGenreByISBN(isbn);
			int[] genreID = new int[genreList.size()];

			for (int i = 0; i < genreList.size(); i++) {
				genreID[i] = genreList.get(i).getGenreID();
			}

			
			ArrayList<Book> tempBook = book_db.getBookByGenreID(genreID, isbn);
			Collections.shuffle(tempBook);
			int limitInt = Integer.parseInt(limit);
			for(int i = 0; i < tempBook.size(); i++) {
				boolean condition = true;
				for(Book book2: bookList) {
					if(tempBook.get(i).getISBNNo().equals(book2.getISBNNo())) {
						condition = false;
						break;
					}
				}
				if(condition) {
					bookList.add(tempBook.get(i));
				}
				if(i >= limitInt)
					break;
			}
			

			bookList = book_db.getBookByGenreID(genreID, isbn);
			Collections.shuffle(bookList);
			bookList = new ArrayList<>(bookList.subList(0, Math.min(Integer.parseInt(limit), bookList.size())));

			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(path = "/getLatest/{no}", method = RequestMethod.GET)
	@Cacheable(value = "bookList", key = "'latest'")
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
	@Cacheable(value = "bookList", key = "'simple'")
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
	@Cacheable(value = "bookList", key = "'details'")
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
	@Cacheable(value = "bookByISBN", key = "#isbn + '-simple'")
	public String getBook(@PathVariable String isbn) {

		Book book = new Book();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			book = book_db.getBookByISBN(isbn);
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(book);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/details/{isbn}")
	@Cacheable(value = "bookByISBN", key = "#isbn + '-details'")
	public String getBookDetails(@PathVariable String isbn) {

		Book book = new Book();
		String json = null;

		try {
			BookDatabase book_db = new BookDatabase();
			book = book_db.getBookByISBN(isbn);
			if (book != null) {
				book.setAuthors(book_db.getAuthorByISBN(isbn));
				book.setGenres(book_db.getGenreByISBN(isbn));
			}
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(book);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", path = "/createBook")
	@Caching(evict = { @CacheEvict(value = "bookList", allEntries = true) })
	public ResponseEntity<?> createBook(@RequestBody Book book, HttpServletRequest request) {

		int row = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {

			try {
				BookDatabase book_db = new BookDatabase();
				if (book_db.getBookByISBN(book.getISBNNo()) == null) {
					row = book_db.createBook(book);
					if (row == 1) {
						if (book_db.createBookAuthor(book.getISBNNo(), book.getAuthors()) == book.getAuthors().size()) {
							if (book_db.createBookGenre(book.getISBNNo(), book.getGenres()) == book.getGenres()
									.size()) {
								row = 1;
							} else {
								row = -2;
							}
						} else {
							row = -2;
						}
					}
				} else {
					row = -1; // setting -1 for row (duplicate isbnno)
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(row);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(row);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", path = "/updateBook/{isbn}")
	@Caching(evict = { @CacheEvict(value = "bookList", allEntries = true),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-simple'"),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-details'") })
	public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody Book book, HttpServletRequest request) {

		int row = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				BookDatabase book_db = new BookDatabase();
				if (book_db.getBookByISBN(isbn) != null) {
					row = book_db.updateBook(isbn, book);
					if (row == 1) {

						// get BookAuthor row count
						// check BookAuthor data is deleted
						if (book_db.getBookAuthorCount(isbn, 0) == book_db.deleteBookAuthor(isbn, 0)) {

							// get BookGenre row count
							// check BookGenre data is deleted
							if (book_db.getBookGenreCount(isbn, 0) == book_db.deleteBookGenre(isbn, 0)) {

								// check all BookAuthor is inserted
								if (book_db.createBookAuthor(book.getISBNNo(), book.getAuthors()) == book.getAuthors()
										.size()) {

									// check all BookGenre is inserted
									if (book_db.createBookGenre(book.getISBNNo(), book.getGenres()) == book.getGenres()
											.size()) {
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
				return ResponseEntity.internalServerError().body(0);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(row);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteBook/{isbn}")
	@Caching(evict = { @CacheEvict(value = "bookList", allEntries = true),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-simple'"),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-details'"), @CacheEvict(value = "toprated"),
			@CacheEvict(value = "bestseller") })
	public ResponseEntity<?> deleteBook(@PathVariable String isbn, HttpServletRequest request) {
		int row = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			// image delete left

			try {
				BookDatabase book_db = new BookDatabase();

				ImageUploadRequest imagerequest = new ImageUploadRequest();
				Book book = book_db.getBookByISBN(isbn);
				String[] image = book.getImage().split("/");
				String[] image3d = book.getImage3D().split("/");
				if (imagerequest.deleteImage(uploadImageAPI, "booknormal", image[image.length - 1])) {
					if (imagerequest.deleteImage(uploadImageAPI, "book3d", image3d[image3d.length - 1])) {
						if (book_db.getBookAuthorCount(isbn, 0) == book_db.deleteBookAuthor(isbn, 0)) {
							if (book_db.getBookGenreCount(isbn, 0) == book_db.deleteBookGenre(isbn, 0)) {
								row = book_db.deleteBook(isbn);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(0);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(row);
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