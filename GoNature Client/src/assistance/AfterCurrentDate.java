package assistance;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 * this class is to block datepicker dates for dates before the current date.
 * implements Callback interface
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class AfterCurrentDate implements Callback<DatePicker, DateCell> {
	
	/**
	 * this method define dates to be from the current date
	 * @param param - the DatePicker to block
	 */
	@Override
	public DateCell call(final DatePicker param) {
		return new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty); // To change body of generated methods, choose Tools | Templates.
				LocalDate today = LocalDate.now();
				setDisable(empty || item.compareTo(today) < 1);
			}
		};
	}
}
