package assistance;

import java.io.IOException;

import gui.VisitorPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import logic.Visitor;

public class LoadVistorPage {

	public void loadVisitor(Visitor visitor,ActionEvent event) throws IOException {
		FXMLLoader loader= new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/VisitorPage.fxml").openStream());
		VisitorPage visitorPage = loader.getController();
		visitorPage.loadVisitor(visitor);
		((Node) event.getSource()).getScene().getWindow().hide();
		visitorPage.start(root);
	}
}
