package newcrm.cpapi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import cn.hutool.http.HttpRequest;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import newcrm.global.GlobalMethods;
import newcrm.utils.encryption.EncryptUtil;
import newcrm.utils.http.CRMHttpClient;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.LogUtils;
import vantagecrm.Utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;
import static utils.HttpResponseParser.getResponseHeaderValue;
import static utils.HttpResponseParser.parseHeadersAccurately;

public class CPAPIBase {

	protected CRMHttpClient httpClient = new CRMHttpClient();
	protected HashMap<String,String> header;
	protected String url ;
	public String TraderName;
	protected REGULATOR regulator;
	protected BRAND brand;
	public static String xToken, loginCookieHeader;
//    public static ThreadLocal<String> xToken = new ThreadLocal<>();
//    public static ThreadLocal<String> loginCookieHeader = new ThreadLocal<>();


	/**
	 * 
	 * @param url
	 * @param cplogin
	 * @param password
	 */
	public CPAPIBase(String url, String cplogin, String password, BRAND brand, REGULATOR regulator) {
		LogUtils.info("CP API Login: " + cplogin + "  password: " + password);
		this.url = Utils.ParseInputURL(url);
		this.TraderName = cplogin;
		this.brand = brand;
		this.regulator = regulator;
		String host = Utils.ParseAdminURLtoHost(url);
		header = new HashMap<>();
		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		header.put("Content-Type", "application/json");
		header.put("Host", host);
		header.put("Multi-Auth-Validate", ",,,,");
		HyTechUtils.genXSourceHeader(header);
//		header.put("Branchversion", "null");

		password = EncryptUtil.MD5(password);
		String fullPath = this.url + "hgw/auth-api/login/customer/pc/to_login";
		JSONObject request = new JSONObject();

		request.put("email", cplogin);
		request.put("passwordLogin", password);
		request.put("loginMode", "EMAILPASSWORD");
		request.put("utc", "36000000");

		try {

			cn.hutool.http.HttpResponse response =
					HttpRequest.post(fullPath)
							.addHeaders(header)
							.body(request.toString())
							.timeout(20000)
							.execute();

			GlobalMethods.printDebugInfo("CP API Login URL: " + fullPath);

			String responseBody = response.body();
			printAPICPInfo(fullPath, request.toString(), responseBody);

			JSONObject result = JSONObject.parseObject(responseBody);
			LogUtils.info("CP API Login Result: " + result.toString());
			Integer res_code = result.getInteger("code");
			Assert.assertTrue(res_code==200, result.toString());

			// Get xtoken from response. This xtoken will be used in all subsequent API call.
			xToken = result.getJSONObject("data").getString("xtoken");

			String cookieHeader = response.header(cn.hutool.http.Header.SET_COOKIE);

			if (cookieHeader == null || cookieHeader.isEmpty()) {
				cookieHeader = loginCookieHeader;
			} else {
				loginCookieHeader= cookieHeader;
			}

			String args[] = cookieHeader.split(";");
			String values[] = args[0].split("=");
			header.put("Cookie", args[0] + ";" + "xtoken=" + xToken + ";");
			BasicClientCookie cookie = new BasicClientCookie(values[0], values[1]);
			cookie.setDomain(host);
			cookie.setPath("/");
			httpClient.setCookie(cookie);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public CPAPIBase(String url, String cplogin, String password) {
		this(url, cplogin, password, null, null);
	}

	
	//For those login not required
	public CPAPIBase(String url, HashMap<String,String> header) {
		this.url = Utils.ParseInputURL(url);
		this.header = header;
	}

	public CPAPIBase(String url, WebDriver driver) {
		this.url = Utils.ParseInputURL(url);

		// Generate Header
		String host = Utils.ParseAdminURLtoHost(url);
		this.header = new HashMap<>();
		this.header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		this.header.put("Content-Type", "application/json");
		this.header.put("Host", host);

		// Login Token required in each API request
		try {
			StringJoiner cookieJoiner = new StringJoiner(";");

			for (String name : new String[]{"__cf_bm", "xtoken"}) {
				Cookie cookie = driver.manage().getCookieNamed(name);
				if (cookie != null) {
					cookieJoiner.add( name + "=" + cookie.getValue());
				}
			}

			String headerCookie = cookieJoiner.toString();

			if (!headerCookie.isEmpty()) {
				header.put("Cookie", headerCookie);
			} else {
				System.out.println("Cookie not found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CPAPIBase() {

	}

	/* Return the first non-archived trading account that within specified currency list.
     * API is originally called in CP home page.
     * This method is used in deposit API.
     * @param String[] currencies
     * @return {account,currency}
     * */
    public JSONObject queryMetaTraderAccountDetails(String[] currencies) throws Exception {
    	String fullPath = this.url + "web-api/cp/api/home/query-metaTrader-account-details";
		header.put("Content-Type", "application/json");
		HyTechUtils.genXSourceHeader(header);
		String body = "{\"hiddenMtAccountIdList\":[],\"unHide\":false,\"platform\":null}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String responseBody = EntityUtils.toString(response.getEntity());
			printAPICPInfo(fullPath, body, responseBody);
			JSONArray data = JSONArray.parseArray(responseBody);

			for (Object item : data) {  
				try {
					String isArchive = ((JSONObject) item).getString("isArchive");
					String currency = ((JSONObject) item).getString("currency");
					//System.out.println("isArchive:" + isArchive + "currency:" +currency);
			        
		            if (isArchive.equals("0") && Arrays.asList(currencies).contains(currency)) {
		            	//Get account
			           	JSONObject accInfo = new JSONObject();
			           	accInfo.put("account", ((JSONObject) item).getString("mt4_account"));
				        accInfo.put("currency", ((JSONObject) item).getString("currency"));
						accInfo.put("balance", ((JSONObject) item).getDouble("balance"));
						accInfo.put("userId", ((JSONObject) item).getString("user_id"));
						accInfo.put("accountType", ((JSONObject) item).getIntValue("mt4_account_type"));
						accInfo.put("credit", ((JSONObject) item).getIntValue("credit"));
				        return accInfo;      
		            }
				} catch (NullPointerException e) {
				    // Handle the exception if archive equals null or any key not exists
				    continue; // Continue to the next iteration of the loop
				}
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query mt4_account failed!");
		}
		
		System.out.println("\nNot able to find a suitable MT4/5 account!\n");
		return null;		
    }

//	public JSONObject queryMetaTraderMT5AccountDetails(String[] currencies) throws Exception {
//		String fullPath = this.url + "web-api/cp/api/home/query-metaTrader-account-details";
//		header.put("Content-Type", "application/json");
//		HyTechUtils.genXSourceHeader(header);
//		String body = "{\"hiddenMtAccountIdList\":[],\"unHide\":false,\"platform\":null}";
//
//		try {
//			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
//			String responseBody = EntityUtils.toString(response.getEntity());
//			printAPICPInfo(fullPath, body, responseBody);
//			JSONArray data = JSONArray.parseArray(responseBody);
//
//			for (Object item : data) {
//				try {
//					String isArchive = ((JSONObject) item).getString("isArchive");
//					String currency = ((JSONObject) item).getString("currency");
//					int accountType = ((JSONObject) item).getInteger("account_type");
//					//System.out.println("isArchive:" + isArchive + "currency:" +currency);
//
//					if (isArchive.equals("0") && Arrays.asList(currencies).contains(currency)&&accountType == 13 ){
//						//Get mt5 account
//						JSONObject accInfo = new JSONObject();
//						accInfo.put("account", ((JSONObject) item).getString("mt4_account"));
//						accInfo.put("currency", ((JSONObject) item).getString("currency"));
//						accInfo.put("balance", ((JSONObject) item).getDouble("balance"));
//						accInfo.put("userId", ((JSONObject) item).getString("user_id"));
//						return accInfo;
//					}
//				} catch (NullPointerException e) {
//					// Handle the exception if archive equals null or any key not exists
//					continue; // Continue to the next iteration of the loop
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			System.out.println("Query mt4_account failed!");
//		}
//
//		System.out.println("\nNot able to find a suitable MT4/5 account!\n");
//		return null;
//	}
    /* Return the first trading account.
     * This method is used in registration API.
     * @return {account}
     * */
    public JSONObject queryMetaTraderAccountDetails() throws Exception {
    	String fullPath = this.url + "web-api/cp/api/home/query-metaTrader-account-details";
		header.put("Content-Type", "application/json");
		HyTechUtils.genXSourceHeader(header);
		String body = "{\"hiddenMtAccountIdList\":[],\"unHide\":false,\"platform\":null}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String responseBody = EntityUtils.toString(response.getEntity());
			JSONArray data = JSONArray.parseArray(responseBody);

			for (Object item : data) {  
				try {
					JSONObject accInfo = new JSONObject();
		           	accInfo.put("account", ((JSONObject) item).getString("mt4_account"));
					   System.out.println(accInfo);
			        return accInfo;      		            
				} catch (NullPointerException e) {
				    // Handle the exception if archive equals null or any key not exists
				    continue; // Continue to the next iteration of the loop
				}
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query mt4_account failed!");
		}
		
		System.out.println("\nNot able to find a suitable MT4/5 account!\n");
		return null;		
    }

	/**
	 * Query deposit transaction history details
	 * @return {account,currency}
	 * */
	public JSONArray queryTransactionHistoryDeposit() {
		String fullPath = this.url + "hgw/rw/payment-api/cp/api/transactionHistory/deposit";
		header.put("Content-Type", "application/x-www-form-urlencoded");
		HyTechUtils.genXSourceHeader(header);
		String body = "pageNo=1&pageSize=10";


		try {
			HttpResponse response = httpClient.getGetResponse(fullPath, header,body);
			LogUtils.info("header: " + header.toString());
			String responseBody = EntityUtils.toString(response.getEntity());
			printAPICPInfo(fullPath, body, responseBody);
			JSONObject history =JSONObject.parseObject(responseBody);
			JSONObject data = history.getJSONObject("data");
			JSONArray depositHistory = data.getJSONArray("depositHistory");
			JSONArray newDepositHistory = new JSONArray();

			String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			for (Object item : depositHistory) {
				try {
					String date = ((JSONObject) item).getString("date");
					if (date.contains(startDate)) {
						newDepositHistory.add(item);
					}
					return newDepositHistory;


				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("查询当天入金记录失败!");
		}

		System.out.println("\n未获取到当天的入金记录!\n");
		return null;
	}

	public JSONObject sendCPAPIrequestArray(String path, String body) throws Exception {
		String fullPath = this.url + path;

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String responseBody = EntityUtils.toString(response.getEntity());
			JSONArray data = JSONArray.parseArray(responseBody);
			JSONObject result = new JSONObject();
			result.put("Accounts", data);
			return result;

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query mt4_account failed!");
		}

		System.out.println("\nNot able to find a suitable MT4/5 account!\n");
		return null;
	}
	
	
	/**
	 * Send CP api request
	 * @param path - url(should not start with /), String request body
	 * @return json response
	 */
    public JSONObject sendCPAPIrequest(String path, String body) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String responseHeader = Arrays.toString(response.getAllHeaders());
//			LogUtils.info("response header:\n " + Arrays.toString(response.getAllHeaders()));
			printAPICPInfoPro(fullPath,header, body, response);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));

			Map<String, List<String>> parsedHeaders = parseHeadersAccurately(responseHeader);

			// 打印结果
			for (Map.Entry<String, List<String>> entry : parsedHeaders.entrySet()) {
				String key = entry.getKey();
				List<String> values = entry.getValue();
				LogUtils.info("==============================================");

				for (String value : values) {
					System.out.println(key + ": " + value+"\n");
				}
				LogUtils.info("==============================================");

			}
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray sendCPAPIrequest_array(String path, String body) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String responseHeader = Arrays.toString(response.getAllHeaders());
			printAPICPInfoPro(fullPath, header, body, response);
			JSONArray message = JSON.parseArray(EntityUtils.toString(response.getEntity(),"UTF-8"));

			Map<String, List<String>> parsedHeaders = parseHeadersAccurately(responseHeader);

			// 打印结果
			for (Map.Entry<String, List<String>> entry : parsedHeaders.entrySet()) {
				String key = entry.getKey();
				List<String> values = entry.getValue();
				LogUtils.info("==============================================");

				for (String value : values) {
					System.out.println(key + ": " + value+"\n");
				}
				LogUtils.info("==============================================");

			}

			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send CP api get request
	 * @param path - url(should not start with /)
	 * @return json response
	 */
	public JSONObject sendCPAPIGETrequest(String path) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getGetResponse(fullPath, header);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray sendCPGETrequestArray(String path) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getGetResponse(fullPath, header);
			JSONArray message = JSON.parseArray(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject sendNonJsonGETrequest(String path) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getGetResponse(fullPath, header);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Print deposit/withdrawal submit result 
	 * @param type - Deposit or Withdrawal
	 * @param cpsCode - Neteller or Banktransfer, etc.
	 */
	public void printUserFundsInfo(String Brand, String account, String currency, String type, String channelName, String cpsCode) {
		System.out.println("***********" + type + " submit successfully***********");
		System.out.printf("%-30s : %s\n","Brand",Brand);
		System.out.printf("%-30s : %s\n","CPS Code",cpsCode);
		System.out.printf("%-30s : %s\n","Channel Name",channelName);
		System.out.printf("%-30s : %s\n","Account",account + " - " + currency);
		System.out.printf("%-30s : %s\n","CP URL",this.url);
		System.out.printf("%-30s : %s\n","CP login",TraderName);
		System.out.println("*************************************************");
	}


	public void printAPICPInfo(String url, String body, String response){
		String methodName;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length >= 3) {
			methodName = stackTrace[2].getMethodName();
		} else {
			methodName = "";
		}

		// 5. 保留控制台输出
		System.out.println("*********** API call successful ***********");
		System.out.printf("%-30s : %s\n", "Method Name",methodName);
		System.out.printf("%-30s : %s\n", "API URL", url);
		System.out.printf("%-30s : %s\n", "API Request", body);
		System.out.printf("%-30s : %s\n", "API Response", response);
		System.out.println("*******************************************");
	}
	/**
	 * Print API call info
	 * @param url - api url
	 * @param body - api request body
	 * @param response - api response
	 */
	public <T> void printAPICPInfoPro(String url, HashMap<String,String> requestHeader, String body, T response) throws IOException {
		String methodName;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length >= 3) {
			methodName = stackTrace[2].getMethodName();
		} else {
			methodName = "";
		}

		String eventId = null;
		if (response instanceof HttpResponse) {
			eventId = getResponseHeaderValue((HttpResponse) response, "Eventid");
		}

		LogUtils.info("*********** API Call Info ***********");
		System.out.printf("%-30s : %s\n", "Method Name",methodName);
		System.out.printf("%-30s : %s\n", "API URL", url);
//		System.out.printf("%-30s : %s\n", "API Request Header", requestHeader);
		if (requestHeader != null && !requestHeader.isEmpty()) {
			for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
				System.out.printf("  %-28s : %s\n", entry.getKey(), entry.getValue());
			}
		}
		System.out.printf("%-30s : %s\n", "API Request", body);
		if (response instanceof HttpResponse) {
			System.out.printf("%-30s : %s\n", "API Response Header", Arrays.toString(((HttpResponse) response).getAllHeaders()));
			System.out.printf("%-30s : %s\n", "API Response EventId：",eventId );
			System.out.printf("%-30s : %s\n", "API Response", EntityUtils.toString(((HttpResponse) response).getEntity(), "UTF-8"));

		}
		if(response instanceof JSONObject){
			System.out.printf("%-30s : %s\n", "API Response：", ((JSONObject) response).toJSONString());
		}
		LogUtils.info("*******************************************");
	}
	//convert json value into form url type
	public static String convertToFormUrlEncoded(String json) {
		StringBuilder sb = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(json);
			Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				String key = field.getKey();
				JsonNode valueNode = field.getValue();

				if (valueNode.isArray() && valueNode.size() == 0) {
					// 忽略空数组
					continue;
				}

				String value;

				if (("attachVariables".equals(key) || "raw".equals(key)) && valueNode.isTextual()) {
					// 保留原始 JSON 字符串，不额外加引号
					value = valueNode.textValue(); // 直接取原始值
				} else if (valueNode.isTextual()) {
					value = valueNode.textValue();
				} else {
					value = valueNode.toString();
				}

				sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
				sb.append("=");
				sb.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
				sb.append("&");
			}

			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public JSONObject postAPICall(String apiPath, Map<String, String> body) throws Exception {
		// Generate x-source value. Require for every request.
		HyTechUtils.genXSourceHeader(header);
		JSONObject result = sendCPAPIrequest(apiPath, String.valueOf(body));
		return result;
	}

	public JSONObject getAPICall(String apiPath) throws Exception {
		// Generate x-source value. Require for every request.
		HyTechUtils.genXSourceHeader(header);

		JSONObject result = sendCPAPIGETrequest(apiPath);
		printAPICPInfo(url + apiPath, "", String.valueOf(result));

		return result;
	}

	public JSONArray postAPIArrayCall(String apiPath, Map<String, String> body) throws Exception {
		// Generate x-source value. Require for every request.
		HyTechUtils.genXSourceHeader(header);
		JSONArray result = sendCPAPIrequest_array(apiPath, String.valueOf(body));
		return result;
	}

	public void validateAPIResponse(String apiPath, JSONObject result) {
		assertEquals(result.get("code"), 0, apiPath + " failed!! \n" + result);
	}

	public void validateAPIResponse200(String apiPath, JSONObject result) {
		assertEquals(result.get("code"), 200, apiPath + " failed!! \n" + result);
	}

}
