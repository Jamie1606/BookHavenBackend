// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 11.7.2023
// Description	: middleware for author

package com.bookshop.bookhaven.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.Admin;
import com.bookshop.bookhaven.model.AdminDatabase;

@RestController
public class AdminController {
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAdmin/{id}")
	public Admin getAdmin(@PathVariable String adminid) {
		Admin admin = null;
		try {
			AdminDatabase admin_db = new AdminDatabase();
			admin = admin_db.getAdmin(Integer.parseInt(adminid));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return admin;
	}
}