package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class PerfecMoney extends CallbackBase {
	private final String path ="perfectmoney/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		
		
		headerMap = new HashMap<>();
		formBody  = new HashMap<>();
		String payeeAcct = "U34315799";
		String PaymentBatchNo = "789012";
		String payerAcct = "U456789";
		String timestampGMT = "876543210";
		String passphraseHash = EncryptUtil.MD5(key).toUpperCase();
		String concatHash = orderNum+":"+payeeAcct+":"+amount+":"+currency+":"+PaymentBatchNo+":"+payerAcct+":"+passphraseHash+":"+timestampGMT;
		String encHash = EncryptUtil.MD5(concatHash).toUpperCase();
		formBody.put("PAYMENT_AMOUNT", amount);
		formBody.put("PAYMENT_BATCH_NUM", PaymentBatchNo);
		formBody.put("PAYER_ACCOUNT", payerAcct);
		formBody.put("V2_HASH", encHash);
		formBody.put("BAGGAGE_FIELDS", "");
		formBody.put("PAYMENT_ID", orderNum);
		formBody.put("PAYMENT_UNITS", currency);
		formBody.put("TIMESTAMPGMT", timestampGMT);
		formBody.put("PAYEE_ACCOUNT", payeeAcct);
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
			key = "mP3T3Np352gNioPMsLtWiMEMk";
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "7557Xv0TMyeTvaABbqhZOnHsl";
		}else if("VT".equalsIgnoreCase(brand)) {
			key = "92eE75Xh1KQWDmxNclVWGQhIF";
		}else {
			key = null;
		}
	}

}
