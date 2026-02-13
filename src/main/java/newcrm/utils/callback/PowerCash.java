package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class PowerCash extends CallbackBase {

	private final String path = "powercash/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		headerMap = new HashMap<>();
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		formBody = new HashMap<>();
		//add key-value to form
		formBody.put("orderid", orderNum);
		formBody.put("bin", first6num);
		formBody.put("ccn_four", last4num);
		formBody.put("card_type", "Mastercard");
		formBody.put("cardholder", "Automation callback Test");
		formBody.put("type", "capture");
		formBody.put("transactioinid", "140527457");
		formBody.put("expiry_year", year);
		formBody.put("expiry_month", month);
		formBody.put("errorcode", "0");
		formBody.put("payment_method", "1");
		formBody.put("timestamp", "1609288540");
		formBody.put("security",EncryptUtil.shaEnc("140527457capture01609288540"+key,"SHA-256"));
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url+path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		key = "QU3B1UmN9X";
	}

}
