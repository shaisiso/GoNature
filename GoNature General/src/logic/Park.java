package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class is to save park details
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Park implements Serializable {

	/**
	 * for Serializable implementation
	 */
	private static final long serialVersionUID = 1L;
	private String parkName, address;
	/**
	 * arraylist of park discount [amount startDate endDate]
	 */
	private ArrayList<String> discounts = new ArrayList<>();
	private int maxCapacity, maxPreOrder, averageVisitTime;

	/**
	 * constructor for new park (no discounts)
	 * 
	 * @param parkName
	 * @param address
	 * @param maxCapacity
	 * @param maxPreOrder
	 * @param averageVisitTime
	 */
	public Park(String parkName, String address, int maxCapacity, int maxPreOrder, int averageVisitTime) {
		super();
		this.parkName = parkName;
		this.address = address;
		this.maxCapacity = maxCapacity;
		this.maxPreOrder = maxPreOrder;
		this.averageVisitTime = averageVisitTime;
	}

	/**
	 * constructor for new park (with discounts)
	 * 
	 * @param parkName
	 * @param address
	 * @param discounts
	 * @param maxCapacity
	 * @param maxPreOrder
	 * @param averageVisitTime
	 */
	public Park(String parkName, String address, ArrayList<String> discounts, int maxCapacity, int maxPreOrder,
			int averageVisitTime) {
		super();
		this.parkName = parkName;
		this.address = address;
		this.discounts = discounts;
		this.maxCapacity = maxCapacity;
		this.maxPreOrder = maxPreOrder;
		this.averageVisitTime = averageVisitTime;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the discounts
	 */
	public ArrayList<String> getDiscounts() {
		return discounts;
	}

	/**
	 * @param discounts the discounts to set
	 */
	public void setDiscounts(ArrayList<String> discounts) {
		this.discounts = discounts;
	}

	/**
	 * @return the maxCapacity
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * @param maxCapacity the maxCapacity to set
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	/**
	 * @return the maxPreOrder
	 */
	public int getMaxPreOrder() {
		return maxPreOrder;
	}

	/**
	 * @param maxPreOrder the maxPreOrder to set
	 */
	public void setMaxPreOrder(int maxPreOrder) {
		this.maxPreOrder = maxPreOrder;
	}

	/**
	 * @return the averageVisitTime
	 */
	public int getAverageVisitTime() {
		return averageVisitTime;
	}

	/**
	 * @param averageVisitTime the averageVisitTime to set
	 */
	public void setAverageVisitTime(int averageVisitTime) {
		this.averageVisitTime = averageVisitTime;
	}

	/**
	 * @return string of the employee details
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %d %d %d\n", parkName, address, discounts, maxCapacity, maxPreOrder,
				averageVisitTime);
	}

	/**
	 * 
	 * @return to string of discounts
	 */
	public String stDiscounts() {
		String st = "";
		for (String s : discounts) {
			st += s + "\n";
		}
		return st;
	}
}