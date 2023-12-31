// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: to store order item

package com.bookshop.bookhaven.model;

public class OrderItem {
	
	private int orderid;
	private String isbnno;
	private int qty;
	private double amount;
	private String status;
	private short rated;
	private Book book;

	public short getRated() {
		return rated;
	}

	public void setRated(short rated) {
		this.rated = rated;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getIsbnno() {
		return isbnno;
	}
	public void setIsbnno(String isbnno) {
		this.isbnno = isbnno;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}