package logic;
/**
 * this class is for assistants method
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Assitant {

	
	
	/**
	 * this method get string of date that comes from db in format (yyyy-mm-dd) and change it to (dd-mm-yyyy)
	 * @param date 	date (yyyy-mm-dd)
	 * @return		date (dd-mm-yyyy)
	 */
	public static String getIsraelFormatDate(String date) {
		return date.substring(8, 10) + date.substring(4, 8) + date.substring(0, 4);
	}
	
	/**
	 * this method get string of date that comes in format (dd-mm-yyyy) and change it to (yyyy-mm-dd)
	 * @param date 	date (dd-mm-yyyy)
	 * @return		date (yyyy-mm-dd)
	 */
	public static String getDBFormatDate(String date) {
		return date.substring(6, 10) + date.substring(2, 6) + date.substring(0, 2);
	}
}
