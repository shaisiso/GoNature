package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import client.ClientUI;
import logic.Employee;
import server.LoginController;
import server.ServerUI;

public class TestLoginServerDB {

	@Before
	public void setUp() throws Exception {
		ServerUI.runServer("1");
		ClientUI.runClient("1");
	}


	//----------------------------Server--------------------------------------
	/*
	 * -----LoginController-----
	 * test Employee Login from DB
	 * need to insert the other details of the employee if the login succeed
	 */
	@Test
	public void testEmployeeLoginFromDB() throws Exception {
		Employee logOfEmployee = new Employee("BarZ","1234");
		Employee expected = new Employee("Bar", "Zomer", "1030", "BZ@gmail.com", "Entrance", "Haifa", "BarZ",
				"1234");
		Employee result = LoginController.employeeLogin(ServerUI.sv.conn,logOfEmployee);
		assertEquals(expected, result); 
		//show that returned instance of the employee is with all details
		assertNotEquals(result.getEmployeeID(), logOfEmployee.getEmployeeID());
		assertEquals(result.getEmployeeID(), expected.getEmployeeID());
	}
	@AfterClass
	public static void afterAll() throws IOException {
		ServerUI.sv.close();
	};

}
