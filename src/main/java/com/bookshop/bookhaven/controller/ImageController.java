// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 26.7.2023
// Description	: middleware for s3 image

package com.bookshop.bookhaven.controller;

import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookshop.bookhaven.model.S3Service;
import com.bookshop.bookhaven.model.TempAuth;
import com.bookshop.bookhaven.model.TempAuthDatabase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ImageController {
	
	@RequestMapping(method = RequestMethod.POST, path = "/uploadImage/book/normal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadNormalBookImage(@RequestParam("image") MultipartFile imageFile, HttpServletRequest request) {
		
		boolean condition = false;
		String role = (String) request.getAttribute("role");
		
		if(role != null && role.equals("ROLE_ADMIN")) {
			try {
				InputStream inputstream = imageFile.getInputStream();
				long filesize = imageFile.getSize();
				TempAuthDatabase tmpauth_db = new TempAuthDatabase();
				TempAuth auth = tmpauth_db.getAuthKey();
				S3Service s3service = new S3Service(auth.getAccesskey(), auth.getSecretkey(), auth.getSessiontoken());
				String key = "book/normal/" + imageFile.getOriginalFilename();
				condition = s3service.uploadImage(key, inputstream, filesize);
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
		
		if(role != null && (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER"))) {
			try {
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
		
		if(role != null && role.equals("ROLE_ADMIN")) {
			try {
				InputStream inputstream = imageFile.getInputStream();
				long filesize = imageFile.getSize();
				TempAuthDatabase tmpauth_db = new TempAuthDatabase();
				TempAuth auth = tmpauth_db.getAuthKey();
				S3Service s3service = new S3Service(auth.getAccesskey(), auth.getSecretkey(), auth.getSessiontoken());
				String key = "book/3d/" + imageFile.getOriginalFilename();
				condition = s3service.uploadImage(key, inputstream, filesize);
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
		
		if(role != null && (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER"))) {
			try {
				InputStream inputstream = imageFile.getInputStream();
				long filesize = imageFile.getSize();
				TempAuthDatabase tmpauth_db = new TempAuthDatabase();
				TempAuth auth = tmpauth_db.getAuthKey();
				S3Service s3service = new S3Service(auth.getAccesskey(), auth.getSecretkey(), auth.getSessiontoken());
				String key = "member/" + imageFile.getOriginalFilename();
				condition = s3service.uploadImage(key, inputstream, filesize);
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