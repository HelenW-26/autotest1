package newcrm.utils.api;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * 邮箱加密使用了该算法
 */
public class RsaUtil {

    public static void main(String[] args) {
        System.out.println(getAdminEmailEncrypt("test"));
    }

    private static final int MAX_ENCRYPT_BLOCK = 117;

    public static String getAdminEmailEncrypt(String string) {
        try {
            byte[] bytes  = encryptByPublicKey(string.getBytes());
            return "rsa."+byte2Hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
