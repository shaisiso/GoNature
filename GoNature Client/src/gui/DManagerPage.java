package gui;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;
import logic.Park;

/**
 * this class is to show gui of Department manager page
 * 
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class DManagerPage implements IEmployee, Initializable {
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
	 * text for hello message
	 */
	@FXML
	private Text helloTxt;
	/**
	 * button for updates page
	 */
	@FXML
	private Button updatesBtn;
	/**
	 * button for reports page
	 */
	@FXML
	private Button reportsBtn;
	/**
	 * label for updates notifications
	 */
	@FXML
	private Label notification;
	/**
	 * button for customer service page
	 */
	@FXML
	private Button registerBtn;
	/**
	 * button for entrance page
	 */
	@FXML
	private Button entranceBtn;
	/**
	 * cBox to choose park for entrance page
	 */
	@FXML
	private ComboBox<String> parkCbox;
	/**
	 * list of values to cBox
	 */
	ObservableList<String> list;
	/**
	 * save employee details
	 */
	private Employee dManager;
	/**
	 * save stage to send later
	 */
	private Stage primaryStage2 = new Stage();

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
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Information");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method will disconnect the employee and load previous page(employees
	 * login) will also logout the employee
	 * 
	 * @param event pressing on back button
	 */
	@FXML
	void goBack(ActionEvent event) {
		ClientUI.chat.accept(new Msg("Log out", this.dManager));
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/EmployeeLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
			primaryStage.setTitle("Employees Log-in");
			primaryStage.setScene(scene);
			primaryStage.show();
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
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("About");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method overrides the interface's method and save department manager
	 * details to show in the page
	 *
	 * @param employee the employee details
	 */

	@Override
	public void loadEmployee(Employee employee) {
		this.dManager = employee;
		helloTxt.setText("Hello " + dManager.getFirstName());
		int numNotification = 0;
		String parksName[] = { "Haifa", "Tel-Aviv", "Eilat" };
		for (int i = 0; i < 3; i++) {
			Msg showRequests = new Msg("Show requests", parksName[i]);
			ClientUI.chat.accept(showRequests);
			Map<String, ArrayList<String>> requests = Client.requests;
			if (requests.get("maxCapacity") != null)
				numNotification++;
			if (requests.get("maxPreOrder") != null)
				numNotification++;
			if (requests.get("averageVisitTime") != null)
				numNotification++;
			if (!requests.get("discount").isEmpty())
				numNotification += requests.get("discount").size();
		}
		if (numNotification > 0) {
			notification.setVisible(true);
			notification.setText("+" + numNotification);
		}

	}

	/**
	 * this method upload this page
	 */
	@Override
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage2.setTitle("DManager");
		primaryStage2.setScene(scene);
		primaryStage2.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				ClientUI.chat.accept(new Msg("Log out", dManager));
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage2.show();
	}

	/**
	 * this method will upload the DepManagerUpdates
	 * 
	 * @param event click on updates
	 * @throws IOException
	 */
	@FXML
	void confirmUpdates(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/DepManagerUpdates.fxml").openStream());
		IEmployee iEmployee = loader.getController();
		iEmployee.loadEmployee(dManager);
		iEmployee.start(root);
	}

	/**
	 * this method will upload the ViewOrGenerate - a small screen to choose if to
	 * view reports of park managers or to make one
	 * 
	 * @param event click on reports
	 * @throws IOException
	 */
	@FXML
	void reports(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/ViewORgenerate.fxml").openStream());
			ViewOrGenerate viewOrGenerate = loader.getController();
			viewOrGenerate.start(dManager, primaryStage2, root);
		} catch (IOException e) {
			System.out.println("E ");
			// e.printStackTrace();
		}
	}

	/**
	 * set cBox values
	 */
	void setParkName() {
		Msg parksMsg=new Msg("Get all parks",null);
		ClientUI.chat.accept(parksMsg);
		ArrayList<Park> allParks= Client.allParks;
		ArrayList<String> al = new ArrayList<String>();	
		for (Park p : allParks)
			al.add(p.getParkName());
		list = FXCollections.observableArrayList(al);
		parkCbox.setItems(list);
	}

	/**
	 * initial cBox
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setParkName();
	}

	/**
	 * this method wiil upload park entrance page after selecting a park from cBox
	 * 
	 * @param event click on park entrance
	 * @throws IOException
	 */
	@FXML
	void toParkEntrance(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		if (parkCbox.getValue() == null) {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg("Choose a park at first");
			error.start(root);
		} else {
			((Node) event.getSource()).getScene().getWindow().hide();
			root = loader.load(getClass().getResource("/gui/Entrance.fxml").openStream());
			ParkEntrance entrance = loader.getController();
			dManager.setParkName(parkCbox.getValue());
			entrance.loadEmployee(dManager);
			entrance.start(root);
		}
	}

	/**
	 * this method will upload ServicePage - the page of customer service for
	 * registering guide/subscriber
	 * 
	 * @param event click on Add membersip/guide
	 * @throws IOException
	 */
	@FXML
	void toRegister(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Service.fxml").openStream());
		ServicePage service = loader.getController();
		service.loadEmployee(dManager);
		service.start(root);
	}
}