package tests;

import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import client.ClientUI;
import gui.EmployeeLogin;
import javafx.embed.swing.JFXPanel;
import logic.Employee;
import logic.Msg;
import server.ServerUI;
import stubsGeneral.MyConnection;
import stubsGeneral.MyErrorController;
import stubsLogin.MyRunEmployee;
import stubsLogin.MyStatementLogin;

public class TestEmployeeLoginClient {

	MyConnection myConnection = new MyConnection();
	MyStatementLogin st = new MyStatementLogin();
	static boolean firstTest = true;
	Employee logOfEmployee;
	MyErrorController err;

	@Before
	public void setUp() throws Exception {
		JFXPanel fxPanel = new JFXPanel();// for the fxml variables
		if (firstTest) {
			logOfEmployee = new Employee("YanivK", "2020");
			st.setEmployee(logOfEmployee);
			myConnection.setStatement(st);
			ServerUI.runServer("2", myConnection);
			ClientUI.runClient("2");
			firstTest = false;
		}
		err = new MyErrorController();
	}

	// -------------------------------Client---------------------------------------------
		/*
		 * test employee successful login
		 */
		@Test
		public void testEmployeeLoginExistingEmployee() throws IOException {
			EmployeeLogin employeeLogin = new EmployeeLogin();
			MyRunEmployee rEmployee = new MyRunEmployee();
			logOfEmployee = new Employee("YanivK", "2020");
			st.setEmployee(logOfEmployee);
			employeeLogin.setValuesForLogin(logOfEmployee, err, rEmployee);
			employeeLogin.checkEmployeeDetails(null);
			Employee expected = new Employee("Yaniv", "Katan", "20", "yk20@haifa.com", "Park-manager", "Sami-Ofer",
					"YanivK", "2020");
			try {
				employeeLogin.checkEmployeeDetails(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Employee result = rEmployee.getEmployee();
			assertEquals(expected, result);
			// show that returned instance of the employee is with all details
			assertEquals(result.getEmployeeID(), expected.getEmployeeID());
		}

		/*
		 * test employee login wrong user name and password
		 */
		@Test
		public void testEmployeeLoginWrongPassword() throws IOException {
			EmployeeLogin employeeLogin = new EmployeeLogin();
			MyRunEmployee rEmployee = new MyRunEmployee();
			Employee invalidEmployee = new Employee("Yaniv2K", "2021");
			st.setEmployee(logOfEmployee);
			employeeLogin.setValuesForLogin(invalidEmployee, err, rEmployee);
			employeeLogin.checkEmployeeDetails(null);
			String expected = "Invalid employee username or password / Employee is already connected";
			try {
				employeeLogin.checkEmployeeDetails(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// show that returned instance of the employee is with all details
			assertEquals(expected, err.getErrMsg());
		}

		/*
		 * test trying to connect to an employee that is currently logged
		 */
		@Test
		public void testEmployeeAlreadyLoged() throws IOException {
			EmployeeLogin employeeLogin = new EmployeeLogin();
			MyRunEmployee rEmployee = new MyRunEmployee();
			logOfEmployee = new Employee("YanivK", "2020");
			st.setEmployee(logOfEmployee);
			employeeLogin.setValuesForLogin(logOfEmployee, err, rEmployee);
			employeeLogin.checkEmployeeDetails(null);//connect employee
			employeeLogin.checkEmployeeDetails(null);//try to conncet again with the same employee
			String expected = "Invalid employee username or password / Employee is already connected";
			try {
				employeeLogin.checkEmployeeDetails(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// show that returned instance of the employee is with all details
			assertEquals(expected, err.getErrMsg());
			ClientUI.chat.accept(new Msg("Log out", logOfEmployee));
		}
		@AfterClass
		public static void afterAll() throws IOException {
			ServerUI.sv.close();
		}
}
