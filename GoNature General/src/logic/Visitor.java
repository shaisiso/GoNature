package logic;

import java.io.Serializable;

/**
 * this class is to save visitor details
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Visitor implements Serializable{
	/**
	 * for Serializable implementation
	 */
	private static final long serialVersionUID = 1L;
	protected String id,firstName,lastName,email,phone;
	/**
	 * constructor for visitor. only id needed
	 * @param id
	 */
	public Visitor(String id) {
		this.id = id;
	}
	
	/**
	 * constructor for registered visitors(for successors)
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 */
	public Visitor(String id, String firstName, String lastName, String email, String phone) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}


	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}


	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

/**
 * 
 * @return id
 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id 	id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * return string format with id
	 */
	@Override
	public String toString(){
		return String.format("%s \n",id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Visitor))
			return false;
		Visitor other = (Visitor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
