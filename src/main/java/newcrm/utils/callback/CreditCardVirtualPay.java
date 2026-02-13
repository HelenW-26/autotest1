package newcrm.utils.callback;

import java.util.HashMap;

public class CreditCardVirtualPay extends CallbackBase {

	private final String path="paywize/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		fullPath = fullPath + "?merchant_order="+orderNum+"&status=approved";
		headerMap = new HashMap<>();
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		formBody = new HashMap<>();
		//add key-value to form
		formBody.put("merchant_order", orderNum);
		formBody.put("status", "approved");
		formBody.put("last-four-digits", this.last4num);
		formBody.put("card-exp-month", this.month);
		formBody.put("card-exp-year", this.year);
		formBody.put("name", "Automation CallBack VirtualPay");
		formBody.put("verified-3d-status", "AUTHENTICATED");
		formBody.put("bin", this.first6num);
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
