package tests;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import client.ClientUI;
import gui.DManagerMakeReport;
import javafx.embed.swing.JFXPanel;
import server.ManagersController;
import server.ServerUI;
import stubsGeneral.MyConnection;
import stubsGeneral.MyErrorController;
import stubsReport.MyStatementVisitation;
import stubsReport.MyVisitationReport;

public class VisitationsReportTestsDB {
	String startHour;
	String endHour;

	@Before
	public void setUp() throws Exception {
		ServerUI.runServer("12");
		ClientUI.runClient("12");
		startHour = "10:00";
		endHour = "17:00";

	}

	/*------------------Server tests--------------------------------------------
	 * test valid data from db : on 2020-11-19 on order from each type of visitor
	 *
	 * type: 			organized group 	subscribers 	singles
	 * enter: 			12:18:34 			13:18:34 		16:18:34
	 * exit: 			13:18:34 			16:18:34 		18:18:34 
	 * total time:		60 minutes 			180 minutes 	120 minutes
	 */
	@Test
	public void VisitationReportValidDataFromDB() throws IOException {
		int hourRange = Integer.parseInt(endHour.substring(0, 2)) - Integer.parseInt(startHour.substring(0, 2)) + 1;
		float[][] expected = new float[3][hourRange];
		expected[0][2] = 60; // organized group : 1 order was entered in 12 for 60 minutes
		expected[1][3] = 180;// subscribers : 1 order was entered in 13 for 180 minutes
		expected[2][6] = 120;// singles : 1 order was entered in 16 for 120 minutes
		float[][] result = ManagersController.visitationReport(ServerUI.sv.conn, "2020-11-19", startHour, endHour);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 8; j++)
				assertTrue(expected[i][j] == (result[i][j]));
	}

}