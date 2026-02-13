package newcrm.utils.callback;

import newcrm.utils.encryption.EncryptUtil;

public class AstroPay extends CallbackBase {

	private final String path = "astropay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.jsonBodyHeader();
		body = "{\"merchant_deposit_id\":\""+orderNum+"\",\"status\":\"APPROVED\"}";
		String sign = EncryptUtil.hmacSHA256(body, key);
		headerMap.put("signature", sign);
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
			key = "xpsa6ip7wCzxiC3uDsFaLrW4lSSK3k0M";
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "DzHpgYjYW0pEZkllpAK5vxnOnrcbQaSL";
		}else {
			key ="DzHpgYjYW0pEZkllpAK5vxnOnrcbQaSL";
		} 
	}

}
