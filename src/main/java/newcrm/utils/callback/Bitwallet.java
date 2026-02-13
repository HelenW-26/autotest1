package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class Bitwallet extends CallbackBase {
	private final String path = "bitwallet/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		headerMap = new HashMap<>();
		formBody  = new HashMap<>();
		String transid = "7XBPw8";
		String org = orderNum+transid+"v0wjNb";
		String token = EncryptUtil.hmacSHA256(org, key);
		
		formBody.put("merchanttransid", orderNum);
		formBody.put("code", "1");
		formBody.put("transid", transid);
		formBody.put("token", token);
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		if("VFX".equalsIgnoreCase(brand)) {
			key = "lksr6RHyFql4VoFe";
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "DY6XXaaiiOee4WQc";
		}else {
			key = "DY6XXaaiiOee4WQc";
		}
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url + path;
	}
}
