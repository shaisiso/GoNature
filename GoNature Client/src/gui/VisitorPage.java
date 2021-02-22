package gui;

import java.io.IOException;
import client.Client;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Guide;
import logic.Msg;
import logic.Subscriber;
import logic.Visitor;
/**
 * this class is to show gui of visitor's page after identifying with id or sub. number
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class VisitorPage {
/**
 * text for name
 */
	@FXML
	private TextField nameTxt;
	/**
	 * label for name
	 */
	@FXML
	private Text nameLbl;
	/**
	 * text for hello message
	 */
	@FXML
	private Text helloTxt;
	/**
	 * button for new order page
	 */
	@FXML
	private Button newOrderBtn;
	/**
	 * button for my order page
	 */
	@FXML
	private Button myOrdersBtn;
	/**
	 * text for phone
	 */
	@FXML
	private TextField phoneTxt;
	/**
	 * label for phone
	 */
	@FXML
	private Text phoneLbl;
	/**
	 * text for email
	 */
	@FXML
	private TextField emailTxt;
	/**
	 * label for email
	 */
	@FXML
	private Text emailLbl;
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
	 * button for return
	 */
	@FXML
	private Button returnBtn;
	/**
	 * image for return
	 */
	@FXML
	private ImageView returnImg;
/**
 * visitor details
 */
	private Visitor visitor;

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
	 * this method load previous page
	 *
	 * @param event pressing on back button
	 */
	@FXML
	void goBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/MainPage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
			primaryStage.setTitle("Main");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
		}

	}
/**
 * this method will upload MyOrders page
 * @param event		click on my orders button
 * @throws IOException
 */
	@FXML
	void myOrders(ActionEvent event) throws IOException {
		Msg myOrdersMsg = new Msg("My orders", visitor);
		Parent root;
		FXMLLoader loader = new FXMLLoader();
		ClientUI.chat.accept(myOrdersMsg);
		if (Client.myOrders.isEmpty()) {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg("You dont have any orders, hope you will make some :)");
			error.start(root);
		} else {
			root = loader.load(getClass().getResource("/gui/MyOrders.fxml").openStream());
			MyOrders myOrders = loader.getController();
			myOrders.loadOrders(visitor,Client.myOrders);
			((Node) event.getSource()).getScene().getWindow().hide();
			myOrders.start(root);
		}
	}
	/**
	 * this method will upload NewOrder page  
	 * @param event		click on new order button
	 * @throws IOException
	 */
	@FXML
	void newOrder(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/NewOrder.fxml").openStream());
		NewOrder newOrder = loader.getController();
		newOrder.loadVisitor(visitor);
		((Node) event.getSource()).getScene().getWindow().hide();
		newOrder.start(root);
	}
/**
 * this method will load visitor details to this class
 * @param existedVisitor	visitor details
 */
	public void loadVisitor(Visitor existedVisitor) {
		this.visitor = existedVisitor;
		if ((visitor instanceof Subscriber) || (visitor instanceof Guide)) {
			helloTxt.setText("Hello " + visitor.getFirstName());
			nameTxt.setText(visitor.getFirstName() + " " + visitor.getLastName());
			phoneTxt.setText(visitor.getPhone());
			emailTxt.setText(visitor.getEmail());
		} else {
			helloTxt.setText("Hello guest: " + visitor.getId());
			nameTxt.setVisible(false);
			phoneTxt.setVisible(false);
			emailTxt.setVisible(false);
			nameLbl.setVisible(false);
			phoneLbl.setVisible(false);
			emailLbl.setVisible(false);

		}
	}
	/**
	 * start presenting this page
	 * @param root parent to set in scene
	 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Visitor");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
