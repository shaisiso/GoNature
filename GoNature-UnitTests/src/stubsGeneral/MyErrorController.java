package stubsGeneral;

import assistance.ErrorController;

public class MyErrorController extends ErrorController {
	private String errMsg="";
	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	@Override
	public void loadError(String errMsg) {
		this.errMsg=errMsg;
	}
}
