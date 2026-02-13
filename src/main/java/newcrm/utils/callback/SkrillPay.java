package newcrm.utils.callback;

import java.util.HashMap;

public class SkrillPay extends CallbackBase {

	private final String path = "skrill/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub

		headerMap = new HashMap<>();
		formBody = new HashMap<>();
		formBody.put("transaction_id", orderNum);
		formBody.put("status", "2");
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub

		this.fullPath = url + path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub

	}

}
