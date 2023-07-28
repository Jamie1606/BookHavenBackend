// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 27.7.2023
// Description	: middleware for admin

package com.bookshop.bookhaven.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Admin;
import com.bookshop.bookhaven.model.AdminDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AdminController {
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAdmin/{id}")
	public ResponseEntity<?> getAdmin(@PathVariable String adminid, HttpServletRequest request) {
		
		String json = null;
		Admin admin = null;
		String role = (String) request.getAttribute("role");
		
		if(role != null && role.equals("ROLE_ADMIN")) {
			try {
				AdminDatabase admin_db = new AdminDatabase();
				admin = admin_db.getAdmin(Integer.parseInt(adminid));
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(admin);
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(null);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.ok().body(json);
	}
}