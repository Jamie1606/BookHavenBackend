// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 29.7.2023
// Description	: order related database functions

package com.bookshop.bookhaven.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDatabase {

	// insert order into database
	public int createOrder(Order order) throws SQLException {
		Connection conn = null;
		int orderid = -1;

		try {
			// connecting to database
			conn = DatabaseConnection.getConnection();

			String sqlStatement = "INSERT INTO `Order` (OrderDate, Amount, GST, TotalAmount, OrderStatus, DeliveryAddress, MemberID, InvoiceID) VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			st.setDouble(1, order.getAmount());
			st.setInt(2, order.getGst());
			st.setDouble(3, order.getTotalamount());
			st.setString(4, order.getOrderstatus());
			st.setString(5, order.getDeliveryaddress());
			st.setInt(6, order.getMemberid());
			st.setString(7, order.getInvoiceid());

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
				String sqlStatement = "INSERT INTO OrderItem VALUES (?, ?, ?, ?, ?)";
				PreparedStatement st = conn.prepareStatement(sqlStatement);
				st.setInt(1, orderid);
				st.setString(2, item.getIsbnno());
				st.setInt(3, item.getQty());
				st.setDouble(4, item.getAmount());
				st.setString(5, item.getStatus());
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