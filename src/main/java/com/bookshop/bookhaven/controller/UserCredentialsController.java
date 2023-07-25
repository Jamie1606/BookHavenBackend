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

@RestController
public class UserCredentialsController {
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/userlogin", consumes = "application/json")
	public UserCredentials login(@RequestBody UserCredentials userCredentials) {
		Admin loginAdmin = null;
		Member loginMember = null;
		UserCredentials loginuserCredentials = null;
		
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
					loginuserCredentials.setEmail(loginAdmin.getEmail());
					loginuserCredentials.setRole("ROLE_ADMIN");
					loginuserCredentials.setToken(token);
					System.out.println(token);
					
					if(admin_db.updateLoginTime(loginAdmin.getAdminID()) != 1) {
						System.out.println("..... Error in loginAdmin in AdminController .....");
					}
				}
			}
			else {
				String token = jwtTokenUtil.generateToken(loginMember.getMemberID() + "", loginMember.getEmail(), "ROLE_MEMBER");
				
				loginuserCredentials = new UserCredentials();
				loginuserCredentials.setEmail(loginMember.getEmail());
				loginuserCredentials.setRole("ROLE_MEMBER");
				loginuserCredentials.setToken(token);
				
				if(member_db.updateLoginTime(loginMember.getMemberID()) != 1) {
					System.out.println("..... Error in loginMember in MemberController .....");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return loginuserCredentials;
	}
}