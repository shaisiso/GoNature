package gui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Assitant;
import logic.Order;

/**
 * this is a form for message simulation with just info
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class MessageOrderInfo {
	/**
	 * text of email
	 */
	@FXML
	private Label emailTxt;
	/**
	 * phone label
	 */
	@FXML
	private Label phoneTxt;
	/**
	 * order id text
	 */
	@FXML
	private Text orderIdTxt;
	/**
	 * order date text
	 */
	@FXML
	private Text dateTxt;
	/**
	 * order time text
	 */
	@FXML
	private Text timeTxt;
	/**
	 * order number of visitors text
	 */
	@FXML
	private Text visitorsTxt;
	/**
	 * order park name text
	 */
	@FXML
	private Text parkTxt;
	/**
	 * visitor id text
	 */
	@FXML
	private Text visitorIDtxt;
	/**
	 * text to write in the message
	 */
	@FXML
	private Text text1;
	/**
	 * another text to write in the message
	 */
	@FXML
	private Text text2;

	/**
	 * load to the class order details
	 * 
	 * @param order      order details
	 * @param isNewOrder to know if this message is after new order or after order
	 *                   was cancelled due to not confirming day before
	 */
	public void loadDetails(Order order, boolean isNewOrder) {

		parkTxt.setText(order.getParkName());
		visitorsTxt.setText(String.valueOf(order.getNumberOfVisitors()));
		dateTxt.setText(Assitant.getIsraelFormatDate(order.getDate()));
		timeTxt.setText(order.getHour() + ":00");
		emailTxt.setText("Sent to email: " + order.getEmail());
		visitorIDtxt.setText(order.getVisitorID());
		if (order.getPhone() != null)
			phoneTxt.setText("Sent to phone number: " + order.getPhone());
		if (isNewOrder) {
			orderIdTxt.setText(String.valueOf(order.getOrderID()));
			text1.setText("Your order has been placed");
			text2.setText("Waiting to see you :)");
		} else { // message for canceled after no approve order
			orderIdTxt.setVisible(false);
			text1.setFill(Color.RED);
			text1.setText("Your order was canceled beacause you did not approve it");
			text2.setText("hope to see you in other time :(");
		}
	}

	/**
	 * start presenting message
	 * 
	 * @param root parent to set in scene
	 */
	public void start(Parent root) {
		AudioClip note = new AudioClip(this.getClass().getResource("NotificationSound.wav").toString());
		note.play();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Message simulation");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
