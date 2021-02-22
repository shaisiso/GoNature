package gui;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logic.Assitant;
import logic.Msg;
import logic.Order;
import logic.Visitor;
import server.OrdersController;
import server.WaitingListController;

/**
 * this is a form for message simulation where the addressee need to
 * approve/cancel
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class MessageApprove {
	/**
	 * email label
	 */
	@FXML
	private Label emailLabel;
	/**
	 * order date text for
	 */
	@FXML
	private Label dateTxt;
	/**
	 * phone text
	 */
	@FXML
	private Label phoneTxt;
	/**
	 * phone label
	 */
	@FXML
	private Label phoneLabel;
	/**
	 * email text
	 */
	@FXML
	private Label emailTxt;
	/**
	 * number of visitors text
	 */
	@FXML
	private Label numberTxt;
	/**
	 * park text
	 */
	@FXML
	private Label parkTxt;
	/**
	 * approve button
	 */
	@FXML
	private Button approveBtn;
	/**
	 * cancel button
	 */
	@FXML
	private Button cancelBtn;
	/**
	 * visitor id text
	 */
	@FXML
	private Label idTxt;
	/**
	 * text for head message
	 */
	@FXML
	private Text headTxt1;
	/**
	 * text for head second message
	 */
	@FXML
	private Text headTxt2;
	/**
	 * order id label
	 */
	@FXML
	private Label orderLbl;
	/**
	 * order id text
	 */
	@FXML
	private Label orderIdTxt;
	/**
	 * save order details
	 */
	private Order order;
	/**
	 * connection to db for sending another messages if canceling
	 */
	private Connection conn;
	/**
	 * to know if it is waiting list message or day before visit message
	 */
	private boolean isWaitingList;
	/**
	 * save this stage
	 */
	private Stage primaryStage;
	/**
	 * for waiting [timeForCancel] and then cancel
	 */
	private PauseTransition delay;
	/**
	 * the time to wait. waiting list - 1 hour, day before visit - 2 hours
	 */
	private int timeForCancel;

	/**
	 * this method approve the order
	 * 
	 * @param event click on approve button
	 * @throws IOException
	 */
	@FXML
	void approveOrder(ActionEvent event) throws IOException {
		// reset method for close without deleting order
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			}
		});
		delay.stop();

		if (isWaitingList) { // add to orders table after approve waiting list message
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(
					"Be aware: all your previous orders and requests for waiting list for the same date will be cancelled");
			alert.setContentText("Are you ok with this?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				ArrayList<Order> list = OrdersController.getVisitorOrder(conn, new Visitor(order.getVisitorID()));
				// if this visitor has order to the same date of the new order from waiting
				// list, we cancel the previous
				for (Order o : list)
					if (o.getDate().equals(order.getDate())) {
						OrdersController.cancelOrder(conn, o);
						break;
					}
				Msg chcek = OrdersController.newOrder(conn, order);
				if (chcek.content == null) {// Doesn't suppose to happen because we take care of those cases
					FXMLLoader loader = new FXMLLoader();
					Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
					Errors error = loader.getController();
					error.setMsg(chcek.action);
					error.start(root);
				} else { // order was added
					((Node) event.getSource()).getScene().getWindow().hide();
					FXMLLoader loader = new FXMLLoader();
					Parent root = loader.load(getClass().getResource("/gui/MessageSimulationInfo.fxml").openStream());
					MessageOrderInfo mo = loader.getController();
					mo.loadDetails(order, true);
					mo.start(root);
				}
			}
		}

		primaryStage.close();
	}

	/**
	 * this method send to cancel method for canceling order
	 * 
	 * @param event click on cancel button
	 */
	@FXML
	void cancelOrder(ActionEvent event) {
		Alert ask = new Alert(AlertType.CONFIRMATION);
		ask.setTitle("Confirmation Dialog");
		ask.setHeaderText("Are you sure you want to cancel this order?");
		Optional<ButtonType> result = ask.showAndWait();
		if (result.get() == ButtonType.OK) {
			cancelMethod();
			delay.stop();
			primaryStage.close();
		}
	}

	/**
	 * this method load the relevant details for this page
	 * 
	 * @param order         order details
	 * @param conn          connection for db
	 * @param isWaitingList true - waiting list message, false - day before message
	 */
	public void loadOrder(Order order, Connection conn, boolean isWaitingList) {
		this.order = order;
		this.conn = conn;
		this.isWaitingList = isWaitingList;
		dateTxt.setText(Assitant.getIsraelFormatDate(order.getDate()) + "	" + order.getHour() + ":00");
		parkTxt.setText(order.getParkName());
		numberTxt.setText("" + order.getNumberOfVisitors());
		emailTxt.setText(order.getEmail());
		idTxt.setText(order.getVisitorID());
		if (order.getPhone() != null) {
			phoneLabel.setVisible(true);
			phoneTxt.setText(order.getPhone());
		}
		if (isWaitingList) { // message to the next in waiting list
			headTxt1.setText("You were the next on the waiting list and your order can now be placed.");
			headTxt2.setText("You must confirm the order within an hour, otherwise it will be canceled.");
			orderIdTxt.setVisible(false);
			orderLbl.setVisible(false);
			timeForCancel = 3600;// 1 hour
		} else { // message to approve order day before
			orderIdTxt.setText(order.getOrderID() + "");
			headTxt1.setText("You have an order for tomorrow.");
			headTxt2.setText("You must confirm the order within 2 hour, otherwise it will be canceled.");
			timeForCancel = 7200;// 2 hours
		}
	}

	/**
	 * start presenting message set delay for stop the presentation and cancel order
	 * 
	 * @param root parent for scene
	 */
	public void start(Parent root) {
		AudioClip note = new AudioClip(this.getClass().getResource("NotificationSound.wav").toString());
		note.play();
		primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Message");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				cancelMethod();
			}
		});
		delay = new PauseTransition(Duration.seconds(timeForCancel)); // wait 1 or 2 hours for approve
		delay.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cancelMethod();
				try {
					FXMLLoader loader = new FXMLLoader();
					Parent root = loader.load(getClass().getResource("/gui/MessageSimulationInfo.fxml").openStream());
					MessageOrderInfo messageOrderInfo = loader.getController();
					messageOrderInfo.loadDetails(order, false);
					messageOrderInfo.start(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
				primaryStage.close();
			}
		});
		delay.play();
		primaryStage.show();

	}

	/**
	 * this is method for cancel order for every reason
	 */
	private void cancelMethod() {
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
	}
}
