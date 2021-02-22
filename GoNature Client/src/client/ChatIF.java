package client;

/**
 * This interface implements the abstract method used to display objects onto
 * the client or server UIs.
 *
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public interface ChatIF {
	/**
	 * Method that when overriden is used to display objects onto a UI.
	 */
	public abstract void display(String message);
}
