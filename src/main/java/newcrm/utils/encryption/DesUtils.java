package newcrm.utils.encryption;

import newcrm.global.GlobalProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
/**
 * 
 * @author copy from dev code repo
 *
 */
public class DesUtils {


    /**
     * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
     * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
     * jce.jar
     * security/US_export_policy.jar
     * security/local_policy.jar
     * ext/sunjce_provider.jar
     */

    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    
    private static final String key = "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE";//for email and phone

    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
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
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
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
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) hs = hs + ":";
        }
        return hs.toUpperCase();
    }

    public static byte[] hex(String key) {
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }

    // 加密
    public static String encode(String src, String key) {
        byte[] enk = hex(key);
        byte[] srcArrEnc = encryptMode(enk, src.getBytes());
//        String srcResult = Base64.encode(srcArrEnc);
        String srcResult = new Base64().encodeToString(srcArrEnc);
        return srcResult;
    }
    
 // 加密
    public static String encode(String src) {
        String umKey = key;
        if(GlobalProperties.brand.equalsIgnoreCase("um"))
        {
            umKey="LaAId932askfasLKDSAI302lapdSI83SKDsdfUSu299sksdSU2IsIdF284DISe6A";
        }
        byte[] enk = hex(umKey);
        byte[] srcArrEnc = encryptMode(enk, src.getBytes());
//        String srcResult = Base64.encode(srcArrEnc);
        String srcResult = new Base64().encodeToString(srcArrEnc);
        return srcResult;
    }

    public static String encodeWithBrand(String src,String brand) {
        //key: apollo, sys-security, field.encrypt.key
        String umKey = key;
        if(brand.equalsIgnoreCase("um"))
        {
            umKey="LaAId932askfasLKDSAI302lapdSI83SKDsdfUSu299sksdSU2IsIdF284DISe6A";
        }
        byte[] enk = hex(umKey);
        byte[] srcArrEnc = encryptMode(enk, src.getBytes());
//        String srcResult = Base64.encode(srcArrEnc);
        String srcResult = new Base64().encodeToString(srcArrEnc);
        return srcResult;
    }

    /**
     * 加密加入编码参数
     *
     * @param src
     * @param key
     * @param encode 编码参数
     * @return
     */
    public static String encode(String src, String key, String encode) {
        byte[] enk = hex(key);
        byte[] srcArrEnc = new byte[0];
        try {
            srcArrEnc = encryptMode(enk, src.getBytes(encode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String srcResult = (new Base64()).encodeToString(srcArrEnc);
        return srcResult;
    }

    // 解密
    public static String decode(String src, String key) {
        byte[] enk = hex(key);
//        byte[] srcDecodeArr = Base64.decode(src);
//        byte[] srcDecodeArr = new BASE64Decoder().decodeBuffer(src);
        byte[] srcDecodeArr = new Base64().decode(src);
        byte[] srcDecodeDec = decryptMode(enk, srcDecodeArr);
        String strResult = new String(srcDecodeDec);
        return strResult;
    }
    
    // 解密
    public static String decode(String src) {
        byte[] enk = hex(key);
        byte[] srcDecodeArr = new Base64().decode(src);
        byte[] srcDecodeDec = decryptMode(enk, srcDecodeArr);
        String strResult = new String(srcDecodeDec);
        return strResult;
    }

    public static void main(String[] args) throws IOException {

        
        String email = "autotestvxmbildl@testcrmautomation.com";
        String encoded = encode(email.toLowerCase(), "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
        System.out.println("encoded email: " + encoded);

        email = "xbCBiMFwLOmG2jDGD3bZBW42+gtca9d1";
        String decoded = decode(email, "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
        System.out.println("decoded email:" + decoded);

        String phone = "45366248";
        encoded = encode(phone, "IZ0Vqouwik1ufyE5y92CwvTdeKnGeLZqyXOp2RfaXWNzYh3RhgZS68OlwGx0TDwE");
        System.out.println("encoded phone: " + encoded);


    }
}
