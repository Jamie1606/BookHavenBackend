// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: to store book data

package com.bookshop.bookhaven.model;

import java.sql.Date;
import java.util.ArrayList;

public class Book {
	private String isbnno;
	private String title;
	private int page;
	private double price;
	private String publisher;
	private Date publicationDate;
	private int qty;
	private double rating;
	private int ratingcount;
	private int soldqty;
	private String description;
	private String image;
	private String image3D;
	private String status;
	private ArrayList<Author> authors;
	private ArrayList<Genre> genres;

	public int getRatingcount() {
		return ratingcount;
	}

	public void setRatingcount(int ratingcount) {
		this.ratingcount = ratingcount;
	}

	public int getSoldqty() {
		return soldqty;
	}

	public void setSoldqty(int soldqty) {
		this.soldqty = soldqty;
	}

	public String getISBNNo() {
		return isbnno;
	}
	
	public void setISBNNo(String isbnno) {
		this.isbnno = isbnno;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public Date getPublicationDate() {
		return publicationDate;
	}
	
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	public int getQty() {
		return qty;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public double getRating() {
		return rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage3D() {
		return image3D;
	}
	
	public void setImage3D(String image3d) {
		image3D = image3d;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}
	
	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}
	
	public ArrayList<Genre> getGenres() {
		return genres;
	}
	
	public void setGenres(ArrayList<Genre> genres) {
		this.genres = genres;
	}
}