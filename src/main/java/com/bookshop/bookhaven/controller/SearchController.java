// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date		  	: 24.7.2023
// Description 	: middleware for search Books

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.SearchDatabase;

@RestController
public class SearchController {

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/title/{bookTitle}")
	public ResponseEntity<?> getBookByBookTitle(@PathVariable("bookTitle") String title) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			SearchDatabase search_db = new SearchDatabase();
			bookList = search_db.getBookByBookTitle(title);
			if (bookList.isEmpty()) {
				return ResponseEntity.badRequest().body("Book does not exist!");
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(null);
		}
		return ResponseEntity.ok().body(bookList);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/author/{name}")
	public ResponseEntity<?> getBookByAuthorName(@PathVariable("name") String authorName) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			SearchDatabase search_db = new SearchDatabase();
			bookList = search_db.getBookByAuthorName(authorName);
			if (bookList.isEmpty()) {
				return ResponseEntity.badRequest().body("Book does not exist!");
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(null);
		}
		return ResponseEntity.ok().body(bookList);
	}
}
