package assistance;

import java.io.IOException;

import gui.Errors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ErrorController {

	public void loadError(String errMsg) {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(errMsg);
			error.start(root);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
}
