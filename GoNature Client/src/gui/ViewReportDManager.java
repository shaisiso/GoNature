package gui;

import java.io.IOException;
import java.util.ArrayList;

import assistance.IEmployee;
import assistance.OnlyNumbers;
import client.Client;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;
import logic.Park;

/**
 * this class is to show gui of department manager to view reports of park
 * manager
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ViewReportDManager implements IEmployee {
	/**
	 * info button
	 */
	@FXML
	private Button infoBtn;
	/**
	 * about button
	 */
	@FXML
	private Button aboutBtn;
	/**
	 * return button
	 */
	@FXML
	private Button returnBtn;
	/**
	 * return image
	 */
	@FXML
	private ImageView returnImg;
	/**
	 * cBox with park names
	 */
	@FXML
	private ComboBox<String> cBoxPark;
	/**
	 * button to view report
	 */
	@FXML
	private Button viewBtn;
	/**
	 * month text
	 */
	@FXML
	private TextField Mtxt;
	/**
	 * year text
	 */
	@FXML
	private TextField Ytxt;
	/**
	 * text area to show the reports of the last month
	 */
	@FXML
	private TextArea txt;
	/**
	 * title text
	 */
	@FXML
	private Text txtTitle;
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
	 * toggle group for radio buttons
	 */
	private ToggleGroup group;
	/**
	 * list for cBox
	 */
	ObservableList<String> list;
	/**
	 * employee details
	 */
	private Employee dManager;
	/**
	 * month and year to choose
	 */
	private int month = 0, year = 0;
	/**
	 * message to send for server with requested report
	 */
	private Msg reportMsg = null;
	/**
	 * to know if the report exist
	 */
	private boolean reportExist;
	/**
	 * ArrayList of capacity report data
	 */
	ArrayList<String> capacity = null;

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
	 * this method will load the previous page
	 * 
	 * @param event click on go back
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
	 * view report
	 * 
	 * @param event click on view
	 * @throws IOException
	 */
	@FXML
	void view(ActionEvent event) throws IOException {
		String[] st = { cBoxPark.getValue(), Mtxt.getText(), Ytxt.getText() };
		if (visitorsBtn.isSelected())
			visitorReport(st);
		if (capacityBtn.isSelected())
			capacityReport(st);
		if (incomBtn.isSelected())
			incomeReport(st);

		// add input checking--------------
	}

	private void incomeReport(String[] st) throws IOException {
		String[] exist = { st[0], "Income report", st[1], st[2] };
		reportMsg = new Msg("Report exist", exist);
		ClientUI.chat.accept(reportMsg);
		reportExist = Client.reportExist;
		if (reportExist) { // The report exist in the DB (the park manager generated the report)
			float[] info = null;
			reportMsg = new Msg("Income report", st);
			ClientUI.chat.accept(reportMsg);
			info = Client.incomes;
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadIncomeReport(st, info);
			reportL.start(root);
		} else
			loadError("the park manager didn't generated the visitor report of " + st[1] + "/" + st[2]);		
	}

	/**
	 * load employee details, set toggle group for radio buttons and set month and
	 * year to be only numbers
	 */
	@Override
	public void loadEmployee(Employee employee) {
		this.dManager = employee;
		group = new ToggleGroup();
		incomBtn.setToggleGroup(group);
		capacityBtn.setToggleGroup(group);
		visitorsBtn.setToggleGroup(group);
		Mtxt.textProperty().addListener(new OnlyNumbers(Mtxt));
		Ytxt.textProperty().addListener(new OnlyNumbers(Ytxt));
		month = java.time.LocalDate.now().getMonthValue();
		month = month == 1 ? 12 : month - 1;
		Mtxt.setText(month + "");
		year = java.time.LocalDate.now().getYear();
		year = month == 12 ? year - 1 : year;
		Ytxt.setText(year + "");
		setParkName();
	}

	/**
	 * set park cBox values
	 */
	void setParkName() {
		Msg parksMsg=new Msg("Get all parks",null);
		ClientUI.chat.accept(parksMsg);
		ArrayList<Park> allParks= Client.allParks;
		ArrayList<String> al = new ArrayList<String>();	
		for (Park p : allParks)
			al.add(p.getParkName());
		list = FXCollections.observableArrayList(al);
		cBoxPark.setItems(list);
	}

	/**
	 * start presenting this page
	 * 
	 * @param root parent to set in scene
	 * @throws IOException
	 */
	@Override
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("DManager - reports");
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
	 * show the existing reports
	 * 
	 * @param event change park in cBox
	 */
	@FXML
	void showReports(ActionEvent event) {
		txt.setVisible(true);
		txtTitle.setVisible(true);
		String[] st = { cBoxPark.getValue(), String.valueOf(month), String.valueOf(year) };
		Msg reportMsg = new Msg("Show exsisting reports", st);
		ClientUI.chat.accept(reportMsg);
		String existingReports = Client.existingReports;
		txtTitle.setText(st[1] + "/" + st[2] + " existing reports of\n" + st[0] + " park:");
		txt.setText(existingReports);
	}

	/**
	 * show visitors report
	 * 
	 * @param st park name month and year
	 * @throws IOException
	 */
	private void visitorReport(String[] st) throws IOException {
		String[] exist = { st[0], "Visitors report", st[1], st[2] };
		reportMsg = new Msg("Report exist", exist);
		ClientUI.chat.accept(reportMsg);
		reportExist = Client.reportExist;
		if (reportExist) { // The report exist in the DB (the park manager generated the report)
			int info[] = null;
			reportMsg = new Msg("Visitors report", st);
			ClientUI.chat.accept(reportMsg);
			info = Client.infoReport;
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadVisitorReport(st, reportMsg.action, info);
			reportL.start(root);
		} else
			loadError("the park manager didn't generated the visitor report of " + st[1] + "/" + st[2]);
	}

	/**
	 * show capacity report
	 * 
	 * @param st park name month and year
	 * @throws IOException
	 */
	private void capacityReport(String[] st) throws IOException {
		String[] exist = { st[0], "Capacity report", st[1], st[2] };
		reportMsg = new Msg("Report exist", exist);
		ClientUI.chat.accept(reportMsg);
		reportExist = Client.reportExist;
		if (reportExist) { // The report exist in the DB (the park manager generated the report)
			reportMsg = new Msg("Capacity report", st);
			ClientUI.chat.accept(reportMsg);
			capacity = Client.capacityReport;
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadCapacityReport(st, capacity);
			reportL.start(root);
		} else
			loadError("the park manager didn't generated the capacity report of " + st[1] + "/" + st[2]);
	}

	/**
	 * load pop up of error with the requested message
	 * 
	 * @param errMsg message for pop up
	 * @throws IOException
	 */
	private void loadError(String errMsg) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
		Errors error = loader.getController();
		error.setMsg(errMsg);
		error.start(root);
	}

}