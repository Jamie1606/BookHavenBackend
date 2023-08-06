package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.OrderDatabase;
import com.bookshop.bookhaven.model.Review;
import com.bookshop.bookhaven.model.ReviewDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ReviewController {
	
	@RequestMapping(method = RequestMethod.PUT, path = "/updateReview/{id}/{status}")
	public ResponseEntity<?> updateReview(@PathVariable("id") String reviewid, @PathVariable("status") String status, 
			HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			
			try {
				
				ReviewDatabase review_db = new ReviewDatabase();
				row = review_db.updateReviewStatus(Integer.parseInt(reviewid), Integer.parseInt(id), status);
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
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAllReview")
	public ResponseEntity<?> getAllReview(HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		ArrayList<Review> reviewList = new ArrayList<Review>();
		String json = "";
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				
				ReviewDatabase review_db = new ReviewDatabase();
				reviewList = review_db.getAllReview();
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(reviewList);
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(null);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.ok().body(json);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/getReview/{isbn}")
	public ResponseEntity<?> getReviewByISBN(@PathVariable("isbn") String isbn) {
		
		String json = "";
		ArrayList<Review> reviewList = new ArrayList<Review>();
		
		try {
			
			ReviewDatabase review_db = new ReviewDatabase();
			reviewList = review_db.getReviewByISBN(isbn);
			ObjectMapper obj = new ObjectMapper();
			json = obj.writeValueAsString(reviewList);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
		
		return ResponseEntity.ok().body(json);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/makeReview/{orderid}/{isbn}", consumes = "application/json")
	@Caching( evict = {
			@CacheEvict(value = "bookList", allEntries = true),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-simple'"),
			@CacheEvict(value = "bookByISBN", key = "#isbn + '-details'"),
			@CacheEvict(value = "memberOrders", allEntries = true),
			@CacheEvict(value = "bestseller", allEntries = true),
			@CacheEvict(value = "toprated", allEntries = true)
		})
	public ResponseEntity<?> makeReview(@PathVariable("orderid") String orderid, @RequestBody Review review, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_MEMBER") && id != null && !id.isEmpty()) {
			
			try {
				
				OrderDatabase order_db = new OrderDatabase();
				ReviewDatabase review_db = new ReviewDatabase();
				BookDatabase book_db = new BookDatabase();
				
				int orderidInt = Integer.parseInt(orderid);
				
				if(!order_db.checkOrderItemRated(orderidInt, review.getISBNNo())) {
					row = review_db.createReview(review, Integer.parseInt(id));
					if(row == 1) {
						if(order_db.updateOrderItemRated(orderidInt, review.getISBNNo(), Short.parseShort("1")) == 1) {
							row = book_db.updateBookRating(review.getISBNNo(), (int)review.getRating());
							if(row != 1) {
								row = 0;
							}
						}
						else {
							row = 0;
						}
					}
					else {
						row = 0;
					}
				}
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