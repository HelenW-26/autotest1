package newcrm.utils.callback;

import newcrm.utils.encryption.EncryptUtil;

public class CardPay extends CallbackBase {

	private final String path = "cardpay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		body = "{"
				+ "    \"callback_time\": \"2022-07-24T20:23:18.552Z\","
				+ "    \"payment_method\": \"BANKCARD\","
				+ "    \"merchant_order\": {"
				+ "        \"id\": \""+orderNum+"\""
				+ "    },"
				+ "    \"customer\": {"
				+ "        \"email\": \"automation_callback@test.com\","
				+ "        \"id\": \"8241512\","
				+ "        \"phone\": \"3421934838\","
				+ "        \"ip\": \"151.18.3.26\","
				+ "        \"locale\": \"en\""
				+ "    },"
				+ "    \"payment_data\": {"
				+ "        \"id\": \"529069193\","
				+ "        \"status\": \"COMPLETED\","
				+ "        \"amount\":"+amount+","
				+ "        \"currency\": \""+currency+"\","
				+ "        \"created\": \"2022-06-30T20:22:49.328584Z\","
				+ "        \"note\": \"cardpay\","
				+ "        \"rrn\": \"000407611335\","
				+ "        \"auth_code\": \"H41173\","
				+ "        \"is_3d\": true"
				+ "    },"
				+ "    \"card_account\": {"
				+ "        \"masked_pan\": \""+first6num+"..."+last4num+"\","
				+ "        \"issuing_country_code\": \"IT\","
				+ "        \"holder\": \"Automation Callback test\","
				+ "        \"expiration\": \""+month+"/"+year+"\""
				+ "    }"
				+ "}";
		//3Ve2CbQA5hy1 is security code for cardpay
		String secStr = EncryptUtil.shaEnc(body+key,"SHA-512");
		jsonBodyHeader();
		this.headerMap.put("signature", secStr);
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
			key = "2J4gbMPh39yE";
		}else if("VT".equalsIgnoreCase(brand)) {
			key = "3Ve2CbQA5hy1";
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "Bm0i9GzIZ42u";
		}else {
			key = null;
		}
	}

}
