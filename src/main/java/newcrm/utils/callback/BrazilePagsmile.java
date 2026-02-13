package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class BrazilePagsmile extends CallbackBase {
	private final String path = "pagsmile/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.headerMap = new HashMap<>();
		this.formBody = new HashMap<>();
		String orgStr = "currency="+currency+"&out_trade_no="+orderNum+"&passback_params="+orderNum+"&pay_channel="+"103"+"&sign_type=MD5&total_amount="+amount+"&trade_no="+
		"2021012702235396313"+"&trade_status="+"TRADE_SUCCESS"+"&version=1.0&key="+key;
		
		String sign = EncryptUtil.MD5(orgStr);
		formBody.put("out_trade_no", orderNum);
		formBody.put("trade_status", "TRADE_SUCCESS");
		formBody.put("sign",sign);
		formBody.put("total_amount",amount);
		formBody.put("pay_channel", "103");
		formBody.put("trade_no", "2021012702235396313");
		formBody.put("currency", currency);
		formBody.put("passback_params", orderNum);
		formBody.put("version", "1.0");
		formBody.put("sign_type", "MD5");
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
			key =  "93f1b9e6e56730bcf804357414017d47";
		}else {
			key = null;
		}
	}

}
