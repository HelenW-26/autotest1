package newcrm.cpapi.CustomPaymentPayload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;



public class submitWDPayloadBuilder {

	/* ------------------------------------ Start of Withdrawal Payload ------------------------------------*/
	//E-Wallet withdrawal
	public static String eWalletWDRequest(JSONObject accInfo, Map<String, String> channelDetails, BigDecimal wdAmt) {
		String obj = "[{"
				+ "\"qAccount\":" + accInfo.getString("account") + ","
				+ "\"amount\":"+wdAmt+","
				+ "\"withdrawalType\":" + channelDetails.get("wdtype") + ","
				+ "\"importantNotes\":\"testapiwithdrawal\","
				+ "\"isRememberInfo\":true,"
				+ "\"userPaymentInfoId\":\"\","
				+ "\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\","
				+ "\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\","
				+ "\"attachVariables\":\"{\\\"card_name\\\":\\\"autotestcrmutdb testcrm\\\",\\\"card_number\\\":\\\"email@test.com\\\"}\","
				+ "\"mandatory\":\"address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\","
				+ "\"fileList\":[],"
				+ "\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\","
				+ "\"withdrawChannel\":" + channelDetails.get("wdchannel") + ","
				+ "\"accountNumber\":\"email@test.com\","
				+ "\"bankAccountName\":\"autotestcrmutdb testcrm\","
				+ "\"raw\":\"{\\\"accountNumber\\\":\\\"email@test.com\\\",\\\"bankAccountName\\\":\\\"autotestcrmutdb testcrm\\\"}\","
				+ "\"device\":\"web\""
				+ ",\"channelMerchantId\":" + channelDetails.get("channelMerchantId")
				+ "}]";

		return obj;
	}

	//LBT and IBT withdrawal
	public static String bankTranWDRequest(JSONObject accInfo, Map<String, String> channelDetails, BigDecimal wdAmt) {
		String obj = "";
		switch (channelDetails.get("wdCpsCode")) {
			case "F00000":
				switch (channelDetails.get("wdActcurrencyCode")) {
					//bankName,bankAccName,bankAccNum,bankBranch
					case "MYR": //Msia Bank Transfer
					case "VND": //Viet Bank Transfer
					case "THB": //Thai Bank Transfer
					case "PHP": //Php Bank Transfer
						obj = "[{\"channelMerchantId\":" + channelDetails.get("channelMerchantId") +",\"qAccount\":" + accInfo.getString("account") + ",\"amount\":"+wdAmt+",\"withdrawalType\":" + channelDetails.get("wdtype") + ",\"importantNotes\":\"testapiwithdrawal\",\"isRememberInfo\":true,\"userPaymentInfoId\":\"\",\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\",\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"bank_branch\\\":\\\"bank branch\\\",\\\"card_name\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"card_number\\\":\\\"123123123\\\"}\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"fileList\":[],\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\",\"withdrawChannel\":" + channelDetails.get("wdchannel") + ",\"bankName\":\"" + channelDetails.get("bankCode") + "\",\"accountNumber\":\"123123123\",\"bankAccountName\":\"testcrm Automation APIwithdrawaluser\",\"bankBranchName\":\"bank branch\",\"bankCity\":\"\",\"bankProvince\":\"\",\"ifscCode\":\"\",\"bankBranchCode\":\"\",\"bankAccType\":\"\",\"targetCurrency\":\"\",\"attachDocumentType\":\"\",\"accountDigit\":\"\",\"personalId\":\"\",\"swift\":\"\",\"attachRegion\":\"\",\"bsbCode\":\"\",\"ebuyEmail\":\"\",\"bankAddress\":\"\",\"sortCode\":\"\",\"holderAddress\":\"\",\"raw\":\"{\\\"bankName\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"accountNumber\\\":\\\"123123123\\\",\\\"bankAccountName\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"bankBranchName\\\":\\\"bank branch\\\"}\",\"device\":\"web\",\"followerResultIds\":[]}]";
						break;
					//bankName,bankAccName,bankAccNum
					case "KRW": //Korea Bank Transfer
						obj = "[{\"channelMerchantId\":" + channelDetails.get("channelMerchantId") +",\"qAccount\":" + accInfo.getString("account") + ",\"amount\":"+wdAmt+",\"withdrawalType\":" + channelDetails.get("wdtype") + ",\"importantNotes\":\"testapiwithdrawal\",\"isRememberInfo\":true,\"userPaymentInfoId\":\"\",\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\",\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"card_name\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"card_number\\\":\\\"123123123\\\"}\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"fileList\":[],\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\",\"withdrawChannel\":" + channelDetails.get("wdchannel") + ",\"bankName\":\"" + channelDetails.get("bankCode") + "\",\"accountNumber\":\"123123123\",\"bankAccountName\":\"testcrm Automation APIwithdrawaluser\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"ifscCode\":\"\",\"bankBranchCode\":\"\",\"bankAccType\":\"\",\"targetCurrency\":\"\",\"attachDocumentType\":\"\",\"accountDigit\":\"\",\"personalId\":\"\",\"swift\":\"\",\"attachRegion\":\"\",\"bsbCode\":\"\",\"ebuyEmail\":\"\",\"bankAddress\":\"\",\"sortCode\":\"\",\"holderAddress\":\"\",\"raw\":\"{\\\"bankName\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"accountNumber\\\":\\\"123123123\\\",\\\"bankAccountName\\\":\\\"testcrm Automation APIwithdrawaluser\\\"}\",\"device\":\"web\",\"followerResultIds\":[]}]";
						break;
					//bankName,bankAccName,bankAccNum,bankAccType
					case "JPY": //Japan Bank Transfer
						obj = "[{\"channelMerchantId\":" + channelDetails.get("channelMerchantId") +",\"qAccount\":" + accInfo.getString("account") + ",\"amount\":"+wdAmt+",\"withdrawalType\":" + channelDetails.get("wdtype") + ",\"importantNotes\":\"testapiwithdrawal\",\"isRememberInfo\":true,\"userPaymentInfoId\":\"\",\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\",\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"attach_account_type\\\":\\\"Account Type\\\",\\\"card_name\\\":\\\"テストクルム・オートメーション・アピウィスドラワリューサー\\\",\\\"card_number\\\":\\\"123123123\\\"}\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"fileList\":[],\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\",\"withdrawChannel\":" + channelDetails.get("wdchannel") + ",\"bankName\":\"" + channelDetails.get("bankCode") + "\",\"accountNumber\":\"123123123\",\"bankAccountName\":\"テストクルム・オートメーション・アピウィスドラワリューサー\",\"bankBranchName\":\"\",\"bankCity\":\"\",\"bankProvince\":\"\",\"ifscCode\":\"\",\"bankBranchCode\":\"\",\"bankAccType\":\"Account Type\",\"targetCurrency\":\"\",\"attachDocumentType\":\"\",\"accountDigit\":\"\",\"personalId\":\"\",\"swift\":\"\",\"attachRegion\":\"\",\"bsbCode\":\"\",\"ebuyEmail\":\"\",\"bankAddress\":\"\",\"sortCode\":\"\",\"holderAddress\":\"\",\"raw\":\"{\\\"bankName\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"accountNumber\\\":\\\"123123123\\\",\\\"bankAccountName\\\":\\\"テストクルム・オートメーション・アピウィスドラワリューサー\\\",\\\"bankAccType\\\":\\\"Account Type\\\"}\",\"device\":\"web\",\"followerResultIds\":[]}]";
						break;
					//non-PCS usage - only STAR using
					default:
						obj = "[{\"channelMerchantId\":" + channelDetails.get("channelMerchantId") +",\"qAccount\":" + accInfo.getString("account") + ",\"amount\":"+wdAmt+",\"withdrawalType\":" + channelDetails.get("wdtype") + ",\"importantNotes\":\"testapiwithdrawal\",\"isRememberInfo\":true,\"userPaymentInfoId\":\"\",\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\",\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\",\"attachVariables\":\"{\\\"bank_code\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"attach_account_type\\\":\\\"CHECKING\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"accdigit123\\\",\\\"personal_id\\\":\\\"taxid123\\\",\\\"bank_branch\\\":\\\"branchcode\\\",\\\"card_name\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"card_number\\\":\\\"123123123\\\"}\",\"mandatory\":\"attach_variable,address,amount,birthday,bank_branch,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"fileList\":[],\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\",\"bankName\":\"" + channelDetails.get("bankCode") + "\",\"accountNumber\":\"123123123\",\"bankAccountName\":\"testcrm Automation APIwithdrawaluser\",\"bankBranchName\":\"branchcode\",\"bankCity\":\"\",\"bankProvince\":\"\",\"ifscCode\":\"\",\"bankBranchCode\":\"\",\"bankAccType\":\"CHECKING\",\"targetCurrency\":\"\",\"attachDocumentType\":\"CPF\",\"accountDigit\":\"accdigit123\",\"personalId\":\"taxid123\",\"swift\":\"\",\"attachRegion\":\"\",\"bsbCode\":\"\",\"ebuyEmail\":\"\",\"bankAddress\":\"\",\"sortCode\":\"\",\"holderAddress\":\"\",\"raw\":\"{\\\"bank_code\\\":\\\"" + channelDetails.get("bankCode") + "\\\",\\\"attach_account_type\\\":\\\"CHECKING\\\",\\\"attach_document_type\\\":\\\"CPF\\\",\\\"attach_account_digit\\\":\\\"accdigit123\\\",\\\"personal_id\\\":\\\"taxid123\\\",\\\"bank_branch\\\":\\\"branchcode\\\",\\\"card_name\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"card_number\\\":\\\"123123123\\\"}\"}]";
						break;
				}
				break;
			case "F00000_500": //IBT
				obj = "[{\"channelMerchantId\":" + channelDetails.get("channelMerchantId") +",\"qAccount\":" + accInfo.getString("account") + ",\"amount\":"+wdAmt+",\"withdrawalType\":" + channelDetails.get("wdtype") + ",\"importantNotes\":\"testapiwithdrawal\",\"isRememberInfo\":true,\"userPaymentInfoId\":\"\",\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\",\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\",\"attachVariables\":\"{\\\"swift\\\":\\\"testswift\\\",\\\"bank_name\\\":\\\"testbankname\\\",\\\"bank_address\\\":\\\"testbankaddr\\\",\\\"sort_code\\\":\\\"testaba\\\",\\\"holder_address\\\":\\\"testAHA\\\",\\\"card_name\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"card_number\\\":\\\"123123123\\\"}\",\"mandatory\":\"attach_variable,address,amount,birthday,card_name,card_number,city,country,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,remarks,state,submit_time,timestamp,transaction_type,user_id,zip,transaction_tag\",\"fileList\":[\"https://crm-vt-alpha.s3.ap-southeast-1.amazonaws.com/other/390779ad73c340c68df7e14c71ae1d6b.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250320T054528Z&X-Amz-SignedHeaders=host&X-Amz-Expires=599&X-Amz-Credential=AKIA6LZROUZKAQU5T4EI%2F20250320%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=32aac0bbb2ed2442396dc3304b0f0856a3de4f63d973db667095c4f444faf42d\"],\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\",\"withdrawChannel\":" + channelDetails.get("wdchannel") + ",\"bankName\":\"testbankname\",\"accountNumber\":\"123123123\",\"bankAccountName\":\"testcrm Automation APIwithdrawaluser\",\"swift\":\"testswift\",\"bankAddress\":\"testbankaddr\",\"sortCode\":\"testaba\",\"holderAddress\":\"testAHA\",\"raw\":\"{\\\"bankName\\\":\\\"testbankname\\\",\\\"accountNumber\\\":\\\"123123123\\\",\\\"bankAccountName\\\":\\\"testcrm Automation APIwithdrawaluser\\\",\\\"swift\\\":\\\"testswift\\\",\\\"bankAddress\\\":\\\"testbankaddr\\\",\\\"sortCode\\\":\\\"testaba\\\",\\\"holderAddress\\\":\\\"testAHA\\\"}\",\"device\":\"web\",\"followerResultIds\":[]}]";
				break;
			default:
				obj = "";
				break;
		}
		return obj;
	}

	//Crypto withdrawal
	public static String cryptoWDRequest(JSONObject accInfo, Map<String, String> channelDetails, BigDecimal wdAmt) {
		String obj = "[{"
				+ "\"qAccount\":" + accInfo.getString("account") + ","
				+ "\"amount\":"+wdAmt+","
				+ "\"withdrawalType\":" + channelDetails.get("wdtype") + ","
				+ "\"importantNotes\":\"testapiwithdrawal\","
				+ "\"isRememberInfo\":true,"
				+ "\"userPaymentInfoId\":\"\","
				+ "\"orderCurrency\":\"" + channelDetails.get("wdActcurrency") + "\","
				+ "\"paymentMethodCode\":\"" + channelDetails.get("wdCpsCode") + "\","
				+ "\"attachVariables\":\"{\\\"card_number\\\":\\\"" + getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode")) + "\\\"}\","
				+ "\"mandatory\":\"address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\","
				+ "\"currencyNumber\":\"" + channelDetails.get("wdcurrency") + "\","
				+ "\"withdrawChannel\":" + channelDetails.get("wdchannel") + ","
				+ "\"accountNumber\":\"" + getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode")) + "\","
				+ "\"bankAccountName\":\"\","
				+ "\"raw\":\"{\\\"accountNumber\\\":\\\"" + getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode")) + "\\\"}\","
				+ "\"device\":\"web\""
				+ ",\"channelMerchantId\":" + channelDetails.get("channelMerchantId")

				+ "}]";

		return obj;
	}

	//Credit Card withdrawal
	public static String creditCardWDRequest(JSONObject accInfo, JSONObject ccDetails, BigDecimal wdAmt){
		JSONObject cc = ccDetails.getJSONObject("creditCard");
		String obj = "[{"
				+ "\"qAccount\":" + accInfo.getString("account") + ","
				+ "\"amount\":"+wdAmt+","
				+ "\"withdrawalType\":1,"
				+ "\"importantNotes\":\"\","
				+ "\"isRememberInfo\":true,"
				+ "\"userPaymentInfoId\":\"\","
				+ "\"creditCardWithdrawalRequests\":[{"
				+ "\"currency\":\"" + accInfo.getString("currency") + "\","
				+ "\"creditCard\":{"
				+ "\"id\":" + cc.get("id") + ","
				+ "\"card_holder_name\":\"" + cc.get("card_holder_name") + "\","
				+ "\"card_begin_six_digits\":\"" + cc.get("card_begin_six_digits") + "\","
				+ "\"card_last_four_digits\":\"" + cc.get("card_last_four_digits") + "\","
				+ "\"expiry_month\":" + cc.get("expiry_month") + ","
				+ "\"expiry_year\":" + cc.get("expiry_year") + ""
				+ "},"
				+ "\"importantNotes\":\"\","
				+ "\"withdrawAmount\":"+wdAmt+ "}],"
				+ "\"followerResultIds\":[]"
				+ "}]";

		return obj;
	}

	//Big amount withdrawal(Crypto)
	public static String bigAmountWDRequest(JSONObject accInfo, Map<String, String> channelDetails, BigDecimal amount){
		String obj = "[{"
				+ "\"qAccount\":"+accInfo.getString("account")+","
				+ "\"amount\":"+amount+","
				+ "\"withdrawalType\":"+channelDetails.get("wdtype")+","
				+ "\"importantNotes\":\"testapiwithdrawal_bigAmount\","
				+ "\"isRememberInfo\":true,"
				+ "\"userPaymentInfoId\":\"\","
				+ "\"orderCurrency\":\""+channelDetails.get("wdActcurrency")+"\","
				+ "\"paymentMethodCode\":\""+channelDetails.get("wdCpsCode")+"\","
				+ "\"attachVariables\":\"{\\\"card_number\\\":\\\""+getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode"))+"\\\"}\","
				+ "\"mandatory\":\"address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip\","
				+ "\"currencyNumber\":\""+channelDetails.get("wdcurrency")+"\","
				+ "\"withdrawChannel\":"+channelDetails.get("wdchannel")+","
				+ "\"accountNumber\":\""+getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode"))+"\","
				+ "\"bankAccountName\":\"\","
				+ "\"raw\":\"{\\\"accountNumber\\\":\\\""+getCryptoWalletAddress(channelDetails.get("wdActcurrencyCode"))+"\\\"}\","
				+ "\"device\":\"web\""
				+ ",\"channelMerchantId\":" + channelDetails.get("channelMerchantId")

				+ "}]";

		return obj;
	}

	public static String mixedWithdrawalWDRequest(BigDecimal ccBalance, BigDecimal lbtBalance, BigDecimal otherBalance,
										    JSONObject accInfo, JSONObject ccDetails, List<Integer> categoriesList,
											Map<Integer, Map<String, String>> pcsChannelDetails)  {

		System.out.println("Categories List: "+categoriesList);
		JSONArray finalBody = new JSONArray();

		//check if user got cc balance and include if have balance
		if (ccBalance.compareTo(BigDecimal.ZERO) > 0) {
			System.out.println("Will include Credit Card in withdrawal submission");
			String ccBody = creditCardWDRequest(accInfo, ccDetails, ccBalance);
			finalBody.addAll(JSONArray.parseArray(ccBody));
		}

		//check if user got lbt balance and include if have balance
		if (lbtBalance.compareTo(BigDecimal.ZERO) > 0) {
			System.out.println("Will include Local Bank Transfer in withdrawal submission");
			//Map<String, String> lbtData = pcswdapi.apiWDPCSChannelInfoByCategory(acctNum, acctBalance, 5);
			Map<String, String> lbtData = pcsChannelDetails.get(5);
			String lbtBody = bankTranWDRequest(accInfo, lbtData, lbtBalance);
			finalBody.addAll(JSONArray.parseArray(lbtBody));
		}

		//check if user got other balance, if have balance withdraw as ewallet. if no ewallet available, then crypto
		if (otherBalance.compareTo(BigDecimal.ZERO) >0 ){
			System.out.println("Will include non-CC / LBT withdrawal type in submission");
			Map<String, String> otherWDMethodData;
			String wdOtherBody;

			if (pcsChannelDetails.containsKey(3)) {
				otherWDMethodData = pcsChannelDetails.get(3);
				wdOtherBody = eWalletWDRequest(accInfo, otherWDMethodData, otherBalance);
			} else {
				otherWDMethodData = pcsChannelDetails.get(4);
				wdOtherBody= cryptoWDRequest(accInfo, otherWDMethodData, otherBalance);

			}
			finalBody.addAll(JSONArray.parseArray(wdOtherBody));
		}
		GlobalMethods.printDebugInfo("Mixed Withdrawal Request Body: "+ finalBody.toJSONString());
		return finalBody.toJSONString();
	}

	public static String getCryptoWalletAddress(String cryptoCurency) {
		String cryptoAddress = "";
		switch(cryptoCurency){
			case "BTC":
				cryptoAddress = "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
				break;
			case "ETH":
				cryptoAddress = "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
				break;
			case "USDT-TRC20":
				cryptoAddress = "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
				break;
			case "USDT-ERC20":
				cryptoAddress = "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
				break;
			case "USDC-ERC20":
				cryptoAddress = "0x6dba6f6b122038854e299c3033757aa681ec2170";
				break;
			case "USDT-BEP20":
				cryptoAddress = "0x85fdb5595095403c4df0b6327b79c7f77d30cef9";
		}
		return cryptoAddress;
	}
	/* ------------------------------------ End of Withdrawal Payload ------------------------------------*/
}

