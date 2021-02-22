package logic;

import java.io.Serializable;

/**
 * this class is to save order details
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Order implements Serializable {

	/**
	 * for Serializable implementation
	 */
	private static final long serialVersionUID = 1L;
	private String visitorID, parkName, email, date, phone = null, subNum = null;
	private int numberOfVisitors, orderID, hour;
	private float cost;
	/**
	 * for organized groups
	 */
	private boolean organized;

	public Order() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor for new order before cost
	 * 
	 * @param visitorID
	 * @param parkName
	 * @param date
	 * @param hour
	 * @param email
	 * @param numberOfVisitors
	 * @param organized
	 */
	public Order(String visitorID, String parkName, String date, int hour, int numberOfVisitors, String email,
			boolean organized) {
		this.visitorID = visitorID;
		this.parkName = parkName;
		this.date = date;
		this.hour = hour;
		this.email = email;
		this.numberOfVisitors = numberOfVisitors;
		this.organized = organized;
	}

	/**
	 * constructor for new order with cost
	 * 
	 * @param visitorID
	 * @param parkName
	 * @param date
	 * @param hour
	 * @param email
	 * @param numberOfVisitors
	 * @param orderID
	 * @param cost
	 * @param organized
	 */
	public Order(int orderID, String visitorID, String parkName, String date, int hour, int numberOfVisitors,
			String email, boolean organized, float cost) {
		this.visitorID = visitorID;
		this.parkName = parkName;
		this.date = date;
		this.hour = hour;
		this.email = email;
		this.numberOfVisitors = numberOfVisitors;
		this.orderID = orderID;
		this.cost = cost;
		this.organized = organized;
	}

	/**
	 * constructor for casual order visit. need only number of visitors
	 * 
	 * @param numberOfVisitors
	 */
	public Order(int numberOfVisitors) {
		super();
		this.numberOfVisitors = numberOfVisitors;
	}

	/**
	 * @return the visitorID
	 */
	public String getVisitorID() {
		return visitorID;
	}

	/**
	 * @param visitorID the visitorID to set
	 */
	public void setVisitorID(String visitorID) {
		this.visitorID = visitorID;
	}

	/**
	 * @return the parkName
	 */
	public String getParkName() {
		return parkName;
	}

	/**
	 * @param parkName the parkName to set
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the numberOfVisitors
	 */
	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}

	/**
	 * @param numberOfVisitors the numberOfVisitors to set
	 */
	public void setNumberOfVisitors(int numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	/**
	 * @return the orderID
	 */
	public int getOrderID() {
		return orderID;
	}

	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * @return the organized
	 */
	public boolean isOrganized() {
		return organized;
	}

	/**
	 * @param orgenized the organized to set
	 */
	public void setOrganized(boolean orgenized) {
		this.organized = orgenized;
	}

	/**
	 * @return string of the order details
	 */
	@Override
	public String toString() {
		return String.format("%d %s %s %s %d %d %s %b %f\n", orderID, visitorID, parkName, date, hour, numberOfVisitors,
				email, organized, cost);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the subNum
	 */
	public String getSubNum() {
		return subNum;
	}

	/**
	 * @param subNum the subNum to set
	 */
	public void setSubNum(String subNum) {
		this.subNum = subNum;
	}
}
