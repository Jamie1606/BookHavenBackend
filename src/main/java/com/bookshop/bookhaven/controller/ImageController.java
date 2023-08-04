// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: middleware for s3 image

package com.bookshop.bookhaven.controller;

import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.bookshop.bookhaven.model.ImageUploadRequest;
import com.bookshop.bookhaven.model.S3Service;
import com.bookshop.bookhaven.model.TempAuth;
import com.bookshop.bookhaven.model.TempAuthDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ImageController {
	
	// this uploadImageAPI is from aws api gateway and that gateway is used to call lambda function to upload image to s3
	private String uploadImageAPI = "https://le5w8tau6b.execute-api.us-east-1.amazonaws.com/imageoptions";
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/uploadImage/book/normal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadNormalBookImage(@RequestParam("image") MultipartFile imageFile, HttpServletRequest request) {
		
		boolean condition = false;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				String base64ImageData = Base64.getEncoder().encodeToString(imageFile.getBytes());
				String key = "book/normal/";
				
				ImageUploadRequest imagerequest = new ImageUploadRequest();
				imagerequest.setImage_data(base64ImageData);
				imagerequest.setImage_name(imageFile.getOriginalFilename());
				imagerequest.setKey(key);
				
				ObjectMapper obj = new ObjectMapper();
				String json = obj.writeValueAsString(imagerequest);
				
				HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(json, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(uploadImageAPI, entity, String.class);

                // Handle the response if needed
                if (response.getStatusCode() == HttpStatus.OK) {
                   	condition = true;
                } else {
                    condition = false;
                }
			}
			catch(Exception e) { 
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(false);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(condition);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteImage/{image}")
	public ResponseEntity<?> deleteImage(@PathVariable String image, HttpServletRequest request) {
		
		boolean condition = false;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER")) && id != null && !id.isEmpty()) {
			try {
				
				MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
				queryParams.add("image", image);
				
				HttpHeaders headers = new HttpHeaders();
                HttpEntity<String> entity = new HttpEntity<>(headers);
                
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uploadImageAPI)
                		.queryParams(queryParams);

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
				
				TempAuthDatabase tmpauth_db = new TempAuthDatabase();
				TempAuth auth = tmpauth_db.getAuthKey();
				S3Service s3service = new S3Service(auth.getAccesskey(), auth.getSecretkey(), auth.getSessiontoken());
				condition = s3service.deleteImage(image);
			}
			catch(Exception e) { 
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(false);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(condition);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/uploadImage/book/3d", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> upload3DBookImage(@RequestParam("image") MultipartFile imageFile, HttpServletRequest request) {
		
		boolean condition = false;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			try {
				String base64ImageData = Base64.getEncoder().encodeToString(imageFile.getBytes());
				String key = "book/3d/";
				
				ImageUploadRequest imagerequest = new ImageUploadRequest();
				imagerequest.setImage_data(base64ImageData);
				imagerequest.setImage_name(imageFile.getOriginalFilename());
				imagerequest.setKey(key);
				
				ObjectMapper obj = new ObjectMapper();
				String json = obj.writeValueAsString(imagerequest);
				
				HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(json, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(uploadImageAPI, entity, String.class);

                // Handle the response if needed
                if (response.getStatusCode() == HttpStatus.OK) {
                   	condition = true;
                } else {
                    condition = false;
                }
			}
			catch(Exception e) { 
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(false);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(condition);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/uploadImage/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadMemberImage(@RequestParam("image") MultipartFile imageFile, HttpServletRequest request) {
		
		boolean condition = false;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER")) && id != null && !id.isEmpty()) {
			try {
				String base64ImageData = Base64.getEncoder().encodeToString(imageFile.getBytes());
				String key = "member/";
				
				ImageUploadRequest imagerequest = new ImageUploadRequest();
				imagerequest.setImage_data(base64ImageData);
				imagerequest.setImage_name(imageFile.getOriginalFilename());
				imagerequest.setKey(key);
				
				ObjectMapper obj = new ObjectMapper();
				String json = obj.writeValueAsString(imagerequest);
				
				HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(json, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(uploadImageAPI, entity, String.class);

                // Handle the response if needed
                if (response.getStatusCode() == HttpStatus.OK) {
                   	condition = true;
                } else {
                    condition = false;
                }
			}
			catch(Exception e) { 
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(false);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(condition);
	}
}