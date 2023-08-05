package com.bookshop.bookhaven.model;

import java.sql.Date;

public class Review {
	private int reviewID;
	private String description;
	private Date reviewDate;
	private short rating;
	private String ISBNNo;
	private int memberID;
	private String status;

	public int getReviewID() {
		return reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getReviewDate() {
		return reviewDate;
	}


	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}


	public short getRating() {
		return rating;
	}


	public void setRating(short rating) {
		this.rating = rating;
	}


	public String getISBNNo() {
		return ISBNNo;
	}


	public void setISBNNo(String iSBNNo) {
		ISBNNo = iSBNNo;
	}


	public int getMemberID() {
		return memberID;
	}


	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	
}