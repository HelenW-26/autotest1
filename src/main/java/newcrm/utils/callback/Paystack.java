package newcrm.utils.callback;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class Paystack extends CallbackBase
{
	private final String path = "paystack/receiveWebDepositResult";
	
	// Using SHA512 to encrypt the body
	protected String getEncrypted(String body) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		
		final String HMAC_SHA512 = "HmacSHA512";	    
		byte [] byteKey = key.getBytes("UTF-8");
		
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);        
        Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);
        sha512_HMAC.init(keySpec);
        
        byte [] mac_data = sha512_HMAC.doFinal(body.getBytes(StandardCharsets.UTF_8));       
        String result = DatatypeConverter.printHexBinary(mac_data);      
        return result.toLowerCase();
	}
	
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		this.jsonBodyHeader();
		
		String data = "{"
				+ "    \"amount\":\"" + amount + "\","
				+ "    \"status\":\"success\","
				+ "    \"currency\":\"" + currency + "\","
				+ "    \"reference\":\"" + orderNum + "\""
				+ "}";
		
		body = "{"
				+ "    \"event\":\"charge.success\","
				+ "    \"data\":" + data
				+ "}";

		try {			
			String sign = getEncrypted(body);		
			headerMap.put("x-paystack-signature", sign);
		} catch (Exception e) {
			System.out.println("exception: " + e);
		}
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub
		this.fullPath = url+path;
	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		
		if("VT".equalsIgnoreCase(brand)) {
			key = "sk_live_1f6a7ad29214e5fb7a93798354c4c0914a0b0ed1";//VT SVG
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "sk_live_3253b04147ec52dae3be81feb93ab60329b84b54";
		}else{
			key = null;
		}
	}
}
