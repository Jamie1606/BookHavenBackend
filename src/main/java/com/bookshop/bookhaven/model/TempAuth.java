// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 16.7.2023
// Description	: to store keys and token for s3

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