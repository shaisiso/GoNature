package client;

import gui.Errors;
import gui.MainPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this is a gui for client connection with port
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ClientFrame {
/**
 * port that entered
 */
	public static String port;
/**
 * port label 
 */
	@FXML
	private Text txt;
/**
 * button to connect
 */
	@FXML
	private Button connectBtn;
/**
 * text field for port input
 */
	@FXML
	private TextField portTxt;
/**
 * connect to server with port from input
 * @param event			click on connect button
 * @throws Exception
 */
	@FXML
	void connect(ActionEvent event) throws Exception {
		port = portTxt.getText();
		FXMLLoader loader = new FXMLLoader();
		if (port.trim().isEmpty()) {
			Parent root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg("You must enter a port number");
			error.start(root);
		} else {
			ClientUI.runClient(port);
			FXMLLoader ld = new FXMLLoader();
			Parent root;
			if (ClientController.isConnected) {
				((Node) event.getSource()).getScene().getWindow().hide();
				root = ld.load(getClass().getResource("/gui/MainPage.fxml").openStream());
				MainPage aFrame = ld.getController();
				aFrame.start(root);
			} else {
				root = ld.load(getClass().getResource("/gui/Errors.fxml").openStream());
				Errors error = ld.getController();
				error.setMsg("wrong port");
				error.start(root);
			}
		}

	}

	/**
	 * start presenting this page
	 * 
	 * @param primaryStage stage to show on
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client/ClientFrame.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
