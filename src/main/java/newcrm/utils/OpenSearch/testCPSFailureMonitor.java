package newcrm.utils.OpenSearch;

import java.util.List;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties;
import newcrm.utils.Bot.LarkNotifier;


public class testCPSFailureMonitor {

	@Test
	@Parameters(value= {"timeRange", "threshold"})
	public void testCPSMonitorFailedDPChannels(String timeRange, Integer threshold) throws Exception {
		LarkNotifier larkNT = new LarkNotifier();
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		api.alert_msg = "";
		
		JSONObject response = api.queryCPSP9999AndP1010(timeRange);
		try {
			List<String> eventIds = api.extractEventIds(response);
			
			JSONArray queryData = new JSONArray();
			
			for (int i = 0; i < eventIds.size(); i++) {
				JSONObject msg = api.queryCPSDetail(eventIds.get(i), timeRange);
				queryData.add(api.extractCPSChannelInfoFromJson(msg));
			}
			
			//When the queried result count is greater than threshold, send notification.
			api.alert_msg = "++++++++++ CPS入金掉单Data Analysis - Past "+ timeRange +" ++++++++++\n\n" + api.alert_msg;
			if(api.processJsonArray(queryData) > threshold) {
				//System.out.println(api.alert_msg);
				larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_Lark_CPS_Monitor_webhookUrl,api.alert_msg);
				larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_Lark_RTS_CPS_Monitor_webhookUrl,api.alert_msg);
			}
			
	    } catch (Exception e) { 
	    	System.out.println("No ERROR log detected."); 
	    }
			 
	}
	

	@Test
	@Parameters(value= {"timeRange", "threshold"})
	public void testCPSMonitorFailedWDChannels(String timeRange, Integer threshold) throws Exception {
		LarkNotifier larkNT = new LarkNotifier();
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		api.alert_msg = "";
		
		JSONObject response = api.queryCPSWithdrawError(timeRange);
		try {
			List<String> eventIds = api.extractEventIds(response);
			
			JSONArray queryData = new JSONArray();
			
			for (int i = 0; i < eventIds.size(); i++) {
				JSONObject msg = api.queryWithdrawDetail(eventIds.get(i), timeRange);
				queryData.add(api.extractWithdrawOrderInfoFromJson(msg));
			}
			
			//When the queried result count is greater than threshold, send notification.
			api.alert_msg = "+++++ 出金下发失败记录 - Past "+ timeRange +" +++++\n" + api.alert_msg;
			System.out.println(queryData);
			
			if(api.processWDJsonArray(queryData) > threshold) {
			  larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_Lark_RTS_CPS_Monitor_webhookUrl,api.alert_msg); 
			}
			 
			
	    } catch (Exception e) { 
	    	System.out.println("No ERROR log detected."); 
	    }
			 
	}
}
