// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 29.7.2023
// Description	: middleware for order

package com.bookshop.bookhaven.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.Member;
import com.bookshop.bookhaven.model.MemberDatabase;
import com.bookshop.bookhaven.model.Order;
import com.bookshop.bookhaven.model.OrderDatabase;
import com.bookshop.bookhaven.model.StripePayment;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderController {
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/makeOrder")
	@Caching(evict = {
		@CacheEvict(value = "bookByISBN", allEntries = true),
		@CacheEvict(value = "bookList", allEntries = true),
		@CacheEvict(value = "bestseller")
	})
	public ResponseEntity<?> makeOrder(@RequestBody Order order, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_MEMBER") && id != null && !id.isEmpty()) {
			try {
				try {
					StripePayment payment = new StripePayment();
					int amountInCents = (int) (order.getTotalamount() * 100);
					Charge charge = payment.processPayment(order.getToken(), amountInCents, "SGD");
					order.setInvoiceid(charge.getInvoice());
				}
				catch(StripeException e) {
					System.out.println(e.getMessage());
					row = -1;
				}
				order.setMemberid(Integer.parseInt(id));
				if(row != -1) {
					if(order.getDeliveryaddress() == null) {
						MemberDatabase member_db = new MemberDatabase();
						Member member = member_db.getMemberByID(Integer.parseInt(id));
						order.setDeliveryaddress(member.getAddress());
					}
					OrderDatabase order_db = new OrderDatabase();
					row = order_db.createOrder(order);
					if(row != -1) {
						row = order_db.createOrderItem(order.getOrderitems(), row);
						if(row == order.getOrderitems().size()) {
							BookDatabase book_db = new BookDatabase();
							row = book_db.reduceBookQty(order.getOrderitems());
							if(row == order.getOrderitems().size()) {
								row = 1;
							}
						}
						else {
							row = 0;
						}
					}
					else {
						row = 0;
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(0);
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.ok().body(row);
	}
}