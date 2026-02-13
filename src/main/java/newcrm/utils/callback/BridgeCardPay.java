package newcrm.utils.callback;

public class BridgeCardPay extends CallbackBase {

	private final String path = "bridgerpay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		body = "{\"webhook\":{\"type\":\"approved\"},\"data\":{\"order_id\":\""+
				orderNum +"\",\"psp_name\":\"card_pay\",\"charge\":{\"type\":\"approved\",\"id\":\"f3b41b8e-0050-4643-a9fa-3da995bc1f7b\",\"psp_order_id\":\"424600493\","
				+"\"attributes\":{\"is3_d\":true,\"live_mode\":true,\"amount\":"+amount+",\"status\":\"approved\",\"card_number\":\"9999\",\"currency\":\""+
				currency+"\",\"payment_method\":\"credit_card\",\"description\":\""
				+orderNum +"\",\"decline_code\":null,\"decline_reason\":null,\"reference_id\":null,\"created_at\":1609288540,\"updated_at\":0,\"source\":{\"email\":\"slantage@mail.com\",\"ip_address\":\"82.132.244.154\",\"name\":\"Test Postman\"},\"card_masked_number\":\""+first6num+"******"+last4num+"\",\"card_expiration\":\""
				+month+year.substring(2)+"\",\"card_brand\":\"MASTERCARD\",\"card_holder_name\":\"Automation callBack bridge\",\"customer\":{\"full_name\":null,\"address\":\"34 Lake Avenue\",\"country\":\"GB\",\"zip_code\":\"LL181HY\",\"extra_data\":{}},\"credit_card_token\":null},\"is_refundable\":true,\"refund_id\":\"7684cb0d-b7ba-4b94-bf71-1247b50ed3d5\",\"operation_type\":\"deposit\",\"deposit_source\":\"cashier\"}},\"meta\":{\"server_time\":1609288540,\"server_timezone\":\"UTC\",\"api_version\":\"1\",\"payload\":null,\"cashier_session_id\":\"6f0502a6-fc99-4305-b0df-a3b5631c31aa\",\"platform_id\":\"\"}}";
		jsonBodyHeader();
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
