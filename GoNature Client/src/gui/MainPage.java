package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import assistance.ErrorController;
import assistance.InputController;
import assistance.LoadVistorPage;
import assistance.OnlyNumbers;
import assistance.VisitationReport;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Msg;
import logic.Visitor;

/**
 * this is to the gui of the main page when entering the system
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class MainPage implements Initializable {
	/**
	 * text for input of id / subscription number
	 */
	@FXML
	private TextField txtInput;
	/**
	 * button for save id and continue to orders
	 */
	@FXML
	private Button goBtn;
	/**
	 * move to employee login page
	 */
	@FXML
	private Button employeeEntranceBtn;
	/**
	 * cBox to choose id or subscription number
	 */
	@FXML
	private ComboBox<String> cBox;
	/**
	 * lis for cBox
	 */
	ObservableList<String> list;
	/**
	 * button to info page
	 */
	@FXML
	private Button infoBtn;
	/**
	 * button to about page
	 */
	@FXML
	private Button aboutBtn;
	ErrorController err = new ErrorController();
	LoadVistorPage loadVistorPage =new LoadVistorPage();
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
	 * this method load employees login page
	 *
	 * @param event pressing on employees login button
	 * @throws IOException
	 */
	@FXML
	void employeeLoginPage(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/EmployeeLogin.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Employees Log-in");
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
	 * this method is to set values to cBox
	 */
	@FXML
	private void chooseLoginMethod() {
		ArrayList<String> al = new ArrayList<String>();
		al.add("ID");
		al.add("Subscription no.");
		list = FXCollections.observableArrayList(al);
		cBox.setItems(list);
		// force the field to be numeric only
		txtInput.textProperty().addListener(new OnlyNumbers(txtInput));
	}

	// for tests
		public void setValuesForLogin(String cBoxValue, String idText,ErrorController err,
				LoadVistorPage loadVistorPage) {
			this.err = err;
			this.loadVistorPage=loadVistorPage;
			cBox = new ComboBox<String>();
			txtInput= new TextField();
			cBox.setValue(cBoxValue);
			txtInput.setText(idText);
		}
	/**
	 * search visitor by id/sub no.
	 * 
	 * @param event click on go
	 * @throws IOException
	 */
	@FXML
	public void vistorLogin(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		String errMsg = "", st[] = new String[2];
		Visitor visitor = null;
		boolean loadVisitor = true;
		st[0] = cBox.getValue();
		if (st[0] == null)// default
			st[0] = "ID";
		st[1] = txtInput.getText();
		if (st[0].equals("ID") && !InputController.isValidID(st[1])) {
			loadVisitor = false;
			errMsg = "Invalid id";
		} else {// valid id or sub number
			Msg visitorDetails = new Msg("Visitor login " + st[0], st[1]);
			ClientUI.chat.accept(visitorDetails); // send message to chat
			System.out.println("after send");
			if (visitorDetails.action.equals("Visitor login Subscription no.")) { // login with sub no.
				if (Client.subscriber == null) { // subscriber doesn't exist
					loadVisitor = false;
					errMsg = "Invalid subscription number";
				} else // existed subscriber
					visitor = Client.subscriber;

			} else {// login with id number
				visitor = Client.visitor;
			}
			if (visitor == null)
				visitor = new Visitor(st[1]);// create guest visitor
		}
		if (loadVisitor) {
			loadVistorPage.loadVisitor(visitor, event);
		} else { // error
			err.loadError(errMsg);
		}
	}

	/**
	 * this method is to load home page from the client controller
	 * 
	 * @param primaryStage stage to show on
	 */
/*	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/MainPage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
	}*/

	/**
	 * initial combox
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chooseLoginMethod();
	}

	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}

}
