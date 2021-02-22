package assistance;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * This class is to set some textfield to be written only numbers implements
 * ChangeListener interface
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class OnlyNumbers implements ChangeListener<String> {
	private TextField txtInput;

	/**
	 * constructor to save textfield
	 * 
	 * @param txtInput	text to set
	 */
	public OnlyNumbers(TextField txtInput) {
		super();
		this.txtInput = txtInput;
	}

	/**
	 * set the text to be get only numbers as input
	 */
	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (!newValue.matches("\\d*")) {
			txtInput.setText(newValue.replaceAll("[^\\d]", ""));
		}
	}

}
