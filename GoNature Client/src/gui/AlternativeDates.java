package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import assistance.InputController;
import client.Client;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Msg;
import logic.Order;
import logic.Visitor;

/**
 * this is a class to show page of alternative dates and hours for next week
 * from order's date after trying to order for date and hour which the park
 * haven't enough place for that number of visitors
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class AlternativeDates {
	/**
	 * cBox of dates
	 */
	@FXML
	private ComboBox<String> dateCbox;
	/**
	 * cBox of hours
	 */
	@FXML
	private ComboBox<String> hoursCbox;
	/**
	 * button to order
	 */
	@FXML
	private Button orderBtn;
	/**
	 * button for info
	 */
	@FXML
	private Button infoBtn;
	/**
	 * button for about
	 */
	@FXML
	private Button aboutBtn;
	/**
	 * button to return
	 */
	@FXML
	private Button returnBtn;
	/**
	 * image of return
	 */
	@FXML
	private ImageView returnImg;
	/**
	 * text of park name
	 */
	@FXML
	private Text parkTxt;
/**
 * text for number of visitors
 */
	@FXML
	private Text visitorsTxt;
	/**
	 * lists for combobox
	 */
	ObservableList<String> list1, list2;
	/**
	 * a map that match for each avaialble date the available hours
	 */
	private Map<String, ArrayList<Integer>> dates;
	/**
	 * save order details
	 */
	private Order order;
	/**
	 * save the visitor details
	 */
	private Visitor visitor;

	/**
	 * the stars method to show this page
	 * 
	 * @param root
	 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Alternative dates");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * this method get map of available dates and hours and load combobox of that
	 * also save order and visitor details
	 * 
	 * @param dates   - map from available date to available hours
	 * @param order   - save the order details that the visitor tried to order
	 * @param visitor - visitor details
	 */
	public void loadDates(Map<String, ArrayList<Integer>> dates, Order order, Visitor visitor) {
		this.dates = dates;
		this.order = order;
		this.visitor = visitor;
		visitorsTxt.setText("Number of visitors: " + order.getNumberOfVisitors());
		parkTxt.setText("Park: " + order.getParkName());
		ArrayList<String> al = new ArrayList<String>();
		for (String date : dates.keySet()) {
			al.add(InputController.getIsraelFormatDate(date)); // to show date dd-mm-yyyy
		}
		list1 = FXCollections.observableArrayList(al);
		dateCbox.setItems(list1);
	}

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
		primaryStage.setTitle("About");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method load previous page - new order
	 *
	 * @param event pressing on back button
	 * @throws IOException
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/NewOrder.fxml").openStream());
		NewOrder newOrder = loader.getController();
		newOrder.loadVisitorAndOrder(visitor, order);
		((Node) event.getSource()).getScene().getWindow().hide();
		newOrder.start(root);
	}

	/**
	 * this method load the available hours after choosing a date
	 * 
	 * @param event - action of changing dates combobox value
	 */
	@FXML
	void loadHours(ActionEvent event) {
		ArrayList<String> al = new ArrayList<String>();
		String date = InputController.getDBFormatDate(dateCbox.getValue());// the date in map is yyyy-mm-dd
		ArrayList<Integer> hours = dates.get(date);
		for (int hour : hours) {
			if (hour < 10)
				al.add("0" + hour + ":00");
			else
				al.add(hour + ":00");
		}
		list2 = FXCollections.observableArrayList(al);
		hoursCbox.setItems(list2);
	}

	/**
	 * this method take the chosen date and hour and place an order with the order
	 * that was saved
	 * 
	 * @param event - click on order button
	 * @throws IOException
	 */
	@FXML
	void makeOrder(ActionEvent event) throws IOException {
		String date = InputController.getDBFormatDate(dateCbox.getValue());// the date in map is yyyy-mm-dd
		order.setDate(date);
		order.setHour(Integer.parseInt(hoursCbox.getValue().substring(0, 2)));
		Msg orderMsg = new Msg("New Order", order);
		ClientUI.chat.accept(orderMsg);
		FXMLLoader loader = new FXMLLoader();
		if (Client.order == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Unavailable date and hour for the requested park");
			alert.showAndWait();
			Parent root = loader.load(getClass().getResource("/gui/NewOrder.fxml").openStream());
			NewOrder newOrder = loader.getController();
			newOrder.loadVisitorAndOrder(visitor, order);
			((Node) event.getSource()).getScene().getWindow().hide();
			newOrder.start(root);

		} else {
			Parent root = loader.load(getClass().getResource("/gui/OrderApproved.fxml").openStream());
			OrderApproved orderApproved = loader.getController();
			orderApproved.loadDetails(Client.order, visitor);
			((Node) event.getSource()).getScene().getWindow().hide();
			orderApproved.start(root);
		}
	}

}
