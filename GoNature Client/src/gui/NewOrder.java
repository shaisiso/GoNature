package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import assistance.AfterCurrentDate;
import assistance.InputController;
import assistance.OnlyNumbers;
import client.Client;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Guide;
import logic.Msg;
import logic.Order;
import logic.Park;
import logic.Subscriber;
import logic.Visitor;

/**
 * this is a class for new order page
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 *
 */
public class NewOrder implements Initializable {
	/**
	 * button to submit order details
	 */
	@FXML
	private Button goBtn;
	/**
	 * cBox of parks
	 */
	@FXML
	private ComboBox<String> parkCbox;
	/**
	 * for selecting order date
	 */
	@FXML
	private DatePicker dateSelectorBtn;
	/**
	 * cBox of hours which the parks are open
	 */
	@FXML
	private ComboBox<String> hourCbox;
	/**
	 * label "number of visitors"
	 */
	@FXML
	private Text visitorLbl;
	/**
	 * text for the number of visitors
	 */
	@FXML
	private TextField visitorsTxt;
	/**
	 * text for the email
	 */
	@FXML
	private TextField emailTxt;
	/**
	 * check box for organized group(only for guides)
	 */
	@FXML
	private CheckBox groupCheck;
	/**
	 * button to show alternative dates
	 */
	@FXML
	private Button alterDatesBtn;
	/**
	 * button to show waiting list
	 */
	@FXML
	private Button waitingListBtn;
	/**
	 * button to show info page
	 */
	@FXML
	private Button infoBtn;
	/**
	 * button to show about page
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
	 * list1- park cBox , list2- hours cBox
	 */
	ObservableList<String> list1, list2;
	/**
	 * for save order details
	 */
	private Visitor visitor;
	/**
	 * save order details
	 */
	private Order order = null;

	/**
	 * this method will show alternative dates
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void alternativeDates(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		Msg altDatesMsg = new Msg("Alternative dates", order);
		ClientUI.chat.accept(altDatesMsg);
		Map<String, ArrayList<Integer>> dates = Client.dates;
		if (dates.isEmpty()) {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(
					"There are not alternative dates for the next 7 days for your requested park and number of visitors");
			error.start(root);
		} else {
			root = loader.load(getClass().getResource("/gui/AlternativeDates.fxml").openStream());
			AlternativeDates alternativeDates = loader.getController();
			alternativeDates.loadDates(dates, order, visitor);
			((Node) event.getSource()).getScene().getWindow().hide();
			alternativeDates.start(root);
		}
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
	 * this method will add order to waiting list
	 * 
	 * @param event click on join waiting list button
	 * @throws IOException
	 */
	@FXML
	void joinWaitingList(ActionEvent event) throws IOException {
		Msg waitingListMsg = new Msg("Join waiting list", order);
		ClientUI.chat.accept(waitingListMsg);
		if (Client.addedWaitingList) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Joined to waiting list");
			alert.setHeaderText("You have joined to the waiting list");
			alert.setContentText("For " + order.getParkName() + " park, on " + order.getDate() + " at "
					+ order.getHour() + ":00\nAn email will be sent to you if the order can be placed");
			alert.showAndWait();
		} else {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg("You are already in the waiting list for the requested time");
			error.start(root);
		}

		// order = null;

	}

	/**
	 * this method will add new order
	 * 
	 * @param event click on go button
	 * @throws Exception
	 */
	@FXML
	void makeOrder(ActionEvent event) throws Exception {
		if (checkInput()) {
			alterDatesBtn.setVisible(false);
			waitingListBtn.setVisible(false);
			LocalDate date = dateSelectorBtn.getValue();
			FXMLLoader loader = new FXMLLoader();
			int hour = Integer.parseInt((hourCbox.getValue()).substring(0, 2));
			order = new Order(visitor.getId(), parkCbox.getValue(), date.toString(), hour,
					Integer.parseInt(visitorsTxt.getText()), emailTxt.getText(), groupCheck.isSelected());
			Msg orderMsg = new Msg("New Order", order);
			ClientUI.chat.accept(orderMsg);
			if (Client.order == null) {
				Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
				Errors error = loader.getController();
				error.setMsg(Client.reason);
				error.start(root);
				if (Client.reason.equals("Unavailable date and hour for the requested park")) {
					alterDatesBtn.setVisible(true);
					waitingListBtn.setVisible(true);
				}
			} else {
				Parent root = loader.load(getClass().getResource("/gui/OrderApproved.fxml").openStream());
				OrderApproved orderApproved = loader.getController();
				orderApproved.loadDetails(Client.order, visitor);
				((Node) event.getSource()).getScene().getWindow().hide();
				orderApproved.start(root);
			}
		}
	}

	/**
	 * check the input that was entered
	 * 
	 * @return true-valid input,false otherwise
	 * @throws IOException
	 */
	private boolean checkInput() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (InputController.notNull(parkCbox.getValue(), "a park") // check park
				&& InputController.notNull(dateSelectorBtn.getValue(), "a date") // check date
				&& InputController.notNull(hourCbox.getValue(), "a hour") // check hour
				&& InputController.isValidVisitorsAmount(visitorsTxt.getText(), groupCheck.isSelected()) // check number
																											// of
																											// visitors
				&& InputController.isValidEmail(emailTxt.getText())) // check email
			return true;
		else {
			Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(InputController.errMsg);
			error.start(root);
			return false;
		}
	}

	/**
	 * this method will load this page page
	 * 
	 * @param root Parent to set in scene
	 */
	public void start(Parent root) {
		// define input dates for order
		Callback<DatePicker, DateCell> callB = new AfterCurrentDate();
		dateSelectorBtn.setDayCellFactory(callB);
		// force the field to be numeric only
		visitorsTxt.textProperty().addListener(new OnlyNumbers(visitorsTxt));
		visitorsTxt.setText("1");
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("New Order");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * this method is for loading visitor details
	 * 
	 * @param visitor visitor details from the previous page
	 */
	public void loadVisitor(Visitor visitor) {
		this.visitor = visitor;
		if ((visitor instanceof Subscriber) || (visitor instanceof Guide)) {
			emailTxt.setText(visitor.getEmail());
			if (visitor instanceof Subscriber) {
				Subscriber sub = (Subscriber) visitor;
				visitorsTxt.setText("" + sub.getFamilyMembers());
			} else
				groupCheck.setVisible(true);
		}

	}

	/**
	 * set cBox park values
	 */
	void setParkName() {
		Msg parksMsg=new Msg("Get all parks",null);
		ClientUI.chat.accept(parksMsg);
		ArrayList<Park> allParks= Client.allParks;
		ArrayList<String> al = new ArrayList<String>();	
		for (Park p : allParks)
			al.add(p.getParkName());		
		list1 = FXCollections.observableArrayList(al);
		parkCbox.setItems(list1);
	}

	/**
	 * set cBox hour values
	 */
	void setHour() {
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 8; i < 10; i++)
			al.add("0" + i + ":00");
		for (int i = 10; i < 18; i++)
			al.add(i + ":00");
		list2 = FXCollections.observableArrayList(al);
		hourCbox.setItems(list2);
	}

	/**
	 * initial cBox
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setParkName();
		setHour();
	}

	/**
	 * this method load previous page - VisitorPage
	 *
	 * @param event pressing on back button
	 * @throws IOException
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException {
		boolean flag = false;
		Optional<ButtonType> result = null;
		if (order != null) {
			flag = true;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("You are about to cancel the order?");
			alert.setContentText("Are you sure?");
			result = alert.showAndWait();
		}
		if (flag == false || result.get() == ButtonType.OK) {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/VisitorPage.fxml").openStream());
			VisitorPage visitorPage = loader.getController();
			visitorPage.loadVisitor(visitor);
			((Node) event.getSource()).getScene().getWindow().hide();
			visitorPage.start(root);
		}
	}

	/**
	 * this method load visitor and order to the class variables (for saving order
	 * details after going to alternative dates page)
	 * 
	 * @param visitor visitor details
	 * @param order   order details
	 */

	public void loadVisitorAndOrder(Visitor visitor, Order order) {
		this.visitor = visitor;
		this.order = order;
		emailTxt.setText(order.getEmail());
		parkCbox.setValue(order.getParkName());
		String hour = order.getHour() < 10 ? "0" : "";
		hour += order.getHour() + ":00";
		hourCbox.setValue(hour);
		visitorsTxt.setText(order.getNumberOfVisitors() + "");
	}
}
