package gui;

import java.io.IOException;

import assistance.IEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import logic.Employee;

/**
 * this class is to show gui of small page to show to department manager to view
 * reports of park manager or generate his reports
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class ViewOrGenerate {
	/**
	 * generate button
	 */
	@FXML
	private Button generateBtn;
	/**
	 * view button
	 */
	@FXML
	private Button viewBtn;
	/**
	 * employee details
	 */
	private Employee dManager;
	/**
	 * previous stage to close after selecting action
	 */
	private Stage oldPrimaryStage = new Stage();

	/**
	 * start presenting this page
	 * 
	 * @param root parent to set in scene
	 * @throws IOException
	 */
	void start(Employee dManager, Stage oldPrimaryStage, Parent root) {
		this.dManager = dManager;
		this.oldPrimaryStage = oldPrimaryStage;
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Report");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * go to generate reports of department manager
	 * 
	 * @param event click on generate button
	 * @throws IOException
	 */
	@FXML
	void generate(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		oldPrimaryStage.close();
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/DManagerMakeReport.fxml").openStream());
		DManagerMakeReport dManagerMakeReport = loader.getController();
		dManagerMakeReport.loadEmployee(dManager);
		dManagerMakeReport.start(root);
	}

	/**
	 * go to view reports of parks manager
	 * 
	 * @param event click on view button
	 * @throws IOException
	 */
	@FXML
	void view(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		oldPrimaryStage.close();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ViewReportDManager.fxml").openStream());
		IEmployee iEmployee = loader.getController();
		iEmployee.loadEmployee(dManager);
		iEmployee.start(root);
	}

}
