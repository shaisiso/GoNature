package gui;

import java.io.IOException;

import assistance.IEmployee;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Msg;

/**
 * this class is to show gui of Park manager page
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class PManagerPage implements IEmployee {
	/**
	 * info button
	 */
	@FXML
	private Button infoBtn;
	/**
	 * entrance page button
	 */
	@FXML
	private Button entranceBtn;
	/**
	 * text with hello message
	 */
	@FXML
	private Text helloTxt;
	/**
	 * about button
	 */
	@FXML
	private Button aboutBtn;

	/**
	 * for save employee details
	 */
	private Employee pManager;

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
	 * login)
	 *
	 * @param event pressing on back button
	 */
	@FXML
	void goBack(ActionEvent event) {
		ClientUI.chat.accept(new Msg("Log out", this.pManager));
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
	 * this method overrides the interface's method and saves the park manager
	 * details to show in the screen
	 * 
	 * @param employee park manager details
	 */
	@Override
	public void loadEmployee(Employee employee) {
		pManager = employee;
		helloTxt.setText("Hello " + pManager.getFirstName());
	}

	/**
	 * this method will go to PManagerReports page
	 * 
	 * @param event click on reports button
	 * @throws IOException
	 */
	@FXML
	void addReports(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/PManagerMakeReports.fxml").openStream());
		PManagerReports pManagerReports = loader.getController();
		pManagerReports.loadDetails(pManager);
		pManagerReports.start(root);

	}

	/**
	 * this method will go to park entrance that this manager works in
	 * 
	 * @param event click on park entrance button
	 * @throws IOException
	 */
	@FXML
	void toParkEntrance(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Entrance.fxml").openStream());
		ParkEntrance entrance = loader.getController();
		entrance.loadEmployee(pManager);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Entrance");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method will load customer service page for register guide/subscriber
	 * 
	 * @param event click on Add membership/Guide
	 * @throws IOException
	 */
	@FXML
	void toRegister(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/Service.fxml").openStream());
		ServicePage service = loader.getController();
		service.loadEmployee(pManager);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Service");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method will go to update page of this park
	 * 
	 * @param event click on updates button
	 * @throws IOException
	 */
	@FXML
	void update(ActionEvent event) throws IOException {
		Msg detailsMsg = new Msg("Park details", pManager.getParkName());
		ClientUI.chat.accept(detailsMsg);
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ParkManagerUpdates.fxml").openStream());
		PMUpdates pMUpdates = loader.getController();
		pMUpdates.loadEmployee(pManager, Client.park);
		pMUpdates.start(root);
	}

	/**
	 * this method will start presenting this page
	 */
	@Override
	public void start(Parent root) throws IOException {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("PManager");
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

}
