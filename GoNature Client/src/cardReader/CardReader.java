package cardReader;

import java.io.IOException;

import assistance.InputController;
import assistance.OnlyNumbers;
import client.Client;
import client.ClientUI;
import gui.Errors;
import gui.Invoice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Msg;
import logic.Order;

/**
 * this class is simulation for enter and exit the park
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class CardReader {
/**
 * text for park name
 */
	@FXML
	private Text parkTxt;
	/**
	 * text field for order id to enter
	 */
	@FXML
	private TextField enterTxt;
	/**
	 * text field for order id to exit
	 */
	@FXML
	private TextField exitTxt;
/**
 * button for enter
 */
	@FXML
	private Button goBtn;
/**
 * button for exit
 */
	@FXML
	private Button exitBtn;
	/**
	 * text field for number of visitors that arrived
	 */
	@FXML
	private TextField visitorsTxt;
/**
 * button to show number of visitor from original order
 */
	@FXML
	private Button visitorsBtn;
	/**
	 * save park details
	 */
	private String park;
	/**
	 * save order details
	 */
	private Order order;

	/**
	 * save park details and show it on screen
	 * 
	 * @param park	park to load
	 */
	public void loadPark(String park) {
		this.park = park;
		parkTxt.setText("Park: " + park);
	}

	/**
	 * simulation for enter the park if the orderID is valid
	 * 
	 * @param event click on enter park
	 * @throws IOException
	 */
	@FXML
	void enterPark(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		String errMsg = "Please enter order ID", toSend[] = new String[2];
		boolean flag = true;
		if (InputController.notNull(enterTxt.getText())) {
			flag = false;
			toSend[0] = enterTxt.getText();// order id
			toSend[1] = park;// get this park
			Msg visitorsMsg = new Msg("Order Details", enterTxt.getText());
			ClientUI.chat.accept(visitorsMsg);
			order = Client.order;
			int thisHour = Integer.parseInt(java.time.LocalTime.now().toString().substring(0, 2));
			if (order.getDate().equals(java.time.LocalDate.now().toString()) && order.getParkName().equals(park)) {
				if (order.getHour()< thisHour) {
					loadError("You came too late. if you want to enter please make a new casual order. sorry :(");
					return;
				} else if (order.getHour()-1 > thisHour) {
					loadError("You came too early. you can not enter right now, come back later");
					return;
				}
			}
			if (InputController.notNull(visitorsTxt.getText())) {
				int newNumberVisitors = Integer.parseInt(visitorsTxt.getText());
				if (order.getNumberOfVisitors() < newNumberVisitors) {
					loadError("You can enter with " + order.getNumberOfVisitors()
							+ ". for more visitors order for them a new visit");
					return;
				} else { // update number of visitors in order
					System.out.println();
					order.setNumberOfVisitors(newNumberVisitors);
					Msg m = new Msg("Update #visitors", order);
					ClientUI.chat.accept(m);
				}
			}
			Msg enterPark = new Msg("Enter park", toSend);
			ClientUI.chat.accept(enterPark);
			if (Client.order != null) {
				ClientUI.chat.accept(new Msg("Update cost casual visit", order));
				order = Client.order;
				root = loader.load(getClass().getResource("/gui/Invoice.fxml").openStream());
				Invoice invoice = loader.getController();
				invoice.loadOrder(order);
				invoice.start(root);

			} else {
				errMsg = "Error beacause one of the following:\n" + "1.order id is not valid\n2. other park or date"
						+ "\n3. already entered";
				flag = true;
			}
		}
		if (flag) {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(errMsg);
			error.start(root);
		}
	}

	/**
	 * simulation for exit the park if the orderID is valid
	 * 
	 * @param event click on enter park
	 * @throws IOException
	 */
	@FXML
	void exitPark(ActionEvent event) throws IOException {
		String errMsg = "Please enter order ID", toSend[] = new String[2];
		boolean flag = true;
		if (InputController.notNull(exitTxt.getText())) {
			flag = false;
			toSend[0] = exitTxt.getText();// order id
			toSend[1] = park;// get this park
			Msg exitPark = new Msg("Exit park", toSend);
			ClientUI.chat.accept(exitPark);
			if (Client.exit) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Exit the park");
				alert.setContentText("Order id: " + exitTxt.getText() + " was exit from the park");
				alert.showAndWait();
			} else {
				errMsg = "Error beacause one of the following:\n1. order was not entered to the park"
						+ "\n2. already exited";
				flag = true;
			}
		}
		if (flag)
			loadError(errMsg);

	}

	/**
	 * this method check the number of visitors that was ordered
	 */
	@FXML
	void getVisitors(ActionEvent event) {
		if (InputController.notNull(enterTxt.getText())) {
			Msg visitorsMsg = new Msg("Order Details", enterTxt.getText());
			ClientUI.chat.accept(visitorsMsg);
			order = Client.order;
			if (order == null)
				loadError("This order id does not exist");
			else
				visitorsTxt.setText(order.getNumberOfVisitors() + "");
		}

		else
			loadError("Enter order id");
	}

	/**
	 * this method get string of error and show pop up with the error messagge
	 * 
	 * @param errMsg - error message to show
	 */
	private void loadError(String errMsg) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(errMsg);
			error.start(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method upload stage of card reader
	 * 
	 * @param root - from another page
	 * @throws IOException
	 */
	public void start(Parent root) throws IOException {
		enterTxt.textProperty().addListener(new OnlyNumbers(enterTxt));
		exitTxt.textProperty().addListener(new OnlyNumbers(exitTxt));
		visitorsTxt.textProperty().addListener(new OnlyNumbers(visitorsTxt));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Card reader");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
