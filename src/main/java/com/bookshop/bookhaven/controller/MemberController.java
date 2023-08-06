// Author 	  	: Thu Htet San
// Admin No    	: 2235022
// Class       	: DIT/FT/2A/02
// Group       	: 10
// Date		  	: 20.7.2023
// Description 	: middleware for member

package com.bookshop.bookhaven.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.AdminDatabase;
import com.bookshop.bookhaven.model.ImageUploadRequest;
import com.bookshop.bookhaven.model.Member;
import com.bookshop.bookhaven.model.MemberDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MemberController {
	
	private String uploadImageAPI = "https://le5w8tau6b.execute-api.us-east-1.amazonaws.com/s3image";

	@RequestMapping(method = RequestMethod.GET, path = "/getMember/{id}")
	public ResponseEntity<?> getMemberByID(@PathVariable("id") int memberID, HttpServletRequest request) {

		String json = null;
		Member member = new Member();
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");

		if (role != null && id != null && !id.isEmpty()) {

			if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_MEMBER")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			try {
				if (role.equals("ROLE_MEMBER")) {
					if (Integer.parseInt(id) != member.getMemberID()) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
					}
				}

				MemberDatabase member_db = new MemberDatabase();
				member = member_db.getMemberByID(memberID);
				if (member == null) {
					return ResponseEntity.badRequest().body("Member does not exist!");
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(member);
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(json);

	}

	@RequestMapping(method = RequestMethod.GET, path = "/getAllMember")
	public ResponseEntity<?> getAllMember(HttpServletRequest request) {

		ArrayList<Member> memberList = new ArrayList<Member>();
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		String json = null;

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {

			try {
				MemberDatabase member_db = new MemberDatabase();
				memberList = member_db.getAllMember();
				for (Member m : memberList) {
					m.setPassword("");
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(memberList);
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(json);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/createMember", consumes = "application/json")
	public ResponseEntity<?> createMember(@RequestBody Member member, HttpServletRequest request) {
		/*
		 * nrow -1 -> email duplication 0 -> server error 1 -> success
		 */
		int nrow = 0;

		try {
			MemberDatabase member_db = new MemberDatabase();
			AdminDatabase admin_db = new AdminDatabase();

			if (member_db.checkMemberEmailExists(member.getEmail()) || admin_db.checkUserByEmail(member.getEmail())) {
				nrow = -1;
			} else {
				nrow = member_db.insertMember(member);
				System.out.println(".....done create member.....");
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
			return ResponseEntity.internalServerError().body(0);
		}

		return ResponseEntity.ok().body(nrow);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateMember", consumes = "application/json")
	public ResponseEntity<?> updateMember(@RequestBody Member member, HttpServletRequest request) {

		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int nrow = 0;

		if (role != null && id != null && !id.isEmpty()) {

			if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_MEMBER")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			try {
				if (role.equals("ROLE_MEMBER")) {
					if (Integer.parseInt(id) != member.getMemberID()) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
					}
				}

				MemberDatabase member_db = new MemberDatabase();
				nrow = member_db.updateMember(member);
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(0);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(nrow);
	}

	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteMember/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable("id") String memberID, HttpServletRequest request) {
		
		int nrow = 0;
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		
		if(role != null && (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER")) && id != null && !id.isEmpty()) {
			
			try {
				
				if(role.equals("ROLE_MEMBER")) {
					if(!id.equals(memberID)) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
					}
				}
				
				MemberDatabase member_db = new MemberDatabase();
				Member member = member_db.getMemberByID(Integer.parseInt(memberID));
				String[] image = member.getImage().split("/");
				
				ImageUploadRequest imagerequest = new ImageUploadRequest();
				if(imagerequest.deleteImage(uploadImageAPI, "member", image[image.length - 1])) {
					nrow = member_db.deleteMember(Integer.parseInt(memberID));
				}
			} 
			catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(0);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.ok().body(nrow);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getTopMemberList/{limit}")
	public ResponseEntity<?> getTopMemberList(@PathVariable("limit") int limit, HttpServletRequest request) {

		ArrayList<Member> memberList = new ArrayList<Member>();
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		String json = null;

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {

			try {
				MemberDatabase member_db = new MemberDatabase();
				memberList = member_db.getTopMemberList(limit);
				for (Member m : memberList) {
					m.setPassword("");
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(memberList);
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(json);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getMemberListByISBN/{isbn}")
	public ResponseEntity<?> getMemberListByISBN(@PathVariable("isbn") String isbn, HttpServletRequest request) {

		ArrayList<Member> memberList = new ArrayList<Member>();
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		String json = null;

		if (role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {

			try {
				MemberDatabase member_db = new MemberDatabase();
				memberList = member_db.getMemberListByISBN(isbn);
				for (Member m : memberList) {
					m.setPassword("");
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(memberList);
			} catch (Exception e) {
				System.out.println("Error :" + e);
				return ResponseEntity.internalServerError().body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().body(json);
	}
}
