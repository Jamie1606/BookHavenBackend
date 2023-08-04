// Author 	  : Thu Htet San
// Admin No    : 2235022
// Class       : DIT/FT/2A/02
// Group       : 10
// Date		  : 20.7.2023
// Description : middleware for genre

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Genre;
import com.bookshop.bookhaven.model.GenreDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import com.bookshop.bookhaven.model.Book;

@RestController
public class GenreController {

	@RequestMapping(method = RequestMethod.GET, path = "/getAllGenre")
	public ResponseEntity<?> getAllGenre() {

		String json = null;
		ArrayList<Genre> genreList = new ArrayList<Genre>();

		try {
			GenreDatabase genre_db = new GenreDatabase();
			genreList = genre_db.getAllGenre();
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(genreList);
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(null);
		}

		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getGenre/{id}")
	public ResponseEntity<?> getGenre(@PathVariable("id") int genreID) {
		String json = null;
		Genre genre = new Genre();
		try {
			GenreDatabase genre_db = new GenreDatabase();
			genre = genre_db.getGenreByID(genreID);
			if (genre == null) {
				return ResponseEntity.badRequest().body("Member does not exist!");
			}
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(genre);
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(null);
		}
		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/createGenre", consumes = "application/json")
	public ResponseEntity<?> createGenre(@RequestBody Genre genre, HttpServletRequest request) {

		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int nrow = 0;
		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				GenreDatabase genre_db = new GenreDatabase();
				System.out.println(".....inside genre controller.....");

				nrow = genre_db.insertGenre(genre);
				System.out.println(".....done create genre.....");

			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(0);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(nrow);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateGenre", consumes = "application/json")
	public ResponseEntity<?> updateGenre(@RequestBody Genre genre, HttpServletRequest request) {
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int nrow = 0;
		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				GenreDatabase genre_db = new GenreDatabase();
				System.out.println(".....inside genre controller.....");
				nrow = genre_db.updateGenre(genre);
				System.out.println(".....done update genre.....");
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(0);
			}
		} else {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(nrow);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteGenre/{id}", consumes = "application/json")
	public ResponseEntity<?> deleteGenre(@PathVariable("id") int genreID, HttpServletRequest request) {

		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int nrow = 0;
		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				GenreDatabase genre_db = new GenreDatabase();
				System.out.println(".....inside genre controller.....");
				nrow = genre_db.deleteGenre(genreID);
				System.out.println(".....done delete genre.....");
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(0);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok().body(nrow);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBookByGenreID/{id}")
	public ResponseEntity<?> getBookByGenreID(@PathVariable("id") int genreID) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String json = null;
		try {
			GenreDatabase genre_db = new GenreDatabase();
			if (genre_db.getGenreByID(genreID) != null) {
				bookList = genre_db.getBookByGenreID(genreID);
				if (bookList == null) {
					return ResponseEntity.badRequest().body("Book does not exist!");
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(bookList);
			} else {
				return ResponseEntity.badRequest().body("Genre does not exist!");
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(null);
		}
		return ResponseEntity.ok().body(json);
	}
}