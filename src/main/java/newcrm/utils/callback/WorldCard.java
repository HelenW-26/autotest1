package newcrm.utils.callback;

public class WorldCard extends CallbackBase {
	private final String path = "worldcard/cc/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		body = "{"
				+ "    \"card\": {\n"
				+ "        \"id\": \"src_fitfyylq6yauviby5jij54dehm\",\n"
				+ "        \"type\": \"card\",\n"
				+ "        \"expiryMonth\": "+month+",\n"
				+ "        \"expiryYear\": "+year+",\n"
				+ "        \"holder\": \"Automation test\",\n"
				+ "        \"last4Digits\": \""+last4num+"\",\n"
				+ "        \"fingerprint\": \"B1291187A7DCEA831407DF0C26D436D0C45685D1EC3ED730F481C065F75D2D04\",\n"
				+ "        \"bin\": \""+first6num+"\",\n"
				+ "        \"avs_check\": \"S\",\n"
				+ "        \"cvv_check\": \"Y\"\n"
				+ "    },\r\n"
				+ "    \"amount\":"+amount+",\n"
				+ "    \"currency\": \""+currency+"\",\n"
				+ "    \"payment_type\": \"Regular\",\n"
				+ "    \"merchantTransactionId\": \""+orderNum+"\",\n"
				+ "    \"result\": {\n"
				+ "            \"code\": \"000.100.110\",\n"
				+ "            \"response_summary\": \"Approved\"\n"
				+ "    }\n"
				+ "}";
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
