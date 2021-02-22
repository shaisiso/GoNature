package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import assistance.IEmployee;
import assistance.InputController;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Employee;
import logic.Guide;
import logic.Msg;
import logic.Subscriber;

/**
 * this class is to show gui of customer service page where he can add
 * membership card or guide available for managers as well
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ServicePage implements Initializable, IEmployee {
	/**
	 * last name text
	 */
	@FXML
	private TextField LnameTxt;
	/**
	 * first name text
	 */
	@FXML
	private TextField FnameTxt;
	/**
	 * phone text
	 */
	@FXML
	private TextField phoneTxt;
	/**
	 * email text
	 */
	@FXML
	private TextField emailTxt;
	/**
	 * id text
	 */
	@FXML
	private TextField idTxt;
	/**
	 * family number text
	 */
	@FXML
	private TextField familyTxt;
	/**
	 * family number label
	 */
	@FXML
	private Text familyLbl;
	/**
	 * credit card text
	 */
	@FXML
	private TextField creditCardTxt;
	/**
	 * red asterisk label
	 */
	@FXML
	private Text redLbl;
	/**
	 * submit button
	 */
	@FXML
	private Button submitBtn;
	/**
	 * cBox for choose membership/guide registration
	 */
	@FXML
	private ComboBox<String> cBox;
	/**
	 * list for cBox
	 */
	ObservableList<String> list;
	/**
	 * text for subscription number after registering subscriber
	 */
	@FXML
	private Text finalSubNum;
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
	 * employee details
	 */
	private Employee employee;
	/**
	 * button for clearing page
	 */
	@FXML
	private Button newRegistrationBtn;

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
	 * this method will disconnect the employee and load previous page(employees
	 * login)
	 *
	 * @param event pressing on back button
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
	 * this method tries to register visitor to new: guide/membership card
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void register(ActionEvent event) throws IOException {
		boolean isGuide = true;
		if (cBox.getValue() == null || cBox.getValue().equals("Memberbership card"))
			isGuide = false;
		if (checkInput(isGuide)) {
			String creditCard = creditCardTxt.getText();
			Msg subMsg;
			boolean flag = false;
			if (creditCardTxt.getText().trim().isEmpty()) // check if need to save credit card
				creditCard = null;
			if (cBox.getValue() == null || cBox.getValue().equals("Memberbership card")) {
				Subscriber subscriber = new Subscriber(idTxt.getText(), FnameTxt.getText(), LnameTxt.getText(),
						phoneTxt.getText(), emailTxt.getText(), Integer.parseInt(familyTxt.getText()), creditCard);
				subMsg = new Msg("Subscriber registration", subscriber);
			} else {
				Guide guide = new Guide(idTxt.getText(), FnameTxt.getText(), LnameTxt.getText(), emailTxt.getText(),
						phoneTxt.getText(), creditCard);
				subMsg = new Msg("Guide registration", guide);
			}
			ClientUI.chat.accept(subMsg);
			String errMsg = "";
			if (subMsg.action.equals("Subscriber registration")) {
				int subNumber = Client.newSubNumber;
				if (subNumber != -1)
					finalSubNum.setText("Your subscription number is: " + subNumber);
				else {
					finalSubNum.setText("");
					errMsg = "There is a subscriber with this ID";
					flag = true;
				}
			} else { // Guide registration
				if (Client.guideWasAdded)
					finalSubNum.setText("The guide was added");
				else {
					finalSubNum.setText("");
					errMsg = "There is already a guide with this ID";
					flag = true;
				}
			}
			if (flag) {
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
				Errors error = loader.getController();
				error.setMsg(errMsg);
				error.start(root);
			} else
				newRegistrationBtn.setVisible(true);
		}
	}

	/**
	 * this method check input if valid
	 * 
	 * @param isGuide true - guide, false - subscriber
	 * @return true - valid input , flase otherwiswe
	 * @throws IOException
	 */
	private boolean checkInput(boolean isGuide) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (InputController.isValidName(FnameTxt.getText()) // check valid first name
				&& InputController.isValidName(LnameTxt.getText()) // check valid last name
				&& InputController.isValidPhone(phoneTxt.getText()) // check valid phone number
				&& InputController.isValidEmail(emailTxt.getText()) // check valid email
				&& InputController.isValidID(idTxt.getText()) // check valid id
				&& InputController.isValidVisitorsAmountReg(familyTxt.getText(), isGuide) // check valid family members
																							// amount
		)
			return true;
		else { // load error
			Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(InputController.errMsg);
			error.start(root);
			return false;
		}
	}

	/**
	 * set cBox values
	 */
	private void setRegistrationType() {
		familyTxt.textProperty().addListener(new OnlyNumbers(familyTxt));
		phoneTxt.textProperty().addListener(new OnlyNumbers(phoneTxt));
		idTxt.textProperty().addListener(new OnlyNumbers(idTxt));
		ArrayList<String> al = new ArrayList<String>();
		al.add("Membership card");
		al.add("Guide");
		list = FXCollections.observableArrayList(al);
		cBox.setItems(list);
	}

	/**
	 * initial cBox
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setRegistrationType();
	}

	/**
	 * this method hide/show fields by the type of registration: guide/membership
	 * card
	 * 
	 * @param event select one of the cBox options
	 */
	@FXML
	void hideFamilyFields(ActionEvent event) {
		String type = cBox.getValue();
		if (type.equals("Guide")) {
			familyLbl.setVisible(false);
			redLbl.setVisible(false);
			familyTxt.setVisible(false);
		} else {
			familyLbl.setVisible(true);
			redLbl.setVisible(true);
			familyTxt.setVisible(true);
		}
	}

	/**
	 * this method overrides the interface's method and save employee details to
	 * show in the page
	 *
	 * @param employee the employee details
	 */
	@Override
	public void loadEmployee(Employee employee) {
		this.employee = employee;
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
		primaryStage.setTitle("Customer service");
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
	 * this method will clear the textfields from data
	 * 
	 * @param event click on new registration button
	 * @throws IOException
	 */
	@FXML
	void newRegistration(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = loader.load(getClass().getResource("/gui/Service.fxml").openStream());
		IEmployee iEmployee = loader.getController();
		iEmployee.loadEmployee(employee);
		iEmployee.start(root);
	}
}