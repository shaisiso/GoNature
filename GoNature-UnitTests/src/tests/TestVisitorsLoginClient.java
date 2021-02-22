package tests;

import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import client.ClientUI;
import gui.EmployeeLogin;
import gui.MainPage;
import javafx.embed.swing.JFXPanel;
import logic.Employee;
import logic.Guide;
import logic.Subscriber;
import logic.Visitor;
import server.ServerUI;
import stubsGeneral.MyConnection;
import stubsGeneral.MyErrorController;
import stubsLogin.MyLoadVistorPage;
import stubsLogin.MyRunEmployee;
import stubsLogin.MyStatementLogin;

public class TestVisitorsLoginClient {
	MyConnection myConnection = new MyConnection();
	MyStatementLogin st = new MyStatementLogin();
	static boolean firstTest = true;
	Employee logOfEmployee;
	MyErrorController err;

	@Before
	public void setUp() throws Exception {
		JFXPanel fxPanel = new JFXPanel();// for the fxml variables
		if (firstTest) {
			myConnection.setStatement(st);
			ServerUI.runServer("2", myConnection);
			ClientUI.runClient("2");
			firstTest = false;
		}
		err = new MyErrorController();

	}


	/*
	 * visitor login invalid id
	 */
	@Test
	public void testVisitorLoginInvalidID() throws IOException {
		MainPage mainPage = new MainPage();
		mainPage.setValuesForLogin("ID", "123", err, null);
		mainPage.vistorLogin(null);
		assertEquals(err.getErrMsg(), "Invalid id");
	}

	/*
	 * new visitor login (not existing. should enter when id is valid)
	 */
	@Test
	public void testNewVisitorLogin() throws IOException {
		MainPage mainPage = new MainPage();
		MyLoadVistorPage myLoadVistorPage = new MyLoadVistorPage();
		mainPage.setValuesForLogin("ID", "312160344", err, myLoadVistorPage);
		mainPage.vistorLogin(null);
		Visitor expected = new Visitor("312160344");
		Visitor result = myLoadVistorPage.getVisitor();
		assertEquals(expected, result);
	}

	/*
	 * existing guide login
	 */
	@Test
	public void testExistingGuideLogin() throws IOException {
		Guide guide = new Guide("204404511", "Dani", "Shelah", "05212121321", "sh@g.com");
		st.setVisitor(guide);
		st.setEmployee(null);
		((MyConnection) ServerUI.sv.conn).setStatement(st);
		MainPage mainPage = new MainPage();
		MyLoadVistorPage myLoadVistorPage = new MyLoadVistorPage();
		mainPage.setValuesForLogin("ID", "204404511", err, myLoadVistorPage);
		mainPage.vistorLogin(null);
		Visitor expected = guide;
		Visitor result = myLoadVistorPage.getVisitor();
		assertTrue(result instanceof Guide);
		assertEquals(expected, result);
	}

	/*
	 * test existing subscriber login with sub. number
	 */
	@Test
	public void testExistingSubNumLogin() throws IOException {
		Subscriber subscriber = new Subscriber("1000", "111111118", "Moshe", "Eyal", "05012121321", "s@g.com", 4);
		st.setVisitor(subscriber);
		((MyConnection) ServerUI.sv.conn).setStatement(st);
		MainPage mainPage = new MainPage();
		MyLoadVistorPage myLoadVistorPage = new MyLoadVistorPage();
		mainPage.setValuesForLogin("Subscription no.", "1000", err, myLoadVistorPage);
		mainPage.vistorLogin(null);
		Visitor expected = subscriber;
		Visitor result = myLoadVistorPage.getVisitor();
		assertTrue(result instanceof Subscriber);
		assertEquals(expected, result);
	}

	/*
	 * test not existing subscriber login with sub. number
	 */
	@Test
	public void testNotExistingSubNumLogin() throws IOException {
		Subscriber subscriber = new Subscriber("1000", "111111118", "Moshe", "Eyal", "05012121321", "s@g.com", 4);
		st.setVisitor(subscriber);
		((MyConnection) ServerUI.sv.conn).setStatement(st);
		MainPage mainPage = new MainPage();
		MyLoadVistorPage myLoadVistorPage = new MyLoadVistorPage();
		mainPage.setValuesForLogin("Subscription no.", "1001", err, myLoadVistorPage);
		mainPage.vistorLogin(null);
		String expected = "Invalid subscription number";
		String result = err.getErrMsg();
		assertEquals(expected, result);
	}

	/*
	 * test existing subscriber login with id
	 */
	@Test
	public void testExistingSubIDLogin() throws IOException {
		Subscriber subscriber = new Subscriber("1000", "111111118", "Moshe", "Eyal", "05012121321", "s@g.com", 4);
		st.setVisitor(subscriber);
		st.setEmployee(null);
		((MyConnection) ServerUI.sv.conn).setStatement(st);
		MainPage mainPage = new MainPage();
		MyLoadVistorPage myLoadVistorPage = new MyLoadVistorPage();
		mainPage.setValuesForLogin("ID", "111111118", err, myLoadVistorPage);
		mainPage.vistorLogin(null);
		Visitor expected = subscriber;
		Visitor result = myLoadVistorPage.getVisitor();
		assertTrue(result instanceof Subscriber);
		assertEquals(expected, result);
	}
	@AfterClass
	public static void afterAll() throws IOException {
		ServerUI.sv.close();
	}
}
