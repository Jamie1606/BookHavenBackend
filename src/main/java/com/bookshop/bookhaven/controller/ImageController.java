// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 10.7.2023
// Description	: this middleware is to upload image to s3

package com.bookshop.bookhaven.controller;

import java.io.InputStream;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookshop.bookhaven.model.Image;
import com.bookshop.bookhaven.model.S3Service;

@RestController
public class ImageController {
	
	@RequestMapping(method = RequestMethod.POST, path = "/uploadImage/", consumes = "application/json")
	public boolean uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestBody Image image) {
		boolean condition = false;
		try {
			InputStream inputstream = imageFile.getInputStream();
			long filesize = imageFile.getSize();
			S3Service s3service = new S3Service();
			String key = "";
			if(image.getType().equals("member")) {
				key = "/member/" + image.getFileName();
			}
			else if(image.getType().equals("3dbook")) {
				key = "/book/3d/" + image.getFileName();
			}
			else if(image.getType().equals("normalbook")) {
				key = "/book/normal/" + image.getFileName();
			}
			else {
				return false;
			}
			
			condition = s3service.uploadImage(key, inputstream, filesize);
		}
		catch(Exception e) { 
			e.printStackTrace();
		}
		return condition;
	}
}