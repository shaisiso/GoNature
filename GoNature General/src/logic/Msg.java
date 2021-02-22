package logic;

import java.io.Serializable;

/**
 * this class is for message pattern to transfer message from client to server and vice vice versa
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version December 2020
 */
public class Msg implements Serializable {

/**
 * for Serializable implementation
 */
	private static final long serialVersionUID = 1L;
	/**
	 * save the action that we want to do
	 */
	public String action;
	/**
	 * object to pass
	 */
	public Object content;
	/**
	 * another object to pass
	 */
	public Object content2;
/**
 * constructor for Msg with one object to pass
 * @param action	string of action 
 * @param content	object to pass in the message
 */
	public Msg(String action, Object content) {
		super();
		this.action = action;
		this.content = content;
	}

	/** * constructor for Msg with one object to pass
	 * @param action	string of action 
 * @param content	object to pass in the message
	 * @param content2	object2 to pass in the message
	 */
	public Msg(String action, Object content, Object content2) {
		super();
		this.action = action;
		this.content = content;
		this.content2 = content2;
	}
/**
 * to string of the action and content
 */
	@Override
	public String toString() {
		return String.format("%s %s\n", action, content);
	}
}
