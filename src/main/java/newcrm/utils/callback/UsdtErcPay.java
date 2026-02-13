package newcrm.utils.callback;

import java.util.HashMap;

import newcrm.utils.encryption.EncryptUtil;

public class UsdtErcPay extends CallbackBase {
	private final String path = "cps/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.headerMap = new HashMap<>();
		this.formBody = new HashMap<>();
		String channelName = "CPS-V-USDTERC";
		String channelTxnAmt = amount;
		String merCd = "6300451100033253";
		String merOrderNo = orderNum;
		String txnAmt = amount;
		String txnCd = "T00400";
		String txnCurry = currency;
		String txnDate = "20220802";
		String txnMsg ="";
		String txnOrderDesc = "123456-"+orderNum;
		String txnOrderNo = "1003838846435065856";
		String txnSubmitTime = "20220801203710";
		String org = "channelName="+channelName + "&channelTxnAmt=" + channelTxnAmt +"&insCd=&merCd="+merCd+"&merOrderNo="+merOrderNo + "&remarks=&txnAmt="
				+ txnAmt + "&txnCd="+txnCd+"&txnCurry=usdterc&txnDate="+txnDate + "&txnMsg=" +txnMsg +"&txnOrderDesc="
				+txnOrderDesc+"&txnOrderNo="+txnOrderNo+"&txnSta=0000&txnSubmitTime="+txnSubmitTime+"&txnTime=014118&versionNo=V01";
		
		String signData = EncryptUtil.hmacSHA1(key, org);
		String callbackData = "{\"txnOrderDesc\":\""+txnOrderDesc+"\",\"signData\":\""+signData+
				"\",\"merOrderNo\":\""+merOrderNo+"\",\"txnOrderNo\":\""+txnOrderNo+"\",\"txnCurry\":\"usdterc\",\"channelTxnAmt\":\""
				+channelTxnAmt+"\",\"txnCd\":\""+txnCd+"\",\"insCd\":\"\",\"merCd\":\""+merCd+"\",\"txnSta\":\"0000\",\"txnMsg\":\""+txnMsg+
				"\",\"versionNo\":\"V01\",\"txnTime\":\"014118\",\"signType\":\"HMAC-SHA1\",\"channelName\":\""+channelName+"\",\"txnSubmitTime\":\""+txnSubmitTime+
				"\",\"txnDate\":\""+txnDate+"\",\"txnAmt\":\""+txnAmt+"\",\"remarks\":\"\"}";
		formBody.put("callbackData", callbackData);
		
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
			key = "61e7bb64f00dd7d323e50a958f9001e00a64b483";
		}
	}

}
