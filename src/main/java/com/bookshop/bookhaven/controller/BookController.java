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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.BookDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BookController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/createBook")
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
			if(book_db.getBookByISBN(book.getISBNNo()) == null) {
				row = book_db.createBook(book);
				if(row == 1) {
					row = book_db.createBookAuthor(book.getISBNNo(), book.getAuthors());
					if(row == book.getAuthors().size()) {
						row = book_db.createBookGenre(book.getISBNNo(), book.getGenres());
						if(row == book.getGenres().size()) {
							row = 1;
						}
					}
				}
			}
			else {
				row = -1;		// setting -1 for row (duplicate isbnno)
			}
		}
		catch(Exception e) {
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
			for(char c: testChars) {
				if(Character.isDigit(c)) {
					isbnInt.add(Integer.parseInt(String.valueOf(c)));
				}
			}
			if(isbnInt.size() == 13) {
				int total = 0;
				for(int i = 0; i < 12; i++) { 
					if(i % 2 == 0) {
						total += (isbnInt.get(i) * 1);
					}
					else {
						total += (isbnInt.get(i) * 3);
					}
				}
				total = total % 10;
				if(total != 0) {
					total = 10 - total;
				}
				if(isbnInt.get(isbnInt.size() - 1) == total) {
					condition = true;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return condition;
	}
}