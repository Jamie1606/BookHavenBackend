// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date        	: 7.6.2023
// Description 	: to store member data from database

package com.bookshop.bookhaven.model;

import java.sql.Date;
import java.util.ArrayList;

public class Member {

	private int memberID;
	private String name;
	private char gender = 'N';
	private Date birthDate;
	private String phone;
	private String address;
	private String email;
	private String password;
	private String image;
	private Date lastActive;
	private double totalPurchase = 0;

	public double getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(double totalPurchase) {
		this.totalPurchase = totalPurchase;
	}

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
}
