package vantagecrm;

import java.io.IOException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import newcrm.global.GlobalProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.sun.crypto.provider.SunJCE;

/*字符串 DESede(3DES) 加密
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的
 * 密钥，P代表明文，C代表密表，这样，
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
 * */
public class DecryptUtils {

    /**
     * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
     * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
     *jce.jar
     *security/US_export_policy.jar
     *security/local_policy.jar
     *ext/sunjce_provider.jar
     */

    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte,byte[] src){
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//在单一方面的加密或解密
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte,byte[] src){
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2Hex(byte[] b){
        String hs="";
        String stmp="";
        for(int n=0; n<b.length; n++){
            stmp = (Integer.toHexString(b[n]& 0XFF));
            if(stmp.length()==1){
                hs = hs + "0" + stmp;
            }else{
                hs = hs + stmp;
            }
            if(n<b.length-1)hs=hs+":";
        }
        return hs.toUpperCase();
    }
    public static byte[] hex(String key){
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i=0;i<24;i++){
            enk[i] = bkeys[i];
        }
        return enk;
    }

    // 加密
    public static String encode(String src) {
        String key = "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE";
    	byte[] enk = hex(key);
 
        byte[] srcArrEnc = encryptMode(enk,src.getBytes());

        String srcResult = new Base64().encodeToString(srcArrEnc);
        return srcResult;
    }

    public static String encode(String src, GlobalProperties.BRAND brand) {
        String key;

        if (brand == GlobalProperties.BRAND.UM) {
            key = "LaAId932askfasLKDSAI302lapdSI83SKDsdfUSu299sksdSU2IsIdF284DISe6A";
        } else {
            key = "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE";
        }

        byte[] enk = hex(key);

        byte[] srcArrEnc = encryptMode(enk,src.getBytes());

        String srcResult = new Base64().encodeToString(srcArrEnc);
        return srcResult;
    }

    /**
     * 加密加入编码参数
     * @param src
     * @param key
     * @param encode  编码参数
     * @return
     */
    public static String encode(String src,String key,String encode)
    {
        byte[] enk = hex(key);
        Security.addProvider(new SunJCE());
        byte[] srcArrEnc=new byte[0];
        try{
            srcArrEnc = encryptMode(enk, src.getBytes(encode));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        String srcResult = (new Base64()).encodeToString(srcArrEnc);
        return srcResult;
    }
    // 解密
    public static String decode(String src, String key) throws IOException {
        byte[] enk = hex(key);
        Security.addProvider(new com.sun.crypto.provider.SunJCE());

        byte[] srcDecodeArr = new Base64().decode(src);
        byte[] srcDecodeDec = decryptMode(enk, srcDecodeArr);
        String strResult = new String(srcDecodeDec);
        return strResult;
    }

    
    public static void main(String[] args) throws Exception {
    	String email = "ibtestvix@test.com";
    	
    	//String encoded = encode(email.toLowerCase(), "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
    	String encoded = encode(email.toLowerCase());
    	System.out.println("encoded email: " + encoded);

    	//email = "yIIWVNR1jMMkI/BKOSEowl/FbLaXR1jx";
    	email="ixF7M5Zze9Hrx90Y+R3QJw==";
    	String decoded = decode(email, "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
    	System.out.println("decoded email:" + decoded);
 
 /*   	String phone = "45366248";
    	//encoded = encode(phone, "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
    	encoded = encode(phone);
    	System.out.println("encoded phone: " + encoded);*/
    	
    	/*Utils.readEmail(email, "db_cima_vgp", "test", 10);*/
    }
}