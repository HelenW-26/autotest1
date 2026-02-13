package newcrm.adminapi;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.Bot.TeamsNotifier;
import newcrm.utils.encryption.EncryptUtil;
import newcrm.utils.http.CRMHttpClient;
import org.testng.annotations.Test;
import utils.LogUtils;
import vantagecrm.DBUtils;
import vantagecrm.Utils;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static utils.HttpResponseParser.getResponseHeaderValue;

public class AdminAPI {
	TeamsNotifier teamsNT = new TeamsNotifier();
	protected CRMHttpClient httpClient;
	protected HashMap<String,String> header;
	protected String url ;
	protected REGULATOR regulator;
	protected BRAND brand;
	protected String code;
	protected ENV testEnv;

	/**
	 * 
	 * @param url
	 * @param regulator
	 * @param adminUser
	 * @param password
	 * @param Brand
	 * @param testEnv
	 * @throws Exception 
	 */
	public AdminAPI(String url,REGULATOR regulator,String adminUser,String password, BRAND Brand, ENV testEnv) {
		LogUtils.info("AdminAPI is initializing...");
		LogUtils.info("url:"+url+"\nregulator:"+regulator+"\nadminUser:"+adminUser+"\npassword:"+password+"\nBrand:"+Brand+"\nTestEnv:"+testEnv);
		String result="",defaultRegulator="",cookie="";
		httpClient = new CRMHttpClient();
		this.url = url;
		this.regulator = regulator;
		this.brand = Brand;
		this.testEnv = testEnv;
		String host = Utils.ParseAdminURLtoHost(url);
		header = new HashMap<>();
		header.put("Accept-Language", "en-GB,En-US;q=0.9en;q=0.8");
		header.put("Accept-Encoding", "gzip,deflate");
		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Host", host);
		//header.put("Current-Regulator", regulator.toString());
		header.put("Origin", "https://"+host);

		password = EncryptUtil.MD5(password);
		String fullPath = url + "/login/to_login";
		HashMap<String,String> body = new HashMap<>();
		body.put("userName_login", adminUser);
		body.put("password_login", password);
		body.put("utc", "36000000");
		
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, body);

			//Save the cookie for use
			Header[] cookies = response.getHeaders("Set-Cookie");
			for (Header s:cookies) {
			  //System.out.println("cookies : "+s.toString());
			  if (s.toString().contains("jsId")) {
				  result = s.toString();
				  cookie = result.substring(result.indexOf("jsId"),result.length());
				  header.put("Cookie", cookie);
			  } 
			}

			if (testEnv.equals(ENV.PROD)) {
				//Read default regulator from DB
				defaultRegulator = DBUtils.getDefaultRegulator(adminUser, Brand.toString(), testEnv.toString(), regulator.toString());

		        //API trigger OTP. Read OTP code from DB. Insert txId and code to body
				body = testPostAdminOTPCode(this.url, testEnv.toString(), Brand.toString(), defaultRegulator, adminUser, body);

				//Re-send login
		        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		        response = httpClient.getPostResponse(fullPath, header, body);
		
		    	//Get login response status code
		        int status = response.getStatusLine().getStatusCode();
		        //System.out.println("\nstatus code for API admin login is: "+status);			        	
			}
			
			Thread.sleep(1000);
			changeRegulator(regulator);	
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


    /* Migrated from RestAPI
     * Post for requesting admin 24h login verification code
     * url should be admin url ends with "/"
     * */
    public HashMap<String,String> testPostAdminOTPCode(String url, String TestEnv, String Brand, String regulator, String AdminName,HashMap<String,String> body) throws Exception {
    	String result="",vars="", txId="";
		String fullPath = url + "/mfa/sendEmailVerificationCode";

		header.put("Content-Type", "application/json;charset=UTF-8");  		
		String otp_body = "{\"userName\":\"" + AdminName + "\",\"namespace\":\"AP_LOGIN\"}"; 
		//System.out.println("header:"+header+"\nv_body:"+otp_body+"\nurl:"+fullPath);
		
		HttpResponse response = httpClient.getPostResponse(fullPath, header, otp_body);
		result = EntityUtils.toString(response.getEntity(),"UTF-8");
		JSONObject message = JSON.parseObject(result);
		Integer rescode = message.getInteger("code");
		
		if (rescode.equals(500)) {
			//'500' is normally due to duplicate requests in 60s
			// Thus no need to request OTP in this case
			body.put("code", "");
			
		}else if (result.contains("code\":0")) {			
			txId = message.getJSONObject("data").getString("txId");
			body.put("txId", txId);
		 	System.out.println("\nRequesting admin login verification code succeeds.");
			
		 	//Fetch OTP code from DB.
		 	vars = DBUtils.funcReadOTPInBusinessDB(TestEnv, Brand, regulator, AdminName);
		 	
		 	//To extract code with regular expressions
		 	Pattern p = Pattern.compile("[0-9]{6}");		 	
		    Matcher m = p.matcher(vars);
		    
	        // if an occurrence if a pattern was found in a given string...
	        if (m.find()) {
	            code =m.group(0); // second matched digits
	            System.out.println("code: "+code);
	            setCode(code);
	            body.put("code", code);
	            
	        }else
	        {
	        	System.out.println("Couldn't find verification code. The body is:" + vars);
	        }
		 	
		} 

		return body;
    }



	
    //switch regulator
	private void changeRegulator(REGULATOR regulator) {
		String fullPath = url + "/admin/switch-regulator?regulator=" + regulator.toString();
		header.put("Content-Type", "application/json;charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param rebateAccount
	 * @return {currency,p_ids,user_id}
	 */
	public JSONObject getRebateAccountInfo(String rebateAccount) {
		String fullPath = url + "/account/query_rebateAccountList";
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		String body = "{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":null},\"userId\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"cpa\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\""+rebateAccount+"\"},\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"commissionType\":{\"filterType\":\"SELECT\",\"input\":\"\"},\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONArray rows = obj.getJSONArray("rows");
			if(rows==null || rows.size()==0) {
				return null;
			}
			JSONObject row = rows.getJSONObject(0);
			JSONObject result = new JSONObject();
			result.put("currency", row.getString("currency"));
			result.put("p_ids", row.getString("p_ids").substring(2));
			result.put("user_id", row.getString("user_id"));
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 
	 * @param account
	 * @return JSONObject {"currency","group","dsId","account","credit","balance}
	 */
	public JSONObject getAccountInfo(String account) {
		String fullPath = url + "/account/query_accountList";
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		String body = "{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":null},\"userId\":{\"fuzzy\":true,"
				+ "\"filterType\":\"INPUT\",\"input\":\"\"},\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},"
				+ "\"cpa\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"mamNumber\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\","
				+ "\"input\":\""+account+"\"},\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},"
				+ "\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONArray rows = obj.getJSONArray("rows");
			if(rows==null || rows.size()==0) {
				return null;
			}
			String group = rows.getJSONObject(0).getString("group");
			group = group.replace("\\", "");
			JSONObject result = new JSONObject();
			result.put("currency", rows.getJSONObject(0).getString("currency"));
			result.put("group", group);
			result.put("dsId", rows.getJSONObject(0).getString("mt4_datasource_id"));
			result.put("account", account);
			result.put("credit", rows.getJSONObject(0).getString("credit"));
			result.put("balance", rows.getJSONObject(0).getString("balance"));
			return result;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Query trading accounts that match specific accName under specific dataSourceId
	 * @param accName
	 * @return JSONArray
	 */
	public JSONArray queryTradingAccListbyName(String dataSourceId, String accName) {
		String fullPath = url + "/account/query_accountList";
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		String body ="{\"skipCount\":true,\"pagination\":{\"limit\":50, \"order\": \"desc\", \"sort\": \"approvedTime\"},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"actact1000\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		//Replace the dummy account with the real account
		body=body.replace("actact1000", accName);
		//Replace the dummy datasourceId with the real dataSourceId
		String bodyNew=body.replace("dsds5", dataSourceId);
		//System.out.println("bodyNew:"+bodyNew);
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, bodyNew);
			//System.out.println("header:"+header);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONArray rows = obj.getJSONArray("rows");
			//System.out.println("rows:"+rows);
			if(rows==null || rows.size()==0) {
				return null;
			}
			return rows;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Query trading accounts that match specific accName under specific dataSourceId
	 * @param accName
	 * @return JSONArray
	 */
	public JSONArray queryRebateAccListbyName(String dataSourceId, String accName) {
		String fullPath = url + "/account/query_rebateAccountList";
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		String body ="{\"skipCount\":true,\"pagination\":{\"limit\":50, \"order\": \"desc\", \"sort\": \"approvedTime\"},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"actact1000\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		//Replace the dummy account with the real account
		body=body.replace("actact1000", accName);
		//Replace the dummy datasourceId with the real dataSourceId
		String bodyNew=body.replace("dsds5", dataSourceId);
		//System.out.println("bodyNew:"+bodyNew);
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, bodyNew);
			//System.out.println("header:"+header);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONArray rows = obj.getJSONArray("rows");
			//System.out.println("rows:"+rows);
			if(rows==null || rows.size()==0) {
				return null;
			}
			return rows;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param account
	 * @param date
	 * @return JSONArray [{symbol,closePrice,volume,ticket}]
	 */
	public JSONArray getTradingHistory(JSONObject account,String date) {
		String fullPath = url + "/account/queryTradesClosedListHistory";
		header.put("Content-Type", "application/json");
		header.put("accept", "application/json, text/javascript, */*; q=0.01");
		String body = "{\"limit\":1000,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"userQuery\":\"\","
				+ "\"startDate\":\""+date+"\",\"endDate\":\""+date+"\",\"mt4Account\":\""+account.getString("account")+"\",\"dataSourceId\":\""+account.getString("dsId")+"\"}}";
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONArray rows = obj.getJSONArray("rows");
			if(rows==null || rows.size()==0) {
				return null;
			}
			JSONArray result = new JSONArray();
			for(int i = 0;i<rows.size();i++) {
				JSONObject row = rows.getJSONObject(i);
				
				if(row.getInteger("cmd")!=0 && row.getInteger("cmd")!=1) {//只需要交易记录
					continue;
				}
				JSONObject values = new JSONObject();
				values.put("symbol", row.getString("symbol"));
				values.put("closePrice", row.getBigDecimal("closePrice"));
				values.put("volume", row.getBigDecimal("volume").divide(BigDecimal.valueOf(100)));
				values.put("ticket", row.getString("ticket"));
				result.add(values);
			}
			
			if(result.size()==0) {
				return null;
			}else {
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean auditDeposit(String id,String amount,String note) {
		String fullPath = this.url + "/payment/deposit/auditDeposit_pass";
		HashMap<String,String> body = new HashMap<>();
		body.put("deposit_id", id);
		body.put("actualAmount", amount);
		body.put("processed_notes", note);
		
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			System.out.println(message);
			return message.getBooleanValue("success");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean auditAccount(String userId,String accountDBiD,String owner,String ib_affid,String affid,String realName,
			String serverNum,String groupName,String accountType,String leverage,String ccid, String currency) {
		String fullPath = this.url + "/audit/audit_individual_agree";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		HashMap<String,String> body = new HashMap<>();
		body.put("user_id", userId);
		body.put("id", accountDBiD);
		body.put("ib_Account_Owner", owner);
		body.put("ib_Affid", ib_affid);//owner id
		body.put("affid", affid);
		body.put("Username1", realName);
		body.put("ib_server", serverNum);
		body.put("ib_mt4_set", groupName);
		body.put("ib_mt4_type", accountType);
		body.put("ib_Leverage", leverage);
		body.put("ccAuto", "false");
		body.put("unionpayAuto", "false");
		body.put("ib_Currency", currency);
		//body.put("PhoneNumber1", phone);
		//body.put("Mailbox1", email);
		body.put("commission_code_id", ccid);
		body.put("ib_MAM_Number", "");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			return message.getBooleanValue("success");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param accountOwner
	 * @return [{name,id}]
	 */
	public JSONArray getServers(String accountOwner,String affid) {
		String fullPath = this.url + "/audit/query_server";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		HashMap<String,String> body = new HashMap<>();
		body.put("ib_Account_Owner",accountOwner);
		body.put("affid", affid);
		
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			if(message != null && message.getBooleanValue("success")) {
				return message.getJSONArray("data");
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param ib_server
	 * @param applyType
	 * @param user_id
	 * @param mt4_account_type
	 * @return list<String>
	 */
	public List<String> getGroups(String ib_server,String applyType, String user_id, String mt4_account_type){
		String fullPath = this.url + "/audit/query_ib_server";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		HashMap<String,String> body = new HashMap<>();
		body.put("ib_server", ib_server);
		body.put("applyType", applyType);
		body.put("isSameAct", "0");
		body.put("user_id", user_id);
		body.put("mt4_account_type", mt4_account_type);
		body.put("accountDealType", applyType);
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			if(message.getBooleanValue("success")) {
				JSONArray groups =  message.getJSONArray("data");
				if(groups.size()==0) {
					return null;
				}
				return groups.toJavaList(String.class);
				
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param ownerId
	 * @return rebate accounts
	 */
	public List<String> getRebateAccounts(String ownerId){
		String fullPath = this.url + "/audit/query_owner_moreRebateAct";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		HashMap<String,String> body = new HashMap<>();
		body.put("owner_id", ownerId);

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			GlobalMethods.printDebugInfo("response:" + response);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			if(message != null && message.getBooleanValue("success")) {
				JSONArray groups =  message.getJSONArray("data");
				if(groups.size()==0) {
					return null;
				}
				return groups.toJavaList(String.class);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param ownerId
	 * @param affid
	 * @return List<id>
	 */
	public List<String> getCommissionCode(String ownerId,String affid){
		String fullPath = this.url + "/audit/query_owner_commissionCode";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		HashMap<String,String> body = new HashMap<>();
		body.put("owner_id", ownerId);
		body.put("affid", affid);

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			if(message != null && message.getBooleanValue("success")) {
				ArrayList<String> result = new ArrayList<>();
				JSONArray groups =  message.getJSONArray("data");
				if(groups.size()==0) {
					return null;
				}
				groups.forEach(e->{
					JSONObject obj = JSON.parseObject(e.toString());
					if(!obj.getBooleanValue("isDel")) {
						result.add(obj.getString("id"));
					}
				});
				
				return result;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

    
    /*Added by AlexL 12/10/2020
     * for decrypting email. Please be aware of the special characters in the decrypted email. 
     * Only "+" and "/" have been handled. If you found the print result of "*** Decrypted Email"
     * is still a decrypt one. That means a new special character needs to be handled.
     */
    public String testPostDecryptEmail(String email, String url) {
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    	url = url.concat("/safe/decrypt/decryptEmailOrPhone");  
    	String new_email="";

		//for handling the special character like "+" and "/"
		email = email.replace("+","%2B").replace("/","%2F");
		String body = "email=" + email + "&phone=&userId=&mt4Account=";
		//System.out.println("\n***   body is   *** " + body);
		
		try {
			HttpResponse response = httpClient.getPostResponse(url, header,body);
			JSONObject obj = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			JSONObject data = obj.getJSONObject("data");
			if(data==null || data.size()==0) {
				return null;
			}
			new_email = data.getString("email");
			//System.out.println("\n***   Decrypted Email is   *** " + new_email);
			return new_email;
			
		} catch (Exception e) {
			System.out.println("\n!!!   Decrypt Email failed   !!!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    /* Post API for query user id by user name
     * */
    public String testQueryUserIdByName(String name) throws Exception {
    	String userID="";
    	String fullPath = this.url + "/audit/query_user_list";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"1\",\"userQuery\":\""+name+"\"}}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println("\ntestQueryUserIdByName: "+message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get user_id
	            	if (key.toString().equals("user_id")) {
	            		userID=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query user id failed!");
		}
		
		return userID;
    }
    
    /* Post API for query user id by user email
     * */
    public String testQueryUserIdByEmail(String email) throws Exception {
    	String userID="";
    	String fullPath = this.url + "/audit/query_user_list";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"3\",\"userQuery\":\""+email+"\"}}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println("\ntestQueryUserIdByName: "+message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get user_id
	            	if (key.toString().equals("user_id")) {
	            		userID=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query user id failed!");
		}
		
		return userID;
    }
    
    /* Post API for query account audit id by user name
     * */
    public String testQueryAccAuditIdByName(String name) throws Exception {
    	String accauditID="";
    	String fullPath = this.url + "/audit/query_user_list";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"1\",\"userQuery\":\""+name+"\"}}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get user_id
	            	if (key.toString().equals("id")) {
	            		accauditID=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query account audit id failed!");
		}
		
		return accauditID;
    }

    /* Post API for query account audit id by user email
     * */
    public String testQueryAccAuditIdByEmail(String email) throws Exception {
    	String accauditID="";
    	String fullPath = this.url + "/audit/query_user_list";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"3\",\"userQuery\":\""+email+"\"},\"order\":\"asc\"}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get user_id
	            	if (key.toString().equals("id")) {
	            		accauditID=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query account audit id failed!");
		}
		
		return accauditID;
    }
    /* Post API for query trading account id by user id
     * */
    public String testQueryTradingAccountByUserid(String userID) throws Exception {
    	String account="";
    	String fullPath = this.url + "/account/query_accountList";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"pagination\":{\"sort\":\"userId\"},\"parameters\":{\"userId\": {\"filterType\":\"INPUT\",\"input\":\""+userID+"\"}}}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			//System.out.println(message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	if (key.toString().equals("isArchive")&&value.toString().equals("1")) {
	            		break;
	            	}
	            	//Get account
	            	if (key.toString().equals("mt4_account")) {
	            		account=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query mt4_account failed!");
		}
		
		return account;
    }
    
    /* Post API for query trading account id by user id
     * */
    public String testQueryTradingAccountByEmail(String encryptemail) throws Exception {
    	String account="";
    	String fullPath = this.url + "/account/query_accountList";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/json");
		String body = "{\"pagination\":{\"sort\":\"userId\"},\"parameters\":{\"tb_user.email\": {\"filterType\":\"INPUT\",\"input\":\""+encryptemail+"\"}}}"; 

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			System.out.println(message);
			JSONArray data = (JSONArray) message.get("rows");
			for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	if (key.toString().equals("isArchive")&&value.toString().equals("1")) {
	            		break;
	            	}
	            	//Get account
	            	if (key.toString().equals("mt4_account")) {
	            		account=value.toString();
	            	}
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query mt4_account failed!");
		}
		
		return account;
    }
    
    /*
	 * API query if email exists in Admin. 
	 * return true if available 
	 * return false if email existed in Admin
	 */
    public boolean testQueryEmailAvailability(String encryptedEmail) throws Exception {
    	String fullPath = this.url + "/user/queryEmailIsExist";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		String body = "email=" + encryptedEmail;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			
			//return false if email existed in Admin
			if(EntityUtils.toString(response.getEntity(),"UTF-8").equals("false")) {
				
				System.out.println("\n!!! Email existed in Admin! ");
				return false;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Query email existed API failed!");
		}
		
		return true;
    }
 
    /*
	 * API upgrade client to IB in Admin. 
	 * return true if upgrade successfully 
	 * return false if failed to upgrade
	 */
    public boolean testClientUpgradeIB(String account, String userID) throws Exception {
    	String fullPath = this.url + "/individual/upgradeIB";
    	//System.out.println("url is: "+url);
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		String body = "mt4Account="+account+"&user_id=" + userID;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			if (message.toString().contains("please apply ACTIVE mtAccount")) {
				System.out.println("Need to activate account "+account+" for upgrading to IB: " + message);
			}else if(message.toString().contains("account type already is IB")){
				System.out.println("IB already exists: " + message);
			}
			return message.getBooleanValue("success");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
    }
    //For adding external user from Admin
	public boolean AddExternalUser(String firstname, String lastname, String email, String ib_Mobile, String salesID, String countryCode) {
		String fullPath = this.url + "/user/add_user";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		//String encrypted_email = (String) ((JavascriptExecutor)driver).executeScript("return commonEncryptedString('"+email+"');");
		//String email = "rsa.31f6de72900e1d0afa897446bd72dcf9305943d35ef2387f4907e8a9cb287cf366139a396e2a7944d76e5e7c4a786c7d3051346ccbe2194329e172803688f74bd7a2c50bac3686a55b65ae714de147f95a91e312a12174eac8a758436827690a9f1f717bcff7074a1594c7806a28fcc918a3fcc74392fd193b11dc682c772915dc9920b3d6a0f0c59ee0abcf9890dcc60fa368cf4f800e75ac4323997d34cf9405d4b22c385af2707344e477d6d812fe0ebc8bff403149418f6f75c3d64e59992ce02b85df296bcb3b66911c72b79a4e01dcf3ad2b6de2551a64fcd229473fe44a07d17f19f79e8be561d03c23d25ebf9478ec780ec595a44d4c8df5bd444da4";
		//String ib_Mobile = "rsa.80877c2dffa3831fae6a29ec552179070dfd439d661f5dcc48a854c26347ae0e98bec4f7f29f3ceb125d43f335cc88385d5c3c60f884127bd877d0ffaa0d8b2f94ed14201842628f874b155badd12783338f4609a0e980ec49a4974be6f4042fd450bcd07c3bbd8fb03d7e4c4f6429f7ec620a4f02c3fdcc6ca2c266d1dede08a352e1f3d285f51d8407b578705100c13f00a10e6400c4cf7b51c704c3db08e1617f2cb00f68bd8725d41af3ff0ff7e3b68baf63b667359c0016edfc6e98fcc0dc424c774943b4953e7c3eea6f8045f0ec971526f2eebcc3463aecceb4fcf7e37762ed1dc9b846eab36b1cfacb666ed37d46c7cf29f228b57edf63eb12ab42e1";
		//String acc_first_name = "acc_first_name";
		//String acc_middle_name = "acc_middle_name";
		//String acc_last_name = "acc_last_name";
		
		String body = "ib_Account_type=1&ib_email="+email
				+"&ib_password=123Qwe&mobile_code=1767&ib_Mobile="+ib_Mobile+"&ib_Country="+countryCode+"&ib_street_adress=crmTestStreet&ib_Suburb=crmTestCity&ib_Postcode=crmTestPcode&ib_State=crmTestState&income=7&ib_referto=&referral=&birthday=2004-10-20&ib_accountType=1&ib_clients=0-9&ib_relation=false&ib_rela_broker=&ib_roped=1-5&ib_trading_volume=0-10+Lots&ib_web_relation=false&ib_website=&ib_Mployment=1&ib_Sinvest=12&ib_funds=26&ib_Nationality=1&passport=&bankStatement=&acc_title=Ms&acc_first_name="+firstname
				+"&acc_middle_name=&acc_last_name="+lastname+"&acc_identification_type=Driver+license&acc_identification_number=crmTestIDNumber&acc_tax_us=0&acc_invest_deposit=19&acc_investmentexp_tradew=38&acc_investmentexp_amount_tradew=41&ib_Role=1&ib_Account_Owner="+salesID+"&ib_Affid=";
		
		//System.out.println("header:" + header);
		//System.out.println("body:" + body.toString());
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			System.out.println("==="+message);
			return message.getBooleanValue("success");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
    
	
	//Admin Audit IB API
	public boolean auditAccountIB(String realname, String userId,String accountDBiD,String ib_server,String ib_mt4_set,
			String ib_IB_group,String ib_Currency,String encrypt_email,String encrypt_mobile,String salesID,String sales,String commCode) {
		String fullPath = this.url + "/audit/audit_IB_agree";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("x-requested-with", "XMLHttpRequest");
		
		/*
		 * String body= "user_id="+userId+"&id="+accountDBiD+"&ib_Affid="+testcrmID+
		 * "&ib_level=1&ib_Account_Owner=test+crm&ib_server="+ib_server+"&ib_mt4_set="+
		 * ib_mt4_set+"&ib_IB_group="+ib_IB_group+
		 * "&mt4_account_type=1&ib_trading_group="+ib_IB_group+"&PhoneNumber1="+
		 * encrypt_mobile+"&Mailbox1="+encrypt_email+
		 * "&ib_Leverage=500&ccAuto=false&unionpayAuto=false&ib_Currency="+
		 * ib_Currency+"&commission_code_id="+commCode+"&commission_code_id2="+commCode+
		 * "&Username1="+firstname+"+acc_middle_name+acc_last_name";
		 */
		
		//body without commission code with trading account
		/*
		 * String body= "user_id="+userId+"&id="+accountDBiD+"&ib_Affid="+salesID+
		 * "&ib_level=1&ib_Account_Owner="+sales+"&ib_server="+ib_server+"&ib_mt4_set="+
		 * ib_mt4_set+"&ib_IB_group="+ib_IB_group+
		 * "&mt4_account_type=1&ib_trading_group="+ib_IB_group+"&PhoneNumber1="+
		 * encrypt_mobile+"&Mailbox1="+encrypt_email+
		 * "&ib_Leverage=500&ccAuto=false&unionpayAuto=false&ib_Currency="+
		 * ib_Currency+"&Username1="+realname;
		 */
		//body without commission code without trading account
		String body= "user_id="+userId+"&id="+accountDBiD+"&ib_Affid="+salesID+"&ib_level=1&ib_Account_Owner="+sales+"&ib_server="+ib_server+"&ib_mt4_set="+ib_mt4_set+"&ib_IB_group="+ib_IB_group+
				"&PhoneNumber1="+encrypt_mobile+"&Mailbox1="+encrypt_email+
				"&ccAuto=false&unionpayAuto=false&ib_Currency="+
				ib_Currency+"&Username1="+realname; 

		//System.out.println(fullPath);
		//System.out.println(header);
		System.out.println("==="+body);
		
		//Audit account only if is testcrm user
		//if (Utils.isTestcrmUser(realname)) {
		if (true) {
			try {
				HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
				JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
				if (message.toString().contains("\"success\":true")) {
					System.out.println("New IB audit successful: " + message);
				}else if(message.toString().contains("The record has been processed")){
					System.out.println("IB already exists: " + message);
				}
				
				return message.getBooleanValue("success");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("!!! Not a test crm user.");
		}
		return false;
	}
	
	
	
	//Return the 'Test CRM' user ID on prod env
	public String getPRODTestCRMUserId(BRAND Brand)
	{
		String userId = "";
		
		switch(Brand)
		{
			case VT:
				userId= "80562";
				break;
			case VFX:
				userId= "211943";
				break;
			case PUG:
				userId= "110140";
				break;
			case POSP:
				userId= "830135";
				break;
			case MO:
				userId= "56717";
				break;
			case UM:
				userId= "830429";
				break;
			case AT:
				userId= "3";
				break;			
			default:
				userId= "0";			
		}	
		
		return userId;
	}
	
	
    public boolean isTestGroup(String groupName)
    {
    	boolean flag = false;     	  	
      	if(groupName.contains("test") || groupName.startsWith("t_"))
    	{
    		flag = true;
    	}
    	return flag;
    }
    
    /*test Post update account with account group
     * will also update the owner to Test CRM
     */
	public boolean testPostUpdateAccountGroup(String url, String account, String commId, String newGroupName, String oldGroupName, String ownerId) throws Exception {
		String body="",result="";
		url = url + "/account/update_account";
		
		header.remove("Content-Type");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		if(commId.equals("")) {
			
			body = "login=" + account + "&mt4set=" + newGroupName + "&comment=&mam=&bpayNumber=&affid=&oriMamNum=&ownerId=" + ownerId + "&previousGroup=" + oldGroupName; 
			
		}else {
			body = "login=" + account + "&mt4set=" + newGroupName + "&comment=&mam=&bpayNumber=&affid=&oriMamNum=&ownerId=" + ownerId + "&commission_code_id=" + commId + "&previousGroup=" + oldGroupName; 
		}
		
		try {
			HttpResponse response = httpClient.getPostResponse(url, header,body);
			result = EntityUtils.toString(response.getEntity(),"UTF-8");
			JSONObject message = JSON.parseObject(result);

			if(result.matches(".*success.*true.*")) {
				System.out.println("***Update account " + account + " with group " + newGroupName + " successfully!\n");
			}else {
				System.out.println("!!!Update account " + account + " with group " + newGroupName + " failed!!!");
				
				//TO DO: Alert to Teams
				String alert_msg = this.brand +"."+ this.regulator + " - !!!Update account " + account + " with group " + newGroupName + " failed!!!";
				//System.out.printf(alert_msg);
				teamsNT.SendNotificationToTeams(GlobalProperties.AUTOMATION_UpdateGroups_webhookUrl,alert_msg);
			}
			return message.getBooleanValue("success");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	} 

    //Cash adjustment
	public boolean cashAdjustment(String account, String adjustment, String type, String currency) {
		String fullPath = Utils.ParseInputURL(url) + "account/accountMakeSubmit";
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		String body = "account="+account+"&remark=testcrm&money="+adjustment+"&type="+type+"&currency="+currency;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			JSONObject message = JSON.parseObject(result);
			return message.getBooleanValue("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Test(dataProvider = "testData",description = "admin登录")
	public static void loginAdmin(Map<String,Object> testData){
		String url = testData.get("url").toString();
		REGULATOR regulator = REGULATOR.valueOf(testData.get("regulator").toString());
		String adminUser = testData.get("userName_login").toString();
		String password = testData.get("password_login").toString();
		BRAND Brand = BRAND.valueOf(testData.get("brand").toString());;
		ENV testEnv  = ENV.valueOf(testData.get("testEnv").toString().toUpperCase());
		AdminAPI api = new AdminAPI(url, regulator, adminUser, password, Brand, testEnv);

		String jsId;
		if(api.header.containsKey("Cookie")){
			jsId = api.header.get("Cookie");
			testData.put("jsId",jsId);
			System.out.println(jsId);
		}else {
			System.out.println("\n***** Admin API Login Failed! ");
		}
	}

	public JSONObject apiGet2FASettingList() {
		String path = this.url + "/multifactorial/authentication/list?pageSize=999";

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "" , result );
			assertTrue("true".equals(result.getString("success")) && "successful.".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONArray("data").isEmpty(), "No 2FA setting return.\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiUpdate2FASetting (String settingID, String status) {
		String path = this.url + "/multifactorial/authentication/update";
		JSONObject body = new JSONObject();
		body.put("id",settingID);
		body.put("status",status);

		header.put("Content-Type", "application/json; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
			assertTrue("true".equals(result.getString("success")) && "successful.".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiUpdateLogin2FASetting (String settingID, String functionCode, String note, String ruleName, JSONArray platformList, JSONArray newPlatformAuthMethodsList) {
		String path = this.url + "/multifactorial/authentication/update";
		JSONObject body = new JSONObject();

		body.put("functionCode", functionCode);
		body.put("id", settingID);
		body.put("mandatory", "1");
		body.put("note", note);
		body.put("platform", platformList);
		body.put("platformAuthMethods", newPlatformAuthMethodsList);
		body.put("ruleName", ruleName);

		header.put("Content-Type", "application/json; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
			assertTrue("true".equals(result.getString("success")) && "successful.".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiDisableLoginWithdrawal2FA() {
		//List<String> codeToDisable = Arrays.asList("withdraw", "register-login", "login", "wallet-withdraw");
		Set<String> codeToUpd = new HashSet<>(Arrays.asList("withdraw", "register-login", "login", "wallet-withdraw"));
		update2FAProcess(codeToUpd, false);
	}

	public boolean apiEnableLogin2FA() {
		return updateLogin2FAProcess("login", true);
	}

	public void apiDisableLogin2FA() {
		Set<String> codeToUpd = Set.of("login");
		update2FAProcess(codeToUpd, false);
	}

    public JSONArray apiQuerySecurityRule(){
        String path = this.url + "/security/rule/query_security_rule";

        JSONObject body = new JSONObject();
        body.put("limit",10);
        body.put("pageNo",1);

        try {
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            this.printAPIInfo(brand, regulator, path, body , result );
            assertTrue(result.getBooleanValue("success"), "API Failed!! Path: " + path + "\n" + result);

            return result.getJSONArray("rows");
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void apiSwitchWithdrawLimits(String ruleId,boolean ruleSwitch){
        String path = this.url + "/security/rule/switch";

        JSONObject body = new JSONObject();
        body.put("ruleId",ruleId);
        body.put("ruleSwitch",ruleSwitch);

        header.put("Content-Type", "application/json; charset=UTF-8");

        try {
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            this.printAPIInfo(brand, regulator, path, body , result );
            assertTrue("200".equals(result.getString("code")) && result.getBooleanValue("success"), "API Failed!! Path: " + path + "\n" + result);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyEmail(String userId,String email){
        String path = this.url + "/individual/modifyemail";

        String body = "id=" + userId + "&email="+email+"&send_mail=1";

        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            this.printAPIInfo(brand, regulator, path, body , result );
            assertTrue("200".equals(result.getString("code")) && result.getBooleanValue("success"), "API Failed!! Path: " + path + "\n" + result);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void syncModifyEmail(String userId){
        String path = this.url + "/individual/modifyemail_sync_submit?userId="+userId;

        try {

            httpClient.getPostResponse(path, header, "");
            this.printAPIInfo(brand, regulator, path, null , null );
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

	public void update2FAProcess(Set<String> codeToUpd, boolean bIsEnable) {
		JSONObject setting = this.apiGet2FASettingList();

		if (setting == null) {
			System.out.println("Skip to check on 2FA Setting. Reason: No 2FA record found");
			return;
		}

		JSONArray settingList = setting.getJSONArray("data");
		String expectedStatus = bIsEnable ? "0" : "1";

		for (int i = 0; i < settingList.size(); i++) {
			JSONObject wdSetting = settingList.getJSONObject(i);
			String functionCode = wdSetting.getString("functionCode");

			if (codeToUpd.contains(functionCode)) {
				String settingID = wdSetting.getString("id");
				String status = wdSetting.getString("status");
				GlobalMethods.printDebugInfo("Found record for " + functionCode + ". Current setting status: "+ status);

				if (expectedStatus.equalsIgnoreCase(status)) {
					GlobalMethods.printDebugInfo("Going to " + (bIsEnable ? "enable" : "disable") + " " + functionCode + " 2FA");
					this.apiUpdate2FASetting(settingID, bIsEnable ? "1" : "0");
				} else {
					GlobalMethods.printDebugInfo("Current status is " + (bIsEnable ? "enabled" : "disabled") + ". Not require to perform update.");
				}
			}
		}
	}

	public boolean updateLogin2FAProcess(String codeToUpd, boolean bIsEnable) {
		JSONObject setting = this.apiGet2FASettingList();

		if (setting == null) {
			System.out.println("Skip to check on Login 2FA Setting. Reason: No 2FA record found");
			return false;
		}

		JSONArray settingList = setting.getJSONArray("data");
		String expectedStatus = bIsEnable ? "0" : "1";
		boolean bIsEnableWebPlatform = false, bIsEnableTOTP = false, bIsHasOtherAuthMethod = false;
		JSONArray newPlatformAuthMethodsList = null;

		// Get Login 2FA Setting
		JSONObject login2FASetting = settingList.stream()
				.map(o -> (JSONObject) o)
				.filter(o -> codeToUpd.equalsIgnoreCase(o.getString("functionCode")))
				.findFirst()
				.orElse(null);

		if (login2FASetting == null) {
			System.out.println("Skip to check on Login 2FA Setting. Reason: Login 2FA function code not found");
			return false;
		}

		String settingID = login2FASetting.getString("id");
		String status = login2FASetting.getString("status");
		String functionCode = login2FASetting.getString("functionCode");
		String note = login2FASetting.getString("note");
		String ruleName = login2FASetting.getString("ruleName");
		String mandatory = login2FASetting.getString("mandatory");
		boolean bIsMandatory = "1".equalsIgnoreCase(mandatory);
		JSONArray platformList = login2FASetting.getJSONArray("platform");

		GlobalMethods.printDebugInfo("Found record for " + functionCode + ". Current setting status: "+ status);

		// Applicable when enable login 2fa
		if (bIsEnable) {
			// Get Platform List
			JSONArray platformAuthMethodsList = login2FASetting.getJSONArray("platformAuthMethods");

			// Get Web Platform Setting
			JSONObject webPlatform = platformAuthMethodsList.stream()
					.map(o -> (JSONObject) o)
					.filter(o -> "1".equalsIgnoreCase(o.getString("platform")))
					.findFirst()
					.orElse(null);

			if (webPlatform != null) {
				bIsEnableWebPlatform = true;

				// Get Web Platform authentication method
				JSONArray authMethodsList = webPlatform.getJSONArray("authMethod");
				StringBuilder sb = new StringBuilder();

				for (String val : authMethodsList.toJavaList(String.class)) {
					if ("totp".equalsIgnoreCase(val)) {
						bIsEnableTOTP = true;
					} else {
						bIsHasOtherAuthMethod = true;
					}

					if (sb.length() > 0) sb.append(", ");
					sb.append(val);
				}

				GlobalMethods.printDebugInfo("Found Web Authentication Method: " + sb);
			}

			// Create web platform and add to list when does not exist
			if (!bIsEnableWebPlatform) {
				platformList.add(0, "1");
			}

			// Create web platform setting and add to list when one the below criteria match:-
			// 1. Web Platform does not exist
			// 2. Web Platform authentication does not include TOTP
			// 3. Web Platform authentication method consists of other authentication method
			// 4. Mandatory option is Optional
			if (!bIsEnableWebPlatform || !bIsEnableTOTP || bIsHasOtherAuthMethod || !bIsMandatory) {
				// Add Web Platform Setting to new list
				JSONArray jsonAuthMethods = new JSONArray();
				jsonAuthMethods.add("totp");

				JSONObject jsonAuthMethodPlatform = new JSONObject();
				jsonAuthMethodPlatform.put("platform", "1");
				jsonAuthMethodPlatform.put("authMethod", jsonAuthMethods);

				newPlatformAuthMethodsList = new JSONArray();
				newPlatformAuthMethodsList.add(jsonAuthMethodPlatform);

				// Add the rest platform setting to list
				JSONArray result = new JSONArray();
				platformAuthMethodsList.stream()
						.map(o -> (JSONObject) o)
						.filter(o -> !"1".equalsIgnoreCase(o.getString("platform")))
						.forEach(o -> {
							JSONObject jsonPlatformOthers = new JSONObject();
							jsonPlatformOthers.put("platform", o.getString("platform"));
							jsonPlatformOthers.put("authMethod", o.getJSONArray("authMethod"));

							result.add(jsonPlatformOthers);
						});
				newPlatformAuthMethodsList.addAll(result);
			}
		}

		if (expectedStatus.equalsIgnoreCase(status)) {
			GlobalMethods.printDebugInfo("Going to " + (bIsEnable ? "enable" : "disable") + " " + functionCode + " 2FA");
			this.apiUpdate2FASetting(settingID, bIsEnable ? "1" : "0");
		} else {
			GlobalMethods.printDebugInfo("Current status is " + (bIsEnable ? "enabled" : "disabled") + ". Not require to perform update.");
		}

		if (bIsEnable && newPlatformAuthMethodsList != null) {
			GlobalMethods.printDebugInfo("Update on " + functionCode + " 2FA. ");
			GlobalMethods.printDebugInfo(!bIsEnableWebPlatform ? "Web Authentication: Enable" : "");
			GlobalMethods.printDebugInfo(!bIsEnableTOTP || bIsHasOtherAuthMethod ? "Web Authentication Method: TOTP" : "");
			GlobalMethods.printDebugInfo(!bIsMandatory ? "Set to Mandatory" : "");
			this.apiUpdateLogin2FASetting(settingID, functionCode, note, ruleName, platformList, newPlatformAuthMethodsList);
		}

		return true;
	}

	public void printAPIInfo(BRAND Brand, REGULATOR regulator, String adminPath, Object body, Object result) {
		System.out.println("************Admin API Trigger Details************");
		System.out.printf("%-30s : %s\n","Brand",Brand);
		System.out.printf("%-30s : %s\n","Regulator",regulator);
		System.out.printf("%-30s : %s\n","Method name",Thread.currentThread().getStackTrace()[2].getMethodName());
		System.out.printf("%-30s : %s\n","Admin API URL",adminPath);
		System.out.printf("%-30s : %s\n","Admin API Request",body);
		System.out.printf("%-30s : %s\n","Admin API Response",result);
		System.out.println("*************************************************");

	}

	/**
	 * Print API call info
	 * @param url - api url
	 * @param body - api request body
	 * @param response - api response
	 */
	public void printAPICPInfoPro(BRAND Brand, REGULATOR regulator,String url,Object body, HttpResponse response){
		String methodName;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length >= 3) {
			methodName = stackTrace[2].getMethodName();
		} else {
			methodName = "";
		}
		String eventId = getResponseHeaderValue(response, "Eventid");
		// 5. 保留控制台输出
		LogUtils.info("*********** API call Details ***********");
		System.out.printf("%-30s : %s\n","Brand",Brand);
		System.out.printf("%-30s : %s\n","Regulator",regulator);
		System.out.printf("%-30s : %s\n", "Method Name",methodName);
		System.out.printf("%-30s : %s\n", "API URL", url);
		System.out.printf("%-30s : %s\n", "API Request Headers",  header);
		System.out.printf("%-30s : %s\n", "API Request", body);
		try {
			System.out.printf("%-30s : %s\n", "API Response", EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (Exception e) {
			System.out.printf("%-30s : %s\n", "API Response", "Failed to parse response body: " + e.getMessage());
		}
		System.out.printf("%-30s : %s\n", "API Response EventId：",eventId );
		LogUtils.info("*******************************************");
	}

	public static void main(String args[]) throws Exception {
		
		 //ALPHA
			
			  String url = "https://admin.vantagemarkets.com"; 
				/*
				 * String user = "cmatest"; String password = "123Qwe"; String testcrmID =
				 * "1002365"; String email =
				 * "rsa.2341b3a7dfa89d6711db29252b1fba4ae81150dfe0b9e598e52e487fd0bfb467472c6503a35302377263591cd3ce3431f7428045e00ce4760881a92be6f74efb2383960b8fb39b8aab4e893e0e815f61a30b92f143154c91aceb6b230f80ad8c379b72b0aa53b7629f8662c610ceccc1f3897873e793b7eb441eaebecc7a03f91e1eea8384f18fd33623ba1b655ad43ee2814ab6a38b1ce8bd7bff96cb551a9a0f0be7a1b0faada741fd0c3adbfbf7abd1e4b97504c918a3f948f2f1f371e8678ac339d8efe9979d04943eabfa3013bf0b96cc4b55ea006edb0d100e4f6aa621831e62cc75afb07de7b1e157003d7020ccdc9283c6177068251a3c37d754c111";
				 */
			  //alpha Test CRM's user_id String commCode = "249";
			 
		 
		

			
			  //String url = "https://admin.puprime.com"; 
			  String user = "Test CRM"; String
			  password = "BDAVJECnh4hovtZLV33N"; 

			 

		//String userID = "190784";
		AdminAPI api = new AdminAPI(url,REGULATOR.VFSC2,user,password, BRAND.VFX, ENV.ALPHA);

		String encrypt_email = EncryptUtil.getAdminEmailEncrypt("a.piesciuc@gmail.com"); 
		
		  String account = api.testQueryTradingAccountByEmail(encrypt_email);
		  System.out.println("account is: "+account);
		 
		
		//System.out.println("acc audit id is: "+api.testQueryAccAuditIdByEmail("ghanablacks1@gmail.com"));
		
		/*
		 * if(!account.isEmpty()) { api.testClientUpgradeIB(account, userID); }else {
		 * System.out.println("\n Not able to upgrade IB coz no mt4/5 account found!");
		 * }
		 */
		//API query account audit id by name 
		//String accauditID = api.testQueryAccAuditIdByName("testalexdemo testcrm");

		//api.changeRegulator(REGULATOR.FCA);

	}

	public void setCode(String scode) {
		code = scode;
	}
	public String getCode() {
		return code;
	}

}
