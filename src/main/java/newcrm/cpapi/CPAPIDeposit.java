package newcrm.cpapi;

import static org.testng.Assert.assertTrue;

import com.alibaba.fastjson.JSONArray;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class CPAPIDeposit extends CPAPIPaymentBase{

	HashMap<String, String> body = new HashMap<>();

	//构造函数
	public CPAPIDeposit(String url, String cplogin, String password){
		super(url, cplogin, password);
		apiAntiReuseToken();
	}

	public void apiSubmitDeposit(String dpbody) throws Exception{
		String dppath = "web-api/cp/api/deposit/cps/deposit";
		HyTechUtils.genXSourceHeader(header);

		try {
			apiAntiReuseToken();
			JSONObject result = sendCPAPIrequest(dppath, dpbody);
			Integer rescode = result.getInteger("code");
			assertTrue(rescode.equals(0) &&  result.get("data") != null && result.get("extendString") != null,"Non-PCS : Submit Deposit Failed!! \n"+result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getChannelDetails(JSONObject result, String cpsCode) {
		String dataString = result.getString("data");
		JSONObject dataJson = JSONObject.parseObject(dataString);
		JSONArray jsonArray = dataJson.getJSONArray("channels");
		assertFalse(jsonArray.isEmpty(),"API response - channels object is empty");

		Map<String, String> channelDetails = new HashMap<>();

		// Iterate through the JSON array to find the matching "payment_method"
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);

			if (obj.containsKey("payment_method") && obj.getString("payment_method").equals(cpsCode)) {
				channelDetails.put("channelName", obj.getString("name"));
				return channelDetails;
			}
		}

		System.out.println("No matching cpsMethodCode found for " + cpsCode);
		return null;
	}

	public void apiDPTransHist() throws Exception {
		String fullPath = this.url + HyTechUrl.DP_TRANS_HISTORY;
		header.put("Content-Type","application/x-www-form-urlencoded");
		HyTechUtils.genXSourceHeader(header);
		body.put("pageNo","1");
		body.put("page25","25");

		HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
		JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
		printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
		Integer resCode = result.getInteger("code");
		String resData = result.getString("data");
		assertTrue(resCode.equals(0) && resData != null,"Get Deposit Transaction History failed!!");
	}

	@Test
	public void apiDPRestrictDepoChannel() throws Exception {
		String fullPath = this.url + HyTechUrl.DP_GET_RESTRICT_DATA;
		header.put("Content-Type","application/json");
		HyTechUtils.genXSourceHeader(header);

		HttpResponse response = httpClient.getPostResponse(fullPath, header,"");
		JSONArray result = JSON.parseArray(EntityUtils.toString(response.getEntity(),"UTF-8"));
		printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
		assertTrue( result != null && !result.isEmpty(),"Get restricted deposit channels failed!!");
	}
}
