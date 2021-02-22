package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.Guide;
import logic.Subscriber;

/**
 * this is a controller for all the registration actions searching from database
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class RegistrationController {
	/**
	 * this method tries to register new subscriber with membership card
	 * 
	 * @param conn       connection to db
	 * @param subscriber subscriber to register
	 * @return new subscription number of the requested subscriber or -1 if
	 *         subscriber id is already exists
	 */
	public static int registerFamMember(Connection conn, Subscriber subscriber) {
		Statement stmt;
		int subNumber = -1;
		try {
			if (VisitorsConroller.idExistInSubscribers(conn, subscriber.getId())) // subscriber ID already exists
				return subNumber;
			stmt = conn.createStatement();
			// checking that id is existed in visitor table(foreign key)
			if (!VisitorsConroller.idExistInVisitors(conn, subscriber.getId())) 
				VisitorsConroller.addVisitor(conn, subscriber.getId());
			//generate subscriber number
			ResultSet rs = stmt.executeQuery("SELECT MAX(subscriberNum) FROM gonature.subscribers;");
			if (rs.next() && rs.getInt(1) != 0)
				subNumber = rs.getInt(1) + 1;
			else
				subNumber = 1000;
			// insert new subscriber
			stmt.executeUpdate("INSERT INTO gonature.subscribers VALUES('" + subNumber + "','" + subscriber.getId()
					+ "','" + subscriber.getFirstName() + "','" + subscriber.getLastName() + "','"
					+ subscriber.getPhone() + "','" + subscriber.getEmail() + "','" + subscriber.getFamilyMembers()
					+ "');");

			System.out.println("new subscriber was added");
			// insert credit card
			System.out.println(subscriber.getCreditCard());
			if (subscriber.getCreditCard() != null) {
				stmt.executeUpdate("INSERT INTO gonature.creditcards VALUES('" + subscriber.getId() + "','"
						+ subscriber.getCreditCard() + "');");
				System.out.println("new cc was added");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subNumber;
	}

	/**
	 * this method tries to register new guide
	 * 
	 * @param conn  connection to db
	 * @param guide guide to register
	 * @return true - the guide was added false - not added, there is a guide with
	 *         this id
	 */
	public static boolean registerGuide(Connection conn, Guide guide) {
		Statement stmt;
		try {
			if (VisitorsConroller.idExistInGuides(conn, guide.getId())) // guide ID already exists
				return false;
			stmt = conn.createStatement();
			if (!VisitorsConroller.idExistInVisitors(conn, guide.getId()))  // checking that id is existed in visitor
				VisitorsConroller.addVisitor(conn, guide.getId());

			stmt.executeUpdate("INSERT INTO gonature.guides VALUES('" + guide.getId() + "','" + guide.getFirstName()
					+ "','" + guide.getLastName() + "','" + guide.getPhone() + "','" + guide.getEmail() + "');");

			System.out.println("new guide was added");
			// insert credit card
			if (guide.getCreditCard() != null) {
				stmt.executeUpdate("INSERT INTO gonature.creditcards VALUES('" + guide.getId() + "','"
						+ guide.getCreditCard() + "');");
				System.out.println("new cc was added");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}