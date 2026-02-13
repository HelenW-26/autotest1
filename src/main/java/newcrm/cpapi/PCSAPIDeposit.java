package newcrm.cpapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.utils.api.HyTechUtils;
import newcrm.utils.api.HyTechUrl;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import utils.LogUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;
import static newcrm.cpapi.CustomPaymentPayload.submitDPPayloadBuilder.*;


public class PCSAPIDeposit extends CPAPIDeposit{

	public PCSAPIDeposit(String url, String cplogin, String password){
		super(url, cplogin, password);
	}

	public JSONObject sendPCSPOSTrequest(String host, String path, String body, String authroization, String pcstoken) {
		HashMap<String,String> header = new HashMap<>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Authorization","Bearer "+authroization);
		header.put("Referer",pcstoken);
		header.put("Host", host);
		LogUtils.info("API Submit Deposit Request - "+path+" \n"+body);
		JSONObject headersMap = new JSONObject();

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			// 获取所有响应头
			Header[] headers = response.getAllHeaders();

			for (Header h : headers) {
				headersMap.put(h.getName(), h.getValue());
			}
			LogUtils.info("API Response Headers- "+path+" \n"+headersMap);
			return JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return headersMap;
	}

	public JSONObject sendPCSGETrequest(String host, String path, String authroization, String pcstoken) {
		HashMap<String,String> header = new HashMap<>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Authorization","Bearer "+authroization);
		header.put("Referer",pcstoken);
		header.put("Host", host);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			return JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject sendPCSPATCHrequest(String host, String path, String body, String authroization, String pcstoken) {
		HashMap<String,String> header = new HashMap<>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Authorization","Bearer "+authroization);
		header.put("Referer",pcstoken);
		header.put("Host", host);

		try {
			HttpResponse response = httpClient.getPatchResponse(path, header,body);
            return JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer getRandomDepositAmount(String cpsCode) {
		switch (cpsCode){
			case "T00600_033":
			case "T00600_123":
				return (int) (1500 + Math.random() * 100);
			default:
				return 50;
		}
	}

	//Init Deposit
	public JSONObject apiDPPCSInitDeposit(JSONObject accInfo, String pcsBrand, Integer depAmt) {
		String initdep_path = HyTechUrl.INIT_DEPOSIT;
		String initdep_body = "{\"mt4Account\":"+accInfo.getString("account")+",\"operateAmount\":"+depAmt+",\"feBrand\":\""+pcsBrand+"\",\"language\":\"en_US\",\"redemptionCode\":null,\"voucherApplied\":null}";
		HyTechUtils.genXSourceHeader(header);

		JSONObject initresult = sendCPAPIrequest(initdep_path, initdep_body);
		assertTrue(initresult.getInteger("code").equals(0) && initresult.getString("extendString").contains("successfully") ,"PCS - Init Deposit Failed!! \n"+initresult);
		return initresult;
	}

	//Checkout - Get Method Channel ID
	public Map<String, String> apiPCSChannelDetails(String cpsCode, String pcsUrl, String pcsHost, String pcstoken, String authorization) {
		String channelID_path =  pcsUrl + "/api/cashier/checkout?is_safari=false";
		JSONObject methodresult = sendPCSGETrequest(pcsHost, channelID_path, authorization, pcstoken);

		JSONArray jsonArray = methodresult.getJSONArray("channels");
		if (jsonArray != null){
			LogUtils.info("API Response - channels object: \n"+jsonArray.toString());

		}
		assertFalse(jsonArray.isEmpty(),"API Response - channels object is empty\n"+methodresult);

		Map<String, String> channelDetails = new HashMap<>();

		// Iterate through the JSON array to find the matching "cpsMethodCode"
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);

			if (obj.containsKey("cpsMethodCode") && obj.getString("cpsMethodCode").equals(cpsCode)) {
				channelDetails.put("channelId", obj.getString("channelId"));
				channelDetails.put("channelName", obj.getString("channelName"));
				return channelDetails;
			}
		}
		System.out.println("No matching cpsMethodCode found for "+ cpsCode);
		return null;
	}

	//Checkout - Choose Method
	public void apiPCSCheckout(JSONObject accInfo, Map<String, String> channelDetails, String pcsUrl, String pcsHost, String pcstoken, String authorization) {
		String checkout_path =  pcsUrl + "/api/cashier/checkout";
		String checkout_body = "{\"channel_id\":"+channelDetails.get("channelId")+"}";
		JSONObject checkoutresult = sendPCSPATCHrequest(pcsHost, checkout_path, checkout_body, authorization, pcstoken);
		String checkoutresData = checkoutresult.getString("mt_account");
		if(checkoutresData== null){
			throw new RuntimeException(checkout_path+" PCS - Checkout Choose Method failed!! \n" + checkoutresult);
		}
		assertTrue(checkoutresData.contains(accInfo.getString("account")), "PCS - Checkout Choose Method failed!! \n" + checkoutresult);
	}

	//Submit Deposit
	public void apiPCSSubmitDeposit(Map<String, String> channelDetails, String pcsUrl, String pcsHost, String pcstoken, String authorization) {
		String dep_path = pcsUrl + "/api/cashier/v1/deposit/cps";
		String dep_body = getPCSdepositBody(channelDetails.get("channelId"));

		JSONObject depresult = sendPCSPOSTrequest(pcsHost, dep_path, dep_body, authorization, pcstoken);
		LogUtils.info("apiPCSSubmitDeposit API Response - Submit Deposit: \n"+depresult.toJSONString());
		String resOrderID = depresult.getString("order_id");
		String paymentCont = depresult.getString("payment_content");
		assertTrue((resOrderID != null && !resOrderID.isEmpty()) && (paymentCont != null && !paymentCont.isEmpty()),
				"PCS - Submit Deposit Failed!! \n" + depresult);

	}
}
