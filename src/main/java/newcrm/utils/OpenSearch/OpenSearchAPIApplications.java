package newcrm.utils.OpenSearch;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.*;



public class OpenSearchAPIApplications extends APIBase{

	private static final String url = "https://devtools.crm-prod.com/";
	private static final String user = "monitorbot";
	private static final String password = "K8AU8CXPF_585RV7DpwwUKf";
	public static String alert_msg = "";
	
	public OpenSearchAPIApplications() {
		super(url, user, password);
		
	}
	
	//query CPS P9999 and P1010 among specific time range
	public JSONObject queryCPSP9999AndP1010(String time){
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		String jsonData = "{ \"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": \"now-"+ time +"\",\"lte\": \"now\"}}},{\"match\": {\"Message\": {\"query\": \"response_code P1010 P9999\",\"minimum_should_match\": \"2\"}}}]}}," +
                " \"_source\": [\"Event_id\"],\"size\": 100}";

		JSONObject response = api.sendGETAPIrequest("_search?filter_path=hits.hits._source",jsonData); 
		return response;
	}
	
	// Method to parse Event_id from JSON response and put them in an array
    public List<String> extractEventIds(JSONObject jsonResponse) {
        List<String> eventIds = new ArrayList<>();
        try {
	        // Parse the JSON response
	        JSONArray hitsArray = jsonResponse.getJSONObject("hits").getJSONArray("hits");
	
	        // Iterate over the hits array to extract Event_id values
	        for (int i = 0; i < hitsArray.size(); i++) {
	            JSONObject hit = hitsArray.getJSONObject(i);
	            JSONObject source = hit.getJSONObject("_source");
	            String eventId = source.getString("Event_id");
	            eventIds.add(eventId);
	        }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No ERROR log detected or exception happened in extractEventIds.");
            return null;
        }
        return eventIds;
    }	
	
	//query CPS detail according to Event_id
	public JSONObject queryCPSDetail(String e_id, String time){
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		String jsonData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": \"now-"+ time
				+"\",\"lte\": \"now\"}}},{\"match_phrase\": {\"Event_id\": \""+ e_id 
				+"\"}},{\"match\": {\"Message\": {\"query\": \"cpsCode referer response_code order_id\","
				+ "\"minimum_should_match\": \"1\"}}}]}},\"_source\": [\"Event_id\",\"Message\",\"@log_name\"]}";

		JSONObject response = api.sendGETAPIrequest("_search?filter_path=hits.hits._source",jsonData); 
		return response;
	}
	
	
    //Extract CSP detailed channel info from JSON response
    public JSONObject extractCPSChannelInfoFromJson(JSONObject data) {
        JSONObject result = new JSONObject();
        String orderNumber = "",cpsCode = "",mt4Account="";
        
        //try {
	        JSONObject hitsObject = data.getJSONObject("hits");
	        JSONArray hitsArray = hitsObject.getJSONArray("hits");
	
	        for (int i = 0; i < hitsArray.size(); i++) {
	            JSONObject hit = hitsArray.getJSONObject(i);
	            JSONObject source = hit.getJSONObject("_source");
	            String message = source.getString("Message");
	
	            result.put("Event_id", source.getString("Event_id"));
	            result.put("Brand", getSubstringBeforeFirstUnderscore(source.getString("@log_name")).toUpperCase());
	            
	            if (message.contains("referer")) {
	                String url = extractValue(message, "referer", "=", ",");
	                result.put("url", url);
	            }
	            if (message.contains("cpsCode")) {
	                cpsCode = extractValue(message, "cpsCode", "\":", ",");
	                mt4Account = extractValue(message, "mt4Account", "\":", ",");
	                result.put("cpsCode", cpsCode);
	                result.put("mt4Account", mt4Account);
	            }
	            if (message.contains("response_code")) {
	                String response_code = extractValue(message, "response_code", "\":", ",");
	                result.put("response_code", response_code);
	            }
	            if (message.contains("order_id")) {
	                orderNumber = extractValue(message, "order_id", "\":", ",");
	                result.put("orderNumber", orderNumber);
	            }

	
	        }
       /* } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception happened in extractCPSChannelInfoFromJson.");
            return null;
        } */ 
        return result;
    }
    
    public static String getSubstringBeforeFirstUnderscore(String str) {
        int index = str.indexOf('_');
        if (index != -1) {
            return str.substring(0, index);
        } else {
            return str; // Return the whole string if no underscore is found
        }
    }
    
    
    //Extract value by delimiter
    private static String extractValue(String message, String key, String startDelimiter, String endDelimiter) {
        int keyIndex = message.indexOf(key + startDelimiter);
        if (keyIndex != -1) {
            int startIndex = keyIndex + key.length() + startDelimiter.length();
            int endIndex = message.indexOf(endDelimiter, startIndex);
            if (endIndex == -1) {
                endIndex = message.length();
            }
            return message.substring(startIndex, endIndex).trim();
        }
        return "";
    }
    
    //Count for the most frequent occurrence
    public int processJsonArray(JSONArray jsonArray) {
        JSONObject countMap = new JSONObject();
        JSONArray sameCodeMsg = new JSONArray();
        //System.out.println(jsonArray.toString());
        // Count occurrences of each value of cpsCode
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String valueOfcpsCode = jsonObject.getString("cpsCode");
            if (countMap.containsKey(String.valueOf(valueOfcpsCode))) {
                countMap.put(String.valueOf(valueOfcpsCode), countMap.getIntValue(valueOfcpsCode) + 1);
            } else {
                countMap.put(String.valueOf(valueOfcpsCode), 1);
            }
        }

        // Find the value of the same cpsCode that appears the most
        int maxCount = 0;
        String mostFrequentCpsCode = "";
        for (String key : countMap.keySet()) {
            int count = countMap.getIntValue(key);
            if (count > maxCount) {
                maxCount = count;
                mostFrequentCpsCode = key;
            }
        }

        // Print all JSONObject with the most frequent value of cpsCode
        alert_msg = alert_msg + "Most frequent failed CPS channel: " + mostFrequentCpsCode;
        alert_msg = alert_msg + "\n\nFailed count = " + maxCount + "\n";

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("cpsCode").trim().equalsIgnoreCase(mostFrequentCpsCode)) {
                sameCodeMsg.add(jsonObject);
            }
        }
        System.out.println(sameCodeMsg);
        
        //Print only the first two in same brands      
        // Array to store brand data
        JSONArray brandObjects = new JSONArray();
        //Array to store brand and mtAccount counts
        JSONArray brandArray = new JSONArray();

        for (int i = 0; i < sameCodeMsg.size(); i++) {
            JSONObject obj = sameCodeMsg.getJSONObject(i);
            String brand = obj.getString("Brand");
            String account = obj.getString("mt4Account");

            boolean brandExists = false;
            for (int j = 0; j < brandArray.size(); j++) {
                JSONObject brandObj = brandArray.getJSONObject(j);
                if (brandObj.getString("Brand").equals(brand)) {
                    JSONArray accounts = brandObj.getJSONArray("accounts");
                    boolean accountExists = false;
                    for (int k = 0; k < accounts.size(); k++) {
                        JSONObject accountObj = accounts.getJSONObject(k);
                        if (accountObj.getString("account").equals(account)) {
                            accountObj.put("count", accountObj.getIntValue("count") + 1);
                            brandObj.put("Total", brandObj.getIntValue("Total") + 1);
                            accountExists = true;
                            break;
                        }
                    }
                    if (!accountExists) {
                        JSONObject newAccount = new JSONObject();
                        newAccount.put("account", account);
                        newAccount.put("count", 1);
                        brandObj.put("Total", brandObj.getIntValue("Total") + 1);
                        accounts.add(newAccount);
                        //Add the first record of each account to Teams notification
                        brandObjects.add(obj);
                    }
                    	
                    brandExists = true;
                    break;
                }
            }
            if (!brandExists) {
                JSONObject newBrand = new JSONObject();
                newBrand.put("Brand", brand);
                newBrand.put("Total", 1);
                JSONArray accounts = new JSONArray();
                JSONObject newAccount = new JSONObject();
                newAccount.put("account", account);
                newAccount.put("count", 1);
                accounts.add(newAccount);
                newBrand.put("accounts", accounts);
                brandArray.add(newBrand);
              //Add the first record of each account to Teams notification
                brandObjects.add(obj);
            }

        }

        // Print the result
        for (int i = 0; i < brandArray.size(); i++) {
            JSONObject brandObj = brandArray.getJSONObject(i);
            String brand = brandObj.getString("Brand");
            String total = brandObj.getString("Total");
            JSONArray accounts = brandObj.getJSONArray("accounts");

            alert_msg = alert_msg + "\n✦ Brand " + brand + "(total "+ total +"):";
            for (int j = 0; j < accounts.size(); j++) {
                JSONObject accountObj = accounts.getJSONObject(j);
                String accDetail = "  Account " + accountObj.getString("account") + ": " + accountObj.getIntValue("count");
                //System.out.println(accDetail);
                alert_msg = alert_msg + accDetail;
            }
        }        
        
        alert_msg = alert_msg + "\n\n++++++++++  Detail  +++++++++++";
        alert_msg = alert_msg + brandObjects.toString().replace("\\\"", "");
        System.out.println(alert_msg);
        
		return maxCount;
    }
    

	//query CPS unionpay & crypto withdraw error among specific time range
	public JSONObject queryCPSWithdrawError(String time){
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		String jsonData = "{\"query\":{\"bool\":{\"must\":[{\"match_phrase\":"
				+ "{\"Message\":\"%E4%BA%A4%E6%98%93%E5%A4%B1%E8%B4%A5\"}},"
				+ "{\"match\":{\"Class\":\"c.t.u.r.RestTemplateLoggingInterceptor\"}}],"
				+ "\"filter\":[{\"range\":{\"@timestamp\":{\"gte\":\"now-"+ time +"\",\"lte\":\"now\"}}}]}},"
				+ "\"_source\":[\"Event_id\"],\"size\": 100}";

		JSONObject response = api.sendGETAPIrequest("_search?filter_path=hits.hits._source",jsonData); 
		return response;
	}
    
	//query CPS unionpay & crypto withdraw detail according to Event_id
	public JSONObject queryWithdrawDetail(String e_id, String time){
		OpenSearchAPIApplications api = new OpenSearchAPIApplications();
		String jsonData = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"@timestamp\":{\"gte\":\"now-"+ time +"\","
				+ "\"lte\":\"now\"}}},{\"match_phrase\":{\"Event_id\":\""+ e_id +"\"}},"
						+ "{\"match\":{\"Message\":{\"query\":\"merOrderNo order_id transData payload\","
						+ "\"minimum_should_match\":\"2\"}}}]}},\"_source\":[\"Event_id\",\"Message\",\"@log_name\"],\"size\": 100}";

		JSONObject response = api.sendGETAPIrequest("_search?filter_path=hits.hits._source",jsonData); 
		return response;
	}
	
    //Extract CSP detailed channel info from JSON response
    public JSONObject extractWithdrawOrderInfoFromJson(JSONObject data) {
        JSONObject result = new JSONObject();
        String orderNumber = "",channelInfo = "";
        
        //try {
	        JSONObject hitsObject = data.getJSONObject("hits");
	        JSONArray hitsArray = hitsObject.getJSONArray("hits");
	
	        for (int i = 0; i < hitsArray.size(); i++) {
	            JSONObject hit = hitsArray.getJSONObject(i);
	            JSONObject source = hit.getJSONObject("_source");
	            String message = source.getString("Message");
	
	            result.put("Event_id", source.getString("Event_id"));
	            result.put("Brand", getSubstringBeforeFirstUnderscore(source.getString("@log_name")).toUpperCase());
	           	            
	            if (message.contains("merOrderNo")) {
	                String response_code = extractValue(message, "merOrderNo", "\":", ",");
	                channelInfo = extractValue(message, "currency", "\":", ",");
	                result.put("orderNumber", response_code);
	                result.put("channelInfo", channelInfo);
	            }else if (message.contains("order_id")) {
	                orderNumber = extractValue(message, "order_id", "\":", ",");
	                channelInfo = extractValue(message, "profiles", "\":", ",");
	                result.put("orderNumber", orderNumber);
	                if (channelInfo.replace("\"", "").equalsIgnoreCase("withdrawal"))
	                	channelInfo = extractValue(message, "actual_currency", "\":", ",");
	                result.put("channelInfo", channelInfo);
	            }

	
	        }
       /* } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception happened in extractCPSChannelInfoFromJson.");
            return null;
        } */ 
        return result;
    }
    
    //Count for the most frequent occurrence
    public int processWDJsonArray(JSONArray jsonArray) {
        Integer maxCount = jsonArray.size();
            
        // Array to store brand data
        JSONArray brandObjects = new JSONArray();
        //Array to store brand and ordernumbers
        JSONArray brandArray = new JSONArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String brand = obj.getString("Brand");
            String orderNumber = obj.getString("orderNumber");

            boolean brandExists = false;
            for (int j = 0; j < brandArray.size(); j++) {
                JSONObject brandObj = brandArray.getJSONObject(j);
                if (brandObj.getString("Brand").equals(brand)) {
                    JSONArray accounts = brandObj.getJSONArray("orderNumbers");
                    boolean accountExists = false;
                    for (int k = 0; k < accounts.size(); k++) {
                        JSONObject accountObj = accounts.getJSONObject(k);
                        if (accountObj.getString("orderNumber").equals(orderNumber)) {
                            accountObj.put("count", accountObj.getIntValue("count") + 1);
                            brandObj.put("Total", brandObj.getIntValue("Total") + 1);
                            accountExists = true;
                            break;
                        }
                    }
                    if (!accountExists) {
                        JSONObject newAccount = new JSONObject();
                        newAccount.put("orderNumber", orderNumber);
                        newAccount.put("count", 1);
                        brandObj.put("Total", brandObj.getIntValue("Total") + 1);
                        accounts.add(newAccount);
                        //Add the first record of each account to Teams notification
                        brandObjects.add(obj);
                    }
                    	
                    brandExists = true;
                    break;
                }
            }
            if (!brandExists) {
                JSONObject newBrand = new JSONObject();
                newBrand.put("Brand", brand);
                newBrand.put("Total", 1);
                JSONArray accounts = new JSONArray();
                JSONObject newAccount = new JSONObject();
                newAccount.put("orderNumber", orderNumber);
                newAccount.put("count", 1);
                accounts.add(newAccount);
                newBrand.put("orderNumbers", accounts);
                brandArray.add(newBrand);
              //Add the first record of each account to Teams notification
                brandObjects.add(obj);
            }

        }

        // Print the result
        for (int i = 0; i < brandArray.size(); i++) {
            JSONObject brandObj = brandArray.getJSONObject(i);
            String brand = brandObj.getString("Brand");
            String total = brandObj.getString("Total");
            JSONArray accounts = brandObj.getJSONArray("orderNumbers");

            alert_msg = alert_msg + "\n✦ Brand " + brand + "(total "+ total +"):";
            for (int j = 0; j < accounts.size(); j++) {
                JSONObject accountObj = accounts.getJSONObject(j);
                String accDetail = "  orderNumber  " + accountObj.getString("orderNumber") + ": " + accountObj.getIntValue("count");
                //System.out.println(accDetail);
                alert_msg = alert_msg + accDetail;
            }
        }        
        
        alert_msg = alert_msg + "\n\n++++++++++  Detail  +++++++++++";
        alert_msg = alert_msg + brandObjects.toString().replace("\\\"", "");
        System.out.println(alert_msg);
        
		return maxCount;
    }
    
	public static void main(String args[]) throws Exception {
		/*
		 * Map<String, Integer> brandCount = new HashMap<>(); String data =
		 * "[{\"Brand\":\"AU\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721203741\\\"\",\"Event_id\":\"[c02082c6-5c9b-4b6d-8831-c4b7e5ae38cf]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"},{\"Brand\":\"VT\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721203900\\\"\",\"Event_id\":\"[10a52feb-5722-4a10-8888-663dbf32dad5]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"},{\"Brand\":\"AU\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721195404\\\"\",\"Event_id\":\"[fffbdcac-17d8-462d-9782-07d49e0350b2]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"}]";
		 * JSONArray brandObjects = (JSONArray) JSONArray.parseArray(data);
		 * brandCount.put("AU", 2); brandCount.put("VT", 1);
		 * 
		 * Map<String, BrandData> brandMap = new HashMap<>(); for (int i = 0; i <
		 * brandObjects.size(); i++) { String account =
		 * brandObjects.getJSONObject(i).getString("mt4Account");
		 * 
		 * if (!mtAccCount.containsKey(account)) { mtAccCount.put(account, 1); }else {
		 * mtAccCount.put(account, mtAccCount.get(account) + 1); } }
		 * 
		 * // Save the brand counts to a String variable StringBuilder brandInfo = new
		 * StringBuilder("Brand: "); Iterator<Map.Entry<String, Integer>> iterator =
		 * brandCount.entrySet().iterator(); while (iterator.hasNext()) {
		 * Map.Entry<String, Integer> entry = iterator.next();
		 * brandInfo.append(entry.getKey()).append(" ").append(entry.getValue()); if
		 * (iterator.hasNext()) { brandInfo.append(", "); } } alert_msg = alert_msg +
		 * brandInfo; alert_msg = alert_msg +
		 * "\n\n\n++++++++++  Detail  +++++++++++\n\n"; alert_msg = alert_msg +
		 * brandObjects.toString().replace("\\\"", ""); System.out.println(alert_msg);
		 */
		String data ="[{\"Brand\":\"AU\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721203741\\\"\",\"Event_id\":\"[c02082c6-5c9b-4b6d-8831-c4b7e5ae38cf]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"},{\"Brand\":\"VT\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721203900\\\"\",\"Event_id\":\"[10a52feb-5722-4a10-8888-663dbf32dad5]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"},{\"Brand\":\"AU\",\"mt4Account\":\"\\\"6602901\\\"\",\"response_code\":\"\\\"P9999\\\"\",\"orderNumber\":\"\\\"V2U660290120240721195404\\\"\",\"Event_id\":\"[fffbdcac-17d8-462d-9782-07d49e0350b2]\",\"url\":\"[https://secure.vantagemarkets.com/deposit/cps/UPI]\",\"cpsCode\":\"\\\"T00312\\\"\"}]";
		JSONArray jsonArray = (JSONArray) JSONArray.parseArray(data);

		// Array to store brand data
        JSONArray brandArray = new JSONArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String brand = obj.getString("Brand");
            String account = obj.getString("mt4Account");

            boolean brandExists = false;
            for (int j = 0; j < brandArray.size(); j++) {
                JSONObject brandObj = brandArray.getJSONObject(j);
                if (brandObj.getString("Brand").equals(brand)) {
                    JSONArray accounts = brandObj.getJSONArray("accounts");
                    boolean accountExists = false;
                    for (int k = 0; k < accounts.size(); k++) {
                        JSONObject accountObj = accounts.getJSONObject(k);
                        if (accountObj.getString("account").equals(account)) {
                            accountObj.put("count", accountObj.getIntValue("count") + 1);
                            accountExists = true;
                            break;
                        }
                    }
                    if (!accountExists) {
                        JSONObject newAccount = new JSONObject();
                        newAccount.put("account", account);
                        newAccount.put("count", 1);
                        accounts.add(newAccount);
                    }
                    brandExists = true;
                    break;
                }
            }
            if (!brandExists) {
                JSONObject newBrand = new JSONObject();
                newBrand.put("Brand", brand);
                JSONArray accounts = new JSONArray();
                JSONObject newAccount = new JSONObject();
                newAccount.put("account", account);
                newAccount.put("count", 1);
                accounts.add(newAccount);
                newBrand.put("accounts", accounts);
                brandArray.add(newBrand);
            }
        }

        // Print the result
        for (int i = 0; i < brandArray.size(); i++) {
            JSONObject brandObj = brandArray.getJSONObject(i);
            String brand = brandObj.getString("Brand");
            JSONArray accounts = brandObj.getJSONArray("accounts");

            System.out.println("Brand " + brand + ":");

            for (int j = 0; j < accounts.size(); j++) {
                JSONObject accountObj = accounts.getJSONObject(j);
                System.out.println("  Account " + accountObj.getString("account") + ": " + accountObj.getIntValue("count"));
            }
        }     
    
	}
}
