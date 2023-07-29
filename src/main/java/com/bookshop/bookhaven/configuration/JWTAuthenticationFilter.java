// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 26.7.2023
// Description	: to process jwt token before proceeding to endpoints

package com.bookshop.bookhaven.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bookshop.bookhaven.model.AdminDatabase;
import com.bookshop.bookhaven.model.MemberDatabase;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	private JWTTokenUtil jwtToken;
	
	@Bean
	AdminDatabase adminDatabase() {
		return new AdminDatabase();
	}
	
	@Bean
	MemberDatabase memberDatabase() {
		return new MemberDatabase();
	}
	
	public JWTAuthenticationFilter(JWTTokenUtil jwtToken) {
		this.jwtToken = jwtToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		String statusRole = "";
		String id = "";
		
		if(header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);		// remove "Bearer " prefix
			if(jwtToken.validateToken(token)) {
				Claims claims = jwtToken.parseToken(token);
				id = claims.getSubject();
				String email = claims.get("email", String.class);
				String role = claims.get("role", String.class);

				try {
					if(id != null && email != null && role != null) {
						if(role.equals("ROLE_MEMBER")) {
							if(memberDatabase().checkUserByEmail(email)) {
								statusRole = role;
							}
						}
						else if(role.equals("ROLE_ADMIN")) {
							if(adminDatabase().checkUserByEmail(email)) {
								statusRole = role;
							}
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	
		request.setAttribute("role", statusRole);
		request.setAttribute("id", id);
		filterChain.doFilter(request, response);		
	}
}