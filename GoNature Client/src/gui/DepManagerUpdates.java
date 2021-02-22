package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import assistance.IEmployee;
import client.Client;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;
import logic.Park;

/**
 * this is a page of department manager show the updates that were sent from
 * different parks and choose to approve or not
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */

public class DepManagerUpdates implements IEmployee, Initializable {
	/**
	 * combox for choosing park
	 */
	@FXML
	private ComboBox<String> cBox;
	/**
	 * button to approve max capacity
	 */
	@FXML
	private Button maxCapacityYes;
	/**
	 * button to decline max capacity
	 */
	@FXML
	private Button maxCapacityNo;
	/**
	 * label for current max capacity text
	 */
	@FXML
	private Text CurrentMaxCapacityLbl1;
	/**
	 * text that show the current max capacity
	 */
	@FXML
	private Text CurrentMaxCapacityLbl2;
	/**
	 * label for current average time text
	 */
	@FXML
	private Text CurrentAvgLbl1;
	/**
	 * text that show the current average time
	 */
	@FXML
	private Text CurrentAvgLbl2;
	/**
	 * label for current max pre-order text
	 */
	@FXML
	private Text CurrentMaxPreOrderLbl1;
	/**
	 * text that show the current pre-order
	 */
	@FXML
	private Text CurrentMaxPreOrderLbl2;
	/**
	 * label of new max capacity
	 */
	@FXML
	private Text NewMaxCapacityLbl;
	/**
	 * text of new max capacity
	 */
	@FXML
	private Text NewMaxCapacityTxt;
	/**
	 * label of new average time
	 */
	@FXML
	private Text NewAvgLbl;
	/**
	 * text of new average time
	 */
	@FXML
	private Text NewAvgTxt;
	/**
	 * label of new pre-order
	 */
	@FXML
	private Text NewMaxPreOrderLbl;
	/**
	 * text of new pre-order
	 */
	@FXML
	private Text NewMaxPreOrderTxt;
	/**
	 * button for approve pre-order
	 */
	@FXML
	private Button maxPreOrderYes;
	/**
	 * button for decline pre-order
	 */
	@FXML
	private Button maxPreOrderNo;
	/**
	 * button for approve average visit time
	 */
	@FXML
	private Button avgYes;
	/**
	 * button for decilne average visit time
	 */
	@FXML
	private Button avgNo;
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
	 * label for current average visit time
	 */
	@FXML
	private Text CurrentAvgLbl;
	/**
	 * label for current discounts
	 */
	@FXML
	private Text CurrentDiscountLbl;
	/**
	 * text area for current discounts presenting
	 */
	@FXML
	private TextArea approvedDiscount;
	/**
	 * label for new discount
	 */
	@FXML
	private Text newDisLbl;
	/**
	 * cBox to choose discount for presenting
	 */
	@FXML
	private ComboBox<String> cBoxDis;
	/**
	 * text to details of new discount
	 */
	@FXML
	private Text newDisDet;
	/**
	 * approve discount
	 */
	@FXML
	private Button disYes;
	/**
	 * decline discount
	 */
	@FXML
	private Button disNo;
	/**
	 * button to return
	 */
	@FXML
	private Button returnBtn;

	@FXML
	private ImageView returnImg;
	/**
	 * save employee details
	 */
	private Employee dManager;
	/**
	 * save park that was chosen in cBox details
	 */
	private Park park;
	/**
	 * lists for cBox
	 */
	ObservableList<String> list, list2;
	/**
	 * map that match park to the requested updates
	 */
	private Map<String, ArrayList<String>> requests;

	/**
	 * method to show this page
	 */
	@Override
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("DManager - updates");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				ClientUI.chat.accept(new Msg("Log out", dManager));
				Platform.exit();
				System.exit(0);
			}
		});
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
	 * show relevant updates and details after choosing a park from cBox
	 * 
	 * @param event choosing park from cBox
	 */
	@FXML
	void showUpdates(ActionEvent event) {
		// show to current details
		String parkName = cBox.getValue();
		Msg showUpdates = new Msg("Show updates", parkName);
		ClientUI.chat.accept(showUpdates);
		park = Client.park;
		otherCity();

		CurrentMaxCapacityLbl1.setVisible(true);
		CurrentMaxCapacityLbl2.setText(park.getMaxCapacity() + "");
		CurrentMaxCapacityLbl2.setVisible(true);

		CurrentMaxPreOrderLbl1.setVisible(true);
		CurrentMaxPreOrderLbl2.setText(park.getMaxPreOrder() + "");
		CurrentMaxPreOrderLbl2.setVisible(true);

		CurrentAvgLbl1.setVisible(true);
		CurrentAvgLbl2.setText(park.getAverageVisitTime() + "");
		CurrentAvgLbl2.setVisible(true);

		CurrentAvgLbl1.setVisible(true);
		CurrentAvgLbl2.setText(park.getAverageVisitTime() + "");
		CurrentAvgLbl2.setVisible(true);

		CurrentDiscountLbl.setVisible(true);
		approvedDiscount.setText(park.stDiscounts());
		approvedDiscount.setVisible(true);

		// show to update requests
		Msg showRequests = new Msg("Show requests", parkName);
		ClientUI.chat.accept(showRequests);
		requests = Client.requests;
		if (requests.get("maxCapacity") != null) {
			NewMaxCapacityLbl.setVisible(true);
			NewMaxCapacityTxt.setText(requests.get("maxCapacity").get(0));
			NewMaxCapacityTxt.setVisible(true);
			maxCapacityYes.setVisible(true);
			maxCapacityNo.setVisible(true);
		}
		if (requests.get("maxPreOrder") != null) {
			NewMaxPreOrderLbl.setVisible(true);
			NewMaxPreOrderTxt.setText(requests.get("maxPreOrder").get(0));
			NewMaxPreOrderTxt.setVisible(true);
			maxPreOrderYes.setVisible(true);
			maxPreOrderNo.setVisible(true);
		}
		if (requests.get("averageVisitTime") != null) {
			NewAvgLbl.setVisible(true);
			NewAvgTxt.setText(requests.get("averageVisitTime").get(0));
			NewAvgTxt.setVisible(true);
			avgYes.setVisible(true);
			avgNo.setVisible(true);
		}
		newDisDet.setVisible(false);
		disYes.setVisible(false);
		disNo.setVisible(false);
		if (!requests.get("discount").isEmpty())
			setDiscount();

	}

	/**
	 * set to cBox discounts to choose ðy serial number
	 */
	void setDiscount() {
		cBoxDis.setVisible(true);
		newDisLbl.setVisible(true);
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 1; i <= requests.get("discount").size(); i++) {
			al.add(i + "");
		}
		list2 = FXCollections.observableArrayList(al);
		cBoxDis.setItems(list2);
	}

	/**
	 * set parks to cBox
	 */
	void setParkName() {
		Msg parksMsg=new Msg("Get all parks",null);
		ClientUI.chat.accept(parksMsg);
		ArrayList<Park> allParks= Client.allParks;
		ArrayList<String> al = new ArrayList<String>();	
		for (Park p : allParks)
			al.add(p.getParkName());
		list = FXCollections.observableArrayList(al);
		cBox.setItems(list);
	}

	/**
	 * load details of employee from previous page
	 */
	@Override
	public void loadEmployee(Employee employee) {
		this.dManager = employee;
	}

	/**
	 * initial cBox
	 */
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		setParkName();
		otherCity();
	}

	/**
	 * reset visible of all to false. for upload new park
	 */
	private void otherCity() {
		NewMaxCapacityLbl.setVisible(false);
		NewMaxCapacityTxt.setVisible(false);
		maxCapacityYes.setVisible(false);
		maxCapacityNo.setVisible(false);
		NewMaxPreOrderLbl.setVisible(false);
		NewMaxPreOrderTxt.setVisible(false);
		maxPreOrderYes.setVisible(false);
		maxPreOrderNo.setVisible(false);
		NewAvgLbl.setVisible(false);
		NewAvgTxt.setVisible(false);
		avgYes.setVisible(false);
		avgNo.setVisible(false);
		NewAvgLbl.setVisible(false);
		NewAvgTxt.setVisible(false);
		avgYes.setVisible(false);
		avgNo.setVisible(false);
		CurrentDiscountLbl.setVisible(false);
		approvedDiscount.setVisible(false);
		newDisLbl.setVisible(false);
		cBoxDis.setVisible(false);
		newDisDet.setVisible(false);
		disYes.setVisible(false);
		disNo.setVisible(false);

	}

	/**
	 * show the discount in the page
	 * 
	 * @param event choosing a discount from cBox
	 */
	@FXML
	void showDiscount(ActionEvent event) {

		if (cBoxDis.getValue() != null) {
			int num = Integer.parseInt(cBoxDis.getValue());
			newDisDet.setVisible(true);
			newDisDet.setText(requests.get("discount").get(num - 1));
			disYes.setVisible(true);
			disNo.setVisible(true);
		}
	}

	/**
	 * this method load previous page - DManagerPage
	 *
	 * @param event pressing on back button
	 * @throws IOException
	 */

	@FXML
	void goBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		try {

			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/DManager.fxml").openStream());
			DManagerPage dManagerPage = loader.getController();
			dManagerPage.loadEmployee(dManager);
			dManagerPage.start(root);
		} catch (Exception e) {
		}
	}

	/**
	 * approving capacity update
	 * 
	 * @param event click yes near capacity update
	 */
	@FXML
	void approveCapacity(ActionEvent event) {
		if (Integer.parseInt(requests.get("maxCapacity").get(0)) < park.getMaxPreOrder())
			loadError("The max capcity number need to be more then pre order");
		else {
			String st = "maxCapacity " + requests.get("maxCapacity").get(0);
			Msg msg = new Msg("Approve update", st);
			actOfUpdate(msg);
			NewMaxCapacityLbl.setVisible(false);
			NewMaxCapacityTxt.setVisible(false);
			maxCapacityYes.setVisible(false);
			maxCapacityNo.setVisible(false);
		}
	}

	/**
	 * approving discount update
	 * 
	 * @param event click yes near discount update
	 */
	@FXML
	void approveDiscount(ActionEvent event) {
		String st = "discount " + requests.get("discount").get(Integer.parseInt(cBoxDis.getValue()) - 1);
		Msg msg = new Msg("Approve update", st);
		actOfUpdate(msg);
		setDiscount();
		showUpdates(null);
	}

	/**
	 * approving pre - order number update
	 * 
	 * @param event click yes near pre - order update
	 */
	@FXML
	void approvePreOrder(ActionEvent event) {
		if (Integer.parseInt(requests.get("maxPreOrder").get(0)) > park.getMaxCapacity())
			loadError("The pre order number need to be less then max capcity");
		else {
			String st = "maxPreOrder " + requests.get("maxPreOrder").get(0);
			Msg msg = new Msg("Approve update", st);
			actOfUpdate(msg);
			NewMaxPreOrderLbl.setVisible(false);
			NewMaxPreOrderTxt.setVisible(false);
			maxPreOrderYes.setVisible(false);
			maxPreOrderNo.setVisible(false);
		}
	}

	/**
	 * approving visit time update
	 * 
	 * @param event click yes near visit time update
	 */
	@FXML
	void approveVisitTime(ActionEvent event) {
		String st = "averageVisitTime " + requests.get("averageVisitTime").get(0);
		Msg msg = new Msg("Approve update", st);
		actOfUpdate(msg);
		NewAvgLbl.setVisible(false);
		NewAvgTxt.setVisible(false);
		avgYes.setVisible(false);
		avgNo.setVisible(false);
	}

	/**
	 * decline capacity update
	 * 
	 * @param event click no near capacity update
	 */
	@FXML
	void declineCapacity(ActionEvent event) {
		String st = "maxCapacity " + requests.get("maxCapacity").get(0);
		Msg msg = new Msg("Remove update", st);
		actOfUpdate(msg);
		NewMaxCapacityLbl.setVisible(false);
		NewMaxCapacityTxt.setVisible(false);
		maxCapacityYes.setVisible(false);
		maxCapacityNo.setVisible(false);
	}

	/**
	 * decline discount update
	 * 
	 * @param event click no near discount update
	 */
	@FXML
	void declineDiscount(ActionEvent event) {
		String st = "discount " + requests.get("discount").get(Integer.parseInt(cBoxDis.getValue()) - 1);
		Msg msg = new Msg("Remove update", st);
		actOfUpdate(msg);
		setDiscount();
		showUpdates(null);

	}

	/**
	 * decline pre order update
	 * 
	 * @param event click no near pre order update
	 */
	@FXML
	void declinePreOrder(ActionEvent event) {
		String st = "maxPreOrder " + requests.get("maxPreOrder").get(0);
		Msg msg = new Msg("Remove update", st);
		actOfUpdate(msg);
		NewMaxPreOrderLbl.setVisible(false);
		NewMaxPreOrderTxt.setVisible(false);
		maxPreOrderYes.setVisible(false);
		maxPreOrderNo.setVisible(false);
	}

	/**
	 * decline visit time update
	 * 
	 * @param event click no near visit time update
	 */
	@FXML
	void declineVisitTime(ActionEvent event) {
		String st = "averageVisitTime " + requests.get("averageVisitTime").get(0);
		Msg msg = new Msg("Remove update", st);
		actOfUpdate(msg);
		NewAvgLbl.setVisible(false);
		NewAvgTxt.setVisible(false);
		avgYes.setVisible(false);
		avgNo.setVisible(false);
	}

	/**
	 * this method send to the server the relevant update to approve/decline
	 * 
	 * @param msg message to send for the server
	 */
	private void actOfUpdate(Msg msg) {
		msg.content2 = cBox.getValue();
		ClientUI.chat.accept(msg);
		// refresh page
		String parkName = cBox.getValue();
		Msg showUpdates = new Msg("Show updates", parkName);
		ClientUI.chat.accept(showUpdates);
		park = Client.park;
		//
		CurrentMaxCapacityLbl2.setText("" + park.getMaxCapacity());
		CurrentAvgLbl2.setText("" + park.getAverageVisitTime());
		CurrentMaxPreOrderLbl2.setText("" + park.getMaxPreOrder());
		approvedDiscount.setText(park.stDiscounts());

	}

	/**
	 * this method get a string of error message and load pop up of error with that
	 * message
	 * 
	 * @param errMsg error to show
	 */
	void loadError(String errMsg) {
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
}
