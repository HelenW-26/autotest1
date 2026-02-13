package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class FasaPay extends CallbackBase {

	private final String path = "fasaPay/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		String paidto = "FP311894";
		//String pass = "VantageFx";
		String str1 = paidto+":FP716941:"+brand+":"+amount+":TR2021033033818:"+currency+":"+key;
		String str2 = paidto+":FP716941:"+brand+":"+amount+":5.00:FiR:"+amount+":TR2021033033818:"+currency+":"+key;
		String sign1 = EncryptUtil.shaEnc(str1, "SHA-256");
		String sign2 = EncryptUtil.shaEnc(str2, "SHA-256");

		this.headerMap = new HashMap<>();
		
		this.formBody = new HashMap<>();
		
		formBody.put("fp_paidto", paidto);
		formBody.put("fp_amnt", amount);
		formBody.put("fp_paidby", "FP716941");
		formBody.put("fp_hash", sign1);
		formBody.put("fp_merchant_ref", orderNum);
		formBody.put("fp_total", amount);
		formBody.put("fp_hash_2", sign2);
		formBody.put("fp_fee_mode", "FiR");
		formBody.put("fp_fee_amnt", "5.00");
		formBody.put("fp_store", brand);
		formBody.put("track_id", orderNum);
		formBody.put("fp_currency", currency);
		formBody.put("fp_batchnumber", "TR2021033033818");
		formBody.put("order_id", orderNum);
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url + path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub

		if("VT".equalsIgnoreCase(brand)) {
			key = "5NrS!2";
		}else if("VFX".equalsIgnoreCase(brand)){
			if("ASIC".equalsIgnoreCase(regulator)) {
				key = "VantageFX";
			}else {
				key = "5NrS!2";
			}
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "PUGMoney";
		}else {
			key = null;
		}
	}

}
