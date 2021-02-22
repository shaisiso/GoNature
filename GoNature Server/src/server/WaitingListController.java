package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logic.Msg;
import logic.Order;

/**
 * this is a controller for all the waiting list actions
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */

public class WaitingListController {
	/**
	 * this method delete order from waiting list
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 */
	public static synchronized void deleteFromList(Connection conn, Order order) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM gonature.waitinglist WHERE visitorID='" + order.getVisitorID()
					+ "' and date='" + order.getDate() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method return the orders in the waiting list that can be implemented
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return ArrayList of the orders that can be implemented
	 */
	public static ArrayList<Order> checkWaitingList(Connection conn, Order order) {
		synchronized (conn) {
			Statement stmt, stmt2;
			Order orderCheck;
			ArrayList<Order> waitingList = new ArrayList<>();
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT  * FROM waitinglist WHERE parkName='" + order.getParkName()
						+ "' AND DATE='" + order.getDate() + "';");
				while (rs.next()) {
					orderCheck = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
							rs.getInt(5), rs.getString(6), rs.getBoolean(7));
					// save order to send message
					if (OrdersController.isAvailable(conn, orderCheck)) {
						Msg m = OrdersController.newOrder(conn, orderCheck); // to save the place
						if (m.content != null)
							orderCheck = (Order) m.content;
						// check if the visitor has a saved phone number
						stmt2 = conn.createStatement();
						ResultSet rs2 = stmt2.executeQuery("select phoneNumber from subscribers where visitorID='"
								+ orderCheck.getVisitorID() + "' union select phoneNumber from guides where visitorID='"
								+ orderCheck.getVisitorID() + "';");
						if (rs2.next())
							orderCheck.setPhone(rs2.getString(1));
						waitingList.add(orderCheck);
						deleteFromList(conn, orderCheck); // delete from table of waiting list
						rs2.close();
					}

				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return waitingList;
		}
	}

	/**
	 * this method add order to waiting list
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return true- was added , false - not added beacause already entered to this
	 *         time and park
	 */
	public static boolean addToWaitingList(Connection conn, Order order) {
		synchronized (conn) {
			Statement stmt;
			System.out.println(order);
			try {
				stmt = conn.createStatement();
				// checking that id is existed in visitor table(foreign key)
				if (!VisitorsConroller.idExistInVisitors(conn, order.getVisitorID()))
					VisitorsConroller.addVisitor(conn, order.getVisitorID());
				stmt.executeUpdate("INSERT INTO gonature.waitinglist VALUES('" + order.getVisitorID() + "','"
						+ order.getParkName() + "','" + order.getDate() + "','" + order.getHour() + "','"
						+ order.getNumberOfVisitors() + "','" + order.getEmail() + "'," + order.isOrganized() + ");");

			} catch (SQLException e) {
				return false;
			}
			return true;
		}
	}
}
