//Author 	  : Thu Htet San
//Admin No    : 2235022
//Class       : DIT/FT/2A/02
//Group       : 10
//Date		  : 20.7.2023
//Description : middleware for genre

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

import com.bookshop.bookhaven.model.Book;

@RestController
public class GenreController {

	@RequestMapping(method = RequestMethod.GET, path = "/getAllGenre")
	public ArrayList<Genre> getAllGenre() {
		ArrayList<Genre> genreList = new ArrayList<Genre>();

		try {
			GenreDatabase genre_db = new GenreDatabase();
			genreList = genre_db.getAllGenre();
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return genreList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getGenre/{uid}")
	public ResponseEntity<?> getGenre(@PathVariable("uid") int genreID) {
		Genre genre = new Genre();
		try {
			GenreDatabase genre_db = new GenreDatabase();
			genre = genre_db.getGenreByID(genreID);
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		if (genre == null) {
			return new ResponseEntity<>("Genre does not exist!", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(genre, HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, path = "/createGenre", consumes = "application/json")
	public int createGenre(@RequestBody Genre genre) {

		int nrow = 0;
		try {
			GenreDatabase genre_db = new GenreDatabase();
			System.out.println(".....inside genre controller.....");

			nrow = genre_db.insertGenre(genre);
			System.out.println(".....done create genre.....");

		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateGenre", consumes = "application/json")
	public int updateGenre(@RequestBody Genre genre) {

		int nrow = 0;
		try {
			GenreDatabase genre_db = new GenreDatabase();
			System.out.println(".....inside genre controller.....");
			nrow = genre_db.updateGenre(genre);
			System.out.println(".....done update genre.....");
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteGenre/{uid}", consumes = "application/json")
	public int deleteGenre(@PathVariable("uid") int genreID) {
		int nrow = 0;
		try {
			GenreDatabase genre_db = new GenreDatabase();
			System.out.println(".....inside genre controller.....");
			nrow = genre_db.deleteGenre(genreID);
			System.out.println(".....done delete genre.....");
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBookByGenreID/{uid}")
	public ResponseEntity<?> getBookByGenreID(@PathVariable("uid") int genreID) {
		ArrayList<Book> bookList = new ArrayList<Book>();

		try {
			GenreDatabase genre_db = new GenreDatabase();
			if (genre_db.getGenreByID(genreID) != null) {
				bookList = genre_db.getBookByGenreID(genreID);
			} else {
				return new ResponseEntity<>("Genre does not exist!", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return new ResponseEntity<>(bookList, HttpStatus.OK);
	}
}