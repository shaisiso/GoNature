package assistance;

import java.io.IOException;

import gui.Report;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class VisitationReport {

	public void loadVisitationReport(float[][] visitorsTime, String[] st) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root;
			root = loader.load(getClass().getResource("/gui/Report.fxml").openStream());
			Report reportL = loader.getController();
			reportL.loadVisitationReport(visitorsTime, st);
			reportL.start(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
