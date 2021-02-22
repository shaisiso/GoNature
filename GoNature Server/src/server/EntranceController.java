package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logic.Msg;
import logic.Order;
import logic.Visitor;

/**
 * this class is a controller for the actions of park entrance check from db
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class EntranceController {
	/**
	 * this method execute enter to the park
	 * 
	 * @param conn     conncetion to db
	 * @param orderID  order id
	 * @param parkName park name
	 * @return order details for show invoice
	 */
	public static Order enterPark(Connection conn, String orderID, String parkName) {
		Statement stmt;
		Order order = null;
		if (!haveOrder(conn, orderID, parkName))
			return null;
		try {

			stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO gonature.entrance VALUES('" + orderID + "','"
					+ java.time.LocalTime.now().toString() + "'," + null + ");");
			// get order details
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.orders WHERE orderID='" + orderID + "';");
			if (rs.next())
				order = new Order(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
						rs.getInt(6), rs.getString(7), rs.getBoolean(8), rs.getFloat(9));
			order.setCost(OrdersController.calculateCostPreOrder(conn, order));// get the update cost by today
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * check if the order id is existing in the orders table with the given park and
	 * the day of today
	 * 
	 * @param conn     conncetion to db
	 * @param orderID  order id
	 * @param parkName park name
	 * @return true- have an order for today to this park, false - otherwise
	 */
	private static boolean haveOrder(Connection conn, String orderID, String parkName) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.orders WHERE orderID='" + orderID
					+ "' AND parkName='" + parkName + "' AND date='" + java.time.LocalDate.now() + "';");
			if (rs.next()) {
				rs.close();
				return true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * exit from park - update exit hour
	 * 
	 * @param conn     conncetion to db
	 * @param orderID  order id
	 * @param parkName park name
	 * @return true- have an order for today to this park, false - otherwise
	 */
	public static boolean exitPark(Connection conn, String orderID, String parkName) {
		try {
			Statement stmt = conn.createStatement();
			// check that wasnt exit already
			ResultSet rs = stmt
					.executeQuery("SELECT  * FROM entrance WHERE orderID='" + orderID + "' AND exitHour is not null;");
			if (rs.next()) {
				rs.close();
				return false;
			}
			rs.close();
			int changed = stmt.executeUpdate("UPDATE gonature.entrance SET exitHour = '"
					+ java.time.LocalTime.now().toString() + "' WHERE (orderID = '" + orderID + "');");
			if (changed == 0) // this order id wasnt entered
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * get the number of visitors currently in the park
	 * 
	 * @param conn     connection to db
	 * @param parkName park name
	 * @return number of visitors currently in the park
	 */
	public static int getCapacity(Connection conn, String parkName) {
		Statement stmt;
		int capacity = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT  SUM(numberOfVisitors) FROM entrance natural join orders where parkName='"
							+ parkName + "'AND date='" + java.time.LocalDate.now() + "' and exitHour is null ;");
			if (rs.next())
				capacity = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return capacity;
	}

	/**
	 * make unplanned order visit
	 * 
	 * @param conn  connection to db
	 * @param order order details to insert
	 * @return message with order details or reason why order was not added
	 */
	public static Msg unplannedOrder(Connection conn, Order order) {
		Msg msgOrder = null;
		Statement stmt;
		Visitor visitor = null;
		if (!OrdersController.isAvailableUnplannedVisit(conn, order))
			return new Msg("Currently we dont have place in the park for this number of visitors", null);
		try {
			stmt = conn.createStatement();
			if (!order.getVisitorID().equals("0")) {// is try to enter as a guide
				if (!VisitorsConroller.idExistInGuides(conn, order.getVisitorID()))
					return new Msg("This id is not registered as a guide", null);
				order.setOrganized(true);
				visitor = new Visitor(order.getVisitorID());
			}
			if (order.getSubNum() != null) {// is try to enter as subscriber
				visitor = LoginController.subsLogin(conn, order.getSubNum());
				if (visitor == null)
					return new Msg("This subscription number is not valid", null);
			}
			// check that that visitor have no other order today
			if (visitor != null) {
				ArrayList<Order> ordersToCheck = OrdersController.getVisitorOrder(conn, visitor);// order by date from
																									// today
				if (!ordersToCheck.isEmpty()
						&& ordersToCheck.get(0).getDate().equals(java.time.LocalDate.now().toString()))
					return new Msg("This visitor already have other order", null);
				order.setVisitorID(visitor.getId());
			}

			// generate order id
			ResultSet rs = stmt.executeQuery("SELECT MAX(orderID) FROM gonature.orders;");
			if (rs.next() && rs.getInt(1) != 0)
				order.setOrderID(rs.getInt(1) + 1);
			else
				order.setOrderID(1000);
			// calculate cost
			order.setCost(OrdersController.calculateCostCasualVisit(conn, order));
			if (!VisitorsConroller.idExistInVisitors(conn, order.getVisitorID()))
				VisitorsConroller.addVisitor(conn, order.getVisitorID());
			stmt.executeUpdate("INSERT INTO gonature.orders VALUES('" + order.getOrderID() + "','"
					+ order.getVisitorID() + "','" + order.getParkName() + "','" + order.getDate() + "','"
					+ order.getHour() + "','" + order.getNumberOfVisitors() + "','" + order.getEmail() + "',"
					+ order.isOrganized() + ",'" + order.getCost() + "');");
			msgOrder = new Msg("Order", order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgOrder;
	}

	/**
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return available place in the park of the order
	 */
	public static int getAvailablePlace(Connection conn, Order order) {
		int visitorsPerHour = 0, averageVisitTime = 0, maxCapacity = 0, start, end = order.getHour(), available = 0;
		Statement stmt;
		try {
			stmt = conn.createStatement();
// get average visit time in this park
			ResultSet rs = stmt.executeQuery("SELECT averageVisitTime,maxCapacity FROM gonature.parks WHERE parkName='"
					+ order.getParkName() + "';");
			if (rs.next()) {
				averageVisitTime = rs.getInt(1);
				maxCapacity = rs.getInt(2);
			}
			available = maxCapacity;
			start = (order.getHour() - averageVisitTime + 1);
			rs.close();
// calculate number of visitors for orders between requested hour and requested hour+averageVisitTime
			for (int i = 0; i < averageVisitTime; i++) {
				visitorsPerHour = OrdersController.visitorsCounters(conn, order, start, end);
				visitorsPerHour -= getExitedOrders(conn, order, start, end);
				available = maxCapacity - visitorsPerHour < available ? maxCapacity - visitorsPerHour : available;
				start++;
				end++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return available;
	}

	/**
	 * this method calculate orders that was exited from park to sub from the park
	 * current visitors
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @param start start hour
	 * @param end   end hour
	 * @return number of orders that exited in the specified time
	 */
	public static int getExitedOrders(Connection conn, Order order, int start, int end) {
		int cnt = 0;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT SUM(numberOfVisitors) FROM orders natural join entrance  WHERE parkName='"
							+ order.getParkName() + "' and date='" + order.getDate() + "' and hour>=" + start
							+ " and hour<=" + end + " AND exitHour is not null;");
			if (rs.next())
				cnt = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

}
