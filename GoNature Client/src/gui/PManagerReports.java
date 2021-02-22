package gui;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;

/**
 * this class is for park manager reports page
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class PManagerReports {
	/**
	 * button to generate report
	 */
	@FXML
	private Button generateBtn;
	/**
	 * park name text
	 */
	@FXML
	private Text parkNameTxt;
	/**
	 * text for month
	 */
	@FXML
	private TextField Mtxt;
	/**
	 * text for year
	 */
	@FXML
	private TextField Ytxt;
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
	 * return image
	 */
	@FXML
	private ImageView returnImg;
	/**
	 * radio button for visitors report
	 */
	@FXML
	private RadioButton visitorsBtn;
	/**
	 * radio button for capacity report
	 */
	@FXML
	private RadioButton capacityBtn;
	/**
	 * radio button for incomes report
	 */
	@FXML
	private RadioButton incomBtn;
	/**
	 * save employee details
	 */
	private Employee pManager;
	/**
	 * toggle group to union radio buttons
	 */
	private ToggleGroup group;
	/**
	 * message to sent for server with info about report
	 */
	private Msg reportMsg = null;
	/**
	 * error message string
	 */
	private String errMsg = "";
	/**
	 * ArrayList for capacity report
	 */
	ArrayList<String> capacity = null;

	/**
	 * this method will sent for each method of each report that is requested
	 * 
	 * @param event click on generate
	 * @throws IOException
	 */
	@FXML
	void generate(ActionEvent event) throws IOException {
		String[] al = new String[3];
		al[0] = (pManager.getParkName());
		al[1] = (Mtxt.getText());
		if (al[1].length() == 1)
			al[1] = "0" + Mtxt.getText();
		al[2] = (Ytxt.getText());
		if (!checkInput())
			return;
		errMsg = ""; // if there is an error - errMsg edit and sent to load error.
		int check = Integer.parseInt(al[1]);
		if (check > 12 || check < 1)
			errMsg = "enter valid month";

		if (incomBtn.isSelected()) // income report
			incomeReport(al);
		else if (visitorsBtn.isSelected()) // visitor report
			visitorReport(al);
		else if (capacityBtn.isSelected()) // capacity report
			capacityReport(al);
		else
			errMsg = "You need to choose a report";
		if (!errMsg.equals(""))
			loadError(errMsg);

	}

	/**
	 * this method will pass a message to server about income report and then will
	 * show it if possible
	 * 
	 * @param al array with month year and park name
	 */
	private void incomeReport(String[] al) {
		reportMsg = new Msg("Income report", al);
		ClientUI.chat.accept(reportMsg);
		float incomes[] = Client.incomes;
		if (incomes[0] == 0 && incomes[1] == 0 && incomes[2] == 0) {
			errMsg = "No data found!";
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadIncomeReport(al, incomes);
			reportL.start(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method check valid input
	 * 
	 * @return
	 */
	private boolean checkInput() {
		if (Mtxt.getText().equals("") || Ytxt.getText().equals("")) {
			loadError("Enter date and year");
			return false;
		}
		int year = Integer.parseInt(Ytxt.getText());
		int thisYear = java.time.LocalDate.now().getYear();
		int month = Integer.parseInt(Mtxt.getText());
		int thisMonth = java.time.LocalDate.now().getMonthValue();
		if (year > thisYear) {
			loadError("You can't predict the future :)");
			return false;
		}
		if (year == thisYear)
			if (month == thisMonth) {
				loadError("This month is not over yet");
				return false;
			} else if (month > thisMonth) {
				loadError("You can't predict the future :)");
				return false;
			}
		return true;
	}

	/**
	 * this method will pass a message to server about visitors report and then will
	 * show it if possible
	 * 
	 * @param al array with month year and park name
	 */
	private void visitorReport(String[] al) {
		int info[] = null;
		reportMsg = new Msg("Visitors report", al);
		ClientUI.chat.accept(reportMsg);
		info = Client.infoReport;
		if (info[0] == 0 && info[1] == 0 && info[2] == 0) {
			errMsg = "No data found!";
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadVisitorReport(al, reportMsg.action, info);
			reportL.start(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method will pass a message to server about capacity report and then will
	 * show it if possible
	 * 
	 * @param al array with month year and park name
	 */
	private void capacityReport(String[] al) throws IOException {
		reportMsg = new Msg("Capacity report", al);
		ClientUI.chat.accept(reportMsg);
		capacity = Client.capacityReport;
		if (capacity.isEmpty())
			loadError("There are no orders in this date");
		else {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadCapacityReport(al,capacity);
			reportL.start(root);
		}
	}

	/**
	 * this method will show pop up of error with requested message
	 * 
	 * @param errMsg message to show
	 */
	private void loadError(String errMsg) {
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
	 * load employee details and set toggle group and date limits
	 * 
	 * @param employee employee details
	 */
	public void loadDetails(Employee employee) {
		pManager = employee;
		parkNameTxt.setText("Park: " + pManager.getParkName());
		group = new ToggleGroup();
		incomBtn.setToggleGroup(group);
		capacityBtn.setToggleGroup(group);
		visitorsBtn.setToggleGroup(group);
		Mtxt.textProperty().addListener(new OnlyNumbers(Mtxt));
		Ytxt.textProperty().addListener(new OnlyNumbers(Ytxt));
		int month = java.time.LocalDate.now().getMonthValue();
		month = month == 1 ? 12 : month - 1;
		Mtxt.setText(month + "");
		int year = java.time.LocalDate.now().getYear();
		year = month == 12 ? year - 1 : year;
		Ytxt.setText(year + "");
	}

	/**
	 * start presenting this page
	 * 
	 * @param root parent to set in scene
	 * @throws IOException
	 */
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("PManager reports");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				ClientUI.chat.accept(new Msg("Log out", pManager));
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	/**
	 * go to previous page - PManger page
	 * 
	 * @param event click on go back button
	 */
	@FXML
	void goBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/PManager.fxml").openStream());
			PManagerPage pManagerPage = loader.getController();
			pManagerPage.loadEmployee(pManager);
			pManagerPage.start(root);
		} catch (Exception e) {
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
}