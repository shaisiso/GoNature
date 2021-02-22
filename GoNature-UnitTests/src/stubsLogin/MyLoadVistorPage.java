package stubsLogin;

import java.io.IOException;

import assistance.LoadVistorPage;
import javafx.event.ActionEvent;
import logic.Visitor;

public class MyLoadVistorPage extends LoadVistorPage {
	private Visitor visitor;
	/**
	 * @return the visitor
	 */
	public Visitor getVisitor() {
		return visitor;
	}
	@Override
	public void loadVisitor(Visitor visitor,ActionEvent event) throws IOException {
		this.visitor=visitor;
	}
}
