package newcrm.utils.callback;

import newcrm.utils.encryption.EncryptUtil;

public class ZotaPay extends CallbackBase {

	private final String path = "zotaPay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.jsonBodyHeader();
		String sign = "400210" + "10317070046"+orderNum+"APPROVED"+amount+"automationcallbacktest@skrill.com"+key;
		String secStr = EncryptUtil.shaEnc(sign, "SHA-256");
		body = "{"
				+ "    \"type\":\"SALE\","
				+ "    \"status\":\"APPROVED\","
				+ "    \"errorMessage\":\"\","
				+ "    \"endpointID\":\"400210\","
				+ "    \"processorTransactionID\":\"2021031200020554\","
				+ "    \"orderID\":\"10317070046\","
				+ "    \"merchantOrderID\":\""+orderNum+"\","
				+ "    \"amount\":\""+amount+"\","
				+ "    \"currency\":\""+currency+"\","
				+ "    \"customerEmail\":\"automationcallbacktest@zota.com\","
				+ "    \"customParam\":\"\","
				+ "    \"signature\":\""+secStr+"\""
				+ "}";
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub

		this.fullPath = url+path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub

		if("VT".equalsIgnoreCase(brand)) {
			key = "9b2c7b46-3b06-4bbe-b943-60d585421e44";//VT SVG
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "f7a23436-acad-4133-bede-253af7f6dac1";
		}else if("VFX".equalsIgnoreCase(brand)) {
			if("ASIC".equalsIgnoreCase(regulator)) {
				key = "4239E382-6DB1-4568-9C24-73455CA2463B";
			}else {
				key = "9b2c7b46-3b06-4bbe-b943-60d585421e44";
			}
		}else{
			key = null;
		}
	}

}
