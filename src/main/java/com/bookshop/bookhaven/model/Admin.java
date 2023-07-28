// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 26.7.2023
// Description	: to store admin data

package com.bookshop.bookhaven.model;

import java.sql.Date;

public class Admin {
	private int adminID;
	private String username;
	private String name;
	private String email;
	private String password;
	private Date lastActive;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAdminID() {
		return adminID;
	}
	
	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public Date getLastActive() {
		return lastActive;
	}
	
	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
}