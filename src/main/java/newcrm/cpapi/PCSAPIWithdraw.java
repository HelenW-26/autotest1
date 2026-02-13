package newcrm.cpapi;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.SkipException;
import utils.LogUtils;

import java.util.*;
import static org.testng.Assert.*;


public class PCSAPIWithdraw extends CPAPIWithdraw {

	public PCSAPIWithdraw(String url, String cplogin, String password) {
		super(url, cplogin, password);
		LogUtils.info("PCSAPIWithdraw \n:url:\n"+ url+ " \ncplogin:\n"+ cplogin+ "\n password:\n"+ password);
	}

	//根据渠道大类，获取对应出金渠道列表
	public Map<Integer, Map<String, String>> apiWDPCSChannelInfoByCategoryList(String acctNum, List<Integer> categoriesList) {
		HyTechUtils.genXSourceHeader(header);
//		String fullPath = this.url + "/hgw/rw/payment-api/cp/api/withdrawal/pcs/channel-info";
		String fullPath = this.url + HyTechUrl.WD_PCS_CHANINFO;

		JSONObject result = null;
		try {
			String channelBody = "{\"mt4Account\":" + acctNum + ",\"applyAmount\": 50,\"device\":\"WEB\"}";
			HttpResponse response = httpClient.getPostResponse(fullPath, header, channelBody);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
		}catch (Exception e) {
			e.printStackTrace();
		}

		JSONArray channelArray = result.getJSONObject("data").getJSONArray("channels");
		assertFalse(channelArray.isEmpty(),"API response - channels object is empty\n"+result);

		Map<Integer, Map<String, String>> categoryDataMap = new HashMap<>();

		for (Integer wdCategory : categoriesList) {
			boolean foundCategory = false;
			for (int i = 0; i < channelArray.size(); i++) {
				JSONObject obj = channelArray.getJSONObject(i);

				if (obj.getInteger("category").equals(wdCategory)) {
					Map<String, String> wdData = new HashMap<>();
					wdData.put("wdCategory", obj.getString("category"));
					wdData.put("wdCpsCode", obj.getString("payment_method"));
					wdData.put("wdtype", obj.getString("withdraw_type"));
					wdData.put("wdchannel", obj.getString("withdraw_channel"));
					wdData.put("wdcurrency", obj.getString("currency_number"));
					wdData.put("wdcurrencyCode", obj.getString("currency_code"));
					wdData.put("wdActcurrency", obj.getString("actual_currency_number"));
					wdData.put("wdActcurrencyCode", obj.getString("actual_currency_code"));

					if (wdCategory.equals(5)) {
						//Withdrawal method - bank code
						JSONObject bankDetails = JSON.parseObject(obj.getString("attach_variable")).getJSONObject("2");
						if (bankDetails.containsKey("field_content") && bankDetails.getJSONArray("field_content").size() > 0) {
							wdData.put("bankCode", bankDetails.getJSONArray("field_content")
									.getJSONObject(0)
									.getString("bank_code")
							);
						} else {
							wdData.put("bankCode", "Bank Name");
						}
					}
					categoryDataMap.put(wdCategory, wdData);
					foundCategory = true;
					break;
				}
			}
			if (!foundCategory) {
				System.out.println("*******Failed! Alert! No matching category withdrawal method found for "+ wdCategory +". Kindly check manually through CP.");
			}
		}
		return categoryDataMap;
	}

	//获取特定渠道的信息，根据cpsCode + category
	public Map<String, String> apiWDPCSChannelInfo(JSONObject accInfo, String cpsCode, Integer wdCategory, Integer wdID) throws Exception {
		HyTechUtils.genXSourceHeader(header);

		String fullPath = this.url +  HyTechUrl.WD_PCS_CHANINFO_V2;

		String channelBody = "{\"mt4Account\":" + accInfo.getString("account") + ",\"applyAmount\":50,\"device\":\"WEB\"}";

		HttpResponse response = httpClient.getPostResponse(fullPath, header, channelBody);

		JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
		LogUtils.info("apiWDPCSChannelInfo  response \n:url:\n"+ fullPath+ " \nheader:\n"+ header+ "\nbody:\n"+ channelBody+ "\nresponse:\n"+ result.toString());

        boolean isLbtLimited = result.getJSONObject("data").getBooleanValue("isLbtLimited");
        String lbtBalance = result.getJSONObject("data").getString("lbtBalance");
        JSONArray jsonArray = result.getJSONObject("data").getJSONObject("channel").getJSONArray("channels");
		assertFalse(jsonArray.isEmpty(),"API response - channels object is empty\n"+result);

		Map<String, String> channelDetails = new HashMap<>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("cps");

			if (obj!=null&&obj.getString("payment_method").equals(cpsCode) && obj.getInteger("category").equals(wdCategory) && obj.getInteger("withdraw_type").equals(wdID)) {
				channelDetails.put("wdCpsCode", obj.getString("payment_method"));
				channelDetails.put("wdtype", obj.getString("withdraw_type"));
				channelDetails.put("wdchannel", obj.getString("withdraw_channel"));
				channelDetails.put("wdcurrency", obj.getString("currency_number"));
				channelDetails.put("wdcurrencyCode", obj.getString("currency_code"));
				channelDetails.put("wdActcurrency", obj.getString("actual_currency_number"));
				channelDetails.put("wdActcurrencyCode", obj.getString("actual_currency_code"));
				channelDetails.put("channelMerchantId", obj.getString("channel_merchant_id"));
				channelDetails.put("isLbtLimited", String.valueOf(isLbtLimited));
				channelDetails.put("lbtBalance", String.valueOf(lbtBalance));

				//Withdrawal method name
				String merchantVariableStr = obj.getString("merchant_variable");
				JSONObject merchantVariableJson = JSONObject.parseObject(merchantVariableStr);
				channelDetails.put("wdchannelTitle",  merchantVariableJson.getString("description_title"));

				//Withdrawal method - bank code
				if (wdCategory == 5) {
					JSONObject bankDetails = JSON.parseObject(obj.getString("attach_variable")).getJSONObject("2");

					if (bankDetails.containsKey("field_content") && bankDetails.getJSONArray("field_content").size() > 0) {
						channelDetails.put("bankCode", bankDetails.getJSONArray("field_content")
								.getJSONObject(0)
								.getString("bank_code")
						);
					} else {
						channelDetails.put("bankCode", "Bank Name"); // Default if "field_content" is missing or empty
					}
				}
			}

		}

        return channelDetails;
    }
	//LBT合单需求调整接口，仅适用于CP，IB无更改
	public Map<String, String> apiWDPCSChannelInfoV2(JSONObject accInfo, String cpsCode, Integer wdCategory, Integer wdID) throws Exception {
		HyTechUtils.genXSourceHeader(header);

		String fullPath = this.url +  HyTechUrl.WD_PCS_CHANINFO_V2;

		String channelBody = "{\"mt4Account\":" + accInfo.getString("account") + ",\"applyAmount\":50,\"device\":\"WEB\"}";

		HttpResponse response = httpClient.getPostResponse(fullPath, header, channelBody);

		JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
		LogUtils.info("apiWDPCSChannelInfo  response \n:url:\n"+ fullPath+ " \nheader:\n"+ header+ "\nbody:\n"+ channelBody+ "\nresponse:\n"+ result.toString());


		JSONArray jsonArray = result.getJSONObject("data").getJSONObject("channel").getJSONArray("channels");
		assertFalse(jsonArray.isEmpty(),"API response - channels object is empty\n"+result);

		Map<String, String> channelDetails = new HashMap<>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("cps");
			if(obj!=null){
				LogUtils.info("apiWDPCSChannelInfoV2  response \n:url:\n"+ fullPath+ " \nheader:\n"+ header+ "\nbody:\n"+ channelBody+ "\nresponse:\n"+ obj.toString());

			}

			if (obj!=null&&obj.getString("payment_method").equals(cpsCode) && obj.getInteger("category").equals(wdCategory)
					&& obj.getInteger("withdraw_type").equals(wdID)) {
				channelDetails.put("wdCpsCode", obj.getString("payment_method"));
				channelDetails.put("wdtype", obj.getString("withdraw_type"));
				channelDetails.put("wdchannel", obj.getString("withdraw_channel"));
				channelDetails.put("wdcurrency", obj.getString("currency_number"));
				channelDetails.put("wdcurrencyCode", obj.getString("currency_code"));
				channelDetails.put("wdActcurrency", obj.getString("actual_currency_number"));
				channelDetails.put("wdActcurrencyCode", obj.getString("actual_currency_code"));
				channelDetails.put("channelMerchantId", obj.getString("channel_merchant_id"));

				//Withdrawal method name
				String merchantVariableStr = obj.getString("merchant_variable");
				JSONObject merchantVariableJson = JSONObject.parseObject(merchantVariableStr);
				channelDetails.put("wdchannelTitle",  merchantVariableJson.getString("description_title"));

				//Withdrawal method - bank code
				if (wdCategory == 5) {
					JSONObject bankDetails = JSON.parseObject(obj.getString("attach_variable")).getJSONObject("2");

					if (bankDetails.containsKey("field_content") && bankDetails.getJSONArray("field_content").size() > 0) {
						channelDetails.put("bankCode", bankDetails.getJSONArray("field_content")
								.getJSONObject(0)
								.getString("bank_code")
						);
					} else {
						channelDetails.put("bankCode", "Bank Name"); // Default if "field_content" is missing or empty
					}
				}
			}

		}

		return channelDetails;
	}

	public JSONObject apiWDPCSSortInfo(JSONObject accInfo) throws Exception {
		JSONArray jsonArray = accInfo.getJSONObject("data").getJSONArray("logins");
		Integer userID = jsonArray.getJSONObject(0).getInteger("login");

		String fullPath = this.url + HyTechUrl.WD_PCS_SORTINFO;
		header.put("Content-Type", "application/json");
		HyTechUtils.genXSourceHeader(header);
		JSONObject body = new JSONObject();
		body.put("applyAmount", String.valueOf(40));
		body.put("device", "WEB");
		body.put("mt4Account", String.valueOf(userID));

		JSONObject result = sendCPAPIrequest(HyTechUrl.WD_PCS_SORTINFO, String.valueOf(body));
		printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
		Integer resCode = result.getInteger("code");
		JSONArray resData = result.getJSONArray("data");
		assertTrue(resCode.equals(0) && (resData != null && !resData.isEmpty()), "Get Withdrawal Sort-Info failed!!");
		return result;
	}

	//获取每个币种的最低和最高出金额
	public void apiWDPCSCurrLimit(JSONObject accInfo) throws Exception {
		JSONArray jsonArray = accInfo.getJSONObject("data").getJSONArray("logins");
		String userID = jsonArray.getJSONObject(0).getString("userId");

		String fullPath = this.url + HyTechUrl.WD_PCS_CURRENCYLIMIT;
		header.put("Content-Type", "application/json");
		HyTechUtils.genXSourceHeader(header);
		JSONObject body = new JSONObject();
		body.put("device", "web");
		body.put("userId", userID);

		HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
		JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
		printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
		Integer resCode = result.getInteger("code");
		String resData = result.getString(("data"));
		assertTrue(resCode.equals(0) && resData != null, "Get WD PCS Currency Limit failed!!");
	}

	//获取中台开关情况
	public void apiWDPCSEnabled(){
		HyTechUtils.genXSourceHeader(header);
		String fullPath = this.url + HyTechUrl.WD_PCS_ENABLED;
		JSONObject body = new JSONObject();
		JSONObject result = sendCPAPIGETrequest(HyTechUrl.WD_PCS_ENABLED);
		printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));

		Integer resCode = result.getInteger("code");
		String resData = result.getString(("data"));
		assertTrue(resCode.equals(0) && resData.equalsIgnoreCase("true") ||resData.equalsIgnoreCase("false"), "Get Withdrawal PCS Enabled failed!!");
	}
}

