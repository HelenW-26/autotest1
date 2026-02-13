package vantagecrm;  
  
import java.io.IOException;  
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;  
  
import org.apache.http.Header;  
import org.apache.http.HttpEntityEnclosingRequest;  
import org.apache.http.HttpRequest;  
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.HttpRequestRetryHandler;  
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.client.protocol.HttpClientContext;  
import org.apache.http.client.utils.URIBuilder;  
import org.apache.http.config.SocketConfig;  
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;  
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Optional;

 
public class RestAPI {  
  
    private static HttpClient httpClient;  
 
    private static final int MAX_CONNECTION = 100;  

    private static final int MAX_CONCURRENT_CONNECTIONS = 100;  
    //microsecond
    private static final int CONNECTION_TIME_OUT = 80000;  
    //microsecond
    private static final int REQUEST_TIME_OUT = 80000;  
 
    private static final int MAX_FAIL_RETRY_COUNT = 3;  

    private static RequestConfig requestConfig;  
    
    private static CookieStore httpCookieStore;
      
    static {  
        SocketConfig socketConfig = SocketConfig.custom()  
                .setSoTimeout(REQUEST_TIME_OUT).setSoKeepAlive(true)  
                .setTcpNoDelay(true).build();  
        
        requestConfig = RequestConfig.custom()  
                .setSocketTimeout(REQUEST_TIME_OUT)  
                .setConnectTimeout(CONNECTION_TIME_OUT).build();  
 
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();  
        connManager.setMaxTotal(MAX_CONNECTION);  
        connManager.setDefaultMaxPerRoute(MAX_CONCURRENT_CONNECTIONS);  
        connManager.setDefaultSocketConfig(socketConfig);  
  
        httpClient = HttpClients.custom().setConnectionManager(connManager).setUserAgent("Mozilla/5.0 CRM-Automation")
  
                .setRetryHandler(new MyHttpRequestRetryHandler()).setDefaultCookieStore(httpCookieStore).build();  
    }  
  


    /** 
     * retry
     * @author manzhizhen 
     * 
     */  
	
	  public static class MyHttpRequestRetryHandler implements
	  HttpRequestRetryHandler {
	  
	  @Override
	  public boolean retryRequest(IOException exception, int executionCount,
	  HttpContext context) {
	  if (executionCount >= MAX_FAIL_RETRY_COUNT) {
	  return false;
	  }
	  
	  if (exception instanceof InterruptedIOException) {
	  
	  return false;
	  }
	  if (exception instanceof UnknownHostException) {
	  
	  return false;
	  }
	  if (exception instanceof ConnectTimeoutException) {
	  
	  return false;
	  }
	  if (exception instanceof SSLException) {
	  // SSL handshake exception
	  return false;
	  }
	  
	  HttpClientContext clientContext = HttpClientContext.adapt(context);
	  HttpRequest request = clientContext.getRequest();
	  boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
	  if (idempotent) {
	  
	  return true;
	  }
	  
	  return false;
	  }
	  }
	  
    //Post MailLogList method
    public static String testPostMailLogList(String url, String cookie) throws Exception {
    	String result=null;
        url = url.concat("admin/mail/mailLogList");  
        System.out.println("url is: "+url);

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"createTime\":{\"mandatory\":true,\"filterType\":\"DATEPICKER\",\"input\":{\"startDate\":\"2020-10-06 05:00:00\",\"endDate\":\"2020-10-06 23:59:59\"}},\"toMail\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"mt4Account\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"subject\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"templateInvokeName\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"status\":{\"filterType\":\"SELECT\",\"input\":null},\"reSendCount\":{\"filterType\":\"INPUT\",\"input\":\"\"}}}"; 

		result = commonPostAPI(url,header,body);
		return result;
    } 
 
    
    //test Post ResendMailSubmit method
    public static void testPostResendMailSubmit(String url, String cookie, String uid, @Optional("") String testStamp, @Optional("") String emailTmp) throws Exception {
    	//System.out.println("---->>>>Entering testPostResendMailSubmit");
    	url = url.concat("admin/mail/resendMailSubmit");
        System.out.println("url is: "+url);
        
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		//String body = "id="+uid+ "&subject=M-Regulator SVG EmailTest1011--"+ emailTmp;
		
        String body = "id="+uid+ "&subject=" + testStamp + "--"+ emailTmp;
       
        //testCRM email
       // body = body +"&toMail=6c56a32a422c1099f7f56ea24f3ca5d9b7e562721a88128cbf6909da621cb1ce8e54434972ea679a9b00bd56d6755d3c0a566cfebb02a44d00e805a56c50c2293bc918d820c99d264b427fef62983720cec964db2d7e358a50298ff01fdf8386235106907a373c7dd82a3f5a55a8bc595319a1bc86055035b870476a16ed0c3c";
        
        //shan.liu email
		body = body +"&toMail=06a506a7e4b492a7f3249cbd05d8a4871d6ffef2317f24d52aa138003854989a6d346fdb8d688092fbde8d3aff2e14e94c5da206f9f5a6be321b7c3e6575ca05bd88dd696c83a7fa4c62903b45a41e8beb431a15ca61496757f63407fc17529e8c0f519cd43ce2f60b0e574bc8d26813884c74ca78d822ee4c741e8f5a9db63a"; 

        //Yanni Qi email
        //body = body +"&toMail=11d97b435102dcd26bbc7d1f479ffeca7aeaaff0858f2166e002d87ee8f28f9802ea45944847559a44ad33ad4b92c08d7783eeee48347d15887bc866a85fb23ada412de901cff55379d9f15e41ed450a7a77b8c9c8fdcf4730eef5573f6a1bb917a9c873d38815a8e2b9359d2eb772be767f28368c3e3c009583536c3eca4fd7";
		
        //Fiona email
      	//body = body +"&toMail=54a3b1a65795051d26ab018064283d72fe2ef2d1589884eca3f26326347be75df6bdda199666a64d2dbb9c9a646179f271d900a05ef503d4be1e0427f7f7e93b92318c3fe45bb7b388ad3145c7d17abde264df0104d3d5565c7cefdaa75b5e42c80953a9d5ddb816b2156bdb746a343f15a084c43639e5d4d81c4f61dd296a1a";
        
      	// Fiona gmail
        //body = body + "54a3b1a65795051d26ab018064283d72fe2ef2d1589884eca3f26326347be75df6bdda199666a64d2dbb9c9a646179f271d900a05ef503d4be1e0427f7f7e93b92318c3fe45bb7b388ad3145c7d17abde264df0104d3d5565c7cefdaa75b5e42c80953a9d5ddb816b2156bdb746a343f15a084c43639e5d4d81c4f61dd296a1a";
       
        //Yanni's gmail
		/* body = body + "hIish9C2gXgk4ltD8v0bmW9o9kWHhVUI"; */
        
		//Michelle's Email
		//body = body +"&toMail=25a247f2c3db47cc0069c4fc783023360cb6fc65dae02a8ba91b5b12426957a45ee9aec2c65171819eaa36c3b28ed09b5f5c788d2b4e49619b22a5867bf658e9f64717cc0a5f5d4cc738f2dee8115248657c211bb1117922328f621b11ad602f6e968176bf927cb7861ca26516be65d0f9e980aa2fda14dba4402832dd954595";
		
		String result = commonPostAPI(url,header,body);
		
		if(result.matches(".*success.*true.*")) {
			System.out.println("***Success");
		}else {
			System.out.println("!!! Sending FAILED");
		}
    } 
 
  //test Post DirectResendMail method
    public static String testDirectResendMail(String url, String cookie, String uid) throws Exception {
    	//System.out.println("---->>>>Entering testDirectResendMail");
    	url = url.concat("admin/mail/resendMailSubmit");
        System.out.println("url is: "+url);
        
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		
        String body = "id="+uid;
       
        String result = commonPostAPI(url,header,body);
		
        return result;
    } 

    //test Post MailLogList method
    public static String testPostWithdrawCPSList(String url, String orderNo, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("payment/withdraw/getPaymentWithdrawCPSList");  
    	
    	//Get current date
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String dateFormatted= dateFormat.format(new Date());

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		//String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"statusQuery\":\"-1\",\"userQuery\":\"" + orderNo + "\",\"typeQuery\":\"-1\",\"searchType\":\"3\",\"dateType\":\"1\",\"startDate\":\"2019-04-01\",\"endDate\":\""+dateFormatted+"\"}}"; 
		String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"statusQuery\":\"-1\",\"userQuery\":\"" + orderNo + "\",\"typeQuery\":\"-1\",\"searchType\":\"3\",\"dateType\":\"0\",\"startDate\":\"2019-04-01\",\"endDate\":\""+dateFormatted+"\"}}"; 

		
		result = commonPostAPI(url,header,body);
		return result;
    } 
  
    //test Post tradeAccount/trade_accounts method
    public static String testPostTradeAccountList(String url, String cookie) throws Exception {
    	String result=null;
    	if(url.substring(url.length() - 1).equals("/")) {
    	    url = url.concat("tradeAccount/trade_accounts");
    	}else {
    		url = url.concat("/tradeAccount/trade_accounts");
    	}
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "{\"limit\":25,\"pageNo\":1,\"offset\":0,\"search\":{},\"order\":\"asc\"}"; 
		
		result = commonPostAPI(url,header,body);
		return result;
    } 
    
  //test Post tradeAccount/trade_accounts method
    public static String testPostAdminQueryAccountList(String url, String cookie, String account) throws Exception {
    	String result="";
    	String[] server= Utils.getDataSourceId(""); 
    	
		String body ="{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"actact1000\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
    	
    	if(url.substring(url.length() - 1).equals("/")) {
    	    url = url.concat("account/query_accountList");
    	}else {
    		url = url.concat("/account/query_accountList");
    	}
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		//check for all the 6 servers
		for (String dataSource : server) {
		    //String body = "{\"search\":{\"search_type\":\"account\",\"userQuery\":\""+account+"\",\"dataSource\":\""+dataSource+"\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}"; 
		    String bodyNew = body.replace("dsds5", dataSource).replace("actact1000", account);
			
			result = commonPostAPI(url,header,bodyNew);
		    String result1 = result.replace("\"","");
		    System.out.println(result1);
		    if (result1.contains(account)) {
		    	System.out.println("this is it!");
		    	break;
		    }
		}
		return result;
    } 
    
    /* Post for resetting mt4/5 password for tradeAccount
     * url should be admin url ends with "/"
     * */
    public static String testPostAdminResetPasswordSubmit(String url, String cookie, String account, String Brand) throws Exception {
    	String result="";
    	//String[] server= {"5","2","3","8","11","12","10"};
    	String[] server=Utils.getDataSourceId(Brand);
    	String pw1 = "12345678Qi";
    	String pw2 = "123456789Qi";
    	
    	
		HashMap<String, String> header = new HashMap<String, String>();
		url = url.concat("account/resetPasswordSubmit");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		//check for all the 6 servers
		for (String dataSource : server) {
		    String body = "login="+account+"&master="+pw1+"&investor="+pw2+"&sendMailFlag=false&dataSourceId="+dataSource; 
		    result = commonPostAPI(url,header,body);
		    if (result.contains("true")) {
		    	System.out.println("Succeed: MT4/5 Passwords pair changed by API to: 12345678Qi/123456789Qi");
		    	break;
		    }else {
		    	System.out.println("Account not in this server. Go on next loop!");
		    	//System.out.println(result);
		    }
		}
		if (result.equals("")) {
			System.out.println("Failed: Not able to use API to reset password for account: "+account);
		}
		return result;
    }
    
    /* Post for resetting trader/client-portal login password
     * url should be admin url ends with "/"
     * */
    public static String testPostResetLoginPassword(String url, String cookie, String email, String id) throws Exception {
    	String result="";
    	String pw = "MTIzUXdl",repw = "123Qwe";
    	
		HashMap<String, String> header = new HashMap<String, String>();
		url = url.concat("individual/modifyemail");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		
		String body = "id="+id+"&password="+pw+"&repassword="+repw+"&email="+email; 
		//System.out.println(body);
		result = commonPostAPI(url,header,body);
		if (result.contains("true")) {
		 	System.out.println("Login Password reseted by API to: 123Qwe");
		}else {
		   	System.out.println("Login Password reseted failed!");
		   	//System.out.println(result);
		}
		
		return result;
    }
    
    /* Post for query user id by user name
     * url should be admin url ends with "/"
     * */
    public static String testQueryUserIdByName(String url, String cookie, String name) throws Exception {
    	String result="",userID="";
    	url =url.concat("audit/query_user_list");
    	//System.out.println("url is: "+url);
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"1\",\"userQuery\":\""+name+"\"}}"; 
		result = commonPostAPI(url,header,body);
		if (result.contains("user_id")) {
		 	//System.out.println(result);
			JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            JSONArray data = (JSONArray) json.get("rows");

            //Limit the print data column to 6 and add is_del column
	        for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get user_id
	            	if (key.toString().equals("user_id")) {
	            		userID=value.toString();
	            	}
	            }
	        }    
		}else {
		   	System.out.println("Query user id failed!");
		   	//System.out.println(result);
		}
		
		return userID;
    }

    
    
    /* Post for getting Admin login cookie
     * Updated by Alex Liu 12/03/2023 for 24h OTP support
     * */
    public static String testPostForAdminCookie(String urlbase, String AdminName, String AdminPass, String Brand, String Regulator, String TestEnv) throws Exception {
    	
    	String result="",passwd="", code="",cookie="";
    	HttpResponse response=null;
    	
    	//defaultRegulator: Internal User's default regualtor; It decides which DB we will use to look for verification code
    	String defaultRegulator=""; 
    	
    	urlbase = urlbase.concat("login/to_login");  
      	
  		HashMap<String, String> header = new HashMap<String, String>();
  		header.put("Content-Type", "application/x-www-form-urlencoded");
  		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(urlbase));
  		
  		
  		passwd = Utils.funcMD5Encrypt(AdminPass);
		//System.out.println("password_login: "+passwd);
		String body = "userName_login="+AdminName+"&password_login="+passwd+"&utc=39600000";
  		
		//Login
		response = RestAPI.rawPostAPI(urlbase,header,body);
		
		//Save the cookie for use
		Header[] cookies = response.getHeaders("Set-Cookie");
		for (Header s:cookies) {
		  //System.out.println("cookies : "+s.toString());
		  if (s.toString().contains("jsId")) {
			  result = s.toString();
			  cookie = result.substring(result.indexOf("jsId"),
			  result.length());
		  } 
		}
	
		if(TestEnv.equalsIgnoreCase("PROD")) {
	        //Read default regulator from DB
			defaultRegulator = DBUtils.getDefaultRegulator(AdminName, Brand, TestEnv,Regulator);
	
	        //Remove the path from admin login url
	        urlbase = Utils.ParseInputURL(urlbase);
	        
	        //API trigger OTP and read OTP code from DB
			code = testPostAdminOTPCode(urlbase, cookie, TestEnv, Brand, defaultRegulator, AdminName);
			  
	    	//re-send login to activate the cookie
	    	urlbase = urlbase.concat("login/to_login");
	    	body = "userName_login="+AdminName+"&password_login="+passwd+"&utc=39600000&code="+code;  		
			response = RestAPI.rawPostAPI(urlbase,header,body);
	        //System.out.println("\n"+response);
		}
    	//Get login response status code
        int status = response.getStatusLine().getStatusCode();
        System.out.println("\nstatus code for API admin login is: "+status);
        
    	//The cookie has been validated.
		return cookie;
    }
    
    //test Post making adjustment method
    public static String testPostAccountMakeSubmit(String url, String account, String amount, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("account/accountMakeSubmit");  

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "account="+account+"&remark=Transfer+In&remarkType=1&money="+amount+"&type=1";
	
		result = commonPostAPI(url,header,body);
		return result;
    } 
    
    
    //test Post Client Data Search by account
    public static String testPostClientDataSearch(String url, String account, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("sale/query_clientDataSearch");  

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"mt4Account\",\"keyword\":\""+account+"\"}}";
	
		result = commonPostAPI(url,header,body);
		return result;
    } 
    
    /*test Post modify_malaysia_channel_setting method
    	channel:  	1    ZotaPay
    		  		2    PayTrust*/
    public static void testPostMalaysiaChannelSetting(String url, String cookie, String channel) throws Exception {
    	if(url.substring(url.length() - 1).equals("/")) {
    	    url = url.concat("config/payment/modify_malaysia_channel_setting");
    	}else {
    		url = url.concat("/config/payment/modify_malaysia_channel_setting");
    	}
    	
    	//System.out.println("url is: "+url);
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "new_method=" + channel; 
		
		commonPostAPI(url,header,body);
    } 
    
    /*test Post modify_thailand_channel_setting method
	channel:  	1    ZotaPay
		  		2    PaytoDay*/
	public static void testPostThaiChannelSetting(String url, String cookie, String channel) throws Exception {
		if(url.substring(url.length() - 1).equals("/")) {
		    url = url.concat("config/payment/modify_thailand_channel_setting");
		}else {
			url = url.concat("/config/payment/modify_thailand_channel_setting");
		}
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "new_method=" + channel; 
		
		commonPostAPI(url,header,body);
	} 

	
	
    //test Post require CP deposit token
    public static String testPostRequireToken(String url, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("cp/api/deposit/require_token");  

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		String body = "";
	
		result = commonPostAPI(url,header,body);
		return result;
    }
	
    
    
    /*test Post update account with Bpay number*/
	public static void testPostUpdateAccBpayNo(String url, String cookie, String account) throws Exception {
		url = url.concat("account/update_account"); 
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		String body = "login=" + account + "&bpayNumber=12345"; 
		
		commonPostAPI(url,header,body);
	} 

    /*test Post update country by id using admin cookie*/
	public static void testPostUpdateCountry(String url, String cookie, String id, String countryCode) throws Exception {
		url = url.concat("individual/update_individual"); 
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "*/*");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		String body = "id="+ id +"&password=&country="+countryCode+"&ib_Nationality=5&state_input=22&ib_funds=Savings+from+employment+income&firstBirthday=1991-06-13"; 
		
		commonPostAPI(url,header,body);
	} 
	
	
    /* Added by Alex L for testing Post query_permission_tree in admin->system settings->role management
     * Only need to check the Super Admin(role_id = 1)'s role display
     * */
    public static List<String> testPostQueryPermissionTree(String url, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("admin/role/query_permission_tree");  
    	int i=1;
    	List<String> founds = new ArrayList<>();
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "*/*");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));

		String body = "role_id=1",name=""; 
		
		result = RestAPI.commonPostAPI(url,header,body);
		
		if (result.contains("name")) {
	
			JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(result);

            for (Object item : json) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get key's name
	            	if (key.toString().equals("name")) {
	            		name=value.toString();
	            		//System.out.print(i+ " "+name+"\n");
	            		i++;
	            		if (name.contains("[")||name.contains(":")) {
	            			System.out.print(i+ " "+name+"\n");
	            			founds.add(name);
	            		}
	            	}
	            }
	        }

                
		}else {
			Assert.assertTrue(false, "Query user id failed!");
		   	//System.out.println(result);
		}
		
		return founds;
    } 
    
    
    /* Added by Alex L 04/06/2020
     * Post for querying user's commission ID by account
     * url should be admin url ends with "/"
     * */
    public static String testPostQueryCommissionIDByAccount(String url, String cookie, String accountNo) throws Exception {
    	String result="",commId="";
    	url =url.concat("audit/query_user_list");
    	//System.out.println("url is: "+url);
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		
		
		String body = "{\"search\":{\"statusQuery\":\"-1\",\"typeQuery\":\"-1\",\"searchType\":\"2\",\"userQuery\":\""+accountNo+"\"}}"; 
		//System.out.println("bodyYanni:" + body);
		result = commonPostAPI(url,header,body);
		if (result.contains("commission_code_id")) {
		 	//System.out.println(result);
			JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            JSONArray data = (JSONArray) json.get("rows");

            //Limit the print data column to 6 and add is_del column
	        for (Object item : data) {  
	            for (Object key: ((JSONObject) item).keySet()) {
	            	Object value = ((JSONObject) item).get(key.toString());
	            	//Get commission_code_id
	            	if (key.toString().equals("commission_code_id")) {
	            		commId=value.toString();
	            	}
	            }
	        }    
		}else {
		   	System.out.println("No commission_code_id in query_user_list API of specified brand!");
		   	//System.out.println(result);
		}
		
		return commId;
    }
    
    
    /*test Post update account with account group
     * will also update the owner to Test CRM
     */
	public static void testPostUpdateAccountGroup(String url, String cookie, String account, String commId, String newGroupName, String oldGroupName, String ownerId) throws Exception {
		String body="",result="";
		url = url.concat("account/update_account"); 
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		if(commId.equals("")) {
			
			body = "login=" + account + "&mt4set=" + newGroupName + "&comment=&mam=&bpayNumber=&affid=&oriMamNum=&ownerId=" + ownerId + "&previousGroup=" + oldGroupName; 
			
		}else {
			body = "login=" + account + "&mt4set=" + newGroupName + "&comment=&mam=&bpayNumber=&affid=&oriMamNum=&ownerId=" + ownerId + "&commission_code_id=" + commId + "&previousGroup=" + oldGroupName; 
		}
		
		result = commonPostAPI(url,header,body);
		if(result.matches(".*success.*true.*")) {
			System.out.println("***Update account " + account + " with group " + newGroupName + " successfully!\n");
		}else {
			System.out.println("!!!Update account " + account + " with group " + newGroupName + " failed!!!");
		}
	} 
	
  
    /* Added by Alex L 07/10/2020
     * Post for requesting admin login verification code
     * url should be admin url ends with "/"
     * */
    public static String testPostAdminVerificationCode(String url, String cookie, String TestEnv, String Brand, String AdminName) throws Exception {
    	String result="",code="";
    	String codeBrand="";
    	String rurl =url.concat("admin/verificationCode");
    	//System.out.println("url is: "+url);
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "userName_login="; 
		result = commonPostAPI(rurl,header,body);
		
		if (result.contains("success")) {
		 	System.out.println("\nRequesting admin login verification code succeeds.");
			
		 	//Going to fetch the verification code.
		 	//code = DBUtils.funcReadVerifyCodeInBusinessDB(TestEnv, Brand);
		 	code = DBUtils.funcReadSMSCodeInGlobalDB(TestEnv, Brand, AdminName);
		 	
		 	//To extract code with regular expressions
		 	Pattern p = Pattern.compile("[0-9]{6}");		 	
		    Matcher m = p.matcher(code);
		    
	        // if an occurrence if a pattern was found in a given string...
	        if (m.find()) {
	            code =m.group(0); // second matched digits
	            System.out.println("code: "+code);
	        }else
	        {
	        	System.out.println("tb_sms_history record doesn't have verification code. The body is:" + code);
	        }
	
		 	
		 	
		}else {
		   	System.out.println("\nRequesting admin login verification code failed!!!");
		   	//System.out.println(result);
		}
		
		return code;
    }
    
    
    /* Added by AlexL 10/03/2023
     * Post for requesting admin 24h login verification code
     * url should be admin url ends with "/"
     * */
    public static String testPostAdminOTPCode(String url, String cookie, String TestEnv, String Brand, String regulator, String AdminName) throws Exception {
    	String result="",vars="", code = "";
    	String rurl =url.concat("mfa/sendEmailVerificationCode");
    	//System.out.println("url is: "+rurl);
    	
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "{\"userName\":\"" + AdminName + "\",\"namespace\":\"AP_LOGIN\"}"; 
		result = commonPostAPI(rurl,header,body);

		if (result.contains("code\":0")) {
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
	        }else
	        {
	        	System.out.println("Couldn't find verification code. The body is:" + vars);
	        }
		 	
		}else {
		   	System.out.println("\nRequesting admin login verification code failed!!!");
		   	//System.out.println(result);
		}
		
		return code;
    }
    
    /* Added by Alex L 07/10/2020
     * Post for checking admin login verification code
     * url should be admin url ends with "/"
     * */
    public static void testPostCheckVerificationCode(String url, String cookie, String code) throws Exception {
    	String result=null;
    	String rurl = url.concat("admin/checkVerificationCode"); 
    	//System.out.println("url: "+url);

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("cookie", cookie);
		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		header.put("Host", Utils.ParseAdminURLtoHost(url));
  		
		String body = "userName_login=&sendMessageCode=" + code;
	
		//System.out.println("body: "+body);
		result = commonPostAPI(rurl,header,body);
		
		if(result.contains("success")) {
			System.out.println("\n***Check verification code successfully!");
		}else {
			/*
			 * comment out print message because the failure doesn't affect the
			 * result.
			 * It usually fails when no need to verify the login code.
			 */
			System.out.println("\nCheck verification code failed. \n"
					+ "* Please notice that the failure doesn't affect the result. It usually fails when no need to verify the login code.");
		}
		
    } 
	  
    
    /*Added by Alex Liu 12/10/2020
     * for decrypt email. Please be aware of the special characters in the decrypt email. 
     * Only "+" and "/" have been handled. If you found the print result of "*** Decrypt Email"
     * is still a decrypt one. That means a new special character needs to be handled by code.
     */
    public static String testPostDecryptEmail(String email, String url, String cookie) throws Exception {
    	String result=null;
    	url = url.concat("safe/decrypt/decryptEmailOrPhone");  

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie", cookie);
  		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		
		//for handling the special character like "+" and "/"
		email = email.replace("+","%2B").replace("/","%2F");
		String body = "email=" + email + "&phone=&userId=&mt4Account=";
		//System.out.println("\n***   body is   *** " + body);
		result = commonPostAPI(url,header,body);
		
		if (result.contains("success")) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(result);
			JSONObject data = (JSONObject) json.get("data");
			email = (String) data.get("email");
//			System.out.println("\n***   Decrypt Email is   *** " + email);

		}else {
			System.out.println("\n!!!   Decrypt Email failed   !!!");
		}
		return email;
    }
    
    
    
    /*Added by Alex Liu 13/10/2020
     * for switching regulator to specified one.
     * e.g. RestAPI.testPostSwitchRegulator(Brand, AdminURL, cookie);
     */
    public static void testPostSwitchRegulator(String Brand, String url, String cookie) throws Exception {
    	String result=null,regulator="";
    	switch(Brand)
		{
			case "au":
			case "ppau":
				regulator = "ASIC";
				break;
			case "vfsc":
			case "movfsc":
				regulator = "VFSC";
				break;
			case "vfsc2":
				regulator = "VFSC2";
				break;
			case "fca":
				regulator = "FCA";
				break;
			case "fsa":
				regulator = "FSA";
				break;
			case "svg":
			case "vtsvg":
			case "um":
				regulator = "SVG";
				break;
			case "vt":
			case "ky":
			default:
				regulator = "CIMA";
		}
    	String rurl = url.concat("admin/switch-regulator?regulator=" + regulator);  

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Cookie", cookie);
		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
  		header.put("Host", Utils.ParseAdminURLtoHost(url));
		
		String body = "";
		result = commonPostAPI(rurl,header,body);
		if(result.equals("")) {
			System.out.println("***Switch regulator to " + regulator + " successfully!");
		}else {
			System.out.println("!!!Switch regulator to " + regulator + " failed!!!");
		}
    }
    
    // Added by Alex Liu for sending Skrill callback - 13/05/2021
	public static void testPostSkrillCallback(String url, String orderNo) throws Exception {
		if(url.substring(url.length() - 1).equals("/")) {
		    url = url.concat("skrill/receiveWebDepositResult");
		}else {
			url = url.concat("/skrill/receiveWebDepositResult");
		}
		
		System.out.println("\nGoing to send callback to " + url);
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		String body = "transaction_id=" + orderNo + "&status=2"; 
		
		commonPostAPI(url,header,body);
	}
	
	
    public static String getDevOPSNo(String dbName, String testEnv)
    {
    	String dbNo = "";
    	
       	switch(dbName)
    		{
    			case "dev_db_asic_vgp":        //Local ASIC business db
    			case "asic_business_beta":   //Alpha ASIC business db
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="146";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="144";
    				}
    				break;
    			case "dev_db_cima_vgp":  //Local CIMA Business DB
    			case "au_business_beta": //Local Alpha Business DB
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="147";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="73";
    				}
    				
    				break;
    			case "dev_db_au_global_vgp": //Local ASIC&CIMA GLOBAL db
    			case "au_global_beta":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="148";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="74";
    				}
    				break;
//    			case "vt_global_db":
    			case "dev_vt_m_regulator_global":  //Local VT Global DB Name
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="177";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="508";
    				}
    				
    				break;
    			case "vt_global_beta":  //Alpha VT Global DB Name
    			case "dev_vt_mt5_global":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="177";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="71";
    				}
    				
    				break;
//    			case "vt_business_db":
    			case "dev_vt_m_regulator_business":  //Local VT Business DB Name
      				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="176";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="506";
    				}
    				break;
    			case "dev_vt_m_svg_business":  
      				if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="804";
    				}
    				break;
    			case "vt_business_beta": //Alpha VT business DB Name
    			case "dev_vt_mt5_business":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="176";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="70";
    				}
    				break;
    			case "db_mt4_vt_business": //Local VT replicate DB
    			case "mt4vt": //Alpha VT replicate DB

    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="76";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="76";
    				}else {
    					dbNo="76";
    				}
    				break;
    			case "db_mt4_vt2": //Local VT replicate DB

    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="302";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="302";
    				}else {
    					dbNo="302";
    				}
    				break;
    			case "db_mt4_uk3":
    			case "mt4uk3":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="186";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="186";
    				}else {
    					dbNo="186";
    				}
    				break;
    			case "db_mt4_au3":
    			case "mt4au3":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="185";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="185";
    				}else {
    					dbNo="185";
    				}
    				break;
    			case "db_mt4_au4":
    				dbNo="530";
    				break;
    			case "db_mt4_uk4":
    				dbNo="531";
    				break;
    			case "db_mt4_vantage_uk2":
    			case "mt4uk2":	
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="143";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="143";
    				}else {
    					dbNo="143";
    				}
    				break;
    			case "db_mt4_vantage_au2":
    			case "mt4au2":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="142";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="142";
    				}else {
    					dbNo="142";
    				}
    				break;
    			case "db_mt4_vantage_uk":
    			case "mt4uk":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="81";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="81";
    				}else {
    					dbNo="81";
    				}
    				break;
    			case "db_mt4_vantage_au":
    			case "mt4au":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="79";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="79";
    				}else {
    					dbNo="79";
    				}
    				break;
    			case "mt5_vfx_live":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="84";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="84";
    				}else {
    					dbNo="401";
    				}
    				break;
    			
    			case "dev_pug_cm1972_global":
    			case "dev_pug_m_reg_fsa_global":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="175";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="487";
    				}
    				break;
    			case "pug_global_beta":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="175";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="153";
    				}
    				
    				break;
    			case "dev_pug_cm1972_fsa":
    			case "dev_pug_m_reg_fsa_business":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="174";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="486";
    				}
    				break;
    			case "dev_pug_cm1972_svg":
    			case "dev_pug_m_reg_svg_business":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="381";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="488";
    				}
    				break;
    			case "pug_business_beta":	
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="174";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					dbNo="152";
    				}
    				break;
				/* Legacy DB
				 * case "db_mt4_pug_business_new":
				 * dbNo="250";
				 * break;
				 */
    			case "db_mt4_pui":
    				dbNo="473";    			
    				break; 	
    			case "mt5_pug_live":
    				dbNo="504";    			
    				break; 	
    			case "mt4pug":
    				dbNo="151";
    				break;
    			case "dev_m_regulator_global":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    //dbNo="234";  //m_regulator_global, will be removed
    				    dbNo="148";  //au_globle_beta: real Beta global db
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					//dbNo="74";
    					dbNo="491";
    				}
    				break;
    			case "dev_m_regulator_cima":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    //dbNo="233";  //m_regulator_cima, will be removed
    				    dbNo="147";  //au_business_beta: real CIMA Beta db
    				}else if (testEnv.equalsIgnoreCase("beta1209")) {
    					dbNo="479";
    				}else if (testEnv.equalsIgnoreCase("betanew")) {
    					dbNo="4";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					//dbNo="73";
    					dbNo="493";
    				}
    				break;
    			case "dev_m_regulator_asic":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    //dbNo="232";  //??
    				    dbNo = "146";   //asic_business_beta: real ASIC Beta db
    				}else if (testEnv.equalsIgnoreCase("beta1209")) {
    					dbNo="475";
    				}else if (testEnv.equalsIgnoreCase("betanew")) {
    					dbNo="363";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					//dbNo="144";
    					dbNo="490";
    				}
    				break;
    			case "dev_m_regulator_vfsc":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="265";
    				}else if (testEnv.equalsIgnoreCase("beta1209")) {
    					dbNo="481";
    				}else if (testEnv.equalsIgnoreCase("betanew")) {
    					dbNo="369";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					//dbNo="252";
    					dbNo="495";
    				}
    				break;
    			case "dev_m_regulator_vfsc2":
    				if(testEnv.equalsIgnoreCase("prod")) {
    	
    					dbNo="655";
    				}
    				break;
    			case "dev_m_regulator_fca":
    				if(testEnv.equalsIgnoreCase("beta")) {
    				    dbNo="300";
    				}else if (testEnv.equalsIgnoreCase("prod")) {
    					//dbNo="301";
    					dbNo="494";
    				}
    				break;

    				
    			case "prospero_global":
    				if(testEnv.equalsIgnoreCase("prod")) {
    	
    					dbNo="642";
    				}
    				break;
    			case "prospero_asic_business":
    				if(testEnv.equalsIgnoreCase("prod")) {
    				    dbNo="641";
    				}
    				break;
    			case "prospero_svg_business":
    				if(testEnv.equalsIgnoreCase("prod")) {
    				    dbNo="653";
    				}
    				break;
    			case "mo_vfsc_business":
    				if(testEnv.equalsIgnoreCase("prod")) {
    				    dbNo="770";
    				}
    				break;
    			case "mo_global":
    				if(testEnv.equalsIgnoreCase("prod")) {
    				    dbNo="769";
    				}
    				break;
    		}
       	
    	return dbNo;
    }
    
 //Extracted by Yanni on 16/12/2020: prepare API data, query DevOPS and return the result.
    public static String queryDevOPS(String dbName, String sql, String testEnv) throws Exception
    {
    	String result="",dbNo="";
    	dbNo = getDevOPSNo(dbName,  testEnv);
    	
        String url = "http://devops_new.tianyitechs.com/api/cmdb/database/db_execute_search/"+dbNo+"/";  
        //System.out.println("usrl is: "+url);
        
        String token = getTianyitechsToken(); 
        
        //Config request body
        String body = "{\"sql\":\""+sql+"\"}";
        
        HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		header.put("Authorization", "token "+token);
		
        System.out.println("=========================Query for "+dbName+"============================");

        //System.out.println("url:" + url + "; header:" + header + " body:" + body);
        result = commonPostAPI(url,header,body);
        
        return result;

    }
    
    //test sql Post db_execute_search method
	public static String APIReadDB(String dbName, String sql, String testEnv) throws Exception {
    	String result="",userID="";
    	Map<String, String> map = new HashMap<String, String>();
    	//Map<Integer, String> map = new HashMap<Integer, String>();
    	
    	result =  queryDevOPS( dbName, sql, testEnv);    	
  
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        JSONArray data = (JSONArray) json.get("data");

        //Limit the print data column to 6 and add is_del column
        for (Object item : data) {  
            for (Object key: ((JSONObject) item).keySet()) {
            	Object value = ((JSONObject) item).get(key.toString());
            	//System.out.println(value);
            	  
            	//In case some key's value is null
            	if (value == null) {
            		map.put(key.toString(), "");
            	}else {
            	    map.put(key.toString(), value.toString());
            	}
            	
            	//Get user_id
            	if ((key.toString().equals("id"))||(key.toString().equals("user_id"))||(key.toString().equals("login"))) {
            		userID=value.toString();
            	}

            	//add is_del column
            	try {
            		map.put("is_del", ((JSONObject) item).get("is_del".toString()).toString());
            	}catch(NullPointerException e) {
            		//System.out.println("is_del nto exist in this table");
            	}
    		}
            System.out.println("map: "+map);
        }	
   
        
        //If userID not found in table, then return all the result 
        if (userID.equals("")) {
        	userID=map.toString();
        }
        return userID;
    }
    

    //test sql Post db_execute_search method
	public static String APIReadDBReturnAll(String dbName, String sql, String testEnv) throws Exception {
		
    	String result="";       	
    	result =  queryDevOPS( dbName, sql, testEnv);    
   
        return result;
    }

    //Get login token for http://devops_new.tianyitechs.com/cmdb
    public static String getTianyitechsToken() throws Exception {
   	   //System.out.println("test starts");
   	   HttpEntity entity = null;
   	   String result="";
       String url = "http://devops_new.tianyitechs.com/api/cmdb/user/login/";  
       //System.out.println("usrl is: "+url);
       
       URIBuilder uriBuilder = new URIBuilder(url);  
       HttpPost httpPost = new HttpPost(uriBuilder.build());  
       
       //Set request header
       httpPost.addHeader("Content-Type", "application/json;charset=UTF-8"); 
       httpPost.addHeader("Accept", "application/json"); 
       
       //Config request body
       String content = "{\"username\":\"shan.liu\",\"password\":\"F6P4HMCMSzqaVnTz\",\"remember\":true}";
       String charset = "UTF-8";
       
       StringEntity se = new StringEntity(content);
       se.setContentEncoding(charset);
       se.setContentType("application/json");
       httpPost.setEntity(se);
       
       //httpPost.setConfig(requestConfig);
       
      // System.out.println("content is: "+content);
       HttpResponse response = httpClient.execute(httpPost);   
       //System.out.println("Executing request " + httpPost.getRequestLine());
       
       int status = response.getStatusLine().getStatusCode();
       //System.out.println("status code is: "+status);
       if (status >= 200 && status < 300) {
           entity = response.getEntity();
           result = EntityUtils.toString(entity);
           //System.out.println(result);
       }
       JSONParser parser = new JSONParser();
       JSONObject json = (JSONObject) parser.parse(result);
       String token = (String) json.get("token");
       //System.out.println("Have gained the login token for http://devops_new.tianyitechs.com/cmdb:" +token);
       //System.out.println(EntityUtils.toString(response.getEntity(), Charsets.UTF_8));  

	   return token;
   }	
    
    //====test common Post method====
    //  Sample code is as below:
    //  String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, "au");
    //	HashMap<String, String> header = new HashMap<String, String>();
    //	String url = "http://ben.vantagefx.com.au:8001/admin/mail/mailLogList";  
    //	header.put("Content-Type", "application/json");
    //	header.put("Accept", "application/json");
    //	header.put("Cookie", cookie);
    //	String body = "{\"limit\":100,\"offset\":0,\"order\":\"asc\",\"search\":{\"userQuery\":\"\"}}";
    public static String commonPostAPI(String url, Map map, String body) throws ParseException, IOException, Exception {
    	HttpEntity entity = null;
    	String result="";
   
        URIBuilder uriBuilder = new URIBuilder(url);  
        HttpPost httpPost = new HttpPost(uriBuilder.build());  
        
        //Set request header
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            httpPost.addHeader((String) key, (String) val); 
        }

        //Config request body
        String charset = "UTF-8";
        StringEntity se = new StringEntity(body);
        se.setContentEncoding(charset);
        //System.out.println("body:"+body);
        		
        //Config content type
        String contentType = (String) map.get("Content-Type");
        se.setContentType(contentType);
        httpPost.setEntity(se);
        
        //httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);   
        //System.out.println("Executing request " + httpPost.getRequestLine());
        int status = response.getStatusLine().getStatusCode();
        //System.out.println("status code is: "+status);
        if (status >= 200 && status < 300) {
            entity = response.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
            //System.out.println(result);
        }else {
        	System.out.println("status code for POST not right: "+status);
        	//System.out.println(result);
        }

		return result;
    }
    
    /***
     * for deposit callback, form data
     * @param url
     * @param map
     * @param body
     * @return
     * @throws ParseException
     * @throws IOException
     * @throws Exception
     */
    public static String commonPostAPI(String url, Map<String,String> map, HashMap<String,String> body) throws ParseException, IOException, Exception {
    	String result="";
    	   
        URIBuilder uriBuilder = new URIBuilder(url);  
        HttpPost httpPost = new HttpPost(uriBuilder.build());  
        
        //Set request header
        
        for(Entry<String, String> entry: map.entrySet()) {
        	httpPost.addHeader(entry.getKey(),entry.getValue());
        }
        
        List<NameValuePair> nvps = new ArrayList<>();
        
        for(Entry<String,String> entry: body.entrySet()) {
        	nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
    	
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
        formEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(formEntity);
        HttpResponse response = httpClient.execute(httpPost);
        
        result = EntityUtils.toString(response.getEntity(),"UTF-8");
        
    	return result;
    }
    
    
    /**
     * 
     * @param url
     * @param map
     * @param body String or HashMap
     * @return [HttpResponse,CookieStore]
     * @throws ParseException
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static List<Object> getResponse(String url,Map<String,String> map, Object body) throws ParseException, IOException, Exception {    	   
        URIBuilder uriBuilder = new URIBuilder(url);  
        HttpPost httpPost = new HttpPost(uriBuilder.build());  
        ArrayList<Object> result = new ArrayList<>();
        //Set request header
        
        for(Entry<String, String> entry: map.entrySet()) {
        	httpPost.addHeader(entry.getKey(),entry.getValue());
        }
        
        if(body.getClass()==HashMap.class) {
        	List<NameValuePair> nvps = new ArrayList<>();
        	for(Entry<String,String> entry: ((HashMap<String,String>) body).entrySet()) {
        		nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        	}
        	UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
        	formEntity.setContentEncoding("UTF-8");
        	httpPost.setEntity(formEntity);
        }else {
        	String charset = "UTF-8";
            StringEntity se = new StringEntity(body.toString());
            se.setContentEncoding(charset);
            //System.out.println("body:"+body);
            		
            //Config content type
            String contentType = (String) map.get("Content-Type");
            se.setContentType(contentType);
            httpPost.setEntity(se);
        }
        HttpResponse response = httpClient.execute(httpPost);
        result.add(response);
        result.add(httpCookieStore);
               
       return result;
    }
    
  //====test common Get method====
    //  Sample code is as below:
    //  String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, "au");
    //	HashMap<String, String> header = new HashMap<String, String>();
    //	String url = "http://ben.vantagefx.com.au:8001/admin/mail/mailLogList";  
    //	header.put("Content-Type", "application/json");
    //	header.put("Accept", "application/json");
    //	header.put("Cookie", cookie);
    //	String body = "{\"limit\":100,\"offset\":0,\"order\":\"asc\",\"search\":{\"userQuery\":\"\"}}";
    public static HttpResponse commonGetAPI(String url, Map map) throws ParseException, IOException, Exception {
    	HttpEntity entity = null;
    	String result="";
   
    	URIBuilder uriBuilder = new URIBuilder(url);  
    	HttpGet httpGet = new HttpGet(uriBuilder.build());  
    	
    	//Set request header
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            httpGet.addHeader((String) key, (String) val); 
        }
  
        httpGet.setConfig(requestConfig);  
    
        HttpResponse response = httpClient.execute(httpGet);  
  
/*        //System.out.println("Executing request " + httpPost.getRequestLine());
        int status = response.getStatusLine().getStatusCode();
        //System.out.println("status code is: "+status);
        if (status >= 200 && status < 300) {
            entity = response.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
            //System.out.println(result);
        }else {
        	System.out.println("status code not right: "+status);
        }*/

		return response;
    }
    
    /* Added by Alex L
     * This rawPostAPI simply return all API return in HttpResponse format. */
    public static HttpResponse rawPostAPI(String url, Map map, String body) throws ParseException, IOException, Exception {
    	HttpEntity entity = null;
   
        URIBuilder uriBuilder = new URIBuilder(url);  
        HttpPost httpPost = new HttpPost(uriBuilder.build());  
        
        //Set request header
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            httpPost.addHeader((String) key, (String) val); 
        }

        //Config request body
        String charset = "UTF-8";
        StringEntity se = new StringEntity(body);
        se.setContentEncoding(charset);
        //System.out.println("body:"+body);
        		
        //Config content type
        String contentType = (String) map.get("Content-Type");
        se.setContentType(contentType);
        httpPost.setEntity(se);
        
        //httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);   
        //System.out.println("Executing request " + httpPost.getRequestLine());   

		return response;
    }
	
    /* Developed by Yanni  
     * Given Brand, AdminURL, Adminlogin Cookie and account, search REBATE account information:
     * like: Balance, Equity, margin, etc
     * */
    
    public static HashMap<String, String> funcGetRebateAccInfo(String Brand, String AdminURL, String accountNo, String cookie) throws ParseException, IOException, Exception
	{
		
		
		String apiPath="account/query_rebateAccountList";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		HashMap<String, String> result = new HashMap<String, String>();
		String body, bodyNew, apiResult;
		int i;
		
		/*DataSource ID:
		 * 11: AU2 , 
		 * 12: UK2
		 * 5: AU
		 * 2: UK
		 * 8: MT5
		 * 3: MXT
		 * 10: VT
		*/
		String[] dataSourceId = Utils.getDataSourceId(Brand);
	
		//body="{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"openaccounttype\":\"-1\",\"commissionRule\":\"\",\"server_type\":\"dsds5\",\"agentuserQuery\":\"\",\"search_type\":\"account\",\"userQuery\":\"actact1000\"}}";
	
	
		body = "{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"web\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"actact1000\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"commissionType\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;
		
		AdminURL=Utils.ParseInputURL(AdminURL);			
		apiPath=AdminURL.concat(apiPath);
		
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
	
		//Replace the dummy account with the real account
		body=body.replace("actact1000", accountNo);
		
		for(i=0; i<dataSourceId.length; i++)
		{
			
			//Replace the dummy datasourceId with the real dataSourceId
			bodyNew=body.replace("dsds5", dataSourceId[i]);	
	
			apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);	
			objAPIResult =(JSONObject)parser.parse(apiResult);
			
			
			JSONArray data = (JSONArray)objAPIResult.get("rows");
			
			if(data.size()==1)
			{
				objRow=(JSONObject) data.get(0);
				
				result.put("balance", objRow.get("balance").toString());
				result.put("equity", objRow.get("equity").toString());
				result.put("margin", objRow.get("margin").toString());
				result.put("dataSourceId", objRow.get("dataSourceId").toString());
				
				break;
			
				
			}
		
		}
		
		if(i>=dataSourceId.length)
		{
			System.out.println ("Can't find Rebate account on all servers: " + accountNo);
		}
		
		return result;
	}

    /* Developed by Yanni  
     * Given Brand, AdminURL, Adminlogin Cookie and account, search TRADING account information:
     * like: Balance, Equity, margin, etc
     * */
    public static HashMap<String, String> funcGetTradingAccInfo(String Brand, String AdminURL, String accountNo, String cookie) throws ParseException, IOException, Exception
	{
		
		
		String apiPath="account/query_accountList";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		HashMap<String, String> result = new HashMap<String, String>();
		String body, bodyNew, apiResult;
		int i;
		
		/*DataSource ID:
		 * 11: AU2 , 
		 * 12: UK2
		 * 5: AU
		 * 2: UK
		 * 8: MT5
		 * 3: MXT
		*/
		
		String[] dataSourceId =Utils.getDataSourceId(Brand);
		

					
		//body="{\"limit\":10,\"pageNo\":1,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"search_type\":\"account\",\"userQuery\":\"actact1000\",\"agentuserQuery\":\"\",\"dataSource\":\"dsds5\",\"queryConfig\":\"0\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}";
		
		body ="{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"actact1000\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;
		
		AdminURL=Utils.ParseInputURL(AdminURL);
		
		apiPath=AdminURL.concat(apiPath);
		
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
		
		//Replace the dummy account with the real account
		body=body.replace("actact1000", accountNo);
		
		for(i=0; i<dataSourceId.length; i++)
		{
			
			//Replace the dummy datasourceId with the real dataSourceId
			bodyNew=body.replace("dsds5", dataSourceId[i]);	
			apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);	

			if(apiResult!=null)
			{
				objAPIResult =(JSONObject)parser.parse(apiResult);
				
				
				JSONArray data = (JSONArray)objAPIResult.get("rows");
				
				if(data.size()==1)
				{
					objRow=(JSONObject) data.get(0);
					
					result.put("balance", objRow.get("balance").toString());
					result.put("equity", objRow.get("equity").toString());
					result.put("margin", objRow.get("margin").toString());
					result.put("dataSourceId", objRow.get("mt4_datasource_id").toString());
					
					break;
				
					
				}
			}
		
		}
		
		if(i>=dataSourceId.length)
		{
			System.out.println ("Can't find Trading account on all servers: " + accountNo);
		}
		
		return result;
	} 
    
    /* Added by Alex L
     * For checking if account is read only account. 
     * Return 1 when account is read only(not able to trade). */
    public static String funcAPICheckAccountTradingStatus(String Brand, String AdminURL, String accountNo, String cookie) throws ParseException, IOException, Exception
	{		
		
		String apiPath="account/query_accountList";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		String result = "";
		String body, bodyNew, apiResult;
		int i;
		
		/*DataSource ID:
		 * 11: AU2 , 
		 * 12: UK2
		 * 5: AU
		 * 2: UK
		 * 8: MT5
		 * 3: MXT
		*/
		
		String[] dataSourceId =Utils.getDataSourceId(Brand);
		

					
		//body="{\"limit\":10,\"pageNo\":1,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"search_type\":\"account\",\"userQuery\":\"actact1000\",\"agentuserQuery\":\"\",\"dataSource\":\"dsds5\",\"queryConfig\":\"0\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}";
		body ="{\"skipCount\":true,\"pagination\":{\"pageNo\":null},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},"
				+"\"cpa\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"actact1000\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;
		
		AdminURL=Utils.ParseInputURL(AdminURL);
		
		apiPath=AdminURL.concat(apiPath);
		
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
	
		//Replace the dummy account with the real account
		body=body.replace("actact1000", accountNo);
		
		for(i=0; i<dataSourceId.length; i++)
		{
			
			//Replace the dummy datasourceId with the real dataSourceId
			bodyNew=body.replace("dsds5", dataSourceId[i]);	
			/*
			System.out.println("bodyNew:" + bodyNew);
			System.out.println("apiPath:" + apiPath);
			 */
			//Added by Yanni on 26/06/2020: can't return results in Beta. Add wait time
			Thread.sleep(1500);
			apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);	
			//System.out.println("apiRe: "+apiResult);
			//if(apiResult!=null)
			if(apiResult.length()!=0)
			{
				objAPIResult =(JSONObject)parser.parse(apiResult);
				
				
				JSONArray data = (JSONArray)objAPIResult.get("rows");
				
				if(data.size()==1)
				{
					objRow=(JSONObject) data.get(0);
					
					result = objRow.get("enableReadonly").toString();
					break;
				
					
				}
			}
		
		}
		
		if(i>=dataSourceId.length)
		{
			System.out.println ("Can't find Trading account on all servers: " + accountNo);
			result = "na";
		}
		
		return result;
	} 
    
    /* Added by Alex L
     * For checking if trading account group is test group. 
     * Replace with test group if not. */
	/*
	 * public static void funcAPIUpdateTradingAccountGroup(String TestEnv, String
	 * Brand, String AdminURL, String cookie) throws ParseException, IOException,
	 * Exception {
	 * 
	 * String apiPath="account/query_accountList"; HashMap<String, String> headerMap
	 * = new HashMap<String, String>(); String accountNo =
	 * "",groupName="",commId="",newGroupName="",currency="",userEmail=""; String
	 * accountName=""; //Yanni on 29/06/2020: client's name String actBalance = "0";
	 * String body, bodyNew, apiResult; int i;
	 * 
	 * DataSource ID: 11: AU2 , 12: UK2 8: MT5 10: VT 7: PUG 14：AU3 15:UK3 18:VT2 3：
	 * AU 5: UK 2: MXT
	 * 
	 * 
	 * 
	 * String[] dataSourceId =Utils.getDataSourceId(Brand);
	 * 
	 * //body=
	 * "{\"limit\":300,\"pageNo\":1,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"search_type\":\"clientName\",\"userQuery\":\"actact1000\",\"agentuserQuery\":\"\",\"dataSource\":\"dsds5\",\"queryConfig\":\"0\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}";
	 * 
	 * body
	 * ="{\"skipCount\":true,\"pagination\":{\"limit\":50, order: \"desc\", sort: \"approvedTime\"},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
	 * +
	 * "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"actact1000\"},"
	 * +
	 * "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
	 * +
	 * "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},"
	 * +
	 * "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
	 * +
	 * "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
	 * +
	 * "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
	 * +
	 * "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
	 * 
	 * JSONParser parser = new JSONParser(); JSONObject objAPIResult, objRow;
	 * 
	 * AdminURL=Utils.ParseInputURL(AdminURL);
	 * 
	 * apiPath=AdminURL.concat(apiPath);
	 * 
	 * 
	 * headerMap.put("Content-Type", "application/json"); headerMap.put("Accept",
	 * "application/json"); headerMap.put("Cookie", cookie);
	 * headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
	 * headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
	 * 
	 * //Replace the dummy account with the real account
	 * body=body.replace("actact1000", Utils.testcrmPrefix);
	 * 
	 * //if(!TestEnv.equalsIgnoreCase("prod")) {
	 * 
	 * System.out.println("Going to change account groups for brand: " +Brand);
	 * 
	 * for(i=0; i<dataSourceId.length; i++) {
	 * 
	 * if(dataSourceId[i].equals("2")||dataSourceId[i].equals("5")) { //MXT
	 * if(dataSourceId[i].equals("3")) { continue; } //Replace the dummy
	 * datasourceId with the real dataSourceId bodyNew=body.replace("dsds5",
	 * dataSourceId[i]);
	 * 
	 * System.out.println("Going to search data source: " + dataSourceId[i]);
	 * 
	 * apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);
	 * 
	 * //System.out.println("apiResult is:" + apiResult); if(apiResult!=null) {
	 * 
	 * objAPIResult =(JSONObject)parser.parse(apiResult); JSONArray data =
	 * (JSONArray)objAPIResult.get("rows"); System.out.println("data:" +
	 * data.toJSONString()); for(int j=0; j<data.size(); j++) { objRow=(JSONObject)
	 * data.get(j);
	 * 
	 * try { groupName = objRow.get("group").toString(); }catch(NullPointerException
	 * e) { groupName = "UNKNOWN";
	 * System.out.println("Group UNKNOW! Will not be replaced by TEST group"); }
	 * 
	 * //userEmail = objRow.get("email").toString(); userEmail =
	 * objRow.get("email_sub").toString();
	 * if(Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("pug")) { accountName
	 * = objRow.get("accountName").toString(); }else { //accountName =
	 * objRow.get("name").toString(); accountName =
	 * objRow.get("real_name").toString(); }
	 * 
	 * actBalance = objRow.get("balance").toString(); userEmail =
	 * testPostDecryptEmail(userEmail,AdminURL,cookie);
	 * 
	 * //if (!isTestGroup(groupName.toLowerCase()) && Utils.isTestUser(accountName)
	 * && Utils.isTestEmail(userEmail)) { if (!isTestGroup(groupName.toLowerCase())
	 * && Utils.isTestcrmUser(accountName) && !groupName.equals("UNKNOWN")){
	 * 
	 * System.out.println("\n!!! Found non-test group "+groupName +
	 * " , Client Name: " + accountName+ " , Email: " + userEmail); //accountNo =
	 * objRow.get("login").toString(); accountNo =
	 * objRow.get("mt4_account").toString(); currency =
	 * objRow.get("currency").toString();
	 * 
	 * if(!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&&
	 * !Brand.equalsIgnoreCase("svg")) { commId =
	 * testPostQueryCommissionIDByAccount(AdminURL, cookie, accountNo); }
	 * 
	 * 
	 * switch(dataSourceId[i]) { //MT5 case "8": //VT MT5 case "19":
	 * if(Brand.equalsIgnoreCase("vtsvg")) { newGroupName = "Test%5CTEST_VT_NM_E_" +
	 * currency; }else { newGroupName = "Test%5CTEST_M_VTC_E_" + currency; } break;
	 * 
	 * //PUG MT5 case "20": newGroupName = "TEST_PUL%5CTEST_CRM_PULS_00_USD"; break;
	 * 
	 * //VT1&2&3 case "10": case "18": case "58":
	 * if(Brand.equalsIgnoreCase("vtsvg")) { newGroupName = "TEST_VT_CRM_"+currency;
	 * }else { newGroupName = "TEST_VTC_E_"+currency; } break; //PUG&PUG2 case "7":
	 * case "21": //newGroupName = "T_PUGS_00_USD";
	 * if(currency.toLowerCase().contains("usd")||currency.toLowerCase().contains(
	 * "usc")) { newGroupName = "TEST_PULS_0_"+currency; }else { newGroupName =
	 * "TEST_PULP_"+currency; } break;
	 * 
	 * //Moneta MT4 case "900":
	 * if(currency.toLowerCase().contains("aud")||currency.toLowerCase().contains(
	 * "gbp")||currency.toLowerCase().contains("eur")) { newGroupName =
	 * "TEST_MON_"+currency; }else { newGroupName = "TEST_MON_USD"; } break;
	 * 
	 * //Moneta MT5 case "901": newGroupName = "Moneta_Hedge%5CTEST_MNT_"+currency;
	 * break;
	 * 
	 * //AU: case "5": //UK: case "2": //AU2 case "11": //UK2 case "12": //AU3 case
	 * "14": //UK3 case "15": //AU4 case "200": //UK4 case "201": //UK5 case "220":
	 * default: if(Brand.equalsIgnoreCase("mo")) { newGroupName =
	 * "TEST_MON_P_"+currency; }else {
	 * if(currency.toLowerCase().contains("hkd")||currency.toLowerCase().contains(
	 * "nzd")) { newGroupName = "TEST_VFX_USD"; }else { newGroupName =
	 * "TEST_VFX_"+currency; } } }
	 * 
	 * 
	 * testPostUpdateAccountGroup(AdminURL, cookie, accountNo, commId,
	 * newGroupName); if(!actBalance.equalsIgnoreCase("0.0")) {
	 * System.out.println(accountName + " + " + groupName +
	 * " has balance. Please manually check the group is changed. Balance is " +
	 * actBalance ); } }
	 * 
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * 
	 * //}else { //System.out.
	 * println("This function should NOT be implemented on production environment!"
	 * ); //}
	 * 
	 * }
	 */
    
    
	/* Updated by AlexL 11/10/2022 for supporting more brands */
    public static void funcAPIUpdateTradingAccountGroup(String TestEnv, String Brand, String AdminURL, String cookie) throws ParseException, IOException, Exception
	{		
		
		String apiPath="account/query_accountList";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		String accountNo = "",groupName="",commId="",newGroupName="",currency="",userEmail="";
		String accountName="";   //Yanni on 29/06/2020: client's name
		String actBalance = "0";
		String body, bodyNew, apiResult;
		int i;
		
		/*DataSource ID:
		 * 3: 	MXT
		 * 5： 	AU
		 * 11: 	AU2 (VFX & MO)
		 * 14: 	AU3
		 * 200: AU4
		 * 2: 	UK
		 * 12: 	UK2
		 * 15: 	UK3
		 * 201: UK4
		 * 220: UK5
		 * 8: 	VFX MT5
		 * 17:	FCA VP
		 * 10: 	VT
		 * 18:	VT2
		 * 58: 	VT3
		 * 68: 	VT4
		 * 19:	VT MT5
		 * 7: 	PUG
		 * 21:	PUG2
		 * 20:	PUG MT5
		 * 900:	MO UK3
		 * 907:	MO MT5
		 * 600: UM MT4
		 */
		
		
		String[] dataSourceId =Utils.getDataSourceId(Brand);	
		String ownerId = Utils.getTestCRMUserId(Brand);
					
		//body="{\"limit\":300,\"pageNo\":1,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"search_type\":\"clientName\",\"userQuery\":\"actact1000\",\"agentuserQuery\":\"\",\"dataSource\":\"dsds5\",\"queryConfig\":\"0\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}";
		    		
		body ="{\"skipCount\":true,\"pagination\":{\"limit\":50, order: \"desc\", sort: \"approvedTime\"},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"actact1000\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;
		
		AdminURL=Utils.ParseInputURL(AdminURL);
		
		apiPath=AdminURL.concat(apiPath);
		
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
		
		//Replace the dummy account with the real account
		body=body.replace("actact1000", Utils.testcrmPrefix);
			
			System.out.println("Going to change account groups for brand: " +Brand);
			
			for(i=0; i<dataSourceId.length; i++)
			{
				
				//Skip for MXT
				if(dataSourceId[i].equals("3")) {
					continue;
				}
				//Replace the dummy datasourceId with the real dataSourceId
				bodyNew=body.replace("dsds5", dataSourceId[i]);	
				
				System.out.println("Going to search data source: " + dataSourceId[i]);
				
				apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);	
				
				//System.out.println("apiResult is:" + apiResult);
				if(apiResult!=null)
				{
					
					objAPIResult =(JSONObject)parser.parse(apiResult);
					JSONArray data = (JSONArray)objAPIResult.get("rows");
					//System.out.println("data:" + data.toJSONString());
					for(int j=0; j<data.size(); j++)
					{
						objRow=(JSONObject) data.get(j);
						
						try {
							groupName = objRow.get("group").toString();
						}catch(NullPointerException e) {
							groupName = "UNKNOWN";
							System.out.println("Group UNKNOW! Will not be replaced by TEST group");
						}
						
						//userEmail = objRow.get("email").toString();
						userEmail = objRow.get("email_sub").toString();
						accountName = objRow.get("real_name").toString();
						
						actBalance = objRow.get("balance").toString();
				    	userEmail = testPostDecryptEmail(userEmail,AdminURL,cookie);
				    	
				    	if (!isTestGroup(groupName.toLowerCase()) && Utils.isTestcrmUser(accountName) && !groupName.equals("UNKNOWN")){
				    		
							System.out.println("\n!!! Found non-test group "+groupName + " , Client Name: " + accountName+ " , Email: " + userEmail);
							//accountNo = objRow.get("login").toString();
							accountNo = objRow.get("mt4_account").toString();
							currency = objRow.get("currency").toString();
							
							if(!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("vtsvg") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
								commId = testPostQueryCommissionIDByAccount(AdminURL, cookie, accountNo);
							}
							
							//Find out matching group from json file
							newGroupName = Utils.readGroupNamefromJson(dataSourceId[i], Brand, currency);
							
							//Update account group 
							testPostUpdateAccountGroup(AdminURL, cookie, accountNo, commId, newGroupName, groupName, ownerId);
							if(!actBalance.equalsIgnoreCase("0.0"))								
							{
								System.out.println(accountName + " + " + groupName + " has balance. Please manually check the group is changed. Balance is " + actBalance );
							}
						}
					
						
					}
				}
			
			}

		
	} 

    
    
    //By Alex Liu 18/08/2021
    //This method only check non-test account group in PROD. Won't change the account group.
    public static void funcAPICheckNonTestAccountGroup(String TestEnv, String Brand, String AdminURL, String cookie) throws ParseException, IOException, Exception
	{		
		
		String apiPath="account/query_accountList";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		String accountNo = "",groupName="",commId="",newGroupName="",currency="",userEmail="";
		String accountName="";   
		String actBalance = "0";
		String body, bodyNew, apiResult;
		int i;
		
		String[] dataSourceId =Utils.getDataSourceId(Brand);	
					
		//body="{\"limit\":300,\"pageNo\":1,\"order\":\"asc\",\"search\":{\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\",\"search_type\":\"clientName\",\"userQuery\":\"actact1000\",\"agentuserQuery\":\"\",\"dataSource\":\"dsds5\",\"queryConfig\":\"0\",\"depositStatus\":\"-1\",\"openaccounttype\":\"-1\"}}";
		    		
		body ="{\"skipCount\":true,\"pagination\":{\"limit\":100, order: \"desc\", sort: \"approvedTime\"},\"parameters\":{\"mt4DatasourceId\":{\"filterType\":\"SELECT\",\"input\":[\"dsds5\"]},"
				+ "\"owner\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"real_name\":{\"filterType\":\"CUSTOM\",\"input\":\"actact1000\"},"
				+ "\"tb_user.phoneNum\":{\"filterType\":\"INPUT\",\"input\":\"\"},\"tb_user.email\":{\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"tb_user.countryCode\":{\"filterType\":\"SELECT\",\"input\":null},\"mt4Account\":{\"fuzzy\":true,\"filterType\":\"INPUT\",\"input\":\"\"},"
				+ "\"mt4AccountType\":{\"filterType\":\"SELECT\",\"input\":null},\"applyCurrency\":{\"filterType\":\"SELECT\",\"input\":null},"
				+ "\"approvedTime\":{\"filterType\":\"DATEPICKER\",\"input\":{}},\"depositStatus\":{\"filterType\":\"SELECT\",\"input\":\"\"},"
				+ "\"is_archive\":{\"filterType\":\"SELECT\",\"input\":\"0\"},\"directLevel\":{\"filterType\":\"CUSTOM\",\"input\":\"5\"},"
				+ "\"user_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"},\"org_id\":{\"filterType\":\"CUSTOM\",\"input\":\"\"}}}";
		
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;
		
		AdminURL=Utils.ParseInputURL(AdminURL);
		
		apiPath=AdminURL.concat(apiPath);
		
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
	
		//Replace the dummy account with the real account
		body=body.replace("actact1000", Utils.testcrmPrefix);
		
			
			for(i=0; i<dataSourceId.length; i++)
			{
				
				/*if(dataSourceId[i].equals("2")||dataSourceId[i].equals("5")) {*/
				if(dataSourceId[i].equals("2")) {
					continue;
				}
				//Replace the dummy datasourceId with the real dataSourceId
				bodyNew=body.replace("dsds5", dataSourceId[i]);	
				
				System.out.println("Going to search data source: " + dataSourceId[i]);
				
				apiResult = RestAPI.commonPostAPI(apiPath,headerMap,bodyNew);	
				
				//System.out.println("apiResult is:" + apiResult);
				if(apiResult!=null)
				{
					
					objAPIResult =(JSONObject)parser.parse(apiResult);
					JSONArray data = (JSONArray)objAPIResult.get("rows");
					//System.out.println("data:" + data.toJSONString());
					for(int j=0; j<data.size(); j++)
					{
						objRow=(JSONObject) data.get(j);
						
						try {
							groupName = objRow.get("group").toString();
						}catch(NullPointerException e) {
							groupName = "UNKNOWN";
							System.out.println("Group UNKNOW! ");
						}
						
						//userEmail = objRow.get("email").toString();
						userEmail = objRow.get("email_sub").toString();
						/*if(Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("pug")) {
							accountName = objRow.get("accountName").toString();
						}else {*/
							//accountName = objRow.get("name").toString();
							accountName = objRow.get("real_name").toString();
							/* } */
						
						actBalance = objRow.get("balance").toString();
				    	userEmail = testPostDecryptEmail(userEmail,AdminURL,cookie);
				    	
						
						if (!groupName.toLowerCase().contains("test") && accountName.contains("testcrm") ) {
							
							System.out.println("!!! Found non-test group "+groupName + " , Client Name: " + accountName+ " , Email: " + userEmail);

							/*
							 * accountNo = objRow.get("mt4_account").toString();
							 * currency = objRow.get("currency").toString();
							 * 
							 * if(!Brand.equalsIgnoreCase("vt") &&
							 * !Brand.equalsIgnoreCase("fsa")&&
							 * !Brand.equalsIgnoreCase("svg")) {
							 * commId =
							 * testPostQueryCommissionIDByAccount(AdminURL,
							 * cookie, accountNo);
							 * }
							 */
							
						}
					
						
					}
				}
			
			}
			
			

		
	} 
    
    /* Added by Alex L
     * For updating live account trading password. */
    public static void funcAPIUpdateTradingAccountPassword(String TestEnv, String Brand, String AdminURL, String cookie, String accountNo, String dataSourceId) throws ParseException, IOException, Exception
	{		
		
		String apiPath="account/resetPasswordSubmit";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		String newPasswd=Utils.randomString(8); 
		String sendEmailFlag = "true";
		String apiResult = "";
		
		//String[] dataSourceId =Utils.getDataSourceId(Brand);	
							    		
		String body ="login=" + accountNo + "&master=" + newPasswd + "&investor=131eddqwcQ&sendMailFlag=" + sendEmailFlag + "&dataSourceId=" + dataSourceId;
		//System.out.println(body);
		
		AdminURL=Utils.ParseInputURL(AdminURL);
		
		apiPath=AdminURL.concat(apiPath);		
		//System.out.println(apiPath);
		headerMap.put("Accept", "*/*");
		headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headerMap.put("Cookie", cookie);
		headerMap.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		headerMap.put("Host", Utils.ParseAdminURLtoHost(AdminURL));
	
		apiResult = RestAPI.commonPostAPI(apiPath, headerMap, body);
		
		if(apiResult.matches(".*success.*true.*")) {
			System.out.println("***Update account " + accountNo + " password to " + newPasswd + " successfully!\n");
		}else {
			System.out.println("!!!Update account " + accountNo + " password failed!!!\n Response:\n" + apiResult +"\n");
		}

		
	} 
    
    
    static boolean isTestGroup(String groupName)
    {
    	boolean flag = false;     	
    	
      	if(groupName.contains("test") || groupName.contains("t_vt_crm") || groupName.contains("t_pug")|| groupName.contains("t_crm"))
    	{
    		flag = true;
    	}
    	
    	return flag;
    }
    
    //By Yanni on 15/10/2020: to get internal user's default regulator. Return Regulator name
    static String getDefaultRegulator(String AdminName, String Brand, String TestEnv) throws Exception
    {
    	
		/*
		 * String AdminName = "alexliu";
		 * String Brand = "au";
		 * String TestEnv = "beta";
		 */
    	String defaultRegulator="";
    	
    	String globalDBName = Utils.getDBName(Brand)[0];
    	String sql = "SELECT default_regulator FROM tb_user_inner where user_name ='" + AdminName + "' order by create_time desc limit 1;";
    	
    	defaultRegulator = DBUtils.funcReadDBReturnAll(globalDBName, sql, TestEnv); 
     	defaultRegulator=RestAPI.transform2Map(TestEnv, defaultRegulator).get("default_regulator");    	
   
    	return defaultRegulator;
    }
    
    //By Yanni on 15/10/2020: to convert DB read result to a Map. It will be easy to get one column value by map.get(key)
	static Map<String, String> transform2Map(String TestEnv, String originResult)
	{

		  String processedResult;
		  Map<String, String> mapResult = new HashMap<String, String>();
		  String[] arrayResult;
	
			/*
			 * Test: [user_name: Alexliu, default_regulator: CIMA]
			 * Beta: {user_name=alexliu, default_regulator=CIMA}
			 */
		 
		  //If it is test env, need to replace [] to empty chars 
		  if(TestEnv.equalsIgnoreCase("test"))
		  {
			  processedResult = originResult.replace("[", "");
			  processedResult = processedResult.replace("]", "");
	
			  
		  }else  //If it is NOT test env, need to replace {} to empty chars and replace = with :
		  {
			  processedResult = originResult.replace("{", "");
			  processedResult = processedResult.replace("}", "");
			  processedResult = processedResult.replace("=", ":");			
		  }  
  		  
		  //remove all space
		  processedResult = processedResult.replace(" ", "");	
		  
		  //Sample after processing: user_name:Alexliu,default_regulator:CIMA
		 arrayResult = processedResult.split(",");
		 for(int i=0; i<arrayResult.length; i++)
		 {
			 String key= arrayResult[i].split(":")[0];
			 String value = arrayResult[i].split(":")[1];
			 mapResult.put(key, value);
		 }
		 
		
		 return mapResult;
		  
	}
}  