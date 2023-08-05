// Author		: Zay Yar Tun
// Admin No		: 2235035, 2235022
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: to only allow certain endpoints and process jwt token before endpoints

package com.bookshop.bookhaven.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// referenced from https://spring.io/guides/gs/securing-web/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private JWTTokenUtil jwtToken;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable()).cors((cors) -> cors.configurationSource(configurationSource()))
				.authorizeHttpRequests((requests) -> requests.requestMatchers("/userlogin").permitAll()
						.requestMatchers("/getLatest/{no}").permitAll()
						.requestMatchers("/getRelated/{isbn}/{limit}").permitAll()
						.requestMatchers("/getAllBook/details").permitAll()
						.requestMatchers("/getBook/{isbn}").permitAll()
						.requestMatchers("/getBook/details/{isbn}")
						.permitAll().requestMatchers("/createBook").permitAll()
						.requestMatchers("/updateBook/{isbn}").permitAll()
						.requestMatchers("/deleteBook/{isbn}").permitAll()
						.requestMatchers("/checkISBN13/{isbn}").permitAll()
						.requestMatchers("/getAllBook").permitAll()
						.requestMatchers("/getBestSeller/{limit}").permitAll()
						.requestMatchers("/getBestSelling/{limit}").permitAll()
						.requestMatchers("/getLeastSelling/{limit}").permitAll()
						.requestMatchers("/getLowStock").permitAll()
						.requestMatchers("/getTopRated/{limit}").permitAll()
						.requestMatchers("/getBookByAuthorID/{id}").permitAll()
						.requestMatchers("/getAdmin/{id}").permitAll()
						.requestMatchers("/getAllAuthor").permitAll()
						.requestMatchers("/getAuthor/{id}").permitAll()
						.requestMatchers("/createAuthor").permitAll()
						.requestMatchers("/updateAuthor/{id}").permitAll()
						.requestMatchers("/deleteAuthor/{id}").permitAll()
						.requestMatchers("/getAllGenre").permitAll()
						.requestMatchers("/getGenre/{id}").permitAll()
						.requestMatchers("/createGenre").permitAll()
						.requestMatchers("/updateGenre").permitAll()
						.requestMatchers("/deleteGenre/{id}").permitAll()
						.requestMatchers("/getBookByGenreID/{id}").permitAll()
						.requestMatchers("/uploadImage/book/normal").permitAll()
						.requestMatchers("/deleteImage/{image}").permitAll()
						.requestMatchers("/uploadImage/book/3d").permitAll()
						.requestMatchers("/uploadImage/member").permitAll()
						.requestMatchers("/uploadImage/book/3d").permitAll()
						.requestMatchers("/getMember/{id}").permitAll()
						.requestMatchers("/getAllMember").permitAll()
						.requestMatchers("/createMember").permitAll()
						.requestMatchers("/updateMember").permitAll()
						.requestMatchers("/deleteMember/{id}").permitAll()
						.requestMatchers("/getBook/title/{bookTitle}").permitAll()
						.requestMatchers("/getBook/author/{name}").permitAll()
						.requestMatchers("/makeOrder").permitAll()
						.requestMatchers("/getMemberOrders/{date}/{status}").permitAll()
						.requestMatchers("/cancelMemberOrderItem").permitAll()
						.requestMatchers("/getAllOrders").permitAll()
						.requestMatchers("/getOrderItem/{id}").permitAll()
						.requestMatchers("/completeOrderItem").permitAll()
						.requestMatchers("/cancelOrderItem").permitAll()
						.anyRequest().authenticated())
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(new JWTAuthenticationFilter(jwtToken), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// this is taken from
	// https://stackoverflow.com/questions/45391264/spring-boot-2-and-oauth2-jwt-configuration
	@Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}