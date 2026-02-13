package newcrm.utils.OpenSearch;

import java.util.HashMap;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.*;

import newcrm.utils.http.CRMHttpClient;
import vantagecrm.Utils;

public class APIBase {

	protected CRMHttpClient httpClient = new CRMHttpClient();
	protected HashMap<String,String> header;
	protected String url ;
	protected String user;
	protected String password;

	public APIBase(String url, String user, String password) {
		this.url = Utils.ParseInputURL(url);
		this.user = user;
		this.password = password;
		String auth = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        header = new HashMap<>();
        header.put("Authorization", authHeader);
        header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
	}			
	
	/**
	 * Send OpenSearch POST api request
	 * @param String url path(should not start with /), String request body
	 * @return json response
	 */
    public JSONObject sendPOSTAPIrequest(String path, String body) {
		String fullPath = this.url + path;
		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send OpenSearch GET api request
	 * @param String url path(should not start with /), String request body
	 * @return json response
	 */
	public JSONObject sendGETAPIrequest(String path, String jsonData) {
		String fullPath = this.url + path;
		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getGetResponse(fullPath, header, jsonData);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static void main(String args[]) throws Exception {
	
		String url = "https://devtools.crm-prod.com/"; 
		String user = "monitorbot"; 
		String password = "K8AU8CXPF_585RV7DpwwUKf"; 
		String jsonData = "{ \"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": \"now-2h\",\"lte\": \"now\"}}},{\"match\": {\"Message\": {\"query\": \"response_code P1010 P9999\",\"minimum_should_match\": \"2\"}}}]}}," +
                " \"_source\": [\"Event_id\"]}";
		APIBase api = new APIBase(url,user,password);
		System.out.println(api.sendGETAPIrequest("_search?filter_path=hits.hits._source",jsonData)); 
 
	}
}

