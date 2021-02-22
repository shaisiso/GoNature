package gui;

import java.io.IOException;

import assistance.ErrorController;
import assistance.IEmployee;
import assistance.RunEmployee;
import client.Client;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Employee;
import logic.Msg;

/**
 * this class is to show gui of employees login page
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
public class EmployeeLogin {
	/**
	 * user name text field
	 */
	@FXML
	private TextField userNameTxt;
	/**
	 * login button
	 */
	@FXML
	private Button goBtn;
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
	 * password text field
	 */
	@FXML
	private PasswordField passwordTxt;
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
	ErrorController err = new ErrorController();
	RunEmployee rEmployee = new RunEmployee();

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

	// for tests
	public void setValuesForLogin(Employee emp, ErrorController err, RunEmployee rEmployee) {
		this.err = err;
		this.rEmployee = rEmployee;
		userNameTxt=new TextField();
		passwordTxt=new PasswordField();
		userNameTxt.setText(emp.getUserName());
		passwordTxt.setText(emp.getPassword());
	}

	/**
	 * this method check the details from the text and send it for checking from the
	 * server
	 *
	 * @param event pressing on info button
	 * @throws IOException
	 */
	@FXML
	public void checkEmployeeDetails(ActionEvent event) throws IOException {
		Employee employee = new Employee(userNameTxt.getText(), passwordTxt.getText());
		Msg employeeMsg = new Msg("Employee login", employee);
		ClientUI.chat.accept(employeeMsg);
		employee = Client.employee;
		if (employee == null) { // invalid employee user or password
			err.loadError("Invalid employee username or password / Employee is already connected");

		} else { // load page by the role of the employee
			rEmployee.loadEmployee(employee, event);
		}
	}

	/**
	 * this method load previous page - MainPage
	 *
	 * @param event pressing on back button
	 */
	@FXML
	void goBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/MainPage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
			primaryStage.setTitle("Main");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
		}

	}
}
