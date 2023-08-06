// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: middleware for order

package com.bookshop.bookhaven.controller;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.bookhaven.model.BookDatabase;
import com.bookshop.bookhaven.model.Member;
import com.bookshop.bookhaven.model.MemberDatabase;
import com.bookshop.bookhaven.model.Order;
import com.bookshop.bookhaven.model.OrderDatabase;
import com.bookshop.bookhaven.model.OrderItem;
import com.bookshop.bookhaven.model.StripePayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderController {
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/cancelOrderItem", consumes = "application/json")
	@CacheEvict(value = "memberOrders", allEntries = true)
	public ResponseEntity<?> cancelOrderItem(@RequestBody OrderItem item, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			
			try {
				OrderDatabase order_db = new OrderDatabase();
				if(order_db.checkOrderIDByAdmin(item.getOrderid()) == 1) {

					row = order_db.cancelOrderItem(item);
					if(row == 1) {
						ArrayList<OrderItem> items = order_db.getOrderItemsByOrderID(item.getOrderid());
						boolean condition = true;
						int completeitems = 0;
						for(int i = 0; i < items.size(); i++) {
							if(items.get(i).getIsbnno().equals(item.getIsbnno())) {
								BookDatabase book_db = new BookDatabase();
								items.get(i).setQty(0 - items.get(i).getQty());
								ArrayList<OrderItem> temp_item = new ArrayList<OrderItem>();
								temp_item.add(items.get(i));
								row = book_db.changeBookQty(temp_item);
								if(row != 1) {
									row = 0;
									break;
								}
							}
							if(!items.get(i).getStatus().equals("pending")) {
								completeitems += 1;
							}
							if(!items.get(i).getStatus().equals("cancelled")) {
								condition = false;
							}
						}
						
						if(condition) {
							row = order_db.cancelOrder(item.getOrderid());
							if(row != 1) {
								row = 0;
							}
						}
						else {
							if(completeitems == items.size()) {
								row = order_db.completeOrder(item.getOrderid());
								if(row != 1) {
									row = 0;
								}
							}
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
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/completeOrderItem", consumes = "application/json")
	@CacheEvict(value = "memberOrders", allEntries = true)
	public ResponseEntity<?> completeOrderItem(@RequestBody OrderItem item, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			
			try {
				
				OrderDatabase order_db = new OrderDatabase();
				if(order_db.checkOrderIDByAdmin(item.getOrderid()) == 1) {
					
					row = order_db.completeOrderItem(item);
					if(row == 1) {
						ArrayList<OrderItem> items = order_db.getOrderItemsByOrderID(item.getOrderid());
						int completeitems = 0;
						for(int i = 0; i < items.size(); i++) {
							if(items.get(i).getIsbnno().equals(item.getIsbnno())) {
								BookDatabase book_db = new BookDatabase();
								row = book_db.increaseSoldQty(items.get(i).getQty(), items.get(i).getIsbnno());
								if(row != 1) {
									row = 0;
									break;
								}
							}
							if(!items.get(i).getStatus().equals("pending")) {
								completeitems += 1;
							}
						}
						if(completeitems == items.size()) {
							
							row = order_db.completeOrder(item.getOrderid());
							if(row != 1) {
								row = 0;
							}
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
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/getOrderItem/{id}")
	public ResponseEntity<?> getOrderItem(@PathVariable("id") String orderid, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
		String json = "";
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			
			try {
				OrderDatabase order_db = new OrderDatabase();
				orderItemList = order_db.getOrderItemsByOrderID(Integer.parseInt(orderid));
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(orderItemList);
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
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/getAllOrders")
	public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		ArrayList<Order> orderList = new ArrayList<Order>();
		String json = "";
		
		if(role != null && role.equals("ROLE_ADMIN") && id != null && !id.isEmpty()) {
			
			try {
				OrderDatabase order_db = new OrderDatabase();
				orderList = order_db.getAllOrders();
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(orderList);
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
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/cancelMemberOrderItem", consumes = "application/json")
	@CacheEvict(value = "memberOrders", allEntries = true)
	public ResponseEntity<?> cancelMemberOrderItem(@RequestBody OrderItem item, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		int row = 0;
		
		if(role != null && role.equals("ROLE_MEMBER") && id != null && !id.isEmpty()) {
			
			try {
				OrderDatabase order_db = new OrderDatabase();
				if(order_db.checkOrderID(item.getOrderid(), Integer.parseInt(id)) == 1) {

					row = order_db.cancelOrderItem(item);
					if(row == 1) {
						ArrayList<OrderItem> items = order_db.getOrderItemsByOrderID(item.getOrderid());
						boolean condition = true;
						for(int i = 0; i < items.size(); i++) {
							if(items.get(i).getIsbnno().equals(item.getIsbnno())) {
								BookDatabase book_db = new BookDatabase();
								items.get(i).setQty(0 - items.get(i).getQty());
								ArrayList<OrderItem> temp_item = new ArrayList<OrderItem>();
								temp_item.add(items.get(i));
								row = book_db.changeBookQty(temp_item);
								if(row != 1) {
									row = 0;
									break;
								}
							}
							if(!items.get(i).getStatus().equals("cancelled")) {
								condition = false;
							}
						}
						
						if(condition) {
							row = order_db.cancelOrder(item.getOrderid());
							if(row != 1) {
								row = 0;
							}
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
	
	@RequestMapping(method = RequestMethod.GET, path = "/getMemberOrders/{date}/{status}")
	@Cacheable(value = "memberOrders", key = "#date + '-' + #status + '-' + T(java.lang.String).valueOf(#request.getAttribute('id'))")
	public ResponseEntity<?> getMemberOrders(@PathVariable("date") String date, @PathVariable("status") String status, HttpServletRequest request) {
		
		String role = (String) request.getAttribute("role");
		String id = (String) request.getAttribute("id");
		ArrayList<Order> orderList = new ArrayList<Order>();
		String json = null;
		
		if(role != null && role.equals("ROLE_MEMBER") && id != null && !id.isEmpty()) {
			
			try {
				OrderDatabase order_db = new OrderDatabase();
				orderList = order_db.getOrders(Integer.parseInt(id), Date.valueOf(date), status);
				for(int i = 0; i < orderList.size(); i++) {
					orderList.get(i).setOrderitems(order_db.getOrderItemsByOrderID(orderList.get(i).getOrderid()));
				}
				ObjectMapper obj = new ObjectMapper();
				json = obj.writeValueAsString(orderList);
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
	
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			path = "/makeOrder")
	@Caching(evict = {
		@CacheEvict(value = "bookByISBN", allEntries = true),
		@CacheEvict(value = "bookList", allEntries = true),
		@CacheEvict(value = "bestseller"),
		@CacheEvict(value = "orderList", allEntries = true),
		@CacheEvict(value = "memberOrders", allEntries = true)
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
					payment.processPayment(order.getToken(), amountInCents, "SGD");
				}
				catch(StripeException e) {
					System.out.println(e.getMessage());
					row = -1;
				}
				order.setMemberid(Integer.parseInt(id));
				if(row != -1) {
					if(order.getDeliveryaddress() == null || order.getDeliveryaddress().isEmpty()) {
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
							row = book_db.changeBookQty(order.getOrderitems());
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