package logic;

import java.io.Serializable;

/**
 * this class is for all the employees
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName, lastName, employeeID, email, role, parkName, userName, password;

	/**
	 * constructor get full details of employee and save them
	 * 
	 * @param firstName
	 * @param lastName
	 * @param employeeID
	 * @param email
	 * @param role
	 * @param parkName
	 * @param userName
	 * @param password
	 */
	public Employee(String firstName, String lastName, String employeeID, String email, String role, String parkName,
			String userName, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.email = email;
		this.role = role;
		this.parkName = parkName;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * this constructor is to save employee user name and password only for login
	 * 
	 * @param userName
	 * @param password
	 */
	public Employee(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 
	 * @return role of employee
	 */
	public String getRole() {
		return role;
	}

	/**
	 * save employee role
	 * 
	 * @param role	role of employee
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 
	 * @return first name of employee
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * save employee first name
	 * 
	 * @param firstName employee first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * @return last name of employee
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * save employee last name
	 * 
	 * @param lastName employee last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 
	 * @return employee id
	 */
	public String getEmployeeID() {
		return employeeID;
	}

	/**
	 * set employee id
	 * 
	 * @param employeeID employee id
	 */
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * 
	 * @return emplyee email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set emplyee email
	 * 
	 * @param email emplyee email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return park name that the employee works(null for department manager and
	 *         customer service)
	 */
	public String getParkName() {
		return parkName;
	}

	/**
	 * set the park name that the employee works
	 * 
	 * @param parkName park name
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	/**
	 * 
	 * @return employee user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * set employee user name
	 * 
	 * @param userName employee user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return employee password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set employee password
	 * 
	 * @param password employee password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return string of the employee details
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s %s %s\n", firstName, lastName, employeeID, email, role, parkName,
				userName, password);
	}

	/**
	 * override hashcode and set it by userName
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/**
	 * this method overrides equal method and return true if the userNames are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Employee))
			return false;
		Employee other = (Employee) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
