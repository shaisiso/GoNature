package client;

import java.io.*;


import logic.Msg;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method.
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */

public class ClientController implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;
	public static boolean isConnected=false;
	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	Client client;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the Client UI.
	 */
	public ClientController(String host, int port) {
		try {
			client = new Client(host, port, this);
			System.out.println("conneted " + host + " " + port);
			isConnected=true;
		} catch (IOException exception) {
			isConnected=false;
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
		}
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */

	public void accept(Msg m) {
		client.handleMessageFromClientUI(m);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}

}
//End of ConsoleChat class
