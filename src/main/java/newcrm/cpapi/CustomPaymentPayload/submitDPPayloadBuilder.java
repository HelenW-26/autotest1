package newcrm.cpapi.CustomPaymentPayload;


public class submitDPPayloadBuilder {

	/* ------------------------------------ Start of Deposit Payload ------------------------------------*/
	//中台提交入金参数
	public static String getPCSdepositBody(String paymentchannel){
		String obj ="";
		switch (paymentchannel) {
			//Japan E-money
			case "520":
				obj = "{\"payment_channel\":"+paymentchannel+",\"email\":\"test@test.com\",\"attachVariables\":\"{\\\"email\\\":\\\"test@test.com\\\"}\"}";
				break;
			//Default
			default:
				obj = "{\"payment_channel\":"+paymentchannel+"}";
				break;
		}
		return obj;
	}

	//LBT 入金 - 根据不同国家LBT渠道获取对应的请求参数
	public static String bankTransDPRequest(String account, String cpsCode){
		String obj ="";
		switch (cpsCode) {
			//Malaysia BT
			case "T00600_055":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiMsiaBT\",\"depositAmount\":\"233.75\",\"cpsCode\":\""+cpsCode+"\",\"rate\":4.25,\"orderCurrency\":\"458\",\"actualCurrency\":\"458\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"redemptionCode\":0,\"email\":\"\",\"raw\":\"{}\"}";
				break;
			//Malaysia BT Xpay
			case "T00600_062":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiMsiaBTXpay\",\"depositAmount\":\"233.75\",\"cpsCode\":\""+cpsCode+"\",\"rate\":4.25,\"orderCurrency\":\"458\",\"actualCurrency\":\"458\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"redemptionCode\":0,\"email\":\"\",\"raw\":\"{}\"}";
				break;
			//Malaysia FXpay
			case "T00600_060":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiMsiaFxPay\",\"depositAmount\":\"233.75\",\"cpsCode\":\""+cpsCode+"\",\"rate\":4.25,\"orderCurrency\":\"458\",\"actualCurrency\":\"458\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"redemptionCode\":0,\"email\":\"\",\"raw\":\"{}\"}";
				break;
			//Japan BT
			case "T00600_119":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":70,\"applicationNotes\":\"apiJapanBT\",\"depositAmount\":\"10500.00\",\"cpsCode\":\""+cpsCode+"\",\"rate\":150,\"orderCurrency\":\"392\",\"actualCurrency\":\"392\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"redemptionCode\":0,\"email\":\"\",\"raw\":\"{}\"}";
				break;
			//Japan E-Money
			case "T00600_167":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":70,\"applicationNotes\":\"apiJapanEmoney\",\"depositAmount\":\"10500.00\",\"cpsCode\":\""+cpsCode+"\",\"rate\":150,\"orderCurrency\":\"392\",\"actualCurrency\":\"392\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{\\\"email\\\":\\\"test@test.com\\\"}\",\"email\":\"\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{\\\"email\\\":\\\"test@test.com\\\"}\"}";
				break;
			//Vietnam BT
			case "T00600":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiVietnamT\",\"depositAmount\":\"1255430.00\",\"cpsCode\":\""+cpsCode+"\",\"rate\":22826,\"orderCurrency\":\"704\",\"actualCurrency\":\"704\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{\\\"email\\\":\\\"test@test.com\\\"}\",\"email\":\"\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{\\\"email\\\":\\\"test@test.com\\\"}\"}";
				break;
			//Vietnam BT
			case "T00312":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiVietnamT\",\"depositAmount\":\"1255430.00\",\"cpsCode\":\""+cpsCode+"\",\"rate\":22826,\"orderCurrency\":\"704\",\"actualCurrency\":\"704\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{\\\"email\\\":\\\"test@test.com\\\"}\",\"email\":\"\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{\\\"email\\\":\\\"test@test.com\\\"}\"}";
				break;
			//Brazil BT Pagsmile
			case "T00600_054":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"apiBrazilBT\",\"depositAmount\":\"277.75\",\"cpsCode\":\""+cpsCode+"\",\"rate\":5.05,\"orderCurrency\":\"986\",\"actualCurrency\":\"986\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"email\":\"\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{}\"}";
				break;
			//Banxa
			case "T00200_009":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":60,\"applicationNotes\":\"apiBanxa\",\"depositAmount\":\"60\",\"cpsCode\":\""+cpsCode+"\",\"rate\":1,\"orderCurrency\":\"840\",\"actualCurrency\":\"840\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip\",\"attachVariables\":\"{}\",\"email\":\"\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{}\"}";
				break;
		}
		return obj;
	}

	//Ewallet 入金 - 根据不同类型的ewallet渠道获取对应的请求参数
	public static String eWalletDPRequest(String account, String cpsCode){
		String obj ="";
		switch (cpsCode) {
			//Malaysia E-wallet
			case "T00100_046":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":55,\"applicationNotes\":\"non-pcs api MsiaEwallet\",\"depositAmount\":233.75,\"cpsCode\":\""+cpsCode+"\",\"rate\":4.25,\"orderCurrency\":\"458\",\"actualCurrency\":\"458\",\"mandatory\":\"transaction_type,payment_method,order_currency,order_amount,notify_url,return_url,order_id,timestamp,user_id,actual_currency\",\"attachVariables\":\"{}\",\"bankCode\":\"\",\"email\":\"\",\"redemptionCode\":null}";
				break;
			//Japan E-Wallet
			case "T00100_067":
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"non-pcs api JapanEwallet\",\"depositAmount\":8250,\"cpsCode\":\""+cpsCode+"\",\"rate\":150,\"orderCurrency\":\"392\",\"actualCurrency\":\"392\",\"mandatory\":\"transaction_type,payment_method,order_currency,order_amount,notify_url,return_url,order_id,timestamp,user_id,actual_currency\",\"attachVariables\":\"{}\",\"bankCode\":\"\",\"email\":\"\",\"redemptionCode\":null}";
				break;
			//Default
			default:
				obj = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"non-pcs apidep\",\"depositAmount\":50,\"cpsCode\":\""+cpsCode+"\",\"rate\":1,\"orderCurrency\":\"840\",\"actualCurrency\":\"840\",\"mandatory\":\"transaction_type,payment_method,order_currency,order_amount,notify_url,return_url,order_id,timestamp,user_id,actual_currency\",\"attachVariables\":\"{}\",\"bankCode\":\"\",\"email\":\"test@test.com\",\"redemptionCode\":null}";
				break;
		}
		return obj;
	}
	/* ------------------------------------ End of Deposit Payload ------------------------------------*/


}

