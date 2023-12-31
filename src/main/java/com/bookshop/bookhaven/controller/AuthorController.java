// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: middleware for author

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

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

import com.bookshop.bookhaven.model.Author;
import com.bookshop.bookhaven.model.AuthorDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthorController {
	
	
	@RequestMapping(path = "/getAllAuthor", method = RequestMethod.GET)
	@Cacheable("authorList")
	public ResponseEntity<?> getAllAuthor() {
		
		String json = null;
		ArrayList<Author> authorList = new ArrayList<Author>();
		
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			authorList = author_db.getAuthors();
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(authorList);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
		return ResponseEntity.ok().body(json);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAuthor/{id}")
	@Cacheable(value = "authorById", key = "#id + '-author'")
	public ResponseEntity<?> getAuthor(@PathVariable("id") String authorid) {
		
		Author author = new Author();
		String json = null;
		
		try {
			AuthorDatabase author_db = new AuthorDatabase();
			author = author_db.getAuthorByID(Integer.parseInt(authorid));
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(author);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
		
		return ResponseEntity.ok().body(json);
	}
	
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/createAuthor")
	@CacheEvict(value = "authorList", allEntries = true)
	public ResponseEntity<?> createAuthor(@RequestBody Author author, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				AuthorDatabase author_db = new AuthorDatabase();
				row = author_db.createAuthor(author);
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(0);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(row);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT,
			consumes = "application/json",
			path = "/updateAuthor/{id}")
	@Caching(evict = {
		@CacheEvict(value = "authorList", allEntries = true),
		@CacheEvict(value = "authorById", key = "#id + '-author'")
	})
	@CacheEvict({"authorList", "authorById"})
	public ResponseEntity<?> updateAuthor(@PathVariable("id") String authorid, @RequestBody Author author, HttpServletRequest request) {
		
		int row = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				AuthorDatabase author_db = new AuthorDatabase();
				row = author_db.updateAuthor(Integer.parseInt(authorid), author);
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(0);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(row);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteAuthor/{id}")
	@Caching( evict = {
		@CacheEvict(value = "authorList", allEntries = true),
		@CacheEvict(value = "authorById", key = "#id + '-author'")
	})
	public ResponseEntity<?> deleteAuthor(@PathVariable("id") String authorid, HttpServletRequest request) {
		
		int row = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				AuthorDatabase author_db = new AuthorDatabase();
				row = author_db.deleteAuthor(Integer.parseInt(authorid));
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(0);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.ok().body(row);
	}
}