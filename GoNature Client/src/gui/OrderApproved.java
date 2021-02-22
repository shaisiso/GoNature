package gui;

import java.io.IOException;

import assistance.InputController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;
import logic.Visitor;

/**
 * this class is to show a confirmation of an order
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
public class OrderApproved {
	/**
	 * order id text
	 */
	@FXML
	private Text orderIDTxt;
	/**
	 * button to info page
	 */
	@FXML
	private Button infoBtn;
	/**
	 * button to about page
	 */
	@FXML
	private Button aboutBtn;
	/**
	 * button to return
	 */
	@FXML
	private Button returnBtn;
	/**
	 * image to return
	 */
	@FXML
	private ImageView returnImg;
	/**
	 * text of order date
	 */
	@FXML
	private Text dateTxt;
	/**
	 * text of park name
	 */
	@FXML
	private Text parkTxt;
	/**
	 * text of order number of visitors
	 */
	@FXML
	private Text visitorsTxt;
	/**
	 * text of order price
	 */
	@FXML
	private Text priceTxt;
	/**
	 * text of order price updated for guide
	 */
	@FXML
	private Text guidePriceTxt;
	/**
	 * text with info to guide if he want to pay in advance
	 */
	@FXML
	private Text guideTxt;
	/**
	 * visitor details
	 */
	private Visitor visitor;
	/**
	 * order details
	 */
	private Order order;

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
	 * this method go to visitor page
	 * 
	 * @param event click on done button
	 * @throws IOException
	 */
	@FXML
	void done(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/VisitorPage.fxml").openStream());
		VisitorPage visitorPage = loader.getController();
		visitorPage.loadVisitor(visitor);
		((Node) event.getSource()).getScene().getWindow().hide();
		visitorPage.start(root);
	}

	/**
	 * this method is to load to class and screen the order and visitor details
	 * 
	 * @param order   order details
	 * @param visitor visitor details
	 */
	public void loadDetails(Order order, Visitor visitor) {
		this.visitor = visitor;
		this.order = order;
		orderIDTxt.setText("Order ID: " + String.valueOf(order.getOrderID()));
		parkTxt.setText(order.getParkName());
		visitorsTxt.setText(String.valueOf(order.getNumberOfVisitors()));
		dateTxt.setText(InputController.getIsraelFormatDate(order.getDate()) + "	" + order.getHour() + ":00");
		priceTxt.setText("" + order.getCost() + "¤");
		if (order.isOrganized()) {
			guidePriceTxt.setVisible(true);
			guideTxt.setVisible(true);
			guidePriceTxt.setText("" + order.getCost() * 0.88 + "¤");
		}

	}

	/**
	 * this method is to start presenting this page
	 * 
	 * @param root parent to set in scene
	 * @throws IOException
	 */
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Approved");
		primaryStage.setScene(scene);
		primaryStage.show();
		FXMLLoader loader = new FXMLLoader();
		root = loader.load(getClass().getResource("/gui/MessageSimulationNewOrder.fxml").openStream());
		MessageOrder messageOrder = loader.getController();
		messageOrder.loadDetails(order, visitor);
		messageOrder.start(root);

	}

}
