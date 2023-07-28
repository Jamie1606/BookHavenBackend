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

import com.bookshop.bookhaven.model.Member;
import com.bookshop.bookhaven.model.MemberDatabase;

@RestController
public class MemberController {

	@RequestMapping(method = RequestMethod.GET, path = "/getMember/{id}")
	public ResponseEntity<?> getMember(@PathVariable("id") int memberID) {
		Member member = new Member();
		try {
			MemberDatabase member_db = new MemberDatabase();
			member = member_db.getMemberByID(memberID);
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		if (member == null) {
			return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(member, HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.GET, path = "/getAllMember")
	public ArrayList<Member> getAllMember() {
		ArrayList<Member> memberList = new ArrayList<>();
		try {
			MemberDatabase member_db = new MemberDatabase();
			memberList = member_db.getAllMember();
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return memberList;

	}

	@RequestMapping(method = RequestMethod.POST, path = "/createMember", consumes = "application/json")
	public int createMember(@RequestBody Member member) {
		/*
		 * nrow -1 -> email duplication 0 -> server error 1 -> success
		 */
		int nrow = 0;
		try {
			MemberDatabase member_db = new MemberDatabase();
			System.out.println(".....inside member controller.....");
			if (member_db.checkMemberEmailExists(member.getEmail())) {
				nrow = -1;
			} else {
				nrow = member_db.insertMember(member);
				System.out.println(".....done create member.....");
			}
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateMember", consumes = "application/json")
	public int updateMember(@RequestBody Member member) {

		int nrow = 0;
		try {
			MemberDatabase member_db = new MemberDatabase();
			System.out.println(".....inside member controller.....");
			nrow = member_db.updateMember(member);
			System.out.println(".....done update member.....");
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/deleteMember/{id}", consumes = "application/json")
	public int deleteMember( @PathVariable("id") int memberID) {
		int nrow = 0;
		try {
			MemberDatabase member_db = new MemberDatabase();
			System.out.println(".....inside member controller.....");
			nrow = member_db.deleteMember(memberID);
			System.out.println(".....done delete member.....");
		} catch (Exception e) {
			System.out.println("Error :" + e);
		}
		return nrow;
	}
}
