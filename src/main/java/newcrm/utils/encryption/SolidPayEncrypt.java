package newcrm.utils.encryption;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.io.Charsets;


public class SolidPayEncrypt {
	public static String decrypt(String ivFromHttpHeader, String authTagFromHttpHeader, byte[] httpBody, String keyFromConfiguration) throws Exception{
		Security.addProvider(new BouncyCastleProvider());

		// Convert data to process
		byte[] key = DatatypeConverter.parseHexBinary(keyFromConfiguration);
		byte[] iv = DatatypeConverter.parseHexBinary(ivFromHttpHeader);
		byte[] authTag = authTagFromHttpHeader.getBytes();
		//byte[] encryptedText = DatatypeConverter.parseHexBinary(httpBody);

		// Unlike other programming language, We have to append auth tag at the end of
		// encrypted text in Java
		byte[] cipherText = ArrayUtils.addAll(httpBody, authTag);

		// Prepare decryption
		SecretKeySpec keySpec = new SecretKeySpec(key, 0, 32, "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(128, iv));

		// Decrypt
		byte[] bytes = cipher.doFinal(cipherText);
		return new String(bytes, Charsets.UTF_8);
	}
	
	public static String encrypt(String keyString, String ivStr, String messageStr) throws Exception {
			Cipher aeadCipher = Cipher.getInstance("AES/GCM/NoPadding");
		   byte[] key = DatatypeConverter.parseHexBinary(keyString);
		   StringBuilder sb = new StringBuilder();
		    for (byte b : key) {
		        sb.append(String.format("%02X ", b));
		    }
		    
		    //System.out.println("key in encrypt: " + sb.toString());
		    
		   byte[] iv = DatatypeConverter.parseHexBinary(ivStr);
		   StringBuilder sb2 = new StringBuilder();
		    for (byte b : iv) {
		        sb2.append(String.format("%02X ", b));
		    }
		     
		   SecretKeySpec keySpec = new SecretKeySpec(key, 0, 32, "AES");		   
		   aeadCipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(128, iv));
		   aeadCipher.updateAAD("".getBytes());
		   byte []  result = aeadCipher.doFinal(messageStr.getBytes());
		   StringBuilder sb3 = new StringBuilder();
		    for (byte b : result) {
		        sb3.append(String.format("%02X", b));
		    }
		    //System.out.println(sb3.toString());
		    return sb3.toString();
	}
	
	
	public static byte[] encrypt(String keyString, String ivStr, String messageStr, String associatedData) throws Exception {
		   Cipher aeadCipher = Cipher.getInstance("AES/GCM/NoPadding");
		   byte[] key = DatatypeConverter.parseHexBinary(keyString);
		   StringBuilder sb = new StringBuilder();
		    for (byte b : key) {
		        sb.append(String.format("%02X ", b));
		    }
		   
		   System.out.println("key in enc: " + sb.toString());
		   byte[] iv = DatatypeConverter.parseHexBinary(ivStr);
		   StringBuilder sb2 = new StringBuilder();
		    for (byte b : iv) {
		        sb2.append(String.format("%02X ", b));
		    }
		    System.out.println("iv in enc: " + sb2.toString());
		   byte[] tag = DatatypeConverter.parseHexBinary(associatedData);
		   //byte[] message = DatatypeConverter.parseHexBinary(messageStr);
		   
		   SecretKeySpec keySpec = new SecretKeySpec(key, 0, 32, "AES");
		   System.out.println("sec key: " + keySpec.toString());
		   
		   aeadCipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(128, iv));
		   aeadCipher.updateAAD(associatedData.getBytes());
		   return aeadCipher.doFinal(messageStr.getBytes());
		}
	public static void main(String args[]) {
		String message = "{\"type\":\"PAYMENT\",\"payload\":{\"id\":\"8ac9a4a87d2e6856017d97080d1e1e8c\",\"paymentType\":\"DB\",\"paymentBrand\":\"VISA\",\"presentationAmount\":\"666.00\",\"presentationCurrency\":\"USD\",\"merchantTransactionId\":\"VT81001425120211209062501\",\"result\":{\"code\":\"000.200.000\",\"description\":\"transaction pending\",\"randomField1254626153\":\"Please allow for new unexpected fields to be added\"},\"resultDetails\":{\"clearingInstituteName\":\"Securetrading_Omnipay\"},\"card\":{\"bin\":\"456188\",\"last4Digits\":\"2614\",\"holder\":\"BISCUEIL FABIEN\",\"expiryMonth\":\"01\",\"expiryYear\":\"2023\"},\"customer\":{\"givenName\":\"FABIEN JEAN ANDRE\",\"surname\":\"BISCUEIL\",\"mobile\":\"+330686469729\",\"email\":\"biscueil.mp@gmail.com\",\"ip\":\"176.165.125.226\"},\"billing\":{\"street1\":\"78bis Rue des merlettes\",\"city\":\"Le vesinet\",\"state\":\"DEFAULT\",\"postcode\":\"78110\",\"country\":\"FR\"},\"threeDSecure\":{\"eci\":\"06\"},\"authentication\":{\"entityId\":\"8ac9a4ca778b485d01778ccb5f0a2635\"},\"customParameters\":{\"CTPE_DESCRIPTOR_TEMPLATE\":\"vtmarkets.com +611300945517\",\"CORRECTED_BRAND\":\"VISA\"},\"redirect\":{\"url\":\"https://sg-3ds-vdm.wlp-acs.com/acs-pa-service/pa/paRequest\",\"parameters\":[{\"name\":\"PaReq\",\"value\":\"eJxUUu9v2jAQ/Veifp2CfyYYdLUURkWRQhdYtVb7ZpxridYEcJKq3V8/O7Sw+tO9e6fnu3cH9zuH\\nOP+JtneoYYVta54xqsrrq3K7FSlubWySVMSSq22sJuIpVklaJk/cUCbLKw1FtsGjhld0bbVvNBvR\\nEQfyCb2iszvTdBqMPc6Wd1qKsVIUyAeEGt1yrimljMpJSqlQSiRATmloTI36V3Z3ny1uokX+Y5bl\\nUbFZrm6iPC+ADDTYfd907l0rngL5BNC7F73rusOUkNeuNu4Pdu3I7msggQFy6azoQ9R6pbeq1Pz3\\nt4d8fiw2R1zPysf130cl7HMW3jWQUAGl6VBzyhnjdBxxPhViKhWQIQ+mDi1o5mca0TDpCcMhfJNd\\nyMD9nwO/A4eNfdeTsRc7I8C3w75BX+GNPcdALm1/vw322s47xlIxpolUXLEkiU8oSbmSUgbPh5og\\nWHmLuKBsUAwASFAhH+v07gyX4KMvF/IPAAD//wXAgQAAAAAAkP9rAIyysMM=\"},{\"name\":\"MD\",\"value\":\"8ac9a4a87d2e6856017d97080d601e9c\"},{\"name\":\"TermUrl\",\"value\":\"https://ppipe.net/connectors/asyncresponse;jsessionid=2D1167822485F15270949FAA62DFDD3A.prod02-vm-con08?asyncsource=THREEDSECURE&ndcid=8ac9a4ca778b485d01778ccb5f0a2635_d7e7e984651943989e4aed7fa9765adf\"}]},\"risk\":{\"score\":\"0\"},\"timestamp\":\"2021-12-07 22:33:47+0000\",\"ndc\":\"8ac9a4ca778b485d01778ccb5f0a2635_d7e7e984651943989e4aed7fa9765adf\",\"merchantAccountId\":\"8ac9a4cd783575cd01783b74ee354e37\",\"channelName\":\"Vtmarkets.com-ecp-ST-3d\",\"source\":\"OPP\"}}"
				+ "";
		String keyStr = "4924795328D7BF2E4F6D5524082420EA6DADCBE2A4B3D63C48B241F7288F8AB1";
		String iv = "253d3fb468a0e24677c28a624be0f93903";
		String tagStr ="";
		String sec;
		String dec = "";
		message.getBytes();
		//String keyStr = "da4756119d5e7e0f";
		byte[] key = keyStr.getBytes();
		StringBuilder sb = new StringBuilder();
	    for (byte b : key) {
	        sb.append(String.format("%02X ", b));
	    }
		System.out.println("key bytes: " + sb.toString());
		
		try {
			sec = encrypt(keyStr,iv,message);
			System.out.println("sec: " +sec);
			
			//byte[] code = sec.getBytes();
			//dec = decrypt(iv,tagStr,code,keyStr);
			//System.out.println("dec: " + dec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
