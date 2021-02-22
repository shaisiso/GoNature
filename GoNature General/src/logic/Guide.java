package logic;

public class Guide extends Visitor {

	/**
	 * this class is for all guides. extends visitor
	 * 
	 * @author Shai Siso
	 * @author Shahar Avital
	 * @author Sean Friedman
	 * @author Golan Yacobov
	 * @author Matan Avraham
	 * 
	 * @version January 2021
	 */
	private static final long serialVersionUID = 1L;
	String creditCard;

	/**
	 * constructor for guides with credit card
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @param creditCard
	 */
	public Guide(String id, String firstName, String lastName, String phone, String email, String creditCard) {
		super(id, firstName, lastName, phone, email);
		this.creditCard = creditCard;
	}

	/**
	 * constructor for guides - no credit card
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 */
	public Guide(String id, String firstName, String lastName, String phone, String email) {
		super(id, firstName, lastName, email, phone);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	/**
	 * @return the creditCard
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard the creditCard to set
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * return string format with guide details
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s\n", id, firstName, lastName, phone, email);
	}

}
