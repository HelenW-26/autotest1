package newcrm.utils.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.cpapi.CPAPIWithdraw;
import newcrm.global.GlobalMethods;
import utils.LogUtils;

public class Withdraw extends APIBase{
    /**
     *
     * @param host app server url from yannibot
     * @param regulator regulator
     */
    public Withdraw(String host,String regulator,String brand) {
        super(host, "/withdrawal/applyWithdrawal_cp", regulator,brand);
        
        //Cancel recent withdraw orders
        
    }
    /**
     *
     * @param userId 用户id
     * @param mtsAccount mts账户
     * @param qAccount q账户
     * @param sourceServerId 源服务器id
     * @param currency 币种
     * @param withdrawalType 出金类型
     * @param amount 数额
     * @param country 国家
     * @param noties 备注
     * @param importantNotes 重要事项
     * @param accountName 账户名
     * @param accountNumber 账户号码
     * @param cardNumberFirstFour 卡号前四位
     * @param cardNumberLastThree 卡号后三位
     * @param cardExpiry 使用期限
     * @param bankName 银行名
     * @param bsb bsb
     * @param swift swift
     * @param bankAddress 银行地址
     * @param accountHolderAddress 账号用户地址
     * @param sortCode sortCode
     * @param bankBranchName 银行分行名称
     * @param cryptoWalletAddress cryptoWallet地址为0x8E6fd509F491152bD377854ec3CeD86e96c2e94e
     * @param bankBranchCity 银行分行城市
     * @param bankBranchProvince 银行分行省
     * @param email 邮箱

     * @return
     */
    //Legacy method
    public String sendCryptoWithdraw(String userId,String mtsAccount,String qAccount,String sourceServerId,String currency,String withdrawalType,String amount,
                               String country,String noties,String importantNotes,String accountName,String accountNumber,String cardNumberFirstFour,
                               String cardNumberLastThree,String cardExpiry,String bankName,String bsb,String swift,String bankAddress,
                               String accountHolderAddress, String sortCode, String bankBranchName,String cryptoWalletAddress,
                               String bankBranchCity,String bankBranchProvince,String email){

        JSONObject obj = new JSONObject();
        obj.put("userId",userId);
        obj.put("mtsAccount",mtsAccount);
        obj.put("qAccount",qAccount);
        obj.put("sourceServerId",sourceServerId);
        obj.put("currency",currency);
        obj.put("withdrawalType",withdrawalType);
        obj.put("amount",amount);
        obj.put("country",country);
        obj.put("noties",noties);
        obj.put("importantNotes",importantNotes);
        obj.put("accountName",accountName);
        obj.put("accountNumber",accountNumber);
        obj.put("cardNumberFirstFour",cardNumberFirstFour);
        obj.put("cardNumberLastThree",cardNumberLastThree);
        obj.put("cardExpiry",cardExpiry);
        obj.put("bankName",bankName);
        obj.put("bsb",bsb);
        obj.put("swift",swift);
        obj.put("bankAddress",bankAddress);
        obj.put("accountHolderAddress",accountHolderAddress);
        obj.put("sortCode",sortCode);
        obj.put("bankBranchName",bankBranchName);
        obj.put("cryptoWalletAddress",cryptoWalletAddress);
        obj.put("bankBranchCity",bankBranchCity);
        obj.put("bankBranchProvince",bankBranchProvince);
        obj.put("email",email);
        return this.send(obj);
    }

    
    //New withdraw API /withdrawal/applyWithdrawal_cp_batch
    public String sendCryptoWithdrawBatch(String userId,String qAccount,String currency,String amount,String withdrawalType,String country,
            String accountName,String cardBeginSixDigits,String cardLastFourDigits,String expiryMonth,String expiryYear,
            String importantNotes,String cryptoWalletAddress){
    	this.uri = "/withdrawal/applyWithdrawal_cp_batch";

		JSONArray fileL = new JSONArray();
		fileL.add("/123");

		JSONObject objChild = new JSONObject();
		objChild.put("qAccount",qAccount);
		objChild.put("accountNumber","b");
		objChild.put("bankAddress","bb");
		objChild.put("bankName","bb");
		objChild.put("beneficiaryName","b");
		objChild.put("country",country);
		objChild.put("amount",amount);
		objChild.put("withdrawalType",withdrawalType);
		objChild.put("fileList",fileL);
		objChild.put("holderAddress",amount);
		objChild.put("sortCode","b");
		objChild.put("swift","b");
		objChild.put("sortCode","b");
		objChild.put("cryptoWalletAddress","0x8E6fd509F491152bD377854ec3CeD86e96c2e94e");
		objChild.put("importantNotes",importantNotes);

		if(GlobalMethods.getBrand().equalsIgnoreCase("star"))
		{
			objChild.put("addressType",3);
		}

		JSONArray withdrawA = new JSONArray();
		withdrawA.add(objChild);

		JSONObject obj = new JSONObject();
		obj.put("userId",userId);
		obj.put("withdrawalApplyDtoList",withdrawA);

        return this.send(obj);
    }
    
    //CPS Malaysia bank transfer withdraw 
    public String sendCPSWithdrawBatch(String userId,String qAccount,String currency,String amount,String withdrawalType){
    	
    	this.uri = "/withdrawal/applyWithdrawal_cp_batch";
        String obj = "{"
        		+ 	"    \"sourceServerId\": \"MT4\","
        		+ 	"    \"userId\": "+userId+","
        		+ 	"    \"withdrawalApplyDtoList\": ["
        		+ 	"        {"
        		+	"              \"accountName\": \"SmokeTest APP\",\r\n"
        		+ "                \"accountNumber\": \"12345678\",\r\n"
        		+ "                \"amount\": \""+ amount +"\",\r\n"
        		+ "        		   \"bankName\": \"CITI.MY\","
        		+ "                \"attachVariables\": \"{\\\"bank_code\\\":\\\"CITI.MY\\\",\\\"bank_branch\\\":\\\"apptest_branch\\\",\\\"card_name\\\":\\\"SmokeTest APP\\\",\\\"card_number\\\":\\\"12345678\\\",\\\"city\\\":null,\\\"province\\\":null}\",\r\n"
        		+ "                \"countryName\": \"MY\",\r\n"
        		+ "                \"currency\": \""+ currency +"\",\r\n"
        		+ "                \"fileList\": [],\r\n"
        		+ "                \"importantNotes\": \"\",\r\n"
        		+ "                \"ipAddress\": \"222.128.9.188\",\r\n"
        		+ "                \"item\": \"\",\r\n"
        		+ "                \"mandatory\": \"attach_variable,transaction_type,payment_method,order_currency,amount,notify_url,order_id,submit_time,timestamp,first_name,last_name,card_name,birthday,email,phone,zip,state,province,city,profiles,card_number,user_id,address,country\",\r\n"
        		+ "                \"mtsAccount\": \"\",\r\n"
        		+ "                \"orderCurrency\": \"458\",\r\n"
        		+ "                \"paymentMethodCode\": \"F00000_099\",\r\n"
        		+ "                \"qAccount\": "+ qAccount +",\r\n"
        		+ "                \"sourceServerId\": \"\",\r\n"
        		+ "                \"withdrawalType\": "+ withdrawalType +""
        		+ "        }"
        		+ "    ]"
        		+ "}";

		LogUtils.info("CPS Malaysia bank transfer withdraw\n"+obj);
        return this.send(obj);
    }

	// CPS Malaysia bank transfer withdraw
	public String sendCPSWithdrawBatchV1(String userId, String qAccount, String currency, String amount, String withdrawalType, String channelMerchantId) {
		this.uri = "/withdrawal/applyWithdrawal_cp_batch";

		// 使用JSONObject构建JSON对象
		JSONObject obj = new JSONObject();
		obj.put("sourceServerId", "MT4");
		obj.put("userId", userId);

		JSONArray withdrawalApplyDtoList = new JSONArray();
		JSONObject dto = new JSONObject();
		dto.put("channelMerchantId", channelMerchantId);
		dto.put("accountName", "SmokeTest APP");
		dto.put("accountNumber", "12345678");
		dto.put("amount", amount);
		dto.put("bankName", "CITI.MY");
		dto.put("attachVariables", "{\"bank_code\":\"CITI.MY\",\"bank_branch\":\"apptest_branch\",\"card_name\":\"SmokeTest APP\",\"card_number\":\"12345678\",\"city\":null,\"province\":null}");
		dto.put("countryName", "MY");
		dto.put("currency", currency);
		dto.put("fileList", new JSONArray());
		dto.put("importantNotes", "");
		dto.put("ipAddress", "222.128.9.188");
		dto.put("item", "");
		dto.put("mandatory", "attach_variable,transaction_type,payment_method,order_currency,amount,notify_url,order_id,submit_time,timestamp,first_name,last_name,card_name,birthday,email,phone,zip,state,province,city,profiles,card_number,user_id,address,country");
		dto.put("mtsAccount", "");
		dto.put("orderCurrency", "458");
		dto.put("paymentMethodCode", "F00000_099");
		dto.put("qAccount", qAccount);
		dto.put("sourceServerId", "");
		dto.put("withdrawalType", withdrawalType);

		withdrawalApplyDtoList.add(dto);
		obj.put("withdrawalApplyDtoList", withdrawalApplyDtoList);

		String jsonString = obj.toJSONString();
		LogUtils.info("CPS Malaysia bank transfer withdraw\n" + jsonString);
		return this.send(jsonString);
	}

    //Specified CPS withdraw - trading account
    public String sendSpecifiedCPSWithdraw(String userId,String qAccount,
    		String currency,String amount,String withdrawalType){    	
    	this.uri = "/withdrawal/applyWithdrawal_cp_batch";
    	String obj ="";

		switch (withdrawalType) {
			//Indonesia
			case "39":
    			obj = "{\"userId\":"+ userId +",\"sourceServerId\":\"MT4\",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"ID\",\"bankName\":\"BSSBR.ID\",\"bankAddress\":\"\",\"accountNumber\":\"12345678\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\""+currency+"\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"CPS automation\",\"bankBranchName\":\"Aceh\",\"bankCity\":\"Aceh\",\"bankProvince\":\"Aceh\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BSSBR.ID\\\",\\\"bank_branch\\\":\\\"Aceh\\\",\\\"card_name\\\":\\\"CPS automation\\\",\\\"card_number\\\":\\\"12345678\\\",\\\"city\\\":\\\"Aceh\\\",\\\"province\\\":\\\"Aceh\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"360\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}]}";
    			break; 
    		//Malaysia
    		case "6":
        		obj = "{\"sourceServerId\": \"MT4\",\"userId\": "+userId+",\"withdrawalApplyDtoList\": [{\"accountNumber\": \"3618294321423\",\"amount\": \""+amount+"\",\"importantNotes\":\"testcrm appAPICPS\",\"attachVariables\": \"{\\\"bank_code\\\":\\\"CITI.MY\\\",\\\"bank_branch\\\":\\\"apptest_branch\\\",\\\"card_name\\\":\\\"CPS automation\\\",\\\"card_number\\\":\\\"3618294321423\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"appTest Province\\\"}\",\"bankAccountName\": \"CPS automation\",\"bankAddress\": \"\",\"bankBranchName\": \"apptest_branch\",\"bankCity\": \"\",\"bankName\": \"CITI.MY\",\"bankProvince\": \"appTest Province\",\"beneficiaryName\": \"PostmanTest APP\",\"countryName\": \"MY\",\"currency\": \""+currency+"\",\"fileList\": [\"xxxrememberme\"],\"ipAddress\": \"222.128.9.188\",\"item\": \"\",\"mandatory\": \"attach_variable ,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transa ction_type,user_id,zip\",\"mtsAccount\": \"\",\"orderCurrency\": \"458\",\"paymentMethodCode\": \"F00000\",\"qAccount\": "+qAccount+",\"sourceServerId\": \"\",\"withdrawalType\": "+withdrawalType+"}]}";
        		break; 
        	//Thailand
        	case "5":
            	obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+ qAccount +",\"countryName\":\"TH\",\"bankName\":\"ICBKTH.TH\",\"accountNumber\":\"8547254412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Terasil\",\"bankBranchName\":\"Hatyai\",\"bankCity\":\"Hatyai\",\"bankProvince\":\"Hatyai\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ICBKTH.TH\\\",\\\"bank_branch\\\":\\\"Hatyai\\\",\\\"card_name\\\":\\\"Terasil\\\",\\\"card_number\\\":\\\"8547254412\\\",\\\"city\\\":\\\"Hatyai\\\",\\\"province\\\":\\\"Hatyai\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"764\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT5\"}";
            	break; 
            //Vietnam
        	case "8":
            	obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"VN\",\"bankName\":\"VCB.VN\",\"bankAddress\":\"\",\"accountNumber\":\"8755125412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Van Duc Hang\",\"bankBranchName\":\"Ho Chi Minh\",\"bankCity\":\"Ho Chi Minh\",\"bankProvince\":\"Ho Chi Minh\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"VCB.VN\\\",\\\"bank_branch\\\":\\\"Ho Chi Minh\\\",\\\"card_name\\\":\\\"Van Duc Hang\\\",\\\"card_number\\\":\\\"8755125412\\\",\\\"city\\\":\\\"Ho Chi Minh\\\",\\\"province\\\":\\\"Ho Chi Minh\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"704\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
            	break; 
            //Nigeria
        	case "9":
        		obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"VN\",\"bankName\":\"CITIBANK.NIGERIA.LTD.NG\",\"bankAddress\":\"\",\"accountNumber\":\"8755125412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"bankAccountName\":\"NigeriaAccName\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CITIBANK.NIGERIA.LTD.NG\\\",\\\"card_name\\\":\\\"NigeriaAccName\\\",\\\"card_number\\\":\\\"8755125412\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"566\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break; 
            //India
        	case "24":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"IN\",\"bankName\":\"ICICI.IN\",\"accountNumber\":\"3586987\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Suresh\",\"bankBranchName\":\"branch India\",\"bankCity\":\"City India\",\"bankProvince\":\"Province India\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"ifscCode\":\"8765\",\"phoneNumber\":\"012345678\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ICICI.IN\\\",\\\"bank_branch\\\":\\\"Red India\\\",\\\"card_name\\\":\\\"Suresh\\\",\\\"card_number\\\":\\\"3586987\\\",\\\"city\\\":\\\"Red India\\\",\\\"province\\\":\\\"Red India\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"356\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Korea
        	case "35":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"KR\",\"bankName\":\"CK.KR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CK.KR\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"410\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Philippines
        	case "40":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"PH\",\"bankName\":\"CBCS.PH\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CBCS.PH\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"608\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//South Africa
        	case "42":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"ZA\",\"bankName\":\"FNB.ZA\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"FNB.ZA\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"710\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Hong Kong - HKD
        	case "49":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"HK\",\"bankName\":\"A02\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"A02\\\",\\\"bank_branch\\\":\\\"002\\\",\\\"bank_account_currency\\\":\\\"HKD\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"344\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\",\"targetCurrency\":\"HKD\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Uganda
        	case "50":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"UG\",\"bankName\":\"SABMU.UG\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"SABMU.UG\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"800\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Rwanda
        	case "51":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"RW\",\"bankName\":\"RWART.RW\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"RWART.RW\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"646\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Cameroon
        	case "54":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"CM\",\"bankName\":\"CITIX.CM\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CITIX.CM\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"950\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Kenya
        	case "56":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"KE\",\"bankName\":\"ABSAK.KE\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ABSAK.KE\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"404\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Ghana
        	case "57":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"GH\",\"bankName\":\"BOG.GH\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BOG.GH\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"936\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Tanzania
        	case "58":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"TZ\",\"bankName\":\"BTZ.TZ\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BTZ.TZ\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"834\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Japan
        	case "62":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"JP\",\"bankName\":\"KYT.JP\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"KYT.JP\\\",\\\"bank_branch\\\":\\\"113\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"attach_account_type\\\":\\\"1\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"392\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Mexico
        	case "63":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"MX\",\"bankName\":\"VEPORMAS.MX\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"VEPORMAS.MX\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"APP Mexico Acc Digit\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"484\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Brazil
        	case "64":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"BR\",\"bankName\":\"ABCB.BR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ABCB.BR\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"APP Brazil Acc Digit\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"986\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
            //Interac
        	case "99":
        		obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"CA\",\"bankName\":\"\",\"bankAddress\":\"\",\"accountNumber\":\"nouse\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"bankAccountName\":\"nouse\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"card_name\\\":\\\"nouse\\\",\\\"card_number\\\":\\\"nouse\\\"}\",\"paymentMethodCode\":\"F00000_601\",\"orderCurrency\":\"124\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break; 
        	//PIX
        	case "100":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"BR\",\"bankName\":\"ABCB.BR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"986\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//Hong Kong - USD
        	case "101":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"HK\",\"bankName\":\"A00\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"A00\\\",\\\"bank_branch\\\":\\\"002\\\",\\\"bank_account_currency\\\":\\\"USD\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"840\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\",\"targetCurrency\":\"USD\"}],\"sourceServerId\":\"MT4\"}";
        		break;
    		//Korea
        	case "102":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"TW\",\"bankName\":\"617.TW\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"617.TW\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"901\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
        	//UAE
        	case "108":
        		obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"AE\",\"bankName\":\"uaebankname\",\"bankAccountName\":\"uaebankaccname\",\"bankAddress\":\"uaebankaddr\",\"accountNumber\":\"uae123123\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_name\\\":\\\"uaebankname\\\",\\\"bank_address\\\":\\\"uaebankaddr\\\",\\\"card_name\\\":\\\"uaebankaccname\\\",\\\"card_number\\\":\\\"uae123123\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"784\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
        		break;
            //Crypto V2 USDT
        	case "86":
               	obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"accountNumber\":\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\",\"importantNotes\":\"\",\"currency\":\""+currency+"\",\"withdrawalType\":"+withdrawalType+",\"fileList\":[],\"item\":\"\",\"cryptoWalletAddress\":\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":null,\\\"bank_branch\\\":null,\\\"card_name\\\":null,\\\"card_number\\\":\\\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\\\",\\\"city\\\":null,\\\"province\\\":null,\\\"ifsc_code\\\":null,\\\"attach_account_type\\\":null,\\\"attach_account_digit\\\":null,\\\"attach_document_type\\\":null,\\\"personal_id\\\":null,\\\"swift\\\":null,\\\"bank_account_currency\\\":null,\\\"fxir_id\\\":null,\\\"local_depositor\\\":null,\\\"bank_type\\\":null,\\\"bank_name\\\":null,\\\"bank_address\\\":null,\\\"holder_address\\\":null,\\\"transaction_tag\\\":\\\"T\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"usdt-trc20\",\"mandatory\":\"actual_currency,address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"raw\":\"{\\\"bank_code\\\":null,\\\"bank_branch\\\":null,\\\"card_name\\\":null,\\\"card_number\\\":\\\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\\\",\\\"city\\\":null,\\\"province\\\":null,\\\"ifsc_code\\\":null,\\\"attach_account_type\\\":null,\\\"attach_account_digit\\\":null,\\\"attach_document_type\\\":null,\\\"personal_id\\\":null,\\\"swift\\\":null,\\\"bank_account_currency\\\":null,\\\"fxir_id\\\":null,\\\"local_depositor\\\":null,\\\"bank_type\\\":null,\\\"bank_name\\\":null,\\\"bank_address\\\":null,\\\"holder_address\\\":null,\\\"transaction_tag\\\":\\\"T\\\"}\",\"currencyNumber\":\"usdt\"}],\"sourceServerId\":\"MT4\",\"emailTxId\":\"\",\"emailCode\":\"\",\"validateCode\":\"\",\"phoneTxId\":\"appPhoneCode\",\"phoneCode\":\"123456\"}";
               	break;
        	default:
    			break;
		}System.out.println(obj);
    	return this.send(obj);
    }


	public String sendAPPSpecifiedCPSWithdraw(String userId,String qAccount,
										   String currency,String amount,String withdrawalType,String channelMerchantId){
		this.uri = "/withdrawal/applyWithdrawal_cp_batch";
		String obj ="";

		switch (withdrawalType) {
			//Indonesia
			case "39":
				obj = "{\"userId\":"+ userId +",\"sourceServerId\":\"MT4\",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"ID\",\"bankName\":\"BSSBR.ID\",\"bankAddress\":\"\",\"accountNumber\":\"12345678\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\""+currency+"\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"CPS automation\",\"bankBranchName\":\"Aceh\",\"bankCity\":\"Aceh\",\"bankProvince\":\"Aceh\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BSSBR.ID\\\",\\\"bank_branch\\\":\\\"Aceh\\\",\\\"card_name\\\":\\\"CPS automation\\\",\\\"card_number\\\":\\\"12345678\\\",\\\"city\\\":\\\"Aceh\\\",\\\"province\\\":\\\"Aceh\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"360\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}]}";
				break;
			//Malaysia
			case "6":
				obj = "{\"sourceServerId\": \"MT4\",\"userId\": "+userId+",\"withdrawalApplyDtoList\": [{\"accountNumber\": \"3618294321423\",\"amount\": \""+amount+"\",\"importantNotes\":\"testcrm appAPICPS\",\"attachVariables\": \"{\\\"bank_code\\\":\\\"CITI.MY\\\",\\\"bank_branch\\\":\\\"apptest_branch\\\",\\\"card_name\\\":\\\"CPS automation\\\",\\\"card_number\\\":\\\"3618294321423\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"appTest Province\\\"}\",\"bankAccountName\": \"CPS automation\",\"bankAddress\": \"\",\"bankBranchName\": \"apptest_branch\",\"bankCity\": \"\",\"bankName\": \"CITI.MY\",\"bankProvince\": \"appTest Province\",\"beneficiaryName\": \"PostmanTest APP\",\"countryName\": \"MY\",\"currency\": \""+currency+"\",\"fileList\": [\"xxxrememberme\"],\"ipAddress\": \"222.128.9.188\",\"item\": \"\",\"mandatory\": \"attach_variable ,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transa ction_type,user_id,zip\",\"mtsAccount\": \"\",\"orderCurrency\": \"458\",\"paymentMethodCode\": \"F00000\",\"qAccount\": "+qAccount+",\"sourceServerId\": \"\",\"withdrawalType\": "+withdrawalType+"}]}";
				break;
			//Thailand
			case "5":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+ qAccount +",\"countryName\":\"TH\",\"bankName\":\"ICBKTH.TH\",\"accountNumber\":\"8547254412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Terasil\",\"bankBranchName\":\"Hatyai\",\"bankCity\":\"Hatyai\",\"bankProvince\":\"Hatyai\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ICBKTH.TH\\\",\\\"bank_branch\\\":\\\"Hatyai\\\",\\\"card_name\\\":\\\"Terasil\\\",\\\"card_number\\\":\\\"8547254412\\\",\\\"city\\\":\\\"Hatyai\\\",\\\"province\\\":\\\"Hatyai\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"764\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT5\"}";
				break;
			//Vietnam
			case "8":
				obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"VN\",\"bankName\":\"VCB.VN\",\"bankAddress\":\"\",\"accountNumber\":\"8755125412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Van Duc Hang\",\"bankBranchName\":\"Ho Chi Minh\",\"bankCity\":\"Ho Chi Minh\",\"bankProvince\":\"Ho Chi Minh\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"VCB.VN\\\",\\\"bank_branch\\\":\\\"Ho Chi Minh\\\",\\\"card_name\\\":\\\"Van Duc Hang\\\",\\\"card_number\\\":\\\"8755125412\\\",\\\"city\\\":\\\"Ho Chi Minh\\\",\\\"province\\\":\\\"Ho Chi Minh\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"704\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Nigeria
			case "9":
				obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"VN\",\"bankName\":\"CITIBANK.NIGERIA.LTD.NG\",\"bankAddress\":\"\",\"accountNumber\":\"8755125412\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"bankAccountName\":\"NigeriaAccName\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CITIBANK.NIGERIA.LTD.NG\\\",\\\"card_name\\\":\\\"NigeriaAccName\\\",\\\"card_number\\\":\\\"8755125412\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"566\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//India
			case "24":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"IN\",\"bankName\":\"ICICI.IN\",\"accountNumber\":\"3586987\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Suresh\",\"bankBranchName\":\"branch India\",\"bankCity\":\"City India\",\"bankProvince\":\"Province India\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"ifscCode\":\"8765\",\"phoneNumber\":\"012345678\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ICICI.IN\\\",\\\"bank_branch\\\":\\\"Red India\\\",\\\"card_name\\\":\\\"Suresh\\\",\\\"card_number\\\":\\\"3586987\\\",\\\"city\\\":\\\"Red India\\\",\\\"province\\\":\\\"Red India\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"356\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Korea
			case "35":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"KR\",\"bankName\":\"CK.KR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CK.KR\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"410\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Philippines
			case "40":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"PH\",\"bankName\":\"CBCS.PH\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CBCS.PH\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"608\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//South Africa
			case "42":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"ZA\",\"bankName\":\"FNB.ZA\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"FNB.ZA\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"710\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Hong Kong - HKD
			case "49":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"HK\",\"bankName\":\"A02\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"A02\\\",\\\"bank_branch\\\":\\\"002\\\",\\\"bank_account_currency\\\":\\\"HKD\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"344\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\",\"targetCurrency\":\"HKD\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Uganda
			case "50":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"UG\",\"bankName\":\"SABMU.UG\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"SABMU.UG\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"800\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Rwanda
			case "51":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"RW\",\"bankName\":\"RWART.RW\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"RWART.RW\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"646\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Cameroon
			case "54":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"CM\",\"bankName\":\"CITIX.CM\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"CITIX.CM\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"950\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Kenya
			case "56":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"KE\",\"bankName\":\"ABSAK.KE\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ABSAK.KE\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"404\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Ghana
			case "57":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"GH\",\"bankName\":\"BOG.GH\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BOG.GH\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"936\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Tanzania
			case "58":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"TZ\",\"bankName\":\"BTZ.TZ\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"BTZ.TZ\\\",\\\"bank_branch\\\":\\\"Branch APP\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"834\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Japan
			case "62":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"JP\",\"bankName\":\"KYT.JP\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"KYT.JP\\\",\\\"bank_branch\\\":\\\"113\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"attach_account_type\\\":\\\"1\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"392\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Mexico
			case "63":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"MX\",\"bankName\":\"VEPORMAS.MX\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"VEPORMAS.MX\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"APP Mexico Acc Digit\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"484\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Brazil
			case "64":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"BR\",\"bankName\":\"ABCB.BR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"ABCB.BR\\\",\\\"bank_branch\\\":\\\"Test cnb sav\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"APP Brazil Acc Digit\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"986\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Interac
			case "99":
				obj = "{\"userId\":"+ userId +",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"CA\",\"bankName\":\"\",\"bankAddress\":\"\",\"accountNumber\":\"nouse\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"bankAccountName\":\"nouse\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"card_name\\\":\\\"nouse\\\",\\\"card_number\\\":\\\"nouse\\\"}\",\"paymentMethodCode\":\"F00000_601\",\"orderCurrency\":\"124\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//PIX
			case "100":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"BR\",\"bankName\":\"ABCB.BR\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"attach_account_type\\\":\\\"PHONE\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"personal_id\\\":\\\"Pesonal ID\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"986\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Hong Kong - USD
			case "101":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"HK\",\"bankName\":\"A00\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"Test cnb sav\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"A00\\\",\\\"bank_branch\\\":\\\"002\\\",\\\"bank_account_currency\\\":\\\"USD\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"840\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\",\"targetCurrency\":\"USD\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Korea
			case "102":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"TW\",\"bankName\":\"617.TW\",\"bankAddress\":\"\",\"accountNumber\":\"1234567890\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"617.TW\\\",\\\"card_name\\\":\\\"Test Mr \\\",\\\"card_number\\\":\\\"1234567890\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"901\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//UAE
			case "108":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"countryName\":\"AE\",\"bankName\":\"uaebankname\",\"bankAccountName\":\"uaebankaccname\",\"bankAddress\":\"uaebankaddr\",\"accountNumber\":\"uae123123\",\"importantNotes\":\"testcrm appAPICPS\",\"currency\":\"USD\",\"withdrawalType\":"+withdrawalType+",\"accountName\":\"Test Mr \",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"fileList\":[],\"item\":\"\",\"isRememberInfo\":\"1\",\"sourceServerId\":\"\",\"mtsAccount\":\"\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_name\\\":\\\"uaebankname\\\",\\\"bank_address\\\":\\\"uaebankaddr\\\",\\\"card_name\\\":\\\"uaebankaccname\\\",\\\"card_number\\\":\\\"uae123123\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"784\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\"}],\"sourceServerId\":\"MT4\"}";
				break;
			//Crypto V2 USDT
			case "86":
				obj = "{\"userId\":"+userId+",\"withdrawalApplyDtoList\":[{\"channelMerchantId\":\""+channelMerchantId+"\",\"amount\":\""+amount+"\",\"qAccount\":"+qAccount+",\"accountNumber\":\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\",\"importantNotes\":\"\",\"currency\":\""+currency+"\",\"withdrawalType\":"+withdrawalType+",\"fileList\":[],\"item\":\"\",\"cryptoWalletAddress\":\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\",\"ipAddress\":\"175.136.241.189\",\"attachVariables\":\"{\\\"bank_code\\\":null,\\\"bank_branch\\\":null,\\\"card_name\\\":null,\\\"card_number\\\":\\\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\\\",\\\"city\\\":null,\\\"province\\\":null,\\\"ifsc_code\\\":null,\\\"attach_account_type\\\":null,\\\"attach_account_digit\\\":null,\\\"attach_document_type\\\":null,\\\"personal_id\\\":null,\\\"swift\\\":null,\\\"bank_account_currency\\\":null,\\\"fxir_id\\\":null,\\\"local_depositor\\\":null,\\\"bank_type\\\":null,\\\"bank_name\\\":null,\\\"bank_address\\\":null,\\\"holder_address\\\":null,\\\"transaction_tag\\\":\\\"T\\\"}\",\"paymentMethodCode\":\"F00000\",\"orderCurrency\":\"usdt-trc20\",\"mandatory\":\"actual_currency,address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"raw\":\"{\\\"bank_code\\\":null,\\\"bank_branch\\\":null,\\\"card_name\\\":null,\\\"card_number\\\":\\\"TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw\\\",\\\"city\\\":null,\\\"province\\\":null,\\\"ifsc_code\\\":null,\\\"attach_account_type\\\":null,\\\"attach_account_digit\\\":null,\\\"attach_document_type\\\":null,\\\"personal_id\\\":null,\\\"swift\\\":null,\\\"bank_account_currency\\\":null,\\\"fxir_id\\\":null,\\\"local_depositor\\\":null,\\\"bank_type\\\":null,\\\"bank_name\\\":null,\\\"bank_address\\\":null,\\\"holder_address\\\":null,\\\"transaction_tag\\\":\\\"T\\\"}\",\"currencyNumber\":\"usdt\"}],\"sourceServerId\":\"MT4\",\"emailTxId\":\"\",\"emailCode\":\"\",\"validateCode\":\"\",\"phoneTxId\":\"appPhoneCode\",\"phoneCode\":\"123456\"}";
				break;
			default:
				break;
		}System.out.println(obj);
		return this.send(obj);
	}
    //New withdraw API /withdrawal/applyWithdrawal_cp_batch
    public String sendCCWithdrawBatch(String userId,String qAccount,String currency,String amount,String withdrawalType,
            String accountName,String cardBeginSixDigits,String cardLastFourDigits,String expiryMonth,String expiryYear,
            String importantNotes){
    	
    	this.uri = "/withdrawal/applyWithdrawal_cp_batch";
        String obj = "{"
        		+ "    \"userId\": "+userId+","
        		+ "    \"withdrawalApplyDtoList\": ["
        		+ "        {"
        		+ "             \"qAccount\": \""+qAccount+"\","
        		+ "             \"amount\": "+amount+","
        		+ "	            \"withdrawalType\": "+withdrawalType+","
        		+ "	            \"creditCardWithdrawalRequests\": ["
        		+ "		         {"
        		+ "			           \"currency\": \""+currency+"\","
        		+ "			           \"creditCard\": {"
        		+ "                         \"id\": 2,"
        		+ "                         \"card_holder_name\": \""+accountName+"\","
        		+ "                         \"card_begin_six_digits\": \""+cardBeginSixDigits+"\","
        		+ "                         \"card_last_four_digits\": \""+cardLastFourDigits+"\","
        		+ "                         \"expiry_month\": "+expiryMonth+","
        		+ "                         \"expiry_year\": "+expiryYear+""
        		+ "                    },"
        		+ "			    \"importantNotes\": \""+importantNotes+"\","
        		+ "			    \"withdrawAmount\": "+amount+""
        		+ "		        }]"  
        		+ "        }"
        		+ "    ]"
        		+ "}";

        System.out.println(obj);
        return this.send(obj);
    }
    
    //E-Wallet CPS withdrawal
    public String sendEwalletWithdrawBatch(String userId,String qAccount,String currency, String ordercurrency,String amount,String withdrawalType, String paymentMethodCode){
    	
    	this.uri = "/withdrawal/applyWithdrawal_cp_batch";
    	String obj = "{"
    	    + "\"userId\": " + userId + ","
    	    + "\"withdrawalApplyDtoList\": ["
    	    + "{"
    	    + "\"qAccount\": \"" + qAccount + "\","
    	    + "\"amount\": " + amount + ","
    	    + "\"withdrawalType\": " + withdrawalType + ","
    	    + "\"bankName\": \"\","
    	    + "\"bankProvince\": \"\","
    	    + "\"bankCity\": \"\","
    	    + "\"bankAccountName\": \"bankaccname\","
    	    + "\"bankBranchName\": \"\","
    	    + "\"accountName\": \"bankaccname\","
    	    + "\"accountNumber\": \"123123123\","
    	    + "\"orderCurrency\": \"" + ordercurrency + "\","
    	    + "\"paymentMethodCode\": \""+ paymentMethodCode +"\","
    	    + "\"currency\": \""+ currency +"\","
    	    + "\"ebuyEmail\": \"test@test.com\","
    	    + "\"attachVariables\":\"{\\\"card_name\\\":\\\"bankaccname\\\",\\\"card_number\\\":\\\"123123123\\\",\\\"fxir_id\\\":\\\"senderid\\\"}\","
    	    + "\"mandatory\": \"address,amount,birthday,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip\","
    	    + "\"targetCurrency\":\"\","
    	    + "\"code\":\"123123\","
    	    + "\"importantNotes\": \"testAppEwallet\""
    	    + "}"
    	    + "],"
    	    + "\"sourceServerId\":\"MT4\""
    	    + "}";

        return this.send(obj);
    }
    
    
    public static void main(String[]args){
        String host= "app-core-vt.crm-alpha.com";
        Withdraw withdraw =new Withdraw(host,"SVG","VT");

		
		/*
		 * String result
		 * =withdraw.sendCryptoWithdrawBatch("10011286","808000116","USD","100","36",
		 * "2", "test app","511566","3344","6","2029",
		 * "api test app withdrawal","0x8E6fd509F491152bD377854ec3CeD86e96c2e94e");
		 */

		
		  String result
		  =withdraw.sendCCWithdrawBatch("10011286","808000116","USD","100","1",
		  "test app","511566","3344","6","2029", "api test app withdrawal");
		 
        
        System.out.println(result);
    }
}
