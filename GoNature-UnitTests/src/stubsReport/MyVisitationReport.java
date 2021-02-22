package stubsReport;

import assistance.VisitationReport;

public class MyVisitationReport extends VisitationReport {

	private float[][] visitorsTime;

	
	public float[][] getVisitorsTime() {
		return visitorsTime;
	}

	@Override
	public void loadVisitationReport(float[][] visitorsTime, String[] st) {
		this.visitorsTime = visitorsTime;
	}
	
}
