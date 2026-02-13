package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.XpayDataEncrypt;

public class Xpay extends CallbackBase {

	private final String path = "xpay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		headerMap = new HashMap<>();
		//headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "*/*");
		headerMap.put("Connetion", "keep-alive");
		headerMap.put("User-Agent", "Apache-HttpClient/4.5.12 (Java/16.0.1)");
		String data = "RefID="+orderNum+"&Curr="+currency+"&Amount="+amount+"&Status=002&TransID=2020082573012178&ValidationKey=29251126&EncryptText="+key;
		type = "get";
		String secString = XpayDataEncrypt.EncryptData(data);
		fullPath = fullPath+"?EncryptText="+key+"&Data="+secString;
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url + path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		key = "03E7690E61D9B4862D17D09431C13D77";
	}

}
