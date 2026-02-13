package newcrm.cpapi;


import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.DateUtils;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import newcrm.utils.api.HytechDBUrl;
import newcrm.utils.db.HyTechDB;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static newcrm.utils.api.HyTechUrl.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class CPAPIAccount extends CPAPIBase{

    public CPAPIAccount(String url, String cplogin, String password) {
        super(url, cplogin, password);
    }

    public CPAPIAccount(String url, WebDriver driver) {
        super(url, driver);
    }

    public void id3Passed(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(id3PassedUrl);
        printAPICPInfo(testData.get("url").toString()+id3PassedUrl,"",result.toString());
        assertEquals(result.get("code"), 0, id3PassedUrl + " failed!! \n" + result);
    }

    public void get_demo_accs(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(demoAccsUrl);
        printAPICPInfo(testData.get("url").toString()+demoAccsUrl,"",result.toString());
        assertEquals(result.get("code"), 0, demoAccsUrl + " failed!! \n" + result);
    }

    //Duplicated? There's another getAllLastProof below
//    public void getAlllastproof(Map<String, Object> testData) throws Exception {
//        JSONObject result = sendCPAPIGETrequest(getAllLastProofURL);
//        printAPICPInfo(testData.get("url").toString()+getAllLastProofURL,"",result.toString());
//        printSuccessAPIInfo(testData.get("brand").toString(), testData.get("url").toString(), getAllLastProofURL,result.toString());
//        assertEquals(result.get("code"), 0, getAllLastProofURL + " failed!! \n" + result);
//    }

    public void banner_enabled(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(bannerEnabled);
        printAPICPInfo(testData.get("url").toString()+bannerEnabled,"",result.toString());
        //assertEquals(result.get("code"), 0, bannerEnabled + " failed!! \n" + result);
    }

    ///api/token/anti-reuse
    public void tokenAntiReuse(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(tokenantireuseURL);
        printAPICPInfo(testData.get("url").toString()+tokenantireuseURL,"",result.toString());
        //assertEquals(result.get("code"), 0, bannerEnabled + " failed!! \n" + result);
    }

    public void tg_bot(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getBotIdUrl);
        printAPICPInfo(testData.get("url").toString()+getBotIdUrl,"",result.toString());
        assertEquals(result.get("code"), 0, getBotIdUrl + " failed!! \n" + result);
    }


    public void queryMetaTraderAccountDetail(Map<String,Object> testData) throws Exception {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        String body = "{\"hiddenMtAccountIdList\":[],\"unHide\":false,\"platform\":null}";
        JSONObject result = sendCPAPIrequestArray(queryMetaTraderAccountDetailURL, body);
        printAPICPInfo(testData.get("url").toString()+queryMetaTraderAccountDetailURL,body,result.toString());
        //assertEquals(result.get("account"), "20772", queryMetaTraderAccountDetailURL + " failed!! \n" + result);
    }

    public JSONObject queryMetaTraderAccountDetail() throws Exception {
        Map<String, String> body = new HashMap<>();

        JSONObject result = postAPICall(queryMetaTraderAccountDetailURL, body);
        validateAPIResponse200(queryMetaTraderAccountDetailURL, result);

        return result;
    }

    public JSONObject queryDemoAccs() throws Exception {
        JSONObject result = getAPICall(getDemoAccs);
        validateAPIResponse200(getDemoAccs, result);

        return result;
    }

    public void queryLeverageStatuses(Map<String,Object> testData) throws Exception {
        String body = "{\"user_id\":-1}";
        //todo: replace -1 with actual user_id
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryLeverageStatusesURL, body);
        printAPICPInfo(testData.get("url").toString()+queryLeverageStatusesURL,body,result.toString());
        //assertEquals(result.get("code"), 0, queryAvailableLeveragesURL + " failed!! \n" + result);
    }

    public void queryAvailableLeverages(Map<String,Object> testData) throws Exception {
        //TODO: remove the hardcode!
        String body = "{\"id\":811454,\"mt4_account\":20772,\"mt4_account_type\":13,\"user_id\":10010528,\"mt4_datasource_id\":8,\"currency\":\"JPY\",\"equity\":4000,\"leverage\":\"25\",\"isInBlackList\":null,\"status\":\"Active\",\"serverCategory\":\"5\",\"isArchive\":0,\"server\":\"VantageInternational-Live\",\"autoModifyLeverage\":null,\"msg\":null,\"tag\":false,\"isHidden\":null,\"credit\":0,\"needRest\":false,\"resetTypeDisplay\":1,\"isReadOnly\":false,\"balance\":4000,\"isGroupUpgrade\":false,\"accountVirtualId\":null,\"nickname\":\"hello0527115249235\",\"group\":\"Test\\\\TEST_CRM_JPY\",\"specialPlatformType\":5,\"currencyLogo\":\"/src/assets/images/currency/JPY.png\",\"leverageStatus\":1,\"platform\":\"AU\"}";
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryAvailableLeveragesURL, body);
        printAPICPInfo(testData.get("url").toString()+queryAvailableLeveragesURL,body,result.toString());
        //assertEquals(result.get("code"), 0, queryAvailableLeveragesURL + " failed!! \n" + result);
    }

    public void updateBalanceDemo(Map<String,Object> testData) throws Exception {
        //TODO: remove the hardcode!
        String body = "{\"metaTraderLogin\":892274392,\"targetBalance\":\"400\"}";
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(updateBalanceDemoURL, body);
        printAPICPInfo(testData.get("url").toString()+updateBalanceDemoURL,body,result.toString());
        // assertEquals(result.get("code"), 0, updateBalanceDemoURL + " failed!! \n" + result);
    }

    public void updateLeverage(Map<String,Object> testData) throws Exception {
        //TODO: remove the hardcode!
        String body = "{\"metaTraderLogin\":20772,\"currentLeverage\":\"500\",\"newLeverage\":25,\"tnc\":true,\"mt4_datasource_id\":8}";
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(updateLeverageURL, body);
        System.out.println(result);
        //printAPICPInfo(testData.get("url").toString()+updateLeverageURL,body,result.toString());
        //assertEquals(result.get("code"), 0, updateLeverageURL + " failed!! \n" + result);
    }

    public void homeQueryMetaTraderAccountOverview(Map<String,Object> testData) throws Exception {
        String body = "{}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequestArray(queryMetaTraderAccountOverviewURL, body);
        testData.put("result",result);
        printAPICPInfo(testData.get("url").toString()+queryMetaTraderAccountOverviewURL,body,result.toString());
        //assertEquals(result.get("account"), "20772", queryMetaTraderAccountOverviewURL + " failed!! \n" + result);
    }

    public void hintInfo(Map<String, Object> testData) throws Exception{
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(hintInfoURL);
        printAPICPInfo(testData.get("url").toString()+hintInfoURL,"",result.toString());
        assertEquals(result.get("code"), 0, hintInfoURL + " failed!! \n" + result);
    }

    public void queryClientTotalEquity(Map<String,Object> testData) throws Exception {
        //todo: remove the hardcode
        String body = "{\"user_id\":10010528,\"currency\":\"USD\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryClientTotalEquityURL,body);
        printAPICPInfo(testData.get("url").toString()+queryClientTotalEquityURL,body,result.toString());
        assertEquals(result.get("code"), 0, queryClientTotalEquityURL + " failed!! \n" + result);
    }

    public JSONObject queryClientTotalEquity(String userId, String currency) throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("currency", currency);

        JSONObject result = postAPICall(queryClientTotalEquityURL, body);
        validateAPIResponse(queryClientTotalEquityURL, result);

        return result;
    }

    public JSONObject querySystemInfo() throws Exception {
        JSONObject result = getAPICall(getSystemInfo);
        return result;
    }

    public void getDirectClient(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getDirectClientURL);
        printAPICPInfo(testData.get("url").toString()+getDirectClientURL,"",result.toString());
        assertEquals(result.get("code"), 0, getDirectClientURL + " failed!! \n" + result);
    }

    public void encryptTextForOfficeWebsite(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> encrtTextParams = new LinkedHashMap<>();
        encrtTextParams.put("encrtText", "1747619768");
        String req = HyTechUtils.mapToForm(encrtTextParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(encrtTextForOfficeWebsitURL,req);
        printAPICPInfo(testData.get("url").toString()+encrtTextForOfficeWebsitURL,"",result.toString());
        assertEquals(result.get("code"), 0, encrtTextForOfficeWebsitURL + " failed!! \n" + result);
    }

    public void bannerDisplayPlatform(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(bannerdisplayPlatformURL);
        printAPICPInfo(testData.get("url").toString()+bannerdisplayPlatformURL,"",result.toString());
        assertEquals(result.get("success"), true, bannerdisplayPlatformURL + " failed!! \n" + result);
    }

    public void cashRebateAvailableAccounts(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(cashRebateAvailableAccountsURL);
        printAPICPInfo(testData.get("url").toString()+cashRebateAvailableAccountsURL,"",result.toString());
        assertEquals(result.get("code"), 0, cashRebateAvailableAccountsURL + " failed!! \n" + result);

    }

    public void getAllLastProof(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getAllLastProofURL);
        printAPICPInfo(testData.get("url").toString()+getAllLastProofURL,"",result.toString());
        assertEquals(result.get("code"), 0, getAllLastProofURL + " failed!! \n" + result);
    }

    public void multiFactorAuth(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(multiFactorAuthURL);
        printAPICPInfo(testData.get("url").toString()+multiFactorAuthURL,"",result.toString());
        assertEquals(result.get("code"), 0, multiFactorAuthURL + " failed!! \n" + result);
    }

    public void multiFactorAuthInfo(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(multiFactorAuthInfoURL);
        printAPICPInfo(testData.get("url").toString()+multiFactorAuthInfoURL,"",result.toString());
        assertEquals(result.get("code"), 0, multiFactorAuthInfoURL + " failed!! \n" + result);
    }

    public void queryOpenAccountStatus(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(queryOpenAccountStatusURL);
        printAPICPInfo(testData.get("url").toString()+queryOpenAccountStatusURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryOpenAccountStatusURL + " failed!! \n" + result);

    }

    public void getOpenAccountCondition(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getOpenAccountConditionURL);
        printAPICPInfo(testData.get("url").toString()+getOpenAccountConditionURL,"",result.toString());
        assertEquals(result.get("code"), 0, getOpenAccountConditionURL + " failed!! \n" + result);
    }

    public void getPerpetualSwitch(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getPerpetualSwitchURL);
        printAPICPInfo(testData.get("url").toString()+getPerpetualSwitchURL,"",result.toString());
        assertEquals(result.get("code"), 0, getPerpetualSwitchURL + " failed!! \n" + result);
    }

    public void getSupportCryptoCurrencies(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getSupportCryptoCurreniesURL);
        printAPICPInfo(testData.get("url").toString()+getSupportCryptoCurreniesURL,"",result.toString());
        assertEquals(result.get("code"), 0, getSupportCryptoCurreniesURL + " failed!! \n" + result);
    }

    public void openAccountValidate(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(openAccountValidateURL);
        printAPICPInfo(testData.get("url").toString()+openAccountValidateURL,"",result.toString());
        assertEquals(result.get("code"), 0, openAccountValidateURL + " failed!! \n" + result);
    }

    public void saveNickname(Map<String,Object> testData) throws Exception {
        //todo: remove the hardcode
        String body = "{\"mt4_account\":21628,\"nickname\":"+ "hello"+DateUtils.getDateTimeStr()+"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(saveNicknameURL,body);
        printAPICPInfo(testData.get("url").toString()+saveNicknameURL,body,result.toString());
        assertEquals(result.get("code"), 0, saveNicknameURL + " failed!! \n" + result);
    }

    public void createDemoAccount(Map<String,Object> testData) throws Exception {
        com.alibaba.fastjson.JSONObject req = new com.alibaba.fastjson.JSONObject();
        req.put("leverage",100);
        req.put("accountType","standardSTP");
        req.put("tradingPlatform","mt4");
        req.put("currency","USD");
        req.put("balance",1000);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(createDemoAccount,req.toString());
        printAPICPInfo(testData.get("url").toString()+createDemoAccount,req.toString(),result.toString());
        //assertEquals(result.get("code"), 0, createDemoAccount + " failed!! \n" + result);
    }

    public void getAccountType(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getAccountTypeURL);
        printAPICPInfo(testData.get("url").toString()+getAccountTypeURL,"",result.toString());
        assertEquals(result.get("code"), 0, getAccountTypeURL + " failed!! \n" + result);

    }

    public void verificationGetData(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(verificationGetDataURL);
        printAPICPInfo(testData.get("url").toString()+verificationGetDataURL,"",result.toString());
        assertEquals(result.get("code"), 0, verificationGetDataURL + " failed!! \n" + result);

    }

    public void securityRule(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(currentcurrentURL);
        printAPICPInfo(testData.get("url").toString()+currentcurrentURL,"",result.toString());
        assertEquals(result.get("code"), 0, currentcurrentURL + " failed!! \n" + result);

    }

    public void getMultiAuthMethod(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getMultiAuthMethodURL);
        printAPICPInfo(testData.get("url").toString()+getMultiAuthMethodURL,"",result.toString());
        assertEquals(result.get("code"), 0, getMultiAuthMethodURL + " failed!! \n" + result);

    }

    public void twoFactorStatus(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(twoFactorStatusURL);
        printAPICPInfo(testData.get("url").toString()+twoFactorStatusURL,"",result.toString());
        assertEquals(result.get("code"), 0, twoFactorStatusURL + " failed!! \n" + result);

    }

    public void twoFactorEnable(Map<String, Object> testData) throws Exception {
        String body = "{token: \"123321\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        //todo: url wrong?
        JSONObject result = sendCPAPIrequest(twoFactorEnableURL,body);
        printAPICPInfo(testData.get("url").toString()+twoFactorEnableURL,body,result.toString());
        assertEquals(result.get("code"), 1213, twoFactorEnableURL + " failed!! \n" + result);

    }

    public void multiFactorAuthverifyStatus(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(multiFactorAuthverifyStatusURL);
        printAPICPInfo(testData.get("url").toString()+multiFactorAuthverifyStatusURL,"",result.toString());
        assertEquals(result.get("code"), 0, multiFactorAuthverifyStatusURL + " failed!! \n" + result);
    }

    public void updatePasswordStatus(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(updatePasswordURL);
        printAPICPInfo(testData.get("url").toString()+updatePasswordURL,"",result.toString());
        assertEquals(result.get("code"), 0, updatePasswordURL + " failed!! \n" + result);
    }

    public void modifyPassword(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(modifyPasswordURL);
        printAPICPInfo(testData.get("url").toString()+modifyPasswordURL,"",result.toString());
        assertEquals(result.get("code"), 0, modifyPasswordURL + " failed!! \n" + result);
    }

    public void updatePwd(String publicKey,String currentPwd,String pwd,String confirmPwd){
        HyTechUtils.genXSourceHeader(header);
        header.put("pk",publicKey);
        JSONObject body = new JSONObject();
        body.put("currentPass",currentPwd);
        body.put("pass",pwd);
        body.put("checkPass",confirmPwd);

        try {
            String path = this.url + updatePwdURL;
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            printAPICPInfo(this.url + updatePwdURL,body.toString(),result.toString());

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void upateEmail(String txId, String code,String pwd,String email){
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        try {
            body.put("txId",txId);
            body.put("code",code);
            body.put("password",pwd);
            body.put("email",email);
            String path = this.url + updateEmailURL;
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            printAPICPInfo(this.url + updateEmailURL,body.toString(),result.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmailVerifyCode(String namespace){
        String txtId = "";
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        try {
            body.put("namespace",namespace);
            body.put("token",null);
            String path = this.url + sendEmailVerifyCode;
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            txtId = result.getJSONObject("data").getString("txId");
            printAPICPInfo(this.url + sendEmailVerifyCode,body.toString(),result.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return txtId;
    }

    public String getRWEmailVerifyCode(String namespace,String email){
        String txtId = "";
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        try {
            body.put("namespace",namespace);
            if(StringUtils.isNotBlank(email)){
                body.put("email",email);
            }
            String path = this.url + sendRWEmailVerifyCode;
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            txtId = result.getJSONObject("data").getString("txId");
            printAPICPInfo(this.url + sendEmailVerifyCode,body.toString(),result.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return txtId;
    }

    public void totpVerifyCode(String code){
        String txtId = "";
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        try {
            body.put("code",code);
            body.put("flag","CURRENT");
            String path = this.url + totpVerify;
            HttpResponse response = httpClient.getPostResponse(path, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            printAPICPInfo(this.url + sendEmailVerifyCode,body.toString(),result.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPassword(Map<String, Object> testData) throws Exception {
        Map<String,String> map = new LinkedHashMap<>();
        map.put("currentPass","7946e795e44db67be4c74219def41e2e");
        JSONObject result = sendCPAPIrequest(checkPasswordURL, map.toString());
        HyTechUtils.genXSourceHeader(header);
        printAPICPInfo(testData.get("url").toString()+checkPasswordURL,map.toString(),result.toString());
        assertEquals(result.get("code"), 0, checkPasswordURL + " failed!! \n" + result);
    }


    public void getClientSwitch(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getClientSwitchURL);
        printAPICPInfo(testData.get("url").toString()+getClientSwitchURL,"",result.toString());
        assertEquals(result.get("code"), 0, getClientSwitchURL + " failed!! \n" + result);
    }

    //需要执行的测试用例
    //api/home/queryRebatesBlackList
    public void queryRebatesBlackList(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(queryRebatesBlackListURL);
        printAPICPInfo(testData.get("url").toString()+queryRebatesBlackListURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryRebatesBlackListURL + " failed!! \n" + result);
    }


    public void getUsername(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getUsername);
        printAPICPInfo(testData.get("url").toString()+getUsername,"",result.toString());
        assertEquals(result.get("code"), 0, getUsername + " failed!! \n" + result);
    }

    public void getInviteCodebyibcount(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(get_invite_code_by_ib_count);
        printAPICPInfo(testData.get("url").toString()+get_invite_code_by_ib_count,"",result.toString());
        assertEquals(result.get("code"), 0, get_invite_code_by_ib_count + " failed!! \n" + result);
    }

    public void getInstruments(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getInstruments);
        printAPICPInfo(testData.get("url").toString()+getInstruments,"",result.toString());
    }

    public void ticker(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendNonJsonGETrequest(HomeActivityTickerURL);
        printAPICPInfo(testData.get("url").toString()+HomeActivityTickerURL,"",result.toString());
        assertEquals(result.get("code"), 0, HomeActivityTickerURL + " failed!! \n" + result);
    }

    public void getcpurl(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(get_cp_url);
        printAPICPInfo(testData.get("url").toString()+get_cp_url,"",result.toString());
        assertEquals(result.get("code"), 0, get_cp_url + " failed!! \n" + result);
    }

    //    api/question/employmentFinance
    public void employmentFinance(Map<String, Object> testData) throws Exception {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(employmentFinance);
        printAPICPInfo(testData.get("url").toString()+employmentFinance,"",result.toString());
        assertEquals(result.get("code"), 0, employmentFinance + " failed!! \n" + result);
    }

    public void questionTrading(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(questionTrading);
        printAPICPInfo(testData.get("url").toString()+questionTrading,"",result.toString());
        assertEquals(result.get("code"), 0, questionTrading + " failed!! \n" + result);
    }

    public void multiFactorAuthverifyNum(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(multiAuthVerifyNum);
        printAPICPInfo(testData.get("url").toString()+multiAuthVerifyNum,"",result.toString());
        assertEquals(result.get("code"), 0, multiAuthVerifyNum + " failed!! \n" + result);
    }

    public void birthPlace(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(birthPlace);
        printAPICPInfo(testData.get("url").toString()+birthPlace,"",result.toString());
        assertEquals(result.get("code"), 0, birthPlace + " failed!! \n" + result);
    }

    public void provinces(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(provinces);
        printAPICPInfo(testData.get("url").toString()+provinces,"",result.toString());
        assertEquals(result.get("code"), 0, provinces + " failed!! \n" + result);
    }

    public void getMetaTraderAccounts(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getMetaTraderAccounts);
        printAPICPInfo(testData.get("url").toString()+getMetaTraderAccounts,"",result.toString());
        assertEquals(result.get("code"), 0, getMetaTraderAccounts + " failed!! \n" + result);
    }

    public void questionuserAnswerCheck(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(userAnswerCheck);
        printAPICPInfo(testData.get("url").toString()+userAnswerCheck,"",result.toString());
        assertEquals(result.get("code"), 0, userAnswerCheck + " failed!! \n" + result);

    }

    public void getCurrentStep(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getCurrentStep);
        printAPICPInfo(testData.get("url").toString()+getCurrentStep,"",result.toString());
        assertEquals(result.get("code"), 0, getCurrentStep + " failed!! \n" + result);
    }

    public void personalDetailverifyMethod(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(verifyMethod);
        printAPICPInfo(testData.get("url").toString()+verifyMethod,"",result.toString());
        assertEquals(result.get("code"), 0, verifyMethod + " failed!! \n" + result);
    }

    public void categorisationTest(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(categorisationTest);
        printAPICPInfo(testData.get("url").toString()+categorisationTest,"",result.toString());
        assertEquals(result.get("code"), 0, categorisationTest + " failed!! \n" + result);
    }

    public void questionnaire(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(questionnaireVerify);
        printAPICPInfo(testData.get("url").toString()+questionnaireVerify,"",result.toString());
        assertEquals(result.get("code"), 0, questionnaireVerify + " failed!! \n" + result);
    }


    public void exchangeRate(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(exchangeRate);
        printAPICPInfo(testData.get("url").toString()+exchangeRate,"",result.toString());
        assertEquals(result.get("code"), 0, exchangeRate + " failed!! \n" + result);
    }

    public void sessionId(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(sessionidURL);
        printAPICPInfo(testData.get("url").toString()+sessionidURL,"",result.toString());
        assertEquals(result.get("code"), 0, sessionidURL + " failed!! \n" + result);
    }


    public void jump(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(jumpURL);
        printAPICPInfo(testData.get("url").toString()+jumpURL,"",result.toString());
        assertEquals(result.get("code"), 0, jumpURL + " failed!! \n" + result);
    }

    public void information(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(informationURL);
        printAPICPInfo(testData.get("url").toString()+informationURL,"",result.toString());
        assertEquals(result.get("referralSite"), "www.vantagemarkets.com", informationURL + " failed!! \n" + result);
    }


    public void getAddressProofAndBankInfor(Map<String, Object> testData) throws Exception{
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getAddressProofAndBankInforURL);
        printAPICPInfo(testData.get("url").toString()+getAddressProofAndBankInforURL,"",result.toString());
        assertEquals(result.get("code"), 0, getAddressProofAndBankInforURL + " failed!! \n" + result);
    }

    public  void checkTransferIB(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(checkTransferIBURL);
        printAPICPInfo(testData.get("url").toString()+checkTransferIBURL,"",result.toString());
        assertEquals(result.get("code"), 0, checkTransferIBURL + " failed!! \n" + result);
    }

    public  void historyIbTransfer(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(historyIbTransferURL);
        printAPICPInfo(testData.get("url").toString()+historyIbTransferURL,"",result.toString());
        assertEquals(result.get("code"), 0, historyIbTransferURL + " failed!! \n" + result);
    }

    public  void transferIBAffiliatenew(Map<String, Object> testData) {
        String body = "{\"partnershipType\":\"1\",\"newIBAffiliateNumber\":\"ffffd\",\"reson\":\"4344\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(transferIBAffiliatenewURL,body);
        printAPICPInfo(testData.get("url").toString()+transferIBAffiliatenewURL,"",result.toString());
        assertEquals(result.get("code"), 410, transferIBAffiliatenewURL + " failed!! \n" + result);
    }

    public  void keyPairPK(Map<String, Object> testData) {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(keyPairPKURL);
        printAPICPInfo(testData.get("url").toString()+keyPairPKURL,"",result.toString());
        assertEquals(result.get("code"), 0, keyPairPKURL + " failed!! \n" + result);
    }

    public  void getSecuritys(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getSecuritysURL);
        printAPICPInfo(testData.get("url").toString()+getSecuritysURL,"",result.toString());
        assertEquals(result.get("code"), 0, getSecuritysURL + " failed!! \n" + result);
    }

    public JSONObject getSecuritys(){
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getSecuritysURL);
        printAPICPInfo(this.url+getSecuritysURL,"",result.toString());
        return result;
    }
    public JSONObject keyPairPK() {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        return sendCPAPIGETrequest(keyPairPKURL);
    }

    public void reqResetPassword(String email){
        header.put("Content-Type", "application/x-www-form-urlencoded");

        HyTechUtils.genXSourceHeader(header);

        LinkedHashMap<String, Object> ibQueryAvailableBalancParams = new LinkedHashMap<>();
        ibQueryAvailableBalancParams.put("email", email);
        ibQueryAvailableBalancParams.put("baseUrl", this.url);
        String req = HyTechUtils.mapToForm(ibQueryAvailableBalancParams);
        try {
            String path = this.url + reqResetPassword;
            HttpResponse response = httpClient.getPostResponse(path, header, req);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            printAPICPInfo(this.url + reqResetPassword,req.toString(),result.toString());

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getPk(String token){
        try {
            String pkUrl = keyPairPKURL.replace("null",token);
            JSONObject pkResult = sendCPAPIGETrequest(pkUrl);
            return pkResult.getString("data");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public void resetPassword(String pk, String token,String pass,String checkPass,String link){

        try {

            header.put("Content-Type", "application/x-www-form-urlencoded");
            header.put("pk", pk);
            header.put("referer", link);

            HyTechUtils.genXSourceHeader(header);

            LinkedHashMap<String, Object> ibQueryAvailableBalancParams = new LinkedHashMap<>();
            ibQueryAvailableBalancParams.put("token", token);
            ibQueryAvailableBalancParams.put("pass", URLEncoder.encode(pass));
            ibQueryAvailableBalancParams.put("checkPass", URLEncoder.encode(checkPass));
            String req = HyTechUtils.mapToForm(ibQueryAvailableBalancParams);

            String path = this.url + resetPassword;
            HttpResponse response = httpClient.getPostResponse(path, header, req);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            printAPICPInfo(this.url + resetPassword,req.toString(),result.toString());
            assertEquals(((JSONObject)result.get("data")).getBoolean("result"), true, resetPassword + " failed!! \n" + result);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public  void profileInfo(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(profileInfoURL);
        printAPICPInfo(testData.get("url").toString()+profileInfoURL,"",result.toString());
        assertEquals(result.get("code"), 0, profileInfoURL + " failed!! \n" + result);
    }

    public JSONObject queryProfileInfo() throws Exception {
        Map<String, String> body = new HashMap<>();

        JSONObject result = postAPICall(profileInfoURL, body);
        validateAPIResponse200(profileInfoURL, result);

        return result;
    }

    public  void accessToken(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(accessTokenURL);
        printAPICPInfo(testData.get("url").toString()+accessTokenURL,"",result.toString());
        assertEquals(result.get("code"), 0, accessTokenURL + " failed!! \n" + result);

    }


    public  void setLanguage(Map<String, Object> testData) {
        LinkedHashMap<String, Object> setlanguageParams = new LinkedHashMap<>();
        setlanguageParams.put("language", "en_US");
        String req = HyTechUtils.mapToForm(setlanguageParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(setlanguageURL,req);
        printAPICPInfo(testData.get("url").toString()+setlanguageURL,"",result.toString());
        assertEquals(result.get("code"), 0, setlanguageURL + " failed!! \n" + result);
    }

    public  void i18nUrl(Map<String, Object> testData) {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(i18nURL);
        printAPICPInfo(testData.get("url").toString()+i18nURL,"",result.toString());
        assertEquals(result.get("code"), 0, i18nURL + " failed!! \n" + result);
    }

    public void applyAdditionalAccount(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("notes", "");
        neverFundedParams.put("accountType", "standardSTP");
        neverFundedParams.put("tradingPlatform", "mt5");
        neverFundedParams.put("currency", "USD");
        neverFundedParams.put("tnc", true);
        neverFundedParams.put("nickname", "0");
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(applyAdditionalAccountURL,req);
        printAPICPInfo(testData.get("url").toString()+applyAdditionalAccountURL,"",result.toString());
        assertEquals(result.get("code"), 0, applyAdditionalAccountURL + " failed!! \n" + result);
    }

    public void resetChangeIPWarn(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("changeIpWarn", false);
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/json,text/plain,*/*");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(resetChangeIPWarnURL,req);
        System.out.println(result);
        printAPICPInfo(testData.get("url").toString()+resetChangeIPWarnURL,"",null);
        assertEquals(result, null, resetChangeIPWarnURL + " failed!! \n" + result);
    }


    public void updateLeverageDemo(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("currentLeverage", 200);
        neverFundedParams.put("metaTraderLogin", "892274392");
        neverFundedParams.put("newLeverage", 100);
        neverFundedParams.put("mt4_datasource_id", "4");
        String req = "{\"currentLeverage\":200,\"metaTraderLogin\":\"892274392\",\"newLeverage\":25,\"mt4_datasource_id\":4}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(updateLeverageDemoURL,req);
        System.out.println(result);
        printAPICPInfo(testData.get("url").toString()+updateLeverageDemoURL,"",null);
        //assertEquals(result, null, updateLeverageDemoURL + " failed!! \n" + result);
    }


    public void sendEmailVerificationCode(Map<String, Object> testData) throws Exception {
        String req = "{\"namespace\":\"CP_VERIFY_EMAIL\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(sendEmailVerificationCodeURL,req);
        System.out.println(result);
        printAPICPInfo(testData.get("url").toString()+sendEmailVerificationCodeURL,"",null);
        assertEquals(result.get("code"), 0, sendEmailVerificationCodeURL + " failed!! \n" + result);
    }

    /* IB methods start here */

    public void sessionid(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(sessionidURL);
        printAPICPInfo(testData.get("url").toString()+sessionidURL,"",result.toString());
        assertEquals(result.get("code"), 0, sessionidURL + " failed!! \n" + result);
    }

    public void toLoginbyToken(Map<String, Object> testData) throws Exception {
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(toLoginbyTokenURL,"");
        printAPICPInfo(testData.get("url").toString()+toLoginbyTokenURL,"",result.toString());
        assertEquals(result.get("code"), 0, toLoginbyTokenURL + " failed!! \n" + result);
    }

    public void ibRebateList(Map<String, Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(IB_REBATELIST);
        printAPICPInfo(testData.get("url").toString()+IB_REBATELIST,"",result.toString());
        assertEquals(result.get("code"), 0, IB_REBATELIST + " failed!! \n" + result);
    }

    public void ibQueryAvailableBalanc(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> ibQueryAvailableBalancParams = new LinkedHashMap<>();
        ibQueryAvailableBalancParams.put("qUserId", "0");
        ibQueryAvailableBalancParams.put("qAccount", "2399");
        String req = HyTechUtils.mapToForm(ibQueryAvailableBalancParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(ibQueryAvailableBalanceURL,req);
        printAPICPInfo(testData.get("url").toString()+ibQueryAvailableBalanceURL,"",result.toString());
        assertEquals(result.get("code"), 0, ibQueryAvailableBalanceURL + " failed!! \n" + result);
    }

    public void getNearestOpenAccount(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> ibQueryAvailableBalancParams = new LinkedHashMap<>();
        ibQueryAvailableBalancParams.put("mt4account", "2399");
        String req = HyTechUtils.mapToForm(ibQueryAvailableBalancParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getNearestOpenAccountURL,req);
        printAPICPInfo(testData.get("url").toString()+getNearestOpenAccountURL,"",result.toString());
        assertEquals(result.get("code"), 0, getNearestOpenAccountURL + " failed!! \n" + result);
    }

    public void queryRebateVolumeList(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> ibQueryAvailableBalancParams = new LinkedHashMap<>();
        ibQueryAvailableBalancParams.put("qUserId", "0");
        ibQueryAvailableBalancParams.put("qAccount", "2399");
        String req = HyTechUtils.mapToForm(ibQueryAvailableBalancParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryRebateVolumeListURL,req);
        printAPICPInfo(testData.get("url").toString()+queryRebateVolumeListURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryRebateVolumeListURL + " failed!! \n" + result);
    }

    public void queryTotalCommissiont(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> queryTotalCommissiontParams = new LinkedHashMap<>();
        queryTotalCommissiontParams.put("qUserId", "0");
        queryTotalCommissiontParams.put("qAccount", "2399");
        queryTotalCommissiontParams.put("qStartTime", "2025-05-01 00:00:00");
        queryTotalCommissiontParams.put("qEndTime", "2025-05-29 23:59:59");
        String req = HyTechUtils.mapToForm(queryTotalCommissiontParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryTotalCommissiontURL,req);
        printAPICPInfo(testData.get("url").toString()+queryTotalCommissiontURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryTotalCommissiontURL + " failed!! \n" + result);
    }

    public void getNewOpenUserTotal(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> getNewOpenUserTotalParams = new LinkedHashMap<>();
        getNewOpenUserTotalParams.put("mt4account", "2399");
        getNewOpenUserTotalParams.put("qStartTime", "2025-05-01 00:00:00");
        getNewOpenUserTotalParams.put("qEndTime", "2025-05-29 23:59:59");
        String req = HyTechUtils.mapToForm(getNewOpenUserTotalParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getNewOpenUserTotalURL,req);
        printAPICPInfo(testData.get("url").toString()+getNewOpenUserTotalURL,"",result.toString());
        assertEquals(result.get("code"), 0, getNewOpenUserTotalURL + " failed!! \n" + result);
    }

    public void queryFoldLineTotalCommission(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> queryFoldLineTotalCommissionParams = new LinkedHashMap<>();
        queryFoldLineTotalCommissionParams.put("qAccount", "2399");
        queryFoldLineTotalCommissionParams.put("qUserId", "0");
        queryFoldLineTotalCommissionParams.put("qStartTime", "2025-05-01 00:00:00");
        queryFoldLineTotalCommissionParams.put("qEndTime", "2025-05-29 23:59:59");
        String req = HyTechUtils.mapToForm(queryFoldLineTotalCommissionParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryFoldLineTotalCommissionURL,req);
        printAPICPInfo(testData.get("url").toString()+queryFoldLineTotalCommissionURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryFoldLineTotalCommissionURL + " failed!! \n" + result);
    }

    public void queryPieTotalVolumeByGoods(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> queryFoldLineTotalCommissionParams = new LinkedHashMap<>();
        queryFoldLineTotalCommissionParams.put("qAccount", "2399");
        queryFoldLineTotalCommissionParams.put("qUserId", "0");
        queryFoldLineTotalCommissionParams.put("qStartTime", "2025-05-01 00:00:00");
        queryFoldLineTotalCommissionParams.put("qEndTime", "2025-05-29 23:59:59");
        String req = HyTechUtils.mapToForm(queryFoldLineTotalCommissionParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(queryPieTotalVolumeByGoodsURL,req);
        printAPICPInfo(testData.get("url").toString()+queryPieTotalVolumeByGoodsURL,"",result.toString());
        assertEquals(result.get("code"), 0, queryPieTotalVolumeByGoodsURL + " failed!! \n" + result);
    }

    public void ibNotification(Map<String, Object> testData) throws Exception {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);

        JSONObject result = sendCPAPIrequest(ibNotificationURL,"{\"pid\":0}");
        printAPICPInfo(testData.get("url").toString()+ibNotificationURL,"{\"pid\":0}",result.toString());
        assertEquals(result.get("code"), 0, ibNotificationURL + " failed!! \n" + result);
    }

    public void subIbStree(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> subIbStreeParams = new LinkedHashMap<>();
        subIbStreeParams.put("userId", "0");
        subIbStreeParams.put("account", "2399");
        String req = HyTechUtils.mapToForm(subIbStreeParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(subIbStreeURL,req);
        printAPICPInfo(testData.get("url").toString()+subIbStreeURL,"",result.toString());
        assertEquals(result.get("code"), 0, subIbStreeURL + " failed!! \n" + result);
    }

    public void retailclientsV2(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> retailclientsV2Params = new LinkedHashMap<>();
        retailclientsV2Params.put("userId", "0");
        retailclientsV2Params.put("account", "2399");
        retailclientsV2Params.put("accountJourney", "");
        String req = HyTechUtils.mapToForm(retailclientsV2Params);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(retailClientsV2URL,req);
        printAPICPInfo(testData.get("url").toString()+retailClientsV2URL,"",result.toString());
        assertEquals(result.get("code"), 0, retailClientsV2URL + " failed!! \n" + result);
    }

    public void zeroBalance(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> zeroBalanceParams = new LinkedHashMap<>();
        zeroBalanceParams.put("userId", "0");
        String req = HyTechUtils.mapToForm(zeroBalanceParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(zeroBalanceURL,req);
        printAPICPInfo(testData.get("url").toString()+zeroBalanceURL,"",result.toString());
        assertEquals(result.get("code"), 0, zeroBalanceURL + " failed!! \n" + result);
    }

    public void neverFunded(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("userId", "0");
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(neverFundedURL,req);
        printAPICPInfo(testData.get("url").toString()+neverFundedURL,"",result.toString());
        assertEquals(result.get("code"), 0, neverFundedURL + " failed!! \n" + result);
    }

    public void queryIbReportData(Map<String,Object> testData) throws Exception {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        String req="{\"userId\":0,\"rebateAccount\":2399,\"startDate\":\"2025-05-01 00:00:00\",\"endDate\":\"2025-05-29 23:59:59\"}";
        JSONObject result = sendCPAPIrequest(queryIbReportDataURL,req);
        printAPICPInfo(testData.get("url").toString()+queryIbReportDataURL,req,result.toString());
        assertEquals(result.get("totalWithdraw"), 0, queryIbReportDataURL + " failed!! \n" + result);
    }

    public void getPendingAccount(Map<String,Object> testData) throws Exception {
        Map<String, Object> getPendingAccountParams=new HashMap<>();
        getPendingAccountParams.put("pid", "0");
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);

        JSONObject result = sendCPAPIrequestArray(getPendingAccountURL,"{\"pid\":0}");
        printAPICPInfo(testData.get("url").toString()+getPendingAccountURL,"{\"pid\":0}",result.toString());
    }

    public void rebateAgreement(Map<String,Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(rebateAgreementURL);
        printAPICPInfo(testData.get("url").toString()+rebateAgreementURL,"",result.toString());
    }

    public void getFirstAccountAuditStatus(Map<String,Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getFirstAccountAuditStatusURL);
        printAPICPInfo(testData.get("url").toString()+getFirstAccountAuditStatusURL,"",result.toString());
    }

    public void rebateHistory(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("pageNo", "0");
        neverFundedParams.put("limit", "10");
        neverFundedParams.put("qUserId", "0");
        neverFundedParams.put("qAccount", "2399");
        neverFundedParams.put("qStartTime", "2025-06-01 00:00:00");
        neverFundedParams.put("qEndTime", "2025-06-04 23:59:59");
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(rebateHistoryURL,req);
        printAPICPInfo(testData.get("url").toString()+rebateHistoryURL,"",result.toString());
        assertEquals(result.get("code"), 0, rebateHistoryURL + " failed!! \n" + result);
    }

    public void transferHistory(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("pageNo", "0");
        neverFundedParams.put("limit", "10");
        neverFundedParams.put("qUserId", "0");
        neverFundedParams.put("qAccount", "2399");
        neverFundedParams.put("qStartTime", "2025-06-01 00:00:00");
        neverFundedParams.put("qEndTime", "2025-06-04 23:59:59");
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(transferHistoryURL,req);
        printAPICPInfo(testData.get("url").toString()+transferHistoryURL,"",result.toString());
        assertEquals(result.get("code"), 0, transferHistoryURL + " failed!! \n" + result);
    }

    public void applyCommission(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("qAccount", "2399");
        neverFundedParams.put("qUserId", "0");
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(applyCommissionURL,req);
        printAPICPInfo(testData.get("url").toString()+applyCommissionURL,"",result.toString());
        assertEquals(result.get("code"), 584, applyCommissionURL + " failed!! \n" + result);
    }

    public void reportQueryByDate(Map<String, Object> testData) throws Exception {
        String body = "{\"account\":2399,\"startDate\":\"2025-06-01 00:00:00\",\"endDate\":\"2025-06-04 23:59:59\",\"symbol\":null,\"dateType\":\"day\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(reportQueryByDateURL,body);
        printAPICPInfo(testData.get("url").toString()+reportQueryByDateURL,body,result.toString());
        assertEquals(result.get("code"), 0, reportQueryByDateURL + " failed!! \n" + result);
    }

    public void rebateReport(Map<String, Object> testData) throws Exception {
        String body = "{\"account\":2399,\"startDate\":\"2025-06-01 00:00:00\",\"endDate\":\"2025-06-04 23:59:59\",\"symbol\":null}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(rebateReportURL,body);
        printAPICPInfo(testData.get("url").toString()+rebateReportURL,body,result.toString());
        assertEquals(result.get("payerSummaries"), new ArrayList(), rebateReportURL + " failed!! \n" + result);
    }

    public void leads(Map<String, Object> testData) throws Exception {
        String body = "{\"userId\":0}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequestArray(leadsURL,body);
        printAPICPInfo(testData.get("url").toString()+leadsURL,body,result.toString());
        //assertEquals(result.get("code"), 0, leadsURL + " failed!! \n" + result);
    }

    public void subIbs(Map<String, Object> testData) throws Exception {
        LinkedHashMap<String, Object> neverFundedParams = new LinkedHashMap<>();
        neverFundedParams.put("userId", "0");
        neverFundedParams.put("account", "2959");
        neverFundedParams.put("filterActive", false);
        String req = HyTechUtils.mapToForm(neverFundedParams);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(subIbsURL,req);
        printAPICPInfo(testData.get("url").toString()+subIbsURL,"",result.toString());
        assertEquals(result.get("code"), 0, subIbsURL + " failed!! \n" + result);
    }

    public void getAgreementList(Map<String,Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getAgreementListURL,"");
        printAPICPInfo(testData.get("url").toString()+getAgreementListURL,"",result.toString());
        assertEquals(result.get("code"), 0, getAgreementListURL + " failed!! \n" + result);
    }


    public void subIbClients(Map<String, Object> testData) throws Exception {
        String body = "{\"filterActive\":false,\"accounts\":[2399]}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(subIbClientsURL,body);
        printAPICPInfo(testData.get("url").toString()+subIbClientsURL,body,result.toString());
        assertEquals(result.get("code"), 0, subIbClientsURL + " failed!! \n" + result);
    }

    public void identityProof(Map<String,Object> testData) throws Exception {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(identityProofURL);
        printAPICPInfo(testData.get("url").toString()+identityProofURL,"",result.toString());
        assertEquals(result.get("code"), 0, identityProofURL + " failed!! \n" + result);
    }


    public void emailPreValidate(Map<String,Object> testData) throws Exception {
        String req="{\"code\":\"111111\",\"txId\":\"PCWDrfNBtdYAd/uN8v7s8TRyVnfo5wPNFh70ofesiVI=\",\"email\":\"a****i@testcrmautomation.com\"}";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(emailPreValidateURL,req);
        printAPICPInfo(testData.get("url").toString()+emailPreValidateURL,req,result.toString());
        assertEquals(result.get("code"), 647, emailPreValidateURL + " failed!! \n" + result);
    }

}
