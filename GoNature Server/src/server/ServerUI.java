package server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

import gui.ServerFrame;
/**
this is the Main that run  the server
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	public static boolean flag = false;
	public static Server sv=null;

/**
 * this method upload to the screen the main page
 * @param args
 * @throws Exception
 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main
/**
 * start runing server frame
 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ServerFrame aFrame = new ServerFrame(); // create ServerFrame
		aFrame.start(primaryStage);
	}
/**
 * this method start running server with given port
 * @param p		port to connect
 */
	public static void runServer(String p) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new Server(port);

		try {
			flag = true;
			sv.listen(); // Start listening for connections			
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			flag = false;
		}
	}
	
	public static void runServer(String p,Connection conn) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new Server(port,conn);

		try {
			flag = true;
			sv.listen(); // Start listening for connections			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR - Could not listen for clients!");
			flag = false;
		}
	}

}
