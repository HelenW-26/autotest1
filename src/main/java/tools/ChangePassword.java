package tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ChangePassword {

    public static void main(String args[]) {
        //please provide the following values
        String brand= "UM";
        String email = "testcrmcopytradingpayment@mailinator.com";
        String newPwd="123Qwe@@";

        //no need to change code below
        String env = "ALPHA";
        String regulator="VFSC";
        String md5NewPwd=null;

        try {
            md5NewPwd = md5(newPwd);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("md5 exception");
        }
        String newSalt = getSalt(email,env,brand,regulator);

        String sha512Password = DigestUtils.sha512Hex(newSalt + md5NewPwd + email);

        updatePwd(email,sha512Password,env,brand,regulator);

    }

    public static void updatePass(String newPwd,String email,String env,String brand,String regulator){
        String md5NewPwd=null;

        try {
            md5NewPwd = md5(newPwd);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("md5 exception");
        }
        String newSalt = getSalt(email,env,brand,regulator);

        String sha512Password = DigestUtils.sha512Hex(newSalt + md5NewPwd + email);

        updatePwd(email,sha512Password,env,brand,regulator);
    }

    public static String md5(String data) throws Exception {
        String strEncrpy = null;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md5.digest(data.getBytes());
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        strEncrpy = hexValue.toString();
        return strEncrpy;
    }

    public static String getSalt(String email,String env,String brand,String regulator)
    {
        UsersDB db = new UsersDB();
        JSONArray jsArray = db.getSalt(email, GlobalProperties.ENV.valueOf(env), GlobalProperties.BRAND.valueOf(brand),GlobalProperties.REGULATOR.valueOf(regulator));
        assertNotNull(jsArray);
        assertTrue(jsArray.size() >0,"Do not find the user by email: " + email);

        JSONObject obj = jsArray.getJSONObject(0);
        String salt = obj.getString("email_salt");
        return salt;
    }

    public static void updatePwd(String email,String emailpwd,String env,String brand,String regulator)
    {
        UsersDB db = new UsersDB();
        db.updatePassword(email, emailpwd,GlobalProperties.ENV.valueOf(env), GlobalProperties.BRAND.valueOf(brand),GlobalProperties.REGULATOR.valueOf(regulator));

    }
}
