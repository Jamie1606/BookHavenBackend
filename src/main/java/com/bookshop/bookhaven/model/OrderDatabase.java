// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 1.8.2023
// Description	: order related database functions

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

public class OrderDatabase {
	
	
	// update order item rated
	public int updateOrderItemRated(int orderid, String isbn, short rated) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "UPDATE OrderItem SET Rated = ? WHERE OrderID = ? AND ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setShort(1, rated);
			st.setInt(2, orderid);
			st.setString(3, isbn);
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in updateOrderItemRated in OrderDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	// check order item rating
	public boolean checkOrderItemRated(int orderid, String isbn) throws SQLException {
		
		Connection conn = null;
		boolean condition = false;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT * FROM OrderItem WHERE OrderID = ? AND ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, orderid);
			st.setString(2, isbn);
			
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				if(rs.getShort("Rated") != 0) {
					condition = true;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in checkOrderItemRated in OrderDatabase .....");
			return true;
		}
		finally {
			conn.close();
		}
		
		return condition;
	}
	
	
	// complete order
	public int completeOrder(int orderid) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "UPDATE `Order` SET OrderStatus = 'delivered' WHERE OrderID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, orderid);
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in completeOrder in OrderDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	// complete order item (status = delivered)
	public int completeOrderItem(OrderItem item) throws SQLException {

		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "UPDATE OrderItem SET Status = 'delivered' WHERE OrderID = ? AND ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, item.getOrderid());
			st.setString(2, item.getIsbnno());
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in completeOrderItem in OrderDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	
	// get all orders with member data
	public ArrayList<Order> getAllOrders() throws SQLException {
		
		Connection conn = null;
		ArrayList<Order> orders = new ArrayList<Order>();
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT * FROM `Order` o LEFT JOIN Member m ON m.MemberID = o.MemberID ORDER BY o.OrderDate DESC";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrderid(rs.getInt("OrderID"));
				order.setOrderdate(rs.getDate("OrderDate"));
				order.setAmount(rs.getDouble("Amount"));
				order.setGst(rs.getInt("GST"));
				order.setTotalamount(rs.getDouble("TotalAmount"));
				order.setOrderstatus(StringEscapeUtils.escapeHtml4(rs.getString("OrderStatus")));
				order.setDeliveryaddress(StringEscapeUtils.escapeHtml4(rs.getString("DeliveryAddress")));
				order.setMemberid(rs.getInt("MemberID"));
				
				Member member = new Member();
				member.setName(StringEscapeUtils.escapeHtml4(rs.getString("Name")));
				member.setAddress(StringEscapeUtils.escapeHtml4(rs.getString("Address")));
				member.setMemberID(rs.getInt("MemberID"));
				member.setEmail(StringEscapeUtils.escapeHtml4(rs.getString("Email")));
				order.setMember(member);
				orders.add(order);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getAllOrders in OrderDatabase .....");
		}
		
		return orders;
	}
	
	
	// cancel order
	public int cancelOrder(int orderid) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "UPDATE `Order` SET OrderStatus = 'cancelled' WHERE OrderID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, orderid);
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in cancelOrder in OrderDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	// check whether order id is valid or not by admin
	public int checkOrderIDByAdmin(int orderid) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM `Order` WHERE OrderID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			
			st.setInt(1, orderid);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				rowsAffected += 1;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in checkOrderIDByAdmin in OrderDatabase .....");
			return 0;
		}
		
		return rowsAffected;
	}
	
	
	// check whether order id is member order id
	public int checkOrderID(int orderid, int memberid) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			String sqlStatement = "SELECT * FROM `Order` WHERE OrderID = ? AND MemberID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			
			st.setInt(1, orderid);
			st.setInt(2, memberid);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				rowsAffected += 1;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in checkOrderID in OrderDatabase .....");
			return 0;
		}
		
		return rowsAffected;
	}
	
	
	// cancel order item
	public int cancelOrderItem(OrderItem item) throws SQLException {
		
		Connection conn = null;
		int rowsAffected = 0;
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "UPDATE OrderItem SET Status = 'cancelled' WHERE OrderID = ? AND ISBNNo = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, item.getOrderid());
			st.setString(2, item.getIsbnno());
			
			rowsAffected = st.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in cancelOrderItem in OrderDatabase .....");
			return 0;
		}
		finally {
			conn.close();
		}
		
		return rowsAffected;
	}
	
	
	// get order items
	public ArrayList<OrderItem> getOrderItemsByOrderID(int orderid) throws SQLException {
		Connection conn = null;
		ArrayList<OrderItem> orderitems = new ArrayList<OrderItem>();
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT oi.Amount, oi.Qty AS OQty, oi.Rated, oi.Status AS OStatus, b.* FROM OrderItem oi"
					+ " LEFT JOIN Book b ON b.ISBNNo = oi.ISBNNo"
					+ " LEFT JOIN `Order` o ON o.OrderID = oi.OrderID"
					+ " WHERE o.OrderID = ?";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, orderid);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				OrderItem orderitem = new OrderItem();
				orderitem.setAmount(rs.getDouble("Amount"));
				orderitem.setOrderid(orderid);
				orderitem.setQty(rs.getInt("OQty"));
				orderitem.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("OStatus")));
				orderitem.setIsbnno(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				orderitem.setRated(rs.getShort("Rated"));
				
				Book book = new Book();
				book.setISBNNo(StringEscapeUtils.escapeHtml4(rs.getString("ISBNNo")));
				book.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("Title")));
				book.setQty(rs.getInt("Qty"));
				book.setPage(rs.getInt("Page"));
				book.setPrice(rs.getDouble("Price"));
				book.setRating(rs.getDouble("Rating"));
				book.setPublisher(StringEscapeUtils.escapeHtml4(rs.getString("Publisher")));
				book.setPublicationDate(rs.getDate("PublicationDate"));
				book.setDescription(StringEscapeUtils.escapeHtml4(rs.getString("Description")));
				book.setImage(StringEscapeUtils.escapeHtml4(rs.getString("Image")));
				book.setImage3D(StringEscapeUtils.escapeHtml4(rs.getString("Image3D")));
				book.setStatus(StringEscapeUtils.escapeHtml4(rs.getString("Status")));
				
				orderitem.setBook(book);
				orderitems.add(orderitem);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getOrderItemsByOrderID in OrderDatabase .....");
			return null;
		}
		finally {
			conn.close();
		}
		
		return orderitems;
	}
	
	
	// get order information
	public ArrayList<Order> getOrders(int memberid, Date orderDate, String status) throws SQLException {
		Connection conn = null;
		ArrayList<Order> orders = new ArrayList<Order>();
		
		try {
			conn = DatabaseConnection.getConnection();
			
			String sqlStatement = "SELECT * FROM `Order` WHERE MemberID = ? AND OrderDate >= ? "
					+ "AND CASE WHEN ? = 'all' THEN 1 "
					+ "WHEN ? = 'pending' THEN OrderStatus = 'pending' "
					+ "WHEN ? = 'delivered' THEN OrderStatus = 'delivered' "
					+ "WHEN ? = 'cancelled' THEN OrderStatus = 'cancelled' "
					+ "END "
					+ "ORDER BY OrderDate DESC";
			PreparedStatement st = conn.prepareStatement(sqlStatement);
			st.setInt(1, memberid);
			st.setDate(2, orderDate);
			st.setString(3, status);
			st.setString(4, status);
			st.setString(5, status);
			st.setString(6, status);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrderid(rs.getInt("OrderID"));
				order.setOrderdate(rs.getDate("OrderDate"));
				order.setAmount(rs.getDouble("Amount"));
				order.setGst(rs.getInt("GST"));
				order.setTotalamount(rs.getDouble("TotalAmount"));
				order.setOrderstatus(StringEscapeUtils.escapeHtml4(rs.getString("OrderStatus")));
				order.setDeliveryaddress(StringEscapeUtils.escapeHtml4(rs.getString("DeliveryAddress")));
				order.setMemberid(rs.getInt("MemberID"));
				orders.add(order);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in getOrders in OrderDatabase .....");
		}
		finally {
			conn.close();
		}
		
		return orders;
	}
	

	// insert order into database
	public int createOrder(Order order) throws SQLException {
		Connection conn = null;
		int orderid = -1;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "INSERT INTO `Order` (OrderDate, Amount, GST, TotalAmount, OrderStatus, DeliveryAddress, MemberID) VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			st.setDouble(1, order.getAmount());
			st.setInt(2, order.getGst());
			st.setDouble(3, order.getTotalamount());
			st.setString(4, order.getOrderstatus());
			st.setString(5, order.getDeliveryaddress());
			st.setInt(6, order.getMemberid());

			orderid = st.executeUpdate();
			
			if(orderid == 1) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					orderid = rs.getInt(1);
				}
				else {
					orderid = -1;
				}
			}
			else {
				orderid = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createOrder in OrderDatabase .....");
		} finally {
			conn.close();
		}
		return orderid;
	}
	
	// insert order items to database
	public int createOrderItem(ArrayList<OrderItem> items, int orderid) throws SQLException {
		Connection conn = null;
		int rowsAffected = 0;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();
			
			for(OrderItem item: items) {
				String sqlStatement = "INSERT INTO OrderItem VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement st = conn.prepareStatement(sqlStatement);
				st.setInt(1, orderid);
				st.setString(2, item.getIsbnno());
				st.setInt(3, item.getQty());
				st.setDouble(4, item.getAmount());
				st.setShort(5, Short.parseShort("0"));
				st.setString(6, item.getStatus());
				rowsAffected += st.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("..... Error in createOrderItem in OrderDatabase .....");
		} finally {
			conn.close();
		}
		
		return rowsAffected;
	}
}