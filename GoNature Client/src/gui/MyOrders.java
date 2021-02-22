package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import assistance.InputController;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Guide;
import logic.Msg;
import logic.Order;
import logic.Subscriber;
import logic.Visitor;

/**
 * this class is a page of visitor's orders
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 *
 *
 */
public class MyOrders {
	/**
	 * label to print visitors name/id
	 */
	@FXML
	private Label hiTxt;
	/**
	 * cBox of orders
	 */
	@FXML
	private ComboBox<String> cBox;
	/**
	 * cancel order button
	 */
	@FXML
	private Button cancelBtn;
	/**
	 * button for info page
	 */
	@FXML
	private Button infoBtn;
	/**
	 * button for about page
	 */
	@FXML
	private Button aboutBtn;
	/**
	 * button for previous page
	 */
	@FXML
	private Button returnBtn;
	/**
	 * image of return
	 */
	@FXML
	private ImageView returnImg;
	/**
	 * text for order id
	 */
	@FXML
	private Text orderTxt;
	/**
	 * text for park name
	 */
	@FXML
	private Text parkTxt;
	/**
	 * text for order's hour
	 */
	@FXML
	private Text hourTxt;
	/**
	 * text for order's number of visitors
	 */
	@FXML
	private Text numberTxt;
	/**
	 * text for visitor's email
	 */
	@FXML
	private Text emailTxt;
	/**
	 * text for cost
	 */
	@FXML
	private Text costTxt;
	/**
	 * text for organized group pre-cost (guide does not pay)
	 */
	@FXML
	private Text organizedCost;
	/**
	 * save visitor details
	 */
	private Visitor visitor;
	/**
	 * ArrayList of visitor's orders
	 */
	private ArrayList<Order> orders;
	/**
	 * list for cBox
	 */
	ObservableList<String> list;
	/**
	 * selected order details
	 */
	private Order orderSelected;

	/**
	 * this method load info page
	 *
	 * @param event pressing on info button
	 * @throws IOException
	 */
	@FXML
	void infoPage(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/InfoPage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Information");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method load about page
	 *
	 * @param event pressing on about button
	 * @throws IOException
	 */
	@FXML
	void aboutPage(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/AboutPage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("About");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method cancels the selected order
	 * 
	 * @param event click on cancel order button
	 * @throws IOException
	 */
	@FXML
	void cancelOrder(ActionEvent event) throws IOException {
		Alert ask = new Alert(AlertType.CONFIRMATION);
		ask.setTitle("Confirmation Dialog");
		ask.setHeaderText("Are you sure you want to cancel this order?");

		Optional<ButtonType> result = ask.showAndWait();
		if (result.get() == ButtonType.OK) {
			Msg cancelMsg = new Msg("Cancel order", orderSelected);
			ClientUI.chat.accept(cancelMsg);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Your order have been canceled");
			alert.setContentText("hope to see you some other time :(");
			alert.showAndWait();
			orders.remove(orderSelected);
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/MyOrders.fxml").openStream());
			MyOrders myOrders = loader.getController();
			myOrders.loadOrders(visitor, orders);
			((Node) event.getSource()).getScene().getWindow().hide();
			myOrders.start(root);
		}
	}

	/**
	 * this method load order details after choosing a date from cBox
	 * 
	 * @param event change order date in cBox
	 */
	@FXML
	void dates(ActionEvent event) {
		String date = InputController.getDBFormatDate(cBox.getValue());
		for (Order temp : orders) {
			if (temp.getDate().equals(date)) {
				orderSelected = temp;
				orderTxt.setText("" + temp.getOrderID());
				parkTxt.setText(temp.getParkName());
				hourTxt.setText(temp.getHour() + ":00");
				numberTxt.setText("" + temp.getNumberOfVisitors());
				emailTxt.setText(temp.getEmail());
				costTxt.setText("" + temp.getCost() + "¤");
				if (temp.isOrganized())
					organizedCost.setText(
							"If you choose to pay in advance the price will be: " + temp.getCost() * 0.88 + "¤");
				else
					organizedCost.setText("");
				break;
			}
		}
	}

	/**
	 * this method will load the previous page - visitorPage
	 * 
	 * @param event click on go back button
	 * @throws IOException
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/VisitorPage.fxml").openStream());
		VisitorPage visitorPage = loader.getController();
		visitorPage.loadVisitor(visitor);
		((Node) event.getSource()).getScene().getWindow().hide();
		visitorPage.start(root);
	}

	/**
	 * this method load visitor's details and orders and save them in class
	 * variables
	 * 
	 * @param visitor visitor that entered
	 * @param orders  visitor's orders
	 */
	public void loadOrders(Visitor visitor, ArrayList<Order> orders) {
		ArrayList<String> al = new ArrayList<>();
		this.visitor = visitor;
		this.orders = orders;
		if (visitor instanceof Subscriber || visitor instanceof Guide)
			hiTxt.setText("Hi " + visitor.getFirstName());
		else
			hiTxt.setText(visitor.getId());
		for (Order o : orders)
			al.add(InputController.getIsraelFormatDate(o.getDate()));
		list = FXCollections.observableArrayList(al);
		cBox.setItems(list);
	}

	/**
	 * this method upload this page
	 * 
	 * @param root parent to set in scene
	 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("My Orders");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
