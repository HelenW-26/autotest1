package newcrm.utils.app;

import com.alibaba.fastjson.JSONObject;

public class Register extends APIBase {
	private final String create_uri = "/registration/register";
	private final String process_uri = "/verification/process";
	private final String get_uri = "/verification/getData";
	private final String get_step_uri = "/verification/getCurrentStep";

	private final String create_uri_New = "/registrationV2/register";
	private final String process_uri_New = "/verificationV2/process";
	private final String get_uri_New = "/verificationV2/getData";

	private final String upload_poa = "/identityProof/verification";
	
	public Register(String host,String regulator,String brand) {
		super(host,"registrationV2/register",regulator,brand);
	}
	/**
	 * 
	 * @param email 邮箱
	 * @param affid 推广IB返佣账号
	 * @param cpaid CPA id
	 * @param firstName 名
	 * @param lastName 姓
	 * @param country 国家
	 * @param mobile 手机号
	 * @param source 来源
	 * @param createDemo 是否创建模拟账号
	 * @param phoneCode 手机区号
	 * @param ls lead source
	 * @param wid website id
	 * @param cxd cxd
	 * @param bizNum 业务编码
	 * @param firstLanguage 首选语言
	 * @param currency 币种
	 * @param accountType 账户类型
	 * @param tradingPlatform 交易平台
	 * @param balance 余额
	 * @param leverage 杠杆
	 * @param accessToken accessToken
	 * @param password password
	 * @param cid cid
	 //* @param reulator 监管
	 * @param campaignCode refer friend code
	 * @param referredBy 推荐人
	 * @return
	 */
	public String createUser(String email,String affid,String cpaid,
			String firstName, String lastName, String country,
			String mobile, String source, Boolean createDemo,String phoneCode,
			String ls,String wid,String cxd,String bizNum,String firstLanguage,
			String currency, Integer accountType, String tradingPlatform, 
			String balance,String leverage,String accessToken,
			String password,String cid,String regulator,String campaignCode,String referredBy) {
		JSONObject obj = new JSONObject();
		this.uri = this.create_uri;
		obj.put("email",email );
		obj.put("affid",affid );
		obj.put("cpaid", cpaid);
		obj.put("firstName", firstName );
		obj.put("lastName", lastName);
		obj.put("country",country );
		obj.put("mobile",mobile );
		obj.put("source", source);
		obj.put("createDemo", createDemo);
		obj.put("phoneCode", phoneCode);
		obj.put("ls", ls);
		obj.put("wid", wid);
		obj.put("cxd", cxd);
		obj.put("bizNum", bizNum);
		obj.put("firstLanguage", firstLanguage);
		obj.put("currency", currency);
		
		obj.put("accountType", accountType);
		obj.put("tradingPlatform", tradingPlatform);
		obj.put("leverage", leverage);
		obj.put("accessToken", accessToken);
		obj.put("password", password);
		obj.put("cid", cid);
		obj.put("regulator", regulator);
		obj.put("campaignCode", campaignCode);
		obj.put("referredBy", referredBy);
		return this.send(obj);
	}

	public String createUserNew(String email,String affid,String cpaid,
							 String firstName, String lastName, String country,
							 String mobile, String source, Boolean createDemo,String phoneCode,
							 String ls,String wid,String cxd,String bizNum,String firstLanguage,
							 String currency, Integer accountType, String tradingPlatform,
							 String balance,String leverage,String accessToken,
							 String password,String cid,String regulator,String campaignCode,String referredBy) {
		JSONObject obj = new JSONObject();
		this.uri = this.create_uri_New;

		obj.put("accountType", accountType);
		obj.put("country",country);
		obj.put("affid",affid );
		obj.put("cpaid", cpaid);
		obj.put("createDemo", createDemo);
		obj.put("currency", currency);
		obj.put("firstLanguage", firstLanguage);
		obj.put("firstName", firstName );
		obj.put("lastName", lastName);
		obj.put("mobile",mobile );
		obj.put("password", password);
		obj.put("phoneCode", phoneCode);
		obj.put("source", source);
		obj.put("tradingPlatform", tradingPlatform);
		obj.put("usCitizen", false);
		obj.put("wid", wid);


		return this.send(obj);
	}
	
	/**
	 * 
	 * @param userid 用户id
	 * @param lastName 
	 * @param firstName
	 * @param middleName
	 * @param email
	 * @param phoneCode
	 * @param mobile
	 * @param nationalityId
	 * @param dob
	 * @param placeOfBirth
	 * @param idNumber
	 * @return
	 */

	public String saveStep1(Integer userid,String lastName,String firstName,String middleName,
							String email,String phoneCode,String mobile,
							String nationalityId,String dob, String placeOfBirth,String idNumber) {
		this.uri = this.process_uri;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", "1");
		obj.put("lastName", lastName);
		obj.put("middleName", middleName);
		obj.put("firstName", firstName);
		obj.put("title", "Mr");

		obj.put("nationalityId", nationalityId);
		obj.put("email", email);
		obj.put("phoneCode", phoneCode);
		obj.put("mobile", mobile);
		obj.put("dob", dob);
		obj.put("placeOfBirth", placeOfBirth);
		obj.put("referredBy", "");
		obj.put("idType", "2");
		obj.put("idNumber", idNumber);
		return this.send(obj);
	}
	public String saveStep1New(Integer userid,String lastName,String firstName,String middleName,
			String email,String phoneCode,String mobile,
			String nationalityId,String dob, String placeOfBirth,String idNumber,String countryid,String gender) {
		this.uri = this.process_uri_New;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("email", email);
		obj.put("lastName", lastName);
		obj.put("firstName", firstName);
		obj.put("nationalityId", nationalityId);
		obj.put("phoneCode", phoneCode);
		obj.put("mobile", mobile);
		obj.put("dob", dob);
		obj.put("countryId", countryid);
		obj.put("gender", gender);
		obj.put("step", "1");
		//obj.put("placeOfBirth", placeOfBirth);
		//obj.put("idType", "2");
		//obj.put("idNumber", idNumber);
		//obj.put("referredBy", "");
		return this.send(obj);
	}
	
	public String saveStep2(Integer userid,Integer countryId, String address,
			String state,String suburb,String postcode) {
		this.uri = this.process_uri;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", "2");
		obj.put("countryId", countryId);
		obj.put("streetNumber", "188");
		obj.put("address", address);
		obj.put("state", "app api test state");
		obj.put("suburb", "app api test suburb");
		obj.put("postcode", "6666");
		obj.put("usCitizen", false);
		obj.put("taxResidencyCountryId", "5748");
		obj.put("yearsAtAddress", "4");
		return this.send(obj);
	}
	
	public String saveStep3(Integer userid) {
		this.uri=this.process_uri;
		String strAnswers = "";
		
		if("fca".equalsIgnoreCase(this.regulator)) {
			strAnswers = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[2]},{\"questionId\":\"2\",\"answers\":[289]},{\"questionId\":\"3\",\"answers\":[317]},{\"questionId\":\"4\",\"answers\":[338]},{\"questionId\":\"5\",\"answers\":[344]}],\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[349]},{\"questionId\":\"7\",\"answers\":[358]},{\"questionId\":\"8\",\"answers\":[363]},{\"questionId\":\"9\",\"answers\":[369]},{\"questionId\":\"10\",\"answers\":[375]},{\"questionId\":\"11\",\"answers\":[378]},{\"questionId\":\"12\",\"answers\":[382]},{\"questionId\":\"13\",\"answers\":[386]},{\"questionId\":\"14\",\"answers\":[389]},{\"questionId\":\"15\",\"answers\":[393]}]}";
		}if("um".equalsIgnoreCase(this.brand)) {
			//strAnswers = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"8\",\"answers\":[45]},{\"questionId\":\"9\",\"answers\":[49]},{\"questionId\":\"10\",\"answers\":[52]},{\"questionId\":\"4\",\"answers\":[19]},{\"questionId\":\"5\",\"answers\":[26]},{\"questionId\":\"12\",\"answers\":[60]},{\"questionId\":\"98\",\"answers\":[440]},{\"questionId\":\"99\",\"answers\":[442]},{\"questionId\":\"100\",\"answers\":[443]}],\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[38]},{\"questionId\":\"7\",\"answers\":[42]}]}";
			strAnswers = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[1]},{\"questionId\":\"2\",\"answers\":[9]},{\"questionId\":\"3\",\"answers\":[15]},{\"questionId\":\"4\",\"answers\":[20]},{\"questionId\":\"5\",\"answers\":[36]},{\"questionId\":\"8\",\"answers\":[45]},{\"questionId\":\"9\",\"answers\":[50]},{\"questionId\":\"10\",\"answers\":[53]},{\"questionId\":\"12\",\"answers\":[61]},{\"questionId\":\"13\",\"answers\":[81]},{\"questionId\":\"98\",\"answers\":[440]},{\"questionId\":\"99\",\"answers\":[442]},{\"questionId\":\"100\",\"answers\":[443]}],\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[38]},{\"questionId\":\"7\",\"answers\":[42]}]}";
			
		}else {
			strAnswers = "{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[2]},{\"questionId\":\"2\",\"answers\":[10]},{\"questionId\":\"3\",\"answers\":[18]},{\"questionId\":\"4\",\"answers\":[25]},{\"questionId\":\"5\",\"answers\":[36]}],\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[40]},{\"questionId\":\"7\",\"answers\":[44]}]}";
		}
		
		JSONObject answers = JSONObject.parseObject(strAnswers);
		answers.put("userId", userid);
		return this.send(answers);
	}
	public String saveStep4(Integer userid,String plateform,String accountType,String currency) {
		this.uri = this.process_uri;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", "4");
		obj.put("skipNextStep", false);
		obj.put("hideAccountType", false);
		obj.put("accountCreated", false);
		obj.put("accountReadOnly", false);

		obj.put("tradingPlatform", plateform);
		obj.put("accountType", accountType);
		obj.put("currency", currency);
		return this.send(obj);
	}
	public String saveStep4New(Integer userid,String plateform,String accountType,String currency,String source) {
		this.uri = this.process_uri_New;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", "4");
		//obj.put("source", source);
		//obj.put("skipNextStep", false);
		//obj.put("hideAccountType", false);
		//obj.put("accountCreated", false);
		//obj.put("accountReadOnly", false);
		//obj.put("checkStatus", true);
		//obj.put("accountStatus", 0);

		obj.put("tradingPlatform", plateform);
		obj.put("accountType", accountType);
		obj.put("currency", currency);
		obj.put("openAccountMethod", 1);
		return this.send(obj);
	}
	
	public String saveStep5(Integer userid) {
		String answer = "{\"idDocFilePathList\":[\"/VTG1/M00/02/68/CgoHQF-9yjKAdoRQAABY-r4QfNY46.jpeg\"],\"poaDocFilePathList\":[\"/VTG1/M00/02/68/CgoHQF-9yjKAdoRQAABY-r4QfNY46.jpeg\"],\"step\":5}";
		this.uri = this.process_uri;
		JSONObject obj = JSONObject.parseObject(answer);
		obj.put("userId", userid);
		
		return this.send(obj);
	}

	public String saveStep5New(Integer userid,String firstName,String middleName,String lastName,String nationalityId, String idType,String idNumber,String countryId,String tradingPlatform,String currency,String accountType,String address,String suburb,String state,String postcode) {
		//String answer = "{\"idDocFilePathList\":[\"https://crm-vt-alpha.s3.ap-southeast-1.amazonaws.com/other/1ee9881b8eb84168ac7ebf1e44a0dc38.jpg\"],\"poaDocFilePathList\":[\"https://crm-vt-alpha.s3.ap-southeast-1.amazonaws.com/other/1ee9881b8eb84168ac7ebf1e44a0dc38.jpg\"]}";
		String answer = "{\"idDocFilePathList\":[\"https://crm-vt-alpha.s3.ap-southeast-1.amazonaws.com/other/1ee9881b8eb84168ac7ebf1e44a0dc38.jpg\"]}";
		this.uri = this.process_uri_New;
		JSONObject obj = JSONObject.parseObject(answer);
		obj.put("userId", userid);
		obj.put("firstName", firstName);
		obj.put("lastName", lastName);
		obj.put("nationalityId", nationalityId);
		obj.put("countryId", countryId);
		obj.put("state", state);
		obj.put("suburb", suburb);
		obj.put("idType", idType);
		obj.put("streetNumber", "test");
		obj.put("streetName", "test");
		obj.put("idNumber", idNumber);
		obj.put("postcode", postcode);
		obj.put("documentType", "12");
		obj.put("address", address);

		obj.put("step","5");
		return this.send(obj);
	}

	public String uploadPOA(Integer userid,String countryId) {
		String answer = "{\"filePathList\":[\"https://crm-vt-alpha.s3.ap-southeast-1.amazonaws.com/other/1ee9881b8eb84168ac7ebf1e44a0dc38.jpg\"]}";
		this.uri = this.upload_poa;
		JSONObject obj = JSONObject.parseObject(answer);
		obj.put("userId", userid);
		//11 for address, 12 for ID
		obj.put("fileType", "11");
		obj.put("countryId", countryId);
		//1. general ; 2 = IBT
		obj.put("auditType", "1");
		return this.send(obj);
	}
	public String getInfo(Integer userid, Integer step) {
		this.uri = this.get_uri;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", step);
		return this.send(obj);
	}

	public String getInfoNew(Integer userid, Integer step) {
		this.uri = this.get_uri_New;
		JSONObject obj = new JSONObject();
		obj.put("userId", userid);
		obj.put("step", step);
		return this.send(obj);
	}
}

