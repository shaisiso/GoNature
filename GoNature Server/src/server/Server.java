package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import gui.MessageApprove;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import logic.Employee;
import logic.Guide;
import logic.Msg;
import logic.Order;
import logic.Park;
import logic.Subscriber;
import logic.Visitor;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Server extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	/**
	 * conn - connection to database clients - arrayList of connected clients to the
	 * server connectedUsers - arrayList of the connected users to the system
	 */
	public Connection conn;
	boolean connFromDB = true;
	/**
	 * ArrayList of clients connection to present later in gui when asked
	 */
	public static ArrayList<ConnectionToClient> clients = new ArrayList<>();
	/**
	 * ArrayList of employees to ensure that employee is connected only on one
	 * device
	 */
	public static ArrayList<Employee> connectedUsers = new ArrayList<>();
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public Server(int port) {
		super(port);
	}

	public Server(int port, Connection conn) {
		super(port);
		this.conn = conn;
		connFromDB = false;
	}
	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		if (!clients.contains(client))
			clients.add(client);
		String id;
		Msg m = (Msg) msg;
		System.out.println("Message received: " + m.action + " from " + client);
		Visitor visitor;
		Order order;
		Park park;
		String[] update;
		String st[], orderID;

		// find action
		switch (m.action) {

		case "Visitor login ID":
			id = (String) m.content;
			visitor = LoginController.guideLogin(conn, id);// search guide
			if (visitor == null) {
				visitor = LoginController.subsIdLogin(conn, id);// search subscriber
				if (visitor == null)
					visitor = LoginController.visitorIdLogin(conn, id);// search existed visitor
			}
			try {
				client.sendToClient(new Msg("Visitor details", visitor));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Visitor login Subscription no.":
			id = (String) m.content;
			Subscriber subscriber;
			subscriber = LoginController.subsLogin(conn, id);
			if (subscriber == null) {
				System.out.println("subscriber not found");
				try {
					client.sendToClient(new Msg("Error", null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("subscriber found\n" + subscriber);
				try {
					client.sendToClient(new Msg("Subscriber details", subscriber.toString()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		case "Employee login":
			Employee employee = (Employee) m.content;
			if (connectedUsers.contains(employee)) {
				System.out.println("employee is connected");

				try {
					client.sendToClient(new Msg("Error", null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // employee not connected
				employee = LoginController.employeeLogin(conn, employee);
				if (employee == null) {
					System.out.println("employee not found");
					try {
						client.sendToClient(new Msg("Error", null));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("employee found\n" + employee.getFirstName() + " " + employee.getLastName());
					try {
						connectedUsers.add(employee);
						System.out.println(employee.getRole());
						client.sendToClient(new Msg("Employee details", employee.toString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		case "Log out":
			if (connectedUsers.remove((Employee) m.content))
				try {
					client.sendToClient(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			break;
		case "Subscriber registration":
			Subscriber sub = (Subscriber) m.content;
			int subNumber = RegistrationController.registerFamMember(conn, sub);
			if (subNumber != -1) {
				try {
					client.sendToClient(new Msg("New subscriber", subNumber));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("subscriber was not added");
				try {
					client.sendToClient(new Msg("Error", null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		case "Guide registration":
			Guide guide = (Guide) m.content;
			if (RegistrationController.registerGuide(conn, guide)) {
				try {
					client.sendToClient(new Msg("New guide", null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("guide was not added");
				try {
					client.sendToClient(new Msg("Error", null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case "New Order":
			order = (Order) m.content;
			Msg getMsg = OrdersController.newOrder(conn, order);
			try {
				client.sendToClient(getMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case "Join waiting list":
			order = (Order) m.content;
			try {
				boolean added = WaitingListController.addToWaitingList(conn, order);
				client.sendToClient(new Msg("Waiting list", added));

			} catch (IOException e) {
				e.printStackTrace();
			}

			break;

		case "Alternative dates":
			order = (Order) m.content;
			Map<String, ArrayList<Integer>> dates = OrdersController.getAlternativeDates(conn, order);
			try {
				client.sendToClient(new Msg("Alternative dates", dates));
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case "My orders":
			visitor = (Visitor) m.content;
			ArrayList<Order> orders = OrdersController.getVisitorOrder(conn, visitor);
			try {
				client.sendToClient(new Msg("My orders", orders));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Cancel order":
			order = (Order) m.content;
			OrdersController.cancelOrder(conn, order);
			ArrayList<Order> waitingList = WaitingListController.checkWaitingList(conn, order);
			for (Order o : waitingList) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							FXMLLoader loader = new FXMLLoader();
							Parent root = loader
									.load(getClass().getResource("/gui/MessageSimulationApprove.fxml").openStream());
							MessageApprove mwl = loader.getController();
							mwl.loadOrder(o, conn, true);
							mwl.start(root);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				Platform.runLater(t);
			}

			try {
				client.sendToClient(new Msg("Was canceled", null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Park details":
			String parkName = (String) m.content;
			park = ParkController.parkDetails(conn, parkName);
			try {
				client.sendToClient(new Msg("Park details", park));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Park updates":
			boolean updateSuccess;
			update = (String[]) m.content;
			if (update.length == 3)
				updateSuccess = ParkController.insertUpdate(conn, update);
			else
				updateSuccess = ParkController.insertDiscountUpdate(conn, update);
			try {
				client.sendToClient(new Msg("Park updates", updateSuccess));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Get all parks":
			ArrayList<Park> allParks = ParkController.getAllParks(conn);
			try {
				client.sendToClient(new Msg("All parks", allParks));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Order Details":
			orderID = (String) m.content;
			order = OrdersController.getOrderDetails(conn, orderID);
			try {
				client.sendToClient(new Msg("Order Details", order));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Update #visitors":
			order = (Order) m.content;
			OrdersController.updateVisitors(conn, order);
			try {
				client.sendToClient(new Msg("Just return", null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Enter park":
			st = (String[]) m.content;
			orderID = st[0];
			parkName = st[1];
			order = EntranceController.enterPark(conn, orderID, parkName);
			try {
				client.sendToClient(new Msg("Enter park", order));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Exit park":
			st = (String[]) m.content;
			orderID = st[0];
			parkName = st[1];
			boolean exit = EntranceController.exitPark(conn, orderID, parkName);
			try {
				client.sendToClient(new Msg("Exit park", exit));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Check capacity":
			parkName = (String) m.content;
			int capacity = EntranceController.getCapacity(conn, parkName);
			try {
				client.sendToClient(new Msg("Capacity", capacity));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Show updates":
			parkName = (String) m.content;
			park = ParkController.parkDetails(conn, parkName);
			try {
				client.sendToClient(new Msg("Show updates", park));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Show requests":
			parkName = (String) m.content;
			Map<String, ArrayList<String>> requests = ParkController.showRequests(conn, parkName);
			try {
				client.sendToClient(new Msg("Show requests", requests));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Approve update":
			parkName = (String) m.content2;
			String updateSt = (String) m.content;
			ParkController.approveUpdate(conn, parkName, updateSt);
			try {
				client.sendToClient(new Msg("Just return", null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Remove update":
			parkName = (String) m.content2;
			String updateSt2 = (String) m.content;
			ParkController.removeUpdate(conn, parkName, updateSt2);
			try {
				client.sendToClient(new Msg("Just return", null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Visitors report":
			st = (String[]) m.content;
			int info[] = ManagersController.visitorsReport(conn, st);
			try {
				client.sendToClient(new Msg("Visitors report", info));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Capacity report":
			st = (String[]) m.content;
			ArrayList<String> notFullCapacity = ManagersController.capacityReport(conn, st);
			try {
				client.sendToClient(new Msg("Capacity report", notFullCapacity));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Get available space":
			int available = EntranceController.getAvailablePlace(conn, (Order) m.content);
			try {
				client.sendToClient(new Msg("Available space", available));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Unplanned visit":
			order = (Order) m.content;
			Msg orderMsg = EntranceController.unplannedOrder(conn, order);
			try {
				client.sendToClient(new Msg("Unplanned visit", orderMsg));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "View report":
			st = (String[]) m.content;
			boolean isExistedReport = ManagersController.reportExist(conn, st[0], st[1], Integer.parseInt(st[2]),
					Integer.parseInt(st[3]));
			try {
				client.sendToClient(new Msg("View report", isExistedReport));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Cancellation report":
			st = (String[]) m.content;
			int cancels[] = ManagersController.cancellationReport(conn, st[0], st[1]);
			try {
				client.sendToClient(new Msg("Cancellation report", cancels));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Show exsisting reports":
			st = (String[]) m.content;
			String existingReports = ManagersController.ShowExsistingReports(conn, st);
			try {
				client.sendToClient(new Msg("Show exsisting reports", existingReports));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Report exist":
			st = (String[]) m.content;
			boolean exist = ManagersController.reportExist(conn, st[0], st[1], Integer.parseInt(st[2]),
					Integer.parseInt(st[3]));
			try {
				client.sendToClient(new Msg("Report exist", exist));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Income report":
			st = (String[]) m.content;
			float[] incomes = ManagersController.incomeReport(conn, st[0], st[1], st[2]);
			try {
				client.sendToClient(new Msg("Income report", incomes));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Visitation report":
			st = (String[]) m.content;
			float visitorsTime[][] = ManagersController.visitationReport(conn, st[0], st[1], st[2]);
			try {
				client.sendToClient(new Msg("Visitation report", visitorsTime));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Update cost casual visit":
			order = (Order) m.content;
			order.setCost(OrdersController.calculateCostCasualVisit(conn, order));
			try {
				client.sendToClient(new Msg("Enter park", order));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		/*
		 * connect db
		 */
		if (connFromDB) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				System.out.println("Driver definition succeed");
			} catch (Exception ex) {
				// handle the error
				System.out.println("Driver definition failed");
			}

			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost/gonature?serverTimezone=IST", "root",
						"Aa123456");
				// Connection conn =
				// DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
				System.out.println("SQL connection succeed");
				// createTableCourses(conn);
			} catch (SQLException ex) {// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}

		//	OrdersController.sendSmsDayBeforeOrder(conn);
		}
	}

	public void setConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.conn = conn;
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
		try {
			conn.close();
		} // close connection to db
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
//End of Server class