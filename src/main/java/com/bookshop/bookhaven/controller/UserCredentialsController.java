// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: middleware for login

package com.bookshop.bookhaven.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.configuration.JWTTokenUtil;
import com.bookshop.bookhaven.model.Admin;
import com.bookshop.bookhaven.model.AdminDatabase;
import com.bookshop.bookhaven.model.Member;
import com.bookshop.bookhaven.model.MemberDatabase;
import com.bookshop.bookhaven.model.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserCredentialsController {
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/userlogin", consumes = "application/json")
	public String login(@RequestBody UserCredentials userCredentials) {
		
		String json = null;
		Admin loginAdmin = null;
		Member loginMember = null;
		UserCredentials loginuserCredentials = null;
		ObjectMapper obj = new ObjectMapper();
		
		try {
			MemberDatabase member_db = new MemberDatabase();
			loginMember = member_db.loginMember(userCredentials.getEmail(), userCredentials.getPassword());
			if(loginMember == null) {
				System.out.println("..... No member in MemberController .....");
				
				AdminDatabase admin_db = new AdminDatabase();
				loginAdmin = admin_db.loginAdmin(userCredentials.getEmail(), userCredentials.getPassword());
				if(loginAdmin == null) {
					System.out.println("..... No admin in AdminController .....");
				}
				else {
					String token = jwtTokenUtil.generateToken(loginAdmin.getAdminID() + "", loginAdmin.getEmail(), "ROLE_ADMIN");
					
					loginuserCredentials = new UserCredentials();
					loginuserCredentials.setRole("ROLE_ADMIN");
					loginuserCredentials.setToken(token);
					
					if(admin_db.updateLoginTime(loginAdmin.getAdminID()) != 1) {
						System.out.println("..... Error in loginAdmin in AdminController .....");
					}
				}
			}
			else {
				String token = jwtTokenUtil.generateToken(loginMember.getMemberID() + "", loginMember.getEmail(), "ROLE_MEMBER");
				
				loginuserCredentials = new UserCredentials();
				loginuserCredentials.setRole("ROLE_MEMBER");
				loginuserCredentials.setToken(token);
				
				if(member_db.updateLoginTime(loginMember.getMemberID()) != 1) {
					System.out.println("..... Error in loginMember in MemberController .....");
				}
			}
			json = obj.writeValueAsString(loginuserCredentials);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return json;
	}
}