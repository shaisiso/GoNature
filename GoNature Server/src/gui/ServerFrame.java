package gui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ocsf.server.ConnectionToClient;
import server.ServerUI;
/**
 * this is a gui for running server
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ServerFrame {
/**
 * ip of connection
 */
	@FXML
	private TextArea ipTxt;
/**
 * button to show connections
 */
	@FXML
	private Button conBtn;
/**
 * connection name text
 */
	@FXML
	private TextArea nameTxt;
	/**
	 * connection state text
	 */
	@FXML
	private TextArea stateTxt;
	/**
	 * text for connection id 
	 */
	@FXML
	private TextArea idTxt;
	/**
	 * text for connection port 
	 */
	@FXML
	private TextField portTxt;
/**
 * button for send port to connect
 */
	@FXML
	private Button doneBtn;
	/**
	 * text for connection port input
	 */
	@FXML
	private Text portLbl;
	/**
	 * label for connection
	 */
	@FXML
	private Text conLbl;
	@FXML
	private Text conLbl1;
/**
 * exit from this page
 * @param event		click on exit button
 */
	@FXML
	void getExit(ActionEvent event) {
		System.out.println("Exit GoNature server");
		try {
			ServerUI.sv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
/**
 * show current conncetions of clients
 * @param event		click on show connection button
 */
	@FXML
	void showConnections(ActionEvent event) {
		Thread th[]=ServerUI.sv.getClientConnections();
		String stIp = "", stName = "", stId = "", stState = "";
		for (Thread c :th) {
			stIp +=((ConnectionToClient)c).toString() + "\n";
			stName += ((ConnectionToClient)c).getName() + "\n";
			stId +=((ConnectionToClient)c).getId() + "\n";
			stState += ((ConnectionToClient)c).getState() + "\n";
		}
		ipTxt.setText(stIp);
		nameTxt.setText(stName);
		idTxt.setText(stId);
		stateTxt.setText(stState);
	}
/**
 *  
 * @return the port that entered in texfield
 */
	private String getPort() {
		return portTxt.getText();

	}
/**
 * connect to server with port from text field
 * @param event		click on done
 * @throws IOException
 */
	@FXML
	public void done(ActionEvent event) throws IOException {
		String p;
		Stage primaryStage = new Stage();

		p = getPort();

		if (p.trim().isEmpty()) {
			Parent root1 = FXMLLoader.load(getClass().getResource("/gui/ServerFrame.fxml"));
			Scene scene = new Scene(root1);
			scene.getStylesheets().add(getClass().getResource("/gui/ServerFrame.css").toExternalForm());
			primaryStage.setTitle("Server");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			ServerUI.runServer(p);
			if (ServerUI.flag) {
				portLbl.setVisible(false);
				portTxt.setVisible(false);
				doneBtn.setVisible(false);
				conLbl.setVisible(true);
				conLbl1.setVisible(false);
				conLbl.setText("Connection to port number " + p + " Succeedded!");
			} else {
				conLbl1.setText("Connection failed!");
				conLbl1.setVisible(true);
				conLbl.setVisible(false);
			}

		

		}

	}
/**
 * start presenting this page
 * @param primaryStage	stage to show
 * @throws IOException
 */
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				getExit(null);
			}
		});
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerFrame.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerFrame.css").toExternalForm());
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
