package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import logic.Assitant;
import logic.Order;

/**
 * this class is a controller for the actions of the mangers : reports actions
 * -----Was changed from EmployeeController in the class diagram--------
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ManagersController {
	/**
	 * this is method get details about visitor report from db and insert them to
	 * report table
	 * 
	 * @param conn db connection
	 * @param st   [park name,month,year]
	 * @return array of numbers[organized,Subscribers,singles]
	 */
	public static int[] visitorsReport(Connection conn, String[] st) {
		int[] info = { 0, 0, 0 };
		Statement stmt;
		try {
			stmt = conn.createStatement();
			// organized group
			ResultSet rs;
			if (!haveOrdersInMonth(conn, st[1], st[2], st[0]))
				return info;
			rs = stmt.executeQuery("SELECT SUM(numberOfVisitors) FROM gonature.orders WHERE parkName='" + st[0]
					+ "' AND organized=" + true + " AND month(date)='" + st[1] + "' AND YEAR(date)='" + st[2] + "';");
			if (rs.next())
				info[0] = rs.getInt(1);
			// Subscribers
			rs = stmt.executeQuery("Select sum(numberOfVisitors) from orders natural join subscribers WHERE parkName='"
					+ st[0] + "' AND organized=" + false + " AND month(date)='" + st[1] + "' AND YEAR(date)='" + st[2]
					+ "';");
			if (rs.next())
				info[1] = rs.getInt(1);
			// regular(singles)
			rs = stmt.executeQuery("Select sum(numberOfVisitors) from orders WHERE parkName='" + st[0]
					+ "' AND month(date)='" + st[1] + "' AND YEAR(date)='" + st[2] + "';");
			if (rs.next())
				info[2] = rs.getInt(1) - (info[0] + info[1]);
			// check if already exist
			String reportName = "Visitors report";
			// already in reports table
			if (!reportExist(conn, st[0], reportName, Integer.parseInt(st[1]), Integer.parseInt(st[2]))) {

				// insert to reports table
				stmt.executeUpdate("INSERT INTO gonature.reports VALUES('" + st[0] + "','" + reportName + "','" + st[1]
						+ "','" + st[2] + "');");
			}
		} catch (SQLException e) {
			System.out.println("already in reports table");
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * this method will return hours and days that the park was not full there in
	 * the requested month
	 * 
	 * @param conn db connection
	 * @param st   [park name,month,year]
	 * @return ArrayList of hours for each date where the park was not full
	 */
	public static ArrayList<String> capacityReport(Connection conn, String[] st) {
		String date = st[2] + "-" + st[1] + "-01"; // the date in map is yyyy-mm-dd
		Order order = new Order("0", st[0], date, 8, 1, "w@gmail.com", false);
		String hours = " ";
		ArrayList<String> capacity = new ArrayList<String>();
		if (!haveOrdersInMonth(conn, st[1], st[2], st[0])) // check that there is data for the requested date
			return capacity;
		int hour = 8;
		for (int i = 0; i < 31; i++) { // number of days
			for (int j = hour; j <= 17; j++) { // hours per day
				order.setHour(j);
				if (isNotFull(conn, order))
					hours += j + " ";
			}

			if (!hours.equals(" "))
				capacity.add(Assitant.getIsraelFormatDate(order.getDate()) + ": " + hours);

			hours = " ";
			order.setDate(LocalDate.parse(order.getDate()).plusDays(1).toString());
		}
		Statement stmt;
		String reportName = "Capacity report";
		if (!reportExist(conn, st[0], reportName, Integer.parseInt(st[1]), Integer.parseInt(st[2]))) {
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO gonature.reports VALUES('" + st[0] + "','" + reportName + "','" + st[1]
						+ "','" + st[2] + "');");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return capacity;
	}

	/**
	 * this method check if the park had orders in the requested month
	 * 
	 * @param conn     connection to db
	 * @param month    requested month
	 * @param year     requested year
	 * @param parkName park name
	 * @return true - there are orders, false - otherwise
	 */
	private static boolean haveOrdersInMonth(Connection conn, String month, String year, String parkName) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM gonature.orders WHERE parkName='" + parkName + "' AND MONTH(date)='"
					+ month + "' AND YEAR(date)='" + year + "';");
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * check that report is exist
	 * 
	 * @param conn     connection to db
	 * @param parkName park name
	 * @param report   report to check
	 * @param month    requested month
	 * @param year     requested year
	 * @return true - report exist, false - otherwise
	 */
	public static boolean reportExist(Connection conn, String parkName, String report, int month, int year) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM gonature.reports WHERE parkName='" + parkName + "' AND report='"
					+ report + "' AND month='" + month + "' AND YEAR='" + year + "';");
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * this method will get from db the number of cancelled orders and orders that
	 * didn't arrived
	 * 
	 * @param conn      connection to db
	 * @param startDate date from
	 * @param endDate   date to
	 * @return cancels[0] - cancelled orders,cancels[1] - didn't arrived
	 */
	public static int[] cancellationReport(Connection conn, String startDate, String endDate) {
		Statement stmt;
		int cancels[] = new int[2];
		try {
			stmt = conn.createStatement();
			ResultSet rs;
			// calculate cancelled orders
			rs = stmt.executeQuery("SELECT COUNT(*) FROM gonature.cancelledorders WHERE date>='" + startDate
					+ "' AND date<='" + endDate + "';");
			if (rs.next())
				cancels[0] = rs.getInt(1);// cancelled orders

			// calculate orders that were not arrived
			rs = stmt.executeQuery("SELECT COUNT(*) FROM gonature.orders where date>='" + startDate + "' AND date<='"
					+ endDate + "' AND orderID NOT IN( select  orderID from entrance);");
			if (rs.next())
				cancels[1] = rs.getInt(1);// did not arrived orders
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cancels;
	}

	/**
	 * this method return string of the exiting report of park manager in requested
	 * month
	 * 
	 * @param conn connection to db
	 * @param st   [park name,month,year]
	 * @return string with the existing report
	 */
	public static String ShowExsistingReports(Connection conn, String[] st) {
		Statement stmt;
		String existingReports = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM gonature.reports WHERE parkName='" + st[0] + "' AND month='" + st[1]
					+ "' AND YEAR='" + st[2] + "';");
			while (rs.next())
				existingReports += rs.getString(2) + "\n";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (existingReports.equals(""))
			existingReports = "The park manager didn't genarate\n any report last month.";
		return existingReports;
	}

	/**
	 * this method will return incomes report divided to
	 * [organized,Subscribers,singles]
	 * 
	 * @param conn     connection to db
	 * @param parkName park name
	 * @param month    requested month
	 * @param year     requested year
	 * @return incomes of [organized,Subscribers,singles]
	 */
	public static float[] incomeReport(Connection conn, String parkName, String month, String year) {
		float incomes[] = { 0, 0, 0 };// 0 organized, 1 - subscribers, 2- singles
		Statement stmt;
		try {
			if (!haveOrdersInMonth(conn, month, year, parkName)) // check that there is data for the requested date
				return incomes;
			stmt = conn.createStatement();
			// organized group
			ResultSet rs;

			rs = stmt.executeQuery("SELECT SUM(cost) FROM gonature.orders WHERE parkName='" + parkName
					+ "' AND organized=" + true + " AND month(date)='" + month + "' AND YEAR(date)='" + year + "';");
			if (rs.next())
				incomes[0] = rs.getInt(1);
			// Subscribers
			rs = stmt.executeQuery("Select sum(cost) from orders natural join subscribers WHERE parkName='" + parkName
					+ "' AND organized=" + false + " AND month(date)='" + month + "' AND YEAR(date)='" + year + "';");
			if (rs.next())
				incomes[1] = rs.getInt(1);
			// regular(singles)
			rs = stmt.executeQuery("Select sum(cost) from orders WHERE parkName='" + parkName + "' AND month(date)='"
					+ month + "' AND YEAR(date)='" + year + "';");
			if (rs.next())
				incomes[2] = rs.getInt(1) - (incomes[0] + incomes[1]);
			// check if already exist
			String reportName = "Income report";
			// already in reports table
			// insert to reports table
			stmt.executeUpdate("INSERT INTO gonature.reports VALUES('" + parkName + "','" + reportName + "','" +month
					+ "','" + year + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return incomes;
	}

	/**
	 * this method return average visit time by enter hours of
	 * [organized,Subscribers,singles]
	 * 
	 * @param conn     connection to db
	 * @param date     date to check
	 * @param hourFrom hour from
	 * @param hourTo   hour to
	 * @return matrix [organized,Subscribers,singles][startHour,...,endHour]
	 */
	public static float[][] visitationReport(Connection conn, String date, String hourFrom, String hourTo) {
		Statement stmt;
		int cnt[] = new int[3], minutes[] = new int[3];
		int startHour = Integer.parseInt(hourFrom.substring(0, 2)),
				endHour = Integer.parseInt(hourTo.substring(0, 2)) + 1;
		float visitorsTime[][] = new float[3][endHour - startHour];
		try {
			stmt = conn.createStatement();
			// Organized groups calculation
			ResultSet rs;
			for (int i = startHour; i < endHour; i++) {
				for (int j = 0; j < 3; j++) { // reset cnt and minutes
					cnt[j] = 0;
					minutes[j] = 0;
				}
				rs = stmt.executeQuery("SELECT HOUR(timediff(exitHour,enterHour)) ,MINUTE(timediff(exitHour,enterHour))"
						+ " FROM orders natural join entrance " + "where date='" + date + "' AND HOUR(enterHour)>='" + i
						+ "' AND HOUR(enterHour)<'" + (i + 1) + "' AND organized=" + true + ";");
				while (rs.next()) {
					minutes[0] += 60 * (rs.getInt(1)) + rs.getInt(2); // calculate time in minutes
					cnt[0]++;
				}
				if (cnt[0] != 0) {
					visitorsTime[0][i - startHour] = (float) minutes[0] / cnt[0];
				}
				// Subscribers calculation
				rs = stmt.executeQuery("SELECT HOUR(timediff(exitHour,enterHour)) ,MINUTE(timediff(exitHour,enterHour))"
						+ " FROM orders o,entrance e,subscribers s where o.orderID=e.orderID and o.visitorID=s.visitorID AND  date='"
						+ date + "' AND HOUR(enterHour)>='" + i + "' AND HOUR(enterHour)<'" + (i + 1)
						+ "' AND organized=" + false + ";");
				while (rs.next()) {
					minutes[1] += 60 * (rs.getInt(1)) + rs.getInt(2); // calculate time in minutes
					cnt[1]++;
				}
				if (cnt[1] != 0) {
					visitorsTime[1][i - startHour] = (float) minutes[1] / cnt[1];
				}
				// Singles calculation
				rs = stmt.executeQuery("SELECT HOUR(timediff(exitHour,enterHour)) ,MINUTE(timediff(exitHour,enterHour))"
						+ " FROM orders o,entrance e where o.orderID=e.orderID AND  date='" + date
						+ "' AND HOUR(enterHour)>='" + i + "' AND HOUR(enterHour)<'" + (i + 1) + "';");
				while (rs.next()) {
					minutes[2] += 60 * (rs.getInt(1)) + rs.getInt(2); // calculate time in minutes
					cnt[2]++;
				}
				cnt[2] -= (cnt[1] + cnt[0]);// reduce duplicates
				if (cnt[2] != 0) {
					minutes[2] -= (minutes[0] + minutes[1]); // reduce duplicates
					visitorsTime[2][i - startHour] = (float) minutes[2] / cnt[2];
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return visitorsTime;
	}

	/**
	 * this method check if the park is not full in requested time. for capacity
	 * report
	 * 
	 * @param conn  connection to db
	 * @param order order to check on
	 * @return true-full park, false - otherwise
	 */
	private static boolean isNotFull(Connection conn, Order order) {
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
				visitorsPerHour = OrdersController.visitorsCounters(conn, order, start, end);
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
}