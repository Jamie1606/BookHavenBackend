//Author 	  : Thu Htet San
//Admin No    : 2235022
//Class       : DIT/FT/2A/02
//Group       : 10
//Date		  : 7.6.2023
//Description : to store Genre data from database

package com.bookshop.bookhaven.model;

public class Genre {

	private int genreID;
	private String genre;

	public int getGenreID() {
		return genreID;
	}

	public void setGenreID(int genreID) {
		this.genreID = genreID;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
}