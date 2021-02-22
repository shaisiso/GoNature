package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import logic.Assitant;
import logic.Park;

/**
 * this is a controller for all the park actions. searching from database
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
public class ParkController {

	/**
	 * searching park details
	 * 
	 * @param conn     connection to db
	 * @param parkName park name
	 * @return park details
	 */
	public static Park parkDetails(Connection conn, String parkName) {
		Park park = null;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.parks WHERE parkName='" + parkName + "';");
			if (rs.next())
				park = new Park(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));

			ArrayList<String> discounts = getAllParkDiscounts(parkName, stmt);
			park.setDiscounts(discounts);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return park;
	}

	private static ArrayList<String> getAllParkDiscounts(String parkName, Statement stmt) throws SQLException {
		ResultSet rs;
		String date = java.time.LocalDate.now().toString();
		ArrayList<String> discounts = new ArrayList<>();
		rs = stmt.executeQuery("SELECT * FROM gonature.parkdiscounts WHERE parkName='" + parkName
				+ "' AND endDate>='" + date + "' AND status=" + true + " ORDER BY startDate;");
		while (rs.next()) {
			discounts.add(rs.getInt(2) + "% :  " + Assitant.getIsraelFormatDate(rs.getString(3)) + " - "
					+ Assitant.getIsraelFormatDate(rs.getString(4)));
		}
		if (discounts.isEmpty())
			discounts.add("There are not discounts for this park");
		return discounts;
	}

	/**
	 * insert update to update tables to show to department manager
	 * 
	 * @param conn    connection to db
	 * @param updates [park name,update name,number to set]
	 * @return true- update was insert, false - was not insert because already have
	 *         an waiting update waiting to approve
	 */
	public static boolean insertUpdate(Connection conn, String[] updates) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO gonature.updates VALUES('" + updates[0] + "','" + updates[1] + "',"
					+ Integer.parseInt(updates[2]) + ");");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * insert discount update with start and end time
	 * 
	 * @param conn    connection to db
	 * @param updates [park name,discount %,start date,end date]
	 * @return true - discount was inserted, false - there is waiting/approved a
	 *         discount in this date range
	 */
	public static boolean insertDiscountUpdate(Connection conn, String[] updates) {
		System.out.println("insertDiscountUpdate");
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.parkdiscounts WHERE parkName='" + updates[0]
					+ "' AND ((startDate<='" + updates[2] + "' AND endDate>='" + updates[2] + "')" + " OR (startDate<='"
					+ updates[3] + "' AND endDate>='" + updates[3] + "') OR (startDate>='" + updates[2]
					+ "' AND endDate<='" + updates[3] + "') );");
			if (rs.next())
				return false;
			stmt.executeUpdate("INSERT INTO gonature.parkdiscounts VALUES('" + updates[0] + "','" + updates[1] + "','"
					+ updates[2] + "','" + updates[3] + "'," + false + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * this method check discount for requested park and date
	 * 
	 * @param conn     connection to db
	 * @param parkName name of the park
	 * @param date     the date to check
	 * @return discount
	 */
	public static int parkDiscount(Connection conn, String parkName, String date) {

		int discount = 0;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT parkDiscount FROM gonature.parkdiscounts WHERE parkName='" + parkName
							+ "' AND startDate<='" + date + "' AND endDate>='" + date + "' and Status=" + true + ";");
			if (rs.next()) {
				discount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return discount;
	}

	/**
	 * show park updates requests
	 * 
	 * @param conn     connection to db
	 * @param parkName name of the park
	 * 
	 * @return map of the updateNam - to update details
	 */
	public static Map<String, ArrayList<String>> showRequests(Connection conn, String parkName) {
		Statement stmt;
		ArrayList<String> al;
		Map<String, ArrayList<String>> requests = new TreeMap<>();

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.updates WHERE parkName='" + parkName + "';");
			while (rs.next()) {
				al = new ArrayList<String>();
				al.add(String.valueOf(rs.getInt(3)));
				requests.put(rs.getString(2), al);
			}
			rs = stmt.executeQuery("SELECT * FROM gonature.parkdiscounts WHERE parkName='" + parkName + "' AND status="
					+ false + " AND endDate>='" + java.time.LocalDate.now() + "' ORDER BY startDate;");
			al = new ArrayList<String>();
			while (rs.next()) {
				al.add(rs.getInt(2) + "% from " + Assitant.getIsraelFormatDate(rs.getString(3)) + " to "
						+ Assitant.getIsraelFormatDate(rs.getString(4)));
			}
			requests.put("discount", al);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
	}

	/**
	 * this method is to set chages in db after dep. manager approve update
	 * 
	 * @param conn     connection to db
	 * @param parkName name of the park
	 * @param updateSt [update name,new number to enter]
	 */
	public static void approveUpdate(Connection conn, String parkName, String updateSt) {
		String[] split = updateSt.split("\\s");
		String updateName = split[0], startDate;
		try {
			Statement stmt = conn.createStatement();
			int num;
			if (updateName.equals("discount")) {
				startDate = Assitant.getDBFormatDate(split[3]);
				stmt.executeUpdate("UPDATE gonature.parkdiscounts SET status =" + true + " WHERE (parkName = '"
						+ parkName + "' AND startDate='" + startDate + "');");
			} else {
				num = Integer.parseInt(split[1]);
				stmt.executeUpdate("UPDATE gonature.parks SET " + updateName + " =" + num + " WHERE (parkName = '"
						+ parkName + "');");
				stmt.executeUpdate("DELETE FROM gonature.updates WHERE parkName='" + parkName + "' and updateName='"
						+ updateName + "';");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method remove update from table after dep. manager declined it
	 * 
	 * @param conn     connection to db
	 * @param parkName name of the park
	 * @param updateSt string of update
	 */
	public static void removeUpdate(Connection conn, String parkName, String updateSt) {
		String[] split = updateSt.split("\\s");
		String updateName = split[0], startDate;
		try {
			Statement stmt = conn.createStatement();
			if (updateName.equals("discount")) {
				startDate = Assitant.getDBFormatDate(split[3]);
				stmt.executeUpdate("DELETE FROM gonature.parkdiscounts WHERE parkName = '" + parkName
						+ "' AND startDate='" + startDate + "';");
			} else {
				stmt.executeUpdate("DELETE FROM gonature.updates WHERE parkName='" + parkName + "' and updateName='"
						+ updateName + "';");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Park> getAllParks(Connection conn) {
		ArrayList<Park> allParks = new ArrayList<>();
		Park park;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.parks;");
			while (rs.next()) {
				Statement stmt2 = conn.createStatement();
				park=new Park(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
				park.setDiscounts(getAllParkDiscounts(park.getParkName(), stmt2));
				allParks.add(park);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allParks;
	}
}
