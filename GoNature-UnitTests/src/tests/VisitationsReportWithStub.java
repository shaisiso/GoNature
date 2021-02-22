package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import client.ClientUI;
import gui.DManagerMakeReport;
import javafx.embed.swing.JFXPanel;
import server.ManagersController;
import server.ServerUI;
import stubsGeneral.MyConnection;
import stubsGeneral.MyErrorController;
import stubsReport.MyStatementVisitation;
import stubsReport.MyVisitationReport;

public class VisitationsReportWithStub {
	String date;
	String startHour;
	String endHour;
	MyConnection myConnection = new MyConnection();
	DManagerMakeReport dManagerMakeReport;
	MyErrorController err = new MyErrorController();
	MyVisitationReport myVisitationReport = new MyVisitationReport();
	static boolean firstTest=true;
	@Before
	public void setUp() throws Exception {
		if (firstTest) {
			myConnection.setStatement(new MyStatementVisitation());
			ServerUI.runServer("1", myConnection);
			firstTest=false;
		}
		ClientUI.runClient("1");
		JFXPanel fxPanel = new JFXPanel(); // for the fxml variables
		dManagerMakeReport = new DManagerMakeReport();
		date = "2021-01-15";
		startHour = "10:00";
		endHour = "10:59";
	}

	/*
	 * Test visitation report in ManagersController class with stub (server)
	 */
	@Test
	public void VisitationReportValidDataTestServer() {
		float avgOrganized = (2 * 60 + 8) / 1; // ( hour * 60 + minutes ) / number of orders = avg in minutes = 128
		float avgSubscribers = (3 * 60 + 40) / 1; // ( hour * 60 + minutes ) / number of orders = avg in minutes = 220
		float total = (7 * 60 + 38);// total time of all visitors type =458
		float avgSingles = (total - (avgOrganized + avgSubscribers)) / 1; // = 110, the singles calculation reduce from
		// total
		float[][] expected = { { avgOrganized }, { avgSubscribers }, { avgSingles } };
		myConnection.setStatement(new MyStatementVisitation());
		float[][] result = ManagersController.visitationReport(myConnection, date, startHour, endHour);
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				assertTrue(expected[i][j] == (result[i][j]));
			}
		}
	}

	/*--------------------------Client tests--------------------------------------
	 * 	Test visitation report input of a date without data from DManagerMakeReport
	 * should upload error message in the GUI
	 */
	@Test
	public void VisitationReportNoDataTestClient() throws Exception {
		String emptyDate = "2019-01-01";
		LocalDate localDate = LocalDate.parse(emptyDate);
		dManagerMakeReport = new DManagerMakeReport();
		dManagerMakeReport.setValuesForReportTest(localDate, startHour, endHour, err, myVisitationReport);
		dManagerMakeReport.visitationReport();
		String expectedMsg = "There were no orders in the requested time";
		assertEquals(expectedMsg, err.getErrMsg());

	}

	/*
	 * Test visitation report input of a date with data
	 */
	@Test
	public void VisitationReportValidDataTestClient() throws Exception {
		LocalDate localDate = LocalDate.parse(date);
		dManagerMakeReport.setValuesForReportTest(localDate, startHour, endHour, err, myVisitationReport);
		dManagerMakeReport.visitationReport();
		float avgOrganized = (2 * 60 + 8) / 1; // ( hour * 60 + minutes ) / number of orders = avg in minutes = 128
		float avgSubscribers = (3 * 60 + 40) / 1; // ( hour * 60 + minutes ) / number of orders = avg in minutes = 220
		float total = (7 * 60 + 38);// total time of all visitors type =458
		float avgSingles = (total - (avgOrganized + avgSubscribers)) / 1; // = 110, the singles calculation reduce from
		// // total
		float[][] expected = { { avgOrganized }, { avgSubscribers }, { avgSingles } };
		float[][] result = myVisitationReport.getVisitorsTime();
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				assertTrue(expected[i][j] == (result[i][j]));
			}
		}
		ServerUI.sv.close();
	}
	
}
