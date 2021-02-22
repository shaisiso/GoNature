package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import logic.Employee;
import logic.Msg;
import logic.Order;
import logic.Park;
import logic.Subscriber;
import logic.Visitor;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Shon Freidman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version December 2020
 */
public class Client extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display all
	 * the variables and objects is for the gui to get the information that came
	 * from server
	 */
	ChatIF clientUI;
	public static Subscriber subscriber = null;
	public static Msg orderMsg = null;
	public static Visitor visitor = null;
	public static Employee employee = null;
	public static int newSubNumber = -1, capacity = 0, infoReport[], available = 0, canceledReport[];
	public static boolean guideWasAdded = false, addedWaitingList = false, exit = false, isExistedReport = false;
	public static boolean updates, reportExist;
	public static Order order = null;
	public static Map<String, ArrayList<Integer>> dates = null;
	public static Map<String, ArrayList<String>> requests = null;
	public static String reason = null, existingReports = null;
	public static ArrayList<Order> myOrders = null;
	public static ArrayList<String> capacityReport;
	public static ArrayList<Park> allParks;
	public static float incomes[], visitationReport[][];
	public static Park park = null;
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public Client(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;
		Msg m = (Msg) msg;
		String[] result;
		// get action
		switch (m.action) {

		case "Error":
			subscriber = null;
			employee = null;
			newSubNumber = -1;
			guideWasAdded = false;
			visitor = null;
			order = null;
			addedWaitingList = false;
			break;

		case "Subscriber details":
			String stSubscriber = (String) m.content;
			result = stSubscriber.split("\\s");
			subscriber = new Subscriber(result[0], result[1], result[2], result[3], result[4], result[5],
					Integer.parseInt(result[6]));

			break;

		case "Visitor details":
			visitor = (Visitor) m.content;
			break;
		case "Employee details":
			String stEmployee = (String) m.content;
			result = stEmployee.split("\\s");
			employee = new Employee(result[0], result[1], result[2], result[3], result[4], result[5], result[6],
					result[7]);
			break;

		case "New subscriber":
			newSubNumber = (int) m.content;
			break;
		case "New guide":
			guideWasAdded = true;
			break;
		case "Order Details":
			order = (Order) m.content;
			reason = null;
			break;
		case "Order: Already have an order to this date":
			order = null;
			reason = "Already have an order on this date";
			break;
		case "Order: Not available date and time":
			order = null;
			reason = "Unavailable date and hour for the requested park";
			break;
		case "Waiting list":
			addedWaitingList = (boolean) m.content;
			break;
		case "Alternative dates":
			dates = (Map<String, ArrayList<Integer>>) m.content;
			break;
		case "My orders":
			myOrders = (ArrayList<Order>) m.content;
			break;
		case "Park details":
			park = (Park) m.content;
			break;
		case "Park updates":
			updates = (boolean) m.content;
			break;
		case "All parks":
			allParks=(ArrayList<Park>)m.content;
			break;
		case "Enter park":
			order = (Order) m.content;
			break;
		case "Exit park":
			exit = (boolean) m.content;
			break;
		case "Capacity":
			capacity = (int) m.content;
			break;
		case "Show updates":
			park = (Park) m.content;
			break;
		case "Show requests":
			requests = (Map<String, ArrayList<String>>) m.content;
			break;
		case "Visitors report":
			infoReport = (int[]) m.content;
			break;
		case "Capacity report":
			capacityReport = (ArrayList<String>) m.content;
			break;
		case "Unplanned visit":
			orderMsg = (Msg) m.content;
			break;
		case "Available space":
			available = (int) m.content;
			break;
		case "View report":
			isExistedReport = (boolean) m.content;
			break;
		case "Cancellation report":
			canceledReport = (int[]) m.content;
			break;
		case "Show exsisting reports":
			existingReports = (String) m.content;
			break;
		case "Report exist":
			reportExist = (boolean) m.content;
			break;
		case "Income report":
			incomes = (float[]) m.content;
			break;
		case "Visitation report":
			visitationReport = (float[][]) m.content;
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Msg message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
