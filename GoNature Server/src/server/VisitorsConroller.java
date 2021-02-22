package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * this class is for getting information about existed type of visitor and to
 * add visitors to table searching from database
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Shon Freidman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version December 2020
 */
public class VisitorsConroller {
	/**
	 * this method checks if id is existed in visitors table
	 * 
	 * @param conn connection to db
	 * @param id   id to check
	 * @return true-existed false-not existed
	 */
	public static boolean idExistInVisitors(Connection conn, String id) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.visitors WHERE visitorID='" + id + "';");
			if (rs.next())
				return true;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * this method checks if id is existed in subscribers table
	 * 
	 * @param conn connection to db
	 * @param id   id to check
	 * @return true-existed false-not existed
	 */
	public static boolean idExistInSubscribers(Connection conn, String id) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.subscribers WHERE visitorID='" + id + "';");
			if (rs.next())
				return true;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * this method checks if id is existed in guides table
	 * 
	 * @param conn connection to db
	 * @param id   id to check
	 * @return true-existed false-not existed
	 */
	public static boolean idExistInGuides(Connection conn, String id) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM gonature.guides WHERE visitorID='" + id + "';");
			if (rs.next())
				return true;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void addVisitor(Connection conn, String id) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO gonature.visitors VALUES('" + id + "');");
	}
}