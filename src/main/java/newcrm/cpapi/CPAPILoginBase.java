package newcrm.cpapi;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import newcrm.utils.api.RsaUtil;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CPAPILoginBase {
    /**
     * 使用指定用户登录CP端
     */
    @Step("loginWithEmail2")
    public static void loginWithEmail2(Map<String,Object> testData){

        Map<String,String> map = loginWithEmail(testData);
        String res = HttpRequest.post(testData.get("url") +HyTechUrl.cpLogin)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .body(HyTechUtils.mapToString(map))
                .timeout(20000)
                .execute().body();

        //System.out.println("Login back paramar："+ res);
        JSONObject result = JSONObject.parseObject(res);
        Integer res_code = result.getInteger("code");
        Assert.assertTrue(res_code==0 || res_code==521,"CP Login failed!\n"+result);
        if(res_code != 521) {
            testData.put("token",result.getJSONObject("data").getString("accessToken"));
            testData.put("userId",result.getJSONObject("data").getString("userId"));
        }
    }
    /**
     * 使用指定用户邮箱登录CP端
     */
    public static Map<String, String> loginWithEmail(Map<String,Object> testData) {
        Map<String, String> map = new HashMap();
        map.put("email", RsaUtil.getAdminEmailEncrypt(testData.get("email").toString()));
        map.put("password_login",testData.get("password").toString());
        map.put("utc", "39600000");
        map.put("loginMode", "EMAILPASSWORD");
        return map;
    }
    //@Test(description = "logout",dataProvider = "testData")
    public static void logout(Map<String,Object> testData){
        Map<String,String> map  = new HashMap<>();
        String userId = testData.get("userId").toString();
        map.put("userId",userId);
        String req = HyTechUtils.mapToString(map);
        String response = HttpRequest.get(testData.get("url").toString()+ HyTechUrl.logout + "?userId=" + userId)
                .header("accessToken",testData.get("token").toString())
                .body(req)
                .timeout(20000)
                .execute().body();
        System.out.println("Logout back paramar" + response);
    }
}
