package vantagecrm;

import javax.mail.*;
import javax.mail.internet.*;
import org.apache.http.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;

import newcrm.global.GlobalProperties;
import newcrm.utils.Bot.LarkNotifier;
import newcrm.utils.Bot.NotificationBot;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

public class SendMail {
	private static Boolean sendMailFlag = true;
	private static String chatId = "856771533";//Alex Liu

	@Test(priority = 0)
	public void checkBalance() throws ParseException, IOException, Exception {
		NotificationBot bot = new NotificationBot();
		LarkNotifier larkNT = new LarkNotifier();
		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, info;

		String mailSubject = "Summary - SendCloud Account Balance";
		String mailBody = "";
		String apiUser;
		String apiKey;


		BigDecimal actualBalance = new BigDecimal("0.00");
		BigDecimal threshBalance = new BigDecimal("0.00");
		String avaBalance;

		String[][] apiInfo = new String[12][4];
		
		apiInfo[0][0] ="VT PROD SENDCLOUD: API User is vt_markets_new";
		apiInfo[0][1] ="vt_markets_new";
		apiInfo[0][2] ="TvguAOBbxECc0d4a";
		apiInfo[0][3] ="100.00";
		 
		apiInfo[1][0] ="PUG old alpha SENDCLOUD(Monitoring.. No need to topup. API User is pup_mail_trigger)";
		apiInfo[1][1] ="pup_mail_trigger";
		apiInfo[1][2] ="TeSfPl9ezDPcwlKA";
		apiInfo[1][3] ="100.00";
		
		apiInfo[2][0] ="ALPHA SENDCLOUD(Monitoring.. No need to topup. API User is scuser_trigger)";
		apiInfo[2][1] ="scuser_trigger";
		apiInfo[2][2] ="mVuxIKskkUNAeEyl";
		apiInfo[2][3] ="100.00";
		
		apiInfo[3][0] = "VFX PROD SENDCLOUD: API User is VFX_VFSC";
		apiInfo[3][1] = "VFX_VFSC";
		apiInfo[3][2] = "8286895f508e56bbe14c745f4067fc04";
		apiInfo[3][3] = "40000.00";
		
		apiInfo[4][0] = "PUG PROD/ALPHA SENDCLOUD: API User is PU_Production";
		apiInfo[4][1] = "PU_Production";
		apiInfo[4][2] = "8c147c440c8836b0bf5c83ec026c025d";
		apiInfo[4][3] = "40000.00";		
		
		/*
		 * apiInfo[5][0] = "PUG old alpha SENDCLOUD: API User is cs_puprime";
		 * apiInfo[5][1] = "cs_puprime"; apiInfo[5][2] =
		 * "1e95290174f70da13975fa67c010775b"; apiInfo[5][3] = "20000.00";
		 */
		
		apiInfo[5][0] = "AT PROD SENDCLOUD: API User is AT_SVG";
		apiInfo[5][1] = "AT_SVG";
		apiInfo[5][2] = "1176619d9c68caac6992e8e52f9d084b";
		apiInfo[5][3] = "40000.00";
		
		apiInfo[6][0] = "Moneta PROD/ALPHA SENDCLOUD: API User is moneta_trigger";
		apiInfo[6][1] = "moneta_trigger";
		apiInfo[6][2] = "8fc58464d5fae0287e43766cf00a272f";
		apiInfo[6][3] = "40000.00";
		
		apiInfo[7][0] = "Infinox PROD SENDCLOUD: API User 1 is infinox_fca_live";
		apiInfo[7][1] = "infinox_fca_live";
		apiInfo[7][2] = "483976ff8e6ac233819ad2b441e5b0d6";
		apiInfo[7][3] = "40000.00";
		
		apiInfo[8][0] = "Infinox PROD SENDCLOUD: API User 2 is infinox_scb";
		apiInfo[8][1] = "infinox_scb";
		apiInfo[8][2] = "7a853c41cb59781011e1b9e26f5c7883";
		apiInfo[8][3] = "40000.00";
		
		apiInfo[9][0] = "UM PROD/ALPHA SENDCLOUD: API User is um_trigger";
		apiInfo[9][1] = "um_trigger";
		apiInfo[9][2] = "40b1486f838d616c02c92fdd1eada13a";
		apiInfo[9][3] = "40000.00";
	
		apiInfo[10][0] = "VJP PROD SENDCLOUD: API User is vantagejapan";
		apiInfo[10][1] = "vantagejapan"; 
		apiInfo[10][2] = "7f54f43a117ea0315b1e8374b6c3b674"; 
		apiInfo[10][3] = "40000.00";
		
		apiInfo[11][0] = "STAR PROD SENDCLOUD: API User is STAR_SVG_LIVE";
		apiInfo[11][1] = "STAR_SVG_LIVE"; 
		apiInfo[11][2] = "32eb5b1fd78767b223e8daea0e240c0b"; 
		apiInfo[11][3] = "20000.00";		 
		
		String endpointAddr = "";
		HashMap<String, String> map = new HashMap<String, String>();
		String result;

		for (int i = 0; i < apiInfo.length-9; i++) {
			mailBody = mailBody + "\r\n======" + apiInfo[i][0] + "\r\n";
			apiUser = apiInfo[i][1];
			apiKey = apiInfo[i][2];

			endpointAddr = "http://api.sendcloud.net/apiv2/userinfo/get?apiUser=VantageFX_USER&apiKey=VantageFX_KEY";
			endpointAddr = endpointAddr.replaceAll("VantageFX_USER", apiUser);
			endpointAddr = endpointAddr.replaceAll("VantageFX_KEY", apiKey);
			// System.out.println(endpointAddr);

			result = RestAPI.commonPostAPI(endpointAddr, map, "");

			if (result.length() > 0) {
				objAPIResult = (JSONObject) parser.parse(result);

				info = (JSONObject) objAPIResult.get("info");
				avaBalance = info.get("avaliableBalance").toString();

				System.out.println("avaliableBalance: " + avaBalance);
				mailBody = mailBody + "\r\nAvailable Balance: " + avaBalance + " \r\n";

				actualBalance = new BigDecimal(avaBalance);
				threshBalance = new BigDecimal(apiInfo[i][3]);

				if ( (actualBalance.compareTo(threshBalance) <= 0) && !apiInfo[i][0].contains("Monitoring") ) {
					System.out.println("Balance is lower than threshhold. Please topup account ASAP");
					mailSubject = "Warning! Topup SendCloud Account please";
					mailBody = mailBody + "\r\nBalance is lower than threshhold (" + apiInfo[i][3]
							+ "). Please topup account ASAP \r\n";

					//Sending notification to telegram group
					bot.sendMessageToUser(chatId, "❗️"+apiInfo[i][0]+"\r\n"+
					"Available Email Number: " + avaBalance + "\r\nPlease topup account ASAP\r\n");	

					//Sending notification to lark
					String aler_msg = "❗️"+apiInfo[i][0]+"\r\n"+
							"Available Email Number: " + avaBalance + "\r\nPlease topup account ASAP\r\n";
					larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_SendCloudBalanceAlarm_webhookUrl,aler_msg);
				} else {
					System.out.println("NO need to topup");
					mailBody = mailBody + "\r\nBalance: (" + apiInfo[i][3]
							+ "). NO need to topup\r\n";
				}

			} else {
				System.out.println("API doesn NOT return any result.");
			}

		}

		//For sendcloud global version
		for (int i = 3; i < apiInfo.length; i++) {
			mailBody = mailBody + "\r\n======" + apiInfo[i][0] + "\r\n";
			apiUser = apiInfo[i][1];
			apiKey = apiInfo[i][2];

			endpointAddr = "http://api2.sendcloud.net/api/userinfo/get?apiUser=VantageFX_USER&apiKey=VantageFX_KEY";
			endpointAddr = endpointAddr.replaceAll("VantageFX_USER", apiUser);
			endpointAddr = endpointAddr.replaceAll("VantageFX_KEY", apiKey);
			// System.out.println(endpointAddr);

			result = RestAPI.commonPostAPI(endpointAddr, map, "");

			if (result.length() > 0) {
				objAPIResult = (JSONObject) parser.parse(result);

				info = (JSONObject) objAPIResult.get("info");
				avaBalance = info.get("restNum").toString();

				System.out.println("Rest Email Number: " + avaBalance);
				mailBody = mailBody + "\r\nAvailable Email Number: " + avaBalance + " \r\n";

				actualBalance = new BigDecimal(avaBalance);
				threshBalance = new BigDecimal(apiInfo[i][3]);

				if (actualBalance.compareTo(threshBalance) <= 0) {
					System.out.println("Balance is lower than threshhold. Please topup account ASAP");
					mailSubject = "Warning! Topup SendCloud Account please";
					mailBody = mailBody + "\r\nLower than threshhold (" + apiInfo[i][3]
							+ "). Please topup account ASAP \r\n";

					//Sending notification to telegram group
					bot.sendMessageToUser(chatId, "❗️"+apiInfo[i][0]+"\r\n"+
					"Available Email Number: " + avaBalance + "\r\nPlease topup account ASAP\r\n");	
					
					//Sending notification to lark channel
					String alert_msg = "!!!"+apiInfo[i][0]+"\r\n"+
							"Available Email Number: " + avaBalance + "\r\nPlease topup account ASAP\r\n";
					larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_SendCloudBalanceAlarm_webhookUrl,alert_msg);

				} else {
					System.out.println("Balance is higher than threshhold balance. NO need to topup");
					mailBody = mailBody + "\r\nHigher than threshhold balance (" + apiInfo[i][3]
							+ "). NO need to topup\r\n";

				}

			} else {
				System.out.println("API doesn NOT return any result.");
			}

		}
		Calendar cal = Calendar.getInstance();
		// boolean monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
		boolean monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
		
	  if(sendMailFlag || monday)
	  {		 
		  sendEmail(mailSubject, mailBody);
		  
		  //Sending notification to teams channel
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  larkNT.SendNotificationToLark(GlobalProperties.AUTOMATION_SendCloud_Daily_webhookUrl,dateFormat.format(cal.getTime()) + "\r\n"
				+mailBody);
	  } 

	}
	
	// SendAPI
	public void getSCBalance(String apiUser, String apiKey) throws ParseException, IOException, Exception {
		String endpointAddr = "http://api.sendcloud.net/apiv2/userinfo/get?apiUser=VantageFX_USER&apiKey=VantageFX_KEY";
		HashMap<String, String> map = new HashMap<String, String>();
		String result;

		apiUser = "VantageFX_TEST";
		apiKey = "eSgr8QJ1KsFci5Ac";

		endpointAddr = endpointAddr.replaceAll("VantageFX_USER", apiUser);
		endpointAddr = endpointAddr.replaceAll("VantageFX_KEY", apiUser);

		System.out.println(endpointAddr);

		result = RestAPI.commonPostAPI(endpointAddr, map, "");

		System.out.println(result);

		// System.out.println(result);

	}

	public static void sendEmail(String subText, String bodyText) {
		// TODO Auto-generated method stub

		//String host = "smtp.163.com";
		String host = "smtp.office365.com";
		//final String user = "van_qa_automation@163.com";// change accordingly
		final String user = "crmqa.automation@hytechc.com";
		//final String password = "EBUHHDQCOUGIKRYS";// change accordingly
		final String password = "4Rrft65!@#$";
		
		//String to = "shan.liu@hytechc.com";
		String to = "alex.wu@hytechc.com";// change accordingly
		
		String cc = "shan.liu@hytechc.com;pm.team@hytechc.com";
		//String cc = ";";
		String[] ccSplit = cc.split(";");

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.trust", "*");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			for (int i = 0; i < ccSplit.length; i++) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccSplit[i]));
			}

			message.setSubject(subText);
			message.setText(bodyText);

			// send the message
			Transport.send(message);
			System.out.println("message sent successfully...");

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
