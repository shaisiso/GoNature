package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import assistance.BeforeCurrentDate;
import assistance.ErrorController;
import assistance.InputController;
import assistance.VisitationReport;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import logic.Employee;
import logic.Msg;

/**
 * this is a class for the page of department manager making reports
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
public class DManagerMakeReport implements Initializable {
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
	 * cBox to choose report
	 */
	@FXML
	private ComboBox<String> reportCbox;
	/**
	 * for start date input
	 */
	@FXML
	private DatePicker startDate;
	/**
	 * for start end input
	 */
	@FXML
	private DatePicker endDate;
	/**
	 * label of date 1
	 */
	@FXML
	private Label dateLbl1;
	/**
	 * label of date 2
	 */
	@FXML
	private Label dateLbl2;
	/**
	 * button to generate report
	 */
	@FXML
	private Button generateBtn;
	/**
	 * cBox of start hour
	 */
	@FXML
	private ComboBox<String> startTime;
	/**
	 * cBox of end hour
	 */
	@FXML
	private ComboBox<String> endTime;
	/**
	 * label to hour from
	 */
	@FXML
	private Label fromHour;
	/**
	 * label to hour to
	 */
	@FXML
	private Label toHour;

	/**
	 * save employee details
	 */
	private Employee dManager;
	/**
	 * lists for cBox values
	 */
	private ObservableList<String> list1, list2, list3;
	private ErrorController err = new ErrorController();
	private VisitationReport visitationReport = new VisitationReport();

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
	 * this method will load the previous page - DManager page
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
	 * this method get the requested report and pass it to the method that do this
	 * 
	 * @param event click on generate button
	 * @throws IOException
	 */
	@FXML
	void makeReport(ActionEvent event) throws IOException {
		if (!InputController.notNull(reportCbox.getValue()))
			loadError("Choose report");
		else if (reportCbox.getValue().equals("Cancellation report")) {
			if (startDate.getValue() == null || endDate.getValue() == null)
				loadError("Choose start and end dates");
			else if ((startDate.getValue().compareTo(endDate.getValue())) > 0)
				loadError("Invalid date range");
			else
				cancellationReport();

		} else if (reportCbox.getValue().equals("Visitation report")) {
			if (startDate.getValue() == null || startTime.getValue() == null || endTime.getValue() == null)
				loadError("Choose date and hours");
			else
				visitationReport();
		}
	}

// for tests
	public void setValuesForReportTest(LocalDate date, String startHour, String endHour, ErrorController err,
			VisitationReport visitationReport) {
		this.err = err;
		startDate = new DatePicker(date);
		startTime = new ComboBox<String>();
		endTime = new ComboBox<String>();
		startTime.setValue(startHour);
		endTime.setValue(endHour);
		this.visitationReport=visitationReport;
	}

	/**
	 * this method send to the server the details for the visitation report and will
	 * load that report after
	 */
	public void visitationReport() {
		String st[] = { startDate.getValue().toString(), startTime.getValue(), endTime.getValue() };// {date,hourFrom,hourTo}
		Msg visitMsg = new Msg("Visitation report", st);
		ClientUI.chat.accept(visitMsg);
		float visitorsTime[][] = Client.visitationReport;
		int cnt = 3 * visitorsTime[0].length;
		// check empty data
		for (int i = 0; i < 3; i++)
			for (float x : visitorsTime[i]) {
				if (x == 0)
					cnt--;
					
				}
		if (cnt == 0) {
			err.loadError("There were no orders in the requested time");
		}
		// loadError("There were no orders in the requested time");
		else {
			visitationReport.loadVisitationReport(visitorsTime, st);
		}
	}

	/**
	 * 
	 * this method send to the server the details for the cancellation report and
	 * will load that report after
	 *
	 * @throws IOException
	 */
	private void cancellationReport() throws IOException {
		String st[] = { startDate.getValue().toString(), endDate.getValue().toString() };
		Msg cancelMsg = new Msg("Cancellation report", st);
		ClientUI.chat.accept(cancelMsg);
		int cancelled[] = Client.canceledReport;
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
		Report reportL = loader.getController();
		reportL.loadCancelledReport(cancelled, st[0], st[1]);
		reportL.start(root);
	}

	/**
	 * this method get a string of error message and load pop up of error with that
	 * message
	 * 
	 * @param errMsg error to show
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
	 * this method will show on the screen the things for input to the chosen report
	 * 
	 * @param event choose type of report in cBox
	 */
	@FXML
	void showReport(ActionEvent event) {
		generateBtn.setVisible(true);
		startDate.setVisible(true);
		dateLbl1.setVisible(true);
		if (reportCbox.getValue().equals("Cancellation report")) {
			endDate.setVisible(true);
			dateLbl2.setVisible(true);
			dateLbl1.setText("From");
			fromHour.setVisible(false);
			toHour.setVisible(false);
			endTime.setVisible(false);
			startTime.setVisible(false);

		}
		if (reportCbox.getValue().equals("Visitation report")) {

			dateLbl1.setText("Date");
			fromHour.setVisible(true);
			toHour.setVisible(true);
			endTime.setVisible(true);
			startTime.setVisible(true);
			endDate.setVisible(false);
			dateLbl2.setVisible(false);
		}

	}

	/**
	 * upload this page
	 * 
	 * @param root
	 * @throws IOException
	 */
	public void start(Parent root) throws IOException {
		Callback<DatePicker, DateCell> callB = new BeforeCurrentDate();
		startDate.setDayCellFactory(callB);
		endDate.setDayCellFactory(callB);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Make reports");
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
	 * load employee details from previous page
	 * 
	 * @param dManager the employee details
	 */
	public void loadEmployee(Employee dManager) {
		this.dManager = dManager;

	}

	/**
	 * set cBox values
	 */
	void setCbox() {
		// reports cbox
		ArrayList<String> al = new ArrayList<String>();
		al.add("Cancellation report");
		al.add("Visitation report");
		list1 = FXCollections.observableArrayList(al);
		reportCbox.setItems(list1);
		// start hour cbox
		al = new ArrayList<String>();
		for (int i = 8; i < 10; i++)
			al.add("0" + i + ":00");
		for (int i = 10; i < 18; i++)
			al.add(i + ":00");
		list2 = FXCollections.observableArrayList(al);
		startTime.setItems(list2);
	}

	/**
	 * initial cBox
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setCbox();

	}

	/**
	 * this method set to end cBox values after selecting start time the values are
	 * bigger then the chosen start time
	 * 
	 * @param event select start time from cBox
	 */
	@FXML
	void setEndHours(ActionEvent event) {
		int hour = Integer.parseInt(startTime.getValue().substring(0, 2));
		ArrayList<String> al = new ArrayList<String>();
		for (int i = hour; i < 10; i++)
			al.add("0" + i + ":59");
		for (int i = hour; i < 18; i++)
			al.add(i + ":59");
		list3 = FXCollections.observableArrayList(al);
		endTime.setItems(list3);
	}
}
