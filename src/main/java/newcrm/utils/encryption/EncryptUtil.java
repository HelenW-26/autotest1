package newcrm.utils.encryption;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import newcrm.utils.StringUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * copy from excalibur/common-utils/src/main/java/com/ty/aes.
 */
public class EncryptUtil {

	/**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final String AESTYPE ="AES/ECB/PKCS5Padding";

    private static final String algorithm = "RSA";

    /**
     * 加密
     * @param keyStr 加密使用的key
     * @param plainText  要加密的字符串
     * @return
     */
    public static String AES_Encrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(encrypt));
    }

    /**
     * 解密
     * @param keyStr  解密使用的key
     * @param encryptData 加密后的字符串
     * @return
     */
    public static String AES_Decrypt(String keyStr, String encryptData) throws Exception {
        byte[] decrypt = null;
        Key key = generateKey(keyStr);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        return new String(decrypt).trim();
    }
    
    public static String aesDecrypt(String encryptedText, String aesKey) throws Exception {
        String text = "";
        if (!StringUtil.isBlank(encryptedText)) {
            URLCodec urlCodec = new URLCodec();
            String aestext = urlCodec.decode(encryptedText);
            text = EncryptUtil.AES_Decrypt(aesKey, aestext);
        }
        return text;
    }

    private static Key generateKey(String key)throws Exception{
        try{
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    public static void main(String[] args) throws Exception {

        /*String keyStr = "da4756119d5e7e0f";

        String plainText = "{\"email\":\"sega32dwer2@foo.com\",\"affid\":\"123\",\"firstName\":\"Bob\",\"lastName\":\"dasdfas\",\"country\":\"A\",\"mobile\":\"67867867\",\"source\":\"App\",\"createDemo\":false}";
        String encText = AES_Encrypt(keyStr, plainText);
        System.out.println(plainText);
        System.out.println("aes encrypted: ");
        System.out.println(encText);
        
        URLCodec urlCodec = new URLCodec();
        String aestext = urlCodec.encode(urlCodec.encode(encText));
        System.out.println("URL Encoded: ");
        System.out.println(aestext);
        
//        String plainTextUrlEncoded = urlCodec.encode(plainText);
//        System.out.println(plainTextUrlEncoded);
        
        String urlDecoded = urlCodec.decode(urlCodec.decode(aestext, "UTF-8"), "UTF-8");
        System.out.println("\n\nURL Decoded:");
        System.out.println(urlDecoded);
        System.out.println(urlDecoded.equals(encText));
        String decString = AES_Decrypt(keyStr, encText);
        System.out.println("aes decrypted: ");
        System.out.println(decString);
        
        System.out.println(decString.equals(plainText));

        System.out.println(mtPassUpdateHelper(keyStr, Arrays.asList(
                "mt4_pass",
                "mt4_pass"
        )));*/
    	/*
    	String result = "IXzs+PMpvxI0WRjnJrUQJwow31RHsWdOElBk0OWunsmxKkOkfExxHzCtQnaVqFg3TxQeNJTeFOGqFZApjFwR8sYxPZrRikmSkAibrCPsMMnmRFSMOO4ln9SSO5dLvWnNT/6SmFREZGWuQMI/I2cydGnWZSqBJEyoVioofo6KkxvxBngKdFNiT63ECmndWZdtCxM52mtieoMC5ia+HAPUACtUHsjfMRIdDxeHrK5vTBWpEeh2X5RxAOthAEszdMT7y8ir5+Q833hFZzHaqKvK9z7C+iSI1r5VY6LDix/j9PPvAYbzMTU0BC6jEDk1YhZYbdApLwnKIlPy+B8jd+tRmMgIVLwWQLDbktUlTBvkxyIzdoeR4t+Jx1vjYaiV3Mr8R9HDYSoy046V4wEanU0GoxVRZctrBLgXvchLwhBDD5mmBJll663DsiCbYPRXmJzbuOwuhNWLon3drtbmsoZe0iusKsRKkGTZl7ca14ZIlSDwsLOJ4yX0hu91b1n5o8cYXgq8lWf5dmaUCdChzgiUwk7aQxlzgStWzTbqn7faCfXDCnsb+LaPm096wcCWmyz/n2LgRpW8XRW4HJJvaAMacAyIpNnLFh9zZAUSXF2z7ItAaDfo2W/MxAaq0BU5BytM0B+kH22TQnH/vfV4dXjcBkBHapVhtKBUKRZjD7KSRh0sJnEBjquNLWE5Vsy4zhMs";
    	System.out.println(AES_Decrypt("VTAPP2XS0AE1QTK8",result));
    	
    	String info = "channelName=CPS-P2P-BearEx&channelTxnAmt=777.00&insCd=&merCd=6300451100033253&merOrderNo=PUSVG10000031920220705091452&remarks=&txnAmt=777.00&txnCd=T00600&txnCurry=usdterc&txnDate=20220802&txnMsg=&txnOrderDesc=123456-PUSVG10000031920220705091452&txnOrderNo=1003838846435065856&txnSta=0000&txnSubmitTime=20220801203710&txnTime=014118&versionNo=V01";
    	String key = "c5e47e08ff0753ba1fc4287922ec4d3fdcdef369";
    	System.out.println(hmacSHA1(key,info));*/
    	System.out.println(getAdminEmailEncrypt("testtestabc@test.com"));
    	
    }

    private static String mtPassUpdateHelper(String keyStr, List<String> plainTextPasswords) {
        StringBuilder sb = new StringBuilder();
        for (String password : plainTextPasswords) {
            sb.append("update tb_data_source set mt4_pass = '").append(AES_Encrypt(keyStr, password)).append("' where mt4_pass = '").append(password).append("';\n");
        }
        return sb.toString();
    }
    /***
     * add function for callback, type: SHA-512,SHA-256
     * @param plainText
     * @param type: SHA-512,SHA-256
     * @return
     * @throws Exception
     */
    public static String shaEnc(String plainText,String type) {
    	MessageDigest digest;
		try {
			if("SHA-512".equalsIgnoreCase(type)) {
				digest = java.security.MessageDigest.getInstance("SHA-512");
				
			}else if("SHA-256".equalsIgnoreCase(type)) {
				digest = java.security.MessageDigest.getInstance("SHA-256");
			}else {
				System.out.println("Sorry, we do not process this type: " + type);
				return null;
			}
			digest.update(plainText.getBytes(StandardCharsets.UTF_8));
	        int outputStringLength = digest.getDigestLength() * 2;
	        String output = new BigInteger(1, digest.digest()).toString(16);
	        // Adding leading zeroes because the signature must have the length exactly 128
	        while (output.length() < outputStringLength) {
	            output = "0".concat(output);
	        }
	        return output;
		} catch (NoSuchAlgorithmException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        
    }
    
    /**
     * 
     * @param plaintext
     * @param key
     * @return
     */
    public static String hmacSHA256(String plaintext,String key) {
    	return HmacUtils.hmacSha256Hex(key, plaintext);
    }
    
    public static String MD5(String plaintext) {
    	return DigestUtils.md5Hex(plaintext);
    }

    /**
     * MD5加密
     *
     * @author <a href="mailto:sooner.wang@tianyitechs.com">sooner.wang</a>
     * @param origin 加密字符串
     * @param charsetname 编码方式
     * @return java.lang.String
     * @since wangning 2017-03-14 17:29:10
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
            "f"};
    
    private static String getFormattedText(byte[] bytes) {
    	char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    
    public static String SHA1encode(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String hmacSHA1(String key,String plaintext) {
    	return HmacUtils.hmacSha1Hex(key, plaintext);
    }

    public static String encryptRsa(String info, String pk) throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(pk)));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(info.getBytes(StandardCharsets.UTF_8)));
    }
    
    private static byte[] encryptByPublicKey(byte[] data)
            throws Exception {
    	String key = "b3764d7f25497d340ae81953af7a898e1c1ea320489b17868906c537b33b6446f24ce902227db22540bfd80da256b4fc53b385ea7a4233a4e1e02d0be19aac8fac1e23830de13968bb089f767533716c03032172f8e32e2f105e22ff12ecc0f0ffae68b70f70fa6994dafd56e011178b339791b762413a29d1960b4fbe133c50bc8c3bf11bd985309440d1b9e3770dcc291f91e5388fe95dee06b2de9ca0ad35c0b89ba9e2ac282276bb09f477f3d0ffc5384f245b900b4d17eb3d16129d0666f02668459ea987531ab8b00c184ffd198d74114ccf34691ba63152812c3f4bbd415fef1770f9b4a4a178ae89eed96fd99c73ff750356a5905ae268a8cdff0657";
        BigInteger bigInt = new BigInteger(key,16);
        BigInteger expont = new BigInteger("10001",16);
        RSAPublicKeySpec keyspec = new RSAPublicKeySpec(bigInt,expont);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicK = (RSAPublicKey) keyFactory.generatePublic(keyspec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    
    private static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) hs = hs + "";
        }
        return hs;
    }
    
    public static String getAdminEmailEncrypt(String plainEmail) {
    	try {
			byte[] bytes  = encryptByPublicKey(plainEmail.getBytes());
			return "rsa."+byte2Hex(bytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
}
