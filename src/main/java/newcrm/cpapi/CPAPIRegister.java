package newcrm.cpapi;

import static org.testng.Assert.assertTrue;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.utils.http.CRMHttpClient;
import vantagecrm.Utils;

public class CPAPIRegister {
	protected static HashMap<String,String> header = new HashMap<>();;
	protected CRMHttpClient httpClient;
	protected String url ;
	protected String TraderName;
	protected String host = null;
	protected String regulator = null;
	protected String brand;
	protected String userId = null;

	//update url for registrationV2
	private final String entry_url = "data/vt_live_form_autotest.php?v=205656517";
	private final String register_url = "api/registrationV2/register";
	private final String loginA_url = "api/login/to_login_register";
	private final String process_url = "api/verificationV2/process";

	
	public CPAPIRegister(String host, String url, String regulator,String brand) {
		httpClient = new CRMHttpClient();
		
		this.url = Utils.ParseInputURL(url);
		this.host = Utils.ParseInputURL(host);
		
		this.regulator = regulator;
		this.brand = brand;
		header.put("User-Agent", "Mozilla/5.0 CRM-Automation");
		header.put("Accept", "*/*");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	}
	
	
	//return encrypt entry info
	public String createUser(String email,String affid,String cpaid,
			String firstName, String lastName, String country,
			String phone, String source, String createDemo,String phoneCode,
			String ls,String wid,String cxd, String accountType, String regulator) {
		HashMap<String,String> obj = new HashMap<>();
		this.url = url + entry_url;
		obj.put("email",email );
		obj.put("password",GlobalMethods.generatePassword());
		obj.put("affid",affid );
		obj.put("cpaid", cpaid);
		obj.put("firstName", firstName );
		//obj.put("lastName", lastName);
		obj.put("country",country );
		//obj.put("phone",phone );
		//obj.put("mobile",phone );
		obj.put("source", source);
		obj.put("createDemo", createDemo);
		//obj.put("phoneCode", phoneCode);
		obj.put("ls", ls);
		obj.put("wid", wid);
		obj.put("cxd", cxd);		
		obj.put("regulator", regulator);
		return this.send(obj).getString("data");
	}
	
	//return encryptA
	public String postRegister(String encryptEntry) {
		HashMap<String,String> obj = new HashMap<>();
		obj.put("text",encryptEntry);
		this.url = host + register_url;

		String encryptA = "";	
		try {
			HttpResponse response = httpClient.getPostResponse(this.url, header,obj);
			//System.out.println( "\nresponse："+response);
			
			String location = response.getFirstHeader("Location").toString();
			encryptA = location.substring(location.indexOf("?a=")+3, location.length());
			System.out.println( "\nencryptA："+encryptA);
			
			String cookie = response.getFirstHeader("Set-Cookie").toString();
			cookie = cookie.substring(cookie.indexOf("ibportalJsId"));
			header.put("Cookie", cookie);
			System.out.println( "\ncookie："+response.getFirstHeader("Set-Cookie").toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return encryptA;
	}
	
	
	//Login with A
	public HashMap<String,String> loginWithA(String encryptA) {
		HashMap<String,String> obj = new HashMap<>();
		obj.put("a",encryptA);
		this.url = host + loginA_url;
		JSONObject message = this.send(obj);
		System.out.println("LoginA response:"+message.toString());
		return header;
	}
	
	
	//step1
	public void step1(String firstName, String lastName,String email,String mobile,
			String phoneCode,String countryCode, String idNo) {
		String obj = "{\"userId\":null,\"source\":null,\"title\":\"Ms\",\"firstName\":\""+firstName+
				"\",\"middleName\":null,\"lastName\":\""+lastName+"\",\"nationalityId\":7,\"email\":\""+email+
				"\",\"mobile\":\""+mobile+"\",\"phoneCode\":\""+phoneCode+
				"\",\"dob\":\"1999-07-07\",\"placeOfBirth\":"+countryCode+",\"referredBy\":null,\"idType\":2,"
				+ "\"idNumber\":\""+idNo+"\",\"step\":1}";

		header.put("Content-Type", "application/json;charset=UTF-8");	
		header.put("Accept", "application/json, text/plain, */*");
		this.url = host + process_url;		

		JSONObject result = this.send(obj);
		
		System.out.println("step1: "+result);
		assertTrue(result.getInteger("code").equals(0),"Step 1 failed!");
		//userId = result.getJSONObject("data").getString("userId");
	}
	
	
	//step2
	public void step2(String countryCode) {	
		String obj = "{\"userId\":null,\"source\":null,\"countryId\":"+countryCode+",\"streetNumber\":null,"
				+ "\"address\":\"119 RURAL ROAD\",\"state\":\"Amphur Muang\",\"suburb\":\"Chiang Mai\","
				+ "\"postcode\":\"50100\",\"usCitizen\":false,\"taxResidencyCountryId\":null,"
				+ "\"nationalInsuranceNumber\":null,\"yearsAtAddress\":null,\"previousCountryId\":null,"
				+ "\"previousAddress\":null,\"previousSuburb\":null,\"previousState\":null,\"previousStreetNumber\":null,"
				+ "\"previousPostcode\":null,\"isOpenRomania\":false,\"step\":2}";	

		JSONObject result = this.send(obj);
		System.out.println("step2: "+result);
		assertTrue(result.getInteger("code").equals(0),"Step 2 failed!");
	}
	
	//step3
	public void step3(String regulator) {	
		String obj = null;		
		switch(regulator) {
			case "ASIC":
				obj = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"8\",\"answers\":[45]},"
						+ "{\"questionId\":\"9\",\"answers\":[51]},{\"questionId\":\"10\",\"answers\":[55]},"
						+ "{\"questionId\":\"11\",\"answers\":[56]},{\"questionId\":\"12\",\"answers\":[61]}],"
						+ "\"tradingAnswers\":[]}";
				break;
			case "FCA":
				obj = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[1]},"
						+ "{\"questionId\":\"2\",\"answers\":[289]},{\"questionId\":\"3\",\"answers\":[316]},"
						+ "{\"questionId\":\"4\",\"answers\":[341]},{\"questionId\":\"5\",\"answers\":[347]}],"
						+ "\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[348]},{\"questionId\":\"7\","
						+ "\"answers\":[356]},{\"questionId\":\"8\",\"answers\":[365]},{\"questionId\":\"9\","
						+ "\"answers\":[371]},{\"questionId\":\"10\",\"answers\":[375]},{\"questionId\":\"11\","
						+ "\"answers\":[379]},{\"questionId\":\"12\",\"answers\":[383]},{\"questionId\":\"13\","
						+ "\"answers\":[387]},{\"questionId\":\"14\",\"answers\":[388]},{\"questionId\":\"15\","
						+ "\"answers\":[390]}]}";
				break;
			default:
				obj = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[5]},"
						+ "{\"questionId\":\"2\",\"answers\":[10]},{\"questionId\":\"3\",\"answers\":[18]},"
						+ "{\"questionId\":\"4\",\"answers\":[25]},{\"questionId\":\"5\",\"answers\":[36]}],"
						+ "\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[40]},{\"questionId\":\"7\","
						+ "\"answers\":[44]}]}";
				break;
		}
		
		JSONObject result = this.send(obj);
		System.out.println("step3: "+result);
		//Comment out due to step3 not necessary for AT
		//assertTrue(result.getInteger("code").equals(0),"Step 3 failed!");
	}
	
	
	//Step4
	public void step4(String platform, String currency) {	
		String obj = "{\"userId\":null,\"source\":null,\"tradingPlatform\":\""+platform +
				"\",\"accountType\":\"standardSTP\",\"currency\":\""+currency + 
				"\",\"skipNextStep\":false,\"hideAccountType\":false,\"accountCreated\":"
				+ "false,\"accountReadOnly\":false,\"step\":4}";	
		
		JSONObject result = this.send(obj);
		System.out.println("step4: "+result);
		assertTrue(result.getInteger("code").equals(0),"Step 4 failed!");
	}
	
	
	//step5
	public void step5(String countryCode, String idNo) {	
		String obj = "{\"countryId\":"+countryCode+",\"nationalityId\":"+countryCode+"," +
				"\"idType\":2,\"idNumber\":\""+idNo+"\",\"step\":5,"
				+ "\"firstName\":\"gvhjbkn\",\"lastName\":\"hbjnkml\",\"idDocFilePathList\":"
				+ "[\"https://vt-markets.s3.amazonaws.com/other/7aaca01e42044334be569493c9e76234.jpg"
				+ "?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230603T105325Z&X-Amz-SignedHeaders="
				+ "host&X-Amz-Expires=600&X-Amz-Credential=AKIARI6DSGFBA52KRQOM%2F20230603%2Fus-east-"
				+ "1%2Fs3%2Faws4_request&X-Amz-Signature=2efd19a4c399f12bb6f83d999ef2f2c35d91099b63059"
				+ "59499a35b063e785bf8\"],\"poaDocFilePathList\":[\"https://vt-markets.s3.amazonaws.com"
				+ "/other/3f345b88eb114db884c3085e477ac1bc.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-"
				+ "Date=20230603T105337Z&X-Amz-SignedHeaders=host&X-Amz-Expires=600&X-Amz-Credential="
				+ "AKIARI6DSGFBA52KRQOM%2F20230603%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature="
				+ "7f9781c63e7c119e212be21d6cf51b9c9308a77f6549a06d239acd346d40d9ec\"]}";	

		JSONObject result = this.send(obj);
		System.out.println("step5: "+result);
		assertTrue(result.getInteger("code").equals(0),"Step 5 failed!");
	}
	
	
	
	/**
	 * Send api request
	 * @param Object body, can be string or Hashmap
	 * @return json response
	 */
    public JSONObject send(Object body) {
		String fullpath = this.url;
		try {
			HttpResponse response = httpClient.getPostResponse(fullpath, header,body);
			//System.out.println("fullpath:"+fullpath+"\nheader:"+header+"\nbody"+body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}   
  
    

	/**
	 * Print registration result 
	 * @param brand, account, email
	 */
	public void printUserFundsInfo(String brand, String account, String email) {
		System.out.println("***********Account creation successfully***********");
		System.out.printf("%-30s : %s\n","Brand",brand);
		System.out.printf("%-30s : %s\n","Account",account);
		System.out.printf("%-30s : %s\n","CP login",email);
		System.out.println("*************************************************");
	}	
	
	
	
	public static void main(String args[]) throws Exception {
	
		String host = "https://myaccount.vtmarkets.com"; 
		String url = "https://www.vtmarkets.com";
		String regulator = "SVG";
		String brand = "VT"; 
		CPAPIRegister reg = new CPAPIRegister(host, url, regulator, brand);

		String email = "a32a1q909o92h@3q.com";
		String lastName = "ApiAutomotion";
		String firstName = "testcrm"+GlobalMethods.getRandomString(8);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String idNo = "API"+GlobalMethods.getRandomString(5)+GlobalMethods.getRandomNumberString(6);
		String phoneCode = "66";
		String countryCode = "6163";
		String country = "Thailand";
		String platform = "mt4";
		String currency = "USD";
		
		String encryptS = reg.createUser(email, "", "", firstName, lastName, country, mobile, "VFX", "FALSE", phoneCode, "", "","", "Individual", regulator);
		
		//Get encryptA and set cookie
		String encryptA = reg.postRegister(encryptS);
		
		//Login with A
		reg.loginWithA(encryptA);
		
		//Step1
		reg.step1(firstName, lastName, email, mobile, phoneCode, countryCode,idNo);
		
		
		//Step2
		reg.step2(countryCode);
		
		//Step3
		reg.step3(regulator);
		
		//Step4
		reg.step4(platform, currency);		
		
		//Step5
		reg.step5(countryCode, idNo);
		
		Thread.sleep(2000);
		
		//Verify auto audit
		CPAPIBase api = new CPAPIBase(host,header);
		JSONObject accInfo = api.queryMetaTraderAccountDetails();
		
		reg.printUserFundsInfo(brand, accInfo.getString("account"),email);
 
	}
}
