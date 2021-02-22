package assistance;

import java.io.IOException;

import javafx.scene.Parent;
import logic.Employee;

/**
 * this interface is for all types of employee pages
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public interface IEmployee {
	/**
	 * save employee details
	 * 
	 * @param employee	employee to load
	 */
	void loadEmployee(Employee employee);

	void start(Parent root) throws IOException;
}
