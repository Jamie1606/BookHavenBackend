// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: middleware for author

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Author;
import com.bookshop.bookhaven.model.AuthorDatabase;

@RestController
public class AuthorController {
	
	@RequestMapping(path = "/getAllAuthor", method = RequestMethod.GET)
	public ArrayList<Author> getAllAuthor() {
		ArrayList<Author> authorList = new ArrayList<Author>();
		
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			authorList = author_db.getAuthors();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return authorList;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAuthor/{id}")
	public Author getAuthor(@PathVariable("id") String authorid) {
		Author author = new Author();
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			author = author_db.getAuthorByID(Integer.parseInt(authorid));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return author;
	}
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/createAuthor")
	public int createAuthor(@RequestBody Author author) {
		int row = 0;
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			row = author_db.createAuthor(author);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	
	@RequestMapping(method = RequestMethod.PUT,
			consumes = "application/json",
			path = "/updateAuthor/{id}")
	public int updateAuthor(@PathVariable("id") String authorid, @RequestBody Author author) {
		int row = 0;
		try {
			author.setAuthorID(Integer.parseInt(authorid));
			AuthorDatabase author_db = new AuthorDatabase();
			row = author_db.updateAuthor(author);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteAuthor/{id}")
	public int deleteAuthor(@PathVariable("id") String authorid) {
		int row = 0;
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			row = author_db.deleteAuthor(Integer.parseInt(authorid));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}
}