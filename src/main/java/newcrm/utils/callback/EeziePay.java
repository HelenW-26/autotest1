package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class EeziePay extends CallbackBase {

	private final String path = "eeziepay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.headerMap = new HashMap<>();
		this.formBody = new HashMap<>();
		String serVersion = "3.0";
		String bNo = "100";
		amount = amount.substring(0, amount.indexOf("."));
		String status = "000";
		String signature = "service_version="+serVersion+"&billno="+bNo+"&partner_orderid="+orderNum+"&currency="+currency
				+"&request_amount="+amount+"&receive_amount="+amount+"&fee=0&status="+status+"&key="+key;
		String signed = EncryptUtil.SHA1encode(signature).toUpperCase();
		
		formBody.put("service_version", serVersion);
		formBody.put("sign", signed);
		formBody.put("billno", bNo);
		formBody.put("currency", currency);
		formBody.put("request_amount", amount);
		formBody.put("receive_amount", amount);
		formBody.put("fee", "0");
		formBody.put("status", status);
		formBody.put("partner_orderid", orderNum);
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url + path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		if("VFX".equalsIgnoreCase(brand)) {
			key = "VDE2FB8pWXzchQ2NeWnJ1cZP7eXnhuan";
		}else if("VT".equalsIgnoreCase(brand)) {
			key = "virpHG0a9cqzXNmbv6I9ppsRTP0CZraK";
		}else {
			key = null;
		}
	}

}
