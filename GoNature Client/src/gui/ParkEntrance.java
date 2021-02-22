package gui;

import java.io.IOException;

import assistance.IEmployee;
import assistance.InputController;
import assistance.OnlyNumbers;
import cardReader.CardReader;
import client.Client;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;
import logic.Order;

/**
 * this class is a page for the park entrance role. managers can get here as
 * well
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
public class ParkEntrance implements IEmployee {
	/**
	 * current visitors of park tex
	 */
	@FXML
	private Text currenVisitorsTxt;
	/**
	 * button to update number of visitors
	 */
	@FXML
	private Button visitorsUpdateBtn;
	/**
	 * text for order id
	 */
	@FXML
	private Text orderIdTxt;
	/**
	 * button for enter visitor
	 */
	@FXML
	private Button enterVisitorBtn;
	/**
	 * text for number of visitors
	 */
	@FXML
	private TextField visitorsTxt;
	/**
	 * text for subscription number
	 */
	@FXML
	private TextField subscriptionTxt;
	/**
	 * text for show park name
	 */
	@FXML
	private Text parkNameTxt;
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
	 * text for guide id
	 */
	@FXML
	private TextField guideIdTxt;
	/**
	 * button to submit order
	 */
	@FXML
	private Button submitBtn;
	/**
	 * button to chekck capacity
	 */
	@FXML
	private Button capacityBtn;
	/**
	 * text with the capacity
	 */
	@FXML
	private Text capacityTxt;
	/**
	 * button to go to card reader - entrance and exit from park
	 */
	@FXML
	private Button cardReaderBtn;
	/**
	 * employee details
	 */
	private Employee employee;
	/**
	 * available place in this park
	 */
	private int availablePlace;
	/**
	 * new casual visit order details
	 */
	private Order newOrder;

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
	 * this method load previous page will logout if it the entrance worker
	 * 
	 * @param event pressing on go back button
	 * @throws IOException
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException {
		String role = employee.getRole();
		Stage primaryStage = new Stage();
		Parent root;
		if (role.equals("PManager") || role.equals("DManager")) { // go back to PManage or DManager
			((Node) event.getSource()).getScene().getWindow().hide();
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(getClass().getResource("/gui/" + role + ".fxml").openStream());
			IEmployee iEmployee = loader.getController();
			iEmployee.loadEmployee(employee);
			primaryStage.setTitle(role);
		} else { // log out entrance employee
			ClientUI.chat.accept(new Msg("Log out", employee));
			root = FXMLLoader.load(getClass().getResource("/gui/EmployeeLogin.fxml"));
			primaryStage.setTitle("Employees Log-in");
		}
		((Node) event.getSource()).getScene().getWindow().hide();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method overrides the one in the interface load employee details
	 */
	@Override
	public void loadEmployee(Employee employee) {
		this.employee = employee;
		newOrder = new Order(0);
		newOrder.setParkName(employee.getParkName());
		newOrder.setDate(java.time.LocalDate.now().toString());
		updateVisitors(null);
		parkNameTxt.setText("Park: " + employee.getParkName());
	}

	/**
	 * this method overrides the one in the interface start presenting this page
	 * 
	 * @param root Parent to scene
	 */
	@Override
	public void start(Parent root) throws IOException {
		guideIdTxt.textProperty().addListener(new OnlyNumbers(guideIdTxt));
		subscriptionTxt.textProperty().addListener(new OnlyNumbers(subscriptionTxt));
		visitorsTxt.textProperty().addListener(new OnlyNumbers(visitorsTxt));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Entrance");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				ClientUI.chat.accept(new Msg("Log out", employee));
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();

	}

	/**
	 * this method update the number of visitors that can be enter to the park
	 * currently
	 * 
	 * @param event click on update button
	 */
	@FXML
	void updateVisitors(ActionEvent event) {
		newOrder = new Order();
		newOrder.setParkName(employee.getParkName());
		newOrder.setDate(java.time.LocalDate.now().toString());
		newOrder.setHour(Integer.parseInt(java.time.LocalTime.now().toString().substring(0, 2)));
		System.out.println();
		Msg unplannedVisitMsg = new Msg("Get available space", newOrder);
		ClientUI.chat.accept(unplannedVisitMsg);
		availablePlace = Client.available;
		System.out.println("w");
		System.out.println(availablePlace);
		currenVisitorsTxt.setText("You can enter " + availablePlace + " visitors");
	}

	/**
	 * this method is happened only after a casual visit order approved and will
	 * enter it to the park
	 * 
	 * @param event click on go have fun button
	 * @throws IOException
	 */
	@FXML
	void enterPark(ActionEvent event) throws IOException {
		String toSend[] = { newOrder.getOrderID() + "", newOrder.getParkName() };
		Msg enterPark = new Msg("Enter park", toSend);
		ClientUI.chat.accept(enterPark);
		if (Client.order != null) {
			((Node) event.getSource()).getScene().getWindow().hide();
			FXMLLoader loader2 = new FXMLLoader();
			Parent root2 = loader2.load(getClass().getResource("/gui/Entrance.fxml").openStream());
			ParkEntrance unplannedVisit = loader2.getController();
			unplannedVisit.loadEmployee(employee);
			unplannedVisit.start(root2);
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Invoice.fxml").openStream());
			Invoice invoice = loader.getController();
			invoice.loadOrder(newOrder);
			invoice.start(root);
		} else {
			loadErrorMsg("Already entered the park");
		}
	}

	/**
	 * this method will try to set casual visit order if possible
	 * 
	 * @param event click on submit button
	 */
	@FXML
	void submitOrder(ActionEvent event) {
		orderIdTxt.setVisible(false);
		int visitors = 0;
		boolean error = false;
		if (!InputController.notNull(visitorsTxt.getText())) {
			loadErrorMsg("Enter number of visitors");
			error = true;
		} else {
			visitors = Integer.parseInt(visitorsTxt.getText());
			if (visitors < 1 || visitors > 15) {
				loadErrorMsg("Number of visitors need to be between 1-15");
				error = true;
			}
		}
		if (InputController.notNull(subscriptionTxt.getText()) && InputController.notNull(guideIdTxt.getText())) {
			loadErrorMsg("Choose one of the two, or none");
			error = true;
		}

		if (!error) {

			newOrder = new Order(Integer.parseInt(visitorsTxt.getText()));
			newOrder.setParkName(employee.getParkName());
			newOrder.setDate(java.time.LocalDate.now().toString());
			newOrder.setHour(Integer.parseInt(java.time.LocalTime.now().toString().substring(0, 2)));
			if (InputController.notNull(subscriptionTxt.getText()))
				newOrder.setSubNum(subscriptionTxt.getText());
			if (InputController.notNull(guideIdTxt.getText()))
				newOrder.setVisitorID(guideIdTxt.getText());
			else
				newOrder.setVisitorID("0");
			Msg orderMsg = new Msg("Unplanned visit", newOrder);
			ClientUI.chat.accept(orderMsg);
			newOrder = (Order) Client.orderMsg.content;
			if (newOrder == null)
				loadErrorMsg(Client.orderMsg.action);
			else {
				orderIdTxt.setVisible(true);
				orderIdTxt.setText("Your order id is: " + newOrder.getOrderID());
				enterVisitorBtn.setVisible(true);
			}
		}

	}

	/**
	 * this method will show pop up of error message
	 * 
	 * @param errMsg message to print in error
	 */
	private void loadErrorMsg(String errMsg) {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(errMsg);
			error.start(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this method show the current visitors in the park
	 * 
	 * @param event
	 */
	@FXML
	void currentVisitors(ActionEvent event) {
		Msg capacityMsg = new Msg("Check capacity", employee.getParkName());
		ClientUI.chat.accept(capacityMsg);
		capacityTxt.setText("There are " + Client.capacity + " visitors currently at the park");
	}

	/**
	 * this method will load the card reader screen
	 * 
	 * @param event click on card reader button
	 * @throws IOException
	 */
	@FXML
	void cardReader(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/cardReader/CardReader.fxml").openStream());
		CardReader cardReader = loader.getController();
		cardReader.loadPark(employee.getParkName());
		cardReader.start(root);
	}
}
