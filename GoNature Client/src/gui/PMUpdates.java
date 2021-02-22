package gui;

import java.io.IOException;

import assistance.AfterCurrentDate;
import assistance.OnlyNumbers;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import logic.Employee;
import logic.Msg;
import logic.Park;

/**
 * this class is a page for park manager updates
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class PMUpdates {
	/**
	 * current capacity label
	 */
	@FXML
	private Text capacityLbl;
	/**
	 * new capacity text
	 */
	@FXML
	private TextField capacityTxt;
	/**
	 * new discount text
	 */
	@FXML
	private TextField discountTxt;
	/**
	 * current visit time text
	 */
	@FXML
	private Text visitTimeLbl;
	/**
	 * park name text
	 */
	@FXML
	private Text parkTxt;
	/**
	 * new visit time text
	 */
	@FXML
	private TextField visitTImeTxt;
	/**
	 * start date for discount
	 */
	@FXML
	private DatePicker startDate;
	/**
	 * end date for discount
	 */
	@FXML
	private DatePicker endDate;
	/**
	 * current pre order text
	 */
	@FXML
	private Text preOrderLbl;
	/**
	 * new pre order text
	 */
	@FXML
	private TextField preOrderTxt;
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
	 * text for discounts that was approved
	 */
	@FXML
	private TextArea approvedDiscount;

	@FXML
	private ImageView returnImg;
	/**
	 * button for send update of pre order
	 */
	@FXML
	private Button sendPreOrder;
	/**
	 * button for send update of average visit time
	 */
	@FXML
	private Button sendAvg;
	/**
	 * button for send update of discount
	 */
	@FXML
	private Button sendDiscount;
	/**
	 * button for send update of capacity
	 */
	@FXML
	private Button sendCapacity;
	/**
	 * save employee details
	 */
	private Employee employee;
	/**
	 * save park details
	 */
	private Park park;
	/**
	 * update - array with update details,discount- for discount details
	 */
	private String[] update = new String[3], discount = new String[4];
	/**
	 * error message
	 */
	private String errMsg = "";

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
	 * this method load previous page - PManage
	 *
	 * @param event pressing on go back button
	 * @throws IOException
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/PManager.fxml").openStream());
		PManagerPage pManagerPage = loader.getController();
		pManagerPage.loadEmployee(employee);
		((Node) event.getSource()).getScene().getWindow().hide();
		pManagerPage.start(root);
	}
/**
 * this method will load error pop up
 * @param errMsg	error message to set in
 * @throws IOException
 */
	void setError(String errMsg) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
		Errors error = loader.getController();
		error.setMsg(errMsg);
		error.start(root);

	}

/**
 * this method send the update to depertment manage
 * @param toMSg 	- update name 
 * @throws IOException
 */
	void sendChange(String toMSg) throws IOException {
		Msg updateMsg;
		if (toMSg.equals("discount"))
			updateMsg = new Msg("Park updates", discount);
		else
			updateMsg = new Msg("Park updates", update);
		ClientUI.chat.accept(updateMsg);
		boolean updateSuccess = Client.updates;
		if (updateSuccess) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Your requsted to update was submitted.");
			alert.setContentText(toMSg + " update was sent to approval.");
			alert.showAndWait();
		} else if (!toMSg.equals("discount"))
			setError("There is already a " + toMSg + " update request in proccess.");
		else
			setError("There is an overlap in discount dates.");
	}

/**
 * update average visit time
 * @param event		click on send near average visit time
 * @throws IOException
 */
	@FXML
	void updateAvg(ActionEvent event) throws IOException {
		errMsg = "valid";
		if ((visitTImeTxt.getText() + " ").equals(" "))
			errMsg = "You must fill in valid average visit time";
		else if (Integer.parseInt(visitTImeTxt.getText()) == 0)
			errMsg = "You must fill in valid average visit time";
		else if (Integer.parseInt(visitTImeTxt.getText()) == park.getAverageVisitTime())
			errMsg = "The average visit time is already " + park.getAverageVisitTime();
		if (!errMsg.equals("valid"))
			setError(errMsg);
		else { // we need to send it to update
			update[0] = park.getParkName();
			update[1] = "averageVisitTime";
			update[2] = visitTImeTxt.getText();
			sendChange("average visit time");
		}
	}
	/**
	 * update capacity
	 * @param event		click on send near edit max capcaity
	 * @throws IOException
	 */
	@FXML
	void updateCapacity(ActionEvent event) throws IOException {
		errMsg = "valid";
		if ((capacityTxt.getText() + " ").equals(" "))
			errMsg = "You must fill in valid max capacity";
		else if (Integer.parseInt(capacityTxt.getText()) == 0)
			errMsg = "You must fill in valid max capacity";
		else if (Integer.parseInt(capacityTxt.getText()) == park.getMaxCapacity())
			errMsg = "The max capacity is already " + park.getMaxCapacity();
		if (!errMsg.equals("valid"))
			setError(errMsg);
		else { // we need to send it to update
			update[0] = park.getParkName();
			update[1] = "maxCapacity";
			update[2] = capacityTxt.getText();
			sendChange("max capacity");
		}
	}
	/**
	 * update discount
	 * @param event		click on send near edit discount
	 * @throws IOException
	 */
	@FXML
	void updateDiscount(ActionEvent event) throws IOException {
		errMsg = "valid";
		if ((discountTxt.getText() + " ").equals(" ") || startDate.getValue() == null || endDate.getValue() == null)
			errMsg = "You need to fill in discount, start date and end date.";
		else if (Integer.parseInt(discountTxt.getText()) == 0)
			errMsg = "Dont worry, 0% discount is the deafult";
		else if (Integer.parseInt(discountTxt.getText()) > 100)
			errMsg = "Ilegal discount percent";
		else if (checkDates() > 0) // end date comes before start date
			errMsg = "end date have to be after the start date.";
		if (!errMsg.equals("valid"))
			setError(errMsg);
		else { // we need to send it to update
			discount[0] = park.getParkName();
			discount[1] = discountTxt.getText();
			discount[2] = startDate.getValue().toString();
			discount[3] = endDate.getValue().toString();
			sendChange("discount");
		}
	}
	/**
	 * update max pre order 
	 * @param event		click on send near edit max pre order
	 * @throws IOException
	 */
	@FXML
	void updatePreOrder(ActionEvent event) throws IOException {
		errMsg = "valid";
		if ((preOrderTxt.getText() + " ").equals(" "))
			errMsg = "You must fill in valid pre order";
		else if (Integer.parseInt(preOrderTxt.getText()) == 0)
			errMsg = "You must fill in valid pre order";
		else if (Integer.parseInt(preOrderTxt.getText()) == park.getMaxPreOrder())
			errMsg = "The pre order is already " + park.getMaxPreOrder();
		if (!errMsg.equals("valid"))
			setError(errMsg);
		else { // we need to send it to update
			update[0] = park.getParkName();
			update[1] = "maxPreOrder";
			update[2] = preOrderTxt.getText();
			sendChange("max pre order");
		}
	}
/**
 * chek that start date is before end date
 * @return >0 is error , <= 0 is valid
 */
	private int checkDates() {
		return startDate.getValue().compareTo(endDate.getValue());
	}
/**
 * this method load park and employee details
 * @param employee	employee details
 * @param park		park details
 */
	public void loadEmployee(Employee employee, Park park) {
		this.employee = employee;
		this.park = park;
		parkTxt.setText("Park: " + employee.getParkName());
		capacityLbl.setText("" + park.getMaxCapacity());
		preOrderLbl.setText("" + park.getMaxPreOrder());
		visitTimeLbl.setText("" + park.getAverageVisitTime());
		approvedDiscount.setText(park.stDiscounts());
	}
	/**
	 * start presenting this page
	 * will also set only numbers for texts, and dates after the current date
	 * @param root parent to set in scene
	 * @throws IOException
	 */
	public void start(Parent root) throws IOException {
		capacityTxt.textProperty().addListener(new OnlyNumbers(capacityTxt));
		preOrderTxt.textProperty().addListener(new OnlyNumbers(preOrderTxt));
		visitTImeTxt.textProperty().addListener(new OnlyNumbers(visitTImeTxt));
		discountTxt.textProperty().addListener(new OnlyNumbers(discountTxt));
		// define input dates for order
		Callback<DatePicker, DateCell> callB = new AfterCurrentDate();
		startDate.setDayCellFactory(callB);
		endDate.setDayCellFactory(callB);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Park updates");
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

}
