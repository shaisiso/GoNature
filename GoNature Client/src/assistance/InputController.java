package assistance;

import java.util.regex.Pattern;

/**
 * This class is to set controller for all the input checking methods
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */

public class InputController {
	public static String errMsg;

	/**
	 * this method get field content to check if null and update errMsg if null
	 * 
	 * @param <E>		generalization
	 * @param toCheck   field content to check if null
	 * @param fieldName the name of the field that was checked (for the returned
	 *                  message)
	 * @return true if not null,otherwise update errMsg and return false
	 */
	public static <E> boolean notNull(E toCheck, String fieldName) {
		if (toCheck == null || toCheck instanceof String && ((String) toCheck).isEmpty()) {
			errMsg = "Please choose " + fieldName;
			return false;
		} else
			return true;

	}

	/**
	 * this method get field content to check if null
	 * 
	 * @param <E>	generalization
	 * @param toCheck	field to check
	 * @return		true - null, false - not null
	 */
	public static <E> boolean notNull(E toCheck) {
		if (toCheck == null)
			return false;
		if ((toCheck instanceof String) && ((String) toCheck).isEmpty())
			return false;
		return true;

	}

	/**
	 * this method check that the number of visitors is not null and between 1-15,
	 * or 2-15 for organized groups
	 * 
	 * @param visitorsAmount the number of visitors from input
	 * @param isGuide        true - guide, false - not
	 * @return true valid,otherwise update errMsg and return false
	 */
	public static boolean isValidVisitorsAmount(String visitorsAmount, boolean isGuide) {
		// System.out.println("v"+visitorsAmount.isEmpty());
		if (visitorsAmount == null || visitorsAmount.isEmpty()) {
			errMsg = "Enter number of visitors";
			return false;
		}
		int visitors = Integer.parseInt(visitorsAmount);
		if (visitors < 1 || visitors > 15) {
			errMsg = "The number of visitors should be between 1-15";
			return false;
		}
		if (isGuide && visitors == 1) {
			errMsg = "You can not be organized group if you are only 1 person";
			return false;
		}
		return true;
	}

	public static boolean isValidVisitorsAmountReg(String visitorsAmount, boolean isGuide) {
		if (isGuide)
			return true;
		if (visitorsAmount == null || visitorsAmount.isEmpty()) {
			errMsg = "Enter number of visitors";
			return false;
		}
		int visitors = Integer.parseInt(visitorsAmount);
		if (visitors < 1 || visitors > 15) {
			errMsg = "The number of visitors should be between 1-15";
			return false;
		}

		return true;
	}

	/**
	 * this method check that email is valid
	 * 
	 * @param email email address
	 * @return true - valid, false - not valid
	 */
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\." + "[a-zA-Z0-9_+&-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null || email.isEmpty()) {
			errMsg = "Please enter email";
			return false;
		}
		boolean answer = pat.matcher(email).matches();
		if (answer == false) {
			errMsg = "Your email is not valid. Please enter valid email";
			return false;
		}
		return true;
	}

	/**
	 * this method check that id is valid (9 digits) this method also check that id
	 * is valid according to the Israeli method of verifying an id
	 * 
	 * @param id id number
	 * @return true - valid id, false - not valid id
	 */
	public static boolean isValidID(String id) {

		if (id.length() != 9) {
			errMsg = "Please enter a valid id";
			return false;
		}
		int arrayCheck[] = new int[9], idArray[] = new int[9], sum = 0;
		for (int i = 0; i < 9; i++) {
			idArray[i] = Integer.parseInt(id.charAt(i) + "");
			arrayCheck[i] = idArray[i] * ((i % 2) + 1);
			if (arrayCheck[i] > 9)
				arrayCheck[i] = (arrayCheck[i] / 10 + arrayCheck[i] % 10);
			sum += arrayCheck[i];
		}
		if (sum % 10 != 0) {
			errMsg = "Please enter a valid id";
			return false;
		}

		return true;
	}

	/**
	 * this method check that name is alphabetic
	 * 
	 * @param name		name to check
	 * @return true - valid name, false - not valid
	 */
	public static boolean isValidName(String name) {
		if (name == null || name.isEmpty()) {
			errMsg = "Please enter a valid name";
			return false;
		}
		for (char c : name.toCharArray()) {
			if (!Character.isAlphabetic(c) && c != '-') {
				errMsg = "Please enter a valid name";
				return false;
			}
		}
		return true;
	}

	/**
	 * this method check valid phone number
	 * 
	 * @param phoneNumber	phone number
	 * @return				true - valid, false-no
	 */
	public static boolean isValidPhone(String phoneNumber) {
		if (phoneNumber.length() != 10 || phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '5') {
			errMsg = "Please enter a legal phone number";
			return false;
		}
		return true;
	}

	/**
	 * this method get string of date that comes from db in format (yyyy-mm-dd) and
	 * change it to (dd-mm-yyyy)
	 * 
	 * @param date date (yyyy-mm-dd)
	 * @return date (dd-mm-yyyy)
	 */
	public static String getIsraelFormatDate(String date) {
		return date.substring(8, 10) + date.substring(4, 8) + date.substring(0, 4);
	}

	/**
	 * this method get string of date that comes in format (dd-mm-yyyy) and change
	 * it to (yyyy-mm-dd)
	 * 
	 * @param date date (dd-mm-yyyy)
	 * @return date (yyyy-mm-dd)
	 */
	public static String getDBFormatDate(String date) {
		return date.substring(6, 10) + date.substring(2, 6) + date.substring(0, 2);
	}
}