package stubsLogin;

import assistance.RunEmployee;
import javafx.event.ActionEvent;
import logic.Employee;

public class MyRunEmployee extends RunEmployee {
	Employee employee;
	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}
	@Override
	public void loadEmployee(Employee employee,ActionEvent event) {
		this.employee=employee;
	}
}
