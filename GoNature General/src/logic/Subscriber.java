package logic;

/**
 * this class is to save Subscriber details. extends visitor
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Subscriber extends Visitor {

	/**
	 * for Serializable implementation
	 */
	private static final long serialVersionUID = 1L;

	private String subNumber, creditCard;
	int familyMembers;

	public Subscriber() {
		super(null);
	}

/**
 * constructor for new subscribers(no subNum) with no credit card
 * @param id
 * @param firstName
 * @param lastName
 * @param phone
 * @param email
 * @param familyMembers
 */
	public Subscriber(String id, String firstName, String lastName, String phone, String email, int familyMembers) {
		super(id, firstName, lastName, email, phone);
		this.subNumber = null;
		this.familyMembers = familyMembers;
		this.creditCard = null;
	}
/**
 * constructor for new subscribers(no subNum) with credit card
 * @param id
 * @param firstName
 * @param lastName
 * @param phone
 * @param email
 * @param familyMembers
 * @param creditCard
 */
	public Subscriber(String id, String firstName, String lastName, String phone, String email, int familyMembers,
			String creditCard) {
		super(id, firstName, lastName, email, phone);
		subNumber = null;
		this.familyMembers = familyMembers;
		this.creditCard = creditCard;
	}

/**
 * constructor for existing subscribers(with subNum)
 * @param subNumber
 * @param id
 * @param firstName
 * @param lastName
 * @param phone
 * @param email
 * @param familyMembers
 */
	public Subscriber(String subNumber, String id, String firstName, String lastName, String phone, String email,
			int familyMembers) {
		super(id, firstName, lastName, email, phone);
		this.subNumber = subNumber;
		this.familyMembers = familyMembers;
		this.creditCard = null;
	}

	/**
	 * @return the subNumber
	 */
	public String getSubNumber() {
		return subNumber;
	}

	/**
	 * @param subNumber the subNumber to set
	 */
	public void setSubNumber(String subNumber) {
		this.subNumber = subNumber;
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
	 * @return the familyMembers
	 */
	public int getFamilyMembers() {
		return familyMembers;
	}

	/**
	 * @param familyMembers the familyMembers to set
	 */
	public void setFamilyMembers(int familyMembers) {
		this.familyMembers = familyMembers;
	}
/**
 * return string format with subscribers details
 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s %d\n", subNumber, id, firstName, lastName, phone, email, familyMembers);
	}

}
