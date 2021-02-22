package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;

/**
 * this is a class to show invoice after placing an order/entering the park
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Invoice {
	/**
	 * text to show price
	 */
	@FXML
	private Text priceTxt;
	/**
	 * button for close screen
	 */
	@FXML
	private Button closeBtn;
	/**
	 * text to show number of visitors
	 */
	@FXML
	private Text visitorsTxt;
	/**
	 * text to show order id
	 */
	@FXML
	private Text ordeIDtxt;

	/**
	 * this method closing the current screen
	 * 
	 * @param event click on close button
	 */
	@FXML
	void closeScreen(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * method to load order details from the previous page
	 * 
	 * @param order - order details
	 */
	public void loadOrder(Order order) {
		priceTxt.setText(order.getCost() + "");
		visitorsTxt.setText(order.getNumberOfVisitors() + "");
		ordeIDtxt.setText(order.getOrderID() + "");

	}

	/**
	 * this method upload Invoice page
	 * 
	 * @param root
	 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Invoice");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
