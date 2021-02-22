package client;


import javafx.application.Application;
import javafx.stage.Stage;

/**
 * this is the Main that run for each client
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ClientUI extends Application {
	public static ClientController chat; // only one instance

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * this method upload to the screen the main page
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientFrame clientFrame = new ClientFrame();
		clientFrame.start(primaryStage);

	}

	public static void runClient(String p) throws Exception {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(p); // Set port

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		chat = new ClientController("localhost", port);
		
	}
}
