// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date		  	: 6.8.2023
// Description 	: middleware for search Books

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Book;
import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.SearchDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SearchController {

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/title/{bookTitle}")
	public String getBookByBookTitle(@PathVariable("bookTitle") String title) {
		
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = "";
		
		try {
			
			SearchDatabase search_db = new SearchDatabase();
			bookList = search_db.getBookByBookTitle(title);
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} 
		catch (Exception e) {
			System.out.println("Error :" + e);
			return null;
		}
		return json;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBook/author/{name}")
	public String getBookByAuthorName(@PathVariable("name") String authorName) {
		
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = "";
		
		try {
			SearchDatabase search_db = new SearchDatabase();
			BookDatabase book_db = new BookDatabase();
			bookList = search_db.getBookByAuthorName(authorName);
			if(bookList != null && bookList.size() > 0) {
				for(int i = 0; i < bookList.size(); i++) {
					bookList.get(i).setAuthors(book_db.getAuthorByISBN(bookList.get(i).getISBNNo()));
				}
			}
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(bookList);
		} 
		catch (Exception e) {
			System.out.println("Error :" + e);
			return null;
		}
		
		return json;
	}
}
