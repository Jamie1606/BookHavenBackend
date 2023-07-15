// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: middleware for author

package com.bookshop.bookhaven.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.BookDatabase;

@RestController
public class BookController {
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/createBook")
	public int createBook(@RequestBody Book book) {
		int row = 0;
		try {
			BookDatabase book_db = new BookDatabase();
			row = book_db.createBook(book);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}
}