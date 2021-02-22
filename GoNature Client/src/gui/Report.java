package gui;

import java.util.ArrayList;

import assistance.InputController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * this class is a page for handle visitor, capacity, income reports.
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class Report {
	/**
	 * title text
	 */
	@FXML
	private Text titleTxt;
	/**
	 * subTitle text
	 */
	@FXML
	private Text subTitleTxt;
	/**
	 * pie chart for some reports
	 */
	@FXML
	private PieChart pieChart;
	/**
	 * text area for some reports
	 */
	@FXML
	private TextArea txt;
	/**
	 * bar chart for some reports
	 */
	@FXML
	private BarChart<String, Number> incomeChart;
	/**
	 * bar chart x axis
	 */
	@FXML
	private CategoryAxis barX;
	/**
	 * bar chart y axis
	 */
	@FXML
	private NumberAxis barY;

	/**
	 * start presenting this page
	 * 
	 * @param root parent to set in scene
	 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Report");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * load visitor report
	 * 
	 * @param title      array with park month and year
	 * @param reportName report name
	 * @param info       report's info
	 */
	public void loadVisitorReport(String[] title, String reportName, int info[]) {
		if (reportName.equals("Visitors report")) {
			pieChart.setVisible(true);
			ObservableList<PieChart.Data> pch = FXCollections.observableArrayList(
					new PieChart.Data("organized groups: " + info[0], info[0]),
					new PieChart.Data("subscribers: " + info[1], info[1]),
					new PieChart.Data("singles: " + info[2], info[2]));
			pieChart.setData(pch);
			pieChart.setTitle("Visitors report - " + title[0] + "\n	   " + title[1] + "/" + title[2]);
		}
	}

	/**
	 * load capacity report
	 * 
	 * @param title    array with park month and year
	 * @param capacity	ArrayList of capcity report
	 */
	public void loadCapacityReport(String[] title, ArrayList<String> capacity) {
		String toSet = "";
		txt.setVisible(true);
		subTitleTxt.setVisible(true);
		titleTxt.setText("Capacity report - " + title[0] + "\n" + title[1] + "/" + title[2]);
		for (String st : capacity)
			toSet += st + "\n";
		txt.setText(toSet);
	}

	/**
	 * load cancelled orders report
	 * 
	 * @param cancelled [0]-canceled orders , [1]- did not arrived orders
	 * @param startDate date from
	 * @param endDate   date to
	 */
	public void loadCancelledReport(int[] cancelled, String startDate, String endDate) {
		pieChart.setVisible(true);
		ObservableList<PieChart.Data> pch = FXCollections.observableArrayList(
				new PieChart.Data("Cancelled orders: " + cancelled[0], cancelled[0]),
				new PieChart.Data("Did not arrive: " + cancelled[1], cancelled[1]));
		pieChart.setData(pch);
		pieChart.setLegendVisible(false);
		pieChart.setTitle("Cancellation report: \n" + InputController.getIsraelFormatDate(startDate) + "  -  "
				+ InputController.getIsraelFormatDate(endDate));

		pch.get(0).getNode().setStyle("-fx-pie-color: #C80000; ");
		pch.get(1).getNode().setStyle("-fx-pie-color:  #ffd700; ");
	}

	/**
	 * load incomes report
	 * 
	 * @param al      array with park month and year
	 * @param incomes incomes array divided to : 0- Organized groups, 1 -
	 *                Subscribers, 2 - Singles
	 */
	@SuppressWarnings("unchecked")
	public void loadIncomeReport(String[] al, float[] incomes) {
		incomeChart.setVisible(true);
		incomeChart.setTitle("Incomes report - " + al[0] + "\n	 " + al[1] + "/" + al[2] + "\n Total incomes : "
				+ (incomes[0] + incomes[1] + incomes[2]));
		barX.setLabel("Type of visitor");
		barY.setLabel("Income in ¤");
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.getData().add(new XYChart.Data<>("Organized groups", incomes[0]));
		series1.getData().add(new XYChart.Data<>("Subscribers", incomes[1]));
		series1.getData().add(new XYChart.Data<>("Singles", incomes[2]));
		series1.setName("Incomes by type");
		incomeChart.getData().addAll(series1);

	}

	/**
	 * load visitation report (department manager)
	 * 
	 * @param visitorsTime matrix that save average visit time for every hour
	 *                     between the defined range. 0- Organized groups, 1 -
	 *                     Subscribers, 2 - Singles
	 * @param st           - date, hour from , hour to
	 */
	@SuppressWarnings("unchecked")
	public void loadVisitationReport(float[][] visitorsTime, String[] st) {
		incomeChart.setVisible(true);
		int start = Integer.parseInt(st[1].substring(0, 2));
		barX.setLabel("Enter hour");
		barY.setLabel("Average visit time in hours");

		incomeChart.setTitle("Visitation report : " + st[0] + "\n	 " + st[1] + " - " + st[2]);
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		XYChart.Series<String, Number> series3 = new XYChart.Series<>();
		series1.setName("Organized groups");
		series2.setName("Subscribers");
		series3.setName("Singles");
		for (int i = 0; i < visitorsTime[0].length; i++) {
			series1.getData()
					.add(new XYChart.Data<>((i + start) + ":00 - \n" + (i + start) + ":59", visitorsTime[0][i] / 60));
			series2.getData()
					.add(new XYChart.Data<>((i + start) + ":00 - \n" + (i + start) + ":59", visitorsTime[1][i] / 60));
			series3.getData()
					.add(new XYChart.Data<>((i + start) + ":00 - \n" + (i + start) + ":59", visitorsTime[2][i] / 60));
		}
		incomeChart.getData().addAll(series1, series2, series3);
	}

}