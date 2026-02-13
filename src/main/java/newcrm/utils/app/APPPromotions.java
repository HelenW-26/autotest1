package newcrm.utils.app;

import com.alibaba.fastjson.JSONObject;

public class APPPromotions extends APIBase {

	/**
	 * 
	 * @param host host app service url
	 * @param regulator regulator
	 * @param brand brand
	 */
	public APPPromotions(String host, String regulator, String brand) {
		super(host, "", regulator, brand);
		// TODO Auto-generated constructor stub
	}
	
	
	public String GetRAFCode(String userId,Integer campaignId) {
		this.uri = "/campaign/campaign-code";
		JSONObject obj = new JSONObject();
		
		obj.put("userId", userId);
		obj.put("campaignId", ""+campaignId);
		
		String response = this.sendGet(obj);
		return this.getResponse(response);
	}
}
