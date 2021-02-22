package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import gui.EmployeeLogin;
import logic.Employee;
import logic.Guide;
import logic.Subscriber;
import logic.Visitor;
import server.LoginController;
import server.ServerUI;
import stubsGeneral.MyConnection;
import stubsLogin.MyStatementLogin;

public class TestLoginServerStub {
	MyConnection myConnection = new MyConnection();
	MyStatementLogin st;
	Visitor visitor;

	@Before
	public void setUp() throws Exception {
		// myConnection
		st = new MyStatementLogin();
		st.setVisitor(null);
		st.setEmployee(null);
		visitor = new Visitor("11");
		EmployeeLogin el = new EmployeeLogin();
	}

	// all test from now is with stub of the connection to DB

	/*
	 * test existing Employee Login with stub instead of the real DB
	 */
	@Test
	public void testEmployeeLogin() {
		Employee logOfEmployee = new Employee("YanivK", "2020");
		st.setEmployee(logOfEmployee);
		myConnection.setStatement(st);
		Employee expected = new Employee("Yaniv", "Katan", "20", "yk20@haifa.com", "Park manager", "Sami Ofer",
				"YanivK", "2020");
		Employee result = LoginController.employeeLogin(myConnection, logOfEmployee);
		assertEquals(expected, result);
		// show that returned instance of the employee is with all details
		assertNotEquals(result.getEmployeeID(), logOfEmployee.getEmployeeID());
		assertEquals(result.getEmployeeID(), expected.getEmployeeID());
	}

	/*
	 * 
	 * test existing visitor login with id
	 */
	@Test
	public void testVisitorIdLogin() {
		Visitor expected = new Visitor("11");
		st.setVisitor(visitor);
		myConnection.setStatement(st);
		Visitor result = LoginController.visitorIdLogin(myConnection, "11");
		assertEquals(expected, result);
	}

	/*
	 * test existing subscriber login with subNumber
	 */
	@Test
	public void testSubNumberLoginExisting() {
		Subscriber expected = new Subscriber("1000", "111111111", "Moshe", "Eyal", "05012121321", "s@g.com", 4);
		st.setVisitor(expected);
		myConnection.setStatement(st);
		Subscriber result = LoginController.subsLogin(myConnection, "1000");
		assertEquals(expected, result);
	}

	/*
	 * test existing subscriber login with id
	 */
	@Test
	public void testSubLoginWithID() {
		Subscriber expected = new Subscriber("1000", "111111111", "Moshe", "Eyal", "05012121321", "s@g.com", 4);
		st.setVisitor(expected);
		myConnection.setStatement(st);
		Subscriber result = LoginController.subsIdLogin(myConnection, "111111111");
		assertEquals(expected, result);
	}

	/*
	 * test existing Guide login with id
	 */
	@Test
	public void testRegisteredGuideLogin() {
		Guide expected = new Guide("222222222", "Dani", "Shelah", "05212121321", "sh@g.com");
		st.setVisitor(expected);
		myConnection.setStatement(st);
		Guide result = LoginController.guideLogin(myConnection, "222222222");
		assertEquals(expected, result);
	}

	@AfterClass
	public static void afterAll() throws IOException {
		ServerUI.sv.close();
	}

}
