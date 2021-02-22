package assistance;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import logic.Employee;

public class RunEmployee {

	public void loadEmployee(Employee employee,ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/gui/" + employee.getRole() + ".fxml").openStream());

		IEmployee iEmployee = loader.getController();
		iEmployee.loadEmployee(employee);
		iEmployee.start(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
