// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: image upload request body

package com.bookshop.bookhaven.model;

public class ImageUploadRequest {
	private String image_data;
	private String image_name;
	private String key;
	
	public String getImage_data() {
		return image_data;
	}
	public void setImage_data(String image_data) {
		this.image_data = image_data;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}