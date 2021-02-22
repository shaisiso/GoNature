package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ VisitationsReportTestsDB.class, VisitationsReportWithStub.class, TestLoginServerDB.class,
		TestLoginServerStub.class,TestVisitorsLoginClient.class,TestEmployeeLoginClient.class })
public class AllTests {
}
