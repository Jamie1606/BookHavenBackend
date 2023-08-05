// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: image upload request body

package com.bookshop.bookhaven.model;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	
	public boolean deleteImage(String imageapi, String type, String image) {
		
		if(image.equals("defaultBookHavenImage_3d.png") || image.equals("defaultBookHavenImage_normal.png") 
				|| image.equals("defaultuser.png")) {
			return true;
		}
		
		boolean condition = false;
		
		HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(imageapi + "?type=" + type + "&image=" + image);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
        		builder.toUriString(), 
        		HttpMethod.DELETE,
        		entity,
        		String.class);

        // Handle the response if needed
        if (response.getStatusCode() == HttpStatus.OK) {
           	condition = true;
        } else {
            condition = false;
        }
		
		return condition;
	}
}