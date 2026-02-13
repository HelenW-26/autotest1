package newcrm.testcases.cptestcases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.business.businessbase.*;
import newcrm.cpapi.CPAPIAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.WithdrawBasePage;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.encryption.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import tools.ChangePassword;
import utils.LogUtils;
import vantagecrm.DBUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static newcrm.global.GlobalProperties.*;
import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public class WithdrawLimitTestCases  extends BaseTestCaseNew {

    // withdraw security limit
    public static boolean limitSwitchOn = true;
    public static boolean limitSwitchOff = false;

    public static String pwdCode = "updatePassword";
    public static String loginCode = "updateUserLogin";
    public static String authCode = "changeAuthenticator";

    protected AdminAPIPayment adminPaymentAPI;
    protected CPAPIAccount cpapiAccount;

    private CPMenu menu;
    protected static CPLogin login;

    protected Map<String,Object> testData;
    protected JSONArray ruleArray;

    private Factor myfactor;
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        login = getLogin();
        myfactor = getFactorNew();
        driver = getDriverNew();

    }
    protected void initBase() {
        menu = myfactor.newInstance(CPMenu.class);
        login.goToCpHome();
        menu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(GlobalProperties.CPMenuName.HOME);
    }

    public String getRuleId(String functionCode){
        String ruleId = "";

        for(Object obj : ruleArray){
            JSONObject js = (JSONObject)obj;
            if(functionCode.equalsIgnoreCase(js.getString("functionCode"))){
                ruleId =  js.getString("id");
                break;
            }
        }
        return ruleId;
    }

    public void judgeMsgEnable2fa(Object data[][],GlobalProperties.DEPOSITMETHOD method,String accountNumber ){
        String settingId = null;
        String status = null;
        try {
            JSONObject JS = adminPaymentAPI.apiGet2FASettingList();

            JSONArray jsArr = JS.getJSONArray("data");


            for(int i = 0; i < jsArr.size();i ++){
                JSONObject obj = jsArr.getJSONObject(i);
                if("Withdrawal".equalsIgnoreCase(obj.getString("functionCodeText"))){
                    settingId = obj.getString("id");
                    status = obj.getString("status");
                    break;
                }
            }

            adminPaymentAPI.apiUpdate2FASetting(settingId,"1");

            CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
            CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);

            initBase();

            menu.goToMenu(GlobalProperties.CPMenuName.WITHDRAWFUNDS);

            this.setWithdrawalMethod(method,cpWithdrawLimit,null);

            String verifyMsg = cpWithdrawLimit.getVerifyMsgEnable2FA();

            Assert.assertTrue(verifyMsg.contains("enable Security Authenticator APP"),"wrong verify message");

            // enable 2fa

            cpWithdrawLimit.clickVerify2faConfirmBtn();

            String pwd = (String)data[0][2];
            String authUri = cpWithdrawLimit.getAuthUri(pwd);
            assertTrue(StringUtils.isNotBlank(authUri), "enable 2fa failed" );
            menu.goToMenu(GlobalProperties.CPMenuName.WITHDRAWFUNDS);

            JSONObject js = this.setWithdrawalMethod(method, cpWithdrawLimit, accountNumber);

            cpWithdrawLimit.submitWithdraw();

            cpWithdrawLimit.inputCode(authUri);

            menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
            Assert.assertTrue(history.checkWithdrawAndCancelOrder(js.getString("account"), method, js.getString("amount"), STATUS.SUBMITTED),
                    "Do not Find the withdraw in history page");

        } catch (Exception e) {
            fail();
            LogUtils.info("Account detected with remote login activity; enable 2fa failed. ");
        }finally {
            adminPaymentAPI.apiUpdate2FASetting(settingId,status);
        }
    }

    public void limitWithResetPwd(Object data[][],GlobalProperties.DEPOSITMETHOD method,String ruleId,String env){

        String oldPwd = (String) data[0][2];
        String loginEmail = (String) data[0][1];

        try {
            String newPwd = "123Qwe!!";

            cpapiAccount.reqResetPassword(loginEmail);

            String vars = DBUtils.funcReadResetPwdInBusinessDB(env.toUpperCase(), GlobalMethods.getBrand(), (String) data[0][0], loginEmail);
            JSONObject js = JSONObject.parseObject(vars);
            JSONObject obj = js.getJSONObject("vars");

            String link =  obj.getString("CHANGE_LINK");

            LogUtils.info("change link is: " + link );
            String token = link.split("=")[1];
            String pk = cpapiAccount.getPk(token);
            String pwd = EncryptUtil.encryptRsa(newPwd,pk);
            String pwd1 = EncryptUtil.encryptRsa(newPwd,pk);
            cpapiAccount.resetPassword(pk,token,pwd,pwd1,link);

            CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);

            data[0][2] = newPwd;

            Thread.sleep(1000L);
            this.verifyLimit(data,cpWithdrawLimit,WithdrawLimitTestCases.pwdCode,method,"login password");

        } catch (Exception e) {
            LogUtils.info("reset password failed");
            fail();
        }finally {
            data[0][2] = oldPwd;
            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId,WithdrawLimitTestCases.limitSwitchOff);

            ChangePassword.updatePass(oldPwd,loginEmail, env.toUpperCase(),brand, regulator.toUpperCase());
        }



    }

    public void limitWithModifyPwd( Object data[][], GlobalProperties.DEPOSITMETHOD method ,String ruleId)  {

        String oldPwd = null;
        String newPwd = "123Qwe!!";
        String loginEmail = (String)data[0][1];
        try{
            oldPwd = (String)data[0][2];
            CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
            cpWithdrawLimit.refresh();

            if(driver.getCurrentUrl().contains("login")){
                CPLogin cpLogin = myfactor.newInstance(CPLogin.class,(String)data[0][3]);
                cpLogin.login((String)data[0][1],oldPwd);
            }

            initBase();
            menu.goToMenu(GlobalProperties.CPMenuName.PROFILES);

            menu.goToMenu(GlobalProperties.CPMenuName.SECURITYMANAGEMENT);

            assertTrue(cpWithdrawLimit.modifyPwd(oldPwd, newPwd), "modify password failed, old password: " + oldPwd);

            data[0][2] = newPwd;

            this.verifyLimit(data,cpWithdrawLimit,WithdrawLimitTestCases.pwdCode,method,"login password");
            
        }finally {

            data[0][2] = oldPwd;
            ChangePassword.updatePass(oldPwd,loginEmail, env.toUpperCase(),brand, regulator.toUpperCase());
            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId,WithdrawLimitTestCases.limitSwitchOff);
        }

    }


    public void limitWithModifyEmail(Object data[][], String brand, GlobalProperties.DEPOSITMETHOD method,String ruleId,String env)  {

        String oldEmail = null;
        String newEmail = null;
        String pwd = (String)data[0][2];
        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
        try{

            cpWithdrawLimit.refresh();

            if(driver.getCurrentUrl().contains("login")){
                CPLogin cpLogin = myfactor.newInstance(CPLogin.class,(String)data[0][3]);
                cpLogin.login((String)data[0][1],pwd);
            }

            initBase();

            menu.goToMenu(GlobalProperties.CPMenuName.PROFILES);

            menu.goToMenu(GlobalProperties.CPMenuName.SECURITYMANAGEMENT);

            oldEmail = (String)data[0][1];

            newEmail = "webwithdrawLimit@testcrm.com";

            assertTrue(cpWithdrawLimit.modifyEmail(pwd, newEmail), "modify email failed, old email: " + oldEmail);
            data[0][1] = newEmail;
            this.verifyLimit(data,cpWithdrawLimit,loginCode,method,"email/phone");
        }
        catch (Exception e) {
            GlobalMethods.printDebugInfo("update email failed");
            fail();
        }
        finally {
            data[0][1] = oldEmail;
//            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId,WithdrawLimitTestCases.limitSwitchOff);
            String url = (String)data[0][3];
            cpapiAccount = new CPAPIAccount(url,newEmail,pwd);
            String txId = "";
            String code = "";

            String emailEncrypt = EncryptUtil.getAdminEmailEncrypt(oldEmail);
            if("AU".equalsIgnoreCase(brand)||"VFX".equalsIgnoreCase(brand)){

                String userId = userId();
                adminPaymentAPI.modifyEmail(userId,emailEncrypt);
                adminPaymentAPI.syncModifyEmail(userId);
                ChangePassword.updatePass((String)data[0][2],oldEmail, env.toUpperCase(),GlobalMethods.setEnvValues(brand).toUpperCase(), regulator.toUpperCase());
            }else {
                txId = cpapiAccount.getEmailVerifyCode("CP_VERIFY_EMAIL");
                code = cpWithdrawLimit.getEmailVerifyCode( GlobalMethods.setEnvValues(brand).toUpperCase(), regulator, newEmail,env);
                LogUtils.info("email code is: " + code);

                cpapiAccount.upateEmail(txId, code,EncryptUtil.MD5(pwd),emailEncrypt);
            }
            loginThreadLocal.remove();
        }

    }

    public void limitWithModify2fa(Object data[][], GlobalProperties.DEPOSITMETHOD method,String ruleId)  {
        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
        try{

            initBase();

            menu.goToMenu(GlobalProperties.CPMenuName.PROFILES);

            menu.goToMenu(GlobalProperties.CPMenuName.SECURITYMANAGEMENT);

            String pwd = (String)data[0][2];
            assertTrue(cpWithdrawLimit.authApp(pwd), "modify 2fa failed" );

            this.verifyLimit(data,cpWithdrawLimit,authCode,method,"Security Authenticator APP");


        }
        catch (Exception e) {
            GlobalMethods.printDebugInfo("update 2fa failed");
            fail();
        } finally {

            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOff);
        }
    }

    public String userId(){
        Map<String,Object> map = (Map<String,Object>)testData.get("result");
        List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("Accounts");
        return list.get(0).get("user_id").toString();
    }

    public void limitWithUnbind2fa(Object data[][], GlobalProperties.DEPOSITMETHOD method,String ruleId)  {
        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
        try{

            adminPaymentAPI.disable2FA(userId());

            this.verifyLimit(data,cpWithdrawLimit,authCode,method,"Security Authenticator APP");

        }
        catch (Exception e) {
            GlobalMethods.printDebugInfo("update 2fa failed");
            fail();
        } finally {

            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOff);
        }
    }


    public void limitWithModifyPhone(Object data[][], GlobalProperties.DEPOSITMETHOD method,String ruleId)  {

        String pwd = (String)data[0][2];
        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
        try{

            cpWithdrawLimit.refresh();

            if(driver.getCurrentUrl().contains("login")){
                CPLogin cpLogin = myfactor.newInstance(CPLogin.class,(String)data[0][3]);
                cpLogin.login((String)data[0][1],pwd);
            }

            initBase();

            menu.goToMenu(GlobalProperties.CPMenuName.PROFILES);

            menu.goToMenu(GlobalProperties.CPMenuName.SECURITYMANAGEMENT);


            String phone = "0000" + GlobalMethods.getRandomNumberString(6);
            assertTrue(cpWithdrawLimit.modifyPhone(pwd,phone), "modify phone failed, old email: " + phone);

           this.verifyLimit(data,cpWithdrawLimit,loginCode,method,"email/phone");

        }
        catch (Exception e) {
            GlobalMethods.printDebugInfo("update phone failed");
            fail();
        }
        finally {

            adminPaymentAPI.apiSwitchWithdrawLimits(ruleId,WithdrawLimitTestCases.limitSwitchOff);
        }

    }

    private JSONObject setWithdrawalMethod(GlobalProperties.DEPOSITMETHOD method, CPWithdrawLimit cpWithdrawLimit,String accountNumber){
        CPSkrillWithdraw instance = myfactor.newInstance(CPSkrillWithdraw.class);

        // Get valid account
        WithdrawBasePage.Account accSelected = instance.getValidAccount();
        assertNotNull(accSelected, "No available account found");

        String account = accSelected.getAccNumber();
        double balance = Double.parseDouble(accSelected.getBalance());

        // get balance and check if balance less than 100, then make cash adjustment
        //checkBalanceAndCashAdjustment(account, acctCurrency, balance);
        Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
        //withdrawal amount
        double amount = (int) (100 + Math.random() * 100);

        instance.setAccountAndAmountNew(account, amount);

        instance.clickContinue();
        cpWithdrawLimit.clickCreditOkBtn();
        instance.setWithdrawMethod(method);
        if(StringUtils.isNotBlank(accountNumber)){
            instance.setAccountNumber(accountNumber);
        }
        JSONObject js = new JSONObject();
        js.put("account",account);
        js.put("amount",amount);
        return js;
    }
    
    private void verifyLimit(Object data[][], CPWithdrawLimit cpWithdrawLimit,String type,GlobalProperties.DEPOSITMETHOD method,String keyword){

        String oldEmail = (String)data[0][1];
        String pwd = (String)data[0][2];

        Map<String, Integer> EmailMap = getLimit(data,type);

        assertTrue(EmailMap!=null && !EmailMap.isEmpty(),"invalid withdraw limit");

        cpWithdrawLimit.refresh();

        if(driver.getCurrentUrl().contains("login")){
            CPLogin cpLogin = myfactor.newInstance(CPLogin.class,(String)data[0][3]);
            cpLogin.login(oldEmail,pwd);
            initBase();
        }

        menu.goToMenu(GlobalProperties.CPMenuName.WITHDRAWFUNDS);

        this.setWithdrawalMethod(method,cpWithdrawLimit,null);

        String limitMsg = cpWithdrawLimit.getLimitMsg();
        LogUtils.info("withdraw limit msg is: " + limitMsg);
        cpWithdrawLimit.clickUnderstanBtn();
        Assert.assertTrue(limitMsg.contains(keyword),"wrong limit error message");
        Assert.assertTrue(limitMsg.contains(EmailMap.get("limitDayNumber").toString()),"wrong limit error message");
        Assert.assertTrue(limitMsg.contains(EmailMap.get("lastHours").toString()),"wrong limit error message");

    }

    private Map<String, Integer> getLimit(Object[][] data,String type){
        cpapiAccount = new CPAPIAccount((String) data[0][3], (String) data[0][1], (String) data[0][2]);
        JSONObject securityResp = cpapiAccount.getSecuritys();
        JSONArray dataArray = securityResp.getJSONArray("data");
        assertFalse(dataArray.isEmpty(),"invalid withdraw limit");
        Map<String,Map<String,Integer>> limitDataMap = new HashMap<>();
        dataArray.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            String fromFunctionCode = obj.getString("fromFunctionCode");
            Integer limitDay = obj.getInteger("limitDayNumber");
            Integer lastHours = obj.getInteger("lastHours");
            Map<String,Integer> map = new HashMap<>();
            map.put("limitDayNumber",limitDay);
            map.put("lastHours",lastHours);

            limitDataMap.put(fromFunctionCode,map);
        });

        return limitDataMap.get(type);

    }

    public void withdrawCredit(Object[][] data){
        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
        cpWithdrawLimit.refresh();

        if(driver.getCurrentUrl().contains("login")){
            CPLogin cpLogin = myfactor.newInstance(CPLogin.class,(String)data[0][3]);
            cpLogin.login((String)data[0][1],(String)data[0][2]);
        }

        initBase();
        menu.goToMenu(GlobalProperties.CPMenuName.WITHDRAWFUNDS);


        CPSkrillWithdraw instance = myfactor.newInstance(CPSkrillWithdraw.class);

        // Get valid account
        WithdrawBasePage.Account accSelected = instance.getValidAccount();
        assertNotNull(accSelected, "No available account found");

        String account = accSelected.getAccNumber();
        double balance = Double.parseDouble(accSelected.getBalance());

        // get balance and check if balance less than 100, then make cash adjustment
        //checkBalanceAndCashAdjustment(account, acctCurrency, balance);
        Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
        //withdrawal amount
        double amount = (int) (100 + Math.random() * 100);

        instance.setAccountAndAmountNew(account, amount);

        instance.clickContinue();

        Map<String,Object> mapCredit = cpWithdrawLimit.getCreditMsg();
        GlobalMethods.printDebugInfo("get credit message:" + new Gson().toJson(mapCredit));

        String credit = "";
        Map<String,Object> map = (Map<String,Object>)testData.get("result");
        List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("Accounts");
        for(Map<String,Object> tmp : list){
            int mt4Account = (int)tmp.get("mt4_account");
            if(String.valueOf(mt4Account).equals(account)){
                credit =tmp.get("credit").toString();
                GlobalMethods.printDebugInfo("get total credit: " + credit);
                break;
            }
        }
        assertTrue(mapCredit.get("account").toString().equalsIgnoreCase(account),"error account");
        double totalCredit = Double.parseDouble(credit);
        String tmp = mapCredit.get("amount").toString();
        String amount1 = getAmount(tmp);

        assertTrue(totalCredit >= Double.parseDouble(amount1),"credit is error");
    }

    protected String getAmount(String value) {

        value = value.replace(",", "");
        String regEx = "(([1-9][0-9]*)+(.[0-9]{1,4})?$)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(value);
        String amount = "0";
        if (matcher.find()) {
            amount = matcher.group(1);
        }
        return amount;
    }
}
