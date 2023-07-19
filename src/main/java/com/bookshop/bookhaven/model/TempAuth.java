package com.bookshop.bookhaven.model;

public class TempAuth {
	private String accesskey;
	private String secretkey;
	private String sessiontoken;
	
	
	public String getAccesskey() {
		return accesskey;
	}
	
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	
	public String getSecretkey() {
		return secretkey;
	}
	
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	
	public String getSessiontoken() {
		return sessiontoken;
	}
	
	public void setSessiontoken(String sessiontoken) {
		this.sessiontoken = sessiontoken;
	}
}