package newcrm.utils.callback;

import java.util.HashMap;

public class SticPay extends CallbackBase {
	
	private final String path = "stic/receiveWebDepositResult";

	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.headerMap = new HashMap<>();
		String json = "{\"parameters\":\"{\\\"order_no\\\":\\\""+orderNum + "\\\"}\"}";
		this.formBody = new HashMap<>();
		formBody.put("callback", json);
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
