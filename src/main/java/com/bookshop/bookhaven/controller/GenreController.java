package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Genre;
import com.bookshop.bookhaven.model.GenreDatabase;

@RestController
public class GenreController {
	@RequestMapping(path = "/getAllGenre", method = RequestMethod.GET)
	public ArrayList<Genre> getAllGenre() {
		ArrayList<Genre> genreList = new ArrayList<Genre>();
		
		try {
			GenreDatabase genre_db = new GenreDatabase();
			genreList = genre_db.getGenres();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return genreList;
	}
}