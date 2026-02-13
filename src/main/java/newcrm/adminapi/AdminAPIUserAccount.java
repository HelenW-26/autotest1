package newcrm.adminapi;

import com.alibaba.fastjson.*;
import com.google.gson.JsonObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.AlphaServerEnv;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

public class AdminAPIUserAccount extends AdminAPI{

	public AdminAPIUserAccount(String url, REGULATOR regulator, String adminUser, String password, BRAND brand, ENV testEnv) {
		super(url, regulator, adminUser, password, brand, testEnv);
	}

	//filter with name = autotest (default)
	public JSONObject apiTradingAcctSearch (String acctNum, String email, PLATFORM platform, String userId, String userName, String serverList) {
		JSONArray serverArray = null;

		// Get servers available
		if (serverList.isEmpty()) {
			List<String> servers = GlobalMethods.getTestServers(platform, testEnv, brand);
			serverArray = new JSONArray();
			serverArray.addAll(servers);
		}

		String path = this.url + "/account/query_accountList";

		JSONObject body = new JSONObject();
		body.put("skipCount", true); // set false to get all records
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("userId", new JSONObject().fluentPut("fuzzy", true).fluentPut("filterType", "INPUT").fluentPut("input", userId));
		parameters.put("real_name", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", userName.isEmpty() ? "autotest" : userName));
		parameters.put("is_archive", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input", "0"));
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		parameters.put("mt4Account", new JSONObject().fluentPut("fuzzy", true).fluentPut("filterType", "INPUT").fluentPut("input", acctNum));
		parameters.put("tb_user.email", new JSONObject().fluentPut("filterType", "INPUT").fluentPut("input", email));

		if (serverArray != null) {
			parameters.put("mt4DatasourceId", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input", serverArray));
		}

		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			if(result== null){
				throw new RuntimeException(path+" API response is null\n"+ response);
			}
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
//            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiCopyTradingAcctSearch (String acctNum) {
		//Use only available Test Servers ID - MT4
		List<String> servers = AlphaServerEnv.getMTSTestServerIdByBrand(brand);
		LogUtils.info("Available Test Servers ID: " + servers.toString());
		JSONArray serverArray = new JSONArray();
		serverArray.addAll(servers);

		String path = this.url + "/account/query_accountList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("userId", new JSONObject().fluentPut("fuzzy", true).fluentPut("filterType", "INPUT").fluentPut("input", ""));
		parameters.put("real_name", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "test"));
		parameters.put("is_archive", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input", "0"));
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		parameters.put("mt4Account", new JSONObject().fluentPut("fuzzy", true).fluentPut("filterType", "INPUT").fluentPut("input", acctNum));
		parameters.put("mt4DatasourceId", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input",serverArray));

		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiTradingAcctUpdate (String acctNum, String acctGroup, String acctType, String acctOwner, String acctOwnerUID, String newAccGroup) {

		String path = this.url + "/account/update_account";
		HashMap<String,String> body = new HashMap<>();
		body.put("login", acctNum);
		body.put("mt4set", newAccGroup);
		body.put("opentype", acctType);
		body.put("comment", "testapi Update");
		body.put("mam", "");
		body.put("bpayNumber", "");
		body.put("owner", acctOwner);
		body.put("ownerId", acctOwnerUID);
		body.put("affid", "");
		body.put("previousGroup", acctGroup);
		body.put("oriMamNum", "");
		body.put("oldAccountOwner", acctOwnerUID);
		body.put("mamComment", "");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//filter with name = autotest, owner = cmatest
	public JSONObject apiRebateAcctSearch () {

		// Get servers available
		List<String> servers = GlobalMethods.getTestServers(PLATFORM.MT5, testEnv, brand);
		JSONArray serverArray = new JSONArray();
		serverArray.addAll(servers);

		String path = this.url + "/account/query_rebateAccountList";


		JSONObject body = new JSONObject();
		body.put("skipCount", true); // set false to get all records
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("real_name", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "autotest"));
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		parameters.put("is_archive", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input", "0"));
		parameters.put("owner", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "cmatest"));
		parameters.put("mt4DatasourceId", new JSONObject().fluentPut("filterType", "SELECT").fluentPut("input", serverArray));
		body.put("parameters",parameters);


		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			if(result== null){
				throw new RuntimeException(path+" response is null\n"+ response);
			}
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiRebateAcctUpdate (String acctNum, String acctGroup, String acctType, String acctOwner, String acctOwnerUID, String ibGroup){

		String path = this.url + "/account/update_rebeatAccount";
		HashMap<String,String> body = new HashMap<>();
		body.put("login", acctNum);
		body.put("mt4set", acctGroup);
		body.put("comment", "testapi Update");
		body.put("mam", "");
		body.put("affid", "");
		body.put("oriMamNum", "");
		body.put("owner", acctOwner);
		body.put("ownerId", acctOwnerUID);
		body.put("balance", "0");
		body.put("previousGroup", acctGroup);
		body.put("oldOwner", acctOwnerUID);
		body.put("isClear", "false");
		body.put("ibGroup", ibGroup);
		body.put("ibpIds", "");
		body.put("mt4_account_type", acctType);


		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//filter with name = testcrm
	public JSONObject apiClientSearch () {
		String path = this.url + "/individual/query_individualList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("show_name", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "testcrm auto"));
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		parameters.put("returnTime", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "0"));
		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiLeadWebsiteUserType (){
		String path = this.url + "/admin/select?context=website_user_type" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//user name = testcrm
	public void apiLeadSearch (){
		String path = this.url + "/intention/query_intentionList" ;

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("show_name", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "test"));
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiIBUserType (){
		String path = this.url + "/admin/select?context=ib_user_type" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//user name = testcrm
	public void apiExternalUserSearch (){
		String path = this.url + "/user/query_userList" ;

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));

		JSONObject parameters = new JSONObject();
		parameters.put("directLevel", new JSONObject().fluentPut("filterType", "CUSTOM").fluentPut("input", "5"));
		parameters.put("realName", new JSONObject().fluentPut("fuzzy", true).fluentPut("filterType", "INPUT").fluentPut("input", "testcrm"));
		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiEnableAutoAcctTransferAudit (){
		String path = this.url + "/admin/mt4Option/mt4account_change?description=0" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertEquals(result.getJSONObject("data").getString("p_value"),"1", "Incorrect result, failed to enable\n" + result);
			this.printAPIInfo(brand, regulator, path, "" , result );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiDisableAutoAcctTransferAudit (){
		String path = this.url + "/admin/mt4Option/mt4account_change?description=1" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertEquals(result.getJSONObject("data").getString("p_value"),"0", "Incorrect result, failed to disable\n" + result);
			this.printAPIInfo(brand, regulator, path, "" , result );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiDisableAutoLeverageAudit (){
		String path = this.url + "/admin/mt4Account/update_leverage_auto_audit?leverage_auto_audit=0" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertEquals(result.getJSONObject("data").getString("p_value"),"0", "Incorrect result, failed to disable\n" + result);
			this.printAPIInfo(brand, regulator, path, "" , result );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiEnableAutoLeverageAudit (){
		String path = this.url + "/admin/mt4Account/update_leverage_auto_audit?leverage_auto_audit=1" ;

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
			assertEquals(result.getJSONObject("data").getString("p_value"),"1", "Incorrect result, failed to disable\n" + result);
			this.printAPIInfo(brand, regulator, path, "" , result );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> apiGetListOfServersID() {
		String path = this.url + "/account/to_accountList?menuid=401" ;
		List<String> serverKeys = new ArrayList<>();

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");

			// Parse the HTML string
			Document doc = Jsoup.parse(result);
			Elements scripts = doc.select("script");

			for (Element script : scripts) {
				String jsContent = script.data();
				if (jsContent.contains("var servers")) {
					// Use regex to extract the JSON part: { "600":"MT4_UM_LIVE", ... }
					Pattern pattern = Pattern.compile("var servers\\s*=\\s*(\\{[^;]+\\})");
					Matcher matcher = pattern.matcher(jsContent);

					if (matcher.find()) {
						String serversJson = matcher.group(1);
						JSONObject serversObj = JSON.parseObject(serversJson);

						// Add the keys to the list
						for (String key : serversObj.keySet()) {
							serverKeys.add(key);
						}
						break;
					}
				}
			}
		} catch(Exception e){
				e.printStackTrace();
		}
		System.out.println("List of servers: " +serverKeys);
		return serverKeys;
	}
	/* Post API for query additional account by criteria (user id, status), return accauditID and realName as Map
	 * */
	public Map<String,String> testQuerySubmittedAddtionalAccountByUID(String uid, String status) {
		Map<String,String> result = new HashMap<>();
		String fullPath = this.url + "/audit/query_sameAct_list";

		JsonObject searchObj = new JsonObject();
		header.put("Content-Type", "application/json");

		searchObj.addProperty("startDate", GlobalMethods.setFromDateTime());
		searchObj.addProperty("endDate", GlobalMethods.setToDateTime());
		searchObj.addProperty("dateType", "1");
		searchObj.addProperty("statusQuery", (status == null || status.isEmpty()) ? "-1" : status);
		searchObj.addProperty("typeQuery", "-1");
		searchObj.addProperty("searchType", "4");
		searchObj.addProperty("userQuery", uid);
		searchObj.addProperty("directLevel", "-2");
		searchObj.addProperty("user_id", "");
		searchObj.addProperty("org_id", "");
		searchObj.addProperty("countryList", "");
		searchObj.addProperty("markType", "-1");
		searchObj.addProperty("mt4AccountTypeList", "");

		JsonObject mainObj = new JsonObject();
		mainObj.addProperty("limit", 10);
		mainObj.addProperty("pageNo", 1);
		mainObj.addProperty("offset", 0);
		mainObj.add("search", searchObj);
		mainObj.addProperty("order", "asc");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,mainObj.toString());
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {
				JSONObject obj = (JSONObject) item;
				if (obj.getInteger("status") == 0) {
					result.put("accauditID",obj.getString("id"));
					result.put("realName",obj.getString("real_name"));

					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Query additional account audit id failed!");
		}
		return result;
	}

	/* Query Additional Account Application by User ID, Status criteria
	* */
	public List<Map<String, String>> testQueryAdditionalAccount(String uid, String status, String startDate, String endDate) {
		List<Map<String, String>> result = new ArrayList<>();
		String fullPath = this.url + "/audit/query_sameAct_list";

		JsonObject searchObj = new JsonObject();
		header.put("Content-Type", "application/json");

		searchObj.addProperty("startDate", (startDate == null || startDate.isEmpty()) ? GlobalMethods.setFromDateOnly() : startDate);
		searchObj.addProperty("endDate", (endDate == null || endDate.isEmpty()) ? GlobalMethods.setToDateOnly() : endDate);
		searchObj.addProperty("dateType", "1");
		searchObj.addProperty("statusQuery", (status == null || status.isEmpty()) ? "-1" : status);
		searchObj.addProperty("typeQuery", "-1");
		searchObj.addProperty("searchType", "4");
		searchObj.addProperty("userQuery", uid);
		searchObj.addProperty("directLevel", "-2");
		searchObj.addProperty("user_id", "");
		searchObj.addProperty("org_id", "");
		searchObj.addProperty("countryList", "");
		searchObj.addProperty("markType", "-1");
		searchObj.addProperty("mt4AccountTypeList", "");

		JsonObject mainObj = new JsonObject();
		mainObj.addProperty("limit", 10);
		mainObj.addProperty("pageNo", 1);
		mainObj.addProperty("offset", 0);
		mainObj.add("search", searchObj);
		mainObj.addProperty("order", "asc");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,mainObj.toString());
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, fullPath, mainObj, message);

			if (message != null) {
				JSONArray data = (JSONArray) message.get("rows");
				for (Object item : data) {
					JSONObject obj = (JSONObject) item;
					Map<String, String> acc = new HashMap<>();
					acc.put("accauditID", obj.getString("id"));
					acc.put("realName", obj.getString("real_name"));
					acc.put("status", obj.getString("status"));
					acc.put("applicationTime", obj.getString("applicationTime"));
					acc.put("update_time", obj.getString("update_time"));
					acc.put("mt4_account_type", obj.getString("mt4_account_type"));
					acc.put("currency", obj.getString("currency"));
					result.add(acc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Query additional account audit id failed!");
		}
		return result;
	}

	/* Post API for reject add additional account by user id
	 * */
	public boolean testRejectAdditionalAccountByUID(String uid,String additionalAccountID,String real_name) {
		String fullPath = this.url + "/audit/audit_sameAct_refuse";

		HashMap<String,Object> body = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		/*String body = "{"
				+ "\"id\": \"" + additionalAccountID + "\","
				+ "\"user_id\": \"" + uid + "\","
				+ "\"refuseReason\": \"automation\","
				+ "\"real_name\": \"" + real_name + "\""
				+ "}";*/

		body.put("id", additionalAccountID);
		body.put("user_id", uid);
		body.put("refuseReason", "automation");
		body.put("real_name", real_name);

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,
					body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message.getBooleanValue("success");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Reject additional account audit failed!");
		}
		return false;
	}

}
