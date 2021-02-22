package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.Employee;
import logic.Guide;
import logic.Subscriber;
import logic.Visitor;

/**
 * this is a controller for all the login actions. searching from database
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class LoginController {
	/**
	 * this method search visitor by id
	 * 
	 * @param conn connection to db
	 * @param id   id of visitor
	 * 
	 * @return required visitor or null if not found
	 */
	public static Visitor visitorIdLogin(Connection conn, String id) {
		Statement stmt;
		Visitor visitor = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.visitors WHERE visitorID='" + id + "';");
			if (rs.next())
				visitor = new Visitor(rs.getString(1));

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return visitor;
	}

	/**
	 * this method search subscriber by subscription number
	 * 
	 * @param conn      connection to db
	 * @param subNumber subscription number
	 * 
	 * @return required subscriber or null if not found
	 */
	public static Subscriber subsLogin(Connection conn, String subNumber) {
		Statement stmt;
		Subscriber subscriber = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM gonature.subscribers WHERE subscriberNum='" + subNumber + "';");
			if (rs.next()) {
				subscriber = new Subscriber(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}

	/**
	 * this method search subscriber by id
	 * 
	 * @param conn      connection to db
	 * @param visitorId the id of the subscriber
	 * 
	 * @return required subscriber or null if not found
	 */
	public static Subscriber subsIdLogin(Connection conn, String visitorId) {
		Statement stmt;
		Subscriber subscriber = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.subscribers WHERE visitorID='" + visitorId + "';");
			if (rs.next()) {
				subscriber = new Subscriber(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}

	/**
	 * this method search employee by user name and password
	 * 
	 * @param conn     connection to db
	 * @param employee employee that tries to login and have user name and password
	 *                 fields only
	 * @return employeeToReturn - full employee details or null if not found
	 */
	public static Employee employeeLogin(Connection conn, Employee employee) {
		Statement stmt;
		Employee employeeToReturn = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.employees WHERE userName='"
					+ employee.getUserName() + "' and password='" + employee.getPassword() + "';");
			if (rs.next()) {
				employeeToReturn = new Employee(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeToReturn;
	}

	/**
	 * this method search guide by id
	 * 
	 * @param conn connection to db
	 * @param id   the id of the checked guide
	 * @return		guide details
	 */
	public static Guide guideLogin(Connection conn, String id) {
		Statement stmt;
		Guide guide = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.guides WHERE visitorID='" + id + "';");
			if (rs.next()) {
				guide = new Guide(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return guide;
	}
}
