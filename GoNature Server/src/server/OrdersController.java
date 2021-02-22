package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import gui.MessageApprove;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import logic.Msg;
import logic.Order;
import logic.Subscriber;
import logic.Visitor;

/**
 * this is a controller for all the orders actions. searching from database
 * 
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class OrdersController {
	/**
	 * basePrice -Full price for individual (Determined by the Ministry of Tourism)
	 */
	final static int basePrice = 100;

	/**
	 * this method check if there is available place for the requested hour. if
	 * there is, an order with order number will be returned
	 * 
	 * @param conn  - connection to db
	 * @param order - requested order details
	 * @return new order with orderID or null if not possible
	 */
	public static Msg newOrder(Connection conn, Order order) {
		synchronized (conn) {
			Statement stmt;
			int orderID;
			Order orderToReturn = order;
			if (!canOrder(conn, order))// check that the visitor didnt have an order to the same date
				return new Msg("Order: Already have an order to this date", null);

			if (!isAvailable(conn, order)) // check available date and hour
				return new Msg("Order: Not available date and time", null);

			try {
				stmt = conn.createStatement();
				// checking that id is existed in visitor table(foreign key)
				if (!VisitorsConroller.idExistInVisitors(conn, order.getVisitorID()))
					VisitorsConroller.addVisitor(conn, order.getVisitorID());
				// generate order id
				ResultSet rs = stmt.executeQuery("SELECT MAX(orderID) FROM gonature.orders;");
				if (rs.next() && rs.getInt(1) != 0)
					orderID = rs.getInt(1) + 1;
				else
					orderID = 1000;

				orderToReturn.setOrderID(orderID);
				orderToReturn.setCost(calculateCostPreOrder(conn, order));// call method for cost calculate
				// save order in db
				stmt.executeUpdate("INSERT INTO gonature.orders VALUES('" + orderID + "','" + order.getVisitorID()
						+ "','" + order.getParkName() + "','" + order.getDate() + "','" + order.getHour() + "','"
						+ order.getNumberOfVisitors() + "','" + order.getEmail() + "'," + order.isOrganized() + ",'"
						+ orderToReturn.getCost() + "');");

				System.out.println("new order was added");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new Msg("Order Details", orderToReturn);
		}
	}

	/**
	 * this method check that the same visitor can order only one visit in a day
	 * 
	 * @param conn  - connection to db
	 * @param order - requested order details
	 * @return
	 */
	private synchronized static boolean canOrder(Connection conn, Order order) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.orders WHERE visitorID='" + order.getVisitorID()
					+ "'and date='" + order.getDate() + "';");
			if (rs.next())
				return false;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * this method check if there is place in the park for the specific time
	 * 
	 * @param conn  - connection to db
	 * @param order - order details
	 * @return true - order can be placed, false - order can not be placed
	 */
	public synchronized static boolean isAvailable(Connection conn, Order order) {
		int visitorsPerHour = 0, averageVisitTime = 0, maxPreOrder = 0, start, end = order.getHour();
		Statement stmt;
		try {
			stmt = conn.createStatement();
// get average visit time in this park
			ResultSet rs = stmt.executeQuery("SELECT averageVisitTime,maxPreOrder FROM gonature.parks WHERE parkName='"
					+ order.getParkName() + "';");
			if (rs.next()) {
				averageVisitTime = rs.getInt(1);
				maxPreOrder = rs.getInt(2);
			}
			start = (order.getHour() - averageVisitTime + 1);
			rs.close();
// calculate number of visitors for orders between requested hour and requested hour+averageVisitTime
			for (int i = 0; i < averageVisitTime; i++) {
				visitorsPerHour = visitorsCounters(conn, order, start, end);
				if (maxPreOrder - visitorsPerHour < order.getNumberOfVisitors()) // some hour in the range is full
					return false;
				start++;
				end++;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * this method return the number of visitors in requested park and date between
	 * hours start - end
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @param start start hour
	 * @param end   end hour
	 * @return number of visitors
	 */
	public synchronized static int visitorsCounters(Connection conn, Order order, int start, int end) {
		int cnt = 0;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT SUM(numberOfVisitors) FROM gonature.orders WHERE parkName='" + order.getParkName()
							+ "' and date='" + order.getDate() + "' and hour>=" + start + " and hour<=" + end + ";");
			if (rs.next())
				cnt = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * this method calculate cost for pre-orders
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return cost
	 */
	public synchronized static float calculateCostPreOrder(Connection conn, Order order) {
		double cost = basePrice * order.getNumberOfVisitors();
		Subscriber subscriber;
		if (order.isOrganized()) {
			cost -= basePrice; // guide does not pay
			cost *= 0.75;
		} else {
			cost *= 0.85;// 15% for preorder

			if ((subscriber = LoginController.subsIdLogin(conn, order.getVisitorID())) != null) {
				if (subscriber.getFamilyMembers() >= order.getNumberOfVisitors())
					cost *= 0.8;// 20% for subscribers
				else {
					double pricePerPerson = cost / order.getNumberOfVisitors();
					cost = (pricePerPerson * subscriber.getFamilyMembers()) * 0.8
							+ (order.getNumberOfVisitors() - subscriber.getFamilyMembers()) * pricePerPerson;
				}
			}
		}
		int discount = ParkController.parkDiscount(conn, order.getParkName(), order.getDate());// park discount
		double dis = (100 - (double) discount) / 100;
		cost *= dis;
		return (float) cost;
	}

	/**
	 * this method check the alternative dates for the requested order
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return map of date - to ArrayList of hours
	 */
	public synchronized static Map<String, ArrayList<Integer>> getAlternativeDates(Connection conn, Order order) {
		ArrayList<Integer> hours = new ArrayList<>();
		Map<String, ArrayList<Integer>> dates = new TreeMap<>();
		int hour = 8;
		for (int i = 0; i < 7; i++) {
			for (int j = hour; j <= 17; j++) {
				order.setHour(j);
				if (isAvailable(conn, order))
					hours.add(j);
			}
			if (!hours.isEmpty())
				dates.put(order.getDate(), hours);
			hours = new ArrayList<>();
			order.setDate(LocalDate.parse(order.getDate()).plusDays(1).toString());
		}
		return dates;

	}

	/**
	 * this method searching the orders of visitor
	 * 
	 * @param conn    connection to db
	 * @param visitor visitor details
	 * @return ArrayList of the orders of visitor
	 */
	public static ArrayList<Order> getVisitorOrder(Connection conn, Visitor visitor) {
		Statement stmt;
		ArrayList<Order> orders = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.orders WHERE visitorID='" + visitor.getId()
					+ "' AND date>='" + java.time.LocalDate.now() + "' ORDER BY date;");
			while (rs.next()) {
				orders.add(
						new Order(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getBoolean(8), rs.getFloat(9)));
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * this method cancels order and returns an arraylist of orders that can be
	 * implemented
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 */
	public static void cancelOrder(Connection conn, Order order) {
		synchronized (conn) {
			Statement stmt1, stmt2;
			int orderID;
			try {
				stmt1 = conn.createStatement();

				// get how many cancellations we have so far
				ResultSet rs = stmt1.executeQuery("SELECT MAX(orderID) FROM gonature.cancelledorders;");
				if (rs.next() && rs.getInt(1) != 0)
					orderID = rs.getInt(1) + 1;
				else
					orderID = 1;

				// insert the order to cancel table
				stmt1.executeUpdate("INSERT INTO gonature.cancelledorders VALUES('" + orderID + "','"
						+ order.getParkName() + "','" + order.getDate() + "','" + order.getHour() + "','"
						+ order.getNumberOfVisitors() + "');");

				stmt2 = conn.createStatement();
				stmt2.executeUpdate("DELETE FROM gonature.orders WHERE orderID='" + order.getOrderID() + "';");
				rs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * this method send a reminder message for every visitor that have an order for
	 * tomorrow
	 * 
	 * @param conn connection to db
	 */
	public static void sendSmsDayBeforeOrder(Connection conn) {
		ArrayList<Order> orders = getOrdersForSMS(conn);
		for (Order o : orders) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						FXMLLoader loader = new FXMLLoader();
						Parent root = loader
								.load(getClass().getResource("/gui/MessageSimulationApprove.fxml").openStream());
						MessageApprove messageApprove = loader.getController();
						messageApprove.loadOrder(o, conn, false);
						messageApprove.start(root);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			Platform.runLater(t);
		}
	}

	/**
	 * this method return list of orders for sending message a day before the visit
	 * 
	 * @param conn connection to db
	 * @return ArrayList of orders for sending message a day before the visit
	 */
	private static ArrayList<Order> getOrdersForSMS(Connection conn) {
		Statement stmt, stmt2;
		ArrayList<Order> orders = new ArrayList<>();
		Order temp;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM gonature.orders WHERE  DATEDIFF(date,'" + java.time.LocalDate.now() + "')=1;");
			while (rs.next()) {
				temp = new Order(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getBoolean(8), rs.getFloat(9));
				// get phone number
				stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery("select phoneNumber from subscribers where visitorID='"
						+ temp.getVisitorID() + "' union select phoneNumber from guides where visitorID='"
						+ temp.getVisitorID() + "';");
				if (rs2.next())
					temp.setPhone(rs2.getString(1));
				orders.add(temp);
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * this method check if unplanned visit is possible
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return true - possible visit, false - otherwise
	 */
	public synchronized static boolean isAvailableUnplannedVisit(Connection conn, Order order) {
		int visitorsPerHour = 0, averageVisitTime = 0, maxCapacity = 0, start, end = order.getHour();
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
			start = (order.getHour() - averageVisitTime + 1);
			rs.close();
// calculate number of visitors for orders between requested hour and requested hour+averageVisitTime
			for (int i = 0; i < averageVisitTime; i++) {
				visitorsPerHour = visitorsCounters(conn, order, start, end);
				visitorsPerHour -= EntranceController.getExitedOrders(conn, order, start, end);
				if (maxCapacity - visitorsPerHour < order.getNumberOfVisitors()) // some hour in the range is full
					return false;
				start++;
				end++;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * this method calculate cost for casual visit
	 * 
	 * @param conn  connection to db
	 * @param order order details
	 * @return the cost
	 */
	public static float calculateCostCasualVisit(Connection conn, Order order) {
		double cost = basePrice * order.getNumberOfVisitors();
		Subscriber subscriber;
		if (order.isOrganized()) {
			cost *= 0.9; // 10% for guides
		} else {
			if ((subscriber = LoginController.subsIdLogin(conn, order.getSubNum())) != null) {
				if (subscriber.getFamilyMembers() >= order.getNumberOfVisitors())
					cost *= 0.8;// 20% for subscribers
				else {
					double pricePerPerson = cost / order.getNumberOfVisitors();
					cost = (pricePerPerson * subscriber.getFamilyMembers()) * 0.8
							+ (order.getNumberOfVisitors() - subscriber.getFamilyMembers()) * pricePerPerson;
				}
			}
		}
		int discount = ParkController.parkDiscount(conn, order.getParkName(), order.getDate());// park discount
		double dis = (100 - (double) discount) / 100;
		cost *= dis;
		return (float) cost;
	}

	/**
	 * 
	 * @param conn    connection to db
	 * @param orderID order id
	 * @return order full details
	 */
	public static Order getOrderDetails(Connection conn, String orderID) {
		Order order = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE orderID='" + orderID + "';");
			if (rs.next()) {
				order = new Order(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
						rs.getInt(6), rs.getString(7), rs.getBoolean(8), rs.getFloat(9));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * this method update the number of visitors in existing order
	 * @param conn		connection to db
	 * @param order		order details
	 */
	public static void updateVisitors(Connection conn, Order order) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE orders SET numberOfVisitors = '" + order.getNumberOfVisitors()
					+ "' WHERE (orderID = '" + order.getOrderID() + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
